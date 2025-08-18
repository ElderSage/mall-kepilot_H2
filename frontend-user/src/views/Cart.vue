<template>
  <div class="cart-page">
    <van-nav-bar title="购物车" />
    
    <div class="cart-content">
      <div class="empty-cart">
        <van-empty description="购物车是空的">
          <van-button type="primary" @click="$router.push('/home')">
            去逛逛
          </van-button>
        </van-empty>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 购物车页面 - 待完善
</script>

<style scoped>
.cart-page {
  min-height: 100vh;
  background-color: #f8fafc;
}

.cart-content {
  padding: 20px;
}

.empty-cart {
  margin-top: 100px;
}
</style>