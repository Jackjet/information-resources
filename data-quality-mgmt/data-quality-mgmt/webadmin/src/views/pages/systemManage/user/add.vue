<template>
  <el-main class="main">
    <div>
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm" v-loading="loading">

        <el-form-item label="头像:" prop="avatar" :label-width="this.formLabelWidth">

          <!--新增or编辑-->
          <el-upload v-if="!disabled" class="avatar-uploader" :action="uploadAvatarUrl" :headers='headers' ref="uploadAvatar" :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload" :on-error="handleAvatarError">
            <el-image :src="avatar" class="avatar" />

            <span v-if="isShowAvatarOpt" class="el-upload-action">
              <span @click.stop="handleAvatarPreview">
                <i class="el-icon-zoom-in"></i>
              </span>

              <span @click.stop="handleAvatarRemove">
                <i class="el-icon-delete"></i>
              </span>
            </span>
          </el-upload>

          <!--详情-->
          <el-image v-else :src="avatar" class="avatar" @click="handleAvatarPreview" />

        </el-form-item>

        <el-form-item label="姓名:" :label-width="this.formLabelWidth" prop='name'>
          <el-input clearable maxlength='100' size="medium" :disabled="disabled" placeholder="请输入姓名" v-model="ruleForm.name" />
        </el-form-item>

        <el-form-item label="账号:" :label-width="this.formLabelWidth" prop='account'>
          <el-input maxlength='100' :placeholder="accountMsg" clearable size="medium" :disabled="disabledo" v-model="ruleForm.account" />
          <p style="color: red; padding: 0; margin: 10px 0px 0px 0px;">注：新用户初始密码是Tjhbq2020可以登录系统之后修改密码</p>
        </el-form-item>

        <el-form-item label="手机号:" :label-width="this.formLabelWidth" prop='phone'>
          <el-input maxlength='100' :placeholder="phoneMsg" clearable size="medium" :disabled="disabled" v-model="ruleForm.phone" />
        </el-form-item>

        <el-form-item label="身份证号:" :label-width="this.formLabelWidth" prop='idCard'>
          <el-input maxlength='100' placeholder="请输入身份证号" clearable size="medium" :disabled="disabled" v-model="ruleForm.idCard" />
        </el-form-item>

        <el-form-item label="邮箱:" :label-width="this.formLabelWidth" prop='email'>
          <el-input maxlength='100' :placeholder="emailMsg" clearable size="medium" :disabled="disabled" v-model="ruleForm.email" />
        </el-form-item>

        <el-form-item label="组织机构:" :label-width="this.formLabelWidth" prop='organizationId'>
          <el-cascader style="width: 100%;" :placeholder="organizationMsg" :props="props" :disabled="disabled" collapse-tags clearable v-model="ruleForm.organizationId" :options="organizationOptions" @change="handleChange" />
        </el-form-item>

        <el-form-item prop="roleId" label="角色权限:" :label-width="this.formLabelWidth" class='InpitWidth'>
          <el-select style="width: 100%;" v-model="ruleForm.roleId" :disabled='disabled' :placeholder="roleMsg">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>

        <el-form-item label="性别:" :label-width="this.formLabelWidth" prop='sex'>
          <el-select style="width: 100%;" v-model="ruleForm.sex" clearable :disabled='disabled' :placeholder="sexMsg">
            <el-option key='0' label="女" value='0' />
            <el-option key='1' label="男" value='1' />
          </el-select>
        </el-form-item>

        <el-form-item label="备注:" :label-width="this.formLabelWidth">
          <el-input maxlength='200' type="textarea" :rows="3" :placeholder="remarkMsg" clearable size="medium" :disabled="disabled" v-model="ruleForm.remark"></el-input>
        </el-form-item>

        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>

    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="avatar" alt="">
    </el-dialog>
  </el-main>

</template>

<script>
import { organizationFindAll } from "@/api/organization.js";
import { roleFindAll } from "@/api/role.js";
import { webAdminUserFind, webAdminUserInsert, webAdminUserUpdate } from "@/api/userMen.js";

let defaultAvatar = require('../../../../assets/image/user.png')

