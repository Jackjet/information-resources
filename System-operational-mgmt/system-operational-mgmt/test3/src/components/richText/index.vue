<template>
  <div :class="editor_rule">
    <div ref="toolbar" class="toolbar"></div>
    <div ref="editor" class="text"></div>
  </div>
</template>

<script>
import E from "wangeditor";

export default {
  data() {
    return {
      editor: null,
      info_: null,
      editor_rule: "editor"
    };
  },

  mounted() {
    this.seteditor();
    this.editor.txt.html();
  },

  methods: {
    seteditor() {
      this.editor = new E(this.$refs.toolbar, this.$refs.editor);
      // base 64 存储图片
      this.editor.customConfig.uploadImgShowBase64 = true;
      // 配置服务器端地址
      // this.editor.customConfig.uploadImgServer = 'http://otp.cdinfotech.top/file/upload_images'
      // 自定义 header
      this.editor.customConfig.uploadImgHeaders = {};
      // 后端接受上传文件的参数名
      // this.editor.customConfig.uploadFileName = 'file'
      // 将图片大小限制为 2M
      this.editor.customConfig.uploadImgMaxSize = 2 * 1024 * 1024;
      // 将图片大小限制为 2M
      this.editor.customConfig.uploadImgMaxLength = 1;
      // 设置超时时间
      this.editor.customConfig.uploadImgTimeout = 3 * 60 * 1000;
      this.editor.customConfig.zIndex = "0";

      // 配置菜单
      this.editor.customConfig.menus = [
        "head",
        "bold",
        "fontSize",
        "fontName",
        "italic",
        "underline",
        "strikeThrough",
        "foreColor",
        "backColor",
        "link",
        "list",
        "justify",
        "quote",
        "emoticon",
        "image",
        "table",
        "video"
      ];

      this.editor.customConfig.uploadImgHooks = {
        customInsert: (insertImg, result, editor) => {
          // 图片上传成功，插入图片的回调
          // let url = 'http://otp.cdinfotech.top/' + result.url
          // insertImg(url)
        }
      };

      this.editor.customConfig.onchange = html => {
        this.editor_rule = "editor";
        this.$emit("monitor", html);
      };

      // 创建富文本编辑器
      this.editor.create();
    },

    htmlContent() {
      return {
        text: this.editor.txt.text(),
        html: this.editor.txt.html()
      };
    },

    setContent(value) {
      this.editor.txt.clear();
      this.editor.txt.html(value);
    },

    setStyle(row) {
      this.editor_rule = "editors";
    }
  }
};
</script>

<style lang="scss">
.editor {
  width: 100%;
  margin: 0 auto;
  position: relative;
  z-index: 0;
  margin-bottom: 20px;
}

.editors {
  width: 100%;
  margin: 0 auto;
  position: relative;
  z-index: 0;
  margin-bottom: 20px;
  .toolbar {
    border: 1px solid #f56c6c;
    border-bottom: none;
  }
  .text {
    border: 1px solid #f56c6c;
    border-top: none;
  }
}

.w-e-toolbar {
  flex-wrap: wrap !important;
  .w-e-droplist {
    top: 0px;
    border: 1px solid #e6e6e6 !important;
    box-shadow: 0px 2px 5px #e6e6e6;
    z-index: 1;
    .w-e-dp-title {
      background: #f1f1f1;
    }
    .w-e-list li.w-e-item {
      color: rgb(85, 84, 84) !important;
    }
  }
  .w-e-toolbar .w-e-menu i:hover {
    color: rgb(85, 84, 84) !important;
  }
}

.toolbar {
  border: 1px solid #ececec;
  background: #f1f1f1;
}

.w-e-text-container .w-e-panel-container {
  border: 1px solid #e6e6e6 !important;
  box-shadow: 0px 2px 5px #e6e6e6;
  input {
    height: 30px !important;
    border-bottom: 1px solid rgb(236, 236, 236) !important;
  }
  .w-e-button-container button {
    color: rgb(85, 84, 84) !important;
  }
}

.w-e-text-container .w-e-panel-container .w-e-panel-tab-content textarea {
  border: 1px solid #ececec !important;
}

.w-e-toolbar .w-e-droplist ul.w-e-list {
  font-size: 14px;
  color: rgb(85, 84, 84) !important;
  span i {
    display: none;
  }
}

.text {
  border: 1px solid #ececec;
  border-top: none;
  text-align: left;
  height: 300px;
}
.prompting {
  text-align: left;
  color: #f56c6c;
}
</style>
