<template>
  <div class="recipe-detail-page">
    <!-- 返回按钮 -->
    <div class="back-button">
      <el-button @click="goBack" :icon="ArrowLeft">
        返回
      </el-button>
    </div>

    <div v-if="recipe" class="recipe-container">
      <!-- 菜谱头部 -->
      <div class="recipe-header">
        <div class="recipe-image">
          <img :src="recipe.image || defaultImage" :alt="recipe.name" />
          <div class="image-overlay">
            <div class="recipe-badges">
              <el-tag :type="getDifficultyType(recipe.difficulty)" size="large">
                {{ getDifficultyText(recipe.difficulty) }}
              </el-tag>
              <el-tag type="info" size="large">
                <el-icon><Clock /></el-icon>
                {{ recipe.cookingTime }}分钟
              </el-tag>
            </div>
            <div class="recipe-actions">
              <el-button 
                :type="isFavorite ? 'danger' : 'default'"
                :icon="isFavorite ? StarFilled : Star"
                @click="toggleFavorite"
                size="large"
              >
                {{ isFavorite ? '已收藏' : '收藏' }}
              </el-button>
              <el-button type="primary" size="large" @click="startCooking">
                <el-icon><VideoPlay /></el-icon>
                开始制作
              </el-button>
            </div>
          </div>
        </div>
        
        <div class="recipe-info">
          <h1 class="recipe-title">{{ recipe.name }}</h1>
          <p class="recipe-description">{{ recipe.description }}</p>
          
          <!-- 营养信息 -->
          <div class="nutrition-panel">
            <h3>营养信息</h3>
            <div class="nutrition-grid">
              <div class="nutrition-item">
                <div class="nutrition-value">{{ recipe.nutrition?.calories || 0 }}</div>
                <div class="nutrition-label">卡路里</div>
              </div>
              <div class="nutrition-item">
                <div class="nutrition-value">{{ recipe.nutrition?.protein || 0 }}g</div>
                <div class="nutrition-label">蛋白质</div>
              </div>
              <div class="nutrition-item">
                <div class="nutrition-value">{{ recipe.nutrition?.carbs || 0 }}g</div>
                <div class="nutrition-label">碳水化合物</div>
              </div>
              <div class="nutrition-item">
                <div class="nutrition-value">{{ recipe.nutrition?.fat || 0 }}g</div>
                <div class="nutrition-label">脂肪</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 菜谱内容 -->
      <div class="recipe-content">
        <div class="content-tabs">
          <el-tabs v-model="activeTab" class="recipe-tabs">
            <!-- 食材清单 -->
            <el-tab-pane label="食材清单" name="ingredients">
              <div class="ingredients-section">
                <div class="serving-size">
                  <span>份量：</span>
                  <el-input-number 
                    v-model="servingSize" 
                    :min="1" 
                    :max="10"
                    @change="updateIngredients"
                  />
                  <span>人份</span>
                </div>
                
                <div class="ingredients-list">
                  <div 
                    v-for="(ingredient, index) in scaledIngredients"
                    :key="index"
                    class="ingredient-item"
                  >
                    <el-checkbox v-model="ingredient.checked">
                      <span class="ingredient-name">{{ ingredient.name }}</span>
                      <span class="ingredient-amount">{{ ingredient.amount }}</span>
                    </el-checkbox>
                  </div>
                </div>
                
                <div class="shopping-actions">
                  <el-button type="primary" @click="generateShoppingList">
                    <el-icon><ShoppingCart /></el-icon>
                    生成购物清单
                  </el-button>
                </div>
              </div>
            </el-tab-pane>

            <!-- 制作步骤 -->
            <el-tab-pane label="制作步骤" name="steps">
              <div class="steps-section">
                <div 
                  v-for="(step, index) in recipe.steps"
                  :key="index"
                  class="step-item"
                  :class="{ 'step-completed': step.completed }"
                >
                  <div class="step-number">
                    <span v-if="!step.completed">{{ index + 1 }}</span>
                    <el-icon v-else class="check-icon"><Check /></el-icon>
                  </div>
                  <div class="step-content">
                    <div class="step-text">{{ step.description }}</div>
                    <div v-if="step.image" class="step-image">
                      <img :src="step.image" :alt="`步骤${index + 1}`" />
                    </div>
                    <div v-if="step.tips" class="step-tips">
                      <el-icon><InfoFilled /></el-icon>
                      <span>{{ step.tips }}</span>
                    </div>
                  </div>
                  <div class="step-actions">
                    <el-button 
                      size="small"
                      @click="toggleStepComplete(index)"
                      :type="step.completed ? 'success' : 'default'"
                    >
                      {{ step.completed ? '已完成' : '标记完成' }}
                    </el-button>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 烹饪技巧 -->
            <el-tab-pane label="烹饪技巧" name="tips">
              <div class="tips-section">
                <div v-for="(tip, index) in recipe.cookingTips" :key="index" class="tip-item">
                  <el-icon class="tip-icon"><Lightbulb /></el-icon>
                  <div class="tip-content">
                    <h4>{{ tip.title }}</h4>
                    <p>{{ tip.content }}</p>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <!-- 评价反馈 -->
            <el-tab-pane label="评价反馈" name="reviews">
              <div class="reviews-section">
                <div class="rating-summary">
                  <div class="rating-score">
                    <span class="score">{{ recipe.rating || 4.5 }}</span>
                    <el-rate v-model="recipe.rating" disabled show-score />
                  </div>
                  <div class="rating-count">
                    基于 {{ recipe.reviewCount || 128 }} 条评价
                  </div>
                </div>
                
                <div class="add-review">
                  <h4>分享您的制作体验</h4>
                  <el-rate v-model="userRating" />
                  <el-input
                    v-model="userReview"
                    type="textarea"
                    :rows="3"
                    placeholder="写下您的制作心得..."
                  />
                  <el-button type="primary" @click="submitReview">
                    提交评价
                  </el-button>
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>

    <!-- 加载状态 -->
    <div v-else class="loading-container">
      <el-skeleton :rows="8" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
