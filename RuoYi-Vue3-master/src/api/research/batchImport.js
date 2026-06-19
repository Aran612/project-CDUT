import request from '@/utils/request'

// 下载一键导入模板
export function importTemplate() {
  return request({
    url: '/research/batchImport/importTemplate',
    method: 'post',
    responseType: 'blob'
  })
}
