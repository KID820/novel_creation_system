<template>
  <!-- 最外层容器 -->
  <div class="dashboard-container">
    <!-- 导航栏 -->
     <navbar />

     <!-- 主内容区 -->
    <div class="main-content h-screen flex bg-gray-50 text-gray-800">
    <!-- 左侧栏：固定320px -->
    <aside class="w-[320px] bg-white border-r border-gray-200 flex flex-col">

    <!-- 项目 -->
    <div class="p-4 border-b border-gray-200">
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-sm font-semibold text-gray-700">当前项目</h2>
        <div class="flex space-x-1">
          <el-button 
            type="primary" 
            link 
            :icon="Edit" 
            @click="editProject"
            title="编辑项目"
          />
          <el-button 
            type="danger" 
            link 
            :icon="Delete" 
            @click="deleteCurrentProject"
            title="删除项目"
          />
        </div>
      </div>
      
      <div class="bg-blue-50 rounded-lg p-4 mb-4">
        <h3 class="font-semibold text-gray-900 truncate mb-1">{{ project.name || '暂无项目' }}</h3>
        <p class="text-xs text-gray-600 mb-2">{{ project.genre || '未设置类型' }}</p>
        
        <div class="flex items-center justify-between text-sm mb-2">
          <span class="text-gray-700">进度</span>
          <span class="font-medium">{{ projectPercent }}%</span>
        </div>
        
        <el-progress 
          :percentage="projectPercent" 
          :show-text="false" 
          class="mb-2"
        />
        
        <div class="flex justify-between text-xs text-gray-500">
          <span>{{ project.current || 0 }} 字</span>
          <span>{{ project.target || 0 }} 字</span>
        </div>
      </div>
      
      <el-button 
        v-if="!project.projectId" 
        type="primary" 
        class="w-full" 
        @click="showCreateProjectDialog = true"
      >
        创建新项目
      </el-button>
    </div>
      
      <!-- 章节 -->
    <div class="flex-1 overflow-y-auto p-4">
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-sm font-semibold text-gray-700">章节管理</h2>
        <el-button type="primary" link :icon="Plus" @click="showCreateChapterDialog = true" />
      </div>
      <div class="space-y-2">
        <div
          v-for="(c, i) in chapters"
          :key="c.chapterId || i"
          :class="[
            'px-3 py-2.5 rounded-lg cursor-pointer text-sm',
            i === activeChapter
              ? 'bg-blue-100 text-blue-700 shadow'
              : 'hover:bg-gray-50'
          ]"
          @click="activeChapter = i"
        >
          <div class="flex items-center justify-between">
            <span>{{ c.title }}</span>
            <div class="flex items-center">
              <span class="text-xs text-gray-400 mr-2">{{ c.words }} 字</span>
              <el-dropdown v-if="chapters.length > 1">
                <el-button type="text" :icon="More" circle size="small" @click.stop />
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click.stop="editChapter(i)">编辑章节</el-dropdown-item>
                    <el-dropdown-item @click.stop="deleteCurrentChapter(i)">删除章节</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>
      </div>
    </div>

