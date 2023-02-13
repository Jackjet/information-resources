<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item style='margin-left: 15px;'>
          <el-button size='medium' @click="toBack" style="color: #5677DF" icon="el-icon-search">返回</el-button>
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
          </el-form>
        </el-card>
      </el-col>
    </el-col>
    <el-col :span="24" class="center">

    </el-col>
  </el-main>

</template>

<script>
// import {findByTaskIdAndName} from "@/api/task.js";
// import go from 'static/gojs/go.js'
import X2JS from 'static/gojs/x2js.js'
import axios from 'axios'
import jsonView from '@/components/json-view/json-view';

export default {
  components: {jsonView},
  data() {
    return {
      taskGroupId: '',
      nodeImg: {
        'SPECIAL': require('static/gojs/img/start.png'),
        'SUCCESS': require('static/gojs/img/end.png'),
        'TRANS': require('static/gojs/img/trans.png'),
        'AccessInput': require('static/gojs/img/access.png'),
        'TableInput': require('static/gojs/img/table.png'),
        'CsvInput': require('static/gojs/img/csv.png'),
        'JsonInput': require('static/gojs/img/json.png'),
        'GetTableNames': require('static/gojs/img/getTable.png'),
        'InsertUpdate': require('static/gojs/img/insert.png'),
        'ExcelInput': require('static/gojs/img/excel.png'),
        'EVAL': require('static/gojs/img/eval.png'),
        'SIMPLE_EVAL': require('static/gojs/img/simple_eval.png'),
        'RowsToResult': require('static/gojs/img/rowstoresult.png'),
        'TableOutput': require('static/gojs/img/tableoutput.png'),
        'SynchronizeAfterMerge' :require('static/gojs/img/synchronizeaftermerge.png'),
        'Delete' :require('static/gojs/img/delete.png'),
        'TypeExitExcelWriterStep' :require('static/gojs/img/typeexitexcelwriterstep.png'),
        'UserDefinedJavaClass' :require('static/gojs/img/userdefinedjavaclass.png'),
        'ScriptValueMod' :require('static/gojs/img/scriptvaluemod.png'),
        'ExecSQL' :require('static/gojs/img/execsql.png'),
        'RegexEval' :require('static/gojs/img/regexeval.png')
      },
      linkImg: {
        'unconditional': 'M7,10 L7,7 C7,4.23857625 9.23857625,2 12,2 C14.7614237,2 17,4.23857625 17,7 L17,10 L18,10 C19.0683513,10 20,10.7763739 20,11.8333333 L20,20.1666667 C20,21.2236261 19.0683513,22 18,22 L6,22 C4.93164867,22 4,21.2236261 4,20.1666667 L4,11.8333333 C4,10.7763739 4.93164867,10 6,10 L7,10 Z M9,10 L15,10 L15,7 C15,5.34314575 13.6568542,4 12,4 C10.3431458,4 9,5.34314575 9,7 L9,10 Z M6,12 L6,20 L18,20 L18,12 L6,12 Z M12,17 C11.4477153,17 11,16.5522847 11,16 C11,15.4477153 11.4477153,15 12,15 C12.5522847,15 13,15.4477153 13,16 C13,16.5522847 12.5522847,17 12,17 Z',
        'evaluation': 'M12,24C5.4,24,0,18.6,0,12S5.4,0,12,0s12,5.4,12,12S18.6,24,12,24z M12,2C6.5,2,2,6.5,2,12s4.5,10,10,10s10-4.5,10-10 S17.5,2,12,2z M11,16c-0.3,0-0.5-0.1-0.7-0.3l-3-3c-0.4-0.4-0.4-1,0-1.4s1-0.4,1.4,0l3,3c0.4,0.4,0.4,1,0,1.4C11.5,15.9,11.3,16,11,16z M11,16c-0.3,0-0.5-0.1-0.7-0.3c-0.4-0.4-0.4-1,0-1.4l6-6c0.4-0.4,1-0.4,1.4,0s0.4,1,0,1.4l-6,6C11.5,15.9,11.3,16,11,16z',
        'err': 'M12,23 C5.92486775,23 1,18.0751322 1,12 C1,5.92486775 5.92486775,1 12,1 C18.0751322,1 23,5.92486775 23,12 C23,18.0751322 18.0751322,23 12,23 Z M12,21 C16.9705627,21 21,16.9705627 21,12 C21,7.02943725 16.9705627,3 12,3 C7.02943725,3 3,7.02943725 3,12 C3,16.9705627 7.02943725,21 12,21 Z M12,13.4142136 L8.70710678,16.7071068 L7.29289322,15.2928932 L10.5857864,12 L7.29289322,8.70710678 L8.70710678,7.29289322 L12,10.5857864 L15.2928932,7.29289322 L16.7071068,8.70710678 L13.4142136,12 L16.7071068,15.2928932 L15.2928932,16.7071068 L12,13.4142136 Z'
      },
      transList: [],
      fileList: [],
      taskId: '',
      taskData: '',
      xmlPath:'',
      dialogVisible: false,
      propertiesWidth: 8,
      designerWidth: 16,
      nodeData: '',
      containerId: ''
    }
  },
  mounted() {
    let mySelf = this;
    const MAKE = window.go.GraphObject.make;
    mySelf.myDiagram = MAKE(window.go.Diagram, "chart-diagram", {
      initialPosition: new window.go.Point(0, 0),//定义左上角为0同步kettle设置
      "undoManager.isEnabled": true,// 支持 Ctrl-Z 和 Ctrl-Y 操作
      "toolManager.hoverDelay": 500,//tooltip提示显示延时
      "toolManager.toolTipDuration": 120000,//tooltip持续显示时间
      "grid.visible": false,//显示网格
      allowVerticalScroll: false,//禁止垂直拖动画布
      allowHorizontalScroll: false//禁止水平拖动画布
    });
  },
  created() {
    this.taskGroupId = this.$route.query.taskGroupId
    this.taskId = this.$route.query.id
    this.taskName = this.$route.query.taskName
    this.containerId = this.$route.query.container
    this.closeProperties()
    this.fetchData()
  },
  methods: {
    // 获取详情
    async fetchData() {
      const that = this
      try {
        that.isSubmitLoading = true
        let url = that.Interface.kettleFiles.findByName
        let res = await that.request.dataGet(that, url, {taskId: this.taskId,name:this.taskName})
        // const res = await findByTaskIdAndName({taskId: this.taskId,name:this.taskName})
        if (res.data.code === 1) {
          that.transList = res.data.data
          console.log(that.transList)
          if(that.transList  == null){
            return this.$message.error('未查询到转换文件')
          }
          if (res.data.data.xmlUrl == null || res.data.data.xmlUrl === "" || res.data.data.xmlUrl.length === 0) {
            return this.$message.error('xml地址为空，加载失败')
          }
          that.xmlPath = this.PortUrl + res.data.data.xmlUrl
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
      axios.get(that.xmlPath).then(function (response) {
        that.xmlToJson(response.data);
      }).catch(function (error) {
        return that.$message.error('xml加载失败,请检查文件路径')
      })
    },
    xmlToJson(xmlData) {
      let that = this;
      if (xmlData.length === 0) {
        return this.$message.error('xml加载失败')
      }
      let x2js = new window.X2JS();
      that.taskData = x2js.xml2js(xmlData)
      if (that.taskData.hasOwnProperty("job")) {
        let jobData = that.taskData.job;
        that.jobToData("job", jobData.entries, jobData.hops)
      } else if (that.taskData.hasOwnProperty("transformation")) {
        let transformationData = that.taskData.transformation;
        that.transformationToData("transformation", transformationData.step, transformationData.order)
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
      //数据模板 {category:"link",from:"Start",to:"转换",color: "orange",geometry:window.go.Geometry.parse("M7,10")}
      for (var key in hops.hop) {
        let linkData = {};
        let keyData = hops.hop[key];
        //let img = window.go.Geometry.parse("M7,10 L7,7 C7,4.23857625 9.23857625,2 12,2 C14.7614237,2 17,4.23857625 17,7 L17,10 L18,10 C19.0683513,10 20,10.7763739 20,11.8333333 L20,20.1666667 C20,21.2236261 19.0683513,22 18,22 L6,22 C4.93164867,22 4,21.2236261 4,20.1666667 L4,11.8333333 C4,10.7763739 4.93164867,10 6,10 L7,10 Z M9,10 L15,10 L15,7 C15,5.34314575 13.6568542,4 12,4 C10.3431458,4 9,5.34314575 9,7 L9,10 Z M6,12 L6,20 L18,20 L18,12 L6,12 Z M12,17 C11.4477153,17 11,16.5522847 11,16 C11,15.4477153 11.4477153,15 12,15 C12.5522847,15 13,15.4477153 13,16 C13,16.5522847 12.5522847,17 12,17 Z");
        linkData["category"] = "link"
        linkData["from"] = keyData.from
        linkData["to"] = keyData.to
        if (keyData.unconditional === "Y") {
          linkData["geometry"] = window.go.Geometry.parse(that.findLinkImgValue("unconditional"))
          linkData["color"] = "orange"
        }
        if (keyData.unconditional === "N" && keyData.evaluation === "Y") {
          linkData["geometry"] = window.go.Geometry.parse(that.findLinkImgValue("evaluation"))
          linkData["color"] = "green"
        }
        if (keyData.unconditional === "N" && keyData.evaluation === "N") {
          linkData["geometry"] = window.go.Geometry.parse(that.findLinkImgValue("err"))
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
      //数据模板 {category:"link",from:"Start",to:"转换",color: "orange",geometry:window.go.Geometry.parse("M7,10")}
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
      const MAKE = window.go.GraphObject.make;
      // 定义节点模板
      mySelf.myDiagram.nodeTemplateMap.add("node", MAKE(window.go.Node, "Auto", {click: mySelf.nodeClick}, {locationSpot: window.go.Spot.Center},
          new window.go.Binding("location", "loc", window.go.Point.parse),
          MAKE(window.go.Panel, "Vertical",
              MAKE(window.go.Picture,
                  {margin: 0, width: 34, height: 34},
                  new window.go.Binding("source", "img")
              ),
              MAKE(window.go.TextBlock, "Default Text", {margin: 4, stroke: "black", font: "12px sans-serif",},
                  new window.go.Binding("text", "key")
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
        mySelf.myDiagram.linkTemplateMap.add("link", MAKE(window.go.Link,
            {routing: window.go.Link.None, corner: 0},
            MAKE(window.go.Shape, {strokeWidth: 0.6, stroke: "#000", fill: "#000"}),
            MAKE(window.go.Shape, {toArrow: "Boomerang", fill: "#000", stroke: null}),//箭头
            MAKE(window.go.Shape, {fill: null, strokeWidth: 1, width: 14, height: 14,},
                new window.go.Binding("geometry", "geometry"),
                new window.go.Binding("stroke", "color")
            )
        ));
      } else {
        // 定义连线模板
        mySelf.myDiagram.linkTemplateMap.add("link", MAKE(window.go.Link,
            {routing: window.go.Link.None, corner: 0},
            MAKE(window.go.Shape, {strokeWidth: 0.6, stroke: "#000"}),
            MAKE(window.go.Shape, {toArrow: "Boomerang", fill: "#000", stroke: null})//箭头
        ));
      }

      //设置连线模式
      let myModel = window.go.GraphObject.make(window.go.GraphLinksModel);
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
        path: '/index/taskDesign',
        query: {
          id: this.taskId,
          groupId: this.taskGroupId,
          container: this.containerId
        }
      })
    },
    nodeClick: function (e, node) {
      this.showProperties()
      let entries = this.taskData.transformation.step
      for (var i = 0; i < entries.length; i++) {
        let item = entries[i];
        if(item.name === node.key){
          console.log(item)
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
    }
  }
}
</script>
<style lang="scss" scoped>

</style>
