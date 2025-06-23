<template>
  <div class="preference-form-overlay" @click="handleOverlayClick">
    <div class="preference-form" @click.stop>
      <!-- 表单头部 -->
      <div class="form-header">
        <div class="header-info">
          <div class="avatar">
            <el-icon>
              <User />
            </el-icon>
          </div>
          <div class="title-info">
            <h3>个性化食谱定制</h3>
            <span class="subtitle">{{ stepTitles[currentStep - 1] }}</span>
          </div>
        </div>
        <div class="header-actions">
          <el-button type="text" @click="$emit('close-form')" :icon="Close" title="关闭" />
        </div>
      </div>

      <!-- 步骤指示器 -->
      <div class="step-indicator">
        <div class="steps">
          <div v-for="(step, index) in steps" :key="index" class="step-item" :class="{
            'active': currentStep === index + 1,
            'completed': currentStep > index + 1
          }">
            <div class="step-number">
              <el-icon v-if="currentStep > index + 1">
                <Check />
              </el-icon>
              <span v-else>{{ index + 1 }}</span>
            </div>
            <div class="step-title">{{ step.title }}</div>
          </div>
        </div>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: `${((currentStep - 1) / (steps.length - 1)) * 100}%` }"></div>
        </div>
      </div>

      <!-- 表单内容 -->
      <div class="form-content">
        <transition name="slide" mode="out-in">
          <div :key="currentStep" class="step-content">
            <!-- 第一步：基本信息 -->
            <div v-if="currentStep === 1" class="step-form">
              <div class="step-description">
                <h4>基本用餐信息</h4>
                <p>请告诉我们您的基本用餐需求</p>
              </div>
              <el-form ref="formRef" :model="formData" :rules="currentStepRules" label-width="100px"
                class="preference-form-inner">
                <el-form-item label="用餐时间" prop="mealTime">
                  <el-select v-model="formData.mealTime" placeholder="请选择用餐时间" style="width: 100%" size="large">
                    <el-option label="早餐" value="breakfast" />
                    <el-option label="午餐" value="lunch" />
                    <el-option label="晚餐" value="dinner" />
                    <el-option label="夜宵" value="supper" />
                    <el-option label="下午茶" value="afternoon-tea" />
                  </el-select>
                </el-form-item>

                <el-form-item label="用餐人数" prop="servings">
                  <el-input-number v-model="formData.servings" :min="1" :max="10" controls-position="right"
                    style="width: 200px" size="large" />
                  <span style="margin-left: 10px; color: #666;">人</span>
                </el-form-item>

                <el-form-item label="烹饪时间" prop="cookingTime">
                  <el-radio-group v-model="formData.cookingTime" size="large">
                    <el-radio-button label="15">15分钟内</el-radio-button>
                    <el-radio-button label="30">30分钟内</el-radio-button>
                    <el-radio-button label="60">1小时内</el-radio-button>
                    <el-radio-button label="120">2小时内</el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </div>

            <!-- 第二步：口味偏好 -->
            <div v-if="currentStep === 2" class="step-form">
              <div class="step-description">
                <h4>口味偏好设置</h4>
                <p>选择您喜欢的口味和饮食限制</p>
              </div>
              <el-form ref="formRef" :model="formData" :rules="currentStepRules" label-width="100px"
                class="preference-form-inner">
                <el-form-item label="口味偏好" prop="taste">
                  <div class="taste-options">
                    <el-tag v-for="taste in tasteOptions" :key="taste.value"
                      :type="formData.taste.includes(taste.value) ? 'primary' : 'info'"
                      :effect="formData.taste.includes(taste.value) ? 'dark' : 'plain'"
                      @click="toggleTaste(taste.value)" class="taste-tag" size="large">
                      {{ taste.icon }} {{ taste.label }}
                    </el-tag>
                  </div>
                </el-form-item>

                <el-form-item label="饮食禁忌" prop="restrictions">
                  <div class="restriction-options">
                    <el-checkbox-group v-model="formData.restrictions">
                      <el-checkbox v-for="restriction in restrictionOptions" :key="restriction.value"
                        :label="restriction.value" size="large" class="restriction-checkbox"
                        :disabled="restriction.value !== 'none' && formData.restrictions.includes('none')"
                        @change="handleRestrictionChange">
                        {{ restriction.icon }} {{ restriction.label }}
                      </el-checkbox>
                    </el-checkbox-group>
                  </div>
                </el-form-item>
              </el-form>
            </div>

            <!-- 第三步：烹饪要求 -->
            <div v-if="currentStep === 3" class="step-form">
              <div class="step-description">
                <h4>烹饪要求</h4>
                <p>设置您的健身目标和烹饪难度偏好</p>
              </div>
              <el-form ref="formRef" :model="formData" :rules="currentStepRules" label-width="100px"
                class="preference-form-inner">
                <el-form-item label="健身目标" prop="fitnessGoal">
                  <div class="goal-options">
                    <div v-for="goal in fitnessGoalOptions" :key="goal.value" class="goal-card"
                      :class="{ 'selected': formData.fitnessGoal === goal.value }"
                      @click="formData.fitnessGoal = goal.value">
                      <div class="goal-icon">{{ goal.icon }}</div>
                      <div class="goal-title">{{ goal.label }}</div>
                      <div class="goal-desc">{{ goal.description }}</div>
                    </div>
                  </div>
                </el-form-item>

                <el-form-item label="烹饪难度" prop="difficulty">
                  <el-radio-group v-model="formData.difficulty" size="large">
                    <el-radio-button v-for="diff in difficultyOptions" :key="diff.value" :label="diff.value">
                      {{ diff.icon }} {{ diff.label }}
                    </el-radio-button>
                  </el-radio-group>
                </el-form-item>
              </el-form>
            </div>

            <!-- 第四步：特殊需求 -->
            <div v-if="currentStep === 4" class="step-form">
              <div class="step-description">
                <h4>特殊需求</h4>
                <p>添加任何其他特殊要求或偏好（可选）</p>
              </div>
              <el-form ref="formRef" :model="formData" :rules="currentStepRules" label-width="100px"
                class="preference-form-inner">
                <el-form-item label="特殊需求" prop="specialRequests">
                  <el-input v-model="formData.specialRequests" type="textarea" :rows="6"
                    placeholder="请描述其他特殊需求或偏好，例如： 不喜欢某种食材 特定的烹饪方式 营养需求 过敏信息等" maxlength="300" show-word-limit
                    size="large" />
                </el-form-item>

                <!-- 预览信息 -->
                <div class="preference-preview">
                  <h5>您的偏好设置预览：</h5>
                  <div class="preview-item">
                    <strong>用餐信息：</strong>
                    {{ mealTimeMap[formData.mealTime] }}，{{ formData.servings }}人，{{ formData.cookingTime }}分钟内
                  </div>
                  <div class="preview-item">
                    <strong>口味偏好：</strong>
                    {{formData.taste.length > 0 ? formData.taste.map(t => tasteMap[t]).join('、') : '无特殊要求'}}
                  </div>
                  <div class="preview-item">
                    <strong>饮食禁忌：</strong>
                    {{formData.restrictions.includes('none') ? '无禁忌' : formData.restrictions.filter(r => r !==
                      'none').map(r => restrictionMap[r]).join('、') }}
                  </div>
                  <div class="preview-item">
                    <strong>健身目标：</strong>
                    {{ goalMap[formData.fitnessGoal] }}
                  </div>
                  <div class="preview-item">
                    <strong>烹饪难度：</strong>
                    {{ difficultyMap[formData.difficulty] }}
                  </div>
                </div>
              </el-form>
            </div>
          </div>
        </transition>
      </div>

      <!-- 表单底部 -->
      <div class="form-footer">
        <div class="footer-left">
          <el-button v-if="currentStep > 1" @click="prevStep" size="large" :icon="ArrowLeft">
            上一步
          </el-button>
        </div>
        <div class="footer-right">
          <el-button @click="resetForm" size="large">重置</el-button>
          <el-button v-if="currentStep < steps.length" type="primary" @click="nextStep" size="large" :icon="ArrowRight">
            下一步
          </el-button>
          <el-button v-else type="primary" @click="submitForm" :loading="isSubmitting" size="large" :icon="Check">
            {{ isSubmitting ? '生成中...' : '生成个性化食谱' }}
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { User, Close, Check, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { chatApi } from '../api/chat'

const emit = defineEmits(['close-form', 'recipe-generated'])

// 表单引用
const formRef = ref(null)
const isSubmitting = ref(false)
const currentStep = ref(1)

// 步骤配置
const steps = [
  { title: '基本信息', key: 'basic' },
  { title: '口味偏好', key: 'taste' },
  { title: '烹饪要求', key: 'cooking' },
  { title: '特殊需求', key: 'special' }
]

const stepTitles = [
  '请填写基本用餐信息',
  '选择您的口味偏好',
  '设置烹饪要求',
  '添加特殊需求（可选）'
]

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

// 选项数据
const tasteOptions = [
  { label: '清淡', value: 'light', icon: '🥗' },
  { label: '麻辣', value: 'spicy', icon: '🌶️' },
  { label: '酸甜', value: 'sweet-sour', icon: '🍯' },
  { label: '咸鲜', value: 'salty', icon: '🧂' },
  { label: '香辣', value: 'hot', icon: '🔥' },
  { label: '微甜', value: 'mild-sweet', icon: '🍬' },
  { label: '酸辣', value: 'sour-spicy', icon: '🍋' },
  { label: '鲜美', value: 'umami', icon: '🍄' }
]

const restrictionOptions = [
  { label: '无', value: 'none', icon: '✅' },
  { label: '素食', value: 'vegetarian', icon: '🥬' },
  { label: '不吃辣', value: 'no-spicy', icon: '🚫' },
  { label: '不吃海鲜', value: 'no-seafood', icon: '🦐' },
  { label: '不吃牛肉', value: 'no-beef', icon: '🐄' },
  { label: '不吃猪肉', value: 'no-pork', icon: '🐷' },
  { label: '不吃鸡蛋', value: 'no-eggs', icon: '🥚' },
  { label: '不吃坚果', value: 'no-nuts', icon: '🥜' },
  { label: '乳糖不耐', value: 'lactose-intolerant', icon: '🥛' },
  { label: '麸质过敏', value: 'gluten-free', icon: '🌾' }
]

const fitnessGoalOptions = [
  {
    label: '保持体重',
    value: 'maintain',
    icon: '⚖️',
    description: '维持当前体重，均衡营养'
  },
  {
    label: '减肥瘦身',
    value: 'lose-weight',
    icon: '📉',
    description: '低卡路里，高纤维食谱'
  },
  {
    label: '增肌健身',
    value: 'gain-muscle',
    icon: '💪',
    description: '高蛋白，适量碳水化合物'
  },
  {
    label: '增重',
    value: 'gain-weight',
    icon: '📈',
    description: '高热量，营养丰富'
  }
]

const difficultyOptions = [
  { label: '简单易做', value: 'easy', icon: '😊' },
  { label: '中等难度', value: 'medium', icon: '🤔' },
  { label: '挑战复杂', value: 'hard', icon: '😤' }
]

// 映射对象
const mealTimeMap = {
  'breakfast': '早餐',
  'lunch': '午餐',
  'dinner': '晚餐',
  'supper': '夜宵',
  'afternoon-tea': '下午茶'
}

const tasteMap = {
  'light': '清淡',
  'spicy': '麻辣',
  'sweet-sour': '酸甜',
  'salty': '咸鲜',
  'hot': '香辣',
  'mild-sweet': '微甜',
  'sour-spicy': '酸辣',
  'umami': '鲜美'
}

const restrictionMap = {
  'none': '无',
  'vegetarian': '素食',
  'no-spicy': '不吃辣',
  'no-seafood': '不吃海鲜',
  'no-beef': '不吃牛肉',
  'no-pork': '不吃猪肉',
  'no-eggs': '不吃鸡蛋',
  'no-nuts': '不吃坚果',
  'lactose-intolerant': '乳糖不耐',
  'gluten-free': '麸质过敏'
}

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

// 分步验证规则
const stepRules = {
  1: {
    mealTime: [
      { required: true, message: '请选择用餐时间', trigger: 'change' }
    ],
    cookingTime: [
      { required: true, message: '请选择烹饪时间', trigger: 'change' }
    ]
  },
  2: {
    taste: [
      { required: true, message: '请选择至少一种口味偏好', trigger: 'change' }
    ],
    restrictions: [
      { required: true, message: '请选择饮食禁忌', trigger: 'change' }
    ]
  },
  3: {
    fitnessGoal: [
      { required: true, message: '请选择健身目标', trigger: 'change' }
    ],
    difficulty: [
      { required: true, message: '请选择烹饪难度', trigger: 'change' }
    ]
  },
  4: {}
}

// 当前步骤的验证规则
const currentStepRules = computed(() => {
  return stepRules[currentStep.value] || {}
})

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
  currentStep.value = 1
}

