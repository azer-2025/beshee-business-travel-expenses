import request from '@/utils/request'

export function listNews(query) {
  return request({ url: '/travel/news/list', method: 'get', params: query })
}

export function listPublishedNews(query) {
  return request({ url: '/travel/news/published/list', method: 'get', params: query })
}

export function getNews(newsId) {
  return request({ url: '/travel/news/' + newsId, method: 'get' })
}

export function addNews(data) {
  return request({ url: '/travel/news', method: 'post', data })
}

export function updateNews(data) {
  return request({ url: '/travel/news', method: 'put', data })
}

export function delNews(newsId) {
  return request({ url: '/travel/news/' + newsId, method: 'delete' })
}
