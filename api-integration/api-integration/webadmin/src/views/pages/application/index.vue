<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入应用名称" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <!--        <el-form-item>-->
        <!--          <el-input clearable-->
        <!--                    size="medium"-->
        <!--                    placeholder="请输入创建人名称"-->
        <!--                    v-model="SearchItem.createByName">-->
        <!--          </el-input>-->
        <!--        </el-form-item>-->
        <el-form-item>
          <el-select v-model="SearchItem.container" placeholder="请选择容器" clearable size="medium">
            <el-option v-for="item in containerInfos" :key="item.container" :label="item.name"
                       :value="item.container"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="Search" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="handleAdd" icon="el-icon-plus" v-if="permissions.apiManageApplicationInsert">新增</el-button>
          <el-button size='medium' @click="handleExport" icon="el-icon-search"
                     v-if="permissions.apiManageApplicationExport">导出
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <el-table :data="tableData" stripe empty-text="暂无数据" class="el_tab_alage">
        <el-table-column align="center" fit prop="id" label="应用ID">
        </el-table-column>
        <el-table-column align="center" fit prop="name" label="应用名称">
        </el-table-column>
        <el-table-column align="center" fit prop="container" label="所属容器">
        </el-table-column>
        <el-table-column align="center" fit prop="createTime" label="创建时间">
        </el-table-column>
        <el-table-column align="center" fit label="操作" width="300px" v-if="permissions.apiManageApplicationOperate">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)" v-permission="edit">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" v-permission="deleteIt" slot="reference"
                       @click="handleDelete(scope.row)">删除
            </el-button>

            <el-button size="mini" type="text" icon="el-icon-key" @click="handleKey(scope.row)" v-permission="key">
              密钥管理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <Add ref="add" @Reload='fetchData'></Add>
    <Key ref="key" @Reload='fetchData'></Key>
  </el-main>
</template>

<script>
import {
  findAll,
  deleteById,
  exportApplication
} from "@/api/application.js";

import {
  findAllMeta,
  findAllContainer,
  findAllApi
} from "@/api/sourceService";
import Pagination from '@/components/table/Pagination.vue'
import Add from '@/views/pages/application/add'
import Key from '@/views/pages/application/key'
import {outExcel} from '@/utils/export'

export default {
  components: {Pagination, Add, Key},

  data() {
    return {
      permissions: {
        apiManageApplicationInsert: false,
        apiManageApplicationExport: false,
        apiManageApplicationOperate: false
      },


      SearchItem: {
        name: '',
        createByName: '',
        container: ''
      },
      tableData: [],
      total: 0,
      pageSize: '20',
      currentPage: '1',
      add: ['none'],
      edit: ['none'],
      deleteIt: ['none'],
      key: ['none'],
      exportData: ['none'],

      //容器
      containerInfos: [],
    }
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
      permissionsArr.forEach(item => {
        let itemArr = item.split('_')
        this.permissions[itemArr[0]] = true
      })
    },

    // 获取列表
    async fetchData() {
      const that = this
      let data = ''
      data = this.SearchItem,
          data.page = this.currentPage,
          data.size = this.pageSize

      try {
        that.isSubmitLoading = true
        const res = await findAll(data)
        that.isSubmitLoading = false
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
    // 搜索
    Search() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      this.fetchData()
    },
    handleAdd() {
      console.log(this.$refs.add)
      this.$refs.add.initial('添加')
    },
    handleEdit(row) {
      this.$refs.add.initial('编辑', row.id)
    },
    async handleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        let response = await deleteById({id: data.id})
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          await this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    async handleExport() {
      const that = this
      outExcel('请确认是否导出文件?', "/webadmin/application/export?name=" + that.SearchItem.name + '&container=' + that.SearchItem.container, {}, '应用列表')
    },
    handleKey(row) {
      this.$refs.key.initial(row.id)
    },

    //容器查询
    async getContainers() {
      const that = this
      let param = {
        page: 1,
        size: 100000,
        type: 3
      }
      let res = await findAllContainer(param)
      if (res.data.code == 1) {
        that.containerInfos = res.data.data.content


        let containerInfos = that.containerInfos

        containerInfos.forEach((item, i) => {
          item.container = item.ip + ':' + item.port
        })

        that.containerInfos = containerInfos
      }
    },
  },


  created() {
    this.fetchData()
    this.getContainers()
    this.findPermission()
  }
}
</script>
