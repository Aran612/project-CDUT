<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;"><el-icon :size="36"><DataAnalysis /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalStudents || 0 }}</div>
              <div class="stat-label">全体学生总学分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;"><el-icon :size="36"><User /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalProfessors || 0 }}</div>
              <div class="stat-label">授课教授数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #E6A23C;"><el-icon :size="36"><Notebook /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalCourses || 0 }}</div>
              <div class="stat-label">开设课程数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;"><el-icon :size="36"><TrendCharts /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ overview.totalRecords || 0 }}</div>
              <div class="stat-label">授课记录数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span><strong>各学期授课积分统计</strong></span>
          </template>
          <el-table :data="semesterStats" stripe>
            <el-table-column label="学期" align="center" prop="semester" width="150" />
            <el-table-column label="记录数" align="center" prop="record_count" width="100" />
            <el-table-column label="学生总数" align="center" prop="total_students" width="100" />
            <el-table-column label="总积分" align="center" prop="total_score">
              <template #default="scope">
                <el-tag type="warning" style="font-weight: bold;">{{ scope.row.total_score }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <span><strong>课程类型分布</strong></span>
          </template>
          <el-table :data="courseTypeStats" stripe>
            <el-table-column label="类型" align="center" prop="course_type" width="120">
              <template #default="scope">
                <el-tag>{{ courseTypeLabel(scope.row.course_type) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="课程数" align="center" prop="course_count" width="100" />
            <el-table-column label="学分合计" align="center" prop="total_credits">
              <template #default="scope">
                <el-tag type="warning">{{ scope.row.total_credits }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px;">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span><strong>教授授课积分排行</strong></span>
          <el-select v-model="selectedSemester" placeholder="选择学期" clearable style="width: 180px;">
            <el-option v-for="s in semesterList" :key="s" :label="s" :value="s" />
          </el-select>
        </div>
      </template>
      <el-table :data="professorStats" v-loading="loading" stripe>
        <el-table-column label="排名" align="center" width="80">
          <template #default="scope">
            <span :style="{color: scope.$index < 3 ? '#E6A23C' : '#606266', fontWeight: scope.$index < 3 ? 'bold' : 'normal'}">
              {{ scope.$index + 1 }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="教授姓名" align="center" prop="professor_name" width="120" />
        <el-table-column label="所属院系" align="center" prop="department" width="180" show-overflow-tooltip />
        <el-table-column label="职称" align="center" prop="title" width="120" />
        <el-table-column label="授课次数" align="center" prop="record_count" width="100" />
        <el-table-column label="学生总数" align="center" prop="total_students" width="100" />
        <el-table-column label="授课总积分" align="center" prop="total_score">
          <template #default="scope">
            <el-tag type="warning" style="font-weight: bold;">{{ scope.row.total_score }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="TeachingStats">
import { getOverview, getProfessorStats, getSemesterStats } from "@/api/research/teachingStats"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const overview = ref({})
const semesterStats = ref([])
const courseTypeStats = ref([])
const professorStats = ref([])
const semesterList = ref([])
const selectedSemester = ref('')

function courseTypeLabel(type) {
  if (type === '0') return '必修课'
  if (type === '1') return '选修课'
  if (type === '2') return '公共课'
  return '-'
}

function loadData() {
  loading.value = true
  Promise.all([
    getOverview(selectedSemester.value),
    getSemesterStats(),
    getProfessorStats(selectedSemester.value)
  ]).then(([overviewRes, semesterRes, professorRes]) => {
    overview.value = {
      totalStudents: overviewRes.data?.semesterStats?.reduce((sum, s) => sum + Number(s.total_students || 0), 0) || 0,
      totalProfessors: professorRes.data?.length || 0,
      totalCourses: overviewRes.data?.courseTypeStats?.reduce((sum, c) => sum + Number(c.course_count || 0), 0) || 0,
      totalRecords: overviewRes.data?.semesterStats?.reduce((sum, s) => sum + Number(s.record_count || 0), 0) || 0
    }
    semesterStats.value = semesterRes.data || []
    courseTypeStats.value = overviewRes.data?.courseTypeStats || []
    professorStats.value = professorRes.data || []
    semesterList.value = semesterRes.data?.map(s => s.semester) || []
    loading.value = false
  }).catch(() => {
    loading.value = false
  })
}

watch(selectedSemester, () => {
  loadData()
})

loadData()
</script>

<style scoped>
.stat-card {
  height: 110px;
}
.stat-content {
  display: flex;
  align-items: center;
}
.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  margin-right: 15px;
  flex-shrink: 0;
}
.stat-info {
  flex: 1;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}
.stat-label {
  font-size: 13px;
  color: #909399;
  margin-top: 6px;
}
</style>
