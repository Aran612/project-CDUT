import request from '@/utils/request'

// 查询论文列表
export function listPaper(query) {
  return request({
    url: '/research/paper/list',
    method: 'get',
    params: query
  })
}

// 查询论文详细
export function getPaper(paperId) {
  return request({
    url: '/research/paper/' + paperId,
    method: 'get'
  })
}

// 新增论文
export function addPaper(data) {
  return request({
    url: '/research/paper',
    method: 'post',
    data: data
  })
}

// 修改论文
export function updatePaper(data) {
  return request({
    url: '/research/paper',
    method: 'put',
    data: data
  })
}

// 删除论文
export function delPaper(paperId) {
  return request({
    url: '/research/paper/' + paperId,
    method: 'delete'
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/research/paper/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
