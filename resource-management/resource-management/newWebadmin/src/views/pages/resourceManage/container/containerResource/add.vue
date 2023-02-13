<template>
  <el-main class="main">
    <div style="margin-left: 260px">
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center" style="margin-left: 260px">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="容器名称:" :label-width="this.formLabelWidth" prop="name">
          <el-input clearable maxlength='100' size="medium" placeholder="请输入容器名称" v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="容器类型:" :label-width="this.formLabelWidth" prop="type">
          <el-select v-model="ruleForm.type" placeholder="请选择容器类型">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="容器IP:" :label-width="this.formLabelWidth" prop="ip">
          <el-input clearable maxlength='100' size="medium" placeholder="请输入容器IP" v-model="ruleForm.ip"></el-input>
        </el-form-item>
        <el-form-item label="容器端口号:" :label-width="this.formLabelWidth" prop="port">
          <el-input clearable maxlength='100' size="medium" placeholder="请输入端口号" v-model="ruleForm.port"></el-input>
        </el-form-item>
        <el-form-item label="标签:" :label-width="this.formLabelWidth">
          <div v-for="(item,i) in metas" :key="i">
            <el-select style="width:40%" size="medium" v-model="item.key" filterable remote reserve-keyword placeholder="请输入标签名" :remote-method="remoteMethod">
              <el-option v-for="item in tagInfos" :key="item.name" :label="item.name" :value="item.name">
              </el-option>
            </el-select>
            <el-tag type="info" style="margin-left: 3px;width:40%">
              <input style="border: #ECF5FF;background-color: #F4F4F5;outline: none;width: 140px;" v-model="item.value" placeholder="请输入" />
            </el-tag>
            <el-button size="medium" icon="el-icon-plus" style="margin-top: 4px;margin-left: 3px;" @click="insertMeta" v-if="i === 0"></el-button>
            <el-button size="medium" icon="el-icon-delete" style="margin-top: 4px;margin-left: 3px;" @click="removeMeta(i)" v-if="i !== 0"></el-button>
          </div>
        </el-form-item>
        <el-form-item label="分组:" :label-width="this.formLabelWidth">
          <el-cascader expandTrigger="hover" v-model="ruleForm.groupId" :options="groupOptions" :props="{ checkStrictly: true,emitPath:false }" clearable>
          </el-cascader>
        </el-form-item>
        <el-form-item label="描述:" :label-width="this.formLabelWidth">
          <el-input type="textarea" :rows="4" clearable maxlength='100' size="medium" placeholder="请输入容器描述" v-model="ruleForm.detail"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
          <el-button size="medium" @click="testConnection">链接测试</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>
</template>

<script>
import { add } from "@/api/containerInfo.js";
import { withoutIpRequest } from "@/api/withoutIpRequest.js";
import { findList } from "@/api/tagInfo.js";
import { getGroupList } from "@/api/groupInfo.js"

export default {
  data() {
    return {
      title: '新增',
      ruleForm: {
        name: '',
        ip: '',
        port: '',
        detail: ''
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "请填写容器名称",
          trigger: "change"
        }, {
          pattern: /^[^\s]*$/, //正则
          message: '请填写容器名称'
        }],
        type: [{
          required: true,
          message: "请选择容器类型",
          trigger: "change"
        }],
        ip: [{
          required: true,
          message: '请输入容器IP',
          trigger: 'change'
        }, {
          pattern: /^(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d{2}|2[0-4]\d|25[0-5])+$/,  //正则
          message: '请输入正确容器IP'
        }],
        port: [{
          required: true,
          message: '请输入端口号',
          trigger: 'change'
        }, {
          pattern: /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/,  //正则
          message: '请输入正确端口号'
        }],
      },
      groupId: '',
      metas: [{ key: '', value: '' }],
      tagInfos: [],
      status: 0,
      options: [{
        value: '0',
        label: 'API运行'
      }, {
        value: '1',
        label: '数据'
      }, {
        value: '2',
        label: '消息'
      }, {
        value: '3',
        label: 'API网关'
      }, {
        value: '4',
        label: '其他'
      }],
      groupOptions: []
    }
  },
  created() {
    this.groupId = this.$route.query.groupId
    this.getGroupList()
  },
  methods: {
    // 获取列表
    async getGroupList() {
      const that = this
      let data = { type: '1' }
      try {
        const res = await getGroupList(data)
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
      let metaInfo = that.metas
      for (let me of metaInfo) {
        if (me.key === '') {
          that.$message.error('请选择标签');
          return false
        }
        if (me.value === '') {
          that.$message.error('请填写标签值');
          return false
        }
      }
      let tempName = ''
      let tempValue = ''
      for (let te of metaInfo) {
        tempName = tempName + te.key + ','
        tempValue = tempValue + te.value + ','
      }
      this.$refs[ruleForm].validate((valid) => {
        if (valid) {
          // 取值
          let data = that.ruleForm
          if (!that.ruleForm.groupId) {
            data.groupId = that.groupId
          }
          data.status = that.status
          data.tagName = tempName.substr(0, tempName.length - 1)
          data.tagValue = tempValue.substr(0, tempValue.length - 1)
          add(data).then((res) => {
            if (res.data.code === 1) {
              that.$message.success('新增成功');
              that.resetForm('ruleForm')

            } else {
              that.$message.error(res.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },

    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.ruleForm.name = ""
      this.ruleForm.type = ""
      this.ruleForm.jdbcUrl = ""
      this.ruleForm.userName = ""
      this.ruleForm.password = ""
      this.ruleForm.detail = ""
      let groupId = this.groupId
      this.groupId = ""
      this.$router.push({
        path: '/container',
        query: {
          groupId: groupId
        }
      })
    },

    async testConnection() {
      let data = this.ruleForm
      let ip = data.ip
      if (!ip) {
        this.$message.error('请填写IP地址');
        return
      }
      let port = data.port
      if (!port) {
        this.$message.error('请填写端口号');
        return
      }
      let type = data.type
      if (!type) {
        this.$message.error('请选择容器类型');
        return
      }
      let baseUrl = 'http://' + ip + ':' + port
      if (data.type === 3) {
        try {
          let res = await withoutIpRequest(baseUrl, '', 'get')
          if (res.status === 200) {
            this.status = 1
            this.$message.success('链接成功');
          } else {
            this.$message.error('链接失败');
          }
        } catch (e) {
          this.$message.error('链接失败');
        }
      } else {
        try {
          let url = '/api/container/status'
          let res = await withoutIpRequest(baseUrl, url, 'get')
          if (res.status === 200) {
            this.status = 1
            this.$message.success('链接成功');
          } else {
            this.$message.error('链接失败');
          }
        } catch (e) {
          this.$message.error('链接失败');
        }
      }
    },

    insertMeta() {
      const that = this
      let item = {
        key: '',
        value: ''
      }
      that.metas.push(item)
    },

    removeMeta(i) {
      const that = this
      that.metas.splice(i, 1)
    },

    async remoteMethod(query) {
      if (query !== '') {
        let res = await findList({ name: query })
        if (res.data.code === 1) {
          this.tagInfos = res.data.data
        }
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.main-center {
  width: 45%;
}
.demo-drawer__footer {
  margin-top: 10px;
  text-align: center;
}
.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}
</style>
