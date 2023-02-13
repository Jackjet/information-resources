<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <!-- <span style="float: left;color: #808080">当前位置：</span> -->
    <transition-group name="breadcrumb" style="margin-left:10px;">
      <el-breadcrumb-item v-for="(item, index) in levelList" :key="item.path">
        <span v-if="item.redirect === 'noredirect' || index == levelList.length - 1 || item.meta.noLink === true" class="no-redirect">{{ item.meta.title }}</span>
        <a v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import pathToRegexp from 'path-to-regexp'

export default {
  data() {
    return {
      levelList: null
    }
  },
  watch: {
    $route(route) {
      // if you go to the redirect page, do not update the breadcrumbs
      if (route.path.startsWith('/redirect/')) {
        return
      }
      this.getBreadcrumb()
    }
  },
  created() {
    this.getBreadcrumb()
  },
  methods: {
    getBreadcrumb() {
      // only show routes with meta.title
      let matched = this.$route.matched.filter(
        item => item.meta && item.meta.title
      )
      const first = matched[0]
      if (!this.isDashboard(first)) {
        matched = [{ path: '/dashboard', meta: { title: '首页', breadHide: true }}].concat(matched)
      }
      this.levelList = matched
      var infoCatalogIndex = {
        path: '/Cataloging/infoCatalogList',
        component: () => import('@/views/Cataloging/infoCatalog/list'),
        name: 'infoCatalogList',
        meta: {
          title: '信息资源目录',
          noCache: true
        }
      }
      var requirementCatalogIndex = {
        path: '/Cataloging/requirementCatalog',
        component: () => import('@/views/Cataloging/requirementCatalog/list'),
        name: 'requirementCatalogList',
        meta: {
          title: '信息需求目录',
          noCache: true
        }
      }
      var OSManagementIndex = {
        path: '/OSManagement/index',
        component: () => import('@/views/OSManagement/index'),
        name: 'OSManagement',
        meta: {
          perms: ['GET /admin/archAppSys/list'],
          title: '应用系统管理',
          icon: 'yingyong',
          noCache: true
        }
      }
      this.levelList = this.levelList.filter((item) => {
        return item.meta.breadHide != true
      })
      this.levelList.forEach((item, index) => {
        if (item.meta.title == '信息资源目录详情' || item.meta.title == '版本管理' || item.meta.title == '信息资源目录维护' || item.meta.title == '版本比较') {
          this.levelList.splice(index, 0, infoCatalogIndex)
        }
        if (item.meta.title == '信息需求目录详情' || item.meta.title == '信息需求目录维护' || item.meta.title == '关联权责清单') {
          this.levelList.splice(index, 0, requirementCatalogIndex)
        }
        if (item.meta.title == '应用系统支撑权责清单与信息资源关系图') {
          this.levelList.splice(index, 0, OSManagementIndex)
        }
      })
      console.log(this.levelList)
      this.$store.dispatch('setBreadCrumb', this.levelList)
      /* this.levelList = matched.filter(
        item => item.meta && item.meta.title && item.meta.breadcrumb !== false
      )*/
    },
    isDashboard(route) {
      const name = route && route.name
      if (!name) {
        return false
      }
      return (
        name.trim().toLocaleLowerCase() === 'Dashboard'.toLocaleLowerCase())
    },
    pathCompile(path) {
      // To solve this problem https://github.com/PanJiaChen/vue-element-admin/issues/561
      const { params } = this.$route
      var toPath = pathToRegexp.compile(path)
      return toPath(params)
    },
    handleLink(item) {
      const { redirect, path } = item
      if (redirect) {
        this.$router.push(redirect)
        return
      }
      this.$router.push(this.pathCompile(path))
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  font-size: 16px;
  line-height: 62px;
  a{
    color: #5a5a5a;
  }
  .no-redirect {
    color: #5a5a5a;
    cursor: text;
  }
}
</style>
