<template>
  <div class="slide-verify" id="slideVerify" :style="widthlable" onselectstart="return false;">
    <canvas :width="w" :height="h" ref="canvas"></canvas>
    <canvas :width="w" :height="h" ref="block" class="slide-verify-block"></canvas>
    <div @click="refresh" class="slide-verify-refresh-icon"></div>
    <div class="slide-verify-slider" :style="widthlable" :class="{'container-active': containerActive, 'container-success': containerSuccess, 'container-fail': containerFail}">
      <div class="slide-verify-slider-mask" :style="{width: sliderMaskWidth}">
        <!-- slider -->
        <div @mousedown="sliderDown"
             @touchstart="touchStartEvent"
             @touchmove="touchMoveEvent"
             @touchend="touchEndEvent"
             class="slide-verify-slider-mask-item"
             :style="{left: sliderLeft}">
          <div :class="{'slide-verify-slider-mask-item-icon': slideTrue, 'slide-verify-sliders': slideFalse}"></div>
        </div>
      </div>
      <span class="slide-verify-slider-text">{{ sliderText }}</span>
    </div>
  </div>
</template>
<script>
const PI = Math.PI;

function sum(x, y) {
  return x + y
}

function square(x) {
  return x * x
}

import dragVerify from 'vue-drag-verify'
export default {
  name: 'DoSlideVerify',
  components: { dragVerify },
  props: {
    l: {
      type: Number,
      default: 42,
    },
    // block radius
    r: {
      type: Number,
      default: 10,
    },
    // canvas width
    w: {
      type: Number,
      default: 310,
    },
    // canvas height
    h: {
      type: Number,
      default: 155,
    },
    block_x: {
      type: Number,
      default: 155,
    },
    block_y: {
      type: Number,
      default: 20,
    },
    sliderText: {
      type: String,
      default: '向右滑动填充拼图',
    },
    imgurl: {
      type: String,
      default: '',
    },
    miniimgurl: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      slideTrue: true,
      slideFalse: false,
      containerActive: false, // container active class
      containerSuccess: false, // container success class
      containerFail: false, // container fail class
      canvasCtx: null,
      blockCtx: null,
      block: null,
      canvasStr: null,
      //block_x: undefined, // container random position
      // block_y: undefined,
      L: this.l + this.r * 2 + 3, // block real lenght
      img: undefined,
      originX: undefined,
      originY: undefined,
      isMouseDown: false,
      trail: [],
      widthlable: '',
      sliderLeft: 0, // block right offset
      sliderMaskWidth: 0, // mask width
    }
  },
  mounted() {
    //随机生成数this.block_x =
    this.init()
  },
  methods: {
    init() {
      this.initDom()
      this.bindEvents()
      this.widthlable = 'width:' + this.w + 'px;'
    },
    initDom() {
      this.block = this.$refs.block;
      this.canvasStr = this.$refs.canvas;
      this.canvasCtx = this.canvasStr.getContext('2d')
      this.blockCtx = this.block.getContext('2d')
    },
    initImg(h) {
      var that = this
      const img = document.createElement('img');
      img.onload = onload;
      img.onerror = () => {
        img.src = that.imgurl
      }
      img.src = that.imgurl
      img.onload = function () {
        that.canvasCtx.drawImage(img, 0, 0, that.w, that.h)
      }

      this.img = img
      const img1 = document.createElement('img');
      var blockCtx = that.blockCtx
      var block_y = h || that.block_y
      img1.onerror = () => {
        img1.src = that.miniimgurl
      }
      img1.src = that.miniimgurl
      img1.onload = function () {
        blockCtx.drawImage(img1, 0, block_y, 61, 61)
      }
    },
    refresh() {
      this.$emit('refresh')
    },
    sliderDown(event) {
      this.originX = event.clientX;
      this.originY = event.clientY;
      this.isMouseDown = true;
    },
    touchStartEvent(e) {
      this.originX = e.changedTouches[0].pageX;
      this.originY = e.changedTouches[0].pageY;
      this.isMouseDown = true;
    },
    bindEvents() {
      document.addEventListener('mousemove', (e) => {
        if (!this.isMouseDown) return false;
        const moveX = e.clientX - this.originX;
        const moveY = e.clientY - this.originY;
        if (moveX < 0 || moveX + 38 >= this.w) return false;
        this.sliderLeft = moveX + 'px';
        let blockLeft = (this.w - 40 - 20) / (this.w - 40) * moveX;
        this.block.style.left = blockLeft + 'px';
        this.containerActive = true; // add active
        this.sliderMaskWidth = moveX + 'px';
        this.trail.push(moveY);
      });
      document.addEventListener('mouseup', (e) => {
        if (!this.isMouseDown) return false
        this.isMouseDown = false
        if (e.clientX === this.originX) return false;
        this.containerActive = false; // remove active
        this.verify()
      })
    },
    touchMoveEvent(e) {
      if (!this.isMouseDown) return false;
      const moveX = e.changedTouches[0].pageX - this.originX;
      const moveY = e.changedTouches[0].pageY - this.originY;
      if (moveX < 0 || moveX + 38 >= this.w) return false;
      this.sliderLeft = moveX + 'px';
      let blockLeft = (this.w - 40 - 20) / (this.w - 40) * moveX;
      this.block.style.left = blockLeft + 'px';
      this.containerActive = true;
      this.sliderMaskWidth = moveX + 'px';
      this.trail.push(moveY);
    },
    touchEndEvent(e) {
      if (!this.isMouseDown) return false
      this.isMouseDown = false
      if (e.changedTouches[0].pageX === this.originX) return false;
      this.containerActive = false;
      this.verify()
    },
    verify() {
      const arr = this.trail // drag y move distance
      const average = arr.reduce(sum) / arr.length // average
      const deviations = arr.map(x => x - average) // deviation array
      const stddev = Math.sqrt(deviations.map(square).reduce(sum) / arr.length) // standard deviation
      const left = parseInt(this.block.style.left)
      this.slideTrue = false,
          this.slideFalse = true,
          this.containerActive = true;
      this.containerSuccess = true;
      this.$emit('success', left)
    },
    reset(h) {
      this.slideTrue = true
      this.slideFalse = false
      this.containerActive = false
      this.containerSuccess = false
      this.containerFail = false
      this.sliderLeft = 0
      this.block.style.left = 0
      this.sliderMaskWidth = 0
      this.canvasCtx.clearRect(0, 0, this.w, this.h)
      this.blockCtx.clearRect(0, 0, this.w, this.h)
      this.initImg(h)
    },
    handleFail() {
      this.fail = true
      this.showInfo = true
      this.infoText = '验证失败'
      this.containerFail = true
      // console.log(6666)
      // setTimeout(() => {
      //   this.block.style.left = 0
      //   this.sliderMaskWidth = 0
      //   this.sliderLeft = 0
      //   this.fail = false
      //   this.showInfo = false
      //   this.containerFail = false
      // }, 800)
    },
    handleSuccess() {
      // console.log(777)
      this.showInfo = true
      this.infoText = '验证成功'
      this.containerSuccess = true
      setTimeout(() => {
        this.block.style.left = 0
        this.sliderMaskWidth = 0
        this.sliderLeft = 0
        this.fail = false
        this.showInfo = false
        this.containerSuccess = false
      }, 1000)
    }
  }
}
</script>
<style>
.slide-verify {
  position: relative;
  width: 310px;
}

