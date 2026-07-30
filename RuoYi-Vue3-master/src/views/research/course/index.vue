<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="课程编号" prop="courseCode">
        <el-input v-model="queryParams.courseCode" placeholder="请输入课程编号" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="课程类型" prop="courseType">
        <el-select v-model="queryParams.courseType" placeholder="请选择课程类型" clearable>
          <el-option label="必修课" value="0" />
          <el-option label="选修课" value="1" />
          <el-option label="公共课" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in sys_normal_disable" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:course:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:course:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:course:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:course:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="课程ID" align="center" prop="courseId" width="80" />
      <el-table-column label="课程名称" align="center" prop="courseName" show-overflow-tooltip min-width="180" />
      <el-table-column label="课程编号" align="center" prop="courseCode" width="120" />
      <el-table-column label="学分" align="center" prop="creditValue" width="80">
        <template #default="scope">
          <el-tag type="warning">{{ scope.row.creditValue }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="课程类型" align="center" prop="courseType" width="100">
        <template #default="scope">
          <el-tag :type="courseTypeTag(scope.row.courseType)">{{ courseTypeLabel(scope.row.courseType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开课院系" align="center" prop="departmentName" width="140" show-overflow-tooltip />
      <el-table-column label="学时" align="center" prop="totalHours" width="80" />
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:course:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:course:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改课程对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="courseRef" :model="form" :rules="rules" label-width="90px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="form.courseName" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程编号" prop="courseCode">
              <el-input v-model="form.courseCode" placeholder="请输入课程编号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="课程学分" prop="creditValue">
              <el-input-number v-model="form.creditValue" :min="0" :max="20" :step="0.5" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="课程类型" prop="courseType">
              <el-select v-model="form.courseType" placeholder="请选择">
                <el-option label="必修课" value="0" />
                <el-option label="选修课" value="1" />
                <el-option label="公共课" value="2" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="总学时">
              <el-input-number v-model="form.totalHours" :min="0" :max="300" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="开课院系">
              <el-input v-model="form.departmentName" placeholder="请输入开课院系名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in sys_normal_disable" :key="dict.value" :value="dict.value">{{ dict.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
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

<script setup name="Course">
import { listCourse, getCourse, delCourse, addCourse, updateCourse } from "@/api/research/course"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const courseList = ref([])
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
    courseName: null,
    courseCode: null,
    courseType: null,
    status: null
  },
  rules: {
    courseName: [{ required: true, message: "课程名称不能为空", trigger: "blur" }],
    creditValue: [{ required: true, message: "课程学分不能为空", trigger: "blur" }],
    courseType: [{ required: true, message: "课程类型不能为空", trigger: "change" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function courseTypeTag(type) {
  if (type === '0') return ''
  if (type === '1') return 'success'
  if (type === '2') return 'warning'
  return 'info'
}

function courseTypeLabel(type) {
  if (type === '0') return '必修课'
  if (type === '1') return '选修课'
  if (type === '2') return '公共课'
  return '-'
}

function getList() {
  loading.value = true
  listCourse(queryParams.value).then(response => {
    courseList.value = response.rows
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
    courseId: null,
    courseName: null,
    courseCode: null,
    creditValue: 3.0,
    courseType: '0',
    departmentId: null,
    departmentName: null,
    totalHours: null,
    status: "0",
    remark: null
  }
  proxy.resetForm("courseRef")
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
  ids.value = selection.map(item => item.courseId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  open.value = true
  title.value = "添加课程"
}

function handleUpdate(row) {
  reset()
  const _courseId = row.courseId || ids.value
  getCourse(_courseId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改课程"
  })
}

function submitForm() {
  proxy.$refs["courseRef"].validate(valid => {
    if (valid) {
      if (form.value.courseId != null) {
        updateCourse(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addCourse(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const _courseIds = row.courseId || ids.value
  proxy.$modal.confirm('是否确认删除课程编号为"' + _courseIds + '"的数据项？').then(function() {
    return delCourse(_courseIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download('research/course/export', {
    ...queryParams.value
  }, `course_${new Date().getTime()}.xlsx`)
}

getList()
</script>
