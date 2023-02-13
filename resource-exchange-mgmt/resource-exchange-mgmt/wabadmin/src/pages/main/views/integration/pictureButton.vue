<template>
    <div style="display: inline-block; width: auto;height: auto; cursor: pointer; margin: 0 15px;">
      <template v-if="type === 'add'">
        <el-image :src="require('@/assets/image/icons/node-collapse.png')"></el-image>
      </template>
      <template v-else-if="type === 'delete'">
        <el-image :src="require('@/assets/image/icons/node-expand.png')"></el-image>
      </template>
      <template v-else-if="type === 'install'">
        <el-image :src="require('@/assets/image/icons/install.png')"></el-image>
      </template>
      <template v-else-if="type === 'info'">
        <el-image :src="require('@/assets/image/icons/info.png')"></el-image>
      </template>
      <template v-else-if="type === 'setting'">
        <el-image :src="require('@/assets/image/icons/配置 (1).png')"></el-image>
      </template>
      <template v-else-if="type === 'start'">
        <el-image :src="require('@/assets/image/icons/开始.png')"></el-image>
      </template>
      <template v-else-if="type === 'stop'">
        <el-image :src="require('@/assets/image/icons/停止.png')"></el-image>
      </template>
      <template v-else-if="type === 'logger'">
        <el-image :src="require('@/assets/image/icons/日志.png')"></el-image>
      </template>
      <template v-else-if="type === 'edit'">
        <el-image :src="require('@/assets/image/icons/edit.png')"></el-image>
      </template>
      <template v-else-if="type === 'reset'">
        <el-image :src="require('@/assets/image/icons/重置.png')"></el-image>
      </template>
      <template v-else-if="type === 'debug'">
        <el-image :src="require('@/assets/image/icons/配置调试.png')"></el-image>
      </template>
      <template v-else-if="type === 'accessControl'">
        <el-image :src="require('@/assets/image/icons/访问控制.png')"></el-image>
      </template>
      <template v-else-if="type === 'develop'">
        <el-image :src="require('@/assets/image/icons/开发.png')"></el-image>
      </template>
      <template v-else-if="type === 'view'">
        <el-image :src="require('@/assets/image/icons/查看.png')"></el-image>
      </template>
      <template v-else-if="type === 'check'">
        <el-image :src="require('@/assets/image/icons/check.png')"></el-image>
      </template>
      <div style="white-space:nowrap; color: #1296db;">{{ label }}</div>
    </div>
</template>

<script>
export default {
  props: {
    type: '',
    label: ''
  }
}
</script>

<style scoped>
.el-image {
  width: 20px;
  height: 20px;
  float: left;
  margin-top: 2px;
}
</style>
