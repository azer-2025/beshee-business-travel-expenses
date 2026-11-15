<template>
  <div class="dashboard-container">
    <div class="dashboard-header">
      <div class="welcome-text">
        <h2>企业差旅报销系统 Dashboard</h2>
        <p>欢迎回来，{{ userStore.name }}。这是系统的实时数据概览。</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-apply">
            <el-icon class="card-panel-icon"><Document /></el-icon>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">出差申请总数</div>
            <div class="card-panel-num">{{ overview.applyCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-reimburse">
            <el-icon class="card-panel-icon"><List /></el-icon>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">报销申请总数</div>
            <div class="card-panel-num">{{ overview.reimburseCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-pending">
            <el-icon class="card-panel-icon"><BellFilled /></el-icon>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">待审批报销</div>
            <div class="card-panel-num">{{ overview.pendingCount || 0 }}</div>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel">
          <div class="card-panel-icon-wrapper icon-money">
            <el-icon class="card-panel-icon"><Money /></el-icon>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">总报销金额 (¥)</div>
            <div class="card-panel-num">{{ formatAmount(overview.approvedAmount) }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :lg="16">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>每月报销趋势</span>
            </div>
          </template>
          <div ref="lineChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>费用支出构成</span>
            </div>
          </template>
          <div ref="pieChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :lg="24">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>各部门报销统计</span>
            </div>
          </template>
          <div ref="barChartRef" style="height: 350px;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import * as echarts from 'echarts'
import useUserStore from '@/store/modules/user'
import { getReportOverview, getDeptStats, getMonthlyStats } from '@/api/travel/report'
import { onMounted, onUnmounted, ref, nextTick } from 'vue'

const userStore = useUserStore()
const overview = ref({})
const lineChartRef = ref(null)
const pieChartRef = ref(null)
const barChartRef = ref(null)

let lineChart = null
let pieChart = null
let barChart = null

function formatAmount(num) {
  if (num === undefined || num === null) return '0.00'
  return parseFloat(num).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const handleResize = () => {
  lineChart?.resize()
  pieChart?.resize()
  barChart?.resize()
}

const initCharts = async () => {
  await nextTick()
  
  // 初始化图表
  lineChart = echarts.init(lineChartRef.value)
  pieChart = echarts.init(pieChartRef.value)
  barChart = echarts.init(barChartRef.value)

  // 获取概览数据
  getReportOverview().then(res => {
    overview.value = res.data
  }).catch(() => {
    // 权限不足或其他错误时使用模拟数据
    overview.value = {
      applyCount: 128,
      reimburseCount: 95,
      pendingCount: 12,
      approvedAmount: 458600.50,
      paidAmount: 325400.00
    }
  })

  // 获取趋势数据
  getMonthlyStats().then(res => {
    updateLineChart(res.data)
  }).catch(() => {
    const mockData = [
      { statMonth: '2025-09', totalAmount: 32000 },
      { statMonth: '2025-10', totalAmount: 45000 },
      { statMonth: '2025-11', totalAmount: 38000 },
      { statMonth: '2025-12', totalAmount: 52000 },
      { statMonth: '2026-01', totalAmount: 48000 },
      { statMonth: '2026-02', totalAmount: 25000 }
    ]
    updateLineChart(mockData)
  })

  // 获取部门数据
  getDeptStats().then(res => {
    updatePieChart(res.data)
    updateBarChart(res.data)
  }).catch(() => {
    const mockData = [
      { deptName: '研发部', totalAmount: 150000 },
      { deptName: '销售部', totalAmount: 120000 },
      { deptName: '财务部', totalAmount: 30000 },
      { deptName: '行政部', totalAmount: 45000 },
      { deptName: '人力资源', totalAmount: 25000 }
    ]
    updatePieChart(mockData)
    updateBarChart(mockData)
  })

  // 监听窗口缩放
  window.addEventListener('resize', handleResize)
}

function updateLineChart(data) {
  const option = {
    tooltip: { trigger: 'axis' },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: data.map(item => item.statMonth)
    },
    yAxis: { type: 'value', name: '金额 (¥)' },
    series: [
      {
        name: '报销金额',
        type: 'line',
        smooth: true,
        data: data.map(item => item.totalAmount),
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0)' }
          ])
        },
        itemStyle: { color: '#409EFF' }
      }
    ]
  }
  lineChart.setOption(option)
}

function updatePieChart(data) {
  const option = {
    tooltip: { trigger: 'item', formatter: '{a} <br/>{b} : {c} ({d}%)' },
    legend: { bottom: '0', left: 'center' },
    series: [
      {
        name: '部门支出',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
        label: { show: false, position: 'center' },
        emphasis: { label: { show: true, fontSize: '20', fontWeight: 'bold' } },
        labelLine: { show: false },
        data: data.map(item => ({ name: item.deptName, value: item.totalAmount }))
      }
    ]
  }
  pieChart.setOption(option)
}

function updateBarChart(data) {
  const option = {
    tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category',
      data: data.map(item => item.deptName),
      axisTick: { alignWithLabel: true }
    },
    yAxis: { type: 'value', name: '金额 (¥)' },
    series: [
      {
        name: '报销总额',
        type: 'bar',
        barWidth: '40%',
        data: data.map(item => item.totalAmount),
        itemStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: '#83bff6' },
            { offset: 0.5, color: '#188df0' },
            { offset: 1, color: '#188df0' }
          ])
        }
      }
    ]
  }
  barChart.setOption(option)
}

onMounted(() => {
  initCharts()
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  lineChart?.dispose()
  pieChart?.dispose()
  barChart?.dispose()
})
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .dashboard-header {
    margin-bottom: 32px;
    .welcome-text {
      h2 {
        font-size: 24px;
        margin-bottom: 8px;
        color: #303133;
      }
      p {
        font-size: 14px;
        color: #909399;
      }
    }
  }

  .panel-group {
    .card-panel-col {
      margin-bottom: 32px;
    }

    .card-panel {
      height: 108px;
      cursor: pointer;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 20px;
      border-radius: 8px;
      transition: all 0.3s;

      &:hover {
        transform: translateY(-5px);
        box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
        
        .card-panel-icon-wrapper {
          color: #fff;
        }
        .icon-apply { background: #40c9c6; }
        .icon-reimburse { background: #36a3f7; }
        .icon-pending { background: #f4516c; }
        .icon-money { background: #f6ab40; }
      }

      .card-panel-icon-wrapper {
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }

      .card-panel-icon {
        font-size: 48px;
      }

      .icon-apply { color: #40c9c6; }
      .icon-reimburse { color: #36a3f7; }
      .icon-pending { color: #f4516c; }
      .icon-money { color: #f6ab40; }

      .card-panel-description {
        font-weight: bold;
        text-align: right;

        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }

        .card-panel-num {
          font-size: 20px;
          color: #666;
        }
      }
    }
  }

  .chart-card {
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,.1);
    
    .card-header {
      font-weight: bold;
      font-size: 16px;
    }
  }
}

@media (max-width:550px) {
  .card-panel-description {
    display: none;
  }
  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
