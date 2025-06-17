<template>
  <div 
    class="floating-chat-ball"
    :class="{ 'is-dragging': isDragging }"
    :style="ballStyle"
    @mousedown="startDrag"
    @touchstart="startDrag"
    @click="handleClick"
    @contextmenu="handleRightClick"
  >
    <div class="ball-content">
      <el-icon class="chat-icon">
        <ChatDotRound />
      </el-icon>
      <div class="pulse-ring"></div>
    </div>
    
    <!-- 提示文字 -->
    <transition name="fade">
      <div v-if="showTip && !isDragging" class="tip-text">
        点击定制食谱
      </div>
    </transition>
    
    <!-- 右键菜单 -->
    <transition name="menu-fade">
      <div v-if="showMenu" class="context-menu" :style="menuStyle">
        <div class="menu-item" @click="openPreferenceForm">
          <el-icon><Document /></el-icon>
          <span>定制食谱</span>
        </div>
        <div class="menu-item" @click="openChat">
          <el-icon><ChatDotRound /></el-icon>
          <span>AI聊天</span>
        </div>
      </div>
    </transition>
    
    <!-- 遮罩层，用于关闭菜单 -->
    <div v-if="showMenu" class="menu-overlay" @click="closeMenu"></div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { ChatDotRound, Document } from '@element-plus/icons-vue'

const emit = defineEmits(['open-chat', 'open-preference-form'])

// 状态管理
const isDragging = ref(false)
const showTip = ref(true)
const showMenu = ref(false)
const ballPosition = reactive({
  x: window.innerWidth - 80,
  y: window.innerHeight - 150
})
const menuPosition = reactive({ x: 0, y: 0 })

// 拖拽相关
const dragStart = reactive({ x: 0, y: 0 })
const ballStart = reactive({ x: 0, y: 0 })

// 计算球的样式
const ballStyle = ref({
  left: ballPosition.x + 'px',
  top: ballPosition.y + 'px'
})

// 计算菜单样式
const menuStyle = ref({
  left: menuPosition.x + 'px',
  top: menuPosition.y + 'px'
})

// 开始拖拽
const startDrag = (e) => {
  e.preventDefault()
  isDragging.value = true
  showTip.value = false
  
  const clientX = e.type === 'touchstart' ? e.touches[0].clientX : e.clientX
  const clientY = e.type === 'touchstart' ? e.touches[0].clientY : e.clientY
  
  dragStart.x = clientX
  dragStart.y = clientY
  ballStart.x = ballPosition.x
  ballStart.y = ballPosition.y
  
  document.addEventListener('mousemove', onDrag)
  document.addEventListener('mouseup', stopDrag)
  document.addEventListener('touchmove', onDrag)
  document.addEventListener('touchend', stopDrag)
}

// 拖拽中
const onDrag = (e) => {
  if (!isDragging.value) return
  
  const clientX = e.type === 'touchmove' ? e.touches[0].clientX : e.clientX
  const clientY = e.type === 'touchmove' ? e.touches[0].clientY : e.clientY
  
  const deltaX = clientX - dragStart.x
  const deltaY = clientY - dragStart.y
  
  let newX = ballStart.x + deltaX
  let newY = ballStart.y + deltaY
  
  // 边界检测
  const ballSize = 60
  newX = Math.max(0, Math.min(window.innerWidth - ballSize, newX))
  newY = Math.max(0, Math.min(window.innerHeight - ballSize, newY))
  
  ballPosition.x = newX
  ballPosition.y = newY
  
  ballStyle.value = {
    left: newX + 'px',
    top: newY + 'px'
  }
}

// 停止拖拽
const stopDrag = () => {
  if (!isDragging.value) return
  
  isDragging.value = false
  
  // 吸壁效果
  const ballSize = 60
  const threshold = window.innerWidth / 2
  
  if (ballPosition.x < threshold) {
    // 吸附到左边
    ballPosition.x = 10
  } else {
    // 吸附到右边
    ballPosition.x = window.innerWidth - ballSize - 10
  }
  
  ballStyle.value = {
    left: ballPosition.x + 'px',
    top: ballPosition.y + 'px'
  }
  
  // 延迟显示提示
  setTimeout(() => {
    showTip.value = true
  }, 1000)
  
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
  document.removeEventListener('touchmove', onDrag)
  document.removeEventListener('touchend', stopDrag)
}

