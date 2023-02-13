<template>
  <div class="code-page">
    <div class="page-content">
      <textarea
        class="code-editor"
        ref="mycode"
        v-model="inputValue.code"
        :readonly="inputValue.locked ? 'readonly' : false"
        @focus="onFocus"
        @blur="onBlur"
      ></textarea>
    </div>
  </div>
</template>

<script>
  import DcrunHint from '../../../core/editor/dcrun-hint'
  import CodeMirror from 'codemirror/lib/codemirror'
  import 'codemirror/theme/dracula.css'
  import 'codemirror/lib/codemirror.css'
  import 'codemirror/addon/lint/lint.css'
  // 支持代码折叠
  import 'codemirror/addon/fold/foldgutter.css'
  // 支持自动提示
  import '../../../style/show-hint.css'

  require('codemirror/mode/javascript/javascript')
  require('codemirror/mode/xml/xml')
  require('codemirror/mode/css/css')
  // lint错误提示
  require('codemirror/addon/lint/lint')
  require('codemirror/addon/lint/javascript-lint')
  require('codemirror/addon/lint/json-lint')
  require('codemirror/addon/lint/css-lint')
  // 折叠
  require('codemirror/addon/fold/foldcode')
  require('codemirror/addon/fold/foldgutter')
  require('codemirror/addon/fold/brace-fold')
  require('codemirror/addon/fold/comment-fold')
  // 括号匹配
  require('codemirror/addon/edit/matchbrackets')
  // js h5自动提示
  require('codemirror/addon/hint/show-hint')
  require('codemirror/addon/hint/javascript-hint')
  require('codemirror/addon/hint/xml-hint')
  require('codemirror/addon/hint/html-hint')
  require('codemirror/addon/hint/css-hint')
  require('codemirror/addon/hint/anyword-hint')

  export default {
    name: 'CodePage',
    props: {
      value: {
        default: () => {
          return {}
        }
      }
    },
    computed: {
      inputValue: {
        get: function () {
          return this.value
        },
        set: function (val) {
          this.$emit('input', val)
        }
      }
    },
    data () {
      return {}
    },

    mounted () {
      $rxbus.$on('saveAllOpenedFiles', this.save)
      this.getFileContent()
    },

    methods: {
      async getFileContent () {
        if (!this.inputValue.code && this.inputValue.path) {

          const url = this.Interface.scriptFile.read + '?path=' + encodeURI(this.inputValue.path)
          const response = await this.request.dataGet(this, url)
          let result = response.data
          if (result.code === 1) {
            this.$set(this.inputValue, 'code', result.data)
            this.initCodeMirror(this.inputValue.code, this.inputValue.fileType)
          }

        } else {
          this.initCodeMirror(this.inputValue.code, this.inputValue.fileType)
        }
      },

      save () {
        let oldValue = this.editor.getValue().trim().replace(/\r/g, '')
        let newValue = this.inputValue.code.trim().replace(/\r/g, '')
        if (oldValue !== newValue) {
          this.inputValue.code = this.editor.getValue()
          this.fireChangeEvent()
          DcrunHint.save(this.inputValue.code, this.inputValue.path)
        }
      },
      onFocus () {
        this.oldCode = this.inputValue.code
      },

      onBlur () {
        if (this.oldCode !== this.inputValue.code) {
          $rxbus.$emit('codeFileChange', this.inputValue)
        }
      },
      // 根据不同的文件扩展名，返回不同的codemirror mode
      getModeByFileType (fileType) {
        switch (fileType) {
          case 'js':
            return 'javascript'
          case 'html':
            return 'xml'
          case 'css':
            return 'css'
          case 'json':
            return 'application/json'
          default:
            return 'xml'
        }
      },
      getHintByFileType (fileType) {
        // 根据文件类型，返回自动提示的规则，其中hint是一个返回列表的函数
        switch (fileType) {
          case 'js':
            return {
              hint: this.javascriptAndAnywordAndDcrun,
              completeSingle: false
            }
          case 'html':
            return {hint: this.htmlAndAnyword, completeSingle: false}
          case 'css':
            return {hint: CodeMirror.hint.css, completeSingle: false}
          case 'json':
            return {hint: CodeMirror.hint.javascript, completeSingle: false}
          default:
            return {hint: CodeMirror.hint.anyword, completeSingle: false}
        }
      },
      htmlAndAnyword (cm, options) {
        // 混合检索全文anyword及html的自动提示
        const anyhint = CodeMirror.hint.anyword(cm, options)
        const htmlhint = CodeMirror.hint.html(cm, options)
        const words = DcrunHint.combine2List(anyhint, htmlhint)
        if (words.size > 0) {
          return {
            list: Array.from(words),
            from: (htmlhint || anyhint).from,
            to: (htmlhint || anyhint).to
          }
        }
      },
      javascriptAndAnywordAndDcrun (cm, options) {
        // 混合检索全文anyword及javascript及dcrun的自动提示
        const anyhint = CodeMirror.hint.anyword(cm, options)
        const jshint = CodeMirror.hint.javascript(cm, options)
        const dcrunhint = CodeMirror.hint.dcrun(cm, options)
        const words = DcrunHint.combine3List(dcrunhint, anyhint, jshint)
        if (words.size > 0) {
          return {
            list: Array.from(words),
            from: (jshint || anyhint).from,
            to: (jshint || anyhint).to
          }
        }
      },
      fireChangeEvent () {
        // 需要去掉字符串中的\r
        var oldValue = this.editor.getValue().trim().replace(/\r/g, '')
        var newValue
        if (this.inputValue.code !== undefined) {
          newValue = this.inputValue.code.trim().replace(/\r/g, '')
        }
        $rxbus.$emit('fileContentChange', this.inputValue.path, oldValue !== newValue)
      },
      initCodeMirror (code, fileType) {
        var that = this

        if (!this.editor) {
          DcrunHint.initDcRunHint(CodeMirror, this.$store.state.componentsHints)
          this.editor = CodeMirror.fromTextArea(this.$refs.mycode, {
            mode: this.getModeByFileType(fileType),
            theme: 'dracula',
            lineNumbers: true,
            lint: true,
            lineWrapping: true, // 代码折叠
            foldGutter: true,
            gutters: ['CodeMirror-linenumbers', 'CodeMirror-foldgutter'],
            matchBrackets: true, // 括号匹配
            autoCloseTags: true,
            // 是否在初始化时自动获取焦点
            autofocus: true
          })
          // 显示智能提示
          this.editor.on('keydown', function (cm, event) {
            // console.log(event.keyCode);
            if (event.keyCode === 83 && event.ctrlKey) {
              that.save()
            }
            if (
              (!event.ctrlKey && event.keyCode >= 65 && event.keyCode <= 90) ||
              (event.keyCode >= 97 && event.keyCode <= 122) ||
              (event.keyCode >= 46 && event.keyCode <= 57) ||
              event.keyCode === 190 // .字符
            ) {
              cm.showHint(that.getHintByFileType(fileType))
            }
          })
          // 自动滚动到最下面
          this.editor.on('changes', (cm, changes) =>
            that.updateByTypewriterMode(cm, changes)
          )
        }
        if (code) {
          if (typeof code === 'object') {
            // JSON对象的话先转成字符串，否则显示[object,object]
            code = JSON.stringify(code)
          }
          this.editor.setValue(code)
        }
        this.editor.setSize('100%', '480px')
        this.editor.on('change', function (cm, changes) {
          // console.log(cm.getValue().trim())
          // console.log(that.inputValue.code.trim())
          that.fireChangeEvent()
        })
        CodeMirror.commands.wpScrollSelectionToCenter = function (cm) {
          // 打字机模式，自动滚动
          if (cm.getOption('disableInput')) {
            return CodeMirror.Pass
          }

          // 获取当前光标所在位置
          let top = cm.cursorCoords(true, 'local').top
          // 获取当前编辑器高度
          let editorHeight = cm.getWrapperElement().offsetHeight
          // 获取当前行高
          let lh = cm.defaultTextHeight()
          let scrollY = Math.round(top - editorHeight / 2 + lh / 2)
          cm.scrollTo(null, scrollY)
        }
      },
      updateByTypewriterMode (cm, changes) {
        if (cm.getSelection().length !== 0) {
          return
        }

        const scrollOrigins = ['+input', '+delete', '*compose', 'paste']

        for (let i = 0, len = changes.length; i < len; i++) {
          let each = changes[i]
          let origin = each.origin
          if (scrollOrigins.includes(origin)) {
            cm.execCommand('wpScrollSelectionToCenter')
            return
          }
        }
      }
    }
  }
</script>

<style>
  .code-page {
    flex: 1;
    display: flex;
    flex-flow: column;
    height: 0;
  }

  .code-page .page-toolbar {
    height: 1px;
    background: #494c45;
    display: flex;
    flex-flow: row;
    justify-content: space-between;
    align-items: center;
  }

  .code-page .page-content {
    flex: 1;
    height: 0;
    overflow: auto;
    display: block;
    border: #494c45 solid 1px;
  }

  .CodeMirror {
    /* height: auto; */
    font-family: "Courier New", monospace;
    font-size: 16px;
    line-height: 150%;
  }

  /* .CodeMirror-scroll {
      height: auto;
    } */

  .CodeMirror-lines {
    /* 需要的时候滚动条有足够的空间 */
    padding-bottom: 50%;
  }
</style>
