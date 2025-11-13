import request from './index.js'
export const getProject = () => request.get('/project')
export const saveWordCount = (projectId, current) => request.put('/project/word', { projectId, current })