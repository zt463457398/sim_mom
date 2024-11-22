import { createRouter, createWebHistory } from 'vue-router'
import BaseLayout from '../components/layout/BaseLayout.vue'
import UserLogin from '../views/UserLogin.vue'
import HomeView from '../views/HomeView.vue'
import UserProfile from '../views/profile/UserProfile.vue'
import ChangePassword from '../views/profile/ChangePassword.vue'
import UserList from '../views/system/user/UserList.vue'

const routes = [
  {
    path: '/',
    redirect: '/home'
  },
  {
    path: '/login',
    name: 'UserLogin',
    component: UserLogin
  },
  {
    path: '/',
    component: BaseLayout,
    children: [
      {
        path: 'home',
        name: 'Home',
        component: HomeView,
        meta: { requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: UserProfile,
        meta: { requiresAuth: true }
      },
      {
        path: 'change-password',
        name: 'ChangePassword',
        component: ChangePassword,
        meta: { requiresAuth: true }
      },
      {
        path: 'system/user',
        name: 'UserList',
        component: UserList,
        meta: { 
          requiresAuth: true,
          title: '用户管理'
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
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