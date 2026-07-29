package com.company.stockflow.mapper;

import com.company.stockflow.dto.request.ProductRequest;
import com.company.stockflow.dto.response.ProductResponse;
import com.company.stockflow.entity.Category;
import com.company.stockflow.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request, Category category) {
        return Product.builder()
                .name(trim(request.getName()))
                .description(trim(request.getDescription()))
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .sku(trim(request.getSku()))
                .category(category)
                .build();
    }

    public void updateEntity(Product product, ProductRequest request, Category category) {
        product.setName(trim(request.getName()));
        product.setDescription(trim(request.getDescription()));
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setSku(trim(request.getSku()));
        product.setCategory(category);
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .sku(product.getSku())
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}