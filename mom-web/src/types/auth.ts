/**
 * 登录相关的类型定义
 */

/** 登录请求参数接口 */
export interface LoginRequest {
  username: string
  password: string
}

/** 登录响应数据接口 */
export interface LoginResponse {
  userId: number
  username: string
  realName: string
  token: string
}

/** API响应数据结构 */
export interface ApiResponse<T> {
  code: number
  message: string
  data: T
} 