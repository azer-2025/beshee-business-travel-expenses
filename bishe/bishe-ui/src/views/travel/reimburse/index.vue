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
          <el-option v-for="d in biz_reimburse_status" :key="d.value" :label="d.label" :value="d.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" v-hasPermi="['travel:reimburse:add']" @click="handleAdd">新增</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="list">
      <el-table-column label="单号" prop="reimburseNo" width="180" />
      <el-table-column label="申请人账号" min-width="120">
        <template #default="scope">{{ getUserAccount(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="真实姓名" min-width="120">
        <template #default="scope">{{ getUserRealName(scope.row) }}</template>
      </el-table-column>
      <el-table-column label="所属部门" prop="deptName" min-width="130" />
      <el-table-column label="目的地" prop="destination" min-width="120" />
      <el-table-column label="总额" prop="totalAmount" width="120" />
      <el-table-column label="状态" width="110">
        <template #default="scope">
          <dict-tag :options="biz_reimburse_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="支付" width="110">
        <template #default="scope">
          <dict-tag :options="biz_pay_status" :value="scope.row.payStatus" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button link type="primary" v-hasPermi="['travel:reimburse:edit']" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button
            link
            type="primary"
            v-hasPermi="['travel:reimburse:submit']"
            @click="handleSubmit(scope.row)"
            :disabled="scope.row.status !== '0'"
          >提交</el-button>
          <el-button
            link
            type="danger"
            v-hasPermi="['travel:reimburse:remove']"
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

    <el-dialog :title="title" v-model="open" width="760px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
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
        <el-form-item label="交通费">
          <el-input-number v-model="form.trafficAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="住宿费">
          <el-input-number v-model="form.hotelAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="餐饮费">
          <el-input-number v-model="form.mealAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="其他费用">
          <el-input-number v-model="form.otherAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="费用说明">
          <el-input v-model="form.expenseDesc" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="open = false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelReimburse">
import { listReimburse, addReimburse, updateReimburse, submitReimburse, delReimburse } from '@/api/travel/reimburse'
import { listEmployeeUsers } from '@/api/travel/common'

const { proxy } = getCurrentInstance()
const { biz_reimburse_status, biz_pay_status } = proxy.useDict('biz_reimburse_status', 'biz_pay_status')

const loading = ref(false)
const showSearch = ref(true)
const open = ref(false)
const title = ref('')
const list = ref([])
const total = ref(0)
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
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }]
}

function resolveDeptName(user) {
  return user?.dept?.deptName || user?.deptName || ''
}

function resetForm() {
  form.value = {
    reimburseId: undefined,
    userId: undefined,
    userName: '',
    deptId: undefined,
    deptName: '',
    destination: '',
    trafficAmount: 0,
    hotelAmount: 0,
    mealAmount: 0,
    otherAmount: 0,
    expenseDesc: ''
  }
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
  listReimburse(queryParams.value).then(res => {
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
  title.value = '新增报销单'
  open.value = true
}

async function handleEdit(row) {
  resetForm()
  await loadUserOptions()
  form.value = { ...row }
  ensureUserOption(row)
  title.value = '编辑报销单'
  open.value = true
}

function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) return
    const totalAmount = (Number(form.value.trafficAmount) || 0)
      + (Number(form.value.hotelAmount) || 0)
      + (Number(form.value.mealAmount) || 0)
      + (Number(form.value.otherAmount) || 0)
    if (totalAmount <= 0) {
      proxy.$modal.msgWarning('请至少填写一项费用金额')
      return
    }
    const request = form.value.reimburseId ? updateReimburse(form.value) : addReimburse(form.value)
    request.then(() => {
      proxy.$modal.msgSuccess('保存成功')
      open.value = false
      getList()
    })
  })
}

function handleSubmit(row) {
  submitReimburse(row.reimburseId).then(() => {
    proxy.$modal.msgSuccess('提交成功')
    getList()
  })
}

function handleDelete(row) {
  proxy.$modal.confirm('确认删除该报销单吗？').then(() => delReimburse(row.reimburseId)).then(() => {
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
