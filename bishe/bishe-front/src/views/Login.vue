<template>
  <div class="login-page">
    <div class="bg-circle c1"></div>
    <div class="bg-circle c2"></div>
    <el-card class="login-card glass-card">
      <h2>员工登录</h2>
      <p class="sub">企业差旅报销系统</p>
      <el-form :model="form" @submit.prevent>
        <el-form-item><el-input v-model="form.username" placeholder="账号" /></el-form-item>
        <el-form-item><el-input v-model="form.password" type="password" show-password placeholder="密码" /></el-form-item>
        <el-form-item>
          <div class="captcha-row">
            <el-input v-model="form.code" placeholder="验证码" :disabled="!captchaEnabled" />
            <img v-if="captchaEnabled && captcha" :src="captcha" class="captcha" @click="loadCaptcha" />
            <div v-else class="captcha-placeholder" @click="loadCaptcha">
              {{ captchaEnabled ? '点击获取' : '验证码已关闭' }}
            </div>
          </div>
        </el-form-item>
        <el-button type="primary" style="width: 100%" :loading="loading" @click="submit">登录</el-button>
        <div class="form-foot">
          <router-link class="register-link" to="/register">没有账号？立即注册</router-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { getCodeImg } from '@/api/auth'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const auth = useAuthStore()

const loading = ref(false)
const captcha = ref('')
const captchaEnabled = ref(true)
const form = ref({ username: 'emp01', password: 'admin123', code: '', uuid: '' })

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
  loading.value = true
  try {
    if (captchaEnabled.value && !form.value.code.trim()) {
      ElMessage.warning('请输入验证码')
      return
    }
    await auth.loginAction(form.value)
    await auth.fetchUser()
    router.replace('/')
  } catch (error) {
    await loadCaptcha()
  } finally {
    loading.value = false
  }
}

loadCaptcha()
</script>

<style scoped>
.login-page {
  position: relative;
  min-height: 100vh;
  display: grid;
  place-items: center;
  overflow: hidden;
}
.login-card {
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
  width: 340px;
  height: 340px;
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
.register-link {
  color: var(--primary-deep);
  text-decoration: none;
  font-size: 14px;
}
.register-link:hover {
  color: var(--primary);
}
@media (max-width: 600px) {
  .login-card {
    width: calc(100% - 24px);
  }
}
</style>
