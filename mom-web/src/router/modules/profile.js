import BaseLayout from '@/components/layout/BaseLayout.vue'

export default {
  path: '/',
  component: BaseLayout,
  children: [
    {
      path: 'profile',
      name: 'Profile',
      component: () => import('@/views/profile/UserProfile.vue'),
      meta: {
        title: '个人信息',
        icon: 'User',
        requiresAuth: true
      }
    },
    {
      path: 'change-password',
      name: 'ChangePassword',
      component: () => import('@/views/profile/ChangePassword.vue'),
      meta: {
        title: '修改密码',
        icon: 'Lock',
        requiresAuth: true
      }
    }
  ]
} 