export default {
  data() {
    return {
      title: '新增',
      disabled: false,
      disabledo: false,
      props: { checkStrictly: true },
      ruleForm: {
        avatar: '',
        account: '',
        name: "",
        phone: '',
        sex: '0',
        organizationId: '',
        email: '',
        remark: '',
        roleId: 'default'
      },
      organizationOptions: [],
      options: [],
      formLabelWidth: "120px",
      accountMsg: '请输入账号,(字母，数字，字母+数字)',
      phoneMsg: '请输入手机号',
      emailMsg: '请输入邮箱',
      organizationMsg: '请选择组织机构',
      roleMsg: '请选择角色权限',
      sexMsg: '请选择性别',
      remarkMsg: '请输入备注',
      rules: {
        name: [{
          required: true,
          message: "请输入姓名",
          trigger: "change",
        }],
        organizationId: [{
          required: true,
          message: "请选择组织机构",
          trigger: "change",
        }],
        roleId: [{
          required: true,
          message: "请选择角色权限",
          trigger: "change",
        }],
        sex: [{
          required: true,
          message: "请选择性别",
          trigger: "change",
        }],
        account: [{
          required: true,
          message: "请输入账号",
          trigger: "change",
        }, {
          pattern: /^[0-9A-Za-z]{1,20}$/, //正则
          message: '请输入账号,(字母，数字，字母+数字)'
        }],
        phone: [{
          required: true,
          message: '请输入手机号',
          trigger: ['change', 'blur']
        }, {
          pattern: /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g,  //正则
          message: '请输入正确的手机号'
        }],
        idCard: [{
          required: true,
          message: '请输入身份证号',
          trigger: ['change', 'blur']
        }, {
          pattern: /^\d{17}([0-9]|x|X){1}$/,  //正则
          message: '请输入正确的身份证号'
        }],
        email: [{
          required: false,
          message: '请输入邮箱',
          trigger: ['change', 'blur']
        }, {
          pattern: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,  //正则
          message: '请输入正确的邮箱'
        }]
      },

      uploadAvatarUrl: process.env.VUE_APP_BASE_API + '/webadmin/system/webAdminUser/upload/users/avatar',
      headers: {
        Authorization: 'token ' + JSON.parse(sessionStorage.getItem("UserInfo")).token,
      },
      avatar: defaultAvatar, //头像
      dialogVisible: false,
      loading: false,
      isShowAvatarOpt: false, //是否显示头像操作层，默认头像就不显示
    }
  },
  created() {
    this.title = this.$route.query.type
    this.findorganizations()
    this.findroles()
    if (this.$route.query.type === '编辑') {
      this.disabledo = true
    } else if (this.$route.query.type === '详情') {
      this.disabledo = true
      this.disabled = true
      this.accountMsg = ''
      this.phoneMsg = ''
      this.emailMsg = ''
      this.organizationMsg = ''
      this.roleMsg = ''
      this.sexMsg = ''
      this.remarkMsg = ''
    }
  },
  methods: {
    handleAvatarRemove() {
      this.avatar = defaultAvatar;
      this.isShowAvatarOpt = false;
      this.ruleForm.avatar = "";
    },
    handleAvatarPreview() {
      if (this.disabled) { //如果是详情的时候，默认头像不能放大
        this.dialogVisible = !this.isDefaultAvatar();
      } else { //不是详情，有时候头像没上传完成，所有看不了大图，暂时先这么处理，做的好的话，可以加一个上传头像的进度条
        this.dialogVisible = true;
      }
    },
    handleAvatarSuccess(res, file) {
      this.avatar = URL.createObjectURL(file.raw);
      this.isShowAvatarOpt = true;
      this.ruleForm.avatar = file.response.fileDownloadUri;
    },
    handleAvatarError(error) {
      this.$refs.uploadAvatar.clearFiles();
      this.avatar = defaultAvatar;
      this.isShowAvatarOpt = false;
      this.$message.error(JSON.parse(error.message).msg)
    },
    beforeAvatarUpload(file) {
      // const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;

      // if (!isJPG) {
      //   this.$message.error('上传头像图片只能是 JPG 格式!');
      // }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    //是否为默认头像
    isDefaultAvatar() {
      console.log(this.avatar)
      return !(this.avatar && this.avatar.length > 0 && (this.avatar.startsWith("http://") || this.avatar.startsWith("https://") || this.avatar.startsWith("./")));
    },
    // 详情
    async userDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      that.loading = true
      const response = await webAdminUserFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data
        that.ruleForm.sex = that.ruleForm.sex === 0 ? '0' : '1'
        if (this.ruleForm.avatar) {
          that.avatar = process.env.VUE_APP_BASE_API + "/" + this.ruleForm.avatar;
        }
        that.isShowAvatarOpt = !this.isDefaultAvatar();
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    handleChange() {
    },
    submitForm(ruleForm) {
      const that = this
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          that.loading = true;

          let data = that.ruleForm
          if (Array.isArray(data.organizationId) && data.organizationId.length > 1) {
            data.organizationId = data.organizationId.pop()
          } else if (Array.isArray(data.organizationId) && data.organizationId.length === 1) {
            data.organizationId = data.organizationId[0]
          }

          if (that.$route.query.type === '编辑') {
            // 编辑
            webAdminUserUpdate(data).then((res) => {
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
            webAdminUserInsert(data).then((res) => {
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
      this.$router.push('/user')
    },
    async findorganizations(data) {
      const that = this
      that.loading = true
      const response = await roleFindAll()
      that.loading = false
      if (response.data.code === 1) {
        that.options = []
        response.data.data.content.map(function (v, k) {
          that.options.push({ value: v.id, label: v.name })
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    async findroles(data) {
      const that = this
      that.loading = true
      const response = await organizationFindAll()
      that.loading = false
      if (response.data.code === 1) {
        let arrData = []
        arrData.push(response.data.data)
        that.organizationOptions = this.getJsonTree(arrData, '')
        if (this.$route.query.type !== '新增') {
          this.userDetail()
        }
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    getJsonTree(data, parentId) {
      let itemArr = []
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
