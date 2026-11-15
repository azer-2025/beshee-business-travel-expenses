<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="handleAdd">新增标准</el-button></el-col>
    </el-row>

    <el-table :data="list" v-loading="loading" style="width: 100%">
      <el-table-column label="地区级别" min-width="120"><template #default="scope"><dict-tag :options="biz_region_level" :value="scope.row.regionLevel"/></template></el-table-column>
      <el-table-column label="员工职级" min-width="120"><template #default="scope"><dict-tag :options="biz_job_level" :value="scope.row.jobLevel"/></template></el-table-column>
      <el-table-column label="交通上限" prop="trafficLimit" width="110"/>
      <el-table-column label="住宿上限" prop="hotelLimit" width="110"/>
      <el-table-column label="餐饮上限" prop="mealLimit" width="110"/>
      <el-table-column label="其他上限" prop="otherLimit" width="110"/>
      <el-table-column label="状态" width="90" align="center"><template #default="scope"><dict-tag :options="sys_normal_disable" :value="scope.row.status"/></template></el-table-column>
      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="地区级别" prop="regionLevel">
          <el-select v-model="form.regionLevel" style="width: 100%" @change="handleLevelChange">
            <el-option v-for="d in biz_region_level" :key="d.value" :label="d.label" :value="d.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="员工职级" prop="jobLevel">
          <el-select v-model="form.jobLevel" style="width: 100%" @change="handleLevelChange">
            <el-option v-for="d in biz_job_level" :key="d.value" :label="d.label" :value="d.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="交通上限"><el-input-number v-model="form.trafficLimit" :min="0" :precision="2" style="width: 100%"/></el-form-item>
        <el-form-item label="住宿上限"><el-input-number v-model="form.hotelLimit" :min="0" :precision="2" style="width: 100%"/></el-form-item>
        <el-form-item label="餐饮上限"><el-input-number v-model="form.mealLimit" :min="0" :precision="2" style="width: 100%"/></el-form-item>
        <el-form-item label="其他上限"><el-input-number v-model="form.otherLimit" :min="0" :precision="2" style="width: 100%"/></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status" style="width: 100%"><el-option v-for="d in sys_normal_disable" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" :loading="matching" @click="submit">确定</el-button>
        <el-button @click="open=false">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelStandard">
import { listStandard, addStandard, updateStandard, delStandard, matchStandard } from '@/api/travel/standard'

const { proxy } = getCurrentInstance()
const { biz_region_level, biz_job_level, sys_normal_disable } = proxy.useDict('biz_region_level', 'biz_job_level', 'sys_normal_disable')

const loading = ref(false)
const list = ref([])
const open = ref(false)
const title = ref('')
const form = ref({})
const formRef = ref()
const matching = ref(false)
const lastAutoMatchKey = ref('')

const rules = {
  regionLevel: [{ required: true, message: '请选择地区级别', trigger: 'change' }],
  jobLevel: [{ required: true, message: '请选择员工职级', trigger: 'change' }]
}

function createDefaultForm() {
  return {
    status: '0',
    regionLevel: undefined,
    jobLevel: undefined,
    trafficLimit: 0,
    hotelLimit: 0,
    mealLimit: 0,
    otherLimit: 0
  }
}

function getList() {
  loading.value = true
  listStandard({ pageNum: 1, pageSize: 100 }).then(res => {
    list.value = res.rows
    loading.value = false
  })
}
function handleAdd() {
  title.value = '新增标准'
  form.value = createDefaultForm()
  lastAutoMatchKey.value = ''
  open.value = true
}
function handleEdit(row) {
  title.value = '编辑标准'
  form.value = { ...row }
  lastAutoMatchKey.value = `${row.regionLevel || ''}_${row.jobLevel || ''}_${row.standardId || ''}`
  open.value = true
}
function handleLevelChange() {
  autoMatchByLevel(true)
}
async function autoMatchByLevel(showMsg = false) {
  const regionLevel = form.value.regionLevel
  const jobLevel = form.value.jobLevel
  if (!regionLevel || !jobLevel) return
  matching.value = true
  try {
    const res = await matchStandard({ regionLevel, jobLevel })
    const matched = res.data
    if (!matched) {
      lastAutoMatchKey.value = `${regionLevel}_${jobLevel}_none`
      form.value.standardId = undefined
      return
    }
    const nextKey = `${regionLevel}_${jobLevel}_${matched.standardId || ''}`
    if (nextKey !== lastAutoMatchKey.value && showMsg) {
      proxy.$modal.msgSuccess('已识别到该组合的差旅标准，已自动带出，可直接修改后保存')
    }
    lastAutoMatchKey.value = nextKey
    form.value = {
      ...form.value,
      ...matched,
      regionLevel,
      jobLevel
    }
  } finally {
    matching.value = false
  }
}
async function submit() {
  await formRef.value.validate()
  const req = form.value.standardId ? updateStandard(form.value) : addStandard(form.value)
  req.then(() => {
    proxy.$modal.msgSuccess('操作成功')
    open.value = false
    getList()
  })
}
function handleDelete(row) {
  proxy.$modal.confirm('确认删除该标准吗？').then(() => delStandard(row.standardId)).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getList()
  })
}

getList()
</script>
