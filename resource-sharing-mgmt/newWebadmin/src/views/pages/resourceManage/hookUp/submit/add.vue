<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="信息资源名称:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item>
        <el-form-item label="信息资源代码:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.uviewNo"></el-input>
        </el-form-item>
        <el-form-item label="提供单位:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.orgName"></el-input>
        </el-form-item>
        <el-form-item label="信息资源摘要:" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' clearable size="medium" :disabled="true" v-model="ruleForm.uviewDesc"></el-input>
        </el-form-item>
        <el-form-item label="挂接类型:" prop="type" :label-width="this.formLabelWidth">
          <el-select clearable v-model="ruleForm.type" placeholder="请选择挂接类型" size="medium" style="width:100%">
            <el-option label="文件" :value="0"></el-option>
            <el-option label="数据库" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <!-- 文件 -->
        <el-form-item v-if="ruleForm.type===0" label="上传文件:" prop="fileName" :label-width="this.formLabelWidth">
          <el-input v-show="false" size="medium" v-model="ruleForm.fileName"></el-input>
          <el-upload :action="uploadFileUrl" :data="{uviewId:ruleForm.uviewId}" :headers="headers" ref="uploadAvatar" :show-file-list="false" :on-success="handleFileSuccess" :before-upload="beforeFileUpload" :on-error="handleFileError">
            <el-button size="medium">上传文件</el-button>
            <span v-if="ruleForm.status!=0" id="text_fileName">{{ruleForm.fileName}}</span>
            <a v-if="ruleForm.status==0" :href="urlHerf+ruleForm.fileDownloadUri">{{ruleForm.fileName}}</a>
          </el-upload>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===0" label="描述:" :label-width="this.formLabelWidth">
          <el-input placeholder="描述" clearable size="medium" v-model="ruleForm.detail"></el-input>
        </el-form-item>
        <!-- 数据库 -->
        <el-form-item v-if="ruleForm.type===1" label="数据库ip:" prop="ip" :label-width="this.formLabelWidth">
          <el-input placeholder="数据库ip" clearable size="medium" v-model="ruleForm.ip"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库端口:" prop="port" :label-width="this.formLabelWidth">
          <el-input placeholder="数据库端口" clearable size="medium" v-model="ruleForm.port"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库账号:" prop="username" :label-width="this.formLabelWidth">
          <el-input :rows='4' maxlength='200' placeholder="数据库账号" clearable size="medium" v-model="ruleForm.username"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库密码:" prop="password" :label-width="this.formLabelWidth">
          <el-input placeholder="数据库密码" clearable size="medium" v-model="ruleForm.password"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="数据库备注:" :label-width="this.formLabelWidth">
          <el-input placeholder="数据库备注" clearable size="medium" v-model="ruleForm.detail"></el-input>
        </el-form-item>
        <el-form-item v-if="ruleForm.type===1" label="表名称:" prop="tableName" :label-width="this.formLabelWidth">
          <el-input placeholder="表名称" clearable size="medium" v-model="ruleForm.tableName"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="resetForm('ruleForm')">取 消</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">保存草稿</el-button>
          <el-button size="medium" type="primary" @click="submitToExamine('ruleForm')">提交审核</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import { getCookies } from '@/utils/auth';
