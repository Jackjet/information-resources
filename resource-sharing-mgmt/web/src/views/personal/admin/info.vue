<template>
  <div class="personal-info-page">
    <h2><span>个人信息</span></h2>
    <ul>
      <li><span>用户账号：</span> {{ userInfo.account }}</li>
      <li><span>用户名：</span> {{ userInfo.name }}</li>
      <li><span>APIkey：</span>{{ key }}</li>
      <li><span>所属部门：</span>{{ userInfo.organizationName }}</li>
      <li><span>手机号：</span>{{ userInfo.phone }}</li>
      <li><span>身份证号：</span>{{ userInfo.idCard }}</li>
      <li><span>邮箱：</span>{{ userInfo.email }}</li>
    </ul>
    <!-- <el-button v-if='userInfo.account != "admin"' class="button" type="primary" @click="rouetrClick">修 改</el-button> -->
  </div>
</template>
<script>
export default {
  data() {
    return {
      userInfo: {},
      key: "",
    };
  },
  mounted() {
    let id = this.$store.state.user.id;
    this.webAdminUserFind({ id: id }).then((res) => {
      if (res.code === 1) {
        this.userInfo = res.data;
      }
    });
    this.findKeyByUserId().then((res) => {
      if (res.code === 1) {
        this.key = res.data;
      } else {
        this.$message.error(res.msg);
      }
    });
  },
  methods: {
    rouetrClick() {
      this.$router.push("/personal/modifyInfo");
    },
  },
};
</script>
<style lang="scss" scope>
.personal-info-page {
  h2 {
    line-height: 60px;
    border-bottom: 1px solid #eee;
    padding-left: 60px;
    span {
      color: #0080ff;
      font-size: 20px;
      border-bottom: 2px solid #0080ff;
      display: inline-block;
      line-height: 60px;
    }
  }
  ul {
    margin: 20px 40px;
    li {
      height: 40px;
      display: flex;
      align-items: center;
      span {
        width: 110px;
        text-align: right;
        margin-right: 20px;
      }
    }
  }
  .button {
    margin-left: 40px;
  }
}
</style>