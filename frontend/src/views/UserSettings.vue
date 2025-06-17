<template>
  <div class="user-settings-page">
    <div class="settings-container">
      <!-- 页面头部 -->
      <div class="settings-header">
        <h1>个人设置</h1>
        <p>管理您的个人信息和偏好设置</p>
      </div>

      <!-- 设置内容 -->
      <div class="settings-content">
        <el-tabs v-model="activeTab" class="settings-tabs">
          <!-- 基本信息 -->
          <el-tab-pane label="基本信息" name="profile">
            <div class="settings-section">
              <div class="section-header">
                <h3>个人资料</h3>
                <p>更新您的基本信息</p>
              </div>
              
              <el-form 
                ref="profileFormRef"
                :model="profileForm"
                :rules="profileRules"
                label-width="120px"
                class="profile-form"
              >
                <el-form-item label="头像">
                  <div class="avatar-upload">
                    <el-avatar :size="80" :src="profileForm.avatar" :icon="UserFilled" />
                    <el-button size="small" @click="uploadAvatar">更换头像</el-button>
                  </div>
                </el-form-item>
                
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="profileForm.nickname" placeholder="请输入昵称" />
                </el-form-item>
                
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="profileForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="profileForm.phone" placeholder="请输入手机号" />
                </el-form-item>
                
                <el-form-item label="生日">
                  <el-date-picker
                    v-model="profileForm.birthday"
                    type="date"
                    placeholder="选择生日"
                    style="width: 100%"
                  />
                </el-form-item>
                
                <el-form-item label="性别">
                  <el-radio-group v-model="profileForm.gender">
                    <el-radio label="male">男</el-radio>
                    <el-radio label="female">女</el-radio>
                    <el-radio label="other">其他</el-radio>
                  </el-radio-group>
                </el-form-item>
                
                <el-form-item>
                  <el-button type="primary" @click="saveProfile">保存更改</el-button>
                  <el-button @click="resetProfile">重置</el-button>
                </el-form-item>
              </el-form>
            </div>
          </el-tab-pane>

          <!-- 饮食偏好 -->
          <el-tab-pane label="饮食偏好" name="preferences">
            <div class="settings-section">
              <div class="section-header">
                <h3>饮食偏好</h3>
                <p>设置您的饮食习惯和偏好，获得更精准的推荐</p>
              </div>
              
              <div class="preference-groups">
                <!-- 饮食类型 -->
                <div class="preference-group">
                  <h4>饮食类型</h4>
                  <el-checkbox-group v-model="preferences.dietTypes">
                    <el-checkbox label="vegetarian">素食主义</el-checkbox>
                    <el-checkbox label="vegan">纯素食</el-checkbox>
                    <el-checkbox label="keto">生酮饮食</el-checkbox>
                    <el-checkbox label="paleo">原始人饮食</el-checkbox>
                    <el-checkbox label="mediterranean">地中海饮食</el-checkbox>
                    <el-checkbox label="lowCarb">低碳水化合物</el-checkbox>
                  </el-checkbox-group>
                </div>
                
                <!-- 口味偏好 -->
                <div class="preference-group">
                  <h4>口味偏好</h4>
                  <div class="taste-preferences">
                    <div class="taste-item">
                      <span>辣度偏好</span>
                      <el-slider v-model="preferences.spiciness" :max="5" show-stops />
                      <span class="taste-label">{{ getSpicinessLabel(preferences.spiciness) }}</span>
                    </div>
                    <div class="taste-item">
                      <span>甜度偏好</span>
                      <el-slider v-model="preferences.sweetness" :max="5" show-stops />
                      <span class="taste-label">{{ getSweetnessLabel(preferences.sweetness) }}</span>
                    </div>
                    <div class="taste-item">
                      <span>咸度偏好</span>
                      <el-slider v-model="preferences.saltiness" :max="5" show-stops />
                      <span class="taste-label">{{ getSaltinessLabel(preferences.saltiness) }}</span>
                    </div>
                  </div>
                </div>
                
                <!-- 过敏信息 -->
                <div class="preference-group">
                  <h4>过敏信息</h4>
                  <el-checkbox-group v-model="preferences.allergies">
                    <el-checkbox label="nuts">坚果类</el-checkbox>
                    <el-checkbox label="seafood">海鲜类</el-checkbox>
                    <el-checkbox label="dairy">乳制品</el-checkbox>
                    <el-checkbox label="eggs">鸡蛋</el-checkbox>
                    <el-checkbox label="gluten">麸质</el-checkbox>
                    <el-checkbox label="soy">大豆</el-checkbox>
                  </el-checkbox-group>
                </div>
                
                <!-- 不喜欢的食材 -->
                <div class="preference-group">
                  <h4>不喜欢的食材</h4>
                  <div class="dislike-ingredients">
                    <el-tag
                      v-for="ingredient in preferences.dislikedIngredients"
                      :key="ingredient"
                      closable
                      @close="removeDislikedIngredient(ingredient)"
                      class="ingredient-tag"
                    >
                      {{ ingredient }}
                    </el-tag>
                    <el-input
                      v-if="showIngredientInput"
                      ref="ingredientInputRef"
                      v-model="newIngredient"
                      size="small"
                      @keyup.enter="addDislikedIngredient"
                      @blur="addDislikedIngredient"
                      class="ingredient-input"
                    />
                    <el-button
                      v-else
                      size="small"
                      @click="showIngredientInput = true"
                      class="add-ingredient-btn"
                    >
                      + 添加食材
                    </el-button>
                  </div>
                </div>
                
                <!-- 烹饪技能 -->
                <div class="preference-group">
                  <h4>烹饪技能水平</h4>
                  <el-radio-group v-model="preferences.cookingSkill">
                    <el-radio label="beginner">新手</el-radio>
                    <el-radio label="intermediate">中级</el-radio>
                    <el-radio label="advanced">高级</el-radio>
                    <el-radio label="expert">专家</el-radio>
                  </el-radio-group>
                </div>
                
                <!-- 烹饪时间偏好 -->
                <div class="preference-group">
                  <h4>烹饪时间偏好</h4>
                  <div class="time-preference">
                    <span>最大烹饪时间：</span>
                    <el-slider 
                      v-model="preferences.maxCookingTime" 
                      :min="10" 
                      :max="180" 
                      :step="10"
                      show-input
                    />
                    <span>分钟</span>
                  </div>
                </div>
              </div>
              
              <div class="preference-actions">
                <el-button type="primary" @click="savePreferences">保存偏好</el-button>
                <el-button @click="resetPreferences">重置为默认</el-button>
              </div>
            </div>
          </el-tab-pane>

          <!-- 通知设置 -->
          <el-tab-pane label="通知设置" name="notifications">
            <div class="settings-section">
              <div class="section-header">
                <h3>通知设置</h3>
                <p>管理您接收通知的方式和频率</p>
              </div>
              
              <div class="notification-groups">
                <div class="notification-group">
                  <h4>推送通知</h4>
                  <div class="notification-item">
                    <div class="notification-info">
                      <span class="notification-title">新菜谱推荐</span>
                      <span class="notification-desc">当有符合您偏好的新菜谱时通知您</span>
                    </div>
                    <el-switch v-model="notifications.newRecipes" />
                  </div>
                  
                  <div class="notification-item">
                    <div class="notification-info">
                      <span class="notification-title">烹饪提醒</span>
                      <span class="notification-desc">提醒您按时开始烹饪</span>
                    </div>
                    <el-switch v-model="notifications.cookingReminders" />
                  </div>
                  
                  <div class="notification-item">
                    <div class="notification-info">
                      <span class="notification-title">每日推荐</span>
                      <span class="notification-desc">每天为您推荐一道菜谱</span>
                    </div>
                    <el-switch v-model="notifications.dailyRecommendations" />
                  </div>
                </div>
                
                <div class="notification-group">
                  <h4>邮件通知</h4>
                  <div class="notification-item">
                    <div class="notification-info">
                      <span class="notification-title">周报摘要</span>
                      <span class="notification-desc">每周发送您的烹饪活动摘要</span>
                    </div>
                    <el-switch v-model="notifications.weeklyDigest" />
                  </div>
                  
                  <div class="notification-item">
                    <div class="notification-info">
                      <span class="notification-title">特别活动</span>
                      <span class="notification-desc">节日特色菜谱和烹饪活动通知</span>
                    </div>
                    <el-switch v-model="notifications.specialEvents" />
                  </div>
                </div>
              </div>
              
              <div class="notification-actions">
                <el-button type="primary" @click="saveNotifications">保存设置</el-button>
              </div>
            </div>
          </el-tab-pane>

          <!-- 隐私设置 -->
          <el-tab-pane label="隐私设置" name="privacy">
            <div class="settings-section">
              <div class="section-header">
                <h3>隐私设置</h3>
                <p>控制您的数据和隐私选项</p>
              </div>
              
              <div class="privacy-groups">
                <div class="privacy-group">
                  <h4>数据使用</h4>
                  <div class="privacy-item">
                    <div class="privacy-info">
                      <span class="privacy-title">个性化推荐</span>
                      <span class="privacy-desc">允许使用您的数据来改善推荐质量</span>
                    </div>
                    <el-switch v-model="privacy.personalizedRecommendations" />
                  </div>
                  
                  <div class="privacy-item">
                    <div class="privacy-info">
                      <span class="privacy-title">使用分析</span>
                      <span class="privacy-desc">帮助我们了解应用使用情况以改进服务</span>
                    </div>
                    <el-switch v-model="privacy.usageAnalytics" />
                  </div>
                </div>
                
                <div class="privacy-group">
                  <h4>账户管理</h4>
                  <div class="privacy-actions">
                    <el-button @click="exportData">导出我的数据</el-button>
                    <el-button @click="clearHistory">清除浏览历史</el-button>
                    <el-button type="danger" @click="deleteAccount">删除账户</el-button>
                  </div>
                </div>
              </div>
              
              <div class="privacy-actions">
                <el-button type="primary" @click="savePrivacy">保存设置</el-button>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue'