.slide-verify-block {
  position: absolute;
  left: 0;
  top: 0
}

.slide-verify-refresh-icon {
  position: absolute;
  right: 0;
  top: 0;
  width: 28px;
  height: 28px;
  cursor: pointer;
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAB80lEQVRIS72Vz4uNYRTHP98/QSJLpZDBzoIiYcp6JkospkwKSexEsWCalawMi5lRakLJxoYUJYmNpFkoMatZIGXD8qtze97b49733vu+953m1Lt4n/N9zvf8es4RqyBaBQ76ktheD+zIvnXAO+AjsCjpWxUne5LYvgRMDTCyAExL+tQP10VieyvwENiZLi4DX4EvwHcg9FvSF5DfiWi6F9F/JLbPA7cy8DFJD8ou2z4AXAd2J/0dSafLsG0S29uBIuy3wCFJfwbl3PYR4FHCTUqa7byTk7wHdgVAUq2us30BuAn8BQ5KCifb0jJm+xpwNZ1ekXRjUASdetvPgVFgVtJkGUkU+ijwTNLhugTJ0QlgPrV2pL4rks/AZuCypEFtW+qD7bgfdkL25CmT7TXAr6TcJ+l1nUhs78/wkfL4vwh8KM6DJA5fDlPwwojtx8BYiXNPJI01jiTVYxvwCoixU8jPiErSYtFdK1GTU8DdjOSMpJlWhpInjbsr2bkPnAAWJB1v1yQpG7+TZGcT8CLei6SYdS1ZkRefF9z2hKR7+Vmj2WV7LXBOUmSipzSZwuNATO29wElJc71Y6u6TH8AIEC27MTN6W9LZyiTZA6uyGd8AMVDjjVRLVyeqZMdvSPMpNuWSpKf9jHd1VxXwsJhay2lYkn+x3cJX9c+KNQAAAABJRU5ErkJggg==) no-repeat;
  background-size: 100% 100%;
}

