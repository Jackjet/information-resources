<template>
  <el-main class="main">
     <div><h4>数据备份</h4></div> 
     <el-col class="main-center">
      <el-form v-loading="loading" :model="baseForm" :rules="rules" ref="baseForm" label-width="150px">
    <el-row class="top">
      <el-form-item label="数据库类型:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabled"
          placeholder="请输入数据库类型"
          v-model="baseForms.type"
        ></el-input>
      </el-form-item>
      <el-form-item label="数据库名称:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabled"
          placeholder="请输入数据库名称"
          v-model="baseForms.name"
        ></el-input>
      </el-form-item>
       <el-form-item label="备份数据大小:" :label-width="this.formLabelWidth" prop='name'>
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabled"
          placeholder="请输入备份数据大小"
          v-model="baseForms.size"
        ></el-input>
      </el-form-item>
       <el-form-item label="执行周期:" :label-width="this.formLabelWidth" prop='cronType'>
         <el-select v-model="type" clearable  :disabled="disabledo"  @change="changeTime" placeholder="请选择执行周期">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>            
          </el-select>
       </el-form-item>
       <el-form-item style="dispaly: flex; " prop='hour' label="请填写相应周期:" :label-width="this.formLabelWidth" v-if="baseForm.cronType === '0'">
         <el-col :span="8">
          <el-form-item prop='hour'><el-input-number  v-model="baseForm.hour" :disabled="disabledo"  controls-position="right"  :min="0" :max="23" ></el-input-number><span style="padding: 10px">时</span></el-form-item>
         </el-col>
         <el-col :span="8">
          <el-form-item prop='minute' ><el-input-number  v-model="baseForm.minute" :disabled="disabledo"   controls-position="right" :min="0" :max="60"></el-input-number><span style="padding: 10px">分</span></el-form-item>
          </el-col>
       </el-form-item> 
        <el-form-item label="请填写相应周期:" prop='cronValue' :label-width="this.formLabelWidth" v-if="baseForm.cronType === '1'">
          <el-col :span="6">
           <el-form-item >
            <el-select v-model="baseForm.cronValue" clearable style="width: 120px;" :disabled="disabledo"  placeholder="请选择周">
              <el-option    
                v-for="item in weekOptions"
                :key="item.value" 
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select><span style="padding: 10px 5px;">周</span>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item prop='hour'>
              <el-input-number v-model="baseForm.hour" controls-position="right" :disabled="disabledo"   :min="0" :max="23" ></el-input-number><span style="padding: 10px;">时</span>
            </el-form-item>
          </el-col>
          <el-col :span="9">
           <el-form-item prop='minute'>
            <el-input-number v-model="baseForm.minute"  controls-position="right" :disabled="disabledo"  :min="0" :max="60"></el-input-number><span style="padding: 10px;">分</span>
           </el-form-item>
          </el-col>
       </el-form-item>  
        <el-form-item label="请填写相应周期:" prop='cronValue' :label-width="this.formLabelWidth" v-if="baseForm.cronType === '2'">
          <el-col :span="8">
            <el-form-item prop='cronValue'>
              <el-input-number :disabled="disabledo"  v-model="baseForm.cronValue" controls-position="right" :min="1" :max="31"></el-input-number><span style="padding: 10px;">日</span>
            </el-form-item >  
          </el-col>  
          <el-col :span="8">
            <el-form-item prop='hour'>
              <el-input-number :disabled="disabledo"  v-model="baseForm.hour" controls-position="right"  :min="0" :max="23"></el-input-number><span style="padding: 10px;">时</span>
            </el-form-item >  
          </el-col>
          <el-col :span="8">
            <el-form-item prop='minute'>
              <el-input-number :disabled="disabledo"  v-model="baseForm.minute"  controls-position="right" :min="0" :max="60"></el-input-number> <span style="padding: 10px;">分</span>
            </el-form-item >    
          </el-col>
       </el-form-item>    
       <el-form-item label="备份路径:"  prop='path' :label-width="this.formLabelWidth">
        <el-input
          clearable
          maxlength = '100'
          size="medium"
          :disabled="disabledo"
          placeholder="请输入英文和数字类型的备份路径例如/root/backup/"
          v-model="baseForm.path"
        ></el-input>
      </el-form-item>
      <el-form-item label="自动备份开关:" :label-width="this.formLabelWidth"  prop='enable' class="center">
        <el-radio-group v-model="baseForm.enable">
          <el-radio key="1" :disabled="disabledo" :label="1">启动备份</el-radio>
          <el-radio key="2" :disabled="disabledo" :label="0">停止备份</el-radio>
        </el-radio-group>
      </el-form-item>
       <div  v-if="!isActive" style="text-align: center;">
        <el-button size="medium" @click="cancal('baseForm')">取 消</el-button>
        <el-button size="medium" type="primary"  @click="submitForm('baseForm')">保 存</el-button>
      </div>
       <div v-if="isActive" style="text-align: center;">
        <el-button size="medium" @click="edit">编辑</el-button>
      </div>
    </el-row>
    </el-form>
    </el-col>
  </el-main>
</template>
<script>

