import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '@/utils/auth'

const routes = [
  { path: '/login', component: () => import('@/views/Login.vue') },
  { path: '/register', component: () => import('@/views/Register.vue') },
  {
    path: '/',
    component: () => import('@/layout/FrontLayout.vue'),
    redirect: '/profile',
    children: [
      { path: '/profile', component: () => import('@/views/travel/ProfileView.vue'), meta: { title: '个人信息管理' } },
      { path: '/apply', component: () => import('@/views/travel/ApplyView.vue'), meta: { title: '出差申请管理' } },
      { path: '/reimburse', component: () => import('@/views/travel/ReimburseView.vue'), meta: { title: '报销填报管理' } },
      { path: '/progress', component: () => import('@/views/travel/ProgressView.vue'), meta: { title: '进度查询管理' } },
      { path: '/news', component: () => import('@/views/travel/NewsView.vue'), meta: { title: '公司资讯管理' } }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = getToken()
  if (to.path === '/login' || to.path === '/register') {
    if (token) next('/')
    else next()
    return
  }
  if (!token) {
    next('/login')
    return
  }
  next()
})

export default router
