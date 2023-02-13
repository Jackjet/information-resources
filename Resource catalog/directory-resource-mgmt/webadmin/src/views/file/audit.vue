<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
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
              <el-tree ref="treeDom" accordion :data="treeData" :props="defaultProps" highlight-current :expand-on-click-node="false" node-key="orgCd" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" @node-click="handleNodeClick">
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
          <div class="related_btns clearfix">

            <el-input v-model="listQuery.name" clearable class="filter-item fl" style="width: 175px" placeholder="请输入资源目录名称" />
            <el-select v-model="listQuery.status" placeholder="审核状态" class="filter-item fl headItem" clearable style="width: 15%" @change="handleFilter">
              <el-option v-for="item in saleTypes" :key="item.id" :label="item.name" :value="item.value" />
            </el-select>
            <span class="filter_text fl">报送日期</span>
            <el-date-picker v-model="timeRange" style=";width: 250px;margin-right: 0" class="filter-item fl" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="起始日期" end-placeholder="截至日期" @change="getTableData" />
            <el-button v-permission="['GET /admin/fileUploadRel/list2']" class="remove fr" style="" icon="el-icon-search" @click="getTableData">查询</el-button>

          </div>
          <el-table ref="multipleTable" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column prop="uviewNm" label="资源目录" width="100" show-overflow-tooltip />
            <el-table-column prop="fileName" label="文件名称" show-overflow-tooltip />
            <el-table-column prop="format" label="文件格式" show-overflow-tooltip />
            <el-table-column prop="orgNm" label="上传部门" show-overflow-tooltip />
            <el-table-column prop="addTime" label="上传时间" show-overflow-tooltip />
            <el-table-column label="审核状态">
              <template slot-scope="scope">
                <span :class=" scope.row.statusNm=='驳回'?'red':''">{{ scope.row.statusNm }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-if="scope.row.status==1||scope.row.status==2" v-permission="['GET /admin/fileUploadRel/detail']" content="详情" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="detail" class-name @click="openDetail(scope.row,0)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-if="scope.row.status==0" v-permission="['POST /admin/fileUploadRel/reviewed']" content="审核" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="openDetail(scope.row,1)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>

        <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addbusinessForm','addbusinessForm')">
          <el-form ref="addbusinessForm" :model="addbusinessForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-row>
              <el-col :span="12">
                <el-form-item label="资源目录：" class="fileShenHeItem" label-width="100px">
                  <el-tooltip :content="readDetail.uviewNm" effect="light" :open-delay="1000" placement="bottom">
                    <span>{{ readDetail.uviewNm }}</span>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="数据文件：" class="fileShenHeItem" label-width="100px">
                  <el-tooltip :content="readDetail.name" effect="light" :open-delay="1000" placement="bottom">
                    <a href="javascript:;" style="color:#336AC8" @click="downloadFileIframe(readDetail.url)">{{ readDetail.name }}</a>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文件名称：" class="fileShenHeItem" label-width="100px">
                  <el-tooltip :content="readDetail.name" effect="light" :open-delay="1000" placement="bottom">
                    <span>{{ readDetail.name }}</span>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="文件说明：" class="fileShenHeItem" label-width="100px">
                  <el-tooltip :content="readDetail.fileRemark" effect="light" :open-delay="1000" placement="bottom-start">
                    <span>{{ readDetail.fileRemark }}</span>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上传时间：" class="fileShenHeItem" label-width="100px">
                  <el-tooltip :content="readDetail.addTime" effect="light" :open-delay="1000" placement="bottom">
                    <span>{{ readDetail.addTime }}</span>
                  </el-tooltip>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="上传部门：" class="fileShenHeItem" label-width="100px">
                  <el-tooltip :content="readDetail.orgNm" effect="light" :open-delay="1000" placement="bottom">
                    <span>{{ readDetail.orgNm }}</span>
                  </el-tooltip>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="showLastShenhe" class="itemBorder">
              <div class="item_title">最后审核意见</div>
              <el-form-item class="fileShenHeItem" label="审核人：" label-width="100px">
                <span>{{ readDetail.updateby }}</span>
              </el-form-item>
              <el-form-item class="fileShenHeItem" label="审核时间：" label-width="100px">
                <span>{{ readDetail.updatetime }}</span>
              </el-form-item>
              <el-form-item class="fileShenHeItem" label="审核意向：" label-width="100px">
                <span>{{ readDetail.statusNm }}</span>
              </el-form-item>
              <el-form-item class="fileShenHeItem" label="审核意见：" label-width="100px">
                <span>{{ readDetail.comment }}</span>
              </el-form-item>
            </el-row>
            <el-row v-if="isShenhe" class="itemBorder">
              <div class="item_title">审核意见</div>
              <el-form-item v-if="isShenhe" class="required_label" label="审核意向：" label-width="100px" prop="status">
                <el-radio-group v-model="addbusinessForm.status">
                  <el-radio label="通过" value="1" />
                  <el-radio label="不通过" value="2" />
                </el-radio-group>
              </el-form-item>
              <el-form-item v-if="isShenhe" label="审核意见：" class="required_label" label-width="100px" prop="comment">
                <el-input v-model="addbusinessForm.comment" :rows="5" type="textarea" style="width:100%" />
              </el-form-item>
            </el-row>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button v-if="isShenhe" v-permission="['POST /admin/fileUploadRel/reviewed']" size="mini" class="add" @click="addMenuDetail()">保 存</el-button>
            <el-button size="mini" class="remove" @click="addWorkOrder=false">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="'审核记录'" align="center" :visible.sync="auditRecord" width="48%" :close-on-click-modal="false" @close="closeAddDialog">
          <el-row :gutter="10">
            <!-- <p class="recordTitle fl">{{checkRecordData.title}}</p> -->
            <p class="recordTitle fl dialogText">{{ checkTitle }}</p>
            <el-col v-for="(item,index) in checkRecordData" :key="index" :span="24" class="record_item">
              <div v-if="item.status == 1">
                <el-col :span="6" class="svg_icon">
                  <svg-icon icon-class="correct" class-name="status-icon" />
                </el-col>
                <el-col :span="18">
                  <div class="record_info">
                    <p>审核时间:{{ item.checkTime }}</p>
                    <p>审核人:{{ item.checkByName }}</p>
                    <p>审核意见:{{ item.comment }}</p>
                  </div>
                </el-col>
              </div>
              <div v-else-if="item.status == 0">
                <el-col :span="6" class="svg_icon">
                  <svg-icon icon-class="error" class-name="status-icon" />
                </el-col>
                <el-col :span="18">
                  <div class="record_info">
                    <p>审核时间:{{ item.checkTime }}</p>
                    <p>审核人:{{ item.checkByName }}</p>
                    <p>审核意见:{{ item.comment }}</p>
                  </div>
                </el-col>
              </div>
            </el-col>
          </el-row>
          <el-row>
            <pagination v-show="total1>0" :small="true" :background="false" :layout="'prev,pager,next'" :total="total1" :page.sync="checkListQuery.page" :limit.sync="checkListQuery.limit" @pagination="getShenTable" />
          </el-row>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="auditRecord = false">关 闭</el-button>
          </div>
        </el-dialog>

        <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
          <del-confirm :tip-str="tipStr" :icon-class="iconClass" @cancelDel="delDialog=false" @confirmDel="confirmSubmit" />
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  fileShenHeInfoList,
  fileShenHeApprove,
  fileShenHeDetail
} from '@/api/shenHe'
import { dsysDictGetByTypet } from '@/api/assetCata'
import { getDepartmentTree } from '@/api/departmentRes'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import Hamburger from '@/components/Hamburger'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
export default {
  name: 'FileAudit',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  props: {
    status: {
      type: String
    }
  },
  data() {
    return {
      iconClass: 'check_confirm',
      delDialog: false,
      auditRecord: false, // 审核记录
      saleTypes: [
        {
          name: '等待审核',
          value: '0'
        },
        {
          name: '通过',
          value: '1'
        },
        {
          name: '驳回',
          value: '2'
        }
      ], // 审核部门
      timeRange: null,
      selecetCata: {}, // 选择目录
      addSystemDialog: false,
      addbusinessForm: {},
      dialogTitle: '审核文件',
      addWorkOrder: false,
      isShenhe: false,
      showLastShenhe: false,
      filterText: '',
      listQuery: {
        current: 1,
        size: 10,
        typeName: ''
      },
      checkListQuery: {
        page: 1,
        limit: 5
      },
      mapIcon: mapIcon,
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加
        status: [{ required: true, message: '请选择审核意向', trigger: 'change' }],
        comment: [
          { required: true, message: '请输入审核意见', trigger: 'blur' }
        ]
      },
      defaultProps: {
        children: 'children',
        label: 'orgNm'
      },
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      total1: 0,
      rightTitle: '',
      tipStr: '确定要提交审核意见吗？',
      checkRecordData: {},
      infoResProver: {},
      checkTitle: '',
      thisinfoId: '',
      readDetail: {}
    }
  },
  watch: {
    filterText(val) {
      this.$refs.treeDom.filter(val)
    }
  },
  created() {
    this.getByType()
  },
  methods: {
    getByType() {
      if (this.$route.query.status != null) {
        this.listQuery.status = this.$route.query.status
      }
      this.getTreeData()
    },
    // 审核记录
    checkRecord(data) {
      this.checkTitle = data.uview_nm
      this.thisinfoId = data.uview_id
      this.getShenTable()
    },
    getShenTable() {
      fileShenHeList(
        Object.assign({}, this.checkListQuery, { infoId: this.thisinfoId })
      )
        .then(res => {
          this.checkRecordData = res.data.data.records
          this.total1 = res.data.data.total
          this.auditRecord = true
        })
        .catch(res => {
          this.checkRecordData = []
          this.total1 = 0
        })
    },
    openDetail(row, data) {
      if (data == 0) {
        this.isShenhe = false
      } else {
        this.isShenhe = true
      }
      fileShenHeDetail({ id: row.id })
        .then(res => {
          this.readDetail = res.data.data
          if (this.readDetail.updateby == undefined) {
            this.showLastShenhe = false
          } else {
            this.showLastShenhe = true
          }
          this.addWorkOrder = true
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
      this.getPName('addbusinessForm')
    },
    handleClose(tag) {
      this.addbusinessForm.system.splice(
        this.addbusinessForm.system.indexOf(tag),
        1
      )
    },
    addMenuDetail() {
      this.$refs.addbusinessForm.validate(valid => {
        if (valid) {
          this.delDialog = true
        }
      })
    },
    confirmSubmit() {
      if (this.addbusinessForm.status == '通过') {
        this.addbusinessForm.status = 1
      } else {
        this.addbusinessForm.status = 2
      }
      var asd = {}
      asd.comment = this.addbusinessForm.comment
      asd.status = this.addbusinessForm.status
      asd.id = this.readDetail.id
      fileShenHeApprove(asd)
        .then(res => {
          this.$notify.success(`${this.dialogTitle}成功`)
          this.addWorkOrder = false
          this.delDialog = false
          this.getTableData()
        })
        .catch(response => {
          this.delDialog = false
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row, true)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    addMenu(data) {
      this.isShenhe = true
      this.addWorkOrder = true
      this.getPName('addbusinessForm')
    },

    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },
    getTableData() {
      this.tableLoading = true
      if (this.timeRange) {
        this.listQuery.startTime = this.timeRange[0]
        this.listQuery.endTime = this.timeRange[1]
      } else {
        this.listQuery.startTime = this.listQuery.endTime = ''
      }
      this.infoResProver = {
        pid: this.pid
          ? this.pid
          : this.$refs.treeDom.getCurrentNode().orgId
            ? this.$refs.treeDom.getCurrentNode().orgId
            : '',
        provOrgName: this.provOrgNm
          ? this.provOrgNm
          : this.$refs.treeDom.getCurrentNode().orgNm
            ? this.$refs.treeDom.getCurrentNode().orgNm
            : '',
        provOrgCode: this.provOrgCd
          ? this.provOrgCd
          : this.$refs.treeDom.getCurrentNode().orgCd
            ? this.$refs.treeDom.getCurrentNode().orgCd
            : ''
      }
      //  const sendData = Object.assign({},this.listQuery,{
      //    pid:this.pid ? this.pid : this.$refs.treeDom.getCurrentNode().orgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
      //    typeId:this.$refs.treeDom.getCurrentNode().orgId ? '' : this.$refs.treeDom.getCurrentNode().typId})
      const sendData = Object.assign({}, this.listQuery)
      fileShenHeInfoList(sendData)
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
      getDepartmentTree({ isTop: 0 })
        .then(response => {
          this.treeData = response.data
          this.listLoading = false
          this.currentNodekey = this.treeData[0].orgCd
          setTimeout(() => {
            this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
            this.getTableData()
            this.rightTitle = this.$refs.treeDom.getCurrentNode().orgNm
          }, 20)
          this.treeLoading = false
        })
        .catch(() => { })
    },
    handleNodeClick(data, e) {
      const orgId = ''
      const orgNm = ''
      const orgCd = ''

      this.listQuery.deptId = data.orgId
      if (this.listQuery.deptId == 0) {
        this.listQuery.deptId = null
      }
      this.getTableData()
    },

    handleSelectionChange(val) {
      this.selecetCata = val
    }
  }
}
</script>

<style scoped>
.itemBorder {
  border-top: 1px solid #cbcbcb;
  margin-top: 10px;
}
.el-form-item {
  margin-bottom: 22px;
}
.fileShenHeItem.el-form-item {
  margin-bottom: 0;
}

.item_title {
  margin-top: 10px;
  margin-bottom: 10px;
  line-height: 30px;
  height: 30px;
  font-size: 16px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  color: #242424;
  font-weight: Bold;
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
.headItemContent {
  margin-top: 20px;
}
.headItem {
}
.dialogText {
  color: #172853;
  font-size: 16px;
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
  opacity: 1;
  border-radius: 6px;
}
.filter_text {
  line-height: 36px;
  margin-right: 6px;
  margin-left: 6px;
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
.filter-item {
  float: left;
  margin-right: 15px;
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
.red {
  color: #e65555;
}
</style>

