<template>
  <div class="nutrition-analysis">
    <!-- 分析方式选择 -->
    <div class="analysis-tabs">
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="📁 文件上传" name="file">
          <div class="upload-section">
            <el-upload
              ref="uploadRef"
              class="upload-dragger"
              drag
              :auto-upload="false"
              :on-change="handleFileChange"
              :before-upload="beforeUpload"
              :show-file-list="false"
              accept="image/*,.txt,.pdf"
            >
              <div class="upload-content">
                <div class="upload-icon">📤</div>
                <div class="upload-text">
                  <p>将文件拖拽到此处，或<em>点击上传</em></p>
                  <p class="upload-tip">支持图片、文本文件和PDF，大小不超过10MB</p>
                </div>
              </div>
            </el-upload>
            
            <!-- 已选择的文件 -->
            <div v-if="selectedFile" class="selected-file">
              <div class="file-info">
                <span class="file-icon">📄</span>
                <div class="file-details">
                  <div class="file-name">{{ selectedFile.name }}</div>
                  <div class="file-size">{{ formatFileSize(selectedFile.size) }}</div>
                </div>
                <el-button 
                  type="danger" 
                  size="small" 
                  text 
                  @click="removeFile"
                >
                  ❌
                </el-button>
              </div>
            </div>
            
            <!-- 上传按钮 -->
            <div class="upload-actions">
              <el-button 
                type="primary" 
                size="large"
                :loading="isAnalyzing"
                :disabled="!selectedFile"
                @click="analyzeFile"
              >
                <span v-if="!isAnalyzing">🔍 开始分析</span>
                <span v-else>分析中...</span>
              </el-button>
            </div>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="✏️ 文本输入" name="text">
          <div class="text-section">
            <el-input
              v-model="inputText"
              type="textarea"
              :rows="6"
              placeholder="请输入要分析的食物信息，例如：\n- 一碗白米饭\n- 100克鸡胸肉\n- 一个苹果\n- 今天吃了什么..."
              maxlength="2000"
              show-word-limit
              class="text-input"
            />
            
            <div class="text-actions">
              <el-button 
                type="primary" 
                size="large"
                :loading="isAnalyzing"
                :disabled="!inputText.trim()"
                @click="analyzeText"
              >
                <span v-if="!isAnalyzing">🔍 开始分析</span>
                <span v-else>分析中...</span>
              </el-button>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 分析进度 -->
    <div v-if="isAnalyzing" class="analysis-progress">
      <el-progress 
        :percentage="uploadProgress" 
        :status="uploadProgress === 100 ? 'success' : undefined"
        :stroke-width="8"
      />
      <p class="progress-text">
        {{ uploadProgress < 100 ? '正在上传文件...' : '正在分析营养成分...' }}
      </p>
    </div>
    
    <!-- 分析结果 -->
    <div v-if="analysisResult" class="analysis-result">
      <div class="result-header">
        <h3>📊 营养分析结果</h3>
        <div class="result-actions">
          <el-button size="small" @click="saveToHistory">
            💾 保存到历史
          </el-button>
          <el-button size="small" @click="continueChat">
            📤 继续对话
          </el-button>
        </div>
      </div>
      
      <div class="result-content">
        <div class="result-info">
          <div v-if="analysisResult.fileName" class="analyzed-file">
            <span class="info-label">分析文件：</span>
            <span class="info-value">{{ analysisResult.fileName }}</span>
          </div>
          <div v-if="analysisResult.inputText" class="analyzed-text">
            <span class="info-label">分析内容：</span>
            <span class="info-value">{{ analysisResult.inputText }}</span>
          </div>
          <div class="analysis-time">
            <span class="info-label">分析时间：</span>
            <span class="info-value">{{ formatTime(analysisResult.timestamp) }}</span>
          </div>
        </div>
        
        <div class="analysis-text">
          <div class="markdown-content" v-html="formatAnalysisText(analysisResult.analysis)"></div>
        </div>
      </div>
    </div>
    
    <!-- 分析历史 -->
    <div class="analysis-history">
      <div class="history-header">
        <h3>📋 分析历史</h3>
        <el-button size="small" @click="loadHistory">
          🔄 刷新
        </el-button>
        <el-button size="small" type="danger" @click="clearHistory">
          🗑️ 清空
        </el-button>
      </div>
      
      <div v-if="historyList.length === 0" class="empty-history">
        <p>暂无分析历史</p>
      </div>
      
      <div v-else class="history-list">
        <div 
          v-for="(item, index) in historyList" 
          :key="index"
          class="history-item"
          @click="viewHistoryItem(item)"
        >
          <div class="history-info">
            <div class="history-title">
              {{ item.fileName || '文本分析' }}
            </div>
            <div class="history-time">
              {{ formatTime(item.timestamp) }}
            </div>
          </div>
          <div class="history-preview">
            {{ item.analysis.substring(0, 100) }}...
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { nutritionApi } from '@/api/nutrition'
import { aiChatApi } from '@/api/only-chat'

