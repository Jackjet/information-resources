<template>
  <el-main>
     <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-select clearable v-model="SearchItem.nodeId" @change="nodeChange" placeholder="请选择节点名称">
            <el-option
                v-for="item in nodeList"
                :key="item.value"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
         <el-form-item>
          <el-select clearable v-model="SearchItem.databaseId" placeholder="请选择数据库">
            <el-option
                v-for="item in databasesList"
                :key="item.value"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入实例名称" prefix-icon="el-icon-search" v-model="SearchItem.name">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"  size='medium' @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
          <el-button type="primary"  size='medium' @click="add" icon="el-icon-add">添加</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
    </el-col>
    <el-col :span='24'>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
     <drawer :modal-objjj='modalObjjj' ref="callDetail" @Reload="SearchNoteList"></drawer>
  </el-main>
</template>

<script>
import { findAllNodes, findAllDatabases, findAll, deleteIns } from '@/api/dataBase/dataBase'
import TableList from '@/components/table/TableList.vue'
import Pagination from '@/components/table/Pagination.vue'
import drawer from './detail'
export default {
  components: { TableList, Pagination, drawer },
  data() {
    return {
      drawer: false,
      tableData: [],
      tableHeader: [{
        id: false, type: '', label: '节点名称', list: 'nodeName'
      }, {
        id: false, type: '', label: '数据库名称', list: 'databaseName'
      }, {
        id: false, type: '', label: '实例名称', list: 'name'
      }, {
        id: false, type: '', label: '参数配置名称', list: 'configName'
      }, {
        id: false, type: '', label: '备注', list: 'remark'
      }
      ],
      tableOpction: {
        label: '操作',
        width: '300px',
        value: 0,
        options: [{
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
            this.delete(row)
          }
        }
        ]
      },
      nodeList: [],
      databasesList: [],
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObjjj: '',
      SearchItem: {
        name: '',
        nodeId: '',
        databaseId: ''
      },
      lastItem: {
        name: '',
        nodeId: '',
        databaseId: ''
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: ''
    }
  },
  created() {
    this.fetchData()
    this.findNodes()
    this.findDatabases() 
  },
  methods: {
    async fetchData() {
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        name: this.SearchItem.name,
        nodeId: this.SearchItem.nodeId,
        databaseId: this.SearchItem.databaseId,
      }
      try {
        const res = await findAll(data)
        this.tableData = res.data.data.content
        this.total = res.data.data.totalElements
      } catch (even) {
        this.$message.error(even.msg)
      }
    },

    edit: function(row) {
      this.modalObjjj = '编辑'
      this.$refs.callDetail.initial(row.id)
    },

    add: function() {
      this.modalObjjj = '添加'
      this.$refs.callDetail.initial(false)
    },
    SearchNoteList(){
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.fetchData()
    },

    pageChange(item) {
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.fetchData()
    },

    // 删除当前单条数据 重载列表
    delete(row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async() => {
        that.Loading = true
        let data = {
            id: row.id
        }
        const response = await deleteIns(data)
        that.Loading = false
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

    async findNodes() {
      const res = await findAllNodes();
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        });
      });
    },
    nodeChange() {
      this.SearchItem.databaseId = "";
      this.databasesList = [];
      this.findDatabases();
    },

    async findDatabases() {
      const data = {
        nodeId: this.SearchItem.nodeId
      }
      const res = await findAllDatabases(data);
      res.data.data.forEach(item => {
        this.databasesList.push({
          id: item.id,
          name: item.name
        });
      });
    }

  }
}
</script>
