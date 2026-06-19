import request from '@/utils/request'

// 查询科研项目列表
export function listProject(query) {
  return request({
    url: '/research/project/list',
    method: 'get',
    params: query
  })
}

// 查询科研项目详细
export function getProject(projectId) {
  return request({
    url: '/research/project/' + projectId,
    method: 'get'
  })
}

// 新增科研项目
export function addProject(data) {
  return request({
    url: '/research/project',
    method: 'post',
    data: data
  })
}

// 修改科研项目
export function updateProject(data) {
  return request({
    url: '/research/project',
    method: 'put',
    data: data
  })
}

// 删除科研项目
export function delProject(projectId) {
  return request({
    url: '/research/project/' + projectId,
    method: 'delete'
  })
}

// 查询所有正常状态的项目等级（下拉用）
export function listAllProjectGrade() {
  return request({
    url: '/research/project/grade/all',
    method: 'get'
  })
}

// 查询项目等级列表
export function listProjectGrade(query) {
  return request({
    url: '/research/project/grade/list',
    method: 'get',
    params: query
  })
}

// 查询项目等级详细
export function getProjectGrade(gradeId) {
  return request({
    url: '/research/project/grade/' + gradeId,
    method: 'get'
  })
}

// 新增项目等级
export function addProjectGrade(data) {
  return request({
    url: '/research/project/grade',
    method: 'post',
    data: data
  })
}

// 修改项目等级
export function updateProjectGrade(data) {
  return request({
    url: '/research/project/grade',
    method: 'put',
    data: data
  })
}

// 删除项目等级
export function delProjectGrade(gradeId) {
  return request({
    url: '/research/project/grade/' + gradeId,
    method: 'delete'
  })
}

// 下载项目导入模板
export function importTemplate() {
  return request({
    url: '/research/project/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
