<template>
  <el-main class="main">
    <el-tabs v-model="activeName" @tab-click="getTaskList">
      <el-tab-pane label="容器基本信息" name="first">
        <el-col class="main-center">
          <el-form
              :model="ruleForm"
              :rules="rules"
              ref="ruleForm"
              label-width="150px"
              class="demo-ruleForm"
          >
            <el-form-item label="容器名称:" :label-width="this.formLabelWidth" prop="name">
              <el-input
                  clearable
                  maxlength = '100'
                  size="medium"
                  placeholder="请输入容器名称"
                  v-model="ruleForm.name"
              ></el-input>
            </el-form-item>
            <el-form-item label="容器类型:" :label-width="this.formLabelWidth" prop="type">
              <el-select v-model="ruleForm.type" placeholder="请选择容器类型">
                <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="容器IP:" :label-width="this.formLabelWidth" prop="ip">
              <el-input
                  clearable
                  maxlength = '100'
                  size="medium"
                  placeholder="请输入IP地址"
                  v-model="ruleForm.ip"
              ></el-input>
            </el-form-item>
            <el-form-item label="容器端口号:" :label-width="this.formLabelWidth" prop="port">
              <el-input
                  clearable
                  maxlength = '100'
                  size="medium"
                  placeholder="请输入端口号"
                  v-model="ruleForm.port"
              ></el-input>
            </el-form-item>
            <el-form-item label="标签:" :label-width="this.formLabelWidth">
              <div v-for="(item,i) in metas">
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
            <el-form-item label="分组:" :label-width="this.formLabelWidth">
              <el-cascader
                  expandTrigger="hover"
                  v-model="ruleForm.groupId"
                  :options="groupOptions"
                  :props="{ checkStrictly: true,emitPath:false }"
                  clearable>
              </el-cascader>
            </el-form-item>
            <el-form-item label="描述:" :label-width="this.formLabelWidth">
              <el-input
                  type="textarea"
                  :rows="4"
                  clearable
                  maxlength = '100'
                  size="medium"
                  placeholder="请输入数据源描述"
                  v-model="ruleForm.detail"
              ></el-input>
            </el-form-item>
            <div class="demo-drawer__footer drawer_footer">
              <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
              <el-button size="medium" @click="testConnection">链接测试</el-button>
              <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 认</el-button>
            </div>
          </el-form>
        </el-col>
      </el-tab-pane>
      <el-tab-pane label="容器运行任务" name="second">
        <el-col class="main-left" style="margin-left: 20px">
          <el-row>
            <el-form :inline="true">
              <el-form-item>
                <el-input clearable
                          size="mini"
                          placeholder="请输入API名称"
                          v-model="apiDesignSearchItem.name">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" plain size='mini' @click="apiDesignSearch" icon="el-icon-search">查询</el-button>
              </el-form-item>
            </el-form>
            <el-col :span='24'>
              <el-table
                  :data="apiDesignTableData"
                  stripe
                  empty-text="暂无数据"
                  class="el_tab_alage">
                <el-table-column
                    align="left"
                    show-overflow-tooltip
                    prop="name"
                    label="API名称"
                    width="300px"
                >
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
                    align="left"
                    fit
                    prop="path"
                    label="请求路径">
                </el-table-column>
              </el-table>
            </el-col>
            <el-col :span='24'>
              <paginationT ref="apiDesignPage" :total="apiDesignTotal" @pageChange="apiDesignPageChange"></paginationT>
            </el-col>
          </el-row>
        </el-col>
      </el-tab-pane>
      <el-tab-pane label="容器运行日志" name="third">
        <el-col class="main-right">
          <!-- <el-form :inline="true" class='el-InputForm'> -->
            <!-- <el-form-item  label="操作时间" >
              <el-date-picker
                  v-model="apiDesignTime"
                  value-format="yyyy-MM-dd"
                  type="date"
                  placeholder="选择日期"
                  @change="getApiDesignLogList"
              >
              </el-date-picker>
            </el-form-item> -->
          <!-- </el-form> -->
          <el-col :span='24'>
            <el-table
                :data="apiDesignLogTableData"
                stripe
                empty-text="暂无数据"
                class="el_tab_alage">
              <el-table-column
                  align="left"
                  fit
                  prop="name"
                  label="文件名">
              </el-table-column>
              <el-table-column
                  align="left"
                  label="操作"
                  fit>
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-document" slot="reference" @click="downloadApiDesignLogFile(scope.row)">查看日志</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </el-col>
      </el-tab-pane>
    </el-tabs>
    <el-button size='mini' type="primary"  @click="resetForm('ruleForm')" style="position: absolute;top: 42px;right: 10%" icon="el-icon-back">返回列表</el-button>
  </el-main>
</template>

<script>
import {findById, update} from '@/api/containerInfo.js'
import {withoutIpRequest,apiDesignList} from '@/api/withoutIpRequest.js';
import {findList} from '@/api/tagInfo.js';
import pagination from '@/components/table/PaginationSmall'
import paginationT from '@/components/table/Pagination'
import {getGroupList} from "@/api/groupInfo.js"

