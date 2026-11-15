<template>
  <div class="app-container">
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="用户ID" prop="userId">
        <el-input v-model="queryParams.userId" placeholder="请输入用户ID" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="姓名" prop="realName">
        <el-input v-model="queryParams.realName" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="部门" prop="deptName">
        <el-input v-model="queryParams.deptName" placeholder="请输入部门" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="电话" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入电话" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="职级" prop="jobLevel">
        <el-select v-model="queryParams.jobLevel" clearable style="width: 160px">
          <el-option v-for="d in biz_job_level" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable style="width: 160px">
          <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" v-hasPermi="['travel:profile:add']" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" v-hasPermi="['travel:profile:remove']" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column label="档案ID" prop="profileId" width="90" />
      <el-table-column label="用户ID" prop="userId" width="90" />
      <el-table-column label="姓名" prop="realName" width="120" />
      <el-table-column label="所属部门" prop="deptName" min-width="140" />
      <el-table-column label="员工职级" min-width="120">
        <template #default="scope">
          <dict-tag :options="biz_job_level" :value="scope.row.jobLevel" />
        </template>
      </el-table-column>
      <el-table-column label="收款银行" prop="bankName" min-width="130" />
      <el-table-column label="银行账号" prop="bankAccount" min-width="150" show-overflow-tooltip />
      <el-table-column label="联系电话" prop="phone" width="140" />
      <el-table-column label="邮箱" prop="email" min-width="180" show-overflow-tooltip />
      <el-table-column label="头像" width="90" align="center">
        <template #default="scope">
          <image-preview :src="scope.row.avatarUrl" :width="38" :height="38" />
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90" align="center">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="更新时间" prop="updateTime" width="170" />
      <el-table-column label="操作" width="170" align="center">
        <template #default="scope">
          <el-button link type="primary" v-hasPermi="['travel:profile:edit','travel:profile:add']" @click="handleEdit(scope.row)">
            {{ scope.row.profileId ? '修改' : '新建档案' }}
          </el-button>
          <el-button
            v-if="scope.row.profileId"
            link
            type="danger"
            v-hasPermi="['travel:profile:remove']"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog :title="title" v-model="open" width="760px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="95px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户" prop="userId">
              <el-select
                v-model="form.userId"
                placeholder="请选择员工用户"
                filterable
                clearable
                :disabled="!!form.profileId"
                :loading="userLoading"
                style="width: 100%"
                @change="handleUserChange"
              >
                <el-option
                  v-for="u in employeeOptions"
                  :key="u.userId"
                  :label="`${u.userName}${u.nickName ? '（' + u.nickName + '）' : ''}`"
                  :value="u.userId"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="realName">
              <el-input v-model="form.realName" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-input v-model="form.deptName" disabled />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工职级" prop="jobLevel">
              <el-select v-model="form.jobLevel" style="width: 100%">
                <el-option v-for="d in biz_job_level" :key="d.value" :label="d.label" :value="d.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款银行">
              <el-select v-model="form.bankName" clearable filterable placeholder="请选择收款银行" style="width: 100%">
                <el-option v-for="d in biz_bank_name" :key="d.value" :label="d.label" :value="d.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号">
              <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-select v-model="form.status" style="width: 100%">
                <el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="d.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="头像">
              <image-upload v-model="form.avatarUrl" :limit="1" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系地址">
              <el-input v-model="form.contactAddress" placeholder="请输入联系地址" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="3" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelProfile">
import { listProfile, getProfile, addProfile, updateProfile, delProfile } from '@/api/travel/profile'
import { listEmployeeUsers } from '@/api/travel/common'

const { proxy } = getCurrentInstance()
const { sys_normal_disable, biz_bank_name, biz_job_level } = proxy.useDict('sys_normal_disable', 'biz_bank_name', 'biz_job_level')

const loading = ref(false)
const showSearch = ref(true)
const total = ref(0)
const list = ref([])
const ids = ref([])
const multiple = ref(true)
const open = ref(false)
const title = ref('')
const employeeOptions = ref([])
const userLoading = ref(false)
const queryRef = ref()
const formRef = ref()

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userId: undefined,
  realName: undefined,
  deptName: undefined,
  phone: undefined,
  jobLevel: undefined,
  status: undefined
})

