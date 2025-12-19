import request from './index.js'

export const register = (data) => request.post('/user/register', data)
export const login = (data) => request.post('/user/login', data)
export const getProfile = () => request.get('/user/profile')

/* 
新接口
*/
// 更新用户信息
export const updateProfile = (data) => request.put('/user/profile', data)