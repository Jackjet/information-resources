<template>
  <el-main v-loading="loading">
    <el-col :span="24">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="formwidth">
        <el-form-item
          prop="controlMqAddress"
          label="数据库类别"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-select style="width: 100%;" v-model="databaseType">
            <el-option label="postgres" value="postgres"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="url"
          label="数据库url"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" placeholder="例:jdbc:mysql://localhost:3306/api_gateway_service"
                    v-model="ruleForm.url"></el-input>
        </el-form-item>
        <el-form-item
          prop="username"
          label="数据库User"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" placeholder="请输入数据库User" v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item
          prop="password"
          label="数据库Password"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" placeholder="请输入数据库password" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-button type="primary" @click="Save" icon="el-icon-check">暂存</el-button>
        <el-button type="primary" @click="refresh" icon="el-icon-s-promotion">发布</el-button>
      </el-form>
    </el-col>
  </el-main>
</template>

<script>

export default {
  data () {
    return {
      integrationId: this.common.session('currentUser').id,
      appid: '',
      databaseType: 'postgres',
      ruleForm: {
        url: '',
        username: '',
        password: ''
      },
      rules: {
        url: [{
          required: true,
          message: '请输入数据库url',
          trigger: ['blur', 'change']
        }],
        username: [{
          required: true,
          message: '请输入数据库User',
          trigger: ['blur', 'change']
        }],
        password: [{
          required: true,
          message: '请输入数据库password',
          trigger: ['blur', 'change']
        }]
      },
      loading: false,
      formLabelWidth: '150px',
      isOnline: ''
    }
  },

  methods: {
    initial (appid, serviceUrl, isOnline) {
      const that = this
      that.appid = appid
      that.isOnline = isOnline
      that.askInfo()
    },

    async askInfo () {
      const that = this
      try {
        let findUrl = that.Interface.api.config.find
        let parameter = {appid: that.appid}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          let Data = response.data.data
          that.ruleForm.url = Data['spring.datasource.url']
          that.ruleForm.username = Data['spring.datasource.username']
          that.ruleForm.password = Data['spring.datasource.password']
        } else {
          console.log(1)
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    Save () {
      const that = this
      that.edit(that.ruleForm)
    },

    // 修改方法
    async edit (data) {
      const that = this
      data.appid = that.appid
      let url = that.Interface.api.config.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success('保存成功')
        that.askInfo()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    // 更新操作
    async refresh () {
      const that = this
      if (that.isOnline === 'offline') {
        that.$message.error('数据库配置更新失败系统服务离线，不允许同步数据库信息')
        return false
      } else if (that.isOnline !== 'online') {
        that.$message.error('数据库配置更新失败系统服务未安装，不允许同步数据库信息')
        return false
      }
      let flag = false
      Object.entries(that.ruleForm).map((item, index) => {
        if (item[1].length === 0) {
          that.$message.warning('必填项不可为空！')
          flag = true
        }
      })
      if (flag) {
        return false
      }
      console.log(1)
      that.ruleForm.appid = that.appid
      let url = that.Interface.api.config.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, that.ruleForm)
      that.loading = false
      if (response.data.code === 1) {
        that.askInfo()
        // 添加成功后 回调效果
        that.toUp()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },

    async toUp () {
      const that = this
      try {
        let findUrl = that.Interface.api.config.sync
        let parameter = {appid: that.appid}
        that.loading = true
        const response = await that.request.dataPost(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success('远程更新成功')
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.loading = false
        that.$message.error('操作失败')
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

  .disp-btn-box {
    text-align: right;
    float: right;
  }

  .iptCss {
    width: 90%;
    min-width: 302px;
    position: relative;

    .el-input {
      width: 50%;
      float: left;

      input {
        width: 96%;
      }
    }
    button {
      position: absolute;
      left: 102%;
      top: 0;
    }
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
