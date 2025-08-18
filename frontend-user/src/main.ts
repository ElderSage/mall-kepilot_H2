import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

// 样式
import './assets/styles/main.css'
import 'vant/lib/index.css'

// Vant Touch 模拟器（用于桌面端调试）
import '@vant/touch-emulator'

// 懒加载
import VueLazyload from 'vue3-lazyload'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(VueLazyload, {
  loading: '/images/loading.png',
  error: '/images/error.png'
})

app.mount('#app')