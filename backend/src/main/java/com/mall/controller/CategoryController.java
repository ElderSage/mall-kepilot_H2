package com.mall.controller;

import com.mall.common.ApiResponse;
import com.mall.entity.Category;
import com.mall.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/categories")
@Tag(name = "分类管理", description = "商品分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类")
    public ApiResponse<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ApiResponse.success(categories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据ID获取分类")
    public ApiResponse<Category> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ApiResponse.success(category);
    }

    @PostMapping
    @Operation(summary = "创建分类")
    public ApiResponse<Category> createCategory(@RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return ApiResponse.success(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类")
    public ApiResponse<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        category.setId(id);
        Category updated = categoryService.updateCategory(category);
        return ApiResponse.success(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public ApiResponse<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ApiResponse.success();
    }
}