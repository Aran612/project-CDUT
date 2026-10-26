<template>
  <div class="app-container" v-loading="loading">
    <!-- 代录模式提示 -->
    <el-alert v-if="manageMode" type="warning" show-icon :closable="false" class="mb8">
      <template #title>
        正在代录：<b>{{ professorName || ('教师ID ' + targetProfessorId) }}</b> 的 CV，所有保存操作将写入该教师档案（管理员模式 Admin Mode）
      </template>
    </el-alert>

    <!-- 顶部：基本信息档案卡片 -->
    <el-card shadow="never" class="mb8">
      <template #header>
        <div class="card-header">
          <span class="card-title">基本信息 Profile（CV 第 1-2 部分）</span>
          <span>
            <el-button type="info" plain icon="QuestionFilled" @click="helpOpen = true">填写说明</el-button>
            <el-button type="warning" plain icon="Download" :disabled="!exportId" @click="handleExportCv">导出 Word CV</el-button>
          </span>
        </div>
      </template>

      <el-form ref="profileRef" :model="profileForm" label-width="140px">
        <el-row :gutter="20">
          <el-col :span="5">
            <el-form-item label="证件照 Photo" prop="photoUrl">
              <image-upload v-model="profileForm.photoUrl" :limit="1" :file-size="5" />
            </el-form-item>
          </el-col>
          <el-col :span="19">
            <el-row>
              <el-col :span="8">
                <el-form-item label="CDUT工号" prop="cdutId">
                  <el-input v-model="profileForm.cdutId" placeholder="请输入员工编号" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="联系电话 Phone" prop="phone">
                  <el-input v-model="profileForm.phone" placeholder="请输入联系电话" />
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="岗位/职务 Position" prop="position">
                  <el-input v-model="profileForm.position" placeholder="如：教授 / 系主任" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="任教类型" prop="employmentType">
                  <el-radio-group v-model="profileForm.employmentType">
                    <el-radio value="1">本院全职</el-radio>
                    <el-radio value="2">外院兼职</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item label="AACSB师资分类" prop="aacsbClass">
                  <el-radio-group v-model="profileForm.aacsbClass">
                    <el-tooltip v-for="c in aacsbClasses" :key="c.value" :content="c.tip" placement="top" effect="dark">
                      <el-radio :value="c.value">{{ c.label }}</el-radio>
                    </el-tooltip>
                  </el-radio-group>
                  <div class="form-tip">不确定请先与认证办沟通确认后再选择</div>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="所属单位 Employer" prop="employer">
              <el-input v-model="profileForm.employer" placeholder="如：成都理工大学管理科学学院" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通讯地址 Address" prop="address">
              <el-input v-model="profileForm.address" placeholder="请输入通讯地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="研究与教学领域" prop="areasOfInterest">
          <el-input
            v-model="profileForm.areasOfInterest"
            type="textarea"
            :rows="4"
            placeholder="每行一条，建议 3-5 条（中英文），例如：&#10;战略管理与竞争分析 Strategic Management & Competitive Analysis&#10;数字化转型与商业模式创新 Digital Transformation & Business Model Innovation&#10;公司金融与资本市场 Corporate Finance & Capital Markets"
          />
        </el-form-item>
        <el-form-item label="博后/访学补充" prop="otherInfo">
          <el-input
            v-model="profileForm.otherInfo"
            type="textarea"
            :rows="2"
            placeholder="博士后经历、海外访学等补充信息（CV 第 3 部分 Other Information），没有可不填"
          />
        </el-form-item>
        <el-form-item label="真实性声明" prop="declareFlag">
          <el-checkbox v-model="profileForm.declareFlag" true-value="1" false-value="0">
            本人确认以上信息真实、准确、完整，同意用于 AACSB 认证材料（Declaration of Accuracy）
          </el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Check" @click="handleSaveProfile">保存基本信息</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 八大板块 -->
    <el-card shadow="never">
      <el-tabs v-model="activeTab">
        <!-- 1. 教育背景 -->
        <el-tab-pane label="教育背景 Education" name="education">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="按学位由高到低填写全部学历学位并标注最高学位；学位名称尽量给英文；学位与所授课程匹配度是 AACSB 资质判断重要依据" />
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('education')">新增教育背景</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.educations" border>
            <el-table-column label="学位 Degree" align="center" prop="degree" min-width="140" show-overflow-tooltip />
            <el-table-column label="专业 Major" align="center" prop="major" min-width="160" show-overflow-tooltip />
            <el-table-column label="授予院校 Institution" align="center" prop="institution" min-width="200" show-overflow-tooltip />
            <el-table-column label="年份 Year" align="center" prop="gradYear" width="100" />
            <el-table-column label="最高学位" align="center" prop="isHighest" width="90">
              <template #default="scope">
                <el-tag v-if="scope.row.isHighest === '1'" type="success">是</el-tag>
                <span v-else>否</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('education', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('education', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 2. 工作经历（学术 / 行业实务 两个子表） -->
        <el-tab-pane label="工作经历 Experience" name="experience">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="行业实务经历是 PA/SP/IP 举证核心，写明时长、层级、职责；近5年为主" />
          <div class="sub-title">学术经历 Academic Experience</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('experience', null, '1')">新增学术经历</el-button>
            </el-col>
          </el-row>
          <el-table :data="experienceList('1')" border class="mb16">
            <el-table-column label="起止时间 Period" align="center" prop="period" width="170" />
            <el-table-column label="单位与职务 Organization" align="center" prop="organization" min-width="200" show-overflow-tooltip />
            <el-table-column label="主要职责/业绩 Description" align="center" prop="description" min-width="260" show-overflow-tooltip />
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('experience', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('experience', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
          <div class="sub-title">行业实务经历 Industry / Professional Experience</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('experience', null, '2')">新增行业实务经历</el-button>
            </el-col>
          </el-row>
          <el-table :data="experienceList('2')" border>
            <el-table-column label="起止时间 Period" align="center" prop="period" width="170" />
            <el-table-column label="单位 Organization" align="center" prop="organization" min-width="180" show-overflow-tooltip />
            <el-table-column label="职务/岗位 Role" align="center" prop="role" width="140" show-overflow-tooltip />
            <el-table-column label="时长/层级/职责 Description" align="center" prop="description" min-width="260" show-overflow-tooltip />
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('experience', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('experience', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 3. 职业资格 -->
        <el-tab-pane label="职业资格 Certification" name="certification">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="PA/SP/IP 举证重要内容，注明取得时间与现状（有效/已更新）" />
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('certification')">新增职业资格</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.certifications" border>
            <el-table-column label="证书名称 Certificate" align="center" prop="certName" min-width="200" show-overflow-tooltip />
            <el-table-column label="颁发机构 Issuing Body" align="center" prop="issuingBody" min-width="180" show-overflow-tooltip />
            <el-table-column label="取得年份 Year" align="center" prop="certYear" width="110" />
            <el-table-column label="现状 Status" align="center" prop="statusText" width="110">
              <template #default="scope">
                <el-tag :type="scope.row.statusText === '有效' ? 'success' : 'warning'">{{ scope.row.statusText || '-' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('certification', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('certification', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 4. 授课课程 -->
        <el-tab-pane label="授课课程 Courses Taught" name="course">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="近5年讲授的全部课程；课程名称中英文、层次(本科/硕士/MBA)、学年、角色(主讲/助教)、周学时" />
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('course')">新增课程</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button type="success" plain icon="Refresh" @click="handleSyncCourses">从授课记录同步</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.courses" border>
            <el-table-column label="课程名称 Course" align="center" prop="courseName" min-width="220" show-overflow-tooltip />
            <el-table-column label="层次 Level" align="center" prop="levelName" width="90" />
            <el-table-column label="学年 Academic Year" align="center" prop="academicYear" width="120" />
            <el-table-column label="角色 Role" align="center" prop="teachRole" width="90" />
            <el-table-column label="周学时 Hours/Week" align="center" prop="weeklyHours" width="110" />
            <el-table-column label="来源" align="center" prop="source" width="110">
              <template #default="scope">
                <el-tag :type="scope.row.source === '1' ? 'success' : 'info'" size="small">
                  {{ scope.row.source === '1' ? '授课记录同步' : '手工录入' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('course', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('course', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 5. 荣誉与科研项目（只读 + 新增写入科研库） -->
        <el-tab-pane label="荣誉与科研项目 Honors & Projects" name="research">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="数据自动导入自科研库（近6年窗口）；新增将写入科研库并计入科研积分" />
          <div class="sub-title">荣誉获奖 Honors & Awards</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openResearchDialog('award')">新增获奖</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.awards" border class="mb16">
            <el-table-column label="获奖名称" align="center" prop="awardName" min-width="220" show-overflow-tooltip />
            <el-table-column label="等级" align="center" width="150">
              <template #default="scope">
                <el-tag type="warning" size="small">{{ gradeNameOf(cvData.awardGrades, scope.row.gradeId) || '未定级' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="完成人排名" align="center" prop="awardRank" width="110" />
            <el-table-column label="颁发单位" align="center" prop="awardOrg" min-width="150" show-overflow-tooltip />
            <el-table-column label="获奖日期" align="center" prop="awardDate" width="110">
              <template #default="scope">
                <span>{{ parseTime(scope.row.awardDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
          </el-table>
          <div class="sub-title">科研项目 Research Projects</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openResearchDialog('project')">新增项目</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.projects" border>
            <el-table-column label="项目名称" align="center" prop="projectName" min-width="220" show-overflow-tooltip />
            <el-table-column label="等级" align="center" width="150">
              <template #default="scope">
                <el-tag type="warning" size="small">{{ gradeNameOf(cvData.projectGrades, scope.row.gradeId) || '未定级' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="承担角色" align="center" prop="role" width="90" />
            <el-table-column label="经费(万元)" align="center" prop="fundingAmount" width="100" />
            <el-table-column label="项目来源" align="center" prop="projectSource" min-width="140" show-overflow-tooltip />
            <el-table-column label="起止日期" align="center" width="200">
              <template #default="scope">
                <span>{{ parseTime(scope.row.startDate, '{y}-{m}-{d}') }} ~ {{ parseTime(scope.row.endDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 6. 学术成果（三个只读表 + 新增弹窗） -->
        <el-tab-pane label="学术成果 Publications" name="outputs">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="论文须注明收录/分级(SSCI/SCI/EI/CSSCI等)；近5年为主" />
          <div class="sub-title">期刊论文 Journal Papers</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openResearchDialog('paper')">新增论文</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.papers" border class="mb16">
            <el-table-column label="题目 Title" align="center" prop="title" min-width="240" show-overflow-tooltip />
            <el-table-column label="等级" align="center" width="130">
              <template #default="scope">
                <el-tag type="danger" size="small">{{ gradeNameOf(cvData.paperGrades, scope.row.gradeId) || '未定级' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="作者身份" align="center" prop="authorPosition" width="100" />
            <el-table-column label="期刊/会议" align="center" prop="journalName" min-width="160" show-overflow-tooltip />
            <el-table-column label="发表日期" align="center" prop="publishDate" width="110">
              <template #default="scope">
                <span>{{ parseTime(scope.row.publishDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="DOI" align="center" prop="doi" width="150" show-overflow-tooltip />
          </el-table>
          <div class="sub-title">专著教材 Books & Textbooks</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openResearchDialog('book')">新增著作</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.books" border class="mb16">
            <el-table-column label="书名 Book" align="center" prop="bookName" min-width="240" show-overflow-tooltip />
            <el-table-column label="等级" align="center" width="130">
              <template #default="scope">
                <el-tag type="warning" size="small">{{ gradeNameOf(cvData.bookGrades, scope.row.gradeId) || '未定级' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="作者角色" align="center" prop="authorRole" width="100" />
            <el-table-column label="出版社 Publisher" align="center" prop="publisher" min-width="170" show-overflow-tooltip />
            <el-table-column label="出版日期" align="center" prop="publishDate" width="110">
              <template #default="scope">
                <span>{{ parseTime(scope.row.publishDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="ISBN" align="center" prop="isbn" width="150" show-overflow-tooltip />
          </el-table>
          <div class="sub-title">专利等其他 Intellectual Property</div>
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openResearchDialog('patent')">新增专利</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.patents" border>
            <el-table-column label="名称 Patent" align="center" prop="patentName" min-width="240" show-overflow-tooltip />
            <el-table-column label="等级" align="center" width="130">
              <template #default="scope">
                <el-tag type="warning" size="small">{{ gradeNameOf(cvData.patentGrades, scope.row.gradeId) || '未定级' }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="发明人排名" align="center" prop="inventorRank" width="110" />
            <el-table-column label="专利号 No." align="center" prop="patentNumber" width="170" show-overflow-tooltip />
            <el-table-column label="授权日期" align="center" prop="grantDate" width="110">
              <template #default="scope">
                <span>{{ parseTime(scope.row.grantDate, '{y}-{m}-{d}') }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 7. 职业发展 -->
        <el-tab-pane label="职业发展 Development" name="development">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="SP/IP 维持资质的必要证据；研修/行业实践/资格更新，注明学时；近5年" />
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('development')">新增职业发展</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.developments" border>
            <el-table-column label="时间 Period" align="center" prop="period" width="150" />
            <el-table-column label="活动内容 Activity" align="center" prop="activity" min-width="280" show-overflow-tooltip />
            <el-table-column label="组织单位 Organizer" align="center" prop="organizer" min-width="180" show-overflow-tooltip />
            <el-table-column label="学时 Hours" align="center" prop="hours" width="100" />
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('development', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('development', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <!-- 8. 学术与社会服务 -->
        <el-tab-pane label="学术与社会服务 Service" name="service">
          <el-alert type="info" show-icon :closable="false" class="mb8"
            title="审稿、学会任职、评审专家、政府/企业咨询、社会培训、学院公共服务" />
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button type="primary" plain icon="Plus" @click="openSectionDialog('service')">新增服务记录</el-button>
            </el-col>
          </el-row>
          <el-table :data="cvData.services" border>
            <el-table-column label="时间 Period" align="center" prop="period" width="150" />
            <el-table-column label="服务内容 Service" align="center" prop="serviceContent" min-width="300" show-overflow-tooltip />
            <el-table-column label="相关单位 Organization" align="center" prop="organization" min-width="180" show-overflow-tooltip />
            <el-table-column label="操作" align="center" width="140">
              <template #default="scope">
                <el-button link type="primary" icon="Edit" @click="openSectionDialog('service', scope.row)">编辑</el-button>
                <el-button link type="danger" icon="Delete" @click="handleDeleteSection('service', scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 通用板块 / 科研成果 新增编辑对话框 -->
    <el-dialog v-model="dialog.open" :title="dialog.title" width="680px" append-to-body>
      <el-form ref="dialogFormRef" :model="dialog.form" :rules="dialogRules" label-width="120px">
        <el-row>
          <el-col v-for="f in dialog.fields" :key="f.prop" :span="f.type === 'textarea' ? 24 : 12">
            <el-form-item :label="f.label" :prop="f.prop">
              <el-input
                v-if="f.type === 'input'"
                v-model="dialog.form[f.prop]"
                :placeholder="f.placeholder || ('请输入' + f.label)"
              />
              <el-input
                v-else-if="f.type === 'textarea'"
                v-model="dialog.form[f.prop]"
                type="textarea"
                :rows="3"
                :placeholder="f.placeholder || ('请输入' + f.label)"
              />
              <el-select
                v-else-if="f.type === 'select'"
                v-model="dialog.form[f.prop]"
                :placeholder="'请选择' + f.label"
                style="width: 100%"
              >
                <el-option
                  v-for="o in (f.optionsKey ? (cvData[f.optionsKey] || []) : (f.options || []))"
                  :key="typeof o === 'string' ? o : o.gradeId"
                  :label="typeof o === 'string' ? o : (o.gradeName + (o.baseScore ? ' (' + o.baseScore + '分)' : ''))"
                  :value="typeof o === 'string' ? o : o.gradeId"
                />
              </el-select>
              <el-radio-group v-else-if="f.type === 'radio'" v-model="dialog.form[f.prop]">
                <el-radio v-for="o in f.options" :key="o.value" :value="o.value">{{ o.label }}</el-radio>
              </el-radio-group>
              <el-date-picker
                v-else-if="f.type === 'date'"
                v-model="dialog.form[f.prop]"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="选择日期"
                style="width: 100%"
              />
              <el-input-number
                v-else-if="f.type === 'number'"
                v-model="dialog.form[f.prop]"
                :min="0"
                :precision="2"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitDialog">确 定</el-button>
          <el-button @click="dialog.open = false">取 消</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- AACSB 教师CV填写说明 -->
    <el-drawer v-model="helpOpen" title="AACSB 教师CV填写说明（Guidance for Faculty CV）" size="62%">
      <div class="cv-help">
        <h3>一、AACSB 师资分类三步判定法（Faculty Classification）</h3>
        <ol>
          <li><b>第一步 · 看学历：</b>博士（Ph.D./DBA）→ 进入 SA 或 PA 判定；硕士及以下 → 进入 SP 或 IP 判定。</li>
          <li><b>第二步 · 看近5年学术产出：</b>博士 + 同行评审期刊论文（SSCI/SCI/EI/CSSCI 等）→ <b>SA 学术型学者</b>；博士但以业界实践为主 → <b>PA 实务型学者</b>。</li>
          <li><b>第三步 · 硕士学历者：</b>硕士 + 持续学术发表 → <b>SP 学术实务型</b>；硕士 + 显著业界实务 → <b>IP 教学实务型</b>。</li>
        </ol>
        <el-alert type="warning" show-icon :closable="false"
          title="SA=学术型学者(PhD+同行评审论文)；PA=实务型学者(PhD+业界实践)；SP=学术实务型(硕士+发表)；IP=教学实务型(硕士+实务)；不确定请先与认证办沟通确认" />

        <h3>二、各栏目填写要点</h3>
        <table class="help-table">
          <thead>
            <tr><th style="width: 160px;">栏目</th><th>填写要点</th></tr>
          </thead>
          <tbody>
            <tr><td>基本信息 Profile</td><td>证件照、CDUT工号、联系电话、岗位/职务、任教类型（本院全职/外院兼职）、AACSB 师资分类、所属单位、通讯地址；研究与教学领域列 3-5 条中英文。</td></tr>
            <tr><td>教育背景 Education</td><td>按学位由高到低填写全部学历学位并标注最高学位；学位名称尽量给英文；学位与所授课程匹配度是 AACSB 资质判断重要依据。</td></tr>
            <tr><td>工作经历 Experience</td><td>分「学术经历」与「行业实务经历」两个子表；行业实务经历是 PA/SP/IP 举证核心，写明时长、层级、职责；近5年为主。</td></tr>
            <tr><td>职业资格 Certification</td><td>PA/SP/IP 举证重要内容，注明取得时间与现状（有效/已更新）。</td></tr>
            <tr><td>授课课程 Courses</td><td>近5年讲授的全部课程；课程名称中英文、层次（本科/硕士/MBA）、学年、角色（主讲/助教）、周学时；可点击【从授课记录同步】自动导入近6年授课记录。</td></tr>
            <tr><td>荣誉与科研项目</td><td>数据自动导入自科研库（近6年窗口）；新增将写入科研库并计入科研积分。</td></tr>
            <tr><td>学术成果 Publications</td><td>期刊论文/专著教材/专利等其他；论文须注明收录/分级（SSCI/SCI/EI/CSSCI 等）；近5年为主。</td></tr>
            <tr><td>职业发展 Development</td><td>SP/IP 维持资质的必要证据；研修/行业实践/资格更新，注明学时；近5年。</td></tr>
            <tr><td>学术与社会服务 Service</td><td>审稿、学会任职、评审专家、政府/企业咨询、社会培训、学院公共服务。</td></tr>
          </tbody>
        </table>

        <h3>三、提交自查清单（Submission Checklist）</h3>
        <ul class="check-list">
          <li>最高学位已标注，且学位与所授课程匹配；</li>
          <li>行业实务经历已完整填写（PA/SP/IP 必填）；</li>
          <li>职业资格已注明取得时间与现状（PA/SP/IP）；</li>
          <li>近5年课程齐全，层次、角色、周学时准确；</li>
          <li>论文等级与收录信息（SSCI/SCI/EI/CSSCI 等）完整；</li>
          <li>职业发展活动已注明学时（SP/IP 必填）；</li>
          <li>已勾选《真实性声明》并保存基本信息。</li>
        </ul>
      </div>
    </el-drawer>
  </div>
</template>

<script setup name="MyCv">
import {
  getMyCv, getCvDetail, updateMyProfile, updateManageProfile,
  sectionApi, syncMyCourses, syncManageCourses, addResearch
} from "@/api/cv/cv"
import { getProfessor } from "@/api/research/professor"

const { proxy } = getCurrentInstance()
const route = useRoute()

// ===================== 模式与基础状态 =====================
// 无 professorId 参数 → 本人模式（/cv/my/*）；有参 → 代录模式（/cv/manage/*）
const targetProfessorId = ref(route.query.professorId ? Number(route.query.professorId) : null)
const manageMode = computed(() => !!targetProfessorId.value)
const professorName = ref("")
const loading = ref(false)
const activeTab = ref("education")
const helpOpen = ref(false)

// AACSB 师资分类（SA/PA/SP/IP）选项与解释文案
const aacsbClasses = [
  { value: "SA", label: "SA 学术型学者", tip: "SA = 学术型学者（Scholarly Academic）：PhD + 同行评审论文" },
  { value: "PA", label: "PA 实务型学者", tip: "PA = 实务型学者（Practice Academic）：PhD + 业界实践" },
  { value: "SP", label: "SP 学术实务型", tip: "SP = 学术实务型（Scholarly Practitioner）：硕士 + 发表" },
  { value: "IP", label: "IP 教学实务型", tip: "IP = 教学实务型（Instructional Practitioner）：硕士 + 实务" }
]

// CV 全量数据（档案 + 八板块 + 科研成果 + 各类型等级字典）
const cvData = reactive({
  profile: {},
  educations: [], experiences: [], certifications: [], courses: [],
  developments: [], services: [],
  papers: [], books: [], patents: [], awards: [], projects: [],
  paperGrades: [], bookGrades: [], patentGrades: [], awardGrades: [], projectGrades: []
})

// 档案表单（字段与 ms_cv_profile 列一致）
const profileForm = reactive({
  professorId: null,
  photoUrl: "",
  cdutId: "",
  phone: "",
  position: "",
  employmentType: "1",
  aacsbClass: "",
  employer: "",
  address: "",
  areasOfInterest: "",
  otherInfo: "",
  declareFlag: "0"
})

// 导出 CV 使用的教师ID
const exportId = computed(() => manageMode.value ? targetProfessorId.value : profileForm.professorId)

// ===================== CV 数据加载 =====================
function loadCv() {
  loading.value = true
  const req = manageMode.value ? getCvDetail(targetProfessorId.value) : getMyCv()
  req.then(res => {
    const d = res.data || res || {}
    cvData.profile = d.profile || {}
    cvData.educations = d.educations || []
    cvData.experiences = d.experiences || []
    cvData.certifications = d.certifications || []
    cvData.courses = d.courses || []
    cvData.developments = d.developments || []
    cvData.services = d.services || []
    cvData.papers = d.papers || []
    cvData.books = d.books || []
    cvData.patents = d.patents || []
    cvData.awards = d.awards || []
    cvData.projects = d.projects || []
    cvData.paperGrades = d.paperGrades || []
    cvData.bookGrades = d.bookGrades || []
    cvData.patentGrades = d.patentGrades || []
    cvData.awardGrades = d.awardGrades || []
    cvData.projectGrades = d.projectGrades || []
    // 回填档案表单
    Object.keys(profileForm).forEach(key => {
      if (cvData.profile[key] !== undefined && cvData.profile[key] !== null) {
        profileForm[key] = cvData.profile[key]
      }
    })
    if (!profileForm.employmentType) profileForm.employmentType = "1"
    if (!profileForm.declareFlag) profileForm.declareFlag = "0"
  }).finally(() => {
    loading.value = false
  })
}

// 代录模式下获取教师姓名用于顶部提示与导出文件名
function loadProfessorName() {
  if (!manageMode.value) return
  getProfessor(targetProfessorId.value).then(res => {
    professorName.value = (res.data || {}).name || ""
  })
}

// 前端映射等级名称：实体仅含 gradeId 时，从对应 Grades 列表 map 出 gradeName
function gradeNameOf(list, id) {
  if (id === undefined || id === null) return ""
  const g = (list || []).find(item => String(item.gradeId) === String(id))
  return g ? g.gradeName : ""
}

// 工作经历按类型过滤（1=学术 2=行业实务）
function experienceList(expType) {
  return cvData.experiences.filter(item => String(item.expType) === expType)
}

// ===================== 档案保存与导出 =====================
function handleSaveProfile() {
  if (profileForm.declareFlag !== "1") {
    proxy.$modal.msgWarning("请先勾选《真实性声明》后再保存")
    return
  }
  const payload = { ...profileForm }
  if (manageMode.value) payload.professorId = targetProfessorId.value
  const fn = manageMode.value ? updateManageProfile : updateMyProfile
  fn(payload).then(() => {
    proxy.$modal.msgSuccess("基本信息保存成功")
    loadCv()
  })
}

function handleExportCv() {
  if (!exportId.value) {
    proxy.$modal.msgWarning("尚未获取到教师ID，无法导出")
    return
  }
  const namePart = manageMode.value ? (professorName.value || targetProfessorId.value) : (profileForm.cdutId || exportId.value)
  proxy.download("cv/export/" + exportId.value, {}, `CV_${namePart}_${new Date().getTime()}.docx`)
}

// ===================== 授课同步 =====================
function handleSyncCourses() {
  proxy.$modal.confirm("确认从授课记录同步近6年授课数据到 CV 授课课程板块？同步后请核对层次、角色与周学时。").then(() => {
    const fn = manageMode.value ? () => syncManageCourses(targetProfessorId.value) : syncMyCourses
    return fn()
  }).then(() => {
    proxy.$modal.msgSuccess("同步成功")
    loadCv()
  }).catch(() => {})
}

// ===================== 六个可编辑板块（通用对话框） =====================
// 各板块字段定义（与数据库列一致）
const SECTION_FIELDS = {
  education: [
    { prop: "degree", label: "学位 Degree", type: "input", placeholder: "如：博士 Ph.D. / 硕士 Master / 学士 Bachelor（尽量给英文）" },
    { prop: "major", label: "专业 Major", type: "input", placeholder: "请输入专业名称" },
    { prop: "institution", label: "授予院校 Institution", type: "input", placeholder: "请输入授予院校" },
    { prop: "gradYear", label: "毕业年份 Year", type: "input", placeholder: "如：2015" },
    { prop: "isHighest", label: "最高学位", type: "radio", options: [{ label: "是", value: "1" }, { label: "否", value: "0" }] }
  ],
  experience: [
    { prop: "expType", label: "经历类型", type: "radio", options: [{ label: "学术经历", value: "1" }, { label: "行业实务经历", value: "2" }] },
    { prop: "period", label: "起止时间 Period", type: "input", placeholder: "如：2018.07 - 至今" },
    { prop: "organization", label: "单位 Organization", type: "input", placeholder: "请输入单位名称" },
    { prop: "role", label: "职务/岗位 Role", type: "input", placeholder: "行业经历请写明岗位层级" },
    { prop: "description", label: "主要职责/业绩", type: "textarea", placeholder: "行业实务经历请写明时长、层级、职责" }
  ],
  certification: [
    { prop: "certName", label: "证书名称 Certificate", type: "input", placeholder: "如：CPA / CFA / ACCA / FRM" },
    { prop: "issuingBody", label: "颁发机构 Issuer", type: "input", placeholder: "请输入颁发机构" },
    { prop: "certYear", label: "取得年份 Year", type: "input", placeholder: "如：2023" },
    { prop: "statusText", label: "现状 Status", type: "select", options: ["有效", "已更新"] }
  ],
  course: [
    { prop: "courseName", label: "课程名称 Course", type: "input", placeholder: "中英文，如：管理学 Principles of Management" },
    { prop: "levelName", label: "层次 Level", type: "select", options: ["本科", "硕士", "MBA"] },
    { prop: "academicYear", label: "学年 Academic Year", type: "input", placeholder: "如：2025-2026" },
    { prop: "teachRole", label: "角色 Role", type: "select", options: ["主讲", "助教"] },
    { prop: "weeklyHours", label: "周学时 Hours/Week", type: "input", placeholder: "如：4" }
  ],
  development: [
    { prop: "period", label: "时间 Period", type: "input", placeholder: "如：2024.07 - 2024.08" },
    { prop: "activity", label: "活动内容 Activity", type: "textarea", placeholder: "研修 / 行业实践 / 资格更新" },
    { prop: "organizer", label: "组织单位 Organizer", type: "input", placeholder: "请输入组织单位" },
    { prop: "hours", label: "学时 Hours", type: "input", placeholder: "如：40 学时" }
  ],
  service: [
    { prop: "period", label: "时间 Period", type: "input", placeholder: "如：2023 - 至今" },
    { prop: "serviceContent", label: "服务内容 Service", type: "textarea", placeholder: "审稿 / 学会任职 / 评审专家 / 政府企业咨询 / 社会培训 / 学院公共服务" },
    { prop: "organization", label: "相关单位 Organization", type: "input", placeholder: "请输入相关单位" }
  ]
}

// 各板块必填字段
const SECTION_REQUIRED = {
  education: ["degree", "institution"],
  experience: ["period", "organization"],
  certification: ["certName"],
  course: ["courseName", "academicYear"],
  development: ["activity"],
  service: ["serviceContent"]
}

// 各板块默认值
const SECTION_DEFAULTS = {
  education: { isHighest: "0" },
  experience: { expType: "1" },
  certification: { statusText: "有效" },
  course: { teachRole: "主讲" },
  development: {},
  service: {}
}

// 各板块主键名（判断新增/编辑）
const SECTION_ID_KEY = {
  education: "educationId",
  experience: "experienceId",
  certification: "certId",
  course: "cvCourseId",
  development: "devId",
  service: "serviceId"
}

// 五类科研成果的新增字段（写入科研库）
const RESEARCH_FIELDS = {
  award: [
    { prop: "awardName", label: "获奖名称", type: "input", placeholder: "请输入获奖名称" },
    { prop: "gradeId", label: "获奖等级", type: "select", optionsKey: "awardGrades" },
    { prop: "awardRank", label: "完成人排名", type: "select", options: ["第一完成人", "第二完成人", "第三完成人", "其他完成人"] },
    { prop: "awardOrg", label: "颁发单位", type: "input", placeholder: "请输入颁发单位" },
    { prop: "awardDate", label: "获奖日期", type: "date" }
  ],
  project: [
    { prop: "projectName", label: "项目名称", type: "input", placeholder: "请输入项目名称" },
    { prop: "gradeId", label: "项目等级", type: "select", optionsKey: "projectGrades" },
    { prop: "role", label: "承担角色", type: "select", options: ["主持", "参与"] },
    { prop: "fundingAmount", label: "经费(万元)", type: "number" },
    { prop: "projectSource", label: "项目来源", type: "input", placeholder: "如：国家自然科学基金" },
    { prop: "startDate", label: "开始日期", type: "date" },
    { prop: "endDate", label: "结束日期", type: "date" }
  ],
  paper: [
    { prop: "title", label: "论文题目", type: "input", placeholder: "请输入论文题目" },
    { prop: "gradeId", label: "论文等级", type: "select", optionsKey: "paperGrades" },
    { prop: "authorPosition", label: "作者身份", type: "select", options: ["第一作者", "通讯作者", "第二作者", "第三作者", "其他作者"] },
    { prop: "journalName", label: "期刊/会议名称", type: "input", placeholder: "请输入期刊或会议名称" },
    { prop: "publishDate", label: "发表日期", type: "date" },
    { prop: "doi", label: "DOI", type: "input", placeholder: "请输入DOI号" }
  ],
  book: [
    { prop: "bookName", label: "书名", type: "input", placeholder: "请输入书名" },
    { prop: "gradeId", label: "著作等级", type: "select", optionsKey: "bookGrades" },
    { prop: "authorRole", label: "作者角色", type: "select", options: ["著", "编著", "译", "主编", "副主编", "参编"] },
    { prop: "publisher", label: "出版社", type: "input", placeholder: "请输入出版社" },
    { prop: "publishDate", label: "出版日期", type: "date" },
    { prop: "isbn", label: "ISBN", type: "input", placeholder: "请输入ISBN号" }
  ],
  patent: [
    { prop: "patentName", label: "专利名称", type: "input", placeholder: "请输入专利名称" },
    { prop: "gradeId", label: "专利等级", type: "select", optionsKey: "patentGrades" },
    { prop: "inventorRank", label: "发明人排名", type: "select", options: ["第一发明人", "第二发明人", "第三发明人", "其他发明人"] },
    { prop: "patentNumber", label: "专利号", type: "input", placeholder: "请输入专利号" },
    { prop: "grantDate", label: "授权日期", type: "date" }
  ]
}

// 科研成果默认值
const RESEARCH_DEFAULTS = {
  award: { awardRank: "第一完成人" },
  project: { role: "主持" },
  paper: { authorPosition: "第一作者" },
  book: { authorRole: "著" },
  patent: { inventorRank: "第一发明人" }
}

// 科研成果类型中文名
const RESEARCH_TITLES = { award: "获奖", project: "科研项目", paper: "期刊论文", book: "专著教材", patent: "专利" }

// 通用对话框状态：kind = section（六大板块）| research（科研成果写入科研库）
const dialog = reactive({
  open: false,
  kind: "section",
  section: "education",
  title: "",
  fields: [],
  form: {}
})

// 动态校验规则
const dialogRules = computed(() => {
  const req = dialog.kind === "section" ? (SECTION_REQUIRED[dialog.section] || []) : [dialog.fields[0]?.prop]
  const rules = {}
  req.forEach(prop => {
    if (!prop) return
    const field = dialog.fields.find(f => f.prop === prop) || {}
    const isSelect = field.type === "select" || field.type === "radio" || field.type === "date"
    rules[prop] = [{ required: true, message: (field.label || prop) + "不能为空", trigger: isSelect ? "change" : "blur" }]
  })
  return rules
})

// 打开板块对话框；presetExpType 用于"新增学术/行业经历"按钮预置类型
function openSectionDialog(section, row, presetExpType) {
  dialog.kind = "section"
  dialog.section = section
  dialog.fields = SECTION_FIELDS[section]
  dialog.form = row ? { ...row } : { ...SECTION_DEFAULTS[section] }
  if (!row && section === "experience" && presetExpType) {
    dialog.form.expType = presetExpType
  }
  dialog.title = (row ? "编辑" : "新增") + ({ education: "教育背景", experience: "工作经历", certification: "职业资格", course: "授课课程", development: "职业发展", service: "社会服务" }[section])
  dialog.open = true
  nextTick(() => proxy.$refs.dialogFormRef?.clearValidate())
}

// 打开科研成果对话框（新增写入科研库并计入科研积分）
function openResearchDialog(type) {
  dialog.kind = "research"
  dialog.section = type
  dialog.fields = RESEARCH_FIELDS[type]
  dialog.form = { ...RESEARCH_DEFAULTS[type] }
  dialog.title = "新增" + RESEARCH_TITLES[type] + "（写入科研库）"
  dialog.open = true
  nextTick(() => proxy.$refs.dialogFormRef?.clearValidate())
}

// 提交对话框：按 kind 分流
function submitDialog() {
  proxy.$refs.dialogFormRef.validate(valid => {
    if (!valid) return
    const payload = { ...dialog.form }
    if (manageMode.value) payload.professorId = targetProfessorId.value
    if (dialog.kind === "research") {
      // 新增科研成果 → 写入科研库
      addResearch(dialog.section, payload).then(() => {
        proxy.$modal.msgSuccess("新增成功，已写入科研库并计入科研积分")
        dialog.open = false
        loadCv()
      })
      return
    }
    // 六大板块 CRUD：本人走 /cv/my/*，代录走 /cv/manage/*
    const api = sectionApi[dialog.section]
    const isEdit = !!payload[SECTION_ID_KEY[dialog.section]]
    const fn = isEdit
      ? (manageMode.value ? api.updateManage : api.updateMy)
      : (manageMode.value ? api.addManage : api.addMy)
    fn(payload).then(() => {
      proxy.$modal.msgSuccess("保存成功")
      dialog.open = false
      loadCv()
    })
  })
}

// 删除板块记录（带确认）
function handleDeleteSection(section, row) {
  const id = row[SECTION_ID_KEY[section]]
  proxy.$modal.confirm("是否确认删除该条记录？删除后不可恢复。").then(() => {
    const api = sectionApi[section]
    const fn = manageMode.value ? api.delManage : api.delMy
    return fn(id)
  }).then(() => {
    proxy.$modal.msgSuccess("删除成功")
    loadCv()
  }).catch(() => {})
}

// ===================== 初始化 =====================
watch(() => route.query.professorId, val => {
  const id = val ? Number(val) : null
  if (id !== targetProfessorId.value) {
    targetProfessorId.value = id
    loadProfessorName()
    loadCv()
  }
})

loadProfessorName()
loadCv()
</script>

<style scoped lang="scss">
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title {
  font-weight: 600;
}
.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 18px;
  margin-top: 2px;
}
.sub-title {
  font-weight: 600;
  margin: 4px 0 8px;
  padding-left: 8px;
  border-left: 3px solid var(--el-color-primary);
}
.mb16 {
  margin-bottom: 16px;
}
.cv-help {
  padding: 0 4px;
  h3 {
    margin: 18px 0 10px;
    font-size: 15px;
    color: #303133;
  }
  ol, ul {
    padding-left: 20px;
    line-height: 24px;
  }
  .help-table {
    width: 100%;
    border-collapse: collapse;
    font-size: 13px;
    th, td {
      border: 1px solid #dcdfe6;
      padding: 8px 10px;
      text-align: left;
      line-height: 20px;
    }
    th {
      background: #f5f7fa;
      font-weight: 600;
    }
  }
  .check-list {
    list-style: none;
    padding-left: 4px;
    li::before {
      content: "☑ ";
      color: var(--el-color-primary);
      font-weight: bold;
    }
  }
}
</style>
