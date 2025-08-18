package com.mall.service;

import com.mall.entity.Product;
import com.mall.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 商品服务类
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 根据ID查找商品
     */
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    /**
     * 获取所有在售商品
     */
    public Page<Product> findAllOnSale(Pageable pageable) {
        return productRepository.findByStatusOrderBySortOrderDesc(Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 根据分类查找商品
     */
    public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndStatusOrderBySortOrderDesc(
                categoryId, Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 搜索商品
     */
    public Page<Product> searchProducts(String keyword, Long categoryId, BigDecimal minPrice, 
                                      BigDecimal maxPrice, String sortBy, Pageable pageable) {
        return productRepository.searchProducts(keyword, categoryId, minPrice, maxPrice, sortBy, pageable);
    }

    /**
     * 根据关键词搜索商品
     */
    public Page<Product> searchByKeyword(String keyword, Pageable pageable) {
        return productRepository.searchByKeyword(keyword, Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 获取热门商品
     */
    public Page<Product> findHotProducts(Pageable pageable) {
        return productRepository.findByIsHotTrueAndStatusOrderBySortOrderDesc(
                Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 获取新品
     */
    public Page<Product> findNewProducts(Pageable pageable) {
        return productRepository.findByIsNewTrueAndStatusOrderBySortOrderDesc(
                Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 获取推荐商品
     */
    public Page<Product> findRecommendProducts(Pageable pageable) {
        return productRepository.findByIsRecommendTrueAndStatusOrderBySortOrderDesc(
                Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 根据销量排序获取商品
     */
    public Page<Product> findBySales(Pageable pageable) {
        return productRepository.findByStatusOrderBySalesDesc(Product.ProductStatus.ON_SALE, pageable);
    }

    /**
     * 获取相关商品
     */
    public List<Product> findRelatedProducts(Long categoryId, Long productId, Pageable pageable) {
        return productRepository.findRelatedProducts(categoryId, productId, pageable);
    }

    /**
     * 保存商品
     */
    public Product save(Product product) {
        return productRepository.save(product);
    }

    /**
     * 删除商品
     */
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    /**
     * 更新商品库存
     */
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

    /**
     * 增加商品销量
     */
    public void increaseSales(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        
        product.setSales(product.getSales() + quantity);
        productRepository.save(product);
    }

    /**
     * 批量上架商品
     */
    public void batchOnSale(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        products.forEach(product -> product.setStatus(Product.ProductStatus.ON_SALE));
        productRepository.saveAll(products);
    }

    /**
     * 批量下架商品
     */
    public void batchOffSale(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        products.forEach(product -> product.setStatus(Product.ProductStatus.OFF_SALE));
        productRepository.saveAll(products);
    }

    /**
     * 获取库存不足的商品
     */
    public List<Product> findLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    /**
     * 统计在售商品数量
     */
    public long countOnSaleProducts() {
        return productRepository.countByStatus(Product.ProductStatus.ON_SALE);
    }
}