// 步骤导航
const nextStep = async () => {
  if (!formRef.value) return

  try {
    // 验证当前步骤
    await formRef.value.validate()

    if (currentStep.value < steps.length) {
      currentStep.value++
    }
  } catch (error) {
    console.error('当前步骤验证失败:', error)
    ElMessage.error('请完善当前步骤的信息')
  }
}

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--
  }
}

const goToStep = (step) => {
  currentStep.value = step
}

// 口味选择切换
const toggleTaste = (taste) => {
  const index = formData.taste.indexOf(taste)
  if (index > -1) {
    formData.taste.splice(index, 1)
  } else {
    formData.taste.push(taste)
  }
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

.step-indicator {
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;
}

.steps {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
}

.step-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;
}

.step-number {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #f0f0f0;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  margin-bottom: 8px;
  transition: all 0.3s ease;
}

.step-item.active .step-number {
  background: var(--primary-color);
  color: white;
}

.step-item.completed .step-number {
  background: #67c23a;
  color: white;
}

.step-title {
  font-size: 12px;
  color: #666;
  text-align: center;
}

.step-item.active .step-title {
  color: var(--primary-color);
  font-weight: 600;
}

.progress-bar {
  height: 4px;
  background: #f0f0f0;
  border-radius: 2px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-color), var(--secondary-color));
  transition: width 0.3s ease;
}