<!--     <div class="p-4 border-t border-gray-200">
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-sm font-semibold text-gray-700">角色管理</h2>
        <el-button type="primary" link :icon="Plus" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div
          v-for="char in characters"
          :key="char.name"
          class="flex flex-col items-center p-3 bg-gray-50 rounded-xl hover:bg-gray-100 cursor-pointer transition-all"
        >
          <div :class="['w-10 h-10 rounded-full mb-2 shadow', char.avatar]" />
          <span class="text-xs font-medium text-gray-800">{{ char.name }}</span>
          <span class="text-xs text-gray-500">{{ char.role }}</span>
        </div>
      </div>
    </div> -->
   <!-- 角色 -->
    <div class="p-4 border-t border-gray-200">
      <div class="flex items-center justify-between mb-3">
        <h2 class="text-sm font-semibold text-gray-700">角色管理</h2>
        <el-button type="primary" link :icon="Plus" @click="createNewCharacter" />
      </div>
      <div class="grid grid-cols-2 gap-3">
        <div
          v-for="(char, i) in characters"
          :key="char.characterId"
          class="flex flex-col items-center p-3 bg-gray-50 rounded-xl hover:bg-gray-100 cursor-pointer transition-all relative"
        >
          <div class="absolute top-1 right-1">
            <el-dropdown>
              <el-button type="text" :icon="More" circle size="small" />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="editCharacter(i)">编辑角色</el-dropdown-item>
                  <el-dropdown-item @click="deleteCharacter(i)">删除角色</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
          <div :class="['w-10 h-10 rounded-full mb-2 shadow', char.avatar]" />
          <span class="text-xs font-medium text-gray-800">{{ char.name }}</span>
          <span class="text-xs text-gray-500">{{ char.role }}</span>
        </div>
      </div>
    </div>
    </aside>

    <!-- 中间：自适应 -->
    <section class="flex-1 bg-white border-x border-gray-200 flex flex-col">

      <!-- 编辑区 -->
  <div class="flex-1 p-6">
    <div class="h-full flex flex-col">
      <!-- <h1 class="text-2xl font-bold text-gray-900 mb-4">{{ chapters[activeChapter].title }}</h1>
      <el-input
        v-model="content"
        type="textarea"
        class="flex-1"
        :input-style="{height:'calc(100vh - 240px)',minHeight: '600px',resize:'none',border:'none',outline:'none',background:'transparent',fontSize:'18px',lineHeight:1.8,padding:'20px'}"
        placeholder="开始你的创作之旅..."
        @input="onInput"
      />
        </div>
      </div>
    </section> -->
       <!-- 确保章节存在再显示标题 -->
          <h1 v-if="chapters.length > 0" class="text-2xl font-bold text-gray-900 mb-4">{{ chapters[activeChapter].title }}</h1>
          <el-input
            v-model="content"
            type="textarea"
            class="flex-1"
            :input-style="{height:'calc(100vh - 240px)',minHeight: '600px',resize:'none',border:'none',outline:'none',background:'transparent',fontSize:'18px',lineHeight:1.8,padding:'20px'}"
            placeholder="开始你的创作之旅..."
            @input="onInput"
          />
        </div>
      </div>
    </section>

    <!-- 右侧栏：固定280px -->
    <aside class="w-[280px] bg-white border-l border-gray-200 flex flex-col">
      <!-- 头部 -->
      <div class="p-4 border-b border-gray-200">
        <div class="flex items-center justify-between mb-3">
          <h2 class="text-sm font-semibold text-gray-700">AI 助手</h2>
          <div class="flex items-center space-x-1">
            <div class="w-2 h-2 bg-green-500 rounded-full" />
            <span class="text-xs text-gray-500">在线</span>
          </div>
        </div>
        <el-select v-model="writingStyle" size="small" class="w-full mb-3">
          <el-option label="科幻风格" value="sci-fi" />
          <el-option label="悬疑风格" value="mystery" />
          <el-option label="浪漫风格" value="romance" />
          <el-option label="奇幻风格" value="fantasy" />
          <el-option label="惊悚风格" value="thriller" />
        </el-select>
        <div class="grid grid-cols-2 gap-2">
          <el-button type="primary" size="small" plain @click="getInspirationHandler">灵感</el-button>
          <el-button type="primary" size="small" plain @click="continueWritingHandler">续写</el-button>
        </div>
      </div>

      <!-- 建议 -->
      <div class="flex-1 overflow-y-auto p-4 space-y-3">
        <div
          v-for="(s, i) in suggestions"
          :key="i"
          class="bg-blue-50 border-l-4 border-blue-400 p-3 rounded-lg"
        >
          <div class="text-xs font-medium text-blue-700 mb-1">{{ s.type }}</div>
          <div class="text-xs text-gray-700 mb-2">{{ s.text }}</div>
          <el-button type="text" size="small" @click="applySuggestion(s)">应用</el-button>
        </div>
        <div v-if="thinking" class="text-xs text-gray-500 flex items-center">
          <div class="w-2 h-2 bg-blue-400 rounded-full mr-2 animate-pulse" />
          AI 分析中...
        </div>
      </div>

      <!-- 快速操作 -->
      <div class="p-4 border-t border-gray-200">
        <h3 class="text-sm font-semibold text-gray-700 mb-2">快速操作</h3>
        <div class="space-y-1">
          <el-button
            v-for="op in quickOps"
            :key="op"
            class="w-full justify-start"
            text
            size="small"
            @click="quickOperate(op)"
          >
            {{ op }}
          </el-button>
        </div>
      </div>
    </aside>
  </div>

    <!-- 底部统计 -->
    <div class="fixed bottom-2 right-4 bg-white/90 backdrop-blur border border-gray-200 rounded-md px-3 py-2 shadow flex items-center space-x-3 text-xs">
      <div class="text-center">
        <div class="font-mono text-gray-900 text-sm font-medium">{{ currentWords }}</div>
        <div class="text-gray-500 text-xs leading-none">本次字数</div>
      </div>
      <el-divider direction="vertical" />
      <div class="text-center">
        <div class="font-mono text-gray-900 text-sm font-medium">{{ productivity }}/h</div>
        <div class="text-gray-500 text-xs leading-none">效率</div>
      </div>
      <el-divider direction="vertical" />
      <div class="text-center">
        <div class="font-mono text-gray-900 text-sm font-medium">{{ wordsPerMinute }}/m</div>
        <div class="text-gray-500 text-xs leading-none">速度</div>
      </div>
      <el-divider direction="vertical" />
      <div class="text-center">
        <div class="font-mono text-gray-900 text-sm font-medium">{{ formatDuration(writingTime) }}</div>
        <div class="text-gray-500 text-xs leading-none">时长</div>
      </div>
    </div>
    
     <!-- 创建项目对话框 -->
    <el-dialog v-model="showCreateProjectDialog" title="创建新项目" width="500">
      <el-form 
        :model="newProjectForm" 
        :rules="projectRules" 
        ref="projectFormRef" 
        label-width="80px"
      >
        <el-form-item label="项目名称" prop="name">
          <el-input 
            v-model="newProjectForm.name" 
            placeholder="请输入项目名称"
          />
        </el-form-item>
        <el-form-item label="小说类型" prop="genre">
          <el-select v-model="newProjectForm.genre" class="w-full">
            <el-option label="科幻小说" value="科幻小说" />
            <el-option label="悬疑小说" value="悬疑小说" />
            <el-option label="言情小说" value="言情小说" />
            <el-option label="奇幻小说" value="奇幻小说" />
            <el-option label="武侠小说" value="武侠小说" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标字数" prop="target">
          <el-input-number 
            v-model="newProjectForm.target" 
            :min="1000" 
            :max="5000000" 
            :step="1000"
            class="w-full"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateProjectDialog = false">取消</el-button>
          <el-button type="primary" @click="handleCreateProject">确认创建</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 编辑项目对话框 -->
    <el-dialog v-model="showEditProjectDialog" title="编辑项目" width="500">
      <el-form :model="editProjectForm" label-width="80px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="editProjectForm.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="小说类型" prop="genre">
          <el-select v-model="editProjectForm.genre" class="w-full">
            <el-option label="科幻小说" value="科幻小说" />
            <el-option label="悬疑小说" value="悬疑小说" />
            <el-option label="言情小说" value="言情小说" />
            <el-option label="奇幻小说" value="奇幻小说" />
            <el-option label="武侠小说" value="武侠小说" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标字数" prop="target">
          <el-input-number 
            v-model="editProjectForm.target" 
            :min="1000" 
            :max="5000000" 
            :step="1000"
            class="w-full"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditProjectDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateProject">确认更新</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 创建章节对话框 -->
    <el-dialog v-model="showCreateChapterDialog" title="创建新章节" width="500">
      <el-form :model="newChapterForm" label-width="80px">
        <el-form-item label="章节标题">
          <el-input v-model="newChapterForm.title" placeholder="请输入章节标题" />
        </el-form-item>
        <el-form-item label="章节顺序">
          <el-input-number 
            v-model="newChapterForm.order" 
            :min="1" 
            class="w-full"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateChapterDialog = false">取消</el-button>
          <el-button type="primary" @click="handleCreateChapter">确认创建</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加编辑章节对话框 -->
    <el-dialog v-model="showEditChapterDialog" title="编辑章节" width="500">
      <el-form :model="editChapterForm" label-width="80px">
        <el-form-item label="章节标题">
          <el-input v-model="editChapterForm.title" placeholder="请输入章节标题" />
        </el-form-item>
        <el-form-item label="章节顺序">
          <el-input-number 
            v-model="editChapterForm.order" 
            :min="1" 
            class="w-full"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditChapterDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateChapter">确认更新</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加编辑角色对话框 -->
    <el-dialog v-model="showEditCharacterDialog" title="编辑角色" width="500">
      <el-form :model="editCharacterForm" label-width="80px">
        <el-form-item label="角色姓名">
          <el-input v-model="editCharacterForm.name" placeholder="请输入角色姓名" />
        </el-form-item>
        <el-form-item label="角色身份">
          <el-input v-model="editCharacterForm.role" placeholder="例如：主角、配角、反派等" />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input 
            v-model="editCharacterForm.description" 
            type="textarea" 
            placeholder="请输入角色描述"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="头像颜色">
          <el-select v-model="editCharacterForm.avatar" placeholder="请选择头像颜色">
            <el-option label="蓝色" value="bg-blue-500" />
            <el-option label="绿色" value="bg-green-500" />
            <el-option label="紫色" value="bg-purple-500" />
            <el-option label="红色" value="bg-red-500" />
            <el-option label="黄色" value="bg-yellow-500" />
            <el-option label="粉色" value="bg-pink-500" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showEditCharacterDialog = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateCharacter">确认更新</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 创建角色对话框 -->
    <el-dialog v-model="showCreateCharacterDialog" title="创建新角色" width="500">
      <el-form :model="newCharacterForm" label-width="80px">
        <el-form-item label="角色姓名">
          <el-input v-model="newCharacterForm.name" placeholder="请输入角色姓名" />
        </el-form-item>
        <el-form-item label="角色身份">
          <el-input v-model="newCharacterForm.role" placeholder="例如：主角、配角、反派等" />
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input 
            v-model="newCharacterForm.description" 
            type="textarea" 
            placeholder="请输入角色描述"
            :rows="3"
          />
        </el-form-item>
        <el-form-item label="头像颜色">
          <el-select v-model="newCharacterForm.avatar" placeholder="请选择头像颜色">
            <el-option label="蓝色" value="bg-blue-500" />
            <el-option label="绿色" value="bg-green-500" />
            <el-option label="紫色" value="bg-purple-500" />
            <el-option label="红色" value="bg-red-500" />
            <el-option label="黄色" value="bg-yellow-500" />
            <el-option label="粉色" value="bg-pink-500" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateCharacterDialog = false">取消</el-button>
          <el-button type="primary" @click="handleCreateCharacter">确认创建</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted , watch} from 'vue'
