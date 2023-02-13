<template>
  <el-main v-loading="loading">
    <el-col :span="24">
      <el-button type="primary" style="display: block;margin-bottom: 20px;" @click="back" icon="el-icon-back">返 回</el-button>
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-select v-model="SearchItem.type" clearable size="medium" placeholder="请选择操作类型">
            <el-option
              v-for="(item, index) in activeArr"
              :key="index"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-search"
            @click="search">查询
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- 调用表格 -->
    <el-col :span="24">
      <tableForm :table-data='tableData'
                 :table-selection="tableSelection"
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
    <el-col :span='24'>
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>
  </el-main>
</template>

<script>
import tableForm from '../../../integration/tableList'
import paging from '../../../integration/pagination'

export default {
  components: { tableForm, paging },

  data () {
    return {
      loading: false,
      id: '',
      nodeID: '',
      SearchItem: {
        sysServiceName: '',
        sysServiceType: '',
        type: ''
      },
      lastSearch: {
        sysServiceName: '',
        sysServiceType: '',
        type: ''
      },
      typeArr: [
        {
          id: 'webapi,cmd',
          name: '脚本运行服务'
        },
        {
          id: 'emq',
          name: '消息集成服务'
        },
        {
          id: 'dcapigateway',
          name: 'API集成服务'
        }
      ],
      activeArr: [],
      tableData: [],
      tableSelection: {
        key: false,
        type: 'selection'
      },
      tableHeader: [
        {
          id: false,
          type: '',
          label: '系统服务名称',
          list: 'sysServiceName'
        },
        {
          id: false,
          type: '',
          label: '系统服务类型',
          list: 'sysServiceType'
        },
        {
          id: false,
          type: '',
          label: '操作类型',
          list: 'type'
        },
        {
          id: false,
          type: '',
          label: '启动时间',
          list: 'startTime'
        },
        {
          id: false,
          type: '',
          label: '结束时间',
          list: 'endTime'
        }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        width: '150px',
        options: [{
          label: '查看操作结果',
          key: 0,
          type: 'view',
          icon: 'el-icon-tickets',
          State: false,
          method: (row) => {
            let strs = row.result.split('\n')
            let result = strs.join('@@')
            console.log(result)
            window.open(encodeURI('./static/html/index.html?result=' + result), '_blank')
          }
        }]
      },
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },

  mounted () {
    this.id = this.$route.query.id
    this.nodeID = this.$route.query.nodeID
    this.getList()
    this.getActive()
  },

  methods: {
    // 列表
    async getList () {
      const that = this
      try {
        let findUrl = this.Interface.operation.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        let obj = {
          appid: that.id,
          sysServiceName: that.SearchItem.sysServiceName,
          sysServiceType: that.SearchItem.sysServiceType,
          type: that.SearchItem.type
        }
        const response = await this.request.dataGet(that, url, obj)
        if (response.data.code === 1) {
          response.data.data.content.forEach(item => {
            switch (item.sysServiceType) {
              case 'cmd':
                item.sysServiceType = '脚本运行服务'
                break
              case 'webapi':
                item.sysServiceType = '脚本运行服务'
                break
              case 'emq':
                item.sysServiceType = '消息集成服务'
                break
              case 'dcapigateway':
                item.sysServiceType = 'API集成服务'
                break
            }
          })
          that.tableData = response.data.data.content
          that.total = response.data.data.totalElements
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    // 列表
    async getActive () {
      const that = this
      try {
        let url = this.Interface.operation.findActive
        const response = await this.request.dataGet(that, url, {})
        if (response.data.code === 1) {
          that.activeArr = response.data.data
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },

    // 查询
    search: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      that.currentPage = 1
      this.getList()
    },

    // 分页
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
    },
    back () {
      this.$router.push({
        path: 'services'
      })
    }
  }
}
</script>
