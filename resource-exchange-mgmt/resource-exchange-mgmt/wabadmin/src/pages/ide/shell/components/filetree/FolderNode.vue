<template>
  <div class="tree-node">
    <div class="tree-node-title" @click="click" @contextmenu.prevent='onContextMenu' ref="nodTitle">
      <div class="node-icon">
        <i v-show="icon" :class="icon" style="color:#00FFFF"/>
      </div>

      {{inputValue.name}}
      <i v-if="locked" class="fas fa-lock lock-icon"></i>

    </div>

    <div v-if="opened" class="children-nodes">

      <template v-if="inputValue.children && inputValue.children.length > 0 && !inputValue.hidden">
        <FileNode v-for="(child, i) in inputValue.children" :key="i" v-model="inputValue.children[i]" @remove="removeChild"/>
      </template>

    </div>

    <div v-if='showContextMenu' class="node-context-menu" :style="{'top':contextMenuTop, 'left':contextMenuLeft}">

      <div class="menu-item" @click="openDialog(1)" v-show='isShowNewFileMenu'>
        <i class="fas fa-file"></i> {{$t('widgets.new-file')}}
      </div>

      <div class="menu-item" @click="openDialog(0)" v-show='isShowNewDirMenu'>
        <i class="fas fa-folder"></i> {{$t('widgets.new-dir')}}
      </div>

      <div class="menu-item" @click="openDialog(2)" v-show='isShowRenameMenu'>
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
    name: 'FolderNode',
    props: {
      value: {
        default: () => {
          return []
        }
      },
    },
    components: {
      NewFileDialog
    },
    data () {
      return {
        isOpen: false,
        opened: false,
        contextMenuPoped: false,

        isShowNewDirMenu: true,
        isShowNewFileMenu: true,
        isShowRenameMenu: true,
        isShowDeleteMenu: true,

        contextMenuTop: '0',
        contextMenuLeft: '0',

        type: 0, // 0：创建文件夹 1：创建文件 2：重命名文件夹 3：重命名文件
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

      icon () {
        return this.opened ? 'fas fa-folder-open' : 'fas fa-folder'
      },

      showContextMenu () {
        return this.contextMenuPoped
      }

    },
    mounted () {
      $rxbus.$on('globalClick', this.globalClick)
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
        this.opened = !this.opened
      },

      globalClick (event) {
        if (this.contextMenuPoped) {
          this.contextMenuPoped = false
          event.stopPropagation()
        }
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

      openDialog (type) {
        this.opened = true

        this.isOpen = true
        this.type = type

        switch (type) {
          case 0: // 新建文件夹
          case 1: // 新建文件
            this.fileName = ''
            break
          case 2:// 重命名文件夹
            this.fileName = this.inputValue.name
            break
        }

        this.contextMenuPoped = false
        event.stopPropagation()
      },

      callback (data, type) {
        switch (type) {
          case 0: // 新建文件夹
          case 1: // 新建文件
            this.create(data, type)
            break
          case 2: // 重命名文件夹
            this.rename(data, type)
            break
        }
      },
      checkRepeatName (parent, name, desc) {
        if (parent && parent.children) {
          for (let i = 0; i < parent.children.length; i++) {
            if (parent.children[i].name === name) {
              this.$message.warning('名字重复,' + desc + '失败')
              return false
            }
          }
        }
        return true
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
      // 调用后台接口创建文件
      async create (d, t) {
        let name = d
        let type
        if (t === 0) {
          type = 'dir'
        } else {
          let index = d.lastIndexOf('.')
          if (index !== -1) {
            type = d.substring(d.lastIndexOf('.') + 1)
          } else {
            name += '.js'
            type = 'js'
          }
        }
        //校验名字是否重复
        if (!this.checkRepeatName(this.inputValue, name, '创建')) {
          return
        }
        const url = this.Interface.scriptFile.new
        let data = {
          name: name,
          path: this.inputValue.path + this.FileSeparator + name,
          type: type
        }

        if (this.inputValue.children === undefined) {
          this.inputValue.children = []
        }

        this.inputValue.children.push({
          name: data.name,
          path: data.path,
          selected: true,
          permission: 'dir' === data.type ? '1,1,1' : '0,1,1',
          code: '',
          fileType: data.type
        })

        const response = await this.request.dataPost(this, url, data)
        if (response.data.code !== 1) {
          this.$message.error('创建失败')
        }

      },

      async rename (d, t) {
        //校验名字是否重复
        if (!this.checkRepeatName(this.parent, d, '重命名')) {
          return
        }

        const url = this.Interface.scriptFile.rename
        let data = {
          oldPath: this.inputValue.path,
          name: d
        }

        this.inputValue.name = d
        this.inputValue.path = data.oldPath.substring(0, data.oldPath.lastIndexOf(this.FileSeparator) + 1) + d + this.FileSeparator
        this.inputValue.type = d.substring(d.lastIndexOf('.') + 1)

        // 所有子目录也需要修改
        for (let i in this.inputValue.children) {
          let child = this.inputValue.children[i]
          child.path = this.inputValue.path + child.name
        }

        const response = await this.request.dataPut(this, url, data)
        if (response.data.code !== 1) {
          this.$message.error('目录重命名失败')
        }
      },

      async remove (event) {
        this.contextMenuPoped = false
        event.stopPropagation()
        // 清空子目录
        this.inputValue.children = []
        // 发送消息让父级目录删除自己
        this.$emit('remove', this.inputValue.path)

        // 请求后台删除自己
        const url = this.Interface.scriptFile.delete + '?path=' + encodeURI(this.inputValue.path)
        const response = await this.request.dataDelete(this, url)
        let result = response.data
        if (result.code !== 1) {
          this.$message.error('目录删除失败')
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
      }
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

        this.isShowNewDirMenu = permission[0] == 1
        this.isShowNewFileMenu = permission[0] == 1
        this.isShowRenameMenu = permission[1] == 1
        this.isShowDeleteMenu = permission[2] == 1
      }
    }
  }
</script>

<style>
  .node-tree {
    flex: 1;
    width: 100%;
    overflow: auto;
    height: 0;
    display: flex;
    flex-flow: column;
  }

  .tree-node .node-icon {
    width: 20px;
    height: 20px;
    display: flex;
    align-items: center;
    flex-shrink: 0;
    justify-content: center;
  }

  .tree-node.selected {
    background: rgba(0, 123, 255, 0.1)
  }

  .children-nodes {
    padding-left: 15px;
  }

  .tree-node .tree-node-title {
    height: 30px;
    display: flex;
    flex-flow: row;
    flex-wrap: nowrap;
    align-items: center;
    padding-left: 0px;
    flex-shrink: 0;
    position: relative;
    cursor: pointer;
  }

  .tree-node-title.locked {
    color: #999;
  }

  .tree-node .tree-node-title:hover {
    background: rgba(255, 255, 255, 0.05);
  }

  .node-context-menu {
    position: fixed;
    display: flex;
    flex-flow: column;
    min-width: 80px;
    background: #fff;
    color: #000;
    box-shadow: 1px 0px 5px 0px rgba(0, 0, 0, 0.5);
    z-index: 1;
    cursor: pointer;
  }

  .node-context-menu .menu-item {
    flex: 1;
    padding: 10px;
    border-bottom: 1px solid #ebebeb;
  }

  .node-context-menu .menu-item i {
    color: #75b325;
    width: 20px;
  }

  .node-context-menu .menu-item:hover {
    background: #ebebeb;
  }

  .node-title input {
    border: 0;
    outline: 0;
    background: rgba(0, 0, 0, 0.2);
    padding: 4px;
    border: #555 solid 1px;
    color: #fff;
  }

</style>
