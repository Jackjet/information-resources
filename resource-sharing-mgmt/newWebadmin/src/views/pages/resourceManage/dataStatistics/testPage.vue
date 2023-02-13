<template>
  <el-main class="main">
    <el-row class="content">
      <!--<el-col :span="8" class="el-col_left">-->
      <!--<el-row>-->
      <!--<el-col :span="24">-->
      <!--<el-tabs v-model="active" @tab-click="handleClick">-->
      <!--<el-tab-pane label="" disabled></el-tab-pane>-->
      <!--<el-tab-pane label="历史记录" name="record"></el-tab-pane>-->
      <!--<el-tab-pane label="测试用例" name="testCase"></el-tab-pane>-->
      <!--<el-tab-pane label="" disabled></el-tab-pane>-->
      <!--</el-tabs>-->
      <!--</el-col>-->
      <!--</el-row>-->
      <!--<el-row v-if="active === 'record'">-->
      <!--<el-col :span="24">-->
      <!--<div style="height: 100vh; overflow:auto;">-->
      <!--<el-table-->
      <!--:data="records"-->
      <!--style="width: 100%"-->
      <!--:show-header="false">-->
      <!--<el-table-column-->
      <!--type="expand"-->
      <!--prop="id">-->
      <!--<template slot-scope="props">-->
      <!--<el-table-->
      <!--:data="props.row.sourceApiTestRecords"-->
      <!--:show-header="false">-->
      <!--<el-table-column-->
      <!--prop="method"-->
      <!--width="75px">-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--prop="responseCode"-->
      <!--width="88px">-->
      <!--<template slot-scope="scope">-->
      <!--状态：{{scope.row.responseCode}}-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--prop="responseTime"-->
      <!--width="55px">-->
      <!--<template slot-scope="scope">-->
      <!--{{scope.row.responseTime}}ms-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--style="width: 5px">-->
      <!--<template slot-scope="scope">-->
      <!--<el-button type="text" icon="el-icon-delete" @click="deleteRecord(scope.row)"></el-button>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--</el-table>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--prop="requestDay">-->
      <!--</el-table-column>-->
      <!--</el-table>-->
      <!--</div>-->
      <!--</el-col>-->
      <!--</el-row>-->
      <!--<el-row v-if="active === 'testCase'">-->
      <!--<el-col :span="16" :offset="8">-->
      <!--<el-upload-->
      <!--class="upload-demo"-->
      <!--:action="action"-->
      <!--:headers="headers"-->
      <!--:file-list="fileList"-->
      <!--multiple="false"-->
      <!--:on-success="handleFileUploadSuccess"-->
      <!--:before-upload="beforeUpload">-->
      <!--<el-button type="mini" icon="el-icon-upload2">导入测试用例</el-button>-->
      <!--<div slot="tip" class="el-upload__tip">只能上传txt和json文件</div>-->
      <!--</el-upload>-->
      <!--</el-col>-->
      <!--</el-row>-->
      <!--<el-row v-if="active === 'testCase'">-->
      <!--<el-col :span="24">-->
      <!--<el-table-->
      <!--:data="testcase"-->
      <!--style="width: 100%"-->
      <!--:show-header="false">-->
      <!--<el-table-column-->
      <!--type="expand"-->
      <!--prop="id">-->
      <!--<template slot-scope="props">-->
      <!--<el-table-->
      <!--ref="singleTable"-->
      <!--:data="props.row.sourceApiTestCases"-->
      <!--:show-header="false"-->
      <!--highlight-current-row-->
      <!--@current-change="rowClick">-->
      <!--<el-table-column-->
      <!--prop="method">-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--prop="ip"-->
      <!--min-width="110px">-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--style="width: 5px">-->
      <!--<template slot-scope="scope">-->
      <!--<el-button type="text" icon="el-icon-download" @click="downloadTestCase(scope.row)"></el-button>-->
      <!--<el-button type="text" icon="el-icon-delete" @click="deleteTestCase(scope.row)"></el-button>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--</el-table>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <!--<el-table-column-->
      <!--prop="createDay">-->
      <!--</el-table-column>-->
      <!--</el-table>-->
      <!--<el-button type="primary" @click="clearCurrentRow()" size="small" style="position: relative;left: 38%">取消选择</el-button>-->
      <!--</el-col>-->
      <!--</el-row>-->
      <!--</el-col>-->
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
              <el-form-item label="名称" label-width="150px" style="width: 80%">
                <el-input disabled v-model="sourceApiName"></el-input>
              </el-form-item>
              <el-form-item label="Host" label-width="150px" style="width: 80%">
                <el-select v-model="host" placeholder="请选择" style="width: 100%">
                  <el-option v-for="item in sourceApiHost" :key="item.target" :label="item.target" :value="item.target">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="Path" label-width="150px" style="width: 80%">
                <el-input disabled v-model="sourceApiPath"></el-input>
              </el-form-item>
              <el-form-item label="请求方式" label-width="150px" style="width: 80%">
                <el-select v-model="sourceApiMethod" style="width: 100px;">
                  <el-option v-for="item in methodOptions" :value="item"></el-option>
                </el-select>
              </el-form-item>
              <!--<el-form-item-->
              <!--prop="saveResult"-->
              <!--label="是否保存测试结果"-->
              <!--label-width="150px"-->
              <!--style="width: 80%">-->
              <!--<el-radio-group v-model="saveResult">-->
              <!--<el-radio label="1">是</el-radio>-->
              <!--<el-radio label="0">否</el-radio>-->
              <!--</el-radio-group>-->
              <!--</el-form-item>-->
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
              <el-form-item label="内容类型">
                <el-select v-model="content.body.type" @change="changeBody" style="width: 37%;">
                  <el-option value="application/json"></el-option>
                  <el-option value="form-data"></el-option>
                  <el-option value="application/xml"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="请求内容">
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
              <!--<el-button @click="addTestCase">保存测试用例</el-button>-->
              <el-button @click="returnBack">返回</el-button>
            </div>
          </el-col>
        </el-row>
      </el-col>
    </el-row>

    <el-row v-if="resultBox" style="border:1px solid #b6b5b5; margin-top: 120px;border-radius: 5px;">
      <el-col :span="24">
        <h4 style="display: inline-block;float: left;margin-left: 5px">测试结果</h4>
        <el-button type="primary" style="display: inline-block;float: right;margin-top: 10px;margin-right: 10px" @click="clearResult">清空结果</el-button>
      </el-col>
      <el-col :span="24">
        <div name="testResult" style="height: 45vh;background-color: #292A2B;">
          <el-button type="text" icon="el-icon-close" @click="closeResultBox" style="position: relative;top:4%;left: 96%;display: inline-block"></el-button>
          <p style="color: white;width: 200px;display: inline-block">耗时：{{time}}ms</p>
          <p style="color: white;width: 200px;margin-left: 14px">返回结果：</p>
          <codemirror ref="myCode" :value="curCode" :options="cmOptions" class="code">
          </codemirror>
        </div>
      </el-col>
    </el-row>
    <outer-ip></outer-ip>
  </el-main>
