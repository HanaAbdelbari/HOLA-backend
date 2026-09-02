package com.marketplace.hola.product;

import com.marketplace.hola.category.Category;
import com.marketplace.hola.category.CategoryRepository;
import com.marketplace.hola.product.dto.ProductDetailDto;
import com.marketplace.hola.product.dto.ProductRequest;
import com.marketplace.hola.product.dto.ProductSummaryDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private static final int NEW_ARRIVALS_LIMIT = 8;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // Shop: all active products.
    public List<ProductSummaryDto> getAllProducts() {
        return productRepository
                .findByIsActiveTrueOrderByDisplayOrderAscCreatedAtDesc()
                .stream()
                .map(ProductSummaryDto::from)
                .toList();
    }

    // Category page: active products in one category.
    public List<ProductSummaryDto> getProductsByCategory(String categorySlug) {
        return productRepository
                .findByCategorySlugAndIsActiveTrueOrderByDisplayOrderAsc(categorySlug)
                .stream()
                .map(ProductSummaryDto::from)
                .toList();
    }

    // Home — New Arrivals: newest few.
    public List<ProductSummaryDto> getNewArrivals() {
        return productRepository
                .findByIsActiveTrueOrderByCreatedAtDesc()
                .stream()
                .limit(NEW_ARRIVALS_LIMIT)
                .map(ProductSummaryDto::from)
                .toList();
    }

    // Home Offers section + Sale page: products actually on sale.
    public List<ProductSummaryDto> getOnSaleProducts() {
        return productRepository.findOnSale()
                .stream()
                .map(ProductSummaryDto::from)
                .toList();
    }

    // Product details page. Throws if not found or inactive.
    public ProductDetailDto getProductBySlug(String slug) {
        Product product = productRepository
                .findBySlugAndIsActiveTrue(slug)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with slug: " + slug));
        return ProductDetailDto.from(product);
    }

    // ==========================================
    // ADMIN OPERATIONS (WRITE)
    // ==========================================

    @Transactional
    public ProductDetailDto createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.categoryId()));

        Product product = new Product();
        mapRequestToProduct(request, product, category);

        Product savedProduct = productRepository.save(product);
        return ProductDetailDto.from(savedProduct);
    }

    @Transactional
    public ProductDetailDto updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + request.categoryId()));

        mapRequestToProduct(request, product, category);

        Product savedProduct = productRepository.save(product);
        return ProductDetailDto.from(savedProduct);
    }

    private void mapRequestToProduct(ProductRequest request, Product product, Category category) {
        product.setCategory(category);
        product.setName(request.name());
        product.setSlug(request.slug());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setSalePrice(request.salePrice());
        product.setMaterial(request.material());
        product.setSize(request.size());
        product.setChainLength(request.chainLength());
        product.setStockQuantity(request.stockQuantity() != null ? request.stockQuantity() : 0);
        product.setDisplayOrder(request.displayOrder() != null ? request.displayOrder() : 0);

        // 1. Map Variants safely using entity helper methods
        if (request.variants() != null) {
            List<ProductVariant> variants = request.variants().stream()
                    .map(v -> new ProductVariant(v.label(), v.price(), v.stockQuantity()))
                    .toList();
            product.setVariants(variants);
        } else {
            product.setVariants(List.of());
        }

        // 2. Map Images safely
        if (request.imageUrls() != null) {
            product.getImages().clear();
            for (String url : request.imageUrls()) {
                ProductImage image = new ProductImage();
                image.setImageUrl(url); // إذا كان اسم الـ field مختلف بـ ProductImage (مثلاً setUrl)، عدليه هنا فقط
                image.setProduct(product);
                product.getImages().add(image);
            }
        }
    }
}