<template>
  <el-dialog
    title="重置密码"
    :model-value="visible"
    width="400px"
    @close="handleClose"
  >
    <el-form
      ref="formRef"
      :model="form"
      :rules="rules"
      label-width="80px"
    >
      <el-form-item label="新密码" prop="password">
        <el-input
          v-model="form.password"
          type="password"
          show-password
          placeholder="请输入新密码"
        />
        <div class="password-strength" v-if="form.password">
          <span>密码强度：</span>
          <el-progress
            :percentage="passwordStrength"
            :color="strengthColor"
            :format="strengthText"
            :stroke-width="10"
          />
        </div>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          v-model="form.confirmPassword"
          type="password"
          show-password
          placeholder="请确认新密码"
        />
      </el-form-item>
      <div class="password-tips">
        密码必须包含数字和字母，长度在8-20位之间
      </div>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">确 定</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { defineProps, defineEmits } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const props = defineProps({
  visible: Boolean,
  userId: Number
})

const emit = defineEmits(['update:visible', 'success'])

const formRef = ref(null)
const loading = ref(false)

const form = reactive({
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== form.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

// 密码强度验证
const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入密码'))
    return
  }
  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,20}$/.test(value)) {
    callback(new Error('密码必须包含数字和字母，长度在8-20位之间'))
    return
  }
  callback()
}

const rules = {
  password: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

// 计算密码强度
const passwordStrength = computed(() => {
  const pwd = form.password
  if (!pwd) return 0
  let strength = 0
  if (pwd.length >= 8) strength += 20
  if (pwd.length >= 12) strength += 20
  if (/[A-Z]/.test(pwd)) strength += 20
  if (/[a-z]/.test(pwd)) strength += 20
  if (/\d/.test(pwd)) strength += 20
  return strength
})

// 密码强度颜色
const strengthColor = computed(() => {
  const strength = passwordStrength.value
  if (strength < 40) return '#F56C6C'
  if (strength < 60) return '#E6A23C'
  if (strength < 80) return '#409EFF'
  return '#67C23A'
})

// 密码强度文本
const strengthText = computed(() => {
  const strength = passwordStrength.value
  if (strength < 40) return '弱'
  if (strength < 60) return '中'
  if (strength < 80) return '强'
  return '很强'
})

// 修改提交方法，添加确认提示
const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    await ElMessageBox.confirm('确认要重置该用户的密码吗？', '提示', {
      type: 'warning',
      confirmButtonText: '确定',
      cancelButtonText: '取消'
    })
    
    loading.value = true
    await request.put(`/api/user/${props.userId}/password/reset`, { password: form.password })
    ElMessage.success('密码重置成功')
    emit('success')
    emit('update:visible', false)
  } catch (error) {
    if (error !== 'cancel') {
      console.error('密码重置失败：', error)
    }
  } finally {
    loading.value = false
  }
}

const handleClose = () => {
  formRef.value?.resetFields()
  emit('update:visible', false)
}
</script>

<style scoped>
.password-strength {
  margin-top: 8px;
  font-size: 12px;
}

.password-tips {
  color: #909399;
  font-size: 12px;
  margin-top: -10px;
  margin-bottom: 10px;
}
</style> 