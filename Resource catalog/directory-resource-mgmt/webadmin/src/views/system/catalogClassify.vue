<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">目录分类</span>
            <el-tooltip :content="treeExpansion ? '收起' : '展开'" effect="light" :open-delay="1000" placement="bottom">
              <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
            </el-tooltip>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" highlight-current node-key="typId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :expand-on-click-node="false" @node-click="handleNodeClick">
                <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
                    <span class="overflowEllips">
                      <span class="level">{{ data.typType.slice(0,1) }}</span>
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
        <div class="type_area">{{ rightTitle }}</div>
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="20">
              <el-col :span="3">
                <el-button v-if="canClick" v-permission="['POST /admin/dictAssetCate/add']" class="add" size="mini" icon="el-icon-plus" @click="addMenu">新增分类</el-button>
              </el-col>
              <el-col :span="21">
                <el-button v-if="canClick" v-permission="['GET /admin/dictAssetCate/list']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="getTableData">查询</el-button>
                <el-input v-model="listQuery.name" clearable class="filter-item fr" style="width: 35%" placeholder="目录分类名称" @clear="handleFilter" />
                <!-- <span class="filter_text fr">目录分类名称</span>-->
              </el-col>
            </el-row>
          </div>
          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column prop="typCd" width="200px" label="目录分类编码" />
            <el-table-column prop="typNm" label="目录分类名称" show-overflow-tooltip />
            <el-table-column prop="remark" label="备注" show-overflow-tooltip />
            <el-table-column prop="state" label="状态">
              <template slot-scope="scope">
                <div v-if="scope.row.status==2">
                  <span class="state_err">禁用</span>
                </div>
                <div v-else>
                  <span class="state_ok">启用</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150px" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['POST /admin/dictAssetCate/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/dictAssetCate/isDisabled']" :content="scope.row.status == 2 ? '启用':'禁用'" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon v-if="scope.row.status == 2" icon-class="qiyong" class-name @click="caozuo(1,scope.row)" />
                    <svg-icon v-else icon-class="jinyong" class-name @click="caozuo(2,scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip content="移除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon v-permission="['GET /admin/dictAssetCate/delete']" icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>
      </el-col>
    </el-row>
    <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addMenuForm','addMenuForm')">
      <el-form ref="addMenuForm" :model="addMenuForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
        <el-form-item label="上级分类：" class="required_label" label-width="86px">
          <span>{{ rightTitle }}</span>
        </el-form-item>
        <el-form-item label="分类编码：" class="required_label" label-width="86px" prop="typCd">
          <el-input v-model="addMenuForm.typCd" style="width:100%" placeholder="分类编码" disabled readonly />
        </el-form-item>
        <el-form-item label="分类名称：" class="required_label" label-width="86px" prop="typNm">
          <el-input v-model="addMenuForm.typNm" style="width:100%" placeholder="请输入部门名称（例：天津市政府办公厅）" />
        </el-form-item>
        <el-form-item v-if="showOrignid" label="所属部门" class="required_label" prop="orgId" label-width="86px">
          <select-tree v-model="addMenuForm.orgId" :select-ids="thisOrgIdAdd" :is-multiple="false" :check-strictly="true" :select-treedata="orgTree" node-key="orgId" placeholder="所属部门" :disabled=" this.thisOrgId==null?false:true" @checkedChoose="getAddPeopleChose(arguments)">
            <template slot="prepend">所属部门</template>
          </select-tree>
        </el-form-item>
        <el-form-item label="显示序号：" class="required_label" label-width="86px" prop="displaySn">
          <el-input v-model="addMenuForm.displaySn" type="number" style="width:100%" placeholder="请输入序号" />
        </el-form-item>
        <el-form-item label="备注：" label-width="86px">
          <el-input v-model="addMenuForm.remark" :rows="5" type="textarea" style="width:100%" />
        </el-form-item>
      </el-form>
      <div class="dialogDom" align="center">
        <el-button v-permission="['POST /admin/dictAssetCate/add','POST /admin/dictAssetCate/update']" size="mini" class="add" @click="dialogTitle=='编辑'?addMenuDetail(2):addMenuDetail(1)">保 存</el-button>
        <el-button size="mini" class="remove" @click="hideDialog(2)">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
  </div>
</template>

