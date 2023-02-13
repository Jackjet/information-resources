<template>
  <el-main>
    <el-row :gutter="20">
      <el-col span="5" style="padding:0;border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;min-height: 83vh;">
        <el-col :span="24" style="margin-top: 15px;">
          <el-form :inline="true">
            <el-form-item>
              <el-input clearable size="small" placeholder="请输入搜索名称" v-model="filterText">
              </el-input>
            </el-form-item>
            <el-form-item style="margin-top: -15px;">
              <el-button size='mini' @click="resetTree" icon="el-icon-refresh-left"></el-button>
              <el-button size='mini' @click="openInsertNodeDialog" icon="el-icon-plus" v-if="permissions.apiManageApiInsertNode"></el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="center">
          <el-scrollbar style="height:60vh">
            <el-tree class="filter-tree" :data="treeData" :props="defaultProps" node-key="id" :default-expanded-keys="showTree" accordion :filter-node-method="filterNode" @node-click="getNodeInfo" style="margin-top: 10px;margin-bottom: 20px;background-color: #FBFCFC;" ref="tree">
            </el-tree>
          </el-scrollbar>
          <el-button size='mini' @click="openUpdateNodeNameDialog" v-if="permissions.apiManageApiUpdateNode">重命名</el-button>
          <el-popconfirm title="确认删除？" @confirm="deleteNode" style="margin-left: 10px;" v-if="permissions.apiManageApiDeleteNode">
            <el-button slot="reference" size='mini'>删除</el-button>
          </el-popconfirm>
        </el-col>
      </el-col>
      <el-col span="19">
        <el-row>
          <el-col :span="24">
            <el-form :inline="true" class='el-InputForm'>
              <el-form-item>
                <el-input clearable size="medium" placeholder="请输入API名称" v-model="SearchItem.name">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-input clearable size="medium" placeholder="请输入接口资源名称" v-model="SearchItem.sourceName">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-select v-model="SearchItem.container" placeholder="请选择容器" clearable @click="getContainers" size="medium">
                  <el-option v-for="item in containerInfos" :key="item.container" :label="item.name" :value="item.container"></el-option>
                </el-select>
              </el-form-item>

              <!-- <el-form-item>
                <el-select size="medium" v-model="SearchItem.key" filterable remote reserve-keyword placeholder="请输入标签名" :remote-method="remoteMethod" clearable>
                  <el-option v-for="item in tagInfos" :key="item.name" :label="item.name" :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-input clearable size="medium" placeholder="请输入标签值" v-model="SearchItem.value">
                </el-input>
              </el-form-item> -->

              <el-form-item style='margin-left: 1%;'>
                <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
                 <el-button size='medium' @click="add" icon="el-icon-plus" style="margin-left: 3px;" v-if="permissions.apiManageApiInsertApi">新增</el-button>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <div style="padding-top: 20px;">
              <el-button type="primary" v-permission="exportAllApiDoc" @click="showApiName" plain size='medium' icon="el-icon-download" v-if="permissions.apiManageApiGenerateDoc">批量生成文档</el-button>
            </div>
            <el-dialog title="生成API文档" :visible.sync="dialogVisible" width="30%">
              <el-form :model="docForm" :rules="docFormRules" ref="docForm">
                <el-form-item label="文件名" prop="apiDocName">
                  <el-input clearable size="medium" placeholder="请输入文件名" style="width: 300px;" v-model.trim="docForm.apiDocName">
                  </el-input>
                </el-form-item>
                <el-form-item label="文档格式" prop="apiDocType">
                  <el-select size="medium" placeholder="请选择文档格式" v-model.trim="docForm.apiDocType">
                    <el-option label="Word文档" value="0"></el-option>
                    <el-option label="Txt格式（适用于postman）" value="1"></el-option>
                  </el-select>
                </el-form-item>
              </el-form>

              <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="exportApiDoc">确 定</el-button>
              </span>
            </el-dialog>

            <el-table :data="tableData" stripe style="width: 100%" v-loading="isSubmitLoading" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center">
              </el-table-column>
              <el-table-column label="API名称">
                <template slot-scope="scope">
                  <el-tag v-if="scope.row.method === 'GET'" color="#993300" effect="dark" size="small">GET</el-tag>
                  <el-tag v-else-if="scope.row.method === 'POST'" size="small" effect="dark" color="#336699">POST</el-tag>
                  <el-tag v-else-if="scope.row.method === 'PUT'" size="small" effect="dark" color="#FCA130">PUT</el-tag>
                  <el-tag v-else-if="scope.row.method === 'DELETE'" size="small" effect="dark" color="#F93E3E">DELETE</el-tag>
                  <el-tag v-else-if="scope.row.method" color="#109612" effect="dark" size="small">MULT</el-tag>
                  {{scope.row.name}}
                </template>
              </el-table-column>
              <el-table-column prop="routeInfo" label="路由路径">
              </el-table-column>
              <el-table-column prop="sourceName" label="接口资源">
              </el-table-column>
              <!-- <el-table-column prop="metaStr" label="标签">
              </el-table-column> -->
              <el-table-column prop="updateTime" label="更新时间">
              </el-table-column>
              <el-table-column prop="container" label="所属容器">
              </el-table-column>
              <el-table-column prop="userName" label="更新人">
              </el-table-column>
              <el-table-column label="操作" width="200px" align="center" v-if="permissions.apiManageApiOperate">
                <template slot-scope="scope">
                  <el-button type="text" plain='false' icon="el-icon-edit" @click="edit(scope.row)">编辑
                  </el-button>
                  <el-button type="text" plain='false' icon="el-icon-tickets" @click="copyApi(scope.row)">复制
                  </el-button>
                  <el-button type="text" plain='false' icon="el-icon-edit" @click="doTest(scope.row)" v-if="scope.row.status === '1'">测试
                  </el-button>
                  <el-button type="text" plain='false' icon="el-icon-edit" @click="publish(scope.row)" v-if="scope.row.status === '0'">发布
                  </el-button>
                  <el-button type="text" plain='false' icon="el-icon-edit" @click="revocation(scope.row)" v-if="scope.row.status === '1'">撤回发布
                  </el-button>
                  <el-button type="text" plain='false' icon="el-icon-edit" @click="deleteApi(scope.row)">删除
                  </el-button>
                </template>

              </el-table-column>
            </el-table>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span='24'>
            <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-dialog title="添加节点" :visible.sync="insertNodeDialog" :before-close="clearInsertNodeDialog">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
        <el-row>
          <el-form-item label="名称:" prop='name'>
            <el-input clearable size="medium" placeholder="请输入名称" v-model.trim="ruleForm.name" style="width: 400px"></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <div style="margin-top: 20px;position: relative;left:38%">
        <el-button size="medium" @click="clearInsertNodeDialog('ruleForm')">取消</el-button>
        <el-button size="medium" type="primary" @click="insert">提 交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="重命名" :visible.sync="updateNodeNameDialog" :before-close="clearUpdateNodeNameDialog">
      <el-form :model="updateNameRuleForm" :rules="rules" ref="updateNameRuleForm" label-width="80px" class="demo-ruleForm">
        <el-row>
          <el-form-item label="名称:" prop='name'>
            <el-input clearable size="medium" placeholder="请输入名称" v-model.trim="updateNameRuleForm.name" style="width: 400px"></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <div style="margin-top: 20px;position: relative;left:38%">
        <el-button size="medium" @click="clearUpdateNodeNameDialog">取消</el-button>
        <el-button size="medium" type="primary" @click="updateNodeName">提 交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="发布" :visible.sync="publishDialog" :before-close="clearPublishDialog">
      <el-form :model="publishForm" :rules="publishRules" ref="publishForm" label-width="80px" class="demo-ruleForm">
        <el-row>
          <el-form-item label="容器:" prop='container'>
            <el-select v-model="publishForm.container">
              <el-option v-for="item in containerInfos" :label="item.name" :value="item.ip + ':' + item.port"></el-option>
            </el-select>
          </el-form-item>
        </el-row>
      </el-form>
      <div style="margin-top: 20px;position: relative;left:38%">
        <el-button size="medium" @click="clearPublishDialog('ruleForm')">取消</el-button>
        <el-button size="medium" type="primary" @click="submitPublish">提 交</el-button>
      </div>
    </el-dialog>
  </el-main>
