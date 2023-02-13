<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入需求标题" v-model="SearchItem.title">
          </el-input>
        </el-form-item>
        <el-form-item>
          <!-- <el-input clearable
                              size="medium"
                              placeholder="请输入提出部门"
                              v-model="SearchItem.createDeptName">
                    </el-input> -->
          <el-cascader size="medium" placeholder="请选择提出部门" :props="props" collapse-tags clearable v-model="SearchItem.createDeptName" :options="organizationOptions" />
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.requestType" placeholder="请选择需求类型">
            <el-option key="0" label="目录资源变更" value="0"></el-option>
            <el-option key="1" label="目录资源新增" value="1"></el-option>
            <el-option key="2" label="云数据变更" value="2"></el-option>
            <el-option key="3" label="云数据新增" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { demandedInfoFindAllByAcceptDeptId } from "@/api/demandedInfo.js"
import { organizationFindAll } from "@/api/organization.js";
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      organizationOptions: [],
      props: { checkStrictly: true },
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: 'html',
          label: '提出日期',
          list: 'createTime',
          code: (row) => {
            return '<span>' + row.createTime.split(' ')[0] + '</span>'
          }
        },
        {
          type: 'html',
          label: '需求标题',
          list: 'title',
          code: (row) => {
            return '<span>' + row.title + '</span>'
          }
        },
        {
          type: 'html',
          label: '受理部门',
          list: 'acceptDept',
          code: (row) => {
            return '<span>' + row.acceptDept + '</span>'
          }
        },
        {
          type: 'html',
          label: '受理需求',
          list: 'requestType',
          code: (row) => {
            switch (row.requestType) {
              case "0":
                return '<span>' + "目录资源变更" + '</span>'
                break;
              case "1":
                return '<span>' + "目录资源新增" + '</span>'
                break
              case "2":
                return '<span>' + "云数据变更" + '</span>'
                break;
              case "3":
                return '<span>' + "云数据新增" + '</span>'
                break;
              default:
                this.requestType = "未知";
                break;
            }
          }
        },
        {
          type: 'html',
          label: '提出部门',
          list: '',
          code: (row) => {
            return '<span>' + row.createDeptName + '</span>'
          }
        },
        // {
        //   type: 'html',
        //   label: '申请类型',
        //   list: 'type',
        //   code: (row) => {
        //     if (row.type === '1') {
        //       return '<span>云接口</span>'
        //     } else if (row.type === '2') {
        //       return '<span>云文件</span>'
        //     } else if (row.type === '3') {
        //       return '<span>云数据</span>'
        //     }
        //   }
        // },
        {
          type: 'html',
          label: '状态',
          list: 'status',
          code: (row) => {
            switch (row.status) {
              case "0":
                return '<span>' + "未受理" + '</span>'
                break;
              case "1":
                return '<span>' + "已受理" + '</span>'
                break
              case "2":
                return '<span>' + "已驳回" + '</span>'
                break;
              default:
                return '<span>' + "未知" + '</span>'
                break;
            }
          }
        }
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '受理',
          key: 0,
          type: 'text',
          icon: 'el-icon-tickets',
          State: false,
          method: (row) => {
            this.handleUpdate(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        title: '',
        createDeptName: '',
        requestType: '',
        status: '0'
      },
      lastItem: {
        title: '',
        createDeptName: '',
        requestType: '',
        status: '0'
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    this.fetchData()
    this.findorganizations()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.title = '',
        that.SearchItem.createDeptName = '',
        that.SearchItem.requestType = '',
        that.SearchItem.status = '0',
        Object.entries(that.SearchItem).map((item, index) => {
          that.lastItem[item[0]] = that.SearchItem[item[0]]
        })
    },
    // 获取列表
    async fetchData() {
      const that = this
      let data = {}
      data = this.SearchItem,
        data.page = this.currentPage,
        data.size = this.pageSize
      if (typeof (data.createDeptName) !== 'string' && typeof (data.createDeptName) !== 'undefined') {
        data.createDeptName = data.createDeptName[data.createDeptName.length - 1]
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await demandedInfoFindAllByAcceptDeptId(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 搜索
    SearchNoteList() {
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },
    // 受理
    handleUpdate(data) {
      const that = this
      that.$router.push({
        path: '/demandedInfoUpdate',
        query: {
          id: data.id
        }
      })
    },
    async findorganizations(data) {
      const that = this
      that.loading = true
      const response = await organizationFindAll()
      that.loading = false
      if (response.data.code === 1) {
        let arrData = []
        arrData.push(response.data.data)
        that.organizationOptions = this.getJsonTree(arrData, '')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    getJsonTree(data, parentId) {
      let itemArr = []
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId) {
          let newNode = {};
          newNode.value = node.name;
          newNode.label = node.name;
          if (node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode);
        }
      }
      return itemArr;
    }
  }
}
</script>
