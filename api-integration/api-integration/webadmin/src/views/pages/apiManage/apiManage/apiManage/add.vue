<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
      <div class="content-head">
        <el-steps :active="indexFlag" align-center process-status="finish" finish-status="finish">
          <el-step title="1.API基本信息" @click.native="goToTable(0)" ></el-step>
          <el-step title="2.API路由信息" @click.native="goToTable(1)" ></el-step>
          <el-step title="3.API流量配置信息" @click.native="goToTable(2)" ></el-step>
        </el-steps>
    </div>
     <el-col>
      <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      label-width="150px"
      class="demo-ruleForm"
     >
        <div v-if="indexFlag === 0">
          <el-row style="margin-bottom: 30px">
            <el-col span="12">
              <el-form-item label="名称:" :label-width="this.formLabelWidth" prop='name'>
                <el-input
                  clearable
                  size="medium"
                  placeholder="请输入名称"
                  v-model="ruleForm.name"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col span="9">
              <el-form-item label="接口资源:" :label-width="this.formLabelWidth" prop='sourceName'>
                <el-input
                    v-show="false"
                    clearable
                    size="medium"
                    placeholder="请选择接口资源"
                    v-model="ruleForm.sourceName"
                ></el-input>
                <el-popover
                    placement="right"
                    width="700"
                    trigger="click"
                    ref="myPopover"
                    :hide="closeMyPopover"
                >
                  <el-form :inline="true" class='el-InputForm'>
                    <el-form-item>
                      <el-input clearable
                                size="small"
                                placeholder="请输入接口名称"
                                v-model="SearchItem.name">
                      </el-input>
                    </el-form-item>
                    <el-form-item>
                      <el-select
                          size="small"
                          v-model="SearchItem.tagName"
                          filterable
                          remote
                          reserve-keyword
                          placeholder="请输入标签名"
                          :remote-method="remoteMethod"
                          clearable
                      >
                        <el-option
                            v-for="item in tagInfos"
                            :key="item.name"
                            :label="item.name"
                            :value="item.name">
                        </el-option>
                      </el-select>
                    </el-form-item>
                    <el-form-item>
                      <el-input clearable
                                size="small"
                                placeholder="请输入标签值"
                                v-model="SearchItem.tagValue">
                      </el-input>
                    </el-form-item>
                    <el-form-item style='margin-left: 1%;'>
                      <el-button size='small' @click="getSourceApiList" icon="el-icon-search">查询</el-button>
                    </el-form-item>
                  </el-form>
                  <template>
                    <el-table
                        :data="tableData"
                        style="width: 100%;margin-top: 10px;"
                        @row-click="getSourceInfo"
                    >
                      <el-table-column
                          prop="name"
                          label="接口名称"
                          width="280">
                        <template slot-scope="scope">
                          <el-tag v-if="scope.row.method === 'GET'" color="#993300" effect="dark" size="small">GET</el-tag>
                          <el-tag v-else-if="scope.row.method === 'POST'" size="small" effect="dark" color="#336699" >POST</el-tag>
                          <el-tag v-else-if="scope.row.method === 'PUT'" size="small" effect="dark" color="#FCA130" >PUT</el-tag>
                          <el-tag v-else-if="scope.row.method === 'DELETE'" size="small" effect="dark" color="#F93E3E" >DELETE</el-tag>
                          <el-tag v-else-if="scope.row.method" color="#109612" effect="dark" size="small">MULT</el-tag>
                          {{scope.row.name}}
                        </template>
                      </el-table-column>
                      <el-table-column
                          prop="host"
                          label="Host"
                          width="200">
                      </el-table-column>
                      <el-table-column
                          prop="tags"
                          label="标签"
                          width="200">
                        <template slot-scope="scope">
                          <el-tag v-for="(tag,i) in scope.row.tags">{{tag}}</el-tag>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>
                  <el-col :span='24'>
                    <pagination ref="page" :total="total" @pageChange="pageChange" style="position: relative;right: 10px"></pagination>
                  </el-col>
                  <el-button slot="reference" @click.native="getSourceApiList" style="width: 300px;">{{ruleForm.sourceName}}</el-button>
                </el-popover>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col span="12">
              <el-form-item label="Path:" :label-width="this.formLabelWidth" prop='path'>
                <el-input size="medium" v-model="ruleForm.sourceApi.path" disabled></el-input>
              </el-form-item>
            </el-col>

            <el-col span="9">
              <el-form-item label="Host:" :label-width="this.formLabelWidth" prop='hosts'>
                <el-input size="medium" v-for="(host,i) in sourceApiData.host" v-model="sourceApiData.host[i]" disabled></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col span="12">
              <el-form-item label="标签:" prop="metas">
                <div v-for="(item,i) in ruleForm.metas">
                  <el-select
                      size="small"
                      v-model="item.key"
                      filterable
                      remote
                      reserve-keyword
                      placeholder="请输入标签名"
                      :remote-method="remoteMethod"
                  >
                    <el-option
                        v-for="item in tagInfos"
                        :key="item.name"
                        :label="item.name"
                        :value="item.name">
                    </el-option>
                  </el-select>
                  <el-tag type="info" style="margin-left: 3px;">
                    <input style="border: #ECF5FF;background-color: #F4F4F5;outline: none;width: 140px;" v-model="item.value"/>
                  </el-tag>
                  <el-button type="small" icon="el-icon-plus" style="margin-top: 4px;margin-left: 3px;" @click="insertMeta" v-if="i === 0"></el-button>
                  <el-button type="small" icon="el-icon-delete" style="margin-top: 4px;margin-left: 3px;" @click="removeMeta(i)" v-if="i !== 0"></el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col span="12">
              <el-form-item label="分组:" :label-width="this.formLabelWidth" prop="groupId">
                <el-cascader
                    expandTrigger="hover"
                    v-model="ruleForm.groupId"
                    :options="groupOptions"
                    :props="{ checkStrictly: true,emitPath:false,label:'name',value:'id' }"
                    clearable>
                </el-cascader>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col span="15">
              <el-form-item label="描述:" :label-width="this.formLabelWidth">
                <el-input
                  clearable
                  size="medium"
                  :rows="4"
                  type="textarea"
                  placeholder="请输入描述"
                  v-model="ruleForm.detail"
                ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div v-if="indexFlag === 1">
          <el-form-item label="路由路径:" :label-width="this.formLabelWidth" prop='routeInfo' style="width: 85%">
            <el-input placeholder="须以“/”开头，只能包含字母、数字、“/”、“_”、“-”和“.”，长度不少于2位，如“/api/request”"
                      clearable
                      size="medium"
                      v-model="ruleForm.routeInfo"
            ></el-input>
          </el-form-item>
          <el-form-item label="验证方式:" :label-width="this.formLabelWidth" prop='verificationMode' style="width: 85%">
            <el-select v-model="ruleForm.verificationMode" placeholder="请选择验证方式">
              <el-option label="Hmac签名" value="0"></el-option>
              <el-option label="Key-Auth" value="1"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-if="indexFlag === 2" style="margin-left: 200px">
          <el-row>
            <el-col span="8">
              <el-form-item label="每分钟:" :label-width="this.formLabelWidth">
                <el-input placeholder="默认:1000" v-model="ruleForm.perMinute">
                  <template slot="append">次</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col span="8">
              <el-form-item label="每次:" :label-width="this.formLabelWidth">
                <el-input placeholder="默认:1" v-model="ruleForm.singleSize">
                  <template slot="append">MB</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col span="8">
              <el-form-item label="每小时:" :label-width="this.formLabelWidth">
                <el-input placeholder="默认:6000" v-model="ruleForm.everyHour">
                  <template slot="append">次</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col span="8">
              <el-form-item label="每天:" :label-width="this.formLabelWidth">
                <el-input placeholder="默认:10000" v-model="ruleForm.everyDay">
                  <template slot="append">次</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <div align="center">
          <el-button v-if="indexFlag !== 0" size="medium" @click="backTable">上一步</el-button>
          <el-button v-if="indexFlag !== 2" size="medium" @click="nextTable('ruleForm')">下一步</el-button>
          <el-button v-if="indexFlag === 2" size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
          <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
        </div>
    </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {
  sourceApiList,
  add
} from "@/api/apiManage";

