<template>
  <div class="app-container">
    <!-- 筛选条件 -->
    <el-form :inline="true" :model="queryParams" class="filter-form">
      <el-form-item label="学期">
        <el-select v-model="queryParams.semester" placeholder="全部学期" clearable style="width: 180px;">
          <el-option v-for="s in semesterList" :key="s" :label="s" :value="s" />
        </el-select>
      </el-form-item>
      <el-form-item label="师资类型">
        <el-select v-model="queryParams.teacherType" placeholder="全部类型" clearable style="width: 180px;">
          <el-option v-for="(name, key) in teacherTypeDict" :key="key" :label="name" :value="key" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 专业汇总表 -->
    <el-card>
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span><strong>专业学分汇总</strong></span>
          <el-button type="success" @click="exportData">导出数据</el-button>
        </div>
      </template>
      <el-table :data="departmentStats" v-loading="loading" stripe border>
        <el-table-column label="专业/院系" align="center" prop="department" width="180" fixed />
        <el-table-column label="教授数" align="center" prop="professor_count" width="90" />
        <el-table-column label="授课次数" align="center" prop="record_count" width="90" />
        <el-table-column label="学生总数" align="center" prop="total_students" width="90" />
        <el-table-column label="总学分" align="center" prop="total_credits" width="100">
          <template #default="scope">
            <el-tag type="warning" style="font-weight: bold;">{{ scope.row.total_credits }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="研究型学者学分" align="center" width="120">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.research_professor_credits || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="教学型教师学分" align="center" width="120">
          <template #default="scope">
            <el-tag>{{ scope.row.teaching_professor_credits || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="双师型教师学分" align="center" width="120">
          <template #default="scope">
            <el-tag type="info">{{ scope.row.dual_professor_credits || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="行政兼职学分" align="center" width="120">
          <template #default="scope">
            <el-tag type="warning" effect="plain">{{ scope.row.admin_professor_credits || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="研究型占比" align="center" width="130">
          <template #default="scope">
            <el-progress
              v-if="scope.row.total_credits > 0"
              :percentage="Number(((scope.row.research_professor_credits || 0) / scope.row.total_credits * 100).toFixed(1))"
              :color="((scope.row.research_professor_credits || 0) / scope.row.total_credits) >= 0.4 ? '#67C23A' : '#F56C6C'"
              :stroke-width="14"
              :text-inside="true"
            />
            <span v-else>0%</span>
          </template>
        </el-table-column>
        <el-table-column label="是否达标" align="center" width="90">
          <template #default="scope">
            <el-tag
              v-if="scope.row.total_credits > 0"
              :type="((scope.row.research_professor_credits || 0) / scope.row.total_credits) >= 0.4 ? 'success' : 'danger'"
            >
              {{ ((scope.row.research_professor_credits || 0) / scope.row.total_credits) >= 0.4 ? '达标' : '未达标' }}
            </el-tag>
            <el-tag v-else type="info">-</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 各师资类型学分汇总 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span><strong>各专业师资类型学分对比</strong></span>
          </template>
          <el-table :data="departmentStats" v-loading="loading" stripe max-height="400">
            <el-table-column label="专业" align="center" prop="department" width="160" fixed />
            <el-table-column label="研究型" align="center" width="100">
              <template #default="scope">
                <span style="color: #67C23A; font-weight: bold;">{{ scope.row.research_professor_credits || 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="教学型" align="center" width="100">
              <template #default="scope">
                <span>{{ scope.row.teaching_professor_credits || 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="双师型" align="center" width="100">
              <template #default="scope">
                <span style="color: #909399;">{{ scope.row.dual_professor_credits || 0 }}</span>
              </template>
            </el-table-column>
            <el-table-column label="行政兼职" align="center" width="100">
              <template #default="scope">
                <span style="color: #E6A23C;">{{ scope.row.admin_professor_credits || 0 }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span><strong>专业达标情况统计</strong></span>
          </template>
          <div v-loading="loading">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="总专业数">{{ departmentStats.length }}</el-descriptions-item>
              <el-descriptions-item label="达标专业数">
                <span style="color: #67C23A; font-weight: bold;">{{ passedCount }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="未达标专业数">
                <span style="color: #F56C6C; font-weight: bold;">{{ departmentStats.length - passedCount }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="整体达标率">
                <el-progress
                  :percentage="departmentStats.length > 0 ? Number((passedCount / departmentStats.length * 100).toFixed(1)) : 0"
                  :color="passedCount / (departmentStats.length || 1) >= 0.8 ? '#67C23A' : '#E6A23C'"
                />
              </el-descriptions-item>
              <el-descriptions-item label="目标要求">研究型学者学分占比 ≥ 40%</el-descriptions-item>
            </el-descriptions>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="MajorSummary">
import { ref, computed, onMounted } from 'vue'
import {
  getCreditByDepartment,
  getSemesterList,
  getTeacherTypeDict
} from '@/api/research/credit'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const departmentStats = ref([])
const semesterList = ref([])
const teacherTypeDict = ref({})

const queryParams = ref({
  semester: '',
  teacherType: ''
})

const passedCount = computed(() => {
  return departmentStats.value.filter(item => {
    return item.total_credits > 0 && (item.research_professor_credits || 0) / item.total_credits >= 0.4
  }).length
})

async function loadData() {
  loading.value = true
  try {
    const res = await getCreditByDepartment(
      queryParams.value.semester,
      queryParams.value.teacherType
    )
    departmentStats.value = res.data || []
  } catch (error) {
    proxy.$modal.msgError('加载数据失败')
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  queryParams.value = { semester: '', teacherType: '' }
  loadData()
}

function exportData() {
  proxy.$modal.msgSuccess('导出功能待实现')
}

onMounted(async () => {
  try {
    const [semRes, typeRes] = await Promise.all([
      getSemesterList(),
      getTeacherTypeDict()
    ])
    semesterList.value = semRes.data || []
    teacherTypeDict.value = typeRes.data || {}
  } catch (e) {
    console.error(e)
  }
  loadData()
})
</script>

<style scoped>
.filter-form {
  margin-bottom: 10px;
}
</style>
