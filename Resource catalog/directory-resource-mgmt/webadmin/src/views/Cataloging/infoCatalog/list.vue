<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">信息资源分类</span>
            <el-tooltip :content="treeExpansion ? '收起' : '展开'" effect="light" :open-delay="1000" placement="bottom">
              <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
            </el-tooltip>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" :expand-on-click-node="false" highlight-current node-key="orgCd" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" @node-click="handleNodeClick">
                <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
                    <span class="overflowEllips"> {{ node.label }} </span>
                  </el-tooltip>
                  <div />
                </span>
              </el-tree>
            </el-scrollbar>
          </div>
        </el-row>
      </el-col>

      <el-col ref="contentDomParents" :xl="20" :lg="19" class="tab_area">
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="18">
              <el-col :span="24" style="margin-bottom:16px">
                <el-button v-permission="['GET /admin/cataInfoTemp/list']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="getTableData">查询
                </el-button>
                <el-input v-model="listQuery.uviewNo" v-permission="['GET /admin/cataInfoTemp/list']" clearable class="filter-item searchInputWidth" placeholder="信息资源代码" />
                <el-input v-model="listQuery.uviewNm" v-permission="['GET /admin/cataInfoTemp/list']" clearable class="filter-item searchInputWidth" placeholder="信息资源名称" />
                <!-- <el-select v-model="listQuery.provider" placeholder="请选择" class="searchInputWidth">
                    <el-option
                      v-for="item in providers"
                      :key="item.value"
                      :label="item.label"
                      class="filter-item"
                      :value="item.value">
                    </el-option>
                  </el-select> -->
                <el-select v-model="listQuery.pubSts" v-permission="['GET /admin/cataInfoTemp/list']" clearable placeholder="开放类型" class="searchInputWidth selectRadius" @change="handleFilter">
                  <el-option label="无条件开放" class="filter-item " value="01" />
                  <el-option label="有条件开放" class="filter-item " value="02" />
                  <el-option label="不予开放" class="filter-item " value="03" />
                </el-select>
                <el-select v-model="listQuery.shareLv" v-permission="['GET /admin/cataInfoTemp/list']" clearable placeholder="共享类型" class="searchInputWidth selectRadius" @change="handleFilter">
                  <el-option label="无条件共享" class="filter-item " value="01" />
                  <el-option label="有条件共享" class="filter-item " value="02" />
                  <el-option label="不予共享" class="filter-item " value="03" />
                </el-select>
                <el-select v-model="listQuery.auditStatus" v-permission="['GET /admin/cataInfoTemp/list']" clearable placeholder="状态" class="searchInputWidth selectRadius" @change="handleFilter">
                  <el-option v-for="item in statuss" :key="item.value" :label="item.name" class="filter-item " :value="item.value" />
                </el-select>
              </el-col>
              <el-col :span="24">
                <el-button v-permission="['POST /admin/cataInfoTemp/update']" class="add fl" size="mini" icon="el-icon-plus" @click="addRes">新增
                </el-button>
                <el-button v-permission="['POST /admin/cataInfoTemp/delete']" class="add fl" size="mini" icon="el-icon-delete" @click="deleteRes">删除
                </el-button>
                <el-button v-permission="['POST /admin/cataInfoTemp/check']" class="remove fl" size="mini" icon="" @click="submitCheck">提交审核
                </el-button>
                <el-button v-permission="['POST /admin/cataInfoTemp/allCheck']" class="remove fl" size="mini" icon="" @click="allSubmitCheck">全部提交审核
                </el-button>
                <el-button v-permission="['POST /admin/cataInfoTemp/back']" class="remove fl" size="mini" icon="" @click="recall">撤回
                </el-button>
              </el-col>
            </el-row>
          </div>
          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="uviewNo" label="信息资源代码" show-overflow-tooltip />
            <el-table-column prop="uviewNm" label="信息资源名称" show-overflow-tooltip />
            <el-table-column prop="provOrgName" label="提供单位" width="" show-overflow-tooltip />
            <el-table-column prop="crtDt" label="注册时间" width="150" />
            <el-table-column prop="version" label="版本" width="60">
              <template slot-scope="scope">
                <router-link v-permission="['GET /admin/cataInfoHistory/list']" class="file_name" :to="{path:`/Cataloging/versionManagement/${scope.row.uviewId}`,query:{dataResourceName:scope.row.uviewNm,provOrgId: infoResProver.provOrgId,
                                                                                         provOrgName:infoResProver.provOrgName,
                                                                                         provOrgCode:infoResProver.provOrgCode,
                                                                                         socialCreditCd:infoResProver.socialCreditCd}}">
                  {{ scope.row.version }}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="auditStatus" label="状态" width="80">
              <template slot-scope="scope">
                <div>{{ auditStatusObj[scope.row.auditStatus] }}</div>
              </template>

            </el-table-column>
            <el-table-column label="操作" width="120" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['GET /admin/cataInfoTemp/read']" content="详情" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="detail" class-name="" @click="showDetail(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-if="scope.row.auditStatus != '1'&&scope.row.auditStatus != '3'" v-permission="['POST /admin/cataInfoTemp/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="editRes(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/cataInfoApprove/approveList']" content="审核记录" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="res_check" class-name @click="checkRecord(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
        </div>

        <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
          <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="isConfirmCheck" class="del_dialog" :visible.sync="confirmCheckDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
          <section>
            <el-container style="flex-wrap: wrap;">
              <div class="icon">
                <svg-icon v-show="checkIcon" icon-class="check_confirm" class-name="" style="width: 54px;height: 54px" />
                <svg-icon v-show="checkAllIcon" icon-class="allCheck_confirm" class-name="" style="width: 54px;height: 54px" />
                <svg-icon v-show="backIcon" icon-class="recallIcon" class-name="" style="width: 54px;height: 54px" />
              </div>
              <div class="tip_str">{{ confirmCheckMessage }}</div>
            </el-container>
            <div slot="footer" class="dialog-footer" align="center">
              <el-button v-show="isConfirm" type="primary" class="add" @click="confirmCheck">确定</el-button>
              <el-button v-show="isAllConfirm" type="primary" class="add" @click="allConfirmCheck">确定</el-button>
              <el-button v-show="isBackConfirm" type="primary" class="add" @click="backAllConfirm">确定</el-button>
              <el-button class="remove" @click="confirmCheckDialog = false">取 消</el-button>
            </div>
          </section>

        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="'审核记录'" align="center" :visible.sync="checkRecordDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog">
          <el-row :gutter="10">
            <!-- <p class="recordTitle fl">{{checkRecordData.title}}</p> -->
            <p class="recordTitle fl">{{ checkRecordTitle }}</p>
            <div v-if="checkRecordData.length != 0">
              <el-col v-for="(item,index) in checkRecordData" :key="index" :span="24" class="record_item">
                <div v-if="item.status == 1 || item.status == ''">
                  <el-col :span="6" class="svg_icon">
                    <svg-icon icon-class="correct" class-name="status-icon" />
                  </el-col>
                  <el-col :span="18">
                    <div class="record_info">
                      <p>操作类型：{{ item.optType }}</p>
                      <p>审核时间：{{ item.checkTime }}</p>
                      <p>审核人：{{ item.checkByName }}</p>
                      <p>审核意见：{{ item.comment }}</p>
                    </div>
                  </el-col>
                </div>
                <div v-else-if="item.status == 0">
                  <el-col :span="6" class="svg_icon">
                    <svg-icon icon-class="error" class-name="status-icon" />
                  </el-col>
                  <el-col :span="18">
                    <div class="record_info">
                      <p>操作类型：{{ item.optType }}</p>
                      <p>审核时间:{{ item.checkTime }}</p>
                      <p>审核人:{{ item.checkByName }}</p>
                      <p>审核意见:{{ item.comment }}</p>
                    </div>
                  </el-col>
                </div>
              </el-col>
            </div>
            <div v-else>
              <el-col :span="24" class="record_item" style="    line-height: 110px;">
                <div>
                  暂无审核记录
                </div>

              </el-col>
            </div>

          </el-row>
          <el-row>
            <pagination v-show="total1>0" :small="true" :background="false" :layout="'prev,pager,next'" :total="total1" :page.sync="checkListQuery.page" :limit.sync="checkListQuery.limit" @pagination="getCheckData" />
          </el-row>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="checkRecordDialog = false">关 闭</el-button>
          </div>

        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="'支撑的应用系统'" align="center" :visible.sync="addSystemDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog">
          <el-input v-model="searchQuery.keyword" class="filter_input" style="width: 50%" placeholder="输入应用系统名称或代码" suffix-icon="el-icon-search" />
          <el-button class="remove" size="mini" icon="el-icon-search" @click="searchRelateds">查询
          </el-button>
          <div class="search_result">
            <el-table v-if="searchRelatedResults.length>0" :data="searchRelatedResults" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="RelatedResultsSelectionChange">
              <el-table-column type="selection" width="55" />
              <el-table-column prop="date" label="应用系统编码" width />
              <el-table-column prop="name" label="应用系统名称" width show-overflow-tooltip />
            </el-table>
            <pagination v-show="searchTotal>0" :layout="'prev,pager,next'" :total="searchTotal" :small="true" :background="false" :page.sync="searchQuery.current" :limit.sync="searchQuery.size" @pagination="getSearchData" />
          </div>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="saveaddSystems">保 存</el-button>
            <el-button size="mini" class="remove" @click="addSystemDialog=false">取 消</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  getTableList,
  getTreeAll,
  submitCheckRes,
  allCheckRes,
  recallBack,
  deleteRes,
  resDetail,
  checkRecordDetail
} from '@/api/resCatalog'
import { getDictByType } from '@/api/departmentRes'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import SelectTree from '@/components/SelectTree'
import Hamburger from '@/components/Hamburger'

export default {
  name: 'OSManagement',
  components: { Pagination, DelConfirm, SelectTree, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      checkIcon: true,
      checkAllIcon: false,
      backIcon: false,
      confirmCheckDialog: false,
      confirmCheckMessage: '确认提交审核吗？',
      isConfirmCheck: '确认审核',
      isConfirm: true,
      isAllConfirm: false,
      isBackConfirm: false,
      checkRecordTitle: '',
      multipleSelection: [],
      checkRecordDialog: false,
      providers: [],
      statuss: [],
      addSystemDialog: false,
      addbusinessForm: {},
      dialogTitle: '新增应用系统',
      addWorkOrder: false,
      filterText: '',
      listQuery: {
        page: 1,
        limit: 10
      },
      checkListQuery: {
        page: 1,
        limit: 5
      },
      mapIcon: mapIcon,
      treeData: [],
      // 新增表单校验
      defaultProps: {
        children: 'children',
        label: 'typNm'
      },
      activeName: 0,
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      total1: 0,
      rightTitle: '',
      tipStr: '确认删除所选信息资源吗？',
      delDialog: false,
      selectOrgs: [],
      selectOrgVal: '',
      checkedTreeNodes: '',
      checkRecordData: [],
      uviewId: '',
      provOrgId: '',
      provOrgNm: '',
      provOrgCd: '',
      infoResProver: {},
      infoReBackDeptId: '',
      socialCreditCd: '',
      auditStatusObj: {}
    }
  },
  watch: {
    filterText(val) {
      this.$refs.treeDom.filter(val)
    }
  },
  mounted() {
    // this.$refs.treeDomParents.$el.style.height = document.body.scrollHeight + 'px'
  },
  created() {
    this.infoReBackDeptId = this.$store.state.app.infoReBackDeptId
    this.getTreeData()
    // 获取状态数据字典
    getDictByType({ type: 'audit_status' }).then(res => {
      if (res.data.errno === 0) {
        this.statuss = res.data.data
        res.data.data.forEach(item => {
          this.auditStatusObj[item.value] = item.name
        })
      }
    })
  },
  methods: {
    handleClose(tag) {
      this.addbusinessForm.system.splice(
        this.addbusinessForm.system.indexOf(tag),
        1
      )
    },
    addMenuDetail() {
      this.$refs.addbusinessForm.validate(valid => {
        if (valid) {
          console.log(this.addbusinessForm)
          console.log(this.selectOrgVal)
          console.log(this.checkedTreeNodes)
          this.$notify.success(`${this.dialogTitle}成功`)
          this.addWorkOrder = false
          this.tableData.push(this.addbusinessForm)
          //   editRepair(this.addOrgForm).then(res => {
          //     this.$notify.success("添加回访成功");
          //     this.returnList = false;
          //     this.getList();
          //   });
        }
      })
    },
    getCheckedOrg(args) {
      this.selectOrgVal = args[0]
      this.checkedTreeNodes = args[1]
    },
    showCodenIfo(data) {
      this.getPName('addbusinessForm')
      this.addbusinessForm = Object.assign({}, this.addbusinessForm, data)
      this.selectOrgs = []
      this.dialogTitle = '编辑'
      this.addWorkOrder = true
      setTimeout(() => {
        this.addbusinessForm.orgId ? this.selectOrgs.push(this.addbusinessForm.orgId) : this.selectOrgs = []
      }, 20)
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },

    getTableData() {
      this.tableLoading = true
      this.infoResProver = {
        provOrgId: this.provOrgId ? this.provOrgId : this.$refs.treeDom.getCurrentNode().orgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
        provOrgName: this.provOrgNm ? this.provOrgNm : this.$refs.treeDom.getCurrentNode().typNm ? this.$refs.treeDom.getCurrentNode().typNm : '',
        // 这里拿统一信用代码
        provOrgCode: this.provOrgCd ? this.provOrgCd : this.$refs.treeDom.getCurrentNode().orgCd ? this.$refs.treeDom.getCurrentNode().orgCd : '',
        socialCreditCd: this.socialCreditCd ? this.socialCreditCd : this.$refs.treeDom.getCurrentNode().socialCreditCd ? this.$refs.treeDom.getCurrentNode().socialCreditCd : ''

      }
      const sendData = Object.assign({}, this.listQuery, {
        provOrgId: this.provOrgId ? this.provOrgId : this.$refs.treeDom.getCurrentNode().orgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
        typeId: this.$refs.treeDom.getCurrentNode().orgId ? '' : this.$refs.treeDom.getCurrentNode().typId
      })
      // const sendData = Object.assign({}, this.listQuery, {
      //   provOrgId: this.provOrgId ? this.provOrgId : this.$refs.treeDom.getCurrentNode() ? this.$refs.treeDom.getCurrentNode().orgId : '',
      //   typeId: this.$refs.treeDom.getCurrentNode() ? this.$refs.treeDom.getCurrentNode().fullTypCd ? this.$refs.treeDom.getCurrentNode().typId : '' : ''
      // })
      // console.log(sendData)
      getTableList(sendData)
        .then(response => {
          this.tableData = response.data.data.records
          this.total = response.data.data.total
          this.tableLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.tableLoading = false
        })
    },
    getTreeData() {
      this.treeLoading = true
      getTreeAll()
        .then(response => {
          if (response.data.errno == 0) {
            this.treeData = response.data.data
            this.listLoading = false
            this.currentNodekey = this.infoReBackDeptId ? this.infoReBackDeptId : this.treeData[0].orgCd
            // this.currentNodekey = this.treeData[0].orgCd;
            // console.log(this.currentNodekey)
            setTimeout(() => {
              this.$store.dispatch('setInfoReBackDeptId', '')
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
              this.getTableData()
              this.rightTitle = this.$refs.treeDom.getNode()
            }, 20)
          }
          this.treeLoading = false
        })
        .catch(() => {
          this.treeLoading = false
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleNodeClick(data, e) {
      console.log(data, e, "000000000")
      let orgId = ''
      let orgNm = ''
      let orgCd = ''
      let socialCreditCd = ''
      const _this = this
      if (e.parent.parent == null) {
        orgId = e.data.orgId
        orgNm = e.data.orgNm
        orgCd = e.data.orgCd
        socialCreditCd = e.data.socialCreditCd
      } else {
        _resolve(e)
      }

      function _resolve(e) {
        for (const k in e) {
          if (k == 'parent') {
            if (e[k] != null && e[k].level != 0) {
              _resolve(e.parent)
            } else {
              orgId = e.data.orgId
              orgNm = e.data.orgNm
              orgCd = e.data.orgCd
              socialCreditCd = e.data.socialCreditCd
            }
          }
        }
      }
      this.provOrgId = orgId
      this.provOrgNm = orgNm
      this.provOrgCd = orgCd
      this.socialCreditCd = socialCreditCd
      this.getTableData()
    },
    RelatedResultsSelectionChange(val) {
      // console.log(val)
      this.choosedRelatedResults = val
    },
    searchRelateds() {
      if (this.searchQuery.keyword == '') {
        this.$message({
          message: '请输入应用系统名称或编码',
          type: 'error'
        })
        return
      }
      this.getSearchData()
    },
    getSearchData() {
      this.searchRelatedResults = [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄',
          id: 1
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄',
          id: 3
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄',
          id: 2
        }
      ]
      this.searchTotal = this.searchRelatedResults.length
    },
    saveaddSystems() {
      const sendData = {
        departmentId: this.$refs.treeDom.getCurrentNode().id,
        addSystems: this.choosedRelatedResults
      }
      console.log(sendData)
      this.addSystemDialog = false
      this.addbusinessForm = Object.assign({}, this.addbusinessForm, {
        system: this.choosedRelatedResults
      })
    },
    showSystems() {
      this.addSystemDialog = true
    },
    handleTabClick(tab, event) {
      console.log(tab, event)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 新增
    addRes() {
      this.$router.push({
        path: '/Cataloging/infoCatalogMaintain/0',
        query: {
          provOrgId: this.infoResProver.provOrgId,
          provOrgName: this.infoResProver.provOrgName,
          provOrgCode: this.infoResProver.provOrgCode,
          socialCreditCd: this.infoResProver.socialCreditCd
        }
      })
    },
    // 详情
    showDetail(data) {
      this.$router.push({
        path: `/Cataloging/infoCatalogDetail/${data.uviewId}`, query: {
          provOrgId: this.infoResProver.provOrgId,
          provOrgName: this.infoResProver.provOrgName,
          provOrgCode: this.infoResProver.provOrgCode,
          socialCreditCd: this.infoResProver.socialCreditCd
        }
      })
    },
    // 编辑
    editRes(data) {
      this.$router.push({
        path: `/Cataloging/infoCatalogMaintain/${data.uviewId}`, query: {
          provOrgId: this.infoResProver.provOrgId,
          provOrgName: this.infoResProver.provOrgName,
          provOrgCode: this.infoResProver.provOrgCode,
          socialCreditCd: this.infoResProver.socialCreditCd
        }
      })
    },
    // 删除
    deleteRes(data) {
      this.tempDelData = Object.assign({}, data)
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请至少选择一条信息资源',
          type: 'error'
        })
      } else {
        this.delDialog = true
      }
    },
    // 确认提交审核
    confirmCheck() {
      const listData = this.multipleSelection
      const idArr = []
      for (const i in listData) {
        idArr.push(listData[i].uviewId)
      }
      submitCheckRes(idArr)
        .then(response => {
          if (response.data.errno == 0) {
            this.getTableData()
            this.confirmCheckDialog = false
            this.$notify.success('提交审核成功')
          } else {
            this.$message.error(response.data.errmsg || '出错了~')
          }
        }).catch(err => {
          this.$message.error(err.data.errmsg || '出错了~')
        })
    },
    allConfirmCheck() {
      allCheckRes().then(response => {
        if (response.data.errno == 0) {
          this.getTableData()
          this.confirmCheckDialog = false
          this.$notify.success('全部审核成功')
        } else {
          this.$message.error(response.data.errmsg || '出错了~')
        }
      }
      ).catch(err => {
        this.$message.error(err.data.errmsg || '出错了~')
      })
    },
    backAllConfirm() {
      const listData = this.multipleSelection
      const idArr = []
      for (const i in listData) {
        idArr.push(listData[i].uviewId)
      }
      recallBack(idArr)
        .then(response => {
          if (response.data.errno == 0) {
            this.getTableData()
            this.confirmCheckDialog = false
            this.$notify.success('撤回成功')
          } else {
            this.$message.error(response.data.errmsg || '出错了~')
          }
        }).catch(err => {
          console.log(err)
          this.$message.error(err.data.errmsg || '出错了~')
        })
    },
    // 提交审核
    submitCheck() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请至少选择一条信息资源',
          type: 'error'
        })
      } else {
        this.isConfirmCheck = '确认审核'
        this.confirmCheckMessage = '确认提交审核吗？'
        this.isConfirm = true
        this.isAllConfirm = false
        this.isBackConfirm = false
        this.checkIcon = true
        this.checkAllIcon = false
        this.backIcon = false
        this.confirmCheckDialog = true
      }
    },
    // 全部提交审核
    allSubmitCheck() {
      this.isConfirmCheck = '确认全部审核'
      this.confirmCheckMessage = '确认全部提交审核吗？'
      this.isConfirm = false
      this.isAllConfirm = true
      this.isBackConfirm = false
      this.checkIcon = false
      this.checkAllIcon = true
      this.backIcon = false
      this.confirmCheckDialog = true
    },
    // 撤回
    recall() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请至少选择一条信息资源',
          type: 'error'
        })
      } else {
        this.isConfirmCheck = '确认撤回'
        this.confirmCheckMessage = '确认撤回吗？'
        this.isConfirm = false
        this.isAllConfirm = false
        this.isBackConfirm = true
        this.checkIcon = false
        this.checkAllIcon = false
        this.backIcon = true
        this.confirmCheckDialog = true
      }
    },
    getCheckData() {
      checkRecordDetail(Object.assign({}, this.checkListQuery, { infoId: this.uviewId * 1 }))
        .then(response => {
          this.checkRecordData = response.data.data.records
          this.total1 = response.data.data.total
        })
        .catch(() => {
          this.checkRecordData = []
          this.total1 = 0
        })
    },
    // 审核记录
    checkRecord(data) {
      const dataArr = [{
        title: '123',
        date: '2020-1-1',
        status: '1',
        person: 'test',
        idea: '无意见'
      },
      {
        title: '123',
        date: '2020-1-1',
        status: '0',
        person: 'test1',
        idea: '无意见'
      }]
      this.checkRecordTitle = data.uviewNm
      this.uviewId = data.uviewId
      this.getCheckData()

      // this.checkRecordData = Object.assign({}, dataArr);
      //  this.checkRecordDataTitle = data;
      this.checkRecordDialog = true
    },
    confirmDelete() {
      console.log(this.tempDelData)
      const listData = this.multipleSelection
      const idArr = []
      for (const i in listData) {
        idArr.push(listData[i].uviewId)
      }
      deleteRes(idArr)
        .then(response => {
          if (response.data.errno == 0) {
            this.$notify.success(response.data.errmsg)
            this.getTableData()
            this.delDialog = false
            this.tempDelData = {}
          } else {
            this.$message.error(response.data.errmsg || '出错了~')
          }
        }).catch(err => {
          console.log(err)
          this.$message.error(err.data.errmsg || '出错了~')
        })
    }
  }
}

