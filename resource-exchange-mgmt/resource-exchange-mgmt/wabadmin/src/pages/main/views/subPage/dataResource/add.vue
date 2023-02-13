<template>
  <el-main>
    <el-col class="main-center">
      <el-form
        v-loading="isSubmitLoading"
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="150px">
        <el-form-item label="数据源名称:" :label-width="this.formLabelWidth" prop="name">
          <el-input
            clearable
            maxlength = '100'
            size="medium"
            placeholder="请输入数据源名称"
            v-model="ruleForm.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据源类型:" :label-width="this.formLabelWidth" prop="type">
          <el-select v-model="ruleForm.type" clearable placeholder="请选择数据源类型" style="width: 100%;">
            <el-option
              key='MySQL'
              label="MySQL"
              value='MySQL'>
            </el-option>
            <el-option
              key='PostgreSQL'
              label="PostgreSQL"
              value='PostgreSQL'>
            </el-option>
            <el-option
                key='Oracle'
                label="Oracle"
                value='Oracle'>
            </el-option>
            <el-option
                key='SQLserver'
                label="SQLserver"
                value='SQLserver'>
            </el-option>
            <el-option
                key='dameng'
                label="dameng"
                value='dameng'>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据库IP:" :label-width="this.formLabelWidth" prop="host">
          <el-input
              clearable
              maxlength = '100'
              size="medium"
              placeholder="请输入数据库IP"
              v-model="ruleForm.host"
          ></el-input>
        </el-form-item>
        <el-form-item label="端口号:" :label-width="this.formLabelWidth" prop="port">
          <el-input
              clearable
              maxlength = '100'
              size="medium"
              placeholder="请输入端口号"
              v-model="ruleForm.port"
          ></el-input>
        </el-form-item>
        <el-form-item label="数据库名称:" :label-width="this.formLabelWidth" prop="dataName">
          <el-input
              clearable
              maxlength = '100'
              size="medium"
              placeholder="请输入数据库名称"
              v-model="ruleForm.dataName"
          ></el-input>
        </el-form-item>
        <el-form-item label="用户名:" :label-width="this.formLabelWidth" prop="userName">
          <el-input
              clearable
              maxlength = '100'
              size="medium"
              placeholder="请输入用户名"
              v-model="ruleForm.userName"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码:" :label-width="this.formLabelWidth" prop="password">
          <el-input
            show-password
            maxlength = '100'
            size="medium"
            placeholder="请输入密码"
            v-model="ruleForm.password"
          ></el-input>
        </el-form-item>
        <el-form-item label="分组:" :label-width="this.formLabelWidth">
          <el-cascader
            :disabled="loadingGroup"
            expandTrigger="hover"
            v-model="ruleForm.groupId"
            :options="groupOptions"
            :props="{ checkStrictly: true,emitPath:false }"
            clearable
            style="width: 100%;">
          </el-cascader>
        </el-form-item>
        <el-form-item label="描述:" :label-width="this.formLabelWidth">
          <el-input
              type="textarea"
              :rows="4"
              clearable
              maxlength = '100'
              size="medium"
              placeholder="请输入数据源描述"
              v-model="ruleForm.detail"
          ></el-input>
        </el-form-item>
        <div class="demo-drawer__footer" style="margin-bottom: 30px;">
          <el-button size="medium" @click="goBack">取 消</el-button>
            <el-button size="medium" @click="testConnection('ruleForm')">连接测试</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
    </el-form>
    </el-col>
  </el-main>

</template>

<script>
// import {findById,add,update,testConnectionButton} from "@/api/dataResource.js";
// import {findList} from "@/api/tagInfo.js";
// import {getGroupList} from "@/api/groupInfo.js"

