// src/api/ai.js
import request from './index.js'

// 获取写作灵感
export const getInspiration = (data) => request.post('/ai/inspiration', data)

// AI续写
export const continueWriting = (data) => request.post('/ai/continue', data)

// 获取写作建议
export const getSuggestions = (data) => request.post('/ai/suggestions', data)