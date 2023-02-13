<template>
  <el-main v-loading="loading">
    <el-col :span="24">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="formwidth">
        <el-form-item
          prop="databaseType"
          label="数据库类别"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-select style="width: 100%;" v-model="databaseType">
            <el-option label="postgres" value="postgres"></el-option>
            <!-- <el-option label="mysql" value="mysql"></el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item
          prop="host"
          label="数据库服务地址"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" placeholder="请输入数据库服务地址" v-model="ruleForm.host"></el-input>
        </el-form-item>
        <el-form-item
          prop="port"
          label="数据库端口号"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" placeholder="请输入数据库端口号" v-model="ruleForm.port"></el-input>
        </el-form-item>
        <el-form-item
          prop="database"
          label="数据库名称"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-input clearable autocomplete="off" placeholder="请输入数据库名称" v-model="ruleForm.database"></el-input>
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
          <el-input clearable autocomplete="off" placeholder="请输入数据库Password" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-button type="primary" @click="Save('ruleForm')" icon="el-icon-check">暂存</el-button>
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
        host: '',
        port: '',
        database: '',
        username: '',
        password: ''
      },
      rules: {
        host: [{
          required: true,
          message: '请输入数据库服务地址',
          trigger: ['blur', 'change']
        }],
        port: [{
          required: true,
          message: '请输入数据库端口号',
          trigger: ['blur', 'change']
        }],
        database: [{
          required: true,
          message: '请输入数据库名称',
          trigger: ['blur', 'change']
        }],
        username: [{
          required: true,
          message: '请输入数据库User',
          trigger: ['blur', 'change']
        }],
        password: [{
          required: true,
          message: '请输入数据库Password',
          trigger: ['blur', 'change']
        }]
      },
      loading: false,
      formLabelWidth: '150px'
    }
  },

  methods: {
    initial (appid) {
      const that = this
      that.appid = appid
      that.askInfo()
    },

    async askInfo () {
      const that = this
      try {
        let findUrl = that.Interface.emq.config.find
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, {appid: that.appid})
        that.loading = false
        if (response.data.code === 1) {
          that.ruleForm = response.data.data
        } else {
          that.$message.error(response.data.msg)
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
        let askData = {
          appid: that.appid,
          data: that.ruleForm
        }
        that.edit(askData)
      })
    },

    // 修改方法
    async edit (data) {
      const that = this
      let findUrl = that.Interface.emq.config.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, findUrl, data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
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
      let askData = {
        appid: that.appid,
        data: that.ruleForm
      }
      let url = that.Interface.emq.config.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, askData)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.askInfo()
        // 添加成功后 执行
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
        let findUrl = that.Interface.emq.config.sync
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
