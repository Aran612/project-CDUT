<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>管理科学学院信息管理系统</h2>
        <p>
          本系统是成都理工大学管理科学学院综合信息管理平台，涵盖教学管理、学生管理、师资管理等核心业务模块，致力于提升学院信息化管理水平，为师生提供高效便捷的服务。
        </p>
        <p>
          <b>系统版本：</b><span>v1.0.0</span>
        </p>
        <p>
          <el-tag type="primary">管理科学学院</el-tag>
          <el-tag type="success" style="margin-left: 8px">成都理工大学</el-tag>
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术选型</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Spring Security</li>
              <li>JWT</li>
              <li>MyBatis</li>
              <li>Druid</li>
              <li>Redis</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Vue 3</li>
              <li>Pinia</li>
              <li>Element Plus</li>
              <li>Axios</li>
              <li>Vite</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
</script>

<style scoped lang="scss">
.home {
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    padding: 0;
    margin: 0;
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;
    b {
      font-weight: 700;
    }
  }
}
</style>