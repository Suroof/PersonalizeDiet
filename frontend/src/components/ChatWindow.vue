<template>
  <div class="chat-window-overlay" @click="handleOverlayClick">
    <div class="chat-window" @click.stop>
      <!-- 聊天窗口头部 -->
      <div class="chat-header">
        <div class="header-info">
          <div class="avatar">
            <el-icon>
              <User />
            </el-icon>
          </div>
          <div class="title-info">
            <h3>食谱助手</h3>
            <span class="status">在线</span>
          </div>
        </div>
        <div class="header-actions">
          <el-button type="text" @click="clearChat" :icon="Delete" title="清空聊天" />
          <el-button type="text" @click="$emit('close-chat')" :icon="Close" title="关闭" />
        </div>
      </div>

      <!-- 聊天消息区域 -->
      <div class="chat-messages" ref="messagesContainer">
        <div v-if="!hasMessages" class="welcome-message">
          <div class="welcome-content">
            <el-icon class="welcome-icon">
              <ChatDotRound />
            </el-icon>
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
        <div v-for="message in messages" :key="message.id" class="message-item" :class="`message-${message.sender}`">
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
          <el-input v-model="inputMessage" type="textarea" :rows="1" :autosize="{ minRows: 1, maxRows: 4 }"
            placeholder="输入您的问题..." @keydown.enter="handleEnter" :disabled="isLoading" class="message-input" />
          <el-button type="primary" :icon="isLoading ? Loading : Promotion" :loading="isLoading" @click="sendMessage"
            :disabled="!inputMessage.trim()" class="send-button">
            发送
          </el-button>
        </div>

        <!-- 快捷回复 -->
        <div v-if="!hasMessages" class="quick-replies">
          <el-tag v-for="reply in quickReplies" :key="reply" @click="selectQuickReply(reply)" class="quick-reply-tag">
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

  let formatted = content

  // 处理表格
  formatted = formatted.replace(/\|(.+?)\|/g, (match, content) => {
    const cells = content.split('|').map(cell => cell.trim())
    return `<tr>${cells.map(cell => `<td>${cell}</td>`).join('')}</tr>`
  })

  // 包装表格
  formatted = formatted.replace(/(<tr>.*?<\/tr>)/gs, '<table class="message-table">$1</table>')

  // 处理食材链接格式：食材名: [食材名](URL) -> 食材名: 食材名（可点击）
  formatted = formatted.replace(/(\w+?):\s*\[([^\]]+)\]\(([^)]+?)\)(?![）])/g, '$1: <a href="$3" target="_blank" class="message-link">$2</a>');
  
  // 处理直接URL格式：食材名: URL -> 食材名: 食材名（可点击）
  formatted = formatted.replace(/(\w+?):\s*(https?:\/\/[^\s]+?)(?=\s|）|$)/g, '$1: <a href="$2" target="_blank" class="message-link">$1</a>');
  
  // 处理其他独立的URL（保持原样显示）
  formatted = formatted.replace(/(?<!href=")(https?:\/\/[^\s<>"]+)(?!")/g, '<a href="$1" target="_blank" class="message-link">$1</a>');

  // 处理标题
  formatted = formatted.replace(/^### (.+)$/gm, '<h3 class="message-h3">$1</h3>')
  formatted = formatted.replace(/^## (.+)$/gm, '<h2 class="message-h2">$1</h2>')
  formatted = formatted.replace(/^# (.+)$/gm, '<h1 class="message-h1">$1</h1>')

  // 处理列表
  formatted = formatted.replace(/^- (.+)$/gm, '<li class="message-li">$1</li>')
  formatted = formatted.replace(/(<li class="message-li">.*?<\/li>)/gs, '<ul class="message-ul">$1</ul>')

  // 处理有序列表
  formatted = formatted.replace(/^\d+\. (.+)$/gm, '<li class="message-oli">$1</li>')
  formatted = formatted.replace(/(<li class="message-oli">.*?<\/li>)/gs, '<ol class="message-ol">$1</ol>')

  // 处理分隔线
  formatted = formatted.replace(/^---$/gm, '<hr class="message-hr">')

  // 处理粗体和斜体
  formatted = formatted.replace(/\*\*(.*?)\*\*/g, '<strong class="message-bold">$1</strong>')
  formatted = formatted.replace(/\*(.*?)\*/g, '<em class="message-italic">$1</em>')

  // 处理代码块
  formatted = formatted.replace(/`([^`]+)`/g, '<code class="message-code">$1</code>')

  // 处理emoji和特殊符号
  formatted = formatted.replace(/✅/g, '<span class="message-emoji success">✅</span>')
  formatted = formatted.replace(/🌟/g, '<span class="message-emoji star">🌟</span>')
  formatted = formatted.replace(/💪/g, '<span class="message-emoji strong">💪</span>')
  formatted = formatted.replace(/🍽️/g, '<span class="message-emoji food">🍽️</span>')

  // 最后处理换行
  formatted = formatted.replace(/\n/g, '<br>')

  return formatted
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
  max-width: 800px;
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
  padding-left: 32px;
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

  0%,
  60%,
  100% {
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

/* 消息格式化样式 */
.message-table {
  width: 100%;
  border-collapse: collapse;
  margin: 8px 0;
  font-size: 14px;
}

.message-table td {
  padding: 8px 12px;
  border: 1px solid var(--border-color);
  background: rgba(0, 0, 0, 0.02);
}

.message-table tr:first-child td {
  background: var(--primary-color);
  color: white;
  font-weight: 600;
}

.message-link {
  color: var(--primary-color);
  text-decoration: none;
  border-bottom: 1px solid var(--primary-color);
  transition: all 0.2s;
}

.message-link:hover {
  background: var(--primary-color);
  color: white;
  padding: 2px 4px;
  border-radius: 4px;
  border-bottom: none;
}

.message-h1,
.message-h2,
.message-h3 {
  margin: 16px 0 8px 0;
  color: var(--text-primary);
  font-weight: 600;
}

.message-h1 {
  font-size: 20px;
  border-bottom: 2px solid var(--primary-color);
  padding-bottom: 4px;
}

.message-h2 {
  font-size: 18px;
  color: var(--primary-color);
}

.message-h3 {
  font-size: 16px;
  color: var(--secondary-color);
}

.message-ul,
.message-ol {
  margin: 4px 0 4px 8px;
  padding-left: 20px;
}

.message-li,
.message-oli {
  margin: 2px 0 2px 8px;
  line-height: 1.5;
}

.message-hr {
  border: none;
  height: 1px;
  background: linear-gradient(to right, transparent, var(--border-color), transparent);
  margin: 16px 0;
}

.message-bold {
  font-weight: 600;
  color: var(--text-primary);
}

.message-italic {
  font-style: italic;
  color: var(--text-regular);
}

.message-code {
  background: rgba(0, 0, 0, 0.05);
  padding: 2px 6px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 13px;
  color: var(--primary-color);
}

.message-emoji {
  font-size: 16px;
  margin: 0 2px;
}

.message-emoji.success {
  color: #67C23A;
}

.message-emoji.star {
  color: #E6A23C;
}

.message-emoji.strong {
  color: #F56C6C;
}

.message-emoji.food {
  color: #909399;
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

  .message-table {
    font-size: 12px;
  }

  .message-table td {
    padding: 6px 8px;
  }

  .message-h1 {
    font-size: 18px;
  }

  .message-h2 {
    font-size: 16px;
  }

  .message-h3 {
    font-size: 14px;
  }
}
</style>