package com.mall.repository;

import com.mall.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 分类数据访问层
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 根据状态查找分类
     */
    List<Category> findByStatusOrderBySortOrderAsc(Category.CategoryStatus status);

    /**
     * 根据父分类ID查找子分类
     */
    List<Category> findByParentIdAndStatusOrderBySortOrderAsc(Long parentId, Category.CategoryStatus status);

    /**
     * 查找顶级分类
     */
    List<Category> findByParentIdIsNullAndStatusOrderBySortOrderAsc(Category.CategoryStatus status);

    /**
     * 根据名称查找分类
     */
    List<Category> findByNameContainingAndStatus(String name, Category.CategoryStatus status);

    /**
     * 检查分类名称是否存在
     */
    boolean existsByNameAndParentId(String name, Long parentId);

    /**
     * 统计子分类数量
     */
    @Query("SELECT COUNT(c) FROM Category c WHERE c.parentId = :parentId AND c.status = 'ACTIVE'")
    long countByParentId(Long parentId);

    /**
     * 查找有商品的分类
     */
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p WHERE p.status = 'ON_SALE'")
    List<Category> findCategoriesWithProducts();
}