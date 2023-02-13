<template>
  <el-main>
    <el-col class="main-center">
      <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm">
      <el-form-item label="容器名称:" :label-width="this.formLabelWidth" prop="name">
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          placeholder="请输入容器名称"
          v-model="ruleForm.name"
        ></el-input>
      </el-form-item>
      <el-form-item label="容器类型:" :label-width="this.formLabelWidth" prop="type">
        <el-select v-model="ruleForm.type" placeholder="请选择容器类型" style="width: 100%;">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="容器IP:" :label-width="this.formLabelWidth" prop="ip">
        <el-input
            clearable
            maxlength = '100'
            size="medium"
            placeholder="请输入IP地址"
            v-model="ruleForm.ip"
        ></el-input>
      </el-form-item>
      <el-form-item label="容器端口号:" :label-width="this.formLabelWidth" prop="port">
        <el-input
            clearable
            maxlength = '100'
            size="medium"
            placeholder="请输入端口号"
            v-model="ruleForm.port"
        ></el-input>
      </el-form-item>
      <el-form-item label="分组:" :label-width="this.formLabelWidth">
          <el-cascader
              expandTrigger="hover"
              v-model="ruleForm.groupId"
              :options="groupOptions"
              :props="{ checkStrictly: true,emitPath:false }"
              clearable style="width: 100%;">
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
         <el-button size="medium" @click="testConnection">连接测试</el-button>
        <el-button size="medium" type="primary" @click="submitForm('ruleForm')">保 存</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>
</template>

<script>
// import {add} from "@/api/containerInfo.js";
// import {withoutIpRequest} from "@/api/withoutIpRequest.js";
// import {getGroupList} from "@/api/groupInfo.js"

export default {
  data() {
    return {
      ruleForm: {
        name:'',
        ip:'',
        port:'',
        detail:''
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "请填写容器名称",
          trigger: "change"
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请填写容器名称'
        }],
        type: [{
          required: true,
          message: "请选中容器名称",
          trigger: "change"
        }],
        ip: [{
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
      },
      groupId:'',
      status:0,
      options: [{
        value: '0',
        label: 'API运行'
      }, {
        value: '1',
        label: '数据'
      }, {
        value: '2',
        label: '消息'
      }, {
        value: '3',
        label: 'API网关'
      }, {
        value: '4',
        label: '其他'
      }],
      groupOptions:[]
    }
  },
  created () {
    this.groupId = this.$route.query.groupId
    this.getGroupList()
  },
  methods: {
    // 获取列表
    async getGroupList() {
      const that = this
      try {
        let url = this.Interface.groupInfo.getList
        let res = await this.request.dataGet(that, url, { type: '1' })
        if (res.data.code === 1) {
          that.groupOptions = res.data.data
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
          if (!that.ruleForm.groupId){
            data.groupId = that.groupId
          }
          data.status = that.status
          this.addSave(data)
        } else {
          return false;
        }
      });
    },
    async addSave (data) {
      const that = this
      let url = that.Interface.container.insert
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

    goBack() {
      this.$router.push({
        path: '/index/container',
        query: {
          groupId: this.groupId
        }
      })
    },

    async testConnection(){
      let data = this.ruleForm
      let ip = data.ip
      if (!ip){
        this.$message.error('请填写IP地址');
        return
      }
      let port = data.port
      if(!port){
        this.$message.error('请填写端口号');
        return
      }
      let type = data.type
      if(!type){
        this.$message.error('请选择容器类型');
        return
      }
      let baseUrl = 'http://'+ip+':'+port
      let url = ''
      if (data.type !== 3) {
        url = '/api/container/status'
      }
      try {
        let res = await that.request.dataGet(that, (baseUrl + url), {})
        if (res.status === 200) {
          this.status = 1
          this.$message.success('连接成功');
        } else {
          this.$message.error('连接失败');
        }
      } catch (e) {
        this.$message.error('连接失败');
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.main-center{
  width: 45%;
  margin: 10px auto;
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