// 点击处理
const handleClick = () => {
  if (!isDragging.value && !showMenu.value) {
    emit('open-preference-form')
  }
  closeMenu()
}

// 右键点击处理
const handleRightClick = (e) => {
  e.preventDefault()
  if (isDragging.value) return
  
  // 计算菜单位置
  const menuWidth = 120
  const menuHeight = 80
  let x = ballPosition.x + 70
  let y = ballPosition.y
  
  // 边界检测
  if (x + menuWidth > window.innerWidth) {
    x = ballPosition.x - menuWidth - 10
  }
  if (y + menuHeight > window.innerHeight) {
    y = window.innerHeight - menuHeight - 10
  }
  
  menuPosition.x = x
  menuPosition.y = y
  
  menuStyle.value = {
    left: x + 'px',
    top: y + 'px'
  }
  
  showMenu.value = true
  showTip.value = false
}

// 打开偏好表单
const openPreferenceForm = () => {
  emit('open-preference-form')
  closeMenu()
}

// 打开聊天
const openChat = () => {
  emit('open-chat')
  closeMenu()
}

// 关闭菜单
const closeMenu = () => {
  showMenu.value = false
  setTimeout(() => {
    showTip.value = true
  }, 500)
}

// 窗口大小变化处理
const handleResize = () => {
  const ballSize = 60
  ballPosition.x = Math.min(ballPosition.x, window.innerWidth - ballSize)
  ballPosition.y = Math.min(ballPosition.y, window.innerHeight - ballSize)
  
  ballStyle.value = {
    left: ballPosition.x + 'px',
    top: ballPosition.y + 'px'
  }
}

// 自动隐藏提示
const autoHideTip = () => {
  setTimeout(() => {
    showTip.value = false
  }, 3000)
}

onMounted(() => {
  window.addEventListener('resize', handleResize)
  autoHideTip()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  document.removeEventListener('mousemove', onDrag)
  document.removeEventListener('mouseup', stopDrag)
  document.removeEventListener('touchmove', onDrag)
  document.removeEventListener('touchend', stopDrag)
})
</script>

<style scoped>
.floating-chat-ball {
  position: fixed;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  box-shadow: var(--shadow-base);
  cursor: pointer;
  z-index: 1000;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  user-select: none;
}

.floating-chat-ball:hover {
  transform: scale(1.1);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
}

.floating-chat-ball.is-dragging {
  transition: none;
  transform: scale(1.1);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.ball-content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chat-icon {
  font-size: 24px;
  color: white;
  z-index: 2;
}

.pulse-ring {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% {
    transform: translate(-50%, -50%) scale(1);
    opacity: 1;
  }
  100% {
    transform: translate(-50%, -50%) scale(1.5);
    opacity: 0;
  }
}

.tip-text {
  position: absolute;
  right: 70px;
  top: 50%;
  transform: translateY(-50%);
  background: rgba(0, 0, 0, 0.8);
  color: white;
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 12px;
  white-space: nowrap;
  pointer-events: none;
}

.tip-text::after {
  content: '';
  position: absolute;
  left: 100%;
  top: 50%;
  transform: translateY(-50%);
  border: 6px solid transparent;
  border-left-color: rgba(0, 0, 0, 0.8);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 菜单样式 */
.context-menu {
  position: fixed;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  padding: 8px 0;
  min-width: 120px;
  z-index: 1001;
  border: 1px solid #e4e7ed;
}

.menu-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-size: 14px;
  color: #606266;
}

.menu-item:hover {
  background-color: #f5f7fa;
  color: var(--primary-color);
}

.menu-item .el-icon {
  font-size: 16px;
}

.menu-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 1000;
  background: transparent;
}

.menu-fade-enter-active,
.menu-fade-leave-active {
  transition: all 0.2s ease;
}

.menu-fade-enter-from,
.menu-fade-leave-to {
  opacity: 0;
  transform: scale(0.8);
}

/* 移动端适配 */
@media (max-width: 768px) {
  .floating-chat-ball {
    width: 50px;
    height: 50px;
  }
  
  .chat-icon {
    font-size: 20px;
  }
  
  .tip-text {
    right: 60px;
    font-size: 11px;
    padding: 6px 10px;
  }
}
</style>