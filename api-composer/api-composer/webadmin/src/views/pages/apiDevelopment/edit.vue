<template>
  <el-main class="main">
    <el-row style="border-radius: 5px;padding: 0 5px;background-color: #fff;">
      <el-col :span="4">
        <el-card style="width: 100%;" class="box-card myPaletteDiv">
          <div slot="header" class="clearfix" style>
            <span>连接节点</span>
          </div>
          <div id="myPaletteDiv" style="height: 720px;"></div>
        </el-card>
      </el-col>
      <el-col :span="20">
        <el-row>
          <el-col :span="designerWidth">
            <el-card style="width: 100%" class="box-card">
              <div slot="header" class="clearfix">
                <span>设计器</span>
                <el-button
                  size="medium"
                  style="float: right;"
                  @click="testFiled = true"
                >调试</el-button>
                <el-button
                  size="medium"
                  style="float: right;"
                  @click="openDeploy()"
                >运行</el-button>
              </div>
              <div
                id="myDiagramDiv"
                style="border: 1px solid #EBEEF5;height: 500px; position: relative; cursor: auto;"
              ></div>
            </el-card>
          </el-col>
          <el-col :span="propertiesWidth">
            <el-card style="width: 100%;height: 600px" class="box-card">
              <div slot="header" class="clearfix">
                <span>属性</span>
                <el-button
                  size="mini"
                  icon="el-icon-close"
                  @click="closeProperties"
                  style="float: right"
                ></el-button>
              </div>
              <el-scrollbar style="height:500px">
                <el-form
                  ref="form"
                  v-show="databaseFormShow"
                  :model="databaseForm"
                  label-width="120px"
                >
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('database')"
                      style="width: 60%"
                      v-model="databaseForm.text"
                    ></el-input>
                  </el-form-item>
                  <!-- <el-form-item label="数据源">
                    <el-cascader
                      @change="tableBaseChoose"
                      v-model="ruleForm.dataBase"
                      :options="options"
                      clearable
                    ></el-cascader>
                  </el-form-item> -->
                  <el-form-item label="数据源">
                  <el-select @change="tableBaseChoose" value-key="id" v-model="ruleForm.dataBase"
                             placeholder="请选择"
                             clearable
                             style="width: 60%">
                    <el-option
                        v-for="item in sourceApiHost"
                        :key="item.id"
                        :label="item.name"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
                  <el-form-item label="SQL语句">
                    <el-col :span="15">
                      <el-input
                      style="width:97%;"
                      @change="saveData('database')"
                      type="textarea"
                      :autosize="{ minRows: 6, maxRows: 10}"
                      v-model="databaseForm.sql"
                      placeholder="select * from d1_role"
                    ></el-input>
                    </el-col>
                    <el-col :span="6">
                     <el-button style="margin-left:15px" type="primary" title="自动生成SQL语句" :disabled="btnDisabled" @click="toSql">SQl生成</el-button>
                    </el-col>
                  </el-form-item>
                  <el-dialog title="表单填写" :visible.sync="dialogVisible" width="50%">
                    <el-form :model="ruleForm" ref='ruleForm' :rules='rules1' label-width='120px'>
                      <el-form-item label="选择实体表" prop="dataTable">
                        <el-cascader
                          @change="tableBaseChange1"
                          v-model="ruleForm.dataTable"
                          :options="options1"
                          clearable
                        ></el-cascader>
                      </el-form-item>
                      <el-form-item label="选择输出字段">
                        <el-cascader
                          v-model="ruleForm.metadataData2"
                          :disabled="tableDisabled"
                          :options="options2"
                          :props="props1"
                        ></el-cascader>
                      </el-form-item>
                      <el-form-item label="选择输入字段">
                        <el-cascader
                          placeholder="添加字段"
                          :disabled="tableDisabled"
                          @change="handleItemChange1"
                          v-model="ruleForm.metadataData1"
                          :props="props1"
                          :options="options2"
                        ></el-cascader>
                      </el-form-item>
                      <el-table v-if="tableData.length > 0" :data="tableData" style="width: 100%">
                        <el-table-column type="index"></el-table-column>
                        <el-table-column prop="source" label="源字段"></el-table-column>
                        <el-table-column prop="sourceType" label="字段类型"></el-table-column>
                        <el-table-column label="映射参数">
                          <template slot-scope="scope">
                            <el-select
                              v-model="scope.row.target"
                              placeholder="请选择"
                              @change="selectChange(scope.$index,scope.row.target)"
                            >
                              <el-option
                                v-for="item in selectOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value"
                              ></el-option>
                            </el-select>
                          </template>
                        </el-table-column>
                        <el-table-column prop="targetType" label="映射参数类型"></el-table-column>
                      </el-table>
                    </el-form>
                    <span slot="footer" class="dialog-footer">
                      <el-button @click="dialogVisible = false">取 消</el-button>
                      <el-button type="primary" @click="handleToSql">确 定</el-button>
                    </span>
                  </el-dialog>
                </el-form>
                <el-form
                  ref="form"
                  v-show="conditionFormShow"
                  :model="conditionForm"
                  label-width="90px"
                >
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('condition')"
                      style="width: 260px"
                      v-model="conditionForm.text"
                    ></el-input>
                  </el-form-item>
                  <div
                    style="border: 1px solid #EBEEF5;padding-top: 5px;width: 380px;margin-bottom: 10px;"
                    v-for="(item, index) in conditions"
                    :key="item.key"
                  >
                    <el-form-item :label="'条件'+index" :prop="'conditions.' + index +'.condition'">
                      <el-input
                        @change="saveData('condition')"
                        v-model="item.condition"
                        placeholder="如：${request.params.age}>10"
                        style="width: 260px"
                      ></el-input>
                      <el-button @click="addConditions()">添加</el-button>
                    </el-form-item>
                    <el-form-item :label="'节点'+index" :prop="'conditions.'+ index +'.node'">
                      <el-select
                        @change="saveData('condition')"
                        v-model="item.node"
                        placeholder="请选择"
                        style="width: 260px"
                      >
                        <el-option
                          v-for="node in nodeList"
                          :key="node.key"
                          :label="node.text"
                          :value="node.key"
                        ></el-option>
                      </el-select>
                      <el-button @click="deleteConditions(item, index)">删除</el-button>
                    </el-form-item>
                  </div>
                </el-form>
                <el-form
                  ref="form"
                  :rules="rules"
                  v-show="httpFormShow"
                  :model="httpForm"
                  label-width="90px"
                >
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('http')"
                      style="width: 290px"
                      v-model="httpForm.text"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="请求地址" prop="url">
                    <el-input
                      @change="saveData('http')"
                      style="width: 290px"
                      v-model="httpForm.url"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="请求方式">
                    <el-radio-group @change="saveData('http')" v-model="httpForm.method">
                      <el-radio :label="1">GET</el-radio>
                      <el-radio :label="2">POST</el-radio>
                      <el-radio :label="3">PUT</el-radio>
                      <el-radio :label="4">DELETE</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="请求头信息">
                    <div
                      style="border: 1px solid #EBEEF5;padding: 5px;width: 290px;"
                      v-for="(item, index) in head"
                      :key="item.key"
                    >
                      <el-form-item :prop="'head.' + index +'.name'">
                        <el-input
                          size="mini"
                          @change="saveData('http')"
                          v-model="item.name"
                          style="width: 60px;margin-right: 5px"
                        ></el-input>
                        <span style="margin-right: 5px">=</span>
                        <el-input
                          size="mini"
                          @change="saveData('http')"
                          v-model="item.value"
                          style="width: 160px;margin-right: 5px"
                        ></el-input>
                        <el-button size="mini" v-if="index ===0" @click="addHeads()">+</el-button>
                        <el-button size="mini" v-else @click="deleteHeads(item, index)">-</el-button>
                      </el-form-item>
                    </div>
                  </el-form-item>
                  <el-form-item label="请求参数">
                    <div
                      style="border: 1px solid #EBEEF5;padding: 5px;width: 290px;"
                      v-for="(item, index) in param"
                      :key="item.key"
                    >
                      <el-form-item :prop="'param.' + index +'.name'">
                        <el-input
                          size="mini"
                          @change="saveData('http')"
                          v-model="item.name"
                          style="width: 60px;margin-right: 5px"
                        ></el-input>
                        <span style="margin-right: 5px">=</span>
                        <el-input
                          size="mini"
                          @change="saveData('http')"
                          v-model="item.value"
                          style="width: 160px;margin-right: 5px"
                        ></el-input>
                        <el-button size="mini" v-if="index ===0" @click="addParams()">+</el-button>
                        <el-button size="mini" v-else @click="deleteParams(item, index)">-</el-button>
                      </el-form-item>
                    </div>
                  </el-form-item>
                  <el-form-item label="内容类型">
                    <el-radio-group @change="saveData('http')" v-model="httpForm.bodyType">
                      <el-radio :label="1">JSON</el-radio>
                      <el-radio :label="2">form-data</el-radio>
                      <el-radio :label="3">XML</el-radio>
                    </el-radio-group>
                  </el-form-item>
                  <el-form-item label="BODY">
                    <el-input
                      @change="saveData('http')"
                      type="textarea"
                      :autosize="{ minRows: 6, maxRows: 10}"
                      style="width: 290px"
                      v-model="httpForm.body"
                    ></el-input>
                  </el-form-item>
                </el-form>
                <el-form
                  ref="form"
                  v-show="functionFormShow"
                  :model="functionForm"
                  label-width="90px"
                >
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('function')"
                      style="width: 260px"
                      v-model="functionForm.text"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="返回对象">
                    <el-input
                      @change="saveData('function')"
                      type="textarea"
                      :autosize="{ minRows: 6, maxRows: 10}"
                      style="width: 260px"
                      v-model="functionForm.connectObject"
                      placeholder="//示例代码
                            function run(input){
                            var aa = {}
                            aa.age = ${request.body.age}+10
                            aa.name = 'xxx'
                            return aa;}"
                    ></el-input>
                  </el-form-item>
                </el-form>
                <el-form ref="form" v-show="outFormShow" :model="outForm" label-width="90px">
                  <el-form-item label="节点名称">
                    <el-input @change="saveData('out')" style="width: 260px" v-model="outForm.text"></el-input>
                  </el-form-item>
                  <el-form-item label="名称">
                    <el-input
                      @change="saveData('out')"
                      style="width: 260px"
                      v-model="outForm.name"
                      placeholder="输出的excel名称"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="数据路径">
                    <el-input
                      @change="saveData('out')"
                      type="textarea"
                      :autosize="{ minRows: 4, maxRows: 10}"
                      style="width: 260px"
                      v-model="outForm.path"
                      placeholder="支持jsonPath表达式,数据指向的路径必须是数组,如下：
