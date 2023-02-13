<template>
  <div class="personal-DemandDetails-page">
    <h2>需求详情</h2>
    <ul>
      <h3>需求详情</h3>
      <li>需求标题：{{ detail.title }}</li>
      <li>受理单位：{{ detail.acceptDept }}</li>
      <li>
        需求类型：{{
          detail.requestType === "0"
            ? "资源目录变更"
            : detail.requestType === "1"
            ? "资源目录新增"
            : detail.requestType === "2"
            ? "资源数据变更"
            : "资源数据新增"
        }}
      </li>
      <li>需求描述：{{ detail.describe }}</li>
      <li>联系人：{{ detail.contacts }}</li>
      <li>联系电话：{{ detail.mobilePhone }}</li>
      <li>期望解决时间：{{ detail.expectTime }}</li>
      <li>电子邮件：{{ detail.email }}</li>
      <li>
        附件：<a style="display: block;" v-for="(item, index) in detail.fileUrl" :href="url+item.url" :key="index">{{ item.name }}</a>
      </li>
      <li>提交人：{{ detail.createByName }}</li>
      <li>提交部门：{{ detail.createDeptName }}</li>
      <li>提交时间：{{ detail.createTime }}</li>
      <h3>需求受理</h3>
      <li>
        受理状态：
        {{
          detail.status === "0"
            ? "未受理"
            : detail.status === "1"
            ? "已受理"
            : "已驳回"
        }}
      </li>
      <li>受理意见：{{ detail.opinion }}</li>
      <li>受理时间：{{ detail.updateTime }}</li>
    </ul>
    <el-button class="button" @click="returnClick">返 回</el-button>
  </div>
</template>
<script>
import config from "@/config/index.js";
export default {
  data() {
    return {
      detail: {},
      url: config.baseURL,
    };
  },
  mounted() {
    this.demandedInfoFindById({ id: this.$route.query.id }).then((res) => {
      if (res.code === 1) {
        this.detail = res.data;
        this.detail.expectTime = this.detail.expectTime.split(' ')[0]
        this.detail.fileUrl = JSON.parse(this.detail.fileUrl);
      } else {
        this.$message.error(res.mag);
      }
    });
  },
  methods: {
    returnClick() {
      this.$router.push("/personal/demand");
    },
  },
};
</script>
<style lang="scss" scope>
.personal-DemandDetails-page {
  padding-bottom: 30px;
  h2 {
    text-align: center;
    line-height: 50px;
    border-bottom: 1px solid #ccc;
  }
  ul {
    margin: 20px 40px;
    h3 {
      display: inline-block;
      color: #8080ff;
      line-height: 40px;
      border-bottom: 1px solid #8080ff;
    }
    li {
      line-height: 40px;
    }
  }
  ul::-webkit-scrollbar {
    display: none;
    width: 0;
  }
  .button {
    margin-left: 40px;
  }
}
</style>