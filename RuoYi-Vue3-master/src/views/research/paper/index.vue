<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="论文名称" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入论文名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="教授姓名" prop="professorName">
        <el-input v-model="queryParams.professorName" placeholder="请输入教授姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="论文等级" prop="gradeId">
        <el-select v-model="queryParams.gradeId" placeholder="请选择论文等级" clearable>
          <el-option v-for="g in gradeOptions" :key="g.gradeId" :label="g.gradeName" :value="g.gradeId" />
        </el-select>
      </el-form-item>
      <el-form-item label="作者身份" prop="authorPosition">
        <el-select v-model="queryParams.authorPosition" placeholder="请选择作者身份" clearable>
          <el-option label="第一作者" value="第一作者" />
          <el-option label="通讯作者" value="通讯作者" />
          <el-option label="第二作者" value="第二作者" />
          <el-option label="第三作者" value="第三作者" />
          <el-option label="其他作者" value="其他作者" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:paper:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:paper:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:paper:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:paper:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['research:paper:import']">导入</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="paperList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="论文ID" align="center" prop="paperId" width="80" />
      <el-table-column label="论文名称" align="center" prop="title" show-overflow-tooltip min-width="200" />
      <el-table-column label="论文等级" align="center" prop="gradeName" width="160">
        <template #default="scope">
          <el-tag :type="gradeTagType(scope.row.gradeName)">{{ scope.row.gradeName || '-' }}</el-tag>
          <el-tooltip v-if="scope.row.forEvaluationOnly === '1'" content="该等级仅用于岗位聘期考核，不计入成果奖励" placement="top">
            <el-tag type="warning" size="small" style="margin-left: 4px;">考核</el-tag>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="教授姓名" align="center" prop="professorName" width="100" />
      <el-table-column label="作者身份" align="center" prop="authorPosition" width="100" />
      <el-table-column label="期刊/会议" align="center" prop="journalName" show-overflow-tooltip min-width="150" />
      <el-table-column label="发表时间" align="center" prop="publishDate" width="110">
        <template #default="scope">
          <span>{{ parseTime(scope.row.publishDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际积分" align="center" prop="actualScore" width="90" sortable />
      <el-table-column label="论文链接" align="center" prop="paperUrl" width="100">
        <template #default="scope">
          <el-link v-if="scope.row.paperUrl" :href="scope.row.paperUrl" target="_blank" type="primary">查看</el-link>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:paper:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:paper:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改论文对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="paperRef" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="论文名称" prop="title">
              <el-input v-model="form.title" placeholder="请输入论文名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="论文等级" prop="gradeId">
              <el-select v-model="form.gradeId" placeholder="请选择论文等级" @change="onGradeChange">
                <el-option v-for="g in gradeOptions" :key="g.gradeId" :label="g.gradeName + ' (' + g.baseScore + '分)'" :value="g.gradeId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="作者身份" prop="authorPosition">
              <el-select v-model="form.authorPosition" placeholder="请选择作者身份" @change="previewScore">
                <el-option label="第一作者" value="第一作者" />
                <el-option label="通讯作者（按第二作者计）" value="通讯作者" />
                <el-option label="第二作者" value="第二作者" />
                <el-option label="第三作者" value="第三作者" />
                <el-option label="其他作者" value="其他作者" />
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
            <el-form-item label="发表时间" prop="publishDate">
              <el-date-picker v-model="form.publishDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="期刊/会议" prop="journalName">
          <el-input v-model="form.journalName" placeholder="请输入期刊或会议名称" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="DOI号" prop="doi">
              <el-input v-model="form.doi" placeholder="请输入DOI号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="论文链接" prop="paperUrl">
              <el-input v-model="form.paperUrl" placeholder="请输入论文链接" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="摘要" prop="abstractText">
          <el-input v-model="form.abstractText" type="textarea" :rows="3" placeholder="请输入摘要" />
        </el-form-item>
        <el-form-item label="实际积分" prop="actualScore">
          <el-input-number v-model="form.actualScore" :min="0" :max="9999" />
          <span style="margin-left: 8px; color: #909399; font-size: 12px;">
            预览：{{ scorePreview }} 分
          </span>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
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

    <!-- 导入对话框 -->
    <el-dialog title="导入论文" v-model="importOpen" width="400px" append-to-body>
      <el-upload
        ref="uploadRef"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :data="{ updateSupport: importUpdateSupport }"
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        :file-list="importFileList"
        drag
      >
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">
            <el-checkbox v-model="importUpdateSupport" style="margin-top: 8px;" />
            是否更新已存在的数据
          </div>
        </template>
      </el-upload>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
          <el-button type="success" @click="submitUpload">确 定</el-button>
          <el-button @click="importOpen = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Paper">
import { listPaper, getPaper, delPaper, addPaper, updatePaper, importTemplate } from "@/api/research/paper"
import { listAllGrade } from "@/api/research/grade"
import { listProfessor } from "@/api/research/professor"
import { getToken } from "@/utils/auth"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const paperList = ref([])
const gradeOptions = ref([])
const professorOptions = ref([])
const open = ref(false)
const importOpen = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const scorePreview = ref("—")
const importUpdateSupport = ref(false)
const importFileList = ref([])

const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/research/paper/importData")
const uploadHeaders = ref({ Authorization: "Bearer " + getToken() })

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    title: null,
    gradeId: null,
    professorId: null,
    professorName: null,
    authorPosition: null,
    journalName: null,
    publishDate: null,
    doi: null,
    status: null
  },
  rules: {
    title: [{ required: true, message: "论文名称不能为空", trigger: "blur" }],
    gradeId: [{ required: true, message: "论文等级不能为空", trigger: "change" }],
    professorId: [{ required: true, message: "教授不能为空", trigger: "change" }],
    authorPosition: [{ required: true, message: "作者身份不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

// 加载下拉选项
function loadOptions() {
  listAllGrade().then(response => {
    gradeOptions.value = response.data
  })
  listProfessor({ pageNum: 1, pageSize: 1000 }).then(response => {
    professorOptions.value = response.rows
  })
}

function gradeTagType(gradeName) {
  if (!gradeName) return 'info'
  if (gradeName.startsWith('N/S/C') || gradeName.startsWith('NSC')) return ''
  if (gradeName.startsWith('SCIE') || gradeName.startsWith('SCI')) return 'danger'
  if (gradeName.startsWith('NI')) return 'warning'
  if (gradeName.startsWith('A&HCI')) return 'success'
  if (gradeName.startsWith('EI')) return 'warning'
  if (gradeName.startsWith('CSSCI')) return 'success'
  return 'info'
}

function getList() {
  loading.value = true
  listPaper(queryParams.value).then(response => {
    paperList.value = response.rows
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
    paperId: null,
    title: null,
    gradeId: null,
    professorId: null,
    authorPosition: null,
    journalName: null,
    publishDate: null,
    doi: null,
    paperUrl: null,
    abstractText: null,
    actualScore: null,
    status: "0",
    remark: null
  }
  scorePreview.value = "—"
  proxy.resetForm("paperRef")
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
  ids.value = selection.map(item => item.paperId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  loadOptions()
  open.value = true
  title.value = "添加论文"
}

function handleUpdate(row) {
  reset()
  loadOptions()
  const _paperId = row.paperId || ids.value
  getPaper(_paperId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改论文"
    previewScore()
  })
}

function onGradeChange() {
  previewScore()
}

function previewScore() {
  const grade = gradeOptions.value.find(g => g.gradeId === form.value.gradeId)
  if (grade && form.value.authorPosition) {
    const base = grade.baseScore
    let coeff = 1.0
    // 依据：成理校发〔2025〕8号 — 通讯作者按第二作者计(1/2)，第三作者取1/3
    if (form.value.authorPosition === '通讯作者' || form.value.authorPosition === '第二作者') coeff = 0.5
    else if (form.value.authorPosition === '第三作者') coeff = 0.33
    else if (form.value.authorPosition === '其他作者') coeff = 0.2
    scorePreview.value = Math.floor(base * coeff).toString()
  } else {
    scorePreview.value = "—"
  }
}

function submitForm() {
  proxy.$refs["paperRef"].validate(valid => {
    if (valid) {
      if (form.value.paperId != null) {
        updatePaper(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addPaper(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const _paperIds = row.paperId || ids.value
  proxy.$modal.confirm('是否确认删除论文编号为"' + _paperIds + '"的数据项？').then(function() {
    return delPaper(_paperIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download('research/paper/export', {
    ...queryParams.value
  }, `paper_${new Date().getTime()}.xlsx`)
}

// 导入相关
function handleImport() {
  importFileList.value = []
  importOpen.value = true
}

function downloadTemplate() {
  importTemplate().then(response => {
    proxy.download('research/paper/importTemplate', {}, 'paper_template.xlsx')
  })
}

function submitUpload() {
  proxy.$refs["uploadRef"].submit()
}

function handleImportSuccess(response, file, fileList) {
  if (response.code === 200) {
    proxy.$modal.msgSuccess(response.msg || "导入成功")
    importOpen.value = false
    getList()
  } else {
    proxy.$modal.msgError(response.msg || "导入失败")
  }
}

function handleImportError(error) {
  proxy.$modal.msgError("导入失败，请检查文件格式")
}

loadOptions()
getList()
</script>
