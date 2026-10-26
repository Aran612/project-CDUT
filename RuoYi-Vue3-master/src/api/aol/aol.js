import request from '@/utils/request'

// ===================== AoL 学习保障（Assurance of Learning）API =====================

// ---------- 学位项目 Program ----------
// 查询学位项目列表
export function listProgram(query) {
  return request({
    url: '/aol/program/list',
    method: 'get',
    params: query
  })
}

// 新增学位项目
export function addProgram(data) {
  return request({
    url: '/aol/program',
    method: 'post',
    data: data
  })
}

// 修改学位项目
export function updateProgram(data) {
  return request({
    url: '/aol/program',
    method: 'put',
    data: data
  })
}

// 删除学位项目
export function delProgram(programId) {
  return request({
    url: '/aol/program/' + programId,
    method: 'delete'
  })
}

// ---------- 能力目标 CG（Competence Goal） ----------
// 按项目查询 CG 列表
export function listCg(programId) {
  return request({
    url: '/aol/cg/list',
    method: 'get',
    params: { programId: programId }
  })
}

// 新增 CG
export function addCg(data) {
  return request({
    url: '/aol/cg',
    method: 'post',
    data: data
  })
}

// 修改 CG
export function updateCg(data) {
  return request({
    url: '/aol/cg',
    method: 'put',
    data: data
  })
}

// 删除 CG
export function delCg(cgId) {
  return request({
    url: '/aol/cg/' + cgId,
    method: 'delete'
  })
}

// ---------- 学习目标 LG（Learning Objective，含布鲁姆层级） ----------
// 按 CG 查询 LG 列表
export function listLg(cgId) {
  return request({
    url: '/aol/lg/list',
    method: 'get',
    params: { cgId: cgId }
  })
}

// 新增 LG
export function addLg(data) {
  return request({
    url: '/aol/lg',
    method: 'post',
    data: data
  })
}

// 修改 LG
export function updateLg(data) {
  return request({
    url: '/aol/lg',
    method: 'put',
    data: data
  })
}

// 删除 LG
export function delLg(lgId) {
  return request({
    url: '/aol/lg/' + lgId,
    method: 'delete'
  })
}

// ---------- 课程图谱（图谱课程 + 覆盖矩阵 I/D/M） ----------
// 查询项目下的图谱课程行
export function listMap(programId) {
  return request({
    url: '/aol/map/list',
    method: 'get',
    params: { programId: programId }
  })
}

// 新增图谱课程行
export function addMap(data) {
  return request({
    url: '/aol/map',
    method: 'post',
    data: data
  })
}

// 修改图谱课程行
export function updateMap(data) {
  return request({
    url: '/aol/map',
    method: 'put',
    data: data
  })
}

// 删除图谱课程行
export function delMap(mapCourseId) {
  return request({
    url: '/aol/map/' + mapCourseId,
    method: 'delete'
  })
}

// 批量保存覆盖格：body { mapCourseId, cells: [{ lgId, coverageLevel }] }
export function saveCells(data) {
  return request({
    url: '/aol/map/cells',
    method: 'put',
    data: data
  })
}

// ---------- 直接与间接测评方式 Assessment ----------
// 查询项目下的测评配置
export function listAssessment(programId) {
  return request({
    url: '/aol/assessment/list',
    method: 'get',
    params: { programId: programId }
  })
}

// 新增测评
export function addAssessment(data) {
  return request({
    url: '/aol/assessment',
    method: 'post',
    data: data
  })
}

// 修改测评
export function updateAssessment(data) {
  return request({
    url: '/aol/assessment',
    method: 'put',
    data: data
  })
}

// 删除测评
export function delAssessment(assessId) {
  return request({
    url: '/aol/assessment/' + assessId,
    method: 'delete'
  })
}

// ---------- 课程 AoL 委员会 ----------
// 查询项目下委员会成员
export function listCommittee(programId) {
  return request({
    url: '/aol/committee/list',
    method: 'get',
    params: { programId: programId }
  })
}

// 新增委员会成员
export function addCommittee(data) {
  return request({
    url: '/aol/committee',
    method: 'post',
    data: data
  })
}

// 修改委员会成员
export function updateCommittee(data) {
  return request({
    url: '/aol/committee',
    method: 'put',
    data: data
  })
}

// 删除委员会成员
export function delCommittee(committeeId) {
  return request({
    url: '/aol/committee/' + committeeId,
    method: 'delete'
  })
}

// 查询教师列表（委员会成员选择用，选项文案：姓名(工号)）
export function getTeachers() {
  return request({
    url: '/aol/teachers',
    method: 'get'
  })
}
