package com.marketplace.hola.product.dto;

import com.marketplace.hola.product.Product;
import com.marketplace.hola.product.ProductVariant;

import java.math.BigDecimal;

// Product row for the admin list (includes inactive/hidden products).
public record AdminProductDto(
        Long id,
        String name,
        String slug,
        BigDecimal price,
        BigDecimal salePrice,
        Integer stockQuantity,
        Boolean isActive,
        String categoryName,
        String mainImageUrl,
        int variantCount
) {
    public static AdminProductDto from(Product p) {
        String mainImage = (p.getImages() != null && !p.getImages().isEmpty())
                ? p.getImages().get(0).getImageUrl()
                : null;

        // إذا كان المنتج يحتوي على Variants، نقوم بتجميع إجمالي المخزون منها
        int calculatedStock = (p.getVariants() != null && !p.getVariants().isEmpty())
                ? p.getVariants().stream().mapToInt(v -> v.getStockQuantity() != null ? v.getStockQuantity() : 0).sum()
                : (p.getStockQuantity() != null ? p.getStockQuantity() : 0);

        int count = p.getVariants() != null ? p.getVariants().size() : 0;

        return new AdminProductDto(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getPrice(),
                p.getSalePrice(),
                calculatedStock,
                p.getIsActive(),
                p.getCategory() != null ? p.getCategory().getName() : null,
                mainImage,
                count
        );
    }
}