# Session Summary — 2026-06-19

## 环境迁移 ✅

项目从旧电脑迁移到新 Windows 11 机器，已完成全环境重建：

### 安装的软件

| 组件 | 版本 | 安装方式 | 位置 |
|------|------|----------|------|
| JDK | 17.0.19 Temurin | winget | `C:\Program Files\Eclipse Adoptium\` |
| Maven | 3.9.16 | 手动下载 | `C:\apache-maven-3.9.16\` |
| MySQL | 8.4.9 | winget | `C:\Program Files\MySQL\MySQL Server 8.4\` |
| Redis | 3.0.504 | winget | `C:\Program Files\Redis\` |
| Node.js | v24.11.1 | 已有 | — |

### 数据库

- `ry` 数据库已创建，导入 `ry_20250522.sql` + `quartz.sql` + `research_init.sql`
- root 密码: `admin123`

### 配置修改

| 文件 | 修改项 |
|------|--------|
| `application.yml` | 上传路径: `D:/ruoyi/uploadPath` → `${user.home}/ruoyi/uploadPath` |
| `application.yml` | 数据库密码: `123456` → `admin123` |
| `application-druid.yml` | 数据库密码: `123456` → `admin123` |

### Git

- 重新初始化仓库，已做初始提交

---

## 运行状态

| 服务 | 地址 | 状态 |
|------|------|------|
| MySQL | localhost:3306 | ✅ 服务自启 |
| Redis | localhost:6379 | ✅ 服务自启 |
| 后端 | http://localhost:8080 | ✅ mvn spring-boot:run -pl ruoyi-admin |
| 前端 | http://localhost:80 | ✅ npm run dev |

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

# 浏览器 http://localhost
# 登录 admin / admin123
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
