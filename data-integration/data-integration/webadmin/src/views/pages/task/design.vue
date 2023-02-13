<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="toBack" icon="el-icon-back">返回</el-button>
          <el-button size='medium' @click="showDialog" style="margin-left: 15px;" icon="el-icon-upload">导入作业</el-button>
          <el-button size='medium' @click="debugDialog" style="margin-left: 15px;" icon="el-icon-caret-right">调试</el-button>
          <el-button size='medium' @click="downloadFiles" style="margin-left: 15px;" icon="el-icon-download">下载</el-button>
          <el-button size='medium' @click="deleteFiles" style="margin-left: 15px;" icon="el-icon-delete">删除</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <el-col :span="designerWidth">
        <div id="chart-diagram" style="height: 600px;"></div>
      </el-col>
      <el-col :span="propertiesWidth">
        <el-card style="width: 100%;height: 600px" class="box-card">
          <div slot="header" class="clearfix">
            <span>属性</span>
            <el-button size='mini' icon="el-icon-close" @click="closeProperties" style="float: right"></el-button>
          </div>
          <el-form ref="form">
            <el-form-item>
              <el-scrollbar style="height: 450px">
                <json-view :data="nodeData" />
              </el-scrollbar>
            </el-form-item>
            <el-form-item>
            <el-button size='mini' v-if="transButtonShow" @click="showKtr"
                       style="float: right">查看转换</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
    </el-col>
    <el-col :span="24" class="center">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <span>日志</span>
          <el-button style="padding: 3px 0; margin-left: 10px;" type="text" @click="clearLog">清空日志</el-button>
          <el-button style="float: right; padding: 3px 0" type="text" @click="closeSocket">关闭调试</el-button>
        </div>
        <div style="height: 350px;background-color: #292A2B;">
          <el-scrollbar style="height: 300px">
            <json-view :data="websocketData" theme="one-dark" />
          </el-scrollbar>
        </div>
      </el-card>
    </el-col>
    <el-dialog
        title="导入作业"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="closeDialog">
      <el-form v-loading='loading' label-width="100px" style="width: 90%;">
        <el-upload
            style="display: inline-block; margin: 0 10px;"
            ref="upload"
            :action="action"
            :on-change="fileChange"
            :on-remove="fileRemove"
            accept=".ktr,.kjb"
            limit="10"
            multiple
            :auto-upload="false">
          <el-button plain size='medium' icon="el-icon-bottom-left">导入作业</el-button>
        </el-upload>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="closeDialog">取 消</el-button>
          <el-button type="primary" :disabled="uploadFileButton" @click="uploadSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </el-main>

</template>

<script>
import {
  findByTaskIdAndType,
  downloadTaskKettleFiles,
  deleteTaskKettleFiles,
  runTask, stopTask
} from "@/api/task.js";
import go from '@/assets/gojs/go.js'
import X2JS from '@/assets/gojs/x2js.js'
import axios from 'axios'
import jsonView from '@/components/json-view/json-view';

