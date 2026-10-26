<template>
  <div class="app-container" v-loading="loading">
    <!-- 顶部：项目选择与导出 -->
    <el-card shadow="never" class="mb8">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="学位项目">
          <el-select v-model="currentProgramId" placeholder="请选择学位项目" filterable style="width: 320px" @change="handleProgramChange">
            <el-option v-for="p in programList" :key="p.programId" :value="p.programId"
              :label="p.programName + (p.programNameEn ? ' (' + p.programNameEn + ')' : '')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" plain icon="Plus" :disabled="!currentProgramId" @click="openCourseDialog()">新增课程</el-button>
          <el-button type="warning" plain icon="Download" :disabled="!currentProgramId" @click="handleExportExcel">导出 Excel</el-button>
          <el-button type="warning" plain icon="Download" :disabled="!currentProgramId" @click="handleExportWord">导出 Word</el-button>
          <el-button type="warning" plain icon="Document" :disabled="!currentCourse" @click="handleExportSyllabus">导出课程 Syllabus</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 课程行表格 -->
    <el-card shadow="never" class="mb8">
      <template #header>
        <div class="card-header">
          <span class="card-title">图谱课程 Courses（点击行选中后可导出 Syllabus）</span>
        </div>
      </template>
      <el-table :data="courseRows" border highlight-current-row @current-change="handleCourseCurrentChange">
        <el-table-column label="课程名称" align="center" prop="courseName" min-width="200" show-overflow-tooltip />
        <el-table-column label="课程名称（英文）" align="center" prop="courseNameEn" min-width="220" show-overflow-tooltip />
        <el-table-column label="年级 Year" align="center" prop="yearLevel" width="90">
          <template #default="scope">
            <span>{{ scope.row.yearLevel ? '第 ' + scope.row.yearLevel + ' 学年' : '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="性质 Kind" align="center" prop="courseKind" width="110">
          <template #default="scope">
            <el-tag :type="{ Core: 'primary', Elective: 'info', Capstone: 'warning' }[scope.row.courseKind] || 'info'" v-if="scope.row.courseKind">
              {{ scope.row.courseKind }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="主要支撑 LGs" align="center" prop="primaryLgs" min-width="160">
          <template #default="scope">
            <el-tag v-for="code in (scope.row.primaryLgs || '').split(',').filter(c => c)" :key="code" size="small" class="cell-tag" type="success">
              {{ code }}
            </el-tag>
            <span v-if="!scope.row.primaryLgs">-</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="140">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="openCourseDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDeleteCourse(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 覆盖矩阵：全部课程行 × LG 列（I/D/M） -->
    <el-card shadow="never">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            覆盖矩阵 Coverage Matrix
            <span class="matrix-tip">空 = 无覆盖；I = 引入 Introduce；D = 发展 Develop；M = 精通 Master</span>
          </span>
        </div>
      </template>
      <el-empty v-if="courseRows.length === 0" description="暂无图谱课程，请先新增课程" />
      <el-table v-else :data="courseRows" border size="small">
        <el-table-column label="课程" align="center" prop="courseName" fixed="left" min-width="180" show-overflow-tooltip />
        <el-table-column v-for="lg in allLgs" :key="lg.lgId" align="center" width="72">
          <template #header>
            <el-tooltip :content="lg.description" placement="top">
              <span class="lg-header">{{ lg.lgCode }}</span>
            </el-tooltip>
          </template>
          <template #default="scope">
            <el-select
              v-model="scope.row.cells[lg.lgId]"
              placeholder=""
              size="small"
              style="width: 62px"
              clearable
            >
              <el-option v-for="opt in coverageOptions" :key="opt.value" :label="opt.value" :value="opt.value" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="120">
          <template #default="scope">
            <el-button link type="primary" icon="Check" @click="handleSaveCells(scope.row)">保存覆盖格</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 图谱课程新增/编辑对话框 -->
    <el-dialog :title="courseTitle" v-model="courseOpen" width="640px" append-to-body>
      <el-form ref="courseRef" :model="courseForm" :rules="courseRules" label-width="130px">
        <el-form-item label="选择课程" prop="courseId">
          <el-select v-model="courseForm.courseId" placeholder="从课程库选择（自动反填课程名）" filterable clearable style="width: 100%" @change="onCoursePicked">
            <el-option v-for="c in courseOptions" :key="c.courseId" :label="c.courseName" :value="c.courseId" />
          </el-select>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="courseForm.courseName" placeholder="课程名称（中文）" />
        </el-form-item>
        <el-form-item label="课程名称(英文)" prop="courseNameEn">
          <el-input v-model="courseForm.courseNameEn" placeholder="Course Name in English" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="年级 Year" prop="yearLevel">
              <el-select v-model="courseForm.yearLevel" placeholder="请选择开课年级" style="width: 100%">
                <el-option label="第 1 学年" value="1" />
                <el-option label="第 2 学年" value="2" />
                <el-option label="第 3 学年" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性质 Kind" prop="courseKind">
              <el-select v-model="courseForm.courseKind" placeholder="请选择课程性质" style="width: 100%">
                <el-option label="Core 必修" value="Core" />
                <el-option label="Elective 选修" value="Elective" />
                <el-option label="Capstone 顶点" value="Capstone" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="主要支撑 LGs" prop="primaryLgsArray">
          <el-select v-model="courseForm.primaryLgsArray" multiple filterable placeholder="选择该课程主要支撑的学习目标" style="width: 100%">
            <el-option v-for="lg in allLgs" :key="lg.lgId" :label="lg.lgCode + ' ' + (lg.description || '')" :value="lg.lgCode" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitCourse">确 定</el-button>
        <el-button @click="courseOpen = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AolCourseMap">
import { listProgram, listCg, listLg, listMap, addMap, updateMap, delMap, saveCells } from "@/api/aol/aol"
import { listAllCourse } from "@/api/research/course"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const programList = ref([])
const currentProgramId = ref(null)
const currentProgram = computed(() => programList.value.find(p => p.programId === currentProgramId.value))

const courseRows = ref([]) // 图谱课程行（含 cells: {lgId: coverageLevel}）
const allLgs = ref([]) // 项目的全部 LG（矩阵动态列头 lg_code）
const courseOptions = ref([]) // 课程库选项
const currentCourse = ref(null) // 当前选中课程行（用于 Syllabus 导出）

// 覆盖层级：I 引入 / D 发展 / M 精通
const coverageOptions = [
  { value: "I", label: "I 引入 Introduce" },
  { value: "D", label: "D 发展 Develop" },
  { value: "M", label: "M 精通 Master" }
]

// ---------- 数据加载 ----------
function loadPrograms() {
  listProgram().then(res => {
    programList.value = res.rows || res.data || []
    if (!currentProgramId.value && programList.value.length > 0) {
      currentProgramId.value = programList.value[0].programId
    }
    loadMapData()
  })
}

// 加载项目的全部 LG（逐 CG 拉取后扁平化）
function loadAllLgs() {
  allLgs.value = []
  if (!currentProgramId.value) return Promise.resolve()
  return listCg(currentProgramId.value).then(res => {
    const cgs = res.rows || res.data || []
    return Promise.all(cgs.map(cg => listLg(cg.cgId).then(r => r.rows || r.data || [])))
  }).then(lists => {
    allLgs.value = lists.flat()
  })
}

// 加载图谱课程行，并将覆盖格归一化为 {lgId: level} 映射
function loadMapData() {
  courseRows.value = []
  if (!currentProgramId.value) return Promise.resolve()
  return loadAllLgs().then(() => {
    return listMap(currentProgramId.value).then(res => {
      const rows = res.rows || res.data || []
      courseRows.value = rows.map(row => {
        const cells = {}
        if (Array.isArray(row.cells)) {
          row.cells.forEach(c => { cells[c.lgId] = c.coverageLevel || "" })
        }
        return { ...row, cells }
      })
      // 选中行若仍存在则保留
      if (currentCourse.value && !courseRows.value.find(r => r.mapCourseId === currentCourse.value.mapCourseId)) {
        currentCourse.value = null
      }
    })
  })
}

function handleProgramChange() {
  currentCourse.value = null
  loadMapData()
}

// ---------- 课程行 CRUD ----------
const courseOpen = ref(false)
const courseTitle = ref("")
const courseForm = reactive({
  mapCourseId: null,
  courseId: null,
  courseName: "",
  courseNameEn: "",
  yearLevel: "",
  courseKind: "",
  primaryLgsArray: []
})
const courseRules = {
  courseName: [{ required: true, message: "课程名称不能为空", trigger: "blur" }]
}

function openCourseDialog(row) {
  courseForm.mapCourseId = row ? row.mapCourseId : null
  courseForm.courseId = row ? (row.courseId || null) : null
  courseForm.courseName = row ? row.courseName : ""
  courseForm.courseNameEn = row ? row.courseNameEn : ""
  courseForm.yearLevel = row ? row.yearLevel : ""
  courseForm.courseKind = row ? row.courseKind : ""
  courseForm.primaryLgsArray = row ? (row.primaryLgs || "").split(",").map(s => s.trim()).filter(s => s) : []
  courseTitle.value = row ? "编辑图谱课程" : "新增图谱课程"
  courseOpen.value = true
  // 加载课程库选项（用于反填课程名称）
  listAllCourse().then(res => {
    courseOptions.value = res.data || res.rows || []
  }).catch(() => {
    courseOptions.value = []
  })
}

// 选择课程库课程后反填课程名称
function onCoursePicked(courseId) {
  const c = courseOptions.value.find(item => item.courseId === courseId)
  if (c) courseForm.courseName = c.courseName
}

function submitCourse() {
  proxy.$refs.courseRef.validate(valid => {
    if (!valid) return
    const payload = {
      mapCourseId: courseForm.mapCourseId,
      programId: currentProgramId.value,
      courseId: courseForm.courseId,
      courseName: courseForm.courseName,
      courseNameEn: courseForm.courseNameEn,
      yearLevel: courseForm.yearLevel,
      courseKind: courseForm.courseKind,
      primaryLgs: courseForm.primaryLgsArray.join(",")
    }
    const fn = courseForm.mapCourseId ? updateMap : addMap
    fn(payload).then(() => {
      proxy.$modal.msgSuccess("保存成功")
      courseOpen.value = false
      loadMapData()
    })
  })
}

function handleDeleteCourse(row) {
  proxy.$modal.confirm(`是否确认删除图谱课程「${row.courseName}」？该课程的覆盖格与委员会配置将一并失效。`).then(() => {
    return delMap(row.mapCourseId)
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功")
    if (currentCourse.value && currentCourse.value.mapCourseId === row.mapCourseId) {
      currentCourse.value = null
    }
    loadMapData()
  }).catch(() => {})
}

// ---------- 覆盖矩阵 ----------
function handleCourseCurrentChange(row) {
  currentCourse.value = row || null
}

// 保存某课程行的全部覆盖格：PUT /aol/map/cells
function handleSaveCells(row) {
  const cells = allLgs.value.map(lg => ({
    lgId: lg.lgId,
    coverageLevel: row.cells[lg.lgId] || ""
  }))
  saveCells({ mapCourseId: row.mapCourseId, cells: cells }).then(() => {
    proxy.$modal.msgSuccess(`「${row.courseName}」覆盖格保存成功`)
  })
}

// ---------- 导出 ----------
function handleExportExcel() {
  proxy.download("aol/export/mapExcel", { programId: currentProgramId.value }, `课程图谱_${currentProgram.value?.programName || currentProgramId.value}_${new Date().getTime()}.xlsx`)
}

function handleExportWord() {
  proxy.download("aol/export/mapWord", { programId: currentProgramId.value }, `课程图谱_${currentProgram.value?.programName || currentProgramId.value}_${new Date().getTime()}.docx`)
}

function handleExportSyllabus() {
  proxy.download("aol/export/syllabus", { mapCourseId: currentCourse.value.mapCourseId }, `Syllabus_${currentCourse.value.courseName || currentCourse.value.mapCourseId}_${new Date().getTime()}.docx`)
}

loadPrograms()
</script>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title {
  font-weight: 600;
}
.matrix-tip {
  font-weight: normal;
  font-size: 12px;
  color: #909399;
  margin-left: 12px;
}
.lg-header {
  font-weight: 600;
  cursor: help;
}
.cell-tag {
  margin: 2px;
}
</style>
