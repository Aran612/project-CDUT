<template>
  <div class="app-container">
    <el-form :model="qp" ref="qf" :inline="true" v-show="ss" label-width="80px">
      <el-form-item label="获奖名称" prop="awardName"><el-input v-model="qp.awardName" placeholder="请输入" clearable @keyup.enter="hq"/></el-form-item>
      <el-form-item label="颁发单位" prop="awardOrg"><el-input v-model="qp.awardOrg" placeholder="请输入" clearable @keyup.enter="hq"/></el-form-item>
      <el-form-item><el-button type="primary" icon="Search" @click="hq">搜索</el-button><el-button icon="Refresh" @click="rq">重置</el-button></el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"><el-button type="primary" plain icon="Plus" @click="ha" v-hasPermi="['research:award:add']">新增</el-button></el-col>
      <el-col :span="1.5"><el-button type="success" plain icon="Edit" :disabled="single" @click="hu" v-hasPermi="['research:award:edit']">修改</el-button></el-col>
      <el-col :span="1.5"><el-button type="danger" plain icon="Delete" :disabled="multiple" @click="hd" v-hasPermi="['research:award:remove']">删除</el-button></el-col>
      <el-col :span="1.5"><el-button type="warning" plain icon="Download" @click="he" v-hasPermi="['research:award:export']">导出</el-button></el-col>
      <el-col :span="1.5"><el-button type="info" plain icon="Upload" @click="hi" v-hasPermi="['research:award:import']">导入</el-button></el-col>
      <right-toolbar v-model:showSearch="ss" @queryTable="gl"/>
    </el-row>
    <el-table v-loading="loading" :data="list" @selection-change="hsc">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" prop="awardId" width="70" align="center"/>
      <el-table-column label="获奖名称" prop="awardName" show-overflow-tooltip min-width="220"/>
      <el-table-column label="获奖等级" prop="gradeName" width="180"><template #d="s"><el-tag :type="at(s.row.gradeName)">{{ s.row.gradeName||'-' }}</el-tag></template></el-table-column>
      <el-table-column label="获奖人" prop="professorName" width="100"/>
      <el-table-column label="排名" prop="awardRank" width="100"/>
      <el-table-column label="颁发单位" prop="awardOrg" width="160" show-overflow-tooltip/>
      <el-table-column label="获奖日期" prop="awardDate" width="110"><template #d="s"><span>{{ parseTime(s.row.awardDate,'{y}-{m}-{d}') }}</span></template></el-table-column>
      <el-table-column label="证书编号" prop="certNumber" width="140"/>
      <el-table-column label="积分" prop="actualScore" width="90" sortable align="center"/>
      <el-table-column label="操作" width="160" align="center"><template #d="s">
        <el-button link type="primary" icon="Edit" @click="hu(s.row)" v-hasPermi="['research:award:edit']">修改</el-button>
        <el-button link type="primary" icon="Delete" @click="hd(s.row)" v-hasPermi="['research:award:remove']">删除</el-button>
      </template></el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" v-model:page="qp.pageNum" v-model:limit="qp.pageSize" @pagination="gl"/>

    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="fr" :model="f" :rules="rules" label-width="100px">
        <el-form-item label="获奖名称" prop="awardName"><el-input v-model="f.awardName"/></el-form-item>
        <el-row><el-col :span="12"><el-form-item label="获奖等级" prop="gradeId"><el-select v-model="f.gradeId" filterable style="width:100%"><el-option v-for="g in grades" :key="g.id" :label="g.label" :value="g.id"/></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="获奖人" prop="professorId"><el-select v-model="f.professorId" filterable style="width:100%"><el-option v-for="p in profs" :key="p.professorId" :label="p.name" :value="p.professorId"/></el-select></el-form-item></el-col></el-row>
        <el-row><el-col :span="12"><el-form-item label="完成人排名"><el-select v-model="f.awardRank" style="width:100%"><el-option v-for="r in ranks" :key="r" :label="r" :value="r"/></el-select></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="颁发单位"><el-input v-model="f.awardOrg"/></el-form-item></el-col></el-row>
        <el-row><el-col :span="12"><el-form-item label="获奖日期"><el-date-picker v-model="f.awardDate" type="date" value-format="YYYY-MM-DD" style="width:100%"/></el-form-item></el-col>
        <el-col :span="12"><el-form-item label="证书编号"><el-input v-model="f.certNumber"/></el-form-item></el-col></el-row>
        <el-form-item label="积分"><el-input-number v-model="f.actualScore" :min="0" :max="99999"/></el-form-item>
        <el-form-item label="状态"><el-radio-group v-model="f.status"><el-radio value="0">正常</el-radio><el-radio value="1">停用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="备注"><el-input v-model="f.remark" type="textarea"/></el-form-item>
      </el-form>
      <template #footer><el-button type="primary" @click="sf">确 定</el-button><el-button @click="cancel">取 消</el-button></template>
    </el-dialog>

    <el-dialog title="导入获奖" v-model="io" width="400px" append-to-body>
      <el-upload ref="ur" :action="uu" :headers="uh" :data="{updateSupport:iu}"
        :auto-upload="false" :limit="1" accept=".xlsx,.xls" :on-success="his" :on-error="hie" drag>
        <el-icon class="el-icon--upload"><upload-filled /></el-icon>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <template #tip><div class="el-upload__tip">仅支持 .xlsx / .xls 格式</div></template>
      </el-upload>
      <el-checkbox v-model="iu" style="margin-top:12px">是否更新已存在的获奖数据</el-checkbox>
      <template #footer>
        <el-button @click="io=false">取 消</el-button>
        <el-button type="primary" @click="dt">下载模板</el-button>
        <el-button type="success" @click="su">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Award">
