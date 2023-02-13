<template>
  <el-main class="lunbo-main" v-loading="Loading">
    <el-row>
      <el-col class="lunbo-btn" :span='24' style="text-align: left">
        <el-form :inline="true" class='el-InputForm'>
          <el-row v-if="tab.type == 0">
            <el-form-item>
              <el-input class='logTime' clearable size="medium" placeholder="请输入操作员" prefix-icon="el-icon-search" v-model="SearchItem.operator">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input class='logTime' clearable size="medium" placeholder="请输入事件类型" prefix-icon="el-icon-search" v-model="SearchItem.type">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input class='logTime' clearable size="medium" placeholder="请输入事件源" prefix-icon="el-icon-search" v-model="SearchItem.source">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input class='logTime' clearable size="medium" placeholder="请输入操作对象" prefix-icon="el-icon-search" v-model="SearchItem.operand">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker class='logTimee' v-model="valueTime" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size='medium' icon="el-icon-search" @click="search">搜索
              </el-button>
            </el-form-item>
          </el-row>
          <el-row v-if="tab.type == 1">
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入事件源" prefix-icon="el-icon-search" v-model="SearchItem.source">
              </el-input>
            </el-form-item>
            <el-date-picker v-model="valueTime" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
            </el-date-picker>
            <el-form-item>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size='medium' icon="el-icon-search" style="margin-left:10px" @click="search">搜索
              </el-button>
            </el-form-item>
          </el-row>
          <el-row v-if="tab.type == 2">
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入事件类型" prefix-icon="el-icon-search" v-model="SearchItem.type">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-input clearable size="medium" placeholder="请输入操作员" prefix-icon="el-icon-search" v-model="SearchItem.operator">
              </el-input>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="valueTime" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size='medium' icon="el-icon-search" @click="search">搜索
              </el-button>
            </el-form-item>
          </el-row>
        </el-form>
      </el-col>
    </el-row>
    <el-tabs class="lunbo-tab" clearable v-model="tab.type" type="border-card" @tab-click="initial()">
      <el-tab-pane v-for="item in typeList" :key="item.value" :label="item.label" :name="item.value">
        <!-- 调用表格 -->
        <el-col :span="24">
          <tableForm :table-data='tableData' v-loading="isSubmitLoading" @onHandleSelectionChange="handleSelectionChange" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOption">
          </tableForm>
        </el-col>
        <!-- 调用分页 -->
        <el-col :span='24'>
          <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
        </el-col>
      </el-tab-pane>
    </el-tabs>
    <drawer :modal-objj='modalObjj' ref="callDetaill" @Reload='submitLoadingg'>
    </drawer>
  </el-main>
</template>

<script>
import {
  getOperationLogList,
  getSystemLogList,
  getAuditLogList
} from "@/api/omManger/log";
import tableForm from "@/components/table/TableList.vue";
import paging from "@/components/table/Pagination.vue";
import drawer from "./detail";

