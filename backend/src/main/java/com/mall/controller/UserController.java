package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.entity.User;
import com.mall.security.UserPrincipal;
import com.mall.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理", description = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "获取用户信息")
    public ApiResponse<User> getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        
        // 清除敏感信息
        user.setPassword(null);
        
        return ApiResponse.success(user);
    }

    @PutMapping("/profile")
    @Operation(summary = "更新用户信息")
    public ApiResponse<User> updateUserProfile(@RequestBody User userInfo) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getId();
        
        User user = userService.findById(userId).orElse(null);
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        
        // 只允许更新部分字段
        if (userInfo.getNickname() != null) {
            user.setNickname(userInfo.getNickname());
        }
        if (userInfo.getEmail() != null) {
            user.setEmail(userInfo.getEmail());
        }
        if (userInfo.getPhone() != null) {
            user.setPhone(userInfo.getPhone());
        }
        if (userInfo.getAvatar() != null) {
            user.setAvatar(userInfo.getAvatar());
        }
        if (userInfo.getGender() != null) {
            user.setGender(userInfo.getGender());
        }
        
        User updatedUser = userService.updateUser(user);
        updatedUser.setPassword(null);
        
        return ApiResponse.success("用户信息更新成功", updatedUser);
    }
}