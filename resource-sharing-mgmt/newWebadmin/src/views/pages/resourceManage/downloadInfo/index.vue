<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入文件名称" v-model="SearchItem.title">
          </el-input>
        </el-form-item>
        <el-form-item label="文件类型:">
          <el-select v-model="SearchItem.type" size="medium" clearable placeholder="请选择类型">
            <el-option label="通知公告" value="1" />
            <el-option label="资料下载" value="2" />
            <el-option label="技术规范" value="3" />
          </el-select>
        </el-form-item>
        <!-- <el-form-item>
                    <el-cascader
                        size="medium"
                        placeholder="请选择所属部门"
                        :props="props"
                        collapse-tags
                        clearable
                        v-model="SearchItem.provOrgName"
                        :options="organizationOptions"
                        v-if='user == "admin"'
                    />
                </el-form-item> -->
        <el-form-item style='margin-left: 1%;'>
          <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" plain size='mini' @click="handleAdd" icon="el-icon-plus">新增</el-button>
      </div>
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { downloadInfoFindAll, downloadInfoDelete } from "@/api/downloadInfo.js"
import { organizationFindAll } from "@/api/organization.js";
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
import { getCookies } from '@/utils/auth';
export default {
  components: { TableList, Pagination },
  data() {
    return {
      user: JSON.parse(getCookies('userInfo')).id,
      props: { checkStrictly: true },
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      organizationOptions: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: 'btn',
          label: '文件名称',
          list: 'title',
          method: (row) => {
            window.location.href = process.env.VUE_APP_BASE_API + "web/downloadInfo/download?id=" + row.id;
            return '<span>' + row.title + '</span>'
          }
        },
        {
          type: 'html',
          label: '文件类型',
          list: 'type',
          code: (row) => {
            if (row.type == '1') {
              return '<span>通知公告</span>'
            } else if (row.type == '2') {
              return '<span>资料下载</span>'
            } else if (row.type == '3') {
              return '<span>技术规范</span>'
            }
            // return '<span>' + row.title + '</span>'
          }
        },
        {
          type: 'html',
          label: '文件大小',
          list: 'acceptDept',
          code: (row) => {
            return '<span>' + row.fileSize + '</span>'
          }
        },
        // {
        //     type: 'html',
        //     label: '更新周期',
        //     list: 'updateCyc',
        //     code: (row) => {

        //         switch (row.updateCyc) {
        //             case "0":
        //                 return '<span>' + "每周" + '</span>'
        //                 break;
        //             case "1":
        //                 return '<span>' + "每月" + '</span>'
        //                 break
        //             case "2":
        //                 return '<span>' + "半年" + '</span>'
        //                 break;
        //             case "3":
        //                 return '<span>' + "每年" + '</span>'
        //                 break;
        //             default:
        //                 return '<span>' + "未知" + '</span>'
        //                 break;
        //         }
        //     }
        // },
        // {
        //     type: 'html',
        //     label: '所属部门',
        //     list: '',
        //     code: (row) => {
        //         return '<span>' + row.provOrgName + '</span>'
        //     }
        // },
        {
          type: 'html',
          label: '文件描述',
          list: 'status',
          code: (row) => {
            return '<span>' + row.fileDesc + '</span>'
          }
        },
        {
          type: 'html',
          label: '提出日期',
          list: 'createTime',
          code: (row) => {
            return '<span>' + row.createTime.split(' ')[0] + '</span>'
          }
        }
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 0,
        options: [{
          label: '删除',
          key: 0,
          type: 'text',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        }, {
          label: '编辑',
          key: 0,
          type: 'text',
          State: false,
          method: (row) => {
            this.handleEdit(row)
          }
        }]
      },
      time: [],
      SearchItem: {
        title: '',
        provOrgName: ''
      },
      lastItem: {
        title: '',
        provOrgName: '',
        fileDesc: '',
        fileSize: '',
        updateCyc: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1'
    }
  },
  created() {
    console.log(getCookies('userInfo'), "============")
    this.SearchItem.provOrgId = JSON.parse(getCookies('userInfo')).id == 'admin' ? '' : JSON.parse(getCookies('userInfo')).organizations[0]
    this.fetchData()
    this.findorganizations()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.SearchItem.title = ''
      that.SearchItem.type = ''
      that.SearchItem.provOrgName = ''
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
      if (typeof (data.provOrgName) !== 'string' && typeof (data.provOrgName) !== 'undefined') {
        data.provOrgName = data.provOrgName[data.provOrgName.length - 1]
      }
      console.log(data)
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await downloadInfoFindAll(data)
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
    // 删除
    async delete(row) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await downloadInfoDelete(row.id)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 添加
    handleAdd(data) {
      const that = this
      that.$router.push({
        path: '/downloadInfoAdd',
        query: {
          type: '新增'
        }
      })
    },
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/downloadInfoAdd',
        query: {
          type: '编辑',
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
