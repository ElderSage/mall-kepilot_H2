package com.mall.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 */
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    @NotBlank(message = "优惠券名称不能为空")
    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CouponType type;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    @NotNull(message = "开始时间不能为空")
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_count")
    private Integer totalCount;

    @Column(name = "used_count")
    private Integer usedCount = 0;

    @Column(name = "per_limit")
    private Integer perLimit = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CouponStatus status = CouponStatus.ACTIVE;

    @Column(name = "category_ids", length = 500)
    private String categoryIds; // 适用分类ID，逗号分隔

    @Column(name = "product_ids", length = 500)
    private String productIds; // 适用商品ID，逗号分隔

    // 优惠券类型枚举
    public enum CouponType {
        FIXED_AMOUNT("满减券"), PERCENTAGE("折扣券"), NO_THRESHOLD("无门槛券");
        
        private final String description;
        
        CouponType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    // 优惠券状态枚举
    public enum CouponStatus {
        ACTIVE("有效"), INACTIVE("无效"), EXPIRED("已过期");
        
        private final String description;
        
        CouponStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getPerLimit() {
        return perLimit;
    }

    public void setPerLimit(Integer perLimit) {
        this.perLimit = perLimit;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }
}