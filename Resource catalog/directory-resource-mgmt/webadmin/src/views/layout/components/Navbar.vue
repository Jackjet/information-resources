<template>
  <div class="navbar">
    <img :src="logo" alt="" width="30px" class="logo" style="">
    <span class="logo_text">资源目录管理系统</span>
    <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container" />
    <breadcrumb v-if="showBreadcrumb" class="breadcrumb-container" />
    <div class="right-menu">
      <!-- <el-button  class="remove fl" size="mini" style="margin-top: 15px;margin-right: 15px;" @click="goComplex">综合门户</el-button> -->
      <!--<el-button plain  size="mini" style="margin-top: 15px;margin-right: 15px;" @click="goComplex">综合门户</el-button>-->
      <template v-if="device!=='mobile'">

        <!--    <el-tooltip content="全屏"  effect="light" :open-delay="1000" placement="bottom">
          <screenfull class="right-menu-item" />
        </el-tooltip>-->

      </template>

      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <img :src="_avatar+'?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom" />
          <span class="name">{{ name }}</span>
        </div>

        <el-dropdown-menu slot="dropdown">
          <router-link to="/">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>
          <!-- <el-dropdown-item divided>
            <router-link to="/profile/password">
              密码修改
            </router-link>
          </el-dropdown-item> -->
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      <!-- <el-tooltip content="退出"  effect="light" :open-delay="1000" placement="bottom">
        <img :src="loginoutPng" alt="" width="16px" style="margin-right: 32px;margin-top: 22px;margin-left: 20px;cursor: pointer" @click="logout">
      </el-tooltip> -->
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Notice from '@/components/Notice'
import loginoutPng from '@/assets/loginout.png'
import logo from '@/assets/logo.png'
import avatar from '@/assets/avatar.svg'
import Breadcrumb from '@/components/Breadcrumb'
import router from "@/router/index.js";

export default {
  components: {
    Hamburger,
    Screenfull,
    SizeSelect,
    Notice,
    Breadcrumb
  },
  data() {
    return {
      loginoutPng: loginoutPng,
      logo: logo,
      _avatar: ''
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar',
      'portalUrl',
      'device'
    ])
  },
  created() {
    this._avatar = this.avatar ? this.avatar : avatar
  },
  methods: {
    showBreadcrumb() {
      return this.$route.name !== 'Dashboard'
    },
    toggleSideBar() {
      this.$store.dispatch('toggleSideBar')
    },
    logout() {
      /* this.$store.dispatch('LogOut').then(() => {
         this.$router.push(`/login?redirect=${this.$route.fullPath}`)
       })*/
      this.$store.dispatch('keyLoadLogOut').then(() => {
        router.app.$keycloak.logoutFn()
      })

    },
    goComplex() {
      window.open(this.portalUrl)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.logo {
  float: left;
  position: relative;
  top: 16px;
}
.logo_text {
  float: left;
  position: relative;
  top: 9px;
  margin-left: 10px;
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  line-height: 46px;
  color: rgba(23, 40, 83, 1);
  opacity: 1;
}
.name {
  position: relative;
  top: -10px;
}
.navbar {
  z-index: 10;
  background: #fff;
  position: fixed;
  top: 0;
  width: 100%;
  padding: 0 20px;
  height: 64px;
  line-height: 64px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 70px;
    height: 65px;
    float: left;
    padding: 0 10px;
  }
  .breadcrumb-container {
    float: left;
  }
  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }
  .right-menu {
    float: right;
    height: 100%;
    &:focus {
      outline: none;
    }
    .right-menu-item {
      display: inline-block;
      margin: 0 8px;
      vertical-align: top;
    }
    .avatar-container {
      height: 65px;
      margin-right: 30px;
      .avatar-wrapper {
        cursor: pointer;
        position: relative;
        padding-top: 10px;
        .user-avatar {
          width: 36px;
          height: 36px;
          border-radius: 18px;
        }
        .el-icon-caret-bottom {
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
