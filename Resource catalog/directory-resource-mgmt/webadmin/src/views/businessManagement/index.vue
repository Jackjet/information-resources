<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">部门的权责清单树</span>
            <el-tooltip v-permission="['GET /admin/cataInfoTemp/read']" :content="treeExpansion ? '收起' : '展开'" effect="dark" placement="bottom">
              <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
            </el-tooltip>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :expand-on-click-node="false" :data="treeData" :props="defaultProps" highlight-current node-key="busiNo" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" @node-click="handleNodeClick">
                <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
                    <span class="overflowEllips">{{ node.label }}</span>
                  </el-tooltip>
                  <div />
                </span>
              </el-tree>
            </el-scrollbar>
          </div>
        </el-row>
      </el-col>

      <el-col ref="contentDomParents" :xl="20" :lg="19" class="tab_area">
        <div class="type_area">{{ rightTitle }}</div>
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="20">
              <el-col :span="20">
                <span style="margin-right:10px">是否产生业务资源</span>
                <el-select v-model="listQuery.filterCata" v-permission="['GET /admin/cataInfoTemp/list']" clearable placeholder="是否产生业务资源" class="searchInputWidth selectRadius" @change="handleFilter">
                  <el-option label="全部" class="filter-item " value="2" />
                  <el-option label="是" class="filter-item " value="1" />
                  <el-option label="否" class="filter-item " value="0" />
                </el-select>
                <el-input v-model="listQuery.name" clearable class="filter-item" style="width: 35%;" placeholder="职责/权责清单名称" @clear="handleFilter" />
                <el-button v-permission="['GET /admin/archBusi/list']" class="remove" icon="el-icon-search" @click="handleFilter">查询</el-button>
              </el-col>
              <el-col :span="20" style="margin-top:10px;">
                <el-button v-permission="['POST /admin/archBusi/saveOrUpdate']" class="add" size="mini" icon="el-icon-plus" @click="addMenu">新增</el-button>
                <el-button v-permission="['GET /admin/archBusi/export']" class="add" size="mini" @click="exportFile" :loading="downloadBtn">导出</el-button>
                <el-upload style="display: inline-block;margin-left: 10px;" ref="uploadDom" v-permission="['POST /admin/archBusi/upload']" :headers="headers" class="upload-demo" :on-exceed="handleExceed" :http-request="toUploadSuccess" action="auto" :on-progress="uploadProgress" :show-file-list="false" accept=".xls, .xlsx" :before-upload="beforeUpload">
                  <el-button class="add" size="mini" :loading="btnLoading">
                    导入
                  </el-button>
                </el-upload>
              </el-col>
            </el-row>
          </div>
          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column type="index" label="序号" />
            <el-table-column prop="busiNo" label="职责/权责编码" width="160" />
            <el-table-column prop="busiNm" label="职责/权责名称" width="150" show-overflow-tooltip>
            </el-table-column>
            <el-table-column prop="busiType" label="权责类别" width="100" />

            <el-table-column prop="pId" label="类型" width="50" :formatter="formatterBusiType" />

            <el-table-column label="操作" align="center" width="200" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['POST /admin/archBusi/saveOrUpdate']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/archBusi/deleteCataInfoRel']" content="移除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                  </span>
                </el-tooltip>
                <span v-permission="['GET /admin/archBusi/cataInfoPage']" class="product_resource_btn fl" @click="showDataReource(scope.row,0)" v-if="scope.row.pId!=0">产生的业务资源</span>
                <!--
                <span
                  v-permission="['GET /admin/archBusi/cataInfoPage']"
                  class="need_resource_btn fl"
                  @click="showDataReource(scope.row,1)"
                >需要的业务资源</span>
                -->
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>
        <!--新增权责清单-->
        <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="50%" :close-on-click-modal="false" @close="closedDialog('addbusinessForm','addbusinessForm')">
          <el-form ref="addbusinessForm" :model="addbusinessForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="部门名称：" label-width="140px" prop="departmentName">
              <span>{{ addbusinessForm.departmentName || addbusinessForm.orgNm }}</span>
            </el-form-item>
            <el-form-item label="职责：" label-width="140px" prop="name" v-if="!currentZhize">
              <span>{{ addbusinessForm.name || addbusinessForm.pBusiNm }}</span>
            </el-form-item>
            <el-form-item label="职责名称：" class="required_label" label-width="140px" prop="busiNm" v-if="currentZhize">
              <el-input v-model="addbusinessForm.busiNm" style="width:100%" placeholder="请输入职责名称" />
            </el-form-item>
            <el-form-item label="职责编码：" class="required_label" label-width="140px" v-if="currentZhize">
              <el-input v-model="addbusinessForm.busiNo" style="width:100%" placeholder="职责编码" disabled />
            </el-form-item>
            <el-form-item label="权责名称：" class="required_label" label-width="140px" prop="busiNm" v-if="!currentZhize">
              <el-input v-model="addbusinessForm.busiNm" style="width:100%" placeholder="请输入权责名称" />
            </el-form-item>
            <el-form-item label="权责编码：" class="required_label" label-width="140px" v-if="!currentZhize">
              <el-input v-model="addbusinessForm.busiNo" style="width:100%" placeholder="权责编码" disabled />
            </el-form-item>
            <el-form-item label="权责类别：" class="" label-width="140px" prop="busiType" v-if="!currentZhize">
              <el-select v-model="addbusinessForm.busiType" style="width:100%" placeholder="请选择权责类别">
                <el-option v-for="item in busiTypes" :key="item.label" :label="item.label" :value="item.label" />
              </el-select>
            </el-form-item>
            <el-form-item label="所支撑的应用系统：" class="required_label" label-width="140px">
              <el-input v-if="!addbusinessForm.appList || addbusinessForm.appList.length == 0" style="cursor: pointer;width:100%" placeholder="所支撑的应用系统" readonly suffix-icon="el-icon-plus" @click.native="showSystems" />

              <el-tag v-for="tag in addbusinessForm.appList" v-else :key="tag.appsysId" closable class="posTag" style="margin-right: 10px" size="small" type="success" :disable-transitions="false" @close="handleClose(tag)">
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="tag.appsysNm"><span>{{ tag.appsysNm }}</span></el-tooltip>
              </el-tag>

              <svg-icon v-if="addbusinessForm.appList && addbusinessForm.appList.length > 0" icon-class="add_circle_green" style="cursor: pointer" @click="showSystems" />
            </el-form-item>
            <el-form-item label="备注：" label-width="140px">
              <el-input v-model="addbusinessForm.remark" :rows="5" type="textarea" style="width:100%" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="addMenuDetail">保 存</el-button>
            <el-button size="mini" class="remove" @click="addWorkOrder=false">取 消</el-button>
          </div>
        </el-dialog>
        <!--删除权责清单-->
        <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="375px" :close-on-click-modal="false">
          <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
        </el-dialog>
        <!--新增权责清单中的支撑应用系统-->
        <el-dialog :modal-append-to-body="false" :title="'支撑的应用系统'" align="center" :visible.sync="addSystemDialog" width="50%" :close-on-click-modal="false" @close="closeAddDialog('searchRelatedResults','searchTotal','searchQuery')">
          <el-input v-model="searchQuery.appNoOrAppNm" class="filter_input" clearable style="width: 50%" placeholder="输入应用系统名称或代码" suffix-icon="el-icon-search" @clear="searchRelateds" />
          <el-button class="remove" size="mini" icon="el-icon-search" @click="searchRelateds">查询</el-button>
          <div class="search_result">
            <el-table :data="searchRelatedResults" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="RelatedResultsSelectionChange">
              <el-table-column type="selection" width="55" />
              <el-table-column prop="appsysNo" label="应用系统编码" width />
              <el-table-column prop="appsysNm" label="应用系统名称" width show-overflow-tooltip />
            </el-table>
            <pagination v-show="searchTotal>0" :layout="'prev,pager,next'" :total="searchTotal" :small="true" :background="false" :page.sync="searchQuery.current" :limit.sync="searchQuery.size" @pagination="getSearchData" />
          </div>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="saveaddSystems">保 存</el-button>
            <el-button size="mini" class="remove" @click="addSystemDialog=false">取 消</el-button>
          </div>
        </el-dialog>
        <!--产生/需要的业务资源-->
        <el-dialog :modal-append-to-body="false" :title="dataResourceTitle" align="center" :visible.sync="dataResourceDialog" width="60%" class="busi_management_dialog_one" :close-on-click-modal="false" @close="closeAddDialog('dataResourceResults','dataResourceTotal','dataResourceQuery')">
          <div class="data_resource_name">
            <span style="display: inline-block;">权责清单名称：</span>
            <span>{{ dataResouceForm.busiNm }}</span>
          </div>
          <div class="add_btn_area">
            <span style="display: inline-block;width: 120px">信息资源列表</span>
            <span>
              <el-button v-permission="['GET /admin/archBusi/getCataInfoPage']" size="mini" class="remove" icon="el-icon-plus" @click="addDataResouce">新增</el-button>
            </span>
          </div>
          <div class="search_result">
            <el-table :data="dataResourceResults" stripe :header-cell-style="getRowClass" style="width: 100%">
              <el-table-column prop="uviewNo" label="信息资源代码" width />
              <el-table-column prop="uviewNm" label="信息资源名称" width show-overflow-tooltip />
              <el-table-column prop="orgNm" label="提供单位" width show-overflow-tooltip>
                <template slot-scope="scope">
                  {{ scope.row.orgNm || scope.row.provOrgCode }}
                </template>
              </el-table-column>
              <el-table-column label="操作" fixed="right">
                <template slot-scope="scope">
                  <el-tooltip v-permission="['GET /admin/archBusi/detail']" content="详情" effect="light" :open-delay="1000" placement="bottom">
                    <span class="tr_detail_icon fl">
                      <svg-icon icon-class="detail_sel" class-name @click="delDataResourcesDetail(scope.row)" />
                    </span>
                  </el-tooltip>
                  <el-tooltip v-permission="['GET /admin/archBusi/delete']" content="删除" effect="light" :open-delay="1000" placement="bottom">
                    <span class="tr_del_icon fl">
                      <svg-icon icon-class="tr_del" class-name @click="delDataResources(scope.row)" />
                    </span>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="dataResourceTotal>0" :layout="'prev,pager,next'" :total="dataResourceTotal" :small="true" :background="false" :page.sync="dataResourceQuery.current" :limit.sync="dataResourceQuery.size" @pagination="getDataResourceData" />
          </div>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="dataResourceDialog=false">关闭</el-button>
          </div>
        </el-dialog>
        <!--新增产生/需要的业务资源-->
        <el-dialog :modal-append-to-body="false" :title="dataResourceAddTitle" align="center" :visible.sync="addDataResouceDialog" width="60%" :close-on-click-modal="false" @close="closeAddDialog('searchRelatedResults1','searchTotal1','searchQuery1')">
          <div class="data_resource_name">
            <span style="display: inline-block;">权责清单名称：</span>
            <span>{{ dataResouceForm.busiNm }}</span>
          </div>
          <div class="add_btn_area">
            <el-input v-model="searchQuery1.cataName" class="filter_input" style="width: 50%" clearable placeholder="输入信息资源名称" suffix-icon="el-icon-search" @clear="handleFilter1" />
            <el-button class="remove" size="mini" icon="el-icon-search" @click="handleFilter1">查询</el-button>
          </div>
          <div class="search_result">
            <el-table :data="searchRelatedResults1" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="RelatedResultsSelectionChange1">
              <el-table-column type="selection" width="55" />
              <el-table-column prop="uviewNo" label="信息资源代码" width />
              <el-table-column prop="uviewNm" label="信息资源名称" width show-overflow-tooltip />
              <el-table-column prop="orgNm" label="提供单位" width show-overflow-tooltip>
                <template slot-scope="scope">
                  {{ scope.row.orgNm || scope.row.provOrgCode }}
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="searchTotal1>0" :layout="'prev,pager,next'" :total="searchTotal1" :small="true" :background="false" :page.sync="searchQuery1.current" :limit.sync="searchQuery1.size" @pagination="getAddDataResouces" />
          </div>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="confirmAddDataResource">确认</el-button>
            <el-button size="mini" class="remove" @click="addDataResouceDialog=false">取 消</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import Hamburger from '@/components/Hamburger'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import {
  getAppPage,
  getBusinessTree,
  getBusinessList,
  saveOrUpdate,
  getDataResourceList,
  getAddCataInfo,
  delBusiness,
  deleteCataInfoRel,
  getBusinessDetail,
  getBusinessNo,
  relDataResource,
  upload
} from '@/api/businessManagement'
import { getDictByType } from '@/api/departmentRes'

