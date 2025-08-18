<template>
  <div class="settings-page">
    <van-nav-bar title="Settings" left-arrow @click-left="$router.back()" />
    <div class="settings-content">
      <van-empty description="Settings功能开发中..." />
    </div>
  </div>
</template>

<script setup lang="ts">
// Settings页面 - 待完善
</script>

<style scoped>
.settings-page {
  min-height: 100vh;
  background-color: #f8fafc;
}
.settings-content {
  padding: 50px 20px;
}
</style>
