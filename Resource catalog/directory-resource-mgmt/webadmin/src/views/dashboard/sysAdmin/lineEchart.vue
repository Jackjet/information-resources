<template>
  <div>
    <div ref="echarts" style="width:100%;height:350px;" />
  </div>
</template>

<script>
import echarts from 'echarts'
export default {
  name: 'EchartsComponents',
  props: {
    chartData: {
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
    chartData: {
      handler(newVal, oldVal) {
        console.log(newVal, oldVal)
        if (newVal) {
          this.drawChart()
        } else {
          this.drawChart()
        }
      },
      deep: true // 对象内部属性的监听，关键。
    }
  },
  mounted: function() {
    const vm = this
    vm.$nextTick(() => {
      vm.drawChart()
    })
  },
  updated() {
    this.drawChart()
  },
  created: () => {},
  methods: {
    drawChart() {
      const vm = this
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(this.$refs.echarts)
      // 绘制图表
      myChart.setOption({
        color: ['#3398DB'],
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: this.chartData[0],
            axisTick: {
              alignWithLabel: true
            },
            axisLabel: {
              interval: 0,
              rotate: 40, // 倾斜度 -90 至 90 默认为0
              formatter: function(value) {
                var ret = '' // 拼接加\n返回的类目项
                var maxLength = 10 // 每项显示文字个数
                var valLength = value.length // X轴类目项的文字个数
                var rowN = Math.ceil(valLength / maxLength) // 类目项需要换行的行数
                if (rowN > 1) {
                  // 如果类目项的文字大于3,
                  for (var i = 0; i < rowN; i++) {
                    var temp = '' // 每次截取的字符串
                    var start = i * maxLength // 开始截取的位置
                    var end = start + maxLength // 结束截取的位置
                    // 这里也可以加一个是否是最后一行的判断，但是不加也没有影响，那就不加吧
                    temp = value.substring(start, end) + '\n'
                    ret += temp // 凭借最终的字符串
                  }
                  return ret
                } else {
                  return value
                }
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            minInterval: 1
          }
        ],
        series: [
          {
            name: '',
            type: 'bar',
            barWidth: 15,
            itemStyle: {
              color: 'rgba(31,51,101,1)',
              borderWidth: 20,
              barBorderRadius: [50, 50, 0, 0]
            },

            data: this.chartData[1]
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
</style>
