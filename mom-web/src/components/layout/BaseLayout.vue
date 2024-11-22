<template>
  <div class="layout-container">
    <div class="sidebar" :class="{ collapsed: isCollapse }">
      <div class="logo">
        <span v-if="!isCollapse">SIM-MOM</span>
        <span v-else>SIM</span>
      </div>
      <el-menu
        :collapse="isCollapse"
        :default-active="activeMenu"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">
            <el-icon><User /></el-icon>
            <template #title>用户管理</template>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    
    <div class="main">
      <div class="header">
        <div class="header-left">
          <div class="toggle-sidebar" @click="toggleSidebar">
            <el-icon><Fold v-if="!isCollapse"/><Expand v-else/></el-icon>
          </div>
          <nav-breadcrumb />
        </div>
        <header-bar />
      </div>
      <div class="content">
        <router-view v-slot="{ Component }">
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cachedViews">
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { House, Setting, User, Fold, Expand } from '@element-plus/icons-vue'
import HeaderBar from './HeaderBar.vue'
import NavBreadcrumb from './NavBreadcrumb.vue'

const route = useRoute()
const isCollapse = ref(false)
const activeMenu = computed(() => route.path)

// 需要缓存的路由组件名称
const cachedViews = ref(['UserList', 'Profile'])

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
}

.sidebar {
  width: 210px;
  height: 100%;
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.sidebar.collapsed {
  width: 64px;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  border-bottom: 1px solid #1f2d3d;
}

.main {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.header {
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #e6e6e6;
  display: flex;
  align-items: center;
  padding: 0 15px;
}

.header-left {
  display: flex;
  align-items: center;
  flex: 1;
}

.toggle-sidebar {
  padding: 0 15px;
  cursor: pointer;
  font-size: 20px;
  display: flex;
  align-items: center;
}

.breadcrumb {
  flex: 1;
}

.content {
  flex: 1;
  padding: 20px;
  overflow: auto;
  background-color: #f0f2f5;
}

:deep(.el-menu) {
  border-right: none;
}

:deep(.el-menu--collapse) {
  width: 64px;
}

:deep(.el-sub-menu .el-menu-item) {
  min-width: 210px;
}

:deep(.el-menu-item.is-active) {
  background-color: #263445 !important;
}

:deep(.el-menu-item:hover) {
  background-color: #263445 !important;
}

:deep(.el-sub-menu__title:hover) {
  background-color: #263445 !important;
}

/* 添加过渡动画样式 */
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s;
}

.fade-transform-enter-from {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style> 