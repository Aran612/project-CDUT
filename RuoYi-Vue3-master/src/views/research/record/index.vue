<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input v-model="queryParams.courseName" placeholder="请输入课程名称" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="教授姓名" prop="professorName">
        <el-input v-model="queryParams.professorName" placeholder="请输入教授姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="学期" prop="semester">
        <el-select v-model="queryParams.semester" placeholder="请选择学期" clearable>
          <el-option v-for="s in semesterOptions" :key="s" :label="s" :value="s" />
        </el-select>
      </el-form-item>
      <el-form-item label="研究型" prop="isResearch">
        <el-select v-model="queryParams.isResearch" placeholder="请选择" clearable>
          <el-option label="全部" value="" />
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['research:record:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate" v-hasPermi="['research:record:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete" v-hasPermi="['research:record:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['research:record:export']">导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-alert
      title="积分计算公式：课程学分 × 选课学生数量。例如：4学分课程 × 50学生 = 200分"
      type="info"
      show-icon
      :closable="false"
      style="margin-bottom: 10px;"
    />

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="记录ID" align="center" prop="recordId" width="80" />
      <el-table-column label="课程名称" align="center" prop="courseName" show-overflow-tooltip min-width="150" />
      <el-table-column label="授课教授" align="center" prop="professorName" width="100" />
      <el-table-column label="学期" align="center" prop="semester" width="110" />
      <el-table-column label="课程学分" align="center" prop="creditPerCourse" width="90">
        <template #default="scope">
          <el-tag type="warning">{{ scope.row.creditPerCourse }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="学生数量" align="center" prop="studentCount" width="90" />
      <el-table-column label="计算积分" align="center" prop="calculatedScore" width="100" sortable>
        <template #default="scope">
          <span style="font-weight: bold; color: #E6A23C;">{{ scope.row.calculatedScore }}</span>
        </template>
      </el-table-column>
      <el-table-column label="研究型" align="center" prop="isResearch" width="90">
        <template #default="scope">
          <el-tag :type="scope.row.isResearch === '1' ? 'success' : 'info'">
            {{ scope.row.isResearch === '1' ? '是' : '否' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template #default="scope">
          <dict-tag :options="sys_normal_disable" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="160">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['research:record:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['research:record:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改授课记录对话框 -->
    <el-dialog :title="title" v-model="open" width="700px" append-to-body>
      <el-form ref="recordRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="选择课程" prop="courseId">
              <el-select v-model="form.courseId" placeholder="请选择课程" filterable @change="onCourseChange">
                <el-option v-for="c in courseOptions" :key="c.courseId" :label="c.courseName + ' (' + c.creditValue + '学分)'" :value="c.courseId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授课教授" prop="professorId">
              <el-select v-model="form.professorId" placeholder="请选择教授" filterable>
                <el-option v-for="p in professorOptions" :key="p.professorId" :label="p.name" :value="p.professorId" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="学期" prop="semester">
              <el-select v-model="form.semester" placeholder="请选择学期" filterable allow-create>
                <el-option v-for="s in semesterOptions" :key="s" :label="s" :value="s" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="学生数量" prop="studentCount">
              <el-input-number v-model="form.studentCount" :min="0" :max="1000" @change="previewScore" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="课程学分">
              <el-input-number v-model="form.creditPerCourse" :min="0" :max="20" :step="0.5" @change="previewScore" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="是否研究型课程">
              <el-switch v-model="form.isResearch" active-value="1" inactive-value="0" />
              <span style="margin-left: 8px; color: #909399; font-size: 12px;">
                标记为研究型课程将影响研究型学者评估
              </span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="计算积分">
              <el-input :value="scorePreview" disabled>
                <template #prepend>
                  <span style="color: #E6A23C; font-weight: bold;">{{ scorePreview }}</span>
                </template>
              </el-input>
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

<script setup name="Record">
import { listRecord, getRecord, delRecord, addRecord, updateRecord } from "@/api/research/record"
import { listAllCourse } from "@/api/research/course"
import { listProfessor } from "@/api/research/professor"

const { proxy } = getCurrentInstance()
const { sys_normal_disable } = proxy.useDict("sys_normal_disable")

const recordList = ref([])
const courseOptions = ref([])
const professorOptions = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")
const scorePreview = ref("0.0")

const semesterOptions = ref([
  '2024-2025-1', '2024-2025-2',
  '2025-2026-1', '2025-2026-2',
  '2026-2027-1', '2026-2027-2'
])

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    courseName: null,
    professorName: null,
    semester: null,
    isResearch: null,
    status: null
  },
  rules: {
    courseId: [{ required: true, message: "请选择课程", trigger: "change" }],
    professorId: [{ required: true, message: "请选择教授", trigger: "change" }],
    semester: [{ required: true, message: "请选择学期", trigger: "change" }],
    studentCount: [{ required: true, message: "请输入学生数量", trigger: "blur" }]
  }
})

const { queryParams, form, rules } = toRefs(data)

function loadOptions() {
  listAllCourse().then(response => {
    courseOptions.value = response.data
  })
  listProfessor({ pageNum: 1, pageSize: 1000 }).then(response => {
    professorOptions.value = response.rows
  })
}

function onCourseChange(courseId) {
  const course = courseOptions.value.find(c => c.courseId === courseId)
  if (course) {
    form.value.creditPerCourse = course.creditValue
    form.value.courseName = course.courseName
  }
  previewScore()
}

function previewScore() {
  if (form.value.creditPerCourse != null && form.value.studentCount != null) {
    const score = parseFloat(form.value.creditPerCourse) * parseInt(form.value.studentCount)
    form.value.calculatedScore = score
    scorePreview.value = score.toFixed(1)
  } else {
    scorePreview.value = "0.0"
  }
}

function getList() {
  loading.value = true
  listRecord(queryParams.value).then(response => {
    recordList.value = response.rows
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
    recordId: null,
    courseId: null,
    courseName: null,
    professorId: null,
    professorName: null,
    semester: null,
    studentCount: 0,
    creditPerCourse: 3.0,
    calculatedScore: 0,
    isResearch: "0",
    status: "0",
    remark: null
  }
  scorePreview.value = "0.0"
  proxy.resetForm("recordRef")
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
  ids.value = selection.map(item => item.recordId)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

function handleAdd() {
  reset()
  loadOptions()
  open.value = true
  title.value = "添加授课记录"
}

function handleUpdate(row) {
  reset()
  loadOptions()
  const _recordId = row.recordId || ids.value
  getRecord(_recordId).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改授课记录"
    previewScore()
  })
}

function submitForm() {
  proxy.$refs["recordRef"].validate(valid => {
    if (valid) {
      // 自动计算积分
      if (form.value.creditPerCourse != null && form.value.studentCount != null) {
        form.value.calculatedScore = parseFloat(form.value.creditPerCourse) * parseInt(form.value.studentCount)
      }
      if (form.value.recordId != null) {
        updateRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addRecord(form.value).then(response => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

function handleDelete(row) {
  const _recordIds = row.recordId || ids.value
  proxy.$modal.confirm('是否确认删除授课记录编号为"' + _recordIds + '"的数据项？').then(function() {
    return delRecord(_recordIds)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

function handleExport() {
  proxy.download('research/record/export', {
    ...queryParams.value
  }, `record_${new Date().getTime()}.xlsx`)
}

loadOptions()
getList()
</script>
