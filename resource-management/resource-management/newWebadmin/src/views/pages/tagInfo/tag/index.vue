<template>
  <el-main class="main">
    <el-row>
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
              <el-input clearable size="medium" placeholder="请输入标签名称" v-model="SearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item style='margin-left: 15px;'>
              <el-row :gutter="10">
                <el-col :span="12">
                  <el-button size='medium' @click="Search" icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="12">
                  <el-button size='medium' @click="insertTag" icon="el-icon-plus" v-if="permissions.add">新增</el-button>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24">
          <el-table v-loading="isSubmitLoading" :data="tableData" stripe empty-text="暂无数据" class="el_tab_alage">
            <el-table-column align="left" fit prop="name" label="标签名称">
            </el-table-column>
            <el-table-column align="left" fit prop="detail" label="描述" show-overflow-tooltip>
            </el-table-column>
            <el-table-column align="left" fit prop="updateTime" label="更新时间">
              <template slot-scope="scope">
                {{scope.row.updateTime.split(' ')[0]}}
              </template>
            </el-table-column>
            <el-table-column align="left" fit prop="updateName" label="更新人">
            </el-table-column>
            <el-table-column align="left" fit label="操作" width="200px">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-edit" v-if="permissions.edit" @click="updateTag(scope.row)">
                  编辑
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" v-if="permissions.deleteData" slot="reference" @click="handleDelete(scope.row)">删除</el-button>
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
          <el-row style="margin-left: 80px">
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

      <el-dialog :title="tagTitle" :visible.sync="dialogTag" :before-close="closeTag" center>
        <el-form :model="tagForm" :rules="tagRules" ref="tagForm" label-width="80px" class="demo-ruleForm">
          <el-row style="margin-left: 80px">
            <el-form-item label="名称:" prop='name'>
              <el-input clearable size="medium" placeholder="请输入名称" v-model="tagForm.name" style="width: 400px"></el-input>
            </el-form-item>
            <el-form-item label="分组:" :label-width="this.formLabelWidth">
              <el-cascader expandTrigger="hover" v-model="tagForm.groupId" :options="groupOptions" :props="{ checkStrictly: true,emitPath:false }" clearable>
              </el-cascader>
            </el-form-item>
            <el-form-item label="描述:">
              <el-input type="textarea" :rows="4" clearable size="medium" placeholder="请输入描述" v-model="tagForm.detail" style="width: 400px"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <div style="margin-top: 20px;position: relative;left:38%">
          <el-button size="medium" @click="resetTag('tagForm')">取消</el-button>
          <el-button size="medium" type="primary" @click="submitTag('tagForm')">提 交</el-button>
        </div>
      </el-dialog>
    </el-row>
  </el-main>
</template>
<script>
import { deleteById, findAll, insert, update } from "@/api/tagInfo.js"
import { getTree, insertGroup, updateGroup, deleteGroup, getGroupList } from "@/api/groupInfo.js"
import Pagination from '@/components/table/Pagination.vue'
import autoInput from "@/components/input/autoCompleteInput"

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
        add: false,
        edit: false,
        deleteData: false
      },
      isSubmitLoading: false,
      tableData: [],
      SearchItem: {
        name: '',
        groupId: ''
      },
      lastItem: {
        name: '',
        groupId: ''
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
      filterText: '',
      //insert/update tag
      tagTitle: '',
      dialogTag: false,
      tagForm: {
        id: '',
        name: '',
        detail: '',
        groupId: ''
      },
      tagRules: {
        name: [{
          required: true,
          message: "请填写标签名称",
          trigger: "change"
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请填写标签名称'
        }]
      },
      groupOptions: []
    }
  },
  created() {
    this.findPermission()
    this.fetchData()
    this.getTreeList()
    this.getGroupList()
  },
  methods: {
    // 获取列表
    async getGroupList() {
      const that = this
      let data = { type: '3' }
      try {
        const res = await getGroupList(data)
        if (res.data.code === 1) {
          that.groupOptions = res.data.data
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
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
      data = that.SearchItem
      data.page = that.currentPage
      data.size = that.pageSize
      data.groupId = that.groupId
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
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData()
    },

    // 删除
    async handleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        let response = await deleteById(data.id)
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
      that.ruleForm.parentId = ''
      that.groupId = ''

      this.getTreeList()
      this.fetchData()
    },

    /*获取左侧树形列表*/
    async getTreeList() {
      const that = this
      try {
        const res = await getTree({ type: 3 })
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
      this.fetchData();
    },

    insertGroup() {
      const that = this
      that.groupTitle = "新增"
      that.dialogVisible = true
    },

    insertTag() {
      const that = this
      that.tagTitle = "新增"
      that.dialogTag = true
    },

    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.closeModal();
    },

    resetTag(tagForm) {
      this.$refs["tagForm"].resetFields();
      this.closeTag();
    },

    // 初始化表单
    closeModal() {
      const that = this
      that.ruleForm.name = ''
      that.dialogVisible = false
      that.groupTitle = ''
    },

    clearDialog() {
      const that = this
      that.groupTitle = ''
      that.dialogVisible = false
    },

    closeTag() {
      const that = this
      that.tagForm.name = ''
      that.tagForm.detail = ''
      that.dialogTag = false
      that.tagTitle = ''
    },

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          that.ruleForm.type = 3
          let data = that.ruleForm
          // 新增
          if (that.groupTitle === '重命名') {
            data.id = that.groupId
            updateGroup(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
                this.getTreeList();
                this.getGroupList()
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
                this.getTreeList();
                this.getGroupList()
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
      this.getGroupList();
    },

    updateTag(row) {
      const that = this
      that.tagTitle = "编辑"
      that.tagForm.id = row.id
      that.tagForm.name = row.name
      that.tagForm.detail = row.detail
      that.tagForm.groupId = row.groupId
      that.dialogTag = true
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
            this.getGroupList();
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      }
    },

    submitTag(ruleForm) {
      this.$refs["tagForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let data = that.tagForm
          if (!data.groupId) {
            data.groupId = that.groupId
          }
          // 新增
          if (that.tagTitle === '编辑') {
            update(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetTag('tagForm')
                this.fetchData()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            insert(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetTag('tagForm')
                this.fetchData()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        }
      });
    },
  }
}
</script>
<style lang="scss">
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #ebeef5 !important;
  color: #606266;
}
</style>
