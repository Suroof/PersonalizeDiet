<template>
  <div id="app">
    <router-view />
    
    <!-- 悬浮聊天球 -->
    <FloatingChatBall 
      v-if="!isChatOpen && !isFormOpen" 
      @open-chat="openChat" 
      @open-preference-form="openPreferenceForm"
    />
    
    <!-- 偏好表单 -->
    <PreferenceForm 
      v-if="isFormOpen" 
      @close-form="closePreferenceForm"
      @recipe-generated="handleRecipeGenerated"
    />
    
    <!-- 聊天窗口 -->
    <ChatWindow 
      v-if="isChatOpen" 
      @close-chat="closeChat"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useChatStore } from './store'
import FloatingChatBall from './components/FloatingChatBall.vue'
import ChatWindow from './components/ChatWindow.vue'
import PreferenceForm from './components/PreferenceForm.vue'
import { ElMessage } from 'element-plus'

const router = useRouter()
const chatStore = useChatStore()
const isChatOpen = ref(false)
const isFormOpen = ref(false)

const openChat = () => {
  isChatOpen.value = true
  isFormOpen.value = false
}

const closeChat = () => {
  isChatOpen.value = false
}

const openPreferenceForm = () => {
  isFormOpen.value = true
  isChatOpen.value = false
}

const closePreferenceForm = () => {
  isFormOpen.value = false
}

const handleRecipeGenerated = (data) => {
  // 将生成的食谱添加到聊天记录中
  chatStore.addMessage({
    content: `根据您的偏好生成的个性化食谱：\n\n${data.recipe}`,
    sender: 'ai',
    type: 'text'
  })
  
  // 关闭表单，打开聊天窗口显示结果
  isFormOpen.value = false
  isChatOpen.value = true
  
  ElMessage.success('个性化食谱已生成，请查看聊天窗口')
}

// 监听全局事件
onMounted(() => {
  window.addEventListener('open-preference-form', openPreferenceForm)
  window.addEventListener('open-chat', openChat)
})
</script>

<style scoped>
#app {
  min-height: 100vh;
  position: relative;
}
</style>