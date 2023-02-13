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
          <div class="related_btns" style="padding-bottom: 0">
            <el-row :gutter="20">
              <el-col>
                <el-input v-model="listQuery.name" clearable class="filter-item fl" style="width: 35%" placeholder="信息资源名称" />
                <el-select v-model="listQuery.deleteStatus" placeholder="删除状态" class="filter-item fl headItem" clearable @change="handleFilter">
                  <el-option v-for="item in saleTypes" :key="item.id" :label="item.name" :value="item.value" />
                  <template slot="prepend">所属部门</template>
                </el-select>
                <el-button v-permission="['GET /admin/cocataInfoApprove/cataInfoDeleteList']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="getTableData">查询</el-button>
              </el-col>
            </el-row>
            <el-row :gutter="20" class="headItemContent">
              <el-col>
                <el-button v-permission="['POST /admin/cataInfoApprove/approveDelete']" class="add headItem" size="mini" icon="el-icon-check" @click="addMenu(1)">审核通过</el-button>
                <el-button v-permission="['POST /admin/cataInfoApprove/approveDelete']" class="add headItem" size="mini" icon="el-icon-circle-close" @click="addMenu(2)">审核驳回</el-button>
                <el-button v-permission="['POST /admin/cataInfoApprove/approveDelete']" class="add headItem" size="mini" icon="el-icon-check" @click="addMenu(3)">全部审核通过</el-button>
                <el-button v-permission="['POST /admin/cataInfoApprove/approveDelete']" class="add headItem" size="mini" icon="el-icon-circle-close" @click="addMenu(4)">全部审核驳回</el-button>
              </el-col>
            </el-row>
          </div>
          <el-table ref="multipleTable" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="uview_no" label="信息资源代码" width="100" show-overflow-tooltip />
            <el-table-column prop="uview_nm" label="信息资源名称" show-overflow-tooltip />
            <el-table-column prop="org_nm" label="提供单位" width="150" show-overflow-tooltip />
            <el-table-column prop="check_time" label="提交审核时间" show-overflow-tooltip />
            <el-table-column prop="status" label="审核状态" :formatter="statusFormatter">
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template slot-scope="scope">
                <el-tooltip content="详情" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="detail" class-name @click="gotoDetail(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip content="审核记录" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="shenhe" class-name @click="checkRecord(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
        </div>

        <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addbusinessForm','addbusinessForm')">
          <el-form ref="addbusinessForm" :model="addbusinessForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="已选目录：" label-width="140px">
              <span>{{ selectOrgs.length }}个</span>
            </el-form-item>
            <el-form-item label="审核意向：" label-width="140px" prop="resource">
              <el-radio-group v-model="addbusinessForm.approveStatus">
                <el-radio v-if="showTongguo" label="通过" value="1" />
                <el-radio v-if="!showTongguo" label="不通过" value="0" />
              </el-radio-group>
            </el-form-item>
            <el-form-item label="审核意见：" class="required_label" label-width="140px" prop="comment">
              <el-input v-model="addbusinessForm.comment" :rows="5" type="textarea" style="width:100%" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button v-permission="['POST /admin/cataInfoApprove/approve2']" size="mini" class="add" @click="addMenuDetail()">保 存</el-button>
            <el-button size="mini" class="remove" @click="addWorkOrder=false">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="'审核记录'" align="center" :visible.sync="auditRecord" width="48%" :close-on-click-modal="false" @close="closeAddDialog">
          <el-row :gutter="10">
            <!-- <p class="recordTitle fl">{{checkRecordData.title}}</p> -->
            <p class="recordTitle fl dialogText">{{ checkTitle }}</p>
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
          </el-row>
          <el-row>
            <pagination v-show="total1>0" :small="true" :background="false" :layout="'prev,pager,next'" :total="total1" :page.sync="checkListQuery.page" :limit.sync="checkListQuery.limit" @pagination="getShenTable" />
          </el-row>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="auditRecord = false">关 闭</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  cataShenHeInfoDeleteList,
  cataShenHeApproveDelete,
  cataShenHeList
} from '@/api/shenHe'
import { dsysDictGetByTypet } from '@/api/assetCata'
import { getDepartmentTree } from '@/api/departmentRes'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import Hamburger from '@/components/Hamburger'

