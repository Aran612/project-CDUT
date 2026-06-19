import request from '@/utils/request'

// 查询教授信息列表
export function listProfessor(query) {
  return request({
    url: '/research/professor/list',
    method: 'get',
    params: query
  })
}

// 查询教授信息详细
export function getProfessor(professorId) {
  return request({
    url: '/research/professor/' + professorId,
    method: 'get'
  })
}

// 新增教授信息
export function addProfessor(data) {
  return request({
    url: '/research/professor',
    method: 'post',
    data: data
  })
}

// 修改教授信息
export function updateProfessor(data) {
  return request({
    url: '/research/professor',
    method: 'put',
    data: data
  })
}

// 删除教授信息
export function delProfessor(professorId) {
  return request({
    url: '/research/professor/' + professorId,
    method: 'delete'
  })
}

// 查询全部正常状态教授（下拉选择用）
export function listAllProfessors() {
  return request({
    url: '/research/professor/all',
    method: 'get'
  })
}

// 一键清除教授所有成果数据（测试用）
export function clearAllProfessorData(professorId) {
  return request({
    url: '/research/professor/clearAllData/' + professorId,
    method: 'delete'
  })
}

// 下载教授导入模板
export function importTemplate() {
  return request({
    url: '/research/professor/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
