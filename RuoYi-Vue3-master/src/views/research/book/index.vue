<template>
  <div class="app-container">
    <el-form :model="qp" ref="qf" :inline="true" v-show="ss" label-width="80px">
      <el-form-item label="著作名称" prop="bookName"><el-input v-model="qp.bookName" placeholder="请输入" clearable @keyup.enter="hq"/></el-form-item>
      <el-form-item label="出版社" prop="publisher"><el-input v-model="qp.publisher" placeholder="请输入" clearable @keyup.enter="hq"/></el-form-item>
      <el-form-item><el-button type="primary" icon="Search" @click="hq">搜索</el-button><el-button icon="Refresh" @click="rq">重置</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="ha" v-hasPermi="['research:book:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="Edit" :disabled="single" @click="hu" v-hasPermi="['research:book:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="Delete" :disabled="multiple" @click="hd" v-hasPermi="['research:book:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="Download" @click="he" v-hasPermi="['research:book:export']">导出</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain icon="Upload" @click="hi" v-hasPermi="['research:book:import']">导入</el-button></el-col>
      <right-toolbar v-model:showSearch="ss" @queryTable="gl"/>
    </el-row>
    <el-table v-loading="loading" :data="list" @selection-change="hsc">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" prop="bookId" width="70" align="center"/>
      <el-table-column label="著作名称" prop="bookName" show-overflow-tooltip min-width="200"/>
      <el-table-column label="等级" prop="gradeName" width="140"><template #d="s"><el-tag>{{ s.row.gradeName||'-' }}</el-tag></template></el-table-column>
      <el-table-column label="作者" prop="professorName" width="100"/>
      <el-table-column label="角色" prop="authorRole" width="100"/>
      <el-table-column label="出版社" prop="publisher" width="160" show-overflow-tooltip/>
      <el-table-column label="字数(万)" prop="wordCount" width="90" align="center"/>
      <el-table-column label="ISBN" prop="isbn" width="140"/>
      <el-table-column label="出版日期" prop="publishDate" width="110"><template #d="s"><span>{{ parseTime(s.row.publishDate,'{y}-{m}-{d}') }}</span></template></el-table-column>
      <el-table-column label="积分" prop="actualScore" width="80" sortable align="center"/>
      <el-table-column label="操作" width="160" align="center"><template #d="s">
        <el-button link type="primary" icon="Edit" @click="hu(s.row)" v-hasPermi="['research:book:edit']">修改</el-button>
        <el-button link type="primary" icon="Delete" @click="hd(s.row)" v-hasPermi="['research:book:remove']">删除</el-button>
      </template></el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="qp.pageNum" v-model:limit="qp.pageSize" @pagination="gl"/>

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="fr" :model="f" :rules="rules" label-width="100px">
        <el-form-item label="著作名称" prop="bookName"><el-input v-model="f.bookName"/></el-form-item>
        <el-row><el-col :span="12"><el-form-item label="著作等级" prop="gradeId"><el-select v-model="f.gradeId" style="width:100%"><el-option v-for="g in grades" :key="g.id" :label="g.label" :value="g.id"/></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="作者" prop="professorId"><el-select v-model="f.professorId" filterable style="width:100%"><el-option v-for="p in profs" :key="p.professorId" :label="p.name" :value="p.professorId"/></el-select></el-form-item></el-col></el-row>
        <el-row><el-col :span="12"><el-form-item label="作者角色"><el-select v-model="f.authorRole" style="width:100%"><el-option v-for="r in roles" :key="r" :label="r" :value="r"/></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="字数(万)"><el-input-number v-model="f.wordCount" :min="0" style="width:100%"/></el-form-item></el-col></el-row>
        <el-form-item label="出版社"><el-input v-model="f.publisher"/></el-form-item>
        <el-row><el-col :span="12"><el-form-item label="出版日期"><el-date-picker v-model="f.publishDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="ISBN"><el-input v-model="f.isbn"/></el-form-item></el-col></el-row>
        <el-form-item label="积分"><el-input-number v-model="f.actualScore" :min="0" :max="9999"/></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="f.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="f.remark" type="textarea"/></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="sf">确 定</el-button><el-button @click="cancel">取 消</el-button></template>
    </el-dialog>

    <el-dialog title="导入著作" v-model="io" width="400px" append-to-body>
      <el-upload ref="ur" :action="uu" :headers="uh" :data="{updateSupport:iu}"
        :auto-upload="false" :limit="1" accept=".xlsx,.xls" :on-success="his" :on-error="hie" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip><div class="el-upload__tip">仅支持 .xlsx / .xls 格式</div></template>
      </el-upload>
      <el-checkbox v-model="iu" style="margin-top:12px">是否更新已存在的著作数据</el-checkbox>
      <template #footer>
        <el-button @click="io=false">取 消</el-button>
        <el-button type="primary" @click="dt">下载模板</el-button>
        <el-button type="success" @click="su">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Book">
import { getToken } from '@/utils/auth'
import { listBook,getBook,delBook,addBook,updateBook,importTemplate } from "@/api/research/book"
import { listProfessor } from "@/api/research/professor"
const { proxy } = getCurrentInstance()
const grades = [
  {id:1,label:'中宣部主题出版重点出版物(500分)'},{id:2,label:'中国出版政府奖/国家哲社成果文库(300分)'},
  {id:3,label:'国家重点出版物规划/思政文库(300分)'},{id:4,label:'国外知名出版社出版著作(200分)'},
  {id:5,label:'国家出版基金等资助出版物(200分)'},{id:6,label:'国社科一般/青年等项目出版著作(200分)'},
  {id:7,label:'中央级/优秀出版社出版著作(100分)'},{id:8,label:'国内其他出版社出版专著(50分)'},
  {id:9,label:'国内其他出版社其他著作(50分)'},{id:10,label:'编著(20分)'},
  {id:11,label:'国家级规划教材(200分)'},{id:12,label:'省部级规划教材(100分)'},{id:13,label:'其他正式出版教材(30分)'}
]
const roles = ['著','编著','译','主编','副主编','参编']
const list=ref([]),profs=ref([]),open=ref(false),loading=ref(true),ss=ref(true),ids=ref([]),single=ref(true),multiple=ref(true),total=ref(0),title=ref('')
const data=reactive({f:{},qp:{pageNum:1,pageSize:10,bookName:null,publisher:null,status:null},rules:{bookName:[{required:true,message:'不能为空',trigger:'blur'}],gradeId:[{required:true,message:'不能为空',trigger:'change'}],professorId:[{required:true,message:'不能为空',trigger:'change'}]}})
const {qp,f,rules}=toRefs(data)
function gl(){loading.value=true;listBook(qp.value).then(r=>{list.value=r.rows;total.value=r.total;loading.value=false})}
function cancel(){open.value=false;rs()}
function rs(){f.value={bookId:null,bookName:null,gradeId:null,professorId:null,authorRole:'著',publisher:null,publishDate:null,wordCount:null,isbn:null,actualScore:null,status:'0',remark:null};proxy.resetForm('fr')}
function hq(){qp.value.pageNum=1;gl()}
function rq(){proxy.resetForm('qf');hq()}
function hsc(s){ids.value=s.map(i=>i.bookId);single.value=s.length!=1;multiple.value=!s.length}
function ha(){rs();listProfessor({pageNum:1,pageSize:1000}).then(r=>{profs.value=r.rows});open.value=true;title.value='添加著作'}
function hu(row){rs();listProfessor({pageNum:1,pageSize:1000}).then(r=>{profs.value=r.rows});getBook(row.bookId||ids.value).then(r=>{f.value=r.data;open.value=true;title.value='修改著作'})}
function sf(){proxy.$refs['fr'].validate(v=>{if(v){(f.value.bookId?updateBook(f.value):addBook(f.value)).then(()=>{proxy.$modal.msgSuccess('操作成功');open.value=false;gl()})}})}
function hd(row){proxy.$modal.confirm('确认删除?').then(()=>delBook(row.bookId||ids.value)).then(()=>{gl();proxy.$modal.msgSuccess('删除成功')}).catch(()=>{})}
function he(){ proxy.download('research/book/export',{...qp.value},'book_'+new Date().getTime()+'.xlsx') }
const io=ref(false),iu=ref(false),ur=ref(null)
const uu=ref(import.meta.env.VITE_APP_BASE_API+"/research/book/importData")
const uh=ref({Authorization:"Bearer "+getToken()})
function hi(){io.value=true}
function dt(){proxy.download('research/book/importTemplate',{},'著作导入模板.xlsx')}
function su(){ur.value.submit()}
function his(r){if(r.code===200){proxy.$modal.msgSuccess(r.msg||"导入成功");io.value=false;gl()}else proxy.$modal.msgError(r.msg||"导入失败")}
function hie(){proxy.$modal.msgError("导入失败，请检查文件格式")}
gl()
</script>
