import request from './index.js'
export const getProject = () => request.get('/project')
export const saveWordCount = (projectId, current) => request.put('/project/word', { projectId, current })


//"接口里面没有写的"
export const createProject = (data) => request.post('/project', data)
export const updateProject = (projectId, data) => request.put(`/project/${projectId}`, data)
export const deleteProject = (projectId) => request.delete(`/project/${projectId}`)