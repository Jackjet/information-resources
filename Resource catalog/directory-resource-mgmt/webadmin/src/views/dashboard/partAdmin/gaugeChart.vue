<template>
  <div>
    <div ref="echarts" style="width:100%;height:250px;" />
  </div>
</template>

<script>
import echarts from 'echarts'
export default {
  name: 'GaugeChartComponent',
  props: {
    gaugeData: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      count: 1
    }
  },
  computed: {},
  watch: {
    // 观察option的变化
    gaugeData: {
      handler(newVal, oldVal) {
        if (newVal) {
          this.drawChart()
        } else {
          this.drawChart()
        }
      },
      deep: true // 对象内部属性的监听，关键。
    }
  },
  mounted: function () {
    const vm = this
    vm.$nextTick(() => {
      vm.drawChart()
    })
  },
  created: () => { },
  methods: {
    drawChart() {
      const vm = this
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(this.$refs.echarts)
      // 绘制图表
      myChart.setOption({
        backgroundColor: '#ffffff',
        color: ['#37A2DA', '#32C5E9', '#67E0E3'],
        series: [
          {
            name: '',
            type: 'gauge',
            detail: {
              formatter: '{value}%'
            },
            axisLabel: {
              show: false
            },
            // splitNumber:'4',
            axisTick: {
              show: false
            },
            pointer: {
              length: '60%',
              width: 8
            },
            splitLine: {
              // 控制分隔线样式(即表盘上长的刻度线)
              length: 0, // 属性length控制线长
              lineStyle: {
                width: 0,
                color: '#fff',
                shadowColor: '#fff', // 默认透明
                shadowBlur: 10
              }
            },
            axisLine: {
              show: true,
              lineStyle: {
                width: 10,
                shadowBlur: 0,
                color: [
                  [0.3, '#E65555'],
                  [0.7, '#1F3365'],
                  [1, '#5ABD8C']
                ]
              }
            },
            data: this.gaugeData
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
</style>
