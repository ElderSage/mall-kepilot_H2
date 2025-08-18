<template>
  <div class="layout">
    <!-- 主要内容区域 -->
    <main class="main-content">
      <router-view v-slot="{ Component, route }">
        <keep-alive :include="keepAlivePages">
          <component :is="Component" :key="route.fullPath" />
        </keep-alive>
      </router-view>
    </main>

    <!-- 底部导航栏 -->
    <van-tabbar
      v-model="activeTab"
      :fixed="true"
      :placeholder="true"
      :safe-area-inset-bottom="true"
      active-color="#3b82f6"
      inactive-color="#64748b"
    >
      <van-tabbar-item
        to="/home"
        icon="home-o"
        name="home"
      >
        首页
      </van-tabbar-item>
      
      <van-tabbar-item
        to="/category"
        icon="apps-o"
        name="category"
      >
        分类
      </van-tabbar-item>
      
      <van-tabbar-item
        to="/cart"
        icon="shopping-cart-o"
        name="cart"
        :badge="cartCount > 0 ? cartCount : ''"
      >
        购物车
      </van-tabbar-item>
      
      <van-tabbar-item
        to="/profile"
        icon="user-o"
        name="profile"
      >
        我的
      </van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const cartStore = useCartStore()
const userStore = useUserStore()

const activeTab = ref('home')

// 需要缓存的页面
const keepAlivePages = ['Home', 'Category']

// 购物车数量
const cartCount = computed(() => cartStore.cartCount)

// 监听路由变化，更新活跃标签
watch(
  () => route.name,
  (newName) => {
    if (newName && typeof newName === 'string') {
      activeTab.value = newName.toLowerCase()
    }
  },
  { immediate: true }
)

// 获取购物车数量
const fetchCartCount = async () => {
  if (userStore.isLoggedIn) {
    await cartStore.getCartCount()
  }
}

onMounted(() => {
  fetchCartCount()
})

// 监听登录状态变化
watch(
  () => userStore.isLoggedIn,
  (isLoggedIn) => {
    if (isLoggedIn) {
      fetchCartCount()
    } else {
      cartStore.clearCart()
    }
  }
)
</script>

<style scoped>
.layout {
  min-height: 100vh;
  background-color: #f8fafc;
}

.main-content {
  padding-bottom: 50px; /* 为底部导航栏留出空间 */
  min-height: calc(100vh - 50px);
}

/* 自定义底部导航栏样式 */
:deep(.van-tabbar) {
  border-top: 1px solid #e2e8f0;
  background-color: #ffffff;
}

:deep(.van-tabbar-item__text) {
  font-size: 12px;
  margin-top: 2px;
}

:deep(.van-tabbar-item__icon) {
  font-size: 20px;
}

:deep(.van-badge) {
  background-color: #ef4444;
}
</style>