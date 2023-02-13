<template>
  <div class="app-container mt20 department_content">
    <div class="type_area">操作日志</div>
    <div class="operate_area">
      <!-- 查询和其他操作 -->
      <div class="filter-container1" style="margin-top: 15px;    margin-bottom: 14px;">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-button v-permission="['GET /admin/log/export']" style="margin-left: 22px" class="remove fr" size="mini" @click="exportLog">导出</el-button>
            <el-button v-permission="['GET /admin/admin/list']" style="margin-left: 22px" class="remove fr" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>
            <el-input v-model="listQuery.name" style="width:205px" clearable class="filter-item fr" placeholder="登录名、操作人姓名" @clear="handleFilter" />
            <!-- <span class="filter_text fr">登录名、操作人姓名</span>-->
            <el-date-picker v-model="timeRange" style="" class="filter-item fr" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="起始日期" end-placeholder="截至日期" @change="getList" />
            <!--<span class="filter_text fr">操作日期</span>-->
          </el-col>
        </el-row>
      </div>
      <!-- 查询结果 -->
      <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" stripe :header-cell-style="getRowClass">
        <el-table-column label="序号" type="index" />
        <el-table-column label="操作人账号" prop="account" show-overflow-tooltip />
        <el-table-column label="操作人" prop="admin" />
        <el-table-column label="操作时间" prop="addTime" />
        <el-table-column label="功能模块" prop="action" show-overflow-tooltip />
        <el-table-column label="操作内容" prop="result" class-name="small-padding fixed-width" show-overflow-tooltip />
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :layout="'prev,pager,next'" @pagination="getList" />
    </div>
  </div>
</template>

<style>
.leftSpan {
  color: #172853;
  font-size: 18px;
}
.leftTree {
  border: 1px solid rgb(201, 201, 201);
  max-height: 400px;
  overflow-y: scroll;
}
.dialogFormItem {
  margin-bottom: 5px;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}
</style>

<script>
import { getToken } from '@/utils/auth'
import { listLog, exportLog } from '@/api/log'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from 'vuex'
import mixinJs from '@/utils/mixin'
export default {
  name: 'Operation',
  components: { Pagination },
  mixins: [mixinJs],
  data() {
    return {
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
        startDate: '',
        endDate: '',
        name: ''
      }
    }
  },
  computed: {
    ...mapGetters(['userid', 'propertyId', 'roles']),
    headers() {
      return {
        'X-Resourcecatalog-Admin-Token': getToken()
      }
    }
  },
  created() {
    this.getList()
    // console.log(this.userid, this.propertyId, this.roles);
  },
  methods: {
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    exportLog() {
      var _this = this
      const url = location.protocol + '//' + window.location.host + '/admin/log/export?name=' + _this.listQuery.name + '&startDate=' + _this.listQuery.startDate + '&endDate=' + _this.listQuery.endDate
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
            _this.$notify.success('操作日志导出成功！')
            _this.handleFilter()
          }, 3000)
        }
      }, 10)

    },
    getList() {
      this.listLoading = true
      if (this.timeRange) {
        this.listQuery.startDate = this.timeRange[0]
        this.listQuery.endDate = this.timeRange[1]
      } else {
        this.listQuery.startDate = this.listQuery.endDate = ''
      }
      console.log(this.listQuery)
      listLog(this.listQuery).then(res => {
        if (res.data.errno === 0) {
          this.list = res.data.data.records || []
          this.total = res.data.data.total
        }
        this.listLoading = false
      }).catch(err => {
        this.listLoading = false
      })
    }

  }
}
</script>

<style scoped>
.filter-container1 .filter-item {
  display: table;
}
.filter-item {
  margin-right: 15px;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
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
.filter_text {
  margin-left: 36px;
  line-height: 36px;
  margin-right: 6px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
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

