<template>
  <div class="login-container">
    <div class="center">
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <div class="title-container">
          <h3 class="title">API集成服务</h3>
        </div>
        <el-form-item prop="username" class="item user_name">
          <span class="svg-container">
            <i class="el-icon-user"/>
          </span>
          <el-input maxlength='100' ref="username" v-model="loginForm.username" placeholder="请输入账号" name="username" type="text" tabindex="1" autocomplete="off"/>
        </el-form-item>
        <el-form-item prop="password" class="item user_name">
          <span class="svg-container">
            <i class="el-icon-lock"/>
          </span>
          <do-password-input v-model="loginForm.password" @keyup.enter.native="login"></do-password-input>
        </el-form-item>
        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:5px; height: 45px; margin-left: 0px;" @click.native.prevent="login">
          登录
        </el-button>
        <div class="tips wid">
          <el-checkbox v-model="rememberMe" style="float: right;margin-right:5px; margin-top: 10px;" label="记住密码" name="type"></el-checkbox>
          <span></span>
        </div>
      </el-form>
    </div>
    <el-dialog append-to-body :visible.sync="dialogVisible" :show-close="false" width="450px">
      <slideverify
        ref="dialogopen"
        :l="42"
        :r="10"
        :w="410"
        :h="200"
        :block_y="block_y"
        :imgurl="imgurl"
        :miniimgurl="miniimgurl"
        @success="imageVerifySuccess"
        @fail="imageVerifyFail"
        @refresh="imageVerifyRefresh"
        :slider-text="text">
      </slideverify>
    </el-dialog>
  </div>
</template>

<script>
import {getVerifyCode, getVerifyState, login} from '@/api/user'
import {isEmpty, throttle} from '@/utils/tool'
import Md5 from 'js-md5'
import Cookies from 'js-cookie'
import CryptoJS from 'crypto-js'
import DoPasswordInput from "@/components/input/DoPasswordInput"
import slideverify from '@/components/slideVerify/DoSlideVerify'
import {findCurrentUserButton, findCurrentUserSidebar} from "@/api/role"

