// 通用API响应类型
export interface ApiResponse<T = any> {
  success: boolean
  message: string
  data: T
  code: number
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

// 用户相关类型
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
  createTime: string
  updateTime: string
}

export interface LoginRequest {
  username: string
  password: string
  rememberMe?: boolean
}

export interface RegisterRequest {
  username: string
  password: string
  confirmPassword: string
  phone?: string
  email?: string
  smsCode?: string
}

export interface JwtAuthenticationResponse {
  accessToken: string
  refreshToken: string
  tokenType: string
  expiresIn: number
  userInfo: User
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
  category: Category
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
}

// 购物车相关类型
export interface CartItem {
  id: number
  product: Product
  productSku?: ProductSku
  quantity: number
  checked: boolean
  createTime: string
  updateTime: string
}

// 订单相关类型
export interface Order {
  id: number
  orderNo: string
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

// 优惠券相关类型
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
}

export interface UserCoupon {
  id: number
  coupon: Coupon
  status: 'UNUSED' | 'USED' | 'EXPIRED'
  useTime?: string
  createTime: string
}

// 地址相关类型
export interface Address {
  id: number
  receiverName: string
  receiverPhone: string
  province: string
  city: string
  district: string
  detailAddress: string
  zipCode?: string
  isDefault: boolean
  createTime: string
  updateTime: string
}

// 搜索相关类型
export interface SearchParams {
  keyword?: string
  categoryId?: number
  minPrice?: number
  maxPrice?: number
  sortBy?: 'default' | 'price_asc' | 'price_desc' | 'sales'
  page?: number
  size?: number
}

// 轮播图类型
export interface Banner {
  id: number
  title: string
  image: string
  link?: string
  linkType: 'PRODUCT' | 'CATEGORY' | 'URL' | 'NONE'
  sortOrder: number
  isShow: boolean
}

// 通知类型
export interface Notice {
  id: number
  title: string
  content: string
  type: 'SYSTEM' | 'ORDER' | 'PROMOTION'
  isRead: boolean
  createTime: string
}