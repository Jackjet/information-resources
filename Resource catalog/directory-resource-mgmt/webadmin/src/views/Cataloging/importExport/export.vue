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
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" highlight-current node-key="orgId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :expand-on-click-node="false" @node-click="handleNodeClick">
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
              <el-col :span="4">
                <el-button v-permission="['GET /admin/cataExport/export']" :loading="downloadBtn" class="add" size="mini" @click="exportFile">
                  <svg-icon icon-class="download" style="margin-right: 5px" />生成并下载编制表
                </el-button>
              </el-col>
              <el-col :span="3">
                <el-button v-permission="['GET /admin/cataExport/exportCity']" :loading="downloadBtn1" class="add" size="mini" @click="exportFileCity">
                  <svg-icon icon-class="download" style="margin-right: 5px" />市表导出
                </el-button>
              </el-col>
              <el-col :span="17">
                <el-button class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="getTableData">查询</el-button>
                <el-date-picker v-model="timeRange" style="" class="filter-item fr" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="起始日期" end-placeholder="截至日期" @change="handleFilter" />
                <span class="filter_text fr">下载日期</span>
              </el-col>
            </el-row>
          </div>
          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
            <el-table-column type="index" label="序号" />
            <el-table-column prop="fileName" label="文件名称" show-overflow-tooltip>
              <template slot-scope="scope">
                <a href="javascript:;" class="file_name" @click="downloadFileIframe(`${downloadPath}/${scope.row.fileKey}`)">{{ scope.row.fileName }}</a>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="下载时间" />
            <el-table-column prop="createBy" label="操作人" />
            <el-table-column label="操作" fixed="right">
              <template slot-scope="scope">
                <el-tooltip content="删除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
        </div>
      </el-col>
    </el-row>
    <!--删除文件-->
    <el-dialog title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
    <!--异常详情-->
    <el-dialog :title="'导入异常详情'" align="center" :visible.sync="detailDialog" width="48%" :close-on-click-modal="false">
      <div class="search_result" style="margin-top: 0;margin-bottom: 10px">
        <el-input v-model="detailForm.reason" rows="5" type="textarea" readonly />
      </div>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="detailDialog=false">关闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import Hamburger from '@/components/Hamburger'
import { getDepartmentTree } from '@/api/departmentRes'
import {
  exportFile, exportDel, getExportList
} from '@/api/catalogImport'
export default {
  name: 'CatalogingExport',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      timeRange: null,
      dialogTitle: '导入异常详情',
      detailDialog: false,
      filterText: '',
      listQuery: {
        limit: 10,
        page: 1,
        startDate: '',
        endDate: '',
        depId: ''
      },
      mapIcon: mapIcon,
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
      tipStr: '确认删除所选文件吗？',
      delDialog: false,
      detailForm: {

      },
      downloadBtn: false,
      downloadBtn1: false,
    }
  },
  created() {
    this.getTreeData()
  },
  methods: {
    exportFile() {
      this.downloadBtn = true
      this.downloadExport()
      /* exportFile({depId:this.$refs.treeDom.getCurrentNode().orgId}).then(res => {
            this.downloadExport()
        }).catch(err=>{
            this.$message.error(err.data.errmsg || "出错了");
            this.downloadBtn = false
        })
*/
    },
    exportFileCity() {
      this.downloadBtn1 = true
      this.downloadExportCity()
    },
    downloadExport() {
      var _this = this
      const url = location.protocol + '//' + window.location.host + '/admin/cataExport/export?depId=' + this.$refs.treeDom.getCurrentNode().orgId
      var iframe = document.createElement('iframe')
      iframe.src = url
      iframe.id = 'iframedownload'
      document.body.appendChild(iframe)
      iframe.style.display = 'none'
      iframe.onload = function () {
        console.log('start downloading...')
        document.body.removeAttribute(iframe)
      }
      var timer = setInterval(function () {
        iframe = document.getElementById('iframedownload')
        var iframeDoc = iframe.contentDocument || iframe.contentWindow.document
        console.log(iframeDoc)
        // Check if loading is complete
        if (iframeDoc.readyState == 'complete' || iframeDoc.readyState == 'interactive') {
          clearInterval(timer)
          setTimeout(() => {
            _this.downloadBtn = false
            _this.timeRange = null
            _this.$notify.success('编制表导出成功！')
            _this.handleFilter()
          }, 3000)
        }
      }, 10)
    },
    downloadExportCity() {
      var _this = this
      const url = location.protocol + '//' + window.location.host + '/admin/cataExport/exportCity?depId=' + this.$refs.treeDom.getCurrentNode().orgId
      var iframe = document.createElement('iframe')
      iframe.src = url
      iframe.id = 'iframedownload'
      document.body.appendChild(iframe)
      iframe.style.display = 'none'
      iframe.onload = function () {
        console.log('start downloading...')
        document.body.removeAttribute(iframe)
      }
      var timer = setInterval(function () {
        iframe = document.getElementById('iframedownload')
        var iframeDoc = iframe.contentDocument || iframe.contentWindow.document
        console.log(iframeDoc)
        // Check if loading is complete
        if (iframeDoc.readyState == 'complete' || iframeDoc.readyState == 'interactive') {
          clearInterval(timer)
          setTimeout(() => {
            _this.downloadBtn1 = false
            _this.timeRange = null
            _this.$notify.success('市表导出成功！')
            _this.handleFilter()
          }, 3000)
        }
      }, 10)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getTableData()
    },
    getTableData() {
      this.tableLoading = true
      if (this.timeRange) {
        this.listQuery.startDate = this.timeRange[0]
        this.listQuery.endDate = this.timeRange[1]
      } else {
        this.listQuery.startDate = this.listQuery.endDate = ''
      }
      getExportList(this.listQuery).then(res => {
        if (res.data.errno === 0) {
          this.tableData = res.data.data.records || []
          this.total = res.data.data.total
        }
        this.tableLoading = false
      }).catch(err => {
        this.tableLoading = false
        this.$message.error(err.data.errmsg || '出错了~')
      })
    },
    getTreeData() {
      this.treeLoading = true
      getDepartmentTree({ isTop: 0 })
        .then(res => {
          if (res.data) {
            this.treeData = res.data
            // this.currentNodekey = this.currentNodekey ? this.currentNodekey : this.treeData[0].busiNo;
            // this.expandedkeys.push(this.currentNodekey ?  this.currentNodekey : this.treeData[0].busiNo);
            this.currentNodekey = this.treeData[0].children[0].orgId
            this.expandedkeys.push(this.treeData[0].children[0].orgId)
            setTimeout(() => {
              this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
              this.listQuery.depId = this.currentNodekey
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
      this.listQuery.page = 1
      this.listQuery.depId = this.$refs.treeDom.getCurrentNode().orgId
      this.currentNodekey = this.$refs.treeDom.getCurrentNode().orgId
      this.getTableData()
    },
    delRelated(data) {
      this.tipStr = '确认删除所选文件吗？'
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      exportDel({ cataExportId: this.tempDelData.id }).then(res => {
        if (res.data.errno === 0) {
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
  border-radius: 24px;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
  border-radius: 24px;
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
  padding-left: 5px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.operate_area {
  padding: 0 5px;
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
  width: 20px;
  height: 20px;
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

