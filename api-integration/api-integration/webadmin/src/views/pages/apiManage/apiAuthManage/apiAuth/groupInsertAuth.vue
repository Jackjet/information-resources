<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
     <el-col class="main-center">
      <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
      <el-form-item label="应用名称:" :label-width="this.formLabelWidth" prop='appName'>
        <el-row>
          <el-col span="20">
            <el-input
              clearable
              maxlength = '100'
              size="medium"
              placeholder="请选择应用"
              v-model="ruleForm.appNames"
            ></el-input>
          </el-col>
          <el-col span="4">
            <el-popover
              placement="right"
              width="600"
              trigger="click"
              ref="myPopover"
            >
              <el-form :inline="true" class='el-InputForm'>
                <el-form-item>
                  <el-input clearable
                            size="medium"
                            placeholder="请输入应用ID"
                            v-model="searchItem.id">
                  </el-input>
                </el-form-item>
                <el-form-item>
                  <el-input clearable
                            size="medium"
                            placeholder="请输入应用名称"
                            v-model="searchItem.appName">
                  </el-input>
                </el-form-item>
                <el-form-item style='margin-left: 1%;'>
                  <el-button size='medium' @click="getAppList" icon="el-icon-search">查询</el-button>
                  <el-button size='medium' @click="getAppIds" icon="el-icon-check">选择</el-button>
                </el-form-item>
              </el-form>
              <template>
                <el-table
                    :data="tableData"
                    :row-key="getRowKey"
                    style="width: 100%"
                    @selection-change="handleSelectionChange"
                >
                  <el-table-column type="selection" width="50" align="center" :reserve-selection="true">
                  </el-table-column>
                  <el-table-column
                      prop="id"
                      label="应用ID"
                      width="260">
                  </el-table-column>
                  <el-table-column
                      prop="appName"
                      label="应用名称"
                      width="200">
                  </el-table-column>
                  <el-table-column
                      prop="userName"
                      label="创建人"
                      width="90">
                  </el-table-column>
                </el-table>
              </template>
              <el-col :span='24'>
                <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
              </el-col>
              <el-button slot="reference" data-toggle="popover" @click="getAppList" style="height: 35px;width: 100%;background:#f0f7fa">...</el-button>
            </el-popover>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item label="请选择限制:" :label-width="this.formLabelWidth">
        <el-select v-model="ruleForm.listType" clearable placeholder="请选择限制" @change="clearTextarea">
          <el-option
            key='0'
            label="黑名单"
            value='0'>
          </el-option>
          <el-option
            key='1'
            label="白名单"
            value='1'>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="ruleForm.listType === '1'" label="白名单:" :label-width="this.formLabelWidth">
        <el-input maxlength = '200' type="textarea"  placeholder="请输入应用访问白名单（IP地址，多个请以‘,’隔开）" clearable size="medium" v-model="ruleForm.listContent"></el-input>
      </el-form-item>
      <el-form-item v-if="ruleForm.listType === '0'" label="黑名单:" :label-width="this.formLabelWidth">
        <el-input maxlength = '200' type="textarea"  placeholder="请输入应用访问黑名单（IP地址，多个请以‘,’隔开）" clearable size="medium" v-model="ruleForm.listContent"></el-input>
      </el-form-item>
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
        <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {appList, getAuthNum, insertAuth} from "@/api/apiAuthManage";
import {deleteApi} from "@/api/apiManage";
import pagination from "@/components/table/PaginationFor5";

