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
      <el-form-item class="InpitWidth" label="计划名称:" prop="name">
        <el-input clearable size="medium" autocomplete="off" v-model="ruleForm.name" placeholder="请输入计划名称" v-if="id === ''"></el-input>
        <span v-else>{{ruleForm.name}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="接口名称:" style="text-align:left" prop="apiId">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.apiId" placeholder="请选择接口名称" v-if="id === ''">
          <el-option
            v-for="item in apiList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        <span v-else>{{ruleForm.apiName}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="Cron表达式:" prop="cron">
        <el-input clearable autocomplete="off" v-model="ruleForm.cron" placeholder="请输入Cron表达式"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="URL参数:">
        <el-input clearable autocomplete="off" v-model="ruleForm.params" placeholder="请输入URL参数"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="请求参数:">
        <el-input type="textarea" placeholder="请输入请求参数" v-model="ruleForm.body"></el-input>
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

import { dataApiPlanInsert, dataApiPlanUpdate, dataApiList, dataApiPlanFindById } from '@/api/dataProcessingService/http'
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
      apiList: [],
      ruleForm: {
        name: '',
        apiId: '',
        cron: '',
        params: '',
        body: '',
        remark: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入计划名称',
          trigger: ['change', 'blur']
        }],
        apiId: [{
          required: true,
          message: '请选择接口名称',
          trigger: ['change', 'blur']
        }],
        cron: [{
          required: true,
          message: '请输入Cron表达式',
          trigger: ['change', 'blur']
        }]
      },
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    initial (value) {
      this.apiList = []
      this.dialogFormVisible = true
      this.id = value
      this.getApiList()
      if (value) {
        this.details()
      }
    },
    async getApiList () {
      let data = {
        page: '1',
        size: '1000',
        excludeType: 'socket'
      }
      const res = await dataApiList(data)
      res.data.data.content.forEach(item => {
        this.apiList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    async details () {
      const res = await dataApiPlanFindById({ id: this.id })
      this.ruleForm = res.data.data
    },

    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.ruleForm = {
        name: '',
        apiId: '',
        cron: '',
        params: '',
        body: '',
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
        let apiName = ''
        this.apiList.forEach(item => {
          if (this.ruleForm.apiId === item.id) {
            apiName = item.name
          }
        })
        let response = null
        if (that.id) {
          let data = {
            id: that.id,
            cron: this.ruleForm.cron,
            params: this.ruleForm.params,
            body: this.ruleForm.body,
            remark: this.ruleForm.remark
          }
          response = await dataApiPlanUpdate(data)
        } else {
          let data = {
            name: this.ruleForm.name,
            apiId: this.ruleForm.apiId,
            apiName: apiName,
            cron: this.ruleForm.cron,
            params: this.ruleForm.params,
            body: this.ruleForm.body,
            remark: this.ruleForm.remark
          }
          response = await dataApiPlanInsert(data)
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

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