.slide-verify-slider {
  position: relative;
  text-align: center;
  width: 310px;
  height: 40px;
  line-height: 40px;
  margin-top: 15px;
  background: #f7f9fa;
  color: #45494c;
  border: 1px solid #e4e7eb
}

.slide-verify-slider-mask {
  position: absolute;
  left: 0;
  top: 0;
  height: 40px;
  border: 0 solid #1991FA;
  background: #D1E9FE
}

.slide-verify-slider-mask-item {
  position: absolute;
  top: 0;
  left: 0;
  width: 40px;
  height: 40px;
  background: #fff;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  transition: background .2s linear
}

.slide-verify-slider-mask-item:hover {
  /* background: #1991FA */
}

.slide-verify-slider-mask-item:hover .slide-verify-slider-mask-item-icon {
  /* background-position: 0 -13px */
}

.slide-verify-sliders {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 32px;
  height: 32px;
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAADKElEQVRIS72WS4gcVRSGv1NpCYKP7prElUHEvi2ObvJANJGgEBzduDM+goswGmduTwISEIRAIhiIIBFm0tUTiW7EILr2jSgEFSSPhdGQvi0kxOXUnVE3grGOVE9X09WPmZ6A3k3BrXPrO/f851HC/7BkVEZYdxMk3AnJJgiuEfCbnzZfjHJ+RUg478Y10SmBPSBh/wfVK3wggcz7KfPLMOBQSFi7fAQJXgG9osgZUS4kAeeXps2FYt1tDhK2qLBZlJ0Id6HJ275675FBoIGQMHIXgfsV3l+3vlhdmNz45zAvb5l1G28qcErgKeBnb80DvbZ9kFLkzgpslSTYFc/c8/UoMU9tSjVnRagpnFu0Zlv3uRwkjBonQfaJ6nhcrVwaFZDZjUXN/YrOgr7jbeXlbL8D2RA1tyboWRE5FE+Xj64VkNmHkfsUeDJAti3Y8rl0vwMZi1wEPHTrzYXtV/be/dcNQ2qXtiOF7wTqsTU2BwkjtyTwSWzNnrUAwsg9LfBCbE0qfGuFkfsJ2OStKXYgrUJTPhflWFw1r40KSQHAR71ih5H7EHgG4Ym0YFvhCmtuEuEU8JK3Jn2uujJAmrYE7O4uxlK9+aqovonyoq+ad9uQVuEdTv7hsaX95tuMUIqa+0T0am/7WAmQni3OuUeDdXyDJq+nBZq/ScKknzHvdcW2VZTAbm/Nx+14t0I06Aadc7XmQUTfyt+krYmKHl2crhzqGM+7cZLWB1ug9v6KgOXwN04j8lxOk2UPGzEEX3lbfrZbkLRJdoHSV30a9AoYRu4y6AZvK2O5FC5FjVlB9qIy4avl74eA6BW5F1CKfp0Skrqic4u2ciBfJ8se/wBc9Nbs6PNu3o0nf3NHd2L0peCsWx8WWAASAh7OMi7fu5bb+2HgoLfm+Kp53GMQ1hpnEHkky6rsdV8Xztq8IAdiW54bFZQ110Htftg8Scfq48Bn6PU3fPW+nEbd4OKJxk4J5Hg6HoAvvTUTvY6NMBm5LdUp9VBFzut1/VEK8qCobmmndjqk/ljzZOyuE0mYUXgeuH1A6H4XOK0BJ25oxvdl13/1tzKq6KvZ/QvdVF4prvmPeAAAAABJRU5ErkJggg==) no-repeat;
  background-size: 100% 100%;
}

