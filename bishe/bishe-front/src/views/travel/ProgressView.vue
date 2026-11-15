<template>
  <el-card class="glass-card">
    <template #header>进度查询管理</template>
    <el-table :data="list" v-loading="loading" empty-text="暂无报销进度数据">
      <el-table-column label="报销单号" prop="reimburseNo" width="180"/>
      <el-table-column label="目的地" prop="destination"/>
      <el-table-column label="报销金额" prop="totalAmount" width="120"/>
      <el-table-column label="审批状态" width="120"><template #default="scope"><el-tag :type="statusTag(scope.row.status)">{{ statusText(scope.row.status) }}</el-tag></template></el-table-column>
      <el-table-column label="支付状态" width="120"><template #default="scope"><el-tag :type="payStatusTag(scope.row.payStatus)">{{ payStatusText(scope.row.payStatus) }}</el-tag></template></el-table-column>
      <el-table-column label="审批意见" prop="approveComment"/>
      <el-table-column label="操作" width="130"><template #default="scope"><el-button link type="primary" @click="showLog(scope.row)">查看日志</el-button></template></el-table-column>
    </el-table>

    <el-dialog v-model="open" title="流程进度日志" width="600px" append-to-body destroy-on-close class="log-dialog">
      <div v-loading="logLoading" style="min-height: 200px">
        <el-timeline v-if="logs.length" class="process-timeline">
          <el-timeline-item
            v-for="(item, index) in logs"
            :key="item.logId"
            :timestamp="item.createTime"
            :type="index === 0 ? 'primary' : ''"
            :hollow="index !== 0"
          >
            <div class="log-item">
              <div class="log-header">
                <span class="step-name">{{ item.stepName }}</span>
                <el-tag size="small" :type="actionTag(item)">{{ actionText(item) }}</el-tag>
              </div>
              <div class="log-info">
                <p v-if="item.approverName"><span class="label">处理人：</span>{{ item.approverName }}</p>
                <p v-if="item.approveComment"><span class="label">备注：</span>{{ item.approveComment }}</p>
              </div>
            </div>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else description="暂无流程日志"/>
      </div>
      <template #footer>
        <el-button @click="open = false">关闭</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref } from 'vue'
import { travelApi } from '@/api/travel'

const loading = ref(false)
const list = ref([])
const open = ref(false)
const logs = ref([])
const logLoading = ref(false)

function statusText(status) {
  return { '0': '草稿', '1': '待审批', '2': '待支付', '3': '已支付', '4': '已驳回' }[status] || status
}
function statusTag(status) {
  return { '0': 'info', '1': 'warning', '2': '', '3': 'success', '4': 'danger' }[status] || ''
}

function payStatusText(status) {
  return { '0': '待支付', '1': '已支付', '2': '支付失败' }[status] || status
}

function payStatusTag(status) {
  return { '0': 'warning', '1': 'success', '2': 'danger' }[status] || ''
}

function actionText(item) {
  if (item?.stepName === '财务支付') {
    return item?.action === '1' ? '支付成功' : '支付失败'
  }
  return { '1': '通过', '2': '驳回' }[item?.action] || '处理'
}

function actionTag(item) {
  if (item?.stepName === '财务支付') {
    return item?.action === '1' ? 'success' : 'danger'
  }
  return { '1': 'success', '2': 'danger' }[item?.action] || 'info'
}

async function load() {
  loading.value = true
  const res = await travelApi.listMyProgress({ pageNum: 1, pageSize: 100 })
  list.value = res.rows || []
  loading.value = false
}

async function showLog(row) {
  logLoading.value = true
  try {
    const res = await travelApi.listProgressLog('reimburse', row.reimburseId)
    logs.value = res.data || []
    open.value = true
  } finally {
    logLoading.value = false
  }
}

load()
</script>

<style scoped>
.process-timeline {
  margin-top: 10px;
  padding: 0 10px;
}
.log-item {
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  padding: 12px;
  margin-top: 4px;
}
.log-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
.step-name {
  font-weight: 600;
  font-size: 15px;
}
.log-info p {
  margin: 4px 0;
  font-size: 13px;
  color: #909399;
}
.label {
  color: #606266;
  font-weight: 500;
}
</style>