export default {
  components: {},
  data() {
    return {
      title: '添加',
      isSubmitLoading: false,
      loadingGroup: false,
      ruleForm: {
        name:'',
        type: '',
        host:'',
        port:'',
        dataName:'',
        userName: '',
        password: '',
        detail:'',
        status:'0',
        groupId:''
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "请填写数据源名称",
          trigger: "change"
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请填写数据源名称'
        }],
        type: [{
          required: true,
          message: "请选择数据源类型",
          trigger: ""
        }],
        host: [{
          required: true,
          message: '请输入数据库IP',
          trigger: 'change'
        },{
          pattern:  /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])+$/,  //正则
          message: '请输入正确数据库IP'
        }],
        port: [{
          required: true,
          message: '请输入端口号',
          trigger: 'change'
        },{
          pattern:  /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,  //正则
          message: '请输入正确端口号'
        }],
        dataName: [{
          required: true,
          message: "请输入数据库名称",
          trigger: "change"
        }, {
          pattern:  /^[0-9A-Za-z_]+$/, //正则
          message: '请输入数据库名称,(字母，数字，字母+数字)'
        }],
        userName: [{
          required: true,
          message: "请输入用户名",
          trigger: "change"
        }, {
          pattern:  /^[0-9A-Za-z]+$/, //正则
          message: '请输入用户名,(字母，数字，字母+数字)'
        }],
        password: [{
          required: true,
          message: "请输入密码",
          trigger: "change"
        }]
      },
      id:'',
      groupId:'',
      groupOptions:[]
    }
  },
  created () {
    this.title = this.$route.query.type
    this.ruleForm.groupId = this.$route.query.groupId
    this.groupId = this.$route.query.groupId
    if (this.title === '编辑'){
      this.id = this.$route.query.id
      this.fetchData()
    }
    this.getGroupList()

  },
  methods: {
    // 获取列表
    async getGroupList() {
      const that = this
      try {
        let url = this.Interface.groupInfo.getList
        that.loadingGroup = true
        let res = await this.request.dataGet(that, url, { type: '2' })
        if (res.data.code === 1) {
          that.loadingGroup = false
          that.groupOptions = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    // 获取列表
    async fetchData() {
      const that = this
      try {
        let url = this.Interface.dataResource.findById
        that.isSubmitLoading = true
        let res = await this.request.dataGet(that, url, { id: that.id })
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let content = res.data.data
          this.ruleForm = content
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    submitForm(ruleForm) {
      const that = this
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = that.ruleForm
          data.id = that.id
          if (this.$route.query.type === '编辑') {
            // 编辑
            this.editSave(data)
          } else {
            // 新增
            this.addSave(data)
          }
        } else {
          return false;
        }
      });
    },
    async addSave (data) {
      const that = this
      let url = that.Interface.dataResource.insert
      that.loadTree = true
      const response = await that.request.dataPost(that, url, data)
      that.loadTree = false
      if (response.data.code === 1) {
        this.$message.success('新增成功')
        this.goBack()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async editSave (data) {
      const that = this
      let url = that.Interface.dataResource.update
      that.loadTree = true
      const response = await that.request.dataPut(that, url, data)
      that.loadTree = false
      if (response.data.code === 1) {
        this.$message.success('编辑成功')
        this.goBack()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    testConnection(){
      const that = this
      this.$refs["ruleForm"].validate(async (valid) => {
        if (valid) {
          try {
            let url = this.Interface.dataResource.subToLink
            that.isSubmitLoading = true
            let res = await this.request.dataPost(that, url, that.ruleForm)
            that.isSubmitLoading = false
            if (res.data.code === 1) {
              this.$message.success('连接成功');
              this.ruleForm.status="1"
            } else {
              this.$message.error(res.data.msg)
            }
          } catch (even) {
            this.$message.error(even.msg)
          }
        } else {
          return false;
        }
      });
    },

    goBack() {
      this.$router.push({
        path: '/index/dataResource',
        query: {
          groupId: this.groupId
        }
      })
    },
  }
}
</script>
<style lang="scss" scoped>
  .main-center{
    width: 45%;
    margin: 0 auto;
    float: none;
  }
 .demo-drawer__footer{
   margin-top: 10px;
   text-align: center;
 }
  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-left: 10px;
    height: 32px;
    line-height: 30px;
    padding-top: 0;
    padding-bottom: 0;
  }
  .input-new-tag {
    width: 90px;
    margin-left: 10px;
    vertical-align: bottom;
  }
</style>
