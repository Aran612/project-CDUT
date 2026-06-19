<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="所属部门" prop="department">
        <el-input v-model="queryParams.department" placeholder="请输入所属部门" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="职称" prop="title">
        <el-select v-model="queryParams.title" placeholder="请选择职称" clearable>
          <el-option label="教授" value="教授" />
          <el-option label="副教授" value="副教授" />
          <el-option label="讲师" value="讲师" />
          <el-option label="助教" value="助教" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:professor:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:professor:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:professor:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:professor:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="Upload" @click="handleImport" v-hasPermi="['research:professor:import']">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="DeleteFilled" @click="handleClearAllData" v-hasPermi="['research:professor:remove']" :disabled="single">🧹 清除成果</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="professorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="教授ID" align="center" prop="professorId" width="80" />
      <el-table-column label="姓名" align="center" prop="name" width="100" />
      <el-table-column label="所属部门" align="center" prop="department" show-overflow-tooltip />
      <el-table-column label="职称" align="center" prop="title" width="100" />
      <el-table-column label="研究方向" align="center" prop="researchField" show-overflow-tooltip />
      <el-table-column label="邮箱" align="center" prop="email" show-overflow-tooltip />
      <el-table-column label="累计积分" align="center" prop="totalScore" width="100" sortable />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:professor:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:professor:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改教授信息对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="professorRef" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="职称" prop="title">
              <el-select v-model="form.title" placeholder="请选择职称">
                <el-option label="教授" value="教授" />
                <el-option label="副教授" value="副教授" />
                <el-option label="讲师" value="讲师" />
                <el-option label="助教" value="助教" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="所属部门" prop="department">
          <el-input v-model="form.department" placeholder="请输入所属部门" />
        </el-form-item>
        <el-form-item label="研究方向" prop="researchField">
          <el-input v-model="form.researchField" placeholder="请输入研究方向" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
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

    <!-- 导入教授对话框 -->
    <el-dialog title="导入教授" v-model="importOpen" width="400px" append-to-body>
      <el-upload ref="uploadRef" :action="uploadUrl" :headers="uploadHeaders" :data="{updateSupport:importUpdateSupport}"
        :auto-upload="false" :limit="1" accept=".xlsx,.xls" :on-success="handleImportSuccess" :on-error="handleImportError"
        drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip><div class="el-upload__tip">仅支持 .xlsx / .xls 格式</div></template>
      </el-upload>
      <el-checkbox v-model="importUpdateSupport" style="margin-top:12px">是否更新已存在的教授数据</el-checkbox>
      <template #footer>
        <el-button @click="importOpen=false">取 消</el-button>
        <el-button type="primary" @click="downloadTemplate">下载模板</el-button>
        <el-button type="success" @click="submitUpload">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Professor">
import { getToken } from '@/utils/auth'
import { listProfessor, getProfessor, delProfessor, addProfessor, updateProfessor, importTemplate, clearAllProfessorData } from "@/api/research/professor"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const professorList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    department: null,
    title: null,
    researchField: null,
    email: null,
    status: null
  },
  rules: {
    name: [{ required: true, message: "姓名不能为空", trigger: "blur" }],
    title: [{ required: true, message: "职称不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listProfessor(queryParams.value).then(response => {
    professorList.value = response.rows
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
    professorId: null,
    userId: null,
    name: null,
    department: null,
    title: null,
    researchField: null,
    email: null,
    totalScore: 0,
    status: "0",
    remark: null
  }
  proxy.resetForm("professorRef")
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
  ids.value = selection.map(item => item.professorId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加教授信息"
}

function handleUpdate(row) {
  reset()
  const _professorId = row.professorId || ids.value
  getProfessor(_professorId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改教授信息"
  })
}

function submitForm() {
  proxy.$refs["professorRef"].validate(valid => {
    if (valid) {
      if (form.value.professorId != null) {
        updateProfessor(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addProfessor(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const _professorIds = row.professorId || ids.value
  proxy.$modal.confirm('是否确认删除教授编号为"' + _professorIds + '"的数据项？').then(function() {
    return delProfessor(_professorIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download('research/professor/export', {
    ...queryParams.value
  }, `professor_${new Date().getTime()}.xlsx`)
}

// 导入相关
const importOpen = ref(false)
const importUpdateSupport = ref(false)
const importFileList = ref([])
const uploadRef = ref(null)
const uploadUrl = ref(import.meta.env.VITE_APP_BASE_API + "/research/professor/importData")
const uploadHeaders = ref({ Authorization: "Bearer " + getToken() })
function handleImport() { importOpen.value = true }
function downloadTemplate() { proxy.download('research/professor/importTemplate', {}, '教授导入模板.xlsx') }
function submitUpload() { uploadRef.value.submit() }
function handleImportSuccess(res, file, fileList) {
  if (res.code === 200) { proxy.$modal.msgSuccess(res.msg || "导入成功"); importOpen.value = false; getList() }
  else { proxy.$modal.msgError(res.msg || "导入失败") }
}
function handleImportError(err) { proxy.$modal.msgError("导入失败，请检查文件格式") }

function handleClearAllData() {
  const row = professorList.value.find(p => p.professorId === ids.value[0])
  const name = row ? row.name : ('ID=' + ids.value[0])
  proxy.$modal.confirm('确定要清除教授【' + name + '】的全部成果数据吗？（论文/项目/专利/著作/获奖全部删除，此操作不可恢复！）', '危险操作', {
    confirmButtonText: '确定清除',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    clearAllProfessorData(ids.value[0]).then(r => {
      proxy.$modal.msgSuccess(r.msg)
      getList()
    })
  }).catch(() => {})
}

getList()
</script>
