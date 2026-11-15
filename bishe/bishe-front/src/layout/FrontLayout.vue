<template>
  <div>
    <header class="topbar">
      <div class="page-wrap nav-wrap">
        <div class="brand">
          <div class="brand-dot"></div>
          <div>
            <div class="brand-title">企业差旅报销系统</div>
            <div class="brand-sub">员工端工作台</div>
          </div>
        </div>
        <el-menu mode="horizontal" :default-active="activePath" class="menu" @select="onSelect">
          <el-menu-item index="/profile">个人信息</el-menu-item>
          <el-menu-item index="/apply">出差申请</el-menu-item>
          <el-menu-item index="/reimburse">报销填报</el-menu-item>
          <el-menu-item index="/progress">进度查询</el-menu-item>
          <el-menu-item index="/news">公司资讯</el-menu-item>
        </el-menu>
        <div class="user-area" v-if="auth.user">
          <el-avatar :src="auth.user.avatar" :size="34">{{ auth.user.userName?.slice(0, 1) }}</el-avatar>
          <span>{{ auth.user.userName }}</span>
          <el-button text @click="logoutAction">退出</el-button>
        </div>
      </div>
    </header>

    <main class="page-wrap main-wrap">
      <transition name="page" mode="out-in">
        <router-view />
      </transition>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/store/auth'

const router = useRouter()
const route = useRoute()
const auth = useAuthStore()
const activePath = computed(() => route.path)

function onSelect(index) {
  router.push(index)
}

async function logoutAction() {
  await auth.logoutAction()
  router.replace('/login')
}

onMounted(async () => {
  if (!auth.user) {
    await auth.fetchUser()
  }
})
</script>

<style scoped>
.topbar {
  position: sticky;
  top: 0;
  z-index: 20;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.88);
  border-bottom: 1px solid rgba(14, 165, 233, 0.12);
}
.nav-wrap {
  display: grid;
  grid-template-columns: 280px 1fr 220px;
  align-items: center;
  gap: 12px;
}
.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}
.brand-dot {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary), var(--accent));
}
.brand-sub {
  color: var(--text-sub);
  font-size: 12px;
}
.menu {
  border-bottom: none;
  background: transparent;
  min-width: 0;
}
.user-area {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  white-space: nowrap;
}
.main-wrap {
  padding-top: 20px;
}
:deep(.el-menu--horizontal > .el-menu-item.is-active) {
  border-bottom: none;
  background: rgba(14, 165, 233, 0.1);
  border-radius: 12px;
}
:deep(.el-menu--horizontal > .el-menu-item:hover) {
  background: rgba(14, 165, 233, 0.06);
  border-radius: 12px;
}
@media (max-width: 1024px) {
  .nav-wrap {
    grid-template-columns: 1fr;
    gap: 6px;
  }
  .user-area {
    justify-content: flex-start;
  }
}
</style>
