<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入需求标题" v-model="SearchItem.title">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.requestType" placeholder="请选择受理需求">
            <el-option key="0" label="资源目录变更" value="0"></el-option>
            <el-option key="1" label="资源目录新增" value="1"></el-option>
            <el-option key="2" label="资源数据变更" value="2"></el-option>
            <el-option key="3" label="资源数据新增" value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.status" placeholder="请选择受理状态">
            <el-option key="0" label="未受理" value="0"></el-option>
            <el-option key="1" label="已受理" value="1"></el-option>
            <el-option key="2" label="已驳回" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-date-picker size="medium" v-model="time" value-format="yyyy-MM-dd HH:mm:ss" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        <el-form-item style="margin-left: 1%">
          <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
          <!-- <el-button size="medium" @click="outUser" icon="el-icon-refresh-left">导出</el-button> -->
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
  </el-main>
</template>
<script>
import { demandedInfoFindAll } from "@/api/demandedInfo.js"
import { organizationFindAll } from "@/api/organization.js";
import { outExcel1 } from '@/utils/export'
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
export default {
  components: { TableList, Pagination },
  data() {
    return {
      props: { checkStrictly: true },
      organizationOptions: [],
      tableSelection: {
        key: true,
        type: "",
        detaile: false,
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: "html",
          label: "序号",
          list: "sort",
          code: (row) => {
            return "<span>" + row.sort + "</span>";
          },
        },
        {
          type: "html",
          label: "申请单位",
          list: "createDeptName",
          code: (row) => {
            if (row.createDeptName === null) {
              return "";
            } else {
              return "<span>" + row.createDeptName + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "需求标题",
          list: "title",
          code: (row) => {
            return "<span>" + row.title + "</span>";
          },
        },
        {
          type: "html",
          label: "受理单位",
          list: "acceptDept",
          code: (row) => {
            if (row.acceptDept === null) {
              return "";
            } else {
              return "<span>" + row.acceptDept + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "申请日期",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "受理状态",
          list: "status",
          code: (row) => {
            switch (row.status) {
              case "0":
                return '<span>未受理</span>'
                break;
              case "1":
                return '<span>已受理</span>'
                break
              case "2":
                return '<span>已驳回</span>'
                break;
            }
          },
        },
        {
          type: "html",
          label: "受理需求",
          list: "requestType",
          code: (row) => {
            switch (row.requestType) {
              case "0":
                return '<span>' + "资源目录变更" + '</span>'
                break;
              case "1":
                return '<span>' + "资源目录新增" + '</span>'
                break
              case "2":
                return '<span>' + "资源数据变更" + '</span>'
                break;
              case "3":
                return '<span>' + "资源数据新增" + '</span>'
                break;
            }
          },
        },
      ],
      tableOpction: {
        label: "",
        width: "",
        value: 0,
        options: [],
      },
      time: this.$route.query.queryTime,
      SearchItem: {},
      lastItem: {},
      total: 0,
      pageSize: "20",
      currentPage: "1",
    };
  },
  created() {
    this.fetchData();
    this.findorganizations()
  },
  methods: {
    // 重置
    reset() {
      const that = this;
      that.SearchItem.title = '';
      that.SearchItem.requestType = '';
      that.SearchItem.status = '';
      that.SearchItem.startTime = '';
      that.SearchItem.endTime = '';
      that.time = [];
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
    },
    // 获取列表
    async fetchData(type) {
      const that = this;
      let data = {};
      data = this.SearchItem
      data.createDeptId = this.$route.query.id
      data.page = this.currentPage
      data.size = this.pageSize
      if (type !== 'page') {
        if (that.time !== null && that.time !== '' && that.time.length > 0) {
          data.startTime = this.time[0],
            data.endTime = this.time[1]
        } else {
          data.startTime = '',
            data.endTime = ''
        }
      }

      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      try {
        that.isSubmitLoading = true;
        const res = await demandedInfoFindAll(data);
        that.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.content;
          this.tableData.forEach((v, i) => {
            v.sort = i + 1
          })
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
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData();
    },
    // 翻页
    pageChange(item) {
      let that = this;
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]];
      });
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      this.fetchData("page");
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
    },
    // 导出模板
    outUser() {
      let that = this
      let data = {};
      (data = this.SearchItem)
      if (that.time !== null && that.time !== '' && that.time.length > 0) {
        data.startTime = this.time[0],
          data.endTime = this.time[1]
      } else {
        data.startTime = '',
          data.endTime = ''
      }
      outExcel1('请确认是否导出统计详情?', "webadmin/resourceUseInfo/exportByProvOrgId", data, '统计详情')
    },
  },
};
</script>
