<template>
  <div>
    <div ref="echarts" style="width:100%;height:250px;" />
  </div>
</template>

<script>
import echarts from 'echarts'

export default {
  name: 'PieChartComponent',
  props: {
    pieData: {
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
    pieData: {
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
  mounted: function() {
    const vm = this
    vm.$nextTick(() => {
      vm.drawChart()
    })
  },
  created: () => {},
  methods: {
    drawChart() {
      const vm = this
      // 基于准备好的dom，初始化echarts实例
      var myChart = echarts.init(this.$refs.echarts)
      // 绘制图表
      myChart.setOption({
        color: ['#1F3365', '#E65555'],
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [
          {
            name: '共享',
            type: 'pie',
            radius: ['64%', '72%'],
            itemStyle: {},
            data: this.pieData
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
</style>
