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
              <el-select v-model="ruleForm.type" placeholder="请选择容器类型" style="width: 100%;">
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
            <el-form-item label="分组:" :label-width="this.formLabelWidth">
              <el-cascader
                  expandTrigger="hover"
                  v-model="ruleForm.groupId"
                  :options="groupOptions"
                  :props="{ checkStrictly: true,emitPath:false }"
                  clearable style="width: 100%;">
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
            <div class="demo-drawer__footer">
              <el-button size="medium" @click="testConnection">连接测试</el-button>
              <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 认</el-button>
            </div>
          </el-form>
        </el-col>
      </el-tab-pane>
<!--       <el-tab-pane label="容器运行任务" name="second">
        <el-col class="main-left" style="margin-left: 20px">
          <el-row>
            <el-row class="numRow">任务数:{{this.taskNum}}</el-row>
            <el-row class="numRow">定时任务:{{this.timingNum}}</el-row>
            <el-row class="numRow">实时任务:{{this.realTimeNum}}</el-row>
            <el-row class="numRow">启动任务:{{this.startNum}}</el-row>
            <el-row class="numRow">停止任务:{{this.endNum}}</el-row>
          </el-row>
          <el-divider></el-divider>
          <el-row>
            <el-form :inline="true">
              <el-form-item>
                <el-input clearable
                          size="mini"
                          placeholder="请输入任务名称"
                          v-model="dataSearchItem.name">
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" plain size='mini' @click="dataSearch" icon="el-icon-search">查询</el-button>
              </el-form-item>
            </el-form>
            <el-col :span='24'>
              <el-table
                  :data="dataTableData"
                  stripe
                  empty-text="暂无数据"
                  class="el_tab_alage">
                <el-table-column
                    align="left"
                    show-overflow-tooltip
                    prop="name"
                    label="任务名称"
                    fit
                >
                </el-table-column>
                <el-table-column
                    align="left"
                    fit
                    prop="status"
                    label="运行状态">
                  <template slot-scope="scope">
                    <div v-if="scope.row.status === 0">
                      <div style="display:inline-block;width: 18px;height: 18px;border-radius: 50%;border: 1px solid #ff0000; position: relative;top:5px" >
                        <div style="width: 10px;height: 10px;border-radius: 50%;background-color: #ff0000;position: relative;top:3px;left: 3px"></div>
                      </div>
                      <span style="margin-left: 5px">未启动</span>
                    </div>
                    <div v-if="scope.row.status === 1">
                      <div style="display:inline-block;width: 18px;height: 18px;border-radius: 50%;border: 1px solid #00ff00; position: relative;top:5px" >
                        <div style="width: 10px;height: 10px;border-radius: 50%;background-color: #00ff00;position: relative;top:3px;left: 3px"></div>
                      </div>
                      <span style="margin-left: 5px">已启动</span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                    align="left"
                    fit
                    prop="mode"
                    label="集成模式">
                </el-table-column>
                <el-table-column
                    align="left"
                    fit
                    prop="runNum"
                    label="运行次数">
                </el-table-column>
                <el-table-column
                    align="left"
                    fit
                    prop="taskSuccessNum"
                    label="成功次数">
                </el-table-column>
                <el-table-column
                    align="left"
                    fit
                    prop="taskFailNum"
                    label="失败次数">
                </el-table-column>
              </el-table>
            </el-col>
            <el-col :span='24'>
              <pagination ref="dataPage" :total="dataTotal" @pageChange="dataPageChange"></pagination>
            </el-col>
          </el-row>
        </el-col>
      </el-tab-pane>
      <el-tab-pane label="容器运行日志" name="third">
        <el-col v-if="this.type ==='1'" class="main-right">
          <el-form :inline="true" class='el-InputForm'>
            <el-form-item  label="操作时间" >
              <el-date-picker
                  v-model="dataLogTime"
                  value-format="yyyy-MM-dd"
                  type="date"
                  placeholder="选择日期"
              >
              </el-date-picker>
            </el-form-item>
          </el-form>
          <el-col :span='24'>
            <el-table
                :data="dataLogTableData"
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
                  <el-button size="mini" type="text" icon="el-icon-document" slot="reference" @click="downloadLogFile(scope.row)">查看日志</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span='24'>
            <pagination ref="dataPage" :total="dataTotal" @pageChange="dataPageChange"></pagination>
          </el-col>
        </el-col>
      </el-tab-pane> -->
    </el-tabs>
  </el-main>
</template>

<script>
import pagination from '../../../integration/PaginationSmall'