</template>
<script>
import {
  findAll,
  deleteApi,
  copy,
  exportApiDoc,
  publish,
  revocation
} from "@/api/apiManage"

import {
  tree,
  add,
  update,
  deleteGroup
} from "@/api/apiGroupManage"

import {
  findAllMeta,
  findAllContainer,
  findAllApi
} from "@/api/sourceService";
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'

import AutoCompleteInput from '@/components/input/autoCompleteInput.vue'
export default {
  components: { TableList, Pagination, AutoCompleteInput },

  data() {
    return {
      permissions: {
        apiManageApiInsertNode: false,
        apiManageApiUpdateNode: false,
        apiManageApiDeleteNode: false,
        apiManageApiInsertApi: false,
        apiManageApiGenerateDoc: false,
        apiManageApiOperate: false
      },

      //左侧 组
      treeData: [],
      filterText: '',
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      showTree: [],
      nodeId: '',
      operator: '0',

      //添加节点对话框
      insertNodeDialog: false,
      ruleForm: {
        name: '',
        parentId: '0'
      },
      rules: {
        name: [{
          required: true,
          message: "请输入名称",
          trigger: ['change', 'blur']
        }]
      },

      //节点重命名
      updateNodeNameDialog: false,
      updateNameRuleForm: {
        id: '',
        name: ''
      },

      //右侧  Api
      theAddress: '',
      SearchItem: {
        name: '',
        sourceName: '',
        container: '',
        key: '',
        value: '',
        groupId: ''
      },
      tagInfos: [],
      containerInfos: [],

      //发布
      publishDialog: false,
      publishForm: {
        id: '',
        container: ''
      },
      publishRules: {
        container: [{
          required: true,
          message: "请输入名称",
          trigger: ['change', 'blur']
        }]
      },

      drawer: false,
      exportAllApiDoc: ['none'],
      insertApi: ['none'],
      isSubmitLoading: false,
      deletelistiD: [],
      tableData: [],
      time: [],

      lastItem: {
        apiName: '',
        sourceApiName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      dialogVisible: false,

      docForm: {
        apiDocName: '',
        apiDocType: ''
      },
      docFormRules: {
        apiDocName: [{
          required: true,
          message: "请输入文件名",
          trigger: "change"
        }],
        apiDocType: [{
          required: true,
          message: "请选择文档格式",
          trigger: "change"
        }]
      },
    }
  },
  created() {
    const that = this
    that.resetTree()
    that.getContainers()
    that.findPermission()
  },

  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
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


    //组
    async getGroupInfo() {
      const that = this
      let data = {}
      that.isSubmitLoading = true
      const res = await tree(data)
      that.isSubmitLoading = false

      if (res.data.code === 1) {
        that.treeData = res.data.data

        if (that.treeData.length > 0) {
          that.showTree.push(that.treeData[0].id);
        }
      } else {
        this.$message.error(res.data.msg)
      }
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },

    async getNodeInfo(data) {
      const that = this
      that.nodeId = data.id
      that.operator = data.id
      that.showTree = [data.id]

      await that.getApiList()
    },

    async getApiList() {
      const that = this

      let param = {}

      param = this.SearchItem
      param.groupId = that.nodeId
      param.page = this.currentPage
      param.size = this.pageSize

      that.isSubmitLoading = true
      const res = await findAll(param)
      that.isSubmitLoading = false

      if (res.data.code === 1) {
        that.tableData = res.data.data.content
        that.total = res.data.data.totalElements
      } else {
        this.$message.error(res.data.msg)
      }
    },
    async resetTree() {
      const that = this
      that.filterText = ''
      that.operator = '0'
      that.nodeId = ''
      await that.getGroupInfo()
      await that.getApiList()
    },

    //节点重命名
    openUpdateNodeNameDialog() {
      const that = this
      if (that.operator === '0') {
        that.$message.warning('请选择组')
      } else {
        that.updateNodeNameDialog = true
      }
    },
    clearUpdateNodeNameDialog() {
      const that = this
      that.updateNodeNameDialog = false
      that.$refs.updateNameRuleForm.resetFields()
    },

    async updateNodeName() {
      const that = this
      that.$refs["updateNameRuleForm"].validate(validate => {
        console.log(validate)
        if (validate) {
          that.doUpdateNode()
        }
      })
    },
    async doUpdateNode() {
      const that = this
      that.updateNameRuleForm.id = that.operator

      that.isSubmitLoading = true
      const res = await update(that.updateNameRuleForm)
      that.isSubmitLoading = false

      if (res.data.code === 1) {
        that.$message.success("修改成功")

        that.resetTree()
        that.clearUpdateNodeNameDialog()
      } else {
        that.$message.error(res.data.msg)
      }
    },

    //添加节点对话框
    openInsertNodeDialog() {
      const that = this
      that.insertNodeDialog = true
    },
    async insert() {
      const that = this

      that.$refs["ruleForm"].validate(validate => {
        if (validate) {
          that.doInsertNode()
        }
      })

    },
    async doInsertNode() {
      const that = this
      that.ruleForm.parentId = that.operator
      const response = await add(that.ruleForm)
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        that.$message.success('添加成功')
        that.resetTree()

        that.clearInsertNodeDialog()
      } else {
        that.$message.error(response.data.msg)
      }
    },

    clearInsertNodeDialog() {
      const that = this
      that.insertNodeDialog = false
      that.$refs.ruleForm.resetFields()
    },


    // 重置
    reset() {
      const that = this
      that.SearchItem.apiName = ''
      that.SearchItem.sourceApiName = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },


    //删除节点
    async deleteNode() {
      const that = this
      if (that.operator !== '0') {
        that.isSubmitLoading = true
        let res = await deleteGroup(that.operator)
        that.isSubmitLoading = false

        if (res.data.code === 1) {
          that.resetTree()
          this.$message.success('删除成功')
        } else {
          this.$message.error(res.data.msg)
        }
      } else {
        this.$message.warning('请选择组')
      }
    },



    //标签查询
    async remoteMethod(query) {
      const that = this
      if (query !== '') {
        let res = await findAllMeta({ name: query })
        if (res.data.code == 1) {
          that.tagInfos = res.data.data.content
        }
      }
    },

    //容器查询
    async getContainers() {
      const that = this
      let param = {
        type: 3,
        page: 1,
        size: 100000
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

    // 获取列表
    async fetchData(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
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
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.deletelistiD = []
      that.listiRead = []
      vals.map(function (v, k) {
        that.deletelistiD.push(v.id)
        that.listiRead.push({ id: v.id, status: v.status })
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
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },

    // 行高
    cellStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    // 启用 重载列表
    async deleteApi(row) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await deleteApi(row.id)
        that.isSubmitLoading = false
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

    // 导出DOC
    async exportApiDoc() {
      const that = this

      await this.$refs["docForm"].validate(valid => {
        if (valid) {
          that.$confirm('请确认是否导出文档?', '提示', {
            type: 'warning'
          }).then(async () => {
            let params = {
              ids: that.deletelistiD + '',
              type: that.docForm.apiDocType,
              name: that.docForm.apiDocName
            }
            const response = await exportApiDoc(params)
            if (response.status === 200) {
              that.$message.success('导出成功')
              let blob

              if (that.docForm.apiDocType === '0') {
                blob = new Blob([response.data], { type: `application/msword` });
              } else if (that.docForm.apiDocType === '1') {
                blob = new Blob([response.data], { type: `text/plain` });
              }

              let objectUrl = URL.createObjectURL(blob);
              let link = document.createElement("a");
              let fileName = that.docForm.apiDocName;
              link.href = objectUrl;
              link.setAttribute("download", fileName);
              link.setAttribute("download", fileName);
              document.body.appendChild(link);
              link.click();
              document.body.removeChild(link)
              await this.fetchData()
            } else {
              that.$message.error(response.data.msg)
            }
            that.$refs["docForm"].resetFields();
            that.dialogVisible = false
          }).catch(() => {
            that.dialogVisible = false
            that.apiDocName = ''
            return false
          })
        }
      })
    },
    //新增
    add(data) {
      const that = this
      that.$router.push({
        path: '/apiManage/apiManage/add',
        query: {
          type: '新增',
          groupId: that.nodeId
        }
      })
    },
    //编辑
    edit(data) {
      const that = this
      that.$router.push({
        path: '/apiManage/apiManage/edit',
        query: {
          id: data.id,
          type: '编辑'
        }
      })
    },
    //复制API
    copyApi(row) {
      console.log(row)
      let data = { id: row.id }
      copy(data).then((res) => {
        if (res.data.code === 1) {
          this.$message.success('复制成功');
          this.loading = false;
          this.fetchData();
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },

    showApiName() {
      const that = this
      if (that.deletelistiD.length === 0) {
        return that.$message.warning('请先选择API')
      }
      that.dialogVisible = true
    },

    doTest(row) {
      const that = this
      that.$router.push({
        path: '/apiManage/apiTest/doTest',
        query: {
          id: row.id,
          container: row.container
        }
      })
    },
    async publish(row) {
      const that = this
      that.publishDialog = true
      that.publishForm.id = row.id
      that.getContainers()
    },

    async submitPublish() {
      const that = this

      that.$refs["publishForm"].validate(valid => {
        if (!valid) {
          return false
        } else {
          that.doPublish(that.publishForm)
        }
      })
    },
    async doPublish(data) {
      const that = this
      const response = await publish(data)
      if (response.data.code === 1) {
        that.$message.success('发布成功')
        await that.fetchData()
        that.clearPublishDialog()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    revocation(row) {
      const that = this
      that.$confirm('请确认是否撤回发布?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        let data = {
          id: row.id
        }
        const response = await revocation(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('撤回成功')
          await this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },


    clearPublishDialog() {
      const that = this
      that.publishDialog = false
      that.$refs.publishForm.resetFields()
    }
  }
}
</script>

<style>
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #ebeef5 !important;
  color: #606266;
}
</style>
