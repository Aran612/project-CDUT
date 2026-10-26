<template>
  <div class="app-container" v-loading="loading">
    <!-- 顶部：项目选择 -->
    <el-card shadow="never" class="mb8">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="学位项目">
          <el-select v-model="currentProgramId" placeholder="请选择学位项目" filterable style="width: 320px" @change="loadData">
            <el-option v-for="p in programList" :key="p.programId" :value="p.programId"
              :label="p.programName + (p.programNameEn ? ' (' + p.programNameEn + ')' : '')" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图谱课程 × 委员会成员（每门课 1-3 名教师） -->
    <el-card shadow="never" class="mb8">
      <template #header>
        <span class="card-title">课程 AoL 委员会 Course Committee（每门课程配置 1-3 名教师成员）</span>
      </template>
      <el-empty v-if="courseRows.length === 0" description="该项目暂无图谱课程，请先在【课程图谱】中新增课程" />
      <el-table v-else :data="courseRows" border>
        <el-table-column label="课程名称" align="center" prop="courseName" min-width="200" show-overflow-tooltip />
        <el-table-column label="课程名称（英文）" align="center" prop="courseNameEn" min-width="200" show-overflow-tooltip />
        <el-table-column v-for="order in [1, 2, 3]" :key="order" :label="'委员 ' + order" align="center" min-width="200">
          <template #default="scope">
            <el-select
              :model-value="memberProfessorId(scope.row, order)"
              placeholder="选择教师"
              filterable
              clearable
              style="width: 100%"
              @update:model-value="val => handleMemberChange(scope.row, order, val)"
            >
              <el-option v-for="t in teacherList" :key="t.professorId" :label="teacherLabel(t)" :value="t.professorId" />
            </el-select>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 填表说明 -->
    <el-alert type="info" show-icon :closable="false"
      title="填表说明：每门进入图谱的课程配置 1-3 名 AoL 委员会成员（授课教师或课程负责人），下拉修改后自动保存。" />
  </div>
</template>

<script setup name="AolCommittee">
import { listProgram, listMap, listCommittee, addCommittee, updateCommittee, delCommittee, getTeachers } from "@/api/aol/aol"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const programList = ref([])
const currentProgramId = ref(null)
const courseRows = ref([])
const committeeList = ref([])
const teacherList = ref([])

// ---------- 数据加载 ----------
function loadPrograms() {
  listProgram().then(res => {
    programList.value = res.rows || res.data || []
    if (!currentProgramId.value && programList.value.length > 0) {
      currentProgramId.value = programList.value[0].programId
    }
    loadData()
  })
}

function loadData() {
  courseRows.value = []
  committeeList.value = []
  if (!currentProgramId.value) return
  loading.value = true
  Promise.all([
    listMap(currentProgramId.value).then(res => res.rows || res.data || []),
    listCommittee(currentProgramId.value).then(res => res.rows || res.data || []),
    getTeachers().then(res => res.data || res.rows || [])
  ]).then(([courses, committees, teachers]) => {
    courseRows.value = courses
    committeeList.value = committees
    teacherList.value = teachers
  }).finally(() => {
    loading.value = false
  })
}

// 教师选项文案：姓名(工号)
function teacherLabel(t) {
  return t.cdutId ? `${t.name}(${t.cdutId})` : (t.name || t.professorId)
}

// 取某课程某顺序的委员会成员（member_order 1-3）
function memberOf(row, order) {
  return committeeList.value.find(c => String(c.mapCourseId) === String(row.mapCourseId) && Number(c.memberOrder) === order)
}

function memberProfessorId(row, order) {
  const m = memberOf(row, order)
  return m ? m.professorId : undefined
}

// 行内变更：无记录→新增；已有记录→有值更新/清空删除
function handleMemberChange(row, order, val) {
  const existing = memberOf(row, order)
  if (!val) {
    if (existing) {
      delCommittee(existing.committeeId).then(() => {
        proxy.$modal.msgSuccess("已移除该委员")
        reloadCommittee()
      })
    }
    return
  }
  if (existing) {
    updateCommittee({ committeeId: existing.committeeId, programId: currentProgramId.value, mapCourseId: row.mapCourseId, professorId: val, memberOrder: order }).then(() => {
      reloadCommittee()
    })
  } else {
    addCommittee({ programId: currentProgramId.value, mapCourseId: row.mapCourseId, professorId: val, memberOrder: order }).then(() => {
      proxy.$modal.msgSuccess("已添加委员")
      reloadCommittee()
    })
  }
}

// 静默刷新委员会数据（不整页 loading）
function reloadCommittee() {
  listCommittee(currentProgramId.value).then(res => {
    committeeList.value = res.rows || res.data || []
  })
}

loadPrograms()
</script>

<style scoped lang="scss">
.card-title {
  font-weight: 600;
}
</style>
