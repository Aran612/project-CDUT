<template>
  <div class="app-container">
    <!-- 筛选条件 -->
    <el-form :inline="true" :model="queryParams" class="filter-form">
      <el-form-item label="学期">
        <el-select v-model="queryParams.semester" placeholder="全部学期" clearable style="width: 180px;">
          <el-option v-for="s in semesterList" :key="s" :label="s" :value="s" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="loadData">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 整体指标 -->
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="metric-card" shadow="hover">
          <div class="metric-content">
            <div class="metric-label">整体研究型占比</div>
            <div class="metric-value" :style="{color: overallRatio >= 0.4 ? '#67C23A' : '#F56C6C'}">
              {{ (overallRatio * 100).toFixed(1) }}%
            </div>
            <el-progress
              :percentage="Number((overallRatio * 100).toFixed(1))"
              :color="overallRatio >= 0.4 ? '#67C23A' : '#F56C6C'"
              :stroke-width="16"
            />
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="metric-card" shadow="hover">
          <div class="metric-content">
            <div class="metric-label">达标专业数</div>
            <div class="metric-value">{{ passedDepartments.length }}</div>
            <div class="metric-sub">/ {{ departmentRatio.length }} 个专业</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="metric-card" shadow="hover">
          <div class="metric-content">
            <div class="metric-label">目标要求</div>
            <div class="metric-value" style="color: #E6A23C;">40%</div>
            <div class="metric-sub">研究型学者学分占比</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 告警信息 -->
    <el-alert
      v-if="overallRatio < 0.4 && totalCredits > 0"
      :title="'当前研究型占比为 ' + (overallRatio * 100).toFixed(1) + '%，未达到40%目标'"
      type="error"
      show-icon
      :closable="false"
      style="margin-top: 20px;"
    >
      <template #default>
        <p>建议措施：</p>
        <ul>
          <li>增加研究型学者的教学任务量</li>
          <li>鼓励研究型学者开设更多研究型课程</li>
          <li>适当减少教学型教师的学分要求</li>
        </ul>
      </template>
    </el-alert>

    <!-- 各专业研究型占比详情 -->
    <el-card style="margin-top: 20px;">
      <template #header>
        <span><strong>各专业研究型占比详情</strong></span>
      </template>
      <el-table :data="departmentRatio" v-loading="loading" stripe>
        <el-table-column label="专业" align="center" prop="department" width="180" />
        <el-table-column label="教授总数" align="center" prop="professor_count" width="100" />
        <el-table-column label="研究型教授数" align="center" prop="research_professor_count" width="110" />
        <el-table-column label="总学分" align="center" prop="total_credits" width="100">
          <template #default="scope">
            <el-tag type="warning">{{ scope.row.total_credits }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="研究型学分" align="center" prop="research_credits" width="100">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.research_credits }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="研究型占比" align="center" width="150">
          <template #default="scope">
            <el-progress
              :percentage="Number((scope.row.research_ratio * 100).toFixed(1))"
              :color="scope.row.research_ratio >= 0.4 ? '#67C23A' : '#F56C6C'"
              :stroke-width="14"
              :text-inside="true"
            />
          </template>
        </el-table-column>
        <el-table-column label="与目标差距" align="center" width="120">
          <template #default="scope">
            <span v-if="scope.row.research_ratio >= 0.4" style="color: #67C23A;">已达标</span>
            <span v-else style="color: #F56C6C;">
              差 {{ ((0.4 - scope.row.research_ratio) * 100).toFixed(1) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="90">
          <template #default="scope">
            <el-tag
              :type="scope.row.research_ratio >= 0.4 ? 'success' : 'danger'"
            >
              {{ scope.row.research_ratio >= 0.4 ? '达标' : '未达标' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 师资类型研究型占比 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span><strong>各师资类型的研究型课程占比</strong></span>
          </template>
          <el-table :data="teacherTypeRatio" v-loading="loading" stripe>
            <el-table-column label="师资类型" align="center" prop="teacher_type_name" width="150">
              <template #default="scope">
                <el-tag>{{ scope.row.teacher_type_name }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="教授数" align="center" prop="professor_count" width="100" />
            <el-table-column label="授课次数" align="center" prop="record_count" width="100" />
            <el-table-column label="总学分" align="center" prop="total_credits" width="100">
              <template #default="scope">
                <el-tag type="warning">{{ scope.row.total_credits }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="研究型课程学分" align="center" prop="research_credits" width="120">
              <template #default="scope">
                <el-tag type="success">{{ scope.row.research_credits }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="研究型课程占比" align="center" width="150">
              <template #default="scope">
                <el-progress
                  :percentage="Number((scope.row.research_ratio * 100).toFixed(1))"
                  :color="scope.row.research_ratio >= 0.4 ? '#67C23A' : '#E6A23C'"
                  :stroke-width="14"
                  :text-inside="true"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 未达标专业改进建议 -->
    <el-card v-if="failedDepartments.length > 0" style="margin-top: 20px;" type="warning">
      <template #header>
        <span><strong>未达标专业改进建议</strong></span>
      </template>
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane v-for="item in failedDepartments" :key="item.department" :label="item.department" :name="item.department">
          <div class="suggestion-content">
            <p><strong>{{ item.department }}</strong> 当前研究型占比 <span style="color: #F56C6C; font-weight: bold;">{{ (item.research_ratio * 100).toFixed(1) }}%</span>，距目标40%还差 <span style="color: #F56C6C; font-weight: bold;">{{ ((0.4 - item.research_ratio) * 100).toFixed(1) }}%</span></p>
            <h4>建议措施：</h4>
            <ul>
              <li>研究型教授（{{ item.research_professor_count }}人）需增加教学任务</li>
              <li>鼓励开设更多研究型课程（is_research=1）</li>
              <li>考虑将部分教学任务从教学型教师转移给研究型学者</li>
              <li>每门研究型课程的贡献 = 课程学分 × 学生人数</li>
            </ul>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup name="ResearchRatio">
import { ref, computed, onMounted } from 'vue'
import {
  getResearchRatioByDepartment,
  getResearchRatioByTeacherType,
  getCreditOverview,
  getSemesterList
} from '@/api/research/credit'

const { proxy } = getCurrentInstance()

const loading = ref(false)
const departmentRatio = ref([])
const teacherTypeRatio = ref([])
const overview = ref({})
const semesterList = ref([])
const activeTab = ref('')

const queryParams = ref({
  semester: ''
})

const totalCredits = computed(() => overview.value.total_credits || 0)

const overallRatio = computed(() => {
  if (overview.value.research_ratio != null) {
    return overview.value.research_ratio
  }
  return 0
})

const passedDepartments = computed(() => {
  return departmentRatio.value.filter(item => item.research_ratio >= 0.4)
})

const failedDepartments = computed(() => {
  return departmentRatio.value.filter(item => item.research_ratio < 0.4)
})

async function loadData() {
  loading.value = true
  try {
    const [deptRes, typeRes, overviewRes] = await Promise.all([
      getResearchRatioByDepartment(queryParams.value.semester),
      getResearchRatioByTeacherType(queryParams.value.semester),
      getCreditOverview(queryParams.value.semester)
    ])
    departmentRatio.value = deptRes.data || []
    teacherTypeRatio.value = typeRes.data || []
    overview.value = overviewRes.data || {}
    
    // 设置默认选中的tab
    if (failedDepartments.value.length > 0 && !activeTab.value) {
      activeTab.value = failedDepartments.value[0].department
    }
  } catch (error) {
    proxy.$modal.msgError('加载数据失败')
  } finally {
    loading.value = false
  }
}

function resetQuery() {
  queryParams.value = { semester: '' }
  loadData()
}

onMounted(async () => {
  try {
    const semRes = await getSemesterList()
    semesterList.value = semRes.data || []
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

.metric-card {
  text-align: center;
}

.metric-content {
  padding: 10px 0;
}

.metric-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 10px;
}

.metric-value {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
}

.metric-sub {
  font-size: 12px;
  color: #909399;
}

.suggestion-content {
  line-height: 1.8;
}

.suggestion-content ul {
  padding-left: 20px;
}

.suggestion-content li {
  margin: 5px 0;
}
</style>
