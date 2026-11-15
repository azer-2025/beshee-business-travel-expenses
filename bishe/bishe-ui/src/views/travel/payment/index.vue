<template>
  <div class="app-container">
    <el-form ref="queryRef" :model="queryParams" :inline="true" v-show="showSearch">
      <el-form-item label="流水号" prop="paymentNo">
        <el-input v-model="queryParams.paymentNo" placeholder="请输入流水号" clearable @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="报销ID" prop="reimburseId">
        <el-input-number v-model="queryParams.reimburseId" :min="1" controls-position="right" style="width: 180px"/>
      </el-form-item>
      <el-form-item label="收款人" prop="userName">
        <el-input v-model="queryParams.userName" placeholder="请输入收款人" clearable @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="支付状态" prop="payStatus">
        <el-select v-model="queryParams.payStatus" clearable style="width: 160px">
          <el-option v-for="d in biz_pay_status" :key="d.value" :label="d.label" :value="d.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-card class="mb8">
      <template #header>待支付报销单</template>
      <el-table :data="pendingList" v-loading="loadingPending" style="width: 100%">
        <el-table-column label="报销单号" prop="reimburseNo" min-width="180"/>
        <el-table-column label="申请人" prop="userName" width="120"/>
        <el-table-column label="金额" prop="totalAmount" width="120"/>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" link v-hasPermi="['travel:payment:pay']" @click="openPay(scope.row)">执行支付</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <span>支付流水</span>
          <div>
            <el-button type="primary" plain icon="Plus" v-hasPermi="['travel:payment:add']" @click="handleAdd">新增流水</el-button>
            <el-button type="danger" plain icon="Delete" v-hasPermi="['travel:payment:remove']" :disabled="multiple" @click="handleDelete()">批量删除</el-button>
            <right-toolbar v-model:showSearch="showSearch" @queryTable="getPayList"/>
          </div>
        </div>
      </template>
      <el-table :data="payList" v-loading="loadingPay" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column label="流水号" prop="paymentNo" min-width="200"/>
        <el-table-column label="报销ID" prop="reimburseId" width="100"/>
        <el-table-column label="收款人" prop="userName" width="120"/>
        <el-table-column label="金额" prop="payAmount" width="120"/>
        <el-table-column label="支付方式" width="120" align="center">
          <template #default="scope">
            <dict-tag :options="biz_pay_channel" :value="scope.row.payChannel"/>
          </template>
        </el-table-column>
        <el-table-column label="支付账号" prop="payAccount" min-width="180"/>
        <el-table-column label="状态" width="120" align="center"><template #default="scope"><dict-tag :options="biz_pay_status" :value="scope.row.payStatus"/></template></el-table-column>
        <el-table-column label="支付时间" prop="payTime" width="180"/>
        <el-table-column label="操作人" prop="operatorName" width="120"/>
        <el-table-column label="操作" width="220" align="center">
          <template #default="scope">
            <el-button link type="primary" v-hasPermi="['travel:payment:query']" @click="handleView(scope.row)">查看</el-button>
            <el-button link type="primary" v-hasPermi="['travel:payment:edit']" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button link type="danger" v-hasPermi="['travel:payment:remove']" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getPayList"
      />
    </el-card>

    <el-dialog :title="title" v-model="open" width="560px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="报销ID" prop="reimburseId"><el-input-number v-model="form.reimburseId" :min="1" :disabled="mode==='view' || mode==='execute'" style="width: 100%"/></el-form-item>
        <el-form-item label="报销人"><el-input v-model="form.userName" disabled placeholder="自动带出"/></el-form-item>
        <el-form-item label="支付金额" prop="payAmount"><el-input-number v-model="form.payAmount" :min="0" :precision="2" :disabled="mode==='view'" style="width: 100%"/></el-form-item>
        <el-form-item label="支付方式" prop="payChannel">
          <el-select v-model="form.payChannel" :disabled="mode==='view'" placeholder="请选择支付方式" style="width: 100%">
            <el-option v-for="d in biz_pay_channel" :key="d.value" :label="d.label" :value="d.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="支付账号"><el-input v-model="form.payAccount" :disabled="mode==='view'"/></el-form-item>
        <el-form-item label="支付状态" prop="payStatus"><el-select v-model="form.payStatus" :disabled="mode==='view'" style="width: 100%"><el-option v-for="d in biz_pay_status" :key="d.value" :label="d.label" :value="d.value"/></el-select></el-form-item>
        <el-form-item label="支付凭证"><image-upload v-model="form.voucherImage" :disabled="mode==='view'"/></el-form-item>
      </el-form>
      <template #footer>
        <el-button v-if="mode==='view'" @click="open=false">关闭</el-button>
        <template v-else>
          <el-button type="primary" @click="submitForm">确定</el-button>
          <el-button @click="open=false">取消</el-button>
        </template>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TravelPayment">
