import request from '@/utils/request'

// 查询数据源列表
export function listDataSource(query) {
  return request({
    url: '/tool/datasource/list',
    method: 'get',
    params: query
  })
}

// 查询数据源详细
export function getDataSource(id) {
  return request({
    url: '/tool/datasource/' + id,
    method: 'get'
  })
}

// 新增数据源
export function addDataSource(data) {
  return request({
    url: '/tool/datasource',
    method: 'post',
    data: data
  })
}

// 修改数据源
export function updateDataSource(data) {
  return request({
    url: '/tool/datasource',
    method: 'put',
    data: data
  })
}

// 删除数据源
export function delDataSource(id) {
  return request({
    url: '/tool/datasource/' + id,
    method: 'delete'
  })
}

// 测试连接
export function testDataSource(data) {
  return request({
    url: '/tool/datasource/test',
    method: 'post',
    data: data
  })
}

// 获取数据源Schema
export function getDataSourceSchema(id) {
  return request({
    url: '/tool/datasource/' + id + '/schema',
    method: 'get'
  })
}
