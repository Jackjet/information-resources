<template>
  <div class="personal-info-page">
    <el-form :model="editPassword" :rules="rules" ref="editPassword" style="width: 50%;margin:100px auto;">
      <el-form-item label="原密码" label-width="120px" prop="oldPassword">
        <el-input type="password" v-model="editPassword.oldPassword" placeholder="请输入原密码"></el-input>
      </el-form-item>
      <el-form-item label="新密码" label-width="120px" prop="newPassword">
        <el-input type="password" v-model="editPassword.newPassword" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" label-width="120px" prop="yesPassword">
        <el-input type="password" v-model="editPassword.yesPassword" placeholder="请输入新密码"></el-input>
      </el-form-item>
      <el-form-item style="text-align: center;">
        <el-button type="primary" @click="editPasswordClick('editPassword')">确 定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import Md5 from 'js-md5';
import { removeToken } from '@/utils/storage.js';
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'));
      } else {
        if (this.editPassword.yesPassword !== '') {
          this.$refs.editPassword.validateField('yesPassword');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.editPassword.newPassword) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      editPassword: {
        oldPassword: "",
        newPassword: "",
      },
      rules: {
        oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
        newPassword: [{ required: true, validator: validatePass, trigger: 'blur' }],
        yesPassword: [{ required: true, validator: validatePass2, trigger: 'blur' }]
      }
    };
  },
  methods: {
    editPasswordClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.updatePassword({
            id: this.$store.state.user.id,
            oldPassword: Md5(this.editPassword.oldPassword),
            newPassword: this.editPassword.newPassword
          }).then(res => {
            if (res.code === 1) {
              this.$confirm('密码修改成功，是否重新登录？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                this.webAdminSignOut({}).then((res) => {
                  if (res.code === 1) {
                    removeToken();
                    window.location.href = "login.html?url=" + location.href;
                  }
                });
              }).catch(() => { });
            } else {
              this.$message.error(res.msg);
            }
          })
        } else {
          return false;
        }
      });

    },
  },
};
</script>
<style lang="scss" scope>
.personal-info-page {
  margin: 20px 40px;
}
</style>