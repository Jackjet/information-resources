<template>
  <el-main class="main">
    <div>
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm" v-loading="loading">

        <el-form-item label="规则名称:" :label-width="this.formLabelWidth" prop='name'>
          <el-input clearable maxlength='100' size="medium" :disabled="disabled" placeholder="请输入规则名称" v-model="ruleForm.name" />
        </el-form-item>

        <el-form-item label="数据模板:" :label-width="this.formLabelWidth" prop='ruleTemplateId'>
          <el-select style="width: 100%;" v-model="ruleForm.ruleTemplateId" :disabled='disabled' @change="handleChange(ruleForm.ruleTemplateId)">
            <el-option v-for="(v,i) in modelOptions" :key='i' :label='v.name' :value="v.id+'-'+v.name" />
          </el-select>
        </el-form-item>

        <el-form-item label="元数据:" :label-width="this.formLabelWidth" prop='metadataData' v-if='modelLabel != "等值一致性依赖约束" &&  modelLabel != "逻辑一致性依赖约束" &&  modelLabel != "取值准确性约束"'>
          <el-cascader @focus="getBlur()" @change="handleCascaderChange" @expand-change="handleItemChange" :disabled='disabled2' style="width: 100%;" maxlength='100' v-model='ruleForm.metadataData' :only-last='true' :options="options1" :props="props" :show-all-levels="false" clearable></el-cascader>
        </el-form-item>
        <el-form-item label="元数据主键:" prop="key" :label-width="this.formLabelWidth" v-if=' modelLabel == "取值准确性约束"'>
          <el-col :span="12">
            <el-cascader :disabled="disabled" @expand-change="handleItemChange" @change='getCheckedNodes1()' v-model='ruleForm.key' :options="options1" :props="props" :show-all-levels="false" clearable></el-cascader>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="keyValue" label="客观实体值:" :label-width="this.formLabelWidth">
              <el-input :disabled="disabled" @input='getCheckedNodes3()' v-model="ruleForm.keyValue"></el-input>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="元数据主键:" prop="key" :label-width="this.formLabelWidth" v-if='modelLabel == "等值一致性依赖约束" ||  modelLabel == "逻辑一致性依赖约束"'>
          <el-col :span="12">
            <el-cascader @change='getCheckedNodes1()' @expand-change="handleItemChange" :disabled='disabled' v-model='ruleForm.key' :options="options1" :props="props" :show-all-levels="false" clearable></el-cascader>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="targetKey" label="比较元数据主键:" :label-width="this.formLabelWidth">
              <el-input v-if="title == '编辑'" maxlength='100' size="medium" :disabled="disabled" v-model="ruleForm.targetKey" />
              <el-cascader v-else @change='getCheckedNodes2()' @expand-change="handleItemChange1" :disabled='thenDisabled' v-model='ruleForm.targetKey' :options="thenOptions" :props="props" :show-all-levels="false" clearable></el-cascader>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item v-if='modelLabel == "等值一致性依赖约束"' v-for='(v,i) in ruleForm.metaDatas' :key='i' :label="i== 0 ?'检核元数据:' : ''" style="position:relative" :label-width="v.formLabelWidth">
          <el-col :span="12">
            <el-input v-if="title == '编辑'" style='width:220px' maxlength='100' size="medium" :disabled="v.disabled1" v-model="v.check" />
            <el-cascader v-else :disabled='v.disabled1' @change='v.change(v.check,i)' maxlength='100' v-model='v.check' :options="v.options2" :props="props" :show-all-levels="false" clearable></el-cascader>
          </el-col>
          <el-col :span="12">
            <el-form-item label='比较元数据:' :label-width="v.formLabelWidth">
              <el-input v-if="title == '编辑'" maxlength='100' size="medium" :disabled="v.disabled2" v-model="v.targetCheck" />
              <el-cascader ref="rule" v-else :disabled='v.disabled2' @change='v.change(v.targetCheck,i)' maxlength='100' v-model='v.targetCheck' :options="v.options3" :props="props" :show-all-levels="false" clearable></el-cascader>
            </el-form-item>
          </el-col>
          <i class="el-icon-plus" style="position:absolute;right:-30px;top:15px" v-if="i== 0" @click="addData"></i>
          <i class="el-icon-delete" style="position:absolute;right:-30px;top:15px" v-if="i!= 0" @click="delData(i)"></i>
        </el-form-item>
        <el-form-item v-if='modelLabel == "逻辑一致性依赖约束"' v-for='(v,i) in ruleForm.metaDatas' :key='i' :label="i== 0 ?'检核元数据:' : ''" style="position:relative" :label-width="v.formLabelWidth">
          <el-col :span="8">
            <el-input v-if="title == '编辑'" style='width:220px' maxlength='100' size="medium" :disabled="v.disabled1" v-model="v.check" />
            <el-cascader v-else :disabled='v.disabled1' @change='v.change(v.check,i)' maxlength='100' v-model='v.check' :options="v.options2" :props="props" :show-all-levels="false" clearable></el-cascader>
          </el-col>
          <el-col :span="8">
            <el-form-item label='比较元数据:' :label-width="v.formLabelWidth">
              <el-input v-if="title == '编辑'" maxlength='100' size="medium" :disabled="v.disabled2" v-model="v.targetCheck" />
              <el-cascader v-else ref="rule" :disabled='v.disabled2' @change='v.change(v.targetCheck,i)' maxlength='100' v-model='v.targetCheck' :options="v.options3" :props="props" :show-all-levels="false" clearable></el-cascader>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label='约束条件:' :label-width="v.formLabelWidth">
              <el-select :disabled='v.disabled2' v-model="v.range">
                <el-option key='0' label="大于" value='>' />
                <el-option key='1' label="大于等于" value='>=' />
                <el-option key='2' label="小于" value='<' />
                <el-option key='3' label="小于等于" value='<=' />
              </el-select>
            </el-form-item>
          </el-col>
          <i class="el-icon-plus" style="position:absolute;right:-30px;top:15px" v-if="i== 0" @click="addData"></i>
          <i class="el-icon-delete" style="position:absolute;right:-30px;top:15px" v-if="i!= 0" @click="delData(i)"></i>
        </el-form-item>
        <el-form-item v-if='modelLabel == "取值准确性约束"' v-for='(v,i) in ruleForm.metaDatas' :key='i' :label="i== 0 ?'检核元数据:' : ''" style="position:relative" :label-width="v.formLabelWidth">
          <el-col :span="12">
            <el-input v-if="title == '编辑'" style='width:220px' maxlength='100' size="medium" :disabled="v.disabled1" v-model="v.check" />
            <el-cascader v-else :disabled='v.disabled1' @change='v.change(v.check,i)' maxlength='100' v-model='v.check' :options="v.options2" :props="props" :show-all-levels="false" clearable></el-cascader>
          </el-col>
          <el-col :span="12">
            <el-form-item label='准确值：' :label-width="v.formLabelWidth">
              <el-input :disabled='v.disabled2' v-model="v.keyValue"></el-input>
            </el-form-item>
          </el-col>
          <i class="el-icon-plus" style="position:absolute;right:-30px;top:15px" v-if="i== 0" @click="addData"></i>
          <i class="el-icon-delete" style="position:absolute;right:-30px;top:15px" v-if="i!= 0" @click="delData(i)"></i>
        </el-form-item>
        <el-form-item label="比较元数据:" :label-width="this.formLabelWidth" prop='target' v-if='modelLabel == "代码值域约束" '>
          <el-cascader style="width: 100%;" @expand-change="handleItemChange1" :disabled='thenDisabled' maxlength='100' v-model='ruleForm.target' :options="thenOptions" :props="props" :show-all-levels="false" clearable></el-cascader>
        </el-form-item>
        <el-form-item label="" :label-width="this.formLabelWidth">
          <div class="local">
            <img src="../../../../assets/image/local.png" alt="">
            <div>{{miaoshu}}</div>
          </div>
        </el-form-item>
        <el-form-item label="联合为空:" :label-width="this.formLabelWidth" v-if='modelLabel == "完整性非空约束"'>
          <el-checkbox :disabled="disabled" v-model="checked">勾选表示检查字段可以单个为空</el-checkbox>
        </el-form-item>
        <el-form-item label="约束长度:" :label-width="this.formLabelWidth" v-if='modelLabel == "长度约束"'>
          <el-col :span="11">
            <el-form-item prop="range">
              <el-select style="" v-model="ruleForm.range" :disabled='disabled'>
                <el-option key='0' label="大于" value='>' />
                <el-option key='1' label="大于等于" value='>=' />
                <el-option key='2' label="小于" value='<' />
                <el-option key='3' label="小于等于" value='<=' />
                <el-option key='4' label="等于" value='=' />
                <el-option key='5' label="不等于" value='!=' />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item prop="lang">
              <el-input style="width:100px" :disabled='disabled' v-model="ruleForm.lang"></el-input><span style="padding-left:20px;color:gray">字符</span>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="取值范围:" :label-width="this.formLabelWidth" v-if='modelLabel == "取值范围约束"'>
          <el-col :span="15">
            <el-form-item prop="valueRange">
              <el-select v-model="ruleForm.valueRange" :disabled='disabled' @change="handleChange1(ruleForm.valueRange)">
                <el-option key='0' label="等于" value='=' />
                <el-option key='1' label="不等于" value='!=' />
                <el-option key='2' label="大于" value='>' />
                <el-option key='3' label="大于等于" value='>=' />
                <el-option key='4' label="小于" value='<' />
                <el-option key='5' label="小于等于" value='<=' />
                <el-option key='6' label="在....和....之间" value='between' />
                <el-option key='7' label="不在....和.....之间" value='not_between' />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="9" v-if='show'>
            <el-form-item prop="value1">
              <el-input :disabled='disabled' v-model="ruleForm.value1"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="4" v-if='show1'>
            <el-form-item prop="value2">
              <el-input :disabled='disabled' v-model="ruleForm.value2"></el-input>
            </el-form-item>
          </el-col>
          <el-col v-if='show1' class="line" style="text-align:center" :span="1">-</el-col>
          <el-col :span="4" v-if='show1'>
            <el-form-item prop="value3">
              <el-input :disabled='disabled' v-model="ruleForm.value3"></el-input>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="约束值:" :label-width="this.formLabelWidth" v-if='modelLabel == "标志取值约束"' prop='bound'>
          <el-input clearable maxlength='100' size="medium" :disabled="disabled" placeholder="请用,隔开" v-model="ruleForm.bound" />
        </el-form-item>
        <el-form-item label="规范内容:" :label-width="this.formLabelWidth" v-if='modelLabel == "内容规范约束"' prop='regs'>
          <el-input maxlength='200' type="textarea" :rows="3" :placeholder="remarkMsg1" clearable size="medium" :disabled="disabled" v-model="ruleForm.regs"></el-input>
        </el-form-item>
        <el-form-item label="规则描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' type="textarea" :rows="3" :placeholder="remarkMsg" clearable size="medium" v-model="ruleForm.description"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button v-if="isHistory" size="medium" @click="historyData">变更历史</el-button>
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
    <el-dialog title="变更历史" :visible.sync="dialogTableVisible">
      <el-table :data="historyList">
        <el-table-column property="name" label="规则名称"></el-table-column>
        <el-table-column property="ruleTemplateName" label="数据模板"></el-table-column>
        <el-table-column property="sourceName" label="规则数据源"></el-table-column>
        <el-table-column property="sourceData" label="规则实体表"></el-table-column>
        <el-table-column property="createByName" label="创建人"></el-table-column>
        <el-table-column property="description" label="规则描述"></el-table-column>
        <el-table-column property="updatetime" label="变更时间"></el-table-column>
      </el-table>
    </el-dialog>
  </el-main>
