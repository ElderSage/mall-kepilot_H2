<template>
  <div class="orderdetail-page">
    <van-nav-bar title="OrderDetail" left-arrow @click-left="$router.back()" />
    <div class="orderdetail-content">
      <van-empty description="OrderDetail功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// OrderDetail页面 - 待完善
</script>

<style scoped>
.orderdetail-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.orderdetail-content {
  padding: 50px 20px;
}
</style>
