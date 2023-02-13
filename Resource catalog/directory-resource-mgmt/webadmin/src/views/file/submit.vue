<template>
  <div class="app-container mt20 department_content">
    <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
      <el-row>
        <div class="tree_title">
          <span class="tree_desc">部门树</span>
          <el-tooltip :content="treeExpansion ? '收起' : '展开'" effect="light" :open-delay="1000" placement="bottom">
            <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
          </el-tooltip>
        </div>
      </el-row>
      <el-row class="tree_content_parent">
        <div class="tree_content">
          <el-scrollbar class="scrollbar_device">
            <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" highlight-current node-key="orgId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :expand-on-click-node="false" @node-click="handleNodeClick">
              <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
                  <span>
                    <span class>{{ data.typType }}</span>
                    {{ data.typCd }} {{ node.label }}
                  </span>
                </el-tooltip>
              </span>
            </el-tree>
          </el-scrollbar>
        </div>
      </el-row>
    </el-col>
    <el-col ref="contentDomParents" :xl="20" :lg="19" class="tab_area">
      <div class="type_area">文件上报</div>
      <div class="operate_area">
        <!-- 查询和其他操作 -->
        <div class="filter-container" style="margin-top: 15px;">
          <el-row>
            <el-col :span="24">
              <el-input v-model="listQuery.name" style="width:175px" clearable class="filter-item" placeholder="请输入资源目录名称" @clear="handleFilter" />
              <!--  <span class="filter_text" v-if="!isDeptAdmin">提交部门</span>
              <el-select
                v-if="!isDeptAdmin"
                v-model="listQuery.orgId"
                placeholder="请选择部门"
                class="filter-item"
                @change="handleFilter"
              >
                <el-option
                  v-for="item in deptTrees"
                  :key="item.orgId"
                  :label="item.orgNm"
                  :value="item.orgId"
                />
              </el-select>-->
              <el-select v-model="listQuery.status" style="width:115px" clearable placeholder="审核状态" class="filter-item" @change="handleFilter">
                <el-option v-for="item in statuss" :key="item.value" :label="item.name" :value="item.value" />
              </el-select>
              <span class="filter_text">报送日期</span>
              <el-date-picker v-model="timeRange" style=";width: 250px;margin-right: 0" class="filter-item" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="起始日期" end-placeholder="截至日期" @change="getList" />
              <el-button v-permission="['GET /admin/fileUploadRel/list1']" class="remove fr" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>

            </el-col>
          </el-row>
          <el-row>
            <el-button v-permission="['POST /admin/fileUploadRel/save']" style="margin-left: 10px;position: relative;left: -10px" class="add" size="mini" @click="addUpload(0)">
              <svg-icon icon-class="i_upload" style="margin-right: 5px;position: relative;top:1px;font-size: 14px" />上传文件
            </el-button>
          </el-row>
        </div>
        <!-- 查询结果 -->
        <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" stripe :header-cell-style="getRowClass">
          <el-table-column label="资源目录" prop="uviewNm" show-overflow-tooltip />
          <el-table-column label="文件名称" prop="fileName" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.fileName }}
              <!--<span><a class="link" style="text-decoration: underline;color: #1f3365" :href="scope.row.url" download target="_blank">{{scope.row.fileName || '暂无'}}</a></span>-->
            </template>
          </el-table-column>
          <el-table-column label="文件格式" prop="format" width="100" />
          <el-table-column label="上传时间" prop="addTime" />
          <el-table-column label="审核状态" prop="status">
            <template slot-scope="scope">
              <div v-if="scope.row.status == 0">等待审核</div>
              <div v-if="scope.row.status == 1">通过</div>
              <div v-if="scope.row.status == 2" style="color:#E65555;">驳回</div>
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="result" width="100px" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-tooltip v-if="scope.row.status !== '0 '" v-permission="['POST /admin/fileUploadRel/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                <span class="tr_detail_icon fl">
                  <svg-icon icon-class="catalog_edit" class-name @click="addUpload(1,scope.row)" />
                </span>
              </el-tooltip>
              <el-tooltip v-permission="['GET /admin/fileUploadRel/detail']" content="详情" effect="light" :open-delay="1000" placement="bottom">
                <span class="tr_detail_icon fl">
                  <svg-icon icon-class="detail" class-name @click="getDetail(scope.row)" />
                </span>
              </el-tooltip>
              <el-tooltip v-if="scope.row.status !== '0 '" v-permission="['POST /admin/fileUploadRel/delete']" content="删除" effect="light" :open-delay="1000" placement="bottom">
                <span class="tr_del_icon fl">
                  <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                </span>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" :layout="'prev,pager,next'" @pagination="getList" />
      </div>
    </el-col>

    <!--上传文件弹窗-->
    <el-dialog v-if="addWorkOrder" :modal-append-to-body="false" title="上传文件" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog1('uploadForm','uploadForm')">
      <el-form ref="uploadForm" :model="uploadForm" align="center" :rules="uploadRules" class="demo-ruleForm" label-position="left">
        <el-form-item ref="subjectNameDom" label="资源目录：" class="required_label" label-width="100px" prop="subjectName">
          <el-input v-model="uploadForm.subjectName" placeholder="请选择信息资源" disabled>
            <el-button slot="append" class="selectBtn" @click="showInfoCatas">选择</el-button>
          </el-input>
        </el-form-item>
        <!--<el-form-item label="选择部门：" v-if="!this.isDeptAdmin" class="required_label" label-width="100px" prop="deptId">
          <el-select
            v-model="uploadForm.deptId"
            placeholder="请选择部门"
            style="width: 100%;"
          >
            <el-option
              v-for="item in deptTrees"
              :key="item.orgId"
              :label="item.orgNm"
              :value="item.orgId"
            />
          </el-select>
        </el-form-item>-->
        <el-form-item ref="uploadItemDom" label="数据文件：" class="required_label" label-width="100px" prop="fileName2" align="left">
          <el-upload ref="uploadDom" :headers="headers" :action="uploadPath" :show-file-list="true" :limit="1" :on-progress="uploadProgress" :on-exceed="handleExceed" :before-upload="beforeUpload" :on-success="uploadSuccess" :file-list="fileList" :on-remove="removeFlieList" accept=".zip, .rar, .pdf, .txt, .doc, .docx, .xls, .xlsx">
            <el-input v-model="uploadForm.fileName2" class="fl" readonly :disabled="true" placeholder="文件名称" style="width: 205px" />
            <el-button class="remove" size="mini" icon="el-icon-search" style="margin-left: 22px" :loading="btnLoading">浏览</el-button>
            <div slot="tip" class="el-upload__tip">
              <p style="text-align: left">上传说明：</p>
              <p style="text-align: left">1、只能上传一个文件，多个文件时请打包上传。</p>
              <p style="text-align: left">2、文件格式包括zip、rar、pdf、txt、doc、docx、xls、 xlsx</p>
              <p style="text-align: left">3、文件大小不超：40M</p>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item ref="fileNameDom" label="文件名称：" class="required_label" label-width="100px" prop="fileName">
          <el-input v-model="uploadForm.fileName" readonly style="width:100%" placeholder="文件名称" />
        </el-form-item>
        <el-form-item label="文件说明：" label-width="100px">
          <el-input v-model="uploadForm.fileRemark" :rows="5" type="textarea" style="width:100%" placeholder="文件说明" />
        </el-form-item>
      </el-form>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="confirmUpload">保 存</el-button>
        <el-button size="mini" class="remove" @click="addWorkOrder=false;closedDialog1('uploadForm','uploadForm')">取 消</el-button>
      </div>
    </el-dialog>
    <!--文件详情弹窗-->
    <el-dialog v-if="detailWorkOrder" :modal-append-to-body="false" class="detail_dialog" align="center" title="上报详情" :visible.sync="detailWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog1('detailForm','detailForm')">
      <el-form ref="detailForm" :model="detailForm" align="left" class="demo-ruleForm" label-position="left">
        <el-form-item label="资源目录：" class="required_label" label-width="100px" prop="subjectName">
          <span>{{ detailForm.uviewNm || '暂无' }}</span>
        </el-form-item>
        <el-form-item label="提交部门：" class="required_label" label-width="100px" prop="subjectName">
          <span>{{ detailForm.orgNm || '暂无' }}</span>
        </el-form-item>
        <el-form-item label="数据文件：" class="required_label" label-width="100px" prop="fileName" align="left">
          <span>
            <a class="link" style="text-decoration: underline;color: #1f3365" href="javascript:;" @click="downloadFileIframe(detailForm.url)">{{ detailForm.name || '暂无' }}</a>
          </span>
        </el-form-item>
        <el-form-item label="文件名称：" class="required_label" label-width="100px" prop="fileName">
          <span>{{ detailForm.fileName || '暂无' }}</span>
        </el-form-item>
        <el-form-item label="文件说明：" label-width="100px">
          <span>{{ detailForm.fileRemark || '暂无' }}</span>
        </el-form-item>
        <hr>
        <p v-if="detailForm.status != 0">审核意见</p>
        <el-form-item v-if="detailForm.status != 0" label="审核人：" class="required_label" label-width="100px" prop="fileName" align="left">
          <span>
            {{ detailForm.updateby || '暂无' }}
          </span>
        </el-form-item>
        <el-form-item v-if="detailForm.status != 0" label="审核时间：" class="required_label" label-width="100px" prop="fileName">
          <span>{{ detailForm.updatetime || '暂无' }}</span>
        </el-form-item>
        <el-form-item v-if="detailForm.status != 0" label="审核意向：" label-width="100px">
          <span>{{ detailForm.statusNm || '暂无' }}</span>
        </el-form-item>
        <el-form-item v-if="detailForm.comment" label="审核意见：" label-width="100px">
          <span>{{ detailForm.comment || '暂无' }}</span>
        </el-form-item>
      </el-form>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="remove" @click="detailWorkOrder=false">关闭</el-button>
      </div>
    </el-dialog>
    <!--选择信息资源目录弹窗-->
    <el-dialog :modal-append-to-body="false" :title="'选择资源目录'" align="center" :visible.sync="addInfoCataDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog('searchRelatedResults','search1Total','searchQuery1','uviewNm')">
      <el-input v-model="searchQuery1.uviewNm" class="filter_input" clearable style="width: 50%" placeholder="请输入信息资源名称" suffix-icon="el-icon-search" @clear="searchRelateds" />
      <el-button class="remove" size="mini" icon="el-icon-search" @click="searchRelateds">查询</el-button>
      <div class="search_result">
        <el-table v-loading="tableLoading" :data="searchRelatedResults" stripe :header-cell-style="getRowClass" style="width: 100%">
          <el-table-column prop="uviewNm" label="信息资源名称" width />
          <el-table-column prop="provOrgName" label="资源提供单位" width />
          <el-table-column prop="shareLv" label="共享级别" width>
            <template slot-scope="scope">{{ shareObjs[scope.row.shareLv] }}</template>
          </el-table-column>
          <el-table-column label="操作" width>
            <template slot-scope="scope">
              <a href="javavscript:;" style="text-decoration: underline;color: #1F3365" @click="chooseOneInfo(scope.row)">选择</a>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="search1Total>0" :layout="'prev,pager,next'" :total="search1Total" :small="true" :background="false" :page.sync="searchQuery1.page" :limit.sync="searchQuery1.limit" @pagination="getSearchData" />
      </div>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="saveaddSystems">保 存</el-button>
        <el-button size="mini" class="remove" @click="addInfoCataDialog=false">取 消</el-button>
      </div>
    </el-dialog>
    <!--删除-->
    <el-dialog :modal-append-to-body="false" :title="'删除'" class="del_dialog" :visible.sync="delDialog" align="center" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from '@/utils/auth'
