import request from '@/utils/request'

export function listProgress(query) {
  return request({ url: '/travel/progress/list', method: 'get', params: query })
}

export function listMyProgress(query) {
  return request({ url: '/travel/progress/myList', method: 'get', params: query })
}

export function listProgressLog(bizType, bizId) {
  return request({ url: '/travel/progress/log/' + bizType + '/' + bizId, method: 'get' })
}
