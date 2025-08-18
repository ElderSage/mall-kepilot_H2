package com.mall.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * 商品SKU实体类
 */
@Entity
@Table(name = "product_skus")
public class ProductSku extends BaseEntity {

    @NotNull(message = "商品不能为空")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @NotBlank(message = "SKU编码不能为空")
    @Column(name = "sku_code", unique = true, length = 100)
    private String skuCode;

    @Column(name = "name", length = 200)
    private String name;

    @NotNull(message = "SKU价格不能为空")
    @DecimalMin(value = "0.01", message = "SKU价格必须大于0")
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "stock")
    private Integer stock = 0;

    @Column(name = "sales")
    private Integer sales = 0;

    @Column(name = "image", length = 500)
    private String image;

    @Column(name = "weight", precision = 8, scale = 2)
    private BigDecimal weight;

    @Column(name = "attributes", length = 1000)
    private String attributes; // JSON格式存储属性，如：{"颜色":"红色","尺码":"L"}

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SkuStatus status = SkuStatus.ACTIVE;

    // SKU状态枚举
    public enum SkuStatus {
        ACTIVE("正常"), INACTIVE("停用");
        
        private final String description;
        
        SkuStatus(String description) {
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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public SkuStatus getStatus() {
        return status;
    }

    public void setStatus(SkuStatus status) {
        this.status = status;
    }
}