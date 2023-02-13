<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'

const animationDuration = 3000
export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el)

      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        radar: {
          radius: '66%',
          center: ['50%', '42%'],
          splitNumber: 8,
          splitArea: {
            areaStyle: {
              color: '#6dd8da',
              opacity: 1,
              shadowBlur: 45,
              shadowColor: 'rgba(0,0,0,.5)',
              shadowOffsetX: 0,
              shadowOffsetY: 15
            }
          },
          indicator: [
            { name: '第一', max: 10000 },
            { name: '第二', max: 20000 },
            { name: '第三', max: 20000 },
            { name: '第四', max: 20000 },
            { name: '第五', max: 20000 },
            { name: '第六', max: 20000 }
          ]
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['一', '二', '三']
        },
        series: [{
          type: 'radar',
          symbolSize: 0,
          areaStyle: {
            normal: {
              shadowBlur: 13,
              shadowColor: 'rgba(0,0,0,.2)',
              shadowOffsetX: 0,
              shadowOffsetY: 10,
              opacity: 1
            }
          },
          data: [
            {
              value: [5000, 7000, 12000, 11000, 15000, 14000],
              name: '一',
              itemStyle: {
                  normal: {

                      areaStyle: {

                          type: 'default',

                          /*opacity: 1,*/

                          color: "#6dd8da" // 图表中各个图区域的颜色

                      }

                  }

              },
            },
            {
              value: [4000, 9000, 15000, 15000, 13000, 11000],
              name: '二'
            },
            {
              value: [5500, 11000, 12000, 15000, 12000, 12000],
              name: '三'
            }
          ],
          animationDuration: animationDuration
        }]
      })
    }
  }
}
</script>