export default {
  components: {pagination },
  data() {
    return {
      title: '授权应用',
      props: { checkStrictly: true },
      ruleForm: {
        appNames:'',
        container:'',
        appIdList:[],
        apiIdList:[],
        listType: '',
        listContent: ''
      },
      organizationOptions: [],
      options: [],
      tableData:[],
      formLabelWidth: "120px",
      rules: {
        appNames: [{
          required: true,
          message: "请选择应用",
          trigger: "change",
        }],
        listType: [{
          required: true,
          message: "请选择限制",
          trigger: "change",
        }],
        listContent: [{
          required: true,
          message: "请填写限制内容",
          trigger: "change",
        }]
      },
      searchItem:{
        id:'',
        appName:''
      },
      lastItem: {
        id:'',
        appName: ''
      },
      total: 0,
      pageSize: '5',
      currentPage: '1',
      groupId:'',
      groupName:'',


      //多选框
      deletelistiD: [],
    }
  },
  created () {
    this.groupId = this.$route.query.groupId
    this.groupName = this.$route.query.groupName
    this.title = this.$route.query.type
    this.ruleForm.apiIdList = this.$route.query.ids
    this.ruleForm.container = this.$route.query.container
    console.log('this.ruleForm.container',this.ruleForm.container)
  },
  methods: {

    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          const that = this
          let data = {
            container:that.ruleForm.container,
            appIdList:that.ruleForm.appIdList,
            apiIdList:that.ruleForm.apiIdList,
            listType: that.ruleForm.listType,
            listContent: that.ruleForm.listContent
          }

          let appIds = '';
          data.appIdList.forEach((item,i) =>{
            appIds+= item
            appIds+= ','
          })
          let param = {
            appIds:appIds.substring(0,appIds.length-1)
          }

          getAuthNum(param).then((res) =>{
            let appNums = res.data.data
            if (!appNums){
              insertAuth(data).then((res) => {
                if (res.data.code === 1) {
                  that.$confirm('授权信息提交成功','提示', {
                    type: 'success'
                  }).then(async () => {
                    this.resetForm('ruleForm')
                    this.loading = false;
                  }).catch(() => {
                    return false
                  })
                } else {
                  this.$message.error(res.data.msg);
                }
              });
            }else {
              that.$confirm('继续授权将会覆盖原授权信息,请确认是否继续','提示', {
                type: 'warning'
              }).then(async () => {
                insertAuth(data).then((res) => {
                  if (res.data.code === 1) {
                    that.$confirm('授权信息提交成功','提示', {
                      type: 'success'
                    }).then(async () => {
                      this.resetForm('ruleForm')
                      this.loading = false;
                    }).catch(() => {
                      return false
                    })
                  } else {
                    this.$message.error(res.data.msg);
                  }
                });
              }).catch(() => {
                return false
              })
            }
          }).catch(()=>{
            return false
          })
        } else {
          return false;
        }
      });
    },

    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      const that = this
      let tempGroupId = that.groupId
      let tempGroupName = that.groupName
      this.ruleForm.appId = "";
      this.ruleForm.appName = "";
      this.ruleForm.apiIdList = [];
      this.ruleForm.listType = "";
      this.ruleForm.listContent = "";
      this.groupId = "";
      this.groupName = "";
      this.$router.push({
        path: '/apiManage/apiAuth/groupApiList',
        query: {
          id:tempGroupId,
          name:tempGroupName
        }
      })
    },

    getAppId(row){
      const that = this
      that.ruleForm.appName = row.appName
      that.ruleForm.appId = row.id
      this.$refs.myPopover.doClose()
    },

    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.searchItem).map((item, index) => {
        that.searchItem[item[0]] = that.lastItem[item[0]]
      })
      this.getAppList('page')
    },

    async getAppList(type) {
      const that = this
      let data = that.searchItem
      data.page = that.currentPage
      data.size = that.pageSize
      Object.entries(that.searchItem).map((item, index) => {
        that.lastItem[item[0]] = that.searchItem[item[0]]
      })
      try {
        const res = await appList(data)
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

    clearTextarea(){
      const that = this
      that.ruleForm.listContent = ''
    },

    //全选
    handleSelectionChange(vals) {
      const that = this
      that.deletelistiD = []

      that.appNames = ''
      vals.map(function(v, k){
        that.deletelistiD.push(v.id)

        if(that.appNames !== ''){
          that.appNames +=','
        }
        console.log('v',v)
        that.appNames +=v.appName
      })
    },

    //选择app
    getAppIds(){
      const that = this
      that.ruleForm.appIdList = that.deletelistiD
      that.ruleForm.appNames = that.appNames
      this.$refs.myPopover.doClose()
    },
    getRowKey(row){
      return row.id
    }
  }
}
</script>