import { ElMessage, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElInputNumber ,ElOption } from 'element-plus'
import { Plus, Edit, Link, Picture, More , Delete, ArrowDown} from '@element-plus/icons-vue'
import { getProject, saveWordCount,createProject, updateProject,deleteProject } from '@/api/project' // 导入项目API
import { getChapters, createChapter, updateChapter, deleteChapter } from '@/api/chapter'
import { getCharacters, createCharacter } from '@/api/character' // 导入角色API
import { getInspiration, continueWriting, getSuggestions } from '@/api/ai'//导入AI API
import { debounce } from 'lodash-es' // 引入防抖函数
import '@/assets/styles/dashboard.css'
import Navbar from '../components/Navbar.vue'


/* 
----------------- 数据 ----------------- 
*/
//硬编码
//const project = reactive({ name: '星际迷航：新纪元', target: 80000, current: 12450 })
//响应式引用
const project = ref({
  projectId: 0,
  name: '',
  genre: '',
  target: 0,
  current: 0,
  status: ''
})

//创建项目对话框
const showCreateProjectDialog = ref(false)

//新项目表单
const newProjectForm = reactive({
  name: '',
  genre: '科幻小说',
  target: 80000
})

//项目表单验证
const projectRules = {
  name: [
    { required: true, message: '请输入项目名称', trigger: 'blur' },
    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
  ],
  target: [
    { required: true, message: '请输入目标字数', trigger: 'blur' },
    { type: 'number', min: 1000, message: '目标字数至少1000字', trigger: 'blur' }
  ]
}

