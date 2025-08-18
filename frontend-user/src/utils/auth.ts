import Cookies from 'js-cookie'

const TOKEN_KEY = 'mall_token'
const REFRESH_TOKEN_KEY = 'mall_refresh_token'

// Token相关操作
export const getToken = (): string | undefined => {
  return Cookies.get(TOKEN_KEY)
}

export const setToken = (token: string): void => {
  Cookies.set(TOKEN_KEY, token, { expires: 7 }) // 7天过期
}

export const removeToken = (): void => {
  Cookies.remove(TOKEN_KEY)
  Cookies.remove(REFRESH_TOKEN_KEY)
}

// 刷新Token相关操作
export const getRefreshToken = (): string | undefined => {
  return Cookies.get(REFRESH_TOKEN_KEY)
}

export const setRefreshToken = (token: string): void => {
  Cookies.set(REFRESH_TOKEN_KEY, token, { expires: 30 }) // 30天过期
}

// 检查Token是否存在
export const hasToken = (): boolean => {
  return !!getToken()
}