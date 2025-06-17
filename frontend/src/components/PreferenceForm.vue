<template>
  <div class="preference-form-overlay" @click="handleOverlayClick">
    <div class="preference-form" @click.stop>
      <!-- 表单头部 -->
      <div class="form-header">
        <div class="header-info">
          <div class="avatar">
            <el-icon><User /></el-icon>
          </div>
          <div class="title-info">
            <h3>个性化食谱定制</h3>
            <span class="subtitle">告诉我们您的偏好，为您推荐最适合的食谱</span>
          </div>
        </div>
        <div class="header-actions">
          <el-button 
            type="text" 
            @click="$emit('close-form')"
            :icon="Close"
            title="关闭"
          />
        </div>
      </div>

      <!-- 表单内容 -->
      <div class="form-content">
        <el-form 
          ref="formRef" 
          :model="formData" 
          :rules="rules" 
          label-width="100px"
          class="preference-form-inner"
        >
          <!-- 口味偏好 -->
          <el-form-item label="口味偏好" prop="taste">
            <el-select 
              v-model="formData.taste" 
              placeholder="请选择您喜欢的口味"
              multiple
              collapse-tags
              style="width: 100%"
            >
              <el-option label="清淡" value="light" />
              <el-option label="麻辣" value="spicy" />
              <el-option label="酸甜" value="sweet-sour" />
              <el-option label="咸鲜" value="salty" />
              <el-option label="香辣" value="hot" />
              <el-option label="微甜" value="mild-sweet" />
              <el-option label="酸辣" value="sour-spicy" />
              <el-option label="鲜美" value="umami" />
            </el-select>
          </el-form-item>

          <!-- 饮食禁忌 -->
          <el-form-item label="饮食禁忌" prop="restrictions">
            <el-checkbox-group v-model="formData.restrictions">
              <el-checkbox label="无" value="none" />
              <el-checkbox label="素食" value="vegetarian" />
              <el-checkbox label="不吃辣" value="no-spicy" />
              <el-checkbox label="不吃海鲜" value="no-seafood" />
              <el-checkbox label="不吃牛肉" value="no-beef" />
              <el-checkbox label="不吃猪肉" value="no-pork" />
              <el-checkbox label="不吃鸡蛋" value="no-eggs" />
              <el-checkbox label="不吃坚果" value="no-nuts" />
              <el-checkbox label="乳糖不耐" value="lactose-intolerant" />
              <el-checkbox label="麸质过敏" value="gluten-free" />
            </el-checkbox-group>
          </el-form-item>

          <!-- 健身目标 -->
          <el-form-item label="健身目标" prop="fitnessGoal">
            <el-radio-group v-model="formData.fitnessGoal">
              <el-radio label="maintain">保持体重</el-radio>
              <el-radio label="lose-weight">减肥瘦身</el-radio>
              <el-radio label="gain-muscle">增肌健身</el-radio>
              <el-radio label="gain-weight">增重</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 烹饪难度 -->
          <el-form-item label="烹饪难度" prop="difficulty">
            <el-radio-group v-model="formData.difficulty">
              <el-radio label="easy">简单易做</el-radio>
              <el-radio label="medium">中等难度</el-radio>
              <el-radio label="hard">挑战复杂</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 用餐时间 -->
          <el-form-item label="用餐时间" prop="mealTime">
            <el-select 
              v-model="formData.mealTime" 
              placeholder="请选择用餐时间"
              style="width: 100%"
            >
              <el-option label="早餐" value="breakfast" />
              <el-option label="午餐" value="lunch" />
              <el-option label="晚餐" value="dinner" />
              <el-option label="夜宵" value="supper" />
              <el-option label="下午茶" value="afternoon-tea" />
            </el-select>
          </el-form-item>

          <!-- 烹饪时间 -->
          <el-form-item label="烹饪时间" prop="cookingTime">
            <el-radio-group v-model="formData.cookingTime">
              <el-radio label="15">15分钟内</el-radio>
              <el-radio label="30">30分钟内</el-radio>
              <el-radio label="60">1小时内</el-radio>
              <el-radio label="120">2小时内</el-radio>
            </el-radio-group>
          </el-form-item>

          <!-- 人数 -->
          <el-form-item label="用餐人数" prop="servings">
            <el-input-number 
              v-model="formData.servings" 
              :min="1" 
              :max="10" 
              controls-position="right"
              style="width: 150px"
            />
            <span style="margin-left: 10px; color: #666;">人</span>
          </el-form-item>

          <!-- 特殊需求 -->
          <el-form-item label="特殊需求" prop="specialRequests">
            <el-input
              v-model="formData.specialRequests"
              type="textarea"
              :rows="3"
              placeholder="请描述其他特殊需求或偏好（可选）"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </el-form>
      </div>

      <!-- 表单底部 -->
      <div class="form-footer">
        <el-button @click="resetForm" size="large">重置</el-button>
        <el-button 
          type="primary" 
          @click="submitForm" 
          :loading="isSubmitting"
          size="large"
        >
          {{ isSubmitting ? '生成中...' : '生成个性化食谱' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Close } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { chatApi } from '../api/chat'

const emit = defineEmits(['close-form', 'recipe-generated'])

// 表单引用
const formRef = ref(null)
const isSubmitting = ref(false)

// 表单数据
const formData = reactive({
  taste: [], // 口味偏好
  restrictions: ['none'], // 饮食禁忌
  fitnessGoal: 'maintain', // 健身目标
  difficulty: 'easy', // 烹饪难度
  mealTime: 'lunch', // 用餐时间
  cookingTime: '30', // 烹饪时间
  servings: 2, // 用餐人数
  specialRequests: '' // 特殊需求
})

// 表单验证规则
const rules = {
  taste: [
    { required: true, message: '请选择至少一种口味偏好', trigger: 'change' }
  ],
  restrictions: [
    { required: true, message: '请选择饮食禁忌', trigger: 'change' }
  ],
  fitnessGoal: [
    { required: true, message: '请选择健身目标', trigger: 'change' }
  ],
  difficulty: [
    { required: true, message: '请选择烹饪难度', trigger: 'change' }
  ],
  mealTime: [
    { required: true, message: '请选择用餐时间', trigger: 'change' }
  ],
  cookingTime: [
    { required: true, message: '请选择烹饪时间', trigger: 'change' }
  ]
}

// 处理遮罩点击
const handleOverlayClick = () => {
  emit('close-form')
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  // 重置为默认值
  Object.assign(formData, {
    taste: [],
    restrictions: ['none'],
    fitnessGoal: 'maintain',
    difficulty: 'easy',
    mealTime: 'lunch',
    cookingTime: '30',
    servings: 2,
    specialRequests: ''
  })
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  try {
    // 验证表单
    await formRef.value.validate()
    
    isSubmitting.value = true
    
    // 构建提示词
    const prompt = buildPrompt(formData)
    
    // 调用AI API生成食谱
    let fullResponse = ''
    
    await chatApi.sendStreamMessage(prompt, {
      onMessage: (data) => {
        fullResponse += data
      },
      onComplete: () => {
        ElMessage.success('个性化食谱生成成功！')
        emit('recipe-generated', {
          preferences: { ...formData },
          recipe: fullResponse
        })
        emit('close-form')
      },
      onError: (error) => {
        console.error('生成食谱失败:', error)
        ElMessage.error('生成食谱失败，请稍后重试')
      }
    })
    
  } catch (error) {
    if (error.message) {
      ElMessage.error('请完善表单信息')
    } else {
      console.error('提交表单失败:', error)
      ElMessage.error('提交失败，请稍后重试')
    }
  } finally {
    isSubmitting.value = false
  }
}

// 构建AI提示词
const buildPrompt = (data) => {
  const tasteText = data.taste.length > 0 ? data.taste.join('、') : '无特殊要求'
  const restrictionsText = data.restrictions.includes('none') ? '无禁忌' : data.restrictions.join('、')
  
  const goalMap = {
    'maintain': '保持体重',
    'lose-weight': '减肥瘦身',
    'gain-muscle': '增肌健身',
    'gain-weight': '增重'
  }
  
  const difficultyMap = {
    'easy': '简单易做',
    'medium': '中等难度',
    'hard': '挑战复杂'
  }
  
  const mealTimeMap = {
    'breakfast': '早餐',
    'lunch': '午餐',
    'dinner': '晚餐',
    'supper': '夜宵',
    'afternoon-tea': '下午茶'
  }
  
  let prompt = `请根据以下用户偏好，为我推荐一道个性化食谱：

`
  prompt += `🍽️ 用餐信息：
`
  prompt += `- 用餐时间：${mealTimeMap[data.mealTime]}
`
  prompt += `- 用餐人数：${data.servings}人
`
  prompt += `- 烹饪时间：${data.cookingTime}分钟内
\n`
  
  prompt += `👅 口味偏好：${tasteText}
\n`
  
  prompt += `🚫 饮食禁忌：${restrictionsText}
\n`
  
  prompt += `💪 健身目标：${goalMap[data.fitnessGoal]}
\n`
  
  prompt += `👨‍🍳 烹饪难度：${difficultyMap[data.difficulty]}
\n`
  
  if (data.specialRequests.trim()) {
    prompt += `📝 特殊需求：${data.specialRequests}
\n`
  }
  
  prompt += `请提供详细的食谱，包括：
`
  prompt += `1. 菜品名称和简介
`
  prompt += `2. 所需食材和用量
`
  prompt += `3. 详细制作步骤
`
  prompt += `4. 营养价值分析
`
  prompt += `5. 烹饪小贴士
\n`
  
  prompt += `请确保推荐的食谱符合用户的所有要求，特别是饮食禁忌和健身目标。`
  
  return prompt
}
</script>

<style scoped>
.preference-form-overlay {
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

.preference-form {
  width: 100%;
  max-width: 600px;
  max-height: 90vh;
  background: white;
  border-radius: 16px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.2);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.form-header {
  padding: 20px 24px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-info {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.title-info h3 {
  margin: 0 0 4px 0;
  font-size: 20px;
  font-weight: 600;
}

.subtitle {
  font-size: 14px;
  opacity: 0.9;
}

.header-actions .el-button {
  color: white;
}

.form-content {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
}

.preference-form-inner {
  max-width: none;
}

.preference-form-inner .el-form-item {
  margin-bottom: 24px;
}

.preference-form-inner .el-form-item__label {
  font-weight: 600;
  color: var(--text-primary);
}

.el-checkbox-group {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
  gap: 8px;
}

.el-radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 16px;
}

.form-footer {
  padding: 20px 24px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.form-footer .el-button {
  min-width: 120px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .preference-form {
    margin: 10px;
    max-height: calc(100vh - 20px);
  }
  
  .form-header {
    padding: 16px 20px;
  }
  
  .header-info {
    gap: 12px;
  }
  
  .avatar {
    width: 40px;
    height: 40px;
    font-size: 18px;
  }
  
  .title-info h3 {
    font-size: 18px;
  }
  
  .form-content {
    padding: 20px;
  }
  
  .el-checkbox-group {
    grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
  }
  
  .el-radio-group {
    flex-direction: column;
    gap: 12px;
  }
  
  .form-footer {
    padding: 16px 20px;
    flex-direction: column;
  }
  
  .form-footer .el-button {
    width: 100%;
  }
}
</style>