.slide-verify-slider-mask-item-icon {
  position: absolute;
  top: 3px;
  left: 3px;
  width: 32px;
  height: 32px;
  background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAZCAYAAADE6YVjAAAC0ElEQVRIS82VT2gTQRSHf28SWwVRsol48mKy7UXaQ7y1goLgTVGoom1FW6HuruihCoKCFfQg/jmUZmPFlqKtYgRFb4KgoL2ZQ4uXdhMvnsRkgyJoa7JPJtWSJrNpQSnmEsi8zDdv5/2+JazCh1aBgf8HsnHQiQbXUIsHxMHcBqJJAaSLP3n6y2k9u9zTqNvJhrsfteDcjyQIB303YqSKjWuNrye2uH41vpBQ0ukk5jGAgsSYgMALjwNTBXPrdMj+0CKo1AoPe5jQCXCRiY4VDH1CBVJCQsPZdip5b8p/YO+yazUP+J1SS8wMgMSlcmlA7Cj0Rd9W19ZANiXer/eoMcdAo2vqKx4MzXaYgDnBc5HP1rZvlaCaTcL2bJJBJwWwL2fqzyuLNdvpcE39saqriO3s9YBnBL6dN5sMX0hkKNvkCW8GjFHX0nurAQBSIBpzjdhxFUhLOCMg9AhPNOdORWf/1CzpJJTIHibyHoDQ6xr6aPVGmu2kAHSAaNA1Ymdq1pNODxgjzOJIwYo+VEK0hHMDhH4B2p4zY2nlaX+DCLiWN/XzlTUROxP3wO/AuOla+ll1J7bzioCdy134YkeKyZMDwMDrgqnvUkOSmSvEfIERaJV5qNcJgFuuqfdX1sj8EEpTTHS1YMQuKiHhZGY/Mz8hwtG8od/3vRPQHdeM9VWvh5NONzPuEdGBvBF7qoRIRwWCyMiE5y29SzVdDIwXTL1b1WU44YxLA5SKiFU6rSYnWsJ5VHaV+nn75mQx+YyUa+mH6oaxLMX575+ks5YbgKpOWTqs2LBuc7Us1e4qyxHjchNV8peO7ULSy+4idKkk6W/h4Wy7KHkvpcOkASAwKZimZH7KeSCWFm6TCZfO8gJit0qOEl5XgAuybLguXeZnYekqwfPnqqVY906U8hvKNpUIcYIXZ/nNSDNEOsBIVzrK/yB+K//w9xW/L/6GuSqQXxGyOym67WPZAAAAAElFTkSuQmCC) no-repeat;
  background-size: 100% 100%;
}

.container-active .slide-verify-slider-mask-item {
  height: 38px;
  top: -1px;
  border: 1px solid #1991FA;
}

.container-active .slide-verify-slider-mask {
  height: 38px;
  border-width: 1px;
}

.container-success .slide-verify-slider-mask-item {
  height: 38px;
  top: -1px;
  border: 1px solid #52CCBA;
  background-color: #52CCBA !important;
}

.container-success .slide-verify-slider-mask {
  height: 38px;
  border: 1px solid #52CCBA;
  background-color: #D2F4EF;
}

.container-success .slide-verify-slider-mask-item-icon {
  background-position: 0 0 !important;
}

.container-fail .slide-verify-slider-mask-item {
  height: 38px;
  top: -1px;
  border: 1px solid #f57a7a;
  background-color: #f57a7a !important;
}

.container-fail .slide-verify-slider-mask {
  height: 38px;
  border: 1px solid #f57a7a;
  background-color: #fce1e1;
}

.container-fail .slide-verify-slider-mask-item-icon {
  top: 14px;
  background-position: 0 -82px !important;
}

.container-active .slide-verify-slider-text,
.container-success .slide-verify-slider-text,
.container-fail .slide-verify-slider-text {
  display: none;
}
</style>
