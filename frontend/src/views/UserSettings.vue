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
          <!-- 登录注册 / 账户管理 -->
          <el-tab-pane :label="userStore.isLoggedIn ? '账户管理' : '登录注册'" name="auth">
            <div class="settings-section">
              <!-- 已登录状态 -->
              <div v-if="userStore.isLoggedIn" class="logged-in-section">
                <div class="section-header">
                  <h3>账户管理</h3>
                  <p>管理您的账户信息</p>
                </div>
                
                <el-card class="user-info-card">
                  <div class="user-info">
                    <el-avatar :size="60" :src="userStore.userInfo.avatar" :icon="UserFilled" />
                    <div class="user-details">
                      <h4>{{ userStore.userInfo.username || userStore.userInfo.name || '用户' }}</h4>
                      <p>{{ userStore.userInfo.email }}</p>
                    </div>
                  </div>
                  
                  <div class="user-actions">
                    <el-button type="danger" @click="handleLogout">退出登录</el-button>
                  </div>
                </el-card>
              </div>
              
              <!-- 未登录状态 -->
              <div v-else class="login-section">
                <div class="section-header">
                  <h3>账户管理</h3>
                  <p>登录或注册您的账户</p>
                </div>
                
                <div class="auth-container">
                  <el-card class="auth-card">
                    <template #header>
                      <div class="auth-header">
                        <el-segmented v-model="authMode" :options="authOptions" size="large" />
                      </div>
                    </template>
                  
                  <!-- 登录表单 -->
                  <div v-if="authMode === 'login'" class="auth-form">
                    <el-form
                      ref="loginFormRef"
                      :model="loginForm"
                      :rules="loginRules"
                      label-width="0"
                      size="large"
                    >
                      <el-form-item prop="username">
                        <el-input
                          v-model="loginForm.username"
                          placeholder="请输入用户名或邮箱"
                          prefix-icon="User"
                        />
                      </el-form-item>
                      
                      <el-form-item prop="password">
                        <el-input
                          v-model="loginForm.password"
                          type="password"
                          placeholder="请输入密码"
                          prefix-icon="Lock"
                          show-password
                        />
                      </el-form-item>
                      
                      <el-form-item>
                        <el-button
                          type="primary"
                          @click="handleLogin"
                          :loading="loginLoading"
                          style="width: 100%"
                        >
                          {{ loginLoading ? '登录中...' : '登录' }}
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                  
                  <!-- 注册表单 -->
                  <div v-else class="auth-form">
                    <el-form
                      ref="registerFormRef"
                      :model="registerForm"
                      :rules="registerRules"
                      label-width="0"
                      size="large"
                    >
                      <el-form-item prop="username">
                        <el-input
                          v-model="registerForm.username"
                          placeholder="请输入用户名"
                          prefix-icon="User"
                        />
                      </el-form-item>
                      
                      <el-form-item prop="email">
                        <el-input
                          v-model="registerForm.email"
                          placeholder="请输入邮箱"
                          prefix-icon="Message"
                        />
                      </el-form-item>
                      
                      <el-form-item prop="password">
                        <el-input
                          v-model="registerForm.password"
                          type="password"
                          placeholder="请输入密码"
                          prefix-icon="Lock"
                          show-password
                        />
                      </el-form-item>
                      
                      <el-form-item prop="confirmPassword">
                        <el-input
                          v-model="registerForm.confirmPassword"
                          type="password"
                          placeholder="请确认密码"
                          prefix-icon="Lock"
                          show-password
                        />
                      </el-form-item>
                      
                      <el-form-item>
                        <el-button
                          type="primary"
                          @click="handleRegister"
                          :loading="registerLoading"
                          style="width: 100%"
                        >
                          {{ registerLoading ? '注册中...' : '注册' }}
                        </el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                </el-card>
                </div>
              </div>
            </div>
          </el-tab-pane>

          <!-- 饮食偏好 -->
          <el-tab-pane label="饮食偏好" name="preferences">
            <div class="settings-section">
              <!-- 需要登录提示 -->
              <div v-if="!userStore.isLoggedIn" class="login-required">
                <el-empty description="请先登录以设置饮食偏好">
                  <el-button type="primary" @click="activeTab = 'auth'">去登录</el-button>
                </el-empty>
              </div>
              
              <!-- 已登录内容 -->
              <div v-else>
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
import { ref, reactive, nextTick, onMounted, watch } from 'vue'
import { UserFilled } from '@element-plus/icons-vue'
import { userApi } from '@/api/user'
import { useUserStore } from '../store'
import { ElMessage, ElMessageBox } from 'element-plus'