const sessionWords = ref(0)
const writingTime = ref(0)
let timer = null

const focusMode = ref(false)
const writingStyle = ref('sci-fi')
const thinking = ref(false)
const activeChapter = ref(0)

/* 
const chapters = reactive([
  { title: '第一章：启程', words: 2340 },
  { title: '第二章：异星', words: 3120 },
  { title: '第三章：接触', words: 4230 },
  { title: '第四章：真相', words: 2760 }
]) 
  */
// 修改章节数据为从API获取
const chapters = ref([])

// 创建章节创建对话框
const showCreateChapterDialog = ref(false)

// 新建章节表单
const newChapterForm = reactive({
  title: '',
  order: 1
})

// 创建章节编辑对话框
const showEditChapterDialog = ref(false)

//添加编辑章节表单
const editChapterForm = reactive({
  title: '',
  order: 1
})

//编辑章节索引
const editingChapterIndex = ref(-1)

/* 
const characters = reactive([
  { name: '艾莉克斯', role: '主角', avatar: 'bg-blue-500' },
  { name: '马克', role: '船长', avatar: 'bg-green-500' },
  { name: '佐拉', role: 'AI助手', avatar: 'bg-purple-500' },
  { name: '神秘人', role: '反派', avatar: 'bg-red-500' }
]) 
  */
// 修改角色数据为从API获取
const characters = ref([])

// 创建角色对话框相关数据
const showCreateCharacterDialog = ref(false)
const newCharacterForm = reactive({
  name: '',
  role: '',
  description: '',
  avatar: 'bg-blue-500'
})

// 添加编辑角色相关数据
const showEditCharacterDialog = ref(false)

//添加编辑角色表单
const editCharacterForm = reactive({
  name: '',
  role: '',
  description: '',
  avatar: 'bg-blue-500'
})

//编辑角色索引
const editingCharacterIndex = ref(-1)


// AI建议数据
const suggestions = reactive([
  { type: '情节建议', text: '考虑让艾莉克斯发现信号中隐藏的模式，这可能是外星文明试图传达重要信息。' },
  { type: '角色发展', text: '可以深入描写艾莉克斯的内心冲突，她对未知的恐惧与科学好奇心之间的斗争。' },
  { type: '环境描写', text: '增加更多关于飞船内部环境的细节描写，营造更加沉浸的科幻氛围。' }
])

const quickOps = ['📊 生成对话', '🎭 角色分析', '🌍 世界观构建', '✨ 创意激发']

