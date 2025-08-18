<template>
  <div class="orders-page">
    <van-nav-bar title="Orders" left-arrow @click-left="$router.back()" />
    <div class="orders-content">
      <van-empty description="Orders功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// Orders页面 - 待完善
</script>

<style scoped>
.orders-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.orders-content {
  padding: 50px 20px;
}
</style>
