<template>
  <el-dialog
    title="系统服务信息"
    :visible.sync="isShow"
    width="50%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="closeModal"
    v-loading='loading'>
    <el-col :span="24">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="formwidth">
        <el-form-item
          prop="name"
          label="系统服务名称"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable disabled autocomplete="off" v-model="ruleForm.name" placeholder="请输入系统服务名称"></el-input>
        </el-form-item>
        <el-form-item
          prop="appid"
          label="系统服务appId"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable disabled="disabled" autocomplete="off" v-model="ruleForm.appid"></el-input>
        </el-form-item>
        <el-form-item
          label="系统服务appKey"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable disabled="disabled" autocomplete="off" v-model="ruleForm.appkey"></el-input>
        </el-form-item>
        <el-form-item
          prop="templateName"
          label="服务模板名称"
          :label-width="this.formLabelWidth"
          class='InpitWidth urlTit'>
          <el-input clearable disabled='disabled' autocomplete="off" v-model="ruleForm.templateName"></el-input>
          <el-button size="mini" type="primary" @click="replay" icon="el-icon-more-outline">重选服务</el-button>
        </el-form-item>
        <el-form-item
          prop="createTime"
          label="创建时间"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable disabled='disabled' autocomplete="off" v-model="ruleForm.createTime"></el-input>
        </el-form-item>
        <el-form-item
          prop="version"
          label="系统服务版本号"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable disabled='disabled' autocomplete="off" v-model="ruleForm.version"></el-input>
        </el-form-item>
        <el-form-item
          prop="url"
          v-show="isCMD"
          label="系统服务url"
          :label-width="this.formLabelWidth"
          class='urlTit InpitWidth'>
          <el-input clearable autocomplete="off" v-model="ruleForm.url" placeholder="请输入系统服务url"></el-input>
          <el-tooltip class="item" effect="dark" content="系统服务URL为该系统服务安装部署服务地址" placement="top">
            <i class="el-icon-info"></i>
          </el-tooltip>
        </el-form-item>
        <el-form-item
          prop="port"
          v-show="isCMD"
          label="端口号"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" v-model="ruleForm.port" placeholder="请输入端口号"></el-input>
        </el-form-item>
        <el-form-item
          prop="remark"
          label="备注"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <div style="float: right">
        <el-button @click="backOut" style="margin-right: 10px" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="Save('ruleForm')" icon="el-icon-check">保 存</el-button>
      </div>
    </el-col>
    <!-- 调用操作弹层 -->
    <choose
      ref="chooseList"
      @Reload='changeAppIds'>
    </choose>
  </el-dialog>
</template>

<script>
  import choose from './chooseService'

  export default {
    components: {choose},
    data () {
      return {
        id: '',
        appkey: '',
        ruleForm: {
          appid: '',
          templateType: '',
          templateVersion: '',
          templateType_c: '',
          type: '',
          createTime: '',
          version: '',
          status_c: '',
          status: '',
          reportTime: '',
          name: '',
          url: '',
          port: '',
          remark: ''
        },
        store: {
          name: '',
          url: '',
          port: '',
          remark: ''
        },
        rules: {},
        isCMD: false,
        loading: false,
        formLabelWidth: '150px',
        isShow: false
      }
    },

    methods: {
      changeAppIds () {
        this.initial(this.id)
      },
      async initial (sid) {
        const that = this
        that.isShow = true
        that.id = sid
        try {
          let findUrl = that.Interface.sysServiceConfig.find
          that.loading = true
          const response = await that.request.dataGet(that, findUrl, {appid: that.id})
          that.loading = false
          let result = response.data

          if (result.code === 1) {
            if (result.data.status === 'offline') {
              result.data.status_c = '离线'
            } else if (result.data.status === 'online') {
              result.data.status_c = '在线'
            }
            if (result.data.templateType === 'cmd') {
              that.isCMD = false
            } else {
              that.isCMD = true
            }
            if (result.data['server.port']) {
              result.data.port = result.data['server.port']
            }

            that.appkey = result.data.appkey
            that.templateId = result.data.templateId
            that.ruleForm = result.data
            that.store.name = that.ruleForm.name
            that.store.url = that.ruleForm.url
            that.store.remark = that.ruleForm.remark
          } else {
            that.$message.error('数据获取失败')
          }
        } catch (even) {
          that.loading = false
          that.$message.error('数据获取失败')
        }
      },

      Save (ruleForm) {
        const that = this
        that.$refs[ruleForm].validate((index) => {
          if (index === false) {
            return false
          }
          let data = {
            appid: that.ruleForm.appid,
            name: that.ruleForm.name,
            url: that.ruleForm.url,
            remark: that.ruleForm.remark
          }
          if (that.ruleForm.port) {
            data['serverPort'] = that.ruleForm.port
          }
          that.edit(data)
        })
      },

      backOut () {
        const that = this
        that.ruleForm.name = that.store.name
        that.ruleForm.url = that.store.url
        that.ruleForm.port = that.store.port
        that.ruleForm.remark = that.store.remark
      },

      // 修改方法
      async edit (data) {
        const that = this
        try {
          let url = that.Interface.sysServiceConfig.update
          // PUT 请求修改数据
          that.loading = true
          const response = await that.request.dataPut(that, url, data)
          that.loading = false
          if (response.data.code === 1) {
            that.closeModal()
            // 添加成功后 回调效果
            that.$message.success('保存成功')
          } else {
            // 添加上传失败后 回调失败信息
            that.$message.error(response.data.msg)
            return false
          }
        } catch (even) {
          that.$message.error('数据获取失败')
        }
      },
      replay () {
        this.$refs.chooseList.initial(this.id, this.templateId)
      },
      closeModal () {
        this.isShow = false
      }
    }
  }
</script>

<style lang="scss">
  .formwidth {
    width: 60%;
    margin: 0 auto;
  }

  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }

  .noCopy {
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
  }

  .el-dialog__header {
    position: relative;
    z-index: 99;
  }

  .ab-c {
    opacity: 0;
    position: absolute;
  }

  .urlTit {
    position: relative;

    .item {
      position: absolute;
      left: 105%;
      top: 35%;
    }
    .el-button {
      position: absolute;
      left: 105%;
      top: 15%;
    }
  }
</style>