export default {
  name: 'BusinessManagement',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      downloadBtn: false,
      addSystemDialog: false,
      serviceObjs: [],
      busiTypes: [{ 'label': '行政处罚' },
      { 'label': '行政确认' },
      { 'label': '行政许可' },
      { 'label': '行政征收' },
      { 'label': '行政检查' },
      { 'label': '行政给付' },
      { 'label': '行政强制' },
      { 'label': '行政奖励' },
      { 'label': '卫生从业人员管理' },
      { 'label': '其他类别' }],
      addbusinessForm: {},
      dialogTitle: '新增权责清单',
      addWorkOrder: false,
      listQuery: {
        id: '',
        current: 1,
        size: 10,
        name: '',
        isDept: 0,
        filterCata: '2'
      },
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加
        busiNm: [
          { required: true, message: '请填写职责/权责名称', trigger: 'blur' }
        ],
        busiType: [
          { required: true, message: '请选择权责类别', trigger: 'change' }
        ]
        /* serviceObj: [
          { required: true, message: "请选择服务对象", trigger: "change" }
        ]*/
      },
      defaultProps: {
        children: 'children',
        label: 'busiNm' || 'orgNm'
      },
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      rightTitle: '',
      tipStr: '确认删除所选权责清单吗？',
      delDialog: false,
      dataResourceResults: [],
      dataResourceDialog: false,
      dataResourceTitle: '',
      dataResourceTotal: 0,
      dataResourceQuery: {
        current: 1,
        size: 5
      },
      dataResouceForm: {},
      searchQuery1: {
        cataName: '',
        current: 1,
        size: 5
      },
      searchTotal1: 0,
      searchRelatedResults1: [],
      type: '0',
      dataResourceAddTitle: '',
      addDataResouceDialog: false,
      addedDataResources: [],
      businessId: '',
      currentZhize: false

    }
  },
  created() {
    this.getTreeData()
    // getDictByType({ type: 'service_obj' }).then(res => {
    //   if (res.data.errno === 0) {
    //     this.serviceObjs = res.data.data
    //   }
    // })
  },
  methods: {
    handleClose(tag) {
      this.addbusinessForm.appList.splice(
        this.addbusinessForm.appList.indexOf(tag),
        1
      )
    },
    addMenuDetail() {
      this.$refs.addbusinessForm.validate(valid => {
        if (valid) {
          const busis = []
          if (
            this.addbusinessForm.appList &&
            this.addbusinessForm.appList.length > 0
          ) {
            this.addbusinessForm.appList.forEach(item => {
              if (item.id || item.appsysId) {
                busis.push(item.id || item.appsysId)
              }
            })
          }
          const sendData = Object.assign({}, this.addbusinessForm, {
            deptId:
              this.addbusinessForm.deptId ||
              this.$refs.treeDom.getCurrentNode().deptId ||
              this.$refs.treeDom.getCurrentNode().orgId ||
              '',
            pId: this.$refs.treeDom.getCurrentNode().orgId
              ? 0
              : this.$refs.treeDom.getCurrentNode().busiId,
            appIds: busis
          })
          saveOrUpdate(sendData)
            .then(res => {
              this.$notify.success(`${this.dialogTitle}成功`)
              this.addWorkOrder = false
              this.getTableData()
              this.currentNodekey = this.$refs.treeDom.getCurrentKey()
              this.getTreeData()
            })
            .catch(err => {
              this.$message.error(err.data.errmsg || '出错了')
            })
        }
      })
    },
    addMenu() {
      var currentNode = this.$refs.treeDom.getCurrentNode()
      if (!currentNode.busiNo) {
        this.$message.error('请选择一个部门或权责')
        return
      }
      if (currentNode.pId == 0) {
        this.dialogTitle = '新增权责'
        this.currentZhize = false
      } else {
        this.dialogTitle = '新增职责'
        this.currentZhize = true
      }

      // 新增获取权责清单编码
      getBusinessNo({
        deptId:
          this.$refs.treeDom.getCurrentNode().deptId ||
          this.$refs.treeDom.getCurrentNode().orgId ||
          '',
        // 当前选择是否是部门 y：pid='' N:pid为当前的busiid 添加到当前的子集
        pId: this.$refs.treeDom.getCurrentNode().orgId
          ? 0
          : this.$refs.treeDom.getCurrentNode().busiId
      }).then(res => {
        if (res.data.errno === 0) {
          this.addbusinessForm = Object.assign(
            {},
            this.addbusinessForm,
            res.data.data
          )
          this.getBusinessAddForm('addbusinessForm')
          this.addWorkOrder = true
        }
      })
    },
    toUploadSuccess(file, fileList) {
      this.btnLoading = true
      const formData = new FormData()
      formData.append('file', file.file)
      upload(formData)
        .then(res => {
          if (res.data.errno == 0) {
            this.btnLoading = false
            this.$notify.success('导入成功')
            this.getTreeData()
            this.$refs.uploadDom.clearFiles()
            this.fileList = []
          }
        })
        .catch(err => {
          this.btnLoading = false
          this.$refs.uploadDom.clearFiles()
          this.fileList = []
          this.$message.error(err.data.errmsg || '出错了')
        })
      console.log(file)
    },
    exportFile() {
      this.downloadBtn = true
      this.downloadExport()
    },
    downloadExport() {
      var _this = this
      const url = location.protocol + '//' + window.location.host + '/admin/archBusi/export'
      var iframe = document.createElement('iframe')
      iframe.src = url
      iframe.id = 'iframedownload'
      document.body.appendChild(iframe)
      iframe.style.display = 'none'
      iframe.onload = function () {
        console.log('start downloading...')
        document.body.removeAttribute(iframe)
      }
      var timer = setInterval(function () {
        iframe = document.getElementById('iframedownload')
        var iframeDoc = iframe.contentDocument || iframe.contentWindow.document
        console.log(iframeDoc)
        // Check if loading is complete
        if (iframeDoc.readyState == 'complete' || iframeDoc.readyState == 'interactive') {
          clearInterval(timer)
          setTimeout(() => {
            _this.downloadBtn = false
            _this.timeRange = null
            _this.$notify.success('权责信息导出成功！')
            _this.handleFilter()
          }, 3000)
        }
      }, 10)
    },
    showCodenIfo(data) {
      if (data.pId == 0) {
        this.currentZhize = true
      } else {
        this.currentZhize = false
      }
      getBusinessDetail({ busiId: data.busiId }).then(res => {
        if (res.data.errno === 0) {
          this.getBusinessAddForm('addbusinessForm')
          this.addbusinessForm = Object.assign({}, res.data.data)
          this.dialogTitle = '编辑'
          this.addWorkOrder = true
        }
      })
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },
    handleFilter1() {
      this.searchQuery1.current = 1
      this.getAddDataResouces()
    },
    getTableData() {
      this.tableLoading = true
      this.listQuery.id = this.$refs.treeDom.getCurrentNode().orgId
        ? this.$refs.treeDom.getCurrentNode().orgId
        : this.$refs.treeDom.getCurrentNode().busiId
      this.listQuery.isDept = this.$refs.treeDom.getCurrentNode().orgId ? 0 : 1
      getBusinessList(this.listQuery)
        .then(res => {
          if (res.data.errno === 0) {
            if (
              res.data.data &&
              res.data.data.records &&
              res.data.data.records.length > 0
            ) {
              this.tableData = res.data.data.records
            } else {
              this.tableData = []
            }
            this.total = res.data.data.total || 0
          }
          this.tableLoading = false
        })
        .catch(err => {
          this.tableLoading = false
        })
    },
    getTreeData() {
      this.treeLoading = true
      getBusinessTree()
        .then(res => {
          if (res.data.errno === 0) {
            this.treeData = res.data.data
            this.currentNodekey = this.currentNodekey
              ? this.currentNodekey
              : this.treeData[0].children[0].busiNo
            this.expandedkeys = []
            // this.expandedkeys.push(this.currentNodekey ? this.currentNodekey : this.treeData[0].busiNo);
            this.expandedkeys.push(
              this.currentNodekey
                ? this.currentNodekey
                : this.treeData[0].busiNo
            )
            setTimeout(() => {
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
              console.log(this.$refs.treeDom.getCurrentNode())
              this.rightTitle = this.$refs.treeDom.getCurrentNode().busiNm
              this.getTableData()
            }, 20)
          }
          this.treeLoading = false
        })
        .catch(err => {
          this.treeLoading = false
        })
    },
    handleNodeClick(data) {
      if (!data.busiNo || data.busiNo == '0') {
        this.$refs.treeDom.setCurrentKey(this.currentNodekey)
        return
      }
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().busiNo
      this.rightTitle = data.busiNm
      this.listQuery.current = 1
      this.getTableData()
    },
    // 选择所支撑的应用系统
    RelatedResultsSelectionChange(val) {
      this.choosedRelatedResults = val
    },
    // 选择产生的支援
    RelatedResultsSelectionChange1(val) {
      this.addedDataResources = val
    },
    searchRelateds() {
      this.searchQuery.current = 1
      this.getSearchData()
    },
    searchDataResource() {
      this.getAddDataResouces()
    },
    getSearchData() {
      // 查询所支撑的业务系统
      const sendData = Object.assign({}, this.searchQuery, {
        orgId:
          this.addbusinessForm.deptId ||
          this.$refs.treeDom.getCurrentNode().deptId ||
          this.$refs.treeDom.getCurrentNode().orgId,
        busiId: this.addbusinessForm.busiId || ''
      })
      getAppPage(sendData).then(res => {
        if (res.data.errno === 0) {
          this.searchRelatedResults = res.data.data.records
          this.searchTotal = res.data.data.total
        }
      })
    },
    getAddDataResouces() {
      if (this.type == 0) {
        this.dataResourceAddTitle = '选择产生的信息资源'
      } else {
        this.dataResourceAddTitle = '选择需要的信息资源'
      }
      const filterData = Object.assign({}, this.searchQuery1, {
        type: this.type,
        orgId:
          this.addbusinessForm.deptId ||
          this.$refs.treeDom.getCurrentNode().deptId ||
          this.$refs.treeDom.getCurrentNode().orgId,
        busiId: this.businessId * 1 || ''
      })
      getAddCataInfo(filterData).then(res => {
        if (res.data.errno === 0) {
          this.searchRelatedResults1 = res.data.data.records
          this.searchTotal1 = res.data.data.total
        }
      })
    },
    saveaddSystems() {
      if (this.choosedRelatedResults.length == 0) {
        this.$message({
          message: '请选择所支撑的业务系统',
          type: 'error'
        })
        return
      }
      let flag = true
      this.choosedRelatedResults.forEach(item => {
        if (
          this.addbusinessForm.appList &&
          this.addbusinessForm.appList.length > 0
        ) {
          this.addbusinessForm.appList.forEach(item1 => {
            if (item1.appsysId == item.appsysId) {
              flag = false
              return false
            }
          })
        }
      })
      if (flag) {
        this.addSystemDialog = false
        this.addbusinessForm = Object.assign({}, this.addbusinessForm, {
          appList: this.choosedRelatedResults.concat(
            this.addbusinessForm.appList || []
          )
        })
      } else {
        this.$message({
          message: '所选择的应用系统中与已新增的应用系统有重复项',
          type: 'error'
        })
        return
      }
    },
    showSystems() {
      this.addSystemDialog = true
      this.searchQuery.current = 1
      this.getSearchData()
    },
    handleTabClick(tab, event) {
      console.log(tab, event)
    },
    handleSelectionChange(val) {
      console.log(val)
    },
    delRelated(data) {
      this.tipStr = '确认删除所选权责清单（包含所有下级权责清单）吗？'
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    delDataResources(data) {
      this.tipStr = '确认删除此信息资源么？'
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    delDataResourcesDetail(data) {
      const id = data.infoId
      if (this.type == 0) {
        // this.$router.push({path:`/Cataloging/infoCatalogDetail/${id}`})
        // provOrgId=1&provOrgName=天津市公安局&provOrgCode=01
        const routeUrl = this.$router.resolve({
          path: `/Cataloging/infoCatalogDetail/${id}`,
          query: {
            provOrgId: data.provOrgId,
            provOrgName: data.orgNm,
            provOrgCode: data.provOrgCode
          }
        })
        window.open(routeUrl.href, '_blank')
      } else {
        // this.$router.push({path:`/Cataloging/requirementCatalogDetail/${id}`})
        const routeUrl = this.$router.resolve({
          path: `/Cataloging/requirementCatalogDetail/${id}`
        })
        window.open(routeUrl.href, '_blank')
      }
    },
    confirmDelete() {
      console.log(this.tempDelData)
      if (this.dataResourceDialog != true) {
        // 删除信息资源
        delBusiness({
          busiId: this.tempDelData.busiId
        }).then(res => {
          if (res.data.errno == 0) {
            this.$notify.success('删除成功')
            this.delDialog = false
            this.tempDelData = {}
            this.getTableData()
            this.currentNodekey = this.$refs.treeDom.getCurrentKey()
            this.getTreeData()
          }
        }).catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
      } else {
        // 删除权责清单
        deleteCataInfoRel({
          id: this.tempDelData.id,
          type: this.type
        }).then(res => {
          if (res.data.errno == 0) {
            this.$notify.success('删除成功')
            this.delDialog = false
            this.tempDelData = {}
            this.getDataResourceData()
          }
        }).catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
      }
    },
    showDataReource(data, type) {
      this.type = type
      const businessId = data.busiId
      this.businessId = businessId
      this.dataResouceForm = Object.assign({}, data)
      this.dataResourceQuery.current = 1
      // todo获取资源列表 type：0 产生的业务资源 1 需要的业务资源
      if (type == 0) {
        this.dataResourceTitle = '产生的信息资源'
      } else {
        this.dataResourceTitle = '需要的信息资源'
      }
      this.getDataResourceData()
    },
    getDataResourceData(businessId) {
      // 获取资源列表 type：0 产生的业务资源 1 需要的业务资源
      const sendData = Object.assign({}, this.dataResourceQuery, {
        type: this.type,
        busiId: this.businessId || ''
      })
      console.log(sendData)
      getDataResourceList(sendData).then(res => {
        if (res.data.errno === 0) {
          this.dataResourceResults = res.data.data.records
          this.dataResourceTotal = res.data.data.total
          this.dataResourceDialog = true
        }
      })
    },
    addDataResouce() {
      this.type == 0
        ? (this.dataResourceAddTitle = '选择产生的信息资源')
        : (this.dataResourceAddTitle = '选择需要的信息资源')
      this.searchQuery1.cataName = ''
      this.getAddDataResouces()
      this.addDataResouceDialog = true
    },
    confirmAddDataResource() {
      const sendData = {}
      sendData.busId = this.businessId
      sendData.type = this.type
      sendData.infoIds = []
      this.addedDataResources.forEach(item => {
        sendData.infoIds.push(item.uviewId)
      })
      relDataResource(sendData).then(res => {
        if (res.data.errno === 0) {
          this.addDataResouceDialog = false
          this.dataResourceQuery.current = 1
          this.getDataResourceData()
        }
      })
    },
    formatterBusiType(row, column, cellValue, index) {
      return row.pId ? '权责' : '职责';
    }
  }
}
</script>

<style scoped>
.tree_area .el-tree {
  width: 100%;
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.add_btn_area {
  text-align: left;
  margin-bottom: 10px;
}

.data_resource_name {
  margin-bottom: 20px;
  text-align: left;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  line-height: 22px;
  color: rgba(36, 36, 36, 1);
}

.product_resource_btn {
  display: inline-block;
  height: 20px;
  line-height: 20px;
  color: #1f3365;
  padding: 0px 5px;
  border: 1px solid rgba(31, 51, 101, 1);
  border-radius: 4px;
  cursor: pointer;
  margin-right: 14px;
  font-size: 12px;
}

.need_resource_btn {
  font-size: 12px;
  display: inline-block;
  height: 20px;
  line-height: 20px;
  border: 1px solid #5abd8c;
  color: #5abd8c;
  padding: 0px 5px;
  border-radius: 4px;
  cursor: pointer;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.department_content {
  background: #fff;
  border: 1px solid rgba(230, 229, 234, 1);
  border-radius: 6px;
}

.filter_text {
  line-height: 36px;
  margin-right: 6px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}

.tree_title {
  padding-left: 30px;
  padding-right: 1vh;
  height: 60px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  line-height: 60px;
  opacity: 1;
}

.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}

.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
}

.tree_content {
  padding-top: 24px;
  padding-left: 20px;
  padding-bottom: 24px;
  box-sizing: border-box;
}

.tree_city {
  margin-right: 12px;
  margin-left: 6px;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}

.tree_desc,
.type_area {
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
}

.tree_area {
  background: #f9f9f9;
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}
.tab_area {
  height: 100%;
}

.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}

.operate_area {
  padding: 0 45px;
}

.related_btns {
  margin-top: 15px;
}

.tr_detail_icon {
  color: #172853;
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  text-align: center;
  margin-right: 14px;
  cursor: pointer;
}

.tr_detail_icon svg {
  cursor: pointer;
  width: 20px;
  height: 20px;
}

.tr_del_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50% 50%;
  border: 2px solid #172853;
  border-radius: 50%;
  opacity: 1;
  text-align: center;
  position: relative;
  cursor: pointer;
  margin-right: 14px;
}

.tr_del_icon svg {
  cursor: pointer;
  width: 10px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}

.level {
  width: 16px;
  display: inline-block;
  height: 16px;
  background: #1f3365;
  color: #fff;
  font-size: 14px;
  text-align: center;
  line-height: 16px;
  border-radius: 4px;
  margin-right: 4px;
}
</style>

