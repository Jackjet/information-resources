<template>
  <div class="main">
    <div class="content">
      <el-steps :active="active" align-center process-status="finish">
        <el-step title="1.API基本信息" @click.native="now(0)"></el-step>
        <el-step title="2.API请求信息" @click.native="now(1)"></el-step>
        <el-step title="3.API常量信息" @click.native="now(2)"></el-step>
      </el-steps>

      <el-row v-show="active === 0">
        <el-form :model="ruleForm1" :rules="rules1" ref="ruleForm1">
          <el-row>
            <h3 style="color: grey">API基本信息</h3>
            <hr color="#cccccc" />
          </el-row>
          <el-row>
            <el-col :span="7">
              <el-form-item prop="name" label="名称" class='InpitWidth' label-width="80px" style="width: 95%">
                <el-input v-model="ruleForm1.name" placeholder="请输入名称" size="mini"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item prop="path" label="Path" class='InpitWidth' label-width="80px" style="width: 95%">
                <el-input v-model="ruleForm1.path" placeholder="请输入Path" @change="checkPathFormat" size="mini"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-row>
                <el-col :span="14">
                  <el-form-item prop="host" label="Host" label-width="80px" style="width: 95%">
                    <el-input v-model="ruleForm1.host" placeholder="请输入Host" @change="checkHostFormat(ruleForm1.host)" size="mini"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="10">
                  <div style="display: flex;flex-direction: row">
                    <el-form-item class='InpitWidth'>
                      <el-checkbox v-model="ruleForm1.loadBalancing">支持负载均衡</el-checkbox>
                    </el-form-item>
                    <div style="padding: 10px 0 0 10px;"><i class="el-icon-warning-outline" title="勾选负载均衡即可添加多个Host" style="cursor: pointer"></i></div>
                  </div>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="16" v-if="ruleForm1.loadBalancing">
                  <el-row>
                    <el-col :span="20">
                      <el-form-item label=" " label-width="80px" style="margin-right: 10px;">
                        <el-input v-model="host2" @change="checkHostFormat(host2)" size="mini"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="4" style="padding-top: 7px">
                      <el-button @click="addElement" size="mini">继续添加</el-button>
                    </el-col>
                  </el-row>
                </el-col>
              </el-row>
              <el-row v-if="ruleForm1.loadBalancing">
                <template>
                  <el-row v-for="(item,index) in hostList" :key="index">
                    <el-col :span="14">
                      <el-form-item label="" label-width="80px" style="width: 95%">
                        <el-input v-model="item.value" :key="item.key" @change="checkHostFormat(item.value)" size="mini"></el-input>
                      </el-form-item>
                    </el-col>
                    <el-col :span="10" style="padding-top: 7px">
                      <el-button @click="deleteHost(item.key)" size="mini">删除</el-button>
                    </el-col>
                  </el-row>
                </template>
              </el-row>
            </el-col>
            <el-col :span="12">
              <el-col :span="2" style="padding-top: 10px">
                <span style="color: #f56c6c;">*</span><span style="display:inline-block; font-size: 14px;width:50px">&nbsp;标签:</span>
              </el-col>
              <el-col :span="22">
                <el-form-item :label-width="this.formLabelWidth">
                  <div v-for="(item,i) in metas" :key="i">
                    <el-select size="mini" v-model="item.key" filterable remote reserve-keyword placeholder="请输入标签名" :remote-method="remoteMethod">
                      <el-option v-for="item in tagInfos" :key="item.name" :label="item.name" :value="item.name">
                      </el-option>
                    </el-select>
                    <el-input style="border: #ECF5FF;width: 140px;margin-left: 5px" v-model="item.value" size="mini"></el-input>
                    <el-button type="mini" icon="el-icon-plus" style="margin-top: 4px;margin-left: 3px;" @click="insertMeta" v-if="i === 0"></el-button>
                    <el-button type="mini" icon="el-icon-delete" style="margin-top: 4px;margin-left: 3px;" @click="removeMeta(i)" v-if="i !== 0"></el-button>
                  </div>
                </el-form-item>
              </el-col>
            </el-col>
          </el-row>
          <el-row>
            <el-col style="margin-left:35px">
              <el-form-item label="分组:" :label-width="this.formLabelWidth">
                <el-cascader size="mini" expandTrigger="hover" v-model="ruleForm1.groupId" :options="groupOptions" :props="{ checkStrictly: true,emitPath:false }" clearable>
                </el-cascader>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-row>

      <el-row v-show="active === 1">
        <el-form :model="ruleForm2" :rules="rules2" ref="ruleForm2">
          <el-row>
            <h3 style="color: grey">源API请求信息</h3>
            <hr color="#cccccc" />
          </el-row>
          <el-row>
            <el-col :span="8" :offset="8">
              <el-form-item prop="protocol" label="请求协议" class='InpitWidth'>
                <el-radio-group v-model="ruleForm2.protocol">
                  <el-radio label="Http">Http</el-radio>
                  <el-radio label="Https">Https</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="10" :offset="8">
              <el-form-item prop="method" label="请求方式" class='InpitWidth'>
                <el-checkbox-group v-model="ruleForm2.method">
                  <el-checkbox label="GET"></el-checkbox>
                  <el-checkbox label="POST"></el-checkbox>
                  <el-checkbox label="PUT"></el-checkbox>
                  <el-checkbox label="DELETE"></el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <h3 style="color: grey">入参内容定义</h3>
            <hr color="#cccccc" />
          </el-row>
          <el-row>
            <el-col :span="2">
              <h4 style="color: grey">请求参数设置</h4>
            </el-col>
            <el-col :span="8">
              <el-popover placement="left" width="400" trigger="click" :ref="popver">
                <div>
                  <h5>请求示例：</h5>
                  <hr>
                  <p>http://www.xxx.com/route?params=xxxx</p>
                </div>
                <el-button slot="reference" type="mini" style="margin-top: 15px;margin-left: 10px;">请求示例</el-button>
              </el-popover>
              <el-button type="mini" style="margin-top: 15px;margin-left: 10px;" @click="addParam">添加参数</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-table :data="ruleForm2.params" stripe empty-text="暂无数据" class="el_tab_alage">
              <el-table-column align="center" fit prop="name" label="参数名称">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="type" label="参数位置">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type">
                    <el-option value="Parameter"></el-option>
                    <el-option value="Header"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="isRequired" label="是否必填">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.isRequired"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="defaultValue" label="默认值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.defaultValue"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="regular" label="校验格式">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.regular"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="description" label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit label="操作" width="120px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteParam(scope.row.id)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
          <el-row>
            <el-form-item prop="formatType" label="内容格式类型" class='InpitWidth'>
              <el-radio-group v-model="ruleForm2.formatType" @change="formatTypeChange">
                <el-radio label="application/json">json</el-radio>
                <el-radio label="multipart/form-data">form-data</el-radio>
                <el-radio label="application/xml">xml</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-row>
          <el-row :gutter="2">
            <el-col :span="3">
              <h4 style="color: grey;width: 150px;">请求内容参数设置</h4>
            </el-col>
            <el-col :span="2" v-if="ruleForm2.formatType !== 'multipart/form-data'">
              <el-upload class="upload-demo" ref="uploadText" :file-list="bodyFileList" :multiple="false" :show-file-list="false" style="margin-top: 15px;" :before-upload="beforeUpload">
                <el-button type="mini" icon="el-icon-upload2">导入参数</el-button>
              </el-upload>
            </el-col>
            <el-col :span="1" v-if="ruleForm2.formatType !== 'multipart/form-data'">
              <i class="el-icon-warning-outline" style="margin-top:20px;cursor:pointer" title="导入文件支持.txt和.json"></i>
            </el-col>
            <el-col :span="3" v-if="ruleForm2.formatType === 'multipart/form-data'">
              <el-button type="mini" style="margin-top: 15px;" @click="addBody('-1')">添加参数</el-button>
            </el-col>
          </el-row>
          <el-row v-if="ruleForm2.formatType === 'multipart/form-data'">
            <el-table :data="bodyArray" style="width: 100%;margin-bottom: 20px;" row-key="id" default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren1'}" stripe empty-text="暂无数据" class="el_tab_alage">
              <el-table-column align="center" fit prop="name" label="参数名称" min-width="300px">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" style="width: 40%"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="type" label="参数位置">
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
              <el-table-column align="center" fit prop="isRequired" label="是否必填">
                <template slot-scope="scope">
                  <el-checkbox v-model="scope.row.isRequired"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="defaultValue" label="默认值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.defaultValue"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="regular" label="校验格式">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.regular"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="description" label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit label="操作" width="200px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteBody(scope.row.id)">删除
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="addBody(scope.row.id)" v-if="scope.row.type === 'Object'">添加子节点
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
              <el-upload class="upload-demo" :headers="headers" :file-list="responseFileList" :on-success="handleResponseUploadSuccess" :auto-upload="false" multiple="false" :before-upload="beforeUpload">
                <el-button type="mini" icon="el-icon-upload2">导入参数</el-button>
              </el-upload>
            </el-col>
            <el-col :span="2">
              <el-button type="mini" @click="addResponse('-1')">添加参数</el-button>
            </el-col>
          </el-row>
          <el-row v-if="showType === '模板'">
            <el-table :data="ruleForm2.response" style="width: 100%;margin-bottom: 20px;" row-key="id" default-expand-all :tree-props="{children: 'children', hasChildren: 'hasChildren1'}" stripe empty-text="暂无数据" class="el_tab_alage">
              <el-table-column align="center" fit prop="name" label="参数名称" min-width="300px">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name" style="width: 40%"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="type" label="参数类型">
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
              <el-table-column align="center" fit prop="defaultValue" label="默认值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.defaultValue"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="description" label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit label="操作" width="200px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteResponse(scope.row.id)">删除
                  </el-button>
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="addResponse(scope.row.id)" v-if="scope.row.type === 'Object'">添加子节点
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

      <el-row v-show="active === 2">
        <el-form :model="ruleForm3" :rules="rules3" ref="ruleForm3">
          <el-row>
            <el-col :span="2">
              <h3 style="color: grey">常量参数</h3>
            </el-col>
            <el-col :span="8">
              <el-button type="mini" style="margin-top: 15px;margin-left: 10px;" @click="addConstants">添加参数</el-button>
            </el-col>
          </el-row>
          <el-row>
            <el-table :data="ruleForm3.constants" stripe empty-text="暂无数据" class="el_tab_alage">
              <el-table-column align="center" fit prop="name" label="参数名称">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.name"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="value" label="参数值">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.value"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="type" label="参数类型">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.type">
                    <el-option value="Parameter"></el-option>
                    <el-option value="Header"></el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column align="center" fit prop="description" label="描述">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.description"></el-input>
                </template>
              </el-table-column>
              <el-table-column align="center" fit label="操作" width="120px">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" icon="el-icon-delete" @click="deleteConstants(scope.row.id)">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-row>
        </el-form>
      </el-row>

      <el-row>
        <el-col :span="8" :offset="8">
          <el-button @click="last" v-if="active !== 0">上一步</el-button>
          <el-button @click="next" v-if="active !== 2">下一步</el-button>
          <el-button @click="sure" v-if="active === 2">提交</el-button>
          <el-button @click="close">取消</el-button>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { add, findById, update } from "@/api/sourceApi.js";
