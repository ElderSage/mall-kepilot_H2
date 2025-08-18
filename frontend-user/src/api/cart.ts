import request from '@/utils/request'
import type { ApiResponse, CartItem } from '@/types'

export const cartApi = {
  // 获取购物车列表
  getCartItems: (): Promise<ApiResponse<CartItem[]>> => {
    return request.get('/cart')
  },

  // 添加到购物车
  addToCart: (data: {
    productId: number
    skuId?: number
    quantity: number
  }): Promise<ApiResponse<string>> => {
    return request.post('/cart', data)
  },

  // 更新商品数量
  updateQuantity: (itemId: number, quantity: number): Promise<ApiResponse<string>> => {
    return request.put(`/cart/${itemId}/quantity`, { quantity })
  },

  // 删除购物车商品
  removeFromCart: (itemId: number): Promise<ApiResponse<string>> => {
    return request.delete(`/cart/${itemId}`)
  },

  // 批量删除
  batchRemove: (itemIds: number[]): Promise<ApiResponse<string>> => {
    return request.delete('/cart/batch', { data: { itemIds } })
  },

  // 更新选中状态
  updateCheck: (itemId: number, checked: boolean): Promise<ApiResponse<string>> => {
    return request.put(`/cart/${itemId}/check`, { checked })
  },

  // 更新全选状态
  updateAllCheck: (checked: boolean): Promise<ApiResponse<string>> => {
    return request.put('/cart/check-all', { checked })
  },

  // 获取购物车商品数量
  getCartCount: (): Promise<ApiResponse<number>> => {
    return request.get('/cart/count')
  },

  // 清空购物车
  clearCart: (): Promise<ApiResponse<string>> => {
    return request.delete('/cart/clear')
  }
}