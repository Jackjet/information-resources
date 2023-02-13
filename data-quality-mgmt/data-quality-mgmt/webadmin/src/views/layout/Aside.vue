<!--
 * 侧边栏组件
-->
<template>
  <div class="left-nav">
    <div class="nav-logo">
      <img class="theLogo" src="../../assets/image/logo.png" width="40"/>
      <span class="nav-title" v-show="titleShow">{{ title }}</span>
      <el-button type="text" class="open-btn" @click="toggleCollapse">
        <i v-show="!isCollapse" class="el-icon-s-fold"></i>
        <i v-show="isCollapse" class="el-icon-s-unfold"></i>
      </el-button>
    </div>
    <el-scrollbar class="nav-scrollbar" style="box-shadow: 0 0 30px #e6e6e6;">
      <el-menu class="el-menu-vertical-demo" :default-active="onRoutes" :unique-opened="true" :collapse="isCollapse">
        <template v-for="(item, index) in items">
          <template v-if="item.children && item.children.length > 0">
            <el-submenu :index="index" :key="index">
              <template slot="title">
                <b class="chooseLine"></b>
                <i class="icon" v-if="item.icon" v-html="theIcon[item.icon] ? theIcon[item.icon].icon: ''"></i>
                <span style="padding-left: 10px;" slot="title">{{ item.name }}</span>
              </template>
              <template v-for="(subItem, ele) in item.children">
                <el-submenu v-if="subItem.children && subItem.children.length > 0" :index="index + '-' + ele" :key="index + '-' + ele">
                  <template slot="title">
                    <b class="chooseLine"></b>
                    <i class="icon" v-if="subItem.icon" v-html="theIcon[subItem.icon] ? theIcon[subItem.icon].icon: ''"></i>
                    <span style="font-size: 13px; padding-left: 10px;" slot="title">{{ subItem.name }}</span>
                  </template>
                  <el-menu-item v-for="(threeItem, i) in subItem.children" :key="index + '-' + ele + '-' + i" :index="threeItem.path" @click="clickMenu(threeItem)">
                    <b class="chooseLine"></b>
                    <i class="icon" v-if="threeItem.icon" v-html="theIcon[threeItem.icon] ? theIcon[threeItem.icon].icon: ''"></i>
                    <span style="font-size: 12px; padding-left: 10px;" slot="title">{{ threeItem.name }}</span>
                  </el-menu-item>
                </el-submenu>
                <el-menu-item v-else :index="subItem.path" :key="index + '-' + ele" @click="clickMenu(subItem)">
                  <b class="chooseLine"></b>
                  <i class="icon" v-if="subItem.icon" v-html="theIcon[subItem.icon] ? theIcon[subItem.icon].icon: ''"></i>
                  <span style="font-size: 13px; padding-left: 10px;" slot="title">{{ subItem.name }}</span>
                </el-menu-item>
              </template>
            </el-submenu>
          </template>
          <template v-else>
            <el-menu-item :index="item.path" :key="index" @click="clickMenu(item)">
              <b class="chooseLine"></b>
              <i class="icon" v-if="item.icon" v-html="theIcon[item.icon].icon"></i>
              <span style="padding-left: 10px;" slot="title">{{ item.name }}</span>
            </el-menu-item>
          </template>
        </template>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import bus from '@/utils/bus'
import {icons} from '@/icons/iconList'
import {findCurrentUserButton, findCurrentUserSidebar} from "@/api/role"