export default {
  components: {jsonView},
  data() {
    return {
      permissions: {
      },
      headersConfig: {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: 'token ' + JSON.parse(sessionStorage.getItem("UserInfo")).token
        }
      },
      action: process.env.VUE_APP_BASE_API  + '/webadmin/task/kettleFiles/uploadFile',
      taskGroupId: '',
      nodeImg: {
        'SPECIAL': require('@/assets/gojs/img/start.png'),
        'SUCCESS': require('@/assets/gojs/img/end.png'),
        'TRANS': require('@/assets/gojs/img/trans.png'),
        'AccessInput': require('@/assets/gojs/img/access.png'),
        'TableInput': require('@/assets/gojs/img/table.png'),
        'CsvInput': require('@/assets/gojs/img/csv.png'),
        'JsonInput': require('@/assets/gojs/img/json.png'),
        'GetTableNames': require('@/assets/gojs/img/getTable.png'),
        'InsertUpdate': require('@/assets/gojs/img/insert.png'),
        'ExcelInput': require('@/assets/gojs/img/excel.png'),
        'EVAL': require('@/assets/gojs/img/eval.png'),
        'SIMPLE_EVAL': require('@/assets/gojs/img/simple_eval.png'),
        'RowsToResult': require('@/assets/gojs/img/rowstoresult.png'),
        'TableOutput': require('@/assets/gojs/img/tableoutput.png'),
        'SynchronizeAfterMerge' :require('@/assets/gojs/img/synchronizeaftermerge.png'),
        'Delete' :require('@/assets/gojs/img/delete.png'),
        'TypeExitExcelWriterStep' :require('@/assets/gojs/img/typeexitexcelwriterstep.png'),
        'UserDefinedJavaClass' :require('@/assets/gojs/img/userdefinedjavaclass.png'),
        'ScriptValueMod' :require('@/assets/gojs/img/scriptvaluemod.png'),
        'ExecSQL' :require('@/assets/gojs/img/execsql.png'),
        'RegexEval' :require('@/assets/gojs/img/regexeval.png')
      },
      linkImg: {
        'unconditional': 'M7,10 L7,7 C7,4.23857625 9.23857625,2 12,2 C14.7614237,2 17,4.23857625 17,7 L17,10 L18,10 C19.0683513,10 20,10.7763739 20,11.8333333 L20,20.1666667 C20,21.2236261 19.0683513,22 18,22 L6,22 C4.93164867,22 4,21.2236261 4,20.1666667 L4,11.8333333 C4,10.7763739 4.93164867,10 6,10 L7,10 Z M9,10 L15,10 L15,7 C15,5.34314575 13.6568542,4 12,4 C10.3431458,4 9,5.34314575 9,7 L9,10 Z M6,12 L6,20 L18,20 L18,12 L6,12 Z M12,17 C11.4477153,17 11,16.5522847 11,16 C11,15.4477153 11.4477153,15 12,15 C12.5522847,15 13,15.4477153 13,16 C13,16.5522847 12.5522847,17 12,17 Z',
        'evaluation': 'M12,24C5.4,24,0,18.6,0,12S5.4,0,12,0s12,5.4,12,12S18.6,24,12,24z M12,2C6.5,2,2,6.5,2,12s4.5,10,10,10s10-4.5,10-10 S17.5,2,12,2z M11,16c-0.3,0-0.5-0.1-0.7-0.3l-3-3c-0.4-0.4-0.4-1,0-1.4s1-0.4,1.4,0l3,3c0.4,0.4,0.4,1,0,1.4C11.5,15.9,11.3,16,11,16z M11,16c-0.3,0-0.5-0.1-0.7-0.3c-0.4-0.4-0.4-1,0-1.4l6-6c0.4-0.4,1-0.4,1.4,0s0.4,1,0,1.4l-6,6C11.5,15.9,11.3,16,11,16z',
        'err': 'M12,23 C5.92486775,23 1,18.0751322 1,12 C1,5.92486775 5.92486775,1 12,1 C18.0751322,1 23,5.92486775 23,12 C23,18.0751322 18.0751322,23 12,23 Z M12,21 C16.9705627,21 21,16.9705627 21,12 C21,7.02943725 16.9705627,3 12,3 C7.02943725,3 3,7.02943725 3,12 C3,16.9705627 7.02943725,21 12,21 Z M12,13.4142136 L8.70710678,16.7071068 L7.29289322,15.2928932 L10.5857864,12 L7.29289322,8.70710678 L8.70710678,7.29289322 L12,10.5857864 L15.2928932,7.29289322 L16.7071068,8.70710678 L13.4142136,12 L16.7071068,15.2928932 L15.2928932,16.7071068 L12,13.4142136 Z'
      },
      transList: [],
      fileList: [],
      dialogVisible: false,
      taskId: '',
      taskData: '',
      xmlPath:'',
      propertiesWidth: 8,
      designerWidth: 16,
      nodeData: '',
      transButtonShow: false,
      ktrName: '',
      container: {},
      websocket: {},
      websocketData:[],
      uploadFileButton:false
    }
  },
  mounted() {
    let mySelf = this;
    const MAKE = go.GraphObject.make;
    mySelf.myDiagram = MAKE(go.Diagram, "chart-diagram", {
      initialPosition: new go.Point(0, 0),//定义左上角为0同步kettle设置
      "undoManager.isEnabled": true,// 支持 Ctrl-Z 和 Ctrl-Y 操作
      "toolManager.hoverDelay": 500,//tooltip提示显示延时
      "toolManager.toolTipDuration": 120000,//tooltip持续显示时间
      "grid.visible": false,//显示网格
      allowVerticalScroll: false,//禁止垂直拖动画布
      allowHorizontalScroll: false//禁止水平拖动画布
    });
  },
  created() {
    this.taskGroupId = this.$route.query.groupId
    this.taskId = this.$route.query.id
    this.container = this.$route.query.container
    this.closeProperties()
    this.fetchData()
  },
  methods: {
    // 获取详情
    async fetchData() {
      const that = this
      try {
        that.isSubmitLoading = true
        const res = await findByTaskIdAndType({taskId: this.$route.query.id,type:'kjb'})
        if (res.data.code === 1) {
          if(res.data.data == null){
            return
          }
          that.transList = res.data.data
          if (res.data.data.xmlUrl == null || res.data.data.xmlUrl === "" || res.data.data.xmlUrl.length === 0) {
            return this.$message.error('xml地址为空，加载失败')
          }
          that.xmlPath = process.env.VUE_APP_BASE_API+res.data.data.xmlUrl

          await this.readXML();
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    readXML() {
      let that = this;
      console.log(that.xmlPath)
      axios.get(that.xmlPath).then(function (response) {
        that.xmlToJson(response.data);
      }).catch(function (error) {
        return that.$message.error('xml加载失败,请检查文件路径')
      })
    },
    xmlToJson(xmlData) {
      if (xmlData.length === 0) {
        return this.$message.error('xml加载失败')
      }
      let x2js = new X2JS();
      this.taskData = x2js.xml2js(xmlData)
      if (this.taskData.hasOwnProperty("job")) {
        let jobData = this.taskData.job;
        this.jobToData("job", jobData.entries, jobData.hops)
      } else if (this.taskData.hasOwnProperty("transformation")) {
        let transformationData = this.taskData.transformation;
        this.transformationToData("transformation", transformationData.step, transformationData.order)
      } else {
        return;
      }
    },
    jobToData(type, entries, hops) {
      let that = this;
      let nodesData = [];
      let linksData = [];
      //数据模板 {category:"node",key:"开始", loc: "352 304", img: require("../assets/img/start.png") }
      for (let key in entries.entry) {
        let nodeData = {};
        let keyData = entries.entry[key];
        let nodeType = keyData.type;
        let loc = keyData.xloc + " " + keyData.yloc;
        let img = that.findNodeImgValue(nodeType);
        if (nodeType === "TRANS") {
          for (let i in that.transList) {
            let filePath = keyData.filename
            let filename
            if (filePath.indexOf('/') > 1) {
              filename = keyData.filename.substring(keyData.filename.lastIndexOf("/") + 1)
              filename = filename.substring(0, filename.lastIndexOf("."))
            } else if (filePath.indexOf('\\') > 1) {
              filename = keyData.filename.substring(keyData.filename.lastIndexOf("\\") + 1)
              filename = filename.substring(0, filename.lastIndexOf("."))
            }
            if (that.transList[i].name === filename) {
              nodeData["path"] = that.transList[i].xmlUrl
            }
          }
        }
        nodeData["category"] = "node"
        nodeData["key"] = keyData.name
        nodeData["loc"] = loc
        nodeData["img"] = img
        nodesData.push(nodeData);
      }
      //数据模板 {category:"link",from:"Start",to:"转换",color: "orange",geometry:go.Geometry.parse("M7,10")}
      for (var key in hops.hop) {
        let linkData = {};
        let keyData = hops.hop[key];
        //let img = go.Geometry.parse("M7,10 L7,7 C7,4.23857625 9.23857625,2 12,2 C14.7614237,2 17,4.23857625 17,7 L17,10 L18,10 C19.0683513,10 20,10.7763739 20,11.8333333 L20,20.1666667 C20,21.2236261 19.0683513,22 18,22 L6,22 C4.93164867,22 4,21.2236261 4,20.1666667 L4,11.8333333 C4,10.7763739 4.93164867,10 6,10 L7,10 Z M9,10 L15,10 L15,7 C15,5.34314575 13.6568542,4 12,4 C10.3431458,4 9,5.34314575 9,7 L9,10 Z M6,12 L6,20 L18,20 L18,12 L6,12 Z M12,17 C11.4477153,17 11,16.5522847 11,16 C11,15.4477153 11.4477153,15 12,15 C12.5522847,15 13,15.4477153 13,16 C13,16.5522847 12.5522847,17 12,17 Z");
        linkData["category"] = "link"
        linkData["from"] = keyData.from
        linkData["to"] = keyData.to
        if (keyData.unconditional === "Y") {
          linkData["geometry"] = go.Geometry.parse(that.findLinkImgValue("unconditional"))
          linkData["color"] = "orange"
        }
        if (keyData.unconditional === "N" && keyData.evaluation === "Y") {
          linkData["geometry"] = go.Geometry.parse(that.findLinkImgValue("evaluation"))
          linkData["color"] = "green"
        }
        if (keyData.unconditional === "N" && keyData.evaluation === "N") {
          linkData["geometry"] = go.Geometry.parse(that.findLinkImgValue("err"))
          linkData["color"] = "red"
        }
        linksData.push(linkData);
      }
      that.addNodeAndLink(type, nodesData, linksData)
    },
    transformationToData(type, step, order) {
      let that = this;
      let nodesData = [];
      let linksData = [];
      //数据模板 {category:"node",key:"开始", loc: "352 304", img: require("../assets/img/start.png") }
      for (var key in step) {
        let nodeData = {};
        let keyData = step[key];
        let loc = keyData.GUI.xloc + " " + keyData.GUI.yloc;
        let img = that.findNodeImgValue(keyData.type);
        nodeData["category"] = "node"
        nodeData["key"] = keyData.name
        nodeData["loc"] = loc
        nodeData["img"] = img
        nodesData.push(nodeData);
      }
      //数据模板 {category:"link",from:"Start",to:"转换",color: "orange",geometry:go.Geometry.parse("M7,10")}
      let length = Array.from(order.hop).length;
      if (length === 0) {
        let linkData = {};
        let keyData = order.hop;
        linkData["category"] = "link"
        linkData["from"] = keyData.from
        linkData["to"] = keyData.to
        linksData.push(linkData);
      } else {
        for (let key in order.hop) {
          let linkData = {};
          let keyData = order.hop[key];
          linkData["category"] = "link"
          linkData["from"] = keyData.from
          linkData["to"] = keyData.to
          linksData.push(linkData);
        }
      }
      that.addNodeAndLink(type, nodesData, linksData)
    },
    addNodeAndLink(type, nodesData, linksData) {
      let mySelf = this;
      const MAKE = go.GraphObject.make;
      // 定义节点模板
      mySelf.myDiagram.nodeTemplateMap.add("node", MAKE(go.Node, "Auto", {click: mySelf.nodeClick},{locationSpot: go.Spot.Center},
          new go.Binding("location", "loc", go.Point.parse),
          MAKE(go.Panel, "Vertical",
              MAKE(go.Picture,
                  {margin: 0, width: 34, height: 34},
                  new go.Binding("source", "img")
              ),
              MAKE(go.TextBlock, "Default Text", {margin: 4, stroke: "black", font: "12px sans-serif",},
                  new go.Binding("text", "key")
              ),
              {
                doubleClick: function (e, node) {
                  // 双击事件
                  console.log(node.part.data)
                  if (node.part.data.path != null) {
                    window.location.href = "/#/taskManage/trans/transDetail?xml=" + node.part.data.path;
                  }
                },
                cursor: "pointer"//改变鼠标样式变成小手
              }
          )
      ));

      if (type === "job") {
        // 定义连线模板
        mySelf.myDiagram.linkTemplateMap.add("link", MAKE(go.Link,
            {routing: go.Link.None, corner: 0},
            MAKE(go.Shape, {strokeWidth: 0.6, stroke: "#000", fill: "#000"}),
            MAKE(go.Shape, {toArrow: "Boomerang", fill: "#000", stroke: null}),//箭头
            MAKE(go.Shape, {fill: null, strokeWidth: 1, width: 14, height: 14,},
                new go.Binding("geometry", "geometry"),
                new go.Binding("stroke", "color")
            )
        ));
      } else {
        // 定义连线模板
        mySelf.myDiagram.linkTemplateMap.add("link", MAKE(go.Link,
            {routing: go.Link.None, corner: 0},
            MAKE(go.Shape, {strokeWidth: 0.6, stroke: "#000"}),
            MAKE(go.Shape, {toArrow: "Boomerang", fill: "#000", stroke: null})//箭头
        ));
      }

      //设置连线模式
      let myModel = go.GraphObject.make(go.GraphLinksModel);
      //设置数据
      for (let nodeData in nodesData) {
        myModel.addNodeData(nodesData[nodeData]);
      }
      for (let linkData in linksData) {
        myModel.addLinkData(linksData[linkData]);
      }
      // 设置创建的模式
      mySelf.myDiagram.model = myModel;
      // mySelf.myDiagram.addDiagramListener("ObjectDoubleClicked",function (e) {
      //   const that = this
      //   that.toDetail(e.subject.part.key)
      // });
    },
    findNodeImgValue(key) {
      let that = this;
      for (let k in that.nodeImg) {
        if (k === key) {
          return that.nodeImg[k];
        }
      }
    },
    findLinkImgValue(key) {
      let that = this;
      for (let k in that.linkImg) {
        if (k === key) {
          return that.linkImg[k];
        }
      }
    },
    toBack() {
      this.$router.push({
        path: '/task',
        query: {
          groupId:this.taskGroupId
        }
      })
    },
    async downloadFiles() {
      const that = this
      that.isSubmitLoading = true
      const response = await downloadTaskKettleFiles(that.taskId)
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        let url = process.env.VUE_APP_BASE_API+response.data.data
        that.$message.success("文件下载成功")
        axios({method:'get', url:url, responseType:'blob',
        }).then((data) => {
          if (!data) {
            return
          }
          let url = window.URL.createObjectURL(data.data)
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          document.body.appendChild(link)
          link.click()
        })
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async deleteFiles() {
      const that = this
      that.isSubmitLoading = true
      const response = await deleteTaskKettleFiles(that.taskId)
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        that.$message.success("删除成功")
        this.myDiagram.model=go.Model.fromJson({})
        this.fetchData()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    showDialog () {
      this.fileList = []
      this.dialogVisible = true
    },
    closeDialog () {
      this.fileList = []
      this.dialogVisible = false
      this.$refs.upload.clearFiles();
    },
    fileChange(file, fileList) {
      this.fileList.push(file.raw)
    },
    fileRemove(file, fileList) {
      this.fileList.some((item, i) =>{
        if(item.name == file.name){
          this.fileList.splice(i, 1)
          return true;
        }
      })
    },
    uploadSubmit(){
      console.log(this.fileList)
      this.uploadFileButton = true;
      if(this.fileList.length === 0){
        this.$message.error("请选择任务文件")
        this.uploadFileButton = false;
        return
      }
      let formData = new FormData();
      this.fileList.map(element => {
        formData.append("file", element);
      });
      formData.append('taskId', this.taskId)
      axios.post(this.action, formData, this.headersConfig).then(res=>{
        this.uploadFileButton = false;
        if(res.data.code===1){
          this.closeDialog ()
          this.$message.success("上传成功")
          this.fetchData()
        }else{
          this.$message.error(res.data.msg)
        }
      })
      .catch(error => {
        this.$message.error("上传异常")
        this.uploadFileButton = false;
      })
    },
    nodeClick: function (e, node) {
      console.log(node.key)
      this.showProperties()
      let entries = this.taskData.job.entries.entry
      console.log(entries)
      for (var i = 0; i < entries.length; i++) {
        let item = entries[i];
        if(item.name === node.key){
          if(item.type === "TRANS"){
            this.transButtonShow = true
            let fileName = item.filename
            this.ktrName = fileName.slice(fileName.indexOf('/') + 1)
            console.log(this.ktrName)
          }else{
            this.transButtonShow = false
          }
          this.nodeData=item;
        }
      }
    },
    showProperties(){
      this.propertiesWidth = 8
      this.designerWidth = 16
    },
    closeProperties(){
      this.propertiesWidth = 0
      this.designerWidth = 24
    },
    showKtr(){
      const that = this
      that.$router.push({
        path: '/taskShowKtr',
        query: {
          id: that.taskId,
          taskName: that.ktrName,
          taskGroupId: that.taskGroupId
        }
      })
    },
    clearLog(){
      this.websocketData = []
    },
    async closeSocket(){
      const that = this
      that.isSubmitLoading = true
      const response = await stopTask({id:this.taskId})
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        that.$message.success('调试已关闭')
      } else {
        that.$message.error(response.data.msg)
      }
      this.websocketClose();
    },
    async debugDialog(){
      console.log(this.container)
      this.initWebSocket(this.container.ip + ":" + this.container.port + "/task/container/" + this.taskId)
      // this.initWebSocket("127.0.0.1:9094/task/container/" + this.taskId)
      this.isSubmitLoading = true
      const response = await runTask({id:this.taskId})
      this.isSubmitLoading = false
      if (response.data.code === 1) {
        this.$message.success('调试中')
      } else {
        this.$message.error(response.data.msg)
      }
    },
    //初始化websocket
    initWebSocket(url) {
      this.websocket = new WebSocket("ws://" + url);
      this.websocket.onmessage = this.websocketOnMessage;
      this.websocket.onerror = this.websocketOnError;
      this.websocket.onclose = this.websocketClose;
    },

    //连接建立之后执行send方法发送数据
    websocketOnError(e) {//连接建立失败重连
      console.log('建立连接失败', e)
      this.websocketClose();
    },
    websocketOnMessage(e) { //数据接收
      const data = JSON.parse(e.data);
      console.log(data)
      this.websocket = Object.assign({},this.websocket,data);
      this.websocketData = this.websocketData.concat(this.websocket)
      console.log("websocket接受到的数据")
    },
    websocketClose(e) {  //关闭
      this.websocket.close();
      console.log('断开连接', e);
    },
  }
}
</script>
<style lang="scss" scoped>

</style>
