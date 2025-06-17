<template>
  <div class="chat-window-overlay" @click="handleOverlayClick">
    <div class="chat-window" @click.stop>
      <!-- 聊天窗口头部 -->
      <div class="chat-header">
        <div class="header-info">
          <div class="avatar">
            <el-icon><User /></el-icon>
          </div>
          <div class="title-info">
            <h3>食谱助手</h3>
            <span class="status">在线</span>
          </div>
        </div>
        <div class="header-actions">
          <el-button 
            type="text" 
            @click="clearChat"
            :icon="Delete"
            title="清空聊天"
          />
          <el-button 
            type="text" 
            @click="$emit('close-chat')"
            :icon="Close"
            title="关闭"
          />
        </div>
      </div>

      <!-- 聊天消息区域 -->
      <div class="chat-messages" ref="messagesContainer">
        <div v-if="!hasMessages" class="welcome-message">
          <div class="welcome-content">
            <el-icon class="welcome-icon"><ChatDotRound /></el-icon>
            <h4>欢迎使用个性化食谱助手！</h4>
            <p>我可以帮您：</p>
            <ul>
              <li>🍳 推荐个性化菜谱</li>
              <li>🥗 根据食材搭配菜谱</li>
              <li>⏰ 制定用餐计划</li>
              <li>🍎 提供营养建议</li>
            </ul>
            <p>请告诉我您想要什么样的菜谱吧！</p>
          </div>
        </div>

        <!-- 消息列表 -->
        <div 
          v-for="message in messages" 
          :key="message.id"
          class="message-item"
          :class="`message-${message.sender}`"
        >
          <div class="message-avatar">
            <el-icon v-if="message.sender === 'user'">
              <User />
            </el-icon>
            <el-icon v-else>
              <ChatDotRound />
            </el-icon>
          </div>
          
          <div class="message-content">
            <div class="message-bubble">
              <div v-if="message.type === 'recipe'" class="recipe-message">
                <RecipeCard :recipe="message.recipe" />
              </div>
              <div v-else class="text-message">
                <span v-html="formatMessage(message.content)"></span>
                <div v-if="message.isStreaming" class="typing-indicator">
                  <span></span>
                  <span></span>
                  <span></span>
                </div>
              </div>
            </div>
            <div class="message-time">
              {{ formatTime(message.timestamp) }}
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
            placeholder="输入您的问题..."
            @keydown.enter="handleEnter"
            :disabled="isLoading"
            class="message-input"
          />
          <el-button
            type="primary"
            :icon="isLoading ? Loading : Promotion"
            :loading="isLoading"
            @click="sendMessage"
            :disabled="!inputMessage.trim()"
            class="send-button"
          >
            发送
          </el-button>
        </div>
        
        <!-- 快捷回复 -->
        <div v-if="!hasMessages" class="quick-replies">
          <el-tag 
            v-for="reply in quickReplies"
            :key="reply"
            @click="selectQuickReply(reply)"
            class="quick-reply-tag"
          >
            {{ reply }}
          </el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, onMounted, watch } from 'vue'
import { 
  User, 
  ChatDotRound, 
  Close, 
  Delete, 
  Promotion, 
  Loading 
} from '@element-plus/icons-vue'
import { useChatStore } from '../store'
import { ElMessage } from 'element-plus'
import RecipeCard from './RecipeCard.vue'

const emit = defineEmits(['close-chat'])

// Store
const chatStore = useChatStore()

// 响应式数据
const inputMessage = ref('')
const messagesContainer = ref(null)

// 计算属性
const messages = computed(() => chatStore.messages)
const isLoading = computed(() => chatStore.isLoading)
const hasMessages = computed(() => chatStore.hasMessages)

// 快捷回复选项
const quickReplies = ref([
  '推荐一道简单的家常菜',
  '我想要低卡路里的菜谱',
  '有什么素食菜谱推荐',
  '30分钟内能做完的菜',
  '适合小孩的营养菜谱'
])

// 发送消息
const sendMessage = async () => {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return

  try {
    await chatStore.sendMessage(message)
    inputMessage.value = ''
    scrollToBottom()
  } catch (error) {
    ElMessage.error('发送消息失败，请稍后重试')
  }
}

