<!--
 * @Author: Buter
 * @Date: 2020-12-08 19:00:16
 * @Description: ElementUI的密码输入框点击小眼睛显示密码,但是没有实现点击按钮换睁眼和闭眼的效果。这个组件基于ElementUi的input封装。
 * @FileName: DoPasswordInput.vue
-->
<template>
  <el-input maxlength='100' :type="inputType" :value="text" placeholder="请输入密码" @input="handlePasswordInput">
    <i slot="suffix" @click="showPwd" v-show="isShow">
      <!-- 打开眼睛的图标-->
      <svg v-if="isOpenEye" t="1607395171229" class="doPasswordInputIcon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="4309" width="200" height="200">
        <path
            d="M512 279.272727c171.985455 0 328.610909 162.909091 388.421818 232.727273-59.810909 69.818182-216.436364 232.727273-388.421818 232.727273S183.389091 581.818182 123.578182 512c59.810909-69.818182 216.436364-232.727273 388.421818-232.727273m0-69.818182C298.589091 209.454545 117.76 407.970909 56.785455 483.374545a44.916364 44.916364 0 0 0 0 57.25091C117.76 616.029091 298.589091 814.545455 512 814.545455s394.24-198.516364 455.214545-273.92a44.916364 44.916364 0 0 0 0-57.25091C906.24 407.970909 725.410909 209.454545 512 209.454545z"
            p-id="4310" fill="#dbdbdb"></path>
        <path d="M512 442.181818a69.818182 69.818182 0 1 1-69.818182 69.818182 69.818182 69.818182 0 0 1 69.818182-69.818182m0-69.818182a139.636364 139.636364 0 1 0 139.636364 139.636364 139.636364 139.636364 0 0 0-139.636364-139.636364z" p-id="4311" fill="#dbdbdb"></path>
      </svg>
      <!-- 闭上眼睛的图标-->
      <svg v-else t="1607395101404" class="doPasswordInputIcon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3877" width="200" height="200">
        <path
            d="M986.624 302.848c-130.92864 97.59744-304.07168 156.17536-466.58048 157.25568-161.5872 1.07008-336.72192-60.46208-466.58048-157.25568-26.50624-19.75808-51.98336 24.71936-25.84064 44.2112 43.93984 32.74752 93.76256 58.752 143.31392 81.69984 109.44 50.68288 228.11648 81.7408 349.1072 82.54464 118.71744 0.78848 235.64288-31.73376 343.29088-79.89248 51.6608-23.1168 103.59296-50.41152 149.13024-84.352 26.14784-19.49184 0.6656-63.96928-25.84064-44.2112z"
            p-id="3878" fill="#dbdbdb"></path>
        <path
            d="M363.58144 478.8992l-26.5472 157.71648c-5.43744 32.28672 43.89888 46.11584 49.37216 13.60896l26.5472-157.71648c5.43232-32.28672-43.89888-46.11584-49.37216-13.60896zM645.83168 492.50816l26.5472 157.71648c5.46816 32.50688 54.80448 18.67264 49.37216-13.60896l-26.5472-157.71648c-5.47328-32.50688-54.8096-18.67776-49.37216 13.60896zM119.84896 373.2736L31.5136 515.47136c-17.47456 28.12928 26.8288 53.81632 44.2112 25.84064l88.33536-142.19776c17.47456-28.12928-26.83392-53.81632-44.2112-25.84064zM850.62656 421.7088l88.33536 142.208c17.3824 27.97568 61.68576 2.28864 44.2112-25.84064l-88.33536-142.208c-17.3824-27.97568-61.68576-2.28864-44.2112 25.84064z"
            p-id="3879" fill="#dbdbdb"></path>
      </svg>
    </i>
  </el-input>
</template>

<script>
export default {
  name: "DoPasswordInput",
  data() {
    return {
      isOpenEye: false,
      isShow: false,
      inputType: "password"
    }
  },
  //https://cn.vuejs.org/v2/api/#model
  model: {
    prop: "text",
    event: "input"
  },
  props: {
    text: [String, Number]
  },
  methods: {
    showPwd() {
      this.isOpenEye = !this.isOpenEye
    },

    handlePasswordInput(val) {
      //v-model这里等同于 @input 加 :value
      this.$emit('input', val)
    },
  },
  watch: {
    isOpenEye: {
      handler() {
        if (this.isOpenEye) {
          this.inputType = "text"
        } else {
          this.inputType = "password"
        }
      }
    },//
    text:{
      handler() {
        this.isShow = (this.text)
      }
    }
  },
}
</script>

<style scoped>

/deep/ .doPasswordInputIcon {
  width: 1.2em;
  height: 1.2em;
  vertical-align: -0.20em;
  fill: currentColor;
  overflow: hidden;
}
</style>
