package com.nexusengine.core.admin.catalog.mapper;

import com.nexusengine.core.admin.catalog.dto.ProductSummaryResponse;
import com.nexusengine.core.domain.catalog.entity.Product;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductSummaryResponse toSummary(Product product);

    List<ProductSummaryResponse> toSummaryList(List<Product> products);
}
