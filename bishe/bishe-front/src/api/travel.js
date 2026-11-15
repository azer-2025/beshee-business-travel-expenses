import request from '@/utils/request'

export const travelApi = {
  getMyProfile: () => request({ url: '/travel/profile/my', method: 'get' }),
  saveMyProfile: (data) => request({ url: '/travel/profile/my', method: 'post', data }),
  getDictByType: (dictType) => request({ url: `/system/dict/data/type/${dictType}`, method: 'get' }),

  listMyApply: (params) => request({ url: '/travel/apply/myList', method: 'get', params }),
  matchMyStandard: (params) => request({ url: '/travel/standard/myMatch', method: 'get', params }),
  addMyApply: (data) => request({ url: '/travel/apply/my', method: 'post', data }),
  updateMyApply: (data) => request({ url: '/travel/apply/my', method: 'put', data }),
  submitMyApply: (id) => request({ url: `/travel/apply/my/submit/${id}`, method: 'post' }),
  delMyApply: (id) => request({ url: `/travel/apply/my/${id}`, method: 'delete' }),

  listMyReimburse: (params) => request({ url: '/travel/reimburse/myList', method: 'get', params }),
  addMyReimburse: (data) => request({ url: '/travel/reimburse/my', method: 'post', data }),
  updateMyReimburse: (data) => request({ url: '/travel/reimburse/my', method: 'put', data }),
  submitMyReimburse: (id) => request({ url: `/travel/reimburse/my/submit/${id}`, method: 'post' }),
  delMyReimburse: (id) => request({ url: `/travel/reimburse/my/${id}`, method: 'delete' }),

  listMyProgress: (params) => request({ url: '/travel/progress/myList', method: 'get', params }),
  listProgressLog: (bizType, bizId) => request({ url: `/travel/progress/log/${bizType}/${bizId}`, method: 'get' }),

  listPublishedNews: (params) => request({ url: '/travel/news/published/list', method: 'get', params })
}
