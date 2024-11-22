import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import axios from 'axios'
import router from './router'

const app = createApp(App)

// 配置axios
axios.defaults.baseURL = 'http://localhost:8080'
app.config.globalProperties.$http = axios

app.use(ElementPlus)
app.use(router)
app.mount('#app')
