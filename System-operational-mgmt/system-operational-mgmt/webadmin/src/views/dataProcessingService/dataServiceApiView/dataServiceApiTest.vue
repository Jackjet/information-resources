<template>
  <el-drawer
    :title="modalObjjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="CloseModal"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" ref="ruleForm" label-width="120px">
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="接口名称:">{{ruleForm.name}}</el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="服务类型:">{{ruleForm.type}}</el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="方法:" v-if="ruleForm.type!='Socket'">{{ruleForm.methodType}}</el-form-item>
      </el-col>
      <el-form-item class="InpitWidth" label="HEADER:" v-if="ruleForm.type=='HTTP'">
        <el-input clearable autocomplete="off" v-model="ruleForm.header" placeholder="请输入HEADER"></el-input>
      </el-form-item>
      <el-col :span="24">
        <el-form-item class="InpitWidth" :label="ruleForm.type!='Socket'?'URL:':'IP:'">{{ruleForm.urlOrIp}}</el-form-item>
      </el-col>
      <el-form-item class="InpitWidth" label="URL参数:" v-if="ruleForm.type!='Socket'">
        <el-input clearable autocomplete="off" v-model="ruleForm.urlParameter" placeholder="请输入URL参数"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" :label="ruleForm.type!='Socket'?'请求参数:':'发送数据:'">
        <el-input type="textarea" :placeholder="ruleForm.placeholder" v-model="ruleForm.body"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="返回数据:">
        <el-input type="textarea" disabled v-model="msg"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick">发 送</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { dataApiTest, dataApiFindById } from '@/api/dataProcessingService/http'
export default {
  props: {
    modalObjjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      id: '',
      isShow: true,
      msg: '',
      loading: false,
      ruleForm: {
        header: '',
        urlParameter: '',
        body: ''
      },
      dialogFormVisible: this.show
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    initial (value) {
      this.dialogFormVisible = true
      this.id = value
      if (value) {
        this.details()
      }
    },
    async details () {
      const res = await dataApiFindById({ id: this.id })
      let Data = res.data.data
      if (Data.type === 'http') {
        Data.type = 'HTTP'
        Data.placeholder = '请输入请求参数'
      }
      if (Data.type === 'webservice') {
        Data.type = 'WebService'
        Data.placeholder = '参数类型^参数（参数类型：int,long,float,double,string;多参数时，参数以英文“,”分隔。例：“int^8,9”）'
      }
      if (Data.type === 'socket') {
        Data.type = 'Socket'
        Data.placeholder = '请输入发送数据'
      }
      this.ruleForm = Data
    },

    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.ruleForm = {
        header: '',
        urlParameter: '',
        body: ''
      }
      this.msg = ''
    },

    async SuretoAddClick () {
      let data = {
        id: this.id,
        header: this.ruleForm.header,
        urlParameter: this.ruleForm.urlParameter,
        body: this.ruleForm.body
      }
      this.loading = true
      let response = await dataApiTest(data)
      this.loading = false
      if (response.data.code === 1) {
        this.msg = response.data.data
        this.$message.success(response.data.msg)
        return false
      }
      this.msg = ''
      this.$message.error('测试失败')
    }
  }
}
</script>

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
