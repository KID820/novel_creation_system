<!-- src/views/Profile.vue -->
<template>
  <div class="profile-page">
    <!-- 导航栏 -->
    <Navbar />
    
    <div class="profile-container">
      <div class="profile-card">
        <div class="profile-header">
          <h2>个人设置</h2>
          <p>管理您的账户信息</p>
        </div>
        
        <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
          <el-form-item label="昵称" prop="nickname">
            <el-input 
              v-model="form.nickname" 
              placeholder="请输入昵称" 
              size="large"
            />
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input 
              v-model="form.email" 
              placeholder="请输入邮箱" 
              size="large"
              disabled
            />
          </el-form-item>

          <el-form-item label="头像">
            <div class="avatar-upload">
              <el-avatar :size="80" :src="form.avatar" />
              <el-button type="primary" size="small" class="upload-btn">
                更换头像
              </el-button>
            </div>
          </el-form-item>

          <el-form-item label="旧密码" prop="oldPassword">
            <el-input 
              v-model="form.oldPassword" 
              type="password" 
              placeholder="请输入旧密码" 
              show-password 
              size="large"
            />
          </el-form-item>

          <el-form-item label="新密码" prop="newPassword">
            <el-input 
              v-model="form.newPassword" 
              type="password" 
              placeholder="请输入新密码 (6~20位)" 
              show-password 
              size="large"
            />
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input 
              v-model="form.confirmPassword" 
              type="password" 
              placeholder="请再次输入新密码" 
              show-password 
              size="large"
            />
          </el-form-item>

          <el-form-item>
            <el-button 
              type="primary" 
              @click="handleSubmit" 
              size="large" 
              class="save-btn"
            >
              保存设置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import Navbar from '@/components/Navbar.vue'
 import { getProfile, updateProfile } from '@/api/user'

defineOptions({
  name: 'UserProfile'
})

const form = reactive({
  nickname: '',
  email: '',
  avatar: '',
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const formRef = ref()

const rules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度 2~10', trigger: 'blur' }
  ],
  email: [
    { required: false, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ],
  newPassword: [
    { min: 6, max: 20, message: '长度 6~20', trigger: 'blur' }
  ],
  confirmPassword: [
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

function validateConfirmPassword(rule, value, callback) {
  if (value && value !== form.newPassword) {
    callback(new Error('两次密码输入不一致'))
  } else {
    callback()
  }
}

// 获取用户信息
 async function fetchProfile() {
   try {
      const res = await getProfile()
      form.nickname = res.nickname
      form.email = res.email
      form.avatar = res.avatar || ''
  } catch (error) {
    console.error('获取用户信息失败:', error)
      ElMessage.error('获取用户信息失败')
  }
  }

// 提交表单
async function handleSubmit() {
  await formRef.value.validate()
  
  try {
    // 如果没有修改密码，不发送密码字段
    const requestData = {
      nickname: form.nickname
    }
    
    if (form.newPassword) {
      requestData.oldPassword = form.oldPassword
      requestData.newPassword = form.newPassword
    }
    
     await updateProfile(requestData)
    ElMessage.success('个人信息更新成功')
  } catch (error) {
    console.error('更新失败:', error)
    ElMessage.error('更新失败')
  }
}

onMounted(() => {
   fetchProfile()
})
</script>

<style scoped>
.profile-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #e6f7ff 0%, #ffffff 100%);
}

.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px); /* 减去导航栏高度 */
  padding: 20px;
}

.profile-card {
  width: 100%;
  max-width: 500px;
  padding: 40px 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 123, 255, 0.1);
  backdrop-filter: blur(10px);
}

.profile-header {
  text-align: center;
  margin-bottom: 30px;
}

.profile-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 8px;
}

.profile-header p {
  color: #666;
  font-size: 14px;
}

.avatar-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 15px;
}

.upload-btn {
  background: #1890ff;
  border-color: #1890ff;
}

.save-btn {
  width: 100%;
  background: #1890ff;
  border-color: #1890ff;
  margin-top: 10px;
}
</style>