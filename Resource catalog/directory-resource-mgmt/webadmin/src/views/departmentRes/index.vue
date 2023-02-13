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
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" highlight-current node-key="orgId" :expand-on-click-node="false" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :props="defaultProps" @node-click="handleNodeClick">
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
      <!--tab选择区域-->
      <el-col ref="contentDomParents" :xl="20" :lg="19" class="tab_area">
        <div class="type_area">{{ rightTitle }}</div>
        <div class="content_area">
          <el-tabs v-model="activeName" @tab-click="handleTabClick">
            <el-tab-pane label="部门职能职责" name="0">
              <el-form ref="departmentFormDom" :model="departmentForm" label-width="100px" :rules="rules" label-position="left" style="width: 64%;margin-left: 30px">
                <el-form-item label="机构名称：" label-width="100px">
                  <span>{{ departmentName }}</span>
                </el-form-item>
                <el-form-item label="职能职责：" prop="orgDuty" label-width="100px" class="required_label">
                  <el-input v-model="departmentForm.orgDuty" rows="10" type="textarea" />
                </el-form-item>
                <el-form-item label="联系人姓名：" prop="orgLinkman" label-width="100px">
                  <el-input v-model="departmentForm.orgLinkman" placeholder="请输入姓名" />
                </el-form-item>
                <el-form-item label="联系人电话：" prop="orgTel" label-width="100px">
                  <el-input v-model="departmentForm.orgTel" placeholder="多个电话以“，”隔开，手机13812341234，座机028-12341234" />
                </el-form-item>
                <el-form-item label="联系人邮箱：" prop="orgMail" label-width="100px">
                  <el-input v-model="departmentForm.orgMail" placeholder="请输入邮箱" />
                </el-form-item>
                <el-form-item label="传 真：" prop="orgFax" label-width="100px">
                  <el-input v-model="departmentForm.orgFax" placeholder="格式参考 028-12341234" />
                </el-form-item>
                <el-form-item>
                  <el-button v-permission="['POST /admin/org/updateDept']" class="add" @click="submit">保存</el-button>
                  <el-button v-permission="['POST /admin/org/updateDept']" class="remove" @click="resetForm('departmentFormDom','departmentForm')">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            <el-tab-pane v-permission="['GET /admin/org/isCilckBusi']" label="关联权责清单" name="1">
              <!--
              <div class="related_btns">
                <el-button v-permission="['POST /admin/org/saveBusiRef']" class="add" size="mini" icon="el-icon-plus" @click="addRelated">新增</el-button>
                <el-button v-permission="['POST /admin/org/removeBusiList/{deptId}']" class="remove" size="mini" icon="el-icon-close" @click="delBusinesses">移除</el-button>
              </div>
              -->
              <el-table v-loading="tableLoading" v-permission="['GET /admin/org/addBusiPage']" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%">
                <el-table-column prop="busiNo" label="职责/权责清单编码" />
                <el-table-column prop="busiNm" label="职责/权责清单名称" show-overflow-tooltip>
                </el-table-column>
                <el-table-column prop="busiType" label="权责类别" width="100" />

                <el-table-column prop="pId" label="类型" width="50" :formatter="formatterBusiType" />
                <el-table-column label="操作" width="100px" fixed="right">
                  <template slot-scope="scope">
                    <el-tooltip content="详情" effect="light" :open-delay="1000" placement="bottom">
                      <span class="tr_detail_icon fl">
                        <svg-icon icon-class="detail" class-name @click="showDetail(scope.row)" />
                      </span>
                    </el-tooltip>
                    <!--
                    <el-tooltip content="移除"  effect="light" :open-delay="1000" placement="bottom">
                      <span class="tr_del_icon fl">
                        <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                      </span>
                    </el-tooltip>
                    -->
                  </template>
                </el-table-column>
              </el-table>
              <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getTableData" />
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
    <el-dialog :modal-append-to-body="false" :title="'新增权责清单'" align="center" :visible.sync="addRelatedDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog('searchRelatedResults','searchTotal','searchQuery')">
      <el-input v-model="searchQuery.busiNm" class="filter_input" style="width: 50%" placeholder="输入权责清单名称或编码" suffix-icon="el-icon-search" clearable @clear="searchRelateds" />
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
    <el-dialog :modal-append-to-body="false" :title="RelatedDetailTitle" align="center" :visible.sync="RelatedDetailDialog" width="48%" :close-on-click-modal="false" @close="closeDetailDialog">
      <div class="search_result" style="border:1px solid rgba(203,203,203,1);margin-top: 0;margin-bottom: 10px">
        <ul class="detail_ul">
          <li>
            <span class="title" v-if="detailForm.pId==0">职责名称：</span>
            <span class="title" v-if="detailForm.pId!=0">权责名称：</span>
            <span>{{ detailForm.busiNm || '暂无' }}</span>
          </li>
          <li>
            <span class="title" v-if="detailForm.pId==0">职责编码：</span>
            <span class="title" v-if="detailForm.pId!=0">权责编码：</span>
            <span>{{ detailForm.busiNo || '暂无' }}</span>
          </li>
          <li v-if="detailForm.pId!=0">
            <span class="title">权责类别：</span>
            <span>{{ detailForm.busiType || '暂无' }}</span>
          </li>
          <li>
            <span class="title">所支撑的业务系统：</span>
            <span>
              <el-tag v-for="tag in detailForm.appList" v-if="detailForm.appList && detailForm.appList.length>0" :key="tag.busi_id" style="margin-right: 10px" size="small" type="success" :disable-transitions="false">{{ tag.appsysNm }}</el-tag>
              <span v-if="!detailForm.appList || detailForm.appList.length==0">暂无</span>
            </span>
          </li>
          <li>
            <span class="title">备注：</span>
            <span>{{ detailForm.remark || '暂无' }}</span>
          </li>
          <li>
            <span class="title">部门名称：</span>
            <span>{{ detailForm.orgNm || '暂无' }}</span>
          </li>
        </ul>
      </div>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="RelatedDetailDialog=false">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { checkMobile, checkMobileRepeat } from '@/utils/formValidateMixin'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import { isCilckBusi, getDepartmentTree, getDeptOrgTree, getBusinessList, delBusiness, saveDepartment, getOrgDetail, addRelBusList, saveBusiRef } from '@/api/departmentRes'
