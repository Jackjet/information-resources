<template>
  <div class="echart-container ">
    <el-row>
      <el-col :span="24">
        <el-form :inline="true" class="el-InputForm">
          <el-form-item>
            <el-input
              clearable
              size="medium"
              placeholder="请输入用户名称"
              prefix-icon="el-icon-search"
              v-model="SearchItem.name"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-select
              v-model="SearchItem.unit"
              placeholder="请选择统计单位"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="valueTime"
              :picker-options="pickerOptions"
              size="medium"
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
    <el-row :gutter="32" class="row-style">
      <el-col :xs="24" :sm="24" :lg="24">
        <div class="chart-wrapper">
          <bar-line v-if="lock" :height="height" :wid="24" :optionss="Data" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getActivity } from "@/api/behavior/loginInfo";
import { getYear, fun_date } from "@/utils/date";
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
        unit: "day",
        name: ""
      },
      lastItem: {
        beginTime: "",
        endTime: "",
        unit: "day",
        name: ""
      },
      height: "450px",
      lock: "true",
      options: [
        { value: "day", label: "日" },
        { value: "week", label: "周" },
        { value: "month", label: "月" }
      ],
      total: 20,
      pageSize: "6",
      currentPage: "1"
    };
  },
  created() {
    this.valueTime = [fun_date(), getYear()];
    this.getList();
  },
  methods: {
    async getList() {
      const that = this;
      if (that.valueTime) {
        that.SearchItem.startTime = that.valueTime[0];
        that.SearchItem.endTime = that.valueTime[1];
      } else {
        that.SearchItem.startTime = "";
        that.SearchItem.endTime = "";
      }
      if (this.SearchItem.startTime === '') {
        this.$message.error('请选择日期');
        return false
      }
      const data = {
        page: this.currentPage,
        size: this.pageSize,
        startTime: this.SearchItem.startTime,
        endTime: this.SearchItem.endTime,
        unit: this.SearchItem.unit,
        name: this.SearchItem.name
      };
      try {
        that.lock = false;
        const res = await getActivity(data);
        if (res.data.code === 1) {
          that.Data = [];
          let month = [];
          let num = [];
          res.data.data.map(function(v, k) {
            month.push(v.date);
            num.push(v.number);
          });
          that.Data.push({
            y: "bottom",
            legend: ['次数'],
            unit: "",
            name: "用户活跃度趋势图",
            month: month,
            data: num,
            data2: []
          });
          that.lock = true;
          this.total = res.data.data.number;
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    add: function() {
      this.$refs.add.initial("system");
    },
    SearchNoteList() {
      this.currentPage = 1;
      Object.entries(this.SearchItem).map(item => {
        this.lastItem[item[0]] = this.SearchItem[item[0]];
      });
      this.getList();
    },
    handleCurrentChange(item) {
      this.pageSize = "6";
      this.currentPage = item;
      Object.entries(this.SearchItem).map(item => {
        this.SearchItem[item[0]] = this.lastItem[item[0]];
      });
      this.fetchData();
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
    margin-top: 30px;
    margin-bottom: 32px;
  }
}
@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>