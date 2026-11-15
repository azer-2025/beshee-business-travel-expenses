import request from '@/utils/request'

export function auditBiz(data) {
  return request({ url: '/travel/approve/audit', method: 'post', data })
}