// import { 
//   ArrowLeft, 
//   Clock, 
//   Star, 
//   StarFilled, 
//   VideoPlay,
//   ShoppingCart,
//   Check,
//   InfoFilled,
//   Lightbulb
// } from '@element-plus/icons-vue'
import { useRecipeStore } from '../store'
import { recipeApi } from '../api/chat'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const recipeStore = useRecipeStore()

// 响应式数据
const recipe = ref(null)
const activeTab = ref('ingredients')
const servingSize = ref(2)
const scaledIngredients = ref([])
const userRating = ref(0)
const userReview = ref('')
const defaultImage = '/images/default-recipe.jpg'

// 计算属性
const isFavorite = computed(() => {
  return recipe.value ? recipeStore.isFavorite(recipe.value.id) : false
})

// 模拟菜谱数据
const mockRecipe = {
  id: 1,
  name: '宫保鸡丁',
  description: '经典川菜，麻辣鲜香，口感丰富。选用优质鸡胸肉，搭配花生米和干辣椒，调味独特，是下饭的绝佳选择。',
  image: '/images/gongbao-chicken.jpg',
  difficulty: 'medium',
  cookingTime: 25,
  rating: 4.6,
  reviewCount: 156,
  nutrition: {
    calories: 320,
    protein: 28,
    carbs: 15,
    fat: 18
  },
  ingredients: [
    { name: '鸡胸肉', amount: '300g', baseAmount: 300 },
    { name: '花生米', amount: '50g', baseAmount: 50 },
    { name: '干辣椒', amount: '10个', baseAmount: 10 },
    { name: '花椒', amount: '1茶匙', baseAmount: 1 },
    { name: '葱', amount: '2根', baseAmount: 2 },
    { name: '姜', amount: '1块', baseAmount: 1 },
    { name: '蒜', amount: '3瓣', baseAmount: 3 },
    { name: '生抽', amount: '2汤匙', baseAmount: 2 },
    { name: '料酒', amount: '1汤匙', baseAmount: 1 },
    { name: '糖', amount: '1茶匙', baseAmount: 1 }
  ],
  steps: [
    {
      description: '将鸡胸肉切成1.5cm见方的丁，用料酒、生抽、盐腌制15分钟',
      image: '/images/step1.jpg',
      tips: '鸡肉切丁要大小均匀，腌制时间不宜过长',
      completed: false
    },
    {
      description: '花生米用油炸至金黄色，捞出沥油备用',
      tips: '花生米要用小火慢炸，避免外焦内生',
      completed: false
    },
    {
      description: '热锅下油，下入腌好的鸡丁炒至变色盛起',
      tips: '鸡丁下锅后要快速翻炒，保持嫩滑',
      completed: false
    },
    {
      description: '锅内留底油，下干辣椒和花椒爆香',
      tips: '火候要控制好，避免辣椒炒糊',
      completed: false
    },
    {
      description: '下入葱姜蒜爆香，倒入鸡丁翻炒',
      completed: false
    },
    {
      description: '调入生抽、糖，最后撒入花生米炒匀即可',
      tips: '最后调味要快，保持花生米的酥脆',
      completed: false
    }
  ],
  cookingTips: [
    {
      title: '选材技巧',
      content: '选择新鲜的鸡胸肉，肉质紧实有弹性。花生米要选择颗粒饱满的优质品种。'
    },
    {
      title: '火候掌握',
      content: '炒制过程要用大火快炒，保持食材的鲜嫩口感。花椒和干辣椒不要炒过头。'
    },
    {
      title: '调味秘诀',
      content: '糖的用量要适中，既能提鲜又不会过甜。生抽的咸度要根据个人口味调整。'
    }
  ]
}

