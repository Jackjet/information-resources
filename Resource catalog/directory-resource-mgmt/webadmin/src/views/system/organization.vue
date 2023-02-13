<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">组织机构</span>
            <el-tooltip :content="treeExpansion ? '收起' : '展开'" effect="light" :open-delay="1000" placement="bottom">
              <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
            </el-tooltip>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-input v-model="filterText" class="filter_input" clearable placeholder="输入部门名称" suffix-icon="el-icon-search" />
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" highlight-current node-key="orgId" :expand-on-click-node="false" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :filter-node-method="filterNode" @node-click="handleNodeClick">
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
              <el-col :span="3">
                <el-button v-permission="['POST /admin/org/save']" class="add" size="mini" icon="el-icon-plus" @click="addOrg">新增</el-button>
              </el-col>
            </el-row>
          </div>
          <el-table :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column type="index" width="55" label="序号" />
            <el-table-column prop="orgNm" label="组织机构名称" width show-overflow-tooltip />
            <el-table-column prop="orgCd" label="机构编码" width show-overflow-tooltip />
            <el-table-column label="操作" width="150px" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['POST /admin/org/updateOrg']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/org/delete']" content="移除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>

        <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addOrgForm','addOrgForm')">
          <el-form ref="addOrgForm" :model="addOrgForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="上级组织机构名称：" label-width="140px" prop="applyUserId">
              <span>{{ dialogName }}</span>
            </el-form-item>
            <el-form-item label="机构编码：" class="required_label" label-width="140px" prop="orgCd">
              <el-input v-model="addOrgForm.orgCd" style="width:100%" placeholder="机构编码" readonly />
            </el-form-item>
            <el-form-item label="组织机构名称：" class="required_label" label-width="140px" prop="orgNm">
              <el-input v-model="addOrgForm.orgNm" style="width:100%" placeholder="请输入部门全程（例：XX市政府办公厅）" />
            </el-form-item>
            <el-form-item label="排序序号：" class="required_label" label-width="140px" prop="dispalySn">
              <el-input v-model="addOrgForm.dispalySn" style="width:100%" />
            </el-form-item>
            <el-form-item label="组织机构简称：" label-width="140px">
              <el-input v-model="addOrgForm.orgAlias" style="width:100%" placeholder="请输入组织机构简称" />
            </el-form-item>
            <el-form-item label="统一社会信用代码：" prop="socialCreditCd" label-width="140px">
              <el-input v-model="addOrgForm.socialCreditCd" style="width:100%" placeholder="请输入统一社会信用代码" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button v-permission="['POST /admin/org/save','POST /admin/org/updateOrg']" size="mini" class="add" @click="dialogTitle=='新增'?addOrgDetail(1):addOrgDetail(2)">保 存</el-button>
            <el-button size="mini" class="remove" @click="hideDialog(2)">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
          <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  orgDeptTree,
  orgUpdateOrg,
  orgSave,
  orgDelete,
  orgList,
  orgGetPosition,
  orgGenerateCd,
  orgDetail
} from '@/api/assetCata'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import Hamburger from '@/components/Hamburger'
import DelConfirm from '@/components/DelConfirm'