// 响应式数据
const router = useRouter()
const activeTab = ref('file')
const selectedFile = ref(null)
const inputText = ref('')
const isAnalyzing = ref(false)
const uploadProgress = ref(0)
const analysisResult = ref(null)
const historyList = ref([])
const uploadRef = ref(null)

// 组件挂载时加载历史记录
onMounted(() => {
  loadHistory()
})

// 标签页切换
const handleTabChange = (tabName) => {
  // 清除之前的结果
  analysisResult.value = null
  uploadProgress.value = 0
  
  if (tabName === 'file') {
    inputText.value = ''
  } else {
    selectedFile.value = null
  }
}

// 文件选择处理
const handleFileChange = (file) => {
  selectedFile.value = file.raw
}

// 文件上传前检查
const beforeUpload = (file) => {
  const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg', 'text/plain', 'application/pdf']
  const isValidType = allowedTypes.includes(file.type)
  const isValidSize = file.size <= 10 * 1024 * 1024 // 10MB
  
  if (!isValidType) {
    ElMessage.error('不支持的文件类型，请上传图片、文本或PDF文件')
    return false
  }
  
  if (!isValidSize) {
    ElMessage.error('文件大小不能超过10MB')
    return false
  }
  
  return false // 阻止自动上传
}

// 移除文件
const removeFile = () => {
  selectedFile.value = null
  uploadRef.value?.clearFiles()
}

// 分析文件
const analyzeFile = async () => {
  if (!selectedFile.value) {
    ElMessage.warning('请先选择要分析的文件')
    return
  }
  
  isAnalyzing.value = true
  uploadProgress.value = 0
  analysisResult.value = null
  
  try {
    const result = await nutritionApi.analyzeFile(selectedFile.value, {
      onProgress: (progress) => {
        uploadProgress.value = progress
      },
      onComplete: (result) => {
        analysisResult.value = result
        ElMessage.success('营养分析完成！')
      },
      onError: (error) => {
        ElMessage.error(error.message || '分析失败，请重试')
      }
    })
    
    // 自动保存到历史
    await nutritionApi.saveAnalysisToHistory(result)
    await loadHistory()
    
  } catch (error) {
    console.error('File analysis error:', error)
    ElMessage.error(error.message || '分析失败，请重试')
  } finally {
    isAnalyzing.value = false
  }
}

// 分析文本
const analyzeText = async () => {
  if (!inputText.value.trim()) {
    ElMessage.warning('请输入要分析的食物信息')
    return
  }
  
  isAnalyzing.value = true
  analysisResult.value = null
  
  try {
    const result = await nutritionApi.analyzeText(inputText.value, {
      onComplete: (result) => {
        analysisResult.value = result
        ElMessage.success('营养分析完成！')
      },
      onError: (error) => {
        ElMessage.error(error.message || '分析失败，请重试')
      }
    })
    
    // 自动保存到历史
    await nutritionApi.saveAnalysisToHistory(result)
    await loadHistory()
    
  } catch (error) {
    console.error('Text analysis error:', error)
    ElMessage.error(error.message || '分析失败，请重试')
  } finally {
    isAnalyzing.value = false
  }
}

// 保存到历史
const saveToHistory = async () => {
  if (!analysisResult.value) return
  
  try {
    await nutritionApi.saveAnalysisToHistory(analysisResult.value)
    await loadHistory()
    ElMessage.success('已保存到历史记录')
  } catch (error) {
    ElMessage.error('保存失败')
  }
}

// 继续对话
const continueChat = async () => {
  if (!analysisResult.value) return
  
  try {
    // 弹出确认对话框
    await ElMessageBox.confirm(
      '是否要基于当前营养分析结果继续智能对话？系统将为您提供个性化的营养建议和问答服务。',
      '继续对话',
      {
        confirmButtonText: '开始对话',
        cancelButtonText: '取消',
        type: 'info',
        customClass: 'continue-chat-dialog'
      }
    )
    
    // 用户确认后，准备对话数据
    const chatContext = {
      analysisResult: analysisResult.value.analysis,
      fileName: analysisResult.value.fileName || '营养分析',
      timestamp: new Date().toISOString()
    }
    
    // 跳转到对话页面，并传递分析结果作为初始上下文
    router.push({
      name: 'Chat',
      query: {
        context: 'nutrition-analysis',
        data: JSON.stringify(chatContext)
      }
    })
    
  } catch (error) {
    // 用户取消或其他错误
    if (error !== 'cancel') {
      console.error('继续对话失败:', error)
      ElMessage.error('启动对话失败，请稍后重试')
    }
  }
}

