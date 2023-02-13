<template>
  <div class="theHeader">
    <el-breadcrumb separator='/' class="the-breadcrumb">
      <el-breadcrumb-item style="cursor: pointer;" v-for="(item, index) in menuData" :index="index" :key="index">
        <span @click.prevent="clickMenu(item)">{{ item.title }}</span>
      </el-breadcrumb-item>
      <el-breadcrumb-item style="cursor: pointer;" v-for="(item, index) in routerData" :index="index" :key="index">
        <span @click.prevent="clickRouter(item)">{{ item.title }}</span>
      </el-breadcrumb-item>
    </el-breadcrumb>
    <div class="right-menu">
      <el-dropdown placement="bottom-start" class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <svg t="1600311663297" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5329" width="35" height="40">
            <path d="M512 148.5c49.1 0 96.7 9.6 141.5 28.5 43.3 18.3 82.2 44.5 115.6 77.9 33.4 33.4 59.6 72.3 77.9 115.6 18.9 44.8 28.5 92.4 28.5 141.5s-9.6 96.7-28.5 141.5c-18.3 43.3-44.5 82.2-77.9 115.6-33.4 33.4-72.3 59.6-115.6 77.9-44.8 18.9-92.4 28.5-141.5 28.5s-96.7-9.6-141.5-28.5c-43.3-18.3-82.2-44.5-115.6-77.9-33.4-33.4-59.6-72.3-77.9-115.6-18.9-44.8-28.5-92.4-28.5-141.5s9.6-96.7 28.5-141.5c18.3-43.3 44.5-82.2 77.9-115.6s72.3-59.6 115.6-77.9c44.8-18.9 92.4-28.5 141.5-28.5m0-50C283.6 98.5 98.5 283.6 98.5 512S283.6 925.5 512 925.5 925.5 740.4 925.5 512 740.4 98.5 512 98.5z" p-id="5330" fill="#606266"></path>
            <path d="M512 401m-106 0a106 106 0 1 0 212 0 106 106 0 1 0-212 0Z" p-id="5331" fill="#606266"></path>
            <path d="M514.5 540.8c-105.1 0-195 68.8-231.6 166.2 28.2 27.9 60.4 50.1 95.9 65.9 40.2 18 83 27.1 127.1 27.1s86.9-9.1 127.1-27.1c38.9-17.4 73.8-42.3 103.8-74.1 1.4-1.5 2.7-3 4.1-4.4-39.2-90.6-125.8-153.6-226.4-153.6z" p-id="5332" fill="#606266"></path>
          </svg>
          <span class="na">{{ name }}</span>
          <i class="icon el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu style="margin-top: 10px;" slot="dropdown" class="user-dropdown">
          <!-- <el-dropdown-item @click.native="openDialog">
            <span style="display:block;">修改密码</span>
          </el-dropdown-item> -->
          <el-dropdown-item @click.native="logout">
            <span style="display:block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="40%" :before-close="closeDialog">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" v-loading='loading' label-width="100px" style="width: 90%;">
        <el-form-item prop="oldPassword" label="原密码" :label-width="this.formLabelWidth">
          <el-input clearable autocomplete="off" v-model="ruleForm.oldPassword" placeholder="请输入原密码"></el-input>
        </el-form-item>
        <el-form-item prop="newPassword" label="新密码" :label-width="this.formLabelWidth">
          <el-input clearable autocomplete="off" v-model="ruleForm.newPassword" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item prop="quePassword" label="确认密码" :label-width="this.formLabelWidth">
          <el-input clearable autocomplete="off" v-model="ruleForm.quePassword" placeholder="请确认密码"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog">取 消</el-button>
          <el-button type="primary" @click="edit('ruleForm')">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import bus from '@/utils/bus'
import Md5 from 'js-md5'
import { resetPassword } from '@/api/user'
import { ssoLogout } from '@/api/user1'
import { removeCookies, getCookies } from '@/utils/auth';

