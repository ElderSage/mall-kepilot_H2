// 通用API响应类型
export interface ApiResponse<T = any> {
  success: boolean
  message: string
  data: T
  code: number
}

// 分页请求参数
export interface PageRequest {
  page: number
  size: number
  sort?: string
  order?: 'asc' | 'desc'
}

// 分页响应类型
export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
  first: boolean
  last: boolean
}

// 管理员用户类型
export interface AdminUser {
  id: number
  username: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  role: string
  status: 'ACTIVE' | 'INACTIVE'
  lastLoginTime?: string
  createTime: string
  updateTime: string
}

// 登录请求
export interface LoginRequest {
  username: string
  password: string
  rememberMe?: boolean
}

// JWT认证响应
export interface JwtAuthenticationResponse {
  accessToken: string
  tokenType: string
  expiresIn: number
  userInfo: AdminUser
  permissions: string[]
}

// 商品相关类型
export interface Product {
  id: number
  name: string
  subtitle?: string
  mainImage: string
  subImages?: string
  detail?: string
  price: number
  originalPrice?: number
  stock: number
  sales: number
  status: 'ON_SALE' | 'OFF_SALE' | 'SOLD_OUT'
  categoryId: number
  category?: Category
  weight?: number
  unit?: string
  keywords?: string
  description?: string
  isHot: boolean
  isNew: boolean
  isRecommend: boolean
  videoUrl?: string
  skus?: ProductSku[]
  attributes?: ProductAttribute[]
  createTime: string
  updateTime: string
}

export interface ProductSku {
  id: number
  skuCode: string
  name: string
  price: number
  originalPrice?: number
  stock: number
  sales: number
  image?: string
  weight?: number
  attributes: string // JSON格式
  status: 'ACTIVE' | 'INACTIVE'
}

export interface ProductAttribute {
  id: number
  name: string
  value: string
  type: 'TEXT' | 'NUMBER' | 'COLOR' | 'SIZE' | 'IMAGE'
  sortOrder: number
}

export interface Category {
  id: number
  name: string
  parentId?: number
  level: number
  sortOrder: number
  icon?: string
  image?: string
  keywords?: string
  description?: string
  isShow: boolean
  children?: Category[]
  createTime: string
  updateTime: string
}

// 订单相关类型
export interface Order {
  id: number
  orderNo: string
  userId: number
  user?: User
  status: OrderStatus
  totalAmount: number
  discountAmount: number
  shippingFee: number
  actualAmount: number
  paymentMethod?: PaymentMethod
  paymentTime?: string
  shippingTime?: string
  receiveTime?: string
  closeTime?: string
  receiverName: string
  receiverPhone: string
  receiverAddress: string
  receiverProvince: string
  receiverCity: string
  receiverDistrict: string
  receiverZip?: string
  logisticsCompany?: string
  logisticsNo?: string
  remark?: string
  couponId?: number
  pointsUsed: number
  orderItems: OrderItem[]
  createTime: string
  updateTime: string
}

export interface OrderItem {
  id: number
  productId: number
  productName: string
  productImage: string
  productPrice: number
  quantity: number
  totalPrice: number
  productAttributes?: string
}

export type OrderStatus = 
  | 'PENDING_PAYMENT'
  | 'PAID'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'RECEIVED'
  | 'COMPLETED'
  | 'CANCELLED'
  | 'REFUNDING'
  | 'REFUNDED'

export type PaymentMethod = 'WECHAT' | 'ALIPAY' | 'BANK_CARD' | 'BALANCE'

// 用户类型
export interface User {
  id: number
  username: string
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  gender?: 'MALE' | 'FEMALE' | 'UNKNOWN'
  memberLevel: string
  growthValue: number
  points: number
  balance: number
  status: 'ACTIVE' | 'INACTIVE' | 'BANNED'
  createTime: string
  updateTime: string
}

// 优惠券类型
export interface Coupon {
  id: number
  name: string
  description?: string
  type: 'FIXED_AMOUNT' | 'PERCENTAGE' | 'NO_THRESHOLD'
  discountAmount?: number
  discountRate?: number
  minAmount?: number
  maxDiscount?: number
  startTime: string
  endTime: string
  totalCount?: number
  usedCount: number
  perLimit: number
  status: 'ACTIVE' | 'INACTIVE' | 'EXPIRED'
  categoryIds?: string
  productIds?: string
  createTime: string
  updateTime: string
}

// 统计数据类型
export interface DashboardStats {
  todaySales: number
  todayOrders: number
  todayUsers: number
  totalProducts: number
  totalUsers: number
  totalOrders: number
  salesTrend: Array<{
    date: string
    sales: number
    orders: number
  }>
  orderStatusStats: Array<{
    status: string
    count: number
  }>
  categoryStats: Array<{
    name: string
    sales: number
    count: number
  }>
}

// 搜索参数
export interface SearchParams {
  keyword?: string
  status?: string
  categoryId?: number
  startDate?: string
  endDate?: string
  [key: string]: any
}

// 表格列配置
export interface TableColumn {
  prop: string
  label: string
  width?: number | string
  minWidth?: number | string
  fixed?: boolean | 'left' | 'right'
  sortable?: boolean
  formatter?: (row: any, column: any, cellValue: any) => string
  type?: 'selection' | 'index' | 'expand'
}

// 表单项配置
export interface FormItem {
  prop: string
  label: string
  type: 'input' | 'select' | 'date' | 'datetime' | 'textarea' | 'number' | 'switch' | 'upload' | 'editor'
  required?: boolean
  placeholder?: string
  options?: Array<{ label: string; value: any }>
  rules?: any[]
  span?: number
  [key: string]: any
}