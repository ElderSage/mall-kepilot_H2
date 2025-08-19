package com.mall.service;

import com.mall.entity.Category;
import com.mall.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 分类服务类
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 获取所有分类
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 根据ID获取分类
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * 创建分类
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * 更新分类
     */
    public Category updateCategory(Long id, Category categoryDetails) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryDetails.getName());
            category.setParentId(categoryDetails.getParentId());
            category.setLevel(categoryDetails.getLevel());
            category.setSortOrder(categoryDetails.getSortOrder());
            category.setIcon(categoryDetails.getIcon());
            category.setImage(categoryDetails.getImage());
            category.setKeywords(categoryDetails.getKeywords());
            category.setDescription(categoryDetails.getDescription());
            category.setIsShow(categoryDetails.getIsShow());
            category.setStatus(categoryDetails.getStatus());
            return categoryRepository.save(category);
        }
        return null;
    }

    /**
     * 删除分类
     */
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * 根据父级ID获取子分类
     */
    public List<Category> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentIdAndStatusOrderBySortOrderAsc(parentId, Category.CategoryStatus.ACTIVE);
    }

    /**
     * 获取顶级分类
     */
    public List<Category> getTopCategories() {
        return categoryRepository.findByParentIdIsNullAndStatusOrderBySortOrderAsc(Category.CategoryStatus.ACTIVE);
    }
}