</template>

<script>
// import { getToken } from '@/utils/auth'
import outerIp from '@/components/ip/outerNetIp.vue'
import { getHttpsMethod } from '@/api/withoutIpRequest'
import { codemirror } from 'vue-codemirror'
import "codemirror/theme/panda-syntax.css"
require("codemirror/mode/javascript/javascript")
export default {
  components: {
    outerIp,
    codemirror
  },
  data() {
    return {
      // url:'',
      // //右上参数
      sourceApiId: '',
      sourceApiName: '',
      sourceApiHost: '',
      sourceApiPath: '',
      sourceApiMethod: '',
      sourceApiProtocol: '',
      saveResult: '0',
      host: '',
      methodOptions: [],
      // //左侧边栏
      // active: 'record',
      activeParams: 'headers',
      // //测试记录
      // records: [],
      // //测试用例
      // testcase: [],
      // testcaseId:'',
      // //导入测试用例
      // action: '',
      headers: {
        Authorization: ''
      },
      // fileList:[],
      //
      loading: false,
      //右下参数
      content: {
        headers: [],
        parameters: [],
        body: {
          type: 'application/json',
          content: ''
        }
      },
      //测试结果
      resultBox: false,
      testResult: '',
      message: '',
      time: 0,
      curCode: '',
      cmOptions: {
        value: '',
        mode: "text/x-mariadb",
        theme: "panda-syntax",
        readOnly: true,
      },
      fullscreenLoading: false,
    }
  },
  created() {
    this.sourceApiId = this.$route.query.id
    this.sourceApiName = this.$route.query.name
    this.sourceApiHost = JSON.parse(this.$route.query.host)
    this.host = this.sourceApiHost[0].target
    this.sourceApiPath = this.$route.query.path
    this.methodOptions = this.$route.query.method.split(',')
    this.sourceApiMethod = this.methodOptions[0]
    this.sourceApiProtocol = this.$route.query.protocol
    // this.findAllRecordGroup()

    // this.headers.Authorization = 'token ' + getToken()
    // this.action = process.env.VUE_APP_BASE_API + '/webadmin/sourceApiTest/importTestCase?sourceApiId=' + this.sourceApiId + '&method='+ this.sourceApiMethod + '&file='
  },
  methods: {
    //右边栏
    // async findAllRecordGroup() {
    //     const that = this
    //     let params = {
    //         sourceApiId: this.sourceApiId
    //     }
    //     try {
    //         that.loading = true
    //         const res = await findAllRecordGroup(params)
    //         that.loading = false
    //         if (res.data.code === 1) {
    //             that.records = res.data.data
    //         } else {
    //             this.$message.error(res.data.msg)
    //         }
    //     } catch (even) {
    //         this.$message.error(even.msg)
    //     }
    // },

    // async findAllTestCaseGroup() {
    //     const that = this
    //     let params = {
    //         sourceApiId: this.sourceApiId
    //     }
    //     try {
    //         that.loading = true
    //         const res = await findAllTestCaseGroup(params)
    //         that.loading = false
    //         if (res.data.code === 1) {
    //             that.testcase = res.data.data
    //         } else {
    //             this.$message.error(res.data.msg)
    //         }
    //     } catch (even) {
    //         this.$message.error(even.msg)
    //     }
    // },
    // async deleteRecord(row){
    //     const that = this
    //     let response = await sourceApiTestRecordDelete({id:row.id})
    //     if (response.data.code === 1) {
    //         that.$message.success(response.data.msg)
    //         await that.findAllRecordGroup()
    //     } else {
    //         that.$message.error(response.data.msg)
    //     }
    // },
    // beforeUpload (file) {
    //     const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
    //
    //     const whiteList = ['json','txt'];
    //
    //     if (whiteList.indexOf(fileSuffix) === -1) {
    //         this.$message.error("上传文件只能是json和txt格式")
    //         return false;
    //     }
    //
    // },

    // async deleteTestCase(row){
    //     const that = this
    //     let response = await deleteTestCase({id:row.id})
    //     if (response.data.code === 1) {
    //         that.$message.success(response.data.msg)
    //         await that.findAllTestCaseGroup()
    //         that.clearCurrentRow()
    //     } else {
    //         that.$message.error(response.data.msg)
    //     }
    // },

    // async downloadTestCase (row) {
    //     const that = this
    //     let response = await downloadTestCase({id:row.id})
    //     if (response.status === 200) {
    //         const downloadUrl = window.URL.createObjectURL(new Blob([JSON.stringify(response.data)], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}))
    //         const link = document.createElement('a')
    //         link.style.display = 'none'
    //         link.href = downloadUrl
    //         link.setAttribute('download', row.id + ".json")
    //         document.body.appendChild(link)
    //         link.click()
    //         document.body.removeChild(link)
    //     } else {
    //         that.$message.error('下载失败')
    //     }
    // },
    // handleClick() {
    //     if(this.active === 'record'){
    //         this.findAllRecordGroup()
    //     } else {
    //         this.findAllTestCaseGroup()
    //     }
    // },
    // handleFileUploadSuccess(){
    //     this.fileList=[]
    //     this.findAllTestCaseGroup()
    //     this.clearCurrentRow()
    // },
    // rowClick (row) {
    //     this.testcaseId = row.id
    //     let content = JSON.parse(row.content)
    //
    //     //headers
    //     let id= 0;
    //     this.content.headers = []
    //     for(var key in content['Headers']){
    //         let item = {
    //             id:id,
    //             key:key,
    //             value:content['Headers'][key]
    //         }
    //
    //         this.content.headers.push(item)
    //         id ++
    //     }
    //
    //     //parameters
    //     id= 0;
    //     this.content.parameters = []
    //     for(var key in content['Parameters']){
    //         let item = {
    //             id:id,
    //             key:key,
    //             value:content['Parameters'][key]
    //         }
    //
    //         this.content.parameters.push(item)
    //         id ++
    //     }
    //
    //     //body
    //     id= 0;
    //     this.content.body.type = content['Content-type']
    //     if(this.content.body.type === 'form-data'){
    //         for(var key in content['Body']){
    //             let item = {
    //                 id:id,
    //                 key:key,
    //                 value:content['Body'][key]
    //             }
    //
    //             this.content.body.content.push(item)
    //             id ++
    //         }
    //     } else if(this.content.body.type === 'application/json'){
    //         this.content.body.content = JSON.stringify(content['Body'])
    //     } else {
    //         this.content.body.content = content['Body']
    //     }
    // },
    //右下参数
    addHeader() {
      let head = {
        id: this.content.headers.length,
        key: '',
        value: ''
      }

      this.content.headers.push(head)
    },
    deleteHeader(id) {
      this.content.headers.splice(id, 1)

      let headersTemp = this.content.headers
      headersTemp.forEach((item, index) => {
        item.id = index
      })

      this.content.headers = headersTemp
    },
    addParameter() {
      let parameter = {
        id: this.content.parameters.length,
        key: '',
        value: ''
      }

      this.content.parameters.push(parameter)
    },
    deleteParameter(id) {
      this.content.parameters.splice(id, 1)

      let parametersTemp = this.content.parameters
      parametersTemp.forEach((item, index) => {
        item.id = index
      })

      this.content.parameters = parametersTemp
    },
    changeBody() {
      if (this.content.body.type === 'application/json' || this.content.body.type === 'application/xml') {
        this.content.body.content = ''
      } else {
        this.content.body.content = []
      }
    },
    addBody() {
      let body = {
        id: this.content.parameters.length,
        key: '',
        value: ''
      }

      this.content.body.content.push(body)
    },
    deleteBody(id) {
      this.content.body.content.splice(id, 1)

      let contentTemp = this.content.body.content
      contentTemp.forEach((item, index) => {
        item.id = index
      })

      this.content.body.content = contentTemp
    },

    //右下按钮
    async startTest() {
      const that = this
      that.fullscreenLoading = true
      let content = this.getContentJSONString()
      let tempContent = JSON.parse(content)
      let params = tempContent.Parameters
      let headers = tempContent.Headers
      let body = tempContent.Body
      let stTime = new Date()
      let type = that.sourceApiProtocol
      let url = type.toLowerCase() + "://" + that.host
      let res = await getHttpsMethod(url, that.sourceApiPath, that.sourceApiMethod, headers, params, body)
      let enTime = new Date()
      let relTime = enTime.getTime() - stTime.getTime()
      that.curCode = JSON.stringify(res, null, "\t");
      that.resultBox = true
      that.time = relTime
      if (that.saveResult === '1') {
        let data = {
          apiId: that.sourceApiId,
          method: that.sourceApiMethod,
          resultStr: JSON.stringify(res),
          requestStr: content,
          time: relTime
        }
        let resp = await insertTestResult(data)
        if (resp.data.code !== 1) {
          that.$message.error(resp.data.msg)
        }
      }
      // this.findAllRecordGroup()
      that.fullscreenLoading = false

    },

    // async addTestCase() {
    //     let content= this.getContentJSONString()
    //
    //     let params = {
    //         id:this.testcaseId,
    //         ip:returnCitySN['cip'],
    //         saveResult: this.saveResult,
    //         method: this.sourceApiMethod,
    //         content:content,
    //         sourceApiId: this.sourceApiId
    //     }
    //
    //     this.loading = true
    //     const response = await addTestCase(params)
    //     this.loading = false
    //     if (response.data.code === 1) {
    //         // 添加成功后 回调效果
    //         this.$message.success(response.data.msg)
    //         await this.findAllTestCaseGroup()
    //         this.clearCurrentRow()
    //     } else {
    //         // 添加上传失败后 回调失败信息
    //         this.$message.error(response.data.msg)
    //         return false
    //     }
    // },
    returnBack() {
      this.$router.push({
        path: '/archBusiUviewAdd'
      })
    },

    getContentJSONString() {
      let content = {}
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

      if (this.content.body.type === 'form-data') {
        this.content.body.content.forEach((item) => {
          content['Body'][item.key] = item.value
        })
      } else if (this.content.body.type === 'application/xml') {
        content['Body'] = '<body>' + this.content.body.content + '</body>'
      } else if (this.content.body.content === '') {
        content['Body'] = {}
      } else {
        content['Body'] = JSON.parse(this.content.body.content)
      }

      return JSON.stringify(content)
    },
    //测试框
    closeResultBox() {
      this.resultBox = false
    },
    // clearCurrentRow(){
    //     const that = this
    //     that.testcaseId =''
    //     this.$refs.singleTable.setCurrentRow();
    // },
    clearResult() {
      this.time = '0'
      this.curCode = ''

    }
  }
}
</script>

<style scoped>
.main {
  background: #00000000;
  height: 100%;
  width: 100%;
  padding: 0 30px;
  min-height: 600px;
}

.content {
  height: 100vh;
  border-radius: 5px;
  background-color: white;
  padding: 0 0 30px 0;
}

.el-col_left {
  height: 100vh;
  box-shadow: 3px 5px 5px #cccccc;
}

.el-col_right {
  height: 100vh;
  padding: 0 0 0 20px;
}

.content_style {
  height: 25vh;
  overflow: auto;
}
</style>
