# Session Summary — 2026-06-19

## 本次完成工作

### 🏫 管理科学学院框架定制

将若依系统管理演示数据全部替换为成都理工大学管理科学学院的组织架构：

- **部门**：成都理工大学 → 管理科学学院 → 11个系/办公室
- **岗位**：12个学术行政岗位（院长/教授/辅导员/教务员等）
- **角色**：5个学院角色（系统管理员/学院领导/系主任/普通教师/行政人员）
- **用户**：8位教职员工，各分配角色/岗位/部门
- **通知公告**：2条学院公告替换若依原生公告
- **菜单**：menu_id=4 "若依官网" → "学校官网"（cdut.edu.cn）

新增文件：`RuoYi-Vue-master/sql/migrate_cdut.sql`（可重复执行）

### 🌐 环境迁移（已完成）

| 组件 | 版本 | 位置 |
|------|------|------|
| JDK | 17.0.19 Temurin | `C:\Program Files\Eclipse Adoptium\` |
| Maven | 3.9.16 | `C:\apache-maven-3.9.16\` |
| MySQL | 8.4.9 | winget 安装，密码 admin123 |
| Redis | 3.0.504 | winget 安装，端口 6379 |

配置文件修改：
- `application.yml`：上传路径 → `${user.home}/ruoyi/uploadPath`；DB密码 → `admin123`
- `application-druid.yml`：DB密码 → `admin123`

---

## 运行状态

| 服务 | 地址 | 状态 |
|------|------|------|
| MySQL | localhost:3306 | ✅ 自启 |
| Redis | localhost:6379 | ✅ 自启 |
| 后端 | http://localhost:8080 | ✅ 运行中 |
| 前端 | http://localhost:80 | ✅ 运行中 |

登录：`admin / admin123`

---

## 快速启动

```bash
# 后端
cd RuoYi-Vue-master
export PATH="/c/apache-maven-3.9.16/bin:$PATH"
mvn spring-boot:run -pl ruoyi-admin

# 前端
cd RuoYi-Vue3-master
npm run dev
```

---

## 剩余待办

### 高优先级
1. **端到端测试** — 完整走一遍导入流程
2. **一键导入上传按钮提前可见** — 选择教授前渲染但置灰

### 中优先级
3. 区分考核计分和奖励计分的前端展示
4. 按钮级权限验证
5. 模板等级名称列添加下拉验证

### 低优先级
6. 单元测试
7. 一键导入支持 `updateSupport` 参数
