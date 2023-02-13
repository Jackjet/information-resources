<template>
  <el-drawer
    :title="title"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="ClearTextConeten"
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading'>
      <el-form-item
        prop="name"
        label="应用名称"
        :label-width="this.formLabelWidth"
        class='InpitWidth'>
        <el-input clearable autocomplete="off" v-model.trim="ruleForm.name" placeholder="请输入应用名称"></el-input>
      </el-form-item>

      <el-form-item
          prop="container"
          label="选择容器"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
        <el-select v-model="ruleForm.container" placeholder="请选择容器" clearable @click.native="getContainers">
          <el-option v-for="item in containerInfos"
                     :key="item.container"
                     :label="item.name"
                     :value="item.container"
          ></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button icon="el-icon-back" @click="CloseModal">取 消</el-button>
      <el-button icon="el-icon-check" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>
  import { findById,add,update } from '@/api/application'

  import {
    findAllMeta,
    findAllContainer,
    findAllApi
  } from "@/api/sourceService";
  export default {
    data () {
      return {
        ruleForm: {
          id:'',
          name: '',
          container:''
        },
        rules: {
          name: [{
            required: true,
            message: '请输入应用名称',
            trigger: ['change', 'blur']
          }],
          container: [{
            required: true,
            message: '请选择容器',
            trigger: ['change', 'blur']
          }],
        },

        dialogFormVisible: false,
        formLabelWidth: '120px',
        loading: false,

        //容器
        containerInfos:[]
      }
    },
    methods: {
      /**
       * @param value 当前需要操作的数据集
       * @param state 根据不同状态 展示不同操作
       */
      initial: function (title,id) {
        const that = this
        that.dialogFormVisible = true
        that.title = title
        if (id && title) {
          that.nowId = id
          that.title = title
          that.initFormDetail(id)
        } else {
          that.title = title
          that.ProhibitFrom = false
        }
      },

      /**
       * 初始化获取当前需要编辑 查看的详细数据
       * @param value 当前需要操作的唯一iD
       */
      async initFormDetail (id) {
        const that = this
        try {
          let parameter = {id: id}
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
      CloseModal () {
        const that = this
        that.ClearTextConeten()
      },

      // 初始化表单
      ClearTextConeten () {
        const that = this
        that.$refs['ruleForm'].resetFields()
        that.ruleForm.id = ''
        that.ruleForm.name = ''
        that.dialogFormVisible = false
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
          if (this.title === '添加') {
            that.add(that.ruleForm)
            return false
          } else if (this.title === '编辑') {
            that.ruleForm.id = that.nowId
            that.edit(that.ruleForm)
            return false
          }
        })
      },

      // 添加方法
      async add(data) {
        const that = this

        that.loading = true
        const response = await add(data)
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
        that.loading = true
        const response = await update(data)
        that.loading = false
        if (response.data.code === 1) {

          that.$message.success(response.data.msg)
          that.ClearTextConeten()
          that.$emit('Reload')
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      },

      //容器查询
      async getContainers(){
        const that = this
        let param= {
          page:1,
          size:100000,
          type:3
        }
        let res = await findAllContainer(param)

        console.log('res',res)
        if (res.data.code == 1) {
          that.containerInfos = res.data.data.content
          let containerInfos = that.containerInfos

          containerInfos.forEach((item,i) => {
            item.container = item.ip + ':' + item.port
          })

          that.containerInfos = containerInfos
        }
      },
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