const userStore = useUserStore()

// 响应式数据
const activeTab = ref('auth')
const profileFormRef = ref()
const ingredientInputRef = ref()
const showIngredientInput = ref(false)
const newIngredient = ref('')

// 登录注册相关
const authMode = ref('login')
const loginFormRef = ref()
const registerFormRef = ref()
const loginLoading = ref(false)
const registerLoading = ref(false)

// 认证选项
const authOptions = [
  { label: '登录', value: 'login' },
  { label: '注册', value: 'register' }
]

// 登录表单
const loginForm = reactive({
  username: '',
  password: ''
})

// 注册表单
const registerForm = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: ''
})

// 登录验证规则
const loginRules = {
  username: [
    { required: true, message: '请输入用户名或邮箱', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ]
}

// 注册验证规则
const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

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

// 登录处理
const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    loginLoading.value = true
    
    const credentials = {
      username: loginForm.username,
      password: loginForm.password
    }
    
    const response = await userApi.login(credentials)
    console.log('登录响应完整数据:', response)
    
    // 检查响应结构
    if (response.success && response.data) {
      const { user, token } = response.data
      console.log('用户数据:', user)
      console.log('Token:', token)
      
      // 使用store的login方法
      userStore.login(user, token || 'session-based')
      
      ElMessage.success('登录成功！')
      activeTab.value = 'preferences'
      
      // 清空登录表单
      Object.assign(loginForm, {
        username: '',
        password: ''
      })
    } else {
      ElMessage.error(response.message || '登录失败')
    }
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败，请重试')
  } finally {
    loginLoading.value = false
  }
}

// 登出处理
const handleLogout = () => {
  ElMessageBox.confirm(
    '确定要退出登录吗？',
    '确认退出',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    userStore.logout()
    ElMessage.success('已退出登录')
    activeTab.value = 'auth'
  }).catch(() => {
    // 用户取消
  })
}

// 注册处理
const handleRegister = async () => {
  try {
    await registerFormRef.value.validate()
    registerLoading.value = true
    
    const response = await fetch('http://localhost:8080/api/users', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: registerForm.username,
        email: registerForm.email,
        password: registerForm.password
      })
    })
    
    console.log('注册响应状态:', response.status)
    console.log('注册响应头:', response.headers)
    
    // 检查多种成功状态
    if (response.ok || response.status === 200 || response.status === 201) {
      // 注册成功
      ElMessage.success('注册成功！请登录')
      // 切换到登录模式
      authMode.value = 'login'
      // 清空注册表单
      Object.assign(registerForm, {
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
      })
    } else {
      // 注册失败，尝试解析响应
      let errorMessage = '注册失败，请重试'
      try {
        const data = await response.json()
        console.log('注册响应数据:', data)
        errorMessage = data.message || data.msg || data.error || errorMessage
      } catch (jsonError) {
        // 如果不是JSON格式，获取文本内容
        try {
          const textData = await response.text()
          console.log('注册响应文本:', textData)
          errorMessage = textData || errorMessage
        } catch (textError) {
          console.error('解析响应失败:', textError)
        }
      }
      console.error('注册失败:', errorMessage)
      ElMessage.error(errorMessage)
    }
  } catch (error) {
    console.error('注册请求失败:', error)
    ElMessage.error('注册失败，请检查网络连接')
  } finally {
    registerLoading.value = false
  }
}

