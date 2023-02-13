<template>
  <el-main class="main">
    <el-col :span="isWitch?6:4" style="padding:0;border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;min-height: 83vh;">
      <el-col :span="24" style="margin-top:15px;margin-left: 5px">
        <el-form :inline="true">
          <el-form-item>
            <el-input clearable size="mini" placeholder="请输入搜索名称" v-model="filterText">
            </el-input>
            <el-button size='mini' @click="restTree" icon="el-icon-refresh-left"></el-button>
            <el-button size='mini' @click="insertGroup" icon="el-icon-plus"></el-button>
            <el-button size='mini' @click="isWitchClick" :icon="isWitch?'el-icon-s-unfold':'el-icon-s-fold'"></el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" class="center">
        <!-- <el-tree class="filter-tree" :data="treeData" :props="defaultProps" node-key="id" :default-expanded-keys="showTree" accordion :filter-node-method="filterNode" groupId @node-click="getNodeInfo" style="margin-bottom: 20px;background-color: #FBFCFC;" ref="tree">
        </el-tree> -->
        <el-tree ref="tree" accordion :data="treeData" highlight-current node-key="id" :expand-on-click-node="false" :default-expanded-keys="showTree" :props="defaultProps" :filter-node-method="filterNode" @node-click="getNodeInfo" style="margin-bottom: 20px;background-color: #FBFCFC;">
          <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
            <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
              <span class="overflowEllips"> {{ node.label }} </span>
            </el-tooltip>
            <div />
          </span>
        </el-tree>
        <el-col :span="24" style="margin-left: 15px;margin-bottom:15px;">
          <el-button size='mini' @click="updateGroupName">重命名</el-button>
          <el-button size='mini' @click="deleteGroup">删除</el-button>
        </el-col>
      </el-col>
    </el-col>
    <el-col :span="isWitch?17:19" style="margin-left: 10px">
      <el-col :span="24">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-input clearable size="mini" placeholder="请输入API名称" v-model="SearchItem.name">
            </el-input>
          </el-form-item>
          <el-form-item>
            <autoInput ref="findTagName" the-id="findTagName" :the-responsepath="['data', 'content']" :on-focus="false" size="mini" the-key="name" the-address="/webadmin/tagInfo/findAll" the-placeholder="请输入标签名称">
            </autoInput>
          </el-form-item>
          <el-form-item>
            <el-input clearable size="mini" placeholder="请输入标签值名称" v-model="SearchItem.tagValue">
            </el-input>
          </el-form-item>
          <el-form-item style='margin-left: 15px;'>
            <el-row :gutter="10">
              <el-col :span="6">
                <el-button size='mini' @click="Search" icon="el-icon-search">查询</el-button>
              </el-col>
              <el-col :span="6">
                <el-button size='mini' @click="handleAdd" icon="el-icon-plus" v-if="permissions.add">新增</el-button>
              </el-col>
              <el-col :span="6">
                <el-button size='mini' @click="handleExport" icon="el-icon-search">导出</el-button>
              </el-col>
              <el-col :span="5">
                <el-upload class="upload-demo" :action="action" :data="uploadData" :headers="headers" :file-list="fileList" multiple="false" :on-success="onSuccess" :before-upload="beforeUpload">
                  <el-button type="mini" icon="el-icon-upload2" size='mini'>导入</el-button>
                </el-upload>
              </el-col>
              <el-col :span="1">
                <el-popover placement="top-start" title="导入示例(导入文件支持.txt和.json)" width="300" trigger="hover">
                  <slot>
                    <el-divider></el-divider>
                    <p>[{</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"name": "API名称",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"host": [{</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"target": "39.102.73.127:10086"</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;}],</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"isLoadBalancing": false,</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"tagName": "标签名1,标签名2",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"tagValue": "标签值1,标签值2",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"path": "/kongTest/test1",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"protocol": "Http",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"method": "GET",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"formatType": "JSON",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"body": "",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"params": "",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"response": "",</p>
                    <p>&nbsp;&nbsp;&nbsp;&nbsp;"constants": ""</p>
                    <p>}]</p>
                  </slot>
                  <i slot="reference" class="el-icon-warning-outline" style="margin-left: 10px;cursor:pointer"></i>
                </el-popover>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" style="margin-top:20px">
        <el-table :data="tableData" stripe empty-text="暂无数据" class="el_tab_alage">
          <el-table-column align="left" show-overflow-tooltip prop="name" label="API名称" width="200px">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.method === 'GET'" color="#993300" effect="dark" size="small">GET</el-tag>
              <el-tag v-else-if="scope.row.method === 'POST'" size="small" effect="dark" color="#336699">POST</el-tag>
              <el-tag v-else-if="scope.row.method === 'PUT'" size="small" effect="dark" color="#FCA130">PUT</el-tag>
              <el-tag v-else-if="scope.row.method === 'DELETE'" size="small" effect="dark" color="#F93E3E">DELETE</el-tag>
              <el-tag v-else-if="scope.row.method" color="#109612" effect="dark" size="small">MULT</el-tag>
              {{scope.row.name}}
            </template>
          </el-table-column>
          <el-table-column align="left" show-overflow-tooltip prop="host" label="host">
          </el-table-column>
          <el-table-column align="left" show-overflow-tooltip prop="path" label="path">
          </el-table-column>
          <el-table-column align="left" show-overflow-tooltip prop="tagName" label="标签">
          </el-table-column>
          <el-table-column align="left" show-overflow-tooltip prop="updateTime" label="更新时间">
            <template slot-scope="scope">
              {{scope.row.updateTime.split(' ')[0]}}
            </template>
          </el-table-column>
          <el-table-column align="left" fit label="操作" width="200px">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" v-if="permissions.edit" @click="handleEdit(scope.row)">
                编辑
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-s-platform" v-if="permissions.test" @click="handleTest(scope.row)">测试
              </el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" v-if="permissions.deleteIt" slot="reference" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span='24'>
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </el-col>
    <el-dialog :title="groupTitle" :visible.sync="dialogVisible" :before-close="clearDialog" center>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="80px" class="demo-ruleForm">
        <el-row>
          <el-form-item label="名称:" prop='name' style="margin-left: 80px">
            <el-input clearable size="medium" placeholder="请输入名称" v-model="ruleForm.name" style="width: 400px;"></el-input>
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
import { deleteById, findAll } from "@/api/sourceApi.js";
import TableList from '@/components/table/tableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import { outExcel } from '@/utils/export'
import { getToken } from '@/utils/auth'
import autoInput from "@/components/input/autoCompleteInput"
import { getTree, insertGroup, updateGroup, deleteGroup } from "@/api/groupInfo.js"

