<template>
  <div class="header-container">
    <div class="header-left">
      <!-- 预留空间，可以添加其他功能按钮 -->
    </div>
    <div class="header-right">
      <div class="server-time">
        <el-icon><Timer /></el-icon>
        <span class="time-text">{{ currentTime }}</span>
      </div>
      <el-divider direction="vertical" />
      <el-dropdown @command="handleCommand" trigger="click">
        <div class="user-info">
          <el-avatar 
            :size="32" 
            :src="userInfo.avatar || defaultAvatar"
            class="user-avatar"
          />
          <span class="username">{{ userInfo.realName || userInfo.username }}</span>
          <el-icon class="dropdown-icon"><CaretBottom /></el-icon>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>
              <span>个人信息</span>
            </el-dropdown-item>
            <el-dropdown-item command="changePwd">
              <el-icon><Lock /></el-icon>
              <span>修改密码</span>
            </el-dropdown-item>
            <el-dropdown-item divided command="logout">
              <el-icon><SwitchButton /></el-icon>
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { logout } from '@/api/auth'
import { 
  Timer, 
  CaretBottom, 
  User, 
  Lock, 
  SwitchButton 
} from '@element-plus/icons-vue'

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
  
  updateTime()
  timer = setInterval(updateTime, 1000)
})

onBeforeUnmount(() => {
  if (timer) {
    clearInterval(timer)
  }
})

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      router.push('/profile')
      break
    case 'changePwd':
      router.push('/change-password')
      break
    case 'logout':
      try {
        console.log('开始退出登录流程')
        
        await ElMessageBox.confirm('确认退出登录吗？', '提示', {
          type: 'warning',
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
        
        console.log('用户确认退出')
        
        try {
          console.log('调用登出API')
          const response = await logout()
          console.log('登出API响应:', response)
          
          localStorage.clear()
          router.push('/login')
          ElMessage.success('退出登录成功')
        } catch (error) {
          console.error('登出失败:', error)
          ElMessage.error('退出登录失败：' + (error.response?.data?.message || error.message || '未知错误'))
        }
      } catch (error) {
        console.log('用户取消退出:', error)
      }
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
  padding: 0 20px;
}

.header-left {
  flex: 1;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.server-time {
  display: flex;
  align-items: center;
  color: #606266;
  font-size: 14px;
}

.time-text {
  margin-left: 6px;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 8px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.user-avatar {
  border: 2px solid #fff;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.1);
}

.username {
  margin: 0 8px;
  font-size: 14px;
  color: #303133;
}

.dropdown-icon {
  color: #909399;
  font-size: 12px;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
}

:deep(.el-dropdown-menu__item .el-icon) {
  margin-right: 4px;
}

:deep(.el-divider--vertical) {
  height: 20px;
  margin: 0;
}
</style> 