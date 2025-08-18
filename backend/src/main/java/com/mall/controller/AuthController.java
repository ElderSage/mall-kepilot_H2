package com.mall.controller;

import com.mall.dto.ApiResponse;
import com.mall.dto.JwtAuthenticationResponse;
import com.mall.dto.LoginRequest;
import com.mall.entity.User;
import com.mall.security.JwtTokenProvider;
import com.mall.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户登录、注册、退出等认证相关接口")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户名密码登录")
    public ApiResponse<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest,
                                                       HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = tokenProvider.generateAccessToken(authentication);
            String refreshToken = tokenProvider.generateRefreshToken(authentication);

            // 更新用户登录信息
            User user = userService.findByUsername(loginRequest.getUsername()).orElse(null);
            if (user != null) {
                userService.updateLoginInfo(user.getId(), getClientIpAddress(request));
            }

            // 构建用户信息
            JwtAuthenticationResponse.UserInfo userInfo = new JwtAuthenticationResponse.UserInfo();
            if (user != null) {
                userInfo.setId(user.getId());
                userInfo.setUsername(user.getUsername());
                userInfo.setNickname(user.getNickname());
                userInfo.setEmail(user.getEmail());
                userInfo.setPhone(user.getPhone());
                userInfo.setAvatar(user.getAvatar());
                userInfo.setMemberLevel(user.getMemberLevel().getDescription());
                userInfo.setGrowthValue(user.getGrowthValue());
                userInfo.setPoints(user.getPoints());
            }

            JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                    accessToken, refreshToken, jwtExpiration / 1000, userInfo);

            return ApiResponse.success("登录成功", response);
        } catch (Exception e) {
            return ApiResponse.error("用户名或密码错误");
        }
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册", description = "新用户注册")
    public ApiResponse<String> register(@Valid @RequestBody User user) {
        try {
            userService.register(user);
            return ApiResponse.success("注册成功");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "用户退出", description = "用户退出登录")
    public ApiResponse<String> logout() {
        SecurityContextHolder.clearContext();
        return ApiResponse.success("退出成功");
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新Token", description = "使用刷新令牌获取新的访问令牌")
    public ApiResponse<JwtAuthenticationResponse> refreshToken(@RequestParam String refreshToken) {
        try {
            if (!tokenProvider.validateToken(refreshToken)) {
                return ApiResponse.error("刷新令牌无效", 401);
            }

            Long userId = tokenProvider.getUserIdFromToken(refreshToken);
            User user = userService.findById(userId).orElse(null);
            
            if (user == null) {
                return ApiResponse.error("用户不存在", 401);
            }

            // 创建新的认证对象
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), null, null);

            String newAccessToken = tokenProvider.generateAccessToken(authentication);
            String newRefreshToken = tokenProvider.generateRefreshToken(authentication);

            // 构建用户信息
            JwtAuthenticationResponse.UserInfo userInfo = new JwtAuthenticationResponse.UserInfo();
            userInfo.setId(user.getId());
            userInfo.setUsername(user.getUsername());
            userInfo.setNickname(user.getNickname());
            userInfo.setEmail(user.getEmail());
            userInfo.setPhone(user.getPhone());
            userInfo.setAvatar(user.getAvatar());
            userInfo.setMemberLevel(user.getMemberLevel().getDescription());
            userInfo.setGrowthValue(user.getGrowthValue());
            userInfo.setPoints(user.getPoints());

            JwtAuthenticationResponse response = new JwtAuthenticationResponse(
                    newAccessToken, newRefreshToken, jwtExpiration / 1000, userInfo);

            return ApiResponse.success("刷新成功", response);
        } catch (Exception e) {
            return ApiResponse.error("刷新失败", 401);
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0];
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}