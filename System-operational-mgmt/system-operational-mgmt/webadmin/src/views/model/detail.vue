<template>
  <el-drawer
    :title="modalObj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='50%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="itemType"
        label="项目名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.itemType" placeholder="请输入项目名称"></el-input>
      </el-form-item>
      <el-form-item
        prop="taxType"
        label="收入类别"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.taxType" :disabled='ProhibitFrom' placeholder="请选择">
          <el-option label="税收" value="1"></el-option>
          <el-option label="非税收" value="2"></el-option>
        </el-select>
      </el-form-item>
       <el-form-item
        prop="areaType"
        label="收入类型"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-select v-model="ruleForm.areaType" :disabled='ProhibitFrom' placeholder="请选择">
          <el-option label="境内" value="1"></el-option>
          <el-option label="境外" value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-row>
         <el-col :span="2">
            <el-form-item
            prop="year"
            label="年"
            :label-width="this.formLabelWidth"
            class='InpitWidth'>
            <el-select v-model="ruleForm.year" class="a" :disabled='ProhibitFrom' placeholder="请选择">
              <el-option
                  v-for="item in itenYear"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
            </el-select>
             </el-form-item>
         </el-col>
         <el-col  :span="2" :offset="3"   >
            <el-form-item
              prop="month"
              label="月"
              :label-width="this.formLabelWidth"
              class='InpitWidth'>
              <el-select v-model="ruleForm.month" class="a" :disabled='ProhibitFrom' placeholder="请选择">
                <el-option
                  v-for="item in itemMonth"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
               </el-form-item>
         </el-col>
      </el-row>
      <el-form-item
        prop="newYearAmount"
        label="本年数额"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom'  oninput = "value=value.replace(/[^\d]/g,'')" autocomplete="off"  v-model="ruleForm.newYearAmount" placeholder="请输入本年数额"></el-input>
      </el-form-item>
      <el-form-item
        prop="lastYearAmount"
        label="上年数额"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm'    oninput = "value=value.replace(/[^\d]/g,'')" autocomplete="off" v-model="ruleForm.lastYearAmount" placeholder="请输入上年数额"></el-input>
      </el-form-item>
       <el-form-item
        prop="thanLastYearRer"
        label="比上年增减%:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFromm'    oninput = "value=value.replace(/[^\d]/g,'')" autocomplete="off" v-model="ruleForm.thanLastYearRer" placeholder="请输入比上年增减%"></el-input>
      </el-form-item>
      <el-form-item
        label="备注"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注"></el-input>
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
      itenYear: [{
        value: '2020',
        label: '2020'
      }, {
        value: '2019',
        label: '2019'
      }, {
        value: '2018',
        label: '2018'
      }, {
        value: '2017',
        label: '2017'
      }, {
        value: '2016',
        label: '2016'
      }],
      itemMonth: [{
        value: '1',
        label: '1月'
      }, {
        value: '2',
        label: '2月'
      }, {
        value: '3',
        label: '3月'
      }, {
        value: '4',
        label: '4月'
      }, {
        value: '5',
        label: '5月'
      }, {
        value: '6',
        label: '6月'
      }, {
        value: '7',
        label: '7月'
      }, {
        value: '8',
        label: '8月'
      }, {
        value: '9',
        label: '9月'
      }, {
        value: '10',
        label: '10月'
      }, {
        value: '11',
        label: '11月'
      }, {
        value: '12',
        label: '12月'
      }],
      ruleForm: {
        itemType: '',
        taxType: '',
        areaType: '',
        newYearAmount: '',
        lastYearAmount: '',
        thanLastYearRer: '',
        year: '',
        month: '',
        remark: ''
      },
      rule: {},
      rules: {
        lastYearAmount: [{
          required: true,
          message: '请输入去年金额',
          trigger: 'blur'
        }],
        newYearAmount: [{
          required: true,
          message: '请输入本年金额',
          trigger: 'blur'
        }],
        itemType: [{
          required: true,
          message: '请输入项目名称',
          trigger: 'blur'
        }],
        areaType: [{
          required: true,
          message: '请选择收入类型',
          trigger: ['change', 'blur']
        }],
        taxType: [{
          required: true,
          message: '请选择收入类别',
          trigger: ['change', 'blur']
        }],
        year: [{
          required: true,
          message: '请选择年',
          trigger: ['change', 'blur']
        }],
        month: [{
          required: true,
          message: '请选择月',
          trigger: ['change', 'blur']
        }]
      },

      // 上传七牛云请求地址
      ActionUrl: 'https://upload-z2.qiniup.com',
      // 封面图显示路径
      imageUrl: '',
      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: '100px',
      isClear: false,
      // Http: 'http://apptest.liangxin.net.cn/',
      dialogVisible: true,
      videoFlag: false,
      loading: false,
      ImageName: '',
      nowId: '',
      Class: 'avatar-uploader',
      QiniuToken: {
        token: ''
      },
      ClassDisable: 'ClassDisable'
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
      console.log(value)
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
        let findUrl = that.Interface.areaIncome.findById
        let parameter = {id: value}
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, parameter)
        that.loading = false
        that.ruleForm = response.data.data
        if (this.modalObj === '详情') {
          that.ProhibitFrom = true
          // 禁用编辑器
        } else if (this.modalObj === '编辑') {
          that.ProhibitFromm = true
        } else {
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
      that.ruleForm.itemType = ''
      that.ruleForm.taxType = ''
      that.ruleForm.areaType = ''
      that.ruleForm.newYearAmount = ''
      that.ruleForm.lastYearAmount = ''
      that.ruleForm.year = ''
      that.ruleForm.month = ''
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
      let url = that.Interface.areaIncome.insert
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
      let url = that.Interface.areaIncome.update
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

</style>
