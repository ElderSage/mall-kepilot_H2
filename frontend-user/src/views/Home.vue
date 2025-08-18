<template>
  <div class="home">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <van-search
        v-model="searchKeyword"
        placeholder="搜索商品"
        shape="round"
        background="#3b82f6"
        @click="goToSearch"
        @search="onSearch"
      />
    </div>

    <!-- 轮播图 -->
    <div class="banner-section">
      <van-swipe
        :autoplay="3000"
        indicator-color="white"
        class="banner-swipe"
      >
        <van-swipe-item
          v-for="banner in banners"
          :key="banner.id"
          @click="onBannerClick(banner)"
        >
          <img
            :src="banner.image"
            :alt="banner.title"
            class="banner-image"
          />
        </van-swipe-item>
      </van-swipe>
    </div>

    <!-- 快捷入口 -->
    <div class="quick-nav">
      <div class="nav-grid">
        <div
          v-for="nav in quickNavs"
          :key="nav.id"
          class="nav-item"
          @click="onNavClick(nav)"
        >
          <div class="nav-icon">
            <van-icon :name="nav.icon" size="24" />
          </div>
          <span class="nav-text">{{ nav.title }}</span>
        </div>
      </div>
    </div>

    <!-- 商品分类 -->
    <div class="section">
      <div class="section-header">
        <h3 class="section-title">商品分类</h3>
        <van-button
          type="primary"
          size="small"
          plain
          @click="$router.push('/category')"
        >
          查看全部
        </van-button>
      </div>
      <div class="category-grid">
        <div
          v-for="category in categories"
          :key="category.id"
          class="category-item"
          @click="goToCategory(category.id)"
        >
          <img
            :src="category.image"
            :alt="category.name"
            class="category-image"
          />
          <span class="category-name">{{ category.name }}</span>
        </div>
      </div>
    </div>

    <!-- 热门商品 -->
    <div class="section">
      <div class="section-header">
        <h3 class="section-title">热门商品</h3>
        <van-button
          type="primary"
          size="small"
          plain
          @click="goToProductList('hot')"
        >
          查看更多
        </van-button>
      </div>
      <div class="product-grid">
        <ProductCard
          v-for="product in hotProducts"
          :key="product.id"
          :product="product"
          @click="goToProduct(product.id)"
        />
      </div>
    </div>

    <!-- 新品推荐 -->
    <div class="section">
      <div class="section-header">
        <h3 class="section-title">新品推荐</h3>
        <van-button
          type="primary"
          size="small"
          plain
          @click="goToProductList('new')"
        >
          查看更多
        </van-button>
      </div>
      <div class="product-grid">
        <ProductCard
          v-for="product in newProducts"
          :key="product.id"
          :product="product"
          @click="goToProduct(product.id)"
        />
      </div>
    </div>

    <!-- 推荐商品 */
    <div class="section">
      <div class="section-header">
        <h3 class="section-title">为你推荐</h3>
      </div>
      <div class="product-grid">
        <ProductCard
          v-for="product in recommendProducts"
          :key="product.id"
          :product="product"
          @click="goToProduct(product.id)"
        />
      </div>
    </div>

    <!-- 加载更多 -->
    <div v-if="loading" class="loading-more">
      <van-loading size="24px">加载中...</van-loading>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import type { Product, Category, Banner } from '@/types'

const router = useRouter()

// 响应式数据
const searchKeyword = ref('')
const loading = ref(false)
const banners = ref<Banner[]>([])
const categories = ref<Category[]>([])
const hotProducts = ref<Product[]>([])
const newProducts = ref<Product[]>([])
const recommendProducts = ref<Product[]>([])

// 快捷导航
const quickNavs = ref([
  { id: 1, title: '限时折扣', icon: 'fire-o', link: '/products?type=discount' },
  { id: 2, title: '新人专享', icon: 'gift-o', link: '/products?type=newbie' },
  { id: 3, title: '品牌特卖', icon: 'star-o', link: '/products?type=brand' },
  { id: 4, title: '每日签到', icon: 'calendar-o', link: '/checkin' }
])

// 搜索相关
const goToSearch = () => {
  router.push('/search')
}

const onSearch = (value: string) => {
  router.push(`/search?keyword=${encodeURIComponent(value)}`)
}

// 轮播图点击
const onBannerClick = (banner: Banner) => {
  if (banner.link) {
    if (banner.linkType === 'PRODUCT') {
      router.push(`/product/${banner.link}`)
    } else if (banner.linkType === 'CATEGORY') {
      router.push(`/products?categoryId=${banner.link}`)
    } else if (banner.linkType === 'URL') {
      window.open(banner.link)
    }
  }
}

