<template>
  <el-main class="main">
    <div>
      <h4>{{ title }}</h4>
    </div>
    <el-form
        :model="form"
        :rules="rules"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm"
        v-loading="loading">
      <el-form-item :label-width="0" prop='foreword'>
        <el-input
            maxlength="200"
            :rows="4"
            type="textarea"
            placeholder="请输入报告前言"
            clearable
            size="medium"
            v-model="form.foreword"
            v-if='!show'
        ></el-input>
      </el-form-item>
      <div v-if="show">{{ form.foreword }}</div>
      <div style="margin:20px 0;font-size:14px;text-indent:28px;padding:0 80px;">
        在<span style="color:blue"> {{ ruleForm.count.startTime }}</span>到<span style="color:blue"> {{ ruleForm.count.endTime }}</span>期间，总共运行了<span style="color:blue"> {{ ruleForm.count.runTaskCount }}</span>条任务，新建了<span style="color:blue"> {{ ruleForm.count.createRuleCount }}</span>条规则，
        新建了<span style="color:blue"> {{ ruleForm.count.createWorkOrderCount }}</span>个工单，关闭了<span style="color:blue"> {{ ruleForm.count.closeWorkOrderCount }}</span>个工单，
        核检了<span style="color:blue"> {{ ruleForm.count.validateDataCount }}</span>条数据，其中错误数据为<span style="color:blue"> {{ ruleForm.count.errorDataCount }}</span>条，错误率<span style="color:blue"> {{ ruleForm.count.errorRateCount }}</span>%。
      </div>
      <div class="chars" style="margin:20px 0;">
        <div id="char1"></div>
        <div id="char2"></div>
        <div id="char3"></div>
      </div>
      <div class="rulesNum" id="rulesNum"></div>
      <div class="runNum" id="runNum"></div>
      <div class="checkNum" id="checkNum"></div>
      <el-form-item prop="closing">
        <el-input
            maxlength="200"
            :rows="4"
            type="textarea"
            style="margin-top:30px"
            placeholder="请输入报告结束语"
            clearable
            size="medium"
            v-model="form.closing"
            v-if='!show'
        ></el-input>
      </el-form-item>
    </el-form>
    <div v-if='show'>{{ form.closing }}</div>
    <div class="demo-drawer__footer drawer_footer">
      <el-button size="medium" @click="goBack">取 消</el-button>
      <el-button size="medium" type="primary" v-if="!show" @click="submitForm('ruleForm')">保 存</el-button>
      <el-button size="medium" type="primary" v-if="show" @click="goBack">确定</el-button>

    </div>
  </el-main>
</template>

<script>
import * as echarts from "echarts";
import {statisticsFind, statisticsUpdate} from "@/api/qualityReport.js";

