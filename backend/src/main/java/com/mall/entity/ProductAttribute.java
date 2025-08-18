package com.mall.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 商品属性实体类
 */
@Entity
@Table(name = "product_attributes")
public class ProductAttribute extends BaseEntity {

    @NotNull(message = "商品不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotBlank(message = "属性名称不能为空")
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "attr_value", length = 500)
    private String value;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AttributeType type = AttributeType.TEXT;

    // 属性类型枚举
    public enum AttributeType {
        TEXT("文本"), NUMBER("数字"), COLOR("颜色"), SIZE("尺寸"), IMAGE("图片");
        
        private final String description;
        
        AttributeType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    // Getters and Setters
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public AttributeType getType() {
        return type;
    }

    public void setType(AttributeType type) {
        this.type = type;
    }
}