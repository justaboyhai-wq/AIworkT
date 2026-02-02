import request from '@/utils/request'

// 查询大模型配置列表
export function listLlmConfig(query) {
  return request({
    url: '/tool/llm/list',
    method: 'get',
    params: query
  })
}

// 查询大模型配置详细
export function getLlmConfig(id) {
  return request({
    url: '/tool/llm/' + id,
    method: 'get'
  })
}

// 新增大模型配置
export function addLlmConfig(data) {
  return request({
    url: '/tool/llm',
    method: 'post',
    data: data
  })
}

// 修改大模型配置
export function updateLlmConfig(data) {
  return request({
    url: '/tool/llm',
    method: 'put',
    data: data
  })
}

// 删除大模型配置
export function delLlmConfig(id) {
  return request({
    url: '/tool/llm/' + id,
    method: 'delete'
  })
}

// 测试连接
export function testLlmConnection(data) {
  return request({
    url: '/tool/llm/test',
    method: 'post',
    data: data
  })
}
