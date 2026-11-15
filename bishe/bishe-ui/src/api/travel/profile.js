import request from '@/utils/request'

export function listProfile(query) {
  return request({ url: '/travel/profile/list', method: 'get', params: query })
}

export function getMyProfile() {
  return request({ url: '/travel/profile/my', method: 'get' })
}

export function saveMyProfile(data) {
  return request({ url: '/travel/profile/my', method: 'post', data })
}

export function getProfile(profileId) {
  return request({ url: '/travel/profile/' + profileId, method: 'get' })
}

export function addProfile(data) {
  return request({ url: '/travel/profile', method: 'post', data })
}

export function updateProfile(data) {
  return request({ url: '/travel/profile', method: 'put', data })
}

export function delProfile(profileId) {
  return request({ url: '/travel/profile/' + profileId, method: 'delete' })
}
