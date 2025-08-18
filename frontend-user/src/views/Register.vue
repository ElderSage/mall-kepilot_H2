<template>
  <div class="register-page">
    <van-nav-bar
      title="注册"
      left-arrow
      @click-left="$router.back()"
    />

    <div class="register-container">
      <van-form @submit="onSubmit" class="register-form">
        <van-cell-group inset>
          <van-field
            v-model="form.username"
            name="username"
            label="用户名"
            placeholder="请输入用户名"
            left-icon="user-o"
            :rules="[{ required: true, message: '请输入用户名' }]"
            clearable
          />
          
          <van-field
            v-model="form.phone"
            name="phone"
            label="手机号"
            placeholder="请输入手机号"
            left-icon="phone-o"
            :rules="[
              { required: true, message: '请输入手机号' },
              { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' }
            ]"
            clearable
          />
          
          <van-field
            v-model="form.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入密码"
            left-icon="lock"
            :rules="[
              { required: true, message: '请输入密码' },
              { min: 6, message: '密码至少6位' }
            ]"
            clearable
          />
          
          <van-field
            v-model="form.confirmPassword"
            type="password"
            name="confirmPassword"
            label="确认密码"
            placeholder="请再次输入密码"
            left-icon="lock"
            :rules="[
              { required: true, message: '请确认密码' },
              { validator: validateConfirmPassword }
            ]"
            clearable
          />
        </van-cell-group>

        <div class="form-actions">
          <van-button
            round
            block
            type="primary"
            native-type="submit"
            :loading="loading"
            loading-text="注册中..."
            size="large"
          >
            注册
          </van-button>
        </div>
      </van-form>

      <div class="login-link">
        <span>已有账号？</span>
        <van-button
          type="primary"
          size="small"
          plain
          @click="goToLogin"
        >
          立即登录
        </van-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { showToast } from 'vant'
import { useUserStore } from '@/stores/user'
import type { RegisterRequest } from '@/types'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)

const form = reactive<RegisterRequest>({
  username: '',
  phone: '',
  password: '',
  confirmPassword: ''
})

// 验证确认密码
const validateConfirmPassword = (value: string) => {
  if (value !== form.password) {
    return '两次输入的密码不一致'
  }
  return true
}

// 提交注册
const onSubmit = async () => {
  try {
    loading.value = true
    
    const result = await userStore.register(form)
    
    if (result.success) {
      showToast('注册成功')
      router.replace('/login')
    } else {
      showToast(result.message)
    }
  } catch (error: any) {
    showToast(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-container {
  padding: 20px;
  min-height: calc(100vh - 46px);
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.register-form {
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

.form-actions {
  margin-bottom: 30px;
}

:deep(.van-button--large) {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
}

.login-link {
  text-align: center;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

.login-link .van-button {
  margin-left: 8px;
}
</style>