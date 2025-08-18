package com.mall.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户实体类
 */
@Entity
@Table(name = "users")
public class User extends BaseEntity {

    @NotBlank(message = "用户名不能为空")
    @Column(name = "username", unique = true, length = 50)
    private String username;

    @JsonIgnore
    @NotBlank(message = "密码不能为空")
    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Email(message = "邮箱格式不正确")
    @Column(name = "email", length = 100)
    private String email;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birthday")
    private LocalDateTime birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status = UserStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role = UserRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_level")
    private MemberLevel memberLevel = MemberLevel.NORMAL;

    @Column(name = "growth_value")
    private Integer growthValue = 0;

    @Column(name = "points")
    private Integer points = 0;

    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "last_login_ip", length = 50)
    private String lastLoginIp;

    @Column(name = "wechat_openid", length = 100)
    private String wechatOpenid;

    @Column(name = "alipay_userid", length = 100)
    private String alipayUserid;

    // 枚举定义
    public enum Gender {
        MALE("男"), FEMALE("女"), UNKNOWN("未知");
        
        private final String description;
        
        Gender(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    public enum UserStatus {
        ACTIVE("正常"), LOCKED("锁定"), DISABLED("禁用");
        
        private final String description;
        
        UserStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    public enum MemberLevel {
        NORMAL("普通会员", 0, BigDecimal.ONE),
        SILVER("白银会员", 1000, new BigDecimal("0.95")),
        GOLD("黄金会员", 5000, new BigDecimal("0.90")),
        DIAMOND("钻石会员", 20000, new BigDecimal("0.85"));
        
        private final String description;
        private final Integer requiredGrowthValue;
        private final BigDecimal discount;
        
        MemberLevel(String description, Integer requiredGrowthValue, BigDecimal discount) {
            this.description = description;
            this.requiredGrowthValue = requiredGrowthValue;
            this.discount = discount;
        }
        
        public String getDescription() {
            return description;
        }
        
        public Integer getRequiredGrowthValue() {
            return requiredGrowthValue;
        }
        
        public BigDecimal getDiscount() {
            return discount;
        }
    }

    // 用户角色枚举
    public enum UserRole {
        USER("普通用户"), ADMIN("管理员");
        
        private final String description;
        
        UserRole(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDateTime getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDateTime birthday) {
        this.birthday = birthday;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public MemberLevel getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(MemberLevel memberLevel) {
        this.memberLevel = memberLevel;
    }

    public Integer getGrowthValue() {
        return growthValue;
    }

    public void setGrowthValue(Integer growthValue) {
        this.growthValue = growthValue;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getWechatOpenid() {
        return wechatOpenid;
    }

    public void setWechatOpenid(String wechatOpenid) {
        this.wechatOpenid = wechatOpenid;
    }

    public String getAlipayUserid() {
        return alipayUserid;
    }

    public void setAlipayUserid(String alipayUserid) {
        this.alipayUserid = alipayUserid;
    }
}