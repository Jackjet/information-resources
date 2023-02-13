<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.nodeId" placeholder="请选择节点名称" @change="nodeChange">
            <el-option
              v-for="item in nodeList"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.sysId" placeholder="请选择系统名称">
            <el-option
              v-for="item in sysList"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.type" placeholder="请选择部署类型">
            <el-option label="自动部署" value="deployment"></el-option>
            <el-option label="补丁更新" value="upgrade"></el-option>
            <el-option label="版本回滚" value="rollback"></el-option>
            <el-option label="配置更新" value="configUpdate"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker
            v-model="valueTime"
            size="medium"
            type="daterange"
            value-format="yyyy-MM-dd"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <el-table
        v-loading="isSubmitLoading"
        ref="multipleTable"
        :data='tableData'
        empty-text='暂无数据'
        class='el_tab_alage'>
        <template v-for="(item, index) in tableHeader">
          <el-table-column fit
            align='center'
            :key='index'
            :label="item.label"
            :width="item.width"
            :prop="item.list">
            <template slot-scope="scope">
              <el-tooltip class="item" effect="dark" :content="scope.row[item.list]" placement="top">
                <div style="width:90px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;">
                  {{scope.row[item.list]}}
                </div>
              </el-tooltip>
            </template>
          </el-table-column>
        </template>
        <el-table-column fit label="操作" align='center'>
          <template slot-scope="scope">
            <el-button  size="mini"
              type="success"
              :plain='false'
              @click.native.prevent="details(scope.row)">详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <deploymentDetalis
      :modal-objjj='title'
      @getList="getList"
      ref="add">
    </deploymentDetalis>
  </el-main>
</template>

<script>
import { deploymentLogfindAll } from '@/api/deploymentView/http'
import { findAllNodes, findAllServices } from '@/api/moduleMonitorView/http'
import Pagination from '@/components/table/Pagination.vue'
import deploymentDetalis from './deploymentDetalis'
export default {
  components: { Pagination, deploymentDetalis },
  data() {
    return {
      valueTime: '',
      lastTime: '',
      tableData: [],
      nodeList: [],
      sysList: [],
      title: '详情',
      tableHeader: [
        {
          id: false, type: '', label: '节点名称', list: 'nodeName'
        },
        {
          id: false, type: '', label: '系统名称', list: 'sysName'
        },
        {
          id: false, type: '', label: '部署类型', list: 'type'
        },
        {
          id: false, type: '', label: '部署接口url', list: 'apiUrl'
        },
        {
          id: false, type: '', label: '方法', list: 'apiMethod'
        },
        {
          id: false, type: '', label: '版本号', list: 'versionNumber'
        },
        {
          id: false, type: '', label: '开始时间', list: 'beginTime'
        },
        {
          id: false, type: '', label: '结束时间', list: 'endTime'
        },
        {
          id: false, type: '', label: '执行日志', list: 'resultLog'
        }
      ],
      isSubmitLoading: false,
      SearchItem: {
        nodeId: '',
        sysId: '',
        type: '',
        beginTime: '',
        endTime: ''
      },
      lastItem: {
        nodeId: '',
        sysId: '',
        type: '',
        beginTime: '',
        endTime: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    this.getList()
    this.getNodeList()
  },
  methods: {
    async getList() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        nodeId: this.SearchItem.nodeId,
        sysId: this.SearchItem.sysId,
        type: this.SearchItem.type,
        beginTime: this.SearchItem.beginTime,
        endTime: this.SearchItem.endTime
      }
      try {
        const res = await deploymentLogfindAll(data)
        res.data.data.content.forEach(item => {
          switch (item.type) {
            case 'deployment':
              item.type = '自动部署'
              break
            case 'upgrade':
              item.type = '补丁更新'
              break
            case 'rollback':
              item.type = '版本回滚'
              break
            case 'configUpdate':
              item.type = '配置更新'
              break
          }
        })
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async getNodeList () {
      const res = await findAllNodes()
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    nodeChange () {
      this.SearchItem.sysId = ''
      this.sysList = []
      this.getSysList()
    },
    async getSysList () {
      let data = {
        nodeId: this.SearchItem.nodeId
      }
      const res = await findAllServices(data)
      res.data.data.forEach(item => {
        this.sysList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    details (row) {
      this.$refs.add.initial(row.id)
    },
    SearchNoteList () {
      let that = this
      if (that.valueTime) {
        that.SearchItem.beginTime = that.valueTime[0] + ' 00:00:00'
        that.SearchItem.endTime = that.valueTime[1] + ' 23:59:59'
      } else {
        that.SearchItem.beginTime = ''
        that.SearchItem.endTime = ''
      }
      this.currentPage = 1
      Object.entries(that.SearchItem).map(item => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      this.lastTime = this.valueTime
      this.getList()
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.valueTime = this.lastTime
      this.getList()
    }
  }
}
</script>
