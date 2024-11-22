import { createRouter, createWebHistory } from 'vue-router'
import systemRoutes from './modules/system'
import profileRoutes from './modules/profile'

// 基础路由
const baseRoutes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'UserLogin',
    component: () => import('@/views/UserLogin.vue')
  },
  {
    path: '/',
    component: () => import('@/components/layout/BaseLayout.vue'),
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/HomeView.vue'),
        meta: {
          title: '首页',
          icon: 'House',
          requiresAuth: true
        }
      }
    ]
  }
]

// 动态路由
const asyncRoutes = [
  systemRoutes,
  profileRoutes
]

const router = createRouter({
  history: createWebHistory(),
  routes: [...baseRoutes, ...asyncRoutes]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router 