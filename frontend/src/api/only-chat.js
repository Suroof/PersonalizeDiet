import axios from 'axios'

// 智能问答API配置
const AI_DIFY_API_URL = import.meta.env.VITE_AI_DIFY_API_URL || 'https://api.dify.ai/v1'
const AI_DIFY_API_KEY = import.meta.env.VITE_AI_DIFY_API_KEY || ''

// 智能问答API
export const aiChatApi = {
  // 发送智能问答消息
  async sendMessage(message, options = {}) {
    const { onMessage, onComplete, onError } = options
    
    try {
      // 检查 API Key
      if (!AI_DIFY_API_KEY) {
        throw new Error('智能问答 API Key 未配置，请在 .env 文件中设置 VITE_AI_DIFY_API_KEY')
      }

      // 构建请求URL
      const url = `${AI_DIFY_API_URL}/chat-messages`
      
      // 使用 axios 发送 POST 请求
      const response = await axios.post(url, {
        inputs: {},
        query: message,
        response_mode: 'blocking', // 阻塞模式
        conversation_id: '',
        user: 'user-' + Date.now()
      }, {
        headers: {
          'Authorization': `Bearer ${AI_DIFY_API_KEY}`,
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
      console.error('Send AI message error:', error)
      
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

  // 开始新对话
  async startConversation(context = null) {
    try {
      // 如果有上下文，构建初始消息
      let initialMessage = '您好！我是您的智能营养助手，可以为您提供营养相关的建议和问答服务。'
      
      if (context && context.analysisResult) {
        initialMessage = `您好！我是您的智能营养助手。我已经了解了您的营养分析结果，可以为您提供个性化的营养建议和问答服务。\n\n基于您的分析结果，您可以问我：\n• 如何改善营养搭配\n• 推荐适合的食材\n• 饮食注意事项\n• 营养补充建议\n\n请随时向我提问！`
      }
      
      return {
        conversationId: 'conv-' + Date.now(),
        initialMessage
      }
    } catch (error) {
      console.error('Start conversation error:', error)
      throw error
    }
  }
}

export default aiChatApi