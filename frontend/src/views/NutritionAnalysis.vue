<template>
  <div class="nutrition-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>🍎 营养分析</h1>
      <p>上传食物图片或输入食物描述，获取详细的营养成分分析</p>
    </div>

    <!-- 分析方式选择 -->
    <div class="analysis-tabs">
      <button 
        :class="['tab-btn', { active: activeTab === 'upload' }]"
        @click="activeTab = 'upload'"
      >
        📷 图片上传
      </button>
      <button 
        :class="['tab-btn', { active: activeTab === 'text' }]"
        @click="activeTab = 'text'"
      >
        ✏️ 文字描述
      </button>
    </div>

    <!-- 图片上传分析 -->
    <div v-if="activeTab === 'upload'" class="upload-section">
      <div class="upload-area" :class="{ 'drag-over': isDragOver }" 
           @drop="handleDrop" 
           @dragover="handleDragOver" 
           @dragleave="handleDragLeave"
           @click="triggerFileInput">
        <input 
          ref="fileInput" 
          type="file" 
          accept="image/*" 
          @change="handleFileSelect" 
          style="display: none"
        >
        
        <div v-if="!selectedFile" class="upload-placeholder">
          <div class="upload-icon">📁</div>
          <h3>点击或拖拽上传食物图片</h3>
          <p>支持 JPG、PNG、WEBP 格式，最大 10MB</p>
        </div>
        
        <div v-else class="file-preview">
          <img :src="previewUrl" alt="预览图片" class="preview-image">
          <div class="file-info">
            <p class="file-name">{{ selectedFile.name }}</p>
            <p class="file-size">{{ formatFileSize(selectedFile.size) }}</p>
            <button @click.stop="clearFile" class="clear-btn">❌ 重新选择</button>
          </div>
        </div>
      </div>
      
      <div v-if="selectedFile" class="upload-actions">
        <button 
          @click="analyzeFile" 
          :disabled="isAnalyzing"
          class="analyze-btn"
        >
          <span v-if="isAnalyzing">🔄 分析中...</span>
          <span v-else>🔍 开始分析</span>
        </button>
      </div>
      
      <!-- 上传进度 -->
      <div v-if="uploadProgress > 0 && uploadProgress < 100" class="progress-bar">
        <div class="progress-fill" :style="{ width: uploadProgress + '%' }"></div>
        <span class="progress-text">{{ uploadProgress }}%</span>
      </div>
    </div>

    <!-- 文字描述分析 -->
    <div v-if="activeTab === 'text'" class="text-section">
      <div class="text-input-area">
        <textarea 
          v-model="textInput"
          placeholder="请描述您要分析的食物，例如：一碗白米饭、一个苹果、一份宫保鸡丁等..."
          rows="4"
          class="text-input"
        ></textarea>
        
        <div class="text-actions">
          <button 
            @click="analyzeText" 
            :disabled="isAnalyzing || !textInput.trim()"
            class="analyze-btn"
          >
            <span v-if="isAnalyzing">🔄 分析中...</span>
            <span v-else>🔍 开始分析</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 分析结果 -->
    <div v-if="analysisResult" class="result-section">
      <div class="result-header">
        <h2>📊 分析结果</h2>
        <div class="result-actions">
          <button @click="saveToHistory" class="save-btn">💾 保存</button>
          <button @click="clearResult" class="clear-btn">🗑️清除</button>
        </div>
      </div>
      
      <div class="result-content">
        <div class="analysis-text" v-html="formatAnalysisText(analysisResult.analysis)"></div>
        
        <div class="result-meta">
          <p><strong>分析时间:</strong> {{ formatTime(analysisResult.timestamp) }}</p>
          <p v-if="analysisResult.fileName"><strong>文件名:</strong> {{ analysisResult.fileName }}</p>
          <p v-if="analysisResult.inputText"><strong>输入内容:</strong> {{ analysisResult.inputText }}</p>
        </div>
      </div>
    </div>

    <!-- 历史记录 -->
    <div class="history-section">
      <div class="history-header">
        <h2>📋 分析历史</h2>
        <button @click="clearHistory" class="clear-history-btn">🗑️ 清空历史</button>
      </div>
      
      <div v-if="analysisHistory.length === 0" class="empty-history">
        <p>暂无分析历史</p>
      </div>
      
      <div v-else class="history-list">
        <div 
          v-for="(item, index) in analysisHistory" 
          :key="index"
          class="history-item"
          @click="viewHistoryItem(item)"
        >
          <div class="history-preview">
            <div class="history-type">
              {{ item.fileName ? '📷' : '✏️' }}
            </div>
            <div class="history-content">
              <p class="history-title">
                {{ item.fileName || item.inputText?.substring(0, 30) + '...' }}
              </p>
              <p class="history-time">{{ formatTime(item.timestamp) }}</p>
            </div>
          </div>
          <div class="history-actions">
            <button @click.stop="deleteHistoryItem(index)" class="delete-btn">❌</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 错误提示 -->
    <div v-if="errorMessage" class="error-message">
      <p>❌ {{ errorMessage }}</p>
      <button @click="clearError" class="close-error">✖️</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { nutritionApi } from '@/api/nutrition'