import {
  findAllMeta,
  findAllContainer,
  findAllApi
} from "@/api/sourceService";

import {
  tree
} from "@/api/apiGroupManage";
import Pagination from '@/components/table/PaginationFor5.vue'

import AutoCompleteInput from '@/components/input/autoCompleteInput.vue'
export default {
  components: { Pagination,AutoCompleteInput },
  data() {
    return {
      title: '',


      tagInfos:[],

      //接口资源
      props: { checkStrictly: true },
      ruleForm: {
        name:'',
        detail:'',
        routeInfo:'',
        perMinute:'',
        everyHour:'',
        everyDay:'',
        singleSize:'',
        groupId:'',
        verificationMode:'',

        sourceName:'',

        sourceApi:{
          name:'',
          host:[''],
          path:'',
          protocol:[''],
          method:'',
          formatType:'',
          body:'',
          params:'',
          response:'',
          constants:'',
          apiId:''
        },

        //标签
        metas:[{
          key:'',
          value:''
        }]
      },
      rules: {
        name: [{
          required: true,
          message: "请输入API名称",
          trigger: "change"
        }],
        sourceName: [{
          required: true,
          message: '请选择接口资源',
          trigger: ['change', 'blur']
        }],
        metas: [{
          required: true,
          validator: (rule, value, callback) => {
            console.log('value',value)
            if (value === undefined || value === null || value === '') {
              callback(new Error('标签不能为空'))

            }else {
              let sign = true
              value.forEach((item,i) => {
                console.log('item',item)
                if(item.key === ''){
                  sign = false
                }
              })
              if(sign){
                callback()
              }else {
                callback(new Error('标签不能为空'))
              }
            }
          },
          trigger: ['change', 'blur']
        }],
        routeInfo: [{
          required: true,
          message: '请输入路由路径',
          trigger: ['change', 'blur']
        },{
          pattern: /\/[^\s]*/,
          message: '请输入正确的路由路径'
        }],
        verificationMode: [{
          required: true,
          message: '请选择验证方式',
          trigger: ['change', 'blur']
        }],
      },
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px",
      indexFlag:0,
      sourceApiData:[],
      total: 0,
      pageSize: '5',
      currentPage: '1',
      SearchItem:{
        name:'',
        tagName:'',
        tagValue:''
      },
      lastItem: {
        name: ''
      },
      isSubmitLoading: false,

      tableData:[]
    }
  },
  async created () {
    const that = this
    await this.getGroupList()
    that.ruleForm.groupId = this.$route.query.groupId
  },
  methods: {
    //标签
    insertMeta(){
      const that = this
      let item = {
        key:'',
        value:''
      }
      that.ruleForm.metas.push(item)
    },
    removeMeta(i){
      const that = this
      that.ruleForm.metas.splice(i,1)
    },

    async remoteMethod(query) {
      const that = this
      if (query !== '') {
        let res = await findAllMeta({name: query})
        if (res.data.code == 1) {
          that.tagInfos = res.data.data.content
          console.log('that.tagInfos',that.tagInfos)
        }
      }
    },

    //获取源API数据
    async getSourceApiList(type) {
      const that = this
      let data = {}
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await findAllApi(data)
        that.isSubmitLoading = false
        if(res.data.code === 1){
          that.tableData = res.data.data.content

          let tableData = that.tableData
          tableData.forEach((item,i) => {
            let host = JSON.parse(item.host)

            let hostStr = ''
            host.forEach((hostItem,j) => {
              hostStr += hostItem.target
              hostStr += ','
            })

            if(hostStr !== '') {
              hostStr = hostStr.substring(0,hostStr.length - 1)
            }
            item.host = hostStr

            let tagNames = item.tagName.split(',')
            let tagValues = item.tagValue.split(',')

            let tags = []
            for(let i = 0; i < tagNames.length; i++){
              let tag = tagNames[i] + ':' + tagValues[i]
              tags.push(tag)
            }

            item.tags = tags
          })

          that.tableData = tableData

          this.total =  res.data.data.totalElements
        } else {
          this.$message.error("查询失败")
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    getSourceInfo(row){
      const that = this

      that.exchangSourceData(row)

      that.closeMyPopover()
      that.$refs.myPopover.doClose()
    },


    exchangSourceData(data){
      const that = this

      that.sourceApiData.name = data.name
      that.sourceApiData.host = data.host.split(',')

      that.sourceApiData.path = data.path
      that.sourceApiData.protocol = data.protocol.split(',')
      that.sourceApiData.method = data.method.split(',')
      that.sourceApiData.formatType = data.formatType
      if(data.body !== ''){
        that.sourceApiData.body = JSON.parse(data.body)
      } else {
        that.sourceApiData.body = {}
      }

      if(data.params !== '') {
        that.sourceApiData.params = JSON.parse(data.params)
      } else {
        that.sourceApiData.params = {}
      }

      that.sourceApiData.response = data.response

      if(data.response !== '') {
        that.sourceApiData.jsonResponse = JSON.parse(data.response)
      } else{
        that.sourceApiData.jsonResponse = {}
      }

      if(data.constants !== '') {
        that.sourceApiData.constants = JSON.parse(data.constants)
      } else {
        that.sourceApiData.constants = {}
      }

      that.ruleForm.sourceApi.name = data.name
      that.ruleForm.sourceApi.host = data.host
      that.ruleForm.sourceApi.path = data.path
      that.ruleForm.sourceApi.protocol = data.protocol
      that.ruleForm.sourceApi.method = data.method
      that.ruleForm.sourceApi.formatType = data.formatType
      that.ruleForm.sourceApi.body = data.body
      that.ruleForm.sourceApi.params = data.params
      that.ruleForm.sourceApi.response = data.response
      that.ruleForm.sourceApi.constants = data.constants

      that.ruleForm.sourceName = data.name
    },

    closeMyPopover(){
      const that = this
      that.SearchItem.name = ''
      that.SearchItem.tagName = ''
      that.SearchItem.tagValue = ''
      that.tagInfos = []
    },




    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.ruleForm.name = "";
      this.ruleForm.sourceApiId = "";
      this.ruleForm.detail = "";
      this.ruleForm.routeInfo = "";
      this.ruleForm.perMinute = "";
      this.ruleForm.everyHour = "";
      this.ruleForm.everyDay = "";
      this.ruleForm.singleSize = "";
      this.ruleForm.method = '';
      this.name = "请选择接口资源";
      this.$router.push({
        path: '/apiManage/apiManage/apiList'
      })
    },

    nextTable(){
      const that = this
      this.$refs["ruleForm"].validate( valid =>{
        console.log('valid',valid)
        if(!valid) {
          return false
        }else {
          that.indexFlag += 1
        }
      })
    },
    backTable(){
      const that = this

      this.$refs["ruleForm"].validate( valid =>{
        console.log('valid',valid)
        if(!valid) {
          return false
        }else {
          that.indexFlag -= 1
        }
      })
    },
    goToTable(num){
      const that = this
      this.$refs["ruleForm"].validate( valid =>{
        console.log('valid',valid)
        if(!valid) {
          return false
        }else {
          that.indexFlag = num
        }
      })
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.getSourceApiList('page')
    },


    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let data = that.ruleForm

          that.loading = true;
          data.metas = JSON.stringify(that.ruleForm.metas)
          // 新增
          add(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('新增成功');
              this.resetForm('ruleForm')
            } else {
              this.ruleForm.metas = JSON.parse(data.metas)
              this.$message.error(res.data.msg);
            }
          });
          this.loading = false;
        }
      });
    },

    async getGroupList() {
      const that = this
      let data = { type: '1' }
      try {
        const res = await tree(data)
        if (res.data.code === 1) {
          that.groupOptions = res.data.data

          console.log('that.groupOptions',that.groupOptions)
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    }
  }
}
</script>
