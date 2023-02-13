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
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading' label-width="120px">
      <el-form-item class="InpitWidth" label="接口名称:" prop="name">
        <el-input clearable size="medium" autocomplete="off" v-model="ruleForm.name" :disabled="id?true:false" placeholder="请输入接口名称"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="服务类型:" style="text-align:left" prop="type">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.type" :disabled="id?true:false" placeholder="请选择服务类型" @change="typeChange">
          <el-option label="HTTP" value="http"></el-option>
          <el-option label="WebService" value="webservice"></el-option>
          <el-option label="Socket" value="socket"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="InpitWidth" label="URL/IP:" prop="urlOrIp">
        <el-input clearable autocomplete="off" v-model="ruleForm.urlOrIp" placeholder="请输入URL/IP"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="方法:" style="text-align:left" prop="methodType" :required="isHaveTo" v-show="isShow">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.methodType" placeholder="请选择方法" v-if="isIpuntShow">
          <el-option label="get" value="get"></el-option>
          <el-option label="post" value="post"></el-option>
          <el-option label="put" value="put"></el-option>
          <el-option label="delete" value="delete"></el-option>
        </el-select>
        <el-input v-else placeholder="请输入方法" v-model.trim="ruleForm.methodType"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="备注:">
        <el-input type="textarea" placeholder="请输入备注" v-model="ruleForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { dataApiInsert, dataApiUpdate, dataApiFindById } from '@/api/dataProcessingService/http'
export default {
  components: {
  },

  props: {
    modalObjjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    let validateName = (rule, value, callback) => {
      if (!value && this.isHaveTo) {
        callback(new Error(this.methodTypeText))
      } else {
        callback()
      }
    }
    return {
      id: '',
      isShow: true,
      isIpuntShow: true,
      ruleForm: {
        name: '',
        type: '',
        urlOrIp: '',
        methodType: '',
        remark: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入接口名称',
          trigger: ['change', 'blur']
        }],
        type: [{
          required: true,
          message: '请选择服务类型',
          trigger: ['change', 'blur']
        }],
        urlOrIp: [{
          required: true,
          validator: this.common.isUrl,
          msg: '请输入URL/IP',
          trigger: ['change', 'blur']
        }],
        methodType: [{
          validator: validateName,
          // message: '请选择方法'
        }]
      },
      methodTypeText: '请选择方法',
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  computed: {
    isHaveTo () {
      return this.isShow
    }
  },
  methods: {
    typeChange (val) {
      if (val === 'socket') {
        this.ruleForm.methodType = ''
        this.isShow = false
        return
      }
      if (val === 'webservice') {
        this.ruleForm.methodType = ''
        this.methodTypeText = '请输入方法'
        this.isShow = true
        this.isIpuntShow = false
        return
      }
      this.methodTypeText = '请选择方法'
      this.isShow = true
      this.isIpuntShow = true
    },
    initial (value) {
      this.dialogFormVisible = true
      this.id = value
      if (value) {
        this.details()
      }
    },
    async details () {
      const res = await dataApiFindById({ id: this.id })
      this.ruleForm = res.data.data
      if (res.data.data.type === 'socket') {
        this.isShow = false
        return
      }
      if (res.data.data.type === 'webservice') {
        this.methodTypeText = '请输入方法'
        this.isShow = true
        this.isIpuntShow = false
        return
      }
      this.methodTypeText = '请选择方法'
      this.isShow = true
      this.isIpuntShow = true
    },

    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.isShow = true
      this.ruleForm = {
        name: '',
        type: '',
        urlOrIp: '',
        methodType: '',
        remark: ''
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate(async (index) => {
        if (index === false) {
          return false
        }
        that.loading = true
        let type = this.ruleForm.type
        let urlOrIp = this.ruleForm.urlOrIp
        if (type === 'webservice' && urlOrIp.substr(-4) !== 'wsdl') {
          that.$message.error('URL/IP结尾必须为wsdl')
          that.loading = false
          return false
        }
        let data = {
          name: this.ruleForm.name,
          type: this.ruleForm.type,
          urlOrIp: this.ruleForm.urlOrIp,
          methodType: this.ruleForm.methodType,
          remark: this.ruleForm.remark
        }
        let response = null
        if (that.id) {
          data.id = that.id
          response = await dataApiUpdate(data)
        } else {
          response = await dataApiInsert(data)
        }
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.$emit('getList')
          that.CloseModal()
          return false
        }
        that.$message.error(response.data.msg)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/styles/drawer.scss";
  // .InpitWidth{
  //   width: 250px;
  // }
</style>
