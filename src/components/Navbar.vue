<!-- src/components/Navbar.vue -->
<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <h2 class="navbar-title">幻笔写作平台</h2>
    </div>
    
    <div class="navbar-menu">
      <el-button 
        type="primary" 
        link 
        @click="$router.push('/dashboard')"
        :class="{ 'active': $route.path === '/dashboard' }"
      >
        工作台
      </el-button>
      <el-button 
        type="primary" 
        link 
        @click="$router.push('/profile')"
        :class="{ 'active': $route.path === '/profile' }"
      >
        个人设置
      </el-button>
    </div>
    
    <div class="navbar-user">
      <span class="username">用户名</span>
      <el-dropdown @command="handleUserCommand">
        <el-avatar :size="32" icon="User" class="user-avatar" />
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">个人设置</el-dropdown-item>
            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </nav>
</template>

<script setup>
import { ElMessage } from 'element-plus'

const handleUserCommand = (command) => {
  switch (command) {
    case 'profile':
      window.location.hash = '#/profile'
      break
    case 'logout':
      localStorage.removeItem('token')
      window.location.hash = '#/login'
      ElMessage.success('已退出登录')
      break
  }
}
</script>

<style scoped>
.navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #ebeef5;
}

.navbar-brand .navbar-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #1890ff;
}

.navbar-menu {
  display: flex;
  gap: 20px;
}

.navbar-menu .el-button {
  font-size: 16px;
  color: #606266;
}

.navbar-menu .el-button.active {
  color: #1890ff;
  font-weight: 500;
}

.navbar-user {
  display: flex;
  align-items: center;
  gap: 10px;
}

.username {
  font-size: 14px;
  color: #606266;
}

.user-avatar {
  cursor: pointer;
}
</style>