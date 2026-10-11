package com.nexusengine.core.portal.ordering.service;

import lombok.RequiredArgsConstructor;

import com.nexusengine.core.domain.ordering.entity.CartItem;

import com.nexusengine.core.domain.promotion.entity.SpendThresholdDiscount;
import com.nexusengine.core.domain.promotion.entity.QuantityTierDiscount;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.catalog.dao.ProductDao;
import com.nexusengine.core.portal.catalog.dto.PromotionProduct;


/**
 * Promotion calculation Service implementation
 */
@Service
@RequiredArgsConstructor
public class PromotionCalculationServiceImpl implements PromotionCalculationService {
    private final ProductDao productDao;

    @Override
    public List<CartPromotionItem> calcCartPromotion(List<CartItem> cartItemList) {
        Map<Long, List<CartItem>> productCartMap = groupCartItemBySpu(cartItemList);
        List<PromotionProduct> promotionProductList = getPromotionProductList(cartItemList);
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        for (Map.Entry<Long, List<CartItem>> entry : productCartMap.entrySet()) {
            Long productId = entry.getKey();
            PromotionProduct promotionProduct = getPromotionProductById(productId, promotionProductList);
            List<CartItem> itemList = entry.getValue();
            if (promotionProduct == null) {
                handleNoReduce(cartPromotionItemList, itemList, null);
                continue;
            }
            Integer promotionType = promotionProduct.product().getPromotionType();
            if (promotionType == null) {
                handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                continue;
            }
            if (promotionType == 1) {
                for (CartItem item : itemList) {
                    String promotionMessage = "Single item promotion";
                    BigDecimal reduceAmount = new BigDecimal(0);
                    Integer realStock = 0;
                    ProductSku skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                    if (skuStock != null) {
                        BigDecimal originalPrice = skuStock.getListPrice();
                        item.setPrice(originalPrice);
                        reduceAmount = originalPrice.subtract(
                                skuStock.getSalePrice() != null ? skuStock.getSalePrice() : originalPrice);
                        realStock = skuStock.getOnHandQty() - skuStock.getReservedQty();
                    } else {
                        realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getOnHandQty() != null ? s.getOnHandQty() : 0) - (s.getReservedQty() != null ? s.getReservedQty() : 0)).sum() : 0;
                    }
                    cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
                }
            } else if (promotionType == 3) {
                int count = getCartItemCount(itemList);
                QuantityTierDiscount ladder = getProductLadder(count, promotionProduct.productVolumeDiscountList());
                if (ladder != null) {
                    for (CartItem item : itemList) {
                        String promotionMessage = "Ladder discount: buy " + ladder.getCount() + " get " + ladder.getDiscount().multiply(new BigDecimal(10)) + "% off";
                        BigDecimal reduceAmount = new BigDecimal(0);
                        Integer realStock = 0;
                        ProductSku skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        if (skuStock != null) {
                            BigDecimal originalPrice = skuStock.getListPrice();
                            reduceAmount = originalPrice.subtract(ladder.getDiscount().multiply(originalPrice));
                            realStock = skuStock.getOnHandQty() - skuStock.getReservedQty();
                        } else {
                            realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getOnHandQty() != null ? s.getOnHandQty() : 0) - (s.getReservedQty() != null ? s.getReservedQty() : 0)).sum() : 0;
                        }
                        cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
                    }
                } else {
                    handleNoReduce(cartPromotionItemList, itemList, promotionProduct);
                }
            } else if (promotionType == 4) {
                BigDecimal totalAmount = getCartItemAmount(itemList, promotionProductList);
                SpendThresholdDiscount fullReduction = getProductFullReduction(totalAmount, promotionProduct.productSpendDiscountList());
                if (fullReduction != null) {
                    for (CartItem item : itemList) {
                        String promotionMessage = "Full reduction: spend " + fullReduction.getFullPrice() + " save " + fullReduction.getReducePrice();
                        BigDecimal reduceAmount = new BigDecimal(0);
                        Integer realStock = 0;
                        ProductSku skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                        if (skuStock != null) {
                            BigDecimal originalPrice = skuStock.getListPrice();
                            reduceAmount = originalPrice.divide(totalAmount, RoundingMode.HALF_EVEN).multiply(fullReduction.getReducePrice());
                            realStock = skuStock.getOnHandQty() - skuStock.getReservedQty();
                        } else {
                            realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getOnHandQty() != null ? s.getOnHandQty() : 0) - (s.getReservedQty() != null ? s.getReservedQty() : 0)).sum() : 0;
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

    private List<PromotionProduct> getPromotionProductList(List<CartItem> cartItemList) {
        List<Long> productIdList = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            productIdList.add(cartItem.getProductId());
        }
        return productDao.getPromotionProductList(productIdList);
    }

    private Map<Long, List<CartItem>> groupCartItemBySpu(List<CartItem> cartItemList) {
        Map<Long, List<CartItem>> productCartMap = new TreeMap<>();
        for (CartItem cartItem : cartItemList) {
            productCartMap.computeIfAbsent(cartItem.getProductId(), k -> new ArrayList<>()).add(cartItem);
        }
        return productCartMap;
    }

    private void handleNoReduce(List<CartPromotionItem> cartPromotionItemList, List<CartItem> itemList, PromotionProduct promotionProduct) {
        for (CartItem item : itemList) {
            String promotionMessage = "No promotion";
            BigDecimal reduceAmount = new BigDecimal(0);
            Integer realStock = 0;
            if (promotionProduct != null) {
                ProductSku skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                if (skuStock != null) {
                    realStock = skuStock.getOnHandQty() - skuStock.getReservedQty();
                } else {
                    realStock = promotionProduct.skuStockList() != null ? promotionProduct.skuStockList().stream().mapToInt(s -> (s.getOnHandQty() != null ? s.getOnHandQty() : 0) - (s.getReservedQty() != null ? s.getReservedQty() : 0)).sum() : 0;
                }
            }
            cartPromotionItemList.add(new CartPromotionItem(item, promotionMessage, reduceAmount, realStock, null));
        }
    }

    private SpendThresholdDiscount getProductFullReduction(BigDecimal totalAmount, List<SpendThresholdDiscount> fullReductionList) {
        if (fullReductionList == null || fullReductionList.isEmpty()) return null;
        fullReductionList.sort((o1, o2) -> o2.getFullPrice().subtract(o1.getFullPrice()).intValue());
        for (SpendThresholdDiscount fullReduction : fullReductionList) {
            if (totalAmount.subtract(fullReduction.getFullPrice()).intValue() >= 0) {
                return fullReduction;
            }
        }
        return null;
    }

    private QuantityTierDiscount getProductLadder(int count, List<QuantityTierDiscount> productVolumeDiscountList) {
        if (productVolumeDiscountList == null || productVolumeDiscountList.isEmpty()) return null;
        productVolumeDiscountList.sort((o1, o2) -> o2.getCount() - o1.getCount());
        for (QuantityTierDiscount productVolumeDiscount : productVolumeDiscountList) {
            if (count >= productVolumeDiscount.getCount()) {
                return productVolumeDiscount;
            }
        }
        return null;
    }

    private int getCartItemCount(List<CartItem> itemList) {
        int count = 0;
        for (CartItem item : itemList) {
            count += item.getQuantity();
        }
        return count;
    }

    private BigDecimal getCartItemAmount(List<CartItem> itemList, List<PromotionProduct> promotionProductList) {
        BigDecimal amount = new BigDecimal(0);
        for (CartItem item : itemList) {
            PromotionProduct promotionProduct = getPromotionProductById(item.getProductId(), promotionProductList);
            if (promotionProduct != null) {
                ProductSku skuStock = getOriginalPrice(promotionProduct, item.getProductSkuId());
                if (skuStock != null) {
                    amount = amount.add(skuStock.getListPrice().multiply(new BigDecimal(item.getQuantity())));
                }
            }
        }
        return amount;
    }

    private ProductSku getOriginalPrice(PromotionProduct promotionProduct, Long productSkuId) {
        if (promotionProduct.skuStockList() == null) return null;
        for (ProductSku skuStock : promotionProduct.skuStockList()) {
            if (productSkuId != null && productSkuId.equals(skuStock.getId())) {
                return skuStock;
            }
        }
        return null;
    }

    private PromotionProduct getPromotionProductById(Long productId, List<PromotionProduct> promotionProductList) {
        for (PromotionProduct promotionProduct : promotionProductList) {
            if (productId.equals(promotionProduct.product().getId())) {
                return promotionProduct;
            }
        }
        return null;
    }
}
