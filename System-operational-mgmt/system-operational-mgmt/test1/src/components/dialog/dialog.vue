<template>
  <el-main>
    <el-dialog top="10vh" :destroy-on-close='true' :title="modalTitle"
      :visible.sync="dialog"
      width='52%'
      v-if="drawerDialog.type === 'dialog' || drawerDialog.type === '' || drawerDialog.type === undefined"
      class='abow_dialog'
      :close-on-click-modal='false'
      @close='closeDialog'>
      <formDialog
        v-if="drawerDialog.form"
        :form-mode='drawerDialog'
        @triggerShield='triggerShield'
        ref="trigger">
      </formDialog>
      <tableForm
        ref="table"
        v-if="drawerDialog.list"
        :table-form='drawerDialog'>
      </tableForm>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialog = false">取 消</el-button>
        <template v-for="(i, v) in drawerDialog.event">
          <el-button :key="v"
            v-if='!openForm && dialogState === "modify" && i.bind === "modify"'
            type="primary"
            :disabled='disabled'
            @click.native.prevent="i.method !== undefined ? i.method(submitForm('form')) : ''">确 定
          </el-button>
          <el-button :key="v"
            v-if='!openForm && dialogState === "increase" && i.bind === "increase"'
            type="primary"
            @click.native.prevent="i.method !== undefined ? i.method(submitForm('form')) : ''">确 定
          </el-button>
        </template>
      </div>
    </el-dialog>
    <el-drawer v-if="drawerDialog.type === 'drawer'"
      :title="modalTitle"
      direction="rtl"
      :visible.sync="dialog"
      custom-class="demo-drawer"
      size='42%'
      @close='closeDialog'
      :wrapperClosable='false'
      ref="drawer">
      <div class="demo-drawer__content">
        <formDialog
        v-if="drawerDialog.form"
        :form-mode='drawerDialog'
        ref="trigger">
      </formDialog>
      <tableForm
        ref="table"
        v-if="drawerDialog.list"
        :table-form='drawerDialog'>
      </tableForm>
        <div class="demo-drawer__footer drawer_footer">
          <el-button @click="dialog = false">取 消</el-button>
          <template v-for="(i, v) in drawerDialog.event">
            <el-button :key="v"
              v-if='!openForm && dialogState === "modify" && i.bind === "modify"'
              type="primary"
              :disabled='disabled'
              @click.native.prevent="i.method !== undefined ? i.method(submitForm('form')) : ''">确 定
            </el-button>
            <el-button :key="v"
              v-if='!openForm && dialogState === "increase" && i.bind === "increase"'
              type="primary"
              @click.native.prevent="i.method !== undefined ? i.method(submitForm('form')) : ''">确 定
            </el-button>
          </template>
        </div>
      </div>
    </el-drawer>
  </el-main>
</template>

<script>
import formDialog from '../form/form'
import tableForm from '../table/SearchTable'

export default {
  components: {
    formDialog, tableForm
  },

  props: {
    drawerDialog: {
      type: Object,
      default: () => {}
    }
  },

  data () {
    return {
      // 接收弹层标题参数字段
      modalTitle: '',
      // 是否开启加载 loading 状态
      dialog: false,
      // 接收父组件传递 应用场景状态
      dialogState: '',
      // 关闭提交按钮点击事件
      openForm: false,
      // 隐藏按钮
      disabled: false
    }
  },

  methods: {
    /**
     * @param name  抬头标题名称
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    popup (name, state, value = {}) {
      this.modalTitle = name
      this.dialog = true
      this.dialogState = state

      if (this.drawerDialog.form) {
        this.$nextTick(() => {
          this.$refs.trigger.initial(value, state)
        })
      }

      if (this.drawerDialog.list) {
        this.drawerDialog.tab = true
      }
    },

    // 提交按钮 提交表单
    submitForm (formName) {
      if (this.drawerDialog.form) return this.$refs.trigger.submit(formName)
    },

    // 关闭模块 清空表单
    closeDialog (done) {
      if (this.drawerDialog.form) {
        this.$refs.trigger.closeDialog()
      }
      this.dialog = false
    },

    // 上传状态下 禁止触发确定按钮
    triggerShield (state) {
      this.disabled = state
    }
  }
}
</script>

<style lang="scss">
  @import '../styles/dialog.scss';
  :focus {
    outline:0;
  }
</style>
