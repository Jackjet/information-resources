<template>
  <div class="main">
    <div class="content">
      <el-steps :active="active" align-center process-status="finish">
        <el-step title="1.API基本信息" @click.native="now(0)"></el-step>
        <el-step title="2.API请求信息" @click.native="now(1)"></el-step>
      </el-steps>
      <el-row v-show="active === 0">
        <el-form :model="ruleForm1" :rules="rules1" ref="ruleForm1">
          <el-row>
            <h3 style="color: grey">API基本信息</h3>
            <hr color="#cccccc">
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item
                  prop="name"
                  label="名称"
                  class='InputWidth'
                  label-width="80px"
                  style="width: 100%">
                <el-input v-model="ruleForm1.name" placeholder="请输入名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item
                  prop="requestUrl"
                  label="Path"
                  class='InputWidth'
                  label-width="80px"
                  style="width: 95%">
                <el-input v-model="ruleForm1.requestUrl" placeholder="请输入Path" @change="checkPathFormat"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="分组:" label-width="80px" prop="groupId">
                <el-cascader
                    style="width: 100%"
                    expandTrigger="hover"
                    v-model="ruleForm1.groupId"
                    :options="groupOptions"
                    :props="groupOptionProps"
                    clearable>
                </el-cascader>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="标签:" label-width="80px" prop="metas">
                <div v-for="(item, index) in ruleForm1.metas">
                  <el-select size="small" style="margin-right: 5px;" placeholder="请选择" filterable
                             v-model="item.metaKey">
                    <el-option v-for="op in options" :key="op.value" :label="op.value"
                               :value="op.value"/>
                  </el-select>
                  <el-tag type="info" style="margin-left: 3px;">
                    <input style="border: #ECF5FF;background-color: #F4F4F5;outline: none;width: 140px;"
                           v-model="item.metaValue"/>
                  </el-tag>
                  <el-button type="small" style="margin-top: 4px;margin-left: 3px;" icon="el-icon-plus"
                             @click="addParameter" v-if="index===0"></el-button>
                  <el-button type="small" style="margin-top: 4px;margin-left: 3px;" icon="el-icon-delete"
                             @click="deleteParameter(item.id)" v-else></el-button>
                </div>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="9">
              <el-form-item label="描述:" label-width="80px">
                <el-input maxlength='200' type="textarea" :rows="3" placeholder="请输入描述" clearable size="medium"
                          v-model="ruleForm1.detail"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-row>
      <el-row v-show="active === 1">
        <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2">
          <el-row>
            <h3 style="color: grey">API请求信息</h3>
            <hr color="#cccccc">
          </el-row>
          <el-row>
            <el-col :span="8" :offset="8">
              <el-form-item
                  prop="protocol"
                  label="请求协议"
                  class='InputWidth'>
                <el-radio-group v-model="ruleForm2.protocol">
                  <el-radio label="Http">Http</el-radio>
                  <el-radio label="Https">Https</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="10" :offset="8">
              <el-form-item
                  prop="method"
                  label="请求方式"
                  class='InputWidth'>
                <el-radio-group v-model="ruleForm2.method">
                  <el-radio label="GET"></el-radio>
                  <el-radio label="POST"></el-radio>
                  <el-radio label="PUT"></el-radio>
                  <el-radio label="DELETE"></el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <h3 style="color: grey">入参内容定义</h3>
            <hr color="#cccccc">
          </el-row>
          <el-row>
            <el-col :span="2">
              <h4 style="color: grey">请求参数设置</h4>
            </el-col>
            <el-col :span="8">
              <el-popover
                  placement="left"
                  width="400"
                  trigger="click"
                  :ref="popver">
                <div>
                  <h5>请求示例：</h5>
                  <hr>
                  <p>http://www.xxx.com/route?params=xxxx</p>
                </div>
                <el-button slot="reference" size="mini" type="primary" style="margin-top: 15px;margin-left: 10px;">
                  请求示例
                </el-button>
              </el-popover>
              <el-button size="mini" type="primary" style="margin-top: 15px;margin-left: 10px;" @click="addParam">添加参数
              </el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-table
                :data="ruleForm2.params"
                stripe
                empty-text="暂无数据"
                class="el_tab_alage">
              <el-table-column
                  align="center"
                  fit
                  prop="name"
                  label="参数名称">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="type"
                  label="参数位置">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type">
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
                  <el-checkbox v-model="scope.row.isRequired"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="defaultValue"
                  label="默认值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.defaultValue"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="regular"
                  label="校验格式">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.regular"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="description"
                  label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  label="操作"
                  width="120px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteParam(scope.row.id)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
          <el-row>
            <el-form-item
                prop="formatType"
                label="内容格式类型"
                class='InputWidth'>
              <el-radio-group v-model="ruleForm2.formatType" @change="formatTypeChange">
                <el-radio label="application/json">JSON</el-radio>
                <el-radio label="multipart/form-data">form-data</el-radio>
                <!-- <el-radio label="application/xml">XML</el-radio> -->
              </el-radio-group>
            </el-form-item>
          </el-row>
          <el-row :gutter="2">
            <el-col :span="3">
              <h4 style="color: grey;width: 150px;">请求内容参数设置</h4>
            </el-col>
            <el-col :span="2" v-if="ruleForm2.formatType !== 'multipart/form-data'">
              <el-upload
                  class="upload-demo"
                  :action="action"
                  :headers="headers"
                  :file-list="bodyFileList"
                  :on-success="handleBodyUploadSuccess"
                  multiple="false"
                  style="margin-top: 15px;"
                  :before-upload="beforeUpload">
                <el-button size="mini" type="primary" icon="el-icon-upload2">导入参数</el-button>
              </el-upload>
            </el-col>
            <el-col :span="1" v-if="ruleForm2.formatType !== 'multipart/form-data'">
              <i class="el-icon-warning-outline" style="margin-top:20px; cursor:pointer" title="导入文件支持.txt和.json"></i>
            </el-col>
            <el-col :span="3" v-if="ruleForm2.formatType === 'multipart/form-data'">
              <el-button size="mini" type="primary" style="margin-top: 15px;" @click="addBody('-1')">添加参数</el-button>
            </el-col>
          </el-row>
          <el-row v-if="ruleForm2.formatType === 'multipart/form-data'">
            <el-table
                :data="bodyArray"
                style="width: 100%;margin-bottom: 20px;"
                row-key="id"
                default-expand-all
                :tree-props="{children: 'children', hasChildren: 'hasChildren1'}"
                stripe
                empty-text="暂无数据"
                class="el_tab_alage">
              <el-table-column
                  align="center"
                  fit
                  prop="name"
                  label="参数名称"
                  min-width="300px">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" style="width: 40%"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="type"
                  label="参数位置">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type" @change="typeChange(scope.row.id,scope.row.type)">
                    <el-option value="String"></el-option>
                    <el-option value="Number"></el-option>
                    <el-option value="Array"></el-option>
                    <el-option value="Object"></el-option>
                    <el-option value="Integer"></el-option>
                    <el-option value="Boolean"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="isRequired"
                  label="是否必填">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.isRequired"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="defaultValue"
                  label="默认值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.defaultValue"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="regular"
                  label="校验格式">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.regular"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="description"
                  label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  label="操作"
                  width="200px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteBody(scope.row.id)">删除
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-plus" @click="addBody(scope.row.id)"
                             v-if="scope.row.type === 'Object'">添加子节点
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
          <el-row v-else>
            <el-input type="textarea" v-model="bodyString" rows="8"></el-input>
          </el-row>
          <el-row>
            <h4 style="color: grey">返回参数设置</h4>
          </el-row>
          <el-row>
            <el-col :span="8" :offset="9">
              <el-radio-group v-model="showType" @click.native="showTypeClick">
                <el-radio-button label="模板"></el-radio-button>
                <el-radio-button label="预览"></el-radio-button>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row v-if="showType === '模板'" :gutter="100">
            <el-col :span="2">
              <el-upload
                  class="upload-demo"
                  :action="action"
                  :headers="headers"
                  :file-list="responseFileList"
                  :on-success="handleResponseUploadSuccess"
                  multiple="false"
                  :before-upload="beforeUpload">
                <el-button size="mini" type="primary" icon="el-icon-upload2">导入参数</el-button>
              </el-upload>
            </el-col>
            <el-col :span="2">
              <el-button size="mini" type="primary" @click="addResponse('-1')">添加参数</el-button>
            </el-col>
          </el-row>
          <el-row v-if="showType === '模板'">
            <el-table
                :data="ruleForm2.response"
                style="width: 100%;margin-bottom: 20px;"
                row-key="id"
                default-expand-all
                :tree-props="{children: 'children', hasChildren: 'hasChildren1'}"
                stripe
                empty-text="暂无数据"
                class="el_tab_alage">
              <el-table-column
                  align="center"
                  fit
                  prop="name"
                  label="参数名称"
                  min-width="300px">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" style="width: 40%"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="type"
                  label="参数类型">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type" @change="responseTypeChange(scope.row.id,scope.row.type)">
                    <el-option value="String"></el-option>
                    <el-option value="Number"></el-option>
                    <el-option value="Array"></el-option>
                    <el-option value="Object"></el-option>
                    <el-option value="Integer"></el-option>
                    <el-option value="Boolean"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="defaultValue"
                  label="默认值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.defaultValue"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  prop="description"
                  label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  fit
                  label="操作"
                  width="200px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteResponse(scope.row.id)">删除
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-plus" @click="addResponse(scope.row.id)"
                             v-if="scope.row.type === 'Object'">添加子节点
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
          <el-row v-if="showType === '预览'">
            <el-col>
              <el-input type="textarea" :rows="10" :cols="100" v-model="responsePreview" disabled></el-input>
            </el-col>
          </el-row>
        </el-form>
      </el-row>
      <el-row>
        <el-col :span="8" :offset="8">
          <el-button @click="last" v-if="active === 1">上一步</el-button>
          <el-button @click="next" v-if="active === 0">下一步</el-button>
          <el-button type="primary" @click="sure" v-if="active === 1">提交</el-button>
          <el-button @click="close">取消</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {add, findById, getTree, update} from "@/api/apiDesign";