export default {
  name: 'Organization',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      tempDelData: {},
      tipStr: '确定要删除所选组织机构吗？',
      delDialog: false,
      addOrgForm: {},
      dialogTitle: '新增',
      addWorkOrder: false,
      filterText: '',
      dialogName: '',
      listQuery: {
        current: 1,
        size: 10
      },
      mapIcon: mapIcon,
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加
        orgCd: [
          { required: true, message: '请输入组织机构代码', trigger: 'blur' }
        ],
        orgNm: [
          { required: true, message: '请输入组织机构名称', trigger: 'blur' }
        ],
        socialCreditCd: [
          {
            validator: this.hasChina,
            message: '统一社会信用代码不能输入汉字',
            trigger: 'blur'
          }
        ],
        dispalySn: [
          { required: true, message: '请输入排列序号', trigger: 'blur' },
          {
            validator: this.isNumber,
            message: '数据长度必须是整数数字',
            trigger: 'blur'
          }
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
      rightTitle: '',
      dialogTitle: '新增'
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
    delRelated(data) {
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      orgDelete({ orgId: this.tempDelData.orgId })
        .then(res => {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.handleFilter()
          this.getTreeData(this.listQuery.orgId)
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    addOrg() {
      this.dialogTitle = '新增'
      // this.getPName("addOrgForm");
      this.dialogName = this.rightTitle
      this.addOrgForm.parOrgId = this.listQuery.orgId
      // 获取编码
      orgGenerateCd({ parOrgId: this.listQuery.orgId })
        .then(response => {
          this.addOrgForm.orgCd = response.data.data.orgCode
          this.addOrgForm.dispalySn = response.data.data.dispalySn
          this.addWorkOrder = true
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    addOrgDetail(num) {
      if (num == 1) {
        this.$refs.addOrgForm.validate(valid => {
          if (valid) {
            this.addOrgForm.parOrgId = this.listQuery.orgId
            this.addOrgForm.dispalySn = this.addOrgForm.dispalySn * 1
            if (this.addOrgForm.parOrgId == 0) {
              this.addOrgForm.parOrgId = null
            }
            orgSave(this.addOrgForm)
              .then(res => {
                this.$notify.success('新增组织机构成功')
                this.addWorkOrder = false
                this.getTableData()
                this.getTreeData(this.listQuery.orgId)
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        })
      } else {
        this.$refs.addOrgForm.validate(valid => {
          if (valid) {
            this.addOrgForm.parOrgId = this.listQuery.orgId
            this.addOrgForm.dispalySn = this.addOrgForm.dispalySn * 1
            if (this.addOrgForm.parOrgId == 0) {
              this.addOrgForm.parOrgId = null
            }
            orgUpdateOrg(this.addOrgForm)
              .then(res => {
                this.$notify.success('更新组织机构成功')
                this.addWorkOrder = false
                this.getTableData()
                this.getTreeData(this.listQuery.orgId)
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        })
      }
    },
    showCodenIfo(data) {
      // this.getPName("addOrgForm");
      this.addOrgForm = Object.assign({}, this.addOrgForm, data)
      if (this.addOrgForm.parOrgId == undefined) {
        this.dialogTitle = '编辑'
        this.addWorkOrder = true
        this.dialogName = this.rightTitle
      } else {
        orgDetail({ orgId: this.addOrgForm.parOrgId })
          .then(response => {
            this.dialogName = response.data.data.orgNm
            this.dialogTitle = '编辑'
            this.addWorkOrder = true
          })
          .catch(err => {
            this.$message.error(err.data.errmsg || '出错了')
          })
      }
    },
    hideDialog(num) {
      if (num == 2) {
        this.addWorkOrder = false
        this.addOrgForm = {}
      }
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },
    qingkong() {
      this.listQuery.userId = ''
      this.listQuery.mobile = ''
      this.listQuery.name = ''
      this.listQuery.type = ''
      this.listQuery.repairType = ''
      this.handleFilter()
    },
    filterNode(value, data) {
      if (!value) return true
      return data.orgNm.indexOf(value) !== -1
    },
    deleteCodeInfo() {
      // 删除操作
    },
    getTableData() {
      this.tableLoading = true
      orgList(this.listQuery)
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
    getTreeData(thisId) {
      this.treeLoading = true

      orgDeptTree({ isTop: 1 })
        .then(response => {
          this.treeData = response.data
          this.expandedkeys.push(this.treeData[0].orgId)
          if (thisId != null) {
            this.currentNodekey = thisId
          } else {
            if (this.treeData[0].children.length == 0) {
              this.currentNodekey = this.treeData[0].orgId
            } else {
              this.currentNodekey = this.treeData[0].children[0].orgId
            }
          }
          setTimeout(() => {
            this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
            this.rightTitle = this.$refs.treeDom.getCurrentNode().orgNm
            this.listQuery.orgId =
              this.$refs.treeDom.getCurrentNode().orgId || thisId
            this.getTableData()
          }, 20)
          this.treeLoading = false
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
          this.treeLoading = false
        })
    },
    handleNodeClick(data) {
      /* if(!data.orgId){
            this.$refs.treeDom.setCurrentKey(this.currentNodekey);
            return
        }*/
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().orgId
      this.rightTitle = data.orgNm
      this.listQuery.orgId = data.orgId
      this.handleFilter()
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

