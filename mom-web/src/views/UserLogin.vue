<template>
  <div class="login-container">
    <div class="login-box">
      <div class="login-left">
        <div class="system-title">
          <h1>SIM-MOM</h1>
          <p>生产制造执行系统</p>
        </div>
      </div>
      <div class="login-form">
        <h2>欢迎登录</h2>
        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          size="large"
        >
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              :prefix-icon="User"
              clearable
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              clearable
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              class="login-button"
              @click="handleLogin"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="login-footer">
      <p>Copyright © 2024 SIM-MOM All Rights Reserved.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await request.post('/api/user/login', loginForm)
        localStorage.setItem('token', res.data.token)
        localStorage.setItem('userInfo', JSON.stringify(res.data.userInfo))
        ElMessage.success('登录成功')
        router.push('/home')
      } catch (error) {
        console.error('登录失败：', error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
}

.login-box {
  display: flex;
  width: 900px;
  height: 500px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.login-left {
  width: 400px;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.system-title {
  color: white;
  text-align: center;
}

.system-title h1 {
  font-size: 36px;
  margin-bottom: 16px;
  font-weight: 600;
}

.system-title p {
  font-size: 18px;
  opacity: 0.9;
}

.login-form {
  flex: 1;
  padding: 40px 60px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.login-form h2 {
  font-size: 28px;
  color: #333;
  margin-bottom: 40px;
  text-align: center;
}

:deep(.el-input__wrapper) {
  background-color: #f5f7fa;
  box-shadow: none;
  border: 1px solid #e4e7ed;
}

:deep(.el-input__wrapper:hover) {
  border-color: #1890ff;
}

:deep(.el-input__wrapper.is-focus) {
  border-color: #1890ff;
  box-shadow: 0 0 0 1px #1890ff;
}

.login-button {
  width: 100%;
  height: 44px;
  font-size: 16px;
  margin-top: 20px;
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
  border: none;
}

.login-button:hover {
  opacity: 0.9;
}

.login-footer {
  position: fixed;
  bottom: 20px;
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
}

/* 响应式设计 */
@media screen and (max-width: 992px) {
  .login-box {
    width: 90%;
    max-width: 900px;
  }
  
  .login-left {
    width: 40%;
  }
}

@media screen and (max-width: 768px) {
  .login-box {
    flex-direction: column;
    height: auto;
    width: 90%;
    max-width: 400px;
  }
  
  .login-left {
    width: 100%;
    padding: 30px;
  }
  
  .login-form {
    padding: 30px;
  }
}
</style> 