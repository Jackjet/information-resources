<template>
  <div class="header-nav" :style="{ backgroundColor: back,position: abs,zIndex:'100' }">
    <div class="logo-text">河北区信息资源共享门户</div>
    <ul class="nav-list">
      <li :class="tabsIndex === 1? 'active-home': ''" @click="tabsChange(1)">
        首页
      </li>
      <li :class="tabsIndex === 2?'active-tabs': ''" @click="tabsChange(2)">
        目录资源
      </li>
      <li :class="tabsIndex === 3 ? 'active-tabs' : ''" @click="tabsChange(3)">
        部门需求
      </li>
      <li :class="tabsIndex === 5? 'active-tabs': ''" @click="tabsChange(5)">
        下载中心
      </li>
      <li :class="tabsIndex === 6? 'active-tabs': ''" @click="tabsChange(6)">
        <el-tooltip placement="bottom" :manual="!name">
          <div slot="content">
            <a target="_blank" v-for="item in servePath" :key="item.num" style="display: block;color: #fff; font-size: 14px; line-height: 30px" :href="item.path">{{item.name}}</a>
          </div>
          <span>服务指引</span>
        </el-tooltip>
      </li>
      <li :class="tabsIndex === 4 ? 'active-tabs' : ''" @click="tabsChange(4)">
        网站相关
      </li>
    </ul>
    <el-tooltip placement="bottom">
      <div slot="content">
        <p style="display: flex; align-items: center" @click="routerPersonal">
          <img style="width: 20px; margin-right: 10px" src="@/assets/image/icon/用户中心.png" alt="" />
          用户中心
        </p>
        <p v-if="name" style="display: flex; align-items: center" @click="signOutClick">
          <img style="width: 20px; margin-right: 10px" src="@/assets/image/icon/退出登录.png" alt="" />
          退出登录
        </p>
      </div>
      <div class="info-user">
        <img src="@/assets/image/icon/头像.png" alt="" /> {{ name }}
      </div>
    </el-tooltip>
  </div>
</template>

<script>
import bus from '@/utils/bus';
import { getToken, removeToken } from '@/utils/storage.js';
import { mapGetters } from 'vuex';
import config from '@/config/index.js';
export default {
  data() {
    return {
      name: '',
      tabsIndex: 1,
      servePath: [],
      back: "",
      abs: "absolute",
    };
  },
  computed: {
    ...mapGetters(['token'])
  },
  watch: {
    '$route': {
      immediate: true,
      handler(to) { // 在函数中可以写自己的判断 to是要去的路由，from来时的路由
        // console.log(to.path, from, "=========")
        if (to.path === '/home') { //  是否跳转到index页面
          this.tabsIndex = 1;
        } else if (to.path === '/catalogue') {
          this.tabsIndex = 2;
        } else if (to.path === '/demand') {
          this.tabsIndex = 3;
        } else if (to.path === '/relevant') {
          this.tabsIndex = 4;
        } else if (to.path === '/download') {
          this.tabsIndex = 5;
        } else {
          this.tabsIndex = 0;
          this.back = "#568bd5";
          this.abs = "";
        }
      }
    },
    'tabsIndex': function () {
      if (this.tabsIndex === 1) {
        this.back = "";
        this.abs = "absolute";
      } else {
        this.back = "#568bd5";
        this.abs = "";
      }
    },
    'token': function (data) {
      if (data) {
        // 项目跳转指引
        this.guideFindAll().then(res => {
          if (res.code === 1) {
            this.servePath = res.data;
          } else {
            this.$message.error(res.msg);
          }
        })
      }
    }
  },
  created() {
    bus.$on('name', (userName) => {
      this.name = userName;
    })
    // this.tabsIndex = getSessionStorage('navActive') * 1;
  },
  methods: {
    tabsChange(index) {
      if (index === 1) {
        this.$router.push("/home");
      } else if (index === 2) {
        this.$router.push("/catalogue");
      } else if (index === 3) {
        if (getToken()) {
          this.$router.push("/demand");
        } else {
          this.$confirm('此操作需要登录, 是否跳转至登录页?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            location.href = "login.html?url=" + config.baseURL + "web/index.html";
          }).catch(() => {
            return false;
          });
        }
      } else if (index === 4) {
        this.$router.push("/relevant");
      } else if (index === 5) {
        this.$router.push("/download");
      } else if (index === 6) {
        if (!getToken()) {
          this.$confirm('此操作需要登录, 是否跳转至登录页?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            window.location.href = "login.html?url=" + config.baseURL + "web/index.html";
          }).catch(() => {
            return false;
          });
        }
      }
    },
    routerPersonal() {
      if (getToken()) {
        this.tabsIndex = 0;
        this.$router.push("/personal");
      } else {
        this.$confirm('此操作需要登录, 是否跳转至登录页?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          location.href = "login.html?url=" + config.baseURL + "web/index.html";
        }).catch(() => {
          return false;
        });
      }
    },
    signOutClick() {
      const that = this;
      this.$confirm('是否退出登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        try {
          this.webAdminSignOut({}).then((res) => {
            if (res.code === 1) {
              bus.$emit("name", "");
              this.$store.dispatch('user/removeLogin');
              removeToken();
              this.$router.push('/home');
            } else {
              that.$message.error(res.msg);
            }
          });
        } catch (even) {
          that.$message.error(even.msg);
        }
      }).catch(() => {
        return false;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.header-nav {
  width: 100%;
  height: 60px;
  // display: flex;
  // justify-content: space-between;
  // align-items: center;
  padding: 0 50px;
  box-sizing: border-box;
  color: #fff;
  .logo-text {
    font-size: 20px;
    font-weight: bold;
    font-style: oblique;
    line-height: 60px;
    float: left;
  }
  .nav-list {
    float: left;
    margin-left: 150px;
    margin-top: 15px;
    width: 40%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: $font-size-big;
    cursor: pointer;
    .active-tabs {
      border-bottom: 2px solid #fff;
      padding-bottom: 5px;
      margin-bottom: -6px;
    }
    .active-home {
      color: #0080ff;
      border-bottom: 2px solid #0080ff;
      padding-bottom: 5px;
      margin-bottom: -6px;
    }
  }
  .info-user {
    margin-top: 10px;
    float: right;
    display: flex;
    align-items: center;
    font-size: $font-size-big;
    cursor: pointer;
    img {
      width: 40px;
      margin-right: 10px;
    }
  }
}
.el-tooltip__popper p {
  width: 100px;
  text-align: center;
  line-height: 30px;
  font-size: 16px;
  cursor: pointer;
}
</style>