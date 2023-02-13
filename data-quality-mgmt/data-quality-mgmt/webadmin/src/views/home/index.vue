<!--
 * 默认主页
-->
<template>
  <main class="main-box">
    <div class="main-img">
      <div class="topDiv">
        <div class="chars">
          <div class="tit">大数据信息中心关键数据质量指标</div>
          <div class="char">
            <div v-for="(v,i) in charsData1" :key="v.template_id">
              <div :id="'echar'+v.template_id" class="echar"></div>
              <div class="ms">
                <div class="name">{{v.name}}</div>
                <div class="time">检查时间：{{time}}</div>
                <div class="num">检查数据量：{{v.total_data_size}} 条</div>
                <div class="num">问题数据量：{{v.total_error_data_size}} 条</div>
              </div>
            </div>
          </div>
        </div>
        <div class="more" v-show="show">
          <span @click="more()">更多>></span>
        </div>
        <div class="more" v-show="!show">
          <span @click="back()">
            <<收回 </span>
        </div>
      </div>
      <div class="botDiv">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="待办工单" name="first"></el-tab-pane>
          <el-tab-pane label="最新提交工单" name="second"></el-tab-pane>
        </el-tabs>
        <div style="width:100%">
          <!-- <div class="datas">
            <div v-for="(v,i) in workData" :key="i">
              <div>
                <span class='ellipsis' :title='v.title'>{{v.title}}</span>
                <span></span>
                <span>{{v.status == 0 ? '处理中' : v.status == 1 ? '已通过' : '已驳回'}}</span>
              </div>
              <div>
                <span>提交人：{{v.createByName}}</span>
                <span>提交日期：{{v.createTime}}</span>
                <span></span>
              </div>
            </div>
          </div> -->
          <TableList :table-data='workData' v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOption">
          </TableList>
          <div style="color: #1d4277;text-decoration: underline;text-align: right;line-height: 50px;padding-bottom: 20px;padding-right: 50px;"><span style="cursor: pointer;" @click="goWork()">更多>></span></div>
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import TableList from '@/components/table/tableList'
import * as echarts from "echarts";
import {
  workOrderFindAll,
  workOrderFindAllByMy,
  statisticsFind
} from "@/api/workOrder.js";

