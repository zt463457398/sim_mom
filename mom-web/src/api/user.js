import request from '@/utils/request'

// 获取用户信息
export function getUserInfo() {
  return request({
    url: '/api/user/info',
    method: 'get'
  })
}

// 更新个人信息
export function updateProfile(data) {
  return request({
    url: '/api/user/profile',
    method: 'put',
    data
  })
}

// 重置用户密码
export function resetUserPassword(data) {
  return request({
    url: `/api/user/${data.userId}/password/reset`,
    method: 'put',
    data: {
      password: data.password
    }
  })
}

// 获取用户列表
export function getUserList(params) {
  return request({
    url: '/api/user/list',
    method: 'get',
    params
  })
}

// 更新用户状态
export function updateUserStatus(userId, status) {
  return request({
    url: `/api/user/${userId}/status`,
    method: 'put',
    data: { status }
  })
}

// 删除用户
export function deleteUser(userId) {
  return request({
    url: `/api/user/${userId}`,
    method: 'delete'
  })
}

// 批量删除用户
export function batchDeleteUsers(userIds) {
  return request({
    url: '/api/user/batch',
    method: 'delete',
    data: userIds
  })
} 