export default {
  components: { tableForm, paging, drawer },
  data() {
    return {
      tableData: [],
      tableSelection: {
        key: true,
        type: "",
        detaile: false
      },
      valueTime: "",
      lastTime: '',
      SearchItem: {
        source: "",
        operator: "",
        type: "",
        operand: "",
        time: "",
        startTime: "",
        endTime: ""
      },
      lastItem: {
        source: "",
        operator: "",
        type: "",
        operand: "",
        time: "",
        startTime: "",
        endTime: ""
      },
      tab: {
        type: ""
      },
      tableHeader: [],
      editableTabsValue: "0",
      tableOption: {},
      typeList: [
        {
          value: "0",
          label: "操作日志"
        },
        {
          value: "1",
          label: "系统日志"
        },
        {
          value: "2",
          label: "登录日志"
        }
      ],
      searchObj: {},
      QiniuToken: "",
      Loading: false,
      modalObjj: "", // 弹层操作标题
      modalObjjj: "", // 弹层操作标题
      modalObjjf: "",
      isSubmitLoading: false, // 表格加载效果
      deleteList: [], // 批量删除数据集
      total: 0, // 总条数
      currentPage: 1, // 起始页
      pageSize: 20 // 条数
    };
  },

  mounted() {
    this.getList();
  },

  methods: {
    async getList() {
      const that = this;
      that.isSubmitLoading = true;

      if (this.tab.type === "0") {
        this.tableHeader = [];
        this.tableOption = {
          label: "操作",
          width: "200px",
          value: 0,
          options: [
            {
              label: "详情",
              key: 0,
              type: "primary",
              State: false,
              method: row => {
                this.detail(row);
              }
            }
          ]
        };
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "事件类型",
            list: "type"
          },
          {
            id: false,
            type: "",
            label: "操作员",
            list: "operator"
          },
          {
            id: false,
            type: "",
            label: "操作时间",
            list: "createTime"
          },
          {
            id: false,
            type: "",
            label: "事件源",
            list: "source"
          },
          {
            id: false,
            type: "",
            label: "操作对象",
            list: "operand"
          },
          {
            id: false,
            type: "",
            label: "操作结果",
            list: "result"
          },
          {
            id: false,
            type: "",
            label: "详细描述",
            list: "description"
          }
        );
      } else if (this.tab.type === "1") {
        this.tableHeader = [];
        this.tableOption = {};
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "事件源",
            list: "source"
          },
          {
            id: false,
            type: "",
            label: "发生时间",
            list: "createTime"
          },
          {
            id: false,
            type: "",
            label: "事件信息",
            list: "info"
          },
          {
            id: false,
            type: "",
            label: "事件结果",
            list: "result"
          }
        );
      } else if (this.tab.type === "2") {
        this.tableHeader = [];
        this.tableOption = {};
        this.tableHeader.push(
          {
            // sort: false,
            id: false,
            type: "",
            label: "事件类型",
            list: "type"
          },
          {
            id: false,
            type: "",
            label: "操作员",
            list: "operator"
          },
          {
            id: false,
            type: "",
            label: "登录IP",
            list: "loginIp"
          },
          {
            id: false,
            type: "",
            label: "操作时间",
            list: "createTime"
          },
          {
            id: false,
            type: "",
            label: "事件级别",
            list: "level"
          },
          {
            id: false,
            type: "",
            label: "操作对象",
            list: "operand"
          },
          {
            id: false,
            type: "",
            label: "操作结果",
            list: "result"
          }
        );
      }

      try {
        let findUrl = null;
        const data = {
          page: this.currentPage,
          size: this.pageSize,
          name: this.SearchItem.name,
          startTime: this.SearchItem.startTime,
          endTime: this.SearchItem.endTime,
          operator: this.SearchItem.operator,
          type: this.SearchItem.type,
          operand: this.SearchItem.operand,
          source: this.SearchItem.source
        };
        switch (that.tab.type) {
          case "0":
            findUrl = getOperationLogList(data);
            break;
          case "1":
            findUrl = getSystemLogList(data);
            break;
          case "2":
            findUrl = getAuditLogList(data);
            break;
        }
        const response = await findUrl;
        response.data.data.content.map(function (v, k) {
          v.level = "普通";
          if (v.result === "1") {
            v.result = "成功";
          } else if (v.result === "0") {
            v.result = "失败";
          }
        });
        response.data.data.content.forEach(item => {
          if (item.description) {
            let str = item.description.slice(0, 13);
            item.description = str + "...";
          }
        });
        that.tableData = response.data.data.content;
        that.total = response.data.data.totalElements;
        that.isSubmitLoading = false;
      } catch (even) {
        console.log(even);
        that.isSubmitLoading = false;
        that.$message.error("数据获取失败");
      }
    },
    initial() {
      this.SearchItem = {
        source: "",
        operator: "",
        type: "",
        operand: "",
        time: "",
        startTime: "",
        endTime: ""
      };
      this.getList();
    },
    search() {
      let that = this;
      if (that.valueTime) {
        that.SearchItem.startTime = that.valueTime[0] + " 00:00:00";
        that.SearchItem.endTime = that.valueTime[1] + " 23:59:59";
      } else {
        that.SearchItem.startTime = "";
        that.SearchItem.endTime = "";
      }
      this.currentPage = 1;
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.lastTime = this.valueTime
      this.getList();
    },
    detail: function (row) {
      let that = this;
      that.modalObjj = "详情";
      that.$refs.callDetaill.initial(row.id);
    },
    pageChange(item) {
      this.currentPage = item.page;
      this.pageSize = item.limit;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.valueTime = this.lastTime
      this.getList();
    }
  }
};
</script>

<style lang="scss" scoped>
.lunbo-tab {
  margin-top: 10vh;
}
.lunbo-main {
  position: relative;

  .lunbo-btn {
    position: absolute;
    top: 0;
    left: 0px;
  }
}
.logTime {
  width: 170px;
}
.logTimee {
  width: 260px;
}
</style>
