<template>
  <el-main class="main">
    <el-row class="content" :gutter="20">
      <el-col :span="8" class="el-col_left">
        <el-row>
          <el-col :span="24">
            <el-tabs v-model="active" @tab-click="handleClick">
              <el-tab-pane label="" disabled></el-tab-pane>
              <el-tab-pane label="历史记录" name="record"></el-tab-pane>
              <el-tab-pane label="测试用例" name="testCase"></el-tab-pane>
              <el-tab-pane label="" disabled></el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
        <el-row v-if="active === 'record'">
          <el-col :span="24">
            <div style="height: 100vh; overflow:auto;">
              <el-table
              :data="records"
              style="width: 100%"
              :show-header="false">
              <el-table-column
                type="expand"
                prop="id">
                <template slot-scope="props">
                  <el-table
                    :data="props.row.logList"
                    :show-header="false">
                    <el-table-column
                      prop="method"
                      width="80px">
                    </el-table-column>
                    <el-table-column
                      prop="responseCode"
                      width="100px">
                      <template slot-scope="scope">
                        状态：{{scope.row.responseCode}}
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="responseTime">
                      <template slot-scope="scope">
                        {{scope.row.responseTime}}ms
                      </template>
                    </el-table-column>
                    <el-table-column
                      style="width: 5px">
                      <template slot-scope="scope">
                        <el-button type="text" icon="el-icon-delete" @click="deleteRecord(scope.row)"></el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </template>
              </el-table-column>
              <el-table-column
                prop="time">
              </el-table-column>
            </el-table>
            </div>
          </el-col>
        </el-row>
        <el-row v-if="active === 'testCase'">
          <el-col :span="16" :offset="8">
            <el-upload
              class="upload-demo"
              :action="action"
              :headers="headers"
              :file-list="fileList"
              :on-success="handleFileUploadSuccess">
              <el-button type="mini" icon="el-icon-upload2">导入测试用例</el-button>
              <div slot="tip" class="el-upload__tip">只能上传.json文件</div>
            </el-upload>
          </el-col>
        </el-row>
        <el-row v-if="active === 'testCase'">
          <el-col :span="24">
            <div style="height: 100vh; overflow:auto;">
              <el-table
                :data="testcase"
                style="width: 100%"
                :show-header="false">
                <el-table-column
                  type="expand"
                  prop="id">
                  <template slot-scope="props">
                    <el-table
                      :data="props.row.caseList"
                      :show-header="false"
                      ref="singleTable"
                      highlight-current-row
                      @current-change="rowClick"
                     >
                      <el-table-column
                        prop="method">
                      </el-table-column>
                      <el-table-column
                        prop="ip"
                        min-width="110px">
                      </el-table-column>
                      <el-table-column
                        style="width: 5px">
                        <template slot-scope="scope">
                          <el-button type="text" icon="el-icon-download" @click.prevent.stop="downloadTestCase(scope.row)"></el-button>
                          <el-button type="text" icon="el-icon-delete" @click.prevent.stop="deleteTestCase(scope.row)"></el-button>
                        </template>
                      </el-table-column>
                    </el-table>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="time">
                </el-table-column>
              </el-table>
              <el-button type="primary" @click="clearCurrentRow()" size="small" style="position: relative;left: 38%">取消选择</el-button>
            </div>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="16" class="el-col_right">
        <el-row>
          <el-col :span="24">
            <el-tabs active-name="other">
              <el-tab-pane label="API测试参数" disabled></el-tab-pane>
              <!--              占位-->
              <el-tab-pane label="" disabled name="other"></el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form ref="form" style="padding: 0 100px">
              <el-form-item
                label="名称"
                label-width="150px"
                style="width:80%">
                <el-input disabled v-model="apiName"></el-input>
              </el-form-item>
              <el-form-item
                label="Host"
                label-width="150px"
                style="width: 80%">
                <template v-for="(item,index) in sourceApiHost">
                  <el-input disabled v-model="sourceApiHost[index]"></el-input>
                </template>
