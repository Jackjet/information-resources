<template>
  <div class="navbar">
    <hamburger
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />
    <breadcrumb class="breadcrumb-container" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import transmit from "../../utils/transmit";
import drawer from "@/components/dialog/dialog.vue";
import md5 from "js-md5";
import { sessionStorageSet, sessionStorageGet } from "@/utils/storage";
import { removeToken } from "@/utils/auth";
export default {
  components: {
    Breadcrumb,
    Hamburger,
    drawer
  },
  data() {
    return {
      name: sessionStorageGet("name"),
      collapse: false,
      mode: {
        type: "dialog",
        form: [
          {
            type: "input",
            label: "旧密码:",
            placeholder: "请输入旧密码",
            model: "oldPassword",
            state: ["detail", "new", "modify"],
            rules: [
              {
                required: true,
                message: "请输入旧密码",
                trigger: "blur"
              }
            ]
          },
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
                trigger: "blur"
              }
            ]
          }
        ],
        event: [
          {
            bind: "increase",
            method: row => {
              this.increase(row);
            }
          },
          {
            bind: "modify",
            method: row => {
              this.bindModify(row);
            }
          }
        ]
      }
    };
  },
  computed: {
    ...mapGetters(["sidebar", "avatar", "id"])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
      this.collapse = !this.sidebar.opened;
      transmit.$emit("collapse", this.collapse);
    },
    //单点登录退出
    logout() {
      this.$confirm("是否退出登录?", "提示", {
        type: "warning"
      })
        .then(async () => {
          this.logoutFn();
        })
        .catch(() => {
          return false;
        });
    },
    async increase(row) {
      const data = {
        // md5修改密码
        id: sessionStorageGet("id"),
        newPassword: md5(row.data.newPassword),
        oldPassword: md5(row.data.oldPassword),
        realPassword: row.data.newPassword,
        realOldPassword: row.data.oldPassword
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
    logoutFn() {
      let url = process.env.VUE_APP_SSOOUT_API;
      sessionStorageSet("id", undefined);
      sessionStorageSet("name", undefined);
      removeToken();
      window.open(url, "_self");
    },
    add() {
      this.$refs.call.popup("修改密码", "increase", "");
    }
  }
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
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;
        .names {
          display: inline-block;
          line-height: 40px;
          float: left;
          color: #7eb2fb;
          margin: 0 5px;
          font-size: 16px;
        }
        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
          float: left;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
          float: left;
        }
      }
    }
  }
}
</style>
