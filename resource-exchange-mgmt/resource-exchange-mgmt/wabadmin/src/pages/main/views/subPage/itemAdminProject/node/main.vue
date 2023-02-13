<template>
  <el-main>
    <!-- 调用表格 -->
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
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-plus"
            @click="addRoot">添加
          </el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="id"
                lazy
                default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
                v-loading="loading">
        <el-table-column
          prop="name"
          label="节点名称">
          <template slot-scope="scope">
            <el-image :src="require('@/assets/image/icons/节点 (1).png')"
                      style="margin-bottom:-5px;width: 20px;height: 20px;"></el-image>
            {{ scope.row.name }}
          </template>
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态">
          <template slot-scope="scope">
            <template v-if="scope.row.status === '在线'">
              <el-image :src="require('@/assets/image/icons/Online.png')"
                        style="float:left;width: 20px;height: 20px;"></el-image>
            </template>
            <template v-else>
              <el-image :src="require('@/assets/image/icons/Online (1).png')"
                        style="float:left;width: 20px;height: 20px;"></el-image>
            </template>
            <div style="float: left">{{scope.row.status}}</div>
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注">
        </el-table-column>
        <!-- 按钮操作 -->
        <el-table-column fit align='center'
                         :label="tableOption.label" :width="tableOption.width">
          <template slot-scope="scope">
            <pictureButton :key="item" :type="value.type" :label="value.label"
                           v-for="(value, item) in tableOption.options"
                           @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
          </template>
        </el-table-column>
      </el-table>
    </el-col>

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogNode"
      :before-close="closeModal"
      width="30%">
      <el-form :model="ruleForm" ref="ruleForm" :rules="rules">
        <el-form-item
          prop="nodename"
          label="节点名称"
          label-width="100px">
          <el-input clearable autocomplete="off" v-model="ruleForm.nodename" :disabled="!isAdd"
                    placeholder="请输入节点名称"></el-input>
        </el-form-item>
        <el-form-item
          prop="remark"
          label="备 注"
          label-width="100px">
          <el-input clearable type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.remark"
                    placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeModal" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="save('ruleForm')" icon="el-icon-check">保 存</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>

<script>
  import tableForm from '../../../integration/tableList'
  import * as session from '../../../../../../assets/js/common'
  import pictureButton from '../../../integration/pictureButton'

  export default {
    components: {tableForm, pictureButton},
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
          width: '250px',
          options: [
            {
              label: '添加',
              key: 0,
              type: 'add',
              icon: 'el-icon-delete',
              State: false,
              method: (row) => {
                this.add(row)
              }
            },
            {
              label: '编辑',
              key: 0,
              type: 'edit',
              icon: 'el-icon-delete',
              State: false,
              method: (row) => {
                this.edit(row)
              }
            },
            {
              label: '删除',
              key: 0,
              type: 'delete',
              icon: 'el-icon-delete',
              State: false,
              method: (row) => {
                this.delete(row)
              }
            },
            {
              label: '安装',
              key: 0,
              type: 'install',
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
        isAdd: true,
        loading: false,
        dialogTitle: ''
      }
    },

    mounted () {
      const that = this
      that.searchItem.integrationId = session.default.session('currentUser').id

      that.getList()
    },

    methods: {
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
      addRoot () {
        this.dialogTitle = '添加节点'
        this.nowId = ''
        this.dialogNode = true
      },
      add (row) {
        this.dialogTitle = '添加子节点'
        this.nowId = row.nodeId
        this.dialogNode = true
        this.isAdd = true
      },
      async edit (row) {
        const that = this
        let url = this.Interface.runNode.findById
        try {
          const response = await this.request.dataGet(this, url, {id: row.nodeId})
          that.ruleForm.nodename = response.data.data.name
          that.ruleForm.remark = response.data.data.remark
          that.nowId = response.data.data.id
          console.log(that.ruleForm)
        } catch (e) {
          that.$message.error('节点信息获取失败')
        }
        that.dialogTitle = '编辑节点'
        that.dialogNode = true
        that.isAdd = false
      },
      delete (row) {
        const that = this
        that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
          type: 'warning'
        }).then(async () => {
          let url = this.Interface.runNode.delete
          that.loading = true
          const response = await this.request.dataDelete(that, url, {id: row.nodeId})
          that.loading = false
          if (response.data.code === 1) {
            that.$message.success('删除成功')
            that.getList()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          that.loading = false
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
              console.log(routeData,routeData.href)
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
      closeModal () {
        this.dialogNode = false
        this.isAdd = true
        this.$refs['ruleForm'].resetFields()
        this.ruleForm.nodename = ''
        this.ruleForm.remark = ''
        this.nowId = ''
        this.getList()
      },
      async save (ruleForm) {
        const that = this
        that.$refs[ruleForm].validate(async (index) => {
          if (index === false) {
            return false
          }
          that.dialogNode = false
          if (that.isAdd) {
            await that.addSave(ruleForm)
          } else {
            await that.editSave(ruleForm)
          }
        })
      },
      async addSave (ruleForm) {
        const that = this
        let data = {
          integrationId: that.integrationId,
          parentNodeId: that.nowId,
          name: that.ruleForm.nodename,
          remark: that.ruleForm.remark
        }
        let url = that.Interface.runNode.insert
        that.loading = true
        const response = await that.request.dataPost(that, url, data)
        that.loading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          that.$message.success(response.data.msg)
          /**
           * 清空表单元素 重置验证信息.
           * @param imageUrl
           * @param detail
           */
          that.closeModal()
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      async editSave (ruleForm) {
        const that = this
        console.log(that.nowId)
        let data = {
          id: that.nowId,
          name: that.ruleForm.nodename,
          remark: that.ruleForm.remark
        }
        let url = that.Interface.runNode.update
        that.addLoading = true
        const response = await that.request.dataPut(that, url, data)
        that.addLoading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          that.$message.success(response.data.msg)
          /**
           * 清空表单元素 重置验证信息.
           * @param imageUrl
           * @param detail
           */
          that.closeModal()
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      getTreeTableData (data) {
        data.forEach(item => {
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
      }
    }
  }
</script>

<style scoped>

</style>