export default {
  name: 'MyAside',
  data() {
    return {
      title: '数据质量分析系统',
      items: [],
      theIcon: icons,
      titleShow: true,
      isCollapse: false
    };
  },
  computed: {
    onRoutes() {
      return this.$route.path
    }
  },
  created() {
    this.SetParameters()
    // this.initRole(JSON.parse(sessionStorage.getItem("UserInfo")).id)
    // bus.$on('permissionData', () => {
    //   this.initRole(JSON.parse(sessionStorage.getItem("UserInfo")).id)
    // })
  },
  methods: {
    async initRole(id) {
      let buttons = await findCurrentUserButton({userId: id});
      if (buttons.data.code === 1) {
        sessionStorage.setItem("UserButtons", JSON.stringify(buttons.data.data))
      }
      let menus = await findCurrentUserSidebar({userId: id})
      if (menus.data.code === 1) {
        let tree = []
        menus.data.data[0].menuTree.forEach((v, i) => {
          if (v.name != '消息管理' && v.name != '系统设置') {
            tree.push(v)
          }
        })
        menus.data.data[0].menuTree = tree.slice(0)
        // sessionStorage.setItem("UserMenus", JSON.stringify(menus.data.data[0].menuTree))
        this.SetParameters(menus.data.data[0].menuTree)
      }
    },
    // 设置系统名称延时，使切换动画更加平滑
    toggleCollapse() {
      this.isCollapse = !this.isCollapse
      if (!this.isCollapse) {
        setTimeout(() => {
          this.titleShow = !this.isCollapse
        }, 500)
      } else {
        this.titleShow = !this.isCollapse
      }
    },
    SetParameters() {
      let key = JSON.parse(sessionStorage.getItem("UserMenus"))
      const that = this
      if (key != null) {
        that.items = [...key]
      }
    },
    // 点击侧边栏动作，获取路由存入面包屑
    clickMenu(info) {
      const that = this
      if (info.type && Number(info.type) === 1) {
        window.open(info.path, '_blank')
        return false
      } else {
        let breadcrumb = []
        that.items.forEach(item => {
          if (item.path && item.path === info.path) {
            breadcrumb.push({'title': item.name, 'path': item.path})
          } else {
            if (item.children) {
              item.children.forEach(item2 => {
                if (item2.path && item2.path === info.path) {
                  breadcrumb.push({'title': item.name, 'path': item2.path})
                  breadcrumb.push({'title': item2.name, 'path': item2.path})
                } else {
                  if (item2.children) {
                    item2.children.forEach((item3) => {
                      if (item3.path && item3.path === info.path) {
                        breadcrumb.push({'title': item.name, 'path': item3.path})
                        breadcrumb.push({'title': item2.name, 'path': item3.path})
                        breadcrumb.push({'title': item3.name, 'path': item3.path})
                      }
                    })
                  }
                }
              })
            }
          }
        })
        bus.$emit('menuData', breadcrumb)
        that.$router.push(info.path)
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.icon {
  fill: currentColor;
  overflow: hidden;

  /deep/ svg {
    margin-top: -5px;
    width: 20px;
    height: 20px;
  }
}

.left-nav {
  width: auto;
  height: 100%;

  .nav-logo {
    background: $color-MenuLogoBg;
    z-index: 99;
    border-bottom: 1px solid #ffffff30;
    box-sizing: border-box;
    position: relative;
    height: 45px;
    line-height: 45px;
    padding-left: 45px;
    padding-right: 10px;
    border-bottom: 1px solid $color-HeaderBorder;
    box-sizing: border-box;

    .theLogo {
      position: absolute;
      left: 10px;
      width: 30px;
      height: 30px;
      margin-top: 7px;
    }

    .nav-title {
      color: $color-MenuLogoText;
      font-weight: 400;
      font-size: 18px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
    }

    .open-btn {
      position: absolute;
      left: calc(100% + 15px);
      color: #000;
      font-size: 20px;
      z-index: 999;
    }
  }

  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 210px;
  }

  .nav-scrollbar {
    height: calc(100% - 45px);
    background-color: $color-MenuBg;
  }

  /deep/ .el-scrollbar__thumb {
    display: none;
  }

  .el-menu-vertical-demo {
    border: none;
    background-color: #ffffff00;

    /deep/ .el-submenu__title {
      &:hover {
        background-color: $color-MenuHoverBg;
      }

      &:focus {
        background-color: $color-MenuHoverBg;
      }
    }

    .el-menu-item {
      span {
        color: $color-MenuText;
        font-size: 14px;
      }

      &:hover {
        background-color: $color-MenuHoverBg;
      }

      &:focus {
        background-color: #ffffff00;
      }
    }

    .el-submenu {
      /deep/ .el-menu--inline {
        background-color: $color-MenuSubBg;
      }

      span {
        color: $color-MenuText;
        font-size: 14px;
      }

      .el-menu-item {
        span {
          color: $color-MenuText;
          font-size: 14px;
        }
        .chooseLine {
          position: absolute;
          left: 35px;
          top: 18px;
          height: 6px;
          width: 6px;
          // background-color: $color-MenuHoverText;
          background-color: #fff;
          display: none;
          border-radius: 100%;
        }
      }
    }

    .el-menu-item.is-active {
      color: $color-MenuHoverText;
      background:$color-MenuHoverBg;
      span {
        color: $color-MenuHoverText;
      }
    }
  }
}
</style>
<style lang="scss">
.el-submenu .el-menu-item{
  height: 40px !important;
  line-height: 40px !important;
  border-bottom:none;
}
.el-menu, .el-menu--horizontal>.el-menu-item:not(.is-disabled):focus, .el-menu--horizontal>.el-menu-item:not(.is-disabled):hover, .el-menu--horizontal>.el-submenu .el-submenu__title:hover{
  background-color: #172953;
}
.el-menu--vertical .el-menu--popup .el-menu-item{
  color: #BFCBD9;
  border-bottom:none;
}
.el-menu-item:focus, .el-menu-item:hover{
  background-color: #0D1C3E;
  color:#fff!important;
}
.el-submenu.is-active .el-submenu__title{
  border-bottom:none;
}
.left-nav .el-menu-vertical-demo .el-menu-item.is-active, .left-nav .el-menu-vertical-demo .el-menu-item.is-active span{
  color:#fff!important;
}
.el-container .el-submenu.is-opened>.el-submenu__title{
  border-bottom:none;
}
.left-nav .el-menu-vertical-demo .el-submenu .el-menu--inline{
  background-color:$color-MenuHoverBg!important;
}
.left-nav .el-menu-vertical-demo .el-menu-item:focus{

}
</style>