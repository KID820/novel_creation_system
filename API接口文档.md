# 幻笔写作平台 - 前后端接口文档

## 基础配置

### 接口基地址
```
http://localhost:8081/api
```

### 请求头配置
```javascript
{
  "Content-Type": "application/json",
  "Authorization": "Bearer {token}"  // 登录后需要携带
}
```

### 响应格式规范
所有接口统一返回格式：
```javascript
{
  "code": 200,           // 状态码：200成功，其他为失败
  "message": "success",  // 提示信息
  "data": {}            // 业务数据
}
```

---

## 一、用户模块 API

### 1.1 用户注册

**接口地址**：`POST /user/register`

**请求参数**：
```javascript
{
  "nickname": "string",  // 昵称，2-10个字符，必填
  "email": "string",     // 邮箱，符合邮箱格式，必填
  "password": "string",  // 密码，6-20位，必填
  "confirm": "string"    // 确认密码，需与password一致，必填
}
```

**请求示例**：
```javascript
{
  "nickname": "张三",
  "email": "zhangsan@example.com",
  "password": "123456",
  "confirm": "123456"
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "userId": 1,
    "nickname": "张三",
    "email": "zhangsan@example.com",
    "createdAt": "2025-11-05T10:30:00Z"
  }
}
```

**前端调用位置**：`src/views/Register.vue` - `handleRegister()` 方法

---

### 1.2 用户登录

**接口地址**：`POST /user/login`

**请求参数**：
```javascript
{
  "email": "string",     // 邮箱，必填
  "password": "string"   // 密码，必填
}
```

**请求示例**：
```javascript
{
  "email": "zhangsan@example.com",
  "password": "123456"
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "userId": 1,
      "nickname": "张三",
      "email": "zhangsan@example.com"
    }
  }
}
```

**特别说明**：
- 前端会将 `token` 存储到 `localStorage`
- 后续所有需要认证的请求都会在请求头中携带此 token

**前端调用位置**：`src/views/Login.vue` - `handleLogin()` 方法

---

### 1.3 获取用户信息

**接口地址**：`GET /user/profile`

**请求参数**：无（通过 Header 中的 token 识别用户）

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": {
    "userId": 1,
    "nickname": "张三",
    "email": "zhangsan@example.com",
    "avatar": "https://example.com/avatar.jpg",
    "createdAt": "2025-11-05T10:30:00Z"
  }
}
```

**前端调用位置**：`src/api/user.js` - `getProfile()` 方法

---

## 二、项目模块 API

### 2.1 获取项目信息

**接口地址**：`GET /project`

**请求参数**：无（通过 token 获取当前用户的项目）

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": {
    "projectId": 1,
    "name": "星际迷航：新纪元",
    "genre": "科幻小说",
    "target": 80000,        // 目标字数
    "current": 12450,       // 当前字数
    "status": "writing",    // 项目状态：writing-写作中, paused-暂停, completed-已完成
    "createdAt": "2025-10-01T08:00:00Z",
    "updatedAt": "2025-11-05T15:30:00Z"
  }
}
```

**字段说明**：
- `projectId`: 项目唯一标识
- `name`: 项目名称
- `genre`: 小说类型
- `target`: 目标字数
- `current`: 当前已写字数
- `status`: 项目状态

**前端调用位置**：`src/api/project.js` - `getProject()` 方法

**前端使用场景**：
- `Dashboard.vue` 组件加载时获取项目信息
- 左侧栏"当前项目"卡片展示
- 计算进度百分比：`(current / target) * 100`

---

### 2.2 保存/更新字数

**接口地址**：`PUT /project/word`

**请求参数**：
```javascript
{
  "projectId": 1,      // 项目ID，必填
  "current": 15230     // 当前字数，必填
}
```

**请求示例**：
```javascript
{
  "projectId": 1,
  "current": 15230
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "保存成功",
  "data": {
    "projectId": 1,
    "current": 15230,
    "increment": 2780,     // 本次增加字数
    "updatedAt": "2025-11-05T16:45:00Z"
  }
}
```

