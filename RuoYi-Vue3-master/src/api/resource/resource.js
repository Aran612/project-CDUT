import request from '@/utils/request'

// 查询资料共享列表
export function listResource(query) {
  return request({
    url: '/resource/resource/list',
    method: 'get',
    params: query
  })
}

// 查询资料共享详细
export function getResource(resourceId) {
  return request({
    url: '/resource/resource/' + resourceId,
    method: 'get'
  })
}

// 新增资料共享
export function addResource(data) {
  return request({
    url: '/resource/resource',
    method: 'post',
    data: data
  })
}

// 修改资料共享
export function updateResource(data) {
  return request({
    url: '/resource/resource',
    method: 'put',
    data: data
  })
}

// 删除资料共享
export function delResource(resourceId) {
  return request({
    url: '/resource/resource/' + resourceId,
    method: 'delete'
  })
}
