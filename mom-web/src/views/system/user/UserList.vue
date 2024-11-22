<template>
  <div class="user-list-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <div class="header-left">
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>新增用户
            </el-button>
            <el-button type="danger" :disabled="!selectedIds.length" @click="handleBatchDelete">
              <el-icon><Delete /></el-icon>批量删除
            </el-button>
          </div>
          <div class="header-right">
            <el-form :inline="true" :model="queryParams" @keyup.enter="handleQuery">
              <el-form-item>
                <el-input
                  v-model="queryParams.username"
                  placeholder="用户名"
                  clearable
                  @clear="handleQuery"
                />
              </el-form-item>
              <el-form-item>
                <el-input
                  v-model="queryParams.realName"
                  placeholder="真实姓名"
                  clearable
                  @clear="handleQuery"
                />
              </el-form-item>
              <el-form-item>
                <el-select
                  v-model="queryParams.status"
                  placeholder="状态"
                  clearable
                  @clear="handleQuery"
                >
                  <el-option label="启用" :value="1" />
                  <el-option label="禁用" :value="0" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQuery">
                  <el-icon><Search /></el-icon>搜索
                </el-button>
                <el-button @click="resetQuery">
                  <el-icon><Refresh /></el-icon>重置
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="userList"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="用户名" prop="username" />
        <el-table-column label="真实姓名" prop="realName" />
        <el-table-column label="手机号码" prop="phone" />
        <el-table-column label="邮箱" prop="email" />
        <el-table-column label="状态" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button link type="primary" @click="handleResetPwd(scope.row)">重置密码</el-button>
            <el-button link type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination
          v-model:current-page="queryParams.pageNum"
          v-model:page-size="queryParams.pageSize"
          :total="total"
          :page-sizes="[10, 20, 30, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 添加用户对话框 -->
    <user-dialog
      v-model:visible="dialogVisible"
      :is-edit="isEdit"
      :user-data="userData"
      @success="handleSuccess"
    />

    <!-- 添加重置密码对话框 -->
    <reset-password-dialog
      v-model:visible="resetPwdVisible"
      :user-id="selectedUserId"
      @success="handleSuccess"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Delete, Search, Refresh } from '@element-plus/icons-vue'
import request from '@/utils/request'
import UserDialog from './UserDialog.vue'
import ResetPasswordDialog from './ResetPasswordDialog.vue'

const loading = ref(false)
const userList = ref([])
const total = ref(0)
const selectedIds = ref([])

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  username: '',
  realName: '',
  status: undefined
})

// 对话框显示控制
const dialogVisible = ref(false)
const isEdit = ref(false)
const userData = ref({})

const resetPwdVisible = ref(false)
const selectedUserId = ref(null)

// 获取用户列表
const getUserList = async () => {
  loading.value = true
  try {
    const res = await request.get('/api/user/list', { params: queryParams.value })
    userList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('获取用户列表失败：', error)
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.value.pageNum = 1
  getUserList()
}

// 重置查询
const resetQuery = () => {
  queryParams.value = {
    pageNum: 1,
    pageSize: 10,
    username: '',
    realName: '',
    status: undefined
  }
  getUserList()
}

// 选择项变化
const handleSelectionChange = (selection) => {
  selectedIds.value = selection.map(item => item.id)
}

// 分页大小变化
const handleSizeChange = (val) => {
  queryParams.value.pageSize = val
  getUserList()
}

// 页码变化
const handleCurrentChange = (val) => {
  queryParams.value.pageNum = val
  getUserList()
}

// 状态变化
const handleStatusChange = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确认${row.status === 1 ? '启用' : '禁用'}用户"${row.username}"吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await request.put(`/api/user/${row.id}/status`, { status: row.status })
    ElMessage.success('状态更新成功')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('状态更新失败：', error)
      row.status = row.status === 1 ? 0 : 1
    } else {
      row.status = row.status === 1 ? 0 : 1
    }
  }
}

// 新增用户
const handleAdd = () => {
  isEdit.value = false
  userData.value = {}
  dialogVisible.value = true
}

// 编辑用户
const handleEdit = (row) => {
  isEdit.value = true
  userData.value = { ...row }
  dialogVisible.value = true
}

// 重置密码
const handleResetPwd = (row) => {
  selectedUserId.value = row.id
  resetPwdVisible.value = true
}

// 删除用户
const handleDelete = (row) => {
  ElMessageBox.confirm(
    `确认删除用户"${row.username}"吗？删除后数据将无法恢复！`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await request.delete(`/api/user/${row.id}`)
      ElMessage.success('删除成功')
      getUserList()
    } catch (error) {
      console.error('删除失败：', error)
    }
  }).catch(() => {})
}

// 批量删除
const handleBatchDelete = () => {
  if (!selectedIds.value.length) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  
  ElMessageBox.confirm(
    `确认删除选中的${selectedIds.value.length}个用户吗？删除后数据将无法恢复！`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(async () => {
    try {
      await request.delete('/api/user/batch', { data: selectedIds.value })
      ElMessage.success('批量删除成功')
      getUserList()
    } catch (error) {
      console.error('批量删除失败：', error)
    }
  }).catch(() => {})
}

// 操作成功回调
const handleSuccess = () => {
  getUserList()
}

onMounted(() => {
  getUserList()
})
</script>

<style scoped>
.user-list-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 