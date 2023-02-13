<template>
  <el-contain>
    <div class="form_container" v-loading="loading">
      <el-form ref="formData" :model="formData" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据源名称:" :label-width="labelWidth" prop="name">
              <el-input clearable placeholder="请输入数据源名称" v-model="formData.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属部门:" :label-width="labelWidth" prop="deptId">
              <el-cascader style="width: 100%;" placeholder="请选择所属部门" :props="props" :disabled="disabled" collapse-tags clearable v-model="formData.deptId" :options="organizations" @change="handleChange" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据源类型:" :label-width="labelWidth" prop="type">
              <el-select clearable placeholder="请选择数据源类型" v-model="formData.type" @change="typeChange">
                <el-option label="Mysql" value="Mysql"></el-option>
                <el-option label="Postgresql" value="Postgresql"></el-option>
                <el-option label="Oracle" value="Oracle"></el-option>
                <el-option label="SqlServer" value="SqlServer"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库IP:" :label-width="labelWidth" prop="ip">
              <el-input clearable placeholder="请输入数据库IP" v-model="formData.ip"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="端口号:" :label-width="labelWidth" prop="port">
              <el-input clearable placeholder="请输入端口号" v-model="formData.port"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Owner:" :label-width="labelWidth" prop="dataBaseName" v-if="formData.type === 'Oracle'">
              <el-input clearable placeholder="请输入Owner" v-model="formData.dataBaseName"></el-input>
            </el-form-item>
            <el-form-item label="数据库名称:" :label-width="labelWidth" prop="dataBaseName" v-else>
              <el-input clearable placeholder="请输入数据库名称" v-model="formData.dataBaseName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户名:" :label-width="labelWidth" prop="username">
              <el-input clearable placeholder="请输入用户名" v-model="formData.username"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="密码:" :label-width="labelWidth" prop="password">
              <el-input clearable placeholder="请输入密码" v-model="formData.password" :type="passwordType">
                <template slot="append">
                  <el-image style="width: 20px; height: 20px" :src="imagePath" :fit="fit" @click="passwordTypeChange"></el-image>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" v-if="formData.type === 'Oracle'">
            <el-form-item label="ServiceName:" :label-width="labelWidth" prop="serviceName">
              <el-input clearable placeholder="请输入ServiceName" v-model="formData.serviceName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分组:" :label-width="labelWidth" prop="groupId">
              <el-select clearable placeholder="请选择分组" v-model="formData.groupId">
                <template v-for="(item,i) in groups">
                  <el-option :label="item.name" :value="item.id" v-bind:key="i"></el-option>
                </template>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="描述:" :label-width="labelWidth" prop="description">
              <el-input type="textarea" :rows="5" clearable placeholder="请输入描述" v-model="formData.description"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <div class="form_footbar">
          <el-button @click="goBack">取消</el-button>
          <el-button @click="connectTest">连接测试</el-button>
          <el-button type="primary" @click="submit('formData')">保存</el-button>
        </div>
      </el-form>

      <!-- <div class="form_footbar">
        <el-button @click="goBack">取消</el-button>
        <el-button @click="connectTest">连接测试</el-button>
        <el-button type="primary" @click="submit">保存</el-button>
      </div> -->
    </div>
  </el-contain>
</template>

