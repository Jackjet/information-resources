<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="15" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <svg xmlns="http://www.w3.org/2000/svg" width="33.231" height="38.632" viewBox="0 0 33.231 38.632">
              <path id="部门_1_" data-name="部门 (1)" fill="#2e4ea1" d="M134.344,44.687H124.156V37.345a.867.867,0,0,0-.865-.865H102.838a.867.867,0,0,0-.865.865v36.9a.867.867,0,0,0,.865.865h21.3v0h10.2a.867.867,0,0,0,.865-.865V45.552a.861.861,0,0,0-.861-.865ZM118.6,63.545a.52.52,0,0,1-.519.519H108.044a.52.52,0,0,1-.519-.519V59.039a.52.52,0,0,1,.519-.519h10.032a.52.52,0,0,1,.519.519v4.506Zm0-8.311a.52.52,0,0,1-.519.519H108.044a.52.52,0,0,1-.519-.519V50.728a.52.52,0,0,1,.519-.519h10.032a.52.52,0,0,1,.519.519v4.506Zm0-8.3a.52.52,0,0,1-.519.519H108.044a.52.52,0,0,1-.519-.519V42.43a.52.52,0,0,1,.519-.519h10.032a.52.52,0,0,1,.519.519v4.506Zm13.837,16.6a.52.52,0,0,1-.519.519h-4.488a.52.52,0,0,1-.519-.519V59.047a.52.52,0,0,1,.519-.519h4.488a.52.52,0,0,1,.519.519v4.488Zm0-8.32a.52.52,0,0,1-.519.519h-4.488a.52.52,0,0,1-.519-.519V50.728a.52.52,0,0,1,.519-.519h4.488a.52.52,0,0,1,.519.519Z" transform="translate(-101.973 -36.48)" />
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">提出部门</div>
            <count-to :start-val="0" :end-val="userTotal" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <svg xmlns="http://www.w3.org/2000/svg" width="40.053" height="36.842" viewBox="0 0 40.053 36.842">
              <path id="业务_2_" data-name="业务 (2)" fill="#2e4ea1" d="M103.417,108.132H95.49V106.99A2.491,2.491,0,0,0,93,104.5H79.25a2.491,2.491,0,0,0-2.49,2.49v1.142H68.832A2.73,2.73,0,0,0,66.1,110.86v3.834a78.787,78.787,0,0,0,20.024,2.266,78.814,78.814,0,0,0,20.024-2.266V110.86A2.737,2.737,0,0,0,103.417,108.132ZM66.1,117.524v21.09a2.727,2.727,0,0,0,2.728,2.728h34.589a2.73,2.73,0,0,0,2.728-2.728v-21.09a84.658,84.658,0,0,1-20.024,2.168A84.852,84.852,0,0,1,66.1,117.524Zm26.375,8.957a1.82,1.82,0,0,1-1.818,1.818H81.6a1.818,1.818,0,0,1,0-3.637h9.056A1.821,1.821,0,0,1,92.475,126.482Z" transform="translate(-66.1 -104.5)" />
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">需求总计</div>
            <count-to :start-val="0" :end-val="goodsTotal" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <svg xmlns="http://www.w3.org/2000/svg" width="50.344" height="50.344" viewBox="0 0 50.344 50.344">
              <path id="信息" fill="#2e4ea1" d="M25.2,0A25.172,25.172,0,1,0,50.377,25.172,25.172,25.172,0,0,0,25.2,0ZM29.15,34.432c-1.9,2.853-3.831,5.049-7.082,5.049-2.218-.36-3.131-1.95-2.651-3.569L23.6,22.059a.658.658,0,0,0-.379-.811c-.309-.107-.915.293-1.436.865l-2.528,3.042a13.914,13.914,0,0,1-.009-1.695c1.9-2.853,5.021-5.1,7.138-5.1A2.843,2.843,0,0,1,29,21.939l-4.21,13.92a.655.655,0,0,0,.394.735c.309.107.959-.293,1.483-.868L29.2,32.687a13.722,13.722,0,0,1-.044,1.745ZM28.591,16.34a2.814,2.814,0,0,1-2.9-2.881,2.9,2.9,0,0,1,5.794,0A2.812,2.812,0,0,1,28.591,16.34Z" transform="translate(-0.033)" />
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">已处理</div>
            <count-to :start-val="0" :end-val="productTotal" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shoppingCard">
            <svg xmlns="http://www.w3.org/2000/svg" width="41" height="41" viewBox="0 0 41 41">
              <path id="选项" fill="#2e4ea1" d="M0,29.286H11.714V41H0Zm29.286,0H41V41H29.286Zm-14.643,0H26.357V41H14.643ZM0,14.643H11.714V26.357H0Zm29.286,0H41V26.357H29.286ZM14.643,0H26.357V11.714H14.643Zm0,14.643H26.357V26.357H14.643Z" />
            </svg>
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">已驳回</div>
            <count-to :start-val="0" :end-val="orderTotal" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">需求缘由</div>
          </div>
          <div>
            <div id="echarts_1" ref="echarts_1" style="height:270px;"></div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">需求类型</div>
          </div>
          <div>
            <div id="echarts_2" ref="echarts_2" style="height:270px;"></div>
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">部门需求审核通过率</div>
          </div>
          <div>
            <div id="echarts_3" ref="echarts_3" style="height:270px;"></div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to';