import { UserFilled } from '@element-plus/icons-vue'
import { useUserStore } from '../store'
import { userApi } from '../api/chat'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()

// 响应式数据
const activeTab = ref('profile')
const profileFormRef = ref()
const ingredientInputRef = ref()
const showIngredientInput = ref(false)
const newIngredient = ref('')

// 个人资料表单
const profileForm = reactive({
  avatar: '',
  nickname: '',
  email: '',
  phone: '',
  birthday: null,
  gender: 'male'
})

// 表单验证规则
const profileRules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 饮食偏好
const preferences = reactive({
  dietTypes: [],
  spiciness: 2,
  sweetness: 2,
  saltiness: 2,
  allergies: [],
  dislikedIngredients: ['香菜', '胡萝卜'],
  cookingSkill: 'intermediate',
  maxCookingTime: 60
})

// 通知设置
const notifications = reactive({
  newRecipes: true,
  cookingReminders: true,
  dailyRecommendations: false,
  weeklyDigest: true,
  specialEvents: true
})

// 隐私设置
const privacy = reactive({
  personalizedRecommendations: true,
  usageAnalytics: false
})

// 获取辣度标签
const getSpicinessLabel = (value) => {
  const labels = ['不辣', '微辣', '中辣', '重辣', '特辣', '变态辣']
  return labels[value] || '中辣'
}

