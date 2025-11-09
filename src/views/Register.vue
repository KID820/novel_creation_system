<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h2>注册</h2>
        <p>创建您的幻笔账户</p>
      </div>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="0">
        <el-form-item prop="nickname">
          <el-input 
            v-model="form.nickname" 
            placeholder="请输入昵称 (2~10个字符)" 
            size="large"
            prefix-icon="User"
          />
        </el-form-item>

        <el-form-item prop="email">
          <el-input 
            v-model="form.email" 
            placeholder="请输入邮箱" 
            size="large"
            prefix-icon="Message"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input 
            v-model="form.password" 
            type="password" 
            placeholder="请输入密码 (6~20位)" 
            show-password 
            size="large"
            prefix-icon="Lock"
          />
        </el-form-item>

        <el-form-item prop="confirm">
          <el-input 
            v-model="form.confirm" 
            type="password" 
            placeholder="请再次输入密码" 
            show-password 
            size="large"
            prefix-icon="Lock"
          />
        </el-form-item>

        <el-form-item>
          <el-button 
            type="primary" 
            @click="handleRegister" 
            size="large" 
            class="register-btn"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="auth-footer">
        <el-link type="primary" @click="$router.push('/login')">
          已有账号？去登录
        </el-link>
      </div>
    </div>
  </div>
</template>

<script setup lang="js">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/user'

defineOptions({
  name: 'UserRegister',
})

const form = reactive({
  nickname: '',
  email: '',
  password: '',
  confirm: '',
})
const formRef = ref()
const router = useRouter()

const validateConfirm = (_, value, callback) => {
  if (value !== form.password) callback(new Error('两次密码不一致'))
  else callback()
}

const rules = {
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 10, message: '长度 2~10', trigger: 'blur' },
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '长度 6~20', trigger: 'blur' },
  ],
  confirm: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirm, trigger: 'blur' },
  ],
}

const handleRegister = async () => {
  await formRef.value.validate()
  await register(form)
  ElMessage.success('注册成功，请登录')
  router.push('/login')
}
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #e6f7ff 0%, #ffffff 100%);
  padding: 20px;
}

.auth-card {
  width: 100%;
  max-width: 400px;
  padding: 40px 30px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 123, 255, 0.1);
  backdrop-filter: blur(10px);
}

.auth-header {
  text-align: center;
  margin-bottom: 30px;
}

.auth-header h2 {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin-bottom: 8px;
}

.auth-header p {
  color: #666;
  font-size: 14px;
}

.register-btn {
  width: 100%;
  background: #1890ff;
  border-color: #1890ff;
  margin-top: 10px;
}

.auth-footer {
  text-align: center;
  margin-top: 20px;
}
</style>