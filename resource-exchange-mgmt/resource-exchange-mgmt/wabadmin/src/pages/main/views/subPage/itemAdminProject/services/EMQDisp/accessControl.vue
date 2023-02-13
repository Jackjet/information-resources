<template>
  <el-main v-loading="isSubmitLoading">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
<!--        emq的访问控制权限页面和数据缓存的访问控制权限页面统一一下-->
<!--        <el-form-item>-->
<!--          <el-input-->
<!--            clearable-->
<!--            size="medium"-->
<!--            autocomplete="off"-->
<!--            v-model="searchItem.appid"-->
<!--            placeholder="请输入用户名">-->
<!--          </el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-button-->
<!--            type="primary"-->
<!--            size='medium'-->
<!--            icon="el-icon-search"-->
<!--            @click="search">查询-->
<!--          </el-button>-->
<!--        </el-form-item>-->
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-plus"
            @click="add">添加
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <tableForm :table-data='tableData'
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
<!--    <el-col :span='24'>-->
<!--      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>-->
<!--    </el-col>-->
    <el-col :span="24" class=" ta-l">
      <el-form label-width="120px">
        <el-row class='el-row-el ta-c'>
          <el-col :span="24">
            <el-form-item class="el-button-el">
              <el-button type="primary" @click="onPageReturn" icon="el-icon-back">返回</el-button>
              <el-button type="primary" @click="submit" icon="el-icon-check">保存</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-col>
    <!--    添加开发者-->
    <el-dialog title="添加开发者" :visible.sync="isShow">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="searchItemDeveloper.name"
            placeholder="请输入用户名">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-search"
            @click="searchDeveloper">查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-search"
            @click="saveDialog">选择
          </el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableDataDeveloper" @selection-change="selectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column
          label="appid"
          align='center'>
          <template slot-scope="scope">{{ scope.row.appid }}</template>
        </el-table-column>
        <el-table-column
          label="用户名"
          align='center'>
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
      </el-table>
      <div class="block">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.page"
          :page-sizes="pageSizes"
          :page-size="page.limit"
          layout="total, sizes, prev, pager, next, jumper" :total="totalDeveloper">
        </el-pagination>
      </div>
    </el-dialog>
  </el-main>
</template>

<script>
import tableForm from './tableList'
import * as session from '../../../../../../../assets/js/common'
import paging from '../../../../integration/pagination'

