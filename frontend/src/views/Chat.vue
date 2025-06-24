<template>
  <div class="chat-container">
    <!-- 头部 -->
    <div class="chat-header">
      <el-button 
        type="text" 
        @click="goBack" 
        class="back-btn"
      >
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h2>智能营养助手</h2>
      <div class="header-actions">
        <el-button 
          type="text" 
          @click="clearChat"
          :disabled="messages.length === 0"
        >
          <el-icon><Delete /></el-icon>
          清空对话
        </el-button>
      </div>
    </div>

    <!-- 对话区域 -->
    <div class="chat-messages" ref="messagesContainer">
      <div 
        v-for="(message, index) in messages" 
        :key="index" 
        :class="['message', message.type]"
      >
        <div class="message-avatar">
          <el-avatar 
            v-if="message.type === 'user'" 
            :size="32" 
            :src="userAvatar"
          >
            <el-icon><User /></el-icon>
          </el-avatar>
          <el-avatar 
            v-else 
            :size="32" 
            class="ai-avatar"
          >
            <el-icon><ChatDotRound /></el-icon>
          </el-avatar>
        </div>
        <div class="message-content">
          <div class="message-text" v-html="formatMessage(message.content)"></div>
          <div class="message-time">{{ formatTime(message.timestamp) }}</div>
        </div>
      </div>
      
      <!-- 加载状态 -->
      <div v-if="isLoading" class="message ai">
        <div class="message-avatar">
          <el-avatar :size="32" class="ai-avatar">
            <el-icon><ChatDotRound /></el-icon>
          </el-avatar>
        </div>
        <div class="message-content">
          <div class="typing-indicator">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="chat-input">
      <div class="input-container">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="1"
          :autosize="{ minRows: 1, maxRows: 4 }"
          placeholder="请输入您的问题..."
          @keydown.enter.prevent="handleEnter"
          :disabled="isLoading"
          class="message-input"
        />
        <el-button 
          type="primary" 
          @click="sendMessage"
          :disabled="!inputMessage.trim() || isLoading"
          class="send-btn"
        >
          <el-icon><Promotion /></el-icon>
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, User, ChatDotRound, Delete, Promotion } from '@element-plus/icons-vue'
import { aiChatApi } from '@/api/only-chat'

const router = useRouter()
const route = useRoute()

// 响应式数据
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)
const userAvatar = ref('')
const conversationId = ref('')
const chatContext = ref(null)

// 初始化对话
const initChat = async () => {
  try {
    // 获取路由参数中的上下文数据
    if (route.query.context === 'nutrition-analysis' && route.query.data) {
      chatContext.value = JSON.parse(route.query.data)
    }
    
    // 开始新对话
    const conversation = await aiChatApi.startConversation(chatContext.value)
    conversationId.value = conversation.conversationId
    
    // 添加初始消息
    messages.value.push({
      type: 'ai',
      content: conversation.initialMessage,
      timestamp: new Date()
    })
    
    // 滚动到底部
    await nextTick()
    scrollToBottom()
    
  } catch (error) {
    console.error('初始化对话失败:', error)
    ElMessage.error('初始化对话失败，请稍后重试')
  }
}

// 发送消息
const sendMessage = async () => {
  if (!inputMessage.value.trim() || isLoading.value) return
  
  const userMessage = inputMessage.value.trim()
  inputMessage.value = ''
  
  // 添加用户消息
  messages.value.push({
    type: 'user',
    content: userMessage,
    timestamp: new Date()
  })
  
  // 滚动到底部
  await nextTick()
  scrollToBottom()
  
  // 发送到AI
  isLoading.value = true
  
  try {
    await aiChatApi.sendMessage(userMessage, {
      onMessage: (response) => {
        // 添加AI回复
        messages.value.push({
          type: 'ai',
          content: response,
          timestamp: new Date()
        })
      },
      onComplete: async () => {
        isLoading.value = false
        // 滚动到底部
        await nextTick()
        scrollToBottom()
      },
      onError: (error) => {
        isLoading.value = false
        console.error('发送消息失败:', error)
        ElMessage.error(error.message || '发送消息失败，请稍后重试')
      }
    })
  } catch (error) {
    isLoading.value = false
    // 错误已在 onError 中处理
  }
}

// 处理回车键
const handleEnter = (event) => {
  if (event.shiftKey) {
    // Shift + Enter 换行
    return
  }
  // Enter 发送消息
  sendMessage()
}

// 清空对话
const clearChat = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要清空所有对话记录吗？此操作不可恢复。',
      '清空对话',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    messages.value = []
    // 重新初始化对话
    await initChat()
    
  } catch (error) {
    // 用户取消
  }
}

// 返回上一页
const goBack = () => {
  router.back()
}

// 滚动到底部
const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

// 格式化消息内容
const formatMessage = (content) => {
  // 简单的换行处理
  return content.replace(/\n/g, '<br>')
}

// 格式化时间
const formatTime = (timestamp) => {
  const now = new Date()
  const time = new Date(timestamp)
  const diff = now - time
  
  if (diff < 60000) { // 1分钟内
    return '刚刚'
  } else if (diff < 3600000) { // 1小时内
    return `${Math.floor(diff / 60000)}分钟前`
  } else if (diff < 86400000) { // 24小时内
    return time.toLocaleTimeString('zh-CN', { hour: '2-digit', minute: '2-digit' })
  } else {
    return time.toLocaleDateString('zh-CN', { month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' })
  }
}

// 监听消息变化，自动滚动
watch(messages, async () => {
  await nextTick()
  scrollToBottom()
}, { deep: true })

// 组件挂载时初始化
onMounted(() => {
  initChat()
})
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #f5f7fa;
}

.chat-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: white;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.chat-header h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #409eff;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message {
  display: flex;
  gap: 12px;
  max-width: 80%;
}

.message.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message.ai {
  align-self: flex-start;
}

.message-avatar {
  flex-shrink: 0;
}

.ai-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.message-content {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.message-text {
  padding: 12px 16px;
  border-radius: 18px;
  line-height: 1.5;
  word-wrap: break-word;
}

.message.user .message-text {
  background: #409eff;
  color: white;
  border-bottom-right-radius: 6px;
}

.message.ai .message-text {
  background: white;
  color: #303133;
  border: 1px solid #e4e7ed;
  border-bottom-left-radius: 6px;
}

.message-time {
  font-size: 12px;
  color: #909399;
  padding: 0 4px;
}

.message.user .message-time {
  text-align: right;
}

.typing-indicator {
  display: flex;
  gap: 4px;
  padding: 12px 16px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 18px;
  border-bottom-left-radius: 6px;
}

.typing-indicator span {
  width: 6px;
  height: 6px;
  background: #c0c4cc;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-indicator span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-indicator span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.5;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

.chat-input {
  padding: 16px 20px;
  background: white;
  border-top: 1px solid #e4e7ed;
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.message-input {
  flex: 1;
}

.send-btn {
  height: 40px;
  padding: 0 16px;
  border-radius: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .chat-messages {
    padding: 16px;
  }
  
  .message {
    max-width: 90%;
  }
  
  .chat-input {
    padding: 12px 16px;
  }
}

/* 滚动条样式 */
.chat-messages::-webkit-scrollbar {
  width: 6px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>