<script>
import {
  getAllTree,
  getDeptTree,
  getPowerTree,
  updataCata,
  addCata,
  getlist,
  deleteCata,
  generateCode,
  getCataDetail,
  ableCata,
  getCataOrderNum
} from '@/api/assetCata'
import { getUserInfo } from '@/api/login'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import Hamburger from '@/components/Hamburger'
import SelectTree from '@/components/SelectTree'
import DelConfirm from '@/components/DelConfirm'
export default {
  name: 'CatalogClassify',
  components: { Pagination, DelConfirm, Hamburger, SelectTree },
  mixins: [mixinJs],
  data() {
    return {
      tempDelData: {},
      showOrignid: false,
      tipStr: '确定要删除所选分类吗？',
      delDialog: false,
      addMenuForm: { typCd: null, fullTypCd: null, typType: null },
      dialogTitle: '新增目录分类',
      addWorkOrder: false,
      filterText: '',
      canClick: false,
      listQuery: {
        page: 1,
        size: 10,
        name: '',
        pid: ''
      },
      mapIcon: mapIcon,
      treeData: null,
      // 新增表单校验
      createRules: {
        // 暂未添加
        typNm: [{ required: true, message: '请输入分类名称', trigger: 'blur' }],
        typCd: [{ required: true, message: '请输入分类编码', trigger: 'blur' }],
        displaySn: [
          { required: true, message: '请输入序号', trigger: 'blur' },
          {
            validator: this.isNumber,
            message: '数据长度必须是整数数字',
            trigger: 'blur'
          }
        ],
        orgId: [
          { required: true, message: '请选择所属部门', trigger: 'change' }
        ]
      },
      defaultProps: {
        children: 'children',
        label: 'typNm'
      },
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      rightTitle: '',
      thisRoles: [], // 角色
      thisOrgId: [], // 组织机构id
      thisOrgIdAdd: [], //
      thisTreeNum: 0, // 0 中心和系统管理员 获取全部树 1：部门管理员获取指定树
      orgTree: []
    }
  },
  watch: {
    filterText(val) {
      this.$refs.treeDom.filter(val)
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      getDeptTree({ isTop: 0 })
        .then(response => {
          this.orgTree = response.data
          getUserInfo()
            .then(res => {
              this.thisRoles = res.data.data.roles
              if (this.thisRoles[0] == '部门资源管理员') {
                this.thisTreeNum = 1
              } else {
                this.thisTreeNum = 0
              }
              this.getTreeData(this.thisTreeNum)
              if (res.data.data.depts == undefined) {
                this.thisOrgId = undefined
              } else {
                this.thisOrgId = [res.data.data.depts[0].key]
              }
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    delRelated(data) {
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      deleteCata({ id: this.tempDelData.typId })
        .then(res => {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getTableData()
          this.getTreeData(this.thisTreeNum, this.listQuery.pid)
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    getAddPeopleChose(args) {
      if (args[1].length > 0) {
        this.addMenuForm.orgId = args[1][0].orgId
      } else {
        this.addMenuForm.orgId = null
      }
    },
    addMenuDetail(num) {
      if (num == 1) {
        this.$refs.addMenuForm.validate(valid => {
          if (valid) {
            this.addMenuForm.displaySn = this.addMenuForm.displaySn * 1
            this.addMenuForm.status = 1
            // this.addMenuForm.orgId = this.showOrignid ? this.thisOrgId : null;
            addCata(this.addMenuForm)
              .then(res => {
                this.$notify.success('新增分类成功')
                this.addWorkOrder = false
                this.getTableData()
                this.getTreeData(this.thisTreeNum, this.listQuery.pid)
              })
              .catch(response => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
          }
        })
      } else {
        this.$refs.addMenuForm.validate(valid => {
          if (valid) {
            this.addMenuForm.displaySn = this.addMenuForm.displaySn * 1
            this.addMenuForm.status = 1
            // this.addMenuForm.orgId = this.showOrignid ? this.thisOrgId : null;
            updataCata(this.addMenuForm)
              .then(res => {
                this.$notify.success('更新分类成功')
                this.addWorkOrder = false
                this.getTableData()
                this.getTreeData(this.thisTreeNum, this.listQuery.pid)
              })
              .catch(response => {
                this.$notify.error({
                  title: '失败',
                  message: response.data.errmsg
                })
              })
          }
        })
      }
    },
    caozuo(num, row) {
      ableCata({ id: row.typId, status: num })
        .then(response => {
          this.$notify.success('操作成功')
          this.getTableData()
          this.getTreeData(this.thisTreeNum, this.listQuery.pid)
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    addMenu() {
      if (this.rightTitle == '') {
        this.$message.error('未选择根目录')
        return false
      }
      this.dialogTitle = '新增目录分类'
      this.addWorkOrder = true
      this.addMenuForm.parTypId = this.listQuery.pid
      this.thisOrgIdAdd = []
      if (this.thisTreeNum == 1) {
        this.thisOrgIdAdd = this.thisOrgId
        this.addMenuForm.orgId = this.thisOrgId[0]
      }
      // 获取编码
      generateCode({ pId: this.listQuery.pid })
        .then(response => {
          this.addMenuForm.typCd = response.data.data.code
          this.addMenuForm.fullTypCd = response.data.data.fullCode
          this.addMenuForm.typType = response.data.data.typType
          this.addMenuForm.orgId = response.data.data.orgId
          if (response.data.data.typType == '细目') {
            this.showOrignid = false;
          }
          // 获取排序序号
          getCataOrderNum({ pId: this.listQuery.pid })
            .then(res => {
              if (res.data.errno === 0) {
                this.addMenuForm = Object.assign({}, this.addMenuForm, {
                  displaySn: res.data.data * 1
                })
                this.addWorkOrder = true
              }
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })

      // this.getPName("addMenuForm");
    },
    showCodenIfo(data) {
      this.getPName('addMenuForm')
      this.addMenuForm = Object.assign({}, this.addMenuForm, data)
      if (this.addMenuForm.typType == '细目') {
        this.showOrignid = false;
      }
      this.dialogTitle = '编辑'
      this.addWorkOrder = true
      this.thisOrgIdAdd = []
      if (this.thisTreeNum == 1) {
        this.thisOrgIdAdd = [this.thisOrgId]
      } else {
        if (data.orgId == undefined) {
          this.thisOrgIdAdd = []
        } else {
          this.thisOrgIdAdd = [data.orgId]
        }
      }
    },
    hideDialog(num) {
      if (num == 2) {
        this.addWorkOrder = false
        this.addCodeForm = {}
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getTableData()
    },

    getTableData() {
      this.tableLoading = true
      getlist(this.listQuery)
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
    getTreeData(num, thisId) {
      // this.expandedkeys.push(this.treeData[0].id);
      this.treeLoading = true
      if (num == 0) {
        getAllTree({ roleId: null })
          .then(response => {
            this.treeData = response.data.data
            if (thisId != null) {
              this.currentNodekey = thisId
            } else {
              this.currentNodekey = this.treeData[0].typId
            }
            setTimeout(() => {
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
              this.rightTitle = this.$refs.treeDom.getCurrentNode().typNm
              this.listQuery.pid = this.$refs.treeDom.getCurrentNode().typId
              this.canClick = true
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
      } else {
        getPowerTree({ roleId: null })
          .then(response => {
            this.treeData = response.data.data
            if (thisId != null) {
              this.currentNodekey = thisId
            } else {
              this.currentNodekey = this.treeData[0].typId
            }
            setTimeout(() => {
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
              this.rightTitle = this.$refs.treeDom.getCurrentNode().typNm
              this.listQuery.pid = this.$refs.treeDom.getCurrentNode().typId

              // this.getTableData();
            }, 20)
            this.treeLoading = false
          })
          .catch(response => {
            this.treeLoading = false
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
      }
    },
    handleNodeClick(data, e) {
      const level = e.level
      this.rightTitle = data.typNm
      this.listQuery.pid = data.typId
      // 用于添加目 细目的所属部门
      if (data.typType == '项' || data.typType == '目') {
        this.showOrignid = true
      } else {
        this.showOrignid = false
      }
      // 用于部门管理员 不可操作项类 相关的
      if (this.thisTreeNum == 1) {
        if (data.typType == '项' || data.typType == '类') {
          this.tableData = []
          this.total = 0
          this.canClick = false
          this.tableData = []
          return false
        } else {
          this.canClick = true
        }
      }
      this.listQuery.page = 1
      this.getTableData()
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