export default {
  components: {pagination},
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
      //数据集成
      taskNum:'12',
      timingNum:'22',
      realTimeNum:'14',
      startNum:'11',
      endNum:'13',
      //数据集成
      dataSearchItem: {
        name: ''
      },
      dataLastItem: {
        name: ''
      },
      dataTotal: 0,
      dataPageSize: '5',
      dataCurrentPage: '1',
      dataTableData:[],
      //日志
      //数据集成
      dataLogTableData:[],
      dataLogTime:'',
      dataLogTotal: 0,
      dataLogPageSize: '5',
      dataLogCurrentPage: '1',
      //级联分组信息
      groupOptions:[]
    }
  },

  props: {
    groupId: {
      type: String,
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
      try {
        let url = this.Interface.groupInfo.getList
        let res = await this.request.dataGet(that, url, { type: '1' })
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
      try {
        let url = this.Interface.container.findById
        let res = await this.request.dataGet(that, url, { id: that.id })
        if (res.data.code === 1) {
          let content = res.data.data
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
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          let data = that.ruleForm
          if (!that.ruleForm.groupId){
            data.groupId = that.groupId
          }
          data.status = that.status
          that.editSave(data)
        } else {
          return false;
        }
      });
    },
    async editSave (data) {
      const that = this
      let url = that.Interface.container.update
      const response = await that.request.dataPut(that, url, data)
      if (response.data.code === 1) {
        this.$message.success('编辑成功')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    //测试连接
    async testConnection(){
      const that = this
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
      let url = '/task/container/status'
      try {
        let res = await that.request.dataGet(that, (baseUrl + url), {})
        if (res.status === 200) {
          this.status = 1
          this.$message.success('连接成功');
        } else {
          this.$message.error('连接失败');
        }
      } catch (e) {
        this.$message.error('连接失败');
      }

    },

    //容器运行任务
    async getTaskList(){
      if (this.activeName === 'second'){
        await this.dataFetchData()
        await this.dataStatistical()
      }
      if (this.activeName === 'third'){
        await this.getLogList()
      }
    },

    //数据集成
    async dataFetchData(){
      const that = this
      let container = that.ruleForm
      let data = that.dataSearchItem
      data.page = that.dataCurrentPage
      data.size = that.dataPageSize
      let ip = container.ip
      let port = container.port
      data.ip=ip
      data.port = port
      let baseUrl = process.env.VUE_APP_DATA_INTEGRATION_URL
      let url = '/webadmin/task/container/findAll'
      try {
        let res = await that.request.dataGet(that, (baseUrl + url), data)
        if (res.data.code === 1) {
          that.dataTableData = res.data.data.content
          that.dataTotal = res.data.data.totalElements
        }else {
          that.$message.error(res.data.msg)
        }
      }catch (e){
        that.$message.error(e.msg)
      }
    },

    async dataStatistical(){
      const that = this
      let row = that.ruleForm
      let ip = row.ip
      let port = row.port
      let data = {
        ip:ip,
        port:port
      }
      let baseUrl = process.env.VUE_APP_DATA_INTEGRATION_URL
      let url = '/webadmin/task/container/findInfo'
      try {
        let res = await that.request.dataGet(that, (baseUrl + url), data)
        if (res.data.code === 1) {
          let content = res.data.data
          that.taskNum = content.taskNum
          that.timingNum = content.timingNum
          that.realTimeNum = content.realTimeNum
          that.startNum = content.startNum
          that.endNum = content.endNum
        }else {
          that.$message.error(res.data.msg)
        }
      }catch (e){
        that.$message.error(e.msg)
      }
    },

    dataSearch(){
      this.dataCurrentPage = 1
      this.$refs.dataPage.changePage(1)
      this.dataFetchData()
    },

    dataPageChange(item){
      let that = this
      this.dataPageSize = item.limit
      this.dataCurrentPage = item.page
      Object.entries(that.dataSearchItem).map((item, index) => {
        that.dataSearchItem[item[0]] = that.dataLastItem[item[0]]
      })
      this.dataFetchData()
    },

    //运行日志
    async getLogList(){
      const that = this
      let time = that.dataLogTime
      if (!time){
        time = that.dateFormat('YYYY-mm-dd',new Date())
      }
      that.dataLogTime = time
      let data = {
        day:time
      }
      let container = that.ruleForm
      let ip = container.ip
      let port = container.port
      let baseUrl = 'http://'+ip +':'+port
      let url = '/task/container/log'
      try {
        let res = await that.request.dataGet(that, (baseUrl + url), data)
        if (res.data.code === 1) {
          let content = res.data.data
          let tempContent = []
          content.forEach(item=>{
            tempContent.push({name:item})
          })
          that.dataLogTableData = tempContent
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (e) {
        that.$message.error(e.msg)
      }
    },

    async downloadLogFile(row){
      const that = this
      let date = that.dataLogTime
      let name = row.name
      let container = that.ruleForm
      let ip = container.ip
      let port = container.port
      let baseUrl = 'http://'+ip +':'+port
      let data = {
        day :date,
        fileName:name
      }
      let url = '/task/container/download'
      try {
        let res = await that.request.dataGet(that, (baseUrl + url), data)
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
  text-align: left;
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
