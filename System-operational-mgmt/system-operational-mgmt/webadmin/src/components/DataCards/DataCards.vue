
<template>
  <div class="data-cards">
    <el-row :span="24">
      <el-col v-for="(item,index) in data" :key="index" :span="span" >
        <div  class="item">
          <el-card :body-style="{ padding: '0px' }">
            <!-- <div class="over" v-if="state === 'pending'">{{cellText}}</div>
            <div class="over" v-if="statee === 'reject'">{{cellText1}}</div> -->
            <el-image :src="item.src" class="image" :lazy='true'  >
              <div slot="placeholder" class="image-slot">
                <i class="el-icon-loading"></i>
              </div>
              <div slot="error" class="image-slot">
                <i class="el-icon-picture-outline"></i>
              </div>
            </el-image>
            <!-- <img :src="item.src" class="image"  @load.prevent="onImageResolve()" @error.prevent="onImageReject()"> -->
            <div style="padding: 14px;">
              <span>{{item.name}}</span>
              <div v-for="(itemkey,index) in item.progress" :key="index" class="bottom clearfix" >
                <p>{{itemkey.name}}</p>
                <el-progress :percentage="itemkey.porss"></el-progress>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  name: 'DataCards',
  data() {
    return {
      state: 'pending',
      statee: 'pending',
      span: this.option.span || 8,
      data: this.option.data || []
    }
  },
  props: {
    option: {
      type: Object,
      default: () => {}
    }
  },
  computed: {
    cellText() {
      const { state } = this
      let message = '加载中'
      switch (state) {
        case 'pending': message = '加载中'
          break
        case 'reject': message = '加载失败'
          break
        case 'resolve': message = '加载成功'
          break
      }
      return message
    },
     cellText1() {
      const { statee } = this
      let message = '加载中'
      switch (statee) {
        case 'pending': message = '加载中'
          break
        case 'reject': message = '加载失败'
          break
        case 'resolve': message = '加载成功'
          break
      }
      return message
    }
  },
  methods: {
    // 图片加载中
    onImageResolve() {
      this.state = 'resolve'
      this.statee = 'resolve'
    // this.$emit('resolve', this.state)
    },
    // 图片加载失败
    onImageReject() {
      this.state = 'reject' // 错误
      this.statee = 'reject' // 错误
      // this.$emit('reject', this.state)
    },

  }
}
</script>

<style lang="scss" scoped>
    @import '../styles/data-cards.scss';
</style>
