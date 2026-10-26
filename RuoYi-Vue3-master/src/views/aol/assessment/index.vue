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
        <el-form-item>
          <el-button type="primary" plain icon="Plus" :disabled="!currentProgramId" @click="openDialog()">新增测评</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 测评方式表格 -->
    <el-card shadow="never" class="mb8">
      <el-table :data="assessmentList" border>
        <el-table-column label="能力目标 CG" align="center" width="180">
          <template #default="scope">
            <el-tag type="primary">{{ cgLabel(scope.row.cgId) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="所评估 LG" align="center" prop="lgsAssessed" width="150" show-overflow-tooltip />
        <el-table-column label="直接测评措施 Direct Measures" align="center" prop="directMeasures" min-width="220" show-overflow-tooltip>
          <template #default="scope">
            <span class="pre-line">{{ scope.row.directMeasures }}</span>
          </template>
        </el-table-column>
        <el-table-column label="评分标准与阈值 Rubric & Threshold" align="center" prop="rubricThreshold" min-width="220" show-overflow-tooltip />
        <el-table-column label="间接测评措施 Indirect Measures" align="center" prop="indirectMeasures" min-width="200" show-overflow-tooltip>
          <template #default="scope">
            <span class="pre-line">{{ scope.row.indirectMeasures }}</span>
          </template>
        </el-table-column>
        <el-table-column label="频率 Frequency" align="center" prop="frequency" width="100" />
        <el-table-column label="操作" align="center" width="140">
          <template #default="scope">
            <el-button link type="primary" icon="Edit" @click="openDialog(scope.row)">编辑</el-button>
            <el-button link type="danger" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 填表说明 -->
    <el-alert type="info" show-icon :closable="false"
      title="填表说明：按 CG 配置直接/间接测评方式；直接措施如课程考试、项目、论文评分（每行一条）；评分标准建议采用 AAC&U VALUE Rubric 或项目自制 Rubric，并写明达标阈值（如 ≥70% 达到 Milestone 3）；间接措施如毕业生/用人单位调查。" />

    <!-- 测评新增/编辑对话框 -->
    <el-dialog :title="title" v-model="open" width="680px" append-to-body>
      <el-form ref="assessRef" :model="form" :rules="rules" label-width="140px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="能力目标 CG" prop="cgId">
              <el-select v-model="form.cgId" placeholder="请选择 CG" style="width: 100%">
                <el-option v-for="cg in cgList" :key="cg.cgId" :label="cg.cgCode + ' ' + (cg.cgName || '')" :value="cg.cgId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所评估 LG" prop="lgsAssessed">
              <el-input v-model="form.lgsAssessed" placeholder="如：1.1,1.2,1.3" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="直接测评措施" prop="directMeasures">
          <el-input v-model="form.directMeasures" type="textarea" :rows="3" placeholder="每行一条，如：&#10;案例分析考试&#10;定量方法项目" />
        </el-form-item>
        <el-form-item label="评分标准与阈值" prop="rubricThreshold">
          <el-input v-model="form.rubricThreshold" type="textarea" :rows="2" placeholder="如：AAC&U 定量素养评分标准——≥70% 的学生达到 Milestone 及以上" />
        </el-form-item>
        <el-form-item label="间接测评措施" prop="indirectMeasures">
          <el-input v-model="form.indirectMeasures" type="textarea" :rows="3" placeholder="每行一条，如：&#10;毕业生调查&#10;用人单位调查" />
        </el-form-item>
        <el-form-item label="频率 Frequency" prop="frequency">
          <el-select v-model="form.frequency" filterable allow-create default-first-option placeholder="请选择或输入频率" style="width: 200px">
            <el-option label="每年" value="每年" />
            <el-option label="每2年" value="每2年" />
            <el-option label="每3年" value="每3年" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="AolAssessment">
import { listProgram, listCg, listAssessment, addAssessment, updateAssessment, delAssessment } from "@/api/aol/aol"

const { proxy } = getCurrentInstance()

const loading = ref(false)
const programList = ref([])
const currentProgramId = ref(null)
const cgList = ref([])
const assessmentList = ref([])

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
  cgList.value = []
  assessmentList.value = []
  if (!currentProgramId.value) return
  listCg(currentProgramId.value).then(res => {
    cgList.value = res.rows || res.data || []
  })
  listAssessment(currentProgramId.value).then(res => {
    assessmentList.value = res.rows || res.data || []
  })
}

// CG 显示标签：编号 + 名称
function cgLabel(cgId) {
  const cg = cgList.value.find(item => item.cgId === cgId)
  return cg ? (cg.cgCode + (cg.cgName ? ' ' + cg.cgName : '')) : (cgId || "-")
}

// ---------- CRUD ----------
const open = ref(false)
const title = ref("")
const form = reactive({ assessId: null, cgId: null, lgsAssessed: "", directMeasures: "", rubricThreshold: "", indirectMeasures: "", frequency: "" })
const rules = {
  cgId: [{ required: true, message: "能力目标 CG 不能为空", trigger: "change" }],
  directMeasures: [{ required: true, message: "直接测评措施不能为空", trigger: "blur" }]
}

function openDialog(row) {
  form.assessId = row ? row.assessId : null
  form.cgId = row ? row.cgId : null
  form.lgsAssessed = row ? row.lgsAssessed : ""
  form.directMeasures = row ? row.directMeasures : ""
  form.rubricThreshold = row ? row.rubricThreshold : ""
  form.indirectMeasures = row ? row.indirectMeasures : ""
  form.frequency = row ? row.frequency : "每年"
  title.value = row ? "修改测评方式" : "新增测评方式"
  open.value = true
}

function submitForm() {
  proxy.$refs.assessRef.validate(valid => {
    if (!valid) return
    const payload = { ...form, programId: currentProgramId.value }
    const fn = form.assessId ? updateAssessment : addAssessment
    fn(payload).then(() => {
      proxy.$modal.msgSuccess("保存成功")
      open.value = false
      loadData()
    })
  })
}

function handleDelete(row) {
  proxy.$modal.confirm("是否确认删除该测评配置？").then(() => {
    return delAssessment(row.assessId)
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功")
    loadData()
  }).catch(() => {})
}

loadPrograms()
</script>

<style scoped lang="scss">
.pre-line {
  white-space: pre-line;
}
</style>
