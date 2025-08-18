<template>
  <div class="checkout-page">
    <van-nav-bar title="Checkout" left-arrow @click-left="$router.back()" />
    <div class="checkout-content">
      <van-empty description="Checkout功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// Checkout页面 - 待完善
</script>

<style scoped>
.checkout-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.checkout-content {
  padding: 50px 20px;
}
</style>
