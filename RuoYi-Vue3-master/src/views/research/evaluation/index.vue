<template>
  <div class="app-container">
    <el-alert
      title="研究型学者评估标准：教授提供的研究型课程积分 占 全体学生总学分 ≥ 40%"
      type="warning"
      show-icon
      :closable="false"
      style="margin-bottom: 15px;"
    />

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #409EFF;"><el-icon :size="32"><DataAnalysis /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ result.totalStudentCredits || 0 }}</div>
              <div class="stat-label">全体学生总学分</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #67C23A;"><el-icon :size="32"><Medal /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ result.qualifiedCount || 0 }}</div>
              <div class="stat-label">合格研究型学者数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" style="background: #F56C6C;"><el-icon :size="32"><Warning /></el-icon></el-icon>
            <div class="stat-info">
              <div class="stat-value">{{ (result.totalCount || 0) - (result.qualifiedCount || 0) }}</div>
              <div class="stat-label">未达标教授数</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px;">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <strong>研究型学者评估明细</strong>
          <div style="display: flex; gap: 10px; align-items: center;">
            <el-select v-model="selectedSemester" placeholder="选择学期" clearable style="width: 180px;">
              <el-option v-for="s in semesterList" :key="s" :label="s" :value="s" />
            </el-select>
            <el-button type="primary" @click="loadData" :loading="loading">刷新评估</el-button>
          </div>
        </div>
      </template>

      <el-table :data="evaluations" v-loading="loading" stripe>
        <el-table-column label="教授姓名" align="center" prop="professor_name" width="120" />
        <el-table-column label="研究型积分" align="center" prop="research_score" width="120">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.research_score }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="总授课积分" align="center" prop="total_score" width="120">
          <template #default="scope">
            <el-tag>{{ scope.row.total_score }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="研究型占比" align="center" width="200">
          <template #default="scope">
            <div style="display: flex; align-items: center; gap: 10px;">
              <el-progress
                :percentage="Math.min(Number(scope.row.researchRatioPercent || 0), 100)"
                :color="getProgressColor(scope.row.researchRatioPercent)"
                :stroke-width="16"
                style="flex: 1;"
              />
              <span style="min-width: 65px; text-align: right; font-weight: bold;">
                {{ scope.row.researchRatioPercent }}%
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评估状态" align="center" width="120">
          <template #default="scope">
            <el-tag :type="scope.row.isResearch ? 'success' : 'danger'" effect="dark">
              {{ scope.row.evaluationStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template #default="scope">
            <el-button link type="primary" @click="showDetail(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 单项评估对话框 -->
    <el-dialog title="研究型学者单项评估" v-model="detailOpen" width="480px" append-to-body>
      <el-descriptions v-if="detailResult.professorName" :column="1" border>
        <el-descriptions-item label="教授姓名">{{ detailResult.professorName }}</el-descriptions-item>
        <el-descriptions-item label="研究型积分">
          <span style="color: #67C23A; font-weight: bold; font-size: 18px;">{{ detailResult.researchScore }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="全体学生总学分">
          <span>{{ detailResult.totalStudentCredits }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="研究型占比">
          <el-tag :type="detailResult.isResearch ? 'success' : 'danger'" style="font-size: 16px;">
            {{ detailResult.researchRatioPercent }}%
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="评估标准">≥ 40%</el-descriptions-item>
        <el-descriptions-item label="评估结论">
          <el-tag :type="detailResult.isResearch ? 'success' : 'danger'" effect="dark" style="font-size: 16px;">
            {{ detailResult.evaluationStatus }}
          </el-tag>
        </el-descriptions-item>
      </el-descriptions>
      <el-empty v-else description="暂无数据" />
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailOpen = false">关 闭</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Evaluation">
import { getEvaluationResult } from "@/api/research/evaluation"
import { getSemesterStats } from "@/api/research/teachingStats"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const result = ref({})
const evaluations = ref([])
const semesterList = ref([])
const selectedSemester = ref('')
const detailOpen = ref(false)
const detailResult = ref({})

function getProgressColor(percent) {
  const val = Number(percent || 0)
  if (val >= 40) return '#67C23A'
  if (val >= 20) return '#E6A23C'
  return '#F56C6C'
}

function loadData() {
  loading.value = true
  getEvaluationResult(selectedSemester.value).then(response => {
    result.value = response.data
    evaluations.value = response.data?.professorEvaluations || []
  }).finally(() => {
    loading.value = false
  })
}

function loadSemesters() {
  getSemesterStats().then(response => {
    semesterList.value = response.data?.map(s => s.semester) || []
  })
}

function showDetail(row) {
  detailResult.value = {
    professorName: row.professor_name,
    researchScore: row.research_score,
    totalStudentCredits: result.value.totalStudentCredits,
    researchRatioPercent: row.researchRatioPercent,
    isResearch: row.isResearch,
    evaluationStatus: row.evaluationStatus
  }
  detailOpen.value = true
}

watch(selectedSemester, () => {
  loadData()
})

loadSemesters()
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
  width: 55px;
  height: 55px;
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
  font-size: 26px;
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
