<template>
  <div>
    <el-dialog :title="title" width="30%" :visible.sync="isShow" :before-close='closeDialog'>
      <slot name="form"></slot>
      <div slot="footer" class="dialog-footer" style="float: right;">
        <el-button @click="closeDialog" icon="el-icon-close">取 消</el-button>
        <el-button type="primary" @click="save()" icon="el-icon-check">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    // 模态框头
    title: {
      type: String,
      default: (e) => {
      }
    },
    // 显示模态框
    isShow: {
      type: Boolean,
      default: (e) => {
      }
    }
  },
  methods: {
    // 关闭模态框
    closeDialog () {
      this.$emit('closeDialog', '')
    },
    save () {
      this.$emit('saveDialog', '')
    }
  }
}
</script>

<style scoped lang="scss">
</style>
