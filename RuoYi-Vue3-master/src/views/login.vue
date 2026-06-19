<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>

    <div class="login-card">
      <!-- 左侧品牌区 -->
      <div class="login-brand">
        <div class="brand-logo">
          <img :src="logoImg" alt="成都理工大学" class="school-logo" />
        </div>
        <h1 class="brand-title">管理科学学院</h1>
        <p class="brand-subtitle">信息管理系统</p>
        <div class="brand-divider"></div>
        <p class="brand-desc">科学管理 · 高效服务 · 智慧校园</p>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-wrap">
        <h2 class="form-title">欢迎登录</h2>
        <p class="form-subtitle">请使用您的账号登录系统</p>

        <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
          <el-form-item prop="username">
            <el-input
                v-model="loginForm.username"
                type="text"
                size="large"
                placeholder="请输入账号"
                prefix-icon="User"
            />
          </el-form-item>

          <el-form-item prop="password">
            <el-input
                v-model="loginForm.password"
                type="password"
                size="large"
                placeholder="请输入密码"
                prefix-icon="Lock"
                show-password
                @keyup.enter="handleLogin"
            />
          </el-form-item>

          <el-form-item prop="code" v-if="captchaEnabled">
            <div class="captcha-wrap">
              <el-input
                  v-model="loginForm.code"
                  size="large"
                  placeholder="请输入验证码"
                  prefix-icon="Key"
                  @keyup.enter="handleLogin"
              />
              <div class="captcha-img" @click="getCode">
                <img :src="codeUrl" alt="验证码" />
              </div>
            </div>
          </el-form-item>

          <div class="form-options">
            <el-checkbox v-model="loginForm.rememberMe">记住密码</el-checkbox>
          </div>

          <el-form-item>
            <el-button
                :loading="loading"
                size="large"
                type="primary"
                class="login-btn"
                @click.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登录中...</span>
            </el-button>
          </el-form-item>
        </el-form>

        <p class="form-footer">成都理工大学 · 管理科学学院</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import logoImg from '@/assets/images/logo.png'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import  useUserStore  from '@/store/modules/user'
import { getCodeImg } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'

const router = useRouter()
const userStore = useUserStore()

const loginForm = ref({
  username: 'admin',
  password: 'admin123',
  rememberMe: false,
  code: '',
  uuid: ''
})

const loginRules = {
  username: [{ required: true, trigger: 'blur', message: '请输入账号' }],
  password: [{ required: true, trigger: 'blur', message: '请输入密码' }],
  code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
}

const codeUrl = ref('')
const captchaEnabled = ref(true)
const loading = ref(false)
const loginRef = ref(null)

function getCode() {
  getCodeImg().then(res => {
    captchaEnabled.value = res.captchaEnabled === undefined ? true : res.captchaEnabled
    if (captchaEnabled.value) {
      codeUrl.value = 'data:image/gif;base64,' + res.img
      loginForm.value.uuid = res.uuid
    }
  })
}

function handleLogin() {
  loginRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      if (loginForm.value.rememberMe) {
        Cookies.set('username', loginForm.value.username, { expires: 30 })
        Cookies.set('password', encrypt(loginForm.value.password), { expires: 30 })
        Cookies.set('rememberMe', loginForm.value.rememberMe, { expires: 30 })
      } else {
        Cookies.remove('username')
        Cookies.remove('password')
        Cookies.remove('rememberMe')
      }
      userStore.login(loginForm.value).then(() => {
        router.push({ path: '/' })
      }).catch(() => {
        loading.value = false
        if (captchaEnabled.value) getCode()
      })
    }
  })
}

onMounted(() => {
  const username = Cookies.get('username')
  const password = Cookies.get('password')
  const rememberMe = Cookies.get('rememberMe')
  if (username) loginForm.value.username = username
  if (password) loginForm.value.password = decrypt(password)
  if (rememberMe) loginForm.value.rememberMe = Boolean(rememberMe)
  getCode()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1a3a5c 0%, #0d5c8a 40%, #1a7a8a 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

/* 背景装饰圆 */
.bg-decoration .circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.04);
}
.circle-1 { width: 500px; height: 500px; top: -150px; right: -100px; }
.circle-2 { width: 350px; height: 350px; bottom: -80px; left: -80px; }
.circle-3 { width: 200px; height: 200px; top: 40%; left: 20%; }

/* 主卡片 */
.login-card {
  display: flex;
  width: 860px;
  min-height: 500px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.3);
  position: relative;
  z-index: 1;
}

/* 左侧品牌区 */
.login-brand {
  width: 340px;
  background: linear-gradient(160deg, #1a5c8a 0%, #0d7a9a 100%);
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
}
.brand-logo .school-logo {
  width: 200px;
  height: auto;
  margin-bottom: 20px;
  mix-blend-mode: multiply;
}
.brand-title {
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 3px;
  margin: 0 0 8px;
}
.brand-subtitle {
  font-size: 16px;
  opacity: 0.85;
  letter-spacing: 2px;
  margin: 0 0 30px;
}
.brand-divider {
  width: 40px;
  height: 2px;
  background: rgba(255,255,255,0.5);
  margin-bottom: 20px;
}
.brand-desc {
  font-size: 13px;
  opacity: 0.65;
  letter-spacing: 1px;
  margin: 0;
}

/* 右侧表单区 */
.login-form-wrap {
  flex: 1;
  padding: 60px 50px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.form-title {
  font-size: 26px;
  font-weight: 600;
  color: #1a3a5c;
  margin: 0 0 8px;
}
.form-subtitle {
  font-size: 14px;
  color: #888;
  margin: 0 0 36px;
}
.login-form {
  width: 100%;
}
.captcha-wrap {
  display: flex;
  gap: 12px;
  width: 100%;
}
.captcha-wrap .el-input {
  flex: 1;
}
.captcha-img {
  width: 120px;
  height: 40px;
  cursor: pointer;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #dcdfe6;
  flex-shrink: 0;
}
.captcha-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.form-options {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}
.login-btn {
  width: 100%;
  height: 46px;
  font-size: 16px;
  letter-spacing: 4px;
  background: linear-gradient(90deg, #1a5c8a, #0d7a9a);
  border: none;
  border-radius: 8px;
}
.login-btn:hover {
  background: linear-gradient(90deg, #0d4a78, #0a6a8a);
}
.form-footer {
  text-align: center;
  color: #bbb;
  font-size: 12px;
  margin-top: 30px;
}
</style>