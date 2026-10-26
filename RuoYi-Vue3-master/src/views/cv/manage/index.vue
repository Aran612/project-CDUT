<template>
  <div class="app-container">
    <!-- 搜索区 -->
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="70px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入教师姓名" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item label="部门" prop="department">
        <el-input v-model="queryParams.department" placeholder="请输入所属系/部门" clearable @keyup.enter="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 教师列表 -->
    <el-table v-loading="loading" :data="professorList">
      <el-table-column label="姓名" align="center" prop="name" width="120" />
      <el-table-column label="所属系/部门" align="center" prop="department" min-width="160" show-overflow-tooltip />
      <el-table-column label="职称" align="center" prop="title" width="120" />
      <el-table-column label="邮箱" align="center" prop="email" min-width="180" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleFillCv(scope.row)">代录 CV</el-button>
          <el-button link type="warning" icon="Download" @click="handleExportCv(scope.row)">导出 Word</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" v-model:page="queryParams.pageNum" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </div>
</template>

<script setup name="CvManage">
import { listProfessor } from "@/api/research/professor"

const { proxy } = getCurrentInstance()
const router = useRouter()

const loading = ref(true)
const showSearch = ref(true)
const professorList = ref([])
const total = ref(0)

const data = reactive({
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    name: null,
    department: null
  }
})

const { queryParams } = toRefs(data)

// 查询教师列表（现有科研模块接口）
function getList() {
  loading.value = true
  listProfessor(queryParams.value).then(response => {
    professorList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

// 跳转到 CV 代录页面（携带 professorId，写入操作走 /cv/manage/*）
function handleFillCv(row) {
  router.push("/cv/myCv?professorId=" + row.professorId)
}

// 导出该教师的 Word 版 CV
function handleExportCv(row) {
  proxy.download("cv/export/" + row.professorId, {}, `CV_${row.name || row.professorId}_${new Date().getTime()}.docx`)
}

getList()
</script>
