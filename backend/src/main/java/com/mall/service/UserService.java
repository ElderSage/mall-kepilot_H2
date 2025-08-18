package com.mall.service;

import com.mall.entity.User;
import com.mall.repository.UserRepository;
import com.mall.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 用户服务类
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + id));

        return UserPrincipal.create(user);
    }

    /**
     * 用户注册
     */
    public User register(User user) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已存在");
        }

        // 检查手机号是否已存在
        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new RuntimeException("手机号已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 设置默认值
        user.setStatus(User.UserStatus.ACTIVE);
        user.setMemberLevel(User.MemberLevel.NORMAL);
        user.setGrowthValue(0);
        user.setPoints(0);

        return userRepository.save(user);
    }

    /**
     * 根据用户名查找用户
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据ID查找用户
     */
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 更新用户登录信息
     */
    public void updateLoginInfo(Long userId, String ip) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(ip);
        userRepository.save(user);
    }

    /**
     * 增加用户成长值
     */
    public void addGrowthValue(Long userId, Integer growthValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setGrowthValue(user.getGrowthValue() + growthValue);
        
        // 更新会员等级
        updateMemberLevel(user);
        
        userRepository.save(user);
    }

    /**
     * 增加用户积分
     */
    public void addPoints(Long userId, Integer points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
    }

    /**
     * 扣减用户积分
     */
    public void deductPoints(Long userId, Integer points) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (user.getPoints() < points) {
            throw new RuntimeException("积分不足");
        }
        
        user.setPoints(user.getPoints() - points);
        userRepository.save(user);
    }

    /**
     * 更新会员等级
     */
    private void updateMemberLevel(User user) {
        Integer growthValue = user.getGrowthValue();
        
        if (growthValue >= User.MemberLevel.DIAMOND.getRequiredGrowthValue()) {
            user.setMemberLevel(User.MemberLevel.DIAMOND);
        } else if (growthValue >= User.MemberLevel.GOLD.getRequiredGrowthValue()) {
            user.setMemberLevel(User.MemberLevel.GOLD);
        } else if (growthValue >= User.MemberLevel.SILVER.getRequiredGrowthValue()) {
            user.setMemberLevel(User.MemberLevel.SILVER);
        } else {
            user.setMemberLevel(User.MemberLevel.NORMAL);
        }
    }

    /**
     * 修改密码
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 重置密码
     */
    public void resetPassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}