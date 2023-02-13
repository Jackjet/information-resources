<template>
  <div class="personal-dataExhibition-page">
    <div class="dataExhibition-box">
      <div class="dataExhibition-left">
        <h2>我的目录</h2>
        <div class="catalogue-dataExhibition">
          <div class="catalogue-dataExhibition-item catalogue-dataExhibition1">
            <h3>我申请的</h3>
            <span>90</span>
            <p>挂接：12</p>
          </div>
          <div class="catalogue-dataExhibition-item catalogue-dataExhibition2">
            <h3>我关注的</h3>
            <span>90</span>
            <p>挂接：12</p>
          </div>
          <div class="catalogue-dataExhibition-item catalogue-dataExhibition3">
            <h3>我发布的</h3>
            <span>90</span>
            <p>挂接：12</p>
          </div>
        </div>
      </div>
      <div class="dataExhibition-right">
        <h2>我的需求</h2>
        <p>申请：5个</p>
        <p><i>已解决：5个</i> <span>前往</span></p>
      </div>
    </div>
    <div class="dataExhibition-echarts">
      <h2>我的云数据分析</h2>
      <div class="echarts_box">
        <div id="echarts_1"></div>
      </div>
    </div>
  </div>
</template>
<script>
import echarts from "echarts";
export default {
  mounted() {
    this.echartsFun1();
  },
  methods: {
    echartsFun1() {
      this.charts = echarts.init(document.getElementById("echarts_1"));
      let colors = ["#5793f3", "#d14a61", "#675bba"];
      let option = {
        color: colors,
        tooltip: {
          trigger: "none",
          axisPointer: {
            type: "cross",
          },
        },
        legend: {
          data: ["2015 降水量", "2016 降水量"],
        },
        grid: {
          top: 70,
          bottom: 50,
        },
        xAxis: [
          {
            type: "category",
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              onZero: false,
              lineStyle: {
                color: colors[1],
              },
            },
            axisPointer: {
              label: {
                formatter: function (params) {
                  return (
                    "降水量  " +
                    params.value +
                    (params.seriesData.length
                      ? "：" + params.seriesData[0].data
                      : "")
                  );
                },
              },
            },
            data: [
              "2016-1",
              "2016-2",
              "2016-3",
              "2016-4",
              "2016-5",
              "2016-6",
              "2016-7",
              "2016-8",
              "2016-9",
              "2016-10",
              "2016-11",
              "2016-12",
            ],
          },
          {
            type: "category",
            axisTick: {
              alignWithLabel: true,
            },
            axisLine: {
              onZero: false,
              lineStyle: {
                color: colors[0],
              },
            },
            axisPointer: {
              label: {
                formatter: function (params) {
                  return (
                    "降水量  " +
                    params.value +
                    (params.seriesData.length
                      ? "：" + params.seriesData[0].data
                      : "")
                  );
                },
              },
            },
            data: [
              "2015-1",
              "2015-2",
              "2015-3",
              "2015-4",
              "2015-5",
              "2015-6",
              "2015-7",
              "2015-8",
              "2015-9",
              "2015-10",
              "2015-11",
              "2015-12",
            ],
          },
        ],
        yAxis: [
          {
            type: "value",
          },
        ],
        series: [
          {
            name: "2015 降水量",
            type: "line",
            xAxisIndex: 1,
            smooth: true,
            data: [
              2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0,
              2.3,
            ],
          },
          {
            name: "2016 降水量",
            type: "line",
            smooth: true,
            data: [
              3.9, 5.9, 11.1, 18.7, 48.3, 69.2, 231.6, 46.6, 55.4, 18.4, 10.3,
              0.7,
            ],
          },
        ],
      };
      this.charts.setOption(option);
    },
  },
};
</script>
<style lang="scss" scope>
.personal-dataExhibition-page {
  h2 {
    font-size: 20px;
  }
  .dataExhibition-box {
    display: flex;
    border-bottom: 1px solid #ccc;
    .dataExhibition-left {
      flex: 2;
      padding: 20px;
      box-sizing: border-box;
      border-right: 1px solid #ccc;
      .catalogue-dataExhibition {
        display: flex;
        justify-content: space-around;
        .catalogue-dataExhibition-item {
          width: 200px;
          color: $font-color-white;
          font-size: $font-size-large;
          line-height: 30px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          padding: 10px 0;
          margin: 10px;
          box-sizing: border-box;
          p {
            font-size: $font-size-middle;
          }
        }
        .catalogue-dataExhibition1 {
          background: linear-gradient(135deg, transparent 10px, #58a 0) top left,
            linear-gradient(-135deg, transparent 10px, #58a 0) top right,
            linear-gradient(-45deg, transparent 10px, #58a 0) bottom right,
            linear-gradient(45deg, transparent 10px, #58a 0) bottom left;
          background-size: 50% 50%;
          background-repeat: no-repeat;
        }
        .catalogue-dataExhibition2 {
          background: linear-gradient(
                135deg,
                transparent 10px,
                rgb(212, 186, 34) 0
              )
              top left,
            linear-gradient(-135deg, transparent 10px, rgb(212, 186, 34) 0) top
              right,
            linear-gradient(-45deg, transparent 10px, rgb(212, 186, 34) 0)
              bottom right,
            linear-gradient(45deg, transparent 10px, rgb(212, 186, 34) 0) bottom
              left;
          background-size: 50% 50%;
          background-repeat: no-repeat;
        }
        .catalogue-dataExhibition3 {
          background: linear-gradient(135deg, transparent 10px, orange 0) top
              left,
            linear-gradient(-135deg, transparent 10px, orange 0) top right,
            linear-gradient(-45deg, transparent 10px, orange 0) bottom right,
            linear-gradient(45deg, transparent 10px, orange 0) bottom left;
          background-size: 50% 50%;
          background-repeat: no-repeat;
        }
      }
    }
    .dataExhibition-right {
      flex: 1;
      padding: 20px;
      box-sizing: border-box;
      p {
        margin-top: 20px;
        color: cornflowerblue;
        font-size: $font-size-large;
        span {
          float: right;
          margin-right: 30px;
          background: orange;
          color: #fff;
          font-size: $font-size-middle;
          padding: 2px 15px;
          border-radius: 5px;
        }
      }
    }
  }
  .dataExhibition-echarts {
    padding: 20px;
    box-sizing: border-box;
    .echarts_box {
      overflow-y: scroll;
    }
    #echarts_1 {
      width: 100%;
      height: 300px;
    }
  }
}
</style>