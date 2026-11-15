<template>
  <div class="app-container">
    <el-tabs v-model="active">
      <el-tab-pane label="出差申请审批" name="apply">
        <el-form ref="applyQueryRef" :model="applyQueryParams" :inline="true" v-show="showSearchApply">
          <el-form-item label="单号" prop="applyNo">
            <el-input v-model="applyQueryParams.applyNo" placeholder="请输入单号" clearable @keyup.enter="getApplyList"/>
          </el-form-item>
          <el-form-item label="申请人" prop="userName">
            <el-input v-model="applyQueryParams.userName" placeholder="请输入申请人" clearable @keyup.enter="getApplyList"/>
          </el-form-item>
          <el-form-item label="目的地" prop="destination">
            <el-input v-model="applyQueryParams.destination" placeholder="请输入目的地" clearable @keyup.enter="getApplyList"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="applyQueryParams.status" clearable style="width: 180px">
              <el-option v-for="d in biz_trip_apply_status" :key="d.value" :label="d.label" :value="d.value"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleApplyQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetApplyQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              v-hasPermi="['travel:apply:add']"
              @click="handleAddApply"
            >新增申请单</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="Check"
              v-hasPermi="['travel:approve:audit']"
              :disabled="applyMultiple"
              @click="handleBatchAudit('apply', '1')"
            >批量通过</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="Close"
              v-hasPermi="['travel:approve:audit']"
              :disabled="applyMultiple"
              @click="handleBatchAudit('apply', '2')"
            >批量驳回</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearchApply" @queryTable="getApplyList"/>
        </el-row>

        <el-table v-loading="loadingApply" :data="applyList" @selection-change="handleApplySelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="单号" prop="applyNo" width="180"/>
          <el-table-column label="申请人账号" min-width="120">
            <template #default="scope">{{ getUserAccount(scope.row) }}</template>
          </el-table-column>
          <el-table-column label="真实姓名" min-width="120">
            <template #default="scope">{{ getUserRealName(scope.row) }}</template>
          </el-table-column>
          <el-table-column label="目的地" prop="destination"/>
          <el-table-column label="出差时间" min-width="200">
            <template #default="scope">{{ scope.row.startDate }} ~ {{ scope.row.endDate }}</template>
          </el-table-column>
          <el-table-column label="预算" prop="budgetAmount" width="120"/>
          <el-table-column label="状态" width="120" align="center">
            <template #default="scope">
              <dict-tag :options="biz_trip_apply_status" :value="scope.row.status"/>
            </template>
          </el-table-column>
          <el-table-column label="审批意见" prop="approveComment" min-width="160"/>
          <el-table-column label="操作" width="260">
            <template #default="scope">
              <el-button link type="primary" v-hasPermi="['travel:approve:query']" @click="handleViewApply(scope.row)">详情</el-button>
              <el-button link type="success" v-hasPermi="['travel:approve:audit']" :disabled="scope.row.status !== '1'" @click="auditSingle('apply', scope.row.applyId, '1')">通过</el-button>
              <el-button link type="danger" v-hasPermi="['travel:approve:audit']" :disabled="scope.row.status !== '1'" @click="auditSingle('apply', scope.row.applyId, '2')">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="applyTotal > 0"
          :total="applyTotal"
          v-model:page="applyQueryParams.pageNum"
          v-model:limit="applyQueryParams.pageSize"
          @pagination="getApplyList"
        />
      </el-tab-pane>

      <el-tab-pane label="报销单审批" name="reimburse">
        <el-form ref="reimburseQueryRef" :model="reimburseQueryParams" :inline="true" v-show="showSearchReimburse">
          <el-form-item label="单号" prop="reimburseNo">
            <el-input v-model="reimburseQueryParams.reimburseNo" placeholder="请输入单号" clearable @keyup.enter="getReimburseList"/>
          </el-form-item>
          <el-form-item label="申请人" prop="userName">
            <el-input v-model="reimburseQueryParams.userName" placeholder="请输入申请人" clearable @keyup.enter="getReimburseList"/>
          </el-form-item>
          <el-form-item label="目的地" prop="destination">
            <el-input v-model="reimburseQueryParams.destination" placeholder="请输入目的地" clearable @keyup.enter="getReimburseList"/>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="reimburseQueryParams.status" clearable style="width: 180px">
              <el-option v-for="d in biz_reimburse_status" :key="d.value" :label="d.label" :value="d.value"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleReimburseQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetReimburseQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              v-hasPermi="['travel:reimburse:add']"
              @click="handleAddReimburse"
            >新增报销单</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="Check"
              v-hasPermi="['travel:approve:audit']"
              :disabled="reimburseMultiple"
              @click="handleBatchAudit('reimburse', '1')"
            >批量通过</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="Close"
              v-hasPermi="['travel:approve:audit']"
              :disabled="reimburseMultiple"
              @click="handleBatchAudit('reimburse', '2')"
            >批量驳回</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearchReimburse" @queryTable="getReimburseList"/>
        </el-row>

        <el-table v-loading="loadingReimburse" :data="reimburseList" @selection-change="handleReimburseSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="单号" prop="reimburseNo" width="180"/>
          <el-table-column label="申请人账号" min-width="120">
            <template #default="scope">{{ getUserAccount(scope.row) }}</template>
          </el-table-column>
          <el-table-column label="真实姓名" min-width="120">
            <template #default="scope">{{ getUserRealName(scope.row) }}</template>
          </el-table-column>
          <el-table-column label="目的地" prop="destination"/>
          <el-table-column label="金额" prop="totalAmount" width="120"/>
          <el-table-column label="状态" width="120" align="center">
            <template #default="scope">
              <dict-tag :options="biz_reimburse_status" :value="scope.row.status"/>
            </template>
          </el-table-column>
          <el-table-column label="支付状态" width="120" align="center">
            <template #default="scope">
              <dict-tag :options="biz_pay_status" :value="scope.row.payStatus"/>
            </template>
          </el-table-column>
          <el-table-column label="审批意见" prop="approveComment" min-width="160"/>
          <el-table-column label="操作" width="260">
            <template #default="scope">
              <el-button link type="primary" v-hasPermi="['travel:approve:query']" @click="handleViewReimburse(scope.row)">详情</el-button>
              <el-button link type="success" v-hasPermi="['travel:approve:audit']" :disabled="scope.row.status !== '1'" @click="auditSingle('reimburse', scope.row.reimburseId, '1')">通过</el-button>
              <el-button link type="danger" v-hasPermi="['travel:approve:audit']" :disabled="scope.row.status !== '1'" @click="auditSingle('reimburse', scope.row.reimburseId, '2')">驳回</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="reimburseTotal > 0"
          :total="reimburseTotal"
          v-model:page="reimburseQueryParams.pageNum"
          v-model:limit="reimburseQueryParams.pageSize"
          @pagination="getReimburseList"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog :title="detailTitle" v-model="detailOpen" width="820px" append-to-body>
      <el-descriptions v-if="detailBizType === 'apply'" :column="2" border>
        <el-descriptions-item label="单号">{{ detailData.applyNo }}</el-descriptions-item>
        <el-descriptions-item label="申请人账号">{{ getUserAccount(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ getUserRealName(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="所属部门">{{ detailData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="目的地">{{ detailData.destination }}</el-descriptions-item>
        <el-descriptions-item label="出差时间">{{ detailData.startDate }} ~ {{ detailData.endDate }}</el-descriptions-item>
        <el-descriptions-item label="预算">{{ detailData.budgetAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态"><dict-tag :options="biz_trip_apply_status" :value="detailData.status"/></el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detailData.approverName }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ detailData.approveTime }}</el-descriptions-item>
        <el-descriptions-item label="审批意见">{{ detailData.approveComment }}</el-descriptions-item>
        <el-descriptions-item label="出差事由" :span="2">{{ detailData.tripReason }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions v-else :column="2" border>
        <el-descriptions-item label="单号">{{ detailData.reimburseNo }}</el-descriptions-item>
        <el-descriptions-item label="申请人账号">{{ getUserAccount(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="真实姓名">{{ getUserRealName(detailData) }}</el-descriptions-item>
        <el-descriptions-item label="所属部门">{{ detailData.deptName }}</el-descriptions-item>
        <el-descriptions-item label="目的地">{{ detailData.destination }}</el-descriptions-item>
        <el-descriptions-item label="交通费">{{ detailData.trafficAmount }}</el-descriptions-item>
        <el-descriptions-item label="住宿费">{{ detailData.hotelAmount }}</el-descriptions-item>
        <el-descriptions-item label="餐饮费">{{ detailData.mealAmount }}</el-descriptions-item>
        <el-descriptions-item label="其他费用">{{ detailData.otherAmount }}</el-descriptions-item>
        <el-descriptions-item label="总额">{{ detailData.totalAmount }}</el-descriptions-item>
        <el-descriptions-item label="状态"><dict-tag :options="biz_reimburse_status" :value="detailData.status"/></el-descriptions-item>
        <el-descriptions-item label="支付状态"><dict-tag :options="biz_pay_status" :value="detailData.payStatus"/></el-descriptions-item>
        <el-descriptions-item label="审批人">{{ detailData.approverName }}</el-descriptions-item>
        <el-descriptions-item label="审批时间">{{ detailData.approveTime }}</el-descriptions-item>
        <el-descriptions-item label="审批意见">{{ detailData.approveComment }}</el-descriptions-item>
        <el-descriptions-item label="费用说明" :span="2">{{ detailData.expenseDesc }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailOpen = false">关闭</el-button>
      </template>
    </el-dialog>

    <el-dialog title="新增出差申请（待审批）" v-model="addApplyOpen" width="680px" append-to-body>
      <el-form ref="addApplyRef" :model="addApplyForm" :rules="addApplyRules" label-width="100px">
        <el-form-item label="申请人" prop="userId">
          <el-select
            v-model="addApplyForm.userId"
            placeholder="请选择申请人"
            :loading="userLoading"
            filterable
            clearable
            style="width: 100%"
            @change="handleApplyUserChange"
          >
            <el-option
              v-for="user in userOptions"
              :key="user.userId"
              :label="`${user.userName}${user.nickName ? '（' + user.nickName + '）' : ''}`"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input :model-value="addApplyForm.deptName || '-'" readonly />
        </el-form-item>
        <el-form-item label="目的地" prop="destination">
          <el-input v-model="addApplyForm.destination" placeholder="请输入目的地"/>
        </el-form-item>
        <el-form-item label="出差时间" prop="dateRange">
          <el-date-picker
            v-model="addApplyDateRange"
            type="daterange"
            value-format="YYYY-MM-DD"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item label="预算金额" prop="budgetAmount">
          <el-input-number v-model="addApplyForm.budgetAmount" :min="0" :precision="2" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="出差事由" prop="tripReason">
          <el-input v-model="addApplyForm.tripReason" type="textarea" :rows="4" placeholder="请输入出差事由"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" :loading="addApplyLoading" @click="submitAddApply">确 定</el-button>
        <el-button @click="addApplyOpen = false">取 消</el-button>
      </template>
    </el-dialog>

    <el-dialog title="新增报销单（待审批）" v-model="addReimburseOpen" width="760px" append-to-body>
      <el-form ref="addReimburseRef" :model="addReimburseForm" :rules="addReimburseRules" label-width="110px">
        <el-form-item label="填报人" prop="userId">
          <el-select
            v-model="addReimburseForm.userId"
            placeholder="请选择填报人"
            :loading="userLoading"
            filterable
            clearable
            style="width: 100%"
            @change="handleReimburseUserChange"
          >
            <el-option
              v-for="user in userOptions"
              :key="user.userId"
              :label="`${user.userName}${user.nickName ? '（' + user.nickName + '）' : ''}`"
              :value="user.userId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input :model-value="addReimburseForm.deptName || '-'" readonly />
        </el-form-item>
        <el-form-item label="目的地" prop="destination">
          <el-input v-model="addReimburseForm.destination" placeholder="请输入目的地"/>
        </el-form-item>
        <el-form-item label="交通费">
          <el-input-number v-model="addReimburseForm.trafficAmount" :min="0" :precision="2" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="住宿费">
          <el-input-number v-model="addReimburseForm.hotelAmount" :min="0" :precision="2" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="餐饮费">
          <el-input-number v-model="addReimburseForm.mealAmount" :min="0" :precision="2" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="其他费用">
          <el-input-number v-model="addReimburseForm.otherAmount" :min="0" :precision="2" style="width: 100%"/>
        </el-form-item>
        <el-form-item label="费用说明" prop="expenseDesc">
          <el-input v-model="addReimburseForm.expenseDesc" type="textarea" :rows="3" placeholder="请输入费用说明"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" :loading="addReimburseLoading" @click="submitAddReimburse">确 定</el-button>
        <el-button @click="addReimburseOpen = false">取 消</el-button>
      </template>
    </el-dialog>

    <!-- 审批确认对话框 -->
    <el-dialog :title="auditTitle" v-model="auditOpen" width="700px" append-to-body>
      <el-form label-width="80px">
        <el-form-item label="审批意见">
          <editor v-model="auditForm.comment" :min-height="192"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" :loading="auditLoading" @click="submitAudit">确 定</el-button>
          <el-button @click="cancelAudit">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelApprove">
import { listApply, getApply, addApply } from '@/api/travel/apply'
import { listReimburse, getReimburse, addReimburse } from '@/api/travel/reimburse'
import { auditBiz } from '@/api/travel/approve'
import { listEmployeeUsers } from '@/api/travel/common'
import Editor from '@/components/Editor'

const { proxy } = getCurrentInstance()
const { biz_trip_apply_status, biz_reimburse_status, biz_pay_status } = proxy.useDict('biz_trip_apply_status', 'biz_reimburse_status', 'biz_pay_status')

const active = ref('apply')
const loadingApply = ref(false)
const loadingReimburse = ref(false)
const showSearchApply = ref(true)
const showSearchReimburse = ref(true)
const applyList = ref([])
const reimburseList = ref([])
const applyTotal = ref(0)
const reimburseTotal = ref(0)
const applyIds = ref([])
const reimburseIds = ref([])
const applyMultiple = ref(true)
const reimburseMultiple = ref(true)
const applyQueryRef = ref()
const reimburseQueryRef = ref()
const detailOpen = ref(false)
const detailTitle = ref('')
const detailBizType = ref('apply')
const detailData = ref({})

const auditOpen = ref(false)
const auditLoading = ref(false)
const auditTitle = ref('')
const auditForm = ref({
  bizType: '',
  bizId: undefined,
  action: '',
  comment: '',
  isBatch: false,
  ids: []
})

const addApplyOpen = ref(false)
const addApplyLoading = ref(false)
const addApplyRef = ref()
const addApplyDateRange = ref([])
const addApplyForm = ref({
  userId: undefined,
  userName: '',
  deptId: undefined,
  deptName: '',
  destination: '',
  budgetAmount: 0,
  tripReason: ''
})
const addReimburseOpen = ref(false)
const addReimburseLoading = ref(false)
const addReimburseRef = ref()
const addReimburseForm = ref({
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
})
const userOptions = ref([])
const userLoading = ref(false)
const recentTravelCache = ref({})

const addApplyRules = {
  userId: [{ required: true, message: '请选择申请人', trigger: 'change' }],
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  budgetAmount: [{ required: true, message: '请输入预算金额', trigger: 'blur' }],
  tripReason: [{ required: true, message: '请输入出差事由', trigger: 'blur' }]
}

const addReimburseRules = {
  userId: [{ required: true, message: '请选择填报人', trigger: 'change' }],
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  expenseDesc: [{ required: true, message: '请输入费用说明', trigger: 'blur' }]
}

const applyQueryParams = ref({
  pageNum: 1,
  pageSize: 10,
  applyNo: undefined,
  userName: undefined,
  destination: undefined,
  status: undefined
})

const reimburseQueryParams = ref({
  pageNum: 1,
  pageSize: 10,
  reimburseNo: undefined,
  userName: undefined,
  destination: undefined,
  status: undefined
})

function getApplyList() {
  loadingApply.value = true
  listApply(applyQueryParams.value).then(res => {
    applyList.value = res.rows
    applyTotal.value = res.total
  }).finally(() => {
    loadingApply.value = false
  })
}

function getReimburseList() {
  loadingReimburse.value = true
  listReimburse(reimburseQueryParams.value).then(res => {
    reimburseList.value = res.rows
    reimburseTotal.value = res.total
  }).finally(() => {
    loadingReimburse.value = false
  })
}

function handleApplyQuery() {
  applyQueryParams.value.pageNum = 1
  getApplyList()
}

function resetApplyQuery() {
  applyQueryRef.value?.resetFields()
  applyQueryParams.value.pageNum = 1
  getApplyList()
}

function handleReimburseQuery() {
  reimburseQueryParams.value.pageNum = 1
  getReimburseList()
}

function resetReimburseQuery() {
  reimburseQueryRef.value?.resetFields()
  reimburseQueryParams.value.pageNum = 1
  getReimburseList()
}

function handleApplySelectionChange(selection) {
  applyIds.value = selection.filter(item => item.status === '1').map(item => item.applyId)
  applyMultiple.value = !applyIds.value.length
}

function handleReimburseSelectionChange(selection) {
  reimburseIds.value = selection.filter(item => item.status === '1').map(item => item.reimburseId)
  reimburseMultiple.value = !reimburseIds.value.length
}

function getActionText(action) {
  return action === '1' ? '通过' : '驳回'
}

function refreshByType(bizType) {
  if (bizType === 'apply') {
    getApplyList()
    return
  }
  getReimburseList()
}

function auditSingle(bizType, bizId, action) {
  const text = getActionText(action)
  auditTitle.value = text + '确认'
  auditForm.value = {
    bizType,
    bizId,
    action,
    comment: '',
    isBatch: false,
    ids: []
  }
  auditOpen.value = true
}

async function executeBatchAudit(ids, bizType, action, comment) {
  let successCount = 0
  for (const id of ids) {
    try {
      await auditBiz({ bizType, bizId: id, action, comment })
      successCount++
    } catch (e) {
      // keep going on batch processing
    }
  }
  return successCount
}

function handleBatchAudit(bizType, action) {
  const ids = bizType === 'apply' ? applyIds.value : reimburseIds.value
  if (!ids.length) {
    proxy.$modal.msgWarning('请先选择要审批的数据')
    return
  }
  const text = getActionText(action)
  auditTitle.value = `批量${text}确认`
  auditForm.value = {
    bizType,
    action,
    comment: '',
    isBatch: true,
    ids: [...ids]
  }
  auditOpen.value = true
}

async function submitAudit() {
  const { bizType, bizId, action, comment, isBatch, ids } = auditForm.value
  const text = getActionText(action)
  
  auditLoading.value = true
  try {
    if (isBatch) {
      const successCount = await executeBatchAudit(ids, bizType, action, comment || '')
      const failCount = ids.length - successCount
      if (failCount === 0) {
        proxy.$modal.msgSuccess(`批量${text}成功，共 ${successCount} 条`)
      } else {
        proxy.$modal.msgWarning(`批量${text}完成：成功 ${successCount} 条，失败 ${failCount} 条`)
      }
    } else {
      await auditBiz({ bizType, bizId, action, comment: comment || '' })
      proxy.$modal.msgSuccess('审批成功')
    }
    
    auditOpen.value = false
    refreshByType(bizType)
  } finally {
    auditLoading.value = false
  }
}

function cancelAudit() {
  auditOpen.value = false
}

function handleViewApply(row) {
  detailTitle.value = '出差申请详情'
  detailBizType.value = 'apply'
  getApply(row.applyId).then(res => {
    detailData.value = res.data || {}
    detailOpen.value = true
  })
}

function handleViewReimburse(row) {
  detailTitle.value = '报销单详情'
  detailBizType.value = 'reimburse'
  getReimburse(row.reimburseId).then(res => {
    detailData.value = res.data || {}
    detailOpen.value = true
  })
}

function resolveDeptName(user) {
  return user?.dept?.deptName || user?.deptName || ''
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

function setApplyUser(userId) {
  const selected = userOptions.value.find(item => String(item.userId) === String(userId))
  addApplyForm.value.userId = selected?.userId
  addApplyForm.value.userName = selected?.userName || ''
  addApplyForm.value.deptId = selected?.deptId
  addApplyForm.value.deptName = resolveDeptName(selected)
}

function setReimburseUser(userId) {
  const selected = userOptions.value.find(item => String(item.userId) === String(userId))
  addReimburseForm.value.userId = selected?.userId
  addReimburseForm.value.userName = selected?.userName || ''
  addReimburseForm.value.deptId = selected?.deptId
  addReimburseForm.value.deptName = resolveDeptName(selected)
}

async function getRecentByUser(userId) {
  if (!userId) return {}
  if (recentTravelCache.value[userId]) return recentTravelCache.value[userId]
  const [applyRes, reimburseRes] = await Promise.all([
    listApply({ pageNum: 1, pageSize: 1, userId }),
    listReimburse({ pageNum: 1, pageSize: 1, userId })
  ])
  const recent = {
    apply: applyRes?.rows?.[0] || null,
    reimburse: reimburseRes?.rows?.[0] || null
  }
  recentTravelCache.value[userId] = recent
  return recent
}

async function handleApplyUserChange(userId) {
  setApplyUser(userId)
  if (!userId) return
  const currentUserId = String(userId)
  try {
    const recent = await getRecentByUser(userId)
    if (String(addApplyForm.value.userId) !== currentUserId) return
    if (!addApplyForm.value.destination) {
      addApplyForm.value.destination = recent.apply?.destination || recent.reimburse?.destination || ''
    }
    if (!addApplyForm.value.tripReason) {
      addApplyForm.value.tripReason = recent.apply?.tripReason || recent.reimburse?.expenseDesc || ''
    }
  } catch (e) {
    // ignore auto-fill errors
  }
}

async function handleReimburseUserChange(userId) {
  setReimburseUser(userId)
  if (!userId) return
  const currentUserId = String(userId)
  try {
    const recent = await getRecentByUser(userId)
    if (String(addReimburseForm.value.userId) !== currentUserId) return
    if (!addReimburseForm.value.destination) {
      addReimburseForm.value.destination = recent.reimburse?.destination || recent.apply?.destination || ''
    }
    if (!addReimburseForm.value.expenseDesc) {
      addReimburseForm.value.expenseDesc = recent.reimburse?.expenseDesc || recent.apply?.tripReason || ''
    }
  } catch (e) {
    // ignore auto-fill errors
  }
}

async function handleAddApply() {
  await loadUserOptions()
  addApplyForm.value = { userId: undefined, userName: '', deptId: undefined, deptName: '', destination: '', budgetAmount: 0, tripReason: '' }
  addApplyDateRange.value = []
  addApplyOpen.value = true
}

function submitAddApply() {
  addApplyRef.value.validate(async valid => {
    if (!valid) return
    if (!addApplyDateRange.value?.[0] || !addApplyDateRange.value?.[1]) {
      proxy.$modal.msgWarning('请选择出差时间')
      return
    }
    addApplyLoading.value = true
    try {
      const payload = {
        ...addApplyForm.value,
        startDate: addApplyDateRange.value[0],
        endDate: addApplyDateRange.value[1],
        status: '1',
        approvalStep: '待审批'
      }
      await addApply(payload)
      proxy.$modal.msgSuccess('新增申请单成功')
      addApplyOpen.value = false
      getApplyList()
    } finally {
      addApplyLoading.value = false
    }
  })
}

async function handleAddReimburse() {
  await loadUserOptions()
  addReimburseForm.value = {
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
  addReimburseOpen.value = true
}

function submitAddReimburse() {
  addReimburseRef.value.validate(async valid => {
    if (!valid) return
    const total = (Number(addReimburseForm.value.trafficAmount) || 0)
      + (Number(addReimburseForm.value.hotelAmount) || 0)
      + (Number(addReimburseForm.value.mealAmount) || 0)
      + (Number(addReimburseForm.value.otherAmount) || 0)
    if (total <= 0) {
      proxy.$modal.msgWarning('请至少填写一项费用金额')
      return
    }
    addReimburseLoading.value = true
    try {
      const payload = {
        ...addReimburseForm.value,
        status: '1',
        payStatus: '0'
      }
      await addReimburse(payload)
      proxy.$modal.msgSuccess('新增报销单成功')
      addReimburseOpen.value = false
      getReimburseList()
    } finally {
      addReimburseLoading.value = false
    }
  })
}

function load() {
  loadUserOptions()
  getApplyList()
  getReimburseList()
}

watch(active, val => {
  if (val === 'apply') {
    getApplyList()
  } else {
    getReimburseList()
  }
})

load()
</script>

<style scoped>
.mb8 {
  margin-bottom: 8px;
}
</style>
