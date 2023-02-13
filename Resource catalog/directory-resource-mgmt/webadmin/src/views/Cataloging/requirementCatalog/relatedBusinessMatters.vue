<template>
  <div class="app-container mt20 department_content">
    <div class="type_area">{{ dataResourceName || '' }}</div>
    <div class="operate_area">
      <!-- 查询和其他操作 -->
      <div class="filter-container" style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-button v-permission="['POST /admin/cataBusInfoRel/save']" class="add" size="mini" icon="el-icon-plus" @click="addRelated">新增</el-button>
            <el-button v-permission="['POST /admin/cataBusInfoRel/delete']" class="add" size="mini" icon="el-icon-close" @click="delVersion">删除</el-button>
          </el-col>
          <el-col :span="18">
            <el-button v-permission="['GET /admin/cataBusInfoRel/list']" class="remove fr" style="margin-left: 22px" icon="el-icon-search" @click="handleFilter">查询</el-button>
            <el-input v-model="listQuery.busiNoAndBusiNm" v-permission="['GET /admin/cataBusInfoRel/list']" clearable class="filter-item fr" style="width: 35%;" placeholder="请输入权责清单名称或编码" @clear="handleFilter" />
            <span class="filter_text fr">权责清单名称或编码</span>
          </el-col>
        </el-row>
      </div>
      <!-- 查询结果 -->
      <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" stripe :header-cell-style="getRowClass" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="busiNo" label="权责清单编码" width="180px" show-overflow-tooltip />
        <el-table-column prop="busiNm" label="权责清单名称" show-overflow-tooltip />
        <el-table-column prop="address" label="服务对象" width="180px" show-overflow-tooltip>
          <template slot-scope="scope">{{ serviceObjs[scope.row.serviceObj] }}</template>
        </el-table-column>
        <el-table-column label="操作" class-name="small-padding fixed-width" width="100px" fixed="right">
          <template slot-scope="scope">
            <el-tooltip content="详情" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl" @click="showDetail(scope.row)">
                <svg-icon icon-class="detail" class-name />
              </span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :layout="'prev,pager,next'" @pagination="getList" />
    </div>
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
    <!--权责清单新增-->
    <el-dialog :modal-append-to-body="false" :title="'关联的权责清单'" align="center" :visible.sync="addRelatedDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog('searchRelatedResults','searchTotal','busiSearchQuery')">
      <div class="add_btn_area">
        <el-input v-model="busiSearchQuery.busiNoAndBusiNm" class="filter_input" style="width: 50%" placeholder="输入权责清单名称或编码" suffix-icon="el-icon-search" clearable @clear="searchRelateds" />
        <el-button class="remove" size="mini" icon="el-icon-search" @click="searchRelateds">查询</el-button>
      </div>

      <div class="search_result">
        <el-table v-if="searchRelatedResults.length>0" v-loading="tableLoading" :data="searchRelatedResults" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="RelatedResultsSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="busiNo" label="权责清单编码" width />
          <el-table-column prop="busiNm" label="权责清单名称" width />
        </el-table>
        <pagination v-show="searchTotal>0" :layout="'prev,pager,next'" :total="searchTotal" :small="true" :background="false" :page.sync="busiSearchQuery.page" :limit.sync="busiSearchQuery.limit" @pagination="getSearchData" />
      </div>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="saveAddRelated">保 存</el-button>
        <el-button size="mini" class="remove" @click="addRelatedDialog=false">取 消</el-button>
      </div>
    </el-dialog>
    <!--权责清单详情-->
    <el-dialog :modal-append-to-body="false" :title="'关联的权责清单'" align="center" :visible.sync="RelatedDetailDialog" width="48%" :close-on-click-modal="false" @close="closeDetailDialog">
      <div class="search_result" style="border:1px solid rgba(203,203,203,1);margin-top: 0;margin-bottom: 10px">
        <ul class="detail_ul">
          <li>
            <span class="title">权责清单名称:</span>
            <span>{{ detailForm.busi_nm || '暂无' }}</span>
          </li>
          <li>
            <span class="title">权责清单编码:</span>
            <span>{{ detailForm.busi_no || '暂无' }}</span>
          </li>
          <li>
            <span class="title">服务对象:</span>
            <span>{{ detailForm.serviceobjname || '暂无' }}</span>
          </li>
          <li>
            <span class="title">所支撑的业务系统:</span>
            <span>
              <el-tag v-for="tag in detailForm.appList" v-if="detailForm.appList && detailForm.appList.length>0" :key="tag.busi_id" style="margin-right: 10px" size="small" type="success" :disable-transitions="false">{{ tag.appsysNm }}</el-tag>
              <span v-if="!detailForm.appList || detailForm.appList.length==0">暂无</span>
            </span>
          </li>
          <li>
            <span class="title">备注:</span>
            <span>{{ detailForm.remark || '暂无' }}</span>
          </li>
          <li>
            <span class="title">部门名称:</span>
            <span>{{ detailForm.orgnm || '暂无' }}</span>
          </li>
        </ul>
      </div>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="RelatedDetailDialog=false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from 'vuex'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import {
  addRequirementrCataRel,
  delRequirementCatalog,
  requirementrCataRelList,
  getToAddBusList
} from '@/api/requirementCatalog'
import { getBusinessDetail } from '@/api/businessManagement'
import { getDictByType } from '@/api/departmentRes'
export default {
  name: 'RelatedBusinessMatters',
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
      tipStr: '确认删除所选权责清单么？',
      timeRange: null,
      listLoading: false,
      list: null,
      total: 0,
      listQuery: {
        page: 1,
        limit: 10,
        username: undefined,
        sort: 'add_time',
        order: 'desc',
        keyword: ''
      },
      tempDelData: {},
      choosedDelArr: [],
      addRelatedDialog: false,
      RelatedDetailDialog: false,
      choosedRelatedResults: [],
      detailForm: {},
      serviceObjs: {},
      busiSearchQuery: {
        limit: 5,
        page: 1,
        busiNoAndBusiNm: ''
      }
    }
  },
  computed: {},
  created() {
    this.$store.dispatch('setReBackDeptId', this.$route.query.deptId)
    this.dataResourceName = this.$route.query.dataResourceName || ''
    getDictByType({ type: 'service_obj' }).then(res => {
      if (res.data.errno === 0) {
        res.data.data.forEach(item => {
          this.serviceObjs[item.value] = item.name
        })
      }
    })
    this.getList()
  },
  methods: {
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    confirmDelete() {
      const ids = []
      this.choosedDelArr.forEach(item => {
        ids.push(item.busiId)
      })
      delRequirementCatalog(
        `/cataBusInfoRel/delete?infoId=${this.id * 1}`,
        ids
      ).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getList()
        }
      })
    },
    closeDetailDialog() {
      this.relatedDetailResults = []
    },
    searchRelateds() {
      this.busiSearchQuery.page = 1
      this.getSearchData()
    },
    getSearchData() {
      const sendData = Object.assign({}, this.busiSearchQuery, {
        infoId: this.id,
        deptId: this.$route.query.deptId
      })
      getToAddBusList(sendData).then(res => {
        if (res.data.errno === 0) {
          this.searchRelatedResults = res.data.data.records
          this.searchTotal = res.data.data.total
        }
      })
    },
    RelatedResultsSelectionChange(val) {
      console.log(val)
      this.choosedRelatedResults = val
    },
    addRelated() {
      this.getSearchData()
      this.addRelatedDialog = true
    },
    saveAddRelated() {
      if (this.choosedRelatedResults.length == 0) {
        this.$message({
          message: '请选择要关联的权责清单',
          type: 'error'
        })
        return
      }
      const sendData = {
        dataId: this.id,
        addRelateds: this.choosedRelatedResults
      }
      const ids = []
      this.choosedRelatedResults.forEach(item => {
        ids.push(item.busiId)
      })
      addRequirementrCataRel(
        `/cataBusInfoRel/save?infoId=${this.id * 1}&type=1`,
        ids
      ).then(res => {
        if (res.data.errno === 0) {
          this.$notify.success('新增关联权责清单成功')
          this.addRelatedDialog = false
          this.getList()
        }
      })
    },
    // 权责清单详情
    showDetail(data) {
      getBusinessDetail({ busiId: data.busiId }).then(res => {
        if (res.data.errno === 0) {
          this.detailForm = Object.assign({}, res.data.data)
          this.RelatedDetailDialog = true
        }
      })
    },

    handleSelectionChange(val) {
      this.choosedDelArr = val
    },
    delVersion() {
      if (this.choosedDelArr.length == 0) {
        this.$message({
          message: '请选择要删除的权责清单',
          type: 'error'
        })
      } else {
        this.delDialog = true
      }
    },
    getList() {
      this.tableLoading = true
      const sendData = Object.assign({}, this.listQuery, { infoId: this.id })
      requirementrCataRelList(sendData).then(res => {
        if (res.data.errno === 0) {
          this.list = res.data.data.records
          this.total = res.data.data.total
        }
        this.tableLoading = false
      }).catch(err => {
        this.tableLoading = false
      })
    }
  }
}
</script>

<style scoped>
.add_btn_area {
  text-align: center;
  margin-bottom: 25px;
}
/*详情start*/
.detail_ul {
  padding: 0;
  margin: 0;
}
.detail_ul li {
  position: relative;
  min-height: 28px;
  line-height: 28px;
  text-align: left;
  padding-left: 40px;
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
/*详情end*/
.filter_text {
  line-height: 36px;
  margin-right: 6px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}
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
