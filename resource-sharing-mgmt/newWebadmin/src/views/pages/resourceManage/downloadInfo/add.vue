<template>
  <el-main class="main">
    <div>
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm" v-loading="loading">

        <el-form-item label="上传文件:" prop="title" :label-width="this.formLabelWidth">
          <!--新增or编辑-->
          <el-upload :action="uploadFileUrl" :headers="headers" ref="uploadAvatar" :show-file-list="false" :on-success="handleFileSuccess" :before-upload="beforeFileUpload" :on-error="handleFileError">
            <!--<el-image :src="avatar" class="avatar"/>-->
            <el-button size="medium">上传文件</el-button>
            <span v-if="isShowAvatarOpt" class="el-upload-action">
              <!--<span @click.stop="handleAvatarPreview">-->
              <!--<i class="el-icon-zoom-in"></i>-->
              <!--</span>-->

              <span @click.stop="handleFileRemove">
                <i class="el-icon-delete"></i>
              </span>
            </span>
          </el-upload>
          <!--详情-->
          <!--<el-image v-else :src="avatar" class="avatar" @click="handleAvatarPreview"/>-->
        </el-form-item>

        <el-form-item label="文件名称:" :label-width="this.formLabelWidth">
          <div>{{ ruleForm.title }}</div>
          <!--<el-input-->
          <!--clearable-->
          <!--maxlength="100"-->
          <!--size="medium"-->
          <!--:disabled="disabled"-->
          <!--placeholder=""-->
          <!--v-model="ruleForm.title"-->
          <!--/>-->
        </el-form-item>

        <el-form-item label="文件大小:" :label-width="this.formLabelWidth" prop="account">
          <div v-if="typeof ruleForm.fileSize==='number'">{{ (ruleForm.fileSize / 1024).toFixed(2) }} KB</div>
          <div v-else>{{ ruleForm.fileSize}}</div>
          <!--<el-input-->
          <!--maxlength="100"-->
          <!--clearable-->
          <!--size="medium"-->
          <!--:disabled="disabled"-->
          <!--v-model="ruleForm.fileSize"-->
          <!--/>-->
        </el-form-item>

        <!-- <el-form-item label="更新周期:" :label-width="this.formLabelWidth" prop="updateCyc">
          <el-select
            style="width: 100%"
            v-model="ruleForm.updateCyc"
            clearable
            placeholder="请选择更新周期"
          >
            <el-option key="0" label="每周" value="0" />
            <el-option key="1" label="每月" value="1" />
            <el-option key="2" label="半年" value="2" />
            <el-option key="3" label="每年" value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="所属部门:" :label-width="this.formLabelWidth" prop="provOrgId">
          <el-cascader
            :disabled='disabled1'
            style="width: 100%"
            :placeholder="organizationMsg"
            :props="props"
            collapse-tags
            clearable
            v-model="ruleForm.provOrgId"
            :options="organizationOptions"
          />
        </el-form-item> -->

        <el-form-item label="文件类型:" :label-width="this.formLabelWidth" prop="type">
          <el-select v-model="ruleForm.type" clearable placeholder="请选择文件类型">
            <el-option label="通知公告" :value="1" />
            <el-option label="资料下载" :value="2" />
            <el-option label="技术规范" :value="3" />
          </el-select>
        </el-form-item>

        <el-form-item label="文件描述:" :label-width="this.formLabelWidth">
          <el-input maxlength="200" type="textarea" :rows="3" placeholder="请输入文件描述" clearable size="medium" v-model="ruleForm.fileDesc"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" @click="submitForm('ruleForm')">确 定</el-button>
        </div>
      </el-form>
    </el-col>

    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="avatar" alt="" />
    </el-dialog>
  </el-main>
</template>

