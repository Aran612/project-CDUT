import request from '@/utils/request'

// 查询课程列表
export function listCourse(query) {
  return request({
    url: '/research/course/list',
    method: 'get',
    params: query
  })
}

// 查询所有课程（下拉选择用）
export function listAllCourse() {
  return request({
    url: '/research/course/all',
    method: 'get'
  })
}

// 查询课程详细
export function getCourse(courseId) {
  return request({
    url: '/research/course/' + courseId,
    method: 'get'
  })
}

// 新增课程
export function addCourse(data) {
  return request({
    url: '/research/course',
    method: 'post',
    data: data
  })
}

// 修改课程
export function updateCourse(data) {
  return request({
    url: '/research/course',
    method: 'put',
    data: data
  })
}

// 删除课程
export function delCourse(courseId) {
  return request({
    url: '/research/course/' + courseId,
    method: 'delete'
  })
}

// 下载导入模板
export function importTemplate() {
  return request({
    url: '/research/course/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