export default {
  components: { tableForm, paging },

  data () {
    return {
      initialData: [],
      isSubmitLoading: false,
      topicId: '',
      tableData: [],
      tableHeader: [{
        id: false,
        type: '',
        label: 'appid',
        list: 'appid'
      },
      {
        id: false,
        type: '',
        label: '用户名',
        list: 'name'
      },
      {
        id: false,
        type: 'checked',
        label: '访问控制权限',
        list: 'permission'
      }],
      tableOption: {
        label: '操作',
        value: 0,
        width: '80px',
        options: [{
          label: '删除',
          key: 0,
          type: 'delete',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        }]
      },
      deleteList: [],
      total: 0,
      pageSize: 20,
      currentPage: 1,
      tableDataDeveloper: [],
      totalDeveloper: 0,
      searchItemDeveloper: {
        name: ''
      },
      isShow: false,
      pageSizes: [10, 20, 50, 100],
      page: {
        page: 1,
        limit: 20
      },
      searchItem: {
        appid: ''
      },
      sysServiceId: ''
    }
  },

  mounted () {
  },

  methods: {
    initial (id, sysServiceId) {
      console.log(id, sysServiceId)
      const that = this
      that.sysServiceId = sysServiceId
      that.topicId = id
      that.searchItem.sysServiceId = sysServiceId
      that.searchItem.integrationId = session.default.session('currentUser').id
      that.searchItem.topicId = id
      this.getList()
    },
    // 列表
    async getList () {
      const that = this
      try {
        that.isSubmitLoading = true
        let findUrl = this.Interface.topicConfig.findAll
        // let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        const response = await this.request.dataGet(that, findUrl, that.searchItem)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          response.data.data.forEach(item => {
            let permission = item.permission
            let checked0 = false
            let checked1 = false
            if (parseInt(permission) === parseInt(0)) {
              checked0 = true
            }
            if (parseInt(permission) === parseInt(1)) {
              checked1 = true
            }
            if (parseInt(permission) === parseInt(2)) {
              checked0 = true
              checked1 = true
            }
            item.permission = [{
              label: '订阅',
              value: 0,
              checked: checked0
            },
            {
              label: '推送',
              value: 1,
              checked: checked1
            }]
          })

          that.tableData = response.data.data
          // that.total = response.data.data.totalElements
          return false
        }
        that.$message.error(response.data.msg)
      } catch (even) {
        console.log(even)
        that.isSubmitLoading = false
        that.$message.error('数据获取失败')
      }
    },
    // 添加数据
    async submit (formValue) {
      const that = this
      try {
        that.isSubmitLoading = true
        let url = this.Interface.topicConfig.insert
        let arr = []
        let appName = ''
        try {
          this.tableData.forEach(item => {
            let appid = item.appid
            let name = item.name
            let checked0 = item.permission[0].checked
            let checked1 = item.permission[1].checked
            let permission = ''

            if (checked0) {
              permission = 0
            }
            if (checked1) {
              permission = 1
            }
            if (checked0 && checked1) {
              permission = 2
            }
            // 勾选用户，未勾选权限，报异常
            if (!checked0 && !checked1) {
              permission = -1
            }
            if (permission !== '') {
              arr.push({
                appid: appid,
                name: name,
                permission: permission
              })
            }
          })
        } catch (e) {
          if (e.message !== 'error') throw e
        }
        if (appName) {
          that.$message.error('用户' + appName + '未选择访问控制权限')
          that.isSubmitLoading = false
          return false
        }
        let obj = {
          data: arr,
          sysServiceId: that.sysServiceId,
          topicId: that.topicId,
          integrationId: session.default.session('currentUser').id
        }
        console.log(obj)
        const response = await this.request.dataPost(that, url, obj)
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.$emit('tabTablt')
          that.getList()
          return false
        }
        that.isSubmitLoading = false
        that.$message.error(response.data.msg)
      } catch (even) {
        that.isSubmitLoading = false
        that.$message.error('数据获取失败')
      }
    },

    onPageReturn () {
      this.$emit('tabTablt')
    },

    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
    },
    async add () {
      const that = this
      that.common.session('tempData', that.tableData)
      await that.askDeveloperInfo()
    },
    async searchDeveloper () {
      await this.askDeveloperInfo()
    },
    // 获取开发者信息
    async askDeveloperInfo () {
      this.isShow = true
      const that = this
      try {
        let findUrl = this.Interface.sysUser.findAll
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let obj = {
          name: this.searchItemDeveloper.name,
          integrationId: session.default.session('currentUser').id
        }
        const response = await this.request.dataGet(that, url, obj)
        that.tableDataDeveloper = response.data.data.content
        that.totalDeveloper = response.data.data.totalElements
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    saveDialog () {
      const that = this
      let tempData = that.common.session('tempData')
      tempData.forEach(item => {
        let flag = true
        that.tableData.forEach(item1 => {
          if (item.appid === item1.appid) {
            flag = false
          }
        })
        if (flag) {
          that.tableData.push(item)
        }
      })

      let selectData = that.common.session('selectData')
      let tableData = that.tableData
      selectData.forEach(item => {
        let flag = true
        tableData.forEach(item1 => {
          // 相同appid的数据不添加到页面
          if (item1.appid === item.appid) {
            flag = false
          }
        })
        if (flag) {
          tableData.push(item)
        }
      })
      that.tableData = tableData
      that.isShow = false
    },
    // 添加选中数据
    selectionChange (val) {
      const that = this
      let data = []
      val.forEach(item => {
        let dataItem = {
          id: '',
          appid: item.appid,
          name: item.name,
          permission: [{
            label: '订阅',
            value: 0,
            checked: false
          },
          {
            label: '推送',
            value: 1,
            checked: false
          }]
        }
        data.push(dataItem)
      })
      that.common.session('selectData', data)
    },
    handleSizeChange (val) {
      this.page.limit = val
      this.page.page = 1
      this.$emit('pageChange', this.page)
    },

    handleCurrentChange (val) {
      this.page.page = val
      this.$emit('pageChange', this.page)
    },
    search () {
      this.getList()
    },
    async delete (row) {
      const that = this
      if (row.id !== '') {
        let url = this.Interface.topicConfig.deleteById
        that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          console.log(row.id)
          const response = await this.request.dataDelete(that, url, { id: row.id })
          console.log(response.data.code)
          if (response.data.code === 1) {
            that.$message.success('删除成功')
            that.getList()
          } else {
            that.$message.error(response.data.message)
          }
        }).catch(() => {
          return false
        })
      } else {
        let index = this.tableData.indexOf(row)
        if (index > -1) {
          this.tableData.splice(index, 1)
        }
      }
    }
  }
}
</script>