// 获取难度类型
const getDifficultyType = (difficulty) => {
  const types = {
    'easy': 'success',
    'medium': 'warning',
    'hard': 'danger'
  }
  return types[difficulty] || 'info'
}

// 获取难度文本
const getDifficultyText = (difficulty) => {
  const texts = {
    'easy': '简单',
    'medium': '中等',
    'hard': '困难'
  }
  return texts[difficulty] || difficulty
}

// 返回上一页
const goBack = () => {
  router.go(-1)
}

// 切换收藏状态
const toggleFavorite = () => {
  if (recipe.value) {
    recipeStore.toggleFavorite(recipe.value.id)
    ElMessage.success(isFavorite.value ? '已添加到收藏' : '已取消收藏')
  }
}

// 开始制作
const startCooking = () => {
  activeTab.value = 'steps'
  ElMessage.success('开始制作模式已启动！')
}

// 更新食材用量
const updateIngredients = () => {
  if (recipe.value) {
    scaledIngredients.value = recipe.value.ingredients.map(ingredient => ({
      ...ingredient,
      amount: scaleAmount(ingredient.baseAmount, ingredient.name),
      checked: false
    }))
  }
}

// 按比例缩放用量
const scaleAmount = (baseAmount, unit) => {
  const scaled = (baseAmount * servingSize.value / 2)
  
  if (unit.includes('g')) {
    return `${scaled}g`
  } else if (unit.includes('汤匙') || unit.includes('茶匙')) {
    return `${scaled}${unit.includes('汤匙') ? '汤匙' : '茶匙'}`
  } else if (unit.includes('个') || unit.includes('根') || unit.includes('块') || unit.includes('瓣')) {
    return `${Math.ceil(scaled)}${unit.slice(-1)}`
  }
  return `${scaled}${unit}`
}

// 生成购物清单
const generateShoppingList = () => {
  const uncheckedItems = scaledIngredients.value
    .filter(item => !item.checked)
    .map(item => `${item.name} ${item.amount}`)
    .join('\n')
  
  if (uncheckedItems) {
    navigator.clipboard.writeText(uncheckedItems).then(() => {
      ElMessage.success('购物清单已复制到剪贴板')
    })
  } else {
    ElMessage.info('所有食材都已准备完毕')
  }
}

// 切换步骤完成状态
const toggleStepComplete = (index) => {
  if (recipe.value) {
    recipe.value.steps[index].completed = !recipe.value.steps[index].completed
  }
}

// 提交评价
const submitReview = () => {
  if (userRating.value === 0) {
    ElMessage.warning('请先给出评分')
    return
  }
  
  if (!userReview.value.trim()) {
    ElMessage.warning('请写下您的制作心得')
    return
  }
  
  ElMessage.success('评价提交成功，感谢您的反馈！')
  userRating.value = 0
  userReview.value = ''
}

// 加载菜谱详情
const loadRecipeDetail = async () => {
  const recipeId = route.params.id
  
  try {
    // 先检查store中是否有当前菜谱
    if (recipeStore.currentRecipe && recipeStore.currentRecipe.id == recipeId) {
      recipe.value = recipeStore.currentRecipe
    } else {
      // 这里应该调用真实的API
      // const response = await recipeApi.getRecipeDetail(recipeId)
      // recipe.value = response.data
      
      // 模拟API调用
      setTimeout(() => {
        recipe.value = mockRecipe
      }, 500)
    }
    
    // 初始化食材列表
    updateIngredients()
  } catch (error) {
    console.error('加载菜谱详情失败:', error)
    ElMessage.error('加载菜谱详情失败')
  }
}

onMounted(() => {
  loadRecipeDetail()
})
</script>

<style scoped>
.recipe-detail-page {
  min-height: 100vh;
  background: var(--background-color);
}

.back-button {
  padding: 20px;
  background: white;
  border-bottom: 1px solid var(--border-color);
}

.recipe-container {
  max-width: 1200px;
  margin: 0 auto;
}

/* 菜谱头部 */
.recipe-header {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  padding: 40px 20px;
  background: white;
  margin-bottom: 20px;
}

.recipe-image {
  position: relative;
  border-radius: 16px;
  overflow: hidden;
  height: 400px;
}