import { getToken } from '@/utils/auth'
import { listAward,getAward,delAward,addAward,updateAward,importTemplate } from "@/api/research/award"
import { listProfessor } from "@/api/research/professor"
const { proxy } = getCurrentInstance()
const grades = [
  {id:1,label:'国家自科/技术发明/科技进步 特等奖(10000)'},{id:2,label:'国家自科/技术发明/科技进步 一等奖(5000)'},
  {id:3,label:'国家自科/技术发明/科技进步 二等奖(3000)'},{id:4,label:'中国专利奖金奖(3000)'},
  {id:5,label:'中国专利奖银奖(2000)'},{id:6,label:'中国专利奖优秀奖(1000)'},
  {id:7,label:'省自科/技术发明/科技进步 特等奖(3000)'},{id:8,label:'省自科/技术发明/科技进步 一等奖(1000)'},
  {id:9,label:'省自科/技术发明/科技进步 二等奖(600)'},{id:10,label:'省自科/技术发明/科技进步 三等奖(400)'},
  {id:11,label:'省部级专利奖特等奖(800)'},{id:12,label:'省部级专利奖一等奖(500)'},{id:13,label:'省部级专利奖二等奖(300)'},{id:14,label:'省部级专利奖三等奖(200)'},
  {id:15,label:'教育部人文社科优秀成果 特等奖(6000)'},{id:16,label:'教育部人文社科优秀成果 一等奖(4000)'},
  {id:17,label:'教育部人文社科优秀成果 二等奖(2000)'},{id:18,label:'教育部人文社科优秀成果 三等奖(1000)'},
  {id:19,label:'中央部委社科优秀成果 特等/一等奖(1500)'},{id:20,label:'省社科优秀成果荣誉奖(500)'},
  {id:21,label:'省社科优秀成果一等奖(300)'},{id:22,label:'省社科优秀成果二等奖(200)'},{id:23,label:'省社科优秀成果三等奖(100)'},
  {id:24,label:'副省级城市社科优秀成果一等奖(300)'},
  {id:25,label:'国家教学成果特等奖(20000)'},{id:26,label:'国家教学成果一等奖(10000)'},{id:27,label:'国家教学成果二等奖(5000)'},
  {id:28,label:'省教学成果特等奖(2000)'},{id:29,label:'省教学成果一等奖(1000)'},{id:30,label:'省教学成果二等奖(600)'},
  {id:31,label:'学会/教指委/校级教学成果特等奖(300)'},{id:32,label:'其他教学研究成果奖(100)'}
]
const ranks = ['第一完成人','第二完成人','第三完成人','第四完成人','第五完成人','其他完成人']
function at(n){if(!n)return'info';if(n.includes('国家')||n.includes('特等'))return'danger';if(n.includes('省'))return'warning';return'success'}
const list=ref([]),profs=ref([]),open=ref(false),loading=ref(true),ss=ref(true),ids=ref([]),single=ref(true),multiple=ref(true),total=ref(0),title=ref('')
const data=reactive({f:{},qp:{pageNum:1,pageSize:10,awardName:null,awardOrg:null,status:null},rules:{awardName:[{required:true,message:'不能为空',trigger:'blur'}],gradeId:[{required:true,message:'不能为空',trigger:'change'}],professorId:[{required:true,message:'不能为空',trigger:'change'}]}})
const {qp,f,rules}=toRefs(data)
function gl(){loading.value=true;listAward(qp.value).then(r=>{list.value=r.rows;total.value=r.total;loading.value=false})}
function cancel(){open.value=false;rs()}
function rs(){f.value={awardId:null,awardName:null,gradeId:null,professorId:null,awardRank:'第一完成人',awardOrg:null,awardDate:null,certNumber:null,actualScore:null,status:'0',remark:null};proxy.resetForm('fr')}
function hq(){qp.value.pageNum=1;gl()}
function rq(){proxy.resetForm('qf');hq()}
function hsc(s){ids.value=s.map(i=>i.awardId);single.value=s.length!=1;multiple.value=!s.length}
function ha(){rs();listProfessor({pageNum:1,pageSize:1000}).then(r=>{profs.value=r.rows});open.value=true;title.value='添加获奖'}
function hu(row){rs();listProfessor({pageNum:1,pageSize:1000}).then(r=>{profs.value=r.rows});getAward(row.awardId||ids.value).then(r=>{f.value=r.data;open.value=true;title.value='修改获奖'})}
function sf(){proxy.$refs['fr'].validate(v=>{if(v){(f.value.awardId?updateAward(f.value):addAward(f.value)).then(()=>{proxy.$modal.msgSuccess('操作成功');open.value=false;gl()})}})}
function hd(row){proxy.$modal.confirm('确认删除?').then(()=>delAward(row.awardId||ids.value)).then(()=>{gl();proxy.$modal.msgSuccess('删除成功')}).catch(()=>{})}
function he(){ proxy.download('research/award/export',{...qp.value},'award_'+new Date().getTime()+'.xlsx') }
const io=ref(false),iu=ref(false),ur=ref(null)
const uu=ref(import.meta.env.VITE_APP_BASE_API+"/research/award/importData")
const uh=ref({Authorization:"Bearer "+getToken()})
function hi(){io.value=true}
function dt(){proxy.download('research/award/importTemplate',{},'获奖导入模板.xlsx')}
function su(){ur.value.submit()}
function his(r){if(r.code===200){proxy.$modal.msgSuccess(r.msg||"导入成功");io.value=false;gl()}else proxy.$modal.msgError(r.msg||"导入失败")}
function hie(){proxy.$modal.msgError("导入失败，请检查文件格式")}
gl()
</script>
