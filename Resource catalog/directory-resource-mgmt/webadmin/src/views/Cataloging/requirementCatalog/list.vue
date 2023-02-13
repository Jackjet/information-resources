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
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :expand-on-click-node="false" :props="defaultProps" highlight-current node-key="orgId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" @node-click="handleNodeClick">
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
        <div class="type_area">{{ rightTitle }}</div>
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="20">
              <el-col :span="8">
                <el-button v-permission="['POST /admin/cataRequire/save']" class="add" size="mini" icon="el-icon-plus" @click="gotoPath(1)">新增</el-button>
                <el-button v-permission="['POST /admin/cataRequire/delete']" class="add" size="mini" icon="el-icon-delete" @click="delChose">删除</el-button>
              </el-col>
              <el-col :span="16">
                <el-button v-permission="['GET /admin/cataRequire/list']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="handleFilter">查询</el-button>
                <el-input v-model="listQuery.nameAndCode" v-permission="['GET /admin/cataRequire/list']" clearable class="filter-item fr" style="width: 35%;" placeholder="信息资源名称或代码" @clear="handleFilter" />
                <!-- <span class="filter_text fr">信息资源名称或代码</span>-->
              </el-col>
            </el-row>
          </div>
          <el-table v-loading="tableLoading" v-permission="['GET /admin/cataRequire/list']" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" />
            <el-table-column prop="code" label="信息资源代码" show-overflow-tooltip />
            <el-table-column prop="name" label="信息资源名称" show-overflow-tooltip />
            <el-table-column label="提供单位" width="200" show-overflow-tooltip>
              <template slot-scope="scope">
                <div v-if="scope.row.provOrgId">{{ scope.row.provOrgName }}</div>
                <div v-else>{{ scope.row.provOrgCode }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="isAccess" label="是否实时获取" width="100">
              <template slot-scope="scope">
                <div v-if="scope.row.isAccess == 1">否</div>
                <div v-else>是</div>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="150" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['POST /admin/cataRequire/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="gotoPath(2,scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/cataBusInfoRel/list']" content="权责清单" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="requirement_busi" class-name @click="gotoPath(3,scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/cataRequire/read']" content="详情" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="detail" class-name @click="gotoPath(4,scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
        </div>
      </el-col>
    </el-row>
    <!--删除信息需求目录-->
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import { getDepartmentTree } from '@/api/departmentRes'
import { getRequirementCatalog, delRequirementCatalog } from '@/api/requirementCatalog'
import Hamburger from '@/components/Hamburger'
export default {
  name: 'RequirementCatalogList',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      tipStr: '确认删除信息需求目录么？',
      addSystemDialog: false,
      serviceObjs: [
        {
          value: '1',
          label: '内部事务'
        }
      ],
      addbusinessForm: {},
      dialogTitle: '新增权责清单',
      listQuery: {
        page: 1,
        limit: 10,
        nameAndCode: '',
        code: ''
      },
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'orgNm'
      },
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      rightTitle: '',
      delDialog: false,
      type: '1',
      selecetCata: [],
      deptId: '',
      reBackDeptId: '',
      provOrgId: '',
      provOrgNm: '',
    }
  },
  activated() {

  },
  created() {
    this.reBackDeptId = this.$store.state.app.rebackdeptid
    this.getTreeData()
  },
  methods: {
    confirmDelete() {
      const ids = []
      this.selecetCata.forEach(item => {
        ids.push(item.id)
      })
      delRequirementCatalog(ids).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.getTableData()
        }
      })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getTableData()
    },
    handleSelectionChange(val) {
      console.log(val)
      this.selecetCata = val
    },
    delChose() {
      if (this.selecetCata.length == null || this.selecetCata.length == 0) {
        this.$message({
          message: '请选择要删除的信息需求目录',
          type: 'error'
        })
        return
      }
      this.delDialog = true
    },
    gotoPath(num, row) {
      switch (num) {
        case 1:
          if (this.$refs.treeDom.getCurrentNode() && this.$refs.treeDom.getCurrentNode().orgId) {
            this.$router.push({
              path: '/Cataloging/requirementCatalogMaintain/0', query: {
                deptId: this.$refs.treeDom.getCurrentNode().orgId,
                provOrgId: this.provOrgId ? this.provOrgId : this.$refs.treeDom.getCurrentNode().orgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
                provOrgName: this.provOrgNm ? this.provOrgNm : this.$refs.treeDom.getCurrentNode().orgNm ? this.$refs.treeDom.getCurrentNode().orgNm : '',
              }

            })
            debugger
          } else {
            this.$message({
              message: '请选择左侧部门',
              type: 'error'
            })
          }
          break
        case 2:
          if (1) {
            this.$router.push({
              path: `/Cataloging/requirementCatalogMaintain/${row.id}`, query: {
                deptId: row.deptId,
                provOrgId: this.provOrgId ? this.provOrgId : this.$refs.treeDom.getCurrentNode().orgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
                provOrgName: this.provOrgNm ? this.provOrgNm : this.$refs.treeDom.getCurrentNode().orgNm ? this.$refs.treeDom.getCurrentNode().orgNm : '',
              }
            })
          } else {
            this.$message({
              message: '请选择左侧部门',
              type: 'error'
            })
          }
          break
        case 3:
          if (1) {
            this.$router.push({
              path: `/Cataloging/relatedBusinessMatters/${row.id}`, query: { deptId: row.deptId }
            })
          } else {
            this.$message({
              message: '请选择左侧部门',
              type: 'error'
            })
          }
          break
        case 4:
          if (1) {
            this.$router.push({
              path: `/Cataloging/requirementCatalogDetail/${row.id}`,
              query: {
                dataResourceName: row.name,
                deptId: row.deptId,
                provOrgId: this.provOrgId ? this.provOrgId : this.$refs.treeDom.getCurrentNode().orgId ? this.$refs.treeDom.getCurrentNode().orgId : '',
                provOrgName: this.provOrgNm ? this.provOrgNm : this.$refs.treeDom.getCurrentNode().orgNm ? this.$refs.treeDom.getCurrentNode().orgNm : '',
              }
            })
          } else {
            this.$message({
              message: '请选择左侧部门',
              type: 'error'
            })
          }
          break
      }
    },
    getTableData() {
      this.tableLoading = true
      const sendData = Object.assign({}, this.listQuery, { deptId: this.$refs.treeDom.getCurrentNode() && this.$refs.treeDom.getCurrentNode().orgId != 0 ? this.$refs.treeDom.getCurrentNode().orgId : '' })
      getRequirementCatalog(sendData).then(res => {
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
      getDepartmentTree({ isTop: 0 }).then(res => {
        if (res.data) {
          this.treeData = res.data
          this.currentNodekey = this.reBackDeptId ? this.reBackDeptId : this.treeData[0].children[0].orgId
          this.expandedkeys.push(this.treeData[0].children[0].orgId)
          setTimeout(() => {
            this.$store.dispatch('setReBackDeptId', '')
            this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
            this.rightTitle = this.$refs.treeDom.getCurrentNode().orgNm
            this.getTableData()
          }, 20)
        }
        this.treeLoading = false
      }).catch(err => {
        this.treeLoading = false
      })
    },
    handleNodeClick(data) {
      if (!data.orgId) {
        this.$refs.treeDom.setCurrentKey(this.currentNodekey)
        return
      }
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().orgId
      this.rightTitle = data.orgNm
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
  line-height: 50px;
  color: rgba(36, 36, 36, 1);
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
  width: 18px;
  height: 18px;
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

