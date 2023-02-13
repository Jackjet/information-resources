<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item label="姓名">
          <el-input clearable size="medium" placeholder="请输入姓名" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item label="账号">
          <el-input clearable size="medium" placeholder="请输入账号" v-model="SearchItem.account">
          </el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input clearable size="medium" placeholder="请输入手机号" v-model="SearchItem.phone">
          </el-input>
        </el-form-item>
        <el-form-item label="组织机构">
          <!-- <el-input clearable
            size="medium"
            placeholder="请输入组织机构"
            v-model="SearchItem.organizationName">
          </el-input> -->
          <el-cascader size="medium" placeholder="请选择组织机构" :props="props" collapse-tags clearable v-model="SearchItem.organizationName" :options="organizationOptions" />
        </el-form-item>
        <el-form-item label="性别">
          <el-select clearable v-model="SearchItem.sex" placeholder="请选择性别">
            <el-option label="女" value="0"></el-option>
            <el-option label="男" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
          <el-button size='medium' @click="synchronization" icon="el-icon-refresh">同步用户</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- <el-col :span="24" style="margin-top: 10px;">
      <el-button type="primary" v-if="permissions.add" plain size='mini' @click="handleAdd" icon="el-icon-plus">新增</el-button>
      <el-button type="primary" v-if="permissions.templateDownload"  plain size='mini' @click="outModel" icon="el-icon-bottom">模板下载</el-button>
      <el-upload
        style="display: inline-block; margin: 0 10px;"
        ref="upload"
        :headers='headers'
        :action="action"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :on-success="handleSuccess"
        :before-remove="beforeRemove"
        :on-error="hadleError"  
        limit="1"
        :on-exceed="handleExceed"
        :file-list="fileList">
         <el-button v-if="permissions.userImport"  type="primary" plain size='mini' @click="add" icon="el-icon-bottom-left">用户导入</el-button>
      </el-upload>
      <el-button type="primary" plain size='mini' v-if="permissions.userExport"  @click="outUser" icon="el-icon-top-right">用户导出</el-button>
      <el-button type="primary" plain size='mini' v-if="permissions.batchDelete" @click="handleDelete" icon="el-icon-delete">批量删除</el-button>
      <el-button type="primary" plain size='mini' v-if="permissions.batchRetPassword" @click="handleResetPasswd" icon="el-icon-setting">批量重置密码</el-button>
      <el-button type="primary" plain size='mini' v-if="permissions.batchEnable" @click="handleEnable(true)" icon="el-icon-circle-check">批量启用</el-button>
      <el-button type="primary" plain size='mini'  v-if="permissions.batchBan" @click="handleEnable(false)" icon="el-icon-remove-outline">批量禁用</el-button>
    </el-col> -->
    <el-col :span='24'>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import {
  webAdminUserFindall,
  webAdminUserDeleteAll,
  webAdminUserUpdatePasswordReset,
  webAdminUserUpdateAllEnable,
  webAdminUserUpdateEnable,
  webAdminUserSync
} from "@/api/userMen.js";
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
import { outExcel } from '@/utils/export'
import { resetPassword, ssoLogout } from '@/api/user'
import { organizationFindAll } from "@/api/organization.js";