// 上传头像
const uploadAvatar = async (file) => {
  try {
    // 检查文件类型
    const allowedTypes = ['image/jpeg', 'image/png', 'image/jpg']
    if (!allowedTypes.includes(file.type)) {
      ElMessage.error('只支持 JPG、PNG 格式的图片')
      return
    }
    
    // 检查文件大小 (2MB)
    const maxSize = 2 * 1024 * 1024
    if (file.size > maxSize) {
      ElMessage.error('图片大小不能超过2MB')
      return
    }
    
    // 调用后端API上传头像
    const response = await userApi.uploadAvatar(file)
    
    // 更新头像URL
    profileForm.avatar = response.avatarUrl
    
    // 更新store中的用户信息
    userStore.updateUserInfo({
      ...userStore.userInfo,
      avatar: response.avatarUrl
    })
    
    ElMessage.success('头像上传成功')
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error(error.message || '头像上传失败，请重试')
  }
}

// 保存个人资料
const saveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    
    // 调用后端API保存用户信息
    const profileData = {
      nickname: profileForm.nickname,
      email: profileForm.email,
      phone: profileForm.phone,
      birthday: profileForm.birthday,
      gender: profileForm.gender,
      avatar: profileForm.avatar
    }
    
    const response = await userApi.updateProfile(profileData)
    
    // 更新本地store
    userStore.updateUserInfo({
      name: profileForm.nickname,
      email: profileForm.email,
      phone: profileForm.phone,
      birthday: profileForm.birthday,
      gender: profileForm.gender,
      avatar: profileForm.avatar
    })
    
    ElMessage.success('个人资料保存成功')
  } catch (error) {
    console.error('保存个人资料失败:', error)
    ElMessage.error(error.message || '保存失败，请重试')
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
    // 调用后端API保存饮食偏好
    const response = await userApi.updatePreferences(preferences)
    
    // 更新本地store
    userStore.updatePreferences(preferences)
    
    ElMessage.success('饮食偏好保存成功')
  } catch (error) {
    console.error('保存饮食偏好失败:', error)
    ElMessage.error(error.message || '保存失败，请重试')
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
  
  // 如果已登录，同步用户信息到表单
  if (userStore.isLoggedIn) {
    Object.assign(profileForm, {
      nickname: userStore.userInfo.name || userStore.userInfo.username || '',
      email: userStore.userInfo.email || '',
      phone: userStore.userInfo.phone || '',
      birthday: userStore.userInfo.birthday || '',
      gender: userStore.userInfo.gender || '',
      avatar: userStore.userInfo.avatar || ''
    })
  }
})

// 监听登录状态变化
watch(() => userStore.isLoggedIn, (newVal) => {
  if (newVal) {
    // 登录后同步用户信息到表单
    Object.assign(profileForm, {
      nickname: userStore.userInfo.name || userStore.userInfo.username || '',
      email: userStore.userInfo.email || '',
      phone: userStore.userInfo.phone || '',
      birthday: userStore.userInfo.birthday || '',
      gender: userStore.userInfo.gender || '',
      avatar: userStore.userInfo.avatar || ''
    })
  } else {
    // 登出后清空表单
    Object.assign(profileForm, {
      nickname: '',
      email: '',
      phone: '',
      birthday: '',
      gender: '',
      avatar: ''
    })
  }
})
</script>

<style scoped>
.user-settings-page {
  min-height: 100vh;
  background: var(--background-color);
  padding: 20px;
}

/* 登录状态相关样式 */
.user-info-card {
  margin-bottom: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 20px;
}

.user-details h4 {
  margin: 0 0 4px 0;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.user-details p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.user-actions {
  display: flex;
  justify-content: flex-end;
}

.login-required {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

/* 认证相关样式 */
.auth-container {
  max-width: 400px;
  margin: 0 auto;
}

.auth-card {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.auth-header {
  display: flex;
  justify-content: center;
}

.auth-form {
  padding: 20px 0;
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