**前端调用位置**：`src/api/project.js` - `saveWordCount()` 方法

**前端使用场景**：
- `Dashboard.vue` 中 `onInput()` 方法触发时
- 用户编辑内容后自动更新字数
- 建议实现防抖：用户停止输入 2-3 秒后再调用接口

**调用时机建议**：
```javascript
// 使用防抖优化
import { debounce } from 'lodash-es'

const saveWords = debounce(async () => {
  await saveWordCount(project.projectId, project.current)
}, 2000)

function onInput() {
  project.current = content.value.trim().length
  sessionWords.value = Math.max(0, project.current - 12450)
  saveWords() // 防抖调用
}
```

---

## 三、章节模块 API（建议扩展）

### 3.1 获取章节列表

**接口地址**：`GET /project/{projectId}/chapters`

**请求参数**：路径参数 `projectId`

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "chapterId": 1,
      "title": "第一章：启程",
      "words": 2340,
      "content": "章节内容...",
      "order": 1,
      "createdAt": "2025-10-01T08:00:00Z",
      "updatedAt": "2025-11-05T15:30:00Z"
    },
    {
      "chapterId": 2,
      "title": "第二章：异星",
      "words": 3120,
      "content": "章节内容...",
      "order": 2,
      "createdAt": "2025-10-02T09:00:00Z",
      "updatedAt": "2025-11-05T16:00:00Z"
    }
  ]
}
```

**前端使用场景**：
- `Dashboard.vue` 左侧栏"章节管理"区域
- 当前代码中章节数据是硬编码的，需要改为从接口获取

---

### 3.2 创建章节

**接口地址**：`POST /project/{projectId}/chapters`

**请求参数**：
```javascript
{
  "title": "第五章：新的挑战",
  "order": 5
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "chapterId": 5,
    "title": "第五章：新的挑战",
    "words": 0,
    "content": "",
    "order": 5,
    "createdAt": "2025-11-05T17:00:00Z"
  }
}
```

**前端使用场景**：点击章节管理的"+"按钮时调用

---

### 3.3 更新章节内容

**接口地址**：`PUT /project/chapters/{chapterId}`

**请求参数**：
```javascript
{
  "title": "第一章：启程（修订版）",
  "content": "更新后的章节内容...",
  "words": 2560
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "chapterId": 1,
    "title": "第一章：启程（修订版）",
    "words": 2560,
    "updatedAt": "2025-11-05T17:15:00Z"
  }
}
```

---

### 3.4 删除章节

**接口地址**：`DELETE /project/chapters/{chapterId}`

**响应示例**：
```javascript
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

---

## 四、角色模块 API（建议扩展）

### 4.1 获取角色列表

