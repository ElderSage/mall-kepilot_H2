package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.entity.Product;
import com.mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品控制器
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "商品管理", description = "商品浏览、搜索等相关接口")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "获取商品列表", description = "分页获取所有在售商品")
    public ApiResponse<Page<Product>> getProducts(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findAllOnSale(pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "根据商品ID获取商品详细信息")
    public ApiResponse<Product> getProduct(@PathVariable Long id) {
        return productService.findById(id)
                .map(product -> ApiResponse.success(product))
                .orElse(ApiResponse.error("商品不存在"));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取商品", description = "根据分类ID分页获取商品")
    public ApiResponse<Page<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findByCategory(categoryId, pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索商品", description = "多条件搜索商品")
    public ApiResponse<Page<Product>> searchProducts(
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "分类ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "最低价格") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "最高价格") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "排序方式：price_asc, price_desc, sales") @RequestParam(defaultValue = "default") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.searchProducts(keyword, categoryId, minPrice, maxPrice, sortBy, pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门商品", description = "获取热门商品列表")
    public ApiResponse<Page<Product>> getHotProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findHotProducts(pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/new")
    @Operation(summary = "获取新品", description = "获取新品列表")
    public ApiResponse<Page<Product>> getNewProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findNewProducts(pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/recommend")
    @Operation(summary = "获取推荐商品", description = "获取推荐商品列表")
    public ApiResponse<Page<Product>> getRecommendProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findRecommendProducts(pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/best-selling")
    @Operation(summary = "获取热销商品", description = "根据销量排序获取商品")
    public ApiResponse<Page<Product>> getBestSellingProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.findBySales(pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/{id}/related")
    @Operation(summary = "获取相关商品", description = "获取与指定商品相关的商品")
    public ApiResponse<List<Product>> getRelatedProducts(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") int limit) {
        
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            return ApiResponse.error("商品不存在");
        }
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> relatedProducts = productService.findRelatedProducts(
                product.getCategory().getId(), id, pageable);
        return ApiResponse.success(relatedProducts);
    }
}