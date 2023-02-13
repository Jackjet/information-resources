<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <breadcrumb class="breadcrumb-container" />
    <div class="right-menu">
      <!-- <div class="robot-icon" @click="robotClick" @mouseover="mouseOver" @mouseleave="mouseLeave">
        <img src="@/assets/image/robot.png" alt />
        <span v-if="tips" class="tips-text">运维机器人</span>
      </div>
      <div @click="to" class="home">
        <img src="../../assets/image/home.png" class="user-avatar" />
      </div> -->
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <!-- <img src="@/assets/image/avatar.svg" class="user-avatar" /> -->
          <svg t="1600311663297" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5329" width="35" height="40">
            <path d="M512 148.5c49.1 0 96.7 9.6 141.5 28.5 43.3 18.3 82.2 44.5 115.6 77.9 33.4 33.4 59.6 72.3 77.9 115.6 18.9 44.8 28.5 92.4 28.5 141.5s-9.6 96.7-28.5 141.5c-18.3 43.3-44.5 82.2-77.9 115.6-33.4 33.4-72.3 59.6-115.6 77.9-44.8 18.9-92.4 28.5-141.5 28.5s-96.7-9.6-141.5-28.5c-43.3-18.3-82.2-44.5-115.6-77.9-33.4-33.4-59.6-72.3-77.9-115.6-18.9-44.8-28.5-92.4-28.5-141.5s9.6-96.7 28.5-141.5c18.3-43.3 44.5-82.2 77.9-115.6s72.3-59.6 115.6-77.9c44.8-18.9 92.4-28.5 141.5-28.5m0-50C283.6 98.5 98.5 283.6 98.5 512S283.6 925.5 512 925.5 925.5 740.4 925.5 512 740.4 98.5 512 98.5z" p-id="5330" fill="#606266"></path>
            <path d="M512 401m-106 0a106 106 0 1 0 212 0 106 106 0 1 0-212 0Z" p-id="5331" fill="#606266"></path>
            <path d="M514.5 540.8c-105.1 0-195 68.8-231.6 166.2 28.2 27.9 60.4 50.1 95.9 65.9 40.2 18 83 27.1 127.1 27.1s86.9-9.1 127.1-27.1c38.9-17.4 73.8-42.3 103.8-74.1 1.4-1.5 2.7-3 4.1-4.4-39.2-90.6-125.8-153.6-226.4-153.6z" p-id="5332" fill="#606266"></path>
          </svg>
          <span class="names">{{name}}</span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>首页</el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="add">
            <span style="display: block;">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span style="display: block;">退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <drawer ref="call" :drawer-dialog="mode"></drawer>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { resetPassword } from "@/api/user";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import transmit from "../../utils/transmit";
import drawer from "@/components/dialog/dialog.vue";
import md5 from "js-md5";
// import { sessionStorageSet, sessionStorageGet } from "@/utils/storage";
import { ssoLogout } from '@/api/user1'
import { removeToken } from "@/utils/auth";
// import axios from "axios";
export default {
  components: {
    Breadcrumb,
    Hamburger,
    drawer,
  },
  data() {
    return {
      tips: false,
      collapse: false,
      name: JSON.parse(sessionStorage.getItem('UserInfo')).name,
      mode: {
        type: "dialog",
        form: [
          // {
          //   type: "input",
          //   label: "旧密码:",
          //   placeholder: "请输入旧密码",
          //   model: "oldPassword",
          //   state: ["detail", "new", "modify"],
          //   rules: [
          //     {
          //       required: true,
          //       message: "请输入旧密码",
          //       trigger: "blur",
          //     },
          //   ],
          // },
          {
            type: "input",
            label: "新密码:",
            placeholder: "请输入新密码",
            model: "newPassword",
            state: ["detail", "new", "modify"],
            rules: [
              {
                required: true,
                message: "请输入新密码",
                trigger: "blur",
              },
            ],
          },
        ],
        event: [
          {
            bind: "increase",
            method: (row) => {
              this.increase(row);
            },
          },
          {
            bind: "modify",
            method: (row) => {
              console.log("aaaaaaaaa");
              this.bindModify(row);
            },
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "id"]),
  },
  methods: {
    mouseOver() {
      this.tips = true;
    },
    mouseLeave() {
      this.tips = false;
    },
    robotClick() {
      window.location.href = process.env.VUE_APP_TOBOT_API;
    },
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
      this.collapse = !this.sidebar.opened;
      transmit.$emit("collapse", this.collapse);
    },
    async logout() {
      // await this.$store.dispatch("user/logout");
      this.$confirm("是否退出登录?", "提示", {
        type: "warning",
      })
        .then(async () => {
          this.logoutFn();
        })
        .catch(() => {
          return false;
        });
    },
    async increase(row) {
      let id = JSON.parse(sessionStorage.getItem("UserInfo")).id
      const data = {
        id: id,
        newPassword: md5(row.data.newPassword),
        // oldPassword: md5(row.data.oldPassword),
        realPassword: row.data.newPassword,
        // realOldPassword: row.data.oldPassword,
      };
      try {
        let res = await resetPassword(data);
        if (res.data.code === 1) {
          this.$message.success(res.data.msg);
          this.logoutFn();
          this.$refs.call.closeDialog();
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    async logoutFn() {
      // let url = process.env.VUE_APP_SSOLOGOUT_API;
      // sessionStorageSet("id", undefined);
      // sessionStorageSet("name", undefined);
      // removeToken();
      // // window.open(url, "_self");
      // this.$router.push('/login')
      const that = this
      try {
        let res = await ssoLogout()
        if (res.data.code === 1) {
          sessionStorage.removeItem("UserInfo")
          localStorage.removeItem('token')
          removeToken()
          window.location.reload()
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        that.$message.error(even.msg)
      }
    },
    add() {
      this.$refs.call.popup("修改密码", "increase", "");
    },
    to() {
      window.location.href = process.env.VUE_APP_INTEGRATION;
    },
  },
};
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    display: flex;
    align-items: center;
    height: 100%;
    line-height: 50px;
    .robot-icon {
      margin-right: 20px;
      color: #60a0fb;
      cursor: pointer;
      display: flex;
      align-items: center;
      position: relative;
      img {
        width: 32px;
      }
      .tips-text {
        position: absolute;
        left: -86px;
        top: -8px;
        z-index: 10;
      }
    }

    &:focus {
      outline: none;
    }

    .home {
      padding-right: 20px;
      cursor: pointer;
      height: 38px;
      img {
        width: 32px;
        height: 32px;
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        // margin-top: 5px;
        position: relative;
        cursor: pointer;
        img {
          width: 32px;
          height: 32px;
        }
        .names {
          line-height: 40px;
          // color: #7eb2fb;
          color: #606266;
          margin: 0 5px;
          font-size: 16px;
        }

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -15px;
          top: 14px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
