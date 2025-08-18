<template>
  <div class="category-page">
    <!-- 顶部搜索栏 -->
    <div class="search-header">
      <van-search
        v-model="searchKeyword"
        placeholder="搜索商品"
        shape="round"
        @search="onSearch"
      />
    </div>

    <div class="category-content">
      <!-- 左侧分类列表 -->
      <div class="category-sidebar">
        <van-sidebar v-model="activeCategory">
          <van-sidebar-item
            v-for="category in categories"
            :key="category.id"
            :title="category.name"
            @click="onCategoryClick(category)"
          />
        </van-sidebar>
      </div>

      <!-- 右侧子分类和商品 -->
      <div class="category-main">
        <div v-if="loading" class="loading-container">
          <van-loading size="24px">加载中...</van-loading>
        </div>
        
        <div v-else class="category-detail">
          <!-- 子分类 -->
          <div v-if="subCategories.length > 0" class="sub-categories">
            <div class="sub-category-grid">
              <div
                v-for="subCategory in subCategories"
                :key="subCategory.id"
                class="sub-category-item"
                @click="goToProducts(subCategory.id)"
              >
                <img
                  :src="subCategory.image"
                  :alt="subCategory.name"
                  class="sub-category-image"
                />
                <span class="sub-category-name">{{ subCategory.name }}</span>
              </div>
            </div>
          </div>

          <!-- 推荐商品 -->
          <div v-if="recommendProducts.length > 0" class="recommend-products">
            <h3 class="section-title">推荐商品</h3>
            <div class="product-grid">
              <ProductCard
                v-for="product in recommendProducts"
                :key="product.id"
                :product="product"
                @click="goToProduct(product.id)"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import ProductCard from '@/components/ProductCard.vue'
import type { Category, Product } from '@/types'

const router = useRouter()

// 响应式数据
const searchKeyword = ref('')
const activeCategory = ref(0)
const loading = ref(false)
const categories = ref<Category[]>([])
const subCategories = ref<Category[]>([])
const recommendProducts = ref<Product[]>([])

// 搜索
const onSearch = (value: string) => {
  router.push(`/search?keyword=${encodeURIComponent(value)}`)
}

// 分类点击
const onCategoryClick = (category: Category) => {
  fetchCategoryDetail(category.id)
}

// 跳转到商品列表
const goToProducts = (categoryId: number) => {
  router.push(`/products?categoryId=${categoryId}`)
}

// 跳转到商品详情
const goToProduct = (productId: number) => {
  router.push(`/product/${productId}`)
}

// 获取分类列表
const fetchCategories = async () => {
  try {
    // 模拟数据
    categories.value = [
      {
        id: 1,
        name: '女装',
        parentId: 0,
        level: 1,
        sortOrder: 1,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=女装',
        isShow: true
      },
      {
        id: 2,
        name: '男装',
        parentId: 0,
        level: 1,
        sortOrder: 2,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=男装',
        isShow: true
      },
      {
        id: 3,
        name: '鞋靴',
        parentId: 0,
        level: 1,
        sortOrder: 3,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=鞋靴',
        isShow: true
      },
      {
        id: 4,
        name: '箱包',
        parentId: 0,
        level: 1,
        sortOrder: 4,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=箱包',
        isShow: true
      },
      {
        id: 5,
        name: '配饰',
        parentId: 0,
        level: 1,
        sortOrder: 5,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=配饰',
        isShow: true
      }
    ]

    // 默认选中第一个分类
    if (categories.value.length > 0) {
      fetchCategoryDetail(categories.value[0].id)
    }
  } catch (error) {
    console.error('获取分类失败:', error)
  }
}

// 获取分类详情
const fetchCategoryDetail = async (categoryId: number) => {
  try {
    loading.value = true

    // 模拟子分类数据
    subCategories.value = [
      {
        id: categoryId * 10 + 1,
        name: '连衣裙',
        parentId: categoryId,
        level: 2,
        sortOrder: 1,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=连衣裙',
        isShow: true
      },
      {
        id: categoryId * 10 + 2,
        name: '上衣',
        parentId: categoryId,
        level: 2,
        sortOrder: 2,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=上衣',
        isShow: true
      },
      {
        id: categoryId * 10 + 3,
        name: '下装',
        parentId: categoryId,
        level: 2,
        sortOrder: 3,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=下装',
        isShow: true
      },
      {
        id: categoryId * 10 + 4,
        name: '套装',
        parentId: categoryId,
        level: 2,
        sortOrder: 4,
        image: 'https://via.placeholder.com/80x80/f3f4f6/374151?text=套装',
        isShow: true
      }
    ]

    // 模拟推荐商品
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

    recommendProducts.value = Array.from({ length: 6 }, (_, i) => 
      mockProduct(categoryId * 100 + i + 1, `推荐商品 ${i + 1}`)
    )

  } catch (error) {
    console.error('获取分类详情失败:', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.category-page {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f8fafc;
}

/* 搜索头部 */
.search-header {
  background-color: white;
  padding: 8px 16px;
  border-bottom: 1px solid #e5e7eb;
}

/* 内容区域 */
.category-content {
  flex: 1;
  display: flex;
  overflow: hidden;
}

/* 左侧分类栏 */
.category-sidebar {
  width: 100px;
  background-color: white;
  border-right: 1px solid #e5e7eb;
}

:deep(.van-sidebar) {
  height: 100%;
}

:deep(.van-sidebar-item) {
  padding: 16px 8px;
  font-size: 13px;
  text-align: center;
}

:deep(.van-sidebar-item--select) {
  background-color: #f3f4f6;
  color: #3b82f6;
  font-weight: 500;
}

:deep(.van-sidebar-item--select::before) {
  background-color: #3b82f6;
}

/* 右侧主内容 */
.category-main {
  flex: 1;
  overflow-y: auto;
  background-color: white;
}

.loading-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.category-detail {
  padding: 16px;
}

/* 子分类网格 */
.sub-categories {
  margin-bottom: 24px;
}

.sub-category-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.sub-category-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: transform 0.2s;
}

.sub-category-item:active {
  transform: scale(0.95);
}

.sub-category-image {
  width: 60px;
  height: 60px;
  border-radius: 8px;
  margin-bottom: 8px;
}

.sub-category-name {
  font-size: 12px;
  color: #374151;
  text-align: center;
}

/* 推荐商品 */
.recommend-products {
  margin-top: 24px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 16px 0;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .category-sidebar {
    width: 80px;
  }
  
  :deep(.van-sidebar-item) {
    padding: 12px 4px;
    font-size: 12px;
  }
  
  .sub-category-grid {
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
  }
  
  .sub-category-image {
    width: 50px;
    height: 50px;
  }
  
  .category-detail {
    padding: 12px;
  }
}
</style>