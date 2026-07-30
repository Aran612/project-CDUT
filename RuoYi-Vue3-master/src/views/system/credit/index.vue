<template>
  <div class="app-container">
    <!-- 搜索栏 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="规则名称" prop="ruleName">
        <el-input
          v-model="queryParams.ruleName"
          placeholder="请输入规则名称"
          clearable
          size="small"
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="Plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
    </el-row>

    <!-- 表格 -->
    <el-table v-loading="loading" :data="creditRuleList">
      <el-table-column label="规则编号" align="center" prop="ruleId" />
      <el-table-column label="规则名称" align="center" prop="ruleName" />
      <el-table-column label="学分值" align="center" prop="creditValue" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status === '0' ? 'success' : 'danger'">
            {{ scope.row.status === '0' ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="Edit" @click="handleUpdate(scope.row)">修改</el-button>
          <el-button size="mini" type="text" icon="Delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="Credit">
import { reactive, ref } from 'vue'

const loading = ref(true)
const total = ref(0)
const creditRuleList = ref([])

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  ruleName: undefined
})

function getList() {
  // 这里暂时用模拟数据
  loading.value = false
  creditRuleList.value = [
    { ruleId: 1, ruleName: '发表SCI论文', creditValue: 10, status: '0' },
    { ruleId: 2, ruleName: '发表中文核心', creditValue: 5, status: '0' },
    { ruleId: 3, ruleName: '发明专利授权', creditValue: 8, status: '1' }
  ]
}

function handleQuery() {
  getList()
}

function resetQuery() {
  queryParams.ruleName = undefined
  handleQuery()
}

function handleAdd() {
  // 后续实现
}

function handleUpdate(row) {
  // 后续实现
}

function handleDelete(row) {
  // 后续实现
}

// 初始化加载
getList()
</script>