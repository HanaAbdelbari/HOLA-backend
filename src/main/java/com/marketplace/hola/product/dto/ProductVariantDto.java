package com.marketplace.hola.product.dto;

import com.marketplace.hola.product.ProductVariant;
import java.math.BigDecimal;

public record ProductVariantDto(
        Long id,
        String label,
        BigDecimal price,
        Integer stockQuantity
) {
    public static ProductVariantDto from(ProductVariant variant) {
        return new ProductVariantDto(
                variant.getId(),
                variant.getLabel(),
                variant.getPrice(),
                variant.getStockQuantity()
        );
    }
}