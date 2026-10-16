package com.nexusengine.core.portal.catalog.mapper;

import com.nexusengine.core.domain.catalog.entity.Product;
import com.nexusengine.core.portal.catalog.dto.ProductSummaryResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PortalProductMapper {
    ProductSummaryResponse toSummary(Product product);

    List<ProductSummaryResponse> toSummaryList(List<Product> products);
}
