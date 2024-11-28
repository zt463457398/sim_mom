<template>
  <el-dialog
    v-model="visible"
    title="重置密码"
    width="500px"
    :close-on-click-modal="false"
    @closed="handleClosed"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <el-form-item label="新密码" prop="password">
        <el-input
          v-model="form.password"
          type="password"
          show-password
          placeholder="请输入新密码"
          @input="checkPasswordStrength"
        />
      </el-form-item>
      
      <!-- 修改密码强度显示部分 -->
      <el-form-item label="密码强度">
        <el-progress
          :percentage="passwordStrength"
          :color="strengthColor"
          :status="strengthStatus"
        >
          <template #default>{{ strengthText }}</template>
        </el-progress>
      </el-form-item>
      
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          show-password
          placeholder="请再次输入新密码"
        />
      </el-form-item>
    </el-form>
    
    <template #footer>
      <el-button @click="visible = false">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="handleSubmit">
        确 定
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { resetUserPassword } from '@/api/user'

const props = defineProps({
  show: Boolean,
  userId: Number
})

const emit = defineEmits(['update:show'])

const visible = computed({
  get: () => props.show,
  set: (val) => emit('update:show', val)
})

const formRef = ref(null)
const loading = ref(false)
const passwordStrength = ref(0)

const form = ref({
  password: '',
  confirmPassword: ''
})

// 密码强度相关计算属性
const strengthColor = computed(() => {
  if (passwordStrength.value < 40) return '#F56C6C'
  if (passwordStrength.value < 80) return '#E6A23C'
  return '#67C23A'
})

const strengthStatus = computed(() => {
  if (passwordStrength.value < 40) return 'exception'
  if (passwordStrength.value < 80) return 'warning'
  return 'success'
})

const strengthText = computed(() => {
  if (passwordStrength.value < 40) return '弱'
  if (passwordStrength.value < 80) return '中'
  return '强'
})

// 检查密码强度
const checkPasswordStrength = (password) => {
  let strength = 0
  if (password.length >= 8) strength += 20
  if (/[a-z]/.test(password)) strength += 20
  if (/[A-Z]/.test(password)) strength += 20
  if (/[0-9]/.test(password)) strength += 20
  if (/[^A-Za-z0-9]/.test(password)) strength += 20
  passwordStrength.value = strength
}

const rules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 8, max: 20, message: '密码长度必须在8-20位之间', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (!/(?=.*[0-9])(?=.*[a-zA-Z])/.test(value)) {
          callback(new Error('密码必须包含数字和字母'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.value.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    loading.value = true
    
    await resetUserPassword({
      userId: props.userId,
      password: form.value.password
    })
    
    ElMessage.success('密码重置成功')
    visible.value = false
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    }
  } finally {
    loading.value = false
  }
}

const handleClosed = () => {
  formRef.value?.resetFields()
  form.value = {
    password: '',
    confirmPassword: ''
  }
  passwordStrength.value = 0
}
</script> 