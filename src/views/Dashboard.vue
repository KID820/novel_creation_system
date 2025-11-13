<template>
  <!-- 最外层容器 -->
  <div class="h-screen flex bg-gray-50 text-gray-800">

    <!-- 左侧栏：固定280px -->
    <aside class="w-[280px] bg-white border-r border-gray-200 flex flex-col">

      <!-- 项目 -->
      <div class="p-4 border-b border-gray-200">
        <div class="flex items-center justify-between mb-3">
          <h2 class="text-sm font-semibold text-gray-700">当前项目</h2>
          <el-button type="primary" link :icon="Plus" />
        </div>
        <div class="p-3 bg-blue-50 rounded-xl">
          <h3 class="text-base font-bold text-gray-900 mb-1">{{ project.name }}</h3>
          <p class="text-xs text-gray-500 mb-2">科幻小说 · 目标 {{ project.target.toLocaleString() }} 字</p>
          <el-progress :percentage="projectPercent" color="#3B82F6" :show-text="false" class="mb-1" />
          <div class="flex justify-between text-xs text-gray-500">
            <span>{{ project.current.toLocaleString() }} 字</span>
            <span>还需 {{ (project.target - project.current).toLocaleString() }} 字</span>
          </div>
        </div>
      </div>

      <!-- 章节 -->
      <div class="flex-1 overflow-y-auto p-4">
        <div class="flex items-center justify-between mb-3">
          <h2 class="text-sm font-semibold text-gray-700">章节管理</h2>
          <el-button type="primary" link :icon="Plus" />
        </div>
        <div class="space-y-2">
          <div
            v-for="(c, i) in chapters"
            :key="i"
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
              <span class="text-xs text-gray-400">{{ c.words }} 字</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 角色 -->
      <div class="p-4 border-t border-gray-200">
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
      </div>
    </aside>

    <!-- 中间：自适应 -->
    <section class="flex-1 bg-white border-x border-gray-200 flex flex-col">
      <!-- 工具栏 -->
      <div class="px-5 py-3 border-b border-gray-200 flex items-center justify-between">
        <div class="flex items-center space-x-2">
          <el-button-group>
            <el-button size="small" :icon="Edit" title="粗体" />
            <el-button size="small" :icon="Edit" title="斜体" />
            <el-button size="small" :icon="Edit" title="下划线" />
          </el-button-group>
          <el-divider direction="vertical" />
          <el-button size="small" :icon="Link" />
          <el-button size="small" :icon="Picture" />
        </div>
        <div class="flex items-center space-x-3 text-sm">
          <span class="text-gray-500">字数</span>
          <span class="font-mono text-gray-900 font-semibold">{{ currentWords }}</span>
          <el-button type="primary" size="small" round @click="focusMode = !focusMode">
            {{ focusMode ? '退出专注' : '专注模式' }}
          </el-button>
        </div>
      </div>

      <!-- 编辑区 -->
  <div class="flex-1 p-6">
    <div class="h-full flex flex-col">
      <h1 class="text-2xl font-bold text-gray-900 mb-4">{{ chapters[activeChapter].title }}</h1>
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
          <el-button type="primary" size="small" plain @click="getInspiration">灵感</el-button>
          <el-button type="primary" size="small" plain @click="continueWriting">续写</el-button>
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
      <!-- 底部统计 -->
      <div class="fixed bottom-4 right-6 bg-white/90 backdrop-blur border border-gray-200 rounded-lg px-3 py-2 shadow flex items-center space-x-3 text-xs">
        <div class="text-center">
          <div class="font-mono text-gray-900">{{ sessionWords }}</div>
          <div class="text-gray-500">本次</div>
        </div>
        <el-divider direction="vertical" />
        <div class="text-center">
          <div class="font-mono text-gray-900">{{ productivity }}</div>
          <div class="text-gray-500">效率</div>
        </div>
        <el-divider direction="vertical" />
        <div class="text-center">
          <div class="font-mono text-gray-900">{{ formatTime(writingTime) }}</div>
          <div class="text-gray-500">时间</div>
        </div>
      </div>

</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Edit, Link, Picture } from '@element-plus/icons-vue'
import '@/assets/styles/dashboard.css'

/* ----------------- 数据 ----------------- */
const project = reactive({ name: '星际迷航：新纪元', target: 80000, current: 12450 })
const sessionWords = ref(0)
const writingTime = ref(0)
let timer = null

const focusMode = ref(false)
const writingStyle = ref('sci-fi')
const thinking = ref(false)
const activeChapter = ref(0)

const chapters = reactive([
  { title: '第一章：启程', words: 2340 },
  { title: '第二章：异星', words: 3120 },
  { title: '第三章：接触', words: 4230 },
  { title: '第四章：真相', words: 2760 }
])

const characters = reactive([
  { name: '艾莉克斯', role: '主角', avatar: 'bg-blue-500' },
  { name: '马克', role: '船长', avatar: 'bg-green-500' },
  { name: '佐拉', role: 'AI助手', avatar: 'bg-purple-500' },
  { name: '神秘人', role: '反派', avatar: 'bg-red-500' }
])

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
const projectPercent = computed(() => Math.round((project.current / project.target) * 100))
const productivity = computed(() => {
  const h = writingTime.value / 3600
  return h ? Math.round(sessionWords.value / h) : 0
})

/* ----------------- 方法 ----------------- */
function formatTime (s) {
  const m = String(Math.floor(s / 60)).padStart(2, '0')
  const h = String(Math.floor(s / 3600)).padStart(2, '0')
  return `${h}:${m}`
}

function onInput () {
  project.current = content.value.trim().length
  sessionWords.value = Math.max(0, project.current - 12450)
}

function getInspiration () {
  thinking.value = true
  setTimeout(() => {
    suggestions.push({ type: '灵感', text: '试试让飞船的 AI 系统突然发生异常，为后续埋下伏笔。' })
    thinking.value = false
    ElMessage.success('已生成新灵感')
  }, 800)
}

function continueWriting () {
  thinking.value = true
  setTimeout(() => {
    content.value += '\n\n【AI 续写】突然，飞船主控室的灯光闪烁了几下，一种低频的共振声从甲板下方传来……'
    onInput()
    thinking.value = false
    ElMessage.success('续写完成')
  }, 800)
}

function applySuggestion (s) {
  content.value += `\n\n【采纳建议】${s.text}`
  onInput()
  ElMessage.success('建议已插入')
}

function quickOperate (op) {
  ElMessage.info(`你点击了：${op}`)
}

/* ----------------- 生命周期 ----------------- */
onMounted(() => {
  timer = setInterval(() => { writingTime.value++ }, 1000)
})
onUnmounted(() => {
  clearInterval(timer)
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