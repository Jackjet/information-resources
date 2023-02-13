<template>
  <div class="examples-box">
    <ul>
      <li>{{ examplesData }}</li>
    </ul>
  </div>
</template>
<script>
export default {
  props: {
    examplesData: {
      type: String,
      default: () => {
        return "";
      },
    },
  },
};
</script>
<style lang="scss" scope>
.examples-box {
  border: 1px solid #eee;
  padding: 20px;
}
</style>