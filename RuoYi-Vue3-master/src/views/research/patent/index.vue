<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="专利名称" prop="patentName">
        <el-input v-model="queryParams.patentName" placeholder="请输入专利名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="专利号" prop="patentNumber">
        <el-input v-model="queryParams.patentNumber" placeholder="请输入专利号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="专利状态" clearable>
          <el-option label="有效" value="0" />
          <el-option label="失效" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:patent:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:patent:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:patent:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:patent:export']">导出</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['research:patent:import']">导入</el-button></el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="patentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="专利ID" align="center" prop="patentId" width="80" />
      <el-table-column label="专利名称" align="center" prop="patentName" show-overflow-tooltip min-width="180" />
      <el-table-column label="专利等级" align="center" prop="gradeName" width="150">
        <template #default="scope">
          <el-tag :type="gradeTagType(scope.row.gradeCode)">{{ scope.row.gradeName || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发明人" align="center" prop="professorName" width="100" />
      <el-table-column label="排名" align="center" prop="inventorRank" width="100" />
      <el-table-column label="专利号" align="center" prop="patentNumber" width="150" show-overflow-tooltip />
      <el-table-column label="授权国家" align="center" prop="patentCountry" width="100" />
      <el-table-column label="申请日" align="center" prop="applicationDate" width="110">
        <template #default="scope"><span>{{ parseTime(scope.row.applicationDate, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
      <el-table-column label="授权日" align="center" prop="grantDate" width="110">
        <template #default="scope"><span>{{ parseTime(scope.row.grantDate, '{y}-{m}-{d}') }}</span></template>
      </el-table-column>
      <el-table-column label="积分" align="center" prop="actualScore" width="80" sortable />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope"><el-tag :type="scope.row.status === '0' ? 'success' : 'info'">{{ scope.row.status === '0' ? '有效' : '失效' }}</el-tag></template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:patent:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:patent:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="patentRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="专利名称" prop="patentName"><el-input v-model="form.patentName" placeholder="请输入专利名称" /></el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="专利等级" prop="gradeId">
              <el-select v-model="form.gradeId" placeholder="请选择" style="width:100%">
                <el-option v-for="g in gradeOptions" :key="g.gradeId" :label="g.gradeName + ' (' + g.baseScore + '分)'" :value="g.gradeId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发明人" prop="professorId">
              <el-select v-model="form.professorId" placeholder="请选择" filterable style="width:100%">
                <el-option v-for="p in professorOptions" :key="p.professorId" :label="p.name" :value="p.professorId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="发明人排名">
              <el-select v-model="form.inventorRank" placeholder="请选择" style="width:100%">
                <el-option label="第一发明人" value="第一发明人" />
                <el-option label="第二发明人" value="第二发明人" />
                <el-option label="第三发明人" value="第三发明人" />
                <el-option label="其他发明人" value="其他发明人" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授权国家"><el-input v-model="form.patentCountry" placeholder="如：中国、美国" /></el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="专利号"><el-input v-model="form.patentNumber" placeholder="请输入专利号（如 ZL202310123456.7）" /></el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="申请日期">
              <el-date-picker v-model="form.applicationDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授权日期">
              <el-date-picker v-model="form.grantDate" type="date" value-format="YYYY-MM-DD" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="实际积分"><el-input-number v-model="form.actualScore" :min="0" :max="9999" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status"><el-radio value="0">有效</el-radio><el-radio value="1">失效</el-radio></el-radio-group>
        </el-form-item>
        <el-form-item label="备注"><el-input v-model="form.remark" type="textarea" /></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="submitForm">确 定</el-button><el-button @click="cancel">取 消</el-button></template>
    </el-dialog>

    <el-dialog title="导入专利" v-model="importOpen" width="400px" append-to-body>
      <el-upload ref="uploadRef" :action="uploadUrl" :headers="uploadHeaders" :data="{updateSupport:importUpdateSupport}"
        :auto-upload="false" :limit="1" accept=".xlsx,.xls" :on-success="handleImportSuccess" :on-error="handleImportError" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip><div class="el-upload__tip">仅支持 .xlsx / .xls 格式</div></template>
      </el-upload>
      <el-checkbox v-model="importUpdateSupport" style="margin-top:12px">是否更新已存在的专利数据</el-checkbox>
      <template #footer>
        <el-button @click="importOpen=false">取 消</el-button>
        <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
        <el-button type="success" @click="submitUpload">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Patent">
import { getToken } from '@/utils/auth'
import { listPatent, getPatent, delPatent, addPatent, updatePatent, importTemplate } from "@/api/research/patent"
import { listProfessor } from "@/api/research/professor"

const { proxy } = getCurrentInstance()

// 专利等级预设（对齐 ms_patent_grade 预置数据）
const PATENT_GRADES = [
  { gradeId: 1, gradeCode: 'PATENT_US_JP_KR_EP', gradeName: '美日韩欧授权发明专利', baseScore: 200 },
  { gradeId: 2, gradeCode: 'PATENT_CN_INVENT',   gradeName: '中国授权发明专利',     baseScore: 30 },
  { gradeId: 3, gradeCode: 'PATENT_OTHER_INTL',  gradeName: '其他涉外授权发明专利',  baseScore: 30 },
  { gradeId: 4, gradeCode: 'PATENT_UTILITY',     gradeName: '实用新型专利',         baseScore: 15 },
  { gradeId: 5, gradeCode: 'PATENT_DESIGN',      gradeName: '外观设计专利',         baseScore: 10 },
  { gradeId: 6, gradeCode: 'SOFTWARE_COPYRIGHT', gradeName: '计算机软件著作权',      baseScore: 10 },
  { gradeId: 7, gradeCode: 'IC_LAYOUT',          gradeName: '集成电路布图设计',      baseScore: 10 }
]

const patentList = ref([])
const gradeOptions = ref(PATENT_GRADES)
const professorOptions = ref([])
const open = ref(false); const loading = ref(true); const showSearch = ref(true)
const ids = ref([]); const single = ref(true); const multiple = ref(true)
const total = ref(0); const title = ref("")

const data = reactive({
  form: {},
  queryParams: { pageNum: 1, pageSize: 10, patentName: null, patentNumber: null, status: null },
  rules: {
    patentName: [{ required: true, message: "专利名称不能为空", trigger: "blur" }],
    gradeId: [{ required: true, message: "专利等级不能为空", trigger: "change" }],
    professorId: [{ required: true, message: "发明人不能为空", trigger: "change" }]
  }
})
const { queryParams, form, rules } = toRefs(data)

function gradeTagType(code) {
  if (!code) return 'info'
  if (code === 'PATENT_US_JP_KR_EP') return 'danger'
  if (code === 'PATENT_CN_INVENT') return 'warning'
  return 'success'
}

function loadProfessors() {
  listProfessor({ pageNum: 1, pageSize: 1000 }).then(r => { professorOptions.value = r.rows })
}

function getList() { loading.value = true; listPatent(queryParams.value).then(r => { patentList.value = r.rows; total.value = r.total; loading.value = false }) }
function cancel() { open.value = false; reset() }
function reset() {
  form.value = { patentId: null, patentName: null, gradeId: null, professorId: null, inventorRank: '第一发明人', patentNumber: null, patentCountry: '中国', applicationDate: null, grantDate: null, actualScore: null, status: "0", remark: null }
  proxy.resetForm("patentRef")
}
function handleQuery() { queryParams.value.pageNum = 1; getList() }
function resetQuery() { proxy.resetForm("queryRef"); handleQuery() }
function handleSelectionChange(s) { ids.value = s.map(i => i.patentId); single.value = s.length != 1; multiple.value = !s.length }
function handleAdd() { reset(); loadProfessors(); open.value = true; title.value = "添加专利" }
function handleUpdate(row) {
  reset(); loadProfessors()
  getPatent(row.patentId || ids.value).then(r => { form.value = r.data; open.value = true; title.value = "修改专利" })
}
function submitForm() {
  proxy.$refs["patentRef"].validate(v => {
    if (v) { (form.value.patentId ? updatePatent(form.value) : addPatent(form.value)).then(() => { proxy.$modal.msgSuccess("操作成功"); open.value = false; getList() }) }
  })
}
function handleDelete(row) {
  proxy.$modal.confirm('确认删除专利"' + (row.patentName || row.patentId) + '"？').then(() => delPatent(row.patentId || ids.value)).then(() => { getList(); proxy.$modal.msgSuccess("删除成功") }).catch(() => {})
}
function handleExport() { proxy.download('research/patent/export', { ...queryParams.value }, `patent_${new Date().getTime()}.xlsx`) }

const importOpen = ref(false), importUpdateSupport = ref(false), uploadRef = ref(null)
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/research/patent/importData")
const uploadHeaders = ref({ Authorization: "Bearer " + getToken() })
function handleImport() { importOpen.value = true }
function downloadTemplate() { proxy.download('research/patent/importTemplate', {}, '专利导入模板.xlsx') }
function submitUpload() { uploadRef.value.submit() }
function handleImportSuccess(res) { if (res.code === 200) { proxy.$modal.msgSuccess(res.msg || "导入成功"); importOpen.value = false; getList() } else proxy.$modal.msgError(res.msg || "导入失败") }
function handleImportError() { proxy.$modal.msgError("导入失败，请检查文件格式") }

loadProfessors()
getList()
</script>
