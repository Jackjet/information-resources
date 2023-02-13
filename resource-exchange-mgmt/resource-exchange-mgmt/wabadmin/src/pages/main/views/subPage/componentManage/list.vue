<template>
  <el-main>
    <el-col :span="24" v-loading="loading">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-select clearable v-model="SearchItem.type" placeholder="请选择组件类型">
            <el-option
              v-for="item in typeArr"
              :key="item.value"
              :label="item.label"
              :value="item.value">
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
import tableForm from '../../integration/tableList'
import paging from '../../integration/pagination'

export default {
  components: {tableForm, paging},
  data () {
    return {
      loading: false,
      typeArr: [{
        value: 'datasource',
        label: '数据源'
      }, {
        value: 'protocol',
        label: '协议'
      }, {
        value: 'integration',
        label: '集成'
      }, {
        value: 'tool',
        label: '工具'
      }, {
        value: 'third',
        label: '第三方平台'
      }, {
        value: 'other',
        label: '其他'
      }],
      tableData: [],
      SearchItem: {
        type: ''
      },
      lastSearch: {
        type: ''
      },
      tableSelection: {},
      tableHeader: [
        { id: false, type: '', label: '类型', list: 'type_c' },
        { id: false, type: '', label: 'group', list: 'groupId' },
        { id: false, type: '', label: '名称', list: 'name' },
        { id: false, type: '', label: '版本', list: 'version' }
      ],
      tableOption: {
        label: '操作',
        value: 0,
        options: [
          {
            label: '组件文档',
            key: 0,
            type: 'check',
            icon: 'el-icon-document',
            State: false,
            method: (row) => {
              this.toDoc(row)
            }
          }
        ]
      },
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },
  mounted () {
    this.getList()
  },
  methods: {
    // 列表
    async getList () {
      const that = this
      try {
        let findUrl = this.Interface.componentFindAll
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        Object.entries(that.SearchItem).map((item, index) => {
          that.SearchItem[item[0]] = that.lastSearch[item[0]]
        })
        let obj = {
          type: that.SearchItem.type
        }
        let response = await this.request.dataGet(that, url, obj)
        let theData = response.data.data
        theData.content.forEach(item => {
          switch (item.type) {
            case 'datasource':
              item.type_c = '数据源'
              break
            case 'protocol':
              item.type_c = '协议'
              break
            case 'integration':
              item.type_c = '集成'
              break
            case 'tool':
              item.type_c = '工具'
              break
            case 'third':
              item.type_c = '第三方平台'
              break
            case 'other':
              item.type_c = '其他'
              break
          }
        })
        that.tableData = theData.content
        that.total = theData.totalElements
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    toDoc (row) {
      window.open("http://47.105.96.207/portalservice/web/page/api/index.html" + "?componentName=" + row.componentName + "&developerId=" + row.developerId + "&version=" + row.maxVersion)
    },
    // 查询
    search: function () {
      const that = this
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastSearch[item[0]] = that.SearchItem[item[0]]
      })
      that.currentPage = 1
      this.$refs.pager.changePage()
      this.getList()
    },
    // 分页
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.getList()
    }
  }
}
</script>
