<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">部门树</span>
            <el-tooltip v-permission="['GET /admin/cataInfoTemp/read']" :content="treeExpansion ? '收起' : '展开'" effect="light" :open-delay="1000" placement="bottom">
              <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
            </el-tooltip>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" highlight-current :expand-on-click-node="false" node-key="orgId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" @node-click="handleNodeClick">
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
                <el-button v-permission="['POST /admin/archAppSys/saveOrUpdate']" class="add" size="mini" icon="el-icon-plus" @click="addMenu">新增应用系统</el-button>
              </el-col>
              <el-col :span="21">
                <el-button v-permission="['GET /admin/archAppSys/list']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="handleFilter">查询</el-button>
                <el-input v-model="listQuery.appsysNm" v-permission="['GET /admin/archAppSys/list']" clearable class="filter-item fr" style="width: 35%" placeholder="应用系统名称" @clear="handleFilter" />
                <!-- <span class="filter_text fr">应用系统名称</span>-->
              </el-col>
            </el-row>
          </div>
          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column type="index" label="序号" />
            <el-table-column prop="appsysNo" label="应用系统编码" />
            <el-table-column prop="appsysNm" label="应用系统名称" show-overflow-tooltip>
              <!--<template slot-scope="scope">
                <el-tooltip class="item"  effect="light" :open-delay="1000" :content="scope.row.appsysNm" placement="bottom-start">
                  <div class="overflowEllips">{{scope.row.appsysNm}}</div>
                </el-tooltip>
              </template>-->
            </el-table-column>
            <!-- <el-table-column prop="appsysNm" label="应用系统名称" />-->
            <el-table-column prop="appsysDesc" label="应用系统简介" show-overflow-tooltip />
            <el-table-column label="操作" width="150" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['POST /admin/archAppSys/saveOrUpdate']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['POST /admin/archAppSys/delete/{appsysId}']" content="移除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['POST /admin/archAppSys/delete/{appsysId}']" content="资源关系图" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="guanxi" class-name @click="gotoPath(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>

        <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addbusinessForm','addbusinessForm')">
          <el-form ref="addbusinessForm" :model="addbusinessForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="部门名称：" label-width="140px" prop="departmentName">
              <span>{{ addbusinessForm.departmentName }}</span>
            </el-form-item>
            <el-form-item label="内设部门：" class label-width="140px">
              <select-tree v-if="addWorkOrder==true" ref="selectTreeDom" :node-key="'orgId'" :is-multiple="false" :check-strictly="true" :select-ids="selectOrgs" :select-treedata="selectTreedata" @checkedChoose="getCheckedOrg(arguments)" placeholder="请输入内设部门" />
            </el-form-item>
            <el-form-item label="应用系统代码：" class="required_label" label-width="140px" prop="appsysNo">
              <el-input v-model="addbusinessForm.appsysNo" style="width:100%" disabled />
            </el-form-item>
            <el-form-item label="应用系统名称：" class="required_label" label-width="140px" prop="appsysNm">
              <el-input v-model="addbusinessForm.appsysNm" style="width:100%" placeholder="请输入应用系统名称" />
            </el-form-item>
            <el-form-item label="应用系统简介：" class="required_label" label-width="140px">
              <el-input v-model="addbusinessForm.appsysDesc" :rows="2" type="textarea" style="width:100%" />
            </el-form-item>

            <el-form-item label="建设性质：" label-width="140px" prop="nature" class>
              <el-select v-model="addbusinessForm.nature" style="width:100%" placeholder="建设性质">
                <el-option v-for="item in buildNatures" :key="item.value" :label="item.name" :value="item.value" />
              </el-select>
            </el-form-item>

            <el-form-item label="部署位置：" label-width="140px" prop="position" class>
              <el-select v-model="addbusinessForm.position" style="width:100%" placeholder="部署位置">
                <el-option v-for="item in deployLocations" :key="item.value" :label="item.name" :value="item.value" />
              </el-select>
            </el-form-item>

            <el-form-item label="接入网络类型：" label-width="140px" prop="netType" class>
              <el-select v-model="addbusinessForm.netType" style="width:100%" placeholder="接入网络类型">
                <el-option v-for="item in networdTypes" :key="item.value" :label="item.name" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item label="所支撑的权责清单：" class="required_label" label-width="140px">
              <el-input v-if="!addbusinessForm.busis || addbusinessForm.busis.length == 0" style="cursor: pointer;width:100%" placeholder="所支撑的权责清单" readonly suffix-icon="el-icon-plus" @click.native="showSystems" />
              <el-tag v-for="tag in addbusinessForm.busis" v-if="addbusinessForm.busis && addbusinessForm.busis.length > 0&&tag!=null" :key="tag.busiId" closable class="posTag" style="margin-right: 10px;" size="small" type="success" :disable-transitions="false" @close="handleClose(tag)">
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="tag.busiNm">
                  <span>{{ tag.busiNm }}</span>
                </el-tooltip>
              </el-tag>
              <svg-icon v-if="addbusinessForm.busis && addbusinessForm.busis.length > 0" icon-class="add_circle_green" style="cursor: pointer" @click="showSystems" />
            </el-form-item>
            <el-form-item label="备注：" label-width="140px">
              <el-input v-model="addbusinessForm.remark" :rows="5" type="textarea" style="width:100%" maxlength="255" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="addMenuDetail">保 存</el-button>
            <el-button size="mini" class="remove" @click="addWorkOrder=false">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
          <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="'所支撑的权责清单'" align="center" :visible.sync="addRelatedDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog('searchRelatedResults','searchTotal','searchQuery')">
          <el-input v-model="searchQuery.busiNmOrBusiNo" class="filter_input" style="width: 50%" placeholder="输入权责清单名称或编码" suffix-icon="el-icon-search" clearable @clear="searchRelateds" />
          <el-button class="remove" size="mini" icon="el-icon-search" @click="searchRelateds">查询</el-button>
          <div class="search_result">
            <el-table :data="searchRelatedResults" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="RelatedResultsSelectionChange">
              <el-table-column type="selection" width="55" />
              <el-table-column prop="busiNo" label="权责清单编码" width />
              <el-table-column prop="busiNm" label="权责清单名称" width show-overflow-tooltip />
            </el-table>
            <pagination v-show="searchTotal>0" :layout="'prev,pager,next'" :total="searchTotal" :small="true" :background="false" :page.sync="searchQuery.current" :limit.sync="searchQuery.size" @pagination="getSearchData" />
          </div>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="saveAddRelated">保 存</el-button>
            <el-button size="mini" class="remove" @click="addRelatedDialog=false">取 消</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import SelectTree from '@/components/SelectTree'
import { getDictByType } from '@/api/departmentRes'
import {
  getOSList,
  delOS,
  getOSDetail,
  saveOS,
  getDepartmentTree,
  getOrgTree,
  getBusiPage,
  getCode
} from '@/api/OSManagement'
import Hamburger from '@/components/Hamburger'
export default {
  name: 'OSManagement',
  components: { Pagination, DelConfirm, SelectTree, Hamburger },
  mixins: [mixinJs],
  data() {
    const checkAppsysNo = function (rule, value, callback) {
      if (value) {
        var reg = /^[a-zA-Z].*\d$/
        var reg1 = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        if (!reg.test(value) || value.length < 2 || reg1.test(value)) {
          return callback(new Error(rule.message))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    return {
      selectTreedata: [],
      addRelatedDialog: false,
      serviceObjs: [
        {
          value: '1',
          label: '内部事务'
        }
      ],
      addbusinessForm: {},
      dialogTitle: '新增应用系统',
      addWorkOrder: false,
      filterText: '',
      listQuery: {
        current: 1,
        size: 10,
        appsysNm: ''
      },
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加
        appsysNo: [
          { required: true, message: '请输入应用系统代码', trigger: 'blur' },
          {
            validator: checkAppsysNo,
            message: '应用系统代码应字母开头数字结尾,并不能输入中文',
            trigger: 'blur'
          }
        ],
        /* nature: [
          { required: true, message: "请选择建设性质", trigger: "change" }
        ],
        position: [
          { required: true, message: "请选择部署位置", trigger: "change" }
        ],
        netType: [
          { required: true, message: "请选择接入网络类型", trigger: "change" }
        ],
        nature: [
          { required: true, message: "请选择建设性质", trigger: "change" }
        ],*/
        appsysNm: [
          { required: true, message: '请输入应用系统名称', trigger: 'blur' }
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
      tempDelData: {},
      tipStr: '确认删除所选应用系统吗？',
      delDialog: false,
      selectOrgs: [],
      selectOrgVal: '',
      checkedTreeNodes: '',
      buildNatures: [],
      deployLocations: [],
      networdTypes: [],
      choosedRelatedResults: [],
      reBackOSId: ''
    }
  },
  created() {
    this.reBackOSId = this.$store.state.app.OSReBackDeptId
    this.getTreeData()
    // 获取数据字典选择项
    getDictByType({ type: 'build_nature' }).then(res => {
      if (res.data.errno === 0) {
        this.buildNatures = res.data.data
      }
    })
    getDictByType({ type: 'deploy_location' }).then(res => {
      if (res.data.errno === 0) {
        this.deployLocations = res.data.data
      }
    })
    getDictByType({ type: 'network_type' }).then(res => {
      if (res.data.errno === 0) {
        this.networdTypes = res.data.data
      }
    })
  },
  methods: {
    gotoPath(data) {
      this.$router.push({
        path: `/OSManagement/OSManagementChart/${data.appsysId}`,
        query: {
          deptId: this.$refs.treeDom.getCurrentNode().orgId,
          pidDeptName: this.departmentName
        }
      })
    },
    saveAddRelated() {
      if (this.choosedRelatedResults.length == 0) {
        this.$message({
          message: '请选择所支撑的权责清单',
          type: 'error'
        })
        return
      }
      let flag = true
      console.log(this.addbusinessForm.busis)
      this.choosedRelatedResults.forEach(item => {
        if (
          this.addbusinessForm.busis &&
          this.addbusinessForm.busis.length > 0
        ) {
          this.addbusinessForm.busis.forEach(item1 => {
            if (item1.busiId == item.busiId) {
              flag = false
              return false
            }
          })
        }
      })
      if (flag) {
        this.addRelatedDialog = false
        this.addbusinessForm = Object.assign({}, this.addbusinessForm, {
          busis: this.choosedRelatedResults.concat(
            this.addbusinessForm.busis || []
          )
        })
      } else {
        this.$message({
          message: '所选择的权责清单中与已新增的权责清单有重复项',
          type: 'error'
        })
        return
      }
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },
    handleClose(tag) {
      this.addbusinessForm.busis.splice(
        this.addbusinessForm.busis.indexOf(tag),
        1
      )
    },
    addMenuDetail() {
      this.$refs.addbusinessForm.validate(valid => {
        if (valid) {
          const busis = []
          if (
            this.addbusinessForm.busis &&
            this.addbusinessForm.busis.length > 0
          ) {
            this.addbusinessForm.busis.forEach(item => {
              if (item.id || item.busiId) {
                busis.push(item.id || item.busiId)
              }
            })
          }
          const sendData = Object.assign({}, this.addbusinessForm, {
            busiIds: busis,
            provOrgId: this.addbusinessForm.provOrgId
              ? this.addbusinessForm.provOrgId
              : this.$refs.treeDom.getCurrentNode().orgId
          })
          saveOS(sendData)
            .then(res => {
              if (res.data.errno === 0) {
                this.$notify.success(`${this.dialogTitle}成功`)
                this.addWorkOrder = false
                this.listQuery.current = 1
                this.getTableData()
              }
            })
            .catch(err => {
              this.$message.error(err.data.errmsg || '出错了')
            })
        }
      })
    },
    addMenu() {
      if (!this.$refs.treeDom.getCurrentNode().orgId) {
        this.$message.error('请选择一个部门')
        return
      }
      getCode().then(res => { this.addbusinessForm.appsysNo = res.data.errmsg })
      this.dialogTitle = '新增应用系统'
      this.selectOrgs = []
      getOrgTree({ orgId: this.$refs.treeDom.getCurrentNode().orgId }).then(
        res => {
          if (res.data) {
            this.selectTreedata = res.data.data
          }
        }
      )
      this.addWorkOrder = true
      this.getPName1('addbusinessForm')
    },
    getCheckedOrg(args) {
      this.selectOrgVal = args[0]
      this.checkedTreeNodes = args[1]
      this.addbusinessForm.belongTo =
        args[1] && args[1].length > 0
          ? args[1][0].orgId
          : this.addbusinessForm.belongTo
    },
    showCodenIfo(data) {
      getOrgTree({ orgId: data.provOrgId }).then(res => {
        if (res.data) {
          this.selectTreedata = res.data.data
          getOSDetail({ appId: data.appsysId }).then(res1 => {
            if (res.data.errno === 0) {
              console.log(res1.data.data)
              this.getPName1('addbusinessForm')
              this.addbusinessForm = Object.assign(
                {},
                this.addbusinessForm,
                res1.data.data
              )
              this.selectOrgs = []
              this.dialogTitle = '编辑应用系统'
              setTimeout(() => {
                this.addbusinessForm.belongTo
                  ? this.selectOrgs.push(this.addbusinessForm.belongTo)
                  : (this.selectOrgs = [])
                this.addWorkOrder = true
              }, 20)
            }
          })
        }
      })
    },

    getTableData() {
      this.tableLoading = true
      const sendData = Object.assign({}, this.listQuery, {
        orgId: this.$refs.treeDom.getCurrentNode().orgId
      })
      getOSList(sendData)
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
            this.total = res.data.data.total
          }
          this.tableLoading = false
        })
        .catch(err => {
          this.tableLoading = false
        })
    },
    getTreeData() {
      this.treeLoading = true
      getDepartmentTree()
        .then(res => {
          if (res.data) {
            this.treeData = res.data
            this.currentNodekey = this.reBackOSId
              ? this.reBackOSId
              : this.treeData[0].children[0].orgId
            this.expandedkeys.push(this.treeData[0].children[0].orgId)
            setTimeout(() => {
              this.$store.dispatch('setReBackOSId', '')
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
              this.departmentForm = Object.assign({}, this.departmentForm, {
                departmentName: this.$refs.treeDom.getCurrentNode().orgNm
              })
              this.departmentName = this.$refs.treeDom.getCurrentNode().orgNm
              this.rightTitle = this.$refs.treeDom.getCurrentNode().orgNm
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
      console.log(data)
      if (!data.orgId) {
        this.$refs.treeDom.setCurrentKey(this.currentNodekey)
        return
      }
      this.rightTitle = data.orgNm
      this.listQuery.current = 1
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().orgId
      this.getTableData()
    },
    RelatedResultsSelectionChange(val) {
      console.log(val)
      this.choosedRelatedResults = val
    },
    searchRelateds() {
      this.searchQuery.current = 1
      this.getSearchData()
    },
    getSearchData() {
      this.searchQuery.size = 10;
      // 查询所支撑的权责清单
      const sendData = Object.assign({}, this.searchQuery, {
        orgId: this.$refs.treeDom.getCurrentNode().orgId,
        appId: this.addbusinessForm.appsysId || ''
      })
      getBusiPage(sendData).then(res => {
        if (res.data.errno === 0) {
          this.searchRelatedResults = res.data.data.records
          this.searchTotal = res.data.data.total
        }
      })
    },

    showSystems() {
      this.addRelatedDialog = true
      this.searchQuery.current = 1
      this.getSearchData()
    },
    delRelated(data) {
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      delOS(`/archAppSys/delete/${this.tempDelData.appsysId}`).then(res => {
        if (res.data.errno === 0) {
          console.log(this.tempDelData)
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getTableData()
        }
      }).catch(err => {
        this.$message.error(err.data.errmsg || '出错了')
      })
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
.el-tag .el-icon-close {
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
</style>

