<template>
  <el-contain>
    <div class="data_field_container" v-loading="loading">
      <el-row>
        <el-col>
          <div class="data_field_container">
            <i class="el-icon-s-data" style="height: 34px;width:34px;"></i>
            <span
                style="display:inline-block;height: 34px;line-height:34px;flex-shrink: 0;font-size:18px;">{{ tableName }}</span>
            <el-button size="medium" icon="el-icon-back" class="header_unit" @click="goBack">返回列表</el-button>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-table
              :data="dataUnitField"
              stripe
              style="width: 100%;min-height:64vh;margin-top: 15px;"
              :header-cell-style="headerStyle"
              :cell-style="cellStyle"
              class='el_tab_alage'>
            <el-table-column
                align="center"
                prop="columnName"
                label="字段名">
            </el-table-column>
            <el-table-column
                align="center"
                prop="columnType"
                label="字段类型">
            </el-table-column>
            <el-table-column
                align="center"
                prop="columnKey"
                label="是否为主键">
              <template slot-scope="scope">
                <template v-if="scope.row.columnKey">
                  是
                </template>
                <template v-else>
                  否
                </template>
              </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="isNullable"
                label="是否可以为空">
              <template slot-scope="scope">
                <template v-if='scope.row.isNullable === "YES"'>
                  是
                </template>
                <template v-else>
                  否
                </template>
              </template>
            </el-table-column>
            <el-table-column
                align="center"
                prop="hasReplace"
                label="是否脱敏">
              <template slot-scope="scope">
                <template v-if='scope.row.hasReplace === 1'>
                  是
                </template>
                <template v-else>
                  否
                </template>
              </template>
            </el-table-column>
            <el-table-column align="center" label="操作" min-width="70px">
              <template slot-scope="scope">
                <el-button type="text" icon="el-icon-search" @click="updateReplaceRule(scope.row)">数据脱敏</el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
        </el-col>

        <el-dialog title="脱敏" :visible.sync="dialogForm" :before-close="closeForm" center>
          <el-form :model="ruleForm" :rules="rules" ref="replaceForm" label-width="80px" class="demo-ruleForm">
            <el-row style="margin-left: 80px">
              <el-form-item label="字段名称:">
                <el-input clearable size="medium" v-model="ruleForm.columnName" disabled="true" style="width: 400px"></el-input>
              </el-form-item>
              <el-form-item label="脱敏规则:" prop='ruleName'>
                <el-select v-model="ruleForm.ruleName" @change="checkReplaceRules" placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in replaceRules" :key="item.name" :label="item.name" :value="item">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="是否脱敏:" prop='hasReplace'>
                <el-radio-group v-model="ruleForm.hasReplace">
                  <el-radio :label="0">否</el-radio>
                  <el-radio :label="1">是</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-row>
          </el-form>
          <div style="margin-top: 20px;position: relative;left:38%">
            <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
            <el-button size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
          </div>
        </el-dialog>
      </el-row>
    </div>
  </el-contain>
</template>

<script>
import {getDataUnitFields, updateDataUnitFieldReplace, getReplaceRuleList} from "@/api/sourceManage.js";
import Pagination from '@/components/table/Pagination'

export default {
  components: {Pagination},
  data() {
    return {
      loading: false,
      dataUnitId: '',
      tableName: '',
      dataSourceId: '',
      dataBaseName: '',
      dialogVisible: false,
      dialogForm: false,
      dataUnitField: [],
      replaceRules: [],
      total: 0,
      pageSize: '20',
      currentPage: '1',
      ruleForm: {
        id: '',
        columnName: '',
        ruleName: '',
        hasReplace: 0,
        replaceSql: ''
      },
      rules: {
        ruleName: [{
          required: true,
          message: "请选择规则名称",
          trigger: "change"
        }]
      },
      type: ''
    }
  },
  methods: {
    //表格样式
    headerStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    cellStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    async getDataUnitFields() {
      const that = this

      let params = {
        dataUnitId: that.dataUnitId,
        size: that.pageSize,
        page: that.currentPage
      }

      that.loading = true
      let response = await getDataUnitFields(params)
      that.loading = false

      if (response.data.code === 1) {
        that.dataUnitField = response.data.data.content
        that.total = response.data.data.totalElements
      } else {
        that.$message.error(response.data.msg)
      }
    },
    // 翻页
    pageChange(item) {
      let that = this
      that.pageSize = item.limit
      that.currentPage = item.page
      that.getDataUnitFields()
    },
    //返回上一级
    goBack() {
      const that = this

      that.$router.push({
        path: '/datasourceDataUnit',
        query: {
          dataSourceId: that.dataSourceId,
          dataBaseName: that.dataBaseName
        }
      })
    },
    closeForm() {
      const that = this
      that.ruleForm.id = ''
      that.ruleForm.columnName = ''
      that.ruleForm.ruleName = ''
      that.ruleForm.hasReplace = 0
      that.ruleForm.replaceSql = ''
      that.dialogForm = false
    },
    resetForm(ruleForm) {
      this.$refs["replaceForm"].resetFields();
      this.closeForm();
    },
    submitForm(ruleForm) {
      this.$refs["replaceForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let replaces = JSON.parse(that.ruleForm.replaceSql)
          let sqlValue = ''
          if(that.type === 'Oracle'){
            sqlValue = replaces.Oracle
          }
          if(that.type === 'Mysql'){
            sqlValue = replaces.Mysql
          }
          if(that.type === 'Postgresql'){
            sqlValue = replaces.Postgresql
          }
          if(that.type === 'Sqlserver'){
            sqlValue = replaces.Sqlserver
          }
          let reg=new RegExp('field_name_xxx','g')//g代表全部
          that.ruleForm.replaceSql = sqlValue.replace(reg, that.ruleForm.columnName)
          let data = that.ruleForm

          updateDataUnitFieldReplace(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('成功');
              this.resetForm('replaceForm')
              this.getDataUnitFields()
              this.loading = false;
            } else {
              this.$message.error(res.data.msg);
            }
          });
        }
      });
    },
    async getReplaceRules() {
      const that = this

      let params = {}

      that.loading = true
      let response = await getReplaceRuleList(params)
      that.loading = false

      if (response.data.code === 1) {
        that.replaceRules = response.data.data
      } else {
        that.$message.error(response.data.msg)
      }
    },
    updateReplaceRule(row) {
      const that = this
      that.ruleForm.id = row.id
      that.ruleForm.columnName = row.columnName
      that.ruleForm.ruleName = row.ruleName
      if(row.hasReplace!==null){
        that.ruleForm.hasReplace = row.hasReplace
      }
      that.ruleForm.replaceSql = row.replaceSql
      that.dialogForm = true
    },
    checkReplaceRules(event){
      console.log(event)
      const that = this
      that.ruleForm.ruleName = event.name
      that.ruleForm.replaceSql = event.sqlValue
    }
  },
  created() {
    const that = this
    that.dataUnitId = that.$route.query.dataUnitId
    that.tableName = that.$route.query.tableName
    that.dataSourceId = that.$route.query.dataSourceId
    that.dataBaseName = that.$route.query.dataBaseName
    that.type = that.$route.query.type

    that.getDataUnitFields()
    this.getReplaceRules()
  }
}
</script>

<style>
.data_field_container {
  padding: 15px;
}
</style>
