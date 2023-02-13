<template >
  <el-row>
    <el-col
      v-for="(item, index) in this.options"
      :key="index"
      :xs="8"
      :sm="8"
      :lg="8"
    >
      <div
        :id="'item' + index"
        :class="className"
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
      default: "8"
    },
    options: {
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
    this.$nextTick(() => {
      console.log(this.options);
      this.options.forEach((item, index) => {
        console.log(item.data);
        // if(item.data.length > 0) {
        this.initChart(`item${index}`, item);
        // }
      });
    });
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    initChart(name, item) {
      let echart = echarts.init(document.getElementById(name));
      let option = {
        title: {
          text: item.name,
          x: "center",
          y: "bottom",
          textStyle: {
            fontSize: "14"
          }
        },
        xAxis: {
          type: "category",
          data: item.month
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            data: item.data,
            type: "bar",
            showBackground: true,
            barWidth: "15px",
            backgroundStyle: {
              color: "rgba(220, 220, 220, 0)"
            },
            itemStyle: {
              normal: {
                //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                color: function(params) {
                  var colorList = [
                    "#6dd8da",
                    "#f67287",
                    "#f29e3c",
                    "#c05bdd",
                    "#7a65f2"
                  ]; //每根柱子的颜色
                  return colorList[0];
                }
              }
            }
          },
          {
            data: item.data2,
            type: "bar",
            showBackground: true,
            barWidth: "15px",
            backgroundStyle: {
              color: "rgba(220, 220, 220, 0)"
            },
            itemStyle: {
              normal: {
                //每个柱子的颜色即为colorList数组里的每一项，如果柱子数目多于colorList的长度，则柱子颜色循环使用该数组
                color: function(params) {
                  var colorList = [
                    "#6dd8da",
                    "#f67287",
                    "#f29e3c",
                    "#c05bdd",
                    "#7a65f2"
                  ]; //每根柱子的颜色
                  return colorList[4];
                }
              }
            }
          }
        ]
      };
      echart.setOption(option);
    }
  }
};
</script>