export default {
  components: { TableList, Pagination },
  data() {
    return {
      organizationOptions: [],
      props: { checkStrictly: true },
      permissions: {
        add: false,
        edit: false,
        enable: false,
        detail: false,
        templateDownload: false,
        batchRetPassword: false,
        batchDelete: false,
        userExport: false,
        userImport: false,
        batchEnable: false,
        batchBan: false
      },
      action: process.env.VUE_APP_BASE_API + '/webadmin/system/webAdminUser/import',
      headers: {
        Authorization: 'token ' + JSON.parse(sessionStorage.getItem("UserInfo")).token,
      },
      fileList: [],
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      enbleList: [],
      tableData: [],
      tableHeader: [
        { label: '姓名', list: 'name' },
        { label: '账号', list: 'account' },
        { label: '手机号', list: 'phone' },
        { label: '性别', list: 'sex' },
        { label: '组织机构', list: 'organizationName' },
        { label: '角色权限', list: 'roleName' },
        { label: '创建时间', list: 'createTime' },
      ],
      tableOpction: {
        label: '操作',
        width: '230px',
        value: 1,
        options: [{
          label: '编辑',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.edit
          },
          icon: 'el-icon-edit-outline',
          method: (row) => {
            this.handleEdit(row)
          }
        },]
        // {
        //   label: '详情',
        //   key: 0,
        //   type: 'text',
        //   show: (row) => {
        //     return this.permissions.detail
        //   },
        //   icon: 'el-icon-tickets',
        //   method: (row) => {
        //     this.handleDetail(row)
        //   }
        // }, {
        //   label: '启用',
        //   key: 0,
        //   style: '{"color": "#0f0"}',
        //   type: 'text',
        //   // 显示隐藏总控制，条件在方法里面写，返回true或者false
        //   show: (row) => {
        //     if (row.id !== 'admin' && row.enable === false && this.permissions.enable) {
        //       return true
        //     } else {
        //       return false
        //     }
        //   },
        //   icon: 'el-icon-circle-check',
        //   method: (row) => {
        //     this.enable(row)
        //   }
        // }, {
        //   label: '禁用',
        //   key: 0,
        //   show: (row) => {
        //     if (row.id !== 'admin' && row.enable === true && this.permissions.enable) {
        //       return true
        //     } else {
        //       return false
        //     }
        //   },
        //   style: '{"color": "#f00"}',
        //   type: 'text',
        //   icon: 'el-icon-circle-check',
        //   method: (row) => {
        //     this.enable(row)
        //   }
        // }]
      },
      SearchItem: {
        name: '',
        account: '',
        phone: '',
        organizationName: '',
        sex: ''
      },
      lastItem: {
        name: '',
        account: '',
        phone: '',
        organizationName: '',
        sex: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
    }
  },
  created() {
    let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
    permissionsArr.forEach(item => {
      let itemArr = item.split('_')
      if (('/' + itemArr[0]) === this.$route.path) {
        this.permissions[itemArr[1]] = true
      }
    })
    this.fetchData()
    this.findroles()
  },
  methods: {
    // 同步用户
    synchronization() {
      webAdminUserSync().then(res => {
        if (res.data.code === 1) {
          this.$message.success("同步用户成功！")
          this.fetchData()
        } else {
          this.$message.error(res.data.msg)
        }
      })
    },
    async findroles(data) {
      const that = this
      that.loading = true
      const response = await organizationFindAll()
      that.loading = false
      if (response.data.code === 1) {
        let arrData = []
        arrData.push(response.data.data)
        that.organizationOptions = this.getJsonTree(arrData, '')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    getJsonTree(data, parentId) {
      let itemArr = []
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId) {
          let newNode = {};
          newNode.value = node.name;
          newNode.label = node.name;
          if (node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode);
        }
      }
      return itemArr;
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.name = '',
        that.SearchItem.account = '',
        that.SearchItem.phone = '',
        that.SearchItem.organizationName = '',
        that.SearchItem.sex = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
    // 获取列表
    async fetchData() {
      const that = this
      let datas = ''
      datas = this.SearchItem,
        datas.page = this.currentPage,
        datas.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      if (Array.isArray(datas.organizationName) && datas.organizationName.length > 1) {
        datas.organizationName = datas.organizationName.pop()
      } else if (Array.isArray(datas.organizationName) && datas.organizationName.length === 1) {
        datas.organizationName = datas.organizationName[0]
      }
      try {
        that.isSubmitLoading = true
        const res = await webAdminUserFindall(datas)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            if (v.sex === 0) {
              v.sex = '女'
            } else if (v.sex === 1) {
              v.sex = '男'
            }
          })
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
      that.enbleList = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(v.id)
        that.enbleList.push({ id: v.id, enable: v.enable })
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
    // 详情
    handleDetail(data) {
      const that = this
      that.$router.push({
        path: '/userDetail',
        query: {
          id: data.id,
          type: '详情'
        }
      })
    },
    // 编辑
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/userEdit',
        query: {
          id: data.id,
          type: '编辑'
        }
      })
    },
    // 添加
    handleAdd(data) {
      const that = this
      that.$router.push({
        path: '/userAdd',
        query: {
          type: '新增'
        }
      })
    },
    // 删除当前数据 重载列表
    async handleDelete(data) {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择用户')
      }
      that.$confirm('删除会清空数据库中除日志相关的其他全部用户信息, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await webAdminUserDeleteAll(that.DeletelistiD + '')
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('批量删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 启用 重载列表
    async enable(row) {
      const that = this
      let tips = ''
      let data = {
        enable: !row.enable,
        id: row.id
      }
      if (row.enable) {
        tips = '禁用'
      } else {
        tips = '启用'
      }
      that.$confirm('请确认是否' + tips + '?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await webAdminUserUpdateEnable(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          if (row.enable) {
            that.$message.success('禁用成功')
            this.fetchData()
          } else {
            that.$message.success('启用成功')
            this.fetchData()
          }
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 重置密码 重载列表
    async handleResetPasswd() {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择用户')
      }
      that.$confirm('重置密码为Tjhbq2020, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await webAdminUserUpdatePasswordReset(that.DeletelistiD)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('批量重置密码成功')

          let userInfo = JSON.parse(sessionStorage.getItem("UserInfo"))
          //如果重置密码的用户包含当前用户
          if (that.DeletelistiD.includes(userInfo.id)) {
            let res = await ssoLogout()
            if (res.data.code === 1) {
              sessionStorage.removeItem("UserButtons")
              sessionStorage.removeItem("UserMenus")
              sessionStorage.removeItem("UserInfo")
              window.location.reload()
            }
          } else {
            this.fetchData()
          }
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 批量启用 重载列表
    async handleEnable(type) {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择用户')
      }
      let num = 0
      that.enbleList.map(function (v, k) {
        if (type === true && v.enable === false) {
          num++
        } else if (type === false && v.enable === true) {
          num++
        }
      })
      if (num === 0) {
        return type === true ? that.$message.warning('所选账号已经是启用状态') : that.$message.warning('所选账号已经是禁用状态')
      }
      let data = {
        enable: type,
        ids: that.DeletelistiD
      }
      let tips = type === true ? '此操作将批量启用账号' : '此操作将批量禁用账号'
      that.$confirm(tips + ', 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await webAdminUserUpdateAllEnable(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          if (type) {
            that.$message.success('批量启用账号成功')
          } else {
            that.$message.success('批量禁用账号成功')
          }
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 上传成功
    handleSuccess(response, file, fileList) {
      this.$refs.upload.clearFiles();
      if (fileList[0].response.code === 1) {
        this.fileList = []
        this.$message.success('导入成功')
        this.fetchData()
      } else {
        this.fileList = []
        this.$message.error(fileList[0].response.msg)
      }
    },
    // 上传失败
    hadleError(error, file, fileList) {
      this.$refs.upload.clearFiles();
      this.fileList = []
      const result = JSON.parse(error.message);
      this.$message.error(result.msg)
    },
    // 导出模板
    outModel() {
      const that = this
      outExcel('请确认是否下载模板?', "/webadmin/system/webAdminUser/exportModel", {}, '用户信息模板')
    },
    // 导出模板
    outUser() {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      outExcel('请确认是否导出用户?', "/webadmin/system/webAdminUser/export", data, '用户信息')
    }
  }
}
</script>
<style lang="scss" scoped>
</style>