const content = ref(`艾莉克斯站在飞船的观察窗前，凝视着外面璀璨的星空。这是她第一次离开地球，前往未知的星系执行任务。作为一名年轻的科学官，她既兴奋又紧张。

"科学官艾莉克斯，请立即到指挥中心报到。"通讯器里传来了船长马克的声音。

她深吸一口气，整理了一下制服，然后快步走向指挥中心。当她到达时，发现整个指挥团队都已经就位，每个人的表情都很严肃。

"我们接收到了一个奇怪的信号，"马克船长解释道，"它来自一个我们从未探索过的星系，而且...它似乎是人为制造的。"

艾莉克斯感到一阵兴奋和担忧。这可能是人类第一次接触外星文明的机会，但也可能是一个危险的陷阱。她看向主屏幕，那里显示着一个她从未见过的星系图案，信号就来自那里。`)

/* ----------------- 计算 ----------------- */
const currentWords = computed(() => content.value.trim().length)
//const projectPercent = computed(() => Math.round((project.current / project.target) * 100))
// 修改projectPercent计算，使用ref值
const projectPercent = computed(() => project.value.target ? Math.round((project.value.current / project.value.target) * 100) : 0)

// 计算每小时字数（效率）
const productivity = computed(() => {
  if (writingTime.value === 0) return 0
  const hours = writingTime.value / 3600
  return Math.round(sessionWords.value / hours)
})

// 计算平均每分钟字数
const wordsPerMinute = computed(() => {
  if (writingTime.value === 0) return 0
  const minutes = writingTime.value / 60
  return (sessionWords.value / minutes).toFixed(1)
})

/* ----------------- 方法 ----------------- */

// 格式化时间
function formatDuration(seconds) {
  const h = Math.floor(seconds / 3600)
  const m = Math.floor((seconds % 3600) / 60)
  const s = seconds % 60
  
  if (h > 0) {
    return `${h}小时${m}分钟`
  } else if (m > 0) {
    return `${m}分钟${s}秒`
  } else {
    return `${s}秒`
  }
}

// 登出功能
function handleLogout() {
  localStorage.removeItem('token')
  $router.push('/login')
}

// 创建防抖保存函数
const debouncedSaveWordCount = debounce(async (newWordCount) => {
  try {
    await saveWordCount(project.value.projectId, newWordCount)
    console.log('字数已自动保存')
  } catch (error) {
    console.error('保存失败:', error)
    ElMessage.error('字数保存失败')
  }
}, 2000)

// 防抖获取AI建议
const debouncedGetSuggestions = debounce(async () => {
  try {
    if (!project.value.projectId) return
    
    const requestData = {
      projectId: project.value.projectId,
      style: writingStyle.value,
      context: content.value,
      chapterId: chapters.value[activeChapter.value]?.chapterId
    }
    
    const res = await getSuggestions(requestData)
    // 只保留前3个建议
    suggestions.splice(0, suggestions.length, ...res.slice(0, 3).map(suggestion => ({
      type: suggestion.type,
      text: suggestion.text
    })))
  } catch (error) {
    console.error('获取AI建议失败:', error)
  }
}, 5000) // 5秒防抖


function onInput() {
  const newWordCount = content.value.trim().length
  project.value.current = newWordCount
  sessionWords.value = Math.max(0, newWordCount - (project.value.projectId ? project.value.current : 12450))
  
  // 更新当前章节的字数
  if (chapters.value.length > 0 && activeChapter.value < chapters.value.length) {
    chapters.value[activeChapter.value].words = newWordCount
  }
  
  // 保存项目字数
  if (project.value.projectId) {
    debouncedSaveWordCount(newWordCount)
    
    // 保存章节内容
    if (chapters.value.length > 0 && activeChapter.value < chapters.value.length) {
      const currentChapter = chapters.value[activeChapter.value]
      debouncedUpdateChapter(currentChapter.chapterId, content.value, newWordCount)
    }

    // 获取AI建议
    debouncedGetSuggestions()
  }
}

//硬强插入灵感
/* function getInspiration () {
  thinking.value = true
  setTimeout(() => {
    suggestions.push({ type: '灵感', text: '试试让飞船的 AI 系统突然发生异常，为后续埋下伏笔。' })
    thinking.value = false
    ElMessage.success('已生成新灵感')
  }, 800)
} */

//硬插入续写
/* function continueWriting () {
  thinking.value = true
  setTimeout(() => {
    content.value += '\n\n【AI 续写】突然，飞船主控室的灯光闪烁了几下，一种低频的共振声从甲板下方传来……'
    onInput()
    thinking.value = false
    ElMessage.success('续写完成')
  }, 800)
} */

//快捷操作
function quickOperate (op) {
  ElMessage.info(`你点击了：${op}`)
}

