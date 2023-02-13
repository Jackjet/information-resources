<template>
  <el-scrollbar class='sidebar'>
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" unique-opened>
      <NavMenu :navMenus="menuData"></NavMenu>
    </el-menu>
  </el-scrollbar>
</template>

<script>
  import NavMenu from './lastMenu'
  import transmit from './../common/transmit'

  export default {
    components: {NavMenu},
    name: 'sidebar',
    props: {
      message: Boolean
    },

    data () {
      return {
        collapse: false,
        allData: {
          adminData: [{
            // 一级
            id: '1',
            name: 'systemManage',
            // icon: 'el-icon-s-tools',
            icon: require('@/assets/image/icons/配置 (1).png'),
            alias: '配置服务',
            // 二级
            childs: [
              {
                id: '1-1',
                name: 'sysService',
                icon: require('@/assets/image/icons/order_unread.png'),
                alias: '服务上传',
                value: '/index/uploadService'
              },
              {
                id: '1-2',
                name: 'otherService',
                // icon: 'el-icon-link',
                icon: require('@/assets/image/icons/配置 (1).png'),
                alias: '服务配置',
                value: '/index/otherService'
              }
            ]
          },
            {
              // 一级
              id: '2',
              name: 'lessee',
              // icon: 'el-icon-user-solid',
              icon: require('@/assets/image/icons/用户.png'),
              alias: '租户管理',
              // 二级
              childs: [
                {
                  id: '2-1',
                  name: 'lesseeManage',
                  // icon: 'el-icon-user',
                  icon: require('@/assets/image/icons/用户.png'),
                  alias: '租户列表',
                  value: '/index/lesseeManage'
                }
              ]
            }],
          lesseeData: [{
            // 一级
            id: '1',
            name: 'integrated',
            // icon: 'el-icon-s-grid',
            icon: require('@/assets/image/icons/用户.png'),
            alias: '用户管理',
            // 二级
            childs: [
              {
                id: '1-1',
                name: 'integratedProject',
                // icon: 'el-icon-tickets',
                icon: require('@/assets/image/icons/用户.png'),
                alias: '用户列表',
                value: '/index/integratedProject'
              }
            ]
          }],
          itemAdminData: [
            {
              id: '1',
              name: 'runNode',
              // icon: 'el-icon-s-grid',
              icon: require('@/assets/image/icons/节点.png'),
              alias: '节点管理',
              value: '/index/runNode'
            },
            {
              id: '2',
              name: 'services',
              // icon: 'el-icon-s-grid',
              icon: require('@/assets/image/icons/系统.png'),
              alias: '服务管理',
              value: '/index/services'
            },
            {
              id: '3',
              name: 'sysUser',
              // icon: 'el-icon-tickets',
              icon: require('@/assets/image/icons/用户.png'),
              alias: '身份管理',
              value: '/index/sysUser'
            }, {
              id: '4',
              name: 'serviceConfig',
              // icon: 'el-icon-tickets',
              icon: require('@/assets/image/icons/自定义.png'),
              alias: '服务适配',
              value: '/index/sysServiceInstallPack'
            }, {
              id: '5',
              name: 'componentManage',
              // icon: 'el-icon-tickets',
              icon: require('@/assets/image/icons/节点 (1).png'),
              alias: '组件适配',
              value: '/index/componentManage'
            }, {
              // 一级
              id: '6',
              name: 'integrated',
              // icon: 'el-icon-s-grid',
              icon: require('@/assets/image/icons/用户.png'),
              alias: '任务管理',
              // 二级
              childs: [
                {
                  id: '6-1',
                  name: 'resources',
                  // icon: 'el-icon-tickets',
                  alias: '资源管理',
                  childs: [
                    {
                      id: '6-1-1',
                      name: 'dataResource',
                      // icon: 'el-icon-tickets',
                      alias: '数据源',
                      value: '/index/dataResource'
                    },
                    {
                      id: '6-1-2',
                      name: 'container',
                      // icon: 'el-icon-tickets',
                      alias: '容器管理',
                      value: '/index/container'
                    }
                  ]
                },
                {
                  id: '6-2',
                  name: 'task',
                  // icon: 'el-icon-tickets',
                  alias: '任务管理',
                  childs: [
                    {
                      id: '6-2-1',
                      name: 'task',
                      // icon: 'el-icon-tickets',
                      alias: '任务列表',
                      value: '/index/task'
                    },
                    {
                      id: '6-2-2',
                      name: 'analysis',
                      // icon: 'el-icon-tickets',
                      alias: '任务分析',
                      value: '/index/analysis'
                    }
                  ]
                }
              ]
            }, {
              id: '7',
              name: 'backups',
              // icon: 'el-icon-tickets',
              icon: require('@/assets/image/icons/info.png'),
              alias: '备份管理',
              value: '/index/backups'
            }, {
              id: '8',
              name: 'operationLog',
              // icon: 'el-icon-tickets',
              icon: require('@/assets/image/icons/节点 (1).png'),
              alias: '系统日志',
              value: '/index/operationLog'
            }
          ]
        },
        menuData: []
      }
    },

    computed: {
      onRoutes () {
        return this.$route.name
      }
    },

    created () {
      transmit.$on('collapse', msg => {
        this.collapse = msg
      })
      // this.allData.itemAdminData = this.common.session('nodeTree')
      this.menuData.splice(0)
      switch (Number(this.common.session('type'))) {
        case 1:
          this.menuData = this.allData.adminData
          break
        case 2:
          this.menuData = this.allData.lesseeData
          break
        case 3:
          this.menuData = this.allData.itemAdminData
          break
        default:
          console.log(1)
          break
      }
    },
    methods: {
      installNav () {
      }
    }
  }
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

    ul {
      height: 100%;
    }

    .Admin {
      font-size: 22px;
      margin-right: 10px;
    }
  }

  .el-scrollbar {
    height: calc(100% - 43px);
  }

  .el-scrollbar__wrap {
    overflow-x: auto;
  }

  .sidebar::-webkit-scrollbar {
    width: 0;
  }

  .sidebar-el-menu:not(.el-menu--collapse) {
    width: 230px;
    overflow-x: auto;
  }
</style>
