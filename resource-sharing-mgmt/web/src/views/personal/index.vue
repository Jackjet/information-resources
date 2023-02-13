<template>
  <div class="personal-core">
    <Breadcrumb :returnRouter="[{path:'',name:'个人中心'}]"></Breadcrumb>
    <div class="personal-box">
      <div class="personal-left">
        <div class="left-top-heade">
          <!-- <img id="heade-bottom" src="@/assets/image/icon/图片1.png" alt="" />
          <img id="heade-top" src="@/assets/image/icon/图片2.png" alt="" />
          <img id="touxiang" src="@/assets/image/icon/头像.png" alt="" /> -->
          <h2>{{ userInfo }}</h2>
          <!-- <h3>{{ userInfo.organizationName }}</h3> -->
        </div>
        <el-menu :default-active="activeNum" @select="selectOPen" class="el-menu-vertical-demo">
          <el-menu-item index="1">
            <span class="title-icon" slot="title">个人信息</span>
            <i class="el-icon-arrow-right"></i>
          </el-menu-item>
          <el-menu-item index="2">
            <span class="title-icon" slot="title">我的需求</span>
            <i class="el-icon-arrow-right"></i>
          </el-menu-item>
          <el-menu-item index="3">
            <span class="title-icon" slot="title">我关注的目录</span>
            <i class="el-icon-arrow-right"></i>
          </el-menu-item>
          <el-menu-item index="6">
            <span class="title-icon" slot="title">我的申请</span>
            <i class="el-icon-arrow-right"></i>
          </el-menu-item>
          <el-menu-item index="5">
            <span class="title-icon" slot="title">我的待办</span>
            <i class="el-icon-arrow-right"></i>
          </el-menu-item>
          <el-menu-item index="7">
            <span class="title-icon" slot="title">修改密码</span>
            <i class="el-icon-arrow-right"></i>
          </el-menu-item>
        </el-menu>
      </div>
      <div class="personal-right">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>
<script>
import config from '@/config/index.js';
import Breadcrumb from "com/breadcrumb";
import bus from '@/utils/bus';
export default {
  data() {
    return {
      userInfo: "",
      activeNum: "1",
    };
  },
  components: {
    Breadcrumb,
  },
  watch: {
    '$route': {
      immediate: true,
      handler(to) { // 在函数中可以写自己的判断 to是要去的路由，from来时的路由
        if (to.path === '/personal/info') { //  是否跳转到index页面
          this.activeNum = '1';
        } else if (to.path === '/personal/demand' || to.path === '/personal/demandDetails') {
          this.activeNum = '2';
        } else if (to.path === '/personal/follow') {
          this.activeNum = '3';
        } else if (to.path === '/personal/apiList') {
          this.activeNum = '4';
        } else if (to.path === '/personal/agency') {
          this.activeNum = '5';
        } else if (to.path === '/personal/weApply') {
          this.activeNum = '6';
        } else if (to.path === '/personal/editPassword') {
          this.activeNum = '7';
        }
      }
    }
  },
  created() {
    bus.$on('name', (userName) => {
      this.userInfo = userName;
    })
  },
  methods: {
    selectOPen(index) {
      if (index === "1") {
        this.$router.push("/personal/info");
      } else if (index === "2") {
        this.$router.push("/personal/demand");
      } else if (index === "3") {
        this.$router.push("/personal/follow");
      } else if (index === "4") {
        this.$router.push("/personal/apiList");
      } else if (index === "5") {
        this.$router.push("/personal/agency");
      } else if (index === "6") {
        this.$router.push("/personal/weApply");
      } else if (index === "7") {
        // this.$router.push("/personal/editPassword");
        window.open(config.authBaseURL)
      }
    },
  },
};
</script>
<style lang="scss" scope>
.personal-core {
  margin-bottom: 30px;
  .personal-box {
    width: 80%;
    margin: auto;
    display: flex;
    .personal-left {
      width: 200px;
      height: 600px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      .left-top-heade {
        background: url("~@/assets/image/ziyuan/个人中心头像.svg");
        background-size: 100% 100%;
        height: 180px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        position: relative;
        h2 {
          margin-top: 100px;
          color: #fff;
        }
        #touxiang {
          width: 80px;
        }
        #heade-bottom {
          position: absolute;
          left: 0;
          bottom: 0;
          width: 80px;
        }
        #heade-top {
          position: absolute;
          right: 0;
          top: 0;
          width: 80px;
        }
      }
      .el-menu-vertical-demo {
        background: none;
        border: none;
        img {
          width: 20px;
        }
      }
    }
    .personal-right {
      flex: 1;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      margin-left: 15px;
      box-sizing: border-box;
      overflow: hidden;
    }
  }
}
.title-icon {
  padding-left: 20px;
}
.title-icon::before {
  content: "";
  position: absolute;
  left: 14px;
  top: 46%;
  border: 1px solid #0080ff;
  background-color: #0080ff;
  display: inline-block; // 此句为css样式展示重点🏁
  width: 5px;
  height: 5px;
  border-radius: 50%;
  margin-right: 12px;
}
.el-menu-item {
  padding-left: 40px;
}
.el-menu-item i {
  display: none;
}
.el-menu-item.is-active i {
  float: right;
  line-height: 54px;
  font-size: 16px;
  display: block;
}

.personal-left .el-menu-item:focus,
.personal-left .el-menu-item:hover {
  background: none !important;
}
</style>