// 获取章节列表
async function fetchChapters() {
  try {
    if (project.value.projectId) {
      const res = await getChapters(project.value.projectId)
      chapters.value = res.map(chapter => ({
        chapterId: chapter.chapterId,
        title: chapter.title,
        words: chapter.words,
        content: chapter.content,
        order: chapter.order
      }))
      
      // 如果没有章节，默认激活第一个章节或创建一个默认章节
      if (chapters.value.length > 0 && activeChapter.value >= chapters.value.length) {
        activeChapter.value = 0
      }
    }
  } catch (error) {
    console.error('获取章节列表失败:', error)
    ElMessage.error('获取章节列表失败')
  }
}

// 处理创建章节
async function handleCreateChapter() {
  try {
    if (!project.value.projectId) {
      ElMessage.error('请先创建项目') 
      return
    }
    
    const res = await createChapter(project.value.projectId, newChapterForm)
    
    chapters.value.push({
      chapterId: res.chapterId,
      title: res.title,
      words: res.words || 0,
      content: res.content || '',
      order: res.order
    })
    
    // 激活新创建的章节
    activeChapter.value = chapters.value.length - 1
    content.value = res.content || ''
    
    showCreateChapterDialog.value = false
    ElMessage.success('章节创建成功')
  } catch (error) {
    console.error('创建章节失败:', error)
    ElMessage.error('创建章节失败')
  }
}


// 编辑章节
function editChapter(index) {
  const chapter = chapters.value[index]
  editChapterForm.title = chapter.title
  editChapterForm.order = chapter.order
  editingChapterIndex.value = index
  showEditChapterDialog.value = true
}

// 处理章节更新
async function handleUpdateChapter() {
  try {
    const chapter = chapters.value[editingChapterIndex.value]
    const requestData = {
      title: editChapterForm.title,
      order: editChapterForm.order
    }
    
    await updateChapter(chapter.chapterId, requestData)
    
    // 更新本地数据
    chapter.title = editChapterForm.title
    chapter.order = editChapterForm.order
    
    showEditChapterDialog.value = false
    ElMessage.success('章节更新成功')
  } catch (error) {
    console.error('更新章节失败:', error)
    ElMessage.error('更新章节失败')
  }
}

// 更新章节内容
const debouncedUpdateChapter = debounce(async (chapterId, content, wordCount) => {
  try {
    await updateChapter(chapterId, {
      content: content,
      words: wordCount
    })
    console.log('章节内容已自动保存')
  } catch (error) {
    console.error('保存章节内容失败:', error)
    ElMessage.error('保存章节内容失败')
  }
}, 2000)

// 删除章节
async function deleteCurrentChapter() {
  if (chapters.value.length <= 1) {
    ElMessage.warning('至少需要保留一个章节')
    return
  }
  
  try {
    const currentChapter = chapters.value[activeChapter.value]
    await deleteChapter(currentChapter.chapterId)
    
    // 从列表中移除章节
    chapters.value.splice(activeChapter.value, 1)
    
    // 调整激活章节索引
    if (activeChapter.value >= chapters.value.length && chapters.value.length > 0) {
      activeChapter.value = chapters.value.length - 1
    }
    
    // 更新内容显示
    if (chapters.value.length > 0) {
      content.value = chapters.value[activeChapter.value]?.content || ''
    } else {
      content.value = ''
    }
    
    ElMessage.success('章节删除成功')
  } catch (error) {
    console.error('删除章节失败:', error)
    ElMessage.error('删除章节失败')
  }
}

/* // 修改删除章节函数以接受索引参数
async function deleteCurrentChapter(index = activeChapter.value) {
  if (chapters.value.length <= 1) {
    ElMessage.warning('至少需要保留一个章节')
    return
  }
  
  try {
    const chapterToDelete = chapters.value[index]
    await deleteChapter(chapterToDelete.chapterId)
    
    // 从列表中移除章节
    chapters.value.splice(index, 1)
    
    // 调整激活章节索引
    if (index <= activeChapter.value && activeChapter.value > 0) {
      activeChapter.value--
    }
    if (activeChapter.value >= chapters.value.length && chapters.value.length > 0) {
      activeChapter.value = chapters.value.length - 1
    }
    
    // 更新内容显示
    if (chapters.value.length > 0) {
      content.value = chapters.value[activeChapter.value]?.content || ''
    } else {
      content.value = ''
    }
    
    ElMessage.success('章节删除成功')
  } catch (error) {
    console.error('删除章节失败:', error)
    ElMessage.error('删除章节失败')
  }
} */

// 当切换章节时更新内容
watch(activeChapter, (newIndex) => {
  if (chapters.value.length > newIndex) {
    content.value = chapters.value[newIndex].content || ''
  }
})


