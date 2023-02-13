<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入规则名称" v-model="SearchItem.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.hasSystem" placeholder="请选择是否系统初始化">
            <el-option key="0" label="否" value="0"></el-option>
            <el-option key="1" label="是" value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item style="margin-left: 1%">
          <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <div style="padding-top: 20px;">
        <el-button type="primary" size='mini' @click="handleAdd" icon="el-icon-plus">新增</el-button>
      </div>
      <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { replaceRuleFindAll, replaceRuleDelete } from "@/api/replaceRule.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      isSubmitLoading: false,
      total: 0,
      SearchItem: {
        name: '',
        hasSystem: '',
        page: 1,
        size: 20,
      },
      lastItem: {
        name: '',
        hasSystem: '',
      },
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      tableData: [],
      tableHeader: [
        {
          type: 'html',
          label: '规则名称',
          list: 'name',
          code: (row) => {
            return '<span>' + row.name + '</span>'
          }
        },
        {
          type: 'html',
          label: '脱敏方式',
          list: 'type',
          code: (row) => {
            return '<span>' + row.type + '</span>'
          }
        },
        {
          type: 'html',
          label: '索引类型',
          list: 'indexType',
          code: (row) => {
            if (row.indexType === '1') {
              return '<span>位数</span>'
            } else {
              return '<span>字符</span>'
            }
          }
        },
        {
          type: 'html',
          label: '起始值',
          list: 'indexOf',
          code: (row) => {
            return '<span>' + row.indexOf + '</span>'
          }
        },
        {
          type: 'html',
          label: '结束值',
          list: 'lastIndexOf',
          code: (row) => {
            return '<span>' + row.lastIndexOf + '</span>'
          }
        },
        {
          type: 'html',
          label: '替换字符',
          list: 'replaceValue',
          code: (row) => {
            return '<span>' + row.replaceValue + '</span>'
          }
        },
        {
          type: 'html',
          label: '是否是系统初始化',
          list: 'hasSystem',
          code: (row) => {
            if (row.hasSystem == '1') {
              return '<span>是</span>'
            } else {
              return '<span>否</span>'
            }
          }
        },
        {
          type: 'html',
          label: '描述',
          list: 'describe',
          code: (row) => {
            if (row.describe === null) {
              return '<span></span>'
            } else {
              return '<span>' + row.describe + '</span>'
            }
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
            this.handleDelete(row)
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
      }
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    handleEdit(data) {
      this.$router.push({
        path: "/replaceRuleAdd",
        query: {
          id: data.id
        }
      });
    },
    handleDelete(data) {
      this.$confirm('此操作将永久删除, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        replaceRuleDelete(data.id).then(res => {
          if (res.data.code === 1) {
            this.$message.success(res.data.msg)
            this.fetchData();
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }).catch(() => { });
    },
    handleAdd() {
      this.$router.push("/replaceRuleAdd");
    },
    // 获取列表
    async fetchData() {
      try {
        this.isSubmitLoading = true;
        const res = await replaceRuleFindAll(this.SearchItem);
        this.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.content;
          this.total = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    // 搜索
    SearchNoteList() {
      this.SearchItem.page = 1;
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.SearchItem.size = item.limit
      this.SearchItem.page = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData('page')
    },
    // 重置
    reset() {
      const that = this
      that.SearchItem.name = ''
      that.SearchItem.hasSystem = ''
      that.SearchItem.page = 1;
      that.SearchItem.size = 20;
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
    },
  },

};
</script>