</script>

<style scoped>
.recordTitle {
  font-size: 18px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
}
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
.file_name:hover {
  color: #1f3365;
  text-decoration: underline;
}

.product_resource_btn {
  display: inline-block;
  height: 20px;
  line-height: 20px;
  color: #1f3365;
  padding: 0px 5px;
  border: 1px solid rgba(31, 51, 101, 1);
  border-radius: 5px;
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
  border-radius: 5px;
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
  color: #5abd8c;
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

.record_item {
  min-height: 110px;
  margin-bottom: 10px;
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(201, 201, 201, 1);
  opacity: 1;
  border-radius: 8px;
}

.record_info {
  text-align: left;
}

.status-icon {
  font-size: 80px;
}

.svg_icon {
  margin-top: 10px;
}
</style>
<style>
.searchInputWidth {
  width: 20%;
}
.selectRadius input {
}
</style>
<style scoped>
.dialog-footer {
  height: 56px;
  line-height: 56px;
  background: rgba(251, 251, 251, 1);
  border: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 0px 0px 6px 6px;
}
.tip_str {
  width: 100%;
  text-align: center;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  line-height: 50px;
  color: rgba(36, 36, 36, 1);
  margin-bottom: 5px;
}
.icon {
  margin-top: 35px;
  width: 100%;
  text-align: center;
}
</style>
