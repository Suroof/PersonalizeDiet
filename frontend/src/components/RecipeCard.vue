<template>
  <div class="recipe-card">
    <div class="recipe-image">
      <img 
        :src="recipe.image || defaultImage" 
        :alt="recipe.name"
        @error="handleImageError"
      />
      <div class="recipe-badges">
        <el-tag v-if="recipe.difficulty" :type="getDifficultyType(recipe.difficulty)" size="small">
          {{ getDifficultyText(recipe.difficulty) }}
        </el-tag>
        <el-tag v-if="recipe.cookingTime" type="info" size="small">
          <el-icon><Clock /></el-icon>
          {{ recipe.cookingTime }}分钟
        </el-tag>
      </div>
      <div class="recipe-actions">
        <el-button 
          :icon="isFavorite ? StarFilled : Star"
          :type="isFavorite ? 'danger' : 'default'"
          size="small"
          circle
          @click="toggleFavorite"
        />
      </div>
    </div>
    
    <div class="recipe-content">
      <h4 class="recipe-title">{{ recipe.name }}</h4>
      <p class="recipe-description">{{ recipe.description }}</p>
      
      <!-- 营养信息 -->
      <div v-if="recipe.nutrition" class="nutrition-info">
        <div class="nutrition-item">
          <span class="label">热量:</span>
          <span class="value">{{ recipe.nutrition.calories }}卡</span>
        </div>
        <div class="nutrition-item">
          <span class="label">蛋白质:</span>
          <span class="value">{{ recipe.nutrition.protein }}g</span>
        </div>
      </div>
      
      <!-- 食材列表 -->
      <div v-if="recipe.ingredients" class="ingredients">
        <h5>主要食材：</h5>
        <div class="ingredient-tags">
          <el-tag 
            v-for="ingredient in recipe.ingredients.slice(0, 3)"
            :key="ingredient"
            size="small"
            effect="plain"
          >
            {{ ingredient }}
          </el-tag>
          <span v-if="recipe.ingredients.length > 3" class="more-ingredients">
            等{{ recipe.ingredients.length }}种
          </span>
        </div>
      </div>
      
      <!-- 操作按钮 -->
      <div class="recipe-footer">
        <el-button type="primary" size="small" @click="viewDetail">
          查看详情
        </el-button>
        <el-button size="small" @click="startCooking">
          开始制作
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { Clock, Star, StarFilled } from '@element-plus/icons-vue'
import { useRecipeStore } from '../store'
import { ElMessage } from 'element-plus'

const props = defineProps({
  recipe: {
    type: Object,
    required: true
  }
})

const router = useRouter()
const recipeStore = useRecipeStore()

// 默认图片
const defaultImage = '/assets/images/default-recipe.jpg'

// 计算属性
const isFavorite = computed(() => {
  return recipeStore.isFavorite(props.recipe.id)
})

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

// 处理图片加载错误
const handleImageError = (e) => {
  e.target.src = defaultImage
}

// 切换收藏状态
const toggleFavorite = () => {
  recipeStore.toggleFavorite(props.recipe.id)
  ElMessage.success(isFavorite.value ? '已添加到收藏' : '已取消收藏')
}

// 查看详情
const viewDetail = () => {
  recipeStore.setCurrentRecipe(props.recipe)
  router.push(`/recipe/${props.recipe.id}`)
}

// 开始制作
const startCooking = () => {
  ElMessage.success('开始制作功能开发中...')
}
</script>

<style scoped>
.recipe-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: var(--shadow-light);
  transition: all 0.3s ease;
  margin-bottom: 16px;
}

.recipe-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.recipe-image {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.recipe-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.recipe-card:hover .recipe-image img {
  transform: scale(1.05);
}

.recipe-badges {
  position: absolute;
  top: 12px;
  left: 12px;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.recipe-badges .el-tag {
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.9);
}

.recipe-actions {
  position: absolute;
  top: 12px;
  right: 12px;
}

.recipe-content {
  padding: 16px;
}

.recipe-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  line-height: 1.4;
}

.recipe-description {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: var(--text-regular);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.nutrition-info {
  display: flex;
  gap: 16px;
  margin-bottom: 12px;
  padding: 8px 12px;
  background: var(--background-color);
  border-radius: 6px;
}

.nutrition-item {
  font-size: 12px;
}

.nutrition-item .label {
  color: var(--text-secondary);
  margin-right: 4px;
}

.nutrition-item .value {
  color: var(--text-primary);
  font-weight: 500;
}

.ingredients {
  margin-bottom: 16px;
}

.ingredients h5 {
  margin: 0 0 8px 0;
  font-size: 13px;
  color: var(--text-regular);
  font-weight: 500;
}

.ingredient-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
}

.more-ingredients {
  font-size: 12px;
  color: var(--text-secondary);
}

.recipe-footer {
  display: flex;
  gap: 8px;
  justify-content: space-between;
}

.recipe-footer .el-button {
  flex: 1;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .recipe-image {
    height: 160px;
  }
  
  .recipe-content {
    padding: 12px;
  }
  
  .recipe-title {
    font-size: 15px;
  }
  
  .nutrition-info {
    flex-direction: column;
    gap: 4px;
  }
  
  .recipe-footer {
    flex-direction: column;
  }
}
</style>