<template>
  <div class='loginBack'>
    <!-- 元素滚动特效 -->
    <div>
      <ul class="circles">
        <li v-for="index in 10" :key="index"></li>
      </ul>
    </div>
    <el-container>
      <el-main>
        <el-form class='login-container' :rules="FormRules" ref="FormValidation"
                 :model="FormValidation"
                 label-position='left'
                 label-width='0px'>
          <div class="title">
            <img v-bind:src="logo" alt="logo" style="margin-bottom: -5px;width: 40px;height: 40px"/>
            <span>资源交换管理系统</span>
          </div>
          <div class='containers'>
            <el-form-item prop='UserName'>
              <el-input type='text'
                        clearable
                        v-model="FormValidation.UserName"
                        auto-complete='off' placeholder="账 号">
              </el-input>
            </el-form-item>
            <el-form-item prop='PassWord'>
              <el-input type='password'
                        clearable
                        v-model="FormValidation.PassWord"
                        auto-complete='off' placeholder="密 码">
              </el-input>
            </el-form-item>
            <el-checkbox class="remember" v-model="checked">记住密码</el-checkbox>
            <el-form-item class='Percentage'>
              <el-button type="primary" id='isloginBut'
                         :loading="isBtnLoading"
                         @keyup.enter.native="inloginSign('FormValidation')"
                         @click="inloginSign('FormValidation')">
                {{loginBtnClick}}
              </el-button>
            </el-form-item>
          </div>
        </el-form>
      </el-main>
    </el-container>
    <div class='copyright'>
      <!-- Copyright <i class='Admin Admin-banquan'></i> 2019 辽宁云领计算机科技有限公司 -->
    </div>
  </div>
</template>

<script>
import md5 from 'js-md5'
import { toggleClass } from '../../store'

export default {
  data () {
    return {
      dengluING: false,
      FormValidation: {
        UserName: null,
        PassWord: null,
        type: 'tenants'
      },

      FormRules: {
        UserName: [{
          required: true,
          message: '账号不可为空',
          trigger: ['blur', 'change']
        }],
        PassWord: [{
          required: true,
          message: '密码不可为空',
          trigger: ['blur', 'change']
        }],
        type: [{
          required: true,
          message: '身份不可为空',
          trigger: ['blur', 'change']
        }]
      },

      checked: '',
      isBtnLoading: false,
      logo: './static/logo_white.svg'
    }
  },

  computed: {
    loginBtnClick () {
      if (this.isBtnLoading) return '登录中...'
      return '登 录'
    }
  },

  mounted () {
    const that = this
    that.GetCookieCache()

    if (that.cookie.get('theme')) {
      toggleClass(document.body, 'custom-' + that.cookie.get('theme'))
    } else {
      toggleClass(document.body, 'custom-' + 'blackGreen')
    }
  },

  created () {
    document.onkeydown = even => {
      const keyCode = even.keyCode
      const KeyUrl = this.$route.path

      if (keyCode === 13 && this.FormValidation.UserName !== '' && this.FormValidation.PassWord !== '' && KeyUrl === '/login') {
        document.getElementById('isloginBut').click()
      }
    }
  },

  methods: {
    async login (SignUrl, SignData) {
      const that = this
      that.dengluING = true
      try {
        const result = await that.request.signGET(that, SignUrl, SignData)
        if (result.data.code === 1) {
          that.common.session('currentUser', result.data.data)
          that.common.session('type', 2)
          that.$router.push('/index')
        } else {
          that.isBtnLoading = false
          that.dengluING = false
          that.$message.error(result.data.msg)
        }
      } catch (even) {
        that.dengluING = false
        that.isBtnLoading = false
        that.$message.error('服务未响应')
      }
    },

    inloginSign (ValParam) {
      const that = this

      // 初始化当前Cookie 是否存在 清除后添加Cookie 设置Cookie参数
      if (that.checked === true) {
        that.setCookie(that.FormValidation.UserName, that.FormValidation.PassWord, 7)
      } else {
        that.clearCookie()
      }

      that.$refs[ValParam].validate((index) => {
        /**
         * @param ValParam
         * 验证表单信息是否符合规则
         */
        if (index === false && typeof (index) === 'boolean') {
          return false
        }
        that.isBtnLoading = true
        const parameter = {
          username: that.FormValidation.UserName,
          password: md5(that.FormValidation.PassWord),
          type: that.FormValidation.type
        }

        const url = that.Interface.UserLogin.login
        /**
         * @param parameter Data参数
         * @param url 请求接口路径
         * Form表单请求登陆 POST 不携带token
         */

        that.login(url, parameter)
      })
    },

    setCookie (CookieName, CookiePwd, exDays) {
      const exDate = new Date()
      /**
       * @param CookieName 账号字段
       * @param CookiePwd  密码字段
       * @param exdays  保存时间
       * 设置Cookie有效期 以字符串拼接形式
       */
      exDate.setTime(exDate.getTime() + 24 * 60 * 60 * 1000 * exDays)
      window.document.cookie = 'userName' + '=' + CookieName + ';path=/;expires=' + exDate.toGMTString()
      window.document.cookie = 'passWord' + '=' + CookiePwd + ';path=/;expires=' + exDate.toGMTString()
    },

    GetCookieCache: function () {
      const that = this
      document.cookie.split('; ').forEach(element => {
        let array = element.split('=')
        /**
         * @param UserName
         * @param PassWord
         * 添加相应表单数据
         * 遍历获取Cookie参数 拆分字符串
         */
        switch (array[0]) {
          case 'UserName':
            that.FormValidation.UserName = array[1]
            that.checked = true
            break
          case 'PassWord':
            that.FormValidation.PassWord = array[1]
            that.checked = true
            break
          default:
            that.checked = false
        }
      })
    },

    clearCookie: function () {
      const that = this
      that.setCookie('', '', -1)
    }
  }
}
</script>

<style lang="scss">
  @import '~./login.scss';
</style>
