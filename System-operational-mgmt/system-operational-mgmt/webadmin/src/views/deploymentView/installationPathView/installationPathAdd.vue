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
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading' label-width="100px">
      <el-form-item class="InpitWidth" label="节点名称:" style="text-align:left" prop="nodeId">
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
      <el-form-item class="InpitWidth" label="安装路径:" prop="path">
        <el-input clearable autocomplete="off" v-model="ruleForm.path" placeholder="请输入安装路径"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="操作系统:">
        <el-input clearable autocomplete="off" v-model="ruleForm.os" placeholder="请输入操作系统"></el-input>
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
import { findAllNodes, findAllServices } from '@/api/moduleMonitorView/http'
import { installPathInsert, installPathUpdate, installPathFindById } from '@/api/deploymentView/http'
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
        os: '',
        path: '',
        remark: ''
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
        path: [{
          required: true,
          message: '请输入安装路径',
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
      // this.getSysList()
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
      const res = await installPathFindById({ id: this.id })
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
        os: '',
        path: '',
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
        let data = {
          nodeId: this.ruleForm.nodeId,
          nodeName: nodeName,
          sysId: this.ruleForm.sysId,
          sysName: sysName,
          os: this.ruleForm.os,
          path: this.ruleForm.path,
          remark: this.ruleForm.remark
        }
        let response = null
        if (that.id) {
          data.id = that.id
          response = await installPathUpdate(data)
        } else {
          response = await installPathInsert(data)
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