// 响应式数据
const activeTab = ref('upload')
const selectedFile = ref(null)
const previewUrl = ref('')
const textInput = ref('')
const isAnalyzing = ref(false)
const uploadProgress = ref(0)
const analysisResult = ref(null)
const analysisHistory = ref([])
const errorMessage = ref('')
const isDragOver = ref(false)

// 文件输入引用
const fileInput = ref(null)

// 页面加载时获取历史记录
onMounted(async () => {
  await loadAnalysisHistory()
})

// 触发文件选择
const triggerFileInput = () => {
  fileInput.value?.click()
}

// 处理文件选择
const handleFileSelect = (event) => {
  const file = event.target.files[0]
  if (file) {
    setSelectedFile(file)
  }
}

// 处理拖拽上传
const handleDrop = (event) => {
  event.preventDefault()
  isDragOver.value = false
  
  const files = event.dataTransfer.files
  if (files.length > 0) {
    setSelectedFile(files[0])
  }
}

const handleDragOver = (event) => {
  event.preventDefault()
  isDragOver.value = true
}

const handleDragLeave = () => {
  isDragOver.value = false
}

// 设置选中的文件
const setSelectedFile = (file) => {
  // 检查文件类型
  if (!file.type.startsWith('image/')) {
    showError('请选择图片文件')
    return
  }
  
  selectedFile.value = file
  
  // 创建预览URL
  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target.result
  }
  reader.readAsDataURL(file)
}

// 清除选中的文件
const clearFile = () => {
  selectedFile.value = null
  previewUrl.value = ''
  uploadProgress.value = 0
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

// 分析文件
const analyzeFile = async () => {
  if (!selectedFile.value) {
    showError('请先选择文件')
    return
  }
  
  isAnalyzing.value = true
  uploadProgress.value = 0
  clearError()
  
  try {
    const result = await nutritionApi.analyzeFile(selectedFile.value, {
      onProgress: (progress) => {
        uploadProgress.value = progress
      },
      onComplete: (result) => {
        analysisResult.value = result
        uploadProgress.value = 100
      },
      onError: (error) => {
        showError(error.message)
      }
    })
    
    // 保存到历史记录
    await nutritionApi.saveAnalysisToHistory(result)
    await loadAnalysisHistory()
    
  } catch (error) {
    showError(error.message || '分析失败，请重试')
  } finally {
    isAnalyzing.value = false
  }
}

// 分析文本
const analyzeText = async () => {
  if (!textInput.value.trim()) {
    showError('请输入要分析的食物描述')
    return
  }
  
  isAnalyzing.value = true
  clearError()
  
  try {
    const result = await nutritionApi.analyzeText(textInput.value, {
      onComplete: (result) => {
        analysisResult.value = result
      },
      onError: (error) => {
        showError(error.message)
      }
    })
    
    // 保存到历史记录
    await nutritionApi.saveAnalysisToHistory(result)
    await loadAnalysisHistory()
    
  } catch (error) {
    showError(error.message || '分析失败，请重试')
  } finally {
    isAnalyzing.value = false
  }
}

// 加载分析历史
const loadAnalysisHistory = async () => {
  try {
    analysisHistory.value = await nutritionApi.getAnalysisHistory()
  } catch (error) {
    console.error('加载历史记录失败:', error)
  }
}

// 保存到历史
const saveToHistory = async () => {
  if (!analysisResult.value) return
  
  try {
    await nutritionApi.saveAnalysisToHistory(analysisResult.value)
    await loadAnalysisHistory()
    showSuccess('已保存到历史记录')
  } catch (error) {
    showError('保存失败')
  }
}

// 清除结果
const clearResult = () => {
  analysisResult.value = null
  uploadProgress.value = 0
}

// 查看历史项目
const viewHistoryItem = (item) => {
  analysisResult.value = item
}

// 删除历史项目
const deleteHistoryItem = async (index) => {
  analysisHistory.value.splice(index, 1)
  try {
    localStorage.setItem('nutrition_analysis_history', JSON.stringify(analysisHistory.value))
  } catch (error) {
    console.error('删除历史记录失败:', error)
  }
}

// 清空历史
const clearHistory = async () => {
  if (confirm('确定要清空所有历史记录吗？')) {
    try {
      await nutritionApi.clearAnalysisHistory()
      analysisHistory.value = []
    } catch (error) {
      showError('清空历史记录失败')
    }
  }
}

// 格式化文件大小
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

// 格式化时间
const formatTime = (timestamp) => {
  return new Date(timestamp).toLocaleString('zh-CN')
}

// 格式化分析文本
const formatAnalysisText = (text) => {
  return text.replace(/\n/g, '<br>')
}

// 显示错误
const showError = (message) => {
  errorMessage.value = message
  setTimeout(() => {
    errorMessage.value = ''
  }, 5000)
}

// 清除错误
const clearError = () => {
  errorMessage.value = ''
}

// 显示成功消息
const showSuccess = (message) => {
  // 可以使用 Element Plus 的消息组件
  console.log('Success:', message)
}
</script>

<style scoped>
.nutrition-analysis {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
}

.page-header {
  text-align: center;
  margin-bottom: 40px;
}

.page-header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 1.1rem;
  color: #7f8c8d;
}

