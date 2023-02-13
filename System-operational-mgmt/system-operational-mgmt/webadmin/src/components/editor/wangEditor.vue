<template>
  <div :class="editor_rule">
    <div ref="toolbar" class="toolbar"></div>
    <div ref="editor" class="text"></div>
  </div>
</template>

<script>
import E from 'wangeditor'

export default {
  data () {
    return {
      editor: null,
      info_: null,
      editor_rule: 'editor'
    }
  },

  mounted () {
    this.seteditor()
    this.editor.txt.html()
  },

  methods: {
    seteditor () {
      this.editor = new E(this.$refs.toolbar, this.$refs.editor)
      // base 64 存储图片
      this.editor.customConfig.uploadImgShowBase64 = true
      // 配置服务器端地址
      // this.editor.customConfig.uploadImgServer = 'http://otp.cdinfotech.top/file/upload_images'
      // 自定义 header
      this.editor.customConfig.uploadImgHeaders = { }
      // 后端接受上传文件的参数名
      // this.editor.customConfig.uploadFileName = 'file'
      // 将图片大小限制为 2M
      this.editor.customConfig.uploadImgMaxSize = 2 * 1024 * 1024
      // 将图片大小限制为 2M
      this.editor.customConfig.uploadImgMaxLength = 1
      // 设置超时时间
      this.editor.customConfig.uploadImgTimeout = 3 * 60 * 1000
      this.editor.customConfig.zIndex = '0'

      // 配置菜单
      this.editor.customConfig.menus = ['head', 'bold', 'fontSize', 'fontName',
        'italic', 'underline', 'strikeThrough', 'foreColor',
        'backColor', 'link', 'list', 'justify', 'quote',
        'emoticon', 'image', 'table', 'video', 'code'
      ]

      this.editor.customConfig.uploadImgHooks = {
        customInsert: (insertImg, result, editor) => {
          // 图片上传成功，插入图片的回调
          // let url = 'http://otp.cdinfotech.top/' + result.url
          // insertImg(url)
        }
      }

      this.editor.customConfig.onchange = (html) => {
        this.editor_rule = 'editor'
        this.$emit('monitor')
      }

      // 创建富文本编辑器
      this.editor.create()
    },

    htmlContent () {
      return {
        text: this.editor.txt.text(),
        html: this.editor.txt.html()
      }
    },

    setContent (value) {
      this.editor.txt.clear()
      this.editor.txt.html(value)
    },

    setStyle (row) {
      this.editor_rule = 'editors'
    }
  }
}
</script>

<style lang="scss">
  @import '../styles/wangEditor.scss';
</style>
