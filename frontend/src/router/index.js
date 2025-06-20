import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import RecipeDetail from '../views/RecipeDetail.vue'
import UserSettings from '../views/UserSettings.vue'
import NutritionAnalysis from '../views/NutritionAnalysis.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      title: '个性化食谱助手'
    }
  },
  {
    path: '/recipe/:id',
    name: 'RecipeDetail',
    component: RecipeDetail,
    meta: {
      title: '菜谱详情'
    }
  },
  {
    path: '/settings',
    name: 'UserSettings',
    component: UserSettings,
    meta: {
      title: '用户设置'
    }
  },
  {
    path: '/nutrition',
    name: 'NutritionAnalysis',
    component: NutritionAnalysis,
    meta: {
      title: '营养分析'
    }
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router