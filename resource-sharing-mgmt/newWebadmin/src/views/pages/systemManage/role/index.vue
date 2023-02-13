<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="角色权限名称">
          <el-input clearable size="medium" placeholder="请输入角色权限名称" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <!--         <el-form-item  label="角色权限名称">
          <autoInput
            ref="quanxianName"
            the-id="quanxianName"
            :the-responsepath="['data', 'content']"
            :on-focus="false"
            size="mini"
            the-key="name"
            the-address="/webadmin/system/role/findAll"
            the-placeholder="请输入角色权限名称">
          </autoInput>
        </el-form-item> -->
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" v-if="permissions.add" plain size='mini' @click="handleAdd" icon="el-icon-plus">新增</el-button>
      </div>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { roleFindAll, roleDelete } from "@/api/role.js"
import autoInput from "@/components/input/autoCompleteInput"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { autoInput, TableList, Pagination },
  data() {
    return {
      permissions: {
        add: false,
        edit: false,
        delete: false,
        userList: false,
        permissionAssignment: false
      },
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      tableData: [],
      tableHeader: [
        { id: false, type: '', label: '角色权限名称', list: 'name' },
        { id: false, type: '', label: '备注', list: 'remark' }
      ],
      tableOpction: {
        label: '操作',
        width: '600px',
        value: 0,
        options: [{
          label: '编辑',
          type: 'text',
          icon: 'el-icon-edit-outline',
          show: (row) => {
            return this.permissions.edit
          },
          State: false,
          method: (row) => {
            this.handleEdit(row)
          }
        },
        {
          label: '用户列表',
          type: 'text',
          icon: 'el-icon-user',
          show: (row) => {
            return this.permissions.userList
          },
          State: false,
          method: (row) => {
            this.handleUser(row)
          }
        },
        {
          label: '权限分配',
          key: 0,
          type: 'text',
          icon: 'el-icon-guide',
          show: (row) => {
            if (row.id !== 'admin' && this.permissions.permissionAssignment) {
              return true
            } else {
              return false
            }
          },
          State: false,
          method: (row) => {
            this.handleAssign(row)
          }
        },
        {
          label: '代办分配',
          key: 0,
          type: 'text',
          icon: 'el-icon-guide',
          State: false,
          method: (row) => {
            this.agencyDistribution(row)
          }
        },
        {
          label: '服务指引分配',
          key: 0,
          type: 'text',
          icon: 'el-icon-guide',
          State: false,
          method: (row) => {
            this.serveDistribution(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          show: (row) => {
            if ((row.id !== 'admin' && row.id !== 'default') && this.permissions.delete) {
              return true
            } else {
              return false
            }
          },
          State: false,
          method: (row) => {
            this.handleDelete(row)
          }
        }]
      },
      SearchItem: {
        name: '',
      },
      lastItem: {
        name: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
    }
  },
  created() {
    this.$store.dispatch('setUserButtons').then(res => {
      let permissionsArr = JSON.parse(res);
      permissionsArr.forEach(item => {
        let itemArr = item.split('_')
        if (('/' + itemArr[0]) === this.$route.path) {
          this.permissions[itemArr[1]] = true;
        }
      })
    })
    this.fetchData()
  },
  methods: {
    agencyDistribution(data) {
      this.$router.push({
        path: '/agencyDistributionList',
        query: {
          id: data.id
        }
      })
    },
    serveDistribution(data) {
      this.$router.push({
        path: '/serviceGuidelinesDistribution',
        query: {
          id: data.id
        }
      })
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.name = '',
        Object.entries(that.SearchItem).map((item, index) => {
          that.lastItem[item[0]] = that.SearchItem[item[0]]
        })
    },
    // 获取列表
    async fetchData() {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        const res = await roleFindAll(data)
        if (res.data.code === 1) {
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(v.id)
      })
    },
    // 搜索
    SearchNoteList() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData()
    },
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/roleEdit',
        query: {
          id: data.id,
          name: data.name,
          remark: data.remark,
          type: '编辑'
        }
      })
    },
    // 用户列表
    handleUser(data) {
      const that = this
      that.$router.push({
        path: '/roleUser',
        query: {
          id: data.id
        }
      })
    },
    // 添加
    handleAdd(data) {
      const that = this
      // 自动补全组件功能测试
      // 将输出框内容存入本地
      // that.$refs.quanxianName.setValue()
      // 获取自动补全输出框返回值
      // let result = that.$refs.quanxianName.returnValue()
      // 清空输入框内容
      // that.$refs.quanxianName.clearValue()
      // 
      // 面包屑传值功能测试
      // 添加页面业务参数
      // let aaa = JSON.parse(sessionStorage.getItem("theBreadcrumb2"))
      // aaa[aaa.length-1].data = {name: '测试'}
      // sessionStorage.setItem("theBreadcrumb2", JSON.stringify(aaa))
      that.$router.push({
        path: '/roleAdd',
        query: {
          type: '新增'
        }
      })
    },
    // 权限分配
    handleAssign(data) {
      const that = this
      that.$router.push({
        path: '/rolePower',
        query: {
          id: data.id,
          name: data.name
        }
      })
    },
    // 删除当前数据 重载列表
    async handleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await roleDelete(data.id)
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    }
  }
}
</script>