<script>
import { getDataSourceById, dataSourceInsert, dataSourceUpdate, getOrganizationOptions, getDataSourceGroupList, connectTest } from "@/api/sourceManage.js";
export default {
  data() {
    return {
      loading: false,
      labelWidth: '150px',

      type: '',
      id: '',

      formData: {
        name: '',
        type: '',
        ip: '',
        port: '',
        dataBaseName: '',
        username: '',
        password: '',
        groupId: '',
        description: '',
        deptId: '',
        serviceName: ''
      },
      rules: {
        name: [{
          required: true,
          message: '请输入数据源名称',
          trigger: ['blur', 'change'],
        }],
        type: [{
          required: true,
          message: '请选择数据源类型',
          trigger: ['blur', 'change'],
        }],
        ip: [{
          required: true,
          message: '请输入ip',
          trigger: ['blur', 'change'],
        }],
        port: [{
          required: true,
          message: '请输入端口号',
          trigger: ['blur', 'change'],
        }],
        dataBaseName: [{
          required: true,
          message: '请输入数据库名称',
          trigger: ['blur', 'change'],
        }],
        username: [{
          required: true,
          message: '请输入用户名',
          trigger: ['blur', 'change'],
        }],
        password: [{
          required: true,
          message: '请输入密码',
          trigger: ['blur', 'change'],
        }],
        deptId: [{
          required: true,
          message: '请选择部门',
          trigger: ['blur', 'change'],
        }]
      },
      organizations: [],
      props: {
        value: 'id',
        label: 'name',
        checkStrictly: true,
        emitPath: false
      },
      groups: [],
      passwordType: 'password',
      imagePath: require('@/icons/png/close_eye.png')
    }
  },
  methods: {
    async getDataSourceGroupList() {
      const that = this
      let params = {}
      let response = await getDataSourceGroupList(params)

      if (response.data.code === 1) {
        that.groups = response.data.data;
        console.log(response.data, "============")
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async getDataSource() {
      const that = this
      let params = {
        id: that.id
      }

      that.loading = true
      let response = await getDataSourceById(params)
      that.loading = false

      if (response.data.code === 1) {
        that.formData = response.data.data
      } else {
        that.$message.error(response.data.msg)
      }
    },
    submit(ruleForm) {
      const that = this
      let data = that.formData
      this.$refs[ruleForm].validate((valid) => {
        if (valid) {
          if (that.type === 'add') {
            data.id = that.id
            that.saveAdd(data)
          } else {
            that.saveEdit(data)
          }
        }
      })
    },
    async saveAdd(data) {
      const that = this
      that.loading = true
      let response = await dataSourceInsert(data)
      that.loading = false

      if (response.data.code === 1) {
        that.$message.success('添加成功')
        that.goBack()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async saveEdit(data) {
      const that = this
      that.loading = true
      let response = await dataSourceUpdate(data)
      that.loading = false

      if (response.data.code === 1) {
        that.$message.success('编辑成功')
        that.goBack()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    goBack() {
      const that = this
      // that.$refs['form'].resetFields()

      that.$router.push({
        path: '/datasource'
      })
    },
    async getOrganizationList() {
      const that = this
      let response = await getOrganizationOptions()
      if (response.data.code === 1) {
        that.organizations = response.data.data
        console.log('that.organizations', that.organizations)
      }
    },

    handleChange(value) {
      console.log('value1111111', value);
    },

    //连接测试
    async connectTest() {
      const that = this
      let params = that.formData

      that.loading = true
      let response = await connectTest(params)
      that.loading = false

      if (response.data.code === 1) {
        if (response.data.data) {
          that.$message.success('连接成功')
        } else {
          that.$message.warning('连接失败')
        }
      } else {
        that.$message.error(response.data.msg)
      }
    },

    //更改数据库类型
    typeChange() {
      const that = this
      if (that.formData.type === 'Oracle') {
        that.rules.serviceName = [{
          required: true,
          message: '请选择ServiceName',
          trigger: ['blur', 'change'],
        }]
      } else {
        delete that.rules.serviceName
      }
    },

    //展示或隐藏密码
    passwordTypeChange() {
      const that = this

      if (that.passwordType === 'password') {
        that.passwordType = 'text'
        that.imagePath = require('@/icons/png/eye.png')
      } else {
        that.passwordType = 'password'
        that.imagePath = require('@/icons/png/close_eye.png')
      }
    }
  },
  async created() {
    const that = this
    that.loading = true

    that.type = that.$route.query.type
    that.groupId = that.$route.query.groupId
    if (that.type === 'edit') {
      that.id = that.$route.query.id
      await that.getDataSource()
    }

    await that.getOrganizationList()
    await that.getDataSourceGroupList()

    that.loading = false
  }
}
</script>

<style>
.form_container {
  padding: 30px;
  justify-content: center;
  width: 80%;
}
.form_footbar {
  padding: 0 15px 0 15px;
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>