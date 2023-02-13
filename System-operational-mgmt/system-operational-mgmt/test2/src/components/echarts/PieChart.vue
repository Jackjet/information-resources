<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'

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
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['一', '二', '三', '四', '五']
        },
        color: ['#6dd8da', '#b6a2de', '#58afed'], 
        series: [
          {
            name: '饼状图',
            type: 'pie',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: 320, name: '一' },
              { value: 240, name: '二' },
              { value: 149, name: '三' },
              { value: 100, name: '四' },
              { value: 59, name: '五' }
            ],
          }
        ]
      })
    }
  }
}
</script>