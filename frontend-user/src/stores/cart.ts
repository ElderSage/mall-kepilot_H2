import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { CartItem, Product } from '@/types'
import { cartApi } from '@/api/cart'
import { showToast } from 'vant'

export const useCartStore = defineStore('cart', () => {
  const cartItems = ref<CartItem[]>([])
  const loading = ref(false)

  // 计算属性
  const cartCount = computed(() => {
    return cartItems.value.reduce((total, item) => total + (item.checked ? item.quantity : 0), 0)
  })

  const totalPrice = computed(() => {
    return cartItems.value
      .filter(item => item.checked)
      .reduce((total, item) => {
        const price = item.productSku?.price || item.product.price
        return total + price * item.quantity
      }, 0)
  })

  const checkedItems = computed(() => {
    return cartItems.value.filter(item => item.checked)
  })

  const allChecked = computed(() => {
    return cartItems.value.length > 0 && cartItems.value.every(item => item.checked)
  })

  // 获取购物车列表
  const fetchCartItems = async () => {
    try {
      loading.value = true
      const response = await cartApi.getCartItems()
      if (response.success) {
        cartItems.value = response.data
      }
    } catch (error) {
      console.error('获取购物车失败:', error)
    } finally {
      loading.value = false
    }
  }

  // 添加到购物车
  const addToCart = async (product: Product, skuId?: number, quantity: number = 1) => {
    try {
      const response = await cartApi.addToCart({
        productId: product.id,
        skuId,
        quantity
      })
      
      if (response.success) {
        showToast('已添加到购物车')
        await fetchCartItems()
        return true
      } else {
        showToast(response.message)
        return false
      }
    } catch (error: any) {
      showToast(error.message || '添加失败')
      return false
    }
  }

  // 更新购物车商品数量
  const updateQuantity = async (itemId: number, quantity: number) => {
    try {
      const response = await cartApi.updateQuantity(itemId, quantity)
      if (response.success) {
        const item = cartItems.value.find(item => item.id === itemId)
        if (item) {
          item.quantity = quantity
        }
        return true
      } else {
        showToast(response.message)
        return false
      }
    } catch (error: any) {
      showToast(error.message || '更新失败')
      return false
    }
  }

  // 删除购物车商品
  const removeFromCart = async (itemId: number) => {
    try {
      const response = await cartApi.removeFromCart(itemId)
      if (response.success) {
        const index = cartItems.value.findIndex(item => item.id === itemId)
        if (index > -1) {
          cartItems.value.splice(index, 1)
        }
        showToast('已删除')
        return true
      } else {
        showToast(response.message)
        return false
      }
    } catch (error: any) {
      showToast(error.message || '删除失败')
      return false
    }
  }

  // 批量删除
  const batchRemove = async (itemIds: number[]) => {
    try {
      const response = await cartApi.batchRemove(itemIds)
      if (response.success) {
        cartItems.value = cartItems.value.filter(item => !itemIds.includes(item.id))
        showToast('已删除')
        return true
      } else {
        showToast(response.message)
        return false
      }
    } catch (error: any) {
      showToast(error.message || '删除失败')
      return false
    }
  }

  // 切换商品选中状态
  const toggleItemCheck = async (itemId: number, checked: boolean) => {
    try {
      const response = await cartApi.updateCheck(itemId, checked)
      if (response.success) {
        const item = cartItems.value.find(item => item.id === itemId)
        if (item) {
          item.checked = checked
        }
        return true
      }
      return false
    } catch (error) {
      console.error('更新选中状态失败:', error)
      return false
    }
  }

  // 全选/取消全选
  const toggleAllCheck = async (checked: boolean) => {
    try {
      const response = await cartApi.updateAllCheck(checked)
      if (response.success) {
        cartItems.value.forEach(item => {
          item.checked = checked
        })
        return true
      }
      return false
    } catch (error) {
      console.error('更新全选状态失败:', error)
      return false
    }
  }

  // 清空购物车
  const clearCart = () => {
    cartItems.value = []
  }

  // 获取购物车商品数量（用于显示角标）
  const getCartCount = async () => {
    try {
      const response = await cartApi.getCartCount()
      if (response.success) {
        return response.data
      }
      return 0
    } catch (error) {
      return 0
    }
  }

  return {
    // 状态
    cartItems: readonly(cartItems),
    loading: readonly(loading),
    
    // 计算属性
    cartCount,
    totalPrice,
    checkedItems,
    allChecked,
    
    // 方法
    fetchCartItems,
    addToCart,
    updateQuantity,
    removeFromCart,
    batchRemove,
    toggleItemCheck,
    toggleAllCheck,
    clearCart,
    getCartCount
  }
})