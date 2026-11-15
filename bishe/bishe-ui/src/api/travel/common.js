import request from '@/utils/request'

export function listEmployeeUsers(query) {
  return request({
    url: '/travel/common/employeeUsers',
    method: 'get',
    params: query
  })
}
