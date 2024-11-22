// 通用的表单验证规则
export const rules = {
  // 用户名验证规则
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_-]*$/, message: '用户名只能包含字母、数字、下划线和横线', trigger: 'blur' }
  ],
  // 密码验证规则
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' },
    { 
      pattern: /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,}$/,
      message: '密码必须包含字母、数字和特殊字符',
      trigger: 'blur'
    }
  ],
  // 手机号验证规则
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  // 邮箱验证规则
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' },
    { max: 50, message: '邮箱长度不能超过50个字符', trigger: 'blur' }
  ],
  // 必填项通用规则
  required: (message) => [
    { required: true, message: message || '该项不能为空', trigger: 'blur' }
  ],
  // 字符串长度验证
  length: (min, max, message) => [
    { min, max, message: message || `长度在 ${min} 到 ${max} 个字符`, trigger: 'blur' }
  ]
}

// 自定义验证方法
export const validators = {
  // 确认密码验证
  confirmPassword: (password) => (rule, value, callback) => {
    if (value !== password) {
      callback(new Error('两次输入的密码不一致'))
    } else {
      callback()
    }
  },
  
  // 强密码验证
  strongPassword: (rule, value, callback) => {
    const strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
    if (!strongRegex.test(value)) {
      callback(new Error('密码必须包含大小写字母、数字和特殊字符，且长度不少于8位'))
    } else {
      callback()
    }
  }
}

// 辅助方法
export const validateHelper = {
  // 检查是否为空
  isEmpty: (value) => {
    return value === undefined || value === null || value === ''
  },
  
  // 检查是否为有效手机号
  isValidPhone: (value) => {
    return /^1[3-9]\d{9}$/.test(value)
  },
  
  // 检查是否为有效邮箱
  isValidEmail: (value) => {
    return /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(value)
  }
} 