import {findAllMetas} from "@/api/resources";

export default {
  components: {},

  data() {
    return {
      id: '',
      isAdd: true,
      active: 0,
      showType: '模板',
      options: [],
      ruleForm1: {
        name: '',
        requestUrl: '',
        detail: '',
        metas: [{
          id: 1,
          metaKey: '',
          metaValue: ''
        }],
        groupId: ''
      },
      ruleForm2: {
        protocol: 'Http',
        method: 'GET',
        formatType: 'application/json',
        body: [],
        params: [],
        response: []
      },
      rules1: {
        name: [{
          required: true,
          message: '请输入名称',
          trigger: ['change', 'blur']
        }],
        requestUrl: [{
          required: true,
          message: '请输入Path',
          trigger: ['change', 'blur']
        }],
        groupId: [{
          required: true,
          message: '请选择组',
          trigger: "change"
        }],
        metas: [{
          required: true,
          message: '请选择标签',
          trigger: "change"
        }]
      },
      rules2: {
        protocol: [{
          required: true,
          message: '请至少选择一个请求协议',
          trigger: ['change', 'blur']
        }],
        method: [{
          required: true,
          message: '请选择请求方式',
          trigger: ['change', 'blur']
        }],
        formatType: [{
          required: true,
          message: '请选择内容格式类型',
          trigger: ['change', 'blur']
        }]
      },
      tableData: [],
      pageSizeList: [5, 10],
      total: 0,
      pageSize: 5,
      currentPage: 1,
      SearchItem: {
        name: ''
      },
      loading: false,
      action: process.env.VUE_APP_BASE_API + '/webadmin/sourceApi/importFile?file=',
      headers: {
        Authorization: "token " + JSON.parse(sessionStorage.getItem("UserInfo")).token
      },
      bodyFileList: [],
      responseFileList: [],
      bodyString: '',
      bodyArray: [],
      responsePreview: '',
      groupOptions: [],
      groupOptionProps: {
        value: 'id',
        label: 'name',
        children: 'child',
        checkStrictly: true,
        emitPath: false
      }
    }
  },
  async created() {
    this.id = this.$route.query.id
    this.ruleForm1.groupId = this.$route.query.groupId
    if (this.id === '') {
      this.isAdd = true
    } else {
      this.isAdd = false
      await this.getSourceApi()
    }
    await this.findMetas()
    await this.getGroupList()
  },
  methods: {
    async findMetas(data) {
      const that = this
      that.loading = true
      const response = await findAllMetas()
      that.loading = false
      if (response.data.code === 1) {
        that.options = []
        response.data.data.content.map(function (v, k) {
          that.options.push({key: v.name, value: v.name, label: v.name})
        })
        console.log(that.options)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    // 获取列表
    async getGroupList() {
      const that = this
      try {
        const res = await getTree()
        if (res.data.code === 1) {
          that.groupOptions = res.data.data
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    last() {
      // if(!this.validate()){
      //   return false
      // }
      if (this.active !== 0) {
        this.active--
      }
    },
    next() {
      console.log(this.ruleForm1.metas)
      if (!this.validate()) {
        return false
      }
      if (this.active === 0) {
        this.active++
      }
    },
    now(pos) {
      if (!this.validate()) {
        return false
      }
      this.active = pos
    },

    isJSON(str) {
      if (/^[\],:{}\s]*$/.test(str.replace(/\\["\\\/bfnrtu]/g, '@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d (?:\.\d*)?(?:[eE][ \-]?\d )?/g, ']').replace(/(?:^|:|,)(?:\s*\[) /g, ''))) {
        return true;
      } else {
        return false;
      }
    },
    validate() {
      let result = true
      let formName = 'ruleForm1'
      //判断标签
      let metas = this.ruleForm1.metas[0].metaKey
      if(metas === ""){
        this.$message.error("请选择标签")
        return
      }
      if (this.active === 1) {
        formName = 'ruleForm2'
        for (let i = 0; i < this.ruleForm2.params.length; i++) {
          if (this.ruleForm2.params[i].name === '') {
            result = false
            this.$message.warning('请求参数第' + (i + 1) + '行参数名为空')
            return false
          }
        }
        if (this.ruleForm2.formatType === 'multipart/form-data') {
          for (let i = 0; i < this.bodyArray.length; i++) {
            if (this.bodyArray[i].name === '') {
              result = false
              this.$message.warning('请求内容第' + (i + 1) + '行参数名为空')
              return false
            }
          }
        }

        if (this.ruleForm2.formatType === 'application/json') {
          let body = this.bodyString.replace(/"/, '\'')
          if (body.length > 0) {
            if (this.isJSON(body)) {
              result = false
              this.$message.error('请求内容格式不合法')
              return false
            }
          } else {
            body = {}
          }
        }

        for (let i = 0; i < this.ruleForm2.response.length; i++) {
          if (this.ruleForm2.response[i].name === '') {
            result = false
            this.$message.error('返回值' +
                '第' + (i + 1) + '行参数名为空')
            break
          }
        }
      }

      this.$refs[formName].validate((index) => {
        if (index === false) {
          result = false
        }
      })

      result = result && this.checkPathFormat()
      return result
    },

    checkPathFormat() {
      if (this.ruleForm1.requestUrl.charAt(0) !== '/') {
        this.$message.error('请按正确格式输入Path,例："/path"')
        return false
      } else {
        return true
      }
    },
    sure() {
      const that = this
      let isGoOn = this.validate()
      if (isGoOn) {
        that.$refs['ruleForm1'].validate((index1) => {
          if (index1 === false) {
            return false
          }

          that.$refs['ruleForm2'].validate((index2) => {
            if (index2 === false) {
              return false
            }
            if (that.isAdd) {
              that.add()
              return false
            } else {
              that.edit(that.ruleForm)
              return false
            }
          })
        })
      }
    },
    setData() {
      const that = this
      let method = that.ruleForm2.method
      let body
      if (that.ruleForm2.formatType === 'multipart/form-data') {
        body = JSON.stringify(that.bodyArray)
      } else {
        body = that.bodyString
      }
      let metaKey=''
      let metaValue=''
      let metas = this.ruleForm1.metas
      metas.forEach((item) => {
        if(item.id===1){
          metaKey+=item.metaKey
          metaValue+=item.metaValue
        }else{
          metaKey+=(','+item.metaKey)
          metaValue+=(','+item.metaValue)
        }
      })
      return {
        name: that.ruleForm1.name,
        requestUrl: that.ruleForm1.requestUrl,
        groupId: that.ruleForm1.groupId,
        metaKey: metaKey,
        metaValue:metaValue,
        detail: that.ruleForm1.detail,
        protocol: that.ruleForm2.protocol,
        method: method,
        formatType: that.ruleForm2.formatType,
        body: body,
        params: JSON.stringify(that.ruleForm2.params),
        response: JSON.stringify(that.ruleForm2.response),
      }
    },
    async add() {
      const that = this
      let data = that.setData()
      that.loading = true
      const response = await add(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        //that.$message.success(response.data.msg)
        data.id = response.data.data
        that.jump(data)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async edit() {
      const that = this
      let data = that.setData()
      data.id = that.id
      that.loading = true
      const response = await update(data)
      that.loading = false
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        //that.$message.success(response.data.msg)
        data.id = response.data.data
        that.jump(data)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    //添加成功跳转到编排页面
    jump(data) {
      const that = this
      this.$confirm('您可直接对API进行编排，选择是否进入', '添加成功', {
        type: 'warning'
      }).then(() => {
        console.log(data)
        that.$router.push({
          path: '/apiDesignEdit',
          query: {
            id: data.id,
            requestUrl: data.requestUrl,
            protocol: data.protocol,
            method: data.method
          }
        })
      }).catch(() => {
        that.close()
      })
    },
    close() {
      const that = this
      that.$router.push({
        path: '/apiDesign',
        query: {
          groupId: that.ruleForm1.groupId
        }
      })
    },
    async getSourceApi() {
      const that = this
      try {
        that.isSubmitLoading = true
        const res = await findById({id: that.id})
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let data = res.data.data
          that.ruleForm1.name = data.name
          that.ruleForm1.requestUrl = data.requestUrl
          that.ruleForm1.groupId = data.groupId
          that.ruleForm1.detail = data.detail
          that.ruleForm2.protocol = data.protocol
          that.ruleForm2.method = data.method
          that.ruleForm2.formatType = data.formatType
          //解析标签
          let metaKey = data.metaKey.split(',')
          let metaValue = data.metaValue.split(',')
          metaKey.forEach((item,index)=>{
            if(index===0){
              that.ruleForm1.metas[0].metaKey = metaKey[index]
              that.ruleForm1.metas[0].metaValue = metaValue[index]
            }else{
              let metas = {
                id:index+1,
                metaKey:metaKey[index],
                metaValue:metaValue[index]
              }
              that.ruleForm1.metas.push(metas)
            }
          })
          console.log(that.ruleForm1.metas)
          if (that.ruleForm2.formatType === 'multipart/form-data') {
            that.bodyArray = JSON.parse(data.body)
          } else {
            that.bodyString = data.body
          }
          if (data.params === null) {
            data.params = []
          }
          that.ruleForm2.params = JSON.parse(data.params)
          that.ruleForm2.response = JSON.parse(data.response)
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    //页面2
    showTypeClick() {
      if (this.showType === '模板') {
        let temp = this.setResponsePreview(this.ruleForm2.response, false)

        this.responsePreview = JSON.stringify(temp, null, "\t")
      }
    },
    setResponsePreview(array, isArray) {
      let temp = {}
      array.forEach((item) => {
        if (!item.hasChildren) {
          temp[item.name] = item.defaultValue
        } else {
          if (item.type === 'Array') {
            temp[item.name] = this.setResponsePreview(item.children, true)
          } else {
            temp[item.name] = this.setResponsePreview(item.children, false)
          }
        }
      })
      if (isArray) {
        let arr = []
        arr.push(temp)
        return arr
      } else {
        return temp
      }
    },
    addParam() {
      let param = {
        id: this.ruleForm2.params.length,
        name: '',
        //Parameter,Header
        type: 'Parameter',
        isRequired: true,
        defaultValue: '',
        regular: '',
        description: ''
      }
      this.ruleForm2.params.push(param)
    },
    deleteParam(id) {
      this.ruleForm2.params.splice(id, 1)
      let paramsTemp = this.ruleForm2.params

      paramsTemp.forEach((item, index) => {
        item.id = index
      })

      this.ruleForm2.params = paramsTemp
    },
    typeChange(id, type) {
      if (type === 'Array') {
        this.addBody(id)
      } else {
        let arr = id.split('-')
        this.bodyArray = this.deleteLeafChildren(arr, 0, this.bodyArray)
      }
    },
    responseTypeChange(id, type) {
      if (type === 'Array') {
        this.addResponse(id)
      } else {
        let arr = id.split('-')
        this.ruleForm2.response = this.deleteLeafChildren(arr, 0, this.ruleForm2.response)
      }
    },
    addBody(id) {
      let body = {
        id: this.bodyArray.length.toString(),
        name: '',
        //String,Number,Array,Object,Integer,Boolean
        type: 'String',
        isRequired: true,
        defaultValue: '',
        regular: '',
        description: '',
        children: [],
        hasChildren: false
      }
      if (id !== '-1') {
        let arr = id.split('-')
        this.bodyArray = this.setLeaf(arr, 0, this.bodyArray, body)
      } else {
        this.bodyArray.push(body)
      }
    },

    handleBodyUploadSuccess(response, file, fileList) {
      this.bodyFileList = []
      if (this.ruleForm2.formatType === 'application/json' || this.ruleForm2.formatType === 'application/xml') {
        this.bodyString = response.data
      } else {
        this.bodyArray = JSON.parse(response.data)
      }
    },
    handleResponseUploadSuccess(response, file, fileList) {
      this.responseFileList = []
      this.ruleForm2.response = JSON.parse(response.data)
      if (this.showType === '预览') {
        this.responsePreview = response.data
      } else {
        this.ruleForm2.response = JSON.parse(response.data)
      }
    },
    deleteBody(id) {
      let arr = id.split('-')
      this.bodyArray = this.deleteLeaf(arr, 0, this.bodyArray)
    },
    addResponse(id) {
      let response = {
        id: this.ruleForm2.response.length.toString(),
        name: '',
        type: 'String',
        defaultValue: '',
        description: '',
        children: [],
        hasChildren: false
      }
      if (id !== '-1') {
        let arr = id.split('-')
        let temp = this.setLeaf(arr, 0, this.ruleForm2.response, response)
        this.ruleForm2.response = temp
      } else {
        this.ruleForm2.response.push(response)
      }
    },
    deleteResponse(id) {
      let arr = id.split('-')
      this.ruleForm2.response = this.deleteLeaf(arr, 0, this.ruleForm2.response)
    },
    formatTypeChange() {
      if (this.ruleForm2.formatType === 'multipart/form-data') {
        this.bodyArray = []
      } else {
        this.bodyString = ''
      }
    },

    //public
    getNowId(arr, floor) {
      let nowId = ''
      for (let i = floor; i >= 0; i--) {
        if (nowId === '') {
          nowId = arr[i]
        } else {
          nowId = arr[i] + '-' + nowId
        }
      }
      return nowId
    },
    deleteLeafChildren(arr, floor, node) {
      let pre = this.getNowId(arr, floor)
      node.forEach((item, index) => {
        if (item.id === pre && floor === arr.length - 1) {
          item.children = []
          item.hasChildren = false
        } else {
          floor++
          item.children = this.deleteLeafChildren(arr, floor, item.children)
        }
      })
      return node
    },
    deleteLeaf(arr, floor, node) {
      let pre = this.getNowId(arr, floor)
      node.forEach((item, index) => {
        if (item.id === pre) {
          node.splice(index, 1)
          if (node === undefined) {
            node = []
          }
        } else {
          floor++
          item.children = this.deleteLeaf(arr, floor, item.children)
          if (item.children.length === 0) {
            item.hasChildren = false
          }
        }
      })
      return node
    },
    setLeaf(arr, floor, node, body) {
      let pre = this.getNowId(arr, floor)

      node.forEach((item) => {
        if (item.id === pre) {
          body.id = pre + '-' + item.children.length
          item.children.push(body)
          item.hasChildren = true
        } else {
          floor++
          item.children = this.setLeaf(arr, floor, item.children, body)
        }
      })
      return node
    },
    beforeUpload(file) {
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
      const whiteList = ['json', 'txt'];

      if (whiteList.indexOf(fileSuffix) === -1) {
        this.$message.error("上传文件只能是json和txt格式")
        return false;
      }
    },
    addParameter() {
      let parameter = {
        id: this.ruleForm1.metas.length+1
      }
      this.ruleForm1.metas.push(parameter)
    },
    deleteParameter(id) {
      this.ruleForm1.metas.splice(id, 1)
      let parametersTemp = this.ruleForm1.metas
      parametersTemp.forEach((item, index) => {
        item.id = index
      })
      this.ruleForm1.metas = parametersTemp
    },
  }
};
</script>
<style scoped>
.main {
  padding: 20px;
}
</style>
