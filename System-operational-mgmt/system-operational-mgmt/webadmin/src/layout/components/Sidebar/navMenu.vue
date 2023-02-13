<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" active-text-color="#fff" unique-opened router>
      <template v-for="item in items">
        <template v-if="item.subs">
          <el-submenu :index="item.index" :key="item.id">
            <template slot="title">
              <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
            </template>
            <template v-for="subItem in item.subs">
              <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.id">
                <template slot="title"><span slot="title">{{ subItem.title }}</span></template>
                <el-menu-item v-for="(threeItem, i) in subItem.subs" :key="i" :index="threeItem.index">
                  {{ threeItem.title }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item v-else :index="subItem.index" :key="subItem.id">
                <span slot="title"> {{ subItem.title }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
        <template v-else>
          <el-menu-item :index="item.index" :key="item.id">
            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import transmit from '../../../utils/transmit'
// import { mapGetters } from 'vuex'
export default {
  name: 'SideBar',
  props: {
    message: Boolean
  },
  // computed: {
  //    ...mapGetters([
  //     'sidebar'
  //   ]),
  //   isCollapse() {
  //     return !this.sidebar.opened
  //   }
  // },
  data() {
    return {
      collapse: false,
      items: []
    }
  },
  computed: {
    onRoutes() {
      return this.$route.path
    }
  },
  created() {
    transmit.$on('collapse', msg => {
      this.collapse = msg
    })
  },
  methods: {
    SetParameters(key) {
      const that = this
      if (key != null) {
        //  that.items.push(key)
        that.items = key
      }
    }
  }
}
</script>

<style scoped lang="scss">
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 50px;
  bottom: 0;
  overflow-y: hidden;
  z-index: 999;
  text-align: left;
  background: rgb(23, 40, 83);
  width: 100%;
  ul {
    height: 100%;
    background: rgb(23, 40, 83);
  }

  .Admin {
    font-size: 22px;
    margin-right: 10px;
  }
}
.el-submenu {
  color: rgb(191, 203, 217) !important;
  background: rgb(23, 40, 83);
  border-bottom: 1px solid #2a417a;
  .el-submenu__title {
    color: rgb(191, 203, 217) !important;
    background: rgb(23, 40, 83);
    border-bottom: 1px solid #2a417a;
  }
}
.is-opened {
  background: rgb(18, 32, 66) !important;
}
.el-submenu__title,
.el-submenu__title span {
  color: rgb(191, 203, 217) !important;
}
.sidebar-el-menu {
  background: rgb(23, 40, 83);
  vertical-align: middle;
}
.sidebar-el-menu span {
  padding-left: 15px;
}
.sidebar-el-menu .iconfont {
  font-size: 20px;
  font-weight: bold;
}
.el-menu-item:focus,
.el-submenu__title:hover {
  background: rgb(18, 32, 66) !important;
}
.el-menu-item {
  color: rgb(191, 203, 217);
  background: rgb(18, 32, 66) !important;
}
.el-scrollbar {
  height: calc(100% - 43px);
}

.el-scrollbar__wrap {
  overflow-x: hidden;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 230px;
}
</style>