**接口地址**：`GET /project/{projectId}/characters`

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "characterId": 1,
      "name": "艾莉克斯",
      "role": "主角",
      "avatar": "bg-blue-500",
      "description": "年轻的科学官，充满好奇心",
      "createdAt": "2025-10-01T08:00:00Z"
    },
    {
      "characterId": 2,
      "name": "马克",
      "role": "船长",
      "avatar": "bg-green-500",
      "description": "经验丰富的飞船指挥官",
      "createdAt": "2025-10-01T08:00:00Z"
    }
  ]
}
```

**前端使用场景**：`Dashboard.vue` 左侧栏"角色管理"区域

---

### 4.2 创建角色

**接口地址**：`POST /project/{projectId}/characters`

**请求参数**：
```javascript
{
  "name": "新角色",
  "role": "配角",
  "avatar": "bg-purple-500",
  "description": "角色描述"
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "创建成功",
  "data": {
    "characterId": 5,
    "name": "新角色",
    "role": "配角",
    "avatar": "bg-purple-500",
    "description": "角色描述",
    "createdAt": "2025-11-05T17:30:00Z"
  }
}
```

---

## 五、AI 辅助模块 API（建议扩展）

### 5.1 获取写作灵感

**接口地址**：`POST /ai/inspiration`

**请求参数**：
```javascript
{
  "projectId": 1,
  "style": "sci-fi",           // 写作风格：sci-fi, mystery, romance, fantasy, thriller
  "context": "当前文本内容...",  // 当前编辑的内容，用于AI理解上下文
  "chapterId": 1
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": {
    "type": "灵感",
    "text": "试试让飞船的 AI 系统突然发生异常，为后续埋下伏笔。",
    "timestamp": "2025-11-05T18:00:00Z"
  }
}
```

**前端使用场景**：`Dashboard.vue` 右侧栏点击"灵感"按钮时调用

---

### 5.2 AI 续写

**接口地址**：`POST /ai/continue`

**请求参数**：
```javascript
{
  "projectId": 1,
  "style": "sci-fi",
  "context": "当前文本内容...",
  "length": 200                 // 期望续写字数
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": {
    "content": "突然，飞船主控室的灯光闪烁了几下，一种低频的共振声从甲板下方传来……",
    "words": 150
  }
}
```

**前端使用场景**：`Dashboard.vue` 右侧栏点击"续写"按钮时调用

---

### 5.3 获取写作建议

**接口地址**：`POST /ai/suggestions`

**请求参数**：
```javascript
{
  "projectId": 1,
  "style": "sci-fi",
  "context": "当前文本内容...",
  "chapterId": 1
}
```

**响应示例**：
```javascript
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "type": "情节建议",
      "text": "考虑让艾莉克斯发现信号中隐藏的模式，这可能是外星文明试图传达重要信息。"
    },
    {
      "type": "角色发展",
      "text": "可以深入描写艾莉克斯的内心冲突，她对未知的恐惧与科学好奇心之间的斗争。"
    },
    {
      "type": "环境描写",
      "text": "增加更多关于飞船内部环境的细节描写，营造更加沉浸的科幻氛围。"
    }
  ]
}
```

**前端使用场景**：
- `Dashboard.vue` 右侧栏自动加载建议
- 或点击刷新按钮获取新建议

---

## 六、错误码说明

| 错误码 | 说明 | 处理建议 |
|--------|------|----------|
| 200 | 成功 | - |
| 400 | 请求参数错误 | 检查请求参数格式和必填项 |
| 401 | 未授权/Token过期 | 跳转到登录页 |
| 403 | 无权限访问 | 提示用户无权限 |
| 404 | 资源不存在 | 提示资源未找到 |
| 409 | 资源冲突（如邮箱已存在） | 提示用户修改信息 |
| 500 | 服务器内部错误 | 提示用户稍后重试 |

---

## 七、前端拦截器说明

### 7.1 请求拦截器
**文件位置**：`src/utils/request.js` 和 `src/api/index.js`

```javascript
request.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
```

**功能**：自动在请求头中添加 token

---

### 7.2 响应拦截器
**文件位置**：`src/utils/request.js`

```javascript
request.interceptors.response.use(
  (res) => res.data,  // 成功时直接返回 data 部分
  (err) => {
    ElMessage.error(err.response?.data?.message || '请求失败')
    return Promise.reject(err)
  }
)
```

**功能**：
- 成功响应：直接返回 `response.data`
- 失败响应：显示错误消息

**后端响应格式要求**：
```javascript
// 成功
{
  code: 200,
  message: "success",
  data: { ... }
}

// 失败
{
  code: 400,
  message: "邮箱格式不正确",
  data: null
}
```

---

## 八、前端调用示例

### 8.1 用户登录完整流程
```javascript
// src/views/Login.vue
const handleLogin = async () => {
  await formRef.value.validate()
  
  // 调用登录接口
  const res = await login(form)
  // res 已经是 response.data（被拦截器处理过）
  
  // 存储 token
  localStorage.setItem('token', res.token)
  
  // 提示成功
  ElMessage.success('登录成功')
  
  // 跳转
  router.push('/dashboard')
}
```

### 8.2 更新字数完整流程
```javascript
// src/views/Dashboard.vue
import { saveWordCount } from '@/api/project'