import {
  getDemandCountAnalysis,
  getDemandByReason,
  getDemandByRequestType,
  getDemandByPassRate
} from "@/api/summary.js";
import * as echarts from "echarts";
export default {
  components: {
    CountTo,
  },
  data() {
    return {
      userTotal: 0,
      goodsTotal: 0,
      productTotal: 0,
      orderTotal: 0,
    }
  },
  created() {
    getDemandCountAnalysis().then(res => {
      if (res.data.code === 1) {
        this.userTotal = res.data.data.org
        this.goodsTotal = res.data.data.num
        this.productTotal = res.data.data.processed
        this.orderTotal = res.data.data.reject
      }
    })
    getDemandByReason().then(res => {
      if (res.data.code === 1) {
        this.echartsFun1(res.data.data);
      }
    })
    getDemandByRequestType().then(res => {
      if (res.data.code === 1) {
        this.echartsFun2(res.data.data);
      }
    })
    getDemandByPassRate().then(res => {
      console.log(res, "===============")
      if (res.data.code === 1) {
        this.echartsFun3(res.data.data.slice(0, -1));
      }
    })
  },
  methods: {
    echartsFun1(datas) {
      let dataList = [{
        name: "政策相关",
        value: datas.related
      }, {
        name: "非政策相关",
        value: datas.unrelated
      }];
      var myChart = echarts.init(this.$refs.echarts_1);
      var option = {
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c} - {d}%",
          confine: true,
        },
        series: [
          {
            type: "pie",
            radius: ["40%", "30%"],
            center: ["50%", "50%"],
            data: dataList,
            label: {
              formatter: "{b}: {d}%",
            },
          },
        ],
      };
      myChart.setOption(option);
    },
    echartsFun2(datas) {
      let myChart = echarts.init(this.$refs.echarts_2);
      let option = {
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c}",
          confine: true,
        },
        xAxis: {
          type: 'category',
          axisLabel: {
            rotate: 45,
          },
          data: ['目录资源变更', '目录资源新增', '云数据变更', '云数据新增']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [datas.archUpdate, datas.archAdd, datas.dataUpdate, datas.dataAdd],
            type: 'line'
          }
        ]
      };
      myChart.setOption(option);
    },
    echartsFun3(datas) {
      let myChart = echarts.init(this.$refs.echarts_3)
      // 绘制图表
      let option = {
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
            title: {
              offsetCenter: [0, '-50%'],
              fontSize: 16
            },
            data: [{ value: datas, name: "审核通过率" }]
          }
        ]
      };
      myChart.setOption(option);
    },
    handleSetLineChartData(type) {
      //   this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  // padding: 32px;
  // background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

.panel-group {
  // margin-top: 18px;
  .card-panel-col {
    margin-bottom: 15px;
  }
  .card-panel {
    height: 108px;
    /*cursor: pointer;*/
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    border-color: rgba(90, 57, 57, 0.05);
    background: rgba(255, 255, 255, 1);
    box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.06);
    opacity: 1;
    border-radius: 6px;
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shoppingCard {
      color: #34bfa3;
    }
    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
      // height: 84px;
      border: 2px solid rgb(247, 245, 245);
      border-radius: 50%;
    }
    .card-panel-icon {
      float: left;
      font-size: 48px;
    }
    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-right: 65px;
      margin-left: 0px;
      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }
      .card-panel-num {
        font-size: 20px;
      }
    }
  }
  .check-panel {
    height: 305px;
    padding: 20px;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    border-color: rgba(90, 57, 57, 0.05);
    background: rgba(255, 255, 255, 1);
    box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.06);
    opacity: 1;
    border-radius: 6px;
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shoppingCard {
      color: #34bfa3;
    }
    .title_block {
      height: 24px;
      .title {
        font-size: 20px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: rgba(36, 36, 36, 1);
        opacity: 1;
        float: left;
      }
    }
    .home_check_btn {
      width: 68px;
      height: 24px;
      float: right;
      background: rgba(255, 255, 255, 1);
      border: 1px solid rgba(46, 78, 161, 1);
      opacity: 1;
      border-radius: 17px;
      font-size: 12px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: rgba(46, 78, 161, 1);
      opacity: 1;
      outline: none;
      cursor: pointer;
    }
  }
  .chart-panel {
    height: 405px;
    padding: 20px;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    border-color: rgba(90, 57, 57, 0.05);
    background: rgba(255, 255, 255, 1);
    box-shadow: 0px 0px 16px rgba(0, 0, 0, 0.06);
    opacity: 1;
    border-radius: 6px;
    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }
    }
    .icon-people {
      color: #40c9c6;
    }
    .icon-message {
      color: #36a3f7;
    }
    .icon-money {
      color: #f4516c;
    }
    .icon-shoppingCard {
      color: #34bfa3;
    }
    .title_block {
      height: 24px;
      margin-bottom: 10px;
      .title {
        font-size: 20px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: rgba(36, 36, 36, 1);
        opacity: 1;
        float: left;
      }
    }
  }
}
</style>
<style>
.tableStyle table {
  font-size: 12px;
}
.tableStyle table .cell {
  line-height: 20px;
  padding-left: unset;
  padding-right: unset;
}
</style>