${input.b.aa}"
                    ></el-input>
                  </el-form-item>
                </el-form>
                <el-form ref="form" v-show="alismsFormShow" :model="alismsForm" label-width="90px">
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('alisms')"
                      style="width: 260px"
                      v-model="alismsForm.text"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="AK_ID">
                    <el-input
                      @change="saveData('alisms')"
                      style="width: 260px"
                      v-model="alismsForm.akId"
                      placeholder="可在短信控制台中获取"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="AK_Secret">
                    <el-input
                      @change="saveData('alisms')"
                      style="width: 260px"
                      v-model="alismsForm.akSecret"
                      placeholder="可在短信控制台中获取"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="短信签名">
                    <el-input
                      @change="saveData('alisms')"
                      style="width: 260px"
                      v-model="alismsForm.signName"
                      placeholder="可在短信控制台中获取"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="短信模板ID">
                    <el-input
                      @change="saveData('alisms')"
                      style="width: 260px"
                      v-model="alismsForm.templateId"
                      placeholder="可在短信控制台中获取"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="手机号">
                    <el-input
                      @change="saveData('alisms')"
                      style="width: 260px"
                      v-model="alismsForm.mobiles"
                      placeholder="发送手机号，多个用“,”隔开"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="模板内容">
                    <el-input
                      @change="saveData('alisms')"
                      type="textarea"
                      :autosize="{ minRows: 6, maxRows: 10}"
                      style="width: 260px"
                      v-model="alismsForm.content"
                      placeholder="模板中的变量替换JSON串,如模板内容为:亲爱的${name},您的验证码为${code}时,
                            此处的值为:{"name":"Tom", "code":"123"}"
                    ></el-input>
                  </el-form-item>
                </el-form>
                <el-form ref="form" v-show="emailFormShow" :model="emailForm" label-width="90px">
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.text"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="smtp服务器地址">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.smtpUrl"
                      placeholder="请输入smtp服务器地址"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="smtp服务器地址端口号">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.smtpPort"
                      placeholder="请输入smtp服务器地址端口号"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="发件人地址">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.sender"
                      placeholder="邮箱地址"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="收件人地址">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.recipients"
                      placeholder="邮箱地址，多个用“,”隔开"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱账号">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.email"
                      placeholder="邮箱账号"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="邮箱授权码">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.authCode"
                      placeholder="邮箱授权码"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="邮件主题">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.subject"
                      placeholder="邮件主题"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="输入正文">
                    <el-input
                      @change="saveData('email')"
                      style="width: 260px"
                      v-model="emailForm.content"
                      placeholder="输入正文"
                    ></el-input>
                  </el-form-item>
                </el-form>
                <el-form
                  ref="form"
                  v-show="webserviceFormShow"
                  :model="webserviceForm"
                  label-width="90px"
                >
                  <el-form-item label="节点名称">
                    <el-input
                      @change="saveData('webservice')"
                      style="width: 260px"
                      v-model="webserviceForm.text"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="请求地址">
                    <el-input
                      @change="saveData('webservice')"
                      style="width: 260px"
                      v-model="webserviceForm.url"
                      placeholder="wsdl请求地址，如：http://127.0.0.1:8888/ws?wsdl"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="方法名称">
                    <el-input
                      @change="saveData('webservice')"
                      style="width: 260px"
                      v-model="webserviceForm.functionName"
                      placeholder="远程调用的方法名"
                    ></el-input>
                  </el-form-item>
                  <el-form-item label="方法参数">
                    <el-input
                      @change="saveData('webservice')"
                      style="width: 260px"
                      v-model="webserviceForm.functionParam"
                      placeholder="远程调用的方法参数，数组类型"
                    ></el-input>
                  </el-form-item>
                </el-form>
              </el-scrollbar>
            </el-card>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-tabs
              v-model="activeName"
              type="border-card"
              @tab-click="handleClick"
              v-show="showTabs"
            >
              <el-tab-pane name="result" label="运行结果">
                <div name="testResult" style="height: 350px;background-color: #292A2B;">
                  <p style="color: white;width: 200px;display: inline-block">耗时：{{ time }}ms</p>
                  <p style="color: white;width: 200px;margin-left: 14px;display: inline-block">返回结果：</p>
                  <el-scrollbar style="height: 300px">
                    <json-view :data="curCode" theme="one-dark" />
                  </el-scrollbar>
                </div>
              </el-tab-pane>
              <el-tab-pane name="logs" label="运行日志">
                <div name="testResult" style="height: 200px;">
                  <el-table :data="codeLog" stripe empty-text="暂无数据" class="el_tab_alage">
                    <el-table-column align="left" fit prop="id" label="id"></el-table-column>
                    <el-table-column align="left" fit prop="timestamp" label="时间戳"></el-table-column>
                    <el-table-column align="left" fit prop="body" label="内容"></el-table-column>
                  </el-table>
                </div>
              </el-tab-pane>
            </el-tabs>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-dialog title="调试" :visible.sync="testFiled" :before-close="clearDialog">
      <el-row>
        <el-col>
          <el-tabs v-model="activeParams">
            <el-tab-pane label disabled name="other"></el-tab-pane>
            <el-tab-pane label="Headers" name="headers"></el-tab-pane>
            <el-tab-pane label="Parameters" name="parameters"></el-tab-pane>
            <el-tab-pane label="Body" name="body"></el-tab-pane>
            <el-tab-pane label disabled name="other"></el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24" v-if="activeParams === 'headers'">
          <el-button type="text" icon="el-icon-plus" @click="addHeader">添加</el-button>
          <hr color="#cccccc" />
          <div class="content_style">
            <template v-for="item in content.headers">
              <div style="margin-bottom: 5px">
                <el-input v-model="item.key" style="width: 40%"></el-input>&nbsp;=&nbsp;
                <el-input v-model="item.value" style="width: 40%"></el-input>&nbsp;&nbsp;
                <el-button type="text" icon="el-icon-delete" @click="deleteHeader(item.id)"></el-button>
              </div>
            </template>
          </div>
        </el-col>
        <el-col :span="24" v-if="activeParams === 'parameters'">
          <el-button type="text" icon="el-icon-plus" @click="addParameter">添加</el-button>
          <hr color="#cccccc" />
          <div class="content_style">
            <template v-for="item in content.parameters">
              <div style="margin-bottom: 5px">
                <el-input v-model="item.key" style="width: 40%"></el-input>&nbsp;=&nbsp;
                <el-input v-model="item.value" style="width: 40%"></el-input>&nbsp;&nbsp;
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
              <el-input
                type="textarea"
                v-model="content.body.content"
                :rows="7"
                style="width: 90%;"
                v-if="content.body.type === 'application/json' || content.body.type === 'application/xml'"
              ></el-input>
              <div v-else>
                <el-button type="text" class="el-icon-plus" style="width: 0px;" @click="addBody">添加</el-button>
                <div style="display: flex;flex-direction: column;overflow:auto;height: 16.2vh;">
                  <template v-for="item in content.body.content">
                    <div style="margin-bottom: 5px">
                      <el-input v-model="item.key" style="width: 40%"></el-input>&nbsp;=&nbsp;
                      <el-input v-model="item.value" style="width: 40%"></el-input>&nbsp;&nbsp;
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
          <hr color="#cccccc" />
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12" :offset="8">
          <div style="margin-bottom: 5px">
            <el-button @click="startTest" v-loading.fullscreen.lock="fullscreenLoading">调试</el-button>
            <el-button @click="returnBack">取消</el-button>
          </div>
        </el-col>
      </el-row>
    </el-dialog>
    <el-dialog title="运行" :visible.sync="showRunList" :before-close="clearDialog">
      <template>
        <el-table
          :data="containerData"
          style="width: 100%;min-height: 300px"
          @row-click="goDeployApi"
        >
          <el-table-column prop="name" label="容器名称" width="200"></el-table-column>
          <el-table-column prop="url" label="服务器地址" width="280"></el-table-column>
          <el-table-column prop="detail" label="描述" width="360"></el-table-column>
        </el-table>
        <el-col :span="24">
          <Pagination5
            ref="page"
            :total="total"
            @pageChange="pageChange"
            style="position: relative;right: 10px"
          ></Pagination5>
        </el-col>
      </template>
    </el-dialog>
  </el-main>
