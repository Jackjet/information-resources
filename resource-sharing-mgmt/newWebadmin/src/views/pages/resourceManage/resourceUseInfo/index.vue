<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">
        <!-- <el-form-item>
          <el-input clearable size="medium" placeholder="请输入云数据名称" v-model="SearchItem.sourceApiName">
          </el-input>
        </el-form-item> -->

        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入资源名称" v-model="SearchItem.resourceName">
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入信息资源名称" v-model="SearchItem.uviewNm">
          </el-input>
        </el-form-item>
        <!-- <el-form-item>
          <el-input
            clearable
            size="medium"
            placeholder="请输入信息资源代码"
            v-model="SearchItem.uviewNo"
          >
          </el-input>
        </el-form-item> -->

        <el-form-item>
          <el-select clearable v-model="SearchItem.type" placeholder="请选择资源类型">
            <el-option label="云接口" value="1"></el-option>
            <el-option label="云文件" value="2"></el-option>
            <el-option label="云数据" value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <!-- <el-input
            clearable
            size="medium"
            placeholder="请输入提供单位名称"
            v-model="SearchItem.provOrgCode"
          >
          </el-input> -->
          <el-cascader size="medium" placeholder="请选择提供单位名称" :props="props" collapse-tags clearable v-model="SearchItem.provOrgName" :options="organizationOptions" />
        </el-form-item>
        <el-form-item>
          <!-- <el-input
            clearable
            size="medium"
            placeholder="请输入申请单位名称"
            v-model="SearchItem.createDeptName"
          >
          </el-input> -->
          <el-cascader size="medium" placeholder="请选择申请单位名称" :props="props" collapse-tags clearable v-model="SearchItem.createDeptName" :options="organizationOptions" />
        </el-form-item>
        <el-form-item>
          <el-select clearable v-model="SearchItem.status" placeholder="请选择审核状态">
            <!-- <el-option key="0" label="未审核" value="0"></el-option>
            <el-option key="1" label="初审通过" value="1"></el-option>
            <el-option key="2" label="已审核" value="2"></el-option> -->
            <el-option key="0" label="待审核" value="0"></el-option>
            <el-option key="1" label="审核通过待实施" value="1"></el-option>
            <el-option key="2" label="已实施" value="2"></el-option>
            <el-option key="3" label="已驳回" value="3"></el-option>
            <el-option key="4" label="审核失败" value="4"></el-option>
          </el-select>
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
import { resourceUseInfoFindAll, resourceUseInfoUpdate } from "@/api/resourceUseInfo.js";
import { organizationFindAll } from "@/api/organization.js";
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
          label: "资源名称",
          list: "resourceName",
          code: (row) => {
            return "<span>" + row.resourceName + "</span>";
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
          label: "申请单位",
          list: "createDeptName",
          code: (row) => {
            return "<span>" + row.createDeptName + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "provOrgName",
          code: (row) => {
            if (row.provOrgName === null) {
              return "";
            } else {
              return "<span>" + row.provOrgName + "</span>";
            }
          },
        },
        {
          type: "html",
          label: "资源类型",
          list: "type",
          code: (row) => {
            if (row.type == 1) {
              return "<span>云接口</span>";
            } else if (row.type == 2) {
              return "<span>云文件</span>";
            } else if (row.type == 3) {
              return "<span>云数据</span>";
            }
          },
        },
        {
          type: "html",
          label: "提交日期",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            switch (row.status) {
              case "0":
                return "<span>待审核</span>";
                break;
              case "1":
                return "<span>审核通过待实施</span>";
                break;
              case "2":
                return "<span>已实施</span>";
                break;
              case "3":
                return "<span>已驳回</span>";
                break;
              default:
                return "<span>审核失败</span>";
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
          // {
          //   label: "审核",
          //   key: 0,
          //   type: "text",
          //   icon: "el-icon-tickets",
          //   State: false,
          //   show: (row) => {
          //     if (row.status === "0") {
          //       return true;
          //     } else {
          //       return false;
          //     }
          //   },
          //   method: (row) => {
          //     this.handleUpdate(row);
          //   },
          // },
          {
            label: "重新提交",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            show: (row) => {
              if (row.status === "4") {
                return true;
              } else {
                return false;
              }
            },
            method: (row) => {
              this.handleAgain(row);
            },
          },
          {
            label: "日志",
            key: 0,
            type: "text",
            icon: "",
            State: false,
            method: (row) => {
              this.handleJournal(row);
            },
          },
        ],
      },
      time: [],
      SearchItem: {
        uviewNm: "",
        uviewNo: "",
        sourceApiName: "",
        status: "",
        createDeptName: "",
        provOrgName: "",
        resourceName: "",
      },
      lastItem: {
        uviewNm: "",
        uviewNo: "",
        sourceApiName: "",
        status: "",
        createDeptName: "",
        provOrgName: "",
        sourceApiDesc: "",
        uviewApiId: "",
        resourceName: "",
      },
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
      that.SearchItem.uviewNm = ""
      that.SearchItem.uviewNo = ""
      that.SearchItem.createDeptName = ""
      that.SearchItem.sourceApiName = ""
      that.SearchItem.status = ""
      that.SearchItem.provOrgName = ""
      that.SearchItem.resourceName = ""
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
    },
    // 获取列表
    async fetchData(type) {
      const that = this;
      let data = {};
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize

      if (typeof data.provOrgName !== 'string' && typeof data.provOrgName !== 'undefined') {
        data.provOrgName = data.provOrgName[data.provOrgName.length - 1]
      }
      if (typeof data.createDeptName !== 'string' && typeof data.createDeptName !== 'undefined') {
        data.createDeptName = data.createDeptName[data.createDeptName.length - 1]
      }

      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      try {
        that.isSubmitLoading = true;
        const res = await resourceUseInfoFindAll(data);
        that.isSubmitLoading = false;
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
      this.fetchData("page");
    },
    // 详情
    handleDetail(data) {
      if (data.type === '1') {
        this.$router.push({
          path: "/assetApiExDetail",
          query: {
            id: data.id,
          },
        });
      } else {
        this.$router.push({
          path: "/resourceUseInfoDetail",
          query: {
            id: data.id,
          },
        });
      }
    },
    //审核
    handleUpdate(data) {
      const that = this;
      that.$router.push({
        path: "/resourceUseInfoUpdate",
        query: {
          id: data.id,
        },
      });
    },
    handleJournal(data) {
      const that = this;
      that.$router.push({
        path: "/resourceUseInfoJournal",
        query: {
          id: data.id,
        },
      });
    },
    handleAgain(row) {
      let data = {};
      data.id = row.id;
      data.perMinute = row.perMinute;
      data.everyHour = row.everyHour;
      data.everyDay = row.everyDay;
      data.singleSize = row.singleSize;
      data.auditDesc = row.auditDesc;
      data.status = "1";
      // 编辑
      resourceUseInfoUpdate(data).then((res) => {
        if (res.data.code === 1) {
          this.$message.success('审核成功');
          this.fetchData();
        } else {
          this.$message.error(res.data.msg);
        }
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
  },
};
</script>
