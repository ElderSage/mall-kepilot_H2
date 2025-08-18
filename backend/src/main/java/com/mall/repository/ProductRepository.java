package com.mall.repository;

import com.mall.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品数据访问层
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 根据状态查找商品
     */
    Page<Product> findByStatusOrderBySortOrderDesc(Product.ProductStatus status, Pageable pageable);

    /**
     * 根据分类ID查找商品
     */
    Page<Product> findByCategoryIdAndStatusOrderBySortOrderDesc(Long categoryId, Product.ProductStatus status, Pageable pageable);

    /**
     * 根据商品名称模糊查询
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.status = :status ORDER BY p.sortOrder DESC")
    Page<Product> findByNameContainingAndStatus(@Param("keyword") String keyword, @Param("status") Product.ProductStatus status, Pageable pageable);

    /**
     * 根据关键词搜索商品
     */
    @Query("SELECT p FROM Product p WHERE (p.name LIKE %:keyword% OR p.keywords LIKE %:keyword% OR p.description LIKE %:keyword%) AND p.status = :status ORDER BY p.sortOrder DESC")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, @Param("status") Product.ProductStatus status, Pageable pageable);

    /**
     * 根据价格区间查找商品
     */
    Page<Product> findByPriceBetweenAndStatusOrderBySortOrderDesc(BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status, Pageable pageable);

    /**
     * 查找热门商品
     */
    Page<Product> findByIsHotTrueAndStatusOrderBySortOrderDesc(Product.ProductStatus status, Pageable pageable);

    /**
     * 查找新品
     */
    Page<Product> findByIsNewTrueAndStatusOrderBySortOrderDesc(Product.ProductStatus status, Pageable pageable);

    /**
     * 查找推荐商品
     */
    Page<Product> findByIsRecommendTrueAndStatusOrderBySortOrderDesc(Product.ProductStatus status, Pageable pageable);

    /**
     * 根据销量排序查找商品
     */
    Page<Product> findByStatusOrderBySalesDesc(Product.ProductStatus status, Pageable pageable);

    /**
     * 查找库存不足的商品
     */
    @Query("SELECT p FROM Product p WHERE p.stock < :threshold AND p.status = 'ON_SALE'")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * 统计商品总数
     */
    long countByStatus(Product.ProductStatus status);

    /**
     * 查找相关商品（同分类）
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.id != :productId AND p.status = 'ON_SALE' ORDER BY p.sales DESC")
    List<Product> findRelatedProducts(@Param("categoryId") Long categoryId, @Param("productId") Long productId, Pageable pageable);

    /**
     * 多条件搜索商品
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:keyword IS NULL OR p.name LIKE %:keyword% OR p.keywords LIKE %:keyword%) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "p.status = 'ON_SALE' " +
           "ORDER BY " +
           "CASE WHEN :sortBy = 'price_asc' THEN p.price END ASC, " +
           "CASE WHEN :sortBy = 'price_desc' THEN p.price END DESC, " +
           "CASE WHEN :sortBy = 'sales' THEN p.sales END DESC, " +
           "p.sortOrder DESC")
    Page<Product> searchProducts(@Param("keyword") String keyword,
                                @Param("categoryId") Long categoryId,
                                @Param("minPrice") BigDecimal minPrice,
                                @Param("maxPrice") BigDecimal maxPrice,
                                @Param("sortBy") String sortBy,
                                Pageable pageable);
}