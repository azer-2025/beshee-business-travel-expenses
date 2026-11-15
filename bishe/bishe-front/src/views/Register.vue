<template>
  <div class="register-page">
    <div class="bg-circle c1"></div>
    <div class="bg-circle c2"></div>
    <el-card class="register-card glass-card">
      <h2>员工注册</h2>
      <p class="sub">创建企业差旅报销系统账号</p>
      <el-form ref="registerRef" :model="form" :rules="rules" @submit.prevent>
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" show-password placeholder="密码" />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input v-model="form.confirmPassword" type="password" show-password placeholder="确认密码" />
        </el-form-item>
        <el-form-item prop="code">
          <div class="captcha-row">
            <el-input v-model="form.code" placeholder="验证码" :disabled="!captchaEnabled" />
            <img v-if="captchaEnabled && captcha" :src="captcha" class="captcha" @click="loadCaptcha" />
            <div v-else class="captcha-placeholder" @click="loadCaptcha">
              {{ captchaEnabled ? '点击获取' : '验证码已关闭' }}
            </div>
          </div>
        </el-form-item>
        <el-button type="primary" style="width: 100%" :loading="loading" @click="submit">注册</el-button>
        <div class="form-foot">
          <router-link class="login-link" to="/login">已有账号？去登录</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { getCodeImg, register } from '@/api/auth'

const router = useRouter()

const registerRef = ref()
const loading = ref(false)
const captcha = ref('')
const captchaEnabled = ref(true)
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  code: '',
  uuid: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请再次输入密码'))
    return
  }
  if (value !== form.value.password) {
    callback(new Error('两次输入的密码不一致'))
    return
  }
  callback()
}

const rules = {
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 2, max: 20, message: '账号长度需在 2-20 位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 5, max: 20, message: '密码长度需在 5-20 位', trigger: 'blur' },
    { pattern: /^[^<>"'|\\]+$/, message: '密码不能包含非法字符', trigger: 'blur' }
  ],
  confirmPassword: [{ validator: validateConfirmPassword, trigger: 'blur' }]
}

async function loadCaptcha() {
  try {
    const res = await getCodeImg()
    captchaEnabled.value = res.captchaEnabled !== false
    form.value.code = ''
    if (captchaEnabled.value && res.img) {
      captcha.value = 'data:image/jpeg;base64,' + res.img
      form.value.uuid = res.uuid || ''
      return
    }
    captcha.value = ''
    form.value.uuid = ''
    if (!captchaEnabled.value) {
      ElMessage.warning('后端配置已关闭验证码')
    }
  } catch (error) {
    captchaEnabled.value = true
    captcha.value = ''
    form.value.uuid = ''
    ElMessage.error('验证码加载失败，请点击重试')
  }
}

async function submit() {
  if (!registerRef.value) {
    return
  }
  try {
    await registerRef.value.validate()
  } catch (error) {
    return
  }
  if (captchaEnabled.value && !form.value.code.trim()) {
    ElMessage.warning('请输入验证码')
    return
  }

  loading.value = true
  try {
    await register(form.value)
    ElMessage.success('注册成功，请登录')
    router.replace('/login')
  } catch (error) {
    await loadCaptcha()
  } finally {
    loading.value = false
  }
}

loadCaptcha()
</script>

<style scoped>
.register-page {
  position: relative;
  min-height: 100vh;
  display: grid;
  place-items: center;
  overflow: hidden;
}
.register-card {
  width: 420px;
  padding: 10px;
  z-index: 2;
}
.sub {
  margin-top: -6px;
  color: var(--text-sub);
  margin-bottom: 20px;
}
.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(2px);
}
.c1 {
  width: 360px;
  height: 360px;
  background: rgba(14, 165, 233, 0.2);
  left: -80px;
  top: -40px;
}
.c2 {
  width: 280px;
  height: 280px;
  background: rgba(249, 115, 22, 0.18);
  right: -60px;
  bottom: -30px;
}
.captcha-row {
  display: grid;
  grid-template-columns: 1fr 120px;
  gap: 10px;
  width: 100%;
}
.captcha {
  width: 120px;
  height: 40px;
  border-radius: 8px;
  cursor: pointer;
}
.captcha-placeholder {
  width: 120px;
  height: 40px;
  border-radius: 8px;
  border: 1px dashed #bfdbfe;
  color: #64748b;
  font-size: 13px;
  display: grid;
  place-items: center;
  cursor: pointer;
}
.form-foot {
  margin-top: 14px;
  text-align: right;
}
.login-link {
  color: var(--primary-deep);
  text-decoration: none;
  font-size: 14px;
}
.login-link:hover {
  color: var(--primary);
}
@media (max-width: 600px) {
  .register-card {
    width: calc(100% - 24px);
  }
}
</style>