.analysis-tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
  gap: 10px;
}

.tab-btn {
  padding: 12px 24px;
  border: 2px solid #e74c3c;
  background: white;
  color: #e74c3c;
  border-radius: 25px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
  transition: all 0.3s ease;
}

.tab-btn.active {
  background: #e74c3c;
  color: white;
}

.tab-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(231, 76, 60, 0.3);
}

.upload-section, .text-section {
  margin-bottom: 30px;
}

.upload-area {
  border: 3px dashed #bdc3c7;
  border-radius: 15px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  background: #f8f9fa;
}

.upload-area:hover, .upload-area.drag-over {
  border-color: #e74c3c;
  background: #fff5f5;
}

.upload-placeholder {
  color: #7f8c8d;
}

.upload-icon {
  font-size: 4rem;
  margin-bottom: 20px;
}

.upload-placeholder h3 {
  margin: 0 0 10px 0;
  color: #2c3e50;
}

.file-preview {
  display: flex;
  align-items: center;
  gap: 20px;
  text-align: left;
}

.preview-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 10px;
  border: 2px solid #e74c3c;
}

.file-info {
  flex: 1;
}

.file-name {
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 5px 0;
}

.file-size {
  color: #7f8c8d;
  margin: 0 0 15px 0;
}

.clear-btn {
  padding: 8px 16px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 0.9rem;
}

.text-input-area {
  background: white;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.text-input {
  width: 100%;
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  padding: 15px;
  font-size: 1rem;
  resize: vertical;
  min-height: 120px;
  font-family: inherit;
}

.text-input:focus {
  outline: none;
  border-color: #e74c3c;
}

.upload-actions, .text-actions {
  text-align: center;
  margin-top: 20px;
}

.analyze-btn {
  padding: 15px 40px;
  background: linear-gradient(135deg, #e74c3c, #c0392b);
  color: white;
  border: none;
  border-radius: 25px;
  cursor: pointer;
  font-size: 1.1rem;
  font-weight: 600;
  transition: all 0.3s ease;
}

.analyze-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(231, 76, 60, 0.4);
}

.analyze-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.progress-bar {
  position: relative;
  width: 100%;
  height: 20px;
  background: #ecf0f1;
  border-radius: 10px;
  overflow: hidden;
  margin-top: 20px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #e74c3c, #c0392b);
  transition: width 0.3s ease;
}

.progress-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  font-weight: 600;
  font-size: 0.9rem;
}

.result-section {
  background: white;
  border-radius: 15px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #ecf0f1;
}

.result-header h2 {
  color: #2c3e50;
  margin: 0;
}

.result-actions {
  display: flex;
  gap: 10px;
}

.save-btn {
  padding: 8px 16px;
  background: #27ae60;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.analysis-text {
  line-height: 1.8;
  color: #2c3e50;
  margin-bottom: 20px;
  font-size: 1.05rem;
}

.result-meta {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 10px;
  border-left: 4px solid #e74c3c;
}

.result-meta p {
  margin: 5px 0;
  color: #7f8c8d;
}

.history-section {
  background: white;
  border-radius: 15px;
  padding: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.history-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #ecf0f1;
}

.history-header h2 {
  color: #2c3e50;
  margin: 0;
}

.clear-history-btn {
  padding: 8px 16px;
  background: #e67e22;
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
}

.empty-history {
  text-align: center;
  color: #7f8c8d;
  padding: 40px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.history-item:hover {
  background: #e8f4fd;
  transform: translateX(5px);
}

.history-preview {
  display: flex;
  align-items: center;
  gap: 15px;
  flex: 1;
}

.history-type {
  font-size: 1.5rem;
}

.history-content {
  flex: 1;
}

.history-title {
  font-weight: 600;
  color: #2c3e50;
  margin: 0 0 5px 0;
}

.history-time {
  color: #7f8c8d;
  font-size: 0.9rem;
  margin: 0;
}

.history-actions {
  display: flex;
  gap: 10px;
}

.delete-btn {
  padding: 5px 10px;
  background: #e74c3c;
  color: white;
  border: none;
  border-radius: 15px;
  cursor: pointer;
  font-size: 0.8rem;
}

.error-message {
  position: fixed;
  top: 20px;
  right: 20px;
  background: #e74c3c;
  color: white;
  padding: 15px 20px;
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(231, 76, 60, 0.3);
  z-index: 1000;
  display: flex;
  align-items: center;
  gap: 15px;
  max-width: 400px;
}

.close-error {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 1.2rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nutrition-analysis {
    padding: 15px;
  }
  
  .page-header h1 {
    font-size: 2rem;
  }
  
  .analysis-tabs {
    flex-direction: column;
    align-items: center;
  }
  
  .file-preview {
    flex-direction: column;
    text-align: center;
  }
  
  .result-header, .history-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .history-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
}
</style>