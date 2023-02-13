<template>
  <el-main class="main">
    <el-row>
      <el-col :span="4" style="padding:0;border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;min-height: 83vh;" v-loading="loadTree">
        <el-col :span="24" style="margin-top:15px;margin-left: 5px">
          <el-form :inline="true">
            <el-form-item>
              <el-input clearable
                        size="mini"
                        placeholder="请输入搜索名称"
                        v-model="filterText">
              </el-input>
              <el-button  size='mini' @click="restTree" icon="el-icon-refresh-left" style="color: #5677DF"></el-button>
              <el-button  size='mini' @click="insertGroup" icon="el-icon-plus" style="color: #5677DF"></el-button>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24" class="center">
          <el-tree
              class="filter-tree"
              :data="treeData"
              :props="defaultProps"
              node-key="id"
              :default-expanded-keys="showTree"
              accordion
              :filter-node-method="filterNode"
              @node-click="getNodeInfo"
              style="margin-bottom: 20px;background-color: #FBFCFC;"
              ref="tree">
          </el-tree>
          <el-col :span="24" style="margin-left: 10px;padding-bottom: 5px;">
            <el-button  size='mini' @click="updateGroupName" style="color: #5677DF">重命名</el-button>
            <el-button  size='mini' @click="deleteGroup" style="color: #5677DF">删除</el-button>
          </el-col>
        </el-col>
      </el-col>
      <el-col :span="19" style="margin-left: 10px">
        <el-col :span="24" style="margin-bottom: 5px">
          <el-form :inline="true" class='el-InputForm'>
            <el-form-item>
              <el-input clearable
                        size="medium"
                        placeholder="请输入数据源名称"
                        v-model="SearchItem.name">
              </el-input>
            </el-form-item>
            <el-form-item style='margin-left: 15px;'>
              <el-row :gutter="10">
                <el-col :span="12">
                  <el-button size='medium' @click="Search" style="color: #5677DF" icon="el-icon-search">查询</el-button>
                </el-col>
                <el-col :span="12">
                  <el-button size='medium' @click="handleAdd" style="color: #5677DF" icon="el-icon-plus">新增</el-button>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="24">
          <el-table
              v-loading="isSubmitLoading"
              :data="tableData"
              stripe
              empty-text="暂无数据"
              class="el_tab_alage">
            <el-table-column
                align="left"
                prop="name"
                label="数据源名称">
              <template slot-scope="scope">
                <el-image
                    v-if="scope.row.type === 'MySQL'"
                    :src="require('@/assets/image/mysql.png')"
                    style="width: 40px;height: 40px;position: relative;top: 4px"
                ></el-image>
                <el-image v-else-if="scope.row.type === 'PostgreSQL'"
                          :src="require('@/assets/image/PostgreSQL.png')"
                          style="width: 40px;height: 40px;position: relative;top: 4px"
                ></el-image>
                <el-image v-else-if="scope.row.type === 'Oracle'"
                          :src="require('@/assets/image/Oracle.png')"
                          style="width: 40px;height: 40px;position: relative;top: 4px"
                ></el-image>
                <el-image
                    v-if="scope.row.type === 'SQLserver'"
                    :src="require('@/assets/image/SQLserver.png')"
                    style="width: 40px;height: 40px;position: relative;top: 4px"
                ></el-image>
                <el-image
                    v-if="scope.row.type === 'dameng'"
                    :src="require('@/assets/image/dameng.png')"
                    style="width: 40px;height: 40px;position: relative;top: 4px"
                ></el-image>
                <span class="nameSpan" >{{scope.row.name}}</span>
              </template>
            </el-table-column>
            <el-table-column
                align="left"
                fit
                prop="status"
                label="状态">
              <template slot-scope="scope">
                <div v-if="scope.row.status === '0'">
                  <div style="display:inline-block;width: 18px;height: 18px;border-radius: 50%;border: 1px solid #ff0000; position: relative;top:5px" >
                    <div style="width: 10px;height: 10px;border-radius: 50%;background-color: #ff0000;position: relative;top:3px;left: 3px"></div>
                  </div>
                  <span style="margin-left: 5px">不可用</span>
                </div>
                <div v-if="scope.row.status === '1'">
                  <div style="display:inline-block;width: 18px;height: 18px;border-radius: 50%;border: 1px solid #00ff00; position: relative;top:5px" >
                    <div style="width: 10px;height: 10px;border-radius: 50%;background-color: #00ff00;position: relative;top:3px;left: 3px"></div>
                  </div>
                  <span style="margin-left: 5px">可用</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column
                align="left"
                fit
                prop="detail"
                label="描述"
                show-overflow-tooltip>
            </el-table-column>
            <el-table-column
                align="left"
                fit
                prop="updateTime"
                label="更新时间">
            </el-table-column>
            <el-table-column
                align="left"
                fit
                label="操作">
              <template slot-scope="scope">
                <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)">
                  编辑
                </el-button>
                <el-button size="mini" type="text" icon="el-icon-connection" slot="reference" @click="dataConnection(scope.row)">测试连接</el-button>
                <el-button size="mini" type="text" icon="el-icon-delete" slot="reference" @click="handleDelete(scope.row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
        <el-col :span='24'>
          <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
        </el-col>
      </el-col>
      <el-dialog :title="groupTitle" :visible.sync="dialogVisible" :before-close="clearDialog" center width="600px">
        <el-form :model="ruleForm"
                 :rules="rules"
                 ref="ruleForm"
                 label-width="80px"
                 class="demo-ruleForm"
                 style="width: 400px;margin: 0 auto;"
        >
          <el-row>
            <el-form-item label="名称:" prop='name'>
              <el-input
                  clearable
                  size="medium"
                  placeholder="请输入名称"
                  v-model="ruleForm.name"
              ></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <div style="margin-top: 20px;text-align: center;">
          <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
        </div>
      </el-dialog>
    </el-row>
  </el-main>
