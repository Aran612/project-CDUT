<template>
  <div class="app-container">
    <!-- 年份筛选栏 -->
    <el-row :gutter="10" class="mb20">
      <el-col :span="4">
        <el-input-number v-model="filterYear" :min="2000" :max="2099" placeholder="全部年份" style="width:100%" @change="loadAll"/>
      </el-col>
      <el-col :span="2"><el-button @click="filterYear=null;loadAll()">全部</el-button></el-col>
      <el-col :span="18" style="text-align:right;color:#909399;line-height:32px">
        当前查看：{{ filterYear || '全部年份' }} | 综合总积分：<b>{{ summary.totalAllScore || 0 }}</b>
      </el-col>
    </el-row>

    <!-- 第一行：论文 + 项目 + 专利 + 著作 -->
    <el-row :gutter="16" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">📄 论文</div></template>
          <div class="card-value">{{ summary.totalPapers || 0 }}<span class="card-unit">篇</span></div>
          <div class="card-sub">积分 {{ summary.totalScore || 0 }} | SCI {{ summary.sciPapers || 0 }}篇</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">🔬 科研项目</div></template>
          <div class="card-value">{{ summary.projectCount || 0 }}<span class="card-unit">项</span></div>
          <div class="card-sub">积分 {{ summary.projectScore || 0 }} | 在研{{ summary.projectActive||0 }}/结题{{ summary.projectCompleted||0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">🔒 专利</div></template>
          <div class="card-value">{{ summary.patentCount || 0 }}<span class="card-unit">件</span></div>
          <div class="card-sub">积分 {{ summary.patentScore || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">📚 著作</div></template>
          <div class="card-value">{{ summary.bookCount || 0 }}<span class="card-unit">部</span></div>
          <div class="card-sub">积分 {{ summary.bookScore || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 第二行：获奖 + 奖励积分 + 考核积分 + 产出等级 -->
    <el-row :gutter="16" class="mb20">
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">🏆 获奖</div></template>
          <div class="card-value">{{ summary.awardCount || 0 }}<span class="card-unit">项</span></div>
          <div class="card-sub">积分 {{ summary.awardScore || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">⭐ 奖励积分</div></template>
          <div class="card-value" style="color:#409eff">{{ summary.rewardScore || 0 }}</div>
          <div class="card-sub">论文 {{ summary.rewardPapers || 0 }} 篇</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">🕐 考核积分<el-tooltip content="仅用于岗位聘期考核"><el-icon style="margin-left:2px"><QuestionFilled/></el-icon></el-tooltip></div></template>
          <div class="card-value" style="color:#e6a23c">{{ summary.evalScore || 0 }}</div>
          <div class="card-sub">论文 {{ summary.evalPapers || 0 }} 篇</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover"><template #header><div class="card-header">🏅 学院产出等级</div></template>
          <div class="card-value"><el-tag :type="levelTagType(summary.collegeLevel)" size="large">{{ levelLabel(summary.collegeLevel) }}</el-tag></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card shadow="hover"><template #header><span>📊 成果类型分布</span></template>
          <div ref="typeChartRef" style="height:360px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover"><template #header><span>📋 论文等级分布</span></template>
          <div ref="gradeChartRef" style="height:360px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 排行榜 + SCI细分 -->
    <el-row :gutter="20" class="mb20">
      <el-col :span="12">
        <el-card shadow="hover"><template #header>
          <div style="display:flex;justify-content:space-between;align-items:center">
            <span>📈 教授综合排行榜</span>
            <span style="font-size:12px;color:#909399">论文+项目+专利+著作+获奖</span>
          </div>
        </template>
          <div ref="rankChartRef" style="height:360px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover"><template #header><span>🔬 SCI/SCIE 论文细分</span></template>
          <el-table :data="sciBreakdownList" size="small">
            <el-table-column label="类别" prop="label" width="160"/>
            <el-table-column label="篇数" prop="count" width="70" align="center"/>
            <el-table-column label="积分" prop="score" width="80" align="center"/>
            <el-table-column label="性质" width="70" align="center">
              <template #d="s"><el-tag v-if="s.row.evalOnly" type="warning" size="small">考核</el-tag><el-tag v-else type="success" size="small">奖励</el-tag></template>
            </el-table-column>
            <el-table-column label="占比" prop="pct"><template #d="s"><el-progress :percentage="s.row.pct" :color="s.row.color" :format="()=>s.row.pct+'%'"/></template></el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- 教授明细表 -->
    <el-card shadow="hover">
      <template #header><span>📋 教授综合明细表</span></template>
      <el-table v-loading="loading" :data="rankingList" :default-sort="{prop:'total_score',order:'descending'}">
        <el-table-column label="排名" align="center" width="55" type="index"/>
        <el-table-column label="教授" prop="professor_name" width="100"/>
        <el-table-column label="部门" prop="department" show-overflow-tooltip/>
        <el-table-column label="论文" width="70" align="center"><template #d="s">{{ s.row.paper_count||0 }}</template></el-table-column>
        <el-table-column label="项目" width="70" align="center"><template #d="s">{{ s.row.project_count||0 }}</template></el-table-column>
        <el-table-column label="专利" width="70" align="center"><template #d="s">{{ s.row.patent_count||0 }}</template></el-table-column>
        <el-table-column label="著作" width="70" align="center"><template #d="s">{{ s.row.book_count||0 }}</template></el-table-column>
        <el-table-column label="获奖" width="70" align="center"><template #d="s">{{ s.row.award_count||0 }}</template></el-table-column>
        <el-table-column label="总积分" prop="total_score" width="100" sortable align="center"/>
        <el-table-column label="产出等级" width="120" align="center">
          <template #d="s"><el-tag :type="levelTagType(s.row.level)">{{ levelLabel(s.row.level) }}</el-tag></template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup name="ResearchStats">
import * as echarts from 'echarts'
import { getSummary, getRanking } from "@/api/research/stats"
const { proxy } = getCurrentInstance()
const summary = ref({})
const rankingList = ref([])
const loading = ref(false)
const filterYear = ref(null)
const typeChartRef = ref(null)
const gradeChartRef = ref(null)
const rankChartRef = ref(null)
let typeChart=null, gradeChart=null, rankChart=null

const SCI_LABELS = {NSC:'N/S/C 正刊/子刊/PNAS',SCIE_Q1:'SCIE/SSCI 一区',SCIE_Q2:'SCIE/SSCI 二区',NI:'NI 指数期刊',AHCI:'A&HCI',SCIE_Q3:'SCIE/SSCI 三区',SCI_OTHER:'SCI/SSCI 其他',EI:'EI 收录',CSSCI:'CSSCI 系列',REGULAR:'普通期刊'}
const EVAL_CATS = ['SCIE_Q3','SCI_OTHER','EI']
const sciBreakdownList = ref([])

function levelTagType(l){return {S:'danger',A:'warning',B:'',C:'success',D:'info'}[l]||'info'}
function levelLabel(l){return {S:'S级(杰出)',A:'A级(优秀)',B:'B级(良好)',C:'C级(合格)',D:'D级(待提升)'}[l]||l}

function loadAll(){loadSummary();loadRanking()}

function loadSummary(){
  getSummary(filterYear.value||null).then(r=>{
    summary.value = r.data
    renderTypeChart(r.data.typeDistribution||{})
    renderGradeChart(r.data.gradeStats||[])
    buildSciBreakdown(r.data.sciBreakdown||{})
  })
}

function loadRanking(){
  loading.value=true
  getRanking(filterYear.value||null).then(r=>{
    rankingList.value = r.data||[]
    loading.value=false
    renderRankChart(rankingList.value)
  })
}

function buildSciBreakdown(bd){
  const total = summary.value.totalPapers||1
  let idx=0
  sciBreakdownList.value = Object.entries(bd).map(([k,v])=>({
    label:SCI_LABELS[k]||k, count:v.count||0, score:v.score||0, evalOnly:EVAL_CATS.includes(k),
    pct:Math.round((v.count||0)/total*100), color:['#409eff','#67c23a','#e6a23c','#f56c6c','#909399','#5470c6','#91cc75','#fac858','#ee6666','#73c0de'][idx++%10]
  })).filter(i=>i.count>0)
}

function renderTypeChart(data){
  if(!typeChartRef.value)return
  if(typeChart){typeChart.dispose()}
  typeChart = echarts.init(typeChartRef.value,'macarons')
  const d = Object.entries(data).map(([k,v])=>({name:k,value:v||0}))
  typeChart.setOption({
    tooltip:{trigger:'item',formatter:'{b}: {c} 分 ({d}%)'},
    legend:{orient:'vertical',left:'left',top:'center'},
    series:[{name:'成果类型',type:'pie',radius:['45%','70%'],center:['60%','50%'],avoidLabelOverlap:false,
      itemStyle:{borderRadius:4,borderColor:'#fff',borderWidth:2},
      label:{show:true,formatter:'{b}\n{c}分'},emphasis:{label:{fontSize:14,fontWeight:'bold'}},data:d}]
  })
  window.addEventListener('resize',()=>typeChart&&typeChart.resize())
}

function renderGradeChart(data){
  if(!gradeChartRef.value)return
  if(gradeChart){gradeChart.dispose()}
  gradeChart = echarts.init(gradeChartRef.value,'macarons')
  const d = data.map(i=>({name:i.grade_name,value:i.count||0}))
  gradeChart.setOption({
    tooltip:{trigger:'item',formatter:'{b}: {c} 篇 ({d}%)'},
    legend:{orient:'vertical',left:'left',top:'center'},
    series:[{name:'论文等级',type:'pie',radius:['40%','65%'],center:['60%','50%'],avoidLabelOverlap:false,
      itemStyle:{borderRadius:4,borderColor:'#fff',borderWidth:2},label:{show:true,formatter:'{b}\n{c}篇'},data:d}]
  })
  window.addEventListener('resize',()=>gradeChart&&gradeChart.resize())
}

function renderRankChart(data){
  if(!rankChartRef.value)return
  if(rankChart){rankChart.dispose()}
  rankChart = echarts.init(rankChartRef.value,'macarons')
  const top = data.slice(0,15)
  rankChart.setOption({
    tooltip:{trigger:'axis',axisPointer:{type:'shadow'}},
    grid:{left:'3%',right:'8%',bottom:'3%',containLabel:true},
    xAxis:{type:'value',name:'积分'},
    yAxis:{type:'category',data:top.map(i=>i.professor_name).reverse(),axisLabel:{width:70,overflow:'truncate'}},
    series:[{name:'综合积分',type:'bar',data:top.map(i=>i.total_score||0).reverse(),
      itemStyle:{color:new echarts.graphic.LinearGradient(0,0,1,0,[{offset:0,color:'#83bff6'},{offset:0.5,color:'#188df0'},{offset:1,color:'#188df0'}])},
      barWidth:20,label:{show:true,position:'right',formatter:'{c}分'}}]
  })
  window.addEventListener('resize',()=>rankChart&&rankChart.resize())
}

onMounted(()=>{loadAll()})
onBeforeUnmount(()=>{[typeChart,gradeChart,rankChart].forEach(c=>c&&c.dispose())})
</script>

<style scoped>
.mb20{margin-bottom:20px}
.card-header{display:flex;align-items:center;gap:6px;font-size:14px;color:#606266}
.card-value{font-size:28px;font-weight:bold;color:#303133;text-align:center;padding:8px 0}
.card-unit{font-size:14px;font-weight:normal;color:#909399;margin-left:4px}
.card-sub{font-size:12px;color:#909399;text-align:center;margin-top:-4px}
</style>
