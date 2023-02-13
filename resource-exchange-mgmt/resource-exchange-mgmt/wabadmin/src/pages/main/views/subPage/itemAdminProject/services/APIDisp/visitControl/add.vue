<template>
  <el-dialog
    :title="thisTit"
    :visible.sync="isShow"
    width="50%"
    :modal-append-to-body='false'
    append-to-body
    :before-close="CloseModal"
    v-loading='loading'>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="routeForm">
      <el-form-item
        prop="name"
        label="策略名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.name" placeholder="请输入策略名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="operation"
        label="动作类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select clearable v-model="ruleForm.operation" placeholder='请选择动作类型' style='width: 100%; float: left'>
          <el-option label='允许' value='0'></el-option>
          <el-option label='禁止' value='1'></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="type"
        label="限制类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth buttonCss'>
        <el-select clearable v-model="ruleForm.type" placeholder='请选择限制类型' style='width: 100%; float: left' @change="visitType">
          <el-option label='限制IP' value='0'></el-option>
          <el-option label='限制开发者' value='1'></el-option>
        </el-select>
        <el-button v-show="isIP" type="primary" @click="toChoose" icon="el-icon-more-outline">选择开发者</el-button>
      </el-form-item>
      <el-form-item
        prop="ips"
        label="ip地址"
        v-show="!isIP"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable placeholder='请输入ip地址,多个用英文逗号隔开' type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.ips"></el-input>
      </el-form-item>
      <el-form-item
        prop="appIds"
        label="开发者appid"
        v-show="isIP"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable placeholder='请输入微应用账号,多个用英文逗号隔开' type="textarea" :rows="3" autocomplete="off" v-model="ruleForm.appIds"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="CloseModal" icon="el-icon-close">取 消</el-button>
      <el-button type="primary" @click="Save('ruleForm')" icon="el-icon-check">保 存</el-button>
    </span>
    <!-- 调用操作弹层 -->
    <choose
      ref="chooseList"
      @Reload='changeAppIds'>
    </choose>
  </el-dialog>
</template>

<script>
import choose from './chooseApp'

export default {
  components: { choose },
  data () {
    return {
      integrationId: this.common.session('currentUser').id,
      appid: '',
      thisTit: '',
      isIP: false,
      ruleForm: {
        name: '',
        operation: '',
        type: '',
        ips: '',
        appIds: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入访问策略名称',
          trigger: ['blur', 'change']
        }],
        operation: [{
          required: true,
          message: '请选择动作类型',
          trigger: ['blur', 'change']
        }],
        type: [{
          required: true,
          message: '请选择限制类型',
          trigger: ['blur', 'change']
        }],
        ips: [{
          required: true,
          message: '请输入ip地址',
          trigger: ['blur', 'change']
        }],
        appIds: [{
          required: true,
          message: '请输入微应用账号',
          trigger: ['blur', 'change']
        }]
      },
      isAdd: false,
      loading: false,
      formLabelWidth: '150px',
      isShow: false
    }
  },

  methods: {
    initial (appid, row) {
      const that = this
      that.appid = appid
      if (row) {
        that.thisTit = '编辑访问控制策略'
        that.isAdd = true
        that.askInfo(row)
      } else {
        that.thisTit = '添加访问控制策略'
        that.isAdd = false
      }
      that.isShow = true
    },

    async askInfo (row) {
      const that = this
      try {
        let findUrl = that.Interface.accessStrategy.findById
        let parameter = {id: row}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        if (response.data.code === 1) {
          if (response.data.data.type === '1') {
            that.isIP = true
            that.rules.appIds.splice(0)
            that.rules.ips.splice(0)
            that.rules.appIds.push({
              required: true,
              message: '请输入微应用账号',
              trigger: ['blur', 'change']
            })
          } else {
            that.isIP = false
            that.rules.appIds.splice(0)
            that.rules.ips.splice(0)
            that.rules.ips.push({
              required: true,
              message: '请输入ip地址',
              trigger: ['blur', 'change']
            })
          }
          that.ruleForm = response.data.data
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },

    visitType (a) {
      const that = this
      if (a === '1') {
        that.isIP = true
        that.rules.appIds.splice(0)
        that.rules.ips.splice(0)
        that.rules.appIds.push({
          required: true,
          message: '请输入微应用账号',
          trigger: ['blur', 'change']
        })
      } else {
        that.isIP = false
        that.rules.appIds.splice(0)
        that.rules.ips.splice(0)
        that.rules.ips.push({
          required: true,
          message: '请输入ip地址',
          trigger: ['blur', 'change']
        })
      }
    },

    toChoose () {
      this.$refs.chooseList.initial(this.ruleForm.appIds, this.appid)
    },

    changeAppIds (val) {
      this.ruleForm.appIds = val
    },

    CloseModal () {
      const that = this
      that.isShow = false
      that.rules.appIds.splice(0)
      that.rules.ips.splice(0)
      that.rules.ips.push({
        required: true,
        message: '请输入ip地址',
        trigger: ['blur', 'change']
      })
      that.rules.appIds.push({
        required: true,
        message: '请输入微应用账号',
        trigger: ['blur', 'change']
      })
      that.$refs['ruleForm'].resetFields()
      that.isIP = false
      that.ruleForm.name = ''
      that.ruleForm.operation = ''
      that.ruleForm.type = ''
      that.ruleForm.ips = ''
      that.ruleForm.appIds = ''
      that.ruleForm.integrationId = ''
    },

    Save (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        that.ruleForm.integrationId = that.integrationId
        if (!this.isAdd) {
          that.add(that.ruleForm)
        } else {
          that.edit(that.ruleForm)
        }
      })
    },

    // 添加方法
    async add (data) {
      const that = this
      data.sysServiceId = that.appid
      let url = that.Interface.accessStrategy.insert
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

    // 修改方法
    async edit (data) {
      const that = this
      data.sysServiceId = that.appid
      let url = that.Interface.accessStrategy.update
      // PUT 请求修改数据
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
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
    }
  }
}
</script>

<style lang="scss">
  .routeForm {
    width: 70%;
  }
  .InpitWidth {
    width: 90%;
    min-width: 302px;
  }
  .buttonCss {
    position: relative;

    .el-button {
      width: 50%;
      position: absolute;
      left: 105%;
      top: 0;
    }
  }
</style>
