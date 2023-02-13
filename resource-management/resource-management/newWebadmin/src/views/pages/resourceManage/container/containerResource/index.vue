<template>
  <el-main class="main">
    <el-col span="4" style="padding:0;border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;min-height: 83vh;">
      <el-col :span="24" style="margin-top:15px;margin-left: 5px">
        <el-form :inline="true">
          <el-form-item>
            <el-input clearable size="mini" placeholder="请输入搜索名称" v-model="filterText">
            </el-input>
            <el-button size='mini' @click="restTree" icon="el-icon-refresh-left"></el-button>
            <el-button size='mini' @click="insertGroup" icon="el-icon-plus"></el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" class="center">
        <el-tree class="filter-tree" :data="treeData" :props="defaultProps" node-key="id" :default-expanded-keys="showTree" accordion :filter-node-method="filterNode" @node-click="getNodeInfo" style="margin-bottom: 20px;background-color: #FBFCFC;" ref="tree">
        </el-tree>
        <el-col :span="24" style="margin-left: 15px;margin-bottom:15px;">
          <el-button size='mini' @click="updateGroupName">重命名</el-button>
          <el-button size='mini' @click="deleteGroup">删除</el-button>
        </el-col>
      </el-col>
    </el-col>
    <el-col span="19" style="margin-left: 10px">
      <el-col :span="24" style="margin-bottom: 5px">
        <el-form :inline="true" class='el-InputForm'>
          <el-form-item>
            <el-input clearable size="mini" placeholder="请输入容器名称" v-model="SearchItem.name">
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
              <el-col :span="12">
                <el-button size='mini' @click="Search" icon="el-icon-search">查询</el-button>
              </el-col>
              <el-col :span="12">
                <el-button size='mini' @click="handleAdd" icon="el-icon-plus" v-if="permissions.add">新增</el-button>
              </el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24">
        <el-table v-loading="isSubmitLoading" :data="tableData" stripe empty-text="暂无数据" class="el_tab_alage">
          <el-table-column show-overflow-tooltip align="left" fit prop="name" label="容器名称">
          </el-table-column>
          <el-table-column show-overflow-tooltip align="left" fit prop="host" label="容器地址">
          </el-table-column>
          <el-table-column show-overflow-tooltip align="left" prop="status" label="状态" width="80px">
            <template slot-scope="scope">
              <div v-if="scope.row.status === '0'">
                <div style="display:inline-block;width: 18px;height: 18px;border-radius: 50%;border: 1px solid #ff0000; position: relative;top:5px">
                  <div style="width: 10px;height: 10px;border-radius: 50%;background-color: #ff0000;position: relative;top:3px;left: 3px"></div>
                </div>
                <span style="margin-left: 5px">不可用</span>
              </div>
              <div v-if="scope.row.status === '1'">
                <div style="display:inline-block;width: 18px;height: 18px;border-radius: 50%;border: 1px solid #00ff00; position: relative;top:5px">
                  <div style="width: 10px;height: 10px;border-radius: 50%;background-color: #00ff00;position: relative;top:3px;left: 3px"></div>
                </div>
                <span style="margin-left: 5px">可用</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column align="left" show-overflow-tooltip prop="tagInfo" fit label="标签">
          </el-table-column>
          <el-table-column show-overflow-tooltip align="left" fit prop="updateTime" label="更新时间" width="110px">
            <template slot-scope="scope">
              {{scope.row.updateTime.split(' ')[0]}}
            </template>
          </el-table-column>
          <el-table-column align="left" label="操作" width="260px">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-document" v-if="permissions.detail" slot="reference" @click="handleDetail(scope.row)">容器信息</el-button>
              <el-button size="mini" type="text" icon="el-icon-connection" v-if="permissions.testConnection" slot="reference" @click="dataConnection(scope.row)">测试链接</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" v-if="permissions.deleteContainer" slot="reference" @click="handleDelete(scope.row)">删除</el-button>
              <el-button size="mini" type="text" icon="el-icon-s-operation" v-if="scope.row.type === '3' && scope.row.isInit != '1'" slot="reference" @click="resetContainer(scope.row)">容器初始化</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span='24'>
        <Pagination ref="page" :total="total" @pageChange="pageChange"></Pagination>
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
import { deleteById, findAll, update } from "@/api/containerInfo.js"
import { getTree, insertGroup, updateGroup, deleteGroup } from "@/api/groupInfo.js"
import Pagination from '@/components/table/Pagination.vue'
import autoInput from "@/components/input/autoCompleteInput"
import { withoutIpRequest, apiDesignList, updateInit } from "@/api/withoutIpRequest.js"