// 快捷导航点击
const onNavClick = (nav: any) => {
  router.push(nav.link)
}

// 分类点击
const goToCategory = (categoryId: number) => {
  router.push(`/products?categoryId=${categoryId}`)
}

// 商品点击
const goToProduct = (productId: number) => {
  router.push(`/product/${productId}`)
}

// 商品列表
const goToProductList = (type: string) => {
  router.push(`/products?type=${type}`)
}

// 获取首页数据
const fetchHomeData = async () => {
  try {
    loading.value = true
    
    // 这里应该调用实际的API
    // 模拟数据
    banners.value = [
      {
        id: 1,
        title: '春季新品上市',
        image: 'https://via.placeholder.com/375x150/3b82f6/ffffff?text=Banner1',
        link: '/products?type=new',
        linkType: 'URL',
        sortOrder: 1,
        isShow: true
      },
      {
        id: 2,
        title: '限时特惠',
        image: 'https://via.placeholder.com/375x150/ef4444/ffffff?text=Banner2',
        link: '/products?type=discount',
        linkType: 'URL',
        sortOrder: 2,
        isShow: true
      }
    ]

    categories.value = [
      {
        id: 1,
        name: '女装',
        parentId: 0,
        level: 1,
        sortOrder: 1,
        image: 'https://via.placeholder.com/60x60/f3f4f6/374151?text=女装',
        isShow: true
      },
      {
        id: 2,
        name: '男装',
        parentId: 0,
        level: 1,
        sortOrder: 2,
        image: 'https://via.placeholder.com/60x60/f3f4f6/374151?text=男装',
        isShow: true
      },
      {
        id: 3,
        name: '鞋靴',
        parentId: 0,
        level: 1,
        sortOrder: 3,
        image: 'https://via.placeholder.com/60x60/f3f4f6/374151?text=鞋靴',
        isShow: true
      },
      {
        id: 4,
        name: '箱包',
        parentId: 0,
        level: 1,
        sortOrder: 4,
        image: 'https://via.placeholder.com/60x60/f3f4f6/374151?text=箱包',
        isShow: true
      }
    ]

    // 模拟商品数据
    const mockProduct = (id: number, name: string) => ({
      id,
      name,
      subtitle: '商品副标题',
      mainImage: `https://via.placeholder.com/200x200/f3f4f6/374151?text=Product${id}`,
      price: Math.floor(Math.random() * 500) + 50,
      originalPrice: Math.floor(Math.random() * 200) + 100,
      stock: Math.floor(Math.random() * 100) + 10,
      sales: Math.floor(Math.random() * 1000),
      status: 'ON_SALE' as const,
      category: categories.value[0],
      isHot: Math.random() > 0.5,
      isNew: Math.random() > 0.5,
      isRecommend: Math.random() > 0.5,
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString()
    })

    hotProducts.value = Array.from({ length: 4 }, (_, i) => 
      mockProduct(i + 1, `热门商品 ${i + 1}`)
    )

    newProducts.value = Array.from({ length: 4 }, (_, i) => 
      mockProduct(i + 5, `新品 ${i + 1}`)
    )

    recommendProducts.value = Array.from({ length: 6 }, (_, i) => 
      mockProduct(i + 9, `推荐商品 ${i + 1}`)
    )

  } catch (error) {
    console.error('获取首页数据失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchHomeData()
})
</script>

<style scoped>
.home {
  background-color: #f8fafc;
  min-height: 100vh;
}

/* 搜索栏 */
.search-bar {
  padding: 8px 16px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
}

:deep(.van-search) {
  padding: 0;
}

:deep(.van-search__content) {
  background-color: rgba(255, 255, 255, 0.9);
  border-radius: 20px;
}

/* 轮播图 */
.banner-section {
  margin-bottom: 16px;
}

.banner-swipe {
  height: 150px;
}

.banner-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 快捷导航 */
.quick-nav {
  background-color: white;
  margin-bottom: 16px;
  padding: 16px;
}

.nav-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.nav-item:active {
  transform: scale(0.95);
}

.nav-icon {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-bottom: 8px;
}

.nav-text {
  font-size: 12px;
  color: #374151;
}

/* 分类网格 */
.category-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  padding: 0 16px;
}

.category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.category-item:active {
  transform: scale(0.95);
}

.category-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  margin-bottom: 8px;
}

.category-name {
  font-size: 12px;
  color: #374151;
  text-align: center;
}

/* 商品网格 */
.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  padding: 0 16px;
}

/* 通用样式 */
.section {
  background-color: white;
  margin-bottom: 16px;
  padding: 16px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 16px;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.loading-more {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  color: #6b7280;
}
</style>