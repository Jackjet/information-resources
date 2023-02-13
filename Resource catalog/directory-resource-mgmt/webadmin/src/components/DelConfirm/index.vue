<template>
  <section>
    <el-container style="flex-wrap: wrap;">
      <div class="icon"><svg-icon :icon-class="iconClass" class-name="" style="width: 54px;height: 54px" /></div>
      <div class="tip_str">{{ tipStr }}</div>
    </el-container>
    <div slot="footer" class="dialog-footer" align="center">
      <el-button type="primary" class="add" @click="confirmDel">确定</el-button>
      <el-button class="remove" @click="cancelDel">取 消</el-button>
    </div>
  </section>
</template>
<script>

export default {
  name: 'DelConfirm', // 这个LoginName最好和引入的vue的LoginName相同
  props: {
    tipStr: {
      required: false,
      type: String,
      default: '确认删除么'
    },
    iconClass: {
      required: false,
      type: String,
      default: 'del_confirm'
    }
  },
  methods: {
    cancelDel() {
      this.$emit('cancelDel')
    },
    confirmDel() {
      this.$emit('confirmDel')
    }
  }
}
</script>
<style scoped>
  .dialog-footer{
    height: 56px;
    line-height: 56px;
    background:rgba(251,251,251,1);
    border:1px solid rgba(230,229,234,1);
    opacity:1;
    border-radius:0px 0px 6px 6px;
  }
  .tip_str{
    width: 100%;
    text-align: center;
    font-size:14px;
    font-family:Microsoft YaHei;
    font-weight:400;
    line-height:50px;
    color:rgba(36,36,36,1);
    margin-bottom: 5px;
  }
  .icon{
    margin-top: 35px;
    width: 100%;
    text-align: center;
  }
</style>
