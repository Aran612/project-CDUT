import request from '@/utils/request'

// 获取教学统计总览
export function getOverview(semester) {
  return request({
    url: '/research/teachingStats/overview',
    method: 'get',
    params: { semester: semester }
  })
}

// 按教授统计
export function getProfessorStats(semester) {
  return request({
    url: '/research/teachingStats/professor',
    method: 'get',
    params: { semester: semester }
  })
}

// 学期统计
export function getSemesterStats() {
  return request({
    url: '/research/teachingStats/semester',
    method: 'get'
  })
}
