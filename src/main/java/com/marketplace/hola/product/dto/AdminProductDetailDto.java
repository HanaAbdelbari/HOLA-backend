package com.marketplace.hola.product.dto;

import com.marketplace.hola.product.Product;
import com.marketplace.hola.product.ProductImage;

import java.math.BigDecimal;
import java.util.List;

// Full product data for the admin edit form (includes categoryId, displayOrder,
// and all image URLs — everything the form needs to pre-fill).
public record AdminProductDetailDto(
        Long id,
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
        Boolean isActive,
        List<String> images,
        List<ProductVariantDto> variants
) {
    public static AdminProductDetailDto from(Product p) {
        List<String> imageUrls = p.getImages().stream()
                .map(ProductImage::getImageUrl)
                .toList();

        // تحويل الـ Variants الحقيقية الموجودة في الـ Product إلى DTOs
        List<ProductVariantDto> variantDtos = p.getVariants() != null
                ? p.getVariants().stream().map(ProductVariantDto::from).toList()
                : List.of();

        return new AdminProductDetailDto(
                p.getId(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getName(),
                p.getSlug(),
                p.getDescription(),
                p.getPrice(),
                p.getSalePrice(),
                p.getMaterial(),
                p.getSize(),
                p.getDimensions(),
                p.getStockQuantity(),
                p.getDisplayOrder(),
                p.getIsActive(),
                imageUrls,
                variantDtos // تمرير الـ variants هنا لتظهر في الفورم
        );
    }
}