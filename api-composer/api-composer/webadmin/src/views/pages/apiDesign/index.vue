<template>
  <el-main class="main">
    <el-row :gutter="20">
      <el-col :span="isWitch?6:4" style="padding:0;border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;height: 83vh;">
        <el-col :span="24" style="margin-top: 15px;">
          <el-form :inline="true">
            <el-form-item>
              <el-input clearable size="small" placeholder="请输入搜索名称" @input="filterChange" v-model="filterText">
              </el-input>
            </el-form-item>
            <el-form-item style="margin-top: -15px;">
              <el-button size='mini' @click="restTree" icon="el-icon-refresh-left"></el-button>
              <el-button size='mini' @click="insertTaskType" icon="el-icon-plus"></el-button>
              <el-button size='mini' @click="isWitchClick" :icon="isWitch?'el-icon-s-unfold':'el-icon-s-fold'"></el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="center">
          <!-- <el-tree class="filter-tree" :data="treeData" :props="defaultProps" node-key="id" :default-expanded-keys="showTree" accordion :filter-node-method="filterNode" @node-click="getNodeInfo" style="margin-top: 10px;margin-bottom: 20px;" ref="tree">
          </el-tree> -->
          <el-scrollbar style="height:60vh">
            <el-tree class="filter-tree" ref="tree" accordion :data="treeData" highlight-current node-key="id" :expand-on-click-node="false" :default-expanded-keys="showTree" :props="defaultProps" :filter-node-method="filterNode" @node-click="getNodeInfo" style="margin-bottom: 20px;background-color: #FBFCFC;">
              <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
                  <span class="overflowEllips"> {{ node.label }} </span>
                </el-tooltip>
                <div />
              </span>
            </el-tree>
          </el-scrollbar>
          <el-button size='mini' @click="updateTaskTypeName">重命名</el-button>
          <el-button size='mini' @click="deleteTaskType">删除</el-button>
        </el-col>
      </el-col>
      <el-col :span="isWitch?17:19">
        <el-row>
          <el-col :span="24">
            <el-form :inline="true" class='el-InputForm'>
              <el-form-item>
                <el-input clearable size="medium" placeholder="请输入API名称" v-model="searchItem.name">
                </el-input>
              </el-form-item>
              <el-form-item>
                <autoInput style="width: 120px;" v-model="searchItem.metaKey" ref="metaName" the-id="metaName" :the-responsepath="['data', 'content']" :on-focus="false" size="medium" the-key="name" the-address="/webadmin/resources/findAllMetas" the-placeholder="请输入标签">
                </autoInput>
                <el-input style="width: 160px;" clearable size="medium" placeholder="请输入标签值名称" v-model="searchItem.metaValue">
                </el-input>
              </el-form-item>
              <el-form-item style='margin-left: 15px;'>
                <el-col :span="24">
                  <el-button size='medium' @click="Search" icon="el-icon-search">查询</el-button>
                  <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
                </el-col>
              </el-form-item>
              <el-row>
                <el-col :span="12">
                  <el-button size='medium' @click="handleAdd" icon="el-icon-plus" v-if="permissions.add">新增
                  </el-button>
                </el-col>
              </el-row>
            </el-form>

          </el-col>
          <el-col :span="24">
            <el-table :data="tableData" stripe empty-text="暂无数据" class="el_tab_alage">
              <el-table-column align="left" fit prop="name" label="API名称">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.method === 'GET'" color="#993300" effect="dark" size="small">GET</el-tag>
                  <el-tag v-else-if="scope.row.method === 'POST'" size="small" effect="dark" color="#336699">POST</el-tag>
                  <el-tag v-else-if="scope.row.method === 'PUT'" size="small" effect="dark" color="#FCA130">PUT</el-tag>
                  <el-tag v-else-if="scope.row.method === 'DELETE'" size="small" effect="dark" color="#F93E3E">DELETE
                  </el-tag>
                  <el-tag v-else-if="scope.row.method" color="#109612" effect="dark" size="small">MULT</el-tag>
                  {{ scope.row.name }}
                </template>
              </el-table-column>
              <el-table-column align="left" fit prop="requestUrl" label="请求路径">
              </el-table-column>
              <el-table-column align="left" fit prop="status" label="状态" width="80px">
              </el-table-column>
              <el-table-column show-overflow-tooltip align="left" fit prop="updateTime" label="更新时间" width="110px">
                <template slot-scope="scope">
                  {{scope.row.updateTime.split(' ')[0]}}
                </template>
              </el-table-column>
              <el-table-column align="left" fit prop="metas" label="标签">
              </el-table-column>
              <el-table-column align="left" fit label="操作" width="130px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-edit" v-if="permissions.edit" @click="handleEdit(scope.row)">
                    编辑
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-delete" v-if="permissions.delete" slot="reference" @click="handleDelete(scope.row)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-col :span='24'>
          <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
        </el-col>
      </el-col>
    </el-row>
    <el-dialog :title="taskTitle" :visible.sync="dialogVisible" :before-close="clearDialog">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
        <el-row>
          <el-form-item label="名称:" prop='name'>
            <el-input clearable size="medium" placeholder="请输入名称" v-model="ruleForm.name" style="width: 400px"></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <div style="margin-top: 20px;position: relative;left:38%">
        <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
        <el-button size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>