import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
export default {
  name: 'Audit',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  props: {
    status: {
      type: String
    }
  },
  data() {
    return {
      auditRecord: false, // 审核记录
      saleTypes: [{
        id: '2',
        name: '待删除',
        value: '2'
      }, {
        id: '1',
        name: '已删除',
        value: '1'
      }, {
        id: '0',
        name: '删除驳回',
        value: '0'
      }], // 审核部门
      selecetCata: {}, // 选择目录
      addbusinessForm: {},
      dialogTitle: '审核目录',
      addWorkOrder: false,
      showTongguo: true,
      filterText: '',
      listQuery: {
        current: 1,
        limit: 10,
        typeName: '',
        deleteStatus: '2'
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
        approveStatus: [
          { required: true, message: '请选择状态', trigger: 'blur' }
        ],
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
      selectOrgs: [],
      checkRecordData: {},
      infoResProver: {},
      checkTitle: '',
      thisinfoId: ''
    }
  },
  watch: {
    filterText(val) {
      this.$refs.treeDom.filter(val)
    }
  },
  created() {
    this.getTreeData()
  },
  methods: {
    // 审核记录
    checkRecord(data) {
      this.checkTitle = data.uview_nm
      this.thisinfoId = data.uview_id
      this.getShenTable()
    },
    getShenTable() {
      cataShenHeList(
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
    gotoDetail(row) {
      this.$router.push({
        path: '/Cataloging/infoCatalogDetail/' + row.uview_id,
        query: {
          provOrgId: row.org_id
        }
      })
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
          if (this.addbusinessForm.approveStatus == '通过') {
            this.addbusinessForm.approveStatus = 1
          } else {
            this.addbusinessForm.approveStatus = 0
          }
          var appIds = []
          this.selectOrgs.forEach(item => {
            appIds.push(item.id)
          })
          var asd = {}
          asd.comment = this.addbusinessForm.comment
          asd.approveStatus = this.addbusinessForm.approveStatus
          cataShenHeApproveDelete(appIds, asd)
            .then(res => {
              console.log(this.addbusinessForm)
              this.$notify.success(`${this.dialogTitle}成功`)
              this.addWorkOrder = false
              this.getTableData()
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
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
    addMenu(num) {
      switch (num) {
        case 1:
          this.addbusinessForm.approveStatus = '通过'
          this.showTongguo = true
          break
        case 2:
          this.addbusinessForm.approveStatus = '不通过'
          this.showTongguo = false

          break
        case 3:
          this.addbusinessForm.approveStatus = '通过'
          this.toggleSelection(this.tableData)
          this.showTongguo = true

          break
        case 4:
          this.addbusinessForm.approveStatus = '不通过'
          this.toggleSelection(this.tableData)
          this.showTongguo = false

          break
      }
      this.dialogTitle = '目录删除审核'
      this.selectOrgs = []
      if (this.selecetCata.length == null || this.selecetCata.length == 0) {
        this.$message({
          message: '请先勾选未审核项目',
          type: 'error'
        })
        return
      }
      this.selecetCata.forEach(element => {
        if (element.status == '0' || element.status == '1') {
        } else {
          this.selectOrgs.push(element)
        }
      })
      if (this.selectOrgs.length == null || this.selectOrgs.length == 0) {
        this.$message({
          message: '请先勾选未审核项目',
          type: 'error'
        })
        return
      }
      this.addWorkOrder = true
      this.getPName('addbusinessForm')
    },

    handleFilter() {
      this.listQuery.page = 1
      this.getTableData()
    },
    getTableData() {
      this.tableLoading = true
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
      const sendData = Object.assign({}, this.listQuery)
      //状态值转换
      let deleteStatus = this.listQuery.deleteStatus
      if (deleteStatus == '') {
        sendData.deleteStatus = null
      } else if (deleteStatus == '2') {
        sendData.deleteStatus = ''
      }
      cataShenHeInfoDeleteList(sendData)
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

      this.listQuery.pid = data.orgId
      if (this.listQuery.pid == 0) {
        this.listQuery.pid = null
      }
      this.getTableData()
    },

    handleSelectionChange(val) {
      this.selecetCata = val
    },
    statusFormatter(row, column, cellValue, index) {
      if (cellValue == '') {
        return '待删除'
      } else if (cellValue == '1') {
        return '已删除'
      }
      return '删除驳回'
    }
  }
}
</script>

<style scoped>
.filter-item {
  float: left;
  margin-right: 15px;
}
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
.headItemContent {
  margin-top: 20px;
}
.headItem {
  margin-right: 20px;
  float: left;
  margin-left: 0;
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

