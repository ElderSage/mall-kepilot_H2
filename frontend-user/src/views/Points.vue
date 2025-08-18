<template>
  <div class="points-page">
    <van-nav-bar title="Points" left-arrow @click-left="$router.back()" />
    <div class="points-content">
      <van-empty description="Points功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// Points页面 - 待完善
</script>

<style scoped>
.points-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.points-content {
  padding: 50px 20px;
}
</style>