<script>
import { deleteById, findAll, getTree, insertApiGroup, updateApiGroup, deleteApiGroupById } from "@/api/apiDesign";
import { findAllMetas } from "@/api/resources"
import autoInput from "@/components/input/autoCompleteInput"
import TableList from '@/components/Table/TableList'
import Pagination from '@/components/Table/Pagination'

export default {
  components: { autoInput, TableList, Pagination },
  data() {
    return {
      permissions: {
        add: false,
        edit: false,
        delete: false
      },
      isWitch: false,
      drawer: false,
      add: ['none'],
      edit: ['none'],
      deleteDesign: ['none'],
      isSubmitLoading: false,
      tableData: [],
      searchItem: {
        name: '',
        metaKey: '',
        metaValue: '',
        groupId: ''
      },
      lastItem: {
        name: '',
        metaKey: '',
        metaValue: '',
        groupId: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      fileList: [],
      treeData: [],
      defaultProps: {
        children: 'child',
        label: 'name'
      },
      filterText: '',
      showTree: [],
      ruleForm: {
        name: '',
        parentId: ''
      },
      taskTitle: '',
      dialogVisible: false,
      taskTypeName: '',
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
    this.searchItem.groupId = this.$route.query.groupId
    this.fetchData()
    this.getTreeList()
  },
  methods: {
    isWitchClick() {
      this.isWitch = !this.isWitch;
    },
    // 重置
    reset() {
      const that = this
      that.$refs.metaName.clearValue()
      that.searchItem.groupId = ''
      that.searchItem.name = ''
      that.searchItem.metaKey = ''
      that.searchItem.metaValue = ''
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
      that.fetchData()
    },
    // 获取列表
    async fetchData() {
      const that = this
      let data = ''
      data = this.searchItem
      data.page = this.currentPage
      data.size = this.pageSize
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await findAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let content = res.data.data.content
          content.forEach((item) => {
            switch (item.status) {
              case '0':
                item.status = '待编排';
                break;
              case '1':
                item.status = '待部署';
                break;
              case '2':
                item.status = '已部署';
                break;
              default:
                item.status = '待编排'
            }
            let tempMetaKey = item.metaKey
            let tempMetaValue = item.metaValue
            let relMeta = ''
            if (tempMetaKey.indexOf(',') === -1) {
              relMeta = tempMetaKey + ':' + tempMetaValue
            } else {
              let metaNameArr = tempMetaKey.split(',')
              let metaValueArr = tempMetaValue.split(',')
              metaNameArr.forEach((item, index) => {
                let len = metaNameArr.length - 1
                if (index === len) {
                  relMeta = relMeta + item + ':' + metaValueArr[index]
                } else {
                  relMeta = relMeta + item + ':' + metaValueArr[index] + ','
                }
              })
            }
            item.metas = relMeta
          })
          this.tableData = content
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
      let metaKey = this.$refs.metaName.returnValue()
      this.searchItem.metaKey = metaKey
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.searchItem).map((item, index) => {
        that.searchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData()
    },
    // 添加
    handleAdd() {
      const that = this
      that.$router.push({
        path: '/apiDesignAdd',
        query: {
          id: '',
          groupId: that.searchItem.groupId
        }
      })
    },
    // 编辑
    handleEdit(row) {
      const that = this
      that.$router.push({
        path: '/apiDesignAdd',
        query: {
          id: row.id,
          groupId: that.searchItem.groupId
        }
      })
    },
    // 删除
    async handleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        let response = await deleteById({ id: data.id })
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
    restTree() {
      const that = this
      that.filterText = ''
      this.getTreeList()
    },
    insertTaskType() {
      const that = this
      that.taskTitle = "新增"
      that.dialogVisible = true
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    filterChange() {
      // 这里每当输入的数据有变化就触发原生的过滤节点这个函数
      this.$refs.tree.filter(this.filterText);
    },
    getNodeInfo(data) {
      const that = this
      that.ruleForm.parentId = data.id
      that.searchItem.groupId = data.id
      that.taskTypeName = data.name
      that.showTree = [data.id]
      this.fetchData()
    },
    updateTaskTypeName() {
      const that = this
      let id = that.searchItem.groupId;
      if (id === '1') {
        this.$message.warning('该库名不可修改')
      } else {
        let parentId = that.ruleForm.parentId;
        if (!parentId) {
          this.$message.warning('请选择集成库')
        } else {
          that.taskTitle = "重命名"
          that.ruleForm.name = that.taskTypeName
          that.dialogVisible = true
        }
      }
    },
    deleteTaskType() {
      const that = this
      let parentId = that.ruleForm.parentId;
      if (!parentId) {
        this.$message.warning('请选择集成库')
      } else {
        that.$confirm('请确认是否删除?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          const response = await deleteApiGroupById(parentId)
          that.Loading = false
          if (response.data.code === 1) {
            that.$message.success('删除成功')
            await this.getTreeList()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      }
    },
    /*获取左侧树形列表*/
    async getTreeList() {
      const that = this
      try {
        const res = await getTree()
        if (res.data.code === 1) {
          that.treeData = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.closeModal();
    },
    // 初始化表单
    closeModal() {
      const that = this
      that.ruleForm.name = "";
      that.dialogVisible = false
      that.taskTitle = ''
    },
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let data = that.ruleForm
          // 新增
          if (that.taskTitle === '重命名') {
            data.id = that.searchItem.groupId
            updateApiGroup(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
                this.getTreeList()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            insertApiGroup(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
                this.getTreeList()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        }
      });
    }
  }
}
</script>
<style lang="scss" scoped>
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #ebeef5 !important;
  color: #606266;
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
