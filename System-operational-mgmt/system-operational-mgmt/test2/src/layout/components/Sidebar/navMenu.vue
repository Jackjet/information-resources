<template>
  <el-scrollbar class="sidebar">
    <el-menu
      :default-active="onRoutes"
      :collapse="collapse"
      class="sidebar-el-menu"
      router
    >
      <template v-for="item in menuData">
        <el-menu-item :index="item.entity.index" :key="item.entity.id">
          <i :class="item.entity.icon" />
          <span slot="title">{{ item.entity.name }}</span>
        </el-menu-item>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import transmit from "../../../utils/transmit";
export default {
  name: "SideBar",
  props: {
    message: Boolean,
  },
  data() {
    return {
      collapse: false,
      menuData: [
        {
          entity: {
            id: "1",
            icon: "iconfont icon-wulianwang-",
            index: "/test",
            name: "菜单1",
          },
          childs: [],
        },
        {
          entity: {
            id: "2",
            icon: "iconfont icon-wulianwang-",
            index: "/test2",
            name: "菜单2",
          },
          childs: [],
        },
      ],
    };
  },
  computed: {
    onRoutes() {
      return this.$route.path;
    },
  },
  created() {
    transmit.$on("collapse", (msg) => {
      this.collapse = msg;
    });
  },
  methods: {
    SetParameters(key) {
      const that = this;
      if (key != null) {
        that.items = key;
      }
    },
  },
};
</script>

<style scoped lang="scss">
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 60px;
  bottom: 0;
  overflow-y: hidden;
  z-index: 999;
  text-align: left;
  background: #15253a;
  width: 100%;
  // padding: 5px;

  ul {
    height: 100%;
    background: #15253a;
  }

  .Admin {
    font-size: 22px;
    margin-right: 10px;
  }
}
.el-submenu {
  color: rgb(191, 203, 217) !important;
  background: #15253a;
  .el-submenu__title {
    color: rgb(191, 203, 217) !important;
    background: #15253a;
  }
}
.is-opened {
  background: #001f28;
}
.el-submenu__title,
.el-submenu__title span {
  color: rgb(191, 203, 217) !important;
}
.sidebar-el-menu {
  background: #15253a;
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
.el-menu-item:hover {
  background: #001f28;
}
.el-menu-item {
  color: rgb(191, 203, 217);
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