</template>

<script>
import go from "@/assets/gojs/go.js";
import $ from "jquery";
import {
  findAllDataSource,
  findAllContainer,
  datasourceFindAll,
  dataSourceById,
  dataSourceByUnitId,
  findRequestMetaData,
  generateSql
} from "@/api/resources";
import { deployApi } from "@/api/apiDeployment";
import { findById, add } from "@/api/apiDevelopment";
import { getMethod } from "@/api/containerTest";
import jsonView from "@/components/json-view/json-view";
import qs from 'qs'

export default {
  components: { jsonView },
  data() {
    return {
      selectValue: [],
      tableDisabled: true,
      btnDisabled: true,
      dialogVisible: false,
      tableData: [],
      props1: {
        multiple: true,
        checkStrictly: false
      },
      ruleForm: {
        dataBase: [],
        dataTable:[],
        metadataData1: [],
        metadataData2: []
      },
      options: [],
      options1: [],
      options2: [],
      selectOptions: [
        {
          value: "id-int",
          label: "id"
        },
        {
          value: "name-string",
          label: "name"
        }
      ],
      apiId: "",
      action: "",
      headers: {
        Authorization: ""
      },
      rules: {
        url: [
          {
            required: true,
            message: "请输入请求地址",
            trigger: "change"
          }
        ]
      },
      rules1: {
        dataTable: [
          {
            required: true,
            message: "请选择实体表",
            trigger: "change"
          }
        ]
      },
      databaseFormShow: false,
      conditionFormShow: false,
      functionFormShow: false,
      httpFormShow: false,
      outFormShow: false,
      alismsFormShow: false,
      emailFormShow: false,
      webserviceFormShow: false,
      defaultForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: ""
      },
      databaseForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        dataSource: {},
        sql: ""
      },
      httpForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        url: "",
        method: "",
        body: "",
        bodyType: "",
        head: [],
        param: []
      },
      conditionForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        conditions: []
      },
      functionForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        connectObject: ""
      },
      outForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        name: "",
        path: ""
      },
      alismsForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        akId: "",
        akSecret: "",
        signName: "",
        templateId: "",
        mobiles: "",
        content: ""
      },
      emailForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        smtpUrl: "",
        smtpPort: "",
        sender: "",
        recipients: "",
        email: "",
        authCode: "",
        subject: "",
        content: ""
      },
      webserviceForm: {
        text: "",
        loc: "",
        type: "",
        key: "",
        category: "",
        url: "",
        functionName: "",
        functionParam: ""
      },
      head: [],
      param: [],
      conditions: [],
      nodeList: [],
      linkList: [],
      sourceApiHost: [],
      total: 0,
      pageSize: "10",
      currentPage: "1",
      testFiled: false,
      activeParams: "headers",
      showRunList: false,
      curCode: "",
      codeLog: [],
      cmOptions: {
        value: "",
        mode: "text/x-mariadb",
        theme: "panda-syntax",
        readOnly: true
      },
      //右下参数
      content: {
        headers: [],
        parameters: [],
        body: {
          type: "application/json",
          content: ""
        }
      },
      fullscreenLoading: false,
      //测试结果
      testResult: "",
      time: 0,
      containerData: [],
      websocket: null,
      apiHost: "",
      apiPath: "",
      protocol: "",
      apiMethod: "",
      groupId: "",
      propertiesWidth: 12,
      designerWidth: 12,
      showTabs: false,
      activeName: ""
    };
  },
  created() {
    this.apiId = this.$route.query.id;
    this.apiPath = this.$route.query.requestUrl;
    this.protocol = this.$route.query.protocol;
    this.apiMethod = this.$route.query.method;
    this.headers.Authorization =
      "token " + JSON.parse(sessionStorage.getItem("UserInfo")).token;
    this.closeProperties();
    this.fetchData();
    this.findDataSourceId();
    this.findRequest()
  },

  methods: {
    // 获取实体库列表
    async findDataSourceId() {
      console.log(this.databaseForm.text)
      let type = ''
      if(this.databaseForm.text == 'Dameng'){
        type = ''
      }else if(this.databaseForm.text == 'MySQL'){
        type = 'Mysql'
      }else if(this.databaseForm.text == 'Oracle'){
        type = 'Oracle'
      }else if(this.databaseForm.text == 'PostgreSQL'){
        type = 'Postgresql'
      }else if(this.databaseForm.text == 'Sqlserver'){
        type = 'SqlServer'
      }
      const response = await datasourceFindAll({type:type});
      let options = [];
      if (response.data.code == 1) {
        let arr = []
        response.data.data.forEach((v,i)=>{
          let obj = v
          obj.host = v.ip
          obj.userName = v.username
          obj.dataName = v.dataBaseName
          delete obj['ip']
          delete obj['username']
          delete obj['dataBaseName']
          arr.push(obj)
        })
        this.sourceApiHost = arr

        // datas.forEach((v, i) => {
        //   let obj = {
        //     value: v.id + "-" + v.type + "-" + v.name,
        //     label: v.name
        //   };
        //   options.push(obj);
        // });
        // this.options = options.slice(0);
      }
    },
    // 获取映射参数
    async findRequest(){
      const that = this;
      const response = await findRequestMetaData({ apiId: that.apiId });
      let options = [];
      if (response.data.code == 1) {
        let datas = response.data.data.payload;
        datas.forEach((v, i) => {
          let obj = {
            value: v.name + "-" + v.type+ "-" + v.id+ "-" + v.from+ "-" + v.description,
            label: v.name
          };
          options.push(obj);
        });
        this.selectOptions = options.slice(0);
      }
    },
    toSql(){
      this.dialogVisible = true
    },
    handleToSql(){
      // this.dialogVisible = false
      this.$refs['ruleForm'].validate((valid) => {
          if (valid) {
            let fields = []
            let wheres = []
            let metadataData2 = this.ruleForm.metadataData2
            let tableData = this.tableData
            let cont = 0
            if(metadataData2 && metadataData2.length > 0){
                metadataData2.forEach((v,i)=>{
                  if(v[0].split('-')[2] === '1'){
                    fields.push(v[0].split('-')[3])
                  }else{
                    fields.push(v[0].split('-')[0])
                  }
                })
            }
            if(tableData && tableData.length > 0){
                tableData.forEach((v,i)=>{
                  let arr = []
                  arr.push({
                    name:v.source
                  })
                  if(v.target){
                    let num = v.target.split('-')
                    arr.push({
                      name:num[0],
                      type:num[1],
                      id:num[2],
                      from:num[3],
                      description:num[4],
                    })
                  }else{
                     this.$message.error('请选择映射参数');
                      cont = 1
                  }
                  wheres.push(arr)
                })
            }
            let data1 = {
              databaseType:this.ruleForm.dataBase.type,
              tableName:this.ruleForm.dataTable[0].split('-')[1],
              fields:fields,
              wheres:wheres
            }
            if(cont == 0){
              generateSql(data1).then((res)=>{
                if(res.data.code == 1){
                  this.databaseForm.sql = res.data.data
                  this.saveData('database')
                  this.dialogVisible = false
                  this.tableData = [];
                  this.ruleForm.dataTable = []
                  this.ruleForm.metadataData1 = []
                  this.ruleForm.metadataData2 = []
                  this.tableDisabled = true;
                }
              });
            }
          } else {
            return false;
          }
        });
    },
    tableBaseChoose(){
      this.tableBaseChange(this.ruleForm.dataBase)
      this.saveData('database')
    },
    // 选择实体库带出实体表
    tableBaseChange(val) {
      this.tableData = [];
      this.ruleForm.dataTable = []
      this.ruleForm.metadataData1 = []
      this.ruleForm.metadataData2 = []
      this.tableDisabled = true;
      if(val.name){
        this.btnDisabled = false
        let children = [];
        dataSourceById({ dataSourceId: val.id }).then(res => {
          if (res.data.code == 1) {
            let datas1 = res.data.data;
            datas1.forEach((v, ind) => {
              let obj = {
                value: v.id + "-" + v.name,
                label: v.name
              };
              children.push(obj);
            });
            this.options1 = children;
          }
        });
      }else{
        this.btnDisabled = true
      }
      // this.saveData('database')
    },
    // 选择实体表带出字段
    tableBaseChange1(val) {
      this.tableData = [];
      this.ruleForm.metadataData1 = []
      this.ruleForm.metadataData2 = []
      if(val.length > 0){
        this.tableDisabled = false;
        let child = [];
        let data = val[0].split("-");
        let children = [];
        dataSourceByUnitId({ dataUnitId: data[0] }).then(respon => {
          if (respon.data.code == 1) {
            let datas2 = respon.data.data;
            if (datas2.length > 0) {
              datas2.forEach((value, index) => {
                let label = value.columnName;
                let obj = {
                  value: value.columnName + "-" + value.columnType + "-" + value.hasReplace + "-" + value.replaceSql,
                  label: label
                };
                children.push(obj);
              });
            }
            this.options2 = children;
          }
       });
      }
    },
    // 选择输入字段改变table
    handleItemChange1(val) {
      let arr = [];
      val.forEach((v, i) => {
        let obj = {};
        obj.source = v[0].split("-")[0];
        obj.sourceType = v[0].split("-")[1];
        if (this.tableData) {
          let target = "";
          let targetType = "";
          this.tableData.forEach((v, i) => {
            if (v.source == obj.source && v.sourceType == obj.sourceType) {
              target = v.target;
              targetType = v.targetType;
            }
          });
          obj.target = target;
          obj.targetType = targetType;
        }
        arr.push(obj);
      });
      this.tableData = arr;
    },
    // 映射参数选择给映射参数类型赋值
    selectChange(ind, val) {
      this.tableData[ind].targetType = val.split("-")[1];
    },
    // 获取列表
    async fetchData() {
      const that = this;
      let data = {};
      data.page = "1";
      data.size = "1000";
      // // 获取源数据列表
      // try {
      //   const res = await findAllDataSource(data);
      //   if (res.data.code === 1) {
      //     that.sourceApiHost = res.data.data.content;
      //   } else {
      //     this.$message.error(res.data.msg);
      //   }
      // } catch (even) {
      //   this.$message.error(even.msg);
      // }s
      try {
        const res = await findById({ apiId: that.apiId });
        if (res.data.code === 1) {
          if (
            res.data.data === null ||
            res.data.data.file === null ||
            res.data.data.file === ""
          ) {
            let requestObj = {
              type: "Request",
              key: "1",
              category: "Request",
              loc: "100 100",
              text: "Request"
            };
            that.nodeList = that.nodeList.concat(requestObj);
            let respondObj = {
              type: "Respond",
              key: "2",
              category: "Respond",
              loc: "100 220",
              text: "Respond"
            };
            that.nodeList = that.nodeList.concat(respondObj);
            that.myDiagram.model = go.Model.fromJson({
              class: "go.GraphLinksModel",
              linkFromPortIdProperty: "fromPort",
              linkToPortIdProperty: "toPort",
              nodeDataArray: [
                {
                  key: "1",
                  loc: "100 100",
                  category: "Request",
                  text: "Request"
                },
                {
                  key: "2",
                  loc: "100 220",
                  category: "Respond",
                  text: "Respond"
                }
              ],
              linkDataArray: []
            });
          } else {
            let jsonData = JSON.parse(res.data.data.file);
            let node = jsonData.nodes;
            let link = jsonData.links;
            this.nodeList = JSON.parse(JSON.stringify(jsonData.nodes));
            this.linkList = JSON.parse(JSON.stringify(jsonData.links));
            that.myDiagram.model = go.Model.fromJson({
              class: "go.GraphLinksModel",
              linkFromPortIdProperty: "fromPort",
              linkToPortIdProperty: "toPort",
              nodeDataArray: node,
              linkDataArray: link
            });
          }
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    //右下按钮
    async startTest() {
      const that = this;
      if (that.apiHost === "" || that.apiHost === "undefined") {
        this.$message.error("请先运行");
        return false;
      }
      that.showTabs = true;
      that.activeName = "result";
      if (that.verifyNodeAndLink()) {
        that.fullscreenLoading = true;
        let content = this.getContentJSONString();
        let tempContent = JSON.parse(content);
        let params = tempContent.Parameters;
        let headers = tempContent.Headers;
        let body = tempContent.Body;
        let stTime = new Date();
        let type = that.protocol;
        let res = [];
        let url = type.toLowerCase() + "://" + that.apiHost;
        res = await getMethod(
          url,
          that.apiPath,
          that.apiMethod,
          headers,
          params,
          body
        );
        let enTime = new Date();
        let relTime = enTime.getTime() - stTime.getTime();
        that.curCode = res;
        that.resultBox = true;
        that.time = relTime;
        that.fullscreenLoading = false;
        that.testFiled = false;
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
    websocketOnError(e) {
      //连接建立失败重连
      console.log("建立连接失败", e);
      this.websocketClose();
    },
    websocketOnMessage(e) {
      //数据接收
      const data = JSON.parse(e.data);
      this.codeLog.push(data);
      console.log("websocket接受到的数据");
    },
    websocketClose(e) {
      //关闭
      console.log("断开连接", e);
    },

    // 部署
    openDeploy() {
      const that = this;
      if (that.verifyNodeAndLink()) {
        that.showRunList = true;
        this.getContainer();
      }
    },
    //获取部署页面
    async getContainer() {
      const that = this;
      let data = {};
      data.page = this.currentPage;
      data.size = this.pageSize;
      data.status = "1";
      try {
        that.isSubmitLoading = true;
        const res = await findAllContainer(data);
        that.isSubmitLoading = false;
        if (res.data.code === 1) {
          res.data.data.content.forEach(item => {
            let url = item.ip + ":" + item.port;
            item.url = url;
          });
          this.containerData = res.data.data.content;
          this.total = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    goDeployApi(row) {
      this.isSubmitLoading = true;
      this.showTabs = true;
      this.activeName = "logs";
      const that = this;
      let data = {
        apiId: that.apiId,
        containerId: row.id,
        name: row.name,
        url: row.url
      };
      deployApi(data).then(res => {
        if (res.data.code === 1) {
          this.$message.success("部署成功");
          that.apiHost = row.url;
          this.initWebSocket(that.apiHost + "/api/container/" + that.apiId);
          that.showRunList = false;
          that.isSubmitLoading = false;
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    verifyNodeAndLink() {
      const that = this;
      if (that.nodeList.length === 2) {
        this.$message.error("请添加节点");
        return false;
      }
      if (that.linkList.length === 0) {
        this.$message.error("请添加连线");
        return false;
      }
      for (let node of Object.values(that.nodeList)) {
        let nodeLinkStatus = false;
        let nodeFrom = false;
        let nodeTo = false;
        for (let link of Object.values(that.linkList)) {
          if (
            (node.category === "Request" || node.category === "Respond") &&
            (node.key === link.from || node.key === link.to)
          ) {
            nodeLinkStatus = true;
            nodeFrom = true;
            nodeTo = true;
          }
          if (node.key === link.from) {
            nodeLinkStatus = true;
            nodeFrom = true;
          }
          if (node.key === link.to) {
            nodeLinkStatus = true;
            nodeTo = true;
          }
        }
        if (!nodeLinkStatus || !nodeFrom || !nodeTo) {
          this.$message.error("请添加" + node.category + "节点的连线");
          return false;
        }
      }
      return true;
    },
    addConditions() {
      const that = this;
      let obj = {
        condition: "",
        node: ""
      };
      that.conditions = that.conditions.concat(obj);
    },
    addHeads() {
      const that = this;
      let obj = {
        name: "",
        value: ""
      };
      that.head = that.head.concat(obj);
    },
    addParams() {
      const that = this;
      let obj = {
        name: "",
        value: ""
      };
      that.param = that.param.concat(obj);
    },
    deleteConditions(item, index) {
      const that = this;
      that.index = that.conditions.indexOf(item);
      if (index === 0) {
        return;
      }
      if (index !== -1) {
        that.conditions.splice(index, 1);
      }
    },
    deleteHeads(item, index) {
      const that = this;
      that.index = that.head.indexOf(item);
      if (index === 0) {
        return;
      }
      if (index !== -1) {
        that.head.splice(index, 1);
      }
    },
    deleteParams(item, index) {
      const that = this;
      that.index = that.param.indexOf(item);
      if (index === 0) {
        return;
      }
      if (index !== -1) {
        that.param.splice(index, 1);
      }
    },
    nodeClick: function(e, node) {
      const that = this;
      that.clearData();
      let type = that.getDataType(node.key);
      if (type === null) {
        return;
      }
      if (node.category !== "Request" && node.category !== "Respond") {
        that.showProperties();
      } else {
        that.closeProperties();
        return;
      }
      that.setDefault(type, node.category, node.key, node.loc, node.text);
    },
    setDefault(type, category, key, loc, text) {
      const that = this;
      if (type != null) {
        that.showDialog(key, category, type);
      } else {
        if (category === "Request") {
          that.defaultForm.text = text;
          that.defaultForm.loc = loc;
          that.defaultForm.key = key;
          that.defaultForm.category = category;
          that.defaultForm.type = "Request";
        } else if (category === "Respond") {
          that.defaultForm.text = text;
          that.defaultForm.loc = loc;
          that.defaultForm.key = key;
          that.defaultForm.category = category;
          that.defaultForm.type = "Respond";
        } else if (
          category === "MySQL" ||
          category === "PostgreSQL" ||
          category === "Oracle" ||
          category === "Sqlserver" ||
          category === "Dameng"
        ) {
          that.databaseForm.text = text;
          that.databaseForm.loc = loc;
          that.databaseForm.key = key;
          that.databaseForm.category = category;
          that.databaseForm.type = "database";
          that.showDialog(key, category, "database");
        } else if (category === "HttpClient") {
          that.httpForm.text = text;
          that.httpForm.loc = loc;
          that.httpForm.key = key;
          that.httpForm.category = category;
          that.httpForm.type = "http";
          that.httpForm.method = 1;
          that.httpForm.bodyType = 1;
          let headObj = {
            name: "",
            value: ""
          };
          that.head = that.head.concat(headObj);
          let paramObj = {
            name: "",
            value: ""
          };
          that.param = that.param.concat(paramObj);
          that.showDialog(key, category, "http");
        } else if (category === "switch") {
          that.conditionForm.text = text;
          that.conditionForm.loc = loc;
          that.conditionForm.key = key;
          that.conditionForm.category = category;
          that.conditionForm.type = "condition";
          let obj = {
            condition: "",
            node: ""
          };
          that.conditions = that.conditions.concat(obj);
          that.showDialog(key, category, "condition");
        } else if (category === "js脚本") {
          that.functionForm.text = text;
          that.functionForm.loc = loc;
          that.functionForm.key = key;
          that.functionForm.category = category;
          that.functionForm.type = "function";
          that.showDialog(key, category, "function");
        } else if (category === "excel") {
          that.outForm.text = text;
          that.outForm.loc = loc;
          that.outForm.key = key;
          that.outForm.category = category;
          that.outForm.type = "out";
          that.showDialog(key, category, "out");
        } else if (category === "AliSMS") {
          that.alismsForm.text = text;
          that.alismsForm.loc = loc;
          that.alismsForm.key = key;
          that.alismsForm.category = category;
          that.alismsForm.type = "alisms";
          that.showDialog(key, category, "alisms");
        } else if (category === "Email") {
          that.emailForm.text = text;
          that.emailForm.loc = loc;
          that.emailForm.key = key;
          that.emailForm.category = category;
          that.emailForm.type = "email";
          that.showDialog(key, category, "email");
        } else if (category === "WebService") {
          that.webserviceForm.text = text;
          that.webserviceForm.loc = loc;
          that.webserviceForm.key = key;
          that.webserviceForm.category = category;
          that.webserviceForm.type = "webservice";
          that.showDialog(key, category, "webservice");
        } else {
          that.showDialog(key, category, "");
        }
      }
    },
    clearData() {
      const that = this;
      that.conditions = [];
      that.head = [];
      that.param = [];
      Object.keys(that.defaultForm).forEach(
        key => (that.defaultForm[key] = "")
      );
      Object.keys(that.databaseForm).forEach(
        key => (that.databaseForm[key] = "")
      );
      Object.keys(that.httpForm).forEach(key => (that.httpForm[key] = ""));
      Object.keys(that.conditionForm).forEach(
        key => (that.conditionForm[key] = "")
      );
      Object.keys(that.functionForm).forEach(
        key => (that.functionForm[key] = "")
      );
      Object.keys(that.outForm).forEach(key => (that.outForm[key] = ""));
      Object.keys(that.alismsForm).forEach(key => (that.alismsForm[key] = ""));
      Object.keys(that.emailForm).forEach(key => (that.emailForm[key] = ""));
      Object.keys(that.webserviceForm).forEach(
        key => (that.webserviceForm[key] = "")
      );
    },
    showDialog(key, category, type) {
      const that = this;
      that.setData(key, type);
      if (type === "database") {
        that.databaseFormShow = true;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      } else if (type === "http") {
        that.databaseFormShow = false;
        that.httpFormShow = true;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      } else if (type === "condition") {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = true;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      } else if (type === "function") {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = true;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      } else if (type === "out") {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = true;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      } else if (type === "alisms") {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = true;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      } else if (type === "email") {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = true;
        that.webserviceFormShow = false;
      } else if (type === "webservice") {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = true;
      } else {
        that.databaseFormShow = false;
        that.httpFormShow = false;
        that.conditionFormShow = false;
        that.functionFormShow = false;
        that.outFormShow = false;
        that.alismsFormShow = false;
        that.emailFormShow = false;
        that.webserviceFormShow = false;
      }
    },
    getDataType(key) {
      const that = this;
      if (that.nodeList != null) {
        for (let v of Object.values(that.nodeList)) {
          if (v.key === key) {
            return v.type;
          }
        }
        return null;
      }
    },
    setData(key, type) {
      const that = this;
      if (that.nodeList != null) {
        for (let v of Object.values(that.nodeList)) {
          if (v.key === key) {
            if (type === "Request") {
              that.defaultForm.text = v.text;
              that.defaultForm.loc = v.loc;
              that.defaultForm.type = v.type;
              that.defaultForm.key = v.key;
              that.defaultForm.category = v.category;
            } else if (type === "Respond") {
              that.defaultForm.text = v.text;
              that.defaultForm.loc = v.loc;
              that.defaultForm.type = v.type;
              that.defaultForm.key = v.key;
              that.defaultForm.category = v.category;
            } else if (type === "database") {
              that.databaseForm.text = v.text;
              that.databaseForm.loc = v.loc;
              that.databaseForm.type = v.type;
              that.databaseForm.key = v.key;
              that.databaseForm.category = v.category;
              that.ruleForm.dataBase = v.dataSource;
              that.findDataSourceId()
              if(that.ruleForm.dataBase.name){
                that.tableBaseChange(that.ruleForm.dataBase)
                that.btnDisabled = false
              }else{
                that.btnDisabled = true
              }
              that.databaseForm.sql = v.sql;
            } else if (type === "http") {
              that.httpForm.text = v.text;
              that.httpForm.loc = v.loc;
              that.httpForm.type = v.type;
              that.httpForm.key = v.key;
              that.httpForm.category = v.category;
              that.httpForm.method = v.method;
              that.httpForm.url = v.url;
              that.httpForm.body = v.body;
              that.httpForm.bodyType = v.bodyType;
              that.head = v.head;
              that.param = v.param;
              that.httpForm.head = v.head;
              that.httpForm.param = v.param;
            } else if (type === "condition") {
              that.conditionForm.loc = v.loc;
              that.conditionForm.text = v.text;
              that.conditionForm.type = v.type;
              that.conditionForm.key = v.key;
              that.conditionForm.category = v.category;
              that.conditions = v.conditions;
              that.conditionForm.conditions = v.conditions;
            } else if (type === "function") {
              that.functionForm.text = v.text;
              that.functionForm.loc = v.loc;
              that.functionForm.type = v.type;
              that.functionForm.key = v.key;
              that.functionForm.category = v.category;
              that.functionForm.connectObject = v.connectObject;
            } else if (type === "out") {
              that.outForm.text = v.text;
              that.outForm.loc = v.loc;
              that.outForm.type = v.type;
              that.outForm.key = v.key;
              that.outForm.category = v.category;
              that.outForm.name = v.name;
              that.outForm.path = v.path;
            } else if (type === "alisms") {
              that.alismsForm.text = v.text;
              that.alismsForm.loc = v.loc;
              that.alismsForm.type = v.type;
              that.alismsForm.key = v.key;
              that.alismsForm.category = v.category;
              that.alismsForm.akId = v.akId;
              that.alismsForm.akSecret = v.akSecret;
              that.alismsForm.signName = v.signName;
              that.alismsForm.templateId = v.templateId;
              that.alismsForm.mobiles = v.mobiles;
              that.alismsForm.content = v.content;
            } else if (type === "email") {
              that.emailForm.text = v.text;
              that.emailForm.loc = v.loc;
              that.emailForm.type = v.type;
              that.emailForm.key = v.key;
              that.emailForm.category = v.category;
              that.emailForm.name = v.name;
              that.emailForm.smtpUrl = v.smtpUrl;
              that.emailForm.smtpPort = v.smtpPort;
              that.emailForm.sender = v.sender;
              that.emailForm.recipients = v.recipients;
              that.emailForm.email = v.email;
              that.emailForm.authCode = v.authCode;
              that.emailForm.subject = v.subject;
              that.emailForm.content = v.content;
            } else if (type === "webservice") {
              that.webserviceForm.text = v.text;
              that.webserviceForm.loc = v.loc;
              that.webserviceForm.type = v.type;
              that.webserviceForm.key = v.key;
              that.webserviceForm.category = v.category;
              that.webserviceForm.name = v.name;
              that.webserviceForm.url = v.url;
              that.webserviceForm.functionName = v.functionName;
              that.webserviceForm.functionParam = v.functionParam;
            }
          }
        }
      }
    },
    saveData(type) {
      const that = this;
      if (that.nodeList != null) {
        for (let v of Object.values(that.nodeList)) {
          if (type === "Request") {
            if (v.key === that.defaultForm.key) {
              v.loc = that.defaultForm.loc;
              that.saveDevelopment();
              return;
            }
          }
          if (type === "Respond") {
            if (v.key === that.defaultForm.key) {
              v.loc = that.defaultForm.loc;
              that.saveDevelopment();
              return;
            }
          } else if (type === "database") {
            if (v.key === that.databaseForm.key) {
              v.text = that.databaseForm.text;
              v.loc = that.databaseForm.loc;
              v.category = that.databaseForm.category;
              v.dataSource = that.ruleForm.dataBase;
              v.sql = that.databaseForm.sql;
              that.saveDevelopment();
              return;
            }
          } else if (type === "http") {
            if (v.key === that.httpForm.key) {
              v.text = that.httpForm.text;
              v.loc = that.httpForm.loc;
              v.category = that.httpForm.category;
              v.method = that.httpForm.method;
              v.url = that.httpForm.url;
              v.body = that.httpForm.body;
              v.bodyType = that.httpForm.bodyType;
              v.head = that.head;
              v.param = that.param;
              that.saveDevelopment();
              return;
            }
          } else if (type === "condition") {
            if (v.key === that.conditionForm.key) {
              v.text = that.conditionForm.text;
              v.loc = that.conditionForm.loc;
              v.category = that.conditionForm.category;
              v.conditions = that.conditions;
              that.saveDevelopment();
              return;
            }
          } else if (type === "function") {
            if (v.key === that.functionForm.key) {
              v.text = that.functionForm.text;
              v.loc = that.functionForm.loc;
              v.category = that.functionForm.category;
              v.connectObject = that.functionForm.connectObject;
              that.saveDevelopment();
              return;
            }
          } else if (type === "out") {
            if (v.key === that.outForm.key) {
              v.text = that.outForm.text;
              v.loc = that.outForm.loc;
              v.category = that.outForm.category;
              v.name = that.outForm.name;
              v.path = that.outForm.path;
              that.saveDevelopment();
              return;
            }
          } else if (type === "alisms") {
            if (v.key === that.alismsForm.key) {
              v.text = that.alismsForm.text;
              v.loc = that.alismsForm.loc;
              v.category = that.alismsForm.category;
              v.akId = that.alismsForm.akId;
              v.akSecret = that.alismsForm.akSecret;
              v.signName = that.alismsForm.signName;
              v.templateId = that.alismsForm.templateId;
              v.mobiles = that.alismsForm.mobiles;
              v.content = that.alismsForm.content;
              that.saveDevelopment();
              return;
            }
          } else if (type === "email") {
            if (v.key === that.emailForm.key) {
              v.text = that.emailForm.text;
              v.loc = that.emailForm.loc;
              v.category = that.emailForm.category;
              v.name = that.emailForm.name;
              v.smtpUrl = that.emailForm.smtpUrl;
              v.smtpPort = that.emailForm.smtpPort;
              v.sender = that.emailForm.sender;
              v.recipients = that.emailForm.recipients;
              v.email = that.emailForm.email;
              v.authCode = that.emailForm.authCode;
              v.subject = that.emailForm.subject;
              v.content = that.emailForm.content;
              that.saveDevelopment();
              return;
            }
          } else if (type === "webservice") {
            if (v.key === that.webserviceForm.key) {
              v.text = that.webserviceForm.text;
              v.loc = that.webserviceForm.loc;
              v.category = that.webserviceForm.category;
              v.name = that.webserviceForm.name;
              v.url = that.webserviceForm.url;
              v.functionName = that.webserviceForm.functionName;
              v.functionParam = that.webserviceForm.functionParam;
              that.saveDevelopment();
              return;
            }
          }
        }
      }
      if (type === "database") {
        let databaseObj = {
          text: that.databaseForm.text,
          loc: that.databaseForm.loc,
          type: that.databaseForm.type,
          key: that.databaseForm.key,
          category: that.databaseForm.category,
          dataSource: that.ruleForm.dataBase,
          sql: that.databaseForm.sql
        };
        if (that.databaseForm.category !== "") {
          that.nodeList = that.nodeList.concat(databaseObj);
        }
      } else if (type === "http") {
        let httpObj = {
          text: that.httpForm.text,
          loc: that.httpForm.loc,
          type: that.httpForm.type,
          key: that.httpForm.key,
          method: that.httpForm.method,
          url: that.httpForm.url,
          body: that.httpForm.body,
          bodyType: that.httpForm.bodyType,
          category: that.httpForm.category,
          head: that.head,
          param: that.param
        };
        if (that.httpForm.category !== "") {
          that.nodeList = that.nodeList.concat(httpObj);
        }
      } else if (type === "condition") {
        let conditionObj = {
          text: that.conditionForm.text,
          loc: that.conditionForm.loc,
          type: that.conditionForm.type,
          key: that.conditionForm.key,
          category: that.conditionForm.category,
          conditions: that.conditions
        };
        if (that.conditionForm.category !== "") {
          that.nodeList = that.nodeList.concat(conditionObj);
        }
      } else if (type === "function") {
        let functionObj = {
          text: that.functionForm.text,
          loc: that.functionForm.loc,
          type: that.functionForm.type,
          key: that.functionForm.key,
          category: that.functionForm.category,
          connectObject: that.functionForm.connectObject
        };
        if (that.functionForm.category !== "") {
          that.nodeList = that.nodeList.concat(functionObj);
        }
      } else if (type === "out") {
        let outObj = {
          text: that.outForm.text,
          loc: that.outForm.loc,
          type: that.outForm.type,
          key: that.outForm.key,
          category: that.outForm.category,
          name: that.outForm.name,
          path: that.outForm.path
        };
        if (that.outForm.category !== "") {
          that.nodeList = that.nodeList.concat(outObj);
        }
      } else if (type === "alisms") {
        let alismsObj = {
          text: that.alismsForm.text,
          loc: that.alismsForm.loc,
          type: that.alismsForm.type,
          key: that.alismsForm.key,
          category: that.alismsForm.category,
          akId: that.alismsForm.akId,
          akSecret: that.alismsForm.akSecret,
          signName: that.alismsForm.signName,
          templateId: that.alismsForm.templateId,
          mobiles: that.alismsForm.mobiles,
          content: that.alismsForm.content
        };
        if (that.alismsForm.category !== "") {
          that.nodeList = that.nodeList.concat(alismsObj);
        }
      } else if (type === "email") {
        let emailObj = {
          text: that.emailForm.text,
          loc: that.emailForm.loc,
          type: that.emailForm.type,
          key: that.emailForm.key,
          category: that.emailForm.category,
          name: that.emailForm.name,
          smtpUrl: that.emailForm.smtpUrl,
          smtpPort: that.emailForm.smtpPort,
          sender: that.emailForm.sender,
          recipients: that.emailForm.recipients,
          email: that.emailForm.email,
          authCode: that.emailForm.authCode,
          subject: that.emailForm.subject,
          content: that.emailForm.content
        };
        if (that.emailForm.category !== "") {
          that.nodeList = that.nodeList.concat(emailObj);
        }
      } else if (type === "webservice") {
        let webserviceObj = {
          text: that.webserviceForm.text,
          loc: that.webserviceForm.loc,
          type: that.webserviceForm.type,
          key: that.webserviceForm.key,
          category: that.webserviceForm.category,
          name: that.webserviceForm.name,
          url: that.webserviceForm.url,
          functionName: that.webserviceForm.functionName,
          functionParam: that.webserviceForm.functionParam
        };
        if (that.webserviceForm.category !== "") {
          that.nodeList = that.nodeList.concat(webserviceObj);
        }
      }
      that.saveDevelopment();
    },
    updateNode(node) {
      const that = this;
      if (that.nodeList != null) {
        for (let v of Object.values(that.nodeList)) {
          if (v.key === node.key) {
            if (node.type === "Request") {
              that.defaultForm.text = v.text;
              that.defaultForm.loc = node.loc;
              that.defaultForm.type = v.type;
              that.defaultForm.key = v.key;
              that.defaultForm.category = v.category;
            } else if (node.type === "Respond") {
              that.defaultForm.text = v.text;
              that.defaultForm.loc = node.loc;
              that.defaultForm.type = v.type;
              that.defaultForm.key = v.key;
              that.defaultForm.category = v.category;
            } else if (node.type === "database") {
              that.databaseForm.text = v.text;
              that.databaseForm.loc = node.loc;
              that.databaseForm.type = v.type;
              that.databaseForm.key = v.key;
              that.databaseForm.category = v.category;
              that.ruleForm.dataBase = v.dataSource;
              if(that.ruleForm.dataBase.name){
                that.tableBaseChange(that.ruleForm.dataBase)
                that.btnDisabled = false
              }else{
                that.btnDisabled = true
              }
              that.databaseForm.sql = v.sql;
            } else if (node.type === "http") {
              that.httpForm.text = v.text;
              that.httpForm.loc = node.loc;
              that.httpForm.type = v.type;
              that.httpForm.key = v.key;
              that.httpForm.category = v.category;
              that.httpForm.method = v.method;
              that.httpForm.url = v.url;
              that.httpForm.body = v.body;
              that.httpForm.bodyType = v.bodyType;
              that.head = v.head;
              that.param = v.param;
              that.httpForm.head = v.head;
              that.httpForm.param = v.param;
            } else if (node.type === "condition") {
              that.conditionForm.loc = node.loc;
              that.conditionForm.text = v.text;
              that.conditionForm.type = v.type;
              that.conditionForm.key = v.key;
              that.conditionForm.category = v.category;
              that.conditions = v.conditions;
              that.conditionForm.conditions = v.conditions;
            } else if (node.type === "function") {
              that.functionForm.text = v.text;
              that.functionForm.loc = node.loc;
              that.functionForm.type = v.type;
              that.functionForm.key = v.key;
              that.functionForm.category = v.category;
              that.functionForm.connectObject = v.connectObject;
            } else if (node.type === "out") {
              that.outForm.text = v.text;
              that.outForm.loc = node.loc;
              that.outForm.type = v.type;
              that.outForm.key = v.key;
              that.outForm.category = v.category;
              that.outForm.name = v.name;
              that.outForm.path = v.path;
            } else if (node.type === "alisms") {
              that.alismsForm.text = v.text;
              that.alismsForm.loc = node.loc;
              that.alismsForm.category = v.category;
              that.alismsForm.akId = v.akId;
              that.alismsForm.akSecret = v.akSecret;
              that.alismsForm.signName = v.signName;
              that.alismsForm.templateId = v.templateId;
              that.alismsForm.mobiles = v.mobiles;
              that.alismsForm.content = v.content;
            } else if (node.type === "email") {
              that.emailForm.text = v.text;
              that.emailForm.loc = node.loc;
              that.emailForm.category = v.category;
              that.emailForm.name = v.name;
              that.emailForm.smtpUrl = v.smtpUrl;
              that.emailForm.smtpPort = v.smtpPort;
              that.emailForm.sender = v.sender;
              that.emailForm.recipients = v.recipients;
              that.emailForm.email = v.email;
              that.emailForm.authCode = v.authCode;
              that.emailForm.subject = v.subject;
              that.emailForm.content = v.content;
            } else if (node.type === "webservice") {
              that.webserviceForm.text = v.text;
              that.webserviceForm.loc = node.loc;
              that.webserviceForm.category = v.category;
              that.webserviceForm.name = v.name;
              that.webserviceForm.url = v.url;
              that.webserviceForm.functionName = v.functionName;
              that.webserviceForm.functionParam = v.functionParam;
            }
          }
        }
      }
      that.saveData(node.type);
    },
    saveDevelopment() {
      const that = this;
      let file = {
        apiId: that.apiId,
        nodes: that.nodeList,
        links: that.linkList
      };
      let data = {
        apiDesignId: that.apiId,
        file: JSON.stringify(file)
      };
      add(data).then(res => {
        if (res.data.code === 1) {
          console.log("保存成功");
        } else {
          this.$message.error(res.data.msg);
        }
      });
    },
    deleteNodeAndLink(type, data) {
      const that = this;
      if (type === "node") {
        this.nodeList.some((node, i) => {
          if (node.key === data.key) {
            this.nodeList.splice(i, 1);
          }
        });
        //删除from连线
        that.linkList.some((link, i) => {
          if (link.from === data.key) {
            that.linkList.splice(i, 1);
          }
        });
        //删除to连线
        that.linkList.some((link, i) => {
          if (link.to === data.key) {
            that.linkList.splice(i, 1);
          }
        });
      } else if (type === "link") {
        that.linkList.some((link, i) => {
          if (link.from === data.from && link.to === data.to) {
            that.linkList.splice(i, 1);
          }
        });
      }
      that.saveData("link");
      return true;
    },
    addLink(data) {
      const that = this;
      if (data.from === "1" && data.to === "2") {
        that.myDiagram.model.removeLinkData(data);
        return;
      }
      if (that.linkList != null) {
        for (let v of Object.values(that.linkList)) {
          if (v.from === data.from && v.to === data.to) {
            //that.myDiagram.
            that.myDiagram.model.removeLinkData(data);
            return;
          }
        }
        that.linkList = that.linkList.concat(data);
      }
      that.saveData("all");
    },
    clearDialog(done) {
      done();
    },
    // 翻页
    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      this.getContainer();
    },
    //右下参数
    addHeader() {
      let head = {
        id: this.content.headers.length,
        key: "",
        value: ""
      };
      this.content.headers.push(head);
    },

    deleteHeader(id) {
      this.content.headers.splice(id, 1);
      let headersTemp = this.content.headers;
      headersTemp.forEach((item, index) => {
        item.id = index;
      });
      this.content.headers = headersTemp;
    },

    addParameter() {
      let parameter = {
        id: this.content.parameters.length,
        key: "",
        value: ""
      };
      this.content.parameters.push(parameter);
    },

    deleteParameter(id) {
      this.content.parameters.splice(id, 1);
      let parametersTemp = this.content.parameters;
      parametersTemp.forEach((item, index) => {
        item.id = index;
      });
      this.content.parameters = parametersTemp;
    },
    changeBody() {
      if (
        this.content.body.type === "application/json" ||
        this.content.body.type === "application/xml"
      ) {
        this.content.body.content = "";
      } else {
        this.content.body.content = [];
      }
    },

    addBody() {
      let body = {
        id: this.content.parameters.length,
        key: "",
        value: ""
      };
      this.content.body.content.push(body);
    },

    deleteBody(id) {
      this.content.body.content.splice(id, 1);
      let contentTemp = this.content.body.content;
      contentTemp.forEach((item, index) => {
        item.id = index;
      });
      this.content.body.content = contentTemp;
    },
    clearTestResult() {
      const that = this;
      that.curCode = "";
      that.time = "";
    },

    returnBack() {
      const that = this;
      that.content = {
        headers: [],
        parameters: [],
        body: {
          type: "application/json",
          content: ""
        }
      };
      that.testFiled = false;
    },

    getContentJSONString() {
      let content = {};
      content["Headers"] = {};
      content["Parameters"] = {};
      content["Body"] = {};
      content["Content-type"] = this.content.body.type;
      this.content.headers.forEach(item => {
        content["Headers"][item.key] = item.value;
      });
      this.content.parameters.forEach(item => {
        content["Parameters"][item.key] = item.value;
      });
      if (this.content.body.type === "form-data") {
        this.content.body.content.forEach(item => {
          content["Body"][item.key] = item.value;
        });
      } else if (this.content.body.type === "application/json") {
        if (this.content.body.content) {
          content["Body"] = JSON.parse(this.content.body.content);
        }
      } else if (this.content.body.type === "application/xml") {
        let tempXml = "<body>" + this.content.body.content + "</body>";
        content["Body"] = this.$x2js.xml2js(tempXml);
      }
      return JSON.stringify(content);
    },
    handleClick(tab, event) {},
    showProperties() {
      this.propertiesWidth = 12;
      this.designerWidth = 12;
    },
    closeProperties() {
      this.propertiesWidth = 0;
      this.designerWidth = 24;
    }
  },
  mounted() {
    let mySelf = this;
    const $ = go.GraphObject.make;

    mySelf.myDiagram = $(go.Diagram, "myDiagramDiv", {
      initialPosition: new go.Point(0, 0), //定义左上角为0同步kettle设置
      LinkDrawn: showLinkLabel,
      LinkRelinked: showLinkLabel,
      "animationManager.duration": 800,
      "undoManager.isEnabled": true,
      allowVerticalScroll: false, //禁止垂直拖动画布
      allowHorizontalScroll: false, //禁止水平拖动画布
      SelectionMoved: SelectionMoved,
      nodeSelectionAdornmentTemplate: $(
        go.Adornment,
        "Auto",
        $(go.Shape, "Rectangle", { fill: "white", stroke: null })
      )
    });

    mySelf.myDiagram.commandHandler.canDeleteSelection = function(e) {
      //用例获取选中的节点或线
      return mySelf.myDiagram.selection.all(function(nodeOrLink) {
        if (nodeOrLink.data.from) {
          return mySelf.deleteNodeAndLink("link", nodeOrLink.data);
        }
        if (nodeOrLink.data.key) {
          //判断是否存在不允许删除的节点
          if (
            nodeOrLink.data.text === "Request" ||
            nodeOrLink.data.text === "Respond"
          ) {
            mySelf.$message.error("不允许删除该节点");
            return false;
          } else {
            return mySelf.deleteNodeAndLink("node", nodeOrLink.data);
          }
        }
      });
    };

    mySelf.myDiagram.addModelChangedListener(function(evt) {
      if (!evt.isTransactionFinished) return;
      let txn = evt.object; // a Transaction
      if (txn === null) return;
      txn.changes.each(function(e) {
        //添加节点
        if (
          e.change === go.ChangedEvent.Insert &&
          e.modelChange === "nodeDataArray"
        ) {
          let type = "";
          if (e.newValue.category === "Request") {
            type = "Request";
          } else if (e.newValue.category === "Respond") {
            type = "Respond";
          } else if (
            e.newValue.category === "MySQL" ||
            e.newValue.category === "PostgreSQL" ||
            e.newValue.category === "Oracle" ||
            e.newValue.category === "Sqlserver" ||
            e.newValue.category === "Dameng"
          ) {
            type = "database";
          } else if (e.newValue.category === "HttpClient") {
            type = "http";
          } else if (e.newValue.category === "switch") {
            type = "condition";
          } else if (e.newValue.category === "js脚本") {
            type = "function";
          } else if (e.newValue.category === "excel") {
            type = "out";
          } else if (e.newValue.category === "AliSMS") {
            type = "alisms";
          } else if (e.newValue.category === "Email") {
            type = "email";
          } else if (e.newValue.category === "WebService") {
            type = "webservice";
          }
          mySelf.clearData();
          mySelf.setDefault(
            null,
            e.newValue.category,
            e.newValue.key,
            e.newValue.loc,
            e.newValue.text
          );
          mySelf.saveData(type);
        }
        //添加连线
        if (
          e.change === go.ChangedEvent.Insert &&
          e.modelChange === "linkDataArray"
        ) {
          mySelf.addLink(e.newValue);
        }
      });
    });

    function SelectionMoved(e) {
      //选择事件
      e.diagram.selection.each(function(nodeOrLink) {
        if (nodeOrLink instanceof go.Node) {
          //选择节点
          mySelf.updateNode(nodeOrLink.data);
        }
      });
    }

    function nodeStyle() {
      return [
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(
          go.Point.stringify
        ),
        {
          locationSpot: go.Spot.Center,
          mouseEnter: function(e, obj) {
            showPorts(obj.part, true);
          },
          mouseLeave: function(e, obj) {
            showPorts(obj.part, false);
          }
        }
      ];
    }

    function makePort(name, spot, output, input) {
      return $(go.Shape, "RoundedRectangle", {
        fill: "transparent",
        stroke: null, // this is changed to "white" in the showPorts function
        desiredSize: new go.Size(8, 8),
        alignment: spot,
        alignmentFocus: spot, // align the port on the main Shape
        portId: name, // declare this object to be a "port"
        fromSpot: spot,
        toSpot: spot, // declare where links may connect at this port
        fromLinkable: output,
        toLinkable: input, // declare whether the user may draw links to/from here
        cursor: "pointer" // show a different cursor to indicate potential link point
      });
    }

    mySelf.myDiagram.nodeTemplateMap.add(
      "Request",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#80A8FF",
            stroke: null
          }),
          $(
            go.TextBlock,
            "Request",
            { font: "16px, Arial, sans-serif", stroke: "whitesmoke" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the top, all output only:
        makePort("T", go.Spot.Top, true, false),
        makePort("L", go.Spot.Left, true, false),
        makePort("R", go.Spot.Right, true, false),
        makePort("B", go.Spot.Bottom, true, false)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "Respond",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FAB4BD",
            stroke: null
          }),
          $(
            go.TextBlock,
            "Respond",
            { font: "16px, Arial, sans-serif", stroke: "whitesmoke" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, false, true),
        makePort("L", go.Spot.Left, false, true),
        makePort("R", go.Spot.Right, false, true),
        makePort("B", go.Spot.Bottom, false, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "MySQL",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "MySQL",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "PostgreSQL",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "PostgreSQL",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "Oracle",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "Oracle",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "Sqlserver",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "Sqlserver",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "Dameng",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "Dameng",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "HttpClient",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "HttpClient",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "switch",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#CAF982",
            stroke: null
          }),
          $(
            go.TextBlock,
            "switch",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "js脚本",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "js脚本",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "excel",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "excel",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "AliSMS",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "AliSMS",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "Email",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "Email",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "WebService",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "WebService",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    mySelf.myDiagram.nodeTemplateMap.add(
      "",
      $(
        go.Node,
        "Spot",
        { click: mySelf.nodeClick },
        nodeStyle(),
        $(
          go.Panel,
          "Auto",
          $(go.Shape, "RoundedRectangle", {
            minSize: new go.Size(162, 36),
            fill: "#FFFF80",
            stroke: null
          }),
          $(
            go.TextBlock,
            "",
            { font: "16px, Arial, sans-serif", stroke: "#000000" },
            new go.Binding("text")
          )
        ),
        // three named ports, one on each side except the bottom, all input only:
        makePort("T", go.Spot.Top, true, true),
        makePort("L", go.Spot.Left, true, true),
        makePort("R", go.Spot.Right, true, true),
        makePort("B", go.Spot.Bottom, true, true)
      )
    );

    // replace the default Link template in the linkTemplateMap
    mySelf.myDiagram.linkTemplate = $(
      go.Link, // the whole link panel
      {
        routing: go.Link.AvoidsNodes,
        curve: go.Link.Bezier,
        corner: 5,
        toShortLength: 4,
        relinkableFrom: true,
        relinkableTo: true,
        reshapable: true,
        resegmentable: true,
        // mouse-overs subtly highlight links:
        mouseEnter: function(e, link) {
          link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
        },
        mouseLeave: function(e, link) {
          link.findObject("HIGHLIGHT").stroke = "transparent";
        }
      },
      new go.Binding("points").makeTwoWay(),
      $(
        go.Shape, // the highlight shape, normally transparent
        {
          isPanelMain: true,
          strokeWidth: 8,
          stroke: "transparent",
          name: "HIGHLIGHT"
        }
      ),
      $(
        go.Shape, // the link path shape
        { isPanelMain: true, stroke: "gray", strokeWidth: 2 }
      ),
      $(
        go.Shape, // the arrowhead
        { toArrow: "standard", stroke: null, fill: "gray" }
      )
    );

    // Make link labels visible if coming out of a "conditional" node.
    // This listener is called by the "LinkDrawn" and "LinkRelinked" DiagramEvents.
    function showLinkLabel(e) {
      var label = e.subject.findObject("LABEL");
      if (label !== null)
        label.visible = e.subject.fromNode.data.figure === "Diamond";
    }

    // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
    mySelf.myDiagram.toolManager.linkingTool.temporaryLink.routing =
      go.Link.Orthogonal;
    mySelf.myDiagram.toolManager.relinkingTool.temporaryLink.routing =
      go.Link.Orthogonal;

    // initialize the Palette that is on the left side of the page
    mySelf.myPalette = $(
      go.Palette,
      "myPaletteDiv", // must name or refer to the DIV HTML element
      {
        "animationManager.duration": 800, // slightly longer than default (600ms) animation
        nodeTemplateMap: mySelf.myDiagram.nodeTemplateMap, // share the templates used by myDiagram
        model: new go.GraphLinksModel([
          // specify the contents of the Palette
          { category: "MySQL", text: "MySQL" },
          { category: "PostgreSQL", text: "PostgreSQL" },
          { category: "Oracle", text: "Oracle" },
          { category: "Sqlserver", text: "Sqlserver" },
          { category: "Dameng", text: "Dameng" },
          { category: "HttpClient", text: "HttpClient" },
          { category: "switch", text: "switch" },
          { category: "js脚本", text: "js脚本" },
          { category: "excel", text: "excel" },
          { category: "AliSMS", text: "AliSMS" },
          { category: "Email", text: "Email" },
          { category: "WebService", text: "WebService" }
        ])
      }
    );

    function customFocus() {
      var x = window.scrollX || window.pageXOffset;
      var y = window.scrollY || window.pageYOffset;
      go.Diagram.prototype.doFocus.call(this);
      window.scrollTo(x, y);
    }

    mySelf.myDiagram.doFocus = customFocus;
    mySelf.myPalette.doFocus = customFocus;

    function showPorts(node, show) {
      var diagram = node.diagram;
      if (!diagram || diagram.isReadOnly || !diagram.allowLink) return;
      node.ports.each(function(port) {
        port.stroke = show ? "white" : null;
      });
    }
  }
};
</script>

<style lang="scss" scoped>
canvas {
  border: 0px;
  outline: none;
}
/deep/ .scrollbar {
  white-space: nowrap; //强制一行显示（看需要）
  .el-scrollbar {
    display: flex;
    justify-content: space-around;
    padding: 0 10px;
  }
}
.runConsole /deep/ .el-card__header {
  padding: 5px;
}

.myPaletteDiv /deep/ .el-card__body {
  padding: 5px;
}

.runLog /deep/ .el-textarea__inner {
  width: 100%;
  background-color: #292a2b;
  color: #ffffff;
}
</style>
