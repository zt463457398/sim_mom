<template>
  <div class="welcome-container">
    <h1>欢迎使用MOM系统</h1>
    <p>当前登录用户：{{ realName || username }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = ref('')
const realName = ref('')

onMounted(() => {
  // 检查是否已登录
  const token = localStorage.getItem('token')
  if (!token) {
    router.push('/login')
    return
  }
  // 从localStorage获取用户信息
  username.value = localStorage.getItem('username') || ''
  realName.value = localStorage.getItem('realName') || ''
})
</script>

<style scoped>
.welcome-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
}

h1 {
  color: #409eff;
  margin-bottom: 20px;
}
</style> 