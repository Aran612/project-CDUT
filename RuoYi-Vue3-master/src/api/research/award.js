import request from '@/utils/request'
export const listAward = q => request({ url:'/research/award/list', method:'get', params:q })
export const getAward = id => request({ url:'/research/award/'+id, method:'get' })
export const addAward = d => request({ url:'/research/award', method:'post', data:d })
export const updateAward = d => request({ url:'/research/award', method:'put', data:d })
export const delAward = id => request({ url:'/research/award/'+id, method:'delete' })
export const importTemplate = () => request({ url:'/research/award/importTemplate', method:'post', responseType:'blob' })
