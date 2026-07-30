<template>
  <div class="app-container">
    <!-- 筛选条件 -->
    <el-form :inline="true" :model="queryParams" class="filter-form">
      <el-form-item label="学期">
        <el-select v-model="queryParams.semester" placeholder="全部学期" clearable style="width: 180px;">
          <el-option v-for="s in semesterList" :key="s" :label="s" :value="s" />
        </el-select>
      </el-form-item>
      <el-form-item label="院系">
        <el-select v-model="queryParams.department" placeholder="全部院系" clearable style="width: 180px;">
          <el-option v-for="d in departmentList" :key="d" :label="d" :value="d" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 总览卡片 -->
    <el-row :gutter="20">
      <el-col :span="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;"><el-icon :size="28"><User /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.total_professors || 0 }}</div>
              <div class="stat-label">授课教授数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;"><el-icon :size="28"><Notebook /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.total_courses || 0 }}</div>
              <div class="stat-label">开设课程数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C;"><el-icon :size="28"><DataAnalysis /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.total_students || 0 }}</div>
              <div class="stat-label">覆盖学生数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;"><el-icon :size="28"><TrendCharts /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.total_credits || 0 }}</div>
              <div class="stat-label">总学分数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{background: overview.is_target_met ? '#67C23A' : '#F56C6C'}">
              <el-icon :size="28"><Medal /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ researchRatioPercent }}</div>
              <div class="stat-label">研究型占比 (目标40%)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="4">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #909399;"><el-icon :size="28"><InfoFilled /></el-icon></div>
            <div class="stat-info">
              <div class="stat-value">{{ overview.total_records || 0 }}</div>
              <div class="stat-label">授课记录数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 研究型占比告警 -->
    <el-alert
      v-if="overview.is_target_met === false && overview.total_credits > 0"
      title="研究型学者学分占比未达到40%目标"
      type="warning"
      show-icon
      :closable="false"
      style="margin-top: 20px;"
      description="当前占比：{{ researchRatioPercent }}，建议提升研究型学者的教学任务量"
    />

    <!-- 师资类型学分统计 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span><strong>师资类型学分分布</strong></span>
          </template>
          <el-table :data="teacherTypeStats" v-loading="loading" stripe>
            <el-table-column label="师资类型" align="center" prop="teacher_type_name" width="120">
              <template #default="scope">
                <el-tag :type="getTeacherTypeTag(scope.row.teacher_type)">{{ scope.row.teacher_type_name }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="教授数" align="center" prop="professor_count" width="100" />
            <el-table-column label="授课次数" align="center" prop="record_count" width="100" />
            <el-table-column label="学生总数" align="center" prop="total_students" width="100" />
            <el-table-column label="总学分" align="center" prop="total_credits">
              <template #default="scope">
                <el-tag type="warning">{{ scope.row.total_credits }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span><strong>研究型占比分析（按师资类型）</strong></span>
          </template>
          <el-table :data="researchByType" v-loading="loading" stripe>
            <el-table-column label="师资类型" align="center" prop="teacher_type_name" width="120">
              <template #default="scope">
                <el-tag :type="getTeacherTypeTag(scope.row.teacher_type)">{{ scope.row.teacher_type_name }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="教授数" align="center" prop="professor_count" width="100" />
            <el-table-column label="总学分" align="center" prop="total_credits" width="100" />
            <el-table-column label="研究型学分" align="center" prop="research_credits">
              <template #default="scope">
                <el-tag type="success">{{ scope.row.research_credits }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="研究型占比" align="center" width="120">
              <template #default="scope">
                <el-progress
                  :percentage="Number((scope.row.research_ratio * 100).toFixed(1))"
                  :color="scope.row.research_ratio >= 0.4 ? '#67C23A' : '#F56C6C'"
                  :stroke-width="14"
                  :text-inside="true"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 教授学分排行榜 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span><strong>教授学分排行</strong></span>
          <div>
            <el-select v-model="queryParams.teacherType" placeholder="师资类型" clearable style="width: 150px; margin-right: 10px;">
              <el-option v-for="(name, key) in teacherTypeDict" :key="key" :label="name" :value="key" />
            </el-select>
            <el-button type="primary" @click="loadProfessorStats">筛选</el-button>
          </div>
        </div>
      </template>
      <el-table :data="professorStats" v-loading="loading" stripe>
        <el-table-column label="排名" align="center" width="70">
          <template #default="scope">
            <span :style="{color: scope.$index < 3 ? '#E6A23C' : '#606266', fontWeight: scope.$index < 3 ? 'bold' : 'normal'}">
              {{ scope.$index + 1 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="教授姓名" align="center" prop="professor_name" width="120" />
        <el-table-column label="所属院系" align="center" prop="department" width="160" show-overflow-tooltip />
        <el-table-column label="职称" align="center" prop="title" width="100" />
        <el-table-column label="师资类型" align="center" width="110">
          <template #default="scope">
            <el-tag :type="getTeacherTypeTag(scope.row.teacher_type)">{{ scope.row.teacher_type_name }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="授课次数" align="center" prop="record_count" width="90" />
        <el-table-column label="课程数" align="center" prop="course_count" width="90" />
        <el-table-column label="学生总数" align="center" prop="total_students" width="90" />
        <el-table-column label="总学分" align="center" prop="total_credits" width="100">
          <template #default="scope">
            <el-tag type="warning" style="font-weight: bold;">{{ scope.row.total_credits }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="研究型学分" align="center" prop="research_credits" width="100">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.research_credits }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="CreditStats">
import { ref, computed, onMounted } from 'vue'
import {
  getCreditOverview,
  getCreditByTeacherType,
  getResearchRatioByTeacherType,
  getCreditByProfessor,
  getSemesterList,
  getDepartmentList,
  getTeacherTypeDict
} from '@/api/research/credit'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const overview = ref({})
const teacherTypeStats = ref([])
const researchByType = ref([])
const professorStats = ref([])
const semesterList = ref([])
const departmentList = ref([])
const teacherTypeDict = ref({})

const queryParams = ref({
  semester: '',
  department: '',
  teacherType: ''
})

const researchRatioPercent = computed(() => {
  if (overview.value.research_ratio != null) {
    return (overview.value.research_ratio * 100).toFixed(1) + '%'
  }
  return '0%'
})

function getTeacherTypeTag(type) {
  const map = { '1': '', '2': 'success', '3': 'warning', '4': 'info' }
  return map[type] || 'info'
}

async function loadData() {
  loading.value = true
  try {
    const [overviewRes, typeRes, researchRes, profRes] = await Promise.all([
      getCreditOverview(queryParams.value.semester, queryParams.value.department),
      getCreditByTeacherType(queryParams.value.semester, queryParams.value.department),
      getResearchRatioByTeacherType(queryParams.value.semester),
      getCreditByProfessor(queryParams.value.semester, queryParams.value.teacherType, queryParams.value.department)
    ])
    overview.value = overviewRes.data || {}
    teacherTypeStats.value = typeRes.data || []
    researchByType.value = researchRes.data || []
    professorStats.value = profRes.data || []
  } catch (error) {
    proxy.$modal.msgError('加载数据失败')
  } finally {
    loading.value = false
  }
}

async function loadProfessorStats() {
  loading.value = true
  try {
    const res = await getCreditByProfessor(
      queryParams.value.semester,
      queryParams.value.teacherType,
      queryParams.value.department
    )
    professorStats.value = res.data || []
  } catch (error) {
    proxy.$modal.msgError('加载数据失败')
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  queryParams.value = { semester: '', department: '', teacherType: '' }
  loadData()
}

onMounted(async () => {
  try {
    const [semRes, deptRes, typeRes] = await Promise.all([
      getSemesterList(),
      getDepartmentList(),
      getTeacherTypeDict()
    ])
    semesterList.value = semRes.data || []
    departmentList.value = deptRes.data || []
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

.stat-card {
  height: 110px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 12px;
  flex-shrink: 0;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}

.stat-label {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}
</style>
