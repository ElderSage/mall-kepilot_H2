<template>
  <div class="coupons-page">
    <van-nav-bar title="Coupons" left-arrow @click-left="$router.back()" />
    <div class="coupons-content">
      <van-empty description="Coupons功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// Coupons页面 - 待完善
</script>

<style scoped>
.coupons-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.coupons-content {
  padding: 50px 20px;
}
</style>