export default {
  components: {pagination,paginationT},
  data() {
    return {
      ruleForm: {
        name:'',
        ip:'',
        port:'',
        detail:''
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "请填写容器名称",
          trigger: "change"
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请填写容器名称'
        }],
        type: [{
          required: true,
          message: "请选中容器类型",
          trigger: "change"
        }],
        ip: [{
          required: true,
          message: '请输入数据库IP',
          trigger: 'change'
        },{
          pattern:  /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])+$/,  //正则
          message: '请输入正确数据库IP'
        }],
        port: [{
          required: true,
          message: '请输入端口号',
          trigger: 'change'
        },{
          pattern:  /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,  //正则
          message: '请输入正确端口号'
        }],
      },
      groupId:'',
      id:'',
      metas:[{key:'',value:''}],
      tagInfos:[],
      status:0,
      type:'',
      options: [{
        value: '0',
        label: 'API运行'
      }, {
        value: '1',
        label: '数据'
      }, {
        value: '2',
        label: '消息'
      }, {
        value: '3',
        label: 'API网关'
      }, {
        value: '4',
        label: '其他'
      }],
      activeName:'first',
      successType:false,
      errorType:false,
      //api编排
      apiDesignTime:'',
      apiDesignSearchItem: {
        name: ''
      },
      apiDesignLastItem: {
        name: ''
      },
      apiDesignTotal: 0,
      apiDesignPageSize: '20',
      apiDesignCurrentPage: '1',
      apiDesignTableData:[],
      //日志
      //API运行
      apiDesignLogTableData:[],
      apiDesignLogTime:{
        startTime:'',
        endTime:''
      },
      apiDesignLogTotal: 0,
      apiDesignLogPageSize: '5',
      apiDesignLogCurrentPage: '1',
      //级联分组信息
      groupOptions:[]
    }
  },

  props: {
    groupId: {
      type: Object,
      default: () => {}
    },
    id: {
      type: Object,
      default: () => {}
    }
  },

  created () {
    this.fetchData()
    this.getGroupList()
  },
  methods: {
    //获取分组列表
    async getGroupList() {
      const that = this
      let data = { type: '1' }
      try {
        const res = await getGroupList(data)
        if (res.data.code === 1) {
          that.groupOptions = res.data.data
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        that.$message.error(even.msg)
      }
    },
    //获取容器信息
    async fetchData() {
      const that = this
      let data = { id: that.id }
      try {
        const res = await findById(data)
        if (res.data.code === 1) {
          let content = res.data.data
          // integrate tag info
          let tempName = content.tagName
          let tempValue = content.tagValue
          if (tempName.indexOf(',') === -1){
            that.metas = [{key:tempName,value:tempValue}]
          }else {
            let metas = []
            let nameArr = tempName.split(',')
            let valueArr = tempValue.split(',')
            nameArr.forEach((item,index)=>{
              metas.push({key:item,value:valueArr[index]})
            })
            that.metas = metas
          }
          that.status = content.status
          that.type = content.type
          that.ruleForm = content
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //编辑容器信息
    submitForm(ruleForm) {
      const that = this
      let metaInfo = that.metas
      if (metaInfo[0].key === ''){
        that.$message.error('请选择标签');
        return false
      }
      let tempName = ''
      let tempValue = ''
      for (let te of metaInfo) {
        tempName = tempName + te.key + ','
        tempValue = tempValue + te.value + ','
      }
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = that.ruleForm
          if (!that.ruleForm.groupId){
            data.groupId = that.groupId
          }
          data.status = that.status
          data.tagName = tempName.substr(0,tempName.length-1)
          data.tagValue = tempValue.substr(0,tempValue.length-1)
          update(data).then((res) => {
            if (res.data.code === 1) {
              that.$message.success('编辑成功');
              that.resetForm('ruleForm')

            } else {
              that.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    //返回容器列表
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    //初始化表单
    CloseModal() {
      this.ruleForm.name = ""
      this.ruleForm.type = ""
      this.ruleForm.jdbcUrl = ""
      this.ruleForm.userName = ""
      this.ruleForm.password = ""
      this.ruleForm.detail = ""
      let groupId = this.groupId
      this.groupId = ""
      this.$router.push({
        path: '/container',
        query: {
          groupId:groupId
        }
      })
    },
    //测试连接
    async testConnection(){
      let data = this.ruleForm
      let ip = data.ip
      if (!ip){
        this.$message.error('请填写IP地址');
        return
      }
      let port = data.port
      if(!port){
        this.$message.error('请填写端口号');
        return
      }
      let type = data.type
      if(!type){
        this.$message.error('请选择容器类型');
        return
      }
      let baseUrl = 'http://'+ip+':'+port
      if (data.type === 3){
        try {
          let res = await withoutIpRequest(baseUrl, '', 'get')
          if (res.status === 200) {
            this.status = 1
            this.$message.success('链接成功');
          } else {
            this.$message.error('链接失败');
          }
        } catch (e) {
          this.$message.error('链接失败');
        }
      }else {
        try {
          let url = '/api/container/status'
          let res = await withoutIpRequest(baseUrl, url, 'get')
          if (res.status === 200) {
            this.status = 1
            this.$message.success('链接成功');
          } else {
            this.$message.error('链接失败');
          }
        } catch (e) {
          this.$message.error('链接失败');
        }
      }
    },

    insertMeta(){
      const that = this
      let item = {
        key:'',
        value:''
      }
      that.metas.push(item)
    },

    removeMeta(i){
      const that = this
      that.metas.splice(i,1)
    },

    async remoteMethod(query){
      if (query !== '') {
        let res = await findList({name:query})
        if (res.data.code === 1){
          this.tagInfos = res.data.data
        }
      }
    },

    //容器运行任务
    async getTaskList(){
      if (this.activeName === 'second'){
        await this.apiDesignFetchData()
      }
      if (this.activeName === 'third'){
        await this.getApiDesignLogList()
      }
    },

    //API编排查询
    async apiDesignFetchData(){
      const that = this
      let container = that.ruleForm
      let data = that.apiDesignSearchItem
      data.page = that.apiDesignCurrentPage
      data.size = that.apiDesignPageSize
      let ip = container.ip
      let port = container.port
      let baseurl = 'http://'+ip +':'+port
      let url = '/api/container/findAll'
      try {
        let res = await apiDesignList(baseurl,url,'GET',data)
        if (res.data.code === 1) {
          that.apiDesignTableData = res.data.data.content
          that.apiDesignTotal = res.data.data.totalElements
        }else {
          that.$message.error(res.data.msg)
        }
      }catch (e){
        that.$message.error(e.msg)
      }
    },

    apiDesignSearch(){
      this.apiDesignCurrentPage = 1
      this.$refs.apiDesignPage.Page(1)
      this.apiDesignFetchData()
    },

    apiDesignPageChange(item){
      let that = this
      this.apiDesignPageSize = item.limit
      this.apiDesignCurrentPage = item.page
      Object.entries(that.apiDesignSearchItem).map((item, index) => {
        that.apiDesignSearchItem[item[0]] = that.apiDesignLastItem[item[0]]
      })
      this.apiDesignFetchData()
    },

    //运行日志
    async getApiDesignLogList(){
      const that = this
      let time = that.apiDesignTime
      if (!time){
        time = that.dateFormat('YYYY-mm-dd',new Date())
      }
      that.apiDesignTime = time
      let data = {
        day:time
      }
      let container = that.ruleForm
      let ip = container.ip
      let port = container.port
      let baseUrl = 'http://'+ip +':'+port
      let url = '/api/container/log'
      try {
        let res = await apiDesignList(baseUrl, url, 'GET', data)

        if (res.data.code === 1) {
          let content = res.data.data
          let tempContent = []
          content.forEach(item=>{
            tempContent.push({name:item})
          })
          that.apiDesignLogTableData = tempContent
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (e) {
        that.$message.error(e.msg)
      }
    },

    async downloadApiDesignLogFile(row){
      const that = this
      let date = that.apiDesignTime
      let name = row.name
      let container = that.ruleForm
      let ip = container.ip
      let port = container.port
      let baseUrl = 'http://'+ip +':'+port
      let url = '/api/download/log/'+date+'/'+name
      try {
        let response = await apiDesignList(baseUrl, url, 'GET', '')
        if (response.status === 200) {
          const downloadUrl = window.URL.createObjectURL(new Blob([JSON.stringify(response.data)], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'}))
          const link = document.createElement('a')
          link.style.display = 'none'
          link.href = downloadUrl
          link.setAttribute('download', name)
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
        } else {
          that.$message.error('下载失败')
        }
      } catch (e) {
        that.$message.error(e.msg)
      }
    },

    //格式化日期用
    dateFormat(fmt, date) {
      let ret;
      const opt = {
        "Y+": date.getFullYear().toString(),        // 年
        "m+": (date.getMonth() + 1).toString(),     // 月
        "d+": date.getDate().toString(),            // 日
        "H+": date.getHours().toString(),           // 时
        "M+": date.getMinutes().toString(),         // 分
        "S+": date.getSeconds().toString()          // 秒
        // 有其他格式化字符需求可以继续添加，必须转化成字符串
      };
      for (let k in opt) {
        ret = new RegExp("(" + k + ")").exec(fmt);
        if (ret) {
          fmt = fmt.replace(ret[1], (ret[1].length === 1) ? (opt[k]) : (opt[k].padStart(ret[1].length, "0")))
        }
      }
      return fmt;
    },
  }
}
</script>
<style lang="scss" scoped>
.main-center{
  width: 45%;
  margin-top: 20px;
  margin-left: 260px
}
.main-left{
  width: 95%;
}
.main-right{
  width: 95%;
}
.numRow{
  height: 30px;
}
.demo-drawer__footer{
  margin-top: 10px;
  text-align: center;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