</template>
<script>
import { ruleTemplateFindAll } from "@/api/ruleTemplate.js"
import { datasourceFindAll, dataSourceById, dataSourceByUnitId, verifyRuleInsert, updateVerifyRule, verifyRuleLogCountByRuleId, verifyRuleLogFindByRuleId } from "@/api/quality.js"
import $ from 'jquery'
export default {
  data() {
    return {
      isHistory: false,
      dialogTableVisible: false,
      historyList: [],
      title: '新增',
      id: '',
      thenDisabled: true,
      modelId: '',
      modelLabel: '',
      modelOptions: [],
      options1: [],
      thenOptions: [],
      options2: [],
      disabled: false,
      disabled2: true,
      props: {
        multiple: true,
        checkStrictly: false
      },
      miaoshu: '检查实体在系统中是否有重复记录',
      disabled1: true,
      metadataData: '',
      checked: false,
      range: '',
      remarkMsg1: '请输入正则表达式:',
      disabledo: false,
      ruleForm: {
        ruleTemplateId: '',
        metadataData: '',
        name: "",
        description: '',
        lang: '',
        range: '',
        bound: '',
        valueRange: '',
        value1: '',
        value2: '',
        value3: '',
        regs: '',
        target: '',
        key: '',
        targetKey: '',
        check: '',
        targetCheck: '',
        metaDatas: [
          {
            formLabelWidth: "130px",
            id: 1,
            disabled1: true,
            disabled2: true,
            check: '',
            targetCheck: '',
            range: '',
            options2: [],
            options3: [],
            change: (val, index) => {
              this.ruleForm.metaDatas.length = index + 1
            }
          }
        ],
      },
      metaDatasId: 1,
      options: [],
      formLabelWidth: "130px",
      remarkMsg: '请输入规则描述:',
      rules: {
        name: [{
          required: true,
          message: "请输入规则名称",
          trigger: "change",
        }],
        ruleTemplateId: [{
          required: true,
          message: "请选择数据模板",
          trigger: "change",
        }],
        metadataData: [{
          required: true,
          message: '请选择元数据',
          trigger: 'change'
        }],
        target: [{
          required: true,
          message: '请选择比较元数据',
          trigger: 'change'
        }],
        lang: [{
          required: true,
          message: '请输入长度约束',
          trigger: 'change'
        }, {
          pattern: /^[0-9]*$/,
          message: '长度只能输入数字',
          trigger: 'change'
        }],
        bound: [{
          required: true,
          message: '请输入约束值',
          trigger: 'change'
        }],
        valueRange: [{
          required: true,
          message: '请选择范围',
          trigger: 'change'
        }],
        value1: [{
          required: true,
          message: '请输入范围',
          trigger: 'change'
        }, {
          pattern: /^[0-9]*$/,
          message: '长度只能输入数字',
          trigger: 'change'
        }],
        value2: [{
          required: true,
          message: '请输入范围',
          trigger: 'change'
        }, {
          pattern: /^[0-9]*$/,
          message: '长度只能输入数字',
          trigger: 'change'
        }],
        value3: [{
          required: true,
          message: '请输入范围',
          trigger: 'change'
        }, {
          pattern: /^[0-9]*$/,
          message: '长度只能输入数字',
          trigger: 'change'
        }],
        regs: [{
          required: true,
          message: '请输入正则表达式',
          trigger: 'change'
        }],
        key: [{
          required: true,
          message: '请选择元数据主键',
          trigger: 'change'
        }],
        targetKey: [{
          required: true,
          message: '请选择比较元数据主键',
          trigger: 'change'
        }],
        name1: [{
          required: true,
          message: '请选择核检元数据',
          trigger: 'change'
        }],
        name2: [{
          required: true,
          message: '请选择比较元数据',
          trigger: 'change'
        }],
        keyValue: [{
          required: true,
          message: '请输入客观实体值',
          trigger: 'change'
        }],
      },
      loading: false,
      leftData: [],
      rightData: [],
      childNum: 0,
      shareScopeEnd: [],
      oneId: ''
    }
  },
  created() {
    this.title = this.$route.query.type
    this.findDataSourceId()
    this.fetchData()
    if (this.$route.query.type === '编辑') {
      // let data = JSON.parse(this.$route.query.data)
      this.id = this.$route.query.id
      // this.detail(data)
    } else if (this.$route.query.type === '新增') {
      this.disabledo = false
      this.disabled = false
    }
    // 变更历史数量
    verifyRuleLogCountByRuleId({ verifyruleid: this.$route.query.id }).then(res => {
      if (res.data.data.count > 0) {
        this.isHistory = true;
      }
    })
    // 变更历史列表
    verifyRuleLogFindByRuleId({ verifyruleid: this.$route.query.id }).then(res => {
      if (res.data.code === 1) {
        res.data.data.forEach(item => {
          let obj = JSON.parse(item.updatalog);
          obj.updatetime = item.updatetime;
          obj.sourceName = JSON.parse(obj.metadataData).source.name
          obj.sourceData = JSON.parse(obj.metadataData).sourceName
          this.historyList.push(obj);
        })
      }
    })
  },
  methods: {
    historyData() {
      this.dialogTableVisible = true;
    },
    // 获得焦点
    getBlur() {
      this.$nextTick(() => {
        $('.el-cascader-menu').eq(0).find('.el-checkbox__input').hide()
        $('.el-cascader-menu').eq(1).find('.el-checkbox__input').hide()
      });
    },
    // 获取模板列表
    async fetchData() {
      const that = this
      try {
        const res = await ruleTemplateFindAll({ status: 1, type: 0 })
        if (res.data.code === 1) {
          this.modelOptions = res.data.data.content
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    handleCascaderChange() {
      if (this.modelLabel == '代码值域约束') {
        this.thenDisabled = false
        this.ruleForm.target = ''
        this.thenOptions = []
        this.options1.forEach((v, i) => {
          let val = JSON.parse(JSON.stringify(v))
          this.thenOptions.push(val)
          if (v.value != this.ruleForm.metadataData[0]) {
            this.thenOptions[i].disabled = true
          }
        })
      }
    },
    handleItemChange(val) {
      if (this.modelLabel == '完整性非空约束' || this.modelLabel == '实体唯一性约束' || this.modelLabel == '长度约束') {
        this.ruleForm.metadataData = ''
      }
      // 二级数据
      if (val.length == 1) {
        let data = val[0].split('-')
        this.oneId = val[0]
        let child = []
        dataSourceById({ dataSourceId: data[0] }).then((res) => {
          if (res.data.code == 1) {
            let datas1 = res.data.data
            datas1.forEach((v, ind) => {
              let obj1 = {
                value: v.id + '-' + v.name,
                label: v.name
              }
              let children = []
              obj1.children = children
              child.push(obj1)
            })
            this.options1.forEach((v, i) => {
              if (v.value == val[0]) {
                v.children = child
              }
            })
          }
          this.$nextTick(() => {
            $('.el-cascader-menu').eq(0).find('.el-checkbox__input').hide()
            $('.el-cascader-menu').eq(1).find('.el-checkbox__input').hide()
          });
        })

      }
      if (val.length == 2) {
        let child = []
        let data = val[1].split('-')
        let children = []
        dataSourceByUnitId({ dataUnitId: data[0] }).then((respon) => {
          if (respon.data.code == 1) {
            let datas2 = respon.data.data
            if (datas2.length > 0) {
              datas2.forEach((value, index) => {
                // let label = value.columnComment ? value.columnName+'('+value.columnComment+')' : value.columnName
                let label = value.columnName
                let obj2 = {
                  value: value.columnName + '-' + value.columnType,
                  label: label
                }
                children.push(obj2)
              })
            }
            this.options1.forEach((v, i) => {
              if (v.value == this.oneId) {
                // v.children = child
                v.children.forEach((v, i) => {
                  if (v.value == val[1]) {
                    v.children = children
                  }
                })
              }
            })
          }
          this.$nextTick(() => {
            $('.el-cascader-menu').eq(0).find('.el-checkbox__input').hide()
            $('.el-cascader-menu').eq(1).find('.el-checkbox__input').hide()
          });
        })

        // obj1.children = children
      }
    },
    handleItemChange1(val) {
      // 二级数据
      if (val.length == 2) {
        let child = []
        let data = val[1].split('-')
        let children = []
        dataSourceByUnitId({ dataUnitId: data[0] }).then((respon) => {
          if (respon.data.code == 1) {
            let datas2 = respon.data.data
            if (datas2.length > 0) {
              datas2.forEach((value, index) => {
                let label = value.columnName
                let obj2 = {
                  value: value.columnName + '-' + value.columnType,
                  label: label
                }
                children.push(obj2)
              })
            }
            this.thenOptions.forEach((v, i) => {
              if (v.value == this.oneId) {
                v.children.forEach((v, i) => {
                  if (v.value == val[1]) {
                    v.children = children
                  }
                })
              }
            })
          }
        })
      }
    },
    // 获取元数据列表
    async findDataSourceId() {
      const response = await datasourceFindAll({ status: 0 })
      let options1 = []
      if (response.data.code == 1) {
        let datas = response.data.data
        datas.forEach((v, i) => {
          let obj = {
            value: v.id + '-' + v.type + '-' + v.name,
            label: v.name
          }
          let child = []
          obj.children = child
          options1.push(obj)
        })
        this.options1 = options1.slice(0)
        this.ruleForm.metaDatas[0].options2 = options1.slice(0)
        this.ruleForm.metaDatas[0].options3 = options1.slice(0)
        if (this.$route.query.type === '编辑') {
          let data = JSON.parse(this.$route.query.data)
          this.detail(data)
        }
      }
    },

    // 回显详情
    detail(data) {
      this.disabled = true
      this.thenDisabled = true
      this.ruleForm.name = data.name
      this.ruleForm.ruleTemplateId = data.ruleTemplateId + '-' + data.ruleTemplateName
      this.modelLabel = data.ruleTemplateName
      this.ruleForm.description = data.description
      let metadataData = JSON.parse(data.metadataData)
      console.log(metadataData)
      let obj = {
        value: metadataData.sourceId,
        label: metadataData.sourceId,
        children: [
          {
            value: metadataData.source.name,
            label: metadataData.source.name,
            children: []
          }
        ]
      }
      this.descriptionFun()
      if (this.modelLabel == '完整性非空约束' || this.modelLabel == '实体唯一性约束' || this.modelLabel == '长度约束') {
        let datas = []
        metadataData.source.fields.forEach((v, i) => {
          let arr = []
          arr.push(metadataData.sourceId)
          arr.push(metadataData.source.name)
          arr.push(v.name)
          datas.push(arr)
          let obj1 = {
            value: v.name,
            label: v.name,
          }
          obj.children[0].children.push(obj1)
        })
        this.options1 = obj
        this.ruleForm.metadataData = datas
      }
      if (this.modelLabel == '标志取值约束' || this.modelLabel == '代码值域约束' || this.modelLabel == '取值范围约束' || this.modelLabel == '内容规范约束') {
        let datas = []
        metadataData.source.fields.forEach((v, i) => {
          datas.push(metadataData.sourceId)
          datas.push(metadataData.source.name)
          datas.push(v.name)
          let obj1 = {
            value: v.name,
            label: v.name,
          }
          obj.children[0].children.push(obj1)
        })
        this.options1 = obj
        this.ruleForm.metadataData = datas
      }
      if (this.modelLabel == '等值一致性依赖约束' || this.modelLabel == '逻辑一致性依赖约束') {
        let datas = []
        datas.push(metadataData.sourceId)
        datas.push(metadataData.source.name)
        datas.push(metadataData.source.fields[0].name)
        let obj1 = {
          value: metadataData.source.fields[0].name,
          label: metadataData.source.fields[0].name,
        }
        obj.children[0].children.push(obj1)
        this.options1 = obj
        this.ruleForm.key = datas
        this.ruleForm.targetKey = metadataData.target.fields[0].name
        this.ruleForm.metaDatas = []
        metadataData.source.fields.forEach((v, i) => {
          if (i != 0) {
            let val = metadataData.target.fields[i]
            let obj = {}
            obj.disabled1 = true
            obj.disabled2 = true
            obj.check = v.name
            obj.targetCheck = val.name ? val.name : ''
            obj.formLabelWidth = "130px"
            if (this.modelLabel == '逻辑一致性依赖约束') {
              obj.range = metadataData.operators[i]
            }
            this.ruleForm.metaDatas.push(obj)
          }
        })
      }
      if (this.modelLabel == '完整性非空约束') {
        this.checked = metadataData.union
      } else if (this.modelLabel == '实体唯一性约束') {
      } else if (this.modelLabel == '长度约束') {
        this.ruleForm.range = metadataData.operators
        this.ruleForm.lang = metadataData.values
      } else if (this.modelLabel == '标志取值约束') {
        this.ruleForm.bound = metadataData.values
      } else if (this.modelLabel == '代码值域约束') {
        let datas = []
        let obj1 = {
          value: metadataData.sourceId,
          label: metadataData.sourceId,
          children: [
            {
              value: metadataData.target.name,
              label: metadataData.target.name,
              children: []
            }
          ]
        }
        metadataData.target.fields.forEach((v, i) => {
          datas.push(metadataData.sourceId)
          datas.push(metadataData.target.name)
          datas.push(v.name)
          let obj2 = {
            value: v.name,
            label: v.name,
          }
          obj1.children[0].children.push(obj2)
          this.thenOptions = obj1
        })
        this.ruleForm.target = datas
      } else if (this.modelLabel == '取值范围约束') {
        this.ruleForm.valueRange = metadataData.operators
        this.handleChange1(this.ruleForm.valueRange)
        if (this.show1) {
          this.ruleForm.value2 = metadataData.values[0]
          this.ruleForm.value3 = metadataData.values[1]
        } else {
          this.ruleForm.value1 = metadataData.values[0]
        }
      } else if (this.modelLabel == '内容规范约束') {
        this.ruleForm.regs = metadataData.values
      } else if (this.modelLabel == '等值一致性依赖约束') {

      } else if (this.modelLabel == '逻辑一致性依赖约束') {

      } else if (this.modelLabel == '取值准确性约束') {
        this.ruleForm.metaDatas = []
        let datas = []
        datas.push(metadataData.sourceId + '-' + metadataData.type)
        datas.push(metadataData.source.name)
        datas.push(metadataData.source.fields[0].name + '-' + metadataData.source.fields[0].type + '-' + metadataData.source.fields[0].length)
        this.ruleForm.key = datas
        this.ruleForm.keyValue = metadataData.values[0]
        metadataData.source.fields.forEach((v, i) => {
          if (i != 0) {
            let obj = {}
            obj.disabled1 = true
            obj.disabled2 = true
            obj.check = v.name
            obj.keyValue = metadataData.values[i]
            obj.formLabelWidth = "130px"
            this.ruleForm.metaDatas.push(obj)
          }
        })
      }
    },
    // 数据模板切换
    handleChange(val) {
      this.ruleForm.metadataData = ''
      this.show = false
      this.disabled2 = false
      this.thenDisabled = true
      this.show1 = false
      this.range = ''
      this.modelId = val.split('-')[0]
      this.modelLabel = val.split('-')[1]
      this.descriptionFun()
    },
    // 描述语句切换
    descriptionFun() {
      if (this.modelLabel == '完整性非空约束') {
        this.props = { multiple: true, checkStrictly: false }
        this.miaoshu = '检查实体在系统中是否有重复记录'
      } else if (this.modelLabel == '实体唯一性约束') {
        this.props = { multiple: true, checkStrictly: false }
        this.miaoshu = '检查实体在系统中是否有重复记录'
      } else if (this.modelLabel == '长度约束') {
        this.props = { multiple: true, checkStrictly: false }
        this.miaoshu = '检查核对对象的长度是否满足长度约束'
      } else if (this.modelLabel == '标志取值约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象的的值是否满足标志的取值约束'
      } else if (this.modelLabel == '代码值域约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象的代码值是否在对应的代码表内'
      } else if (this.modelLabel == '取值范围约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象的取值是否在预定义的范围内'
      } else if (this.modelLabel == '内容规范约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象的值是否按照一定的规范进行数据存储'
      } else if (this.modelLabel == '等值一致性依赖约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象之间数据取值的约束规则。一个检核对象必须与另一个或多个检核对象在一定条件下相等。'
      } else if (this.modelLabel == '逻辑一致性依赖约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象之间数据值逻辑关系的约束规则，一个检核对象的数值必须与另一个检核对象的数值满足某种逻辑关系（如大于，小于等）。'
      } else if (this.modelLabel == '取值准确性约束') {
        this.props = { multiple: false, checkStrictly: false }
        this.miaoshu = '检核对象是否与其对应的客观实体的特征相一致。取值准确性是指不仅数据的格式和内容满足有效性的要求，其值也是客观实体的数据。'
      }
    },
    // 切换取值范围
    handleChange1(val) {
      if (val != 'between' && val != 'not_between') {
        this.show = true
        this.show1 = false
      } else {
        this.show1 = true
        this.show = false
      }
    },
    // 元数据操控禁用
    getCheckedNodes1() {
      this.ruleForm.metaDatas[0].check = ''
      this.ruleForm.metaDatas[0].targetCheck = ''
      if (this.modelLabel == '等值一致性依赖约束' || this.modelLabel == '逻辑一致性依赖约束') {
        this.thenDisabled = false
        this.ruleForm.targetKey = ''
        this.thenOptions = []
        this.options1.forEach((v, i) => {
          let val = JSON.parse(JSON.stringify(v))
          this.thenOptions.push(val)
          if (v.value != this.ruleForm.key[0]) {
            this.thenOptions[i].disabled = true
          }
        })
      }
      let key = this.ruleForm.key
      this.options1.forEach((v, i) => {
        if (v.value == key[0]) {
          v.children.forEach((v1, i1) => {
            if (v1.value == key[1]) {
              if (v1.children.length > this.childNum) {
                this.childNum = v1.children.length
              }
            }
          })
        }
      })
      this.leftData[0] = key
      this.leftData[1] = ''
      this.ruleForm.metaDatas.length = 1
      if (key == '') {
        this.ruleForm.metaDatas[0].disabled1 = true
      } else {
        this.ruleForm.metaDatas[0].disabled1 = false
        let option = JSON.parse(JSON.stringify(this.options1))
        this.ruleForm.metaDatas[0].options2 = this.disabledData(option, this.leftData)
      }
    },
    // 比较元数据操控禁用
    getCheckedNodes2() {
      this.ruleForm.metaDatas[0].targetCheck = ''
      let key = this.ruleForm.targetKey
      this.rightData[0] = key
      this.rightData[1] = ''
      this.ruleForm.metaDatas.length = 1
      if (key == '') {
        this.ruleForm.metaDatas[0].disabled2 = true
      } else {
        this.ruleForm.metaDatas[0].disabled2 = false
        let option = JSON.parse(JSON.stringify(this.thenOptions))
        this.ruleForm.metaDatas[0].options3 = this.disabledData(option, this.rightData)
      }
    },
    getCheckedNodes3() {
      let key = this.ruleForm.keyValue
      if (key == '') {
        this.ruleForm.metaDatas[0].disabled2 = true
      } else {
        this.ruleForm.metaDatas[0].disabled2 = false
      }
    },
    // 禁用封装
    disabledData(option, key) {
      key.forEach((val, ind) => {
        if (val != '') {
          option.forEach((v, i) => {
            if (v.value != val[0]) {
              v.disabled = true
            } else {
              v.disabled = false
            }
            v.children.forEach((v1, i1) => {
              if (v1.value != val[1]) {
                v1.disabled = true
              } else {
                v.disabled = false
              }
              v1.children.forEach((v2, i2) => {
                if (v2.value == val[2]) {
                  v2.disabled = true
                }
              })
            })
          })
        } else {

        }
      })

      return option
    },
    submitForm(ruleForm) {
      const that = this
      if (that.$route.query.type === '编辑') {
        // 编辑
        updateVerifyRule({ id: this.id, name: this.ruleForm.name, description: this.ruleForm.description }).then((res) => {
          that.loading = false;
          if (res.data.code === 1) {
            that.$message.success('编辑成功')
            that.goBack()
          } else {
            that.$message.error(res.data.msg)
          }
        });
      } else {
        that.$refs["ruleForm"].validate((valid) => {
          if (valid) {
            that.loading = true;
            let data = {}
            let ruleForm = JSON.parse(JSON.stringify(that.ruleForm))
            data.ruleTemplateId = ruleForm.ruleTemplateId
            data.metadataData = ruleForm.metadataData ? ruleForm.metadataData : ruleForm.key
            data.name = ruleForm.name
            data.description = ruleForm.description
            data.ruleTemplateId = data.ruleTemplateId.split('-')[0]
            let metadataData = JSON.parse(JSON.stringify(data.metadataData))
            let target = JSON.parse(JSON.stringify(ruleForm.target))
            if (that.modelLabel != '完整性非空约束' && that.modelLabel != '实体唯一性约束' && that.modelLabel != '长度约束') {
              let metadataData1 = JSON.parse(JSON.stringify(metadataData))
              let target1 = JSON.parse(JSON.stringify(target))
              metadataData = [metadataData1]
              target = [target1]
            }
            let fields = []
            metadataData.forEach((v, i) => {
              let fieldsObj = {}
              fieldsObj.name = v[2].split('-')[0]
              fieldsObj.type = v[2].split('-')[1]
              fields.push(fieldsObj)
            })
            let obj = {
              "type": metadataData[0][0].split('-')[1],
              "sourceId": metadataData[0][0].split('-')[0],
              "sourceName": metadataData[0][0].split('-')[2],
              "source": {
                "name": metadataData[0][1].split('-')[1],
                "fields": fields
              },
              "target": {
                "name": "",
                "fields": []
              },
              "operators": '',
              "values": '',
              union: that.checked
            }
            if (that.modelLabel == '长度约束') {
              obj.operators = ruleForm.range
              obj.values = ruleForm.lang
            } else if (that.modelLabel == '标志取值约束') {
              obj.values = ruleForm.bound.split(',')
            } else if (that.modelLabel == '代码值域约束') {
              let fields1 = []
              target.forEach((v, i) => {
                console.log(v)
                let fieldsObj1 = {}
                fieldsObj1.name = v[2].split('-')[0]
                fieldsObj1.type = v[2].split('-')[1]
                fields1.push(fieldsObj1)
              })
              obj.target.name = target[0][1].split('-')[1]
              obj.target.fields = fields1
              console.log(obj.target)
            } else if (that.modelLabel == '取值范围约束') {
              obj.operators = ruleForm.valueRange
              if (ruleForm.valueRange == 'between' || ruleForm.valueRange == 'not_between') {
                obj.values = [ruleForm.value2, ruleForm.value3]
              } else {
                obj.values = [ruleForm.value1, ruleForm.value1]
              }
            } else if (that.modelLabel == '内容规范约束') {
              obj.values = ruleForm.regs
            } else if (that.modelLabel == '等值一致性依赖约束' || that.modelLabel == '逻辑一致性依赖约束' || that.modelLabel == '取值准确性约束') {
              if (that.modelLabel == '等值一致性依赖约束' || that.modelLabel == '逻辑一致性依赖约束') {
                obj.target.name = ruleForm.targetKey[1].split('-')[1]
                obj.target.fields.push({
                  name: ruleForm.targetKey[2].split('-')[0],
                  type: ruleForm.targetKey[2].split('-')[1]
                })
              }
              let operators = ['=']
              let value = [this.ruleForm.keyValue]
              this.ruleForm.metaDatas.forEach((v, i) => {
                let Obj = {}
                let Obj1 = {}
                Obj.name = ''
                Obj.type = ''
                Obj1.name = ''
                Obj1.type = ''
                if (v.check) {
                  Obj.name = v.check[2].split('-')[0]
                  Obj.type = v.check[2].split('-')[1]
                }
                if (v.targetCheck) {
                  Obj1.name = v.targetCheck[2].split('-')[0]
                  Obj1.type = v.targetCheck[2].split('-')[1]
                }
                operators.push(v.range)
                value.push(v.keyValue)
                obj.source.fields.push(Obj)
                if (that.modelLabel == '等值一致性依赖约束' || that.modelLabel == '逻辑一致性依赖约束') {
                  obj.target.fields.push(Obj1)
                }
              })
              if (that.modelLabel == '逻辑一致性依赖约束') {
                obj.operators = operators
              }
              if (that.modelLabel == '取值准确性约束') {
                obj.values = value
              }
            }
            data.metadataData = JSON.stringify(obj)

            // 新增
            verifyRuleInsert(data).then((res) => {
              that.loading = false
              if (res.data.code === 1) {
                that.$message.success('新增成功')
                that.goBack()
              } else {
                that.$message.error(res.data.msg)
              }
            })
          } else {
            return false;
          }
        });
      }
    },
    goBack() {
      this.$router.push('/quality')
    },
    // 添加对比元
    addData() {
      if (this.title != '编辑') {
        if (this.childNum - 2 <= 0 || this.childNum <= this.ruleForm.metaDatas.length + 1) {
          this.$message({
            message: '没有字段可以选择',
            type: 'warning'
          });
        } else if (this.modelLabel == '取值准确性约束' && this.ruleForm.key == '') {
          this.$message({
            message: '请先选择主键',
            type: 'warning'
          });
        } else if (this.modelLabel != '取值准确性约束' && (this.ruleForm.key == '' || this.ruleForm.targetKey == '')) {
          this.$message({
            message: '请先选择主键',
            type: 'warning'
          });
        } else {
          let last = this.ruleForm.metaDatas[this.ruleForm.metaDatas.length - 1]
          if (last.check == '') {
            this.$message({
              message: '请先选择该组检核元数据:',
              type: 'warning'
            });
          } else {
            let last = this.ruleForm.metaDatas[this.ruleForm.metaDatas.length - 1]
            if (this.modelLabel == '逻辑一致性依赖约束' && last.range == '') {
              this.$message({
                message: '请先选择该项约束条件',
                type: 'warning'
              });
            } else {
              this.metaDatasId++
              let options2 = [this.ruleForm.key]
              let options3 = [this.ruleForm.targetKey]
              let option = JSON.parse(JSON.stringify(this.options1))
              let option1 = JSON.parse(JSON.stringify(this.thenOptions))
              this.ruleForm.metaDatas.forEach((v, i) => {
                options2.push(v.check)
                options3.push(v.targetCheck)
              })
              this.ruleForm.metaDatas.push({
                formLabelWidth: "120px",
                id: this.metaDatasId,
                disabled1: false,
                range: '',
                disabled2: false,
                options2: this.disabledData(option, options2),
                options3: this.disabledData(option1, options3),
                check: '',
                targetCheck: '',
                change: (val, index) => {
                  this.ruleForm.metaDatas.length = index + 1
                }
              })
            }
          }
        }
      }

    },
    // 删除对比元
    delData(i) {
      if (this.title != '编辑') {
        this.ruleForm.metaDatas.splice(i, 1)
      }
    },
    change(val) {
    }
  }
}
</script>
<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;
  .main-center {
    padding: 20px;
    border-radius: 5px;
    width: 65%;
    margin: 0 auto;
  }
}
.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}
.local {
  color: #7f7f7f;
  display: flex;
  line-height: 16px;
  font-size: 14px;
  align-items: center;
  div {
    display: flex;
    align-items: flex-start;
  }
}

/deep/ .el-popper[x-placement^="bottom"] {
  display: none !important;
}
</style>
