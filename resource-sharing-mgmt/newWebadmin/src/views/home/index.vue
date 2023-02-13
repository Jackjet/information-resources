<!--
 * 默认主页
-->
<template>
  <main class="main-box">
    <div class="home-box">
      <div class="home-top">
        <div id="echarts_1" ref="echarts_1"></div>
        <div class="resources-num">
          <img src="@/assets/image/home2.png" alt="" />
          <h2>{{ statistics.totaluview }} 个资源</h2>
          <span>已挂接：{{ statistics.hookcount }}</span>
        </div>
        <div class="demand-num">
          <div class="demand-left">
            <img src="@/assets/image/home1.png" alt="" />
            <h2>需求: {{ demand.totals }}</h2>
            <el-button type="primary" @click="routerClick('/demandedInfo')">查 看</el-button>
          </div>
          <div class="demand-right">
            <h3>已受理需求</h3>
            <span>{{ demand.auditcount }} 个</span>
            <el-progress :show-text="false" :percentage="demand.num"></el-progress>
            <span>剩余 {{ demand.notauditcount }} 个</span>
          </div>
        </div>
      </div>
      <div class="home-bottom">
        <div class="bottom-box">
          <div class="tabs-echarts">
            <el-radio-group v-model="radio" @change="timeDateFun">
              <el-radio :label="1">近一月</el-radio>
              <el-radio :label="2">近半年</el-radio>
              <el-radio :label="3">按年</el-radio>
            </el-radio-group>
            <!-- <el-select
              :disabled='radio != 3'
              v-model="value"
              placeholder="请选择"
              style="width: 110px; margin-left: 20px"
              size="mini"
              @change="yearTimeDateFun"
            >
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select> -->
            <el-date-picker size="mini" style="width: 110px; margin-left: 20px" value-format="yyyy" :disabled='radio != 3' v-model="value" type="year" :placeholder="defaultValue" :picker-options="startDateDisabled" @change="yearTimeDateFun">
            </el-date-picker>
          </div>
          <div id="echarts_2" ref="echarts_2"></div>
        </div>
        <div class="bottom-num">
          <h2>• 资源使用管理情况查看</h2>
          <img style="width: 90px" src="@/assets/image/home5.png" alt="" />
          <div class="num-num">
            <p>
              <img src="@/assets/image/2.png" alt="" />待审核
              {{ toExamine.notauditcount }}
            </p>
            <p>
              <img src="@/assets/image/1.png" alt="" />已处理
              {{ toExamine.auditcount }}
            </p>
          </div>
          <el-button type="primary" @click="routerClick('/resourceUseInfo')">查看使用详情</el-button>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import * as echarts from "echarts";
