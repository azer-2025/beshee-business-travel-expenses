<template>
  <el-card class="glass-card">
    <template #header>
      <div class="head">
        <span>出差申请管理</span>
        <el-button type="primary" @click="openDialog()">新增申请</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading">
      <el-table-column label="申请单号" prop="applyNo" width="180" />
      <el-table-column label="目的地" prop="destination" />
      <el-table-column label="出差时间" width="220">
        <template #default="scope">{{ scope.row.startDate }} ~ {{ scope.row.endDate }}</template>
      </el-table-column>
      <el-table-column label="预算" prop="budgetAmount" width="100" />
      <el-table-column label="状态" width="120">
        <template #default="scope">
          <el-tag :type="statusTag(scope.row.status)">{{ statusText(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="260">
        <template #default="scope">
          <el-button link type="primary" @click="openDialog(scope.row)">编辑</el-button>
          <el-button link type="primary" @click="submit(scope.row)" :disabled="scope.row.status !== '0'">提交</el-button>
          <el-button link type="danger" @click="remove(scope.row)" :disabled="scope.row.status !== '0'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="open" width="680px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="目的地" prop="destination">
          <el-input v-model="form.destination" placeholder="请输入详细目的地（街道、酒店等）" />
        </el-form-item>
        <el-form-item label="出差城市" prop="cityPath">
          <el-cascader
            v-model="form.cityPath"
            :options="areaOptions"
            :props="cascaderProps"
            filterable
            clearable
            style="width: 100%"
            placeholder="请选择省/市/县（区）"
            @change="handleCityChange"
          />
        </el-form-item>
        <el-form-item label="出差时间">
          <el-date-picker v-model="dateRange" type="daterange" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预算金额">
          <el-input-number v-model="form.budgetAmount" :precision="2" :min="0" style="width: 100%" />
          <div v-if="matchedStandard" class="budget-tip">
            已按{{ matchedRegionLabel }} / {{ matchedJobLabel }}匹配标准：
            交通{{ matchedStandard.trafficLimit || 0 }}，
            住宿{{ matchedStandard.hotelLimit || 0 }}，
            餐饮{{ matchedStandard.mealLimit || 0 }}，
            其他{{ matchedStandard.otherLimit || 0 }}
          </div>
          <div v-else-if="matchingStandard" class="budget-tip">正在识别差旅标准...</div>
        </el-form-item>
        <el-form-item label="出差事由" prop="tripReason">
          <el-input v-model="form.tripReason" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
        <el-button @click="open = false">取消</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { computed, ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
import { regionData } from 'element-china-area-data'
import { travelApi } from '@/api/travel'

const loading = ref(false)
const list = ref([])
const open = ref(false)
const dialogTitle = ref('')
const dateRange = ref([])
const form = ref({})
const formRef = ref()
const saving = ref(false)
const matchingStandard = ref(false)
const matchedStandard = ref(null)
const matchedRegionLevel = ref('')
const matchedJobLevel = ref('')
const regionOptions = ref([])
const jobOptions = ref([])

const areaOptions = regionData
const cascaderProps = {
  value: 'value',
  label: 'label',
  emitPath: true,
  checkStrictly: true
}

const cityTierOne = new Set(['北京', '上海', '广州', '深圳'])
const cityTierTwo = new Set([
  '杭州', '南京', '天津', '重庆', '成都', '武汉', '西安', '郑州', '长沙', '苏州', '宁波', '青岛', '厦门',
  '福州', '济南', '大连', '无锡', '东莞', '佛山', '合肥', '昆明', '沈阳', '哈尔滨', '长春', '南昌',
  '南宁', '石家庄', '太原', '兰州', '贵阳', '海口', '珠海'
])

const VIRTUAL_CITY_NAMES = new Set(['市辖区', '县', '自治区直辖县级行政区划'])
const AREA_SUFFIX_RE = /(特别行政区|自治区|自治州|地区|盟|省|市|区|县)$/
const areaPathList = []
buildAreaPathList(areaOptions, [], [])

const rules = {
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  cityPath: [{ required: true, message: '请选择出差城市', trigger: 'change' }],
  tripReason: [{ required: true, message: '请填写出差事由', trigger: 'blur' }]
}

const matchedRegionLabel = computed(() => dictLabel(regionOptions.value, matchedRegionLevel.value))
const matchedJobLabel = computed(() => dictLabel(jobOptions.value, matchedJobLevel.value))

function dictLabel(listData, value) {
  if (!value) return '-'
  const item = (listData || []).find(d => d.dictValue === value)
  return item?.dictLabel || value
}

function statusText(status) {
  return { '0': '草稿', '1': '待审批', '2': '已通过', '3': '已驳回' }[status] || status
}

function statusTag(status) {
  return { '0': 'info', '1': 'warning', '2': 'success', '3': 'danger' }[status] || ''
}

function normalizeAreaName(name) {
  let text = String(name || '').trim()
  while (AREA_SUFFIX_RE.test(text)) {
    text = text.replace(AREA_SUFFIX_RE, '')
  }
  return text || String(name || '').trim()
}

function buildAreaPathList(options, labels, values) {
  for (const item of options || []) {
    const nextLabels = [...labels, item.label]
    const nextValues = [...values, item.value]
    areaPathList.push({ labels: nextLabels, values: nextValues })
    if (item.children?.length) {
      buildAreaPathList(item.children, nextLabels, nextValues)
    }
  }
}

function getAreaLabelsByPath(pathValues) {
  if (!Array.isArray(pathValues) || !pathValues.length) return []
  let current = areaOptions
  const labels = []
  for (const value of pathValues) {
    const node = (current || []).find(item => item.value === value)
    if (!node) break
    labels.push(node.label)
    current = node.children || []
  }
  return labels
}

function findPathByAreaText(areaText) {
  const raw = String(areaText || '').trim()
  if (!raw) return []

  const direct = areaPathList.find(item => item.labels.join('/') === raw)
  if (direct) return direct.values

  const labels = raw.split('/').map(item => item.trim()).filter(Boolean)
  if (labels.length > 0) {
    const normalized = labels.map(normalizeAreaName)
    const byPath = areaPathList.find(item => {
      if (item.labels.length !== normalized.length) return false
      return item.labels.every((label, idx) => normalizeAreaName(label) === normalized[idx])
    })
    if (byPath) return byPath.values
  }

  const target = normalizeAreaName(raw)
  if (!target) return []
  const byCity = areaPathList.find(item => {
    const cityName = resolveCityName(item.labels)
    return normalizeAreaName(cityName) === target
  })
  if (byCity) return byCity.values

  const byAny = areaPathList.find(item => item.labels.some(label => normalizeAreaName(label) === target))
  return byAny ? byAny.values : []
}

function resetMatchState() {
  matchedStandard.value = null
  matchedRegionLevel.value = ''
  matchedJobLevel.value = ''
}

function buildDefaultForm() {
  return { applyId: undefined, destination: '', cityPath: [], budgetAmount: 0, tripReason: '' }
}

function splitDestination(text) {
  const raw = (text || '').trim()
  if (!raw) return { cityPath: [], destination: '' }
  const parts = raw.split(/\s*-\s*/)
  if (parts.length >= 2) {
    const areaPart = parts[0].trim()
    const detail = parts.slice(1).join('-').trim()
    return { cityPath: findPathByAreaText(areaPart), destination: detail }
  }
  const path = findPathByAreaText(raw)
  if (path.length) {
    return { cityPath: path, destination: '' }
  }
  return { cityPath: [], destination: raw }
}

function composeDestination(cityPath, destination) {
  const labels = getAreaLabelsByPath(cityPath)
  if (!labels.length) return destination || ''
  const areaText = labels.join('/')
  return destination ? `${areaText} - ${destination}` : areaText
}

function resolveCityName(labels) {
  if (!labels || !labels.length) return ''
  if (labels.length === 1) return labels[0]
  const second = labels[1]
  if (VIRTUAL_CITY_NAMES.has(second)) return labels[0]
  return second || labels[0]
}

function resolveRegionLevel(cityName) {
  const name = normalizeAreaName(cityName)
  if (cityTierOne.has(name)) return '1'
  if (cityTierTwo.has(name)) return '2'
  return '3'
}

async function load() {
  loading.value = true
  try {
    const [applyRes, regionRes, jobRes] = await Promise.all([
      travelApi.listMyApply({ pageNum: 1, pageSize: 100 }),
      travelApi.getDictByType('biz_region_level'),
      travelApi.getDictByType('biz_job_level')
    ])
    list.value = applyRes.rows || []
    regionOptions.value = regionRes.data || []
    jobOptions.value = jobRes.data || []
  } finally {
    loading.value = false
  }
}

function openDialog(row) {
  resetMatchState()
  if (row) {
    dialogTitle.value = '编辑申请'
    const parsed = splitDestination(row.destination)
    form.value = {
      applyId: row.applyId,
      destination: parsed.destination,
      cityPath: parsed.cityPath,
      budgetAmount: row.budgetAmount,
      tripReason: row.tripReason
    }
    dateRange.value = [row.startDate, row.endDate]
    if (parsed.cityPath?.length) {
      handleCityChange(parsed.cityPath, false)
    }
  } else {
    dialogTitle.value = '新增申请'
    form.value = buildDefaultForm()
    dateRange.value = []
  }
  open.value = true
}

async function handleCityChange(path, autoFillBudget = true) {
  resetMatchState()
  if (!path || !path.length) return
  const labels = getAreaLabelsByPath(path)
  const cityName = resolveCityName(labels)
  const regionLevel = resolveRegionLevel(cityName)
  matchingStandard.value = true
  try {
    const res = await travelApi.matchMyStandard({ regionLevel })
    matchedStandard.value = res.standard || null
    matchedRegionLevel.value = res.regionLevel || regionLevel
    matchedJobLevel.value = res.jobLevel || ''
    if (matchedStandard.value && autoFillBudget) {
      const autoBudget = Number(matchedStandard.value.trafficLimit || 0)
        + Number(matchedStandard.value.hotelLimit || 0)
        + Number(matchedStandard.value.mealLimit || 0)
        + Number(matchedStandard.value.otherLimit || 0)
      form.value.budgetAmount = Number(autoBudget.toFixed(2))
    }
  } finally {
    matchingStandard.value = false
  }
}

async function save() {
  await formRef.value.validate()
  if (!dateRange.value?.[0] || !dateRange.value?.[1]) {
    ElMessage.warning('请选择出差时间范围')
    return
  }
  if ((form.value.budgetAmount || 0) <= 0) {
    ElMessage.warning('预算金额必须大于 0')
    return
  }
  const payload = {
    applyId: form.value.applyId,
    destination: composeDestination(form.value.cityPath, form.value.destination),
    startDate: dateRange.value[0],
    endDate: dateRange.value[1],
    budgetAmount: form.value.budgetAmount,
    tripReason: form.value.tripReason
  }
  saving.value = true
  try {
    if (payload.applyId) await travelApi.updateMyApply(payload)
    else await travelApi.addMyApply(payload)
    ElMessage.success('保存成功')
    open.value = false
    load()
  } finally {
    saving.value = false
  }
}

async function submit(row) {
  await travelApi.submitMyApply(row.applyId)
  ElMessage.success('提交成功')
  load()
}

async function remove(row) {
  await ElMessageBox.confirm('确认删除该申请？', '提示', { type: 'warning' })
  await travelApi.delMyApply(row.applyId)
  ElMessage.success('删除成功')
  load()
}

load()
</script>

<style scoped>
.head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
}

.budget-tip {
  color: #64748b;
  font-size: 12px;
  margin-top: 6px;
  line-height: 1.4;
}
</style>
