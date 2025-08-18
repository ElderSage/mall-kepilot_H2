import request from '@/utils/request'
import type { LoginRequest, ApiResponse, JwtAuthenticationResponse } from '@/types'

export const authApi = {
  // 管理员登录
  login: (data: LoginRequest): Promise<ApiResponse<JwtAuthenticationResponse>> => {
    return request.post('/admin/auth/login', data)
  },

  // 管理员退出
  logout: (): Promise<ApiResponse<string>> => {
    return request.post('/admin/auth/logout')
  },

  // 获取用户信息
  getUserInfo: (): Promise<ApiResponse<{
    userInfo: any
    permissions: string[]
  }>> => {
    return request.get('/admin/auth/info')
  },

  // 刷新Token
  refreshToken: (refreshToken: string): Promise<ApiResponse<JwtAuthenticationResponse>> => {
    return request.post('/admin/auth/refresh', null, {
      params: { refreshToken }
    })
  }
}