<template>
  <el-drawer
    :title="modalObj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='55%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        label="模型名称:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model="ruleForm.name" placeholder="请输入模型名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="type"
        label="模型类型:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select clearable style="width:100%;" v-model="ruleForm.type" :disabled='ProhibitFromm' placeholder="请选择模型类型">
            <el-option label="独立服务模型" value="independentService"></el-option>
            <el-option label="复合服务模型" value="compositeService"></el-option>
            <el-option label="编排服务模型" value="choreographyServices"></el-option>
            <el-option label="规则模型" value="rules"></el-option>
            <el-option label="标准规范模型" value="standardSpecification"></el-option>
            <el-option label="页面组件模型" value="pageComponents"></el-option>
            <el-option label="XML结构模型" value="xmlStructural"></el-option>
            <el-option label="模式匹配模型" value="modeMatching"></el-option>
            <el-option label="模式识别模型" value="modeRecognition"></el-option>
            <el-option label="其它模型" value="other"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item
        label="备注:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注"></el-input>
      </el-form-item>
      <el-form-item
        label="内容:"
        :label-width="this.formLabelWidth"
        v-if="this.lock"
        class='InpitWidth'>
        <el-input type="textarea" :autosize="{ minRows: 8, maxRows: 10}" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.content" placeholder="请输入内容"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>
// import editor from '../../integration/wangEditor'
import {insert, update, findById } from '@/api/knowledgeBase/model'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    modalObj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      // itenYear: [{
      //   value: '2020',
      //   label: '2020'
      // }, {
      //   value: '2019',
      //   label: '2019'
      // }, {
      //   value: '2018',
      //   label: '2018'
      // }, {
      //   value: '2017',
      //   label: '2017'
      // }, {
      //   value: '2016',
      //   label: '2016'
      // }],
      // itemMonth: [{
      //   value: '1',
      //   label: '1月'
      // }, {
      //   value: '2',
      //   label: '2月'
      // }, {
      //   value: '3',
      //   label: '3月'
      // }, {
      //   value: '4',
      //   label: '4月'
      // }, {
      //   value: '5',
      //   label: '5月'
      // }, {
      //   value: '6',
      //   label: '6月'
      // }, {
      //   value: '7',
      //   label: '7月'
      // }, {
      //   value: '8',
      //   label: '8月'
      // }, {
      //   value: '9',
      //   label: '9月'
      // }, {
      //   value: '10',
      //   label: '10月'
      // }, {
      //   value: '11',
      //   label: '11月'
      // }, {
      //   value: '12',
      //   label: '12月'
      // }],
      ruleForm: {
        type: '',
        name: '',
        remark: '',
        content: ''
      },
      rule: {},
      rules: {
        name: [{
          required: true,
          message: '请输入模型名称',
          trigger: 'blur'
        }],
        type: [{
          required: true,
          message: '请选择模型类型',
          trigger: ['change', 'blur']
        }]
      },

      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: '120px',
      lock: false,
      // Http: 'http://apptest.liangxin.net.cn/',
      dialogVisible: true,
      loading: false,

    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function (value) {
      const that = this
      that.dialogFormVisible = true
      if (value) {
        that.nowId = value
        that.initFormDetail(value)
      } else {
        // let year = new Date().getYear()
        that.ProhibitFrom = false
      }
    },
    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param value 当前需要操作的唯一iD
     */
    async initFormDetail (value) {
      const that = this
      try {
        let parameter = {id: value}
        that.loading = true
        const response = await findById(parameter)
        that.loading = false
        that.ruleForm = response.data.data
        if (this.modalObj === '详情') {
          that.ProhibitFrom = true
          // 禁用编辑器
        } else if (this.modalObj === '编辑') {
          that.ProhibitFromm = true
          that.lock = true
        } else {
          that.lock = false
          that.ProhibitFrom = false
          that.ProhibitFromm = false
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },



    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal () {
      const that = this
      that.ClearTextConeten()
      that.ProhibitFrom = false
      // if (!that.rules.name) {
      //   that.rules = that.rule
      // }
    },

    // 初始化表单
    ClearTextConeten () {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.type = ''
      that.ruleForm.remark = ''
      that.ruleForm.name = ''
      that.lock = false
      that.ProhibitFrom = false
      that.ProhibitFromm = false
      that.dialogFormVisible = false
      if (that.ruleForm.id) {
        delete that.ruleForm.id
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate((index) => {
        if (index === false) {
          return false
        }
        Object.entries(that.ruleForm).map((a, b) => {
          console.log(b)
          if (a[0] === 'id') {
            delete that.ruleForm[a[0]]
          }
        })
        if (this.modalObj === '添加') {
          that.add(that.ruleForm)
          return false
        } else if (this.modalObj === '编辑') {
          that.ruleForm.id = that.nowId
          that.edit(that.ruleForm)
          return false
        }
      })
    },

    // 添加方法
    async add (data) {
      const that = this
      // POST请求添加轮播图数据
      that.loading = true
      const response = await insert(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten()
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
      // PUT 请求修改数据
      that.loading = true
      const response = await update(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg)
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten()
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
.el-drawer {
  .el-drawer {
    .el-drawer__body {
      padding: 10px 20px !important;
    }
  }
  .InpitWidth {
    width: 80%;
    min-width: 302px;
  }
  .avatar-border-red .el-upload{
    border:1px dashed #f56c6c !important;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #b6b5b5;
    }
    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;
      .el-progress-circle {
        margin: 13% auto 10%;
      }
    }
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #b6b5b5;
    }
    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;
      .el-progress-circle {
        margin: 13% auto 10%;
      }
    }
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 200px;
    height: 178px;
    line-height: 178px;
    text-align: center;
    cursor: pointer;
    border-radius: 6px;
  }
  .avatar {
    width: 200px;
    height: 180px;
    display: block;
  }
}

.edit_main {
  text-align: left;
  padding: 10px 15px;
  border-radius: 5px;
  min-height: 250px;
  background-color: #f5f7fa;
  border:1px solid #e4e7ed;
  color: #c0c4cc;
  cursor: not-allowed;
  img {
    width: auto;
    height: auto;
  }
}

.ClassDisable .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  float: left;
  position: relative;
  overflow: hidden;
  cursor: no-drop;
  &:hover {
    border-color: #b6b5b5;
  }
  .el-progress {
    position: absolute;
    z-index: 999;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: white;
    .el-progress-circle {
      margin: 13% auto 10%;
    }
  }
}

.buttonentry {
  float: left;
  text-align: left;
}
.a{
  width: 6vw!important;
}
.drawer_footer{
  text-align: center;
}
:focus {
  outline:0;
}

</style>
