<template>
  <div class="home-page">
    <!-- 头部导航 -->
    <header class="page-header">
      <div class="header-content">
        <div class="logo">
          <el-icon class="logo-icon"><Restaurant /></el-icon>
          <h1>个性化食谱助手</h1>
        </div>
        <nav class="nav-menu">
          <el-button text @click="scrollToSection('features')">
            功能介绍
          </el-button>
          <el-button text @click="scrollToSection('recipes')">
            推荐菜谱
          </el-button>
          <el-button text @click="$router.push('/profile')">
            个人设置
          </el-button>
        </nav>
      </div>
    </header>

    <!-- 主横幅 -->
    <section class="hero-section">
      <div class="hero-content">
        <div class="hero-text">
          <h2>智能菜谱推荐，让烹饪更简单</h2>
          <p>基于AI技术，为您提供个性化的菜谱推荐和烹饪指导</p>
          <div class="hero-actions">
            <el-button type="primary" size="large" @click="openPreferenceForm">
              <el-icon><ChatDotRound /></el-icon>
              定制食谱
            </el-button>
            <el-button size="large" @click="scrollToSection('features')">
              了解更多
            </el-button>
          </div>
        </div>
        <div class="hero-image">
          <div class="floating-elements">
            <div class="floating-card" v-for="(item, index) in floatingItems" :key="index">
              <el-icon>{{ item.icon }}</el-icon>
              <span>{{ item.text }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能介绍 -->
    <section id="features" class="features-section">
      <div class="section-content">
        <h3 class="section-title">核心功能</h3>
        <div class="features-grid">
          <div 
            v-for="feature in features" 
            :key="feature.title"
            class="feature-card"
          >
            <div class="feature-icon">
              <el-icon>{{ feature.icon }}</el-icon>
            </div>
            <h4>{{ feature.title }}</h4>
            <p>{{ feature.description }}</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐菜谱 -->
    <section id="recipes" class="recipes-section">
      <div class="section-content">
        <div class="section-header">
          <h3 class="section-title">热门推荐</h3>
          <el-button type="primary" @click="loadMoreRecipes">
            查看更多
          </el-button>
        </div>
        
        <div class="recipes-grid">
          <RecipeCard 
            v-for="recipe in recommendedRecipes" 
            :key="recipe.id"
            :recipe="recipe"
          />
        </div>
        
        <div v-if="isLoadingRecipes" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
      </div>
    </section>

    <!-- 使用统计 -->
    <section class="stats-section">
      <div class="section-content">
        <div class="stats-grid">
          <div class="stat-item" v-for="stat in stats" :key="stat.label">
            <div class="stat-number">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="page-footer">
      <div class="footer-content">
        <div class="footer-info">
          <div class="logo">
            <el-icon class="logo-icon"><Restaurant /></el-icon>
            <span>个性化食谱助手</span>
          </div>
          <p>让每一餐都充满惊喜</p>
        </div>
        <div class="footer-links">
          <div class="link-group">
            <h5>产品</h5>
            <a href="#">功能介绍</a>
            <a href="#">使用指南</a>
            <a href="#">常见问题</a>
          </div>
          <div class="link-group">
            <h5>支持</h5>
            <a href="#">联系我们</a>
            <a href="#">意见反馈</a>
            <a href="#">隐私政策</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
// import { 
//   Restaurant, 
//   ChatDotRound, 
//   User, 
//   Clock, 
//   Star,
//   TrendCharts,
//   Food,
//   Guide
// } from '@element-plus/icons-vue'
import RecipeCard from '../components/RecipeCard.vue'
import { recipeApi } from '../api/chat'
import { ElMessage } from 'element-plus'

// 响应式数据
const recommendedRecipes = ref([])
const isLoadingRecipes = ref(false)

// 悬浮元素
const floatingItems = ref([
  { icon: Food, text: '美味菜谱' },
  { icon: Clock, text: '快速制作' },
  { icon: Star, text: '个性推荐' },
  { icon: Guide, text: '详细步骤' }
])

// 功能特性
const features = ref([
  {
    icon: ChatDotRound,
    title: '智能对话',
    description: '通过自然语言交互，轻松获取个性化菜谱推荐'
  },
  {
    icon: Food,
    title: '丰富菜谱',
    description: '涵盖各种菜系和口味，满足不同饮食需求'
  },
  {
    icon: User,
    title: '个性定制',
    description: '根据个人喜好和饮食限制，提供专属推荐'
  },
  {
    icon: TrendCharts,
    title: '营养分析',
    description: '详细的营养成分分析，助您健康饮食'
  }
])

// 统计数据
const stats = ref([
  { value: '10,000+', label: '菜谱数量' },
  { value: '5,000+', label: '用户数量' },
  { value: '50,000+', label: '制作次数' },
  { value: '98%', label: '满意度' }
])

// 模拟推荐菜谱数据
const mockRecipes = [
  {
    id: 1,
    name: '宫保鸡丁',
    description: '经典川菜，麻辣鲜香，下饭神器',
    image: '/images/gongbao-chicken.jpg',
    difficulty: 'medium',
    cookingTime: 25,
    nutrition: {
      calories: 320,
      protein: 28
    },
    ingredients: ['鸡胸肉', '花生米', '干辣椒', '花椒']
  },
  {
    id: 2,
    name: '番茄鸡蛋面',
    description: '简单易做的家常面条，营养丰富',
    image: '/images/tomato-egg-noodles.jpg',
    difficulty: 'easy',
    cookingTime: 15,
    nutrition: {
      calories: 280,
      protein: 15
    },
    ingredients: ['面条', '番茄', '鸡蛋', '葱花']
  },
  {
    id: 3,
    name: '红烧肉',
    description: '传统名菜，肥而不腻，入口即化',
    image: '/images/braised-pork.jpg',
    difficulty: 'hard',
    cookingTime: 90,
    nutrition: {
      calories: 450,
      protein: 25
    },
    ingredients: ['五花肉', '冰糖', '生抽', '老抽']
  }
]

// 打开偏好表单
const openPreferenceForm = () => {
  // 触发父组件的表单开启事件
  window.dispatchEvent(new CustomEvent('open-preference-form'))
}

// 滚动到指定区域
const scrollToSection = (sectionId) => {
  const element = document.getElementById(sectionId)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth' })
  }
}

