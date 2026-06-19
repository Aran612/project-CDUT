import request from '@/utils/request'
export const listBook = q => request({ url:'/research/book/list', method:'get', params:q })
export const getBook = id => request({ url:'/research/book/'+id, method:'get' })
export const addBook = d => request({ url:'/research/book', method:'post', data:d })
export const updateBook = d => request({ url:'/research/book', method:'put', data:d })
export const delBook = id => request({ url:'/research/book/'+id, method:'delete' })
export const importTemplate = () => request({ url:'/research/book/importTemplate', method:'post', responseType:'blob' })