// 获取甜度标签
const getSweetnessLabel = (value) => {
  const labels = ['不甜', '微甜', '中甜', '偏甜', '很甜', '超甜']
  return labels[value] || '中甜'
}

// 获取咸度标签
const getSaltinessLabel = (value) => {
  const labels = ['清淡', '微咸', '适中', '偏咸', '重咸', '超咸']
  return labels[value] || '适中'
}

// 上传头像
const uploadAvatar = () => {
  ElMessage.info('头像上传功能开发中...')
}

// 保存个人资料
const saveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    
    // 这里应该调用真实的API
    // await userApi.updateProfile(profileForm)
    
    // 模拟API调用
    setTimeout(() => {
      userStore.updateProfile(profileForm)
      ElMessage.success('个人资料保存成功')
    }, 500)
  } catch (error) {
    console.error('保存个人资料失败:', error)
  }
}

// 重置个人资料
const resetProfile = () => {
  profileFormRef.value.resetFields()
}

// 添加不喜欢的食材
const addDislikedIngredient = () => {
  if (newIngredient.value && !preferences.dislikedIngredients.includes(newIngredient.value)) {
    preferences.dislikedIngredients.push(newIngredient.value)
  }
  newIngredient.value = ''
  showIngredientInput.value = false
}

// 移除不喜欢的食材
const removeDislikedIngredient = (ingredient) => {
  const index = preferences.dislikedIngredients.indexOf(ingredient)
  if (index > -1) {
    preferences.dislikedIngredients.splice(index, 1)
  }
}

