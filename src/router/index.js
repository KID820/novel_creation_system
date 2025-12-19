import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Register from '@/views/Register.vue'
import Dashboard from '@/views/Dashboard.vue'
import Profile from '@/views/Profile.vue'

const routes = [
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/dashboard', component: Dashboard },
  { path: '/profile', component: Profile },
  { path: '/', redirect: '/login' }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 添加路由守卫（没有后端联调不使用）
/* router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (!token && to.path !== '/login' && to.path !== '/register') {
    next('/login')
  } else if (token && (to.path === '/login' || to.path === '/register')) {
    next('/dashboard')
  } else {
    next()
  }
}) */

export default router