.step-description {
  margin-bottom: 24px;
  text-align: center;
}

.step-description h4 {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: var(--text-primary);
}

.step-description p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.taste-options {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.taste-tag {
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 8px 16px;
  border-radius: 20px;
}

.taste-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.restriction-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 12px;
}

.restriction-checkbox {
  padding: 8px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.restriction-checkbox:hover {
  background: #f8f9fa;
}

.goal-options {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
}

.goal-card {
  padding: 20px;
  border: 2px solid #f0f0f0;
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.goal-card:hover {
  border-color: var(--primary-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.goal-card.selected {
  border-color: var(--primary-color);
  background: rgba(64, 158, 255, 0.05);
}

.goal-icon {
  font-size: 32px;
  margin-bottom: 12px;
}

.goal-title {
  font-weight: 600;
  margin-bottom: 8px;
  color: var(--text-primary);
}

.goal-desc {
  font-size: 12px;
  color: #666;
}

.preference-preview {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  margin-top: 24px;
}

.preference-preview h5 {
  margin: 0 0 16px 0;
  color: var(--text-primary);
  font-size: 16px;
}

.preview-item {
  margin-bottom: 12px;
  font-size: 14px;
  line-height: 1.5;
}

.preview-item:last-child {
  margin-bottom: 0;
}

.preview-item strong {
  color: var(--text-primary);
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter-from {
  opacity: 0;
  transform: translateX(30px);
}

.slide-leave-to {
  opacity: 0;
  transform: translateX(-30px);
}

.form-footer {
  padding: 20px 24px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.footer-left,
.footer-right {
  display: flex;
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