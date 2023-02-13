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
            <el-option label="自动安装" value="install"></el-option>
            <el-option label="版本升级" value="upgrade"></el-option>
            <el-option label="版本回滚" value="rollback"></el-option>
            <el-option label="配置更新" value="configUpdate"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary" size='medium' @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <deploymentAdd
      :modal-objjj='title'
      @getList="getList"
      ref="add">
    </deploymentAdd>
  </el-main>
</template>

<script>
import { deploymentFindAll, deploymentDelete } from '@/api/deploymentView/http'
import { findAllNodes, findAllServices } from '@/api/moduleMonitorView/http'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import deploymentAdd from './deploymentAdd'
export default {
  components: { TableList, Pagination, deploymentAdd },
  data() {
    return {
      tableData: [],
      nodeList: [],
      sysList: [],
      title: '添加',
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
          id: false, type: '', label: '部署接口', list: 'apiName'
        },
        {
          id: false, type: '', label: '备注', list: 'remark'
        }
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [
          {
            label: '执行',
            key: 0,
            type: 'success',
            State: false,
            method: (row) => {
              this.execute(row)
            }
          },
          {
            label: '编辑',
            key: 0,
            type: 'success',
            State: false,
            method: (row) => {
              this.edit(row)
            }
          },
          {
            label: '删除',
            key: 0,
            type: 'danger',
            State: false,
            method: (row) => {
              this.deleteData(row)
            }
          }
        ]
      },
      isSubmitLoading: false,
      SearchItem: {
        nodeId: '',
        sysId: '',
        type: ''
      },
      lastItem: {
        nodeId: '',
        sysId: '',
        type: ''
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
        type: this.SearchItem.type
      }
      try {
        const res = await deploymentFindAll(data)
        res.data.data.content.forEach(item => {
          switch (item.type) {
            case 'install':
              item.type = '自动安装'
              break
            case 'upgrade':
              item.type = '版本升级'
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
        console.log(even)
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
    add () {
      this.title = '添加'
      this.$refs.add.initial('')
    },
    edit (row) {
      this.title = '编辑'
      this.$refs.add.initial(row.id)
    },
    execute (row) {
      this.title = '执行'
      this.$refs.add.initial(row.id, 'execute')
    },
    SearchNoteList () {
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList()
    },
    deleteData(row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        const response = await deploymentDelete({ id: row.id })
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.getList()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.getList()
    }
  }
}
</script>
