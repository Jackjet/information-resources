<template>
  <div class="navMenu">
    <template v-for="navMenu in navMenus">
        <!-- 最后一级菜单 -->
      <el-menu-item
      class="navLabelBox"
      v-if="!navMenu.childs&&navMenu.id"
      :key="navMenu.id"
      :index="navMenu.name"
       @click.native="goto(navMenu.value, navMenu.id, $event)">
<!--        <i :class="navMenu.icon"></i>-->
        <el-image :src="navMenu.icon" style="width: 25px;height: 25px" v-if="navMenu.icon"></el-image>
        <span class="navLabel" :title="navMenu.alias" slot="title">{{navMenu.alias}}</span>
      </el-menu-item>

      <!-- 此菜单下还有子菜单 -->
      <el-submenu
      v-if="navMenu.childs&&navMenu.id"
      class="navLabelBox"
      :key="navMenu.id"
      :index="navMenu.name"
      @click.native="goto(navMenu.value, navMenu.id, $event)">
        <template slot="title">
<!--          <i :class="navMenu.icon"></i>-->
          <el-image :src="navMenu.icon" style="width: 25px;height: 25px" v-if="navMenu.icon"></el-image>
          <span class="navLabel" :title="navMenu.alias"> {{navMenu.alias}}</span>
        </template>
        <!-- 递归 -->
        <NavMenu :navMenus="navMenu.childs"></NavMenu>
      </el-submenu>
    </template>
  </div>
</template>

<script>
export default {
  name: 'NavMenu',
  props: ['navMenus'],
  data () {
    return {
      navClick: false
    }
  },
  created () {
    if (Number(this.common.session('type')) === 3) {
      this.navClick = true
    } else {
      this.navClick = false
    }
  },
  methods: {
    goto (path, id, event) {
      if (event.target.parentNode.type === 'button') {
        return false
      }
      if (path) {
        if (this.navClick) {
          this.$router.push({
            path: path,
            query: {
              id: id
            }
          })
        } else {
          this.$router.push(path)
        }
      }
      event.stopPropagation()
    }
  }
}
</script>
