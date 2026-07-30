<template>
  <div class="app-container">
    <!-- 师资类型说明 -->
    <el-card style="margin-bottom: 20px;" type="info">
      <template #header>
        <strong>师资分类说明</strong>
      </template>
      <el-descriptions :column="4" border>
        <el-descriptions-item label="研究型学者">
          以科研为主，研究型课程学分占比应达到40%以上
        </el-descriptions-item>
        <el-descriptions-item label="教学型教师">
          以教学为主，承担大量基础课和专业课教学
        </el-descriptions-item>
        <el-descriptions-item label="双师型教师">
          兼顾教学与科研，教学科研并重
        </el-descriptions-item>
        <el-descriptions-item label="行政兼职">
          主要从事行政工作，少量承担教学任务
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 筛选条件 -->
    <el-form :inline="true" :model="queryParams" class="filter-form">
      <el-form-item label="教授姓名">
        <el-input v-model="queryParams.name" placeholder="请输入教授姓名" clearable style="width: 180px;" />
      </el-form-item>
      <el-form-item label="院系">
        <el-select v-model="queryParams.department" placeholder="全部院系" clearable style="width: 180px;">
          <el-option v-for="d in departmentList" :key="d" :label="d" :value="d" />
        </el-select>
      </el-form-item>
      <el-form-item label="职称">
        <el-select v-model="queryParams.title" placeholder="全部职称" clearable style="width: 150px;">
          <el-option label="教授" value="教授" />
          <el-option label="副教授" value="副教授" />
          <el-option label="讲师" value="讲师" />
          <el-option label="助教" value="助教" />
        </el-select>
      </el-form-item>
      <el-form-item label="师资类型">
        <el-select v-model="queryParams.teacherType" placeholder="全部类型" clearable style="width: 150px;">
          <el-option v-for="(name, key) in teacherTypeDict" :key="key" :label="name" :value="key" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 师资分类统计 -->
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="6" v-for="(count, type) in teacherTypeCounts" :key="type">
        <el-card class="count-card" :class="'type-' + type" shadow="hover">
          <div class="count-content">
            <div class="count-value">{{ count }}</div>
            <div class="count-label">{{ teacherTypeDict[type] || '未知' }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 教授列表 -->
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span><strong>教授师资分类列表</strong></span>
          <div>
            <el-button type="success" @click="batchEdit" :disabled="selectedRows.length === 0">
              批量修改 ({{ selectedRows.length }})
            </el-button>
            <el-button @click="exportData">导出</el-button>
          </div>
        </div>
      </template>
      <el-table :data="professorList" v-loading="loading" stripe @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" />
        <el-table-column label="ID" align="center" prop="professor_id" width="70" />
        <el-table-column label="姓名" align="center" prop="name" width="100" />
        <el-table-column label="所属院系" align="center" prop="department" width="160" show-overflow-tooltip />
        <el-table-column label="职称" align="center" prop="title" width="100" />
        <el-table-column label="师资类型" align="center" width="120">
          <template #default="scope">
            <el-tag
              :type="getTeacherTypeTag(scope.row.teacher_type)"
              style="cursor: pointer;"
              @click="editTeacherType(scope.row)"
            >
              {{ getTeacherTypeName(scope.row.teacher_type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="累计积分" align="center" prop="total_score" width="100">
          <template #default="scope">
            <el-tag type="warning">{{ scope.row.total_score || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'success' : 'info'">
              {{ scope.row.status === '0' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template #default="scope">
            <el-button link type="primary" @click="editTeacherType(scope.row)">修改类型</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <!-- 修改师资类型对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改师资类型" width="400px">
      <el-form :model="editForm" label-width="100px">
        <el-form-item label="教授姓名">
          <span>{{ editForm.name }}</span>
        </el-form-item>
        <el-form-item label="师资类型">
          <el-select v-model="editForm.teacherType" placeholder="请选择师资类型" style="width: 100%;">
            <el-option v-for="(name, key) in teacherTypeDict" :key="key" :label="name" :value="key" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitEdit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 批量修改对话框 -->
    <el-dialog v-model="batchDialogVisible" title="批量修改师资类型" width="400px">
      <el-form :model="batchForm" label-width="100px">
        <el-form-item label="已选教授">
          <span>{{ selectedRows.length }} 位教授</span>
        </el-form-item>
        <el-form-item label="师资类型">
          <el-select v-model="batchForm.teacherType" placeholder="请选择师资类型" style="width: 100%;">
            <el-option v-for="(name, key) in teacherTypeDict" :key="key" :label="name" :value="key" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="batchDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBatch">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="TeacherType">
import { ref, reactive, computed, onMounted } from 'vue'
import { getDepartmentList, getTeacherTypeDict } from '@/api/research/credit'
import request from '@/utils/request'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const professorList = ref([])
const total = ref(0)
const selectedRows = ref([])
const departmentList = ref([])
const teacherTypeDict = ref({})

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  name: '',
  department: '',
  title: '',
  teacherType: ''
})

const editDialogVisible = ref(false)
const batchDialogVisible = ref(false)

const editForm = reactive({
  professorId: null,
  name: '',
  teacherType: ''
})

const batchForm = reactive({
  teacherType: ''
})

const teacherTypeCounts = computed(() => {
  const counts = {}
  professorList.value.forEach(item => {
    const type = item.teacher_type || '3'
    counts[type] = (counts[type] || 0) + 1
  })
  return counts
})

function getTeacherTypeTag(type) {
  const map = { '1': '', '2': 'success', '3': 'warning', '4': 'info' }
  return map[type] || 'info'
}

function getTeacherTypeName(type) {
  return teacherTypeDict.value[type] || '未分类'
}

async function getList() {
  loading.value = true
  try {
    const res = await request({
      url: '/research/professor/list',
      method: 'get',
      params: queryParams
    })
    professorList.value = res.rows || []
    total.value = res.total || 0
  } catch (error) {
    proxy.$modal.msgError('加载数据失败')
  } finally {
    loading.value = false
  }
}

function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

function resetQuery() {
  queryParams.name = ''
  queryParams.department = ''
  queryParams.title = ''
  queryParams.teacherType = ''
  handleQuery()
}

function handleSelectionChange(rows) {
  selectedRows.value = rows
}

function editTeacherType(row) {
  editForm.professorId = row.professor_id
  editForm.name = row.name
  editForm.teacherType = row.teacher_type || ''
  editDialogVisible.value = true
}

async function submitEdit() {
  try {
    await request({
      url: '/research/professor/' + editForm.professorId,
      method: 'put',
      data: { teacherType: editForm.teacherType }
    })
    proxy.$modal.msgSuccess('修改成功')
    editDialogVisible.value = false
    getList()
  } catch (error) {
    proxy.$modal.msgError('修改失败')
  }
}

function batchEdit() {
  batchForm.teacherType = ''
  batchDialogVisible.value = true
}

async function submitBatch() {
  try {
    const ids = selectedRows.value.map(row => row.professor_id).join(',')
    await request({
      url: '/research/professor/batchTeacherType',
      method: 'put',
      data: { ids: ids, teacherType: batchForm.teacherType }
    })
    proxy.$modal.msgSuccess('批量修改成功')
    batchDialogVisible.value = false
    getList()
  } catch (error) {
    proxy.$modal.msgError('批量修改失败')
  }
}

function exportData() {
  proxy.download('research/professor/export', { ...queryParams }, '教授师资分类.xlsx')
}

onMounted(async () => {
  try {
    const [deptRes, typeRes] = await Promise.all([
      getDepartmentList(),
      getTeacherTypeDict()
    ])
    departmentList.value = deptRes.data || []
    teacherTypeDict.value = typeRes.data || {}
  } catch (e) {
    console.error(e)
  }
  getList()
})
</script>

<style scoped>
.filter-form {
  margin-bottom: 10px;
}

.count-card {
  text-align: center;
  margin-bottom: 10px;
}

.count-content {
  padding: 15px 0;
}

.count-value {
  font-size: 36px;
  font-weight: bold;
  color: #303133;
}

.count-label {
  font-size: 14px;
  color: #606266;
  margin-top: 5px;
}

.type-1 .count-value { color: #409EFF; }
.type-2 .count-value { color: #67C23A; }
.type-3 .count-value { color: #E6A23C; }
.type-4 .count-value { color: #909399; }
</style>