const form = ref({})

const rules = {
  userId: [{ required: true, message: '请选择员工用户', trigger: 'change' }],
  realName: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  jobLevel: [{ required: true, message: '请选择员工职级', trigger: 'change' }],
  phone: [
    {
      pattern: /^$|^1[3-9]\d{9}$/,
      message: '请输入正确手机号',
      trigger: 'blur'
    }
  ],
  email: [{ type: 'email', message: '请输入正确邮箱', trigger: 'blur' }]
}

function resetForm() {
  form.value = {
    profileId: undefined,
    userId: undefined,
    realName: '',
    deptId: undefined,
    deptName: '',
    jobLevel: '1',
    bankName: '',
    bankAccount: '',
    phone: '',
    email: '',
    contactAddress: '',
    avatarUrl: '',
    status: '0',
    remark: ''
  }
  formRef.value?.clearValidate()
}

function getList() {
  loading.value = true
  listProfile(queryParams.value).then(res => {
    list.value = res.rows || []
    total.value = res.total || 0
  }).finally(() => {
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  queryRef.value?.resetFields()
  queryParams.value.pageNum = 1
  getList()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.profileId).filter(Boolean)
  multiple.value = !ids.value.length
}

async function loadEmployeeOptions() {
  if (employeeOptions.value.length || userLoading.value) return
  userLoading.value = true
  try {
    const res = await listEmployeeUsers()
    employeeOptions.value = res.data || []
  } finally {
    userLoading.value = false
  }
}

function ensureUserOption(row) {
  const exists = employeeOptions.value.some(item => String(item.userId) === String(row.userId))
  if (!exists) {
    employeeOptions.value.push({
      userId: row.userId,
      userName: row.realName || String(row.userId),
      nickName: '',
      deptId: row.deptId,
      deptName: row.deptName
    })
  }
}

function handleUserChange(userId) {
  const user = employeeOptions.value.find(item => String(item.userId) === String(userId))
  if (!user) return
  form.value.userId = user.userId
  if (!form.value.realName) {
    form.value.realName = user.nickName || user.userName || ''
  }
  form.value.deptId = user.deptId
  form.value.deptName = user.dept?.deptName || user.deptName || ''
}

async function handleAdd() {
  resetForm()
  await loadEmployeeOptions()
  title.value = '新增员工档案'
  open.value = true
}

async function handleEdit(row) {
  resetForm()
  await loadEmployeeOptions()
  if (row.profileId) {
    const res = await getProfile(row.profileId)
    form.value = res.data || {}
    form.value.status = form.value.status || '0'
    form.value.jobLevel = form.value.jobLevel || '1'
    ensureUserOption(form.value)
    title.value = '修改员工档案'
  } else {
    form.value = {
      profileId: undefined,
      userId: row.userId,
      realName: row.realName || '',
      deptId: row.deptId,
      deptName: row.deptName || '',
      jobLevel: '1',
      bankName: '',
      bankAccount: '',
      phone: row.phone || '',
      email: row.email || '',
      contactAddress: '',
      avatarUrl: row.avatarUrl || '',
      status: row.status || '0',
      remark: ''
    }
    ensureUserOption(form.value)
    title.value = '新增员工档案'
  }
  open.value = true
}

function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    const req = form.value.profileId ? updateProfile(form.value) : addProfile(form.value)
    req.then(() => {
      proxy.$modal.msgSuccess('操作成功')
      open.value = false
      getList()
    })
  })
}

function handleDelete(row) {
  if (row && !row.profileId) {
    proxy.$modal.msgWarning('该员工还未建档，无法删除')
    return
  }
  const profileIds = row?.profileId || ids.value
  if (!profileIds || (Array.isArray(profileIds) && profileIds.length === 0)) return
  proxy.$modal.confirm('确认删除选中的员工档案吗？').then(() => delProfile(profileIds)).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  })
}

getList()
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>