export default {
  name: 'Login',
  components: {DoPasswordInput, slideverify},
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      // 是否开启滑块验证功能
      isShowImageCaptcha: false,
      // 滑块相关参数
      dialogVisible: false,
      block_y: '',
      imgurl: '',
      miniimgurl: '',
      checkMoveId: '',

      rememberMe: false,
      loading: false,
      loginRules: {
        username: [{required: true, message: "请输入账号", trigger: ['change', 'blur']}, {
          pattern: /^[0-9A-Za-z_]{1,12}$/, //正则
          message: '请输入6-12位账号,字母数字下划线组合'
        }],
        password: [{required: true, message: "请输入密码", trigger: ['change', 'blur']}, {
          pattern: /^[0-9A-Za-z_]{6,12}$/, //正则
          message: '请输入6-12位新密码,字母数字下划线组合'
        }],
      },
    }
  },
  mounted() {
    // 清空历史存储的面包屑
    sessionStorage.setItem("theBreadcrumb1", '')
    sessionStorage.setItem("theBreadcrumb2", '')

    this.isNeedVerify()
    this.init()
  },
  methods: {
    init () {
      let rememberPassword = Cookies.get("password")
      if (!isEmpty(rememberPassword)) {
        rememberPassword = CryptoJS.AES.decrypt(rememberPassword, 'secret key 123').toString(CryptoJS.enc.Utf8)
        this.loginForm.password = rememberPassword
        this.loginForm.username = Cookies.get("username")
        this.rememberMe = true
      } else {
        this.rememberMe = false
      }
    },
    async isNeedVerify() {
      let data = await getVerifyState()
      this.isShowImageCaptcha = data.data.data
    },
    login() {
      const that = this
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          if (that.isShowImageCaptcha) {
            that.dialogVisible = true
            that.getImageVerifyCode()
          } else {
            that.handleLogin()
          }
        } else {
          return false
        }
      })
    },
    async getImageVerifyCode () {
      const that = this
      that.imgurl = ''
      that.miniimgurl = ''
      that.block_y = ''
      let data = await getVerifyCode()
      let imgObj = data.data.data
      that.block_y = imgObj.yHeight
      that.imgurl = 'data:image/png;base64,' + imgObj.bigImage
      that.miniimgurl = 'data:image/png;base64,' + imgObj.smallImage
      that.checkMoveId = imgObj.checkMoveId
      if (that.$refs.dialogopen) {
        that.$refs.dialogopen.reset(imgObj.yHeight)
      }
    },
    imageVerifySuccess(left) {
      this.handleLogin(left)
    },
    imageVerifyFail() {
      this.getImageVerifyCode()
    },
    imageVerifyRefresh() {
      this.getImageVerifyCode()
    },
    onRefresh: throttle(function () {
      this.getImageVerifyCode()
    }, 1000),
    handleLogin(left) {
      let that = this
      let parms = Object.assign({}, this.loginForm)
      parms.password = Md5(parms.password)
      if (that.isShowImageCaptcha) {
        parms.xWidth = left
        parms.checkMoveId = that.checkMoveId
      }
      this.loading = true
      login(parms).then(rep => {
        if (rep.data.code === 1) {
          sessionStorage.setItem("UserInfo", JSON.stringify(rep.data.data))
          that.saveRememberPassword()
          that.$router.push('/')
        } else {
          //判断错误提示是否有滑块关键字，有就表示需要刷新滑块重新验证
          let msg = rep.data.msg;
          if (msg.indexOf('滑块') !== -1) {
            that.getImageVerifyCode()
          } else {
            that.dialogVisible = false;
          }
          that.$message.error(rep.data.msg)
        }
      }).finally(() => {
        that.loading = false
      })
    },
    saveRememberPassword() {
      if (this.rememberMe) {
        let rememberPassword = CryptoJS.AES.encrypt(this.loginForm.password, 'secret key 123');
        //自动保存密码设置为7天过期
        Cookies.set("username", this.loginForm.username, {expires: 7})
        Cookies.set("password", rememberPassword, {expires: 7})
      } else {
        Cookies.remove("username")
        Cookies.remove("password")
      }
    }
  }
}
</script>
<style lang="scss" scoped>

$bg: #fff;
$light_gray: #000;
$cursor: #000;

/* reset element-ui css */
.login-container {
  height: 100%;
  min-height: 550px;
  min-width: 1100px;
  width: 100%;
  background: url('../../assets/image/bg.png');
  background-size: 100% 100%;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 500px;
    max-width: 100%;
    padding: 40px 40px 0 30px;
    overflow: hidden;

    /deep/ .el-form-item {
      border: 1px solid rgb(192, 189, 189);
      background: #fff;
      border-radius: 5px;
      color: #000;
    }
  }

  /deep/ .el-input {
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

  .center {
    display: -webkit-flex; /* Safari */
    display: flex;
    flex-direction: row;
    width: 73%;
    height: 50%;
    min-width: 800px;
    position: relative;
    top: 42%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
  }
}

/deep/ .el-button--primary {
  background: #409EFF;
  border: none;
}

/deep/ .el-button--primary:hover, .el-button--primary:focus {
  background: #66B1FF;
}

.svg-container {
  padding: 6px 5px 6px 15px;
  color: #889aa4;
  vertical-align: middle;
  width: 30px;
  display: inline-block;
  box-sizing: border-box;
}

.title-container {
  position: relative;
  max-width: 360px !important;

  .title {
    font-size: 28px;
    color: #222222;
    margin: 0px auto 30px auto;
    text-align: center;
    font-weight: 548;
  }
}

.tips {
  font-size: 14px;
  color: #000;
  cursor: pointer;

  :hover {
    color: #008080;
  }
}

.wid,
.el-button,
.user_name {
  max-width: 360px;
}

/deep/ .el-checkbox__inner is-checked {
  background: #409EFF;
}

</style>
