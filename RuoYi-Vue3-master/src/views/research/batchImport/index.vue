<template>
  <div class="app-container">
    <el-card shadow="never" style="margin-bottom:20px">
      <template #header><div><el-icon><InfoFilled /></el-icon><span style="margin-left:8px;font-weight:600">操作说明</span></div></template>
      <p style="margin:0;color:#666;font-size:14px;line-height:1.8">
        1. 从下方下拉框中选择一位<strong>教授</strong>，所有导入的数据将自动关联到该教授<br/>
        2. 下载Excel模板，按模板格式填写数据：每行指定<strong>成果类型</strong>（论文/项目/专利/著作/获奖）及相关字段<br/>
        3. 上传填好的Excel文件，系统将自动按类型路由到对应模块并计算积分
      </p>
    </el-card>

    <el-form :inline="true" style="margin-bottom:16px">
      <el-form-item label="选择教授" required>
        <el-select v-model="selectedProfessorId" placeholder="请选择教授" filterable clearable style="width:320px" @change="onProfessorChange">
          <el-option v-for="p in professorList" :key="p.professorId" :label="p.name + ' — ' + (p.department||'') + ' ' + (p.title||'')" :value="p.professorId" />
        </el-select>
      </el-form-item>
      <el-form-item v-if="selectedProfessorId">
        <el-tag type="success" size="large">已选教授积分：{{ selectedProfessor?.totalScore || 0 }}</el-tag>
      </el-form-item>
    </el-form>

    <el-card shadow="hover" style="margin-bottom:20px">
      <template #header><span style="font-weight:600">下载Excel模板</span></template>
      <p style="color:#666;font-size:13px;margin-bottom:12px">模板包含论文/项目/专利/著作/获奖各一行示例，请参照填写</p>
      <el-button type="primary" @click="downloadTemplate"><el-icon><Download /></el-icon>下载模板（含示例）</el-button>
    </el-card>

    <el-card shadow="hover" v-if="selectedProfessorId">
      <template #header><span style="font-weight:600">上传Excel文件</span></template>
      <el-upload ref="uploadRef" :action="uploadUrl" :headers="uploadHeaders" :data="uploadData"
        :auto-upload="false" :limit="1" accept=".xlsx,.xls" :on-success="handleSuccess" :on-error="handleError"
        drag style="margin-bottom:16px">
        <el-icon :size="40" color="#409EFF"><UploadFilled /></el-icon>
        <div class="el-upload__text" style="margin-top:16px">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip><div class="el-upload__tip">仅支持 .xlsx / .xls 格式，每行一种成果</div></template>
      </el-upload>
      <el-button type="success" @click="submitUpload"><el-icon><Upload /></el-icon>开始导入</el-button>
    </el-card>

    <!-- 导入结果 -->
    <el-card shadow="hover" v-if="importResult" style="margin-top:20px">
      <template #header><span style="font-weight:600">导入结果</span></template>
      <div v-html="importResult" style="line-height:2;font-size:14px;max-height:400px;overflow-y:auto"></div>
    </el-card>
  </div>
</template>

<script setup name="BatchImport">
import { ref, computed } from 'vue'
import { getToken } from '@/utils/auth'
import { listAllProfessors } from '@/api/research/professor'

const { proxy } = getCurrentInstance()
const professorList = ref([])
const selectedProfessorId = ref(null)
const importResult = ref('')
const uploadRef = ref(null)
const uploadUrl = import.meta.env.VITE_APP_BASE_API + '/research/batchImport/importData'
const uploadHeaders = { Authorization: 'Bearer ' + getToken() }
const uploadData = computed(() => ({ professorId: selectedProfessorId.value }))

const selectedProfessor = computed(() => professorList.value.find(p => p.professorId === selectedProfessorId.value))

listAllProfessors().then(r => { professorList.value = r.data || [] }).catch(() => { proxy.$modal.msgError('加载教授列表失败') })

function onProfessorChange() { importResult.value = '' }

function downloadTemplate() {
  proxy.download('research/batchImport/importTemplate', {}, '样板 ALPHA.xlsx')
}

function submitUpload() {
  if (!selectedProfessorId.value) { proxy.$modal.msgWarning('请先选择教授'); return }
  uploadRef.value.submit()
}

function handleSuccess(res) {
  if (res.code === 200) {
    importResult.value = res.msg || '导入完成'
    proxy.$modal.msgSuccess('导入完成！可在各子模块查看数据')
  } else {
    importResult.value = '<span style="color:red">导入失败：' + (res.msg || '未知错误') + '</span>'
    proxy.$modal.msgError(res.msg || '导入失败')
  }
}

function handleError() {
  importResult.value = '<span style="color:red">导入失败，请检查文件格式和内容</span>'
  proxy.$modal.msgError('导入失败，请检查文件格式')
}
</script>
