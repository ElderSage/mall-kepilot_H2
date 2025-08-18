<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-cards">
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <div class="stat-card">
          <div class="stat-icon sales">
            <el-icon><TrendCharts /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">¥{{ formatNumber(stats.todaySales) }}</div>
            <div class="stat-label">今日销售额</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <div class="stat-card">
          <div class="stat-icon orders">
            <el-icon><List /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.todayOrders }}</div>
            <div class="stat-label">今日订单</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <div class="stat-card">
          <div class="stat-icon users">
            <el-icon><User /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.todayUsers }}</div>
            <div class="stat-label">新增用户</div>
          </div>
        </div>
      </el-col>
      
      <el-col :xs="12" :sm="6" :md="6" :lg="6" :xl="6">
        <div class="stat-card">
          <div class="stat-icon products">
            <el-icon><Goods /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stats.totalProducts }}</div>
            <div class="stat-label">商品总数</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="charts-row">
      <!-- 销售趋势图 -->
      <el-col :xs="24" :sm="24" :md="16" :lg="16" :xl="16">
        <div class="chart-card">
          <div class="chart-header">
            <h3>销售趋势</h3>
            <el-radio-group v-model="trendPeriod" size="small">
              <el-radio-button label="7">近7天</el-radio-button>
              <el-radio-button label="30">近30天</el-radio-button>
            </el-radio-group>
          </div>
          <div class="chart-content">
            <v-chart
              :option="salesTrendOption"
              :style="{ height: '300px' }"
              autoresize
            />
          </div>
        </div>
      </el-col>
      
      <!-- 订单状态分布 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8" :xl="8">
        <div class="chart-card">
          <div class="chart-header">
            <h3>订单状态分布</h3>
          </div>
          <div class="chart-content">
            <v-chart
              :option="orderStatusOption"
              :style="{ height: '300px' }"
              autoresize
            />
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 数据表格区域 -->
    <el-row :gutter="20" class="tables-row">
      <!-- 热销商品 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <div class="table-card">
          <div class="table-header">
            <h3>热销商品 TOP10</h3>
            <el-button type="text" @click="goToProducts">查看更多</el-button>
          </div>
          <el-table :data="hotProducts" style="width: 100%">
            <el-table-column prop="name" label="商品名称" show-overflow-tooltip />
            <el-table-column prop="sales" label="销量" width="80" />
            <el-table-column prop="price" label="价格" width="80">
              <template #default="{ row }">
                ¥{{ row.price }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
      
      <!-- 最新订单 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
        <div class="table-card">
          <div class="table-header">
            <h3>最新订单</h3>
            <el-button type="text" @click="goToOrders">查看更多</el-button>
          </div>
          <el-table :data="recentOrders" style="width: 100%">
            <el-table-column prop="orderNo" label="订单号" show-overflow-tooltip />
            <el-table-column prop="actualAmount" label="金额" width="80">
              <template #default="{ row }">
                ¥{{ row.actualAmount }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="getOrderStatusType(row.status)" size="small">
                  {{ getOrderStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { LineChart, PieChart } from 'echarts/charts'
import {
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
} from 'echarts/components'
import VChart from 'vue-echarts'
import type { DashboardStats, Product, Order } from '@/types'

// 注册 ECharts 组件
use([
  CanvasRenderer,
  LineChart,
  PieChart,
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  GridComponent
])

const router = useRouter()

// 响应式数据
const trendPeriod = ref('7')
const stats = ref<DashboardStats>({
  todaySales: 0,
  todayOrders: 0,
  todayUsers: 0,
  totalProducts: 0,
  totalUsers: 0,
  totalOrders: 0,
  salesTrend: [],
  orderStatusStats: [],
  categoryStats: []
})
const hotProducts = ref<Product[]>([])
const recentOrders = ref<Order[]>([])

// 销售趋势图配置
const salesTrendOption = computed(() => ({
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    data: ['销售额', '订单数']
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true
  },
  xAxis: {
    type: 'category',
    data: stats.value.salesTrend.map(item => item.date)
  },
  yAxis: [
    {
      type: 'value',
      name: '销售额',
      position: 'left'
    },
    {
      type: 'value',
      name: '订单数',
      position: 'right'
    }
  ],
  series: [
    {
      name: '销售额',
      type: 'line',
      data: stats.value.salesTrend.map(item => item.sales),
      smooth: true,
      itemStyle: {
        color: '#409EFF'
      }
    },
    {
      name: '订单数',
      type: 'line',
      yAxisIndex: 1,
      data: stats.value.salesTrend.map(item => item.orders),
      smooth: true,
      itemStyle: {
        color: '#67C23A'
      }
    }
  ]
}))

// 订单状态分布图配置
const orderStatusOption = computed(() => ({
  tooltip: {
    trigger: 'item',
    formatter: '{a} <br/>{b}: {c} ({d}%)'
  },
  legend: {
    orient: 'vertical',
    left: 'left'
  },
  series: [
    {
      name: '订单状态',
      type: 'pie',
      radius: '50%',
      data: stats.value.orderStatusStats.map(item => ({
        name: getOrderStatusText(item.status),
        value: item.count
      })),
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }
  ]
}))

// 格式化数字
const formatNumber = (num: number): string => {
  if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'k'
  }
  return num.toString()
}

// 获取订单状态类型
const getOrderStatusType = (status: string): 'primary' | 'success' | 'warning' | 'info' | 'danger' => {
  const statusMap: Record<string, 'primary' | 'success' | 'warning' | 'info' | 'danger'> = {
    'PENDING_PAYMENT': 'warning',
    'PAID': 'primary',
    'SHIPPED': 'info',
    'DELIVERED': 'success',
    'COMPLETED': 'success',
    'CANCELLED': 'danger',
    'REFUNDED': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取订单状态文本
const getOrderStatusText = (status: string): string => {
  const statusMap: Record<string, string> = {
    'PENDING_PAYMENT': '待付款',
    'PAID': '已付款',
    'SHIPPED': '已发货',
    'DELIVERED': '已送达',
    'COMPLETED': '已完成',
    'CANCELLED': '已取消',
    'REFUNDED': '已退款'
  }
  return statusMap[status] || status
}

// 跳转到商品管理
const goToProducts = () => {
  router.push('/products/list')
}

// 跳转到订单管理
const goToOrders = () => {
  router.push('/orders/list')
}

// 获取仪表盘数据
const fetchDashboardData = async () => {
  try {
    // 模拟数据
    stats.value = {
      todaySales: 125680,
      todayOrders: 156,
      todayUsers: 23,
      totalProducts: 1250,
      totalUsers: 8960,
      totalOrders: 15680,
      salesTrend: Array.from({ length: 7 }, (_, i) => ({
        date: new Date(Date.now() - (6 - i) * 24 * 60 * 60 * 1000).toLocaleDateString(),
        sales: Math.floor(Math.random() * 50000) + 80000,
        orders: Math.floor(Math.random() * 100) + 100
      })),
      orderStatusStats: [
        { status: 'PENDING_PAYMENT', count: 45 },
        { status: 'PAID', count: 78 },
        { status: 'SHIPPED', count: 123 },
        { status: 'COMPLETED', count: 234 }
      ],
      categoryStats: []
    }

    // 模拟热销商品
    hotProducts.value = Array.from({ length: 10 }, (_, i) => ({
      id: i + 1,
      name: `热销商品 ${i + 1}`,
      price: Math.floor(Math.random() * 500) + 50,
      sales: Math.floor(Math.random() * 1000) + 100,
      stock: Math.floor(Math.random() * 100) + 10,
      status: 'ON_SALE' as const,
      mainImage: '',
      subtitle: '',
      originalPrice: 0,
      categoryId: 1,
      isHot: true,
      isNew: false,
      isRecommend: false,
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString()
    }))

    // 模拟最新订单
    recentOrders.value = Array.from({ length: 10 }, (_, i) => ({
      id: i + 1,
      orderNo: `ORD${Date.now()}${i}`,
      userId: i + 1,
      status: ['PENDING_PAYMENT', 'PAID', 'SHIPPED', 'COMPLETED'][Math.floor(Math.random() * 4)] as any,
      totalAmount: Math.floor(Math.random() * 1000) + 100,
      discountAmount: 0,
      shippingFee: 10,
      actualAmount: Math.floor(Math.random() * 1000) + 100,
      receiverName: `用户${i + 1}`,
      receiverPhone: '13800138000',
      receiverAddress: '测试地址',
      receiverProvince: '广东省',
      receiverCity: '深圳市',
      receiverDistrict: '南山区',
      pointsUsed: 0,
      orderItems: [],
      createTime: new Date().toISOString(),
      updateTime: new Date().toISOString()
    }))

  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
  }
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped lang="scss">
.dashboard {
  .stats-cards {
    margin-bottom: 20px;
  }
  
  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;
    
    &:hover {
      transform: translateY(-2px);
    }
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      font-size: 24px;
      color: white;
      
      &.sales {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }
      
      &.orders {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
      
      &.users {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
      
      &.products {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }
    
    .stat-content {
      flex: 1;
      
      .stat-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
  
  .charts-row {
    margin-bottom: 20px;
  }
  
  .chart-card,
  .table-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    
    .chart-header,
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      h3 {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        margin: 0;
      }
    }
  }
  
  .tables-row {
    .table-card {
      height: 400px;
      
      :deep(.el-table) {
        height: calc(100% - 60px);
      }
    }
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .dashboard {
    .stat-card {
      padding: 15px;
      
      .stat-icon {
        width: 50px;
        height: 50px;
        font-size: 20px;
        margin-right: 12px;
      }
      
      .stat-value {
        font-size: 20px;
      }
    }
    
    .chart-card,
    .table-card {
      padding: 15px;
      margin-bottom: 15px;
    }
  }
}
</style>