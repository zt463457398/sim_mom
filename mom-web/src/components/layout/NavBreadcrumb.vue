<template>
  <div class="nav-breadcrumb">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">
        <el-icon><House /></el-icon>
        <span>首页</span>
      </el-breadcrumb-item>
      <el-breadcrumb-item 
        v-for="(item, index) in breadcrumbList" 
        :key="index"
        :to="item.path"
      >
        <el-icon v-if="item.icon" :class="item.icon">
          <component :is="item.icon" />
        </el-icon>
        <span>{{ item.title }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { House, Setting, User } from '@element-plus/icons-vue'

const route = useRoute()
const breadcrumbList = ref([])

// 图标映射
const iconMap = {
  'system': Setting,
  'user': User
}

// 生成面包屑数据
const generateBreadcrumb = (route) => {
  const matched = route.matched.slice(1)
  breadcrumbList.value = matched.map(item => ({
    title: item.meta.title || '',
    path: item.path,
    icon: item.meta.icon || iconMap[item.name?.toLowerCase()]
  })).filter(item => item.title)
}

// 监听路由变化
watch(
  () => route.path,
  () => generateBreadcrumb(route),
  { immediate: true }
)
</script>

<style scoped>
.nav-breadcrumb {
  display: flex;
  align-items: center;
  padding-left: 8px;
  height: 100%;
  white-space: nowrap;
}

:deep(.el-breadcrumb) {
  display: flex;
  align-items: center;
  height: 100%;
}

:deep(.el-breadcrumb__item) {
  display: inline-flex;
  align-items: center;
  line-height: 1;
}

:deep(.el-breadcrumb__inner) {
  display: inline-flex !important;
  align-items: center;
  font-weight: normal !important;
  color: #606266;
  white-space: nowrap;
}

:deep(.el-breadcrumb__inner .el-icon) {
  margin-right: 4px;
  font-size: 14px;
}

:deep(.el-breadcrumb__separator) {
  margin: 0 8px;
  display: inline-flex;
  align-items: center;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: #303133;
  cursor: text;
}

:deep(.el-breadcrumb__inner:hover) {
  color: #409EFF;
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner:hover) {
  color: #303133;
}
</style> 