.recipe-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.3) 0%, rgba(0,0,0,0) 50%, rgba(0,0,0,0.7) 100%);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
}

.recipe-badges {
  display: flex;
  gap: 12px;
}

.recipe-actions {
  display: flex;
  gap: 12px;
  align-self: flex-end;
}

.recipe-info {
  padding: 20px 0;
}

.recipe-title {
  margin: 0 0 16px 0;
  font-size: 36px;
  font-weight: 700;
  color: var(--text-primary);
  line-height: 1.2;
}

.recipe-description {
  margin: 0 0 30px 0;
  font-size: 16px;
  color: var(--text-regular);
  line-height: 1.6;
}

.nutrition-panel {
  background: var(--background-color);
  border-radius: 12px;
  padding: 24px;
}

.nutrition-panel h3 {
  margin: 0 0 20px 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--text-primary);
}

.nutrition-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.nutrition-item {
  text-align: center;
}

.nutrition-value {
  font-size: 24px;
  font-weight: 700;
  color: var(--primary-color);
  margin-bottom: 4px;
}

.nutrition-label {
  font-size: 12px;
  color: var(--text-secondary);
}

/* 菜谱内容 */
.recipe-content {
  background: white;
  border-radius: 16px;
  margin: 0 20px 40px;
  overflow: hidden;
}

.content-tabs {
  padding: 0;
}

.recipe-tabs {
  padding: 0 30px;
}

/* 食材清单 */
.ingredients-section {
  padding: 30px;
}

.serving-size {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 30px;
  padding: 16px;
  background: var(--background-color);
  border-radius: 8px;
}

.ingredients-list {
  margin-bottom: 30px;
}

.ingredient-item {
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color);
  display: flex;
  align-items: center;
}

.ingredient-item:last-child {
  border-bottom: none;
}

.ingredient-name {
  flex: 1;
  font-weight: 500;
}

.ingredient-amount {
  color: var(--text-secondary);
  margin-left: 12px;
}

.shopping-actions {
  text-align: center;
}

/* 制作步骤 */
.steps-section {
  padding: 30px;
}

.step-item {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
  padding: 20px;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.step-item:hover {
  background: var(--background-color);
}

.step-item.step-completed {
  background: rgba(103, 194, 58, 0.1);
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--primary-color);
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  flex-shrink: 0;
}

.step-completed .step-number {
  background: var(--success-color);
}

.check-icon {
  font-size: 20px;
}

.step-content {
  flex: 1;
}

.step-text {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 12px;
}

.step-image {
  margin: 12px 0;
}

.step-image img {
  width: 200px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
}

.step-tips {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: rgba(230, 162, 60, 0.1);
  border-radius: 6px;
  font-size: 14px;
  color: var(--warning-color);
}

.step-actions {
  display: flex;
  align-items: flex-start;
}

/* 烹饪技巧 */
.tips-section {
  padding: 30px;
}

.tip-item {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  padding: 20px;
  background: var(--background-color);
  border-radius: 12px;
}

.tip-icon {
  font-size: 24px;
  color: var(--warning-color);
  flex-shrink: 0;
  margin-top: 4px;
}

.tip-content h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
}

.tip-content p {
  margin: 0;
  color: var(--text-regular);
  line-height: 1.6;
}

/* 评价反馈 */
.reviews-section {
  padding: 30px;
}

.rating-summary {
  text-align: center;
  margin-bottom: 40px;
  padding: 30px;
  background: var(--background-color);
  border-radius: 12px;
}

.rating-score {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 12px;
}

.score {
  font-size: 48px;
  font-weight: 700;
  color: var(--primary-color);
}

.rating-count {
  color: var(--text-secondary);
}

.add-review {
  max-width: 600px;
  margin: 0 auto;
}

.add-review h4 {
  margin: 0 0 20px 0;
  font-size: 18px;
  font-weight: 600;
}

.add-review .el-rate {
  margin-bottom: 16px;
}

.add-review .el-textarea {
  margin-bottom: 16px;
}

.loading-container {
  padding: 40px 20px;
  max-width: 1200px;
  margin: 0 auto;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .recipe-header {
    grid-template-columns: 1fr;
    gap: 20px;
    padding: 20px;
  }
  
  .recipe-image {
    height: 250px;
  }
  
  .recipe-title {
    font-size: 28px;
  }
  
  .nutrition-grid {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .recipe-tabs {
    padding: 0 20px;
  }
  
  .ingredients-section,
  .steps-section,
  .tips-section,
  .reviews-section {
    padding: 20px;
  }
  
  .step-item {
    flex-direction: column;
    gap: 12px;
  }
  
  .step-actions {
    align-self: flex-start;
  }
}
</style>