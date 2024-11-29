export default {
  path: '/system',
  component: () => import('@/components/layout/BaseLayout.vue'),
  meta: {
    title: '系统管理',
    icon: 'Setting',
    requiresAuth: true
  },
  children: [
    {
      path: 'user',
      name: 'UserList',
      component: () => import('@/views/system/user/UserList.vue'),
      meta: {
        title: '用户管理'
      }
    }
  ]
} 