import {
  saveUploadForm,
  uploadEdit,
  getUploadDetail,
  getUploadList,
  deleteUpload
} from '@/api/file'
import { getTableList } from '@/api/resCatalog'
import { readminAdmin } from '@/api/admin'
import {
  getDictByType,
  getDepartmentTree,
  getDeptOrgTree
} from '@/api/departmentRes'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from 'vuex'
import mixinJs from '@/utils/mixin'
import Hamburger from '@/components/Hamburger'
export default {
  name: 'FileSubmit',
  components: { Pagination, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      currentNodekey: '',
      expandedkeys: [],
      treeData: [],
      addInfoCataDialog: false,
      tipStr: '确认删除吗？',
      tempDelData: {},
      delDialog: false,
      shareObjs: {},
      timeRange: null,
      listLoading: false,
      list: null,
      total: 0,
      listQuery: {
        current: 1,
        size: 10,
        startTime: '',
        endTime: '',
        name: '',
        status: '',
        deptId: ''
      },
      statuss: [
        { value: '0', name: '待审核' },
        { value: '1', name: '通过' },
        { value: '2', name: '驳回' }
      ],
      detailWorkOrder: false,
      addWorkOrder: false,
      uploadForm: {},
      uploadRules: {
        deptId: [{ required: true, message: '请选择部门', trigger: 'blur' }],
        subjectName: [
          { required: true, message: '请选择资源目录', trigger: 'change' }
        ],
        fileName: [
          { required: true, message: '请选择上传文件', trigger: 'change' }
        ],
        fileName2: [
          { required: true, message: '请选择上传文件', trigger: 'change' }
        ]
      },
      fileList: [],
      choosedRelatedResults: [],
      subjectId: '',
      subjectName: '',
      searchRelatedResults: [],
      searchQuery1: {
        limit: 5,
        page: 1,
        uviewNm: ''
      },
      search1Total: 0,
      deptId: '',
      isDeptAdmin: '',
      deptTrees: [],
      defaultProps: {
        children: 'children',
        label: 'orgNm'
      },
      type: '',
    }
  },
  computed: {
    ...mapGetters(['userid', 'propertyId', 'roles']),
    headers() {
      return {
        'X-Resourcecatalog-Admin-Token': getToken()
      }
    }
  },
  created() {
    // this.getList()
    this.getShareTypes()
    if (this.roles[0].indexOf('部门资源管理员') > -1) {
      this.isDeptAdmin = true
    } else {
      this.isDeptAdmin = false
    }
    this.getDeptTree()
    /* readminAdmin({id:this.userid}).then(res => {
                this.deptId = res.data.data.orgs[0].orgId
            })*/
    // console.log(this.userid, this.propertyId, this.roles);
  },
  methods: {
    handleNodeClick(data, e) {
      if (!data.orgId && this.isDeptAdmin) {
        this.$refs.treeDom.setCurrentKey(this.currentNodekey)
        return
      }
      let orgId = ''
      const _this = this
      if (e.parent.parent == null) {
        orgId = e.data.orgId
      } else {
        _resolve(e)
      }

      function _resolve(e) {
        for (const k in e) {
          if (k == 'parent') {
            if (e[k] != null && e[k].level != 1) {
              _resolve(e.parent)
            } else {
              orgId = e.data.orgId
            }
          }
        }
      }
      this.deptId = orgId == 0 ? '' : orgId
      this.listQuery.current = 1
      this.listQuery.deptId = this.deptId
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().orgId
      this.getList()
    },
    getDeptTree() {
      /* if(this.isDeptAdmin){
                    getDeptOrgTree().then(res => {
                        this.treeData = res.data
                        this.currentNodekey = this.treeData[0].children[0].orgId
                        this.expandedkeys.push(this.treeData[0].children[0].orgId)
                        setTimeout(() => {
                            this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来

                            this.getList()
                        }, 20)
                    })
                }else{*/
      getDepartmentTree({ isTop: 0 }).then(res => {
        this.treeData = res.data
        this.currentNodekey = this.treeData[0].children[0].orgId
        this.expandedkeys.push(this.treeData[0].children[0].orgId)
        this.deptId = this.treeData[0].children[0].orgId
        setTimeout(() => {
          this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
          this.listQuery.deptId = this.deptId
          this.getList()
        }, 20)
      })
      /* }*/
    },
    getShareTypes() {
      getDictByType({ type: 'share_type' }).then(response => {
        response.data.data.forEach(item => {
          this.shareObjs[item.value] = item.name
        })
      })
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    closedDialog1(attr, formName) {
      this.$refs[formName].resetFields()
      this[attr] = {}
      this.fileList = []
      this.$refs.selectTreeDom ? this.$refs.selectTreeDom.clearAll() : ''
    },
    getList() {
      this.listLoading = true
      if (this.timeRange) {
        this.listQuery.startTime = this.timeRange[0]
        this.listQuery.endTime = this.timeRange[1]
      } else {
        this.listQuery.startTime = this.listQuery.endTime = ''
      }
      getUploadList(this.listQuery)
        .then(res => {
          if (res.data.errno === 0) {
            this.list = res.data.data.records || []
            this.total = res.data.data.total
          }
          this.listLoading = false
        })
        .catch(err => {
          this.listLoading = false
        })
    },
    addUpload(type, data) {
      this.type = type
      if (type === 0) {
        // 新增
        if (this.deptId === '') {
          this.$message.error('请选择一个部门')
          return
        }
        this.addWorkOrder = true
      } else {
        // todo 编辑回显
        const id = data.id
        getUploadDetail({ id: id }).then(res => {
          if (res.data.errno === 0) {
            this.uploadForm = Object.assign({}, res.data.data, {
              subjectName: res.data.data.uviewNm,
              fileName2: res.data.data.name
            })
            this.fileList = []

            this.fileList.push({
              name: this.uploadForm.name,
              url: this.uploadForm.url
            })
            this.addWorkOrder = true
          }
        }).catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
      }
    },
    delRelated(data) {
      const that = this;
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.tempDelData = Object.assign({}, data)
        that.confirmDelete();
      }).catch(() => {
        return false
      })

    },
    //删除
    confirmDelete() {
      deleteUpload({ id: this.tempDelData.id }).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getList()
        }
      }).catch(err => {
        this.$message.error(err.data.errmsg || '出错了')
      })
    },
    uploadSuccess(response) {
      this.btnLoading = false
      console.log(response.data)
      const arr = response.data.name.split('.')
      this.uploadForm = Object.assign({}, this.uploadForm, {
        storageId: response.data.id * 1,
        fileName: arr.slice(0, arr.length - 1).join('.'),
        fileName2: response.data.name
      })
      this.$refs.uploadItemDom.clearValidate()
      this.$refs.fileNameDom.clearValidate()
    },
    removeFlieList(file, fileList) {
      this.uploadForm = Object.assign({}, this.uploadForm, {
        storageId: '',
        fileName: '',
        fileName2: ''
      })
    },
    getDetail(data) {
      const id = data.id
      getUploadDetail({ id: id }).then(res => {
        if (res.data.errno === 0) {
          this.detailForm = Object.assign({}, res.data.data)
          this.detailWorkOrder = true
        }
      })
    },
    confirmUpload() {
      this.$refs['uploadForm'].validate(valid => {
        if (valid) {
          const sendData = Object.assign({}, this.uploadForm, {
            deptId: this.deptId,
            subjectId: this.subjectId
              ? this.subjectId
              : this.uploadForm.uviewId
                ? this.uploadForm.uviewId
                : ''
          })
          if (this.type === 0) {
            saveUploadForm(sendData)
              .then(res => {
                if (res.data.errno === 0) {
                  this.$notify.success('文件上报成功')
                  this.addWorkOrder = false
                  this.closedDialog1('uploadForm', 'uploadForm')
                  this.getList()
                }
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          } else {
            uploadEdit(sendData)
              .then(res => {
                if (res.data.errno === 0) {
                  this.$notify.success('文件编辑成功')
                  this.addWorkOrder = false
                  this.closedDialog1('uploadForm', 'uploadForm')
                  this.getList()
                }
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        }
      })
    },
    searchRelateds() {
      this.searchQuery1.current = 1
      this.getSearchData()
    },
    getSearchData() {
      const sendData = Object.assign({}, this.searchQuery1, {
        provOrgId: this.deptId,
        typeId: '',
        auditStatus: '5'
      })
      getTableList(sendData)
        .then(response => {
          this.searchRelatedResults = response.data.data.records
          this.search1Total = response.data.data.total
          this.tableLoading = false
        })
        .catch(() => {
          this.list = []
          this.search1Total = 0
          this.tableLoading = false
        })
    },
    showInfoCatas() {
      // 选择信息资源目录操作
      this.getSearchData()
      setTimeout(() => {
        this.addInfoCataDialog = true
      }, 50)
      // 资源目录文本框不可编辑，点击“选择”弹出资源目录列表，显示当前登录用户所属部门拥有的已审核通过的目录，可根据目录名称模糊查询。
    },
    chooseOneInfo(data) {
      var arr = []
      arr.push(data)
      this.choosedRelatedResults = arr
      this.saveaddSystems()
    },
    saveaddSystems() {
      if (this.choosedRelatedResults.length === 0) {
        this.$message({
          message: '请选择您要关联的信息资源目录',
          type: 'error'
        })
        return
      }
      this.subjectId = this.choosedRelatedResults[0].uviewId
      this.subjectName = this.choosedRelatedResults[0].uviewNm
      this.uploadForm = Object.assign({}, this.uploadForm, {
        subjectId: this.subjectId * 1,
        subjectName: this.subjectName
      })
      this.$refs.subjectNameDom.clearValidate()
      this.addInfoCataDialog = false
    }
  }
}
</script>
<style>
.leftSpan {
  color: #172853;
  font-size: 18px;
}
.leftTree {
  border: 1px solid rgb(201, 201, 201);
  max-height: 400px;
  overflow-y: scroll;
}
.dialogFormItem {
  margin-bottom: 5px;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
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

.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tab_area {
  height: 100%;
}
.operate_area {
  padding: 0 45px;
}
.related_btns {
  margin-top: 10px;
  padding-bottom: 20px;
}
.add {
  background: #1f3365;
  min-width: 80px;
  color: #fff;
  height: 36px;
}
.remove {
  color: #1f3365;
  min-width: 80px;
  border-color: #1f3365;
  height: 36px;
}
.tr_detail_icon {
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
  border: 2px solid rgba(46, 78, 161, 1);
  border-radius: 50%;
  opacity: 1;
  text-align: center;
  position: relative;
  cursor: pointer;
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>
<style scoped>
.el-upload__tip > p {
  line-height: 20px;
  margin: 0;
}
</style>
<style scoped>
.el-upload--text {
  min-width: 350px;
}
.detail_dialog .el-form-item {
  margin-bottom: 0;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
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
  overflow: hidden;
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
.link:hover {
  color: #0e1c3e;
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

.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tab_area {
  height: 100%;
}
.operate_area {
  padding: 0 45px;
}
.related_btns {
  margin-top: 10px;
  padding-bottom: 20px;
}
.add {
  background: #1f3365;
  min-width: 80px;
  color: #fff;
  height: 36px;
}
.remove {
  color: #1f3365;
  min-width: 80px;
  border-color: #1f3365;
  height: 36px;
}
.tr_detail_icon {
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
.filter-item {
  float: left;
  margin-right: 15px;
}
.filter_text {
  float: left;
  line-height: 36px;
  margin-right: 6px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}
.tr_del_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50% 50%;
  border: 2px solid rgba(46, 78, 161, 1);
  border-radius: 50%;
  opacity: 1;
  text-align: center;
  position: relative;
  cursor: pointer;
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
</style>

