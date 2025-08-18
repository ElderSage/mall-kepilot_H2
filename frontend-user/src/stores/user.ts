import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { User, LoginRequest, RegisterRequest } from '@/types/user'
import { authApi } from '@/api/auth'
import { getToken, setToken, removeToken } from '@/utils/auth'

export const useUserStore = defineStore('user', () => {
  const user = ref<User | null>(null)
  const token = ref<string>('')
  const refreshToken = ref<string>('')

  // 计算属性
  const isLoggedIn = computed(() => !!token.value && !!user.value)
  const userInfo = computed(() => user.value)
  const memberLevel = computed(() => user.value?.memberLevel || '普通会员')
  const points = computed(() => user.value?.points || 0)
  const growthValue = computed(() => user.value?.growthValue || 0)

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
        const { accessToken, refreshToken: newRefreshToken, userInfo } = response.data
        
        token.value = accessToken
        refreshToken.value = newRefreshToken
        user.value = userInfo
        
        setToken(accessToken)
        
        return { success: true, message: '登录成功' }
      } else {
        return { success: false, message: response.message }
      }
    } catch (error: any) {
      return { success: false, message: error.message || '登录失败' }
    }
  }

  // 注册
  const register = async (registerData: RegisterRequest) => {
    try {
      const response = await authApi.register(registerData)
      return response
    } catch (error: any) {
      return { success: false, message: error.message || '注册失败' }
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
      refreshToken.value = ''
      user.value = null
      removeToken()
    }
  }

  // 刷新Token
  const refreshAccessToken = async () => {
    try {
      if (!refreshToken.value) {
        throw new Error('没有刷新令牌')
      }
      
      const response = await authApi.refreshToken(refreshToken.value)
      if (response.success) {
        const { accessToken, refreshToken: newRefreshToken, userInfo } = response.data
        
        token.value = accessToken
        refreshToken.value = newRefreshToken
        user.value = userInfo
        
        setToken(accessToken)
        
        return true
      } else {
        throw new Error(response.message)
      }
    } catch (error) {
      console.error('刷新Token失败:', error)
      logout()
      return false
    }
  }

  // 更新用户信息
  const updateUserInfo = (newUserInfo: Partial<User>) => {
    if (user.value) {
      user.value = { ...user.value, ...newUserInfo }
    }
  }

  // 增加积分
  const addPoints = (points: number) => {
    if (user.value) {
      user.value.points = (user.value.points || 0) + points
    }
  }

  // 扣减积分
  const deductPoints = (points: number) => {
    if (user.value && user.value.points >= points) {
      user.value.points -= points
    }
  }

  // 增加成长值
  const addGrowthValue = (growth: number) => {
    if (user.value) {
      user.value.growthValue = (user.value.growthValue || 0) + growth
    }
  }

  return {
    // 状态
    user: readonly(user),
    token: readonly(token),
    refreshToken: readonly(refreshToken),
    
    // 计算属性
    isLoggedIn,
    userInfo,
    memberLevel,
    points,
    growthValue,
    
    // 方法
    initUser,
    login,
    register,
    logout,
    refreshAccessToken,
    updateUserInfo,
    addPoints,
    deductPoints,
    addGrowthValue
  }
})