<!--                <el-select v-model="apiHost" placeholder="请选择" style="width: 100%">-->
<!--                  <el-option-->
<!--                      v-for="item in sourceApiHost"-->
<!--                      :key="item"-->
<!--                      :label="item"-->
<!--                      :value="item">-->
<!--                  </el-option>-->
<!--                </el-select>-->
              </el-form-item>
              <el-form-item
                label="Path"
                label-width="150px"
                style="width: 80%">
                <el-input disabled v-model="sourceApiPath"></el-input>
              </el-form-item>
              <el-form-item
                label="请求方式"
                label-width="150px"
                style="width: 80%">
                <el-select v-model="apiMethod" placeholder="请选择" style="width: 30%">
                  <el-option
                      v-for="item in sourceApiMethod"
                      :key="item"
                      :label="item"
                      :value="item">
                  </el-option>
                </el-select>
                <el-input v-model="apiRouteInfo" disabled style="width: 70%"></el-input>
              </el-form-item>
<!--              <el-form-item-->
<!--                  label="key:"-->
<!--                  label-width="150px"-->
<!--                  style="width: 80%">-->
<!--                <el-input v-model="key" style="width: 70%"></el-input>-->
<!--                <el-button @click="getSignValue" type="primary" style="width: 30%" id="signButton">生成签名</el-button>-->
<!--              </el-form-item>-->
<!--              <el-form-item-->
<!--                  label="签名:"-->
<!--                  label-width="150px"-->
<!--                  style="width: 80%">-->
<!--                <el-input disabled v-model="sign"></el-input>-->
<!--              </el-form-item>-->
              <el-form-item
                prop="saveResult"
                label="是否保存测试结果"
                label-width="150px"
                style="width: 80%">
                <el-radio-group v-model="saveResult">
                  <el-radio label="1">是</el-radio>
                  <el-radio label="0">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-tabs v-model="activeParams">
              <!--              占位-->
              <el-tab-pane label="" disabled name="other"></el-tab-pane>
              <el-tab-pane label="Headers" name="headers"></el-tab-pane>
              <el-tab-pane label="Parameters" name="parameters"></el-tab-pane>
              <el-tab-pane label="Body" name="body"></el-tab-pane>
              <el-tab-pane label="" disabled name="other"></el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" v-if="activeParams === 'headers'">
            <el-button type="text" icon="el-icon-plus" @click="addHeader">添加</el-button>
            <hr color="#cccccc">
            <div class="content_style">
              <template v-for="item in content.headers">
                <div style="margin-bottom: 5px">
                  <el-input v-model="item.key" style="width: 40%"></el-input>
                  &nbsp;=&nbsp;
                  <el-input v-model="item.value" style="width: 40%"></el-input>
                  &nbsp;&nbsp;
                  <el-button type="text" icon="el-icon-delete" @click="deleteHeader(item.id)"></el-button>
                </div>
              </template>
            </div>
          </el-col>
          <el-col :span="24" v-if="activeParams === 'parameters'">
            <el-button type="text" icon="el-icon-plus" @click="addParameter">添加</el-button>
            <hr color="#cccccc">
            <div class="content_style">
              <template v-for="item in content.parameters">
                <div style="margin-bottom: 5px">
                  <el-input v-model="item.key" style="width: 40%"></el-input>
                  &nbsp;=&nbsp;
                  <el-input v-model="item.value" style="width: 40%"></el-input>
                  &nbsp;&nbsp;
                  <el-button type="text" icon="el-icon-delete" @click="deleteParameter(item.id)"></el-button>
                </div>
              </template>
            </div>
          </el-col>
          <el-col :span="24" v-if="activeParams === 'body'">
            <el-form>
              <el-form-item
                label="内容类型">
                <el-select v-model="content.body.type" @change="changeBody" style="width: 37%;">
                  <el-option value="application/json"></el-option>
                  <el-option value="form-data"></el-option>
                  <el-option value="application/xml"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item
                label="请求内容">
                <el-input type="textarea" v-model="content.body.content" :rows="7" style="width: 90%;" v-if="content.body.type === 'application/json' || content.body.type === 'application/xml'"></el-input>
                <div v-else>
                  <el-button type="text" class="el-icon-plus" style="width: 0px;" @click="addBody">添加</el-button>
                  <div style="display: flex;flex-direction: column;overflow:auto;height: 16.2vh;">
                    <template v-for="item in content.body.content">
                      <div style="margin-bottom: 5px">
                        <el-input v-model="item.key" style="width: 40%"></el-input>
                        &nbsp;=&nbsp;
                        <el-input v-model="item.value" style="width: 40%"></el-input>
                        &nbsp;&nbsp;
                        <el-button type="text" icon="el-icon-delete" @click="deleteBody(item.id)"></el-button>
                      </div>
                    </template>
                  </div>
                </div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <hr color="#cccccc">
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="8">
            <div style="margin-bottom: 5px">
              <el-button @click="startTest" v-loading.fullscreen.lock="fullscreenLoading">测试API</el-button>
              <el-button @click="addTestCase">保存测试用例</el-button>
              <el-button @click="returnBack">返回</el-button>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <br>
    <el-row v-if="resultBox" style="border:1px solid #b6b5b5; margin-top: 120px;border-radius: 5px;">
      <el-col :span="24">
        <h4 style="display: inline-block;float: left;margin-left: 5px">测试结果</h4>
        <el-button type="primary" style="display: inline-block;float: right;margin-top: 10px;margin-right: 10px" @click="clearTestResult">清空结果</el-button>
      </el-col>
      <el-col :span="24">
        <div name="testResult" style="height: 45vh;background-color: #292A2B;">
          <el-button type="text" icon="el-icon-close" @click="closeThisDiv" style="position: relative;top:4%;left: 96%;display: inline-block"></el-button>
          <p style="color: white;width: 200px;display: inline-block">耗时：{{time}}ms</p>
          <p style="color: white;width: 200px;margin-left: 14px">返回结果：</p>
          <codemirror
              ref="myCode"
              :value="curCode"
              :options="cmOptions"
              class="code"
          >
          </codemirror>
        </div>
      </el-col>
    </el-row>
    <outer-ip></outer-ip>
  </el-main>