export default {
  data() {
    return {
      title: "",
      show: false,
      ruleForm: {
        count: ""
      },
      form: {
        foreword: "",
        closing: "",
        di: ""
      },
      formLabelWidth: "120px",
      rules: {
        foreword: [
          {
            required: true,
            message: "请输入报告前言",
            trigger: "change"
          }
        ],
        closing: [
          {
            required: true,
            message: "请选择报告结束语",
            trigger: "change"
          }
        ]
      }
    };
  },
  created() {
    this.userDetail();
    this.form.foreword = this.$route.query.foreword;
    this.form.closing = this.$route.query.closing;
    this.form.id = this.$route.query.id;
    if (this.$route.query.type === 'detail') {
      this.show = 'true'
    }
  },
  mounted() {
  },
  methods: {
    chars() {
      let workOrderLevel = [];
      let workOrderCreateByName = [];
      let workOrderHandlerName = [];
      let ruleCountData = [];
      let ruleCountNum = [];
      let taskCountData = [];
      let taskCountNum = [];
      let validDataCountTime = [];
      let validDataCountNum = [];
      let validDataCountErrorNum = [];

      this.ruleForm.workOrderLevel.forEach((v, i) => {
        let name = "";
        if (v.level === 0) {
          name = "正常";
        } else if (v.level === 1) {
          name = "一般";
        } else if (v.level === 2) {
          name = "关注";
        } else if (v.level === 3) {
          name = "警告";
        } else if (v.level === 4) {
          name = "严重警告";
        }
        workOrderLevel.push({
          name: name,
          value: v.count
        });
      });

      this.ruleForm.workOrderCreateByName.forEach((v, i) => {
        workOrderCreateByName.push({
          name: v.create_by_name,
          value: v.count
        });
      });

      this.ruleForm.workOrderHandlerName.forEach((v, i) => {
        workOrderHandlerName.push({
          name: v.handler_name,
          value: v.count
        });
      });

      this.ruleForm.ruleCount.forEach((v, i) => {
        ruleCountData.push(v.rule_template_name);
        ruleCountNum.push(v.count);
      });

      this.ruleForm.taskCount.forEach((v, i) => {
        taskCountData.push(v.year_month);
        taskCountNum.push(v.count);
      });

      this.ruleForm.validDataCount.forEach((v, i) => {
        validDataCountTime.push(v.year_month);
        validDataCountNum.push(v.total_data_size);
        validDataCountErrorNum.push(v.total_error_data_size);
      });

      this.pieChars('char1', '工单级别饼图', workOrderLevel)
      this.pieChars('char2', '工单人创建饼图', workOrderCreateByName)
      this.pieChars('char3', '工单人解决饼图', workOrderHandlerName)

      var myChart3 = echarts.init(document.getElementById("rulesNum"));
      let option3 = {
        xAxis: {
          type: "category",
          data: ruleCountData,
          "axisLabel": {
            interval: 0
          }
        },
        yAxis: {
          type: "value",
          name: "规则数(个)"
        },
        tooltip: {
          trigger: "item"
        },
        series: [
          {
            type: "bar",
            barWidth: 30,
            data: ruleCountNum
          }
        ]
      };
      myChart3.setOption(option3);

      var myChart4 = echarts.init(document.getElementById("runNum"));
      let option4 = {
        tooltip: {
          trigger: "axis"
        },
        legend: {
          data: ["运行任务个数"]
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: taskCountData
        },
        yAxis: {
          type: "value",
          name: "个数"
        },
        series: [
          {
            name: "运行任务个数",
            type: "line",
            stack: "总量",
            data: taskCountNum
          }
        ]
      };
      myChart4.setOption(option4);

      var myChart5 = echarts.init(document.getElementById("checkNum"));
      let option5 = {
        tooltip: {
          trigger: "axis"
        },
        legend: {
          data: ["检查总数条数", "检查错误条数"]
        },
        grid: {
          left: "3%",
          right: "4%",
          bottom: "3%",
          containLabel: true
        },
        xAxis: {
          type: "category",
          boundaryGap: false,
          data: validDataCountTime
        },
        yAxis: {
          type: "value",
          name: "个数"
        },
        series: [
          {
            name: "检查总数条数",
            type: "line",
            stack: "检查总数条数",
            data: validDataCountNum
          },
          {
            name: "检查错误条数",
            type: "line",
            stack: "检查错误条数",
            data: validDataCountErrorNum
          }
        ]
      };
      myChart5.setOption(option5);
    },
    pieChars(id, title, data) {
      var myChart = echarts.init(document.getElementById(id));
      let option = {
        title: {
          text: title,
          left: "center",
          bottom: "60px"
        },
        tooltip: {
          trigger: "item"
        },
        legend: {
          orient: "horizontal",
          left: "center",
          bottom: "10px"
        },
        series: [
          {
            name: "级别",
            type: "pie",
            center: ["50%", "35%"],
            radius: ["20%", "50%"],
            data: data,
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            }
          }
        ]
      };
      myChart.setOption(option);
    },
    // 详情
    async userDetail() {
      const that = this;
      let data = {
        endTime: this.$route.query.endTime,
        startTime: this.$route.query.startTime
      };
      let startTime = this.$route.query.startTime.split("-");
      let endTime = this.$route.query.endTime.split("-");
      this.title =
          startTime[0] +
          "年" +
          startTime[1] +
          "月~" +
          endTime[0] +
          "年" +
          endTime[1] +
          "月质量报告";
      that.loading = true;
      const response = await statisticsFind(data);
      that.loading = false;
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
        this.chars();
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },
    goBack() {
      this.$router.push("/qualityReport");
    },
    submitForm(ruleForm) {
      const that = this;
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          that.loading = true;
          let data = that.form;
          statisticsUpdate(data).then(res => {
            that.loading = false;
            if (res.data.code === 1) {
              that.$message.success("新增成功");
              that.goBack();
            } else {
              that.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    }
  }
};
</script>
<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;

  h4 {
    text-align: center;
  }

  .main-center {
    padding: 20px;
    border-radius: 5px;
    width: 65%;
    margin: 0 auto;
  }
}

.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}

.chars {
  width: 100%;
  height: 300px;
  display: flex;

  div {
    flex: 1;
  }
}

.rulesNum {
  width: 100%;
  height: 300px;
  margin-top: 30px;
}

.runNum {
  width: 100%;
  height: 300px;
  margin-top: 30px;
}

.checkNum {
  width: 100%;
  height: 300px;
  margin-top: 30px;
}

/deep/ .el-form-item__content {
  margin-left: 0 !important;
}
</style>
