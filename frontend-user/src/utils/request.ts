import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { showToast } from 'vant'
import { useUserStore } from '@/stores/user'
import router from '@/router'

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
request.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const userStore = useUserStore()
    
    // 添加认证token
    if (userStore.token) {
      config.headers = config.headers || {}
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    
    return config
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response: AxiosResponse) => {
    const { data } = response
    
    // 如果是文件下载，直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    
    // 统一处理响应
    if (data.success) {
      return data
    } else {
      // 业务错误
      showToast(data.message || '请求失败')
      return Promise.reject(new Error(data.message || '请求失败'))
    }
  },
  async (error) => {
    const { response } = error
    
    if (response) {
      const { status, data } = response
      
      switch (status) {
        case 401:
          // 未授权，尝试刷新token
          const userStore = useUserStore()
          const refreshSuccess = await userStore.refreshAccessToken()
          
          if (refreshSuccess) {
            // 重新发送原请求
            return request(error.config)
          } else {
            // 刷新失败，跳转到登录页
            showToast('登录已过期，请重新登录')
            router.push({
              name: 'Login',
              query: { redirect: router.currentRoute.value.fullPath }
            })
          }
          break
          
        case 403:
          showToast('没有权限访问')
          break
          
        case 404:
          showToast('请求的资源不存在')
          break
          
        case 500:
          showToast('服务器内部错误')
          break
          
        default:
          showToast(data?.message || `请求失败 (${status})`)
      }
    } else if (error.code === 'ECONNABORTED') {
      showToast('请求超时，请重试')
    } else {
      showToast('网络错误，请检查网络连接')
    }
    
    return Promise.reject(error)
  }
)

export default request