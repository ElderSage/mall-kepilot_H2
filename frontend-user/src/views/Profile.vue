<template>
  <div class="profile-page">
    <div class="profile-header">
      <div class="user-info">
        <van-image
          :src="userStore.userInfo?.avatar || '/images/default-avatar.png'"
          class="avatar"
          round
        />
        <div class="user-details">
          <h3 class="username">{{ userStore.userInfo?.nickname || userStore.userInfo?.username }}</h3>
          <p class="member-level">{{ userStore.memberLevel }}</p>
        </div>
      </div>
      
      <div class="user-stats">
        <div class="stat-item">
          <span class="stat-value">{{ userStore.points }}</span>
          <span class="stat-label">积分</span>
        </div>
        <div class="stat-item">
          <span class="stat-value">{{ userStore.growthValue }}</span>
          <span class="stat-label">成长值</span>
        </div>
      </div>
    </div>

    <div class="menu-section">
      <van-cell-group>
        <van-cell title="我的订单" is-link to="/orders" icon="orders-o" />
        <van-cell title="收货地址" is-link to="/address" icon="location-o" />
        <van-cell title="优惠券" is-link to="/coupons" icon="coupon-o" />
        <van-cell title="积分明细" is-link to="/points" icon="gold-coin-o" />
        <van-cell title="设置" is-link to="/settings" icon="setting-o" />
      </van-cell-group>
    </div>

    <div class="logout-section">
      <van-button type="danger" block @click="onLogout">
        退出登录
      </van-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '@/stores/user'
import { useRouter } from 'vue-router'
import { showConfirmDialog, showToast } from 'vant'

const userStore = useUserStore()
const router = useRouter()

const onLogout = async () => {
  try {
    await showConfirmDialog({
      title: '确认退出',
      message: '确定要退出登录吗？'
    })
    
    await userStore.logout()
    showToast('已退出登录')
    router.replace('/home')
  } catch (error) {
    // 用户取消
  }
}
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background-color: #f8fafc;
}

.profile-header {
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  padding: 20px;
  color: white;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.avatar {
  width: 60px;
  height: 60px;
  margin-right: 16px;
}

.username {
  font-size: 18px;
  font-weight: 600;
  margin: 0 0 4px 0;
}

.member-level {
  font-size: 14px;
  opacity: 0.8;
  margin: 0;
}

.user-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 20px;
  font-weight: 600;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
  margin-top: 4px;
}

.menu-section {
  margin: 16px 0;
}

.logout-section {
  padding: 20px;
}
</style>