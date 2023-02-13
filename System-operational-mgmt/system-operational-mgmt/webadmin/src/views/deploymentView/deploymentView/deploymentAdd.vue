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
      <el-form-item class="InpitWidth" label="部署类型:" prop="type" style="text-align:left">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.type" placeholder="请选择部署类型" v-if="id === ''">
          <el-option label="自动安装" value="install"></el-option>
          <el-option label="版本升级" value="upgrade"></el-option>
          <el-option label="版本回滚" value="rollback"></el-option>
          <el-option label="配置更新" value="configUpdate"></el-option>
        </el-select>
        <span v-else>{{ruleForm.typeName}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="部署接口:" prop="apiId" style="text-align:left">
        <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.apiId" placeholder="请选择部署接口" v-if="btnType !== 'execute'">
          <el-option
            v-for="item in apiList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
        <span v-else>{{ruleForm.apiName}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="备注:" v-if="btnType !== 'execute'">
        <el-input type="textarea" placeholder="请输入备注" v-model="ruleForm.remark"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="接口参数:" v-if="btnType === 'execute'">
        <el-input type="textarea" placeholder="请输入接口参数" v-model="executeForm.params"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="请求参数:" v-if="btnType === 'execute'">
        <el-input type="textarea" placeholder="请输入请求参数" v-model="executeForm.body"></el-input>
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
import { deploymentInsert, deploymentUpdate, deploymentFindById, dataApi, execute } from '@/api/deploymentView/http'
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
      btnType: '',
      isShow: true,
      nodeList: [],
      sysList: [],
      apiList: [],
      executeForm: {
        params: '',
        body: ''
      },
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
        type: [{
          required: true,
          message: '请选择部署类型',
          trigger: ['change', 'blur']
        }],
        apiId: [{
          required: true,
          message: '请选择部署接口',
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
    initial (value, btnType = null) {
      this.nodeList = []
      this.sysList = []
      this.apiList = []
      this.dialogFormVisible = true
      this.btnType = btnType
      this.id = value
      this.getNodeList()
      this.getApiList()
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
    async getApiList () {
      let data = {
        page: '1',
        size: '1000'
      }
      const res = await dataApi(data)
      res.data.data.content.forEach(item => {
        this.apiList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    async details () {
      const res = await deploymentFindById({ id: this.id })
      switch (res.data.data.type) {
        case 'install':
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
        remark: ''
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      // execute拦截走执行
      if (this.btnType === 'execute') {
        this.execute()
        return false
      }
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
        let apiName = ''
        this.apiList.forEach(item => {
          if (this.ruleForm.apiId === item.id) {
            apiName = item.name
          }
        })
        let data = {
          nodeId: this.ruleForm.nodeId,
          nodeName: nodeName,
          sysId: this.ruleForm.sysId,
          sysName: sysName,
          type: this.ruleForm.type,
          apiId: this.ruleForm.apiId,
          apiName: apiName,
          remark: this.ruleForm.remark
        }
        let response = null
        if (that.id) {
          data.id = that.id
          response = await deploymentUpdate(data)
        } else {
          response = await deploymentInsert(data)
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
    },
    // 执行
    async execute () {
      this.loading = true
      let data = {
        id: this.id,
        params: this.executeForm.params,
        body: this.executeForm.body
      }
      let response = await execute(data)
      this.loading = false
      if (response.data.code === 1) {
        this.$message.success(response.data.msg)
        this.$emit('getList')
        this.CloseModal()
        return false
      }
      this.$message.error(response.data.msg)
    }
  }
}
</script>

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
