import request from '@/utils/request'

// 获取研究型学者评估结果
export function getEvaluationResult(semester) {
  return request({
    url: '/research/evaluation/result',
    method: 'get',
    params: { semester: semester }
  })
}

// 获取全体学生总学分
export function getTotalCredits(semester) {
  return request({
    url: '/research/evaluation/totalCredits',
    method: 'get',
    params: { semester: semester }
  })
}

// 检查某位教授是否为研究型学者
export function checkProfessor(professorId, semester) {
  return request({
    url: '/research/evaluation/check',
    method: 'get',
    params: { professorId: professorId, semester: semester }
  })
}