// 获取角色列表
async function fetchCharacters() {
  try {
    if (project.value.projectId) {
      const res = await getCharacters(project.value.projectId)
      characters.value = res.map(character => ({
        characterId: character.characterId,
        name: character.name,
        role: character.role,
        avatar: character.avatar,
        description: character.description
      }))
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
    ElMessage.error('获取角色列表失败')
  }
}

// 创建新角色（打开对话框）
function createNewCharacter() {
  // 重置表单
  newCharacterForm.name = ''
  newCharacterForm.role = ''
  newCharacterForm.description = ''
  newCharacterForm.avatar = 'bg-blue-500'
  showCreateCharacterDialog.value = true
}

// 处理创建角色
async function handleCreateCharacter() {
  try {
    if (!project.value.projectId) {
      ElMessage.error('请先创建项目')
      return
    }
      
    //验证信息
    if (!newCharacterForm.name) {
      ElMessage.warning('请输入角色姓名')
      return
    }

    if (newCharacterForm.name == "牢大" ) {
      ElMessage.warning('what can i say')
    }
    
    const res = await createCharacter(project.value.projectId, newCharacterForm)
    
    characters.value.push({
      characterId: res.characterId,
      name: res.name,
      role: res.role,
      avatar: res.avatar,
      description: res.description
    })
    
    showCreateCharacterDialog.value = false
    ElMessage.success('角色创建成功')
  } catch (error) {
    console.error('创建角色失败:', error)
    ElMessage.error('创建角色失败')
  }
}

// 编辑角色
function editCharacter(index) {
  const character = characters.value[index]
  editCharacterForm.name = character.name
  editCharacterForm.role = character.role
  editCharacterForm.description = character.description
  editCharacterForm.avatar = character.avatar
  editingCharacterIndex.value = index
  showEditCharacterDialog.value = true
}

// 处理角色更新
async function handleUpdateCharacter() {
  try {
    if (!project.value.projectId) return
    
    const character = characters.value[editingCharacterIndex.value]
    const requestData = {
      name: editCharacterForm.name,
      role: editCharacterForm.role,
      description: editCharacterForm.description,
      avatar: editCharacterForm.avatar
    }
    //await updateCharacter(character.characterId, requestData)

    // 暂时采用前端更新方式
    character.name = editCharacterForm.name
    character.role = editCharacterForm.role
    character.description = editCharacterForm.description
    character.avatar = editCharacterForm.avatar
    
    showEditCharacterDialog.value = false
    ElMessage.success('角色更新成功')
  } catch (error) {
    console.error('更新角色失败:', error)
    ElMessage.error('更新角色失败')
  }
}

// 删除角色
async function deleteCharacter(index) {
  try {
    if (!project.value.projectId) return
    
    const character = characters.value[index]
    //await deleteCharacter(character.characterId)
    
    // 暂时采用前端删除方式
    characters.value.splice(index, 1)
    
    ElMessage.success('角色删除成功')
  } catch (error) {
    console.error('删除角色失败:', error)
    ElMessage.error('删除角色失败')
  }
}

// 取得项目信息
async function fetchProject() {
  try {
    const res = await getProject()
    project.value = {
      projectId: res.projectId,
      name: res.name,
      genre: res.genre,
      target: res.target,
      current: res.current,
      status: res.status
    }
    // 初始化sessionWords
    sessionWords.value = Math.max(0, res.current - 12450)
    
    // 获取章节列表
    await fetchChapters()

    // 获取角色列表
    await fetchCharacters()
    
    // 初始化内容显示
    if (chapters.value.length > 0) {
      content.value = chapters.value[activeChapter.value]?.content || ''
    }

    // 获取初始AI建议
    await fetchInitialSuggestions()
  } catch (error) {
    // 如果没有项目，提示用户创建项目
    if (error.response?.status === 404) {
      ElMessage.info('您还没有创建项目，请先创建一个项目')
    } else {
      console.error('获取项目信息失败:', error)
      ElMessage.error('获取项目信息失败')
    }
  }
}

// 创建新项目
async function handleCreateProject() {
  try {
    if (newProjectForm.name === '测试') {
      // 更新项目信息
      project.value = {
        projectId: 1,
        name: newProjectForm.name,
        genre: newProjectForm.genre,
        target: newProjectForm.target,
        current: 0,
        status: '正在编写'
      }
      sessionWords.value = 0
    
      showCreateProjectDialog.value = false
      ElMessage.success('项目创建成功')
      
      // 重置表单
      newProjectForm.name = ''
      newProjectForm.genre = '科幻小说'
      newProjectForm.target = 80000
      return
    }
    const requestData = {
      name: newProjectForm.name,
      genre: newProjectForm.genre,
      target: newProjectForm.target
    }
    
    const res = await createProject(requestData)
    
    // 更新项目信息
    project.value = {
      projectId: res.projectId,
      name: res.name,
      genre: res.genre,
      target: res.target,
      current: res.current || 0,
      status: res.status
    }
    
    sessionWords.value = 0
    
    showCreateProjectDialog.value = false
    ElMessage.success('项目创建成功')
    
    // 重置表单
    newProjectForm.name = ''
    newProjectForm.genre = '科幻小说'
    newProjectForm.target = 80000
  } catch (error) {
    console.error('创建项目失败:', error)
    ElMessage.error('创建项目失败')
  }
}

// 编辑项目
function editProject() {
  editProjectForm.name = project.value.name
  editProjectForm.genre = project.value.genre
  editProjectForm.target = project.value.target
  showEditProjectDialog.value = true
}

// 处理项目更新
async function handleUpdateProject() {
  try {
    const requestData = {
      name: editProjectForm.name,
      genre: editProjectForm.genre,
      target: editProjectForm.target
    }
    
    await updateProject(project.value.projectId, requestData)
    
    // 更新项目信息
    project.value.name = editProjectForm.name
    project.value.genre = editProjectForm.genre
    project.value.target = editProjectForm.target
    
    showEditProjectDialog.value = false
    ElMessage.success('项目更新成功')
  } catch (error) {
    console.error('更新项目失败:', error)
    ElMessage.error('更新项目失败')
  }
}

// 删除项目
async function deleteCurrentProject() {
  try {
      if (project.value.name === '测试') {
        project.value = {
        projectId: 0,
        name: '',
        genre: '',
        target: 0,
        current: 0,
        status: ''
      }
      
      // 清空章节和角色
      chapters.value = []
      characters.value = []
      content.value = ''
      return
    }

    await deleteProject(project.value.projectId)
    
    // 重置项目信息
    project.value = {
      projectId: 0,
      name: '',
      genre: '',
      target: 0,
      current: 0,
      status: ''
    }
    
    // 清空章节和角色
    chapters.value = []
    characters.value = []
    content.value = ''
    
    ElMessage.success('项目删除成功')
  } catch (error) {
    console.error('删除项目失败:', error)
    ElMessage.error('删除项目失败')
  }
}

// 获取初始AI建议
async function fetchInitialSuggestions() {
  try {
    if (!project.value.projectId) return
    
    const requestData = {
      projectId: project.value.projectId,
      style: writingStyle.value,
      context: content.value,
      chapterId: chapters.value[activeChapter.value]?.chapterId
    }
    
    const res = await getSuggestions(requestData)
    suggestions.length = 0 // 清空现有建议
    suggestions.push(...res.map(suggestion => ({
      type: suggestion.type,
      text: suggestion.text
    })))
  } catch (error) {
    console.error('获取AI建议失败:', error)
  }
}

// 获取写作灵感
async function getInspirationHandler() {
  thinking.value = true
  try {
    if (!project.value.projectId) {
      ElMessage.warning('请先创建项目')
      thinking.value = false
      return
    }
    
    const requestData = {
      projectId: project.value.projectId,
      style: writingStyle.value,
      context: content.value,
      chapterId: chapters.value[activeChapter.value]?.chapterId
    }
    
    const res = await getInspiration(requestData)
    
    suggestions.push({
      type: res.type || '灵感',
      text: res.text
    })
    
    ElMessage.success('已生成新灵感')
  } catch (error) {
    console.error('获取灵感失败:', error)
    ElMessage.error('获取灵感失败')
  } finally {
    thinking.value = false
  }
}

// AI续写
async function continueWritingHandler() {
  thinking.value = true
  try {
    if (!project.value.projectId) {
      ElMessage.warning('请先创建项目')
      thinking.value = false
      return
    }
    
    const requestData = {
      projectId: project.value.projectId,
      style: writingStyle.value,
      context: content.value,
      length: 200 // 期望续写字数
    }
    
    const res = await continueWriting(requestData)
    
    content.value += `\n\n${res.content}`
    onInput()
    
    ElMessage.success('续写完成')
  } catch (error) {
    console.error('续写失败:', error)
    ElMessage.error('续写失败')
  } finally {
    thinking.value = false
  }
}

// 应用建议
function applySuggestion(suggestion) {
  content.value += `\n\n【采纳建议】${suggestion.text}`
  onInput()
  ElMessage.success('建议已插入')
}

/* ----------------- 生命周期 ----------------- */
onMounted(() => {
  timer = setInterval(() => { writingTime.value++ }, 1000)
  // 组件挂载时获取项目信息
  fetchProject()

   // 每隔30分钟自动获取一次新建议
  setInterval(() => {
    if (project.value.projectId && !thinking.value) {
      fetchInitialSuggestions()
    }
  }, 300000*6)
})
onUnmounted(() => {
  clearInterval(timer)
  // 清除防抖定时器
  debouncedSaveWordCount.cancel()
})
</script>
<style scoped>
/* 微动画：按钮涟漪 */
button {
  transition: background-color 0.2s ease;
}
/* 进度条流光 */
.progress-bar {
  background: linear-gradient(90deg, #3B82F6, #60A5FA);
  background-size: 200% 200%;
  animation: flow 1.5s linear infinite;
}
@keyframes flow {
  0% { background-position: 0% 50%; }
  100% { background-position: 200% 50%; }
}
</style>