import request from '@/utils/request'

export function listApply(query) {
  return request({ url: '/travel/apply/list', method: 'get', params: query })
}

export function listMyApply(query) {
  return request({ url: '/travel/apply/myList', method: 'get', params: query })
}

export function getApply(applyId) {
  return request({ url: '/travel/apply/' + applyId, method: 'get' })
}

export function addApply(data) {
  return request({ url: '/travel/apply', method: 'post', data })
}

export function addMyApply(data) {
  return request({ url: '/travel/apply/my', method: 'post', data })
}

export function updateApply(data) {
  return request({ url: '/travel/apply', method: 'put', data })
}

export function updateMyApply(data) {
  return request({ url: '/travel/apply/my', method: 'put', data })
}

export function submitMyApply(applyId) {
  return request({ url: '/travel/apply/my/submit/' + applyId, method: 'post' })
}

export function submitApply(applyId) {
  return request({ url: '/travel/apply/submit/' + applyId, method: 'post' })
}

export function delApply(applyId) {
  return request({ url: '/travel/apply/' + applyId, method: 'delete' })
}

export function delMyApply(applyId) {
  return request({ url: '/travel/apply/my/' + applyId, method: 'delete' })
}
