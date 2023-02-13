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
      <el-form-item class="InpitWidth" label="节点名称:" prop="nodeId">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.nodeId" placeholder="请选择节点名称" @change="nodeChange">
          <el-option
            v-for="item in nodeList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="InpitWidth" label="系统名称:" prop="systemId" :required="isHaveTo" v-show="isShow">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.systemId" placeholder="请选择系统名称" @change="sysChange">
          <el-option
            v-for="item in sysList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="mobile"
        label="手机短信:"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.mobile" placeholder="请输入手机短信，中间用逗号隔开"></el-input>
      </el-form-item>
      <el-form-item
        prop="email"
        label="邮件通知:"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.email" placeholder="请输入邮件地址，中间用逗号隔开"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { update, getFindAllNodes, findAllServices, findByNodeId } from '@/api/monitoringView/http'
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
        callback(new Error(rule.message))
      } else {
        callback()
      }
    }
    return {
      type: '',
      isShow: false,
      nodeList: [],
      sysList: [],
      ruleForm: {
        nodeId: '',
        systemId: '',
        mobile: '',
        email: ''
      },
      rules: {
        nodeId: [{
          required: true,
          message: '请选择节点名称',
          trigger: ['change', 'blur']
        }],
        systemId: [{
          validator: validateName,
          message: '请选择系统名称'
        }]
      },
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  computed: {
    isHaveTo () {
      return this.isShow
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作findByNodeId
     */
    initial: function (value) {
      this.nodeList = []
      this.dialogFormVisible = true
      this.type = value
      if (value === 'environment') {
        this.isShow = false
      } else {
        this.isShow = true
      }
      this.getFindAllNodes()
    },
    async getFindAllNodes () {
      let data = {
        page: '1',
        size: '1000'
      }
      const res = await getFindAllNodes(data)
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    nodeChange () {
      this.ruleForm.systemId = ''
      this.sysList = []
      this.getSysList()
      this.findByNodeId()
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
    sysChange () {
      this.findByNodeId()
    },
    async findByNodeId () {
      this.ruleForm.mobile = ''
      this.ruleForm.email =  ''
      let data = {
        type: this.type,
        nodeId: this.ruleForm.nodeId,
        systemId: this.ruleForm.systemId,
      }
      const res = await findByNodeId(data)
      if (res.data.data) {
        this.ruleForm.mobile = res.data.data.mobile
        this.ruleForm.email =  res.data.data.email
      }
    },

    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
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
        let systemName = ''
        this.sysList.forEach(item => {
          if (this.ruleForm.systemId === item.id) {
            systemName = item.name
          }
        })
        let mobile = this.ruleForm.mobile
        let email = this.ruleForm.email
        let strMoile = mobile.replace(/，/ig,',')
        let arrMoile = strMoile.split(',')
        let strEmail = email.replace(/，/ig,',')
        let arrEmail = strEmail.split(',')
        if (mobile !== '') {
          try {
            let reg = /^[1][3,4,5,7,8][0-9]{9}$/
            arrMoile.forEach(item => {
              if (!reg.test(item) && item) {
                throw new Error(item)
              }
            })
          } catch (e) {
            that.loading = false
            that.$message.error('手机号【' + e.message + '】格式不正确')
            return false
          }
        }
        if (mobile !== '') {
          try {
            let reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/
            arrEmail.forEach(item => {
              if (!reg.test(item) && item) {
                throw new Error(item)
              }
            })
          } catch (e) {
            that.loading = false
            that.$message.error('邮箱【' + e.message + '】格式不正确')
            return false
          }
        }
        let data = {
          nodeId: this.ruleForm.nodeId,
          nodeName: nodeName,
          systemId: this.ruleForm.systemId,
          systemName: systemName,
          mobile: mobile,
          email: email,
          type: this.type
        }
        const response = await update(data)
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.$emit('getList')
          that.CloseModal()
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
