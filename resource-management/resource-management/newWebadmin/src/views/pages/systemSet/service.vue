<template>
  <el-main class="main">
     <div><h4>服务器信息</h4></div> 
     <el-col class="main-center">
      <el-form v-loading="loading" :model="ruleForm" ref="ruleForm" style="width: 80%; margin: 0 auto;">
        <el-form-item label="系统类型:" :label-width="this.formLabelWidth" prop='name'>
          <el-input size="medium" :disabled="true" v-model="ruleForm.osName"></el-input>
        </el-form-item>
        <el-form-item label="硬盘大小:" :label-width="this.formLabelWidth" prop='name'>
          <el-input size="medium" :disabled="true" v-model="ruleForm.total"></el-input>
        </el-form-item>
        <el-form-item label="已用硬盘大小:" :label-width="this.formLabelWidth" prop='name'>
          <el-input size="medium" :disabled="true" v-model="ruleForm.used"></el-input>
        </el-form-item>
        <el-form-item label="内存大小:" :label-width="this.formLabelWidth" prop='name'>
          <el-input size="medium" :disabled="true" v-model="ruleForm.memTotal"></el-input>
        </el-form-item>
        <el-form-item label="已用内存大小:" :label-width="this.formLabelWidth" prop='name'>
          <el-input size="medium" :disabled="true" v-model="ruleForm.memRam"></el-input>
        </el-form-item>
        <el-form-item label="CPU已用百分比:" :label-width="this.formLabelWidth" prop='name'>
          <el-input size="medium" :disabled="true" v-model="ruleForm.combined"></el-input>
        </el-form-item>  
      </el-form>
    </el-col>
  </el-main>
</template>
<script>
import { timedTaskFindOsInfo } from "@/api/info.js"
export default {
  components: { },
  data() {
    return {
      title: '新增',
      loading: false,
      ruleForm: {
        osName: "",
        total: '',
        used: "",
        memTotal: '',
        memRam: "",
        combined: ''
      },
      formLabelWidth: "120px"
    }
  },
  created () {
    this.osDetail()
  },
  methods: {
    // 服务器详情
    async osDetail() {
      const that = this
      let data = {}
      that.loading = true
      const response = await timedTaskFindOsInfo(data)
      that.loading = false
      if (response.data.code === 1) {
        let datas =  response.data.data
        that.ruleForm.osName = datas.osBasicInfo.osName
        that.ruleForm.total = datas.hardDiskInfo.total
        that.ruleForm.used = datas.hardDiskInfo.used
        that.ruleForm.memTotal = datas.ramInfo.memTotal
        that.ruleForm.memRam = datas.ramInfo.memRam
        that.ruleForm.combined = datas.cpuInfo.combined
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
  }
}
</script>