import Cookies from 'js-cookie'

const TOKEN_KEY = 'admin_token'

// Token相关操作
export const getToken = (): string | undefined => {
  return Cookies.get(TOKEN_KEY)
}

export const setToken = (token: string): void => {
  Cookies.set(TOKEN_KEY, token, { expires: 7 }) // 7天过期
}

export const removeToken = (): void => {
  Cookies.remove(TOKEN_KEY)
}

// 检查Token是否存在
export const hasToken = (): boolean => {
  return !!getToken()
}