<script>
import { organizationFindAll } from "@/api/organization.js";
import { downloadInfoInsert } from "@/api/downloadInfo.js";
import { downloadInfo, downloadUpdate } from "@/api/userMen.js";
import { getCookies } from '@/utils/auth';
export default {
  data() {
    return {
      title: "新增",
      disabled: true,
      disabled1: false,
      props: { checkStrictly: true },
      ruleForm: {
        type: "",
        title: "",
        fileName: '',
        fileSize: "",
        fileType: '',
        fileDesc: "",
      },
      rules: {
        provOrgId: [{
          required: true,
          message: "请选择所属部门",
          trigger: "change",
        }],
        updateCyc: [{
          required: true,
          message: "请选择更新周期",
          trigger: "change",
        }],
        title: [{
          required: true,
          message: "请上传文件",
          trigger: "change",
        }],
        type: [
          {
            required: true,
            message: "请上选择文件类型",
            trigger: "change",
          }
        ]
      },

      organizationOptions: [],
      organizationMsg: "请选择所属部门",
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/webadmin/file/upload",
      headers: {
        Authorization:
          "token " + JSON.parse(getCookies("userInfo")).token,
      },
      loading: false,
    };
  },
  created() {
    if (this.$route.query.type == '编辑') {
      this.title = "编辑";
      this.userDetail()
    }
    // this.findorganizations();
  },
  methods: {
    // 详情
    async userDetail() {
      let id = this.$route.query.id
      const that = this
      let data = { id: id }
      const response = await downloadInfo(data)
      if (response.data.code === 1) {
        this.ruleForm = response.data.data
        console.log(response)
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    handleFileRemove() {
      this.ruleForm.fileDownloadUri = "";
    },
    handleFileSuccess(res, file) {
      this.ruleForm.title = res.fileName;
      this.ruleForm.fileName = res.fileName;
      this.ruleForm.fileSize = res.size;
      this.ruleForm.fileType = res.fileType;
      this.fileDownloadUri = URL.createObjectURL(file.raw);
      this.ruleForm.fileDownloadUri = file.response.fileDownloadUri;
    },
    handleFileError(error) {
      this.$refs.uploadAvatar.clearFiles();
      this.$message.error(JSON.parse(error.message).msg);
    },
    beforeFileUpload(file) {
      const isLt2M = file.size / 1024 / 1024;
      if (isLt2M > 200) {
        this.$message.error("上传大小不能超过 200M!");
      }
      return isLt2M;
    },
    submitForm(ruleForm) {
      const that = this;
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          that.loading = true;
          if (this.$route.query.type == '编辑') {
            let data = {}
            data.fileDesc = that.ruleForm.fileDesc
            data.fileDownloadUri = that.ruleForm.fileDownloadUri
            data.fileName = that.ruleForm.fileName
            data.fileSize = that.ruleForm.fileSize
            data.fileType = that.ruleForm.fileType
            data.type = that.ruleForm.type
            data.title = that.ruleForm.title
            data.id = that.ruleForm.id
            // 新增
            downloadUpdate(data).then((res) => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success("编辑成功");
                that.goBack();
              } else {
                that.$message.error(res.data.msg);
              }
            });
          } else {
            let data = that.ruleForm;
            // 新增
            downloadInfoInsert(data).then((res) => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success("新增成功");
                that.goBack();
              } else {
                that.$message.error(res.data.msg);
              }
            });
          }

        } else {
          return false;
        }
      });
    },
    goBack() {
      this.$router.push("/downloadInfo");
    },
    async findorganizations(data) {
      const that = this;
      that.loading = true;
      const response = await organizationFindAll();
      that.loading = false;
      if (response.data.code === 1) {
        let arrData = [];
        arrData.push(response.data.data);
        that.organizationOptions = this.getJsonTree(arrData, "");
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false;
      }
    },
    getJsonTree(data, parentId) {
      let itemArr = [];
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId) {
          let newNode = {};
          newNode.value = node.id;
          newNode.label = node.name;
          if (node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode);
        }
      }
      return itemArr;
    },
  },
};
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

    .avatar-uploader {
      width: 64px;
      height: 64px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .avatar-uploader-icon {
      box-sizing: border-box;
      font-size: 28px;
      color: #b7b7b7;
      width: 64px;
      height: 64px;
      line-height: 64px;
      text-align: center;
      &:hover {
        border: 1px dashed #b7b7b7;
        color: #fff;
        background-color: rgba(0, 0, 0, 0.3);
      }
    }
    .avatar {
      position: relative;
      width: 64px;
      height: 64px;
      display: block;
      border-radius: 50%;
    }
    .el-upload-action {
      position: absolute;
      top: 0;
      left: 0;
      display: block;
      width: 100%;
      height: 100%;
      font-size: 0;
      color: #fff;
      text-align: center;
      line-height: 64px;
      &:hover {
        font-size: 20px;
        background-color: #000;
        background-color: rgba(0, 0, 0, 0.3);
        border-radius: 50%;
      }
    }
  }
}
.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}
</style>