// 保存饮食偏好
const savePreferences = async () => {
  try {
    // 这里应该调用真实的API
    // await userApi.updatePreferences(preferences)
    
    // 模拟API调用
    setTimeout(() => {
      userStore.updatePreferences(preferences)
      ElMessage.success('饮食偏好保存成功')
    }, 500)
  } catch (error) {
    console.error('保存饮食偏好失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

// 重置饮食偏好
const resetPreferences = () => {
  ElMessageBox.confirm('确定要重置为默认偏好设置吗？', '确认重置', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    Object.assign(preferences, {
      dietTypes: [],
      spiciness: 2,
      sweetness: 2,
      saltiness: 2,
      allergies: [],
      dislikedIngredients: [],
      cookingSkill: 'intermediate',
      maxCookingTime: 60
    })
    ElMessage.success('已重置为默认设置')
  })
}

// 保存通知设置
const saveNotifications = async () => {
  try {
    // 这里应该调用真实的API
    // await userApi.updateNotifications(notifications)
    
    // 模拟API调用
    setTimeout(() => {
      ElMessage.success('通知设置保存成功')
    }, 500)
  } catch (error) {
    console.error('保存通知设置失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

// 保存隐私设置
const savePrivacy = async () => {
  try {
    // 这里应该调用真实的API
    // await userApi.updatePrivacy(privacy)
    
    // 模拟API调用
    setTimeout(() => {
      ElMessage.success('隐私设置保存成功')
    }, 500)
  } catch (error) {
    console.error('保存隐私设置失败:', error)
    ElMessage.error('保存失败，请重试')
  }
}

// 导出数据
const exportData = () => {
  ElMessage.info('数据导出功能开发中...')
}

// 清除历史
const clearHistory = () => {
  ElMessageBox.confirm('确定要清除所有浏览历史吗？此操作不可恢复。', '确认清除', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    userStore.clearHistory()
    ElMessage.success('浏览历史已清除')
  })
}

// 删除账户
const deleteAccount = () => {
  ElMessageBox.confirm(
    '确定要删除账户吗？此操作将永久删除您的所有数据，且不可恢复。',
    '危险操作',
    {
      confirmButtonText: '确定删除',
      cancelButtonText: '取消',
      type: 'error'
    }
  ).then(() => {
    ElMessage.info('账户删除功能开发中...')
  })
}

// 加载用户数据
const loadUserData = async () => {
  try {
    // 从store中获取用户数据
    const userData = userStore.userInfo
    if (userData) {
      Object.assign(profileForm, userData)
    }
    
    const userPreferences = userStore.preferences
    if (userPreferences) {
      Object.assign(preferences, userPreferences)
    }
  } catch (error) {
    console.error('加载用户数据失败:', error)
  }
}

// 监听输入框显示状态
const watchIngredientInput = () => {
  if (showIngredientInput.value) {
    nextTick(() => {
      ingredientInputRef.value?.focus()
    })
  }
}

onMounted(() => {
  loadUserData()
})
</script>

<style scoped>
.user-settings-page {
  min-height: 100vh;
  background: var(--background-color);
  padding: 20px;
}

.settings-container {
  max-width: 1000px;
  margin: 0 auto;
}

/* 页面头部 */
.settings-header {
  text-align: center;
  margin-bottom: 40px;
  padding: 40px 20px;
  background: white;
  border-radius: 16px;
}

.settings-header h1 {
  margin: 0 0 12px 0;
  font-size: 32px;
  font-weight: 700;
  color: var(--text-primary);
}

.settings-header p {
  margin: 0;
  color: var(--text-secondary);
  font-size: 16px;
}

/* 设置内容 */
.settings-content {
  background: white;
  border-radius: 16px;
  overflow: hidden;
}

.settings-tabs {
  padding: 0 30px;
}

.settings-section {
  padding: 30px;
}

.section-header {
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
}

.section-header h3 {
  margin: 0 0 8px 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.section-header p {
  margin: 0;
  color: var(--text-secondary);
}

/* 个人资料 */
.profile-form {
  max-width: 600px;
}

.avatar-upload {
  display: flex;
  align-items: center;
  gap: 16px;
}

/* 饮食偏好 */
.preference-groups {
  margin-bottom: 30px;
}

.preference-group {
  margin-bottom: 40px;
  padding: 24px;
  background: var(--background-color);
  border-radius: 12px;
}

.preference-group h4 {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.taste-preferences {
  display: grid;
  gap: 20px;
}

.taste-item {
  display: grid;
  grid-template-columns: 100px 1fr 80px;
  align-items: center;
  gap: 16px;
}

.taste-label {
  font-size: 14px;
  color: var(--text-secondary);
  text-align: center;
}

.dislike-ingredients {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
}

.ingredient-tag {
  margin: 0;
}

.ingredient-input {
  width: 120px;
}

.add-ingredient-btn {
  border-style: dashed;
}

.time-preference {
  display: flex;
  align-items: center;
  gap: 16px;
}

.preference-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

/* 通知设置 */
.notification-groups {
  margin-bottom: 30px;
}

.notification-group {
  margin-bottom: 30px;
}

.notification-group h4 {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.notification-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--border-color);
}

.notification-item:last-child {
  border-bottom: none;
}

.notification-info {
  flex: 1;
}

.notification-title {
  display: block;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.notification-desc {
  display: block;
  font-size: 14px;
  color: var(--text-secondary);
}

.notification-actions {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

/* 隐私设置 */
.privacy-groups {
  margin-bottom: 30px;
}

.privacy-group {
  margin-bottom: 30px;
}

.privacy-group h4 {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border-color);
}

.privacy-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  border-bottom: 1px solid var(--border-color);
}

.privacy-item:last-child {
  border-bottom: none;
}

.privacy-info {
  flex: 1;
}

.privacy-title {
  display: block;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.privacy-desc {
  display: block;
  font-size: 14px;
  color: var(--text-secondary);
}

.privacy-actions {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .user-settings-page {
    padding: 10px;
  }
  
  .settings-header {
    padding: 20px;
    margin-bottom: 20px;
  }
  
  .settings-header h1 {
    font-size: 24px;
  }
  
  .settings-tabs {
    padding: 0 20px;
  }
  
  .settings-section {
    padding: 20px;
  }
  
  .taste-item {
    grid-template-columns: 1fr;
    gap: 8px;
  }
  
  .taste-label {
    text-align: left;
  }
  
  .time-preference {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .notification-item,
  .privacy-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
  
  .privacy-actions {
    flex-direction: column;
  }
}
</style>