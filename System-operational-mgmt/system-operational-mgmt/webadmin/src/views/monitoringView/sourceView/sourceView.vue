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
          <el-form-item>
            <el-date-picker
              v-model="valueTime"
              size="medium"
              type="daterange"
              :picker-options="pickerOptions"
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
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <bar-line ref="line" v-if="Data.length > 0" :optionss="Data" />
          <div v-else style="text-align: center;color:#999;">暂无数据</div>
        </div>
      </el-col>
    </el-row>
    <el-pagination
      background
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      :page-size="6"
      :total="total"
    >
    </el-pagination>
  </div>
</template>
<script>
import { getResourceMonitoring } from "@/api/monitoringView/data";
import { getYear, getBegin } from "@/utils/date";
import BarLine from "@/components/echarts/BarLine";
export default {
  name: "ECharts",
  components: { BarLine },
  data() {
    return {
      Data: [],
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
      SearchItem: {
        beginTime: "",
        endTime: "",
        nodeName: ""
      },
      lastItem: {
        beginTime: "",
        endTime: "",
        nodeName: ""
      },
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
        nodeName: this.SearchItem.nodeName
      };
      try {
        const res = await getResourceMonitoring(data);
        if (res.data.code === 1) {
          that.Data = [];
          res.data.data.lists.map(function(v) {
            that.Data.push({
              y: "bottom",
              unit: "",
              legend: ["储存（M）", "内存（M）", "空闲（cpu）"],
              name: v.name,
              month: v.data.datetime,
              data: v.data.rom,
              data2: v.data.ram,
              data3: v.data.cpu
            });
          });
          this.total = res.data.data.number;
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
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
      this.currentPage = 1;
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList();
      setTimeout(() => {
        this.$refs.line.nextTick();
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
    min-height: 366px;
  }
}
@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>