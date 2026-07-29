package com.company.stockflow.mapper;

import com.company.stockflow.dto.request.CategoryRequest;
import com.company.stockflow.dto.response.CategoryResponse;
import com.company.stockflow.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(request.getName() == null ? null : request.getName().trim())
                .description(request.getDescription() == null ? null : request.getDescription().trim())
                .build();
    }

    public void updateEntity(Category category, CategoryRequest request) {
        category.setName(request.getName() == null ? null : request.getName().trim());
        category.setDescription(request.getDescription() == null ? null : request.getDescription().trim());
    }

    public CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}