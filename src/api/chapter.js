// src/api/chapter.js
import request from './index.js'

// 获取章节列表
export const getChapters = (projectId) => request.get(`/project/${projectId}/chapters`)

// 创建新章节
export const createChapter = (projectId, data) => request.post(`/project/${projectId}/chapters`, data)

// 更新章节内容
export const updateChapter = (chapterId, data) => request.put(`/project/chapters/${chapterId}`, data)

// 删除章节
export const deleteChapter = (chapterId) => request.delete(`/project/chapters/${chapterId}`)