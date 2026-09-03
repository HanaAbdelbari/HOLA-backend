package com.marketplace.hola.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductRequest(
        Long categoryId,
        String name,
        String slug,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        String material,
        String size,
        String dimensions,
        Integer stockQuantity,
        Integer displayOrder,
        List<String> imageUrls,
        List<ProductVariantDto> variants
) {
}