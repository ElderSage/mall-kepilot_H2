<template>
  <div class="address-page">
    <van-nav-bar title="Address" left-arrow @click-left="$router.back()" />
    <div class="address-content">
      <van-empty description="Address功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// Address页面 - 待完善
</script>

<style scoped>
.address-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.address-content {
  padding: 50px 20px;
}
</style>
