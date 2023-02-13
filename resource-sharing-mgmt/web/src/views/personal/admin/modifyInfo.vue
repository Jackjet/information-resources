<template>
  <div class="personal-info-page">
    <el-form label-position="left" label-width="120px" :model="userInfo" style="width: 500px">
      <el-form-item label="用户账号：">
        <el-input :disabled='true' v-model="userInfo.account"></el-input>
      </el-form-item>
      <el-form-item label="用户名：">
        <el-input v-model="userInfo.name"></el-input>
      </el-form-item>
      <el-form-item label="所属部门：">
        <el-cascader :disabled='true' v-model="userInfo.organizationId" :options="options" :props="props">
        </el-cascader>
      </el-form-item>
      <el-form-item label="手机号：">
        <el-input v-model="userInfo.phone"></el-input>
      </el-form-item>
      <el-form-item label="邮箱：">
        <el-input v-model="userInfo.email"></el-input>
      </el-form-item>
      <el-form-item label="身份证号：">
        <el-input v-model="userInfo.idCard"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">保 存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  data() {
    return {
      userInfo: {},
      options: [],
      props: {
        value: "id",
        label: "name",
        children: "children",
        checkStrictly: true,
      },
    };
  },
  mounted() {
    this.organizationFindAll().then((res) => {
      this.options.push(res.data);
    });
    let id = this.$store.state.user.id;
    this.webAdminUserFind({ id: id }).then((res) => {
      if (res.code === 1) {
        this.userInfo = res.data;
      }
    });
  },
  methods: {
    onSubmit() {
      if (typeof (this.userInfo.organizationId) == 'string') {
        console.log(this.userInfo.organizationId)
      } else {
        this.userInfo.organizationId =
          this.userInfo.organizationId[this.userInfo.organizationId.length - 1];
      }
      this.webAdminUserUpdate(this.userInfo).then((res) => {
        if (res.code === 1) {
          this.$message.success("修改成功！");
          this.$router.push("/personal/info");
        } else {
          this.$message.error(res.msg);
        }
      });
    },
  },
};
</script>
<style lang="scss" scope>
.personal-info-page {
  padding: 20px;
  box-sizing: border-box;
}
</style>