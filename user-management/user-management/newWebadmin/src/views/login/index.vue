<template>
  <div class="login-container">
    <div class="center">
      <div class='pingtai'>集成中枢<span style="color:#00c3e0">平台</span></div>
      <div class='pingtai1'>JICHENGZHONGSHUPINGTAI</div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <div class="title-container">
          <h3 class="title">用户登陆</h3>
        </div>
        <el-form-item prop="username" class="item user_name" >
            <span class="svg-container">
            <i class="el-icon-user"/>
          </span>
          <el-input maxlength='100' ref="username"  v-model="loginForm.username1" placeholder="请输入账号" name="username" type="text" tabindex="1" style="opacity: 0;position: absolute"/>
          <el-input maxlength='100' ref="username"  v-model="loginForm.username" placeholder="请输入账号" type="text" tabindex="1"/>
        </el-form-item>
        <el-form-item prop="password" class="item user_name" >
             <span class="svg-container">
            <i class="el-icon-lock"/>
          </span>
          <do-password-input name="password" v-model="loginForm.password1" @keyup.enter.native="login" style="opacity: 0;position: absolute"></do-password-input>
          <do-password-input autocomplete="new-password" @click="defaultClick(e)" v-model="loginForm.password" @keyup.enter.native="login"></do-password-input>
          <!-- <el-input maxlength='100' ref="password"  v-model="loginForm.password" placeholder="请输入密码" name="password" type="password"  @keyup.enter.native="login" tabindex="1" style="opacity: 0;position: absolute"/> -->
          <!-- <el-input maxlength='100' ref="password1" autocomplete="new-password"  v-model="loginForm.password" placeholder="请输入密码" name="password" type="password" @focus="defaultClick"  @keyup.enter.native="login" tabindex="1"/> -->

        </el-form-item>
        <div class="tips wid">
          <el-checkbox v-model="rememberMe" style="margin-right:5px; margin-bottom: 22px;" label="记住密码" name="type"></el-checkbox>
          <span></span>
        </div>
        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:5px; height: 45px; margin-left: 0px;" @click.native.prevent="login">
          登录
        </el-button>
        
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
    document.title='集成中枢系统'
    // 清空历史存储的面包屑
    sessionStorage.setItem("theBreadcrumb1", '')
    sessionStorage.setItem("theBreadcrumb2", '')
    this.isNeedVerify()
    this.init()
  },
  created() {
    document.title='集成中枢系统'
  },
  destroyed() {
    document.title='集成中枢系统'
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
$light_gray: white;
$cursor: #000;

/* reset element-ui css */
.login-container {
  height: 100%;
  min-height: 550px;
  min-width: 1100px;
  width: 100%;
  background: url('../../assets/image/background.png');
  background-size: 100% 100%;
  overflow: hidden;
  .login-form {
    position: relative;
    width: 500px;
    background: url('../../assets/image/loginborder.png');
    background-size: 100% 100%;
    padding-bottom: 30px;
    max-width: 100%;
    overflow: hidden;
    /deep/ .el-form-item {
      /deep/ .el-form-item__content{
        width: 70%;
        margin-left: 15%;
          background: url('../../assets/image/inputborder.png');;
          background-size: 100% 100%;
          position: relative;
          .tit{
            position: absolute;
            left: -40px;
          }
      }
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
      color: #fff;
      height: 35px;
      caret-color: $cursor;
    }
  }
  /deep/ input:-webkit-autofill {
    box-shadow: 0 0 0px 1000px transparent inset !important;
    -webkit-text-fill-color: #fff !important;
    transition: background-color 5000s ease-in-out 0s;
  }

  /deep/ input:-webkit-autofill:focus {
    -webkit-text-fill-color: #fff !important;
  }

  .center {
    display: -webkit-flex; /* Safari */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 40%;
    height: 55%;
    position: relative;
    top: 45%;
    left: 70%;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    .pingtai{
      font-size: 30px;
      color: #00a0f1;
      font-family:"楷体","楷体_GB2312";
    }
    .pingtai1{
      font-size: 20px;
      color: #00a0f1;
      font-family:"宋体","宋体_GB2312";
      letter-spacing:5px;
      line-height: 40px;
    }
  }
}

/deep/ .el-button--primary {
  background: #0BA1F8;
  border: none;
  border-radius: 0;
}

/deep/ .el-button--primary:hover, .el-button--primary:focus {
  background: #66B1FF;
}

.svg-container {
  padding: 0 15px 0 15px;
  color: #0a84b1;
  font-size: 20px;
  font-weight: 900;
  vertical-align: middle;
  width: 30px;
  display: inline-block;
  box-sizing: border-box;
}

.title-container {
  position: relative;

  .title {
    font-size: 20px;
    color: #0a84b1;
    margin: 5px auto;
    padding: 10px 0;
    text-align: center;
    font-weight: 548;
  }
}

.tips {
  font-size: 14px;
  color: #000;
  cursor: pointer;
  width: 70%;
  margin-left: 16%;
  line-height: 20px;
  :hover {
    color: #008080;
  }
  /deep/ .el-checkbox {
    color: white;
  }
}


.el-button{
  width: 68% !important;
  margin: 5 20px !important;
  margin-left: 16% !important;
}
/deep/ .el-checkbox__inner{
  background:#154e88;
  border: 2px solid #1970b4;
}

/deep/.el-checkbox__inner::after {
  top: 0;
}
/deep/ input:-webkit-autofill {
    box-shadow: 0 0 0px 1000px transparent inset !important;
    -webkit-text-fill-color: $cursor !important;
    transition: background-color 5000s ease-in-out 0s;
  }
  /deep/ input:-webkit-autofill:focus {
    -webkit-text-fill-color: #333 !important;
  }
 /deep/ input:-internal-autofill-selected {
 background-color: #000000 !important;
 }
</style>
