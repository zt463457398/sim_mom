<template>
  <div class="profile-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>个人信息</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="userForm"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="用户名">
          <el-input v-model="userForm.username" disabled />
        </el-form-item>
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="userForm.realName" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="userForm.phone" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit">保存</el-button>
          <el-button @click="handleCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const formRef = ref(null)
const userForm = ref({})

const rules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

onMounted(() => {
  const userInfo = localStorage.getItem('userInfo')
  if (userInfo) {
    userForm.value = JSON.parse(userInfo)
  }
})

const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    const res = await request.put('/api/user/profile', userForm.value)
    ElMessage.success('保存成功')
    localStorage.setItem('userInfo', JSON.stringify(res.data))
    // 返回上一页
    router.back()
  } catch (error) {
    console.error('保存失败：', error)
  }
}

const handleCancel = () => {
  router.back()
}
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.card-header {
  font-weight: bold;
}

.el-button {
  margin-right: 10px;
}
</style> 