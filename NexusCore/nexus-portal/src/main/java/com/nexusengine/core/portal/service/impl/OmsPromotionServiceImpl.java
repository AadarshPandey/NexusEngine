package com.nexusengine.core.portal.service.impl;

import com.nexusengine.core.model.OmsCartItem;
import com.nexusengine.core.model.PmsProductFullReduction;
import com.nexusengine.core.model.PmsProductLadder;
import com.nexusengine.core.model.PmsSkuStock;
import com.nexusengine.core.portal.dao.PortalProductDao;
import com.nexusengine.core.portal.domain.CartPromotionItem;
import com.nexusengine.core.portal.domain.PromotionProduct;
import com.nexusengine.core.portal.service.OmsPromotionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Promotion calculation Service implementation
 */
@Service
@lombok.RequiredArgsConstructor
public class OmsPromotionServiceImpl implements OmsPromotionService {
    private final PortalProductDao portalProductDao;

    @Override
    public List<CartPromotionItem> calcCartPromotion(List<OmsCartItem> cartItemList) {
        Map<Long, List<OmsCartItem>> productCartMap = groupCartItemBySpu(cartItemList);
        List<PromotionProduct> promotionProductList = getPromotionProductList(cartItemList);
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        for (Map.Entry<Long, List<OmsCartItem>> entry : productCartMap.entrySet()) {
            Long productId = entry.getKey();
            PromotionProduct promotionProduct = getPromotionProductById(productId, promotionProductList);
            List<OmsCartItem> itemList = entry.getValue();
            if (promotionProduct == null) {
                handleNoReduce(cartPromotionItemList, itemList, null);
                continue;
            }
            Integer promotionType = promotionProduct.pmsProduct().getPromotionType();
            if (promotionType == null) {
                handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                continue;
            }
            if (promotionType == 1) {
                for (OmsCartItem item : itemList) {
                    String promotionMessage = "Single item promotion";
                    BigDecimal reduceAmount = new BigDecimal(0);
                    Integer realStock = 0;
                    PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                    if (skuStock != null) {
                        BigDecimal originalPrice = skuStock.getPrice();
                        item.setPrice(originalPrice);
                        reduceAmount = originalPrice.subtract(
                                skuStock.getPromotionPrice() != null ? skuStock.getPromotionPrice() : originalPrice);
                        realStock = skuStock.getStock() - skuStock.getLockStock();
                    } else {
                        realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getStock() != null ? s.getStock() : 0) - (s.getLockStock() != null ? s.getLockStock() : 0)).sum() : 0;
                    }
                    cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
                }
            } else if (promotionType == 3) {
                int count = getCartItemCount(itemList);
                PmsProductLadder ladder = getProductLadder(count, promotionProduct.productLadderList());
                if (ladder != null) {
                    for (OmsCartItem item : itemList) {
                        String promotionMessage = "Ladder discount: buy " + ladder.getCount() + " get " + ladder.getDiscount().multiply(new BigDecimal(10)) + "% off";
                        BigDecimal reduceAmount = new BigDecimal(0);
                        Integer realStock = 0;
                        PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        if (skuStock != null) {
                            BigDecimal originalPrice = skuStock.getPrice();
                            reduceAmount = originalPrice.subtract(ladder.getDiscount().multiply(originalPrice));
                            realStock = skuStock.getStock() - skuStock.getLockStock();
                        } else {
                            realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getStock() != null ? s.getStock() : 0) - (s.getLockStock() != null ? s.getLockStock() : 0)).sum() : 0;
                        }
                        cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
                    }
                } else {
                    handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                }
            } else if (promotionType == 4) {
                BigDecimal totalAmount = getCartItemAmount(itemList, promotionProductList);
                PmsProductFullReduction fullReduction = getProductFullReduction(totalAmount, promotionProduct.productFullReductionList());
                if (fullReduction != null) {
                    for (OmsCartItem item : itemList) {
                        String promotionMessage = "Full reduction: spend " + fullReduction.getFullPrice() + " save " + fullReduction.getReducePrice();
                        BigDecimal reduceAmount = new BigDecimal(0);
                        Integer realStock = 0;
                        PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        if (skuStock != null) {
                            BigDecimal originalPrice = skuStock.getPrice();
                            reduceAmount = originalPrice.divide(totalAmount, RoundingMode.HALF_EVEN).multiply(fullReduction.getReducePrice());
                            realStock = skuStock.getStock() - skuStock.getLockStock();
                        } else {
                            realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getStock() != null ? s.getStock() : 0) - (s.getLockStock() != null ? s.getLockStock() : 0)).sum() : 0;
                        }
                        cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
                    }
                } else {
                    handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                }
            } else {
                handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
            }
        }
        return cartPromotionItemList;
    }

    private List<PromotionProduct> getPromotionProductList(List<OmsCartItem> cartItemList) {
        List<Long> productIdList = new ArrayList<>();
        for (OmsCartItem cartItem : cartItemList) {
            productIdList.add(cartItem.getProductId());
        }
        return portalProductDao.getPromotionProductList(productIdList);
    }

    private Map<Long, List<OmsCartItem>> groupCartItemBySpu(List<OmsCartItem> cartItemList) {
        Map<Long, List<OmsCartItem>> productCartMap = new TreeMap<>();
        for (OmsCartItem cartItem : cartItemList) {
            productCartMap.computeIfAbsent(cartItem.getProductId(), k -> new ArrayList<>()).add(cartItem);
        }
        return productCartMap;
    }

    private void handleNoReduce(List<CartPromotionItem> cartPromotionItemList, List<OmsCartItem> itemList, PromotionProduct promotionProduct) {
        for (OmsCartItem item : itemList) {
            String promotionMessage = "No promotion";
            BigDecimal reduceAmount = new BigDecimal(0);
            Integer realStock = 0;
            if (promotionProduct != null) {
                PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                if (skuStock != null) {
                    realStock = skuStock.getStock() - skuStock.getLockStock();
                } else {
                    realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getStock() != null ? s.getStock() : 0) - (s.getLockStock() != null ? s.getLockStock() : 0)).sum() : 0;
                }
            }
            cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
        }
    }

    private PmsProductFullReduction getProductFullReduction(BigDecimal totalAmount, List<PmsProductFullReduction> fullReductionList) {
        if (fullReductionList == null || fullReductionList.isEmpty()) return null;
        fullReductionList.sort((o1, o2) -> o2.getFullPrice().subtract(o1.getFullPrice()).intValue());
        for (PmsProductFullReduction fullReduction : fullReductionList) {
            if (totalAmount.subtract(fullReduction.getFullPrice()).intValue() >= 0) {
                return fullReduction;
            }
        }
        return null;
    }

    private PmsProductLadder getProductLadder(int count, List<PmsProductLadder> productLadderList) {
        if (productLadderList == null || productLadderList.isEmpty()) return null;
        productLadderList.sort((o1, o2) -> o2.getCount() - o1.getCount());
        for (PmsProductLadder productLadder : productLadderList) {
            if (count >= productLadder.getCount()) {
                return productLadder;
            }
        }
        return null;
    }

    private int getCartItemCount(List<OmsCartItem> itemList) {
        int count = 0;
        for (OmsCartItem item : itemList) {
            count += item.getQuantity();
        }
        return count;
    }

    private BigDecimal getCartItemAmount(List<OmsCartItem> itemList, List<PromotionProduct> promotionProductList) {
        BigDecimal amount = new BigDecimal(0);
        for (OmsCartItem item : itemList) {
            PromotionProduct promotionProduct = getPromotionProductById(item.getProductId(), promotionProductList);
            if (promotionProduct != null) {
                PmsSkuStock skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                if (skuStock != null) {
                    amount = amount.add(skuStock.getPrice().multiply(new BigDecimal(item.getQuantity())));
                }
            }
        }
        return amount;
    }

    private PmsSkuStock getOriginalPrice(PromotionProduct promotionProduct, Long productSkuId) {
        if (promotionProduct.skuStockList() == null) return null;
        for (PmsSkuStock skuStock : promotionProduct.skuStockList()) {
            if (productSkuId != null && productSkuId.equals(skuStock.getId())) {
                return skuStock;
            }
        }
        return null;
    }

    private PromotionProduct getPromotionProductById(Long productId, List<PromotionProduct> promotionProductList) {
        for (PromotionProduct promotionProduct : promotionProductList) {
            if (productId.equals(promotionProduct.pmsProduct().getId())) {
                return promotionProduct;
            }
        }
        return null;
    }
}
