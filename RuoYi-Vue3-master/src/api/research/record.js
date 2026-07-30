import request from '@/utils/request'

// 查询授课记录列表
export function listRecord(query) {
  return request({
    url: '/research/record/list',
    method: 'get',
    params: query
  })
}

// 查询授课记录详细
export function getRecord(recordId) {
  return request({
    url: '/research/record/' + recordId,
    method: 'get'
  })
}

// 新增授课记录
export function addRecord(data) {
  return request({
    url: '/research/record',
    method: 'post',
    data: data
  })
}

// 修改授课记录
export function updateRecord(data) {
  return request({
    url: '/research/record',
    method: 'put',
    data: data
  })
}

// 删除授课记录
export function delRecord(recordId) {
  return request({
    url: '/research/record/' + recordId,
    method: 'delete'
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/research/record/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
