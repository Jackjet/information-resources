<!--
 * @Author: your name
 * @Date: 2020-06-29 17:26:29
 * @LastEditTime: 2020-07-01 14:17:55
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \webadmin\src\components\echarts\BarLine.vue
--> 
<template >
  <el-row>
    <el-col
      v-for="(item, index) in this.optionss"
      :key="index"
      :xs="wid"
      :sm="wid"
      :lg="wid"
    >
      <div
        :id="'line' + index"
        :class="className"
        style="margin-bottom:10px"
        :style="{ height: height, width: width }"
      />
    </el-col>
  </el-row>
</template>

<script>
import echarts from "echarts";
export default {
  props: {
    className: {
      type: String,
      default: "chart"
    },
    width: {
      type: String,
      default: "100%"
    },
    height: {
      type: String,
      default: "350px"
    },
    wid: {
      type: String,
      default: 8
    },
    optionss: {
      type: Object,
      default: ""
    }
  },
  data() {
    return {
      chart: null
    };
  },
  mounted() {
    this.nextTick();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    nextTick() {
      this.$nextTick(() => {
        this.optionss.forEach((item, index) => {
          this.initChart(`line${index}`, item);
        });
      });
    },
    initChart(name, item) {
      let echart = echarts.init(document.getElementById(name));
      let option = {
        title: {
          text: item.name,
          x: "center",
          y: item.y,
          textStyle: {
            fontSize: "14"
          }
        },
        legend: {
          data: item.legend
        },
        tooltip: {
          trigger: "axis"
        },
        grid: {
          top: "20%",
          left: "10%",
          right: "10%",
          bottom: "8%",
          containLabel: true
        },
        dataZoom: [
          {
            type: "slider",
            show: true,
            xAxisIndex: [0],
            left: "9%",
            bottom: -5,
            start: 30,
            height: 20, //????????????
            end: 60 //??????????????????
          }
        ],
        xAxis: {
          type: "category",
          data: item.month,
          axisLabel: {
            show: true,
            interval: 0,
            rotate: 45,
            textStyle: {
              fontSize: 10 //???????????????????????????
            }
          }
        },
        yAxis: {
          name: item.unit,
          type: "value",
          minInterval: 1
        },
        series: [
          {
            name: item.legend.length > 0 ? item.legend[0] : "",
            data: item.data,
            type: "line",
            showBackground: true,
            color: ["#c05bdd"],
            backgroundStyle: {
              color: "rgba(220, 220, 220, 0.8)"
            }
          }
        ]
      };
      if (item.data2) {
        option.series.push({
          name: item.legend[1],
          data: item.data2,
          type: "line",
          showBackground: true,
          color: ["#7a65f2"],
          backgroundStyle: {
            color: "rgba(220, 220, 220, 0.8)"
          }
        });
      }
      if (item.data3) {
        option.series.push({
          name: item.legend[2],
          data: item.data3,
          type: "line",
          showBackground: true,
          color: ["#58afed"],
          backgroundStyle: {
            color: "rgba(220, 220, 220, 0.8)"
          }
        });
      }
      echart.setOption(option);
    }
  }
};
</script>
