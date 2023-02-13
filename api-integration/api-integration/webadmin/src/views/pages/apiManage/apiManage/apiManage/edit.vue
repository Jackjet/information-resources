<template>
  <el-main class="main">
     <div> <h4>{{title}}</h4></div>
      <div class="content-head">
        <el-steps :active="indexFlag" align-center process-status="finish">
          <el-step title="1.API基本信息" @click.native="goToTable(0)" ></el-step>
          <el-step title="2.API请求信息" @click.native="goToTable(1)" ></el-step>
          <el-step title="3.API常量信息" @click.native="goToTable(2)" ></el-step>
          <el-step title="4.API路由信息" @click.native="goToTable(3)" ></el-step>
          <el-step title="5.API流量配置信息" @click.native="goToTable(4)" ></el-step>
        </el-steps>
    </div>
     <el-col class="main-center">
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
              <el-form-item label="Host:" :label-width="this.formLabelWidth" prop='host'>
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
                        v-for="(item,i) in tagInfos"
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
          <el-form :model="sourceApiData">
            <el-row>
              <h4 style="color: grey">API请求信息</h4>
              <hr color="#cccccc" style="position: relative;left: -40px;width: 95%;">
            </el-row>
            <el-row style="margin-top: 20px">
              <el-col :span="8" :offset="3">
                <el-form-item
                    label="请求协议"
                    class='InpitWidth'>
                  <el-checkbox-group v-model="sourceApiData.protocol" disabled>
                    <el-checkbox label="Http"></el-checkbox>
                    <el-checkbox label="Https"></el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row style="margin-bottom: -20px">
              <el-col :span="10" :offset="3">
                <el-form-item
                    label="请求方式"
                    class='InpitWidth'>
                  <el-checkbox-group v-model="sourceApiData.method" :disabled = "methodType">
                    <el-checkbox v-for="method in sourceApiData.method" :label="method" :key="method"></el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <h4 style="color: grey">入参内容定义</h4>
              <hr color="#cccccc" style="position: relative;left: -40px;width: 95%;">
            </el-row>
            <el-row>
              <el-col :span="2">
                <h5 style="color: grey">请求参数</h5>
              </el-col>
              <el-col :span="8">
                <el-popover
                    placement="left"
                    width="400"
                    trigger="click"
                    ref="popver">
                  <div>
                    <h5>请求示例：</h5>
                    <hr>
                    <p>http://www.xxx.com/route?params=xxxx</p>
                  </div>
                  <el-button slot="reference" type="mini" style="margin-top: 15px;margin-left: 10px;">请求示例</el-button>
                </el-popover>
              </el-col>
            </el-row>
            <el-row style="width: 95%">
              <el-table
                  :data="sourceApiData.params"
                  stripe
                  empty-text="暂无数据"
                  class="el_tab_alage">
                <el-table-column
                    align="center"
                    fit
                    prop="name"
                    label="参数名称">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.name" disabled></el-input>
                  </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    fit
                    prop="type"
                    label="参数位置">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" disabled>
                      <el-option value="Parameter"></el-option>
                      <el-option value="Header"></el-option>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    fit
                    prop="isRequired"
                    label="是否必填">
                  <template slot-scope="scope">
                    <el-checkbox v-model="scope.row.isRequired" disabled></el-checkbox>
                  </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    fit
                    prop="defaultValue"
                    label="默认值">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.defaultValue" disabled></el-input>
                  </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    fit
                    prop="regular"
                    label="校验格式">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.regular" disabled></el-input>
                  </template>
                </el-table-column>
                <el-table-column
                    align="center"
                    fit
                    prop="description"
                    label="描述">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.description" disabled></el-input>
                  </template>
                </el-table-column>
              </el-table>
            </el-row>
            <el-row>
              <el-form-item
                  label="内容格式类型"
                  class='InpitWidth'>
                <el-radio-group v-model="sourceApiData.formatType">
                  <el-radio-button label="JSON" disabled>JSON</el-radio-button>
                  <el-radio-button label="form-data" disabled>form-data</el-radio-button>
                  <el-radio-button label="XML" disabled>XML</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="2.5">
                <h5 style="color: grey">请求内容参数设置</h5>
              </el-col>
            </el-row>
            <el-row style="width: 95%">
              <el-table
                  v-if="sourceApiData.formatType === 'application/json' || sourceApiData.formatType === 'multipart/form-data'"
                  :data="sourceApiData.body"
                  style="width: 100%;margin-bottom: 20px;margin-top: 10px"
                  row-key="id"
                  border
                  default-expand-all
                  :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                <el-table-column
                    prop="name"
                    label="参数名称"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="type"
                    label="参数类型"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="isRequired"
                    fit
                    label="是否必填">
                  <template slot-scope="scope">
                    <el-checkbox v-model="scope.row.isRequired" disabled></el-checkbox>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="defaultValue"
                    label="默认值"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="regular"
                    label="校验格式"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="description"
                    label="描述"
                    fit>
                </el-table-column>
              </el-table>
              <el-input v-if="sourceApiData.formatType === 'XML'" type="textarea" :rows="10" :cols="100" v-model="sourceApiData.body" disabled  style="margin-top: 10px"></el-input>
            </el-row>
            <el-row>
              <h5 style="color: grey">返回参数设置</h5>
            </el-row>
            <el-row>
              <el-col :span="8" :offset="9">
                <el-radio-group v-model="showType">
                  <el-radio-button label="模板"></el-radio-button>
                  <el-radio-button label="预览"></el-radio-button>
                </el-radio-group>
              </el-col>
            </el-row>
            <el-row style="width: 95%">
              <el-table
                  v-if="showType === '模板'"
                  :data="sourceApiData.jsonResponse"
                  style="width: 100%;margin-bottom: 20px;margin-top: 10px"
                  row-key="id"
                  border
                  default-expand-all
                  :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                <el-table-column
                    prop="name"
                    label="参数名称"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="type"
                    label="参数类型"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="defaultValue"
                    label="默认值"
                    fit>
                </el-table-column>
                <el-table-column
                    prop="description"
                    fit
                    label="描述">
                </el-table-column>
              </el-table>
              <el-input v-if="showType === '预览'" type="textarea" :rows="10" :cols="100" v-model="sourceApiData.response" disabled  style="margin-top: 10px"></el-input>
            </el-row>
          </el-form>
        </div>
        <div v-if="indexFlag === 2">
          <template>
            <el-table
              :data="sourceApiData.constants"
              style="width: 90%">
              <el-table-column
                prop="name"
                label="后端参数名称"
                width="230">
              </el-table-column>
              <el-table-column
                prop="value"
                label="参数值"
                width="230">
              </el-table-column>
              <el-table-column
                prop="pos"
                label="参数位置">
              </el-table-column>
              <el-table-column
                prop="description"
                label="描述">
              </el-table-column>
            </el-table>
          </template>
        </div>
        <div v-if="indexFlag === 3">
          <el-form-item label="路由路径:" :label-width="this.formLabelWidth" prop='routeInfo' style="width: 85%">
            <el-input placeholder="须以“/”开头，只能包含字母、数字、“/”、“_”、“-”和“.”，长度不少于2位，如“/api/request”"
                      clearable
                      size="medium"
                      v-model="ruleForm.routeInfo"
                      style="width: 85%"
            ></el-input>
          </el-form-item>
          <el-form-item label="验证方式:" :label-width="this.formLabelWidth" prop='verificationMode' style="width: 85%">
            <el-select v-model="ruleForm.verificationMode" placeholder="请选择验证方式">
              <el-option label="Hmac签名" value="0"></el-option>
              <el-option label="Key-Auth" value="1"></el-option>
            </el-select>
          </el-form-item>
        </div>
        <div v-if="indexFlag === 4">
          <el-row>
            <el-col span="8">
              <el-form-item label="每分钟:" :label-width="this.formLabelWidth">
                <el-input placeholder="1000" v-model="ruleForm.perMinute">
                  <template slot="append">次</template>
                </el-input>
              </el-form-item>
            </el-col>
            <el-col span="8">
              <el-form-item label="每次:" :label-width="this.formLabelWidth">
                <el-input placeholder="1" v-model="ruleForm.singleSize">
                  <template slot="append">MB</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col span="8">
              <el-form-item label="每小时:" :label-width="this.formLabelWidth">
                <el-input placeholder="1000" v-model="ruleForm.everyHour">
                  <template slot="append">次</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col span="8">
              <el-form-item label="每天:" :label-width="this.formLabelWidth">
                <el-input placeholder="1000" v-model="ruleForm.everyDay">
                  <template slot="append">次</template>
                </el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
       <div align="center">
          <el-button v-if="indexFlag !== 0" size="medium" @click="backTable">上一步</el-button>
          <el-button v-if="indexFlag !== 4" size="medium" @click="nextTable">下一步</el-button>
          <el-button v-if="indexFlag === 4" size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
          <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
      </div>
    </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {
  sourceApiList,
  findById,update,findSourceApiById
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
export default {
  components: { Pagination },
  data() {
    return {
      //标签
      tagInfos:[],

      title: '',
      props: { checkStrictly: true },
      ruleForm: {
        name:'',
        detail:'',
        routeInfo:'',
        perMinute:'',
        everyHour:'',
        everyDay:'',
        singleSize:'',
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

        metas:[{
          key:'',
          value:''
        }],

        groupId:''
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
        }]
      },
      formLabelWidth: "120px",
      indexFlag:0,
      sourceApiData:{
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
        jsonResponse:[]
      },
      total: 0,
      pageSize: '5',
      currentPage: '1',
      SearchItem:{
        sourceName:''
      },
      lastItem: {
        sourceName: ''
      },
      isSubmitLoading: false,
      sourceContentTableData:[],
      tableSelection: {
        key: true,
        type: 'selection',
        detaile: false
      },
      tableHeader: [
        {id: false, type: 'color', label: '后端参数名称', list: 'name'},
        {id: false, type: 'color', label: '参数值', list: 'value'},
        {id: false, type: 'color', label: '参数位置', list: 'pos'},
        {id: false, type: 'color', label: '描述', list: 'description'}
      ],

      showType:'模板',

      methodType:true,


      thisParams:[],
      groupOptions:[]
    }
  },
  async created () {
    await this.apiAndSourceDetail()
    await this.getGroupList()

    console.log('that.ruleform',this.ruleForm.groupId)
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

          that.total =  res.data.data.totalElements

          that.$forceUpdate()
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

    async apiAndSourceDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      that.loading = true
      const response = await findById(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.exchangSourceData(response.data.data.sourceApi)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },



    resetForm(ruleForm) {
      this.$refs["ruleForm"].clearValidate();
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
      this.ruleForm.method = "";
      this.ruleForm.serviceId="";
      this.sourceApiName = "请选择接口资源";
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
    //获取源API信息
    getSourceId(row){
      const that = this
      that.sourceApiName = row.sourceApiName
      that.ruleForm.sourceApiId = row.id
      that.ruleForm.method = row.method
      that.ruleForm.serviceId = row.serviceId
      this.getSourceApi(row.id)
      this.$refs.myPopover.doClose()
    },

    //获取源API数据
    async getSourceApi(sourceId) {
      const that = this
      let data = {
        id:sourceId
      }
      try {
        const res = await findSourceApiById(data)
        if(res.data.code === 1){
          that.sourceApiData = res.data.data
          that.sourceContentTableData = JSON.parse(that.sourceApiData.constants)
          let tempHost = JSON.parse(that.sourceApiData.host)
          let hostList = []
          for (const tempInfo of tempHost) {
            hostList.push(tempInfo.target)
          }
          that.thisHost = hostList
          that.thisMethods = that.sourceApiData.method.split(',')
          console.log(that.thisMethods.length)
          if (that.thisMethods.length > 1){
            that.methodType = false
          }else {
            that.methodType = true
          }
          that.relThisMethods = that.sourceApiData.method.split(',')
          that.thisProtocol = that.sourceApiData.protocol.split(',')
          let tempParams = that.sourceApiData.params
          that.thisParams = JSON.parse(tempParams)
          let tempResponse = []
          tempResponse = JSON.parse(that.sourceApiData.response)
          that.jsonResponse = tempResponse
          that.strResponse = JSON.stringify(this.getJsonStrResponse(JSON.parse(that.sourceApiData.response),false), null, "\t");
          let tempBody = []
          tempBody = JSON.parse(that.sourceApiData.body)
          that.sourceApiData.body = tempBody
        } else {
          this.$message.error("失败")
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    submitForm() {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let data = that.ruleForm

          data.metas = JSON.stringify(that.ruleForm.metas)
          // 新增
          update(data).then((res) => {
            if (res.data.code === 1) {
              this.$message.success('编辑成功');
              this.resetForm('ruleForm')
              this.loading = false;
            } else {
              this.ruleForm.metas = JSON.parse(data.metas)
              this.$message.error(res.data.msg);
            }
          });
        }
      });
    },

    getJsonStrResponse(strResponse,isArray){
      let temp = {}
      for (const info of strResponse) {
        console.log(info)
        if(!info.hasChildren){
          temp[info.name] = info.defaultValue
        } else {
          if(info.type === 'Array'){
            temp[info.name] = this.getJsonStrResponse (info.children,true)
          }else{
            temp[info.name] = this.getJsonStrResponse (info.children,false)
          }
        }
      }

      if(isArray){
        let arr = []
        arr.push(temp)
        return arr
      }else{
        return temp
      }
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