// 加载推荐菜谱
const loadRecommendedRecipes = async () => {
  isLoadingRecipes.value = true
  try {
    // 这里应该调用真实的API
    // const response = await recipeApi.getRecommendedRecipes()
    // recommendedRecipes.value = response.data
    
    // 模拟API调用
    setTimeout(() => {
      recommendedRecipes.value = mockRecipes
      isLoadingRecipes.value = false
    }, 1000)
  } catch (error) {
    console.error('加载推荐菜谱失败:', error)
    ElMessage.error('加载推荐菜谱失败')
    isLoadingRecipes.value = false
  }
}

// 加载更多菜谱
const loadMoreRecipes = () => {
  ElMessage.info('更多菜谱功能开发中...')
}

// 监听表单开启事件
const handleOpenPreferenceForm = () => {
  openPreferenceForm()
}

onMounted(() => {
  loadRecommendedRecipes()
  window.addEventListener('open-preference-form', handleOpenPreferenceForm)
})
</script>

<style scoped>
.home-page {
  min-height: 100vh;
}

/* 头部导航 */
.page-header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  color: white;
}

.logo-icon {
  font-size: 28px;
  color: var(--primary-color);
}

.logo h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.nav-menu {
  display: flex;
  gap: 20px;
}

/* 主横幅 */
.hero-section {
  padding: 80px 20px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--secondary-color) 100%);
  color: white;
  overflow: hidden;
}

.hero-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
}

.hero-text h2 {
  margin: 0 0 20px 0;
  font-size: 48px;
  font-weight: 700;
  line-height: 1.2;
}

.hero-text p {
  margin: 0 0 30px 0;
  font-size: 18px;
  opacity: 0.9;
  line-height: 1.6;
}

.hero-actions {
  display: flex;
  gap: 16px;
}

.hero-image {
  position: relative;
  height: 400px;
}

.floating-elements {
  position: relative;
  width: 100%;
  height: 100%;
}

.floating-card {
  position: absolute;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  padding: 16px 20px;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  animation: float 6s ease-in-out infinite;
}

.floating-card:nth-child(1) {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}

.floating-card:nth-child(2) {
  top: 10%;
  right: 20%;
  animation-delay: 1.5s;
}

.floating-card:nth-child(3) {
  bottom: 30%;
  left: 20%;
  animation-delay: 3s;
}

.floating-card:nth-child(4) {
  bottom: 20%;
  right: 10%;
  animation-delay: 4.5s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0px);
  }
  50% {
    transform: translateY(-20px);
  }
}

/* 通用区域样式 */
.section-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-title {
  text-align: center;
  font-size: 36px;
  font-weight: 600;
  margin: 0 0 50px 0;
  color: var(--text-primary);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 40px;
}

/* 功能介绍 */
.features-section {
  padding: 80px 20px;
  background: white;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
}

.feature-card {
  text-align: center;
  padding: 30px 20px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--shadow-base);
}

.feature-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
}

.feature-card h4 {
  margin: 0 0 12px 0;
  font-size: 20px;
  font-weight: 600;
  color: var(--text-primary);
}

.feature-card p {
  margin: 0;
  color: var(--text-regular);
  line-height: 1.6;
}

/* 推荐菜谱 */
.recipes-section {
  padding: 80px 20px;
  background: var(--background-color);
}

.recipes-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 24px;
}

.loading-container {
  margin-top: 40px;
}

/* 统计数据 */
.stats-section {
  padding: 60px 20px;
  background: linear-gradient(135deg, var(--primary-color), var(--secondary-color));
  color: white;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 40px;
  text-align: center;
}

.stat-number {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 16px;
  opacity: 0.9;
}

/* 页脚 */
.page-footer {
  background: #2c3e50;
  color: white;
  padding: 40px 20px 20px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 2fr;
  gap: 40px;
}

.footer-info .logo {
  margin-bottom: 16px;
}

.footer-info .logo span {
  font-size: 18px;
  font-weight: 600;
}

.footer-info p {
  margin: 0;
  opacity: 0.8;
}

.footer-links {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 30px;
}

.link-group h5 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 600;
}

.link-group a {
  display: block;
  color: rgba(255, 255, 255, 0.8);
  text-decoration: none;
  margin-bottom: 8px;
  transition: color 0.2s;
}

.link-group a:hover {
  color: white;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    height: auto;
    padding: 16px 20px;
    gap: 16px;
  }
  
  .hero-content {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }
  
  .hero-text h2 {
    font-size: 32px;
  }
  
  .hero-actions {
    justify-content: center;
  }
  
  .section-title {
    font-size: 28px;
  }
  
  .section-header {
    flex-direction: column;
    gap: 20px;
    text-align: center;
  }
  
  .footer-content {
    grid-template-columns: 1fr;
    gap: 30px;
  }
}
</style>