<template>
  <el-drawer
    :title="modalObjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        label="事件类型:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.type" placeholder="请输入事件类型"></el-input>
      </el-form-item>
      <el-form-item
        label="操作员:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.operator" placeholder="请输入操作员"></el-input>
      </el-form-item>
      <el-form-item
        label="操作时间:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.createTime" placeholder="请输入操作时间"></el-input>
      </el-form-item>
      <el-form-item
        label="事件源:"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.source" placeholder="请输入事件源"></el-input>
      </el-form-item>
      <el-form-item
        label="操作对象:"  
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.operand" placeholder="请输入操作对象"></el-input>
      </el-form-item>
      <el-form-item
        label="操作结果:"  
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.result" placeholder="请输入操作结果"></el-input>
      </el-form-item>
      <el-form-item
        label="详情描述:"  
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input type="textarea" :rows="10" clearable :disabled='ProhibitFrom' autocomplete="off" v-model="ruleForm.description" placeholder="请输入详情描述"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if='!ProhibitFrom' type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { findById } from '@/api/omManger/log'
import { isMPStrict } from '@/utils/validate'
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    modalObjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      ruleForm: {
        tel: '',
        name: '',   
        remark: '',
        roleId: '',
        organization: ''
      },
      obj: {},
      rule: {},
      options: [],
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
  created() {
    // alert(1)
    // this.findNodes()
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

    selectGet(vId){
      const that = this 
      that.obj = this.options.find((item)=>{//this.ruleForm
        return item.value === vId;//筛选出匹配数据
      });
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
        if(response.data.data.result === '1'){
            response.data.data.result = '成功'
        }else if(response.data.data.result === '0'){
            response.data.data.result = '失败'
        }
        that.ruleForm = response.data.data
        if (this.modalObjj === '详情') {
          that.ProhibitFrom = true
          // 禁用编辑器
        } else if (this.modalObjj === '编辑') {
          that.ProhibitFromm = false
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
      that.ruleForm.tel = ''
      that.ruleForm.remark = ''
      that.ruleForm.name = ''
      that.ruleForm.roleId = ''
      that.ruleForm.organization = ''
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
        if (this.modalObjj === '添加') {
          that.add(that.ruleForm)
          return false
        } else if (this.modalObjj === '编辑') {
          that.ruleForm.id = that.nowId
          that.edit(that.ruleForm)
          return false
        }
      })
    },

    // 添加方法
    async add(data) {
      const that = this
      // POST请求添加轮播图数据
      let da = data
      da.roleName = that.obj.label
      that.loading = true
      const response = await insert(da)
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
    async edit(data) {
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
    .el-drawer__body {
      overflow: auto;
      padding: 10px 20px !important;
    }
  .el-drawer {
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
