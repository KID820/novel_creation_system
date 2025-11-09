<template>
  <!-- 整体容器 -->
  <div class="dash-shell">
    <!-- 上方三栏 -->
    <div class="dash-top">
      <!-- 左侧：项目+章节+角色 -->
      <aside class="w-[280px] bg-white border-r border-gray-200 flex flex-col">
        <!-- 项目 -->
        <div class="dash-card mb-3">
          <div class="flex items-center justify-between mb-3">
            <h2 class="dash-title">当前项目</h2>
            <el-button type="primary" link :icon="Plus" />
          </div>
          <h3 class="font-bold text-gray-900 mb-1">{{ project.name }}</h3>
          <p class="text-xs text-gray-500 mb-2">科幻小说 · 目标 {{ project.target.toLocaleString() }} 字</p>
          <div class="dash-progress mb-2">
            <div class="dash-progress-bar" :style="{ width: projectPercent + '%' }" />
          </div>
          <div class="flex justify-between text-xs text-gray-500">
            <span>{{ project.current.toLocaleString() }} 字</span>
            <span>还需 {{ (project.target - project.current).toLocaleString() }} 字</span>
          </div>
        </div>

        <!-- 章节 -->
        <div class="dash-card mb-3 flex-1 overflow-y-auto">
          <div class="flex items-center justify-between mb-3">
            <h2 class="dash-title">章节管理</h2>
            <el-button type="primary" link :icon="Plus" />
          </div>
          <div class="space-y-2">
            <div
              v-for="(c, i) in chapters"
              :key="i"
              :class="[
                'px-3 py-2.5 rounded-lg cursor-pointer text-sm',
                i === activeChapter ? 'bg-blue-100 text-blue-700 shadow-inner' : 'hover:bg-gray-50'
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
        <div class="dash-card">
          <div class="flex items-center justify-between mb-3">
            <h2 class="dash-title">角色管理</h2>
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

      <!-- 中间：编辑器 -->
    <section class="flex-1 bg-white border-x border-gray-200 flex flex-col">
      <main class="dash-center">
        <div class="dash-editor-toolbar">
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
        <div class="dash-editor-body">
          <h1 class="text-xl font-bold text-gray-900 mb-3">{{ chapters[activeChapter].title }}</h1>
          <textarea
            v-model="content"
            class="dash-textarea"
            placeholder="开始你的创作之旅..."
            @input="onInput"
          />
        </div>
      </main>
    </section>
      <!-- 右侧：AI 助手 -->
      <aside class="w-[280px] bg-white border-l border-gray-200 flex flex-col">
        <div class="dash-card mb-3">
          <div class="flex items-center justify-between mb-3">
            <h2 class="dash-title">AI 助手</h2>
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

        <div class="dash-card mb-3 flex-1 overflow-y-auto">
          <h3 class="dash-title mb-3">建议</h3>
          <div class="space-y-3">
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
        </div>

        <div class="dash-card">
          <h3 class="dash-title mb-2">快速操作</h3>
          <div class="space-y-2">
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

    <!-- 底部效率栏 -->
    <footer class="dash-footer">
      <div class="footer-item">
        <span class="footer-label">本次写作</span>
        <span class="footer-value">{{ sessionWords }}</span>
      </div>
      <div class="footer-divider" />
      <div class="footer-item">
        <span class="footer-label">时长</span>
        <span class="footer-value">{{ formatTime(writingTime) }}</span>
      </div>
      <div class="footer-divider" />
      <div class="footer-item">
        <span class="footer-label">字/时</span>
        <span class="footer-value">{{ productivity }}</span>
      </div>
    </footer>
  </div>
</template>