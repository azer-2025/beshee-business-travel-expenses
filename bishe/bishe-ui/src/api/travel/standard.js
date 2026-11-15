import request from '@/utils/request'

export function listStandard(query) {
  return request({ url: '/travel/standard/list', method: 'get', params: query })
}

export function getStandard(standardId) {
  return request({ url: '/travel/standard/' + standardId, method: 'get' })
}

export function addStandard(data) {
  return request({ url: '/travel/standard', method: 'post', data })
}

export function updateStandard(data) {
  return request({ url: '/travel/standard', method: 'put', data })
}

export function delStandard(standardId) {
  return request({ url: '/travel/standard/' + standardId, method: 'delete' })
}

export function matchStandard(params) {
  return request({ url: '/travel/standard/match', method: 'get', params })
}
