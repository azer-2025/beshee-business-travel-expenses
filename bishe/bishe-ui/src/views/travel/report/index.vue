<template>
  <div class="app-container">
    <el-row :gutter="20" class="mb8">
      <el-col :span="6"><el-card><div class="metric"><p>申请总数</p><h2>{{ overview.applyCount || 0 }}</h2></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="metric"><p>报销总数</p><h2>{{ overview.reimburseCount || 0 }}</h2></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="metric"><p>待审批数</p><h2>{{ overview.pendingCount || 0 }}</h2></div></el-card></el-col>
      <el-col :span="6"><el-card><div class="metric"><p>已支付金额</p><h2>{{ overview.paidAmount || 0 }}</h2></div></el-card></el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>部门费用占比</template>
          <el-table :data="deptStats" height="360" style="width: 100%">
            <el-table-column label="部门" prop="deptName" min-width="120"/>
            <el-table-column label="报销笔数" prop="reimburseCount" width="100" align="center"/>
            <el-table-column label="总金额" prop="totalAmount" width="120" align="right"/>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>近12个月趋势</template>
          <el-table :data="monthlyStats" height="360" style="width: 100%">
            <el-table-column label="月份" prop="statMonth" min-width="120"/>
            <el-table-column label="报销笔数" prop="reimburseCount" width="100" align="center"/>
            <el-table-column label="总金额" prop="totalAmount" width="120" align="right"/>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="TravelReport">
import { getReportOverview, getDeptStats, getMonthlyStats } from '@/api/travel/report'

const overview = ref({})
const deptStats = ref([])
const monthlyStats = ref([])

function load() {
  getReportOverview().then(res => { overview.value = res.data || {} })
  getDeptStats().then(res => { deptStats.value = res.data || [] })
  getMonthlyStats().then(res => { monthlyStats.value = res.data || [] })
}

load()
</script>

<style scoped>
.metric p {
  margin: 0;
  color: #909399;
}
.metric h2 {
  margin: 10px 0 0;
}
</style>
