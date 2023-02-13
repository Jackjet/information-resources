<template>
  <el-main v-loading="isSubmitLoading">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
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
                 :table-selection="tableSelection"
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
    <el-col :span="24" class=" ta-l">
      <el-form>
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
            @click="search">查询
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
import * as session from '../../../../../../../../assets/js/common'

export default {
  components: { tableForm },

  data () {
    return {
      isSubmitLoading: false,
      key: '',
      sysServiceId: '',
      expire: 0,
      tableData: [],
      tableSelection: {},
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
        options: [
          {
            label: '删除',
            key: 0,
            type: 'delete',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.delete(row)
            }
          }
        ]
      },
      deleteList: [],
      total: 0,
      pageSize: 20,
      currentPage: 1,
      tableDataDeveloper: [],
      tableSelectionDeveloper: {},
      tableHeaderDeveloper: [{
        id: false,
        type: '',
        label: '用户名',
        list: 'appid'
      }],
      tableOptionDeveloper: [],
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
      data: [],
      tempData: []
    }
  },

  mounted () {
  },

  methods: {
    async initial (key, sysServiceId, expire) {
      this.key = key
      this.sysServiceId = sysServiceId
      this.expire = expire
      this.isShow = false
      this.tempData = []
      await this.getList()
    },
    // 列表
    async getList () {
      const that = this
      try {
        that.isSubmitLoading = true
        let findUrl = this.Interface.keyConfig.findAll
        let obj = {
          sysServiceId: that.sysServiceId,
          key: that.key,
          integrationId: session.default.session('currentUser').id
        }
        const response = await this.request.dataGet(that, findUrl, obj)
        that.isSubmitLoading = false
        let data = []
        if (response.data.code === 1) {
          response.data.data.content.forEach(item => {
            let permission = item.permission
            let read = false
            let write = false
            if (parseInt(permission) === parseInt(0)) {
              read = true
            }
            if (parseInt(permission) === parseInt(1)) {
              write = true
            }
            if (parseInt(permission) === parseInt(2)) {
              read = true
              write = true
            }
            item.permission = [{
              label: '读',
              value: 0,
              checked: read
            },
            {
              label: '写',
              value: 1,
              checked: write
            }]

            let dataItem = {
              id: item.id,
              appid: item.appid,
              name: item.name,
              permission: item.permission
            }

            data.push(dataItem)
          })

          that.tableData = data
          that.total = response.data.data.totalElements
          return false
        }
        that.$message.error(response.data.msg)
      } catch (even) {
        console.log(even)
        that.isSubmitLoading = false
        that.$message.error('数据获取失败')
      }
    },
    async initTableData () {
      await this.getList()

      this.tempData.forEach(item => {
        let flag = true
        this.tableData.forEach(item1 => {
          if (item.appid === item1.appid) {
            flag = false
          }
        })
        if (flag) {
          this.tableData.push(item)
        }
      })

      let data1 = this.data
      let tableData = this.tableData
      data1.forEach(item => {
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
      this.tableData = tableData
    },
    // 添加数据
    async submit (formValue) {
      const that = this
      try {
        that.isSubmitLoading = true
        let url = this.Interface.keyConfig.insert
        let arr = []
        this.tableData.forEach(item => {
          let appid = item.appid
          let name = item.name
          let read = item.permission[0].checked
          let write = item.permission[1].checked
          let permission = ''
          if (read) {
            permission = 0
          }
          if (write) {
            permission = 1
          }
          if (read && write) {
            permission = 2
          }
          if (!read && !write) {
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

        let obj = {
          data: arr,
          expire: this.expire,
          sysServiceId: that.sysServiceId,
          key: that.key,
          integrationId: session.default.session('currentUser').id
        }
        const response = await this.request.dataPost(that, url, obj)
        if (response.data.code === 1) {
          that.$message.success('保存成功')
          that.$emit('tabTablt')
          that.getList()
        }
        that.isSubmitLoading = false
      } catch (even) {
        that.isSubmitLoading = false
        that.$message.error('保存失败')
      }
    },

    onPageReturn () {
      this.isShow = false
      this.$emit('tabTablt')
    },

    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
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
    async add () {
      await this.askDeveloperInfo()
    },
    async search () {
      await this.askDeveloperInfo()
    },
    closeDialog () {
      this.isShow = false
    },

    // 保存选中的开发者
    async saveDialog () {
      this.isShow = false
      await this.initTableData()
      this.data = []
      this.tempData = this.tableData
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

    // 添加选中数据
    selectionChange (val) {
      console.log('选中数据')
      console.log(val)
      this.data = []
      val.forEach(item => {
        let dataItem = {
          id: '',
          appid: item.appid,
          name: item.name,
          permission: [{
            label: '读',
            value: 0,
            checked: false
          },
          {
            label: '写',
            value: 1,
            checked: false
          }]
        }
        this.data.push(dataItem)
      })
    },
    async delete (row) {
      const that = this
      if (row.id !== '') {
        let url = this.Interface.keyConfig.delete + '/' + row.id
        that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          const response = await this.request.dataDeleteById(that, url)
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
