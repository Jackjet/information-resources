<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <el-form-item>
          <el-cascader size="medium" placeholder="请选择提供单位" :props="props" collapse-tags clearable v-model="SearchItem.orgId" :options="organizationOptions" />
        </el-form-item>
        <!-- <el-form-item>
          <el-cascader size="medium" placeholder="请选择纠错单位" :props="props" collapse-tags clearable v-model="SearchItem.correctionOrgId" :options="organizationOptions" />
        </el-form-item> -->
        <el-form-item>
          <el-select clearable size="medium" v-model="SearchItem.status" placeholder="请选择状态">
            <el-option key="0" label="未处理" value="0"></el-option>
            <el-option key="1" label="已处理" value="1"></el-option>
            <el-option key="2" label="已驳回" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="提交时间">
          <el-date-picker size="medium" v-model="time" value-format="yyyy-MM-dd HH:mm:ss" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" :default-time="['00:00:00', '23:59:59']">
          </el-date-picker>
        </el-form-item>
        <el-form-item style="margin-left: 1%">
          <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
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
import { correctionFindAll, resourceUseInfoUpdate } from "@/api/resourceUseInfo.js";
import { organizationFindAll } from "@/api/organization.js";
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
import { getCookies } from '@/utils/auth';
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
          label: "纠错单位",
          list: "correctionOrgName",
          code: (row) => {
            return "<span>" + row.correctionOrgName + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "orgName",
          code: (row) => {
            if (row.orgName === null) {
              return "";
            } else {
              return "<span>" + row.orgName + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "提交时间",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "受理时间",
          list: "provOrgName",
          code: (row) => {
            if (row.updateTime === null) {
              return "";
            } else {
              return "<span>" + row.updateTime.split(' ')[0] + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            switch (row.status) {
              case 0:
                return "<span>" + "未处理" + "</span>";
                break;
              case 1:
                return "<span>" + "已处理" + "</span>";
                break;
              case 2:
                return "<span>" + "已驳回" + "</span>";
                break;
              default:
                return "<span>" + "未知" + "</span>";
                break;
            }
          },
        },
      ],
      tableOpction: {
        label: "操作",
        width: "200px",
        value: 0,
        options: [
          {
            label: "详情",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            method: (row) => {
              this.handleDetail(row);
            },
          },
          //   {
          //     label: "审核",
          //     key: 0,
          //     type: "text",
          //     icon: "el-icon-tickets",
          //     State: false,
          //     show: (row) => {
          //       return row.status !== 1;
          //     },
          //     method: (row) => {
          //       this.handleUpdate(row);
          //     },
          //   },
        ],
      },
      time: [],
      SearchItem: {
        status: "",
        // correctionOrgId: "",
        orgId: "",
      },
      lastItem: {
        status: "",
        // correctionOrgId: "",
        orgId: "",
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
    };
  },
  created() {
    this.fetchData('');
    this.findorganizations()
  },
  methods: {
    // 重置
    reset() {
      const that = this;
      (that.SearchItem.uviewNm = ""),
        (that.SearchItem.uviewNo = ""),
        // (that.SearchItem.correctionOrgId = ""),
        (that.SearchItem.sourceApiName = ""),
        (that.SearchItem.status = ""),
        (that.SearchItem.orgId = ""),
        (that.SearchItem.startTime = ''),
        (that.SearchItem.endTime = ''),
        (that.time = []);
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      this.fetchData('')
    },
    // 获取列表
    async fetchData(type) {
      const that = this;
      let data = {};
      data.status = this.SearchItem.status;
      data.page = this.currentPage;
      data.size = this.pageSize;
      if (this.SearchItem.orgId != '') {
        if (this.SearchItem.orgId != '') {
          data.orgId = this.SearchItem.orgId[this.SearchItem.orgId.length - 1];
        }
      }
      data.correctionOrgId = JSON.parse(getCookies('userInfo')).organizations[0];
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
        const res = await correctionFindAll(data);
        that.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.content;
          this.tableData.forEach((v, i) => {
            v.createTime = v.createTime.split(' ')[0]
            v.updateTime = v.updateTime.split(' ')[0]
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
      this.fetchData('');
    },
    // 翻页
    pageChange(item) {
      let that = this;
      this.pageSize = item.limit;
      this.currentPage = item.page;
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]];
      });
      if (that.lastItem.startTime === '' || that.lastItem.startTime === null) {
        that.time = []
      }
      this.fetchData("page");
    },
    // 详情
    handleDetail(data) {
      const that = this;
      that.$router.push({
        path: "/weEditErrorDetail",
        query: {
          id: data.id,
        },
      });
    },
    //审核
    handleUpdate(data) {
      const that = this;
      that.$router.push({
        path: "/editErrorUpdate",
        query: {
          id: data.id,
        },
      });
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
          newNode.value = node.id;
          newNode.label = node.name;
          if (node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode);
        }
      }
      return itemArr;
    }
  },
};
</script>