// 加载历史记录
const loadHistory = async () => {
  try {
    historyList.value = await nutritionApi.getAnalysisHistory()
  } catch (error) {
    console.error('Load history error:', error)
  }
}

// 清空历史记录
const clearHistory = async () => {
  try {
    await ElMessageBox.confirm('确定要清空所有分析历史吗？', '确认删除', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await nutritionApi.clearAnalysisHistory()
    historyList.value = []
    ElMessage.success('历史记录已清空')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('清空失败')
    }
  }
}

// 查看历史项目
const viewHistoryItem = (item) => {
  analysisResult.value = item
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B'
  const k = 1024
  const sizes = ['B', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化时间
const formatTime = (timestamp) => {
  const date = new Date(timestamp)
  return date.toLocaleString('zh-CN')
}

// 格式化分析文本
const formatAnalysisText = (text) => {
  // 简单的 Markdown 格式化
  return text
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/\*(.*?)\*/g, '<em>$1</em>')
    .replace(/\n/g, '<br>')
    .replace(/###\s(.*?)(<br>|$)/g, '<h3>$1</h3>')
    .replace(/##\s(.*?)(<br>|$)/g, '<h2>$1</h2>')
    .replace(/#\s(.*?)(<br>|$)/g, '<h1>$1</h1>')
}
</script>

<style scoped>
.nutrition-analysis {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.analysis-tabs {
  margin-bottom: 30px;
}

.upload-section {
  text-align: center;
}

.upload-dragger {
  width: 100%;
}

.upload-content {
  padding: 40px 20px;
}

.upload-icon {
  font-size: 48px;
  margin-bottom: 16px;
}

.upload-text p {
  margin: 8px 0;
  font-size: 16px;
  color: #606266;
}

.upload-tip {
  font-size: 14px;
  color: #909399;
}

.selected-file {
  margin: 20px 0;
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background-color: #f8f9fa;
}

.file-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.file-icon {
  font-size: 24px;
}

.file-details {
  flex: 1;
  text-align: left;
}

.file-name {
  font-weight: 500;
  color: #303133;
}

.file-size {
  font-size: 14px;
  color: #909399;
}

.upload-actions,
.text-actions {
  margin-top: 20px;
}

.text-section {
  text-align: center;
}

.text-input {
  margin-bottom: 20px;
}

.analysis-progress {
  margin: 30px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.progress-text {
  margin-top: 12px;
  color: #606266;
  font-size: 14px;
}

.analysis-result {
  margin: 30px 0;
  padding: 24px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  background-color: #fff;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.result-header h3 {
  margin: 0;
  color: #303133;
}

.result-actions {
  display: flex;
  gap: 8px;
}

.result-info {
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.result-info > div {
  margin-bottom: 8px;
}

.result-info > div:last-child {
  margin-bottom: 0;
}

.info-label {
  font-weight: 500;
  color: #606266;
  margin-right: 8px;
}

.info-value {
  color: #303133;
}

.analysis-text {
  line-height: 1.6;
}

.markdown-content {
  color: #303133;
}

.markdown-content h1,
.markdown-content h2,
.markdown-content h3 {
  margin: 16px 0 8px 0;
  color: #409eff;
}

.markdown-content strong {
  color: #e6a23c;
}

.analysis-history {
  margin-top: 40px;
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.history-header h3 {
  margin: 0;
  color: #303133;
}

.empty-history {
  text-align: center;
  padding: 40px;
  color: #909399;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  padding: 16px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.history-item:hover {
  border-color: #409eff;
  background-color: #f0f9ff;
}

.history-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.history-title {
  font-weight: 500;
  color: #303133;
}

.history-time {
  font-size: 14px;
  color: #909399;
}

.history-preview {
  font-size: 14px;
  color: #606266;
  line-height: 1.4;
}

@media (max-width: 768px) {
  .nutrition-analysis {
    padding: 16px;
  }
  
  .result-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .result-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  .history-header {
    flex-direction: column;
    gap: 12px;
    align-items: flex-start;
  }
  
  .history-info {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}
</style>