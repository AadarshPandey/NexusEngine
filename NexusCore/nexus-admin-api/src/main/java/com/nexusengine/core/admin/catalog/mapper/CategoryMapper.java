package com.nexusengine.core.admin.catalog.mapper;

import com.nexusengine.core.admin.catalog.dto.CategoryResponse;
import com.nexusengine.core.domain.catalog.entity.Category;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);
}
