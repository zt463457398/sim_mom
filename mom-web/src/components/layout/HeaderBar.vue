<template>
  <div class="header-container">
    <div class="header-left">
      <!-- 预留左侧空间，后续可添加面包屑等 -->
    </div>
    <div class="header-right">
      <div class="server-time">
        <el-icon><Timer /></el-icon>
        <span class="time-text">{{ currentTime }}</span>
      </div>
      <el-divider direction="vertical" />
      <el-dropdown @command="handleCommand">
        <span class="user-info">
          <el-avatar :size="32" :src="userInfo.avatar || defaultAvatar" />
          <span class="username">{{ userInfo.realName || userInfo.username }}</span>
          <el-icon class="el-icon--right"><CaretBottom /></el-icon>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">个人信息</el-dropdown-item>
            <el-dropdown-item command="changePwd">修改密码</el-dropdown-item>
            <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { CaretBottom, Timer } from '@element-plus/icons-vue'

const router = useRouter()
const userInfo = ref({})
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
const currentTime = ref('')
let timer = null

// 格式化时间
const formatTime = (date) => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  const week = ['日', '一', '二', '三', '四', '五', '六'][date.getDay()]
  
  return `${year}年${month}月${day}日 星期${week} ${hours}:${minutes}:${seconds}`
}

// 更新时间
const updateTime = () => {
  currentTime.value = formatTime(new Date())
}

onMounted(() => {
  const storedUserInfo = localStorage.getItem('userInfo')
  if (storedUserInfo) {
    userInfo.value = JSON.parse(storedUserInfo)
  }
  
  // 初始化时间并启动定时器
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onBeforeUnmount(() => {
  // 组件销毁前清除定时器
  if (timer) {
    clearInterval(timer)
  }
})

const handleCommand = (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'changePwd':
      router.push('/change-password')
      break
    case 'logout':
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
      router.push('/login')
      ElMessage.success('退出登录成功')
      break
  }
}
</script>

<style scoped>
.header-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  padding-right: 20px;
}

.server-time {
  display: flex;
  align-items: center;
  margin-right: 8px;
  color: #666;
  font-size: 14px;
}

.time-text {
  margin-left: 5px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin: 0 8px;
  font-size: 14px;
  color: #333;
}

.el-dropdown {
  display: flex;
  align-items: center;
}

:deep(.el-divider--vertical) {
  height: 20px;
  margin: 0 15px;
}
</style> 