import { archBusiUviewExFind } from "@/api/archBusiUviewEx.js";
import { assetExInsert, assetExInsertAndSubmit, assetExFind, updateUpdate, updateUpdateAndSubmit } from "@/api/fileData.js";
export default {
  data() {
    return {
      title: this.$route.query.title,
      ruleForm: {
        uviewNm: "",
        uviewNo: "",
        orgName: "",
        uviewDesc: "",
        fileName: "",
      },
      formLabelWidth: "120px",
      rules: {
        type: [{
          required: true,
          message: "请选择类型",
          trigger: "change",
        }],
        fileName: [{
          required: true,
          message: "请上传文件",
          trigger: "change",
        }],
        ip: [{
          required: true,
          message: "请输入Ip",
          trigger: "change",
        }],
        port: [{
          required: true,
          message: "请输入端口",
          trigger: "change",
        }],
        username: [{
          required: true,
          message: "请输入账号",
          trigger: "change",
        }],
        password: [{
          required: true,
          message: "请输入密码",
          trigger: "change",
        }],
        tableName: [{
          required: true,
          message: "请输入表名称",
          trigger: "change",
        }],
      },
      urlHerf: process.env.VUE_APP_BASE_API,
      // uploadFileUrl: process.env.VUE_APP_BASE_API + "/webadmin/file/upload",
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/webadmin/assetEx/upload",
      headers: {
        Authorization:
          "token " + JSON.parse(getCookies("userInfo")).token,
      },
    }
  },
  created() {
    if (this.$route.query.title === '新建申请') {
      this.resourceUseInfoDetail()
    } else {
      this.editDetail();
    }
  },
  methods: {
    // 编辑详情
    async editDetail() {
      const res = await assetExFind({ id: this.$route.query.id });
      this.loading = false;
      if (res.data.code === 1) {
        this.ruleForm = res.data.data;
        if (res.data.data.type === 0) {
          // this.ruleForm
          this.ruleForm.fileName = res.data.data.name;
          this.ruleForm.name = res.data.data.name;
          this.ruleForm.fileType = res.data.data.fileType;
          this.ruleForm.fileDownloadUri = res.data.data.fileDownloadUri;
        }
      } else {
        this.$message.error(res.data.msg);
        return false
      }
    },
    // 详情
    async resourceUseInfoDetail() {
      const that = this
      let data = { id: this.$route.query.uviewId }
      const response = await archBusiUviewExFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm.uviewNm = response.data.data.uviewNm;
        that.ruleForm.uviewNo = response.data.data.uviewNo;
        that.ruleForm.orgName = response.data.data.name;
        that.ruleForm.uviewDesc = response.data.data.uviewDesc;
        that.ruleForm.uviewId = response.data.data.uviewId;
        that.ruleForm.orgId = response.data.data.provOrgId * 1;

      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    // handleFileRemove() {
    //   this.ruleForm.fileDownloadUri = "";
    // },
    handleFileSuccess(res, file) {
      this.ruleForm.name = res.fileName;
      this.ruleForm.fileName = res.fileName;
      this.ruleForm.fileType = res.fileType;
      this.fileDownloadUri = URL.createObjectURL(file.raw);
      this.ruleForm.fileDownloadUri = file.response.fileDownloadUri;
      document.getElementById('text_fileName').innerHTML = res.fileName;
    },
    handleFileError(error) {
      this.$refs.uploadAvatar.clearFiles();
      this.$message.error(JSON.parse(error.message).msg);
    },
    beforeFileUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 10;
      if (!isLt2M) {
        this.$message.error("上传大小不能超过 10MB!");
      }
      return isLt2M;
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.$route.query.title === '新建申请') {
            assetExInsert(this.ruleForm).then((res) => {
              if (res.data.code === 1) {
                this.$message.success(res.data.msg);
                this.CloseModal();
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            updateUpdate(this.ruleForm).then((res) => {
              if (res.data.code === 1) {
                this.$message.success(res.data.msg);
                this.CloseModal();
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    submitToExamine(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.$route.query.title === '新建申请') {
            assetExInsertAndSubmit(this.ruleForm).then(res => {
              if (res.data.code === 1) {
                this.$message.success(res.data.msg);
                this.CloseModal();
              } else {
                this.$message.error(res.data.msg);
              }
            })
          } else {
            // 编辑
            updateUpdateAndSubmit(this.ruleForm).then(res => {
              if (res.data.code === 1) {
                this.$message.success(res.data.msg);
                this.CloseModal();
              } else {
                this.$message.error(res.data.msg);
              }
            })
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      })
    },
    resetForm(ruleForm) {
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      this.$router.push({
        path: '/submitList'
      })
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