<template>
  <div class="layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="sidebarWidth">
        <div class="sidebar">
          <!-- Logo -->
          <div class="logo">
            <img src="/logo.svg" alt="Logo" class="logo-img" />
            <span v-show="!isCollapsed" class="logo-text">商城管理</span>
          </div>
          
          <!-- 菜单 -->
          <el-menu
            :default-active="activeMenu"
            :collapse="isCollapsed"
            :unique-opened="true"
            router
            class="sidebar-menu"
          >
            <template v-for="route in menuRoutes" :key="route.name">
              <el-sub-menu
                v-if="route.children && route.children.length > 0"
                :index="route.path"
              >
                <template #title>
                  <el-icon v-if="route.meta?.icon">
                    <component :is="route.meta.icon" />
                  </el-icon>
                  <span>{{ route.meta?.title }}</span>
                </template>
                
                <el-menu-item
                  v-for="child in route.children"
                  :key="child.name"
                  :index="child.path"
                >
                  {{ child.meta?.title }}
                </el-menu-item>
              </el-sub-menu>
              
              <el-menu-item v-else :index="route.path">
                <el-icon v-if="route.meta?.icon">
                  <component :is="route.meta.icon" />
                </el-icon>
                <template #title>{{ route.meta?.title }}</template>
              </el-menu-item>
            </template>
          </el-menu>
        </div>
      </el-aside>

      <el-container>
        <!-- 顶部导航 -->
        <el-header class="header">
          <div class="header-left">
            <el-button
              type="text"
              @click="toggleSidebar"
              class="collapse-btn"
            >
              <el-icon>
                <Fold v-if="!isCollapsed" />
                <Expand v-else />
              </el-icon>
            </el-button>
            
            <!-- 面包屑 -->
            <el-breadcrumb separator="/" class="breadcrumb">
              <el-breadcrumb-item
                v-for="item in breadcrumbs"
                :key="item.path"
                :to="item.path"
              >
                {{ item.title }}
              </el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <!-- 用户信息 -->
            <el-dropdown @command="handleCommand">
              <div class="user-info">
                <el-avatar :src="userStore.userInfo?.avatar" size="small">
                  {{ userStore.userInfo?.nickname?.[0] || 'A' }}
                </el-avatar>
                <span class="username">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</span>
                <el-icon><ArrowDown /></el-icon>
              </div>
              
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                  <el-dropdown-item command="settings">设置</el-dropdown-item>
                  <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>

        <!-- 主要内容区域 -->
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapsed = ref(false)

// 计算属性
const sidebarWidth = computed(() => isCollapsed.value ? '64px' : '220px')
const activeMenu = computed(() => route.path)

// 获取菜单路由
const menuRoutes = computed(() => {
  return router.getRoutes()
    .filter(route => route.path === '/' && route.children)
    .flatMap(route => route.children || [])
    .filter(route => !route.meta?.hideInMenu)
    .map(route => ({
      ...route,
      children: route.children?.filter(child => !child.meta?.hideInMenu)
    }))
})

// 面包屑
const breadcrumbs = computed(() => {
  const matched = route.matched.filter(item => item.meta?.title)
  const breadcrumbs = []
  
  for (const item of matched) {
    if (item.meta?.title) {
      breadcrumbs.push({
        title: item.meta.title,
        path: item.path
      })
    }
  }
  
  return breadcrumbs
})

// 切换侧边栏
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

// 处理用户下拉菜单命令
const handleCommand = async (command: string) => {
  switch (command) {
    case 'profile':
      ElMessage.info('个人中心功能开发中...')
      break
    case 'settings':
      ElMessage.info('设置功能开发中...')
      break
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/login')
      } catch (error) {
        // 用户取消
      }
      break
  }
}
</script>

<style scoped lang="scss">
.layout {
  height: 100vh;
  overflow: hidden;
}

.sidebar {
  height: 100vh;
  background-color: #304156;
  overflow-y: auto;
  
  .logo {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #2b3a4b;
    color: white;
    
    .logo-img {
      width: 32px;
      height: 32px;
      margin-right: 8px;
    }
    
    .logo-text {
      font-size: 16px;
      font-weight: 600;
    }
  }
  
  .sidebar-menu {
    border: none;
    background-color: #304156;
    
    :deep(.el-menu-item) {
      color: #bfcbd9;
      
      &:hover {
        background-color: #263445;
        color: #409eff;
      }
      
      &.is-active {
        background-color: #409eff;
        color: white;
      }
    }
    
    :deep(.el-sub-menu) {
      .el-sub-menu__title {
        color: #bfcbd9;
        
        &:hover {
          background-color: #263445;
          color: #409eff;
        }
      }
    }
    
    :deep(.el-sub-menu .el-menu-item) {
      background-color: #1f2d3d;
      
      &:hover {
        background-color: #001528;
      }
      
      &.is-active {
        background-color: #409eff;
      }
    }
  }
}

.header {
  background-color: white;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  
  .header-left {
    display: flex;
    align-items: center;
    
    .collapse-btn {
      margin-right: 20px;
      font-size: 18px;
    }
    
    .breadcrumb {
      font-size: 14px;
    }
  }
  
  .header-right {
    .user-info {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 8px 12px;
      border-radius: 4px;
      transition: background-color 0.3s;
      
      &:hover {
        background-color: #f5f7fa;
      }
      
      .username {
        margin: 0 8px;
        font-size: 14px;
      }
    }
  }
}

.main {
  background-color: #f5f7fa;
  padding: 20px;
  overflow-y: auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
  }
  
  .header-left .breadcrumb {
    display: none;
  }
  
  .main {
    padding: 10px;
  }
}
</style>