import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', hideInMenu: true }
  },
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layouts/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'DataAnalysis' }
      },
      {
        path: '/products',
        name: 'Products',
        component: () => import('@/views/Products/index.vue'),
        meta: { title: '商品管理', icon: 'Goods' },
        children: [
          {
            path: '/products/list',
            name: 'ProductList',
            component: () => import('@/views/Products/ProductList.vue'),
            meta: { title: '商品列表', parent: 'Products' }
          },
          {
            path: '/products/add',
            name: 'ProductAdd',
            component: () => import('@/views/Products/ProductForm.vue'),
            meta: { title: '添加商品', parent: 'Products', hideInMenu: true }
          },
          {
            path: '/products/edit/:id',
            name: 'ProductEdit',
            component: () => import('@/views/Products/ProductForm.vue'),
            meta: { title: '编辑商品', parent: 'Products', hideInMenu: true }
          },
          {
            path: '/products/categories',
            name: 'Categories',
            component: () => import('@/views/Products/Categories.vue'),
            meta: { title: '商品分类', parent: 'Products' }
          }
        ]
      },
      {
        path: '/orders',
        name: 'Orders',
        component: () => import('@/views/Orders/index.vue'),
        meta: { title: '订单管理', icon: 'List' },
        children: [
          {
            path: '/orders/list',
            name: 'OrderList',
            component: () => import('@/views/Orders/OrderList.vue'),
            meta: { title: '订单列表', parent: 'Orders' }
          },
          {
            path: '/orders/detail/:id',
            name: 'OrderDetail',
            component: () => import('@/views/Orders/OrderDetail.vue'),
            meta: { title: '订单详情', parent: 'Orders', hideInMenu: true }
          }
        ]
      },
      {
        path: '/users',
        name: 'Users',
        component: () => import('@/views/Users/index.vue'),
        meta: { title: '用户管理', icon: 'User' },
        children: [
          {
            path: '/users/list',
            name: 'UserList',
            component: () => import('@/views/Users/UserList.vue'),
            meta: { title: '用户列表', parent: 'Users' }
          }
        ]
      },
      {
        path: '/marketing',
        name: 'Marketing',
        component: () => import('@/views/Marketing/index.vue'),
        meta: { title: '营销管理', icon: 'Present' },
        children: [
          {
            path: '/marketing/coupons',
            name: 'Coupons',
            component: () => import('@/views/Marketing/Coupons.vue'),
            meta: { title: '优惠券管理', parent: 'Marketing' }
          },
          {
            path: '/marketing/activities',
            name: 'Activities',
            component: () => import('@/views/Marketing/Activities.vue'),
            meta: { title: '活动管理', parent: 'Marketing' }
          }
        ]
      },
      {
        path: '/statistics',
        name: 'Statistics',
        component: () => import('@/views/Statistics/index.vue'),
        meta: { title: '数据统计', icon: 'TrendCharts' },
        children: [
          {
            path: '/statistics/sales',
            name: 'SalesStatistics',
            component: () => import('@/views/Statistics/Sales.vue'),
            meta: { title: '销售统计', parent: 'Statistics' }
          },
          {
            path: '/statistics/users',
            name: 'UserStatistics',
            component: () => import('@/views/Statistics/Users.vue'),
            meta: { title: '用户统计', parent: 'Statistics' }
          }
        ]
      },
      {
        path: '/system',
        name: 'System',
        component: () => import('@/views/System/index.vue'),
        meta: { title: '系统管理', icon: 'Setting' },
        children: [
          {
            path: '/system/settings',
            name: 'SystemSettings',
            component: () => import('@/views/System/Settings.vue'),
            meta: { title: '系统设置', parent: 'System' }
          }
        ]
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面不存在', hideInMenu: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 在线商城管理系统`
  }
  
  // 检查是否需要登录
  if (to.name !== 'Login' && !userStore.isLoggedIn) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.name === 'Login' && userStore.isLoggedIn) {
    next({ name: 'Dashboard' })
  } else {
    next()
  }
})

export default router