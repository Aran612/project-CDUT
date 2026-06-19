import request from '@/utils/request'

// 查询论文等级列表
export function listGrade(query) {
  return request({
    url: '/research/grade/list',
    method: 'get',
    params: query
  })
}

// 查询所有正常状态的论文等级
export function listAllGrade() {
  return request({
    url: '/research/grade/all',
    method: 'get'
  })
}

// 查询论文等级详细
export function getGrade(gradeId) {
  return request({
    url: '/research/grade/' + gradeId,
    method: 'get'
  })
}

// 新增论文等级
export function addGrade(data) {
  return request({
    url: '/research/grade',
    method: 'post',
    data: data
  })
}

// 修改论文等级
export function updateGrade(data) {
  return request({
    url: '/research/grade',
    method: 'put',
    data: data
  })
}

// 删除论文等级
export function delGrade(gradeId) {
  return request({
    url: '/research/grade/' + gradeId,
    method: 'delete'
  })
}
