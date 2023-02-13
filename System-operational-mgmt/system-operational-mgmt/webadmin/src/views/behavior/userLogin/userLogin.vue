<template>
  <el-main>
    <el-row>
      <el-col :span="24">
        <el-form :inline="true" class="el-InputForm">
          <el-form-item>
            <el-date-picker v-model="valueTime" :picker-options="pickerOptions" size="medium" type="daterange" value-format="yyyy-MM-dd" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="medium" @click="SearchNoteList" icon="el-icon-search">搜索</el-button>
            <el-button type="primary" size="medium" @click="outPdf" icon="">导出PDF</el-button>
            <el-button type="primary" size="medium" @click="outExcel" icon="">导出Excel</el-button>
          </el-form-item>
          <el-form-item>
            <div>
              用户总登录次数: <span>{{ total }}</span>
            </div>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction"></TableList>
      </el-col>
      <el-col :span="24">
        <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      </el-col>
    </el-row>
    <el-row>
      <bar-line ref="line" v-if="lock" :wid="24" :optionss="Data" />
    </el-row>
  </el-main>
</template>

<script>
import { getLoginInfoList, accessTrend } from "@/api/behavior/loginInfo";
import { getYear, fun_date } from "@/utils/date";
import TableList from "@/components/table/TableList.vue";
import Pagination from "@/components/table/Pagination.vue";
import BarLine from "@/components/echarts/BarLine";
import { getToken } from "@/utils/auth";
import axios from "axios";
export default {
  components: { TableList, Pagination, BarLine },
  data() {
    return {
      drawer: false,
      tableData: [],
      valueTime: [],
      pickerOptions: {
        onPick: ({ maxDate, minDate }) => {
          this.selectDate = minDate.getTime();
          if (maxDate) {
            this.selectDate = "";
          }
        },
        disabledDate: time => {
          if (this.selectDate !== "") {
            const one = 30 * 24 * 3600 * 1000;
            const minTime = this.selectDate - one;
            const maxTime = this.selectDate + one;
            return time.getTime() < minTime || time.getTime() > maxTime;
          }
        }
      },
      tableHeader: [
        {
          id: false,
          type: "",
          label: "用户",
          list: "name"
        },
        {
          id: false,
          type: "",
          label: "用户ip",
          list: "ip"
        },
        {
          id: false,
          type: "",
          label: "登录时间",
          list: "loginTime"
        },
        {
          id: false,
          type: "",
          label: "登录系统",
          list: "type"
        },
        {
          id: false, type: 'html', label: '登录方式', list: '', html: (row) => { return '电脑登录' }
        }
      ],
      tableOpction: {},
      isSubmitLoading: false,
      DeletelistiD: [],
      modalObj: "",
      Data: [],
      SearchItem: {
        name: "",
        type: "",
        startTime: "",
        endTime: ""
      },
      lastItem: {
        name: "",
        type: "",
        startTime: "",
        endTime: ""
      },
      lock: true,
      total: 0,
      pageSize: "20",
      currentPage: "1",
      RootUrl: ""
    };
  },
  created() {
    this.valueTime = [fun_date(), getYear()];
    this.fetchData();
    this.getList();
  },
  methods: {
    async fetchData() {
      if (this.valueTime) {
        this.SearchItem.startTime = this.valueTime[0];
        this.SearchItem.endTime = this.valueTime[1];
      } else {
        this.SearchItem.startTime = "";
        this.SearchItem.endTime = "";
      }
      if (this.SearchItem.startTime === '') {
        this.$message.error('请选择日期');
        return false
      }
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        startTime: this.SearchItem.startTime,
        endTime: this.SearchItem.endTime
      };
      try {
        const ress = await getLoginInfoList(data);
        if (ress.data.code === 1) {
          this.tableData = ress.data.data.content;
          console.log(ress.data.data.totalElements)
          this.total = ress.data.data.totalElements;
        } else {
          this.$message.error(ress.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    async getList() {
      const that = this;
      if (this.valueTime) {
        this.SearchItem.startTime = this.valueTime[0];
        this.SearchItem.endTime = this.valueTime[1];
      } else {
        this.SearchItem.startTime = "";
        this.SearchItem.endTime = "";
      }
      const data = {
        startTime: this.SearchItem.startTime,
        endTime: this.SearchItem.endTime
      };
      try {
        that.lock = false;
        const res = await accessTrend(data);
        if (res.data.code === 1) {
          that.Data = [];
          let month = [];
          let num = [];
          res.data.data.map(function (v, k) {
            month.push(v.date);
            num.push(v.number);
          });
          that.Data.push({
            y: "bottom",
            name: "用户访问量趋势图",
            unit: "日访问次数",
            legend: [],
            month: month,
            data: num,
            data2: []
          });
          that.lock = true;
          // this.total = res.data.data.number;
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },

    SearchNoteList() {
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.fetchData();
      this.getList();
      // setTimeout(() => {
      //   this.$refs.line.nextTick();
      // }, 500);
    },
    submitLoading() {
      this.fetchData();
    },

    pageChange(item) {
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.fetchData();
    },
    outExcel() {
      const that = this;
      that
        .$confirm("是否导出Excel?", "提示", {
          type: "warning"
        })
        .then(async () => {
          if (this.valueTime) {
            this.SearchItem.startTime = this.valueTime[0];
            this.SearchItem.endTime = this.valueTime[1];
          } else {
            this.SearchItem.startTime = "";
            this.SearchItem.endTime = "";
          }
          const time = {
            startTime: this.SearchItem.startTime,
            endTime: this.SearchItem.endTime
          };
          axios({
            method: "get",
            url:
              process.env.VUE_APP_BASE_API + "/webadmin/loginInfo/excelExport",
            params: time,
            headers: {
              Authorization: "token " + getToken(),
              "Content-Type": "multipart/form-data"
            },
            responseType: "blob"
          })
            .then(data => {
              if (data.data.type === "application/json") {
                var reader = new FileReader();
                reader.onloadend = function () {
                  let res = JSON.parse(reader.result);
                  if (res && res.msg) {
                    // that.$message.warning(res.msg + "," + res.data);
                    that.$message.warning(res.msg);

                  }
                };
                reader.readAsText(data.data);
                return;
              }
              let url = window.URL.createObjectURL(new Blob([data.data]));
              let link = document.createElement("a");
              link.style.display = "none";
              link.href = url;
              link.setAttribute("download", "用户登录信息.xls");
              document.body.appendChild(link);
              link.click();
            })
            .catch(data => {
              console.log("导出失败");
            });
        })
        .catch(() => {
          return false;
        });
    },
    outPdf() {
      const that = this;
      that
        .$confirm("是否导出PDF?", "提示", {
          type: "warning"
        })
        .then(async () => {
          if (this.valueTime) {
            this.SearchItem.startTime = this.valueTime[0];
            this.SearchItem.endTime = this.valueTime[1];
          } else {
            this.SearchItem.startTime = "";
            this.SearchItem.endTime = "";
          }
          const time = {
            startTime: this.SearchItem.startTime,
            endTime: this.SearchItem.endTime
          };
          axios({
            method: "get",
            url: process.env.VUE_APP_BASE_API + "/webadmin/loginInfo/pdfExport",
            params: time,
            headers: {
              Authorization: "token " + getToken(),
              Accept: "application/pdf"
              // 'Content-Type': 'application/octet-stream;charset=utf-8',
            },
            responseType: "arraybuffer"
          })
            .then(data => {
              const _res = data.data;
              let blob = new Blob([_res], { type: "application/pdf" });
              let href = window.URL.createObjectURL(blob);
              if ("download" in document.createElement("a")) {
                // 非IE下载
                let downloadElement = document.createElement("a");
                downloadElement.href = href;
                downloadElement.download = "用户登录信息.pdf";
                document.body.appendChild(downloadElement);
                downloadElement.click();
                document.body.removeChild(downloadElement);
                window.URL.revokeObjectURL(href);
                document.body.removeChild(elink);
              } else {
                // IE10+下载
                navigator.msSaveBlob(blob, fileName);
              }
            })
            .catch(data => {
              console.log("导出失败");
            });
        })
        .catch(() => {
          return false;
        });
    }
  }
};
</script>
