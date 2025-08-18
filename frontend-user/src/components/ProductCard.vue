<template>
  <div class="product-card" @click="$emit('click')">
    <!-- 商品图片 -->
    <div class="product-image-wrapper">
      <img
        v-lazy="product.mainImage"
        :alt="product.name"
        class="product-image"
      />
      
      <!-- 标签 -->
      <div class="product-badges">
        <span v-if="product.isHot" class="badge badge-hot">热</span>
        <span v-if="product.isNew" class="badge badge-new">新</span>
        <span v-if="product.isRecommend" class="badge badge-recommend">荐</span>
      </div>
    </div>

    <!-- 商品信息 -->
    <div class="product-info">
      <h4 class="product-name line-clamp-2">{{ product.name }}</h4>
      
      <p v-if="product.subtitle" class="product-subtitle line-clamp-1">
        {{ product.subtitle }}
      </p>

      <!-- 价格信息 -->
      <div class="price-info">
        <span class="current-price">¥{{ product.price }}</span>
        <span
          v-if="product.originalPrice && product.originalPrice > product.price"
          class="original-price"
        >
          ¥{{ product.originalPrice }}
        </span>
      </div>

      <!-- 销量信息 -->
      <div class="sales-info">
        <span class="sales-text">已售{{ formatSales(product.sales) }}</span>
        <span v-if="product.stock <= 10" class="stock-warning">
          仅剩{{ product.stock }}件
        </span>
      </div>
    </div>

    <!-- 购物车按钮 -->
    <div class="cart-button" @click.stop="onAddToCart">
      <van-icon name="shopping-cart-o" size="16" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { showToast } from 'vant'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import type { Product } from '@/types'

interface Props {
  product: Product
}

const props = defineProps<Props>()
const emit = defineEmits<{
  click: []
}>()

const cartStore = useCartStore()
const userStore = useUserStore()
const router = useRouter()

// 格式化销量显示
const formatSales = (sales: number): string => {
  if (sales >= 10000) {
    return `${Math.floor(sales / 1000) / 10}万`
  } else if (sales >= 1000) {
    return `${Math.floor(sales / 100) / 10}k`
  }
  return sales.toString()
}

// 添加到购物车
const onAddToCart = async () => {
  if (!userStore.isLoggedIn) {
    showToast('请先登录')
    router.push('/login')
    return
  }

  const success = await cartStore.addToCart(props.product)
  if (success) {
    showToast('已添加到购物车')
  }
}
</script>

<style scoped>
.product-card {
  background-color: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
  position: relative;
}

.product-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-card:active {
  transform: translateY(0);
}

.product-image-wrapper {
  position: relative;
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.product-badges {
  position: absolute;
  top: 8px;
  left: 8px;
  display: flex;
  gap: 4px;
}

.badge {
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 500;
  color: white;
}

.badge-hot {
  background-color: #ef4444;
}

.badge-new {
  background-color: #10b981;
}

.badge-recommend {
  background-color: #3b82f6;
}

.product-info {
  padding: 12px;
}

.product-name {
  font-size: 14px;
  font-weight: 500;
  color: #1f2937;
  margin: 0 0 4px 0;
  line-height: 1.4;
}

.product-subtitle {
  font-size: 12px;
  color: #6b7280;
  margin: 0 0 8px 0;
  line-height: 1.3;
}

.price-info {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 8px;
}

.current-price {
  font-size: 16px;
  font-weight: 600;
  color: #ef4444;
}

.original-price {
  font-size: 12px;
  color: #9ca3af;
  text-decoration: line-through;
}

.sales-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 11px;
}

.sales-text {
  color: #6b7280;
}

.stock-warning {
  color: #f59e0b;
  font-weight: 500;
}

.cart-button {
  position: absolute;
  bottom: 12px;
  right: 12px;
  width: 28px;
  height: 28px;
  background-color: #3b82f6;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  cursor: pointer;
  transition: all 0.2s;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.cart-button:hover {
  background-color: #2563eb;
  transform: scale(1.1);
}

.cart-button:active {
  transform: scale(0.95);
}

/* 响应式设计 */
@media (max-width: 640px) {
  .product-image-wrapper {
    height: 140px;
  }
  
  .product-name {
    font-size: 13px;
  }
  
  .current-price {
    font-size: 15px;
  }
}
</style>