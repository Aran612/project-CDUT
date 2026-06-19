import request from '@/utils/request'

// 查询资料分类列表
export function listCategory(query) {
  return request({
    url: '/resource/category/list',
    method: 'get',
    params: query
  })
}

// 查询资料分类详细
export function getCategory(categoryId) {
  return request({
    url: '/resource/category/' + categoryId,
    method: 'get'
  })
}

// 新增资料分类
export function addCategory(data) {
  return request({
    url: '/resource/category',
    method: 'post',
    data: data
  })
}

// 修改资料分类
export function updateCategory(data) {
  return request({
    url: '/resource/category',
    method: 'put',
    data: data
  })
}

// 删除资料分类
export function delCategory(categoryId) {
  return request({
    url: '/resource/category/' + categoryId,
    method: 'delete'
  })
}