import { findList } from "@/api/tagInfo.js";
import { getToken } from '@/utils/auth'
import { getGroupList } from "@/api/groupInfo.js"

export default {
  data() {
    return {
      id: '',
      isAdd: true,
      active: 0,
      showType: '模板',
      isSupplierId: false,
      ruleForm1: {
        name: '',
        host: '',
        loadBalancing: false,
        path: '',
        groupId: ''
      },
      ruleForm2: {
        protocol: 'Http',
        method: [],
        formatType: 'application/json',
        body: [],
        params: [],
        response: []
      },
      ruleForm3: {
        constants: []
      },
      rules1: {
        name: [{
          required: true,
          message: '请输入名称',
          trigger: ['change', 'blur']
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请输入名称'
        }],
        host: [{
          required: true,
          message: '请输入Host',
          trigger: ['change', 'blur']
        }],
        path: [{
          required: true,
          message: '请输入Path',
          trigger: ['change', 'blur']
        }]
      },
      rules2: {
        protocol: [{
          required: true,
          message: '请至少选择一个请求协议',
          trigger: ['change', 'blur']
        }],
        method: [{
          type: 'array',
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
      rules3: {},
      tableData: [],
      pageSizeList: [5, 10],
      total: 0,
      pageSize: 5,
      currentPage: 1,
      SearchItem: {
        name: ''
      },
      hostList: [],
      host2: '',
      loading: false,
      action: process.env.VUE_APP_BASE_API + '/webadmin/sourceApi/importFile?file=',
      headers: {
        Authorization: 'token ' + getToken()
      },
      bodyFileList: [],
      responseFileList: [],
      bodyString: '',
      bodyArray: [],
      responsePreview: '',
      //tag info
      metas: [{ key: '', value: '' }],
      tagInfos: [],

      fileDataValue: '',
      groupId: '',
      groupOptions: []
    }
  },
  created() {
    this.groupId = this.$route.query.groupId
    this.ruleForm1.groupId = this.$route.query.groupId;
    this.id = this.$route.query.id
    if (this.id === '') {
      this.isAdd = true
    } else {
      this.isAdd = false
      this.getSourceApi()
    }
    this.getGroupList()
  },
  methods: {
    // 获取分组列表
    async getGroupList() {
      const that = this
      let data = { type: '0' }
      try {
        const res = await getGroupList(data)
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
      if (!this.validate()) {
        return false
      }
      if (this.active !== 2) {
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
      if (this.active === 1) {
        formName = 'ruleForm2'

        for (let i = 0; i < this.ruleForm2.params.length; i++) {
          if (this.ruleForm2.params[i].name === '') {
            result = false
            this.$message.error('请求参数第' + (i + 1) + '行参数名为空')
            break
          }
        }

        if (this.ruleForm2.formatType === 'multipart/form-data') {
          for (let i = 0; i < this.bodyArray.length; i++) {
            if (this.bodyArray[i].name === '') {
              result = false
              this.$message.error('请求内容第' + (i + 1) + '行参数名为空')
              break
            }
          }
        }

        if (this.ruleForm2.formatType === 'application/json') {
          let body = this.bodyString.replace(/"/, '\'')
          if (body && this.isJSON(body)) {
            result = false
            this.$message.error('请求内容格式不合法')
          }
        }

        for (let i = 0; i < this.ruleForm2.response.length; i++) {
          if (this.ruleForm2.response[i].name === '') {
            result = false
            this.$message.error('返回值' +
              '3第' + (i + 1) + '行参数名为空')
            break
          }
        }
      } else if (this.active === 2) {
        formName = 'ruleForm3'

        for (let i = 0; i < this.ruleForm3.constants.length; i++) {
          if (this.ruleForm3.constants[i].name === '') {
            this.$message.error('常量信息第' + (i + 1) + '行参数名为空')
            return false
          }

          if (this.ruleForm3.constants[i].value === '') {
            this.$message.error('常量信息第' + (i + 1) + '行参数值为空')
            return false
          }
        }
      } else if (this.active === 0) {
        let metasList = this.metas
        for (let ms of metasList) {
          if (ms.key === '') {
            this.$message.error('请选择标签')
            return false
          }
          if (ms.value === '') {
            this.$message.error('请输入标签值')
            return false
          }
        }
      }

      this.$refs[formName].validate((index) => {
        if (index === false) {
          result = false
        }
      })

      result = result && this.checkHostFormat(this.ruleForm1.host)
      if (this.host2 !== '') {
        result = result && this.checkHostFormat(this.host2)
      }
      for (let i = 0; i < this.hostList.length; i++) {
        result = result && this.checkHostFormat(this.hostList[i].value)
      }

      result = result && this.checkPathFormat()
      return result

    },

    checkHostFormat(host) {
      var reg = /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5]):([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
      if (!reg.test(host)) {
        this.$message.error('请按正确格式输入host,例：127.0.0.1:8080')
        return false
      } else {
        return true
      }
    },

    checkPathFormat() {
      if (this.ruleForm1.path.charAt(0) !== '/') {
        this.$message.error('请按正确格式输入Path,例："/path"')
        return false
      }
      return true
    },

    sure() {
      const that = this
      that.$refs['ruleForm1'].validate((index1) => {
        if (index1 === false) {
          return false
        }

        that.$refs['ruleForm2'].validate((index2) => {
          if (index2 === false) {
            return false
          }

          that.$refs['ruleForm3'].validate((index3) => {
            if (index3 === false) {
              return false
            }

            for (let i = 0; i < that.ruleForm3.constants.length; i++) {
              if (that.ruleForm3.constants[i].name === '') {
                this.$message.error('常量信息第' + (i + 1) + '行参数名为空')
                return false
              }

              if (that.ruleForm3.constants[i].value === '') {
                that.$message.error('常量信息第' + (i + 1) + '行参数值为空')
                return false
              }
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
      })
    },
    setData() {
      const that = this

      let method = JSON.stringify(that.ruleForm2.method)
      method = method.replace(/"/g, '').replace('[', '').replace(']', '')

      let host = []
      let host1 = {
        target: that.ruleForm1.host
      }
      host.push(host1)

      //支持负载均衡
      if (that.ruleForm1.loadBalancing) {
        let host2 = {
          target: that.host2
        }
        host.push(host2)

        that.hostList.forEach((item) => {
          let hostItem = {
            target: item.value
          }
          host.push(hostItem)
        })
      }

      let body
      if (that.ruleForm2.formatType === 'multipart/form-data') {
        body = JSON.stringify(that.bodyArray)
      } else {
        body = that.bodyString
      }
      let metas = that.metas
      let tagName = ''
      let tagValue = ''
      metas.forEach((item) => {
        tagName = tagName + item.key + ','
        tagValue = tagValue + item.value + ','
      })
      tagName = tagName.substr(0, tagName.length - 1)
      tagValue = tagValue.substr(0, tagValue.length - 1)
      if (!that.ruleForm1.groupId) {
        that.ruleForm1.groupId = that.groupId
      }
      return {
        name: that.ruleForm1.name,
        host: JSON.stringify(host),
        loadBalancing: that.ruleForm1.loadBalancing,
        tagName: tagName,
        tagValue: tagValue,
        path: that.ruleForm1.path,
        protocol: that.ruleForm2.protocol,
        method: method,
        formatType: that.ruleForm2.formatType,
        body: body,
        params: JSON.stringify(that.ruleForm2.params),
        response: JSON.stringify(that.ruleForm2.response),
        constants: JSON.stringify(that.ruleForm3.constants),
        groupId: that.ruleForm1.groupId
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
        that.$message.success(response.data.msg)
        that.close()
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
        that.$message.success(response.data.msg)
        that.close()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    close() {
      const that = this
      that.$router.push({
        path: '/apiInfo',
        query: {
          groupId: that.groupId
        }
      })
    },

    //页面1
    addElement() {
      let host = {
        key: this.hostList.length,
        value: ''
      }
      this.hostList.push(host)
    },

    async getSourceApi() {
      const that = this
      try {
        that.isSubmitLoading = true
        const res = await findById({ id: that.id })
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          let data = res.data.data

          that.ruleForm1.name = data.name
          that.ruleForm1.loadBalancing = data.loadBalancing
          let host = JSON.parse(data.host)

          that.ruleForm1.host = host[0].target
          if (that.ruleForm1.loadBalancing) {
            that.host2 = host[1].target
            if (host.length > 2) {
              for (let i = 2; i < host.length; i++) {
                let item = {
                  key: i - 2,
                  value: host[i].target,
                }
                that.hostList.push(item)
              }
            }
          }

          let tempName = data.tagName
          let tempValue = data.tagValue
          if (tempName.indexOf(',') === -1) {
            this.metas = [{ key: tempName, value: tempValue }]
          } else {
            let metas = []
            let nameArr = tempName.split(',')
            let valueArr = tempValue.split(',')
            nameArr.forEach((item, index) => {
              metas.push({ key: item, value: valueArr[index] })
            })
            this.metas = metas
          }

          that.ruleForm1.path = data.path

          that.ruleForm2.protocol = data.protocol
          let method = data.method
          that.ruleForm2.method = method.split(',')

          that.ruleForm2.formatType = data.formatType

          if (that.ruleForm2.formatType === 'form-data') {
            that.bodyArray = JSON.parse(data.body)
          } else {
            that.bodyString = data.body
          }
          that.ruleForm2.params = JSON.parse(data.params)
          that.ruleForm2.response = JSON.parse(data.response)

          that.ruleForm3.constants = JSON.parse(data.constants)
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    rowClick(row) {
      this.ruleForm1.supplierName = row.name
      this.ruleForm1.supplierId = row.id

      this.isSupplierId = false
      this.$refs.popver.doClose()
    },

    deleteHost(key) {
      this.hostList.splice(key, 1);
      let hostTemp = this.hostList
      hostTemp.forEach((item, index) => {
        item.key = index
      })

      this.hostList = hostTemp
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
        this.ruleForm2.response = this.setLeaf(arr, 0, this.ruleForm2.response, response)
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

    //页面3
    addConstants() {
      let response = {
        id: this.ruleForm3.constants.length,
        name: "",
        value: "",
        type: "Parameter",
        description: ""
      }

      this.ruleForm3.constants.push(response)
    },

    deleteConstants(id) {
      this.ruleForm3.constants.splice(id, 1)
      let constantsTemp = this.ruleForm3.constants

      constantsTemp.forEach((item, index) => {
        item.id = index
      })
      this.ruleForm3.constants = constantsTemp
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
      const that = this
      const fileSuffix = file.name.substring(file.name.lastIndexOf(".") + 1);
      const whiteList = ['json', 'txt'];
      if (whiteList.indexOf(fileSuffix) === -1) {
        this.$message.error("上传文件只能是json和txt格式")
        return false
      }
      let reader = new FileReader();
      if (typeof FileReader === 'undefined') {
        this.$message.error('您的浏览器不支持FileReader接口')
        return false
      }
      reader.readAsText(file, "gb2312");
      reader.onload = function () {
        that.bodyString = reader.result
      }
      this.$refs.uploadText.abort()
    },

    insertMeta() {
      const that = this
      let item = {
        key: '',
        value: ''
      }
      that.metas.push(item)
    },

    removeMeta(i) {
      const that = this
      that.metas.splice(i, 1)
    },

    async remoteMethod(query) {
      if (query !== '') {
        let res = await findList({ name: query })
        if (res.data.code === 1) {
          this.tagInfos = res.data.data
        }
      }
    }
  }
};
</script>
<style scoped>
.main {
  height: 100%;
  width: 100%;
  padding: 0 30px;
  min-height: 600px;
}

.content {
  height: 100%;
  width: 90%;
  border: 1px solid #f0f0f0;
  border-radius: 5px;
  background-color: white;
  padding: 30px;
}
</style>