</template>

<script>
import {
  historyList,
  saveApiTestCase,
  getSign,
  testCaseList,
  deleteApiTestLog,
  deleteApiTestCase,
  findById,
  findApiTestCaseById,
  exportCase,
  getApiTestHeader, insertTestResult
} from "@/api/apiTestManage"
import { getToken } from '@/utils/auth'
import outerIp from '@/components/ip/outerNetIp.vue'
import { codemirror } from 'vue-codemirror'
import "codemirror/theme/panda-syntax.css"
import {getMethod} from "@/api/test";
import { getHttpsMethod } from "@/api/testForHttps"
require("codemirror/mode/javascript/javascript")
export default {
  components:{outerIp,codemirror},
  data() {
    return {
      container:'',
      appId:'',
      url:'',
      //右上参数
      apiId: '',
      apiName:'',
      sourceApiHost:[],
      apiHost:'',
      sourceApiPath:'',
      sourceApiMethod:[],
      protocol:'',
      apiRouteInfo:'',
      apiMethod:'',
      key:'',
      sign:'',
      hDate:'',
      sourceApiProtocol:'',
      saveResult: '0',
      //左侧边栏
      active: 'record',
      activeParams: 'headers',
      //测试记录
      records: [],
      //测试用例
      testcase: [],
      //导入测试用例
      action: '',
      headers:{
        Authorization:''
      },
      fileList:[],

      loading: false,
      //右下参数
      content:{
        headers:[],
        parameters:[],
        body:{
          type:'application/json',
          content: ''
        }
      },
      //测试结果
      resultBox: false,
      testResult:'',
      time: 0,
      fullscreenLoading:false,
      apiTestCaseId:'',
      curCode:'',
      cmOptions:{
        value:'',
        mode:"text/x-mariadb",
        theme: "panda-syntax",
        readOnly:true,
      },
      currentRow: ''
    }
  },
  created() {
    this.apiId = this.$route.query.id
    this.appId = this.$route.query.appId
    this.key = this.$route.query.key
    this.findAllRecordGroup()
    this.getApiParams()

    let userInfo = JSON.parse(sessionStorage.getItem('UserInfo'))

    this.headers.Authorization = 'token ' + userInfo.token
    this.action = process.env.VUE_APP_BASE_API + '/webadmin/apiTestManage/importCase?method='+ this.apiMethod + '&apiId='+this.apiId+'&file='

 },

  methods: {
    //右边栏
    async findAllRecordGroup() {
      const that = this
      let params = {
        apiId: this.apiId
      }
      try {
        that.loading = true
        const res = await historyList(params)
        that.loading = false
        if (res.data.code === 1) {
          that.records = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    async getApiParams(){
      const that = this
      let params = {
        id: this.apiId
      }
      try {
        that.loading = true
        const res = await findById(params)
        that.loading = false
        if (res.data.code === 1) {
          let apiInfo = res.data.data
          that.apiName = apiInfo.name
          that.sourceApiHost = apiInfo.sourceApi.host.split(',')
          that.sourceApiPath = apiInfo.sourceApi.path
          that.sourceApiMethod = apiInfo.sourceApi.method.split(',')
          that.apiRouteInfo = apiInfo.routeInfo
          that.protocol = apiInfo.sourceApi.protocol.split(',')
          that.container = apiInfo.container
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    //获取测试用例
    async findAllTestCaseGroup() {
      const that = this
      let params = {
        apiId:that.apiId
      }
      try {
        that.loading = true
        const res = await testCaseList(params)
        that.loading = false
        if (res.data.code === 1) {
          that.testcase = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    //删除记录
    async deleteRecord(row){
      const that = this
      let response = await deleteApiTestLog(row.id)
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        await that.findAllRecordGroup()
      } else {
        that.$message.error(response.data.msg)
      }
    },

    //删除测试用例
    async deleteTestCase(row){
      const that = this
      if (row.id === that.apiTestCaseId){
        that.apiTestCaseId = ''
      }
      let response = await deleteApiTestCase(row.id)
      if (response.data.code === 1) {
        that.$message.success(response.data.msg)
        this.resetInfo()
        that.findAllTestCaseGroup()
      } else {
        that.$message.error(response.data.msg)
      }
    },

    //下载测试用例
    async downloadTestCase (row) {
      const that = this
      let response = await exportCase({id:row.id})
      if (response.status === 200) {
        const downloadurl = window.URL.createObjectURL(new Blob([JSON.stringify(response.data)], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}))
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = downloadurl
        link.setAttribute('download', row.id + ".json")
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
      } else {
        that.$message.error('下载失败')
      }
    },

    handleClick() {
      if(this.active === 'record'){
        this.findAllRecordGroup()
      } else {
        this.findAllTestCaseGroup()
      }
    },

    handleFileUploadSuccess(){
      this.fileList=[]
      this.resetInfo()
      this.findAllTestCaseGroup()
    },

    //测试用例点击自动填充数据
    async rowClick (row) {
      const that = this
      this.currentRow = row;
      that.apiTestCaseId = row.id
      let params = {
        id: row.id
      }
      try {
        const res = await findApiTestCaseById(params)
        if (res.data.code === 1) {
          let data = res.data.data
          that.apiMethod = data.method
          let content = JSON.parse(data.content)
          //headers
          let id = 0;
          that.content.headers = []
          if (content['Headers']){
            for(var key in content['Headers']){
              let item = {
                id:id,
                key:key,
                value:content['Headers'][key]
              }
              that.content.headers.push(item)
            }
          }
          //parameters
          id= 0;
          that.content.parameters = []
          if (content['Parameters']){
            for(var key in content['Parameters']){
              let item = {
                id:id,
                key:key,
                value:content['Parameters'][key]
              }
              that.content.parameters.push(item)
            }
          }
          //body
          if (content['Content-type']){
            id= 0;
            that.content.body.type = content['Content-type']
            if(that.content.body.type === 'form-data'){
              for(var key in content['Body']){
                let item = {
                  id:id,
                  key:key,
                  value:content['Body'][key]
                }
                that.content.body.content.push(item)
              }
            } else if(that.content.body.type === 'application/json'){
              that.content.body.content = JSON.stringify(content['Body'])
            } else {
              that.content.body.content = content['Body']
            }
          }
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    //右下参数
    addHeader () {
      let head = {
        id: this.content.headers.length,
        key:'',
        value:''
      }

      this.content.headers.push(head)
    },

    deleteHeader (id) {
      this.content.headers.splice(id,1)

      let headersTemp = this.content.headers
      headersTemp.forEach((item,index) => {
        item.id = index
      })

      this.content.headers = headersTemp
    },

    addParameter () {
      let parameter = {
        id: this.content.parameters.length,
        key:'',
        value:''
      }

      this.content.parameters.push(parameter)
    },

    deleteParameter (id) {
      this.content.parameters.splice(id,1)

      let parametersTemp = this.content.parameters
      parametersTemp.forEach((item,index) => {
        item.id = index
      })

      this.content.parameters = parametersTemp
    },

    changeBody () {
      if(this.content.body.type === 'application/json' || this.content.body.type === 'application/xml'){
        this.content.body.content = ''
      }else {
        this.content.body.content = []
      }
    },

    addBody () {
      let body = {
        id: this.content.parameters.length,
        key:'',
        value:''
      }
      this.content.body.content.push(body)
    },

    deleteBody (id) {
      this.content.body.content.splice(id,1)
      let contentTemp = this.content.body.content
      contentTemp.forEach((item,index) => {
        item.id = index
      })
      this.content.body.content = contentTemp
    },

    //右下按钮
    async startTest(){
      const that = this

      if(that.apiMethod === undefined || that.apiMethod === ''){
        that.$message.error('请选择请求方式')
        return false
      }
      that.fullscreenLoading = true
      let content = this.getContentJSONString()
      let tempContent = JSON.parse(content)
      let params = tempContent.Parameters
      let headers = tempContent.Headers
      let parameters = {
        apiId: that.apiId,
        container: that.container,
        headers:JSON.stringify(headers),
        appId:that.appId
      }
      let response = await getApiTestHeader(parameters)
      if (response.data.code === 1) {
        headers=response.data.data
        that.fullscreenLoading = false
      } else {
        that.$message.error(response.data.msg)
        that.fullscreenLoading = false
      }
      let body = tempContent.Body
      let stTime = new Date()
      let pro = that.protocol
      let res = []
      console.log(headers)
      if (pro === 'Https'){
        let baseUrl = 'https://' + that.container.replace(':8444',':8443')
        res = await getHttpsMethod(baseUrl + that.apiRouteInfo,that.apiMethod,headers,params,body)
      }else {
        let baseUrl = 'http://' + that.container.replace(':8001',':8000')
        console.log('baseUrl',baseUrl)
        res = await getMethod(baseUrl + that.apiRouteInfo,that.apiMethod,headers,params,body)
        console.log('res',res)
      }
      let enTime = new Date()
      let relTime = enTime.getMilliseconds() - stTime.getMilliseconds()
      that.curCode = JSON.stringify(res, null, "\t");
      that.resultBox = true
      that.time = relTime
      let data = {
        apiId:that.apiId,
        method:that.apiMethod,
        resultStr:JSON.stringify(res),
        requestStr:content,
        time:relTime
      }
      if (that.saveResult === '1'){
        let resp = await insertTestResult(data)
        if (resp.data.code !== 1) {
          that.$message.error(response.data.msg)
        }
      }
      this.findAllRecordGroup()
      that.fullscreenLoading = false

    },

    async addTestCase() {
      const that = this
      let content= this.getContentJSONString()
      let params = {
        id:that.apiTestCaseId,
        apiId:that.apiId,
        ip:returnCitySN['cip'],
        saveResult: that.saveResult,
        method: that.apiMethod,
        content:content
      }
      this.loading = true
      const response = await saveApiTestCase(params)
      this.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        this.resetInfo()
        this.findAllTestCaseGroup()
        this.$message.success(response.data.msg)
      } else {
        // 添加上传失败后 回调失败信息
        this.$message.error(response.data.msg)
        return false
      }
    },

    resetInfo(){
      const that = this
      that.apiTestCaseId = ''
      // that.apiMethod = ''
      // that.key=''
      // that.sign = ''
      // that.hDate = ''
      // that.saveResult = ''
      that.fullscreenLoading = false
      // that.content = {
      //   headers:[],
      //   parameters:[],
      //   body:{
      //     type:'application/json',
      //     content: ''
      //   }
      // }
    },

    returnBack () {
      const that = this
      that.$router.push({
        path: '/apiManage/apiAuth/appList',
        query: {
          id:that.apiId,
          method:that.sourceApiMethod,
          name:that.apiName
        }
      })
    },

    clearTestResult(){
      const that = this
      that.curCode = ''
      that.time = ''
    },

    getContentJSONString (){
      let content={}
      content['Headers'] = {}
      content['Parameters'] = {}
      content['Body'] = {}
      content['Content-type'] = this.content.body.type
      this.content.headers.forEach((item) => {
        content['Headers'][item.key] = item.value
      })
      this.content.parameters.forEach((item) => {
        content['Parameters'][item.key] = item.value
      })
      if(this.content.body.type === 'form-data'){
        this.content.body.content.forEach((item) => {
          content['Body'][item.key] = item.value
        })
      } else if (this.content.body.type === 'application/json') {
        if (this.content.body.content){
          content['Body'] = JSON.parse(this.content.body.content)
        }
      }else if (this.content.body.type === 'application/xml'){
        let tempXml = "<body>"+this.content.body.content+"</body>"
        content['Body'] = this.$x2js.xml2js(tempXml)
      }
      return JSON.stringify(content)
    },

    async getSignValue(){
      const that = this
      if (that.key === '' || that.key === null){
        this.$message.error('请填写KEY值')
      }else {
        let params = {
          key: that.key,
          apiId: that.apiId,
          ip:returnCitySN['cip']
        }
        try {
          that.loading = true
          const res = await getSign(params)
          that.loading = false
          if (res.data.code === 1) {
            that.sign = res.data.data.sign
            that.hDate = res.data.data.hDate
          } else {
            this.$message.error(res.data.msg)
          }
        } catch (even) {
          this.$message.error(even.msg)
        }
      }
    },

    closeThisDiv(){
      const that = this
      that.resultBox = false
    },

    clearCurrentRow(){
      const that = this
      this.$refs.singleTable.setCurrentRow();
      that.apiTestCaseId = ''
      // that.apiMethod = ''
      // that.content = {
      //   headers:[],
      //   parameters:[],
      //   body:{
      //     type:'application/json',
      //     content: ''
      //   }
      // }
    }
  }

}
</script>

<style scoped>
  .el-col_left {
    height: 100vh;
    box-shadow: 3px 5px 5px #cccccc;
  }
</style>
