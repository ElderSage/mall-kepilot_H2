<template>
  <div class="search-page">
    <van-nav-bar title="搜索" left-arrow @click-left="$router.back()" />
    <div class="search-content">
      <van-empty description="搜索功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// 搜索页面 - 待完善
</script>

<style scoped>
.search-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.search-content {
  padding: 50px 20px;
}
</style>