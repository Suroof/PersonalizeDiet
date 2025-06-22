import axios from 'axios'

// 营养分析 API 配置
const NUTRITION_DIFY_API_URL = import.meta.env.VITE_NUTRITION_DIFY_API_URL || 'https://api.dify.ai/v1'
const NUTRITION_DIFY_API_KEY = import.meta.env.VITE_NUTRITION_DIFY_API_KEY || ''

// 营养分析 API
export const nutritionApi = {
  // 上传文件进行营养分析
  async analyzeFile(file, options = {}) {
    const { onProgress, onComplete, onError } = options
    
    try {
      // 检查 API Key
      if (!NUTRITION_DIFY_API_KEY) {
        throw new Error('营养分析 API Key 未配置，请在 .env 文件中设置 VITE_NUTRITION_DIFY_API_KEY')
      }

      // 检查文件类型
      const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg', 'text/plain', 'application/pdf']
      if (!allowedTypes.includes(file.type)) {
        throw new Error('不支持的文件类型，请上传图片、文本或PDF文件')
      }

      // 检查文件大小 (10MB)
      const maxSize = 10 * 1024 * 1024
      if (file.size > maxSize) {
        throw new Error('文件大小不能超过10MB')
      }

      // 创建 FormData
      const formData = new FormData()
      formData.append('file', file)
      formData.append('user', 'user-' + Date.now())
      formData.append('response_mode', 'blocking')
      
      // 根据文件类型设置不同的查询内容
      let query = ''
      if (file.type.startsWith('image/')) {
        query = '请分析这张图片中的食物，提供详细的营养成分分析，包括热量、蛋白质、脂肪、碳水化合物、维生素和矿物质含量。'
      } else {
        query = '请分析这个文件中的食物信息，提供详细的营养成分分析，包括热量、蛋白质、脂肪、碳水化合物、维生素和矿物质含量。'
      }
      formData.append('query', query)

      // 构建请求URL
      const url = `${NUTRITION_DIFY_API_URL}/files/upload`
      
      // 发送文件上传请求
      const uploadResponse = await axios.post(url, formData, {
        headers: {
          'Authorization': `Bearer ${NUTRITION_DIFY_API_KEY}`,
          'Content-Type': 'multipart/form-data'
        },
        timeout: 60000,
        onUploadProgress: (progressEvent) => {
          if (onProgress) {
            const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total)
            onProgress(percentCompleted)
          }
        }
      })

      // 获取文件ID后发送分析请求
      if (uploadResponse.data && uploadResponse.data.id) {
        const fileId = uploadResponse.data.id
        
        // 发送营养分析请求
        const analysisResponse = await axios.post(`${NUTRITION_DIFY_API_URL}/chat-messages`, {
          inputs: {
            "imgURL": {
              type: 'image', 
              transfer_method: 'local_file', 
              upload_file_id: fileId 
            }
          },
          query: query,
          response_mode: 'blocking',
          conversation_id: '',
          user: 'user-' + Date.now()
        }, {
          headers: {
            'Authorization': `Bearer ${NUTRITION_DIFY_API_KEY}`,
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
            fileType: file.type,
            fileSize: file.size,
            timestamp: new Date().toISOString()
          }
          
          onComplete && onComplete(result)
          return result
        } else {
          throw new Error('营养分析响应格式错误')
        }
      } else {
        throw new Error('文件上传失败')
      }
      
    } catch (error) {
      console.error('Nutrition analysis error:', error)
      
      // 处理不同类型的错误
      if (error.response) {
        console.error('API Response Error:', error.response.data)
        const errorMessage = error.response.data?.message || error.response.data?.error || `HTTP ${error.response.status}: ${error.response.statusText}`
        onError && onError(new Error(`营养分析API错误: ${errorMessage}`))
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
      // 检查 API Key
      if (!NUTRITION_DIFY_API_KEY) {
        throw new Error('营养分析 API Key 未配置，请在 .env 文件中设置 VITE_NUTRITION_DIFY_API_KEY')
      }

      // 检查文本长度
      if (!text || text.trim().length === 0) {
        throw new Error('请输入要分析的食物信息')
      }

      if (text.length > 2000) {
        throw new Error('文本长度不能超过2000字符')
      }

      // 构建查询内容
      const query = `请分析以下食物信息的营养成分：${text}\n\n请提供详细的营养成分分析，包括热量、蛋白质、脂肪、碳水化合物、维生素和矿物质含量。`

      // 构建请求URL
      const url = `${NUTRITION_DIFY_API_URL}/chat-messages`
      
      // 发送营养分析请求
      const response = await axios.post(url, {
        inputs: {},
        query: query,
        response_mode: 'blocking',
        conversation_id: '',
        user: 'user-' + Date.now()
      }, {
        headers: {
          'Authorization': `Bearer ${NUTRITION_DIFY_API_KEY}`,
          'Content-Type': 'application/json'
        },
        timeout: 30000
      })

      // 处理响应
      if (response.data && response.data.answer) {
        const result = {
          analysis: response.data.answer,
          inputText: text,
          timestamp: new Date().toISOString()
        }
        
        onComplete && onComplete(result)
        return result
      } else {
        throw new Error('营养分析响应格式错误')
      }
      
    } catch (error) {
      console.error('Text nutrition analysis error:', error)
      
      // 处理不同类型的错误
      if (error.response) {
        console.error('API Response Error:', error.response.data)
        const errorMessage = error.response.data?.message || error.response.data?.error || `HTTP ${error.response.status}: ${error.response.statusText}`
        onError && onError(new Error(`营养分析API错误: ${errorMessage}`))
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
      history.unshift(result)
      
      // 只保留最近50条记录
      if (history.length > 50) {
        history.splice(50)
      }
      
      localStorage.setItem('nutrition_analysis_history', JSON.stringify(history))
      return true
    } catch (error) {
      console.error('Save analysis to history error:', error)
      return false
    }
  },

  // 清除营养分析历史
  async clearAnalysisHistory() {
    try {
      localStorage.removeItem('nutrition_analysis_history')
      return true
    } catch (error) {
      console.error('Clear analysis history error:', error)
      return false
    }
  }
}

export default nutritionApi