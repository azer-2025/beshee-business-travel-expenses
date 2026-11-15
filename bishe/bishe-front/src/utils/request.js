import axios from 'axios'
import { ElMessage } from 'element-plus'
import { getToken, removeToken } from './auth'

const service = axios.create({
  baseURL: '/dev-api',
  timeout: 20000
})

service.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

service.interceptors.response.use(
  (res) => {
    const code = res.data.code
    if (code === 200) {
      return res.data
    }
    ElMessage.error(res.data.msg || '请求失败')
    if (code === 401) {
      removeToken()
      location.href = '/login'
    }
    return Promise.reject(new Error(res.data.msg || 'Error'))
  },
  (err) => {
    ElMessage.error(err.message || '网络异常')
    return Promise.reject(err)
  }
)

export default service
