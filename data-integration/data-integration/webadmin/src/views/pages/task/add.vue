<template>
  <el-main class="main">
    <div><h4>{{ title }}</h4></div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm"
               v-loading="loading">
        <el-form-item label="任务名称:" :label-width="this.formLabelWidth" prop='name'>
          <el-input clearable maxlength='100' size="medium" :disabled="disabled" placeholder="请输入任务名称"
                    v-model="ruleForm.name"/>
        </el-form-item>

        <el-form-item label="容器名称:" :label-width="this.formLabelWidth" prop='container'>
          <el-select style="width: 25%;margin-right: 5px;" placeholder="请选择" filterable v-model="ruleForm.container" value-key="name">
            <el-option v-for="op in container" :key="op.name" :label="op.name" :value="op"/>
          </el-select>
        </el-form-item>

        <el-form-item label="起始数据源:" :label-width="this.formLabelWidth" prop='startDataSource'>
          <el-select style="width: 25%;margin-right: 5px;" placeholder="请选择" filterable v-model="ruleForm.startDataSource">
            <el-option v-for="op in dataSource" :key="op.value" :label="op.value" :value="op.value"/>
          </el-select>
        </el-form-item>

        <el-form-item label="目标数据源:" :label-width="this.formLabelWidth" prop='endDataSource'>
          <el-select style="width: 25%;margin-right: 5px;" placeholder="请选择" filterable v-model="ruleForm.endDataSource">
            <el-option v-for="op in dataSource" :key="op.value" :label="op.value" :value="op.value"/>
          </el-select>
        </el-form-item>

        <el-form-item label="分组:" :label-width="this.formLabelWidth" prop='groupId'>
          <el-cascader
              expandTrigger="hover"
              v-model="ruleForm.groupId"
              :options="groupOptions"
              :props="groupOptionProps"
              clearable>
          </el-cascader>
        </el-form-item>

        <el-form-item label="标签:" label-width="120px" prop='metas'>
          <div v-for="(item, index) in ruleForm.metas">
            <el-select size="small" style="margin-right: 5px;" placeholder="请选择标签" filterable v-model="item.metaKey">
              <el-option v-for="op in options" :key="op.value" :label="op.value"
                         :value="op.value"/>
            </el-select>
            <el-tag type="info" style="margin-left: 3px;">
              <input style="border: #ECF5FF;background-color: #F4F4F5;outline: none;width: 140px;" placeholder="请输入标签值" v-model="item.metaValue"/>
            </el-tag>
            <el-button type="small" icon="el-icon-plus" @click="addParameter" v-if="index===0"></el-button>
            <el-button type="small" icon="el-icon-delete" @click="deleteParameter(item.id)" v-else ></el-button>
          </div>
        </el-form-item>
        <el-form-item label="描述:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' type="textarea" :rows="3" placeholder="请输入描述" clearable size="medium"
                    :disabled="disabled" v-model="ruleForm.remark"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {add, update, getTree} from "@/api/task.js";
import {findAllMetas,findAllContainer,findAllDataSource} from "@/api/resources";

export default {
  data() {
    return {
      title: '新增',
      disabled: false,
      props: {checkStrictly: true},
      options: [],
      container: [],
      dataSource: [],
      ruleForm: {
        name: "",
        container: "",
        startDataSource: "",
        endDataSource: "",
        metas: [{
          id: 1,
          metaKey: '',
          metaValue: ''
        }],
        remark: "",
        groupId: ""
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "任务名称",
          trigger: "change",
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
        }],
        metas: [{
          required: true,
          message: '请选择标签',
          trigger: "change"
        }]
      },
      headers: {
        Authorization: 'token ' + JSON.parse(sessionStorage.getItem("UserInfo")).token,
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
      this.ruleForm.metas= this.$route.query.metas
      this.ruleForm.remark=this.$route.query.remark
    } else if (this.$route.query.type === '详情') {
      this.disabled = true
    }
    await this.findMetas()
    await this.findContainer()
    await this.findDataSource()
    await this.getGroupList()
  },
  methods: {
    async findMetas() {
      const that = this
      that.loading = true
      const response = await findAllMetas()
      that.loading = false
      if (response.data.code === 1) {
        that.options = []
        response.data.data.content.map(function (v, k) {
          that.options.push({key: v.name,value: v.name, label: v.name})
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async findContainer() {
      const that = this
      that.loading = true
      const response = await findAllContainer()
      that.loading = false
      if (response.data.code === 1) {
        that.container = response.data.data.content
        console.log(that.container)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async findDataSource(data) {
      const that = this
      that.loading = true
      const response = await findAllDataSource()
      that.loading = false
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
        const res = await getTree()
        if (res.data.code === 1) {
          that.groupOptions = res.data.data
        } else {
          that.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    handleChange() {
    },
    submitForm(ruleForm) {
      const that = this
      //判断标签
      let metas = that.ruleForm.metas[0].metaKey
      if(metas === ""){
        that.$message.error("请选择标签")
        return
      }
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          that.loading = true;
          //转换一下字符串对象
          that.ruleForm.container = JSON.stringify(that.ruleForm.container)
          let data = that.ruleForm
          if (that.$route.query.type === '编辑') {
            // 编辑
            update(data).then((res) => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success('编辑成功')
                that.goBack()
              } else {
                that.$message.error(res.data.msg)
              }
            });
          } else {
            // 新增
            add(data).then((res) => {
              that.loading = false
              if (res.data.code === 1) {
                that.$message.success('新增成功')
                that.goBack()
              } else {
                that.$message.error(res.data.msg)
              }
            })
          }
        } else {
          return false;
        }
      });
    },
    goBack() {
      this.$router.push('/task')
    },
    addParameter() {
      let parameter = {
        id: this.ruleForm.metas.length,
        key: '',
        value: ''
      }
      this.ruleForm.metas.push(parameter)
    },
    deleteParameter(id) {
      this.ruleForm.metas.splice(id, 1)
      let parametersTemp = this.ruleForm.metas
      parametersTemp.forEach((item, index) => {
        item.id = index
      })
      this.ruleForm.metas = parametersTemp
    },
  }
}
</script>
<style lang="scss" scoped>
.main {
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
