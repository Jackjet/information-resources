<template>
  <el-main class="main">
     <div style="margin-left: 260px"> <h4>{{title}}</h4></div>
     <el-col class="main-center" style="margin-left: 260px">
      <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
     >
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
        <el-select v-model="ruleForm.type" clearable placeholder="请选择数据源类型">
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
            clearable
            maxlength = '100'
            size="medium"
            placeholder="请输入密码"
            v-model="ruleForm.password"
        ></el-input>
      </el-form-item>
        <el-form-item label="标签:" :label-width="this.formLabelWidth">
          <div v-for="(item,i) in metas">
              <el-select
                  size="small"
                  v-model="item.key"
                  filterable
                  remote
                  reserve-keyword
                  placeholder="请输入标签名"
                  :remote-method="remoteMethod"
                  :loading="loading">
                <el-option
                    v-for="item in tagInfos"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            <el-tag type="info" style="margin-left: 3px;">
              <input style="border: #ECF5FF;background-color: #F4F4F5;outline: none;width: 140px;" v-model="item.value"/>
            </el-tag>
            <el-button type="small" icon="el-icon-plus" style="margin-top: 4px;margin-left: 3px;" @click="insertMeta" v-if="i === 0"></el-button>
            <el-button type="small" icon="el-icon-delete" style="margin-top: 4px;margin-left: 3px;" @click="removeMeta(i)" v-if="i !== 0"></el-button>
          </div>
        </el-form-item>
        <el-form-item label="分组:" :label-width="this.formLabelWidth">
          <el-cascader
              expandTrigger="hover"
              v-model="ruleForm.groupId"
              :options="groupOptions"
              :props="{ checkStrictly: true,emitPath:false }"
              clearable>
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
       <div class="demo-drawer__footer drawer_footer">
        <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
         <el-button size="medium" @click="testConnection('ruleForm')">连接测试</el-button>
        <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {findById,add,update,testConnectionButton} from "@/api/dataResource.js";
import {findList} from "@/api/tagInfo.js";
import {getGroupList} from "@/api/groupInfo.js"

export default {
  components: {},
  data() {
    return {
      title: '添加',
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
      metas:[{key:'',value:''}],
      tagInfos:[],
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
      let data = { type: '2' }
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

    // 获取列表
    async fetchData() {
      const that = this
      let data = { id: that.id }
      try {
        that.isSubmitLoading = true
        const res = await findById(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let content = res.data.data
          this.ruleForm = content
          // integrate tag info
          let tempName = content.tagName
          let tempValue = content.tagValue
          if (tempName.indexOf(',') === -1){
            this.metas = [{key:tempName,value:tempValue}]
          }else {
            let metas = []
            let nameArr = tempName.split(',')
            let valueArr = tempValue.split(',')
            nameArr.forEach((item,index)=>{
              metas.push({key:item,value:valueArr[index]})
            })
            this.metas = metas
          }
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    submitForm(ruleForm) {
      const that = this
      let metaInfo = this.metas
      for (let ms of metaInfo) {
        if (ms.key === ''){
          that.$message.error('请选择标签')
          return false
        }
        if(ms.value === '') {
          that.$message.error('请输入标签值')
          return false
        }
      }
      let tempName = ''
      let tempValue = ''
      for (let te of metaInfo) {
        tempName = tempName + te.key + ','
        tempValue = tempValue + te.value + ','
      }
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = that.ruleForm
          data.id = that.id
          data.tagName = tempName.substr(0,tempName.length-1)
          data.tagValue = tempValue.substr(0,tempValue.length-1)
          if (this.$route.query.type === '编辑') {
            // 编辑
            update(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('编辑成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            add(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('新增成功');
                this.resetForm('ruleForm')
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        } else {
          return false;
        }
      });
    },

    testConnection(){
      const that = this
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = this.ruleForm
            testConnectionButton(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('连接成功');
                this.ruleForm.status="1"
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
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
      this.ruleForm.name = ""
      this.ruleForm.type = ""
      this.ruleForm.host = ""
      this.ruleForm.port = ""
      this.ruleForm.dataName = ""
      this.ruleForm.userName = ""
      this.ruleForm.password = ""
      this.ruleForm.detail = ""
      this.ruleForm.status = ""
      this.ruleForm.groupId = ""
      this.title = ""
      this.id = ""
      let groupId = this.groupId
      this.groupId = ""
      this.$router.push({
        path: '/dataSource',
        query: {
          groupId:groupId
        }
      })
    },

    insertMeta(){
      const that = this
      let item = {
        key:'',
        value:''
      }
      that.metas.push(item)
    },

    removeMeta(i){
      const that = this
      that.metas.splice(i,1)
    },

    async remoteMethod(query){
      if (query !== '') {
        this.loading = true;
        let res = await findList({name:query})
        if (res.data.code == 1){
          this.tagInfos = res.data.data
          this.loading = false;
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
  .main-center{
    width: 45%;
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
