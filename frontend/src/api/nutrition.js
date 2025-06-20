import axios from 'axios'

// API配置
const DIFY_API_URL = import.meta.env.VITE_DIFY_NUTRITION_API_URL || 'https://api.dify.ai/v1'
const DIFY_NUTRITION_API_KEY = import.meta.env.VITE_DIFY_NUTRITION_API_KEY || ''

// 营养分析API
export const nutritionApi = {
  // 上传文件进行营养分析
  async analyzeFile(file, options = {}) {
    const { onProgress, onComplete, onError } = options
    
    try {
      // 检查 API Key
      if (!DIFY_NUTRITION_API_KEY) {
        throw new Error('营养分析 API Key 未配置，请在 .env 文件中设置 VITE_DIFY_NUTRITION_API_KEY')
      }

      // 检查文件类型
      const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg', 'image/webp']
      if (!allowedTypes.includes(file.type)) {
        throw new Error('仅支持 JPG、PNG、WEBP 格式的图片文件')
      }

      // 检查文件大小 (10MB)
      const maxSize = 10 * 1024 * 1024
      if (file.size > maxSize) {
        throw new Error('文件大小不能超过 10MB')
      }

      // 创建 FormData
      const formData = new FormData()
      formData.append('file', file)
      formData.append('user', 'nutrition-user-' + Date.now())
      formData.append('inputs', JSON.stringify({}))
      formData.append('response_mode', 'blocking')
      formData.append('conversation_id', '')

      // 构建请求URL
      const url = `${DIFY_API_URL}/files/upload`
      
      // 先上传文件
      const uploadResponse = await axios.post(url, formData, {
        headers: {
          'Authorization': `Bearer ${DIFY_NUTRITION_API_KEY}`,
        },
        timeout: 60000,
        onUploadProgress: (progressEvent) => {
          if (onProgress) {
            const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
            onProgress(percentCompleted)
          }
        }
      })

      if (!uploadResponse.data || !uploadResponse.data.id) {
        throw new Error('文件上传失败')
      }

      const fileId = uploadResponse.data.id

      // 调用营养分析接口
      const analysisUrl = `${DIFY_API_URL}/chat-messages`
      const analysisResponse = await axios.post(analysisUrl, {
        inputs: {
          file_id: fileId
        },
        query: '请分析这张图片中食物的营养成分，包括卡路里、蛋白质、碳水化合物、脂肪等详细信息。',
        response_mode: 'blocking',
        conversation_id: '',
        user: 'nutrition-user-' + Date.now(),
        files: [{
          type: 'image',
          transfer_method: 'remote_url',
          upload_file_id: fileId
        }]
      }, {
        headers: {
          'Authorization': `Bearer ${DIFY_NUTRITION_API_KEY}`,
          'Content-Type': 'application/json'
        },
        timeout: 60000
      })

      // 处理分析结果
      if (analysisResponse.data && analysisResponse.data.answer) {
        const result = {
          analysis: analysisResponse.data.answer,
          fileId: fileId,
          fileName: file.name,
          fileSize: file.size,
          timestamp: new Date().toISOString()
        }
        
        onComplete && onComplete(result)
        return result
      } else {
        throw new Error('营养分析失败，请重试')
      }
      
    } catch (error) {
      console.error('Nutrition analysis error:', error)
      
      // 处理不同类型的错误
      if (error.response) {
        const errorMessage = error.response.data?.message || error.response.data?.error || `HTTP ${error.response.status}: ${error.response.statusText}`
        onError && onError(new Error(`分析失败: ${errorMessage}`))
      } else if (error.request) {
        onError && onError(new Error('网络请求失败，请检查网络连接'))
      } else {
        onError && onError(error)
      }
      
      throw error
    }
  },

  // 文本营养分析
  async analyzeText(text, options = {}) {
    const { onComplete, onError } = options
    
    try {
      if (!DIFY_NUTRITION_API_KEY) {
        throw new Error('营养分析 API Key 未配置')
      }

      if (!text || text.trim().length === 0) {
        throw new Error('请输入要分析的食物描述')
      }

      const url = `${DIFY_API_URL}/chat-messages`
      const response = await axios.post(url, {
        inputs: {},
        query: `请分析以下食物的营养成分：${text}。请提供详细的营养信息，包括卡路里、蛋白质、碳水化合物、脂肪、维生素和矿物质等。`,
        response_mode: 'blocking',
        conversation_id: '',
        user: 'nutrition-text-user-' + Date.now()
      }, {
        headers: {
          'Authorization': `Bearer ${DIFY_NUTRITION_API_KEY}`,
          'Content-Type': 'application/json'
        },
        timeout: 30000
      })

      if (response.data && response.data.answer) {
        const result = {
          analysis: response.data.answer,
          inputText: text,
          timestamp: new Date().toISOString()
        }
        
        onComplete && onComplete(result)
        return result
      } else {
        throw new Error('营养分析失败，请重试')
      }
      
    } catch (error) {
      console.error('Text nutrition analysis error:', error)
      
      if (error.response) {
        const errorMessage = error.response.data?.message || error.response.data?.error || `HTTP ${error.response.status}: ${error.response.statusText}`
        onError && onError(new Error(`分析失败: ${errorMessage}`))
      } else if (error.request) {
        onError && onError(new Error('网络请求失败，请检查网络连接'))
      } else {
        onError && onError(error)
      }
      
      throw error
    }
  },

  // 获取营养分析历史
  async getAnalysisHistory() {
    try {
      // 从本地存储获取历史记录
      const history = localStorage.getItem('nutrition_analysis_history')
      return history ? JSON.parse(history) : []
    } catch (error) {
      console.error('Get analysis history error:', error)
      return []
    }
  },

  // 保存营养分析结果到历史
  async saveAnalysisToHistory(result) {
    try {
      const history = await this.getAnalysisHistory()
      const newHistory = [result, ...history].slice(0, 50) // 只保留最近50条记录
      localStorage.setItem('nutrition_analysis_history', JSON.stringify(newHistory))
      return newHistory
    } catch (error) {
      console.error('Save analysis to history error:', error)
      throw error
    }
  },

  // 清除营养分析历史
  async clearAnalysisHistory() {
    try {
      localStorage.removeItem('nutrition_analysis_history')
      return true
    } catch (error) {
      console.error('Clear analysis history error:', error)
      throw error
    }
  }
}

export default nutritionApi