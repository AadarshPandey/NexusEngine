package com.nexusengine.core.portal.ordering.service;

import lombok.RequiredArgsConstructor;
import com.nexusengine.core.domain.ordering.entity.CartItem;
import com.nexusengine.core.domain.catalog.entity.ProductSku;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import com.nexusengine.core.portal.ordering.dto.CartPromotionItem;
import com.nexusengine.core.portal.catalog.dao.ProductDao;
import com.nexusengine.core.portal.catalog.dto.PromotionProduct;

/**
 * Promotion calculation Service implementation - standard list vs sale price promotions
 */
@Service
@RequiredArgsConstructor
public class PromotionCalculationServiceImpl implements PromotionCalculationService {
    private final ProductDao productDao;

    @Override
    public List<CartPromotionItem> calcCartPromotion(List<CartItem> cartItemList) {
        if (cartItemList == null || cartItemList.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIds = cartItemList.stream().map(CartItem::getProductId).filter(Objects::nonNull).distinct().toList();
        List<PromotionProduct> promotionProducts = productDao.getPromotionProductList(productIds);
        Map<Long, PromotionProduct> productMap = new HashMap<>();
        for (PromotionProduct pp : promotionProducts) {
            if (pp.product() != null && pp.product().getId() != null) {
                productMap.put(pp.product().getId(), pp);
            }
        }

        List<CartPromotionItem> result = new ArrayList<>();
        for (CartItem item : cartItemList) {
            PromotionProduct pp = productMap.get(item.getProductId());
            ProductSku sku = findSku(pp, item.getProductSkuId());

            BigDecimal reduceAmount = BigDecimal.ZERO;
            String message = "Regular price";
            int realStock = 0;

            if (sku != null) {
                BigDecimal listPrice = sku.getListPrice() != null ? sku.getListPrice() : BigDecimal.ZERO;
                BigDecimal salePrice = sku.getSalePrice() != null ? sku.getSalePrice() : listPrice;
                item.setPrice(listPrice);

                if (salePrice.compareTo(listPrice) < 0) {
                    reduceAmount = listPrice.subtract(salePrice);
                    message = "Special discount";
                }

                int onHand = sku.getOnHandQty() != null ? sku.getOnHandQty() : 0;
                int reserved = sku.getReservedQty() != null ? sku.getReservedQty() : 0;
                realStock = Math.max(0, onHand - reserved);
            }

            result.add(new CartPromotionItem(item, message, reduceAmount, realStock, null));
        }

        return result;
    }

    private ProductSku findSku(PromotionProduct promotionProduct, Long skuId) {
        if (promotionProduct == null || promotionProduct.skuStockList() == null || skuId == null) {
            return null;
        }
        for (ProductSku sku : promotionProduct.skuStockList()) {
            if (skuId.equals(sku.getId())) {
                return sku;
            }
        }
        return null;
    }
}
