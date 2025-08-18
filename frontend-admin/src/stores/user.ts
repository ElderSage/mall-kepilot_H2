import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { AdminUser, LoginRequest } from '@/types'
import { authApi } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const user = ref<AdminUser | null>(null)
  const token = ref<string>('')
  const permissions = ref<string[]>([])

  // 计算属性
  const isLoggedIn = computed(() => !!token.value && !!user.value)
  const userInfo = computed(() => user.value)
  const hasPermission = computed(() => (permission: string) => {
    return permissions.value.includes(permission) || permissions.value.includes('*')
  })

  // 初始化用户信息
  const initUser = () => {
    const savedToken = getToken()
    if (savedToken) {
      token.value = savedToken
      // 这里可以调用API获取用户信息
      // getUserInfo()
    }
  }

  // 登录
  const login = async (loginData: LoginRequest) => {
    try {
      const response = await authApi.login(loginData)
      if (response.success) {
        const { accessToken, userInfo, permissions: userPermissions } = response.data
        
        token.value = accessToken
        user.value = userInfo
        permissions.value = userPermissions || []
        
        setToken(accessToken)
        
        return { success: true, message: '登录成功' }
      } else {
        return { success: false, message: response.message }
      }
    } catch (error: any) {
      return { success: false, message: error.message || '登录失败' }
    }
  }

  // 退出登录
  const logout = async () => {
    try {
      await authApi.logout()
    } catch (error) {
      console.error('退出登录失败:', error)
    } finally {
      token.value = ''
      user.value = null
      permissions.value = []
      removeToken()
    }
  }

  // 获取用户信息
  const getUserInfo = async () => {
    try {
      const response = await authApi.getUserInfo()
      if (response.success) {
        user.value = response.data.userInfo
        permissions.value = response.data.permissions || []
        return true
      }
      return false
    } catch (error) {
      console.error('获取用户信息失败:', error)
      return false
    }
  }

  // 更新用户信息
  const updateUserInfo = (newUserInfo: Partial<AdminUser>) => {
    if (user.value) {
      user.value = { ...user.value, ...newUserInfo }
    }
  }

  return {
    // 状态
    user: readonly(user),
    token: readonly(token),
    permissions: readonly(permissions),
    
    // 计算属性
    isLoggedIn,
    userInfo,
    hasPermission,
    
    // 方法
    initUser,
    login,
    logout,
    getUserInfo,
    updateUserInfo
  }
})