export default {
  components: { Pagination, autoInput },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  data() {
    return {
      permissions: {
        detail: false,
        add: false,
        testConnection: false,
        deleteContainer: false
      },
      isSubmitLoading: false,
      tableData: [],
      SearchItem: {
        name: '',
        tagName: '',
        tagValue: ''
      },
      lastItem: {
        name: '',
        tagName: '',
        tagValue: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',

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
      this.groupId = groupId
      this.ruleForm.parentId = groupId
      this.showTree.push(groupId)
      this.fetchData()
    }
    this.getTreeList()
    this.findPermission()
    this.fetchData()
  },
  methods: {
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
          content.forEach(item => {
            let ip = item.ip
            let port = item.port
            item.host = ip + ':' + port
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
            item.tagInfo = relTag
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
        path: '/containerAdd',
        query: {
          type: '新增',
          groupId: that.groupId
        }
      })
    },
    // 详情
    handleDetail(row) {
      const that = this
      that.$router.push({
        path: '/containerDetail',
        query: {
          id: row.id,
          groupId: that.groupId,
          type: row.type
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

    async resetContainer(row) {
      console.log(row)
      const that = this
      let ip = row.ip
      let port = row.port
      let apiIntegrationBaseUrl = process.env.VUE_APP_API_INTEGRATION_URL
      let baseUrl = process.env.VUE_APP_BASE_API
      let data = {
        container: ip + ':' + port,
        logDir: 'http://' + ip + process.env.VUE_APP_API_INTEGRATION_LOG_DIR
      }
      let url = '/kong/kongInit'
      let url1 = '/webadmin/containerInfo/updateInit'
      try {
        let res = await apiDesignList(apiIntegrationBaseUrl, url, 'GET', data)
        if (res.data.code === 1) {
          let res1 = await updateInit(baseUrl, url1, 'PUT', { id: row.id })
          if (res1.data.code === 1) {
            this.fetchData()
          } else {
            that.$message.error(res1.data.msg)
          }
          that.$message.success(res.data.msg)
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (e) {
        that.$message.error(e.msg)
      }
    },

    async dataConnection(row) {
      const that = this
      that.isSubmitLoading = true
      let ip = row.ip
      let port = row.port
      let type = row.type
      if (!type) {
        this.$message.error('请选择容器类型');
        return
      }
      let baseUrl = 'http://' + ip + ':' + port
      if (type === '3') {
        try {
          let res = await withoutIpRequest(baseUrl, '', 'get')
          if (res.status === 200) {
            row.status = 1
            this.$message.success('链接成功');
          } else {
            row.status = 0
            this.$message.error('链接失败');
          }
        } catch (e) {
          row.status = 0
          this.$message.error('链接失败');
        }
      } else {
        let url = '/api/container/status'
        if (type === '1') {
          url = '/task/container/status'
        }
        try {
          let res = await withoutIpRequest(baseUrl, url, 'get')
          if (res.status === 200) {
            row.status = 1
            this.$message.success('链接成功');
          } else {
            row.status = 0
            this.$message.error('链接失败');
          }
        } catch (e) {
          row.status = 0
          this.$message.error('链接失败');
        }
      }
      update(row).then(async (res) => {
        this.loading = true;
        if (res.data.code !== 1) {
          this.$message.error(res.data.msg);
        }
        that.isSubmitLoading = false
        await this.fetchData()
      });
    },

    restTree() {
      const that = this
      that.filterText = ''
      that.ruleForm.parentId = ''
      that.groupId = ''
      this.getTreeList()
      this.fetchData()
    },

    /*获取左侧树形列表*/
    async getTreeList() {
      const that = this
      try {
        const res = await getTree({ type: 1 })
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
      this.CloseModal();
    },

    // 初始化表单
    CloseModal() {
      const that = this
      that.ruleForm.name = "";
      that.dialogVisible = false
      that.groupTitle = ''
    },

    clearDialog() {
      const that = this
      that.groupTitle = ''
      that.ruleForm.name = ''
      that.dialogVisible = false
    },

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          that.ruleForm.type = 1
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
      let parentId = that.groupId;
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
    }

  }
}
</script>
<style lang="scss">
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #ebeef5 !important;
  color: #606266;
}
</style>
