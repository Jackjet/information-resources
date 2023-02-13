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
      <el-col ref="contentDomParents" :xl="20" :lg="19" class="tab_area">
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="18">
              <el-col :span="24" style="margin-bottom:16px">
                <el-button v-permission="['GET /admin/cataInfoTemp/list2']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="getTableData">查询
                </el-button>
                <el-button v-permission="['GET /admin/cataInfoTemp/updateLimitAll']" class="remove fr" size="mini" icon="" @click="updateLimitAll">全部已读
                </el-button>
                <el-input v-model="listQuery.uviewNo" v-permission="['GET /admin/cataInfoTemp/list2']" clearable class="filter-item searchInputWidth" placeholder="信息资源代码" />
                <el-input v-model="listQuery.uviewNm" v-permission="['GET /admin/cataInfoTemp/list2']" clearable class="filter-item searchInputWidth" placeholder="信息资源名称" />
                <el-select v-model="listQuery.updateCyc" v-permission="['GET /admin/cataInfoTemp/list2']" clearable placeholder="更新周期" class="searchInputWidth selectRadius" @change="handleFilter">
                  <el-option label="每日" class="filter-item " value="02" />
                  <el-option label="每周" class="filter-item " value="03" />
                  <el-option label="每月" class="filter-item " value="04" />
                  <el-option label="每季度" class="filter-item " value="05" />
                  <el-option label="每半年" class="filter-item " value="06" />
                  <el-option label="每年" class="filter-item " value="07" />
                </el-select>
              </el-col>

            </el-row>
          </div>
          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column prop="uviewNo" label="信息资源代码" show-overflow-tooltip />
            <el-table-column prop="uviewNm" label="信息资源名称" show-overflow-tooltip />
            <el-table-column prop="provOrgName" label="提供单位" width="" show-overflow-tooltip />
            <el-table-column prop="crtDt" label="更新时间" width="150" />
            <el-table-column prop="updateCyc" label="更新周期" width="80" :formatter="formatterUpdateCyc">
            </el-table-column>
            <el-table-column prop="updateDt" label="到期日期" width="150">
            </el-table-column>
            <el-table-column label="操作" width="85" fixed="right">
              <template slot-scope="scope">
                <el-tooltip v-permission="['GET /admin/cataInfoTemp/read']" content="详情" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="detail" class-name="" @click="showDetail(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-if="scope.row.updateLimitReaded == null || scope.row.updateLimitReaded != 1" v-permission="['GET /admin/cataInfoTemp/updateLimit']" content="已读" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="shenhe" @click="updateLimit(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import { getDepartmentTree } from '@/api/departmentRes'
import Hamburger from '@/components/Hamburger'
import { getTableList2, updateLimit, updateLimitAll } from '@/api/resCatalog'
export default {
  name: 'DepartmentRes',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      listQuery: {
        limit: 10,
        current: 1,
        size: 10,
        uviewNo: '',
        uviewNm: '',
        updateCyc: '',
        orgId: ''
      },
      treeData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      defaultProps: {
        children: 'children',
        label: 'orgNm'
      },
      tableData: [],
      total: 0
    }
  },
  created() {
    this.getTreeData()
  },
  methods: {
    getTreeData() {
      this.treeLoading = true
      getDepartmentTree().then(res => {
        if (res.data) {
          this.treeData = res.data
          if (this.treeData[0].children.length == 1) {
            this.currentNodekey = this.treeData[0].children[0].orgId
            this.expandedkeys.push(this.treeData[0].children[0].orgId)
            this.listQuery.orgId = this.treeData[0].children[0].orgId
          }
          setTimeout(() => {
            if (this.treeData[0].children.length == 1) {
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
            }
            this.getTableData()
          }, 20)
        }
        this.treeLoading = false
      }).catch(err => {
        this.treeLoading = false
      })
    },
    getTableData() {
      this.tableLoading = true
      getTableList2(this.listQuery)
        .then(response => {
          this.tableData = response.data.data.records
          this.total = response.data.data.total
          this.tableLoading = false
        })
        .catch(() => {
          this.total = 0
          this.tableLoading = false
        })
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getTableData()
    },
    handleNodeClick(data, e) {
      this.listQuery.orgId = data.orgId
      this.handleFilter()
    },
    formatterUpdateCyc(row, column, cellValue, index) {
      if (cellValue == '02') {
        return '每日'
      } else if (cellValue == '03') {
        return '每周'
      } else if (cellValue == '04') {
        return '每月'
      } else if (cellValue == '05') {
        return '每季度'
      } else if (cellValue == '06') {
        return '每半年'
      } else if (cellValue == '07') {
        return '每年'
      }

    },
    // 详情
    showDetail(data) {
      this.$router.push({
        path: `/Cataloging/infoCatalogDetail/${data.uviewId}`
      })
    },
    updateLimit(data) {
      updateLimit({ id: data.uviewId })
        .then(response => {
          this.$notify.success('已读成功')
          this.handleFilter()
        })
    },
    updateLimitAll() {
      updateLimitAll(this.listQuery)
        .then(response => {
          this.$notify.success('全部已读成功')
          this.handleFilter()
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

