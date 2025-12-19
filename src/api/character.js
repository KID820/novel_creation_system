// src/api/character.js
import request from './index.js'

// 获取角色列表
export const getCharacters = (projectId) => request.get(`/project/${projectId}/characters`)

// 创建新角色
export const createCharacter = (projectId, data) => request.post(`/project/${projectId}/characters`, data)


/* 
新增的接口 
*/

// 更新角色内容
export const updateCharacter = (characterId, data) => request.put(`/project/characters/${characterId}`, data)

// 删除角色
export const deleteCharacter = (characterId) => request.delete(`/project/characters/${characterId}`)