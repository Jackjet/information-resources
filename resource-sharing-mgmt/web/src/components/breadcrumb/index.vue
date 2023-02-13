<template>
  <div class="common-breadcrumb">
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }"><img style="width:14px;" src="@/assets/image/ziyuan/首页.svg" alt=""> 首页</el-breadcrumb-item>
      <el-breadcrumb-item v-for="(item, index) in returnRouter" :key="index"><a @click="rouetrClick(item.path)">{{ item.name }}</a></el-breadcrumb-item>
    </el-breadcrumb>
  </div>
</template>
<script>
export default {
  props: {
    returnRouter: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
  methods: {
    rouetrClick(path) {
      if (path == '') {
        // console.log(index,this.returnRouter);
      } else {
        // this.$router.go(-1);
        this.$router.push(path);
      }
    },
  },
};
</script>
<style lang="scss" scope>
.common-breadcrumb {
  height: 60px;
  display: flex;
  align-items: center;
  padding-left: 50px;
}
.el-breadcrumb__item {
  float: none;
}
</style>