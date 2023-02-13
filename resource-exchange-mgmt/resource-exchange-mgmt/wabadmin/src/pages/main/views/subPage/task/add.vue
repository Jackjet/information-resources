<template>
  <el-main class="main">
    <el-col>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" v-loading="loading" class="form-box">
        <el-form-item label="任务名称:" :label-width="this.formLabelWidth" prop='name'>
          <el-input clearable maxlength='100' size="medium" :disabled="disabled" placeholder="请输入姓名"
                    v-model="ruleForm.name"/>
        </el-form-item>

        <el-form-item label="容器名称:" :label-width="this.formLabelWidth" prop='container'>
          <el-select style="width: 100%;" placeholder="请选择" filterable v-model="ruleForm.container">
            <el-option v-for="op in container" :key="op.key" :label="op.label" :value="op.value"/>
          </el-select>
        </el-form-item>

        <el-form-item label="起始数据源:" :label-width="this.formLabelWidth" prop='startDataSource'>
          <el-select style="width: 100%;" placeholder="请选择" filterable v-model="ruleForm.startDataSource">
            <el-option v-for="op in dataSource" :key="op.value" :label="op.value" :value="op.value"/>
          </el-select>
        </el-form-item>

        <el-form-item label="目标数据源:" :label-width="this.formLabelWidth" prop='endDataSource'>
          <el-select style="width: 100%;" placeholder="请选择" filterable v-model="ruleForm.endDataSource">
            <el-option v-for="op in dataSource" :key="op.value" :label="op.value" :value="op.value"/>
          </el-select>
        </el-form-item>

        <el-form-item label="分组:" :label-width="this.formLabelWidth" prop='groupId'>
          <el-cascader style="width: 100%;"
              expandTrigger="hover"
              v-model="ruleForm.groupId"
              :options="groupOptions"
              :props="groupOptionProps"
              clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' type="textarea" :rows="3" placeholder="请输入描述" clearable size="medium"
                    :disabled="disabled" v-model="ruleForm.remark"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
// import {add, update, getTree} from "@/api/task.js";

export default {
  data() {
    return {
      title: '新增',
      disabled: false,
      container: [],
      dataSource: [],
      ruleForm: {
        name: "",
        container: "",
        startDataSource: "",
        endDataSource: "",
        remark: "",
        groupId: ""
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "请输入任务名称",
          trigger: "blur",
        }],
        container: [{
          required: true,
          message: "请选择容器",
          trigger: "change",
        }],
        startDataSource: [{
          required: true,
          message: '请选择起始数据源',
          trigger: "change"
        }],
        endDataSource: [{
          required: true,
          message: '请选择目标数据源',
          trigger: "change"
        }],
        groupId: [{
          required: true,
          message: '请选择组',
          trigger: "change"
        }]
      },
      loading: false,
      groupOptions: [],
      groupOptionProps: {
        value: 'id',
        label: 'name',
        children: 'child',
        checkStrictly: true,
        emitPath:false
      }
    }
  },
  async created() {
    this.title = this.$route.query.type
    this.ruleForm.groupId = this.$route.query.groupId
    if (this.$route.query.type === '编辑') {
      this.ruleForm.id = this.$route.query.id
      this.ruleForm.name = this.$route.query.name
      this.ruleForm.container= this.$route.query.container
      this.ruleForm.startDataSource= this.$route.query.startDataSource
      this.ruleForm.endDataSource= this.$route.query.endDataSource
      this.ruleForm.remark=this.$route.query.remark
    } else if (this.$route.query.type === '详情') {
      this.disabled = true
    }
    this.findContainer()
    this.findDataSource()
    this.getGroupList()
  },
  methods: {
    async findContainer() {
      const that = this
      let url = that.Interface.container.find + '?page=1&size=1000'
      let response = await this.request.dataGet(that, url, {})
      if (response.data.code === 1) {
        that.container = response.data.data.content
        that.container = []
        response.data.data.content.map(function (v, k) {
          that.container.push({key: v.id,value: v.id, label: v.name})
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async findDataSource(data) {
      const that = this
      let url = that.Interface.dataResource.find + '?page=1&size=1000'
      let response = await this.request.dataGet(that, url, {})
      if (response.data.code === 1) {
        that.dataSource = []
        response.data.data.content.map(function (v, k) {
          that.dataSource.push({key: v.name,value: v.name, label: v.name})
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    // 获取列表
    async getGroupList() {
      const that = this
      try {
        let url = this.Interface.group.getTree
        let res = await this.request.dataGet(that, url, {})
        if (res.data.code === 1) {
          that.groupOptions = res.data.data
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    submitForm(ruleForm) {
      const that = this
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          let data = that.ruleForm
          if (that.$route.query.type === '编辑') {
            // 编辑
            that.editSave(data)
          } else {
            // 新增
            that.addSave(data)
          }
        } else {
          return false;
        }
      });
    },
    async addSave (data) {
      const that = this
      let url = that.Interface.task.insert
      that.loading = true
      const response = await that.request.dataPost(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        that.$message.success('新增成功')
        that.goBack()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async editSave (data) {
      const that = this
      let url = that.Interface.task.update
      that.loading = true
      const response = await that.request.dataPut(that, url, data)
      that.loading = false
      if (response.data.code === 1) {
        that.$message.success('编辑成功')
        that.goBack()
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      this.$router.push('/index/task')
    }
  }
}
</script>
<style lang="scss" scoped>
.form-box {
  width: 80%;
  max-width: 800px;
  margin: 20px auto;
}
.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}
</style>
