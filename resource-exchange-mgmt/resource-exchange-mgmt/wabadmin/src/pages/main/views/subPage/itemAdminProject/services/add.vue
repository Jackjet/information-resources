<template>
  <el-dialog
    title="添加系统服务"
    :visible.sync="isShow"
    width="50%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="CloseModal"
    v-loading='loading'>
    <el-button @click="showNodes" type="primary" style="float: right" icon="el-icon-more-outline">选择节点</el-button>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="routeForm" style="width: 80%">
      <el-form-item
        prop="nodeName"
        label="节点名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.nodeName" placeholder="请选择节点" disabled></el-input>
      </el-form-item>
      <el-form-item
        prop="name"
        label="系统服务名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.name" placeholder="请输入系统服务名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="templateType"
        label="系统服务类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select style="width: 100%;" placeholder="请选择服务类型" v-model="ruleForm.templateType" @change="typeSelectChange">
          <el-option label="脚本运行服务" value="webapi"></el-option>
          <el-option label="消息集成服务" value="emq"></el-option>
          <el-option label="API集成服务" value="dcapi"></el-option>
          <el-option label="数据缓存服务" value="datacache"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="templateVersion"
        label="系统服务版本"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select style="width: 100%;" v-model="ruleForm.templateVersion" :disabled="isDisabled">
          <el-option v-for="item in versions" :label="item.name" :key="item.id" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="remark"
        label="备注"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable placeholder='请输入备注' type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="CloseModal" icon="el-icon-close">取 消</el-button>
      <el-button type="primary" @click="Save('ruleForm')" icon="el-icon-check">保 存</el-button>
    </span>
    <nodeList ref="nodeList" @getNode="savePickNode"></nodeList>
  </el-dialog>
</template>

<script>
  import nodeList from './nodeList'

  export default {
    components: {nodeList},
    data () {
      return {
        integrationId: this.common.session('currentUser').id,
        nodeID: '',
        isDisabled: false,
        versions: [],
        lastSelectedType: '',
        ruleForm: {
          runNodeId: '',
          nodeName: '',
          name: '',
          templateVersion: '',
          remark: '',
          templateType: ''
        },
        rules: {
          nodeName: [{
            required: true,
            message: '请选择节点',
            trigger: ['blur', 'change']
          }],
          name: [{
            required: true,
            message: '请输入系统服务名称',
            trigger: ['blur', 'change']
          }],
          templateType: [{
            required: true,
            message: '请选择模板类型',
            trigger: ['blur', 'change']
          }]
        },
        loading: false,
        formLabelWidth: '150px',
        isShow: false
      }
    },

    methods: {
      initial (row) {
        const that = this
        that.nodeID = row
        that.isShow = true
      },

      CloseModal () {
        const that = this
        that.isShow = false
        that.$refs['ruleForm'].resetFields()
        that.nodeID = ''
        that.ruleForm.runNodeId = ''
        that.ruleForm.templateVersion = ''
        that.ruleForm.name = ''
        that.ruleForm.remark = ''
        that.ruleForm.templateType = ''
      },

      Save (ruleForm) {
        const that = this

        that.$refs[ruleForm].validate((index) => {
          if (index === false) {
            return false
          }
          let data = {
            integrationId: this.integrationId,
            runNodeId: that.ruleForm.runNodeId,
            name: that.ruleForm.name,
            templateId: that.ruleForm.templateVersion,
            templateType: that.ruleForm.templateType,
            remark: that.ruleForm.remark
          }
          that.add(data)
        })
      },
      async typeSelectChange (type) {
        if (this.lastSelectedType && this.lastSelectedType === type) {
          return false
        }
        this.lastSelectedType = type
        // this.ruleForm.templateVersion = ''

        const that = this
        if (type === 'webapi') {
          this.isDisabled = false
        } else {
          this.isDisabled = true
        }
        try {
          let url = this.Interface.SysServiceInstallPack.findAllVersion
          let obj = {
            type: type
          }
          const response = await this.request.dataGet(that, url, obj)
          if (response.data.code === 1) {
            console.log(response.data.data)
            that.versions.splice(0)
            that.ruleForm.templateVersion = ''
            if (response.data.data !== null && response.data.data.length > 0) {
              that.versions = response.data.data
              that.ruleForm.templateVersion = response.data.data[0].id
            } else {
              if (type === 'emq') {
                that.versions = [{id: '3.1.2', label: '3.1.2'}]
                that.ruleForm.templateVersion = '3.1.2'
              }
            }
          }
        } catch (even) {
          that.$message.error('数据获取失败')
        }

      },

      // 添加方法
      async add (data) {
        const that = this
        let url = that.Interface.sysService.insert
        // POST请求添加轮播图数据
        that.loading = true
        const response = await that.request.dataPost(that, url, data)
        that.loading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          that.$message.success(response.data.msg)
          /**
           * 清空表单元素 重置验证信息.
           * @param imageUrl
           * @param detail
           */
          that.CloseModal()
          // 重新触发父组件加载loading
          that.$emit('Reload')
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },
      showNodes () {
        this.$refs.nodeList.initial(this.ruleForm.runNodeId)
      },
      savePickNode (data) {
        this.ruleForm.nodeName = data.name
        this.ruleForm.runNodeId = data.nodeId
      }
    }
  }
</script>

<style lang="scss">
  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }
</style>
