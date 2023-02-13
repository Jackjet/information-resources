<template>
  <el-main v-loading="loading">
    <el-col :span="24">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="formwidth">
        <el-form-item
          prop="emqUrl"
          label="消息服务地址"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" v-model="ruleForm.emqUrl"></el-input>
        </el-form-item>
        <el-form-item
          prop="user"
          label="消息服务用户名"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" v-model="ruleForm.user"></el-input>
        </el-form-item>
        <el-form-item
          prop="password"
          label="消息服务密码"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-button type="primary" @click="Save('ruleForm')" icon="el-icon-check">暂存</el-button>
        <el-button type="primary" @click="syncConfig" icon="el-icon-s-promotion">发布</el-button>
      </el-form>
    </el-col>
  </el-main>
</template>

<script>
import * as session from '../../../../../../../../assets/js/common'

export default {
  data () {
    return {
      appid: '',
      appkey: '',
      integrationId: this.common.session('currentUser').id,
      dialogVisible: false,
      ruleForm: {
        emqUrl: '',
        user: '',
        password: ''
      },
      rules: {
        emqUrl: [{
          required: true,
          message: '请输入消息服务地址',
          trigger: ['blur', 'change']
        }],
        user: [{
          required: true,
          message: '请输入消息服务用户名',
          trigger: ['blur', 'change']
        }],
        password: [{
          required: true,
          message: '请输入消息服务密码',
          trigger: ['blur', 'change']
        }]
      },
      loading: false,
      formLabelWidth: '150px',
      isShow: false,
      isOnline: ''
    }
  },

  methods: {
    async initial (sid, isOnline) {
      const that = this
      that.appid = sid
      that.isOnline = isOnline
      try {
        let findUrl = that.Interface.datacacheConfig.findAll
        let parameter = {appid: that.appid, integrationId: that.integrationId}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.ruleForm = response.data.data
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
          appid: that.appid,
          data: {
            emqUrl: this.ruleForm.emqUrl,
            user: this.ruleForm.user,
            password: this.ruleForm.password
          }
        }
        that.edit(data, true)
      })
    },

    // 修改方法
    async edit (data,flag) {
      const that = this
      try {
        let url = that.Interface.datacacheConfig.update
        // PUT 请求修改数据
        that.loading = true
        data.integrationId = that.integrationId
        const response = await that.request.dataPut(that, url, data)
        that.loading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          if (flag) {
            that.$message.success('保存成功')
          }
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    async syncConfig () {
      const that = this

      if (that.isOnline === 'offline') {
        that.$message.error('该系统服务离线，不允许同步消息服务配置')
        return false
      } else if (that.isOnline !== 'online') {
        that.$message.error('该系统服务未安装，不允许同步消息服务配置')
        return false
      }

      try {
        that.$refs.ruleForm.validate((index) => {
          if (index === false) {
            return false
          }
          let data = {
            appid: that.appid,
            data: {
              emqUrl: this.ruleForm.emqUrl,
              user: this.ruleForm.user,
              password: this.ruleForm.password
            }
          }
          that.edit(data, false)
        })

        let url = that.Interface.keyConfig.syncKeys
        let parameter = {
          integrationId: session.default.session('currentUser').id,
          sysServiceId: that.appid
        }
        const response = await that.request.dataPut(that, url, parameter)
        if (response.data.code === 1) {
          that.$message.success('同步成功')
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (e) {
        that.$message.error('同步失败')
      }
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
  }
</style>
