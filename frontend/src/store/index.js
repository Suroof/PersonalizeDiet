import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { chatApi } from '../api/chat'

// 聊天状态管理
export const useChatStore = defineStore('chat', () => {
  const messages = ref([])
  const isLoading = ref(false)
  const currentStreamMessage = ref('')
  const conversationId = ref(null)

  // 添加消息
  const addMessage = (message) => {
    messages.value.push({
      id: Date.now(),
      ...message,
      timestamp: new Date()
    })
  }

  // 发送消息
  const sendMessage = async (content) => {
    // 添加用户消息
    addMessage({
      content,
      sender: 'user',
      type: 'text'
    })

    isLoading.value = true
    currentStreamMessage.value = ''

    try {
      // 添加AI消息占位符
      const aiMessageId = Date.now() + 1
      addMessage({
        id: aiMessageId,
        content: '',
        sender: 'ai',
        type: 'text',
        isStreaming: true
      })

      // 调用流式API
      await chatApi.sendStreamMessage(content, {
        onMessage: (data) => {
          currentStreamMessage.value += data
          // 更新最后一条AI消息
          const lastMessage = messages.value[messages.value.length - 1]
          if (lastMessage && lastMessage.sender === 'ai') {
            lastMessage.content = currentStreamMessage.value
          }
        },
        onComplete: () => {
          const lastMessage = messages.value[messages.value.length - 1]
          if (lastMessage && lastMessage.sender === 'ai') {
            lastMessage.isStreaming = false
          }
          currentStreamMessage.value = ''
          isLoading.value = false
        },
        onError: (error) => {
          console.error('Stream error:', error)
          const lastMessage = messages.value[messages.value.length - 1]
          if (lastMessage && lastMessage.sender === 'ai') {
            lastMessage.content = '抱歉，发生了错误，请稍后重试。'
            lastMessage.isStreaming = false
          }
          isLoading.value = false
        }
      })
    } catch (error) {
      console.error('Send message error:', error)
      isLoading.value = false
    }
  }

  // 清空聊天记录
  const clearMessages = () => {
    messages.value = []
    conversationId.value = null
  }

  // 计算属性
  const messageCount = computed(() => messages.value.length)
  const hasMessages = computed(() => messages.value.length > 0)

  return {
    messages,
    isLoading,
    currentStreamMessage,
    conversationId,
    messageCount,
    hasMessages,
    addMessage,
    sendMessage,
    clearMessages
  }
})

// 用户状态管理
export const useUserStore = defineStore('user', () => {
  const isLoggedIn = ref(false)
  const token = ref('')
  const userInfo = ref({
    id: '',
    username: '',
    email: '',
    name: '',
    avatar: '',
    phone: '',
    birthday: '',
    gender: '',
    preferences: {
      cuisine: [], // 菜系偏好
      dietary: [], // 饮食限制
      allergies: [], // 过敏信息
      cookingTime: 30, // 偏好烹饪时间
      difficulty: 'medium' // 难度偏好
    }
  })

  // 登录
  const login = (userData, authToken) => {
    isLoggedIn.value = true
    token.value = authToken
    userInfo.value = { ...userInfo.value, ...userData }
    
    // 保存到本地存储
    localStorage.setItem('token', authToken)
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    localStorage.setItem('isLoggedIn', 'true')
  }

  // 登出
  const logout = () => {
    isLoggedIn.value = false
    token.value = ''
    userInfo.value = {
      id: '',
      username: '',
      email: '',
      name: '',
      avatar: '',
      phone: '',
      birthday: '',
      gender: '',
      preferences: {
        cuisine: [],
        dietary: [],
        allergies: [],
        cookingTime: 30,
        difficulty: 'medium'
      }
    }
    
    // 清除本地存储
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    localStorage.removeItem('isLoggedIn')
  }

  const updateUserInfo = (info) => {
    userInfo.value = { ...userInfo.value, ...info }
    // 保存到本地存储
    if (isLoggedIn.value) {
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    }
  }

  const updatePreferences = (preferences) => {
    userInfo.value.preferences = { ...userInfo.value.preferences, ...preferences }
    if (isLoggedIn.value) {
      localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
    }
  }

  // 从本地存储加载用户信息
  const loadUserInfo = () => {
    const savedToken = localStorage.getItem('token')
    const savedUserInfo = localStorage.getItem('userInfo')
    const savedLoginStatus = localStorage.getItem('isLoggedIn')
    
    if (savedToken && savedUserInfo && savedLoginStatus === 'true') {
      token.value = savedToken
      userInfo.value = JSON.parse(savedUserInfo)
      isLoggedIn.value = true
    }
  }

  return {
    isLoggedIn,
    token,
    userInfo,
    login,
    logout,
    updateUserInfo,
    updatePreferences,
    loadUserInfo
  }
})

// 菜谱状态管理
export const useRecipeStore = defineStore('recipe', () => {
  const recipes = ref([])
  const currentRecipe = ref(null)
  const favorites = ref([])
  const searchHistory = ref([])

  const addRecipe = (recipe) => {
    recipes.value.push(recipe)
  }

  const setCurrentRecipe = (recipe) => {
    currentRecipe.value = recipe
  }

  const toggleFavorite = (recipeId) => {
    const index = favorites.value.indexOf(recipeId)
    if (index > -1) {
      favorites.value.splice(index, 1)
    } else {
      favorites.value.push(recipeId)
    }
    localStorage.setItem('favorites', JSON.stringify(favorites.value))
  }

  const addSearchHistory = (query) => {
    if (!searchHistory.value.includes(query)) {
      searchHistory.value.unshift(query)
      if (searchHistory.value.length > 10) {
        searchHistory.value.pop()
      }
      localStorage.setItem('searchHistory', JSON.stringify(searchHistory.value))
    }
  }

  const loadFavorites = () => {
    const saved = localStorage.getItem('favorites')
    if (saved) {
      favorites.value = JSON.parse(saved)
    }
  }

  const loadSearchHistory = () => {
    const saved = localStorage.getItem('searchHistory')
    if (saved) {
      searchHistory.value = JSON.parse(saved)
    }
  }

  const isFavorite = computed(() => (recipeId) => {
    return favorites.value.includes(recipeId)
  })

  return {
    recipes,
    currentRecipe,
    favorites,
    searchHistory,
    addRecipe,
    setCurrentRecipe,
    toggleFavorite,
    addSearchHistory,
    loadFavorites,
    loadSearchHistory,
    isFavorite
  }
})