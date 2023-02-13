<template>
  <div class="data-display">
    <el-row :span="24">
      <el-col v-for="(item,index) in data" :key="index" :span="span">
        <div class="item" :style="{color:color}">
           <h5 class="count">{{item.count}}</h5>
               <span class="splitLine" />
           <p class="title">{{item.title}}</p>
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: 'DataDisplay',
  data() {
    return {
      span: this.option.span || 6,
      data: this.option.data,
      color: this.option.color || 'rgb(63, 161, 255)'
    }
  },
  props: {
    option: {
      type: Object,
      default: () => {}
    }
  },
  created() {},
  methods: {}
}
</script>
<style lang="scss" scoped>
    @import '../styles/data-display.scss';
</style>
