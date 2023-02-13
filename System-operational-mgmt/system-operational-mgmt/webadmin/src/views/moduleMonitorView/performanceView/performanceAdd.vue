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
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading' label-width="200px">
      <el-form-item class="InpitWidth" label="用户访问次数阈值/日:" prop="access">
        <el-input clearable autocomplete="off" v-model="ruleForm.access" placeholder="请输入用户访问次数阈值/日"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="告警方式:" style="text-align:left" prop="alarmWay">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.alarmWay" placeholder="请选择告警方式">
          <el-option label="短信" value="sms"></el-option>
          <el-option label="邮件" value="email"></el-option>
          <el-option label="锁定账号" value="account"></el-option>
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

import { insert, update, findById } from '@/api/moduleMonitorView/http'
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
      ruleForm: {
        access: '',
        alarmWay: '',
        peopleNotified: ''
      },
      rules: {
        access: [{
          required: true,
          validator: this.common.isInteger,
          msg: '请输入用户访问次数阈值/日',
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
      this.dialogFormVisible = true
      this.id = value
      if (value) {
        this.details()
      }
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
        access: '',
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
        let alarmWay = this.ruleForm.alarmWay
        let peopleNotified = this.ruleForm.peopleNotified
        let str = peopleNotified.replace(/，/ig,',')
        let arr = str.split(',')
        if (peopleNotified !== '') {
          try {
            let reg = /^[1][3,4,5,7,8][0-9]{9}$/
            if (alarmWay === 'email' || alarmWay === 'account') {
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
            if (alarmWay === 'email' || alarmWay === 'account') {
              msg = '邮箱【' + e.message + '】格式不正确'
            }
            that.$message.error(msg)
            return false
          }
        }
        let data = {
          access: this.ruleForm.access,
          alarmWay: alarmWay,
          peopleNotified: peopleNotified,
          type: 'performance'
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
