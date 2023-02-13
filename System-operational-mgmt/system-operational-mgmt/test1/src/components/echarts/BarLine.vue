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
    <el-col v-for="(item, index) in this.optionss" :key="index" :xs="wid" :sm="wid" :lg="wid">
      <div :id="'line' + index" :class="className" :style="{height:height,width:width}" />
    </el-col>
  </el-row>
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
    },
    wid: {
      type: String,
      default: 8
    },
    optionss: {
      type: Object,
      default: ''
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.optionss.forEach((item, index) => {
        if(item.data.length > 0) {
          this.initChart(`line${index}`, item)
        } 
      })
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
    initChart(name, item) {
      let echart = echarts.init(document.getElementById(name))
      let option = {
         title: {
            text: item.name,
            x:'center',
            y: item.y,
            textStyle:{
              fontSize: '14'
            }   
        },
        tooltip : {
          trigger: 'axis',
          // formatter(params){   
          // }
        },
        xAxis: {
          type: 'category',
          data: item.month
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: item.data,
          type: 'line',
          showBackground: true,
          color: ['#58afed'],
          backgroundStyle: {
            color: 'rgba(220, 220, 220, 0.8)'
          }
        },
        // {
        //   data: item.data2,
        //   type: 'line',
        //   showBackground: true,
        //   color: ['#fff000'],
        //   backgroundStyle: {
        //     color: 'rgba(220, 220, 220, 0.8)'
        //   }
        // }
        ]
      }
      echart.setOption(option)
    }
  }
}
</script>