import { getBusinessDetail } from '@/api/businessManagement'
import Hamburger from '@/components/Hamburger'
export default {
  name: 'DepartmentRes',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    checkMobile
    checkMobileRepeat
    return {
      isSaveSuccess: false,
      delDialog: false,
      listQuery: {
        current: 1,
        size: 10
      },
      treeData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      defaultProps: {
        children: 'children',
        label: 'orgNm'
      },
      activeName: 0,
      tableData: [],
      total: 0,
      departmentForm: {},
      departmentName: '',
      rules: {
        orgDuty: [
          { required: true, message: '请输入职能职责', trigger: 'blur' }
        ],
        orgMail: [
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        orgTel: [
          {
            validator: checkMobile,
            message:
              '请输入正确的手机号或座机号(示例：13112341234,028-12341234)',
            trigger: 'blur'
          },
          {
            validator: checkMobileRepeat,
            message: '输入的手机号码有重复',
            trigger: 'blur'
          }
        ]
      },
      rightTitle: '',
      addRelatedDialog: false,
      RelatedDetailDialog: false,
      RelatedDetailTitle: '关联的权责清单',
      choosedToDelRelatedResults: [],
      choosedRelatedResults: [],
      relatedDetailResults: [],
      detailForm: {},
      tipStr: '确认删除所选权责清单么？',
      tempDelData: {},
      deptId: ''
    }
  },
  created() {
    this.getTreeData()
  },
  methods: {
    // 权责清单详情
    showDetail(data) {
      getBusinessDetail({ busiId: data.busiId }).then(res => {
        if (res.data.errno === 0) {
          this.detailForm = Object.assign({}, res.data.data)
          this.RelatedDetailDialog = true
          this.RelatedDetailTitle = data.pId == 0 ? '关联的职责清单' : '关联的权责清单'
        }
      })
    },
    // 新增权责清单
    addRelated() {
      if (!this.$refs.treeDom.getCurrentNode().orgId) {
        this.$message.error('请选择一个部门')
        return
      }
      this.getSearchData()
      this.addRelatedDialog = true
    },
    delBusinesses() {
      if (!this.$refs.treeDom.getCurrentNode().orgId) {
        this.$message.error('请选择一个部门')
        return
      }
      if (this.choosedToDelRelatedResults.length == 0) {
        this.$message({
          message: '请选择要移除关联的权责清单',
          type: 'error'
        })
        return
      } else {
        this.delDialog = true
      }
    },
    closeDetailDialog() {
      this.relatedDetailResults = []
    },
    searchRelateds() {
      this.searchQuery.current = 1
      this.getSearchData()
    },
    getSearchData() {
      const sendData = Object.assign({}, this.searchQuery, { orgId: this.deptId ? this.deptId : this.$refs.treeDom.getCurrentNode().orgId })
      addRelBusList(sendData).then(res => {
        if (res.data.errno === 0) {
          this.searchRelatedResults = res.data.data.records || []
          this.searchTotal = res.data.data.total
        }
      })
    },
    saveAddRelated() {
      if (this.choosedRelatedResults.length === 0) {
        this.$message({
          message: '请选择您要新增的关联权责清单',
          type: 'error'
        })
        return
      }
      const ids = []
      this.choosedRelatedResults.forEach(item => {
        ids.push(item.busiId)
      })
      const sendData = {
        deptId: this.deptId ? this.deptId : this.$refs.treeDom.getCurrentNode().orgId,
        orgId: this.$refs.treeDom.getCurrentNode().parOrgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
        busiIds: ids
      }
      saveBusiRef(sendData).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('新增关联权责清单成功')
          this.addRelatedDialog = false
          this.getTableData()
        }
      })
      console.log(sendData)
    },
    getTableData() {
      this.tableLoading = true
      const sendData = Object.assign({}, this.listQuery, { orgId: this.$refs.treeDom.getCurrentNode().orgId })
      getBusinessList(sendData).then(res => {
        if (res.data.errno === 0) {
          if (res.data.data && res.data.data.records && res.data.data.records.length > 0) {
            this.tableData = res.data.data.records
          } else {
            this.tableData = []
          }
          this.total = res.data.data.total
        }
        this.tableLoading = false
      }).catch(err => {
        this.tableLoading = false
      })
    },
    getTreeData() {
      this.treeLoading = true
      getDeptOrgTree().then(res => {
        if (res.data) {
          this.treeData = res.data
          // this.currentNodekey = this.currentNodekey ? this.currentNodekey : this.treeData[0].busiNo;
          // this.expandedkeys.push(this.currentNodekey ?  this.currentNodekey : this.treeData[0].busiNo);
          this.currentNodekey = this.treeData[0].children[0].orgId
          this.expandedkeys.push(this.treeData[0].children[0].orgId)
          setTimeout(() => {
            this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
            this.departmentForm = Object.assign({}, this.departmentForm, {
              departmentName: this.$refs.treeDom.getCurrentNode().orgNm
            })
            this.departmentName = this.$refs.treeDom.getCurrentNode().orgNm
            this.rightTitle = this.$refs.treeDom.getCurrentNode().orgNm
            this.getIsCilckBusi()
            this.getCurrentDepartmentDetail()
            this.getTableData()
          }, 20)
        }
        this.treeLoading = false
      }).catch(err => {
        this.treeLoading = false
      })
    },
    getCurrentDepartmentDetail() {
      getOrgDetail({ orgId: this.$refs.treeDom.getCurrentNode().orgId }).then(res => {
        if (res.data.errno === 0) {
          this.departmentForm = Object.assign({}, res.data.data)
        }
      })
    },
    handleNodeClick(data, e) {
      if (!data.orgId) {
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
      this.deptId = orgId
      this.$refs.departmentFormDom.resetFields()
      this.departmentForm = Object.assign({}, this.departmentForm, {
        departmentName: data.orgNm
      })
      this.departmentName = data.orgNm
      this.rightTitle = data.orgNm
      this.getCurrentDepartmentDetail()
      this.getIsCilckBusi()
      this.listQuery.current = 1
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().orgId
      this.getTableData()
    },
    getIsCilckBusi() {
      if (!this.$refs.treeDom.getCurrentNode().parOrgId) {
        this.isSaveSuccess = true
        this.activeName = '0'
        return false
      }
      isCilckBusi({ orgId: this.$refs.treeDom.getCurrentNode().orgId }).then(res => {
        if (res.data.errno == 0) {
          this.isSaveSuccess = res.data.errmsg != '可点击！'
          if (this.isSaveSuccess) {
            this.activeName = '0'
          }
        }
      }).catch(err => {
        this.isSaveSuccess = true
        this.activeName = '0'
      })
    },
    handleTabClick(tab, event) {
      console.log(tab, event)
    },
    handleSelectionChange(val) {
      console.log(val)
      this.choosedToDelRelatedResults = val
    },
    RelatedResultsSelectionChange(val) {
      console.log(val)
      this.choosedRelatedResults = val
    },
    submit() {
      if (!this.$refs.treeDom.getCurrentNode().orgId) {
        this.$message.error('请选择一个部门')
        return
      }
      this.$refs.departmentFormDom.validate(valid => {
        if (valid) {
          this.departmentForm = Object.assign({}, this.departmentForm, {
            orgId: this.$refs.treeDom.getCurrentNode().orgId
          })
          saveDepartment(this.departmentForm).then(res => {
            if (res.data.errno === 0) {
              this.$notify.success('保存成功')
              this.isSaveSuccess = false
            }
          }).catch(err => {
            this.$message.error(err.data.errmsg || '出错了')
          })
        }
      })
    },
    delRelated(data) {
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      const busiIds = []
      const deptId = this.$refs.treeDom.getCurrentNode().orgId
      if (this.choosedToDelRelatedResults.length == 0) {
        busiIds.push(this.tempDelData.busiId)
      } else {
        this.choosedToDelRelatedResults.forEach(item => {
          busiIds.push(item.busiId)
        })
      }
      delBusiness(`/org/removeBusiList/${deptId}`, busiIds).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('移除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getTableData()
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
.search_result {
  margin: 0 auto;
  margin-top: 20px;
}
.detail_ul {
  padding: 0;
  margin: 0;
}
.detail_ul li {
  position: relative;
  min-height: 28px;
  line-height: 28px;
  padding-left: 40px;
  text-align: left;
  display: flex;
  align-items: center;
}
.detail_ul li:nth-of-type(odd) {
  background: #f6f6f6;
}

.detail_ul li > span {
  display: inline-block;
  width: 70%;
  text-align: left;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
}
.detail_ul li > span.title {
  width: 30%;
  min-width: 150px;
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

.tree_title {
  padding-right: 1vh;
  padding-left: 30px;
  height: 60px;
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
.content_area {
  padding-left: 45px;
  padding-right: 45px;
}
.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tree_area {
  background: #f9f9f9;
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}

.tab_area {
}

.related_btns {
  margin-top: 10px;
  margin-bottom: 20px;
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
</style>
