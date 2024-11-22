import BaseLayout from '@/components/layout/BaseLayout.vue'

export default {
  path: '/system',
  component: BaseLayout,
  meta: {
    title: '系统管理',
    icon: 'Setting'
  },
  children: [
    {
      path: 'user',
      name: 'UserList',
      component: () => import('@/views/system/user/UserList.vue'),
      meta: {
        title: '用户管理',
        icon: 'User',
        requiresAuth: true
      }
    }
  ]
} 