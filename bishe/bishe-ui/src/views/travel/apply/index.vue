<template>
  <div class="app-container">
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="申请人" prop="userId">
        <el-select
          v-model="queryParams.userId"
          placeholder="请选择申请人"
          clearable
          filterable
          style="width: 200px"
          :loading="userLoading"
        >
          <el-option
            v-for="u in userOptions"
            :key="u.userId"
            :label="`${u.userName}${u.nickName ? '（' + u.nickName + '）' : ''}`"
            :value="u.userId"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="目的地" prop="destination">
        <el-input v-model="queryParams.destination" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable style="width: 180px">
          <el-option v-for="d in biz_trip_apply_status" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" v-hasPermi="['travel:apply:add']" @click="handleAdd">新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="单号" prop="applyNo" width="180" />
      <el-table-column label="申请人账号" min-width="120">
        <template #default="scope">{{ getUserAccount(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="真实姓名" min-width="120">
        <template #default="scope">{{ getUserRealName(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="所属部门" prop="deptName" min-width="130" />
      <el-table-column label="目的地" prop="destination" min-width="120" />
      <el-table-column label="时间" min-width="210">
        <template #default="scope">{{ scope.row.startDate }} ~ {{ scope.row.endDate }}</template>
      </el-table-column>
      <el-table-column label="预算" prop="budgetAmount" width="100" />
      <el-table-column label="状态" width="110">
        <template #default="scope">
          <dict-tag :options="biz_trip_apply_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button link type="primary" v-hasPermi="['travel:apply:edit']" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            link
            type="primary"
            v-hasPermi="['travel:apply:submit']"
            @click="handleSubmit(scope.row)"
            :disabled="scope.row.status !== '0'"
          >提交</el-button>
          <el-button
            link
            type="danger"
            v-hasPermi="['travel:apply:remove']"
            @click="handleDelete(scope.row)"
            :disabled="scope.row.status !== '0'"
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

    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="申请人" prop="userId">
          <el-select
            v-model="form.userId"
            placeholder="请选择申请人"
            filterable
            clearable
            style="width: 100%"
            :loading="userLoading"
            @change="handleUserChange"
          >
            <el-option
              v-for="u in userOptions"
              :key="u.userId"
              :label="`${u.userName}${u.nickName ? '（' + u.nickName + '）' : ''}`"
              :value="u.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input v-model="form.deptName" readonly />
        </el-form-item>
        <el-form-item label="目的地" prop="destination">
          <el-input v-model="form.destination" />
        </el-form-item>
        <el-form-item label="出差时间" prop="dateRange">
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            range-separator="至"
            start-placeholder="开始"
            end-placeholder="结束"
          />
        </el-form-item>
        <el-form-item label="预算金额" prop="budgetAmount">
          <el-input-number v-model="form.budgetAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="出差事由" prop="tripReason">
          <el-input v-model="form.tripReason" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="open = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelApply">
import { listApply, addApply, updateApply, submitApply, delApply } from '@/api/travel/apply'
import { listEmployeeUsers } from '@/api/travel/common'

const { proxy } = getCurrentInstance()
const { biz_trip_apply_status } = proxy.useDict('biz_trip_apply_status')

const loading = ref(false)
const showSearch = ref(true)
const open = ref(false)
const title = ref('')
const list = ref([])
const total = ref(0)
const dateRange = ref([])
const formRef = ref()
const queryRef = ref()
const userOptions = ref([])
const userLoading = ref(false)

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  userId: undefined,
  destination: undefined,
  status: undefined
})

const form = ref({})

const rules = {
  userId: [{ required: true, message: '请选择申请人', trigger: 'change' }],
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  tripReason: [{ required: true, message: '请输入出差事由', trigger: 'blur' }]
}

function resolveDeptName(user) {
  return user?.dept?.deptName || user?.deptName || ''
}

function resetForm() {
  form.value = {
    applyId: undefined,
    userId: undefined,
    userName: '',
    deptId: undefined,
    deptName: '',
    destination: '',
    startDate: undefined,
    endDate: undefined,
    budgetAmount: 0,
    tripReason: ''
  }
  dateRange.value = []
  formRef.value?.clearValidate()
}

async function loadUserOptions() {
  if (userOptions.value.length || userLoading.value) return
  userLoading.value = true
  try {
    const res = await listEmployeeUsers()
    userOptions.value = (res.data || []).sort((a, b) => String(a.userName || '').localeCompare(String(b.userName || '')))
  } catch (e) {
    userOptions.value = []
  } finally {
    userLoading.value = false
  }
}

function ensureUserOption(row) {
  const exists = userOptions.value.some(item => String(item.userId) === String(row.userId))
  if (!exists) {
    userOptions.value.push({
      userId: row.userId,
      userName: row.userName || String(row.userId),
      nickName: '',
      deptId: row.deptId,
      deptName: row.deptName
    })
  }
}

function setUser(userId) {
  const selected = userOptions.value.find(item => String(item.userId) === String(userId))
  form.value.userId = selected?.userId
  form.value.userName = selected?.userName || ''
  form.value.deptId = selected?.deptId
  form.value.deptName = resolveDeptName(selected)
}

function handleUserChange(userId) {
  setUser(userId)
}

function findUserById(userId) {
  return userOptions.value.find(item => String(item.userId) === String(userId))
}

function getUserAccount(row) {
  return findUserById(row?.userId)?.userName || row?.userName || '-'
}

function getUserRealName(row) {
  return findUserById(row?.userId)?.nickName || '-'
}

function getList() {
  loading.value = true
  listApply(queryParams.value).then(res => {
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

async function handleAdd() {
  resetForm()
  await loadUserOptions()
  title.value = '新增出差申请'
  open.value = true
}

async function handleEdit(row) {
  resetForm()
  await loadUserOptions()
  form.value = { ...row }
  ensureUserOption(row)
  dateRange.value = [row.startDate, row.endDate]
  title.value = '编辑出差申请'
  open.value = true
}

function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    if (!dateRange.value?.[0] || !dateRange.value?.[1]) {
      proxy.$modal.msgWarning('请选择出差时间')
      return
    }
    form.value.startDate = dateRange.value[0]
    form.value.endDate = dateRange.value[1]
    const request = form.value.applyId ? updateApply(form.value) : addApply(form.value)
    request.then(() => {
      proxy.$modal.msgSuccess('保存成功')
      open.value = false
      getList()
    })
  })
}

function handleSubmit(row) {
  submitApply(row.applyId).then(() => {
    proxy.$modal.msgSuccess('提交成功')
    getList()
  })
}

function handleDelete(row) {
  proxy.$modal.confirm('确认删除该出差申请吗？').then(() => delApply(row.applyId)).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  })
}

loadUserOptions()
getList()
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>
