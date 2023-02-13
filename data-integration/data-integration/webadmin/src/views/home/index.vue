<!--
 * 默认主页
-->
<template>
  <main class="main-box">
    <div class="main-img">
    </div>
  </main>
</template>

<script>
export default {
  name: "Dashboard"
}
</script>
<style lang="scss" scoped>
.main-box {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  .main-img {
    width: 678px;
    height: 493px;
    background: url('../../assets/image/home.png') no-repeat;
    background-size: 100%;
  }
}
</style>
