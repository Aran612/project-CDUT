import request from '@/utils/request'

// 全院科研总览
export function getSummary(year) {
  return request({
    url: '/research/stats/summary',
    method: 'get',
    params: { year }
  })
}

// 单教授科研统计
export function getProfessorStats(professorId) {
  return request({
    url: '/research/stats/professor/' + professorId,
    method: 'get'
  })
}

// 教授积分排行榜
export function getRanking(year) {
  return request({
    url: '/research/stats/ranking',
    method: 'get',
    params: { year }
  })
}
