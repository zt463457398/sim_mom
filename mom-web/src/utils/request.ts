/**
 * Axios请求封装
 */
import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import router from '@/router'

// 创建axios实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 5000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token && config.headers) {
      config.headers['Authorization'] = encodeURIComponent(`Bearer ${token}`)
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message, data } = response.data
    if (code !== 200) {
      if (code === 401) {
        localStorage.clear()
        router.push('/login')
      }
      return Promise.reject(new Error(message || 'Error'))
    }
    return data
  },
  (error) => {
    if (error.response?.status === 401) {
      localStorage.clear()
      router.push('/login')
    }
    return Promise.reject(error)
  }
)

/**
 * 封装请求方法
 * @param config 请求配置
 * @returns Promise
 */
const request = <T = any>(config: AxiosRequestConfig): Promise<T> => {
  return service(config)
}

export default request 