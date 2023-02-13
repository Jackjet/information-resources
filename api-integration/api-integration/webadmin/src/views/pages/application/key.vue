<template>
  <el-drawer title="密钥管理" direction="rtl" v-loading="loading" :visible.sync="dialogFormVisible" :before-close="ClearTextConeten" :wrapperClosable='false' ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" v-loading='loading'>
      <el-form-item prop="id" label="应用ID" :label-width="this.formLabelWidth" class='InpitWidth'>
        <el-input disabled v-model="ruleForm.id"></el-input>
      </el-form-item>
      <el-form-item prop="key" label="密钥" :label-width="this.formLabelWidth" class='InpitWidth'>
        <el-input :type="keyType" disabled v-model="ruleForm.key">
          <i class="el-icon-view el-input__icon" slot="suffix" style="cursor: pointer" @click="hideOrShow">
          </i>
        </el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button icon="el-icon-back" @click="CloseModal">返 回</el-button>
      <el-button icon="el-icon-refresh" @click="updateKey">更新密钥</el-button>
    </div>
  </el-drawer>
</template>

<script>
import { findById, updateKey } from '@/api/application'
export default {
  data() {
    return {
      ruleForm: {
        id: '',
        secret: ''
      },

      dialogFormVisible: false,
      formLabelWidth: '120px',
      loading: false,
      keyType: 'password'
    }
  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function (id) {
      const that = this
      that.dialogFormVisible = true
      that.initFormDetail(id)
    },

    /**
     * 初始化获取当前需要编辑 查看的详细数据
     * @param value 当前需要操作的唯一iD
     */
    async initFormDetail(id) {
      const that = this
      try {
        let parameter = { id: id }
        that.loading = true
        const response = await findById(parameter)
        that.loading = false

        if (response.data.code === 1) {
          // 查询成功后 回调效果
          that.ruleForm = response.data.data
          console.log(that.ruleForm.name)
        } else {
          // 查询失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal() {
      const that = this
      that.ClearTextConeten()
    },

    // 初始化表单
    ClearTextConeten() {
      const that = this
      that.$refs['ruleForm'].resetFields()
      that.ruleForm.id = ''
      that.ruleForm.secret = ''
      that.dialogFormVisible = false
    },

    async updateKey() {
      const that = this
      let response = await updateKey({ id: this.ruleForm.id })
      if (response.data.code === 1) {
        // 删除成功后 回调效果
        that.$message.success(response.data.msg)
        that.initFormDetail(this.ruleForm.id)
      } else {
        // 删除失败后 回调失败信息
        that.$message.error(response.data.msg)
      }
    },
    hideOrShow() {
      if (this.keyType === 'password') {
        this.keyType = 'text'
      } else {
        this.keyType = 'password'
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
  .avatar-border-red .el-upload {
    border: 1px dashed #f56c6c !important;
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
  border: 1px solid #e4e7ed;
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
.a {
  width: 6vw !important;
}
.drawer_footer {
  text-align: center;
}
:focus {
  outline: 0;
}
</style>