import { findDatabaseInfo, timedTaskInsert, timedTask } from "@/api/info.js"
export default {
  components: { },
  data() {
    return {
      title: '新增',
      disabled: true,
      disabledo: true,
      loading: false,
      isActive: true,
      props: { checkStrictly: true },
      ruleForm: {
        osName: "",
        total: '',
        used: "",
        memTotal: '',
        memRam: "",
        combined: ''
      },
      baseForms: {
        name: "",
        size: '',
        type: "",
      },
      baseForm: {
        cronType: '',
        cronValue: '',
        hour: '',
        minute: '',
        enable: 0,
        path: ''
      },
      type:'',
      organizationOptions: [],
      weekOptions: [{
        value: 'MON',
        label: '周一'
      }, {
        value: 'TUE',
        label: '周二'
      },{
        value: 'WED',
        label: '周三'
      },{
        value: 'THU',
        label: '周四'
      }, {
        value: 'FRI',
        label: '周五'
      },{
        value: 'SAT',
        label: '周六'
      },{
        value: 'SUN',
        label: '周日'
      }],
      options: [{
        value: '0',
        label: '每天'
      }, {
        value: '1',
        label: '每周'
      },{
        value: '2',
        label: '每月'
      }],
      formLabelWidth: "140px",
      rules: {
        path: [{
          required: true,
          message: "请输入备份路径",
          trigger: ['change', 'blur']
        }, {
          pattern: /^\/(?:[^/]+\/)*$/, //正则
          message: '路径格式不正确'
        }],
        cronType: [{ 
          required: true,
          message: '请选择执行周期',
          trigger:  ['change', 'blur'],
        }],
        enable: [{ 
          required: true,
          message: '请选择自动备份开关',
          trigger:  ['change', 'blur']
        }],
        hour: [{ 
          required: true,
          message: '请输入时',
          trigger:  ['change', 'blur']
        }],
        minute: [{ 
          required: true,
          message: '请输入分',
          trigger:  ['change', 'blur']
        }],
        cronValue: [{ 
          required: true,
          message: '请选择值',
          trigger:  ['change', 'blur']
        }],
      },
    }
  },
  created () {
    this.dataBaseDetail()
    this.editInfo()
  },
  methods: {
    //编辑
    edit() {
      const that = this
      that.isActive = false
      that.disabledo = false
    },
    cancal(baseForm) {
      const that = this
      this.$refs["baseForm"].resetFields();
      that.isActive = true
      that.disabledo = true
      that.editInfo()
    },
    // 数据库详情
    async dataBaseDetail() {
      const that = this
      let data = {}
      that.loading = true
      const response = await findDatabaseInfo(data)
      that.loading = false
      if (response.data.code === 1) {
        let datas =  response.data.data
        that.baseForms = datas
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    changeTime() {
      let that = this
      that.baseForm.cronValue = '' 
      that.baseForm.hour = ''
      that.baseForm.minute = ''
      that.baseForm.cronType = that.type
    },
    // 服务器详情
    async editInfo() {
      const that = this
      let data = {
        id: 'databaseBackup'
      }
      that.loading = true
      const response = await timedTask(data)
      that.loading = false
      if (response.data.code === 1) {
        let datas = '', cron = ''
        datas =  response.data.data
        if(datas.cron === ''){ return false}
        cron = that.cronChangeDate(datas.cron)
        that.type = cron.cronType
        if(datas.enable !== ''){
          datas.enable = datas.enable === true ? 1 : 0
          datas.cronType = cron.cronType
          switch(cron.cronType){
            case '0':  datas.cronValue= '';datas.hour = cron.hour; datas.minute = cron.minute; break;
            case '1':  datas.cronValue = cron.cronValue; datas.hour = cron.hour; datas.minute = cron.minute; break;
            case '2':  datas.cronValue = cron.cronValue; datas.hour = cron.hour; datas.minute = cron.minute; break;
          }
        }  
        that.baseForm = datas
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    // 解析cron
    cronChangeDate (cron) {
      let data = {
        minute: '',
        hour: '',
        cronValue: '',
        cronType: '',
        second: '',
        day: '',
        month: '',
        week: ''
      }
      let time = cron.split(' ')
      data.second = time[0]
      data.minute = time[1]
      data.hour = time[2]
      data.day = time[3]
      data.week = time[4]
      data.month = time[5]
      if(data.day === "*" && data.week === "*" && data.month === "?"){
        data.cronType = '0'
        data.cronValue = ''
      }
      else if(data.day === "?" && data.week === "*") {
        data.cronValue = data.month
        data.cronType = '1'
      }
      else if(data.week ===  "*" && data.month === "?") {
        data.cronValue = data.day
        data.cronType = '2'
      }
      return data
    },
    // 提交
    submitForm(baseForm) {
      const that = this
      this.$refs["baseForm"].validate((valid) => {
        if (valid) {
          let data = {
            cron: that.exchangeCron(),
            enable: that.baseForm.enable,
            path: that.baseForm.path,
          }
          that.loading = true
          timedTaskInsert(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('编辑成功');
              that.cancal()
            } else {
              this.$message.error(res.data.msg);
            }
            this.loading = false;
          });
        } else {
          return false;
        }
      })
    },
    // 转换cron
    exchangeCron (){
      const that = this
      let cron = "0 " + that.baseForm.minute + " " + that.baseForm.hour;
      /*类型：0->天 1->周 2->月*/
      switch (that.baseForm.cronType) {
        case '0':
          cron = cron + " * * ?";
          break;
        case '1':
          cron = cron + " ? * " +that.baseForm.cronValue;
          break;
        case '2':
          cron = cron + " " + that.baseForm.cronValue + " * ?";
          break;
      }
      return cron;
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.disabled = true;
      this.disabledo = true
      this.isActive = true
      this.ruleForm.name = "";
      this.ruleForm.remark = "";
      this.$router.push({ 
        path: '/systemManage/role/roles'
      })
    }
  }
}
</script>
<style lang="scss" scoped>
 .main{
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;
  .main-center{
    padding: 20px;
    border-radius: 5px;
    width: 80%;
    margin: 0 auto;
  }
 }
</style>