</template>
<script>
import Pagination from '../../integration/pagination'

export default {
  components: { Pagination },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  data() {
    return {
      isSubmitLoading: false,
      loadTree: false,
      tableData: [],
      SearchItem: {
        name: '',
        groupId:''
      },
      lastSearch: {
        name: '',
        groupId:''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',

      //属性分组用数据
      groupTitle:'',
      treeData:[],
      defaultProps: {
        children: 'child',
        label: 'name'
      },
      showTree:[],
      dialogVisible:false,
      ruleForm:{
        name:'',
        parentId:'',
        type: 2
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
      groupName:'',
      groupId:'',
      filterText:''
    }
  },
  created() {
    let groupId = this.$route.query.groupId
    if (groupId){
      this.SearchItem.groupId = groupId
      this.groupId = groupId
      this.ruleForm.parentId = groupId
      this.showTree.push(groupId)
    }
    this.fetchData()
    this.getTreeList()
  },
  methods: {

    // 获取列表
    async fetchData() {
      const that = this
      try {
        let findUrl = this.Interface.dataResource.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let obj = {}
        Object.entries(that.SearchItem).map((item, index) => {
          that.lastSearch[item[0]] = that.SearchItem[item[0]]
          obj[item[0]] = that.lastSearch[item[0]]
        })
        that.isSubmitLoading = true
        let res = await this.request.dataGet(that, url, obj)
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
      this.$refs.page.changePage()
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastSearch[item[0]]
      })
      this.fetchData()
    },
    // 添加
    handleAdd() {
      const that = this
      that.$router.push({
        path: '/index/dataResourceAdd',
        query: {
          type:'新增',
          groupId:that.groupId
        }
      })
    },
    // 编辑
    handleEdit(row) {
      const that = this
      that.$router.push({
        path: '/index/dataResourceEdit',
        query: {
          id: row.id,
          type : '编辑',
          groupId:that.groupId
        }
      })
    },
    // 删除
    async handleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.dataResource.delete
        that.isSubmitLoading = true
        let response = await this.request.dataDelete(that, url, {id: data.id})
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

    async dataConnection(row) {
      const that = this
      try {
        let url = this.Interface.dataResource.toLink
        that.isSubmitLoading = true
        let res = await this.request.dataGet(that, url, { id:row.id })
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.$message.success('连接成功');
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    restTree(){
      const that = this
      that.filterText = ''
      that.ruleForm.parentId = ''
      that.groupId = ''
      that.SearchItem.groupId=''
      this.getTreeList()
      this.fetchData()
    },

    clearDialog(){
      const that = this
      that.groupTitle = ''
      that.dialogVisible = false
    },

    /*获取左侧树形列表*/
    async getTreeList() {
      const that = this
      try {
        let url = this.Interface.groupInfo.getTree
        that.loadTree = true
        let res = await this.request.dataGet(that, url, {type:2})
        that.loadTree = false
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

    getNodeInfo(data){
      const that = this
      that.ruleForm.parentId = data.id
      that.SearchItem.groupId = data.id
      that.groupId = data.id
      that.groupName = data.name
      that.showTree = [data.id]
      this.fetchData()
    },

    insertGroup(){
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

    submitForm(ruleForm) {
      const that = this
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = that.ruleForm
          // 新增
          if (that.groupTitle === '重命名'){
            data.id = that.groupId
            that.editSave(data)
          }else {
            that.addSave(data)
          }
        }
      });
    },
    async addSave (data) {
      const that = this
      let url = that.Interface.groupInfo.insert
      that.loadTree = true
      const response = await that.request.dataPost(that, url, data)
      that.loadTree = false
      if (response.data.code === 1) {
        this.$message.success('添加成功');
        this.resetForm('ruleForm')
        this.getTreeList()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async editSave (data) {
      const that = this
      let url = that.Interface.groupInfo.update
      that.loadTree = true
      const response = await that.request.dataPut(that, url, data)
      that.loadTree = false
      if (response.data.code === 1) {
        this.$message.success('重命名成功');
        this.resetForm('ruleForm')
        this.getTreeList()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    updateGroupName(){
      const that = this
      let parentId = that.ruleForm.parentId;
      if (!parentId){
        this.$message.warning('请选择分组')
      }else {
        that.groupTitle = "重命名"
        that.ruleForm.name = that.groupName
        that.dialogVisible = true
      }

    },

    deleteGroup(){
      const that = this
      let parentId = that.ruleForm.parentId;
      if (!parentId){
        this.$message.warning('请选择分组')
      }else {
        that.$confirm('请确认是否删除?', '提示', {
          type: 'warning'
        }).then(async () => {
          let url = this.Interface.groupInfo.delete
          that.loadTree = true
          let response = await this.request.dataDelete(that, url, {id: parentId})
          that.loadTree = false
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
.nameSpan{
  position: relative;
  left: 4px;
  display:inline-block;
  word-break:keep-all;
  white-space:nowrap;
  overflow:hidden;
  text-overflow:ellipsis;
  -o-text-overflow:ellipsis;
  -icab-text-overflow:ellipsis;
  -khtml-text-overflow:ellipsis;
  -moz-text-overflow: ellipsis;
  -webkit-text-overflow:ellipsis;
  width: 100px;
}
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #EBEEF5 !important;
  color: #606266;
}
</style>
