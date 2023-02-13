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
      <el-form-item class="InpitWidth" label="节点名称" style="text-align:left" prop="nodeId">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.nodeId" placeholder="请选择节点名称" v-if="id === ''" @change="nodeChange">
          <el-option
            v-for="item in nodeList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        <span v-else>{{ruleForm.nodeName}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="系统名称:" style="text-align:left" prop="sysId">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.sysId" placeholder="请选择系统名称" v-if="id === ''">
          <el-option
            v-for="item in sysList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        <span v-else>{{ruleForm.sysName}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="内存阈值（M）:" prop="ram">
        <el-input clearable autocomplete="off" v-model="ruleForm.ram" placeholder="请输入内存阈值（M）"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="存储阈值（M）:" prop="rom">
        <el-input clearable autocomplete="off" v-model="ruleForm.rom" placeholder="请输入存储阈值（M）"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="告警方式:" style="text-align:left" prop="alarmWay">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.alarmWay" placeholder="请选择告警方式">
          <el-option label="短信" value="sms"></el-option>
          <el-option label="邮件" value="email"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="InpitWidth" label="通知人:">
        <el-input placeholder="中间用逗号隔开" v-model="ruleForm.peopleNotified"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { insert, findAllNodes, findAllServices, update, findById } from '@/api/moduleMonitorView/http'
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
      nodeList: [],
      sysList: [],
      ruleForm: {
        nodeId: '',
        nodeName: '',
        sysId: '',
        sysName: '',
        ram: '',
        rom: '',
        alarmWay: '',
        peopleNotified: ''
      },
      rules: {
        nodeId: [{
          required: true,
          message: '请选择节点名称',
          trigger: ['change', 'blur']
        }],
        sysId: [{
          required: true,
          message: '请选择系统名称',
          trigger: ['change', 'blur']
        }],
        ram: [{
          required: false,
          pattern: /^[0-9]+$/,
          message: '请输入正整数',
          trigger: ['change', 'blur']
        }],
        rom: [{
          required: false,
          pattern: /^[0-9]+$/,
          message: '请输入正整数',
          trigger: ['change', 'blur']
        }],
        alarmWay: [{
          required: true,
          message: '请选择告警方式',
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
      this.nodeList = []
      this.sysList = []
      this.dialogFormVisible = true
      this.id = value
      this.getNodeList()
      if (value) {
        this.details()
      }
    },
    async getNodeList () {
      const res = await findAllNodes()
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    nodeChange () {
      this.ruleForm.sysId = ''
      this.sysList = []
      this.getSysList()
    },
    async getSysList () {
      let data = {
        nodeId: this.ruleForm.nodeId
      }
      const res = await findAllServices(data)
      res.data.data.forEach(item => {
        this.sysList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    async details () {
      const res = await findById({ id: this.id })
      this.ruleForm = res.data.data
    },

    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.ruleForm = {
        nodeId: '',
        nodeName: '',
        sysId: '',
        sysName: '',
        ram: '',
        rom: '',
        alarmWay: '',
        peopleNotified: ''
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate(async (index) => {
        if (index === false) {
          return false
        }
        that.loading = true
        let nodeName = ''
        this.nodeList.forEach(item => {
          if (this.ruleForm.nodeId === item.id) {
            nodeName = item.name
          }
        })
        let sysName = ''
        this.sysList.forEach(item => {
          if (this.ruleForm.sysId === item.id) {
            sysName = item.name
          }
        })
        let alarmWay = this.ruleForm.alarmWay
        let peopleNotified = this.ruleForm.peopleNotified
        let str = peopleNotified.replace(/，/ig,',')
        let arr = str.split(',')
        if (peopleNotified !== '') {
          try {
            let reg = /^[1][3,4,5,7,8][0-9]{9}$/
            if (alarmWay === 'email') {
              reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
            }
            arr.forEach(item => {
              if (!reg.test(item) && item) {
                throw new Error(item)
              }
            })
          } catch (e) {
            that.loading = false
            let msg = '手机号【' + e.message + '】格式不正确'
            if (alarmWay === 'email') {
              msg = '邮箱【' + e.message + '】格式不正确'
            }
            that.$message.error(msg)
            return false
          }
        }
        let data = {
          nodeId: this.ruleForm.nodeId,
          nodeName: nodeName,
          sysId: this.ruleForm.sysId,
          sysName: sysName,
          ram: this.ruleForm.ram,
          rom: this.ruleForm.rom,
          alarmWay: alarmWay,
          peopleNotified: peopleNotified,
          type: 'data'
        }
        let response = null
        if (that.id) {
          data.id = that.id
          response = await update(data)
        } else {
          response = await insert(data)
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
