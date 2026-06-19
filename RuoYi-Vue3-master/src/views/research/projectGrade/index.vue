<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="等级编码" prop="gradeCode">
        <el-input v-model="queryParams.gradeCode" placeholder="请输入等级编码" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="等级名称" prop="gradeName">
        <el-input v-model="queryParams.gradeName" placeholder="请输入等级名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="项目类别" prop="projectCategory">
        <el-select v-model="queryParams.projectCategory" placeholder="请选择项目类别" clearable>
          <el-option label="自然科学" value="自然科学" />
          <el-option label="人文社科" value="人文社科" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:projectGrade:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:projectGrade:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:projectGrade:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:projectGrade:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="gradeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="等级ID" align="center" prop="gradeId" width="80" />
      <el-table-column label="等级编码" align="center" prop="gradeCode" width="130" />
      <el-table-column label="等级名称" align="center" prop="gradeName" min-width="180" show-overflow-tooltip />
      <el-table-column label="项目类别" align="center" prop="projectCategory" width="100" />
      <el-table-column label="基础积分" align="center" prop="baseScore" width="100" sortable />
      <el-table-column label="排序" align="center" prop="sortOrder" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:projectGrade:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:projectGrade:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改项目等级对话框 -->
    <el-dialog :title="title" v-model="open" width="550px" append-to-body>
      <el-form ref="gradeRef" :model="form" :rules="rules" label-width="90px">
        <el-form-item label="等级编码" prop="gradeCode">
          <el-input v-model="form.gradeCode" placeholder="请输入等级编码（如 NSFC_MAJOR）" />
        </el-form-item>
        <el-form-item label="等级名称" prop="gradeName">
          <el-input v-model="form.gradeName" placeholder="请输入等级名称" />
        </el-form-item>
        <el-form-item label="项目类别" prop="projectCategory">
          <el-select v-model="form.projectCategory" placeholder="请选择项目类别">
            <el-option label="自然科学" value="自然科学" />
            <el-option label="人文社科" value="人文社科" />
          </el-select>
        </el-form-item>
        <el-form-item label="基础积分" prop="baseScore">
          <el-input-number v-model="form.baseScore" :min="0" :max="99999" placeholder="请输入基础积分" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" :max="999" placeholder="请输入排序号" />
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
  </div>
</template>

<script setup name="ProjectGrade">
import { listProjectGrade, getProjectGrade, delProjectGrade, addProjectGrade, updateProjectGrade } from "@/api/research/project"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const gradeList = ref([])
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
    gradeCode: null,
    gradeName: null,
    projectCategory: null,
    status: null
  },
  rules: {
    gradeCode: [{ required: true, message: "等级编码不能为空", trigger: "blur" }],
    gradeName: [{ required: true, message: "等级名称不能为空", trigger: "blur" }],
    projectCategory: [{ required: true, message: "项目类别不能为空", trigger: "change" }],
    baseScore: [{ required: true, message: "基础积分不能为空", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function getList() {
  loading.value = true
  listProjectGrade(queryParams.value).then(response => {
    gradeList.value = response.rows
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
    gradeId: null,
    gradeCode: null,
    gradeName: null,
    projectCategory: null,
    baseScore: null,
    sortOrder: 0,
    status: "0",
    remark: null
  }
  proxy.resetForm("gradeRef")
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
  ids.value = selection.map(item => item.gradeId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加项目等级"
}

function handleUpdate(row) {
  reset()
  const _gradeId = row.gradeId || ids.value
  getProjectGrade(_gradeId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改项目等级"
  })
}

function submitForm() {
  proxy.$refs["gradeRef"].validate(valid => {
    if (valid) {
      if (form.value.gradeId != null) {
        updateProjectGrade(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addProjectGrade(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const _gradeIds = row.gradeId || ids.value
  proxy.$modal.confirm('是否确认删除项目等级编号为"' + _gradeIds + '"的数据项？').then(function() {
    return delProjectGrade(_gradeIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download('research/project/grade/export', {
    ...queryParams.value
  }, `projectGrade_${new Date().getTime()}.xlsx`)
}

getList()
</script>
