<template>
  <el-main class="main">
    <div><h4>{{ title }}</h4></div>
    <el-col class="main-center">
      <el-form :model="ruleForm" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="工单标题:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" :disabled="true" placeholder="" v-model="ruleForm.title"></el-input>
        </el-form-item>

        <el-form-item label="工单级别:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.levelStr"></el-input>
        </el-form-item>

        <el-form-item label="工单状态:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.statusStr"></el-input>
        </el-form-item>

        <el-form-item label="工单提交人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.createByName"></el-input>
        </el-form-item>

        <el-form-item label="工单描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' :rows='4' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.description"></el-input>
        </el-form-item>

        <el-form-item label="工单解决人:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.handlerName"></el-input>
        </el-form-item>

        <el-form-item label="工单解决周期:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.handlerPeriod"></el-input>
        </el-form-item>

        <el-form-item label="工单处理意见:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' :rows='4' type="textarea" placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.handlerOpinion"></el-input>
        </el-form-item>

        <el-form-item label="规则数据源:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.dataBasesName"></el-input>
        </el-form-item>
        <el-form-item label="规则实体表:" :label-width="this.formLabelWidth">
          <el-input maxlength='100' placeholder="" clearable size="medium" :disabled="true" v-model="ruleForm.tableName"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">返回列表</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {workOrderFind} from "@/api/workOrder.js"

export default {
  data() {
    return {
      title: '详情',
      ruleForm: {
        createByName: '',
        api: "",
        contentMsg: '',
        result: '',
        createTime: ''
      },
      formLabelWidth: "120px"
    }
  },
  created() {
    this.getDetail()
  },
  methods: {
    // 详情
    async getDetail() {
      const that = this
      let data = {id: this.$route.query.id}
      that.loading = true
      const response = await workOrderFind(data)
      console.log(response)

      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data

        that.ruleForm.statusStr = that.getWorkOrderStatus(response.data.data.status)
        that.ruleForm.levelStr = that.getWorkOrderLevel(response.data.data.level)

      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.push('/workOrder')
    },

    //////

    //0正常、1一般、2关注、3警告、4严重警告
    getWorkOrderLevel(status) {
      switch (status) {
        case 0:
          return "正常"
        case 1:
          return "一般"
        case 2:
          return "关注"
        case 3:
          return "警告"
        case 4:
          return "严重警告"
        default:
          return "未知"
      }
    },
    getWorkOrderStatus(status) {
      switch (status) {
        case 0:
          return "处理中"
        case 1:
          return "已通过"
        case 2:
          return "已驳回"
        default:
          return "未知"
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;

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
</style>
