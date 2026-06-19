<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="项目名称" prop="projectName">
        <el-input v-model="queryParams.projectName" placeholder="请输入项目名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="项目等级" prop="gradeId">
        <el-select v-model="queryParams.gradeId" placeholder="请选择项目等级" clearable>
          <el-option v-for="g in gradeOptions" :key="g.gradeId" :label="g.gradeName" :value="g.gradeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="承担角色" prop="role">
        <el-select v-model="queryParams.role" placeholder="请选择角色" clearable>
          <el-option label="主持" value="主持" />
          <el-option label="参与" value="参与" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="项目状态" clearable>
          <el-option label="在研" value="0" />
          <el-option label="结题" value="1" />
          <el-option label="终止" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:project:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:project:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:project:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:project:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['research:project:import']">导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="项目ID" align="center" prop="projectId" width="80" />
      <el-table-column label="项目名称" align="center" prop="projectName" show-overflow-tooltip min-width="200" />
      <el-table-column label="项目等级" align="center" prop="gradeName" width="160">
        <template #default="scope">
          <el-tag :type="gradeTagType(scope.row.gradeName)">{{ scope.row.gradeName || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="教授" align="center" prop="professorName" width="100" />
      <el-table-column label="角色" align="center" prop="role" width="80">
        <template #default="scope">
          <el-tag :type="scope.row.role === '主持' ? '' : 'info'" size="small">{{ scope.row.role }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="经费(万元)" align="center" prop="fundingAmount" width="110" sortable />
      <el-table-column label="批准号" align="center" prop="approvalNumber" width="140" show-overflow-tooltip />
      <el-table-column label="开始日期" align="center" prop="startDate" width="110">
        <template #default="scope">
          <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束日期" align="center" prop="endDate" width="110">
        <template #default="scope">
          <span>{{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="积分" align="center" prop="actualScore" width="80" sortable />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:project:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:project:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改项目对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="projectRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" placeholder="请输入项目名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="项目等级" prop="gradeId">
              <el-select v-model="form.gradeId" placeholder="请选择项目等级" @change="previewScore">
                <el-option v-for="g in gradeOptions" :key="g.gradeId" :label="g.gradeName + ' (' + g.baseScore + '分)'" :value="g.gradeId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="承担角色" prop="role">
              <el-select v-model="form.role" placeholder="请选择角色" @change="previewScore">
                <el-option label="主持" value="主持" />
                <el-option label="参与" value="参与" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="教授" prop="professorId">
              <el-select v-model="form.professorId" placeholder="请选择教授" filterable>
                <el-option v-for="p in professorOptions" :key="p.professorId" :label="p.name" :value="p.professorId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经费(万元)" prop="fundingAmount">
              <el-input-number v-model="form.fundingAmount" :min="0" :precision="2" :step="10" placeholder="经费" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="批准号" prop="approvalNumber">
              <el-input v-model="form.approvalNumber" placeholder="请输入批准号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目来源" prop="projectSource">
              <el-input v-model="form.projectSource" placeholder="请输入项目来源" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实际积分" prop="actualScore">
          <el-input-number v-model="form.actualScore" :min="0" :max="9999" />
          <span style="margin-left: 8px; color: #909399; font-size: 12px;">
            预览：{{ scorePreview }} 分
          </span>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="项目状态">
            <el-option label="在研" value="0" />
            <el-option label="结题" value="1" />
            <el-option label="终止" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <el-dialog title="导入项目" v-model="importOpen" width="400px" append-to-body>
      <el-upload ref="uploadRef" :action="uploadUrl" :headers="uploadHeaders" :data="{updateSupport:importUpdateSupport}"
        :auto-upload="false" :limit="1" accept=".xlsx,.xls" :on-success="handleImportSuccess" :on-error="handleImportError" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip><div class="el-upload__tip">仅支持 .xlsx / .xls 格式</div></template>
      </el-upload>
      <el-checkbox v-model="importUpdateSupport" style="margin-top:12px">是否更新已存在的项目数据</el-checkbox>
      <template #footer>
        <el-button @click="importOpen=false">取 消</el-button>
        <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
        <el-button type="success" @click="submitUpload">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="ResearchProject">
import { getToken } from '@/utils/auth'
import { listProject, getProject, delProject, addProject, updateProject, listAllProjectGrade, importTemplate } from "@/api/research/project"
import { listProfessor } from "@/api/research/professor"

const { proxy } = getCurrentInstance()

const projectList = ref([])
const gradeOptions = ref([])
const professorOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const scorePreview = ref("—")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    projectName: null,
    gradeId: null,
    role: null,
    status: null
  },
  rules: {
    projectName: [{ required: true, message: "项目名称不能为空", trigger: "blur" }],
    gradeId: [{ required: true, message: "项目等级不能为空", trigger: "change" }],
    professorId: [{ required: true, message: "教授不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function loadOptions() {
  listAllProjectGrade().then(response => {
    gradeOptions.value = response.data
  })
  listProfessor({ pageNum: 1, pageSize: 1000 }).then(response => {
    professorOptions.value = response.rows
  })
}

function gradeTagType(gradeName) {
  if (!gradeName) return 'info'
  if (gradeName.includes('国自然') || gradeName.includes('国家')) return 'danger'
  if (gradeName.includes('重点研发') || gradeName.includes('重大专项')) return 'warning'
  if (gradeName.includes('省')) return 'success'
  if (gradeName.includes('教育部')) return ''
  if (gradeName.includes('横向')) return 'info'
  return 'info'
}

function statusTagType(status) {
  const map = { '0': '', '1': 'success', '2': 'danger' }
  return map[status] || 'info'
}

function statusLabel(status) {
  const map = { '0': '在研', '1': '结题', '2': '终止' }
  return map[status] || status
}

function getList() {
  loading.value = true
  listProject(queryParams.value).then(response => {
    projectList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function cancel() {
  open.value = false
  reset()
}

function reset() {
  form.value = {
    projectId: null,
    projectName: null,
    gradeId: null,
    professorId: null,
    role: '主持',
    fundingAmount: null,
    approvalNumber: null,
    projectSource: null,
    startDate: null,
    endDate: null,
    actualScore: null,
    status: "0",
    remark: null
  }
  scorePreview.value = "—"
  proxy.resetForm("projectRef")
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.projectId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  loadOptions()
  open.value = true
  title.value = "添加科研项目"
}

function handleUpdate(row) {
  reset()
  loadOptions()
  const _projectId = row.projectId || ids.value
  getProject(_projectId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改科研项目"
    previewScore()
  })
}

function previewScore() {
  const grade = gradeOptions.value.find(g => g.gradeId === form.value.gradeId)
  if (grade && grade.baseScore > 0) {
    const base = grade.baseScore
    let coeff = 1.0
    if (form.value.role === '参与') coeff = 0.5
    scorePreview.value = Math.floor(base * coeff).toString()
  } else if (grade && grade.baseScore === 0 && form.value.fundingAmount) {
    // 横向项目按经费计算
    const code = grade.gradeCode
    let rate = code === 'SOCIAL_HORIZONTAL' ? 1.0 : 0.75
    scorePreview.value = Math.floor(form.value.fundingAmount * rate).toString()
  } else {
    scorePreview.value = "—"
  }
}

function submitForm() {
  proxy.$refs["projectRef"].validate(valid => {
    if (valid) {
      if (form.value.projectId != null) {
        updateProject(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addProject(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const _projectIds = row.projectId || ids.value
  proxy.$modal.confirm('是否确认删除项目编号为"' + _projectIds + '"的数据项？').then(function() {
    return delProject(_projectIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download('research/project/export', {
    ...queryParams.value
  }, `project_${new Date().getTime()}.xlsx`)
}

const importOpen = ref(false), importUpdateSupport = ref(false), uploadRef = ref(null)
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/research/project/importData")
const uploadHeaders = ref({ Authorization: "Bearer " + getToken() })
function handleImport() { importOpen.value = true }
function downloadTemplate() { proxy.download('research/project/importTemplate', {}, '项目导入模板.xlsx') }
function submitUpload() { uploadRef.value.submit() }
function handleImportSuccess(res) { if (res.code === 200) { proxy.$modal.msgSuccess(res.msg || "导入成功"); importOpen.value = false; getList() } else proxy.$modal.msgError(res.msg || "导入失败") }
function handleImportError() { proxy.$modal.msgError("导入失败，请检查文件格式") }

loadOptions()
getList()
</script>