export default {
  components: { TableList, Pagination, autoInput },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  data() {
    return {
      isWitch: false,
      drawer: false,
      permissions: {
        add: false,
        edit: false,
        test: false,
        deleteIt: false
      },
      isSubmitLoading: false,
      tableData: [],
      SearchItem: {
        name: '',
        tagName: '',
        tagValue: '',
        groupId: ''
      },
      lastItem: {
        name: '',
        tagName: '',
        tagValue: '',
        groupId: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      action: process.env.VUE_APP_BASE_API + '/webadmin/sourceApi/import?file=',
      uploadData: {
        groupId: ''
      },
      headers: {
        Authorization: 'token ' + getToken()
      },
      fileList: [],
      //属性分组用数据
      groupTitle: '',
      treeData: [],
      defaultProps: {
        children: 'child',
        label: 'name'
      },
      showTree: [],
      dialogVisible: false,
      ruleForm: {
        name: '',
        parentId: '',
        type: ''
      },
      rules: {
        name: [{
          required: true,
          message: "请输入名称",
          trigger: "change"
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请输入名称'
        }]
      },
      groupName: '',
      groupId: '',
      filterText: ''
    }
  },
  created() {
    let groupId = this.$route.query.groupId
    if (groupId) {
      this.SearchItem.groupId = groupId
      this.groupId = groupId
      this.ruleForm.parentId = groupId
      this.showTree.push(groupId)
    }
    this.getTreeList()
    this.fetchData()
    this.findPermission()
  },
  methods: {
    isWitchClick() {
      this.isWitch = !this.isWitch;
    },
    // 查询按钮权限
    findPermission() {
      let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
      permissionsArr.forEach(item => {
        let itemArr = item.split('_')
        if (('/' + itemArr[0]) === this.$route.path) {
          this.permissions[itemArr[1]] = true
        }
      })
    },

    // 获取列表
    async fetchData() {
      const that = this
      let data = ''
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      data.groupId = this.groupId
      try {
        that.isSubmitLoading = true
        const res = await findAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let content = res.data.data.content
          content.forEach((item) => {
            item.hostString = item.host
            let temp = JSON.parse(item.host)
            let host = ''
            temp.forEach((tempItem) => {
              host += tempItem.target
              host += ','
            })

            if (host !== '') {
              item.host = host.substring(0, host.length - 1)
            }
            let tempTagName = item.tagName
            let tempTagValue = item.tagValue
            let relTag = ''
            if (tempTagName.indexOf(',') === -1) {
              relTag = tempTagName + ':' + tempTagValue
            } else {
              let tagNameArr = tempTagName.split(',')
              let tagValueArr = tempTagValue.split(',')
              tagNameArr.forEach((item, index) => {
                let len = tagNameArr.length - 1
                if (index === len) {
                  relTag = relTag + item + ':' + tagValueArr[index]
                } else {
                  relTag = relTag + item + ':' + tagValueArr[index] + ','
                }
              })
            }
            item.tagName = relTag
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
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.SearchItem.tagName = this.$refs.findTagName.returnValue()
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
    // 添加
    handleAdd() {
      const that = this
      that.$router.push({
        path: '/apiAdd',
        query: {
          id: '',
          groupId: that.groupId
        }
      })
    },
    // 编辑
    handleEdit(row) {
      const that = this
      that.$router.push({
        path: '/apiUpdate',
        query: {
          id: row.id,
          groupId: row.groupId
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
    // 测试
    handleTest(row) {
      const that = this
      that.$router.push({
        path: '/apiTest',
        query: {
          id: row.id,
          name: row.name,
          host: row.hostString,
          path: row.path,
          method: row.method,
          protocol: row.protocol
        }
      })
    },
    //导出
    async handleExport() {
      const that = this
      outExcel('请确认是否导出文件?', "/webadmin/sourceApi/export?name=" + that.SearchItem.name + '&tagName=' + that.SearchItem.tagName + '&tagValue=' + that.SearchItem.tagValue + '&groupId=' + that.SearchItem.groupId, {}, 'API列表')
    },

    onSuccess(response, file, fileList) {
      this.fileList = []
      if (response.code === 0) {
        this.$message.error(response.msg)
      } else {
        this.fetchData()
      }
    },
    //导入
    beforeUpload(file) {
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
      const whiteList = ['json', 'txt'];

      if (whiteList.indexOf(fileSuffix) === -1) {
        this.$message.error("上传文件只能是json和txt格式")
        return false;
      }
    },

    restTree() {
      const that = this
      that.filterText = ''
      that.ruleForm.parentId = ''
      that.SearchItem.groupId = ''
      that.groupId = ''
      that.uploadData.groupId = ''
      this.getTreeList()
      this.fetchData()
    },

    /*获取左侧树形列表*/
    async getTreeList() {
      const that = this
      try {
        const res = await getTree({ type: 0 })
        if (res.data.code === 1) {
          that.treeData = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },

    getNodeInfo(data) {
      const that = this
      that.ruleForm.parentId = data.id
      that.SearchItem.groupId = data.id
      that.groupId = data.id
      that.uploadData.groupId = data.id
      that.groupName = data.name
      that.showTree = [data.id]
      this.fetchData()
    },

    insertGroup() {
      const that = this
      that.groupTitle = "新增"
      that.dialogVisible = true
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
      that.groupTitle = ''
    },

    clearDialog() {
      const that = this
      that.groupTitle = ''
      that.dialogVisible = false
    },

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          that.ruleForm.type = 0
          let data = that.ruleForm
          // 新增
          if (that.groupTitle === '重命名') {
            data.id = that.groupId
            updateGroup(data).then((res) => {
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
            insertGroup(data).then((res) => {
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
    },

    updateGroupName() {
      const that = this
      let parentId = that.ruleForm.parentId;
      if (!parentId) {
        this.$message.warning('请选择分组')
      } else {
        that.groupTitle = "重命名"
        that.ruleForm.name = that.groupName
        that.dialogVisible = true
      }
    },

    deleteGroup() {
      const that = this
      let parentId = that.ruleForm.parentId;
      if (!parentId) {
        this.$message.warning('请选择分组')
      } else {
        that.$confirm('请确认是否删除?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          const response = await deleteGroup(parentId)
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
  }
}
</script>
<style lang="scss">
// .el-tree-node.is-current > .el-tree-node__content {
//   background-color: #ebeef5 !important;
//   color: #606266;
// }
// .filter-tree {
//   overflow-x: scroll;
// }
// .filter-tree::-webkit-scrollbar {
//   display: none;
// }
// 后期样式修改
.tree_title {
  border-bottom: 1px solid #ccc;
  height: 50px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
}
.tree_content {
  min-height: 650px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
