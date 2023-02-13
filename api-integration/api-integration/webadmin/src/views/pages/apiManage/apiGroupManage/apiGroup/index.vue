<template>
  <el-main class="main">
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable
            size="medium"
            placeholder="请输入组名称"
            v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-input clearable
                    size="medium"
                    placeholder="请输入创建人名称"
                    v-model="SearchItem.userName">
          </el-input>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' icon="el-icon-plus" @click="dialogVisible = true">新增</el-button>
          <el-dialog title="添加API组" :visible.sync="dialogVisible" :before-close="clearDialog">
            <el-form :model="ruleForm"
                     :rules="rules"
                     ref="ruleForm"
                     label-width="80px"
                     class="demo-ruleForm"
            >
              <el-row>
                <el-form-item label="组名称:" prop='name'>
                  <el-input
                    clearable
                    size="medium"
                    placeholder="请输入组名称"
                    v-model="ruleForm.name"
                    style="width: 400px"
                  ></el-input>
                </el-form-item>
              </el-row>
              <el-row>
                <el-form-item label="描述:">
                  <el-input
                    clearable
                    size="medium"
                    :rows="4"
                    type="textarea"
                    placeholder="请输入描述"
                    v-model="ruleForm.detail"
                    style="width: 400px"
                  ></el-input>
                </el-form-item>
              </el-row>
            </el-form>
            <div style="margin-top: 20px;position: relative;left:38%">
              <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
              <el-button size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
            </div>
          </el-dialog>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data='tableData'
        v-loading="isSubmitLoading"
        :table-selection="tableSelection"
        :table-label="tableHeader"
        :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <el-dialog title="编辑API组" :visible.sync="dialogVisible01" :before-close="clearDialog">
      <el-form :model="ruleForm"
               :rules="rules"
               ref="ruleForm"
               label-width="80px"
               class="demo-ruleForm"
      >
        <el-row>
          <el-form-item label="组名称:" prop='name'>
            <el-input
              clearable
              size="medium"
              placeholder="请输入组名称"
              v-model="ruleForm.name"
              style="width: 400px"
            ></el-input>
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="描述:">
            <el-input
              clearable
              size="medium"
              :rows="4"
              type="textarea"
              placeholder="请输入描述"
              v-model="ruleForm.detail"
              style="width: 400px"
            ></el-input>
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
import {
  findAll,deleteGroup,add,findById,update
} from "@/api/apiGroupManage"
import { mapGetters } from 'vuex'
import TableList from '@/components/table/tableListApi.vue'
import Pagination from '@/components/table/Pagination.vue'
export default {
  components: { TableList, Pagination },
  computed: {
    ...mapGetters([
      'permissions'
    ]),
  },
  data() {
    return {
      drawer: false,
      tableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      exportApiDoc: ['none'],
      insertGroup: ['none'],
      isSubmitLoading: false,
      DeletelistiD: [],
      tableData: [],
      tableHeader: [
        {id: false, type: 'color', label: '组名称', list: 'name'},
        {id: false, type: 'color', label: '组描述', list: 'detail'},
        {id: false, type: 'color', label: 'API数量', list: 'num'},
        {id: false, type: 'color', label: '创建人', list: 'userName'},
        {id: false, type: 'color', label: '更新时间', list: 'updateTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '编辑',
          key: 0,
          type: 'text',
          icon: 'el-icon-edit',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.edit(row)
          }
        }, {
          label: '删除',
          key: 0,
          type: 'text',
          icon: 'el-icon-delete',
          State: false,
          permission: ['none'],
          method: (row) => {
            this.deleteGroup(row)
          }
        },{
          label: 'API列表',
          key: 0,
          type: 'text',
          icon: 'el-icon-document',
          permission: ['none'],
          State: false,
          method: (row) => {
            this.groupApiList(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        name: '',
        userName: ''
      },
      lastItem: {
        name: '',
        userName: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      formLabelWidth:"120px",
      ruleForm:{
        name:'',
        detail:''
      },
      rules: {
        name: [{
          required: true,
          message: "请输入组名称",
          trigger: "change"
        }],
        detail: [{
          required: true,
          message: '请输入描述',
          trigger: "change"
        }]
      },
      dialogVisible:false,
      dialogVisible01:false,
      groupId:''
    }
  },
  created(){
    this.findPermission()
    this.fetchData()
  },
  methods: {
    // 查询按钮权限
    findPermission() {
      const that = this
      let props = {
        edit: function (value) {
          that.tableOpction.options[0].permission = JSON.parse(JSON.stringify(value))
        },
        deleteGroup: function (value) {
          that.tableOpction.options[1].permission = JSON.parse(JSON.stringify(value))
        },
        groupApiList: function (value) {
          that.tableOpction.options[2].permission = JSON.parse(JSON.stringify(value))
        }
      }
      if(this.permissions.length > 0){
        this.permissions.map(function(v, k){
          if(v['apiGroup']){
            for (let i in v['apiGroup']) {
              if(props.hasOwnProperty(i)){ props[i](v['apiGroup'][i])}
            }
          }
        })
      }
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
        if(res.data.code === 1){
          this.tableData = res.data.data.content
          this.total =  res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
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
      if(that.lastItem.startTime === '' || that.lastItem.startTime === null){
        that.time = []
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },

    // 行高
    cellStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    // 头行高
    headerStyle () {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    // 删除分组
    async deleteGroup(row) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await deleteGroup(row.id)
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

    //编辑
    async edit(data) {
      const that = this
      that.groupId = data.id
      let params = {id: data.id}
      const response = await findById(params)
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.dialogVisible01 = true
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    groupApiList(data){
      const that = this
      that.$router.push({
        path: '/apiManage/apiGroup/apiList',
        query: {
          id: data.id,
        }
      })
    },

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let data = that.ruleForm
          // 新增
          let group = that.groupId
          if (group){
            update(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
                this.fetchData()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }else {
            add(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
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

    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.ruleForm.name = "";
      this.ruleForm.detail = "";
      this.dialogVisible = false
      this.dialogVisible01 = false
    },

    clearDialog(done){
      console.log("关闭弹框")
      const that = this
      that.ruleForm.name = ''
      that.ruleForm.detail = ''
      done();
    }
  }
}
</script>
<style lang="scss">
  /deep/ .el-button--primary.is-plain:focus, .el-button--primary.is-plain:hover{
    background: #ffffffff
  }
  /deep/ .el-form-item__label{
    color: #fff;
  }
  /deep/ .el-input--medium .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-date-editor .el-range__close-icon{
    line-height: 24px !important;
  }
  /deep/ .el-input__inner{
    height: 30px;
    line-height: 30px;
  }
  /deep/ .el-button--medium{
    padding: 7px 15px;
  }
  /deep/ .el-form-item{
    margin-bottom: 5px;
  }
  /deep/ .el-InputForm .el-range__icon{
    line-height: 25px!important;
  }
  /deep/ .el-input__icon .el-range__icon {
    line-height: 24px!important;
  }
 /deep/ .el-date-editor .el-range-separator{
   line-height: 24px !important;
 }
  .center{
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }
  .main{
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
    margin-top: -240px;
    .el-InputForm{
      background:url('../../../../assets/image/filter.png');
      background-size: 100% 100%;
      padding:15px 0 0 20px;
      min-height: 100px;
    }
  }
  .add{
    width: 25px;
    height: 25px;
    background:url('../../../../assets/image/add.png');
  }
  .work-query {
    display: flex;
    align-items: center;
    padding: 20px;
  }
  .table-list {
    padding: 20px 20px 0 20px;
    box-sizing: border-box;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 400px;
  }
  .table-pagination {
    display: flex;
    justify-content: flex-end;
    padding: 20px;
  }

</style>
