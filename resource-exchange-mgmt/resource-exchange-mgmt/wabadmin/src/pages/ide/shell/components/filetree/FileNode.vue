<template>
  <div v-if="inputValue.fileType ==='dir'">
    <FolderNode v-model="inputValue" @remove="removeChild"/>
  </div>

  <div v-else class="tree-node" :class="inputValue === activedFile ? 'selected' :''" v-show="!inputValue.hidden">
    <div class="tree-node-title" @click="click" @contextmenu.prevent='onContextMenu' ref="nodTitle" :class="locked ? 'locked' : ''">
      <div class="node-icon">
        <i v-if="inputValue.fileType ==='js'" class="fas fa-file-code" style="color:yellow"></i>
        <i v-else-if="inputValue.fileType ==='html'" class="fas fa-file-contract" style="color:orange"></i>
        <i v-else-if="inputValue.fileType ==='css'" class="fas fa-file-alt" style="color:#1E90FF"></i>
        <i v-else-if="inputValue.fileType ==='json'" class="fas fa-file-image" style="color:#F08080"></i>
        <i v-else class="fas fa-file"></i>
      </div>

      {{inputValue.name}}
      <i v-if="locked" class="fas fa-lock lock-icon"></i>

    </div>

    <div v-if='showContextMenu' class="node-context-menu" :style="{'top':contextMenuTop, 'left':contextMenuLeft}">

      <div class="menu-item" @click="rename" v-show='isShowRenameMenu'>
        <i class="fas fa-pen"></i> {{$t('widgets.rename')}}
      </div>

      <div class="menu-item" @click="remove" v-show='isShowDeleteMenu'>
        <i class="fas fa-trash-alt"></i> {{$t('widgets.delete')}}
      </div>

    </div>

    <NewFileDialog v-model="isOpen" @callback="callback" :type="type" :fileName="fileName"/>

  </div>
</template>

<script>
  import NewFileDialog from '../dialog/NewFileDialog.vue'

  export default {
    name: 'FileNode',
    props: {
      value: {
        default: () => {
          return {}
        }
      }
    },
    components: {
      NewFileDialog
    },
    data () {
      return {
        isOpen: false,
        contextMenuPoped: false,

        isShowRenameMenu: true,
        isShowDeleteMenu: true,

        contextMenuTop: '0',
        contextMenuLeft: '0',

        type: 3, // 0：创建文件夹 1：创建文件 2：重命名文件夹 3：重命名文件
        fileName: '',
        parent: {},
        locked: false,
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
      },

      showContextMenu () {
        return this.contextMenuPoped
      },

      activedFile () {
        return this.$store.state.activedFile
      }
    },

    mounted () {
      document.addEventListener('click', this.clearEditingThings)
      document.addEventListener('contextmenu', this.hideContextMenu)
      this.recursionGetParent(this.$store.state.rootFile)
      this.locked = this.inputValue.permission === '0,0,0'
    },

    beforeDestroyed () {
      document.removeEventListener('click', this.clearEditingThings)
      document.removeEventListener('contextmenu', this.hideContextMenu)
    },

    methods: {
      click () {
        $rxbus.$emit('fileSelected', this.inputValue)
      },

      onContextMenu (event) {
        if (!this.locked) {
          this.contextMenuTop = event.clientY + 'px'
          this.contextMenuLeft = event.clientX + 'px'
          this.contextMenuPoped = true
        }
      },

      hideContextMenu (event) {
        if (event.target !== this.$refs.nodTitle) {
          this.contextMenuPoped = false
        }
      },
      recursionGetParent (parent) {
        if (parent.children) {
          for (let i = 0; i < parent.children.length; i++) {
            if (parent.children[i] === this.inputValue) {
              this.parent = parent
              return
            } else {
              this.recursionGetParent(parent.children[i])
            }
          }
        }
      },

      clearEditingThings () {
        this.contextMenuPoped = false
      },

      rename (event) {
        this.isOpen = true

        this.type = 3 // 重命名目录
        this.fileName = this.inputValue.name

        this.contextMenuPoped = false
        event.stopPropagation()
      },

      async callback (d, t) {
        if (t !== 3) {
          return
        }

        let oldType = this.inputValue.name.substring(this.inputValue.name.lastIndexOf('.') + 1)
        let newType = d.substring(d.lastIndexOf('.') + 1)
        let oldPath = this.inputValue.path

        let name = d
        let type
        if (newType || newType === '') {
          type = oldType
          name += '.' + type
        }
        if (this.parent && this.parent.children) {
          for (let i = 0; i < this.parent.children.length; i++) {
            if (this.parent.children[i].name === name) {
              this.$message.warning('名字重复,重命名失败')
              return
            }
          }
        }
        this.inputValue.name = name
        this.inputValue.path = oldPath.substring(0, oldPath.lastIndexOf(this.FileSeparator) + 1) + name
        this.inputValue.type = type

        const url = this.Interface.scriptFile.rename
        let data = {
          oldPath: oldPath,
          name: name
        }

        const response = await this.request.dataPut(this, url, data)
        if (response.data.code !== 1) {
          this.$message.error('文件重命名失败')
        }
      },

      async remove () {
        // 发送消息让父级目录删除自己
        this.$emit('remove', this.inputValue.path)

        // 请求后台删除自己
        const url = this.Interface.scriptFile.delete + '?path=' + encodeURI(this.inputValue.path)
        const response = await this.request.dataDelete(this, url)
        let result = response.data
        if (result.code !== 1) {
          this.$message.error('文件删除失败')
        }

      },

      removeChild (data) {
        for (let i in this.inputValue.children) {
          if (this.inputValue.children[i].path === data) {
            this.inputValue.children.splice(i, 1)
            return
          }
        }

        this.$emit('remove', data)
      },
    },

    watch: {
      'contextMenuPoped': function (value) {
        if (!value) {
          return
        }

        let permission = this.inputValue.permission.split(',')
        if (permission == null || permission.length !== 3) {
          return
        }

        this.isShowRenameMenu = permission[1] == 1
        this.isShowDeleteMenu = permission[2] == 1
      }
    }
  }
</script>

<style>

  .lock-icon {
    position: absolute;
    right: 10px;
    font-size: 10px;
    top: 12px;
  }
</style>