const onInput = debounce(async () => {
  const newWordCount = content.value.trim().length
  project.current = newWordCount
  
  try {
    // 调用保存接口
    await saveWordCount(project.projectId, newWordCount)
    console.log('字数已自动保存')
  } catch (error) {
    console.error('保存失败', error)
  }
}, 2000)
```

---

## 九、后端开发建议

### 9.1 数据库表设计参考

#### 用户表 (users)
```sql
CREATE TABLE users (
  user_id INT PRIMARY KEY AUTO_INCREMENT,
  nickname VARCHAR(50) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  avatar VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### 项目表 (projects)
```sql
CREATE TABLE projects (
  project_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  genre VARCHAR(50),
  target INT DEFAULT 0,
  current INT DEFAULT 0,
  status VARCHAR(20) DEFAULT 'writing',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);
```

#### 章节表 (chapters)
```sql
CREATE TABLE chapters (
  chapter_id INT PRIMARY KEY AUTO_INCREMENT,
  project_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  content TEXT,
  words INT DEFAULT 0,
  `order` INT DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (project_id) REFERENCES projects(project_id)
);
```

#### 角色表 (characters)
```sql
CREATE TABLE characters (
  character_id INT PRIMARY KEY AUTO_INCREMENT,
  project_id INT NOT NULL,
  name VARCHAR(50) NOT NULL,
  role VARCHAR(50),
  avatar VARCHAR(50),
  description TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (project_id) REFERENCES projects(project_id)
);
```

---

### 9.2 JWT Token 生成建议

```javascript
// Node.js 示例
const jwt = require('jsonwebtoken')

// 生成 token
function generateToken(userId) {
  return jwt.sign(
    { userId },
    'your-secret-key',
    { expiresIn: '7d' }  // 7天过期
  )
}

// 验证 token
function verifyToken(token) {
  try {
    return jwt.verify(token, 'your-secret-key')
  } catch (error) {
    throw new Error('Token无效或已过期')
  }
}
```

---

### 9.3 密码加密建议

```javascript
// 使用 bcrypt
const bcrypt = require('bcryptjs')

// 注册时加密
const hashedPassword = await bcrypt.hash(password, 10)

// 登录时验证
const isValid = await bcrypt.compare(inputPassword, hashedPassword)
```

---

## 十、前后端联调检查清单

### 10.1 接口对接检查
- [ ] 后端接口基地址配置正确（`http://localhost:8081/api`）
- [ ] 所有接口返回格式符合规范（包含 code, message, data）
- [ ] Token 生成和验证机制正常
- [ ] CORS 跨域配置正确（开发环境）

### 10.2 用户模块检查
- [ ] 注册接口参数验证（昵称长度、邮箱格式、密码长度）
- [ ] 邮箱唯一性校验
- [ ] 登录返回正确的 token
- [ ] token 存储到 localStorage
- [ ] 后续请求自动携带 token

### 10.3 项目模块检查
- [ ] 获取项目信息接口正常返回
- [ ] 字数更新接口参数正确
- [ ] 前端防抖机制避免频繁请求
- [ ] 字数计算逻辑一致

### 10.4 错误处理检查
- [ ] 401 错误自动跳转登录页
- [ ] 错误消息正确显示
- [ ] 网络异常提示友好

---

## 十一、环境变量配置

### 11.1 前端环境变量
创建 `.env.development` 文件：
```bash
VITE_API_BASE_URL=http://localhost:8081/api
```

创建 `.env.production` 文件：
```bash
VITE_API_BASE_URL=https://your-domain.com/api
```

### 11.2 后端配置
- 数据库连接配置
- JWT 密钥配置
- CORS 允许的源地址
- 端口配置（8081）

---

## 联系与支持

- 前端代码位置：`src/api/`, `src/views/`
- 拦截器位置：`src/utils/request.js`, `src/api/index.js`
- 路由配置：`src/router/index.js`

**注意事项**：
1. 所有需要认证的接口都需要在请求头携带 token
2. 前端会自动通过拦截器处理响应数据，后端只需返回标准格式
3. 建议后端实现防抖或限流，避免字数保存接口被频繁调用
4. AI 相关接口是扩展功能，可根据实际需求实现

---

**文档版本**：v1.0  
**更新日期**：2025-11-13