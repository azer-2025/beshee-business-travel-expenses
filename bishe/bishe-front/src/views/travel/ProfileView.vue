<template>
  <div class="profile-grid">
    <el-card class="glass-card side-card">
      <div class="avatar-wrap">
        <el-avatar :src="form.avatarUrl || auth.user?.avatar" :size="100">{{ auth.user?.userName?.slice(0,1) }}</el-avatar>
        <el-upload
          :action="uploadUrl"
          :headers="uploadHeaders"
          :show-file-list="false"
          :on-success="onAvatarSuccess"
          :on-error="onAvatarError"
          :before-upload="beforeAvatarUpload"
          accept="image/*"
        >
          <el-button size="small" type="primary">上传头像</el-button>
        </el-upload>
      </div>
      <h3>{{ auth.user?.userName }}</h3>
      <p class="muted">ID: {{ auth.user?.userName }}</p>
      <div class="meta-line">部门：{{ form.deptName || '-' }}</div>
      <div class="meta-line">电话：{{ form.phone || '-' }}</div>
      <div class="meta-line">邮箱：{{ form.email || '-' }}</div>
    </el-card>

    <el-card class="glass-card main-card">
      <template #header><span>档案详细信息</span></template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-row :gutter="16">
          <el-col :span="12"><el-form-item label="真实姓名" prop="realName"><el-input v-model="form.realName"/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="所属部门"><el-input v-model="form.deptName" disabled/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone"/></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="联系邮箱" prop="email"><el-input v-model="form.email"/></el-form-item></el-col>
          <el-col :span="12">
            <el-form-item label="收款银行" prop="bankName">
              <el-select v-model="form.bankName" placeholder="请选择收款银行" style="width: 100%" filterable clearable>
                <el-option v-for="item in bankOptions" :key="item.dictValue" :label="item.dictLabel" :value="item.dictValue" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12"><el-form-item label="银行账号" prop="bankAccount"><el-input v-model="form.bankAccount"/></el-form-item></el-col>
          <el-col :span="24"><el-form-item label="常用地址"><el-input v-model="form.contactAddress"/></el-form-item></el-col>
          <el-col :span="24"><el-button type="primary" :loading="saving" @click="save">保存并同步</el-button></el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { getToken } from '@/utils/auth'
import { travelApi } from '@/api/travel'
import { useAuthStore } from '@/store/auth'

const auth = useAuthStore()
const form = ref({})
const formRef = ref()
const saving = ref(false)
const bankOptions = ref([])

const uploadUrl = '/dev-api/common/upload'
const uploadHeaders = computed(() => ({ Authorization: 'Bearer ' + getToken() }))
const rules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  email: [{ type: 'email', message: '邮箱格式不正确', trigger: 'blur' }],
  bankName: [{ required: true, message: '请选择收款银行', trigger: 'change' }],
  bankAccount: [{ required: true, message: '请输入收款银行账号', trigger: 'blur' }]
}

function beforeAvatarUpload(rawFile) {
  const isImage = rawFile.type && rawFile.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('仅支持上传图片格式头像')
    return false
  }
  const isLt5M = rawFile.size / 1024 / 1024 < 5
  if (!isLt5M) {
    ElMessage.error('头像图片不能超过 5MB')
    return false
  }
  return true
}

function onAvatarSuccess(res) {
  if (res.code === 200) {
    form.value.avatarUrl = res.url
    if (auth.user) {
      auth.user = { ...auth.user, avatar: res.url }
    }
    ElMessage.success('头像上传成功')
  } else {
    ElMessage.error(res.msg || '头像上传失败')
  }
}

function onAvatarError() {
  ElMessage.error('头像上传失败，请稍后重试')
}

async function load() {
  const [profileRes, bankRes] = await Promise.all([
    travelApi.getMyProfile(),
    travelApi.getDictByType('biz_bank_name')
  ])
  form.value = profileRes.data || {}
  if (form.value.avatarUrl && auth.user?.avatar !== form.value.avatarUrl) {
    auth.user = { ...auth.user, avatar: form.value.avatarUrl }
  }
  bankOptions.value = bankRes.data || []
  if (form.value.bankName && !bankOptions.value.some((item) => item.dictValue === form.value.bankName)) {
    bankOptions.value.unshift({ dictLabel: form.value.bankName, dictValue: form.value.bankName })
  }
}

async function save() {
  await formRef.value.validate()
  saving.value = true
  try {
    await travelApi.saveMyProfile(form.value)
    await auth.fetchUser()
    if (form.value.avatarUrl && auth.user?.avatar !== form.value.avatarUrl) {
      auth.user = { ...auth.user, avatar: form.value.avatarUrl }
    }
    ElMessage.success('保存成功')
    load()
  } finally {
    saving.value = false
  }
}

load()
</script>

<style scoped>
.profile-grid {
  display: grid;
  grid-template-columns: 340px 1fr;
  gap: 16px;
}
.side-card {
  min-height: 420px;
}
.avatar-wrap {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
}
.muted {
  color: var(--text-sub);
}
.meta-line {
  margin: 10px 0;
  color: #475569;
}
@media (max-width: 900px) {
  .profile-grid {
    grid-template-columns: 1fr;
  }
}
</style>