export default {
  name: "Dashboard",
  components: { TableList },
  data() {
    return {
      charsData: [
        {
          id: "1",
          name: "非空约束",
          total_error_data_size: "1",
          total_data_size: "4",
        },
        {
          id: "2",
          name: "非空约束",
          total_error_data_size: "1",
          total_data_size: "1",
        }
      ],
      time: '',
      charsData1: [],
      activeName: "first",
      workData: [],
      show: true,
      isSubmitLoading: false,
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      tableHeader: [
        { label: '工单标题', list: 'title' },
        { label: '提交人', list: 'createByName' },
        { label: '提交日期', list: 'createTime' },
        {
          type: 'html',
          label: '状态',
          list: 'status',
          code: (row) => {
            if (row.status == '0') {
              return '<span>处理中</span>'
            } else if (row.status == '1') {
              return '<span>已通过</span>'
            } else {
              return '<span>已驳回</span>'
            }
          }
        },
      ],
      tableOption: {
        label: '操作',
        width: '180px',
        value: 1,
        options: [{
          label: '编辑',
          key: 0,
          type: 'text',
          State: true,
          method: (row) => {
            // this.handleEdit(row)
          }
        },]
      }
    };
  },
  mounted() {

    this.getStatistics();
    this.work();
  },
  methods: {
    chars() {
      let pointerData = 0; // 仪表指针数据
      this.charsData1.forEach((v, i) => {
        setTimeout(function () {
          var myChart = echarts.init(document.getElementById("echar" + v.template_id));
          if (v.total_data_size == 0) {
            pointerData = 0
          } else {
            pointerData = (v.total_data_size - v.total_error_data_size) * 100 / v.total_data_size
            pointerData = pointerData.toFixed(0)
          }
          var option = {
            backgroundColor: "#fff",
            series: [
              {
                type: "gauge",
                radius: 60,
                z: 1,
                startAngle: 225,
                endAngle: -45,
                splitNumber: 50,
                title: {
                  color: "red"
                },
                splitLine: {
                  show: true,
                  length: 15,
                  distance: -10,
                  lineStyle: {
                    color: "#fff",
                    width: 1
                  }
                },
                detail: {
                  show: true,
                  offsetCenter: [0, 45],
                  fontSize: 14,
                  formatter: val => [`{a|${val}}`, `{b|%}`].join(""),
                  rich: {
                    a: {
                      fontSize: 11,
                      color: "rgba(84, 108, 198, 0.65)"
                    },
                    b: {
                      fontSize: 14,
                      color: "rgba(84, 108, 198, 0.65)"
                    }
                  }
                },
                // 仪表盘的线，颜色值为一个数组
                axisLine: {
                  show: true,
                  // 两端是否设置为圆角；在5.0之后的版本有效
                  roundCap: false,
                  lineStyle: {
                    width: 8,
                    color: [
                      [
                        pointerData / 100,
                        {
                          x: 0,
                          y: 0,
                          x1: 1,
                          y1: 0,
                          colorStops: [
                            {
                              offset: 0,
                              color: "rgba(0, 255, 255, 0.6)"
                            },
                            {
                              offset: 1,
                              color: "rgba(0, 0, 255, .6)"
                            }
                          ]
                        }
                      ],
                      [1, "rgba(0,0,0,0.15)"]
                    ]
                  }
                },
                // 仪表盘刻度标签
                axisLabel: {
                  show: true,
                  color: "rgba(84, 108, 198, 0.65)",
                  fontSize: 12,
                  distance: 5,
                  formatter: val => {
                    const num = Math.floor(val);
                    return num % 20 === 0 ? num : "";
                  }
                },
                // 刻度
                axisTick: {
                  show: false
                },
                // 指针，此设置仅对5.0以上的版本生效
                anchor: {
                  show: true,
                  icon: "circle",
                  showAbove: true,
                  size: 10,
                  itemStyle: {
                    borderWidth: 6,
                    borderColor: "rgba(84, 108, 198, 0.85)"
                  }
                },
                data: [pointerData]
              },
              {
                // 背景渐变色
                type: "pie",
                radius: "80%",
                // 不响应及触发鼠标事件
                silent: true,
                // 关闭背景动画
                animation: false,
                z: 0,
                itemStyle: {
                  color: {
                    type: "radial", // 径向渐变
                    x: 0.5,
                    y: 0.5,
                    r: 0.25,
                    colorStops: [
                      {
                        offset: 0,
                        color: "rgba(84, 103, 198, 0.6)"
                      },
                      {
                        offset: 0.1,
                        color: "rgba(84, 103, 198, 0.35)"
                      },
                      {
                        offset: 1,
                        color: "rgba(84, 103, 198, 0)"
                      }
                    ]
                  }
                },
                data: [pointerData]
              }
            ]
          };
          myChart.setOption(option);
        }, 100);
      });
    },
    more() {
      this.show = false;
      this.charsData1 = JSON.parse(JSON.stringify(this.charsData));
      this.chars();
    },
    back() {
      this.show = true;
      this.charsData1 = JSON.parse(JSON.stringify(this.charsData));
      if (this.charsData1.length > 3) {
        this.charsData1.length = 3;
      }
      this.chars();
    },
    goWork() {
      this.$router.push("/workOrder");
    },
    async getStatistics() {
      this.workData = [];
      let data = {
        page: 1,
        size: 10
      };
      let res = [];
      let date = new Date();
      let month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1
      let date1 = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
      let day = date.getFullYear() + "-" + month + "-" + date1
      this.time = day
      res = await statisticsFind({ day });
      if (res.data.code == 1) {
        this.charsData = res.data.data
        console.log(res.data.data)
        this.charsData1 = JSON.parse(JSON.stringify(this.charsData));
        if (this.charsData1.length > 3) {
          this.charsData1.length = 3;
        }
        this.chars();
      }
    },
    async work() {
      this.workData = [];
      let data = {
        page: 1,
        size: 10
      };
      let res = [];
      this.isSubmitLoading = true
      this.activeName == "first"
        ? (res = await workOrderFindAllByMy(Object.assign(data, { status: 0 })))
        : (res = await workOrderFindAll(data));
      if (res.data.code == 1) {
        this.workData = res.data.data.content;
        this.isSubmitLoading = false
      }
    },
    handleClick(tab, event) {
      this.work();
    }
  }
};
</script>
<style lang="scss" scoped>
.main-box {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  .main-img {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    background-size: 100%;
    .topDiv {
      width: 98%;
      margin-left: 1%;
      padding: 10px;
      box-sizing: border-box;
      // box-shadow: 0 20px 20px -30px gray;
      // border-bottom: 2px solid #E4E7ED;
      margin-bottom: 20px;
      display: flex;
      .chars {
        flex: 1;
        display: flex;
        flex-direction: column;
        > .char {
          flex: 1;
          display: flex;
          flex-wrap: wrap;
          align-items: center;
          padding-right: 50px;
          box-sizing: border-box;
          > div {
            width: 31%;
            margin: 0 1%;
            height: 180px;
            display: flex;
            align-items: center;
            > .echar {
              width: 125px;
              height: 125px;
              margin-right: 20px;
            }
            .ms {
              font-size: 14px;
              .name {
                color: #6680ff;
                text-decoration: underline;
                line-height: 50px;
              }
            }
          }
        }
      }
      .more {
        display: flex;
        align-items: center;
        margin-right: 50px;
        color: #1d4277;
        text-decoration: underline;
        cursor: pointer;
      }
      .tit {
        border-left: 8px solid #1d4277;
        margin-top: 20px;
        line-height: 20px;
        padding-left: 10px;
      }
    }
    .botDiv {
      width: 98%;
      margin-left: 1%;
      .work {
        width: 100%;
        height: 340px;
        display: flex;
        .datas {
          flex: 1;
          // display: flex;
          // flex-wrap: wrap;
          > div {
            width: 50%;
            float: left;
            height: 20%;
            padding: 5px 80px 5px 10px;
            box-sizing: border-box;
            > div {
              width: 100%;
              height: 50%;
              display: flex;
              align-items: center;
              > span:nth-child(1) {
                width: 150px;
              }
            }
            > div:nth-child(1) {
              justify-content: space-between;
              > span:nth-child(3) {
                color: #f2541f;
              }
            }
            > div:nth-child(2) {
              color: #aaaaaa;
              font-size: 14px;
            }
          }
        }
        .more {
          display: flex;
          align-items: center;
          margin: 0 50px;
          color: #1d4277;
          text-decoration: underline;
        }
      }
    }
  }
}
.ellipsis {
  width: 140px !important;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
