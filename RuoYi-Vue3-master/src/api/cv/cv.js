import request from '@/utils/request'

// ===================== 教师CV系统 API =====================

// 获取本人CV全部数据（档案 + 八大板块 + 科研成果快照）
export function getMyCv() {
  return request({
    url: '/cv/my',
    method: 'get'
  })
}

// 获取指定教师CV详细（管理员/认证办代录模式）
export function getCvDetail(professorId) {
  return request({
    url: '/cv/detail/' + professorId,
    method: 'get'
  })
}

// 保存本人CV档案（基本信息）
export function updateMyProfile(data) {
  return request({
    url: '/cv/my/profile',
    method: 'put',
    data: data
  })
}

// 管理员保存教师CV档案（body 含 professorId）
export function updateManageProfile(data) {
  return request({
    url: '/cv/manage/profile',
    method: 'put',
    data: data
  })
}

// CV 六个可编辑板块（教育/经历/资格/课程/发展/服务）
export const CV_SECTIONS = ['education', 'experience', 'certification', 'course', 'development', 'service']

// 板块 CRUD：6 组 × 本人(my)/管理(manage) 双通道，manage 侧 body 均含 professorId
export const sectionApi = {}
CV_SECTIONS.forEach(name => {
  sectionApi[name] = {
    // 教师本人通道
    addMy: data => request({ url: `/cv/my/${name}`, method: 'post', data: data }),
    updateMy: data => request({ url: `/cv/my/${name}`, method: 'put', data: data }),
    delMy: id => request({ url: `/cv/my/${name}/${id}`, method: 'delete' }),
    // 管理员代录通道
    addManage: data => request({ url: `/cv/manage/${name}`, method: 'post', data: data }),
    updateManage: data => request({ url: `/cv/manage/${name}`, method: 'put', data: data }),
    delManage: id => request({ url: `/cv/manage/${name}/${id}`, method: 'delete' })
  }
})

// 从授课记录同步CV授课板块（本人）
export function syncMyCourses() {
  return request({
    url: '/cv/my/course/sync',
    method: 'post'
  })
}

// 从授课记录同步CV授课板块（管理员代录）
export function syncManageCourses(professorId) {
  return request({
    url: '/cv/manage/course/sync',
    method: 'post',
    data: { professorId: professorId }
  })
}

// 新增科研成果（写入科研库并计入科研积分）
// type: paper 期刊论文 | book 专著教材 | patent 专利 | award 获奖 | project 科研项目
export function addResearch(type, data) {
  return request({
    url: `/cv/my/research/${type}`,
    method: 'post',
    data: data
  })
}
