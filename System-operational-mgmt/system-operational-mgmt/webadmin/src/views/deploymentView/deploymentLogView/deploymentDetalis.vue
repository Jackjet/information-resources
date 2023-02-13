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
    <el-form :model="ruleForm" ref="ruleForm"  v-loading='loading' label-width="100px">
      <el-form-item class="InpitWidth" label="节点名称:">
        {{ruleForm.nodeName}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="系统名称:">
        {{ruleForm.sysName}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="部署类型:">
        {{ruleForm.typeName}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="部署接口url:">
        {{ruleForm.apiUrl}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="方法:">
        {{ruleForm.apiMethod}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="版本号:">
        {{ruleForm.versionNumber}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="请求参数:">
        {{ruleForm.apiBody}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="开始时间:">
        {{ruleForm.beginTime}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="结束时间:">
        {{ruleForm.endTime}}
      </el-form-item>
      <el-form-item class="InpitWidth" label="执行日志:" style="word-break:break-all">
        {{ruleForm.resultLog}}
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
    </div>
  </el-drawer>
</template>

<script>
import { deploymentLogFindById, findByAutomatedDeploymentEntityId } from '@/api/deploymentView/http'
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
      ruleForm: {
        nodeId: '',
        nodeName: '',
        sysId: '',
        sysName: '',
        type: '',
        apiId: '',
        apiName: '',
        remark: ''
      },
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    initial (value, type = null) {
      this.dialogFormVisible = true
      this.id = value
      if (type) {
        this.findByAutomatedDeploymentEntityId()
      } else {
        this.details()
      }
    },
    async details () {
      const res = await deploymentLogFindById({ id: this.id })
      switch (res.data.data.type) {
        case 'deployment':
          res.data.data.typeName = '自动安装'
          break
        case 'upgrade':
          res.data.data.typeName = '版本升级'
          break
        case 'rollback':
          res.data.data.typeName = '版本回滚'
          break
        case 'configUpdate':
          res.data.data.typeName = '配置更新'
          break
      }
      this.ruleForm = res.data.data
    },
    async findByAutomatedDeploymentEntityId () {
      const res = await findByAutomatedDeploymentEntityId({ id: this.id })
      switch (res.data.data.type) {
        case 'deployment':
          res.data.data.typeName = '自动安装'
          break
        case 'upgrade':
          res.data.data.typeName = '版本升级'
          break
        case 'rollback':
          res.data.data.typeName = '版本回滚'
          break
        case 'configUpdate':
          res.data.data.typeName = '配置更新'
          break
      }
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
        type: '',
        apiId: '',
        apiName: '',
        remark: '',
        resultLog: ''
      }
    }
  }
}
</script>

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
