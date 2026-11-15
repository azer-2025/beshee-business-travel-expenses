import request from '@/utils/request'

export function listReimburse(query) {
  return request({ url: '/travel/reimburse/list', method: 'get', params: query })
}

export function listMyReimburse(query) {
  return request({ url: '/travel/reimburse/myList', method: 'get', params: query })
}

export function getReimburse(reimburseId) {
  return request({ url: '/travel/reimburse/' + reimburseId, method: 'get' })
}

export function addReimburse(data) {
  return request({ url: '/travel/reimburse', method: 'post', data })
}

export function addMyReimburse(data) {
  return request({ url: '/travel/reimburse/my', method: 'post', data })
}

export function updateReimburse(data) {
  return request({ url: '/travel/reimburse', method: 'put', data })
}

export function updateMyReimburse(data) {
  return request({ url: '/travel/reimburse/my', method: 'put', data })
}

export function submitMyReimburse(reimburseId) {
  return request({ url: '/travel/reimburse/my/submit/' + reimburseId, method: 'post' })
}

export function submitReimburse(reimburseId) {
  return request({ url: '/travel/reimburse/submit/' + reimburseId, method: 'post' })
}

export function delReimburse(reimburseId) {
  return request({ url: '/travel/reimburse/' + reimburseId, method: 'delete' })
}

export function delMyReimburse(reimburseId) {
  return request({ url: '/travel/reimburse/my/' + reimburseId, method: 'delete' })
}