// 处理回车键
const handleEnter = (e) => {
  if (e.shiftKey) {
    // Shift + Enter 换行
    return
  }
  e.preventDefault()
  sendMessage()
}

// 选择快捷回复
const selectQuickReply = (reply) => {
  inputMessage.value = reply
  sendMessage()
}

// 清空聊天
const clearChat = () => {
  chatStore.clearMessages()
  ElMessage.success('聊天记录已清空')
}

// 处理遮罩点击
const handleOverlayClick = () => {
  emit('close-chat')
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 格式化消息内容
const formatMessage = (content) => {
  if (!content) return ''
  
  // 简单的markdown格式化
  return content
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/\n/g, '<br>')
}

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return date.toLocaleTimeString('zh-CN', {
    hour: '2-digit',
    minute: '2-digit'
  })
}

// 监听消息变化，自动滚动
watch(
  () => messages.value.length,
  () => {
    scrollToBottom()
  }
)

// 监听流式消息更新
watch(
  () => chatStore.currentStreamMessage,
  () => {
    scrollToBottom()
  }
)

onMounted(() => {
  scrollToBottom()
})
</script>

<style scoped>
.chat-window-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 2000;
  padding: 20px;
}

.chat-window {
  width: 100%;
  max-width: 500px;
  height: 600px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chat-header {
  padding: 16px 20px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

.title-info h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.status {
  font-size: 12px;
  opacity: 0.8;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.header-actions .el-button {
  color: white;
  border: none;
}

.header-actions .el-button:hover {
  background: rgba(255, 255, 255, 0.1);
}

.chat-messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f8f9fa;
}

.welcome-message {
  text-align: center;
  padding: 40px 20px;
}

.welcome-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  box-shadow: var(--shadow-light);
}

.welcome-icon {
  font-size: 48px;
  color: var(--primary-color);
  margin-bottom: 16px;
}

.welcome-content h4 {
  margin: 0 0 16px 0;
  color: var(--text-primary);
}

.welcome-content p {
  margin: 12px 0;
  color: var(--text-regular);
}

.welcome-content ul {
  text-align: left;
  margin: 16px 0;
  padding-left: 0;
  list-style: none;
}

.welcome-content li {
  padding: 4px 0;
  color: var(--text-regular);
}

.message-item {
  display: flex;
  margin-bottom: 16px;
  align-items: flex-start;
  gap: 12px;
}

.message-user {
  flex-direction: row-reverse;
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 14px;
  flex-shrink: 0;
}

.message-user .message-avatar {
  background: var(--chat-bubble-user);
  color: white;
}

.message-ai .message-avatar {
  background: var(--primary-color);
  color: white;
}

.message-content {
  max-width: 70%;
}

.message-bubble {
  padding: 12px 16px;
  border-radius: 18px;
  word-wrap: break-word;
}

.message-user .message-bubble {
  background: var(--chat-bubble-user);
  color: white;
  border-bottom-right-radius: 4px;
}

.message-ai .message-bubble {
  background: var(--chat-bubble-ai);
  color: var(--text-primary);
  border: 1px solid var(--border-color);
  border-bottom-left-radius: 4px;
}

.message-time {
  font-size: 11px;
  color: var(--text-placeholder);
  margin-top: 4px;
  text-align: center;
}

.typing-indicator {
  display: inline-flex;
  gap: 4px;
  margin-left: 8px;
}

.typing-indicator span {
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: currentColor;
  opacity: 0.4;
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
    opacity: 0.4;
  }
  30% {
    opacity: 1;
  }
}

.chat-input {
  padding: 16px 20px;
  background: white;
  border-top: 1px solid var(--border-color);
}

.input-container {
  display: flex;
  gap: 12px;
  align-items: flex-end;
}

.message-input {
  flex: 1;
}

.send-button {
  flex-shrink: 0;
}

.quick-replies {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.quick-reply-tag {
  cursor: pointer;
  transition: all 0.2s;
}

.quick-reply-tag:hover {
  background: var(--primary-color);
  color: white;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .chat-window-overlay {
    padding: 0;
  }
  
  .chat-window {
    width: 100%;
    height: 100%;
    border-radius: 0;
    max-width: none;
  }
  
  .message-content {
    max-width: 85%;
  }
  
  .quick-replies {
    flex-direction: column;
  }
}
</style>