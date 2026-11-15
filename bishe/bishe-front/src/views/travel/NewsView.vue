<template>
  <div class="news-grid">
    <el-card v-for="item in list" :key="item.newsId" class="glass-card news-card" @click="openDetail(item)">
      <img :src="coverUrl(item.coverImage)" class="cover" @error="handleImageError" />
      <div class="body">
        <h3>{{ item.newsTitle }}</h3>
        <p class="meta">{{ item.publishTime }} · {{ typeText(item.newsType) }}</p>
        <p class="content">{{ strip(item.newsContent) }}</p>
      </div>
    </el-card>
  </div>

  <el-drawer v-model="open" :title="current.newsTitle" size="50%">
    <p class="meta">{{ current.publishTime }} · {{ typeText(current.newsType) }}</p>
    <div v-html="current.newsContent"></div>
  </el-drawer>
</template>

<script setup>
import { ref } from 'vue'
import { travelApi } from '@/api/travel'

const fallback = 'https://dummyimage.com/640x360/e0f2fe/0369a1&text=Travel+News'
const apiBase = '/dev-api'
const list = ref([])
const open = ref(false)
const current = ref({})

function typeText(type) {
  return { '1': '差旅政策', '2': '报销标准', '3': '通知公告' }[type] || '资讯'
}
function strip(html) {
  return (html || '').replace(/<[^>]+>/g, '').slice(0, 80)
}
function openDetail(item) {
  current.value = item
  open.value = true
}

function isExternal(url) {
  return /^(https?:|data:|blob:)/i.test(url)
}

function coverUrl(raw) {
  if (!raw) return fallback
  const first = String(raw).split(',')[0].trim()
  if (!first) return fallback
  if (isExternal(first) || first.startsWith(apiBase)) return first
  return first.startsWith('/') ? apiBase + first : `${apiBase}/${first}`
}

function handleImageError(e) {
  if (e?.target && e.target.src !== fallback) {
    e.target.src = fallback
  }
}

async function load() {
  const res = await travelApi.listPublishedNews({ pageNum: 1, pageSize: 100 })
  list.value = res.rows || []
}

load()
</script>

<style scoped>
.news-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 14px;
}
.news-card {
  cursor: pointer;
  overflow: hidden;
  transition: transform 0.2s ease;
}
.news-card:hover {
  transform: translateY(-2px);
}
.cover {
  width: 100%;
  height: 160px;
  object-fit: cover;
}
.body {
  padding: 10px;
}
.meta {
  color: var(--text-sub);
  font-size: 13px;
}
.content {
  color: #334155;
}
</style>
