/**
 * 认证相关的API请求
 */
import request from '@/utils/request'
import type { LoginRequest, LoginResponse } from '@/types/auth'

/**
 * 用户登录
 * @param data 登录请求参数
 * @returns Promise<LoginResponse> 登录响应
 */
export function login(data: LoginRequest) {
  return request<LoginResponse>({
    url: '/api/auth/login',
    method: 'post',
    data
  })
} 