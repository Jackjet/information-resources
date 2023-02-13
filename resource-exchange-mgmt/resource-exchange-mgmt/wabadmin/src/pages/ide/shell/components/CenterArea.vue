<!--
 * @Author: your name
 * @Date: 2020-05-28 16:59:10
 * @LastEditTime: 2020-05-30 18:19:20
 * @LastEditors: Please set LastEditors
 * @Description: In User Settings Edit
 * @FilePath: \d1.project.dcrun.center.webadmin\src\pages\ide\shell\components\CenterArea.vue
-->
<template>
  <div class="center-area">
    <div class="top-area">
      <slot name="top"></slot>
    </div>
    <BottomArea>
      <slot name="bottom"></slot>
    </BottomArea>
    <!--    <YHandle @widthChange="heightChange"></YHandle>-->
  </div>
</template>
<script>
import YHandle from './YHandle.vue'
import BottomArea from './BottomArea.vue'

export default {
  name: 'CenterArea',
  components: {
    YHandle,
    BottomArea
  },
  data() {
    return {
      height: 680,
      sizeClass: ''
    }
  },
  created() {
  },
  methods: {
    heightChange(movement) {
      console.log(this.height)
      this.height += movement
      if (this.height <= 60) {
        this.height = 60
      }
      if (this.height >= 600) {
        this.height = 600
      }
    }
  }
}
</script>

<style>
.center-area {
  /*overflow: hidden;*/
}
</style>
