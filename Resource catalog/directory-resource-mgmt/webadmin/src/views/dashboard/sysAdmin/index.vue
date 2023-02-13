<template>
  <div class="dashboard-editor-container">
    <el-row :gutter="15" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <svg-icon icon-class="home_sys_depart" class-name="card-panel-icon" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">部门</div>
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
            <div class="card-panel-text">权责清单</div>
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
      <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">待审核的信息资源</div>
            <button class="home_check_btn" @click="gotoPath(1)">
              <svg-icon icon-class="home_sys_check_icon" class-name="card-panel-icon" />去审核
            </button>
          </div>
          <div class="check_item">
            <template>
              <el-table class="tableStyle" :data="infoRes" :show-header="false" style="width: 100%">
                <el-table-column prop="uviewNm" label align="left" show-overflow-tooltip />
                <el-table-column prop="orgNm" label align="left" width />
                <el-table-column prop="crtDt" label align="center" width="150" />
              </el-table>
            </template>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
        <div class="check-panel">
          <div class="title_block">
            <div class="title">待审核的文件</div>
            <button class="home_check_btn" @click="gotoPath(2)">
              <svg-icon icon-class="home_sys_check_icon" class-name="card-panel-icon" />去审核
            </button>
          </div>
          <div class="check_item">
            <template>
              <el-table class="tableStyle" :data="infoRes1" :show-header="false" style="width: 100%">
                <el-table-column label align="left" class="tableOver" prop="fileName" show-overflow-tooltip />
                <el-table-column prop="orgNm" label align="left" />
                <el-table-column prop="addTime" label align="center" width="150" />
              </el-table>
            </template>
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
        <div class="chart-panel">
          <div class="title_block">
            <div class="title">资源目录分布情况</div>
          </div>
          <div>
            <EchartsCoponent :chart-data="chartData" />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="12" class="card-panel-col">
        <div class="chart-panel">
          <div class="title_block">
            <div class="title">已挂接目录情况</div>
          </div>
          <div>
            <EchartsCoponent :chart-data="chartData1" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import CountTo from 'vue-count-to'
import { indexSystem } from '@/api/dashboard'
import EchartsCoponent from './lineEchart'
export default {
  components: {
    CountTo,
    EchartsCoponent
  },
  data() {
    return {
      userTotal: 10,
      goodsTotal: 0,
      productTotal: 0,
      orderTotal: 0,
      infoRes: [
        {
          date: '2016-05-02',
          name: '1',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-04',
          name: '2',
          address: '上海市普陀区金沙江路 1517 弄'
        },
        {
          date: '2016-05-01',
          name: '3',
          address: '上海市普陀区金沙江路 1519 弄'
        },
        {
          date: '2016-05-03',
          name: '4',
          address: '上海市普陀区金沙江路 1516 弄'
        },
        {
          date: '2016-05-01',
          name: '5',
          address: '上海市普陀区金沙江路 1519 弄'
        }
      ],
      infoRes1: [
        {
          date: '2016-05-02',
          name: '1',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-04',
          name: '2',
          address: '上海市普陀区金沙江路 1517 弄'
        },
        {
          date: '2016-05-01',
          name: '3',
          address: '上海市普陀区金沙江路 1519 弄'
        },
        {
          date: '2016-05-03',
          name: '4',
          address: '上海市普陀区金沙江路 1516 弄'
        },
        {
          date: '2016-05-01',
          name: '5',
          address: '上海市普陀区金沙江路 1519 弄'
        }
      ],
      chartData: [],
      chartData1: [],
      lineDataX1: [],
      lineDataY1: [],
      lineDataX2: [],
      lineDataY2: []
    }
  },
  mounted: function () {
    const vm = this
    vm.$nextTick(() => { })
  },
  created() {
    indexSystem().then(response => {
      this.userTotal = response.data.data.deptCount
      this.goodsTotal = response.data.data.busiCount
      this.productTotal = response.data.data.infoCataCount
      this.orderTotal = response.data.data.infoItemCount
      this.lineDataX1 = response.data.data.top10SubmitDept[0].name
      this.lineDataY1 = response.data.data.top10SubmitDept[0].total
      this.lineDataX2 = response.data.data.top10ShareDept[0].name
      this.lineDataY2 = response.data.data.top10ShareDept[0].total
      this.infoRes = response.data.data.waitReviewInfoList
      this.infoRes1 = response.data.data.waitReviewFileList
      this.chartData = [this.lineDataX1, this.lineDataY1]
      this.chartData1 = [this.lineDataX2, this.lineDataY2]
    })
  },
  methods: {
    gotoPath(num) {
      if (num === 1) {
        this.$router.push({
          path: '/catalogingManagement/catalogingManagementAuditFirst',
          query: { status: '1' }
        })
      } else {
        this.$router.push({
          path: '/catalogingManagement/fileaudit',
          query: { status: '0' }
        })
      }
    },
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
