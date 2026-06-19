import request from '@/utils/request'

export function listPatent(query) {
  return request({ url: '/research/patent/list', method: 'get', params: query })
}
export function getPatent(patentId) {
  return request({ url: '/research/patent/' + patentId, method: 'get' })
}
export function addPatent(data) {
  return request({ url: '/research/patent', method: 'post', data })
}
export function updatePatent(data) {
  return request({ url: '/research/patent', method: 'put', data })
}
export function delPatent(patentId) {
  return request({ url: '/research/patent/' + patentId, method: 'delete' })
}
export function importTemplate() {
  return request({ url: '/research/patent/importTemplate', method: 'post', responseType: 'blob' })
}
