<template>
  <el-drawer
    :title="modalObjjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="CloseModal"
    custom-class="demo-drawer"
    size='45%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm"  v-loading='loading' label-width="100px">
      <el-form-item class="InpitWidth" label="指令类型:" prop="typeId">
        <el-select style="width:100%;" clearable size="medium" v-model="ruleForm.typeId" placeholder="请选择指令类型">
          <el-option
            v-for="item in typeList"
            :label="item.name"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="InpitWidth" label="指令内容:" prop="content">
        <el-input clearable autocomplete="off" v-model="ruleForm.content" placeholder="请输入指令内容"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="备注:">
        <el-input placeholder="请输入备注" v-model="ruleForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>

import { commandsInsert, commandsFindById, commandsUpdate, commandsFindCommandTypes } from '@/api/longRangeControlView/http'
export default {
  props: {
    modalObjjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      id: '',
      isShow: true,
      typeList: [],
      ruleForm: {
        typeId: '',
        type: '',
        content: '',
        remark: ''
      },
      rules: {
        typeId: [{
          required: true,
          message: '请选择指令类型',
          trigger: ['change', 'blur']
        }],
        content: [{
          required: true,
          message: '请输入指令内容',
          trigger: ['blur']
        }]
      },
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    initial (value) {
      this.typeList = []
      this.dialogFormVisible = true
      this.id = value
      this.commandsFindCommandTypes()
    },
    async commandsFindCommandTypes () {
      let data = {
        page: '1',
        size: '1000'
      }
      const res = await commandsFindCommandTypes(data)
      res.data.data.forEach(item => {
        this.typeList.push({
          id: item.id,
          name: item.name
        })
        if (this.id) {
          this.details()
        }
      })
    },
    async details () {
      const res = await commandsFindById({ id: this.id })
      this.ruleForm = res.data.data
    },

    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.ruleForm = {
        typeId: '',
        type: '',
        content: '',
        remark: ''
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate(async (index) => {
        if (index === false) {
          return false
        }
        that.loading = true
        let type = ''
        this.typeList.forEach(item => {
          if (this.ruleForm.typeId === item.id) {
            type = item.name
          }
        })
        let data = {
          name: this.ruleForm.name,
          typeId: this.ruleForm.typeId,
          type: type,
          content: this.ruleForm.content,
          remark: this.ruleForm.remark
        }
        let response = null
        if (that.id) {
          data.id = that.id
          response = await commandsUpdate(data)
        } else {
          response = await commandsInsert(data)
        }
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.$emit('getList')
          that.CloseModal()
          return false
        }
        that.$message.error(response.data.msg)
      })
    }
  }
}
</script>

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
