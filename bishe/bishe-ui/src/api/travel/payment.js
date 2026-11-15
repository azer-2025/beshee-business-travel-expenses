import request from '@/utils/request'

export function listPayment(query) {
  return request({ url: '/travel/payment/list', method: 'get', params: query })
}

export function getPayment(paymentId) {
  return request({ url: '/travel/payment/' + paymentId, method: 'get' })
}

export function addPayment(data) {
  return request({ url: '/travel/payment', method: 'post', data })
}

export function updatePayment(data) {
  return request({ url: '/travel/payment', method: 'put', data })
}

export function executePayment(data) {
  return request({ url: '/travel/payment/execute', method: 'post', data })
}

export function delPayment(paymentId) {
  return request({ url: '/travel/payment/' + paymentId, method: 'delete' })
}
