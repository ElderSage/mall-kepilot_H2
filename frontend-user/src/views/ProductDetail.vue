<template>
  <div class="productdetail-page">
    <van-nav-bar title="ProductDetail" left-arrow @click-left="$router.back()" />
    <div class="productdetail-content">
      <van-empty description="ProductDetail功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// ProductDetail页面 - 待完善
</script>

<style scoped>
.productdetail-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.productdetail-content {
  padding: 50px 20px;
}
</style>
