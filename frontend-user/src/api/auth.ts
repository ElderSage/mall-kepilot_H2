import request from '@/utils/request'
import type { LoginRequest, RegisterRequest, ApiResponse, JwtAuthenticationResponse } from '@/types'

export const authApi = {
  // 用户登录
  login: (data: LoginRequest): Promise<ApiResponse<JwtAuthenticationResponse>> => {
    return request.post('/auth/login', data)
  },

  // 用户注册
  register: (data: RegisterRequest): Promise<ApiResponse<string>> => {
    return request.post('/auth/register', data)
  },

  // 用户退出
  logout: (): Promise<ApiResponse<string>> => {
    return request.post('/auth/logout')
  },

  // 刷新Token
  refreshToken: (refreshToken: string): Promise<ApiResponse<JwtAuthenticationResponse>> => {
    return request.post('/auth/refresh', null, {
      params: { refreshToken }
    })
  },

  // 发送验证码
  sendSmsCode: (phone: string): Promise<ApiResponse<string>> => {
    return request.post('/auth/sms-code', { phone })
  },

  // 验证手机验证码
  verifySmsCode: (phone: string, code: string): Promise<ApiResponse<boolean>> => {
    return request.post('/auth/verify-sms', { phone, code })
  },

  // 重置密码
  resetPassword: (data: {
    phone: string
    code: string
    newPassword: string
  }): Promise<ApiResponse<string>> => {
    return request.post('/auth/reset-password', data)
  }
}