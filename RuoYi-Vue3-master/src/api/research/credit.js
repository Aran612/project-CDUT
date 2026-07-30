import request from '@/utils/request'

// 学分总览统计
export function getCreditOverview(semester, department) {
  return request({
    url: '/credit/overview',
    method: 'get',
    params: { semester: semester, department: department }
  })
}

// 按师资类型统计学分
export function getCreditByTeacherType(semester, department) {
  return request({
    url: '/credit/byTeacherType',
    method: 'get',
    params: { semester: semester, department: department }
  })
}

// 按专业汇总学分
export function getCreditByDepartment(semester, teacherType) {
  return request({
    url: '/credit/byDepartment',
    method: 'get',
    params: { semester: semester, teacherType: teacherType }
  })
}

// 研究型占比分析（按专业）
export function getResearchRatioByDepartment(semester) {
  return request({
    url: '/credit/researchRatioByDepartment',
    method: 'get',
    params: { semester: semester }
  })
}

// 研究型占比分析（按师资类型）
export function getResearchRatioByTeacherType(semester) {
  return request({
    url: '/credit/researchRatioByTeacherType',
    method: 'get',
    params: { semester: semester }
  })
}

// 学生学分明细
export function getStudentCreditDetail(query) {
  return request({
    url: '/credit/studentDetail',
    method: 'get',
    params: query
  })
}

// 按教授统计教学学分
export function getCreditByProfessor(semester, teacherType, department) {
  return request({
    url: '/credit/byProfessor',
    method: 'get',
    params: { semester: semester, teacherType: teacherType, department: department }
  })
}

// 获取学期列表
export function getSemesterList() {
  return request({
    url: '/credit/semesterList',
    method: 'get'
  })
}

// 获取院系列表
export function getDepartmentList() {
  return request({
    url: '/credit/departmentList',
    method: 'get'
  })
}

// 获取师资类型字典
export function getTeacherTypeDict() {
  return request({
    url: '/credit/teacherTypeDict',
    method: 'get'
  })
}
