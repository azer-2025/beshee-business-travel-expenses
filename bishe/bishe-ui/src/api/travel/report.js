import request from '@/utils/request'

export function getReportOverview() {
  return request({ url: '/travel/report/overview', method: 'get' })
}

export function getDeptStats() {
  return request({ url: '/travel/report/deptStats', method: 'get' })
}

export function getMonthlyStats() {
  return request({ url: '/travel/report/monthlyStats', method: 'get' })
}
