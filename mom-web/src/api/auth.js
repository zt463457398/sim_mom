import request from '@/utils/request'

// 登出接口 - 修改为正确的API路径
export function logout() {
  return request({
    url: '/api/user/logout',  // 修改为与后端controller一致的路径
    method: 'post'
  })
} 