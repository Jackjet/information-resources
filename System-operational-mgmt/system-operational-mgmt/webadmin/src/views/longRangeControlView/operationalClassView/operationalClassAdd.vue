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
      <el-form-item class="InpitWidth" label="类型名称:" prop="name">
        <el-input clearable autocomplete="off" v-model="ruleForm.name" placeholder="请输入类型名称" v-if="id === ''"></el-input>
        <span v-else>{{ruleForm.name}}</span>
      </el-form-item>
      <el-form-item class="InpitWidth" label="权限:">
        <el-input clearable autocomplete="off" disabled v-model="ruleForm.roleNames" placeholder="请选择权限">
          <el-button slot="append" icon="el-icon-circle-plus" @click="roleClick">选择</el-button>
        </el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="备注:">
        <el-input placeholder="请输入备注" v-model="ruleForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
    <el-dialog title="权限" :before-close='cancel' append-to-body :visible.sync="isShow">
      <el-table
        ref="multipleTable"
        v-loading="loading"
        :data='table.list.data'
        empty-text='暂无数据'
        class='el_tab_alage'
        :row-key='getRowKeys'
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" :reserve-selection="true" width="50" align="center"></el-table-column>
        <template v-for="(item, index) in table.list.header">
          <el-table-column fit
            align='center'
            :key='index'
            :label="item.label"
            :width="item.width"
            :prop="item.field">
            <template slot-scope="scope">
              {{scope.row[item.field]}}
            </template>
          </el-table-column>
        </template>
      </el-table>
      <div style="text-align: center;">
        <el-button type="primary" @click="cancel">取消</el-button>
        <el-button type="primary" @click="submit">确定</el-button>
      </div>
    </el-dialog>
  </el-drawer>
</template>

<script>

import { commandTypeInsert, commandTypeFindById, commandTypeUpdate, commandTypeFindRoles } from '@/api/longRangeControlView/http'
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
      ruleForm: {
        name: '',
        roleIds: '',
        roleNames: '',
        remark: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入类型名称',
          trigger: ['change', 'blur']
        }]
      },
      isShow: false,
      // 勾选数据
      tableArr: [],
      table: {
        list: {
          header: [
            {
              label: '角色名称',
              field: 'name',
              width: '100'
            },
            {
              label: '描述',
              field: 'remark',
              width: '110'
            },
            {
              label: '创建时间',
              field: 'createTime'
            }
          ],

          data: []
        }
      },
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    getRowKeys (row) {
      return row.id
    },
    handleSelectionChange (row) {
      // 只要id
      let arr = []
      row.forEach(item => {
        arr.push(item.id)
      })
      this.tableArr = arr
    },
    cancel () {
      this.$refs.multipleTable.clearSelection()
      this.isShow = false
    },
    submit () {
      // 每次选择清空上一次
      this.isShow = false
      let ids = ''
      let names = ''
      // let newArr = Array.from(new Set(this.tableArr))
      this.table.list.data.forEach(item => {
        this.tableArr.forEach(row => {
          if (item.id === row) {
            ids += item.id + ','
            names += item.name + ','
          }
        })
      })
      this.ruleForm.roleIds = ids.substring(0, ids.length - 1)
      this.ruleForm.roleNames = names.substring(0, names.length - 1)
      // 确定权限后清空勾选状态
      this.$refs.multipleTable.clearSelection()
    },
    roleClick () {
      this.isShow = true
      this.commandTypeFindRoles()
    },
    // 获取权限
    async commandTypeFindRoles () {
      const res = await commandTypeFindRoles()
      this.table.list.data = res.data.data
      let arr = this.ruleForm.roleIds.split(',')
      let newArr = []
      // 获取勾选id
      res.data.data.forEach(item => {
        arr.forEach(row => {
          if (item.id === row) {
            newArr.push(item)
          }
        })
      })
      // 设置选中
      newArr.forEach(row => {
        this.$refs.multipleTable.toggleRowSelection(row, true)
      })
    },
    // 初始化添加
    initial (value) {
      this.dialogFormVisible = true
      this.id = value
      if (value) {
        this.details()
      }
    },
    async details () {
      const res = await commandTypeFindById({ id: this.id })
      this.ruleForm = res.data.data
    },

    CloseModal () {
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.ruleForm = {
        name: '',
        roleIds: '',
        roleNames: '',
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
        let data = {
          name: this.ruleForm.name,
          roleIds: this.ruleForm.roleIds,
          roleNames: this.ruleForm.roleNames,
          remark: this.ruleForm.remark
        }
        let response = null
        if (that.id) {
          data.id = that.id
          delete data.name
          response = await commandTypeUpdate(data)
        } else {
          response = await commandTypeInsert(data)
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