import {
  findArchBusiUviewExByProvOrgId,
  findgetArchBusiUviewExDataAnalysis,
  findDemandInfo,
  findgetResourceUseInfoByTime,
  findgetResourceUseInfo,
} from "@/api/home.js";
export default {
  name: "Dashboard",
  data() {
    return {
      startDateDisabled: {
        disabledDate(time) {
          return time.getFullYear() > new Date().getFullYear()
        }
      },
      statistics: {
        totaluview: "",
        hookcount: "",
      },
      demand: {
        auditcount: 0,
        notauditcount: 0,
        totals: 0,
        num: 0,
      },
      toExamine: {
        auditcount: 0,
        notauditcount: 0,
      },
      radio: 1,
      value: "",
      defaultValue: new Date().getFullYear(),
      options: [
        {
          value: "2021",
          label: "2021",
        },
        {
          value: "2020",
          label: "2020",
        },
      ],
    };
  },
  mounted() {
    // 饼图
    findArchBusiUviewExByProvOrgId().then((res) => {
      if (res.data.code === 1) {
        this.cakeEcharts(res.data.data);
      } else {
        this.$message.error(res.data.msg);
      }
    });
    //资源统计
    findgetArchBusiUviewExDataAnalysis().then((res) => {
      this.statistics = res.data.data;
    });
    //需求统计
    findDemandInfo().then((res) => {
      this.demand = res.data.data;
      this.demand.num = this.Percentage(
        this.demand.auditcount,
        this.demand.totals
      );
    });
    //柱状图
    this.columnEchartsApi(this.radio);
    // this.columnEchartsApi("2021");
    //资源使用管理情况查看
    findgetResourceUseInfo().then((res) => {
      this.toExamine = res.data.data;
    });
  },
  methods: {
    // 计算%
    Percentage(num, total) {
      if (num == 0 || total == 0) {
        return 0;
      }
      return Math.round((num / total) * 10000) / 100.0; // 小数点后两位百分比
    },
    timeDateFun() {
      if (this.radio !== 3) {
        this.columnEchartsApi(this.radio);
      } else {
        this.columnEchartsApi(this.defaultValue);
      }
    },
    yearTimeDateFun(year) {
      if (this.radio === 3) {
        this.columnEchartsApi(year);
      }
    },
    columnEchartsApi(num) {
      findgetResourceUseInfoByTime({ typ: num }).then((res) => {
        this.columnEcharts(res.data.data);
      });
    },
    cakeEcharts(datas) {
      let dataList = [];
      datas.forEach((item) => {
        dataList.push({ name: item.name, value: item.hookcount });
      });
      var myChart = echarts.init(this.$refs.echarts_1);
      var option = {
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c} - {d}%",
          confine: true,
        },
        title: {
          text: "• 各部门资源挂接情况分析",
          left: "20",
          top: "20",
          textStyle: {
            color: "#000",
            fontWeight: "normal",
            fontSize: 18,
          },
        },
        series: [
          {
            type: "pie",
            radius: ["20%", "40%"],
            center: ["50%", "50%"],
            data: dataList,
            label: {
              formatter: "{b}: {d}%",
              // normal: {
              //   show: true,
              //   position: "center",
              //   formatter: "各部门资源挂接情况分析",
              // },
              // emphasis: {
              //   //中间文字显示
              //   show: true,
              // },
            },
          },
        ],
      };
      myChart.setOption(option);
    },
    columnEcharts(dataList) {
      let datas = {
        date: [],
        notauditcount: [],
        auditcount: [],
      };
      dataList.forEach((item) => {
        if (item.date) {
          datas.date.push(item.date);
        } else {
          datas.date.push(item.month + "月");
        }
        datas.notauditcount.push(item.notauditcount);
        datas.auditcount.push(item.auditcount);
      });
      var myChart = echarts.init(this.$refs.echarts_2);
      var option = {
        title: {
          text: "• 资源使用申请审核情况分析",
          left: "20",
          top: "20",
          textStyle: {
            color: "#000",
            fontWeight: "normal",
            fontSize: 18,
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: datas.date,
            axisTick: {
              show: false,
            },
            splitLine: {
              show: false,
            },
            axisLine: {
              show: false,
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            show: false,
          },
        ],
        series: [
          {
            name: "已审核",
            type: "bar",
            stack: "one",
            barWidth: "10%",
            itemStyle: {
              normal: {
                show: true,
                color: "#2f79b8",
                barBorderRadius: 50,
                borderWidth: 0,
                borderColor: "#333",
              },
            },
            data: datas.auditcount,
          },
          {
            name: "未审核",
            type: "bar",
            stack: "one",
            barWidth: "20%",
            itemStyle: {
              normal: {
                show: true,
                color: "red",
                barBorderRadius: 50,
                borderWidth: 0,
                borderColor: "#333",
              },
            },
            data: datas.notauditcount,
          },
        ],
      };
      myChart.setOption(option);
    },
    routerClick(path) {
      this.$router.push(path);
    },
  },
};
</script>
<style lang="scss" scoped>
.main-box {
  width: 100%;
  height: 100%;
  background: #f4f4f5;
  .home-box {
    // padding: 20px;
    // box-sizing: border-box;
    .home-top {
      display: flex;
      justify-content: space-between;
      #echarts_1,
      .resources-num,
      .demand-num {
        // width: 400px;
        height: 260px;
        // -webkit-box-shadow: #666 0px 0px 5px;
        background: #fff;
        border-radius: 5px;
      }
      #echarts_1 {
        flex: 1;
        margin-right: 30px;
      }
      .resources-num {
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        margin-right: 30px;
        img {
          display: block;
          width: 80px;
          height: 80px;
        }
        span {
          font-weight: bold;
        }
      }
      .demand-num {
        width: 400px;
        display: flex;
        .demand-left {
          flex: 1;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          img {
            width: 60px;
          }
        }
        .demand-right {
          flex: 2;
          padding-top: 40px;
          padding-left: 40px;
          padding-right: 10px;
          box-sizing: border-box;
          h3 {
            font-size: 18px;
            font-weight: normal;
          }
          span {
            font-weight: bold;
            display: block;
            line-height: 40px;
          }
        }
      }
    }
    .home-bottom {
      margin-top: 20px;
      display: flex;
      .bottom-box {
        flex: 1;
        // -webkit-box-shadow: #666 0px 0px 5px;
        border-radius: 5px;
        background: #fff;
        position: relative;
        #echarts_2 {
          width: 100%;
          height: 100%;
        }
        .tabs-echarts {
          position: absolute;
          top: 20px;
          right: 20px;
          z-index: 1000;
        }
      }
      .bottom-num {
        width: 400px;
        height: 400px;
        margin-left: 30px;
        // -webkit-box-shadow: #666 0px 0px 5px;
        border-radius: 5px;
        background: #fff;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        align-items: center;
        h2 {
          font-size: 18px;
          font-weight: normal;
          color: #000;
        }
        .num-num {
          display: flex;
          justify-content: space-around;
          p {
            display: flex;
            align-items: flex-end;
            margin: 0 30px;
            font-weight: bold;
            img {
              width: 24px;
              margin-right: 10px;
            }
          }
        }
      }
    }
  }
}
</style>