export default {
  name: 'MyHeader',
  data() {
    return {
      name: '',
      dialogVisible: false,
      loading: false,
      menuData: [{
        title: '首页',
        path: '/homePage'
      }],
      routerData: [],
      ruleForm: {
        oldPassword: '',
        newPassword: '',
        quePassword: ''
      },
      rules: {
        oldPassword: [{
          required: true,
          message: '请输入原密码',
          trigger: 'blur'
        }],
        newPassword: [{
          required: true,
          message: '请输入新密码',
          trigger: 'blur'
        }, {
          pattern: /^[0-9A-Za-z_]{6,12}$/, //正则
          message: '请输入6-12位新密码,字母数字下划线组合'
        }],
        quePassword: [{
          required: true,
          message: '请确认密码',
          trigger: 'blur'
        }, {
          pattern: /^[0-9A-Za-z_]{6,12}$/, //正则
          message: '请输入6-12位新密码,字母数字下划线组合'
        }]
      },
      isCollapse: false,
      storeData: {}
    }
  },
  computed: {
    theBreadcrumb() {
      this.storeData = this.$store.state.user;
    }
  },
  created() {
    // 如果存储了面包屑信息
    if (this.storeData.theBreadcrumb1) {
      bus.$emit('menuData', JSON.parse(this.storeData.theBreadcrumb1))
      this.menuData = JSON.parse(this.storeData.theBreadcrumb1)
    }
    if (this.storeData.theBreadcrumb2) {
      bus.$emit('routerData', JSON.parse(this.storeData.theBreadcrumb2))
      this.routerData = JSON.parse(this.storeData.theBreadcrumb2)
    }
    // 通过bus同步路由变化
    bus.$on('menuData', msg => {
      if (JSON.stringify(this.menuData) !== JSON.stringify(msg)) {
        this.routerData.splice(0)
        this.$store.dispatch('setTheBreadcrumb2', JSON.stringify(this.routerData));
        this.menuData = msg
        this.$store.dispatch('setTheBreadcrumb1', JSON.stringify(this.menuData));
      }
    })
    bus.$on('routerData', msg => {
      this.routerData = msg
      this.$store.dispatch('setTheBreadcrumb2', JSON.stringify(this.routerData));
    })
    // 通过bus同步路由变化
    bus.$on('logout', () => {
      this.toDoLogout()
    })
    this.name = JSON.parse(getCookies("userInfo")).name;
  },
  methods: {
    // 点击侧边栏对应面包屑
    clickMenu(item) {
      if (this.$route.path === item.path) {
        return false
      }
      this.routerData.splice(0)
      this.$store.dispatch('setTheBreadcrumb2', JSON.stringify(this.routerData));
      this.$router.push(item.path)
    },
    // 点击路由对应面包屑
    clickRouter(item) {
      if (this.$route.path === item.path) {
        return false
      }
      let theIndex = ''
      this.routerData.forEach((ele, index) => {
        if (item.path === ele.path) {
          theIndex = index
        }
      })
      if (this.routerData.length === (theIndex + 1)) {
        return false
      } else {
        this.routerData.splice(theIndex)
      }
      this.$router.push({
        'path': item.path,
        'query': item.data
      })
    },
    logout() {
      const that = this
      that.$confirm("是否退出登录?", "提示", {
        type: "warning"
      }).then(async () => {
        that.toDoLogout()
      }).catch(() => {
        return false
      })
    },
    async toDoLogout() {
      const that = this
      try {
        let res = await ssoLogout()
        if (res.data.code === 1) {
          removeCookies('userInfo');
          window.location.reload();
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        that.$message.error(even.msg)
      }
    },
    openDialog() {
      this.dialogVisible = true
    },
    edit(ruleForm) {
      const that = this
      this.$refs["ruleForm"].validate(async (valid) => {
        if (valid) {
          if (that.ruleForm.newPassword !== that.ruleForm.quePassword) {
            return this.$message.error('新密码和确认密码输入不一致!')
          }
          const data = {
            id: JSON.parse(getCookies("userInfo")).id,
            newPassword: Md5(that.ruleForm.newPassword),
            oldPassword: Md5(that.ruleForm.oldPassword)
          }
          try {
            let res = await resetPassword(data)
            if (res.data.code === 1) {
              that.$message.success('修改密码成功')
              that.closeDialog()
              that.toDoLogout()
            } else {
              that.$message.error(res.data.msg)
            }
          } catch (even) {
            that.$message.error(even.msg)
          }
        } else {
          return false
        }
      })
    },
    closeDialog() {
      this.ruleForm.oldPassword = ''
      this.ruleForm.newPassword = ''
      this.ruleForm.quePassword = ''
      this.$refs["ruleForm"].resetFields()
      this.dialogVisible = false
    }
  },
  watch: {
    // 监听路由变化
    $route(to, from) {
      const that = this
      if (this.storeData.theBreadcrumb2) {
        that.routerData = JSON.parse(this.$store.state.theBreadcrumb2)
      }
      if (to.path === '/homePage') {
        return false
      }
      let flag = false
      let theIndex = 0
      that.routerData.forEach((ele, index) => {
        if (to.path === ele.path) {
          theIndex = index
          flag = true
        }
      })
      if (flag) {
        that.routerData.splice(theIndex)
      }
      let toPathData = {}
      that.$router.options.routes.forEach(item => {
        if (item.path === '/') {
          item.children.forEach(sub => {
            if (sub.path === to.path) {
              toPathData.title = sub.name
              toPathData.path = sub.path
              toPathData.data = this.$route.query
            }
          })
        }
      })
      that.routerData.push(toPathData)
      this.$store.dispatch('setTheBreadcrumb2', JSON.stringify(this.routerData));
    }
  }
}
</script>

<style lang="scss" scoped>
.theHeader {
  background-color: #fff;
  width: 100%;
  height: 100%;
  position: relative;
  display: flex;
  align-items: center; /* 垂直居中 */
  .the-breadcrumb {
    font-size: 16px;
    padding-left: 50px;
    /deep/ .el-breadcrumb__inner {
      color: $color-Breadcrumb;
      &:hover {
        color: $color-BreadcrumbHover;
      }
    }
  }
  .right-menu {
    position: absolute;
    right: 0;
    top: 0;
    height: 100%;
    width: 150px;

    .avatar-container {
      height: 100%;
      width: auto;
      position: relative;
      .avatar-wrapper {
        display: flex;
        align-items: center;
        color: #606266;
        font-size: 16px;
        height: 100%;
        .na {
          -webkit-user-select: none;
          user-select: none;
        }
        .icon {
          font-size: 16px;
          margin-left: 5px;
        }
      }
    }
  }
}
</style>
