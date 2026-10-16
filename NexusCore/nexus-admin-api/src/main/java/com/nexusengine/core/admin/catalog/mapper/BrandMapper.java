package com.nexusengine.core.admin.catalog.mapper;

import com.nexusengine.core.admin.catalog.dto.BrandResponse;
import com.nexusengine.core.domain.catalog.entity.Brand;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    BrandResponse toResponse(Brand brand);

    List<BrandResponse> toResponseList(List<Brand> brands);
}
