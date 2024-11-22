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
      >
        <el-menu-item index="/home">
          <el-icon><House /></el-icon>
          <template #title>首页</template>
        </el-menu-item>
      </el-menu>
    </div>
    
    <div class="main">
      <div class="header">
        <div class="toggle-sidebar" @click="toggleSidebar">
          <el-icon><Fold v-if="!isCollapse"/><Expand v-else/></el-icon>
        </div>
        <header-bar />
      </div>
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import { House, Fold, Expand } from '@element-plus/icons-vue'
import HeaderBar from './HeaderBar.vue'

const route = useRoute()
const isCollapse = ref(true)
const activeMenu = computed(() => route.path)

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

.toggle-sidebar {
  padding: 0 15px;
  cursor: pointer;
  font-size: 20px;
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
</style> 