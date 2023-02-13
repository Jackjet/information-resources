<template>
  <div class="app-container mt20 department_content">
    <div class="type_area">版本管理</div>
    <div class="operate_area">
      <!-- 查询和其他操作 -->
      <div class="filter-container" style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-button v-permission="['POST /admin/cataInfoHistory/delete']" class="add" size="mini" icon="el-icon-close" @click="delVersion">删除</el-button>
          </el-col>
        </el-row>
      </div>
      <!-- 查询结果 -->
      <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" stripe :header-cell-style="getRowClass" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="版本号" prop="version">
          <template slot-scope="scope">
            <span>{{ scope.row.version }}</span>
            <span v-if="scope.row.isCurrent==1" style="margin-left: 20px">
              <svg-icon icon-class="star_sel" class-name />
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作时间" prop="pubDt" />
        <el-table-column label="操作" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-tooltip v-permission="['GET /admin/cataInfoHistory/read']" content="详情" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl" @click="goToDetail(scope.row)">
                <svg-icon icon-class="detail" class-name />
              </span>
            </el-tooltip>
            <el-tooltip v-if="!(scope.row.isCurrent==1)" v-permission="['GET /admin/cataInfoHistory/compare']" content="比较" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl" @click="compare(scope.row)">
                <svg-icon icon-class="compare" class-name />
              </span>
            </el-tooltip>
            <!--隐藏
            
            <el-tooltip
              v-if="!(scope.row.isCurrent==1)"
              v-permission="['GET /admin/cataInfoHistory/rollback']"
              content="恢复"
               effect="light" :open-delay="1000"
              placement="bottom"
            >
              <span class="tr_detail_icon fl" @click="revert(scope.row)">
                <svg-icon icon-class="revert" class-name />
              </span>
            </el-tooltip>
            -->
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :layout="'prev,pager,next'" @pagination="getList" />
    </div>
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from 'vuex'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import {
  getVersionList,
  delVersion,
  versionRevert,
  getVersionDetail,
  compareVersion
} from '@/api/version'
export default {
  name: 'VersionManagement',
  components: { Pagination, DelConfirm },
  mixins: [mixinJs],
  props: {
    id: {
      type: [String, Number]
    }
  },
  data() {
    return {
      dataResourceName: '',
      delDialog: false,
      tipStr: '确认删除所选版本么？',
      timeRange: null,
      listLoading: false,
      list: null,
      total: 0,
      listQuery: {
        page: 1,
        limit: 20,
        username: undefined,
        sort: 'add_time',
        order: 'desc',
        startDate: '',
        endDate: '',
        name: ''
      },
      tempDelData: {},
      choosedDelArr: []
    }
  },
  computed: {},
  created() {
    this.$store.dispatch('setInfoReBackDeptId', this.$route.query.provOrgCode)
    this.dataResourceName = this.$route.query.dataResourceName || ''
    const alreadyBreadCrumbs = this.$store.state.app.breadcrumb
    if (
      this.dataResourceName &&
      alreadyBreadCrumbs[alreadyBreadCrumbs.length - 1].meta.title !=
      this.dataResourceName
    ) {
      alreadyBreadCrumbs.push({
        path: '',
        component: '',
        name: '',
        redirect: 'noredirect',
        meta: {
          title: this.dataResourceName,
          noCache: true
        }
      })
      this.$store.dispatch('setBreadCrumb', alreadyBreadCrumbs)
    }
    this.getList()
  },
  methods: {
    goToDetail(data) {
      const versionId = data.id
      const dataResourceCatalogId = this.id
      this.$router.push({
        path: `/Cataloging/infoCatalogDetail/${versionId}`,
        query: {
          versionId: versionId,
          isHistory: 1,
          provOrgCode: this.$route.query.provOrgCode
        }
      })
    },
    compare(data) {
      const versionId = data.id
      const dataResourceCatalogId = this.id
      this.$router.push({
        path: `/Cataloging/versionComparison/${versionId}`,
        query: {
          dataResourceCatalogId: dataResourceCatalogId,
          versionNum: data.versionNum,
          dataResourceName: this.dataResourceName,
          provOrgCode: this.$route.query.provOrgCode
        }
      })
    },

    revert(data) {
      const versionId = data.id
      const dataResourceCatalogId = this.id
      this.$confirm('确定要用该版本替换现有版本吗？')
        .then(_ => {
          versionRevert({ id: versionId })
            .then(res => {
              if (res.data.errno === 0) {
                this.$notify.success('申请恢复成功，等待管理员审核')
                this.getList()
              }
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        })
        .catch(_ => { })
    },
    handleSelectionChange(val) {
      this.choosedDelArr = val
    },
    delVersion() {
      var thisNum = 0
      if (this.choosedDelArr.length == 0) {
        this.$message({
          message: '请选择要删除的版本',
          type: 'error'
        })
      } else {
        this.choosedDelArr.forEach(element => {
          if (element.isCurrent == 1) {
            this.$message({
              message: '当前版本不能删除',
              type: 'error'
            })
            thisNum = thisNum + 1
          }
        })
        if (thisNum == 0) {
          this.delDialog = true
        }
      }
    },
    confirmDelete() {
      const ids = []
      this.choosedDelArr.forEach(item => {
        ids.push(item.id)
      })
      delVersion(ids).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getList()
        }
      })
      console.log(this.choosedDelArr)
    },
    handleFilter() {
      this.listQuery.current = 1
      this.getList()
    },
    getList() {
      if (this.timeRange) {
        this.listQuery.startDate = this.timeRange[0]
        this.listQuery.endDate = this.timeRange[1]
      } else {
        this.listQuery.startDate = this.listQuery.endDate = ''
      }
      const sendData = Object.assign({}, this.listQuery, { uviewId: this.id })
      getVersionList(sendData).then(res => {
        if (res.data.errno === 0) {
          this.list = res.data.data.records
          this.total = res.data.data.total
        }
      })
    }
  }
}
</script>

<style scoped>
.department_content {
  background: #fff;
  border: 1px solid rgba(230, 229, 234, 1);
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

.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tab_area {
  height: 100%;
}
.operate_area {
  padding: 0 45px;
}
.related_btns {
  margin-top: 10px;
  padding-bottom: 20px;
}
.add {
  background: #1f3365;
  min-width: 80px;
  color: #fff;
  height: 36px;
}
.remove {
  color: #1f3365;
  min-width: 80px;
  border-color: #1f3365;
  height: 36px;
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
