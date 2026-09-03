package com.marketplace.hola.product.dto;

import com.marketplace.hola.product.Product;
import com.marketplace.hola.product.ProductImage;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailDto(
        Long id,
        String name,
        String slug,
        String description,
        BigDecimal price,
        BigDecimal salePrice,
        boolean onSale,
        Integer discountPercent,
        String material,
        String size,
        String dimensions,
        Integer stockQuantity,
        boolean inStock,
        String categoryName,
        String categorySlug,
        List<String> images,
        List<ProductVariantDto> variants // 1. إضافة قائمة الـ Variants
) {
    public static ProductDetailDto from(Product p) {
        List<String> imageUrls = p.getImages().stream()
                .map(ProductImage::getImageUrl)
                .toList();

        List<ProductVariantDto> variantDtos = p.getVariants().stream()
                .map(v -> new ProductVariantDto(v.getId(), v.getLabel(), v.getPrice(), v.getStockQuantity()))
                .toList();

        return new ProductDetailDto(
                p.getId(),
                p.getName(),
                p.getSlug(),
                p.getDescription(),
                p.getPrice(),
                p.getSalePrice(),
                p.isOnSale(),
                discountPercent(p),
                p.getMaterial(),
                p.getSize(),
                p.getDimensions(),
                p.getStockQuantity(),
                p.getStockQuantity() > 0,
                p.getCategory().getName(),
                p.getCategory().getSlug(),
                imageUrls,
                variantDtos // 2. تمرير الـ Variants
        );
    }

    private static Integer discountPercent(Product p) {
        if (!p.isOnSale()) {
            return null;
        }
        BigDecimal off = p.getPrice().subtract(p.getSalePrice());
        return off.multiply(BigDecimal.valueOf(100))
                .divide(p.getPrice(), 0, java.math.RoundingMode.HALF_UP)
                .intValue();
    }
}