import { listReimburse } from '@/api/travel/reimburse'
import { listPayment, getPayment, addPayment, updatePayment, executePayment, delPayment } from '@/api/travel/payment'

const { proxy } = getCurrentInstance()
const { biz_pay_status, biz_pay_channel } = proxy.useDict('biz_pay_status', 'biz_pay_channel')

const showSearch = ref(true)
const pendingList = ref([])
const payList = ref([])
const loadingPending = ref(false)
const loadingPay = ref(false)
const total = ref(0)
const ids = ref([])
const multiple = ref(true)
const open = ref(false)
const title = ref('')
const mode = ref('view')
const formRef = ref()
const form = ref({})
const queryRef = ref()
const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  paymentNo: undefined,
  reimburseId: undefined,
  userName: undefined,
  payStatus: undefined
})
const rules = {
  reimburseId: [{ required: true, message: '请输入报销ID', trigger: 'blur' }],
  payAmount: [{ required: true, message: '请输入支付金额', trigger: 'blur' }],
  payChannel: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
  payStatus: [{ required: true, message: '请选择支付状态', trigger: 'change' }]
}

function loadPending() {
  loadingPending.value = true
  listReimburse({ pageNum: 1, pageSize: 100, status: '2' })
    .then(res => {
      pendingList.value = res.rows
    })
    .finally(() => {
      loadingPending.value = false
    })
}

function getPayList() {
  loadingPay.value = true
  listPayment(queryParams.value)
    .then(res => {
      payList.value = res.rows
      total.value = res.total
    })
    .finally(() => {
      loadingPay.value = false
    })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getPayList()
}

function resetQuery() {
  if (queryRef.value) {
    queryRef.value.resetFields()
  }
  queryParams.value.pageNum = 1
  getPayList()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.paymentId)
  multiple.value = !selection.length
}

function openPay(row) {
  title.value = '执行支付'
  mode.value = 'execute'
  form.value = { reimburseId: row.reimburseId, userName: row.userName, payAmount: row.totalAmount, payStatus: '1', payChannel: '对公转账', payAccount: '', voucherImage: '' }
  open.value = true
}

function handleAdd() {
  title.value = '新增支付流水'
  mode.value = 'add'
  form.value = { userName: '', payStatus: '1', payAmount: 0, payChannel: '对公转账', payAccount: '', voucherImage: '' }
  open.value = true
}

function handleView(row) {
  title.value = '支付详情'
  mode.value = 'view'
  getPayment(row.paymentId).then(res => {
    form.value = res.data || {}
    open.value = true
  })
}

function handleEdit(row) {
  title.value = '编辑支付流水'
  mode.value = 'edit'
  getPayment(row.paymentId).then(res => {
    form.value = res.data || {}
    open.value = true
  })
}

function submitForm() {
  formRef.value.validate(valid => {
    if (!valid) {
      return
    }
    const request = mode.value === 'execute'
      ? executePayment(form.value)
      : (form.value.paymentId ? updatePayment(form.value) : addPayment(form.value))
    request.then(() => {
      proxy.$modal.msgSuccess('操作成功')
      open.value = false
      loadPending()
      getPayList()
    })
  })
}

function handleDelete(row) {
  const paymentIds = row ? [row.paymentId] : ids.value
  if (!paymentIds.length) {
    proxy.$modal.msgWarning('请先选择要删除的数据')
    return
  }
  proxy.$modal.confirm('确认删除选中的支付流水吗？').then(() => delPayment(paymentIds)).then(() => {
    proxy.$modal.msgSuccess('删除成功')
    getPayList()
  })
}

loadPending()
getPayList()
</script>

<style scoped>
.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
</style>
