<template>
  <div id="iframe-container">
    <iframe
      ref="iframe"
      :src="item.url"
      class="iframee"
      scrolling="none"
    />
  </div>
</template>

<script>
import NProgress from "nprogress";
import "nprogress/nprogress.css";
export default {
  name: "IfRame",
  props: {
    item: {
      type: Object,
      default: () => {
        return {
          url: "https://www.baidu.com/"
        };
      }
    }
  },
  data() {
    return {};
  },
  watch: {
    $route: function() {
      this.load();
    }
  },
  created() {
    NProgress.configure({ showSpinner: false });
  },
  mounted() {
    this.load();
    this.resize();
  },
  methods: {
    // 显示等待框
    show() {
      NProgress.start();
    },
    // 隐藏等待狂
    hide() {
      NProgress.done();
    },
    // 加载浏览器窗口变化自适应
    resize() {
      window.onresize = () => {
        this.iframeInit();
      };
    },
    // 加载组件
    load() {
      this.show();
      this.iframeInit();
    },
    // iframe窗口初始化
    iframeInit() {
      const iframe = this.$refs.iframe;
      const clientHeight = document.documentElement.clientHeight - 0;
      iframe.style.height = `${clientHeight}px`;
      if (iframe.attachEvent) {
        iframe.attachEvent("onload", () => {
          this.hide();
        });
      } else {
        iframe.onload = () => {
          this.hide();
        };
      }
    }
  }
};
</script>

<style lang="scss" scoped>
#iframe-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.iframee {
  // width: calc(100% + 18px);
  width: 100%;
  height: 100%;
  border: 0;
  overflow: hidden;
  box-sizing: border-box;
}
</style>