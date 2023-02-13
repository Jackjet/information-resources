<template>
  <el-dialog
    title="选择节点"
    :visible.sync="isShow"
    width="50%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="closeModal">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="searchItem.name"
            placeholder="请输入节点名称">
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
            icon="el-icon-check"
            @click="pick">选择
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- 调用表格 -->
    <el-col :span="24">
      <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="id"
                lazy
                default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column
          prop="name"
          label="节点名称">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态">
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注">
        </el-table-column>
        <!-- 单选框 -->
        <el-table-column
          align="center"
          width="50px"
          label="选择">
          <template slot-scope="scope">
            <el-radio :label="scope.row.nodeId" v-model="radio">&nbsp;</el-radio>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-dialog>
</template>

<script>
import tableForm from '../../../integration/tableList'
import * as session from '../../../../../../assets/js/common'

export default {
  components: { tableForm },
  data () {
    return {
      tableData: [],
      searchItem: {
        name: '',
        integrationId: ''
      },
      tableSelection: {},
      tableHeader: [
        {
          id: false,
          type: '',
          label: '节点名称',
          list: 'name'
        },
        {
          id: false,
          type: '',
          label: '状态',
          list: 'status'
        },
        {
          id: false,
          type: '',
          label: '备注',
          list: 'remark'
        }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        options: [
          {
            label: '添加',
            key: 0,
            type: 'text',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.add(row)
            }
          },
          {
            label: '删除',
            key: 0,
            type: 'text',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.delete(row)
            }
          },
          {
            label: '安装文档',
            key: 0,
            type: 'text',
            icon: 'el-icon-delete',
            State: false,
            method: (row) => {
              this.install(row)
            }
          }
        ]
      },
      dialogNode: false,
      ruleForm: {
        nodename: '',
        remark: ''
      },
      rules: {
        nodename: [{
          required: true,
          message: '请输入节点名称',
          trigger: ['blur', 'change']
        }]
      },
      integrationId: this.common.session('currentUser').id,
      nowId: '',
      isShow: false,
      radio: '',
      ids: []
    }
  },

  mounted () {
  },

  methods: {
    async initial (nodeId) {
      const that = this
      that.radio = nodeId
      that.searchItem.integrationId = session.default.session('currentUser').id
      await that.getList()
      that.isShow = true
    },
    async getList () {
      const that = this
      let url = that.Interface.runNode.findAll
      try {
        const response = await that.request.dataGet(that, url, that.searchItem)
        if (response.data.code === 1) {
          that.tableData.splice(0)
          that.tableData = this.getTreeTableData(response.data.data)
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (e) {
        that.$message.error('节点获取失败')
      }
    },
    search () {
      this.getList()
    },
    add (row) {
      this.nowId = row.nodeId
      this.dialogNode = true
    },
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.runNode.delete
        that.Loading = true
        const response = await this.request.dataDelete(that, url, {id: row.nodeId})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        that.Loading = false
        return false
      })
    },
    async install (row) {
      const that = this
      try {
        let findUrl = that.Interface.runNode.findById
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, {id: row.nodeId})
        that.loading = false
        if (response.data.code === 1) {
          if (response.data.data.installUrl) {
            let routeData = this.$router.resolve({
              name: 'documents',
              query: {
                url: response.data.data.installUrl,
                batUrl: response.data.data.installBatUrl
              }
            })
            window.open(routeData.href, '_blank')
          } else {
            that.$message.warning('未获取到安装包地址')
          }
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },
    getTreeTableData (data) {
      data.forEach(item => {
        this.ids.push(item.id)
        if (item.status === '0') {
          item.status = '离线'
        } else {
          item.status = '在线'
        }
        if (item.children.length > 0) {
          item.children = this.getTreeTableData(item.children)
        }
      })
      return data
    },
    closeModal () {
      this.isShow = false
      this.radio = ''
      this.searchItem.integrationId = ''
      this.searchItem.name = ''
    },
    pick () {
      let nodeId = this.radio
      let node = this.getNode(nodeId,this.tableData)
      console.log(node)
      if (node) {

        if (node.status === '离线') {
          this.$message.error('当前节点已离线，不可以添加服务')
          return false
        }
        this.$emit('getNode', node)
        this.closeModal()
      } else {
        this.$message.warning('请选择节点')
      }
    },

    getNode (nodeId,data) {
      for (let i = 0; i < data.length; i++) {
        if (data[i].nodeId === nodeId) {
          console.log(111)
          return data[i]
        } else if (data[i].children.length > 0){
          console.log(222)
          let result = this.getNode(nodeId,data[i].children)
          if (result) {
            console.log(333)
            return result
          }
        }
      }
      return false
    }
  }
}
</script>

<style scoped>

</style>
