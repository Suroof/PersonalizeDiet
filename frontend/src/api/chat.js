import { EventSourcePolyfill } from 'event-source-polyfill'
import axios from 'axios'

// API配置
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'
const DIFY_API_URL = import.meta.env.VITE_DIFY_API_URL || 'https://api.dify.ai/v1'
const DIFY_API_KEY = import.meta.env.VITE_DIFY_API_KEY || ''

// 创建axios实例
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    // 可以在这里添加认证token
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    console.error('API Error:', error)
    return Promise.reject(error)
  }
)

// 聊天API
export const chatApi = {
  // 发送流式消息
  async sendStreamMessage(message, options = {}) {
    const { onMessage, onComplete, onError } = options
    
    try {
      // 检查 API Key
      if (!DIFY_API_KEY) {
        throw new Error('Dify API Key 未配置，请在 .env 文件中设置 VITE_DIFY_API_KEY')
      }

      // 构建请求URL
      const url = `${DIFY_API_URL}/chat-messages`
      
      // 使用 axios 发送 POST 请求
      const response = await axios.post(url, {
        inputs: {},
        query: message,
        response_mode: 'blocking', // 改为阻塞模式，避免流式处理的复杂性
        conversation_id: '',
        user: 'user-' + Date.now()
      }, {
        headers: {
          'Authorization': `Bearer ${DIFY_API_KEY}`,
          'Content-Type': 'application/json'
        },
        timeout: 30000
      })

      // 处理响应
      if (response.data && response.data.answer) {
        onMessage && onMessage(response.data.answer)
        onComplete && onComplete()
        return response.data
      } else {
        throw new Error('API 响应格式错误')
      }
      
    } catch (error) {
      console.error('Send stream message error:', error)
      
      // 处理不同类型的错误
      if (error.response) {
        // 服务器响应了错误状态码
        console.error('API Response Error:', error.response.data)
        const errorMessage = error.response.data?.message || error.response.data?.error || `HTTP ${error.response.status}: ${error.response.statusText}`
        onError && onError(new Error(`API错误: ${errorMessage}`))
      } else if (error.request) {
        // 请求发出但没有收到响应
        onError && onError(new Error('网络请求失败，请检查网络连接'))
      } else {
        // 其他错误
        onError && onError(error)
      }
      
      throw error
     }
  },

  // 发送普通消息（非流式）
  async sendMessage(message, conversationId = null) {
    try {
      const response = await apiClient.post('/chat/message', {
        message,
        conversationId
      })
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取聊天历史
  async getChatHistory(conversationId) {
    try {
      const response = await apiClient.get(`/chat/history/${conversationId}`)
      return response
    } catch (error) {
      throw error
    }
  },

  // 创建新对话
  async createConversation() {
    try {
      const response = await apiClient.post('/chat/conversation')
      return response
    } catch (error) {
      throw error
    }
  }
}

// 菜谱API
export const recipeApi = {
  // 搜索菜谱
  async searchRecipes(query, filters = {}) {
    try {
      const response = await apiClient.get('/recipes/search', {
        params: {
          q: query,
          ...filters
        }
      })
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取菜谱详情
  async getRecipeDetail(id) {
    try {
      const response = await apiClient.get(`/recipes/${id}`)
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取推荐菜谱
  async getRecommendedRecipes(preferences = {}) {
    try {
      const response = await apiClient.post('/recipes/recommend', preferences)
      return response
    } catch (error) {
      throw error
    }
  },

  // 收藏菜谱
  async favoriteRecipe(recipeId) {
    try {
      const response = await apiClient.post(`/recipes/${recipeId}/favorite`)
      return response
    } catch (error) {
      throw error
    }
  },

  // 取消收藏
  async unfavoriteRecipe(recipeId) {
    try {
      const response = await apiClient.delete(`/recipes/${recipeId}/favorite`)
      return response
    } catch (error) {
      throw error
    }
  }
}

// 用户API
export const userApi = {
  // 更新用户偏好
  async updatePreferences(preferences) {
    try {
      const response = await apiClient.put('/user/preferences', preferences)
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取用户偏好
  async getPreferences() {
    try {
      const response = await apiClient.get('/user/preferences')
      return response
    } catch (error) {
      throw error
    }
  },

  // 获取用户收藏
  async getFavorites() {
    try {
      const response = await apiClient.get('/user/favorites')
      return response
    } catch (error) {
      throw error
    }
  }
}

export default {
  chatApi,
  recipeApi,
  userApi
}