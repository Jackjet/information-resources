<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="15" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="home_sys_depart" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">权责清单</div>
            <count-to :start-val="0" :end-val="userTotal" :duration="2600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <svg-icon icon-class="home_sys_bussniss" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">应用系统</div>
            <count-to :start-val="0" :end-val="goodsTotal" :duration="3000" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <svg-icon icon-class="home_sys_info" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">信息资源</div>
            <count-to :start-val="0" :end-val="productTotal" :duration="3200" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shoppingCard">
            <svg-icon icon-class="home_sys_select" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">信息项</div>
            <count-to :start-val="0" :end-val="orderTotal" :duration="3600" class="card-panel-num" />
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">信息资源可共享率</div>
          </div>
          <div>
            <PieChartComponent :pie-data="pieData" />
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">信息资源审核通过率</div>
          </div>
          <div>
            <GaugeChartComponent :gauge-data="gaugeData" />
          </div>
        </div>
      </el-col>
      <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">最新审核的信息资源</div>
          </div>
          <div>
            <template>
              <el-table class="tableStyle" :data="infoRes" :show-header="false" style="width: 100%">
                <el-table-column prop="flag" label align="left" width="50">
                  <template slot-scope="scope">
                    <div v-if="scope.row.status == 1">
                      <svg-icon icon-class="home_correct" class-name="card-panel-icon" />
                    </div>
                    <div v-else>
                      <svg-icon icon-class="home_error" class-name="card-panel-icon" />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column align="left" show-overflow-tooltip>
                  <template slot-scope="scope">
                    <span class>{{ scope.row.unUviewNm!=null?scope.row.unUviewNm:scope.row.uviewNm }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="checkTime" label align="center" width="150" />
              </el-table>
            </template>
          </div>
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="24" class="card-panel-col">
        <div class="chart-panel">
          <div class="title_block">
            <div class="title">资源目录分布情况</div>
          </div>
          <div>
            <EchartsCoponent :chart-data="chartData" />
          </div>
        </div>
      </el-col>
      <!-- <el-col :xs="8" :sm="8" :lg="8" class="card-panel-col">
        <div class="chart-panel">
          <div class="title_block">
            <div class="title">最新审核的文件</div>
          </div>
          <div>
            <template>
              <el-table
                class="tableStyle"
                :data="infoRes1"
                :show-header="false"
                style="width: 100%"
              >
                <el-table-column prop="flag" label align="left" width="50">
                  <template slot-scope="scope">
                    <div v-if="scope.row.status == 1">
                      <svg-icon icon-class="home_correct" class-name="card-panel-icon" />
                    </div>
                    <div v-else>
                      <svg-icon icon-class="home_error" class-name="card-panel-icon" />
                    </div>
                  </template>
                </el-table-column>
                <el-table-column prop="fileName" align="left" show-overflow-tooltip />
                <el-table-column prop="addTime" label align="center" width="150" />
              </el-table>
            </template>
          </div>
        </div>
      </el-col> -->
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import { indexDept } from '@/api/dashboard'
import EchartsCoponent from './lineEchart'
import PieChartComponent from './piechart'
import GaugeChartComponent from './gaugeChart'
export default {
  components: {
    CountTo,
    EchartsCoponent,
    PieChartComponent,
    GaugeChartComponent
  },
  data() {
    return {
      userTotal: 10,
      goodsTotal: 0,
      productTotal: 0,
      orderTotal: 0,
      infoRes: [],
      infoRes1: [],
      lineDataX1: ['住建局', '水利局', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      lineDataY1: [10, 52, 200, 334, 390, 330, 220],
      lineDataX2: ['住建局', '水利局', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
      lineDataY2: [10, 52, 200, 334, 390, 330, 220],
      chartData: [],
      chartData1: [],
      pieData: [
        { value: 335, name: '直接访问' },
        { value: 20, name: '邮件营销' }
      ],
      gaugeData: [
        {
          value: 0,
          name: ''
        }
      ]
    }
  },
  mounted: function () {
    const vm = this
    vm.$nextTick(() => { })
  },
  created() {
    indexDept().then(response => {
      this.userTotal = response.data.data.busiCount
      this.goodsTotal = response.data.data.appCount
      this.productTotal = response.data.data.infoCataCount
      this.orderTotal = response.data.data.infoItemCount
      this.lineDataX1 = response.data.data.top10SubmitDept[0].name
      this.lineDataY1 = response.data.data.top10SubmitDept[0].total
      this.chartData = [this.lineDataX1, this.lineDataY1]
      this.infoRes = response.data.data.NewReviewInfoList
      this.infoRes1 = response.data.data.NewReviewFileList
      this.pieData = response.data.data.pieData
      var changeValueToInt = response.data.data.gaugeData
      if (changeValueToInt[0].value == 0) {
      } else {
        changeValueToInt[0].value = changeValueToInt[0].value.split('%')[0] * 1
      }
      this.gaugeData = changeValueToInt
    })
  },
  methods: {
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard-editor-container {
  // padding: 32px;
  background-color: rgb(240, 242, 245);
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

.panel-group {
  margin-top: 18px;
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
      height: 84px;
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
