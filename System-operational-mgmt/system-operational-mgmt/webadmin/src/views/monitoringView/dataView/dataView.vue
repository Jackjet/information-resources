<template>
  <div class="echart-container ">
    <el-row>
      <el-col :span="24">
        <el-form :inline="true" class="el-InputForm">
          <el-form-item>
            <el-input
              clearable
              size="medium"
              placeholder="请输入节点名称"
              prefix-icon="el-icon-search"
              v-model="SearchItem.nodeName"
            ></el-input>
          </el-form-item>
          <el-form-item v-show="isShow">
            <el-input
              clearable
              size="medium"
              placeholder="请输入系统名称"
              prefix-icon="el-icon-search"
              v-model="SearchItem.systemName"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="valueTime"
              size="medium"
              :picker-options="pickerOptions"
              type="daterange"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              size="medium"
              @click="SearchNoteList"
              icon="el-icon-search"
              >搜索</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
    <el-row :gutter="32" class="bo">
      <el-col :xs="24" :sm="24" :lg="24">
        <el-tabs
          class="lunbo-tab"
          clearable
          v-model="tab.type"
          type="border-card"
          @tab-click="initial()"
        >
          <el-tab-pane
            v-for="item in typeList"
            :label="item.label"
            :key="item.key"
            :name="item.name"
          >
            <bar-Chart
              ref="chart"
              v-if="item.value === '0' && lock"
              :options="Data"
            />
            <bar-line
              ref="line"
              v-if="item.value === '1' && lock"
              :optionss="Data"
            />
          </el-tab-pane>
        </el-tabs>
      </el-col>
    </el-row>
    <el-row class="pag">
      <el-pagination
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        layout="prev, pager, next"
        :page-size="6"
        :current-page="currentPage"
        :total="total"
      >
      </el-pagination>
    </el-row>
  </div>
</template>
<script>
import {
  nodeStatusAnalysis,
  systemStatusAnalysis
} from "@/api/monitoringView/data";
import { getYear, getBegin } from "@/utils/date";
import BarChart from "@/components/echarts/BarChart";
import BarLine from "@/components/echarts/BarLine";
export default {
  name: "ECharts",
  components: { BarLine, BarChart },
  data() {
    return {
      activeName: "first",
      isShow: false,
      DataLine: [],
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
      Data: [],
      SearchItem: {
        beginTime: "",
        endTime: "",
        nodeName: "",
        systemName: ""
      },
      lastItem: {
        beginTime: "",
        endTime: "",
        nodeName: "",
        systemName: ""
      },
      lock: true,
      tab: {
        type: "0"
      },
      typeList: [
        {
          value: "0",
          label: "节点状态分析"
        },
        {
          value: "1",
          label: "系统状态分析"
        }
      ],
      total: 20,
      pageSize: "6",
      currentPage: "1"
    };
  },
  created() {
    this.valueTime = [getBegin(), getYear()];
    this.getList();
  },
  methods: {
    async getList() {
      const that = this;
      if (that.valueTime) {
        that.SearchItem.beginTime = that.valueTime[0];
        that.SearchItem.endTime = that.valueTime[1];
      } else {
        that.SearchItem.beginTime = "";
        that.SearchItem.endTime = "";
      }
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        beginTime: this.SearchItem.beginTime,
        endTime: this.SearchItem.endTime,
        nodeName: this.SearchItem.nodeName,
        systemName: this.SearchItem.systemName
      };
      let url = "";
      if (that.tab.type === "0") {
        url = nodeStatusAnalysis(data);
      } else if (that.tab.type === "1") {
        url = systemStatusAnalysis(data);
      }
      try {
        that.lock = false;
        const res = await url;
        if (res.data.code === 1) {
          that.Data = [];
          if (that.tab.type === "0") {
            res.data.data.lists.map(function(v, k) {
              that.Data.push({
                name: v.name,
                unit: "次",
                legend: [],
                month: v.data.month,
                data: v.data.normal,
                data2: v.data.unusual
              });
            });
          } else {
            res.data.data.lists.map(function(v) {
              that.Data.push({
                y: "bottom",
                unit: "",
                legend: ["储存（M）", "内存（M）"],
                name: v.name,
                month: v.data.datetime,
                data: v.data.dataSource,
                data2: v.data.memorySource
              });
            });
          }
          that.lock = true;
          this.total = res.data.data.number;
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    initial() {
      this.currentPage = "1";
      if (this.tab.type === "1") {
        this.isShow = true;
      } else {
        this.isShow = false;
      }
      this.getList();
    },
    SearchNoteList() {
      let that = this;
      if (that.valueTime) {
        that.SearchItem.beginTime = that.valueTime[0];
        that.SearchItem.endTime = that.valueTime[1];
      } else {
        that.SearchItem.beginTime = "";
        that.SearchItem.endTime = "";
      }
      this.currentPage = 1
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList();
      setTimeout(() => {
        if (that.tab.type === "0") {
          this.$refs.chart.nextTick();
        } else {
          this.$refs.line.nextTick();
        }
      }, 500);
    },
    handleCurrentChange(item) {
      this.pageSize = "6";
      this.currentPage = item;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.getList();
    }
  }
};
</script>
<style lang="scss" scoped>
.echart-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;
  height: 100vh;
  .row-style {
    height: 50%;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
  .pag {
    margin-top: 20px;
  }
  .bo {
    min-height: 421px;
  }
}
.lunbo-tab {
  min-height: 421px;
}
@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>