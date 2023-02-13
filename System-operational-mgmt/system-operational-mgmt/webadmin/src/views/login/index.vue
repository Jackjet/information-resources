<template>
  <div class="login-container">
    <div class="center">
      <div class="left">

      </div>
      <div class="right">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="title-container">
        <h3 class="title">运行管理平台</h3>
      </div>
      <el-form-item prop="username" class="item">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="username"
          v-model="loginForm.username"
          placeholder="请输入用户名"
          name="username"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password" class="item">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="请输入密码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">登录</el-button>
<!-- 
      <div class="tips">
        <span style="margin-right:20px;"></span>
        <span></span>
      </div> -->

</el-form> 

      </div>
    </div>
   
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'
export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername}],
        password: [{ required: true, trigger: 'blur', validator: validatePassword}]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then((data) => {
            if (data.code === 1) {
              this.$router.push({ path: this.redirect || '/' })
            } else {
               this.$message.error(data.msg);
            } 
            // this.loading = false
          }).catch((data) => {
            // this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg:#fff;
$light_gray:#000;
$cursor: #000;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 35px;
    width: 85%;
    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 2px 5px 2px 15px;
      color: $light_gray;
      height: 35px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgb(192, 189, 189);
    background:#fff;
    border-radius: 5px;
    color: #000;
  }
  .center{
    width: 55%; 
    height: 70%;
    min-width: 600px;
    min-width: 720px;
    margin: 0 auto;  
    position: absolute;  
    top: 50%; left: 50%;  
    -webkit-transform: translate(-50%,-50%);  
    -ms-transform: translate(-50%,-50%);  
    transform: translate(-50%,-50%);
    .left{
      width: 25%;
      height: 100%;
      float: left;
      background: url('../../assets/image/left.png');
      background-size: 100% 100%;
    }
    .right{
      float: right;
      width: 75%;
      height: 100%;
      background: #fff;
    }
  }
}
</style>

<style lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
$light_gray:rgb(255, 255, 255);
.el-button--primary{
  background: rgb(83, 190, 119);
  border: none;
}
.item{
  height: 44px;
  line-height: 44px;
}
 /deep/ .el-form-item__content{
    line-height: 35px;
  }
.login-container {
  min-height: 100%;
  width: 100%;
  // background-color: $bg;
  // background: #005C97;  /* fallback for old browsers */
  // background: -webkit-linear-gradient(to right, #363795, #005C97);  /* Chrome 10-25, Safari 5.1-6 */
  // background: linear-gradient(to right, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
  background: url('../../assets/image/bank.png');
  overflow: hidden;
 
  .login-form {
    position: relative;
    width: 450px;
    max-width: 100%;
    padding: 70px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 32px;
      color: #646bbf;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: 548;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}
</style>
