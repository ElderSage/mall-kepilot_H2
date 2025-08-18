<template>
  <div class="login-page">
    <!-- 顶部导航 -->
    <van-nav-bar
      title="登录"
      left-arrow
      @click-left="$router.back()"
    />

    <div class="login-container">
      <!-- Logo -->
      <div class="logo-section">
        <div class="logo">
          <van-icon name="shop-o" size="48" color="#3b82f6" />
        </div>
        <h1 class="app-name">在线商城</h1>
        <p class="app-slogan">优质商品，贴心服务</p>
      </div>

      <!-- 登录表单 -->
      <van-form @submit="onSubmit" class="login-form">
        <van-cell-group inset>
          <van-field
            v-model="form.username"
            name="username"
            label="用户名"
            placeholder="请输入用户名/手机号/邮箱"
            left-icon="user-o"
            :rules="[{ required: true, message: '请输入用户名' }]"
            clearable
          />
          
          <van-field
            v-model="form.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入密码"
            left-icon="lock"
            :rules="[{ required: true, message: '请输入密码' }]"
            clearable
          />
        </van-cell-group>

        <!-- 记住我和忘记密码 -->
        <div class="form-options">
          <van-checkbox v-model="form.rememberMe">
            记住我
          </van-checkbox>
          <van-button
            type="primary"
            size="small"
            plain
            @click="goToForgotPassword"
          >
            忘记密码？
          </van-button>
        </div>

        <!-- 登录按钮 -->
        <div class="form-actions">
          <van-button
            round
            block
            type="primary"
            native-type="submit"
            :loading="loading"
            loading-text="登录中..."
            size="large"
          >
            登录
          </van-button>
        </div>
      </van-form>

      <!-- 第三方登录 -->
      <div class="third-party-login">
        <div class="divider">
          <span>其他登录方式</span>
        </div>
        
        <div class="third-party-buttons">
          <van-button
            icon="wechat"
            type="success"
            round
            @click="wechatLogin"
          >
            微信登录
          </van-button>
          
          <van-button
            icon="alipay"
            type="primary"
            round
            @click="alipayLogin"
          >
            支付宝登录
          </van-button>
        </div>
      </div>

      <!-- 注册链接 -->
      <div class="register-link">
        <span>还没有账号？</span>
        <van-button
          type="primary"
          size="small"
          plain
          @click="goToRegister"
        >
          立即注册
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '@/stores/user'
import type { LoginRequest } from '@/types'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)

// 表单数据
const form = reactive<LoginRequest>({
  username: '',
  password: '',
  rememberMe: false
})

// 提交登录
const onSubmit = async () => {
  try {
    loading.value = true
    
    const result = await userStore.login(form)
    
    if (result.success) {
      showToast('登录成功')
      
      // 跳转到原来的页面或首页
      const redirect = route.query.redirect as string
      router.replace(redirect || '/home')
    } else {
      showToast(result.message)
    }
  } catch (error: any) {
    showToast(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register')
}

// 跳转到忘记密码页面
const goToForgotPassword = () => {
  showToast('功能开发中...')
}

// 微信登录
const wechatLogin = () => {
  showToast('微信登录功能开发中...')
}

// 支付宝登录
const alipayLogin = () => {
  showToast('支付宝登录功能开发中...')
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-container {
  padding: 20px;
  min-height: calc(100vh - 46px);
  display: flex;
  flex-direction: column;
  justify-content: center;
}

/* Logo区域 */
.logo-section {
  text-align: center;
  margin-bottom: 40px;
}

.logo {
  width: 80px;
  height: 80px;
  background-color: white;
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}

.app-name {
  font-size: 28px;
  font-weight: 700;
  color: white;
  margin: 0 0 8px 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.app-slogan {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0;
}

/* 登录表单 */
.login-form {
  margin-bottom: 30px;
}

:deep(.van-cell-group--inset) {
  margin: 0 0 20px 0;
  border-radius: 12px;
  overflow: hidden;
}

:deep(.van-field__left-icon) {
  color: #3b82f6;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding: 0 4px;
}

:deep(.van-checkbox) {
  color: white;
}

:deep(.van-checkbox__icon--checked .van-icon) {
  background-color: #3b82f6;
  border-color: #3b82f6;
}

.form-actions {
  margin-bottom: 30px;
}

:deep(.van-button--large) {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

/* 第三方登录 */
.third-party-login {
  margin-bottom: 30px;
}

.divider {
  position: relative;
  text-align: center;
  margin-bottom: 20px;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background-color: rgba(255, 255, 255, 0.3);
}

.divider span {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: rgba(255, 255, 255, 0.8);
  padding: 0 16px;
  font-size: 12px;
  position: relative;
  z-index: 1;
}

.third-party-buttons {
  display: flex;
  gap: 12px;
}

.third-party-buttons .van-button {
  flex: 1;
}

/* 注册链接 */
.register-link {
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

.register-link .van-button {
  margin-left: 8px;
}

/* 响应式设计 */
@media (max-width: 640px) {
  .login-container {
    padding: 16px;
  }
  
  .logo {
    width: 70px;
    height: 70px;
  }
  
  .app-name {
    font-size: 24px;
  }
  
  .third-party-buttons {
    flex-direction: column;
  }
}
</style>