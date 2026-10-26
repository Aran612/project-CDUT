<template>
  <div class="app-container" v-loading="loading">
    <!-- 顶部：项目选择与维护 -->
    <el-card shadow="never" class="mb8">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="学位项目">
          <el-select v-model="currentProgramId" placeholder="请选择学位项目" filterable style="width: 320px" @change="handleProgramChange">
            <el-option v-for="p in programList" :key="p.programId" :value="p.programId"
              :label="p.programName + (p.programNameEn ? ' (' + p.programNameEn + ')' : '')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" plain icon="Plus" @click="openProgramDialog()">新增项目</el-button>
          <el-button type="success" plain icon="Edit" :disabled="!currentProgram" @click="openProgramDialog(currentProgram)">修改项目</el-button>
          <el-button type="danger" plain icon="Delete" :disabled="!currentProgram" @click="handleDeleteProgram">删除项目</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 能力目标 CG 表 -->
    <el-card shadow="never" class="mb8">
      <template #header>
        <div class="card-header">
          <span class="card-title">能力目标 CG（Competence Goal）</span>
          <el-button type="primary" plain icon="Plus" :disabled="!currentProgramId" @click="openCgDialog()">新增 CG</el-button>
        </div>
      </template>
      <el-table :data="cgList" border highlight-current-row @current-change="handleCgCurrentChange">
        <el-table-column label="编号" align="center" prop="cgCode" width="90">
          <template #default="scope">
            <el-tag type="primary">{{ scope.row.cgCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="名称（中文）" align="center" prop="cgName" min-width="180" show-overflow-tooltip />
        <el-table-column label="名称（英文）" align="center" prop="cgNameEn" min-width="220" show-overflow-tooltip />
        <el-table-column label="描述（中文）" align="center" prop="description" min-width="260" show-overflow-tooltip />
        <el-table-column label="描述（英文）" align="center" prop="descriptionEn" min-width="280" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="openCgDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDeleteCg(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 学习目标 LG 表（隶属于选中 CG） -->
    <el-card shadow="never" class="mb8">
      <template #header>
        <div class="card-header">
          <span class="card-title">
            学习目标 LG（Learning Objective）
            <el-tag v-if="currentCg" type="success" class="ml8">{{ currentCg.cgCode }}：{{ currentCg.cgName }}</el-tag>
          </span>
          <el-button type="primary" plain icon="Plus" :disabled="!currentCg" @click="openLgDialog()">新增 LG</el-button>
        </div>
      </template>
      <el-empty v-if="!currentCg" description="请先在上方选择一个能力目标 CG" />
      <el-table v-else :data="lgList" border>
        <el-table-column label="编号" align="center" prop="lgCode" width="90">
          <template #default="scope">
            <el-tag type="success">{{ scope.row.lgCode }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="描述（中文）" align="center" prop="description" min-width="240" show-overflow-tooltip />
        <el-table-column label="描述（英文）" align="center" prop="descriptionEn" min-width="260" show-overflow-tooltip />
        <el-table-column label="布鲁姆层级 Bloom" align="center" prop="bloomLevel" width="130">
          <template #default="scope">
            <el-tag :type="bloomTagType(scope.row.bloomLevel)">{{ bloomLabel(scope.row.bloomLevel) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="布鲁姆行为动词 Verbs" align="center" prop="bloomVerbs" min-width="180" show-overflow-tooltip />
        <el-table-column label="操作" align="center" width="140">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="openLgDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDeleteLg(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 填表说明 -->
    <el-alert type="info" show-icon :closable="false"
      title="填表说明：本科项目 5-6 个 CG；一般 1 个 CG 对应 2 个 LG；每个 LG 至少对应一门课程（在课程图谱中维护）；编号靠后的课程对应布鲁姆层级逐步升高（L1 记忆 → L6 创造）。" />

    <!-- 学位项目对话框 -->
    <el-dialog :title="programTitle" v-model="programOpen" width="600px" append-to-body>
      <el-form ref="programRef" :model="programForm" :rules="programRules" label-width="110px">
        <el-form-item label="项目名称" prop="programName">
          <el-input v-model="programForm.programName" placeholder="请输入项目名称（中文），如：工商管理学士" />
        </el-form-item>
        <el-form-item label="项目名称(英文)" prop="programNameEn">
          <el-input v-model="programForm.programNameEn" placeholder="如：Business Management (BBM)" />
        </el-form-item>
        <el-form-item label="学位层次" prop="degreeLevel">
          <el-radio-group v-model="programForm.degreeLevel">
            <el-radio value="1">本科</el-radio>
            <el-radio value="2">硕士</el-radio>
            <el-radio value="3">博士</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="programForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitProgram">确 定</el-button>
        <el-button @click="programOpen = false">取 消</el-button>
      </template>
    </el-dialog>

    <!-- CG 对话框 -->
    <el-dialog :title="cgTitle" v-model="cgOpen" width="640px" append-to-body>
      <el-form ref="cgRef" :model="cgForm" :rules="cgRules" label-width="110px">
        <el-row>
          <el-col :span="8">
            <el-form-item label="编号 CG" prop="cgCode">
              <el-input v-model="cgForm.cgCode" placeholder="如：CG1" />
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="名称（中文）" prop="cgName">
              <el-input v-model="cgForm.cgName" placeholder="如：分析与批判性思维" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="名称（英文）" prop="cgNameEn">
          <el-input v-model="cgForm.cgNameEn" placeholder="如：Analytical & Critical Thinking" />
        </el-form-item>
        <el-form-item label="描述（中文）" prop="description">
          <el-input v-model="cgForm.description" type="textarea" :rows="2" placeholder="该能力目标期望毕业生达成的整体能力描述" />
        </el-form-item>
        <el-form-item label="描述（英文）" prop="descriptionEn">
          <el-input v-model="cgForm.descriptionEn" type="textarea" :rows="2" placeholder="Description in English" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitCg">确 定</el-button>
        <el-button @click="cgOpen = false">取 消</el-button>
      </template>
    </el-dialog>

    <!-- LG 对话框 -->
    <el-dialog :title="lgTitle" v-model="lgOpen" width="640px" append-to-body>
      <el-form ref="lgRef" :model="lgForm" :rules="lgRules" label-width="130px">
        <el-form-item label="编号 LG" prop="lgCode">
          <el-input v-model="lgForm.lgCode" placeholder="如：1.1（CG编号.序号）" style="width: 200px" />
        </el-form-item>
        <el-form-item label="布鲁姆层级 Bloom" prop="bloomLevel">
          <el-select v-model="lgForm.bloomLevel" placeholder="请选择布鲁姆认知层级" style="width: 100%">
            <el-option v-for="b in bloomLevels" :key="b.value" :label="b.label" :value="b.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="布鲁姆行为动词" prop="bloomVerbs">
          <el-input v-model="lgForm.bloomVerbs" placeholder="顿号分隔，如：识别、界定、诊断" />
        </el-form-item>
        <el-form-item label="描述（中文）" prop="description">
          <el-input v-model="lgForm.description" type="textarea" :rows="2" placeholder="可观察、可测量的学习目标描述" />
        </el-form-item>
        <el-form-item label="描述（英文）" prop="descriptionEn">
          <el-input v-model="lgForm.descriptionEn" type="textarea" :rows="2" placeholder="Description in English" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitLg">确 定</el-button>
        <el-button @click="lgOpen = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AolProgram">
import { listProgram, addProgram, updateProgram, delProgram, listCg, addCg, updateCg, delCg, listLg, addLg, updateLg, delLg } from "@/api/aol/aol"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const programList = ref([])
const currentProgramId = ref(null)
const currentProgram = computed(() => programList.value.find(p => p.programId === currentProgramId.value))

const cgList = ref([])
const currentCg = ref(null)
const lgList = ref([])

// 布鲁姆认知层级（L1记忆 ~ L6创造）
const bloomLevels = [
  { value: "L1", label: "L1 记忆 Remember" },
  { value: "L2", label: "L2 理解 Understand" },
  { value: "L3", label: "L3 应用 Apply" },
  { value: "L4", label: "L4 分析 Analyze" },
  { value: "L5", label: "L5 评价 Evaluate" },
  { value: "L6", label: "L6 创造 Create" }
]
const bloomLabel = level => (bloomLevels.find(b => b.value === level) || {}).label || (level || "-")
const bloomTagType = level => ({ L1: "info", L2: "info", L3: "", L4: "warning", L5: "danger", L6: "danger" }[level] || "info")

// ---------- 学位项目 ----------
const programOpen = ref(false)
const programTitle = ref("")
const programForm = reactive({ programId: null, programName: "", programNameEn: "", degreeLevel: "1", remark: "" })
const programRules = {
  programName: [{ required: true, message: "项目名称不能为空", trigger: "blur" }]
}

function loadPrograms() {
  loading.value = true
  listProgram().then(res => {
    programList.value = res.rows || res.data || []
    // 默认选中第一个项目
    if (!currentProgramId.value && programList.value.length > 0) {
      currentProgramId.value = programList.value[0].programId
    }
    loadCgs()
  }).finally(() => {
    loading.value = false
  })
}

function openProgramDialog(row) {
  programForm.programId = row ? row.programId : null
  programForm.programName = row ? row.programName : ""
  programForm.programNameEn = row ? row.programNameEn : ""
  programForm.degreeLevel = row ? (row.degreeLevel || "1") : "1"
  programForm.remark = row ? row.remark : ""
  programTitle.value = row ? "修改学位项目" : "新增学位项目"
  programOpen.value = true
}

function submitProgram() {
  proxy.$refs.programRef.validate(valid => {
    if (!valid) return
    const fn = programForm.programId ? updateProgram : addProgram
    fn(programForm).then(() => {
      proxy.$modal.msgSuccess("保存成功")
      programOpen.value = false
      loadPrograms()
    })
  })
}

function handleDeleteProgram() {
  const p = currentProgram.value
  proxy.$modal.confirm(`是否确认删除学位项目「${p.programName}」？其下 CG/LG、图谱与测评数据可能受影响。`).then(() => {
    return delProgram(p.programId)
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功")
    currentProgramId.value = null
    currentCg.value = null
    loadPrograms()
  }).catch(() => {})
}

function handleProgramChange() {
  currentCg.value = null
  lgList.value = []
  loadCgs()
}

// ---------- 能力目标 CG ----------
const cgOpen = ref(false)
const cgTitle = ref("")
const cgForm = reactive({ cgId: null, cgCode: "", cgName: "", cgNameEn: "", description: "", descriptionEn: "" })
const cgRules = {
  cgCode: [{ required: true, message: "CG编号不能为空", trigger: "blur" }],
  cgName: [{ required: true, message: "CG名称不能为空", trigger: "blur" }]
}

function loadCgs() {
  cgList.value = []
  if (!currentProgramId.value) return
  listCg(currentProgramId.value).then(res => {
    cgList.value = res.rows || res.data || []
  })
}

function openCgDialog(row) {
  cgForm.cgId = row ? row.cgId : null
  cgForm.cgCode = row ? row.cgCode : ""
  cgForm.cgName = row ? row.cgName : ""
  cgForm.cgNameEn = row ? row.cgNameEn : ""
  cgForm.description = row ? row.description : ""
  cgForm.descriptionEn = row ? row.descriptionEn : ""
  cgTitle.value = row ? "修改能力目标 CG" : "新增能力目标 CG"
  cgOpen.value = true
}

function submitCg() {
  proxy.$refs.cgRef.validate(valid => {
    if (!valid) return
    const payload = { ...cgForm, programId: currentProgramId.value }
    const fn = cgForm.cgId ? updateCg : addCg
    fn(payload).then(() => {
      proxy.$modal.msgSuccess("保存成功")
      cgOpen.value = false
      loadCgs()
    })
  })
}

function handleDeleteCg(row) {
  proxy.$modal.confirm(`是否确认删除能力目标「${row.cgCode} ${row.cgName || ""}」？其下 LG 将一并失效。`).then(() => {
    return delCg(row.cgId)
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功")
    if (currentCg.value && currentCg.value.cgId === row.cgId) {
      currentCg.value = null
      lgList.value = []
    }
    loadCgs()
  }).catch(() => {})
}

// LG 表随选中的 CG 联动
function handleCgCurrentChange(row) {
  currentCg.value = row || null
  lgList.value = []
  if (!row) return
  listLg(row.cgId).then(res => {
    lgList.value = res.rows || res.data || []
  })
}

// ---------- 学习目标 LG ----------
const lgOpen = ref(false)
const lgTitle = ref("")
const lgForm = reactive({ lgId: null, lgCode: "", description: "", descriptionEn: "", bloomLevel: "", bloomVerbs: "" })
const lgRules = {
  lgCode: [{ required: true, message: "LG编号不能为空", trigger: "blur" }],
  description: [{ required: true, message: "LG描述不能为空", trigger: "blur" }],
  bloomLevel: [{ required: true, message: "布鲁姆层级不能为空", trigger: "change" }]
}

function openLgDialog(row) {
  lgForm.lgId = row ? row.lgId : null
  lgForm.lgCode = row ? row.lgCode : ""
  lgForm.description = row ? row.description : ""
  lgForm.descriptionEn = row ? row.descriptionEn : ""
  lgForm.bloomLevel = row ? row.bloomLevel : ""
  lgForm.bloomVerbs = row ? row.bloomVerbs : ""
  lgTitle.value = row ? "修改学习目标 LG" : "新增学习目标 LG"
  lgOpen.value = true
}

function submitLg() {
  proxy.$refs.lgRef.validate(valid => {
    if (!valid) return
    const payload = { ...lgForm, cgId: currentCg.value.cgId }
    const fn = lgForm.lgId ? updateLg : addLg
    fn(payload).then(() => {
      proxy.$modal.msgSuccess("保存成功")
      lgOpen.value = false
      handleCgCurrentChange(currentCg.value)
    })
  })
}

function handleDeleteLg(row) {
  proxy.$modal.confirm(`是否确认删除学习目标「${row.lgCode}」？课程图谱中相关覆盖格将一并失效。`).then(() => {
    return delLg(row.lgId)
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功")
    handleCgCurrentChange(currentCg.value)
  }).catch(() => {})
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
.ml8 {
  margin-left: 8px;
}
</style>
