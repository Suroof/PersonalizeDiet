// 用户相关 API
import axios from 'axios'

// 后端 API 基础地址
const API_BASE_URL = 'http://localhost:8080/api'

// 创建 axios 实例
const userApiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  withCredentials: true, // 支持Cookie/Session
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器 - 添加认证token
userApiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('userToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器 - 处理通用错误
userApiClient.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.error('API请求错误:', error)
    
    if (error.response) {
      // 服务器返回错误状态码
      const { status, data } = error.response
      
      switch (status) {
        case 401:
          // 未授权，清除本地存储的token
          localStorage.removeItem('userToken')
          localStorage.removeItem('userInfo')
          throw new Error('登录已过期，请重新登录')
        case 403:
          throw new Error('没有权限访问该资源')
        case 404:
          throw new Error('请求的资源不存在')
        case 500:
          throw new Error('服务器内部错误')
        default:
          throw new Error(data?.message || '请求失败')
      }
    } else if (error.request) {
      // 网络错误
      throw new Error('网络连接失败，请检查网络设置')
    } else {
      // 其他错误
      throw new Error(error.message || '未知错误')
    }
  }
)

// 用户 API
export const userApi = {
  // 用户登录
  async login(credentials) {
    try {
      const response = await userApiClient.post('/auth/login', credentials)
      return response
    } catch (error) {
      throw error
    }
  },

  // 用户注册
  async register(userData) {
    try {
      const response = await userApiClient.post('/auth/register', userData)
      return response
    } catch (error) {
      throw error
    }
  },

  // 更新用户基本信息
  async updateProfile(profileData) {
    try {
      const response = await userApiClient.put('/user/profile', profileData)
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取用户信息
  async getUserInfo() {
    try {
      const response = await userApiClient.get('/user/profile')
      return response
    } catch (error) {
      throw error
    }
  },

  // 更新用户饮食偏好
  async updatePreferences(preferences) {
    try {
      const response = await userApiClient.post('/interest', preferences)
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取用户饮食偏好
  async getPreferences() {
    try {
      const response = await userApiClient.get('/interest')
      return response
    } catch (error) {
      throw error
    }
  },

  // 上传头像
  async uploadAvatar(file) {
    try {
      const formData = new FormData()
      formData.append('avatar', file)
      
      const response = await userApiClient.post('/user/avatar', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      return response
    } catch (error) {
      throw error
    }
  },

  // 修改密码
  async changePassword(passwordData) {
    try {
      const response = await userApiClient.put('/user/password', passwordData)
      return response
    } catch (error) {
      throw error
    }
  },

  // 用户登出
  async logout() {
    try {
      const response = await userApiClient.post('/auth/logout')
      return response
    } catch (error) {
      throw error
    }
  }
}

export default userApi
