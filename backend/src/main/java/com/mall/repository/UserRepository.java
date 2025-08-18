package com.mall.repository;

import com.mall.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户数据访问层
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据邮箱查找用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 根据手机号查找用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据微信OpenID查找用户
     */
    Optional<User> findByWechatOpenid(String wechatOpenid);

    /**
     * 根据支付宝用户ID查找用户
     */
    Optional<User> findByAlipayUserid(String alipayUserid);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 根据成长值统计用户等级分布
     */
    @Query("SELECT u.memberLevel, COUNT(u) FROM User u GROUP BY u.memberLevel")
    Object[] countByMemberLevel();

    /**
     * 查找活跃用户数量
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE'")
    long countActiveUsers();

    /**
     * 根据成长值范围查找用户
     */
    @Query("SELECT u FROM User u WHERE u.growthValue BETWEEN :minGrowth AND :maxGrowth")
    java.util.List<User> findByGrowthValueBetween(@Param("minGrowth") Integer minGrowth, @Param("maxGrowth") Integer maxGrowth);
}