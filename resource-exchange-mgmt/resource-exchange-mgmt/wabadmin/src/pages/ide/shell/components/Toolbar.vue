<template>
  <div class="toolbar">
    <div class="left">
      <div class="button start" @click="run" v-show="!isRun">
        <i class="fas fa-play"></i>
        {{$t('toolbar.run')}}
      </div>

      <div class="button stop" @click="stop" v-show="isRun">
        <i class="fas fa-stop"></i>
        {{$t('toolbar.stop')}}
      </div>

      <div class="button save" @click="save">
        <i class="fas fa-save"></i>
        {{$t('toolbar.save')}}
      </div>

      <div class="button sync" @click="sync">
        <i class="fas fa-cloud-upload-alt"></i>
        {{$t('toolbar.sync')}}
      </div>

      <div class="button backup" @click="backup">
        <i class="fas fa-circle"></i>
        {{$t('toolbar.backup')}}
      </div>

      <div class="button save" @click="backupMirror">
        <i class="fas fa-clock"></i>
        {{$t('toolbar.backup-mirror')}}
      </div>

      <div class="button setting" @click="setting">
        <i class="fas fa-cog"></i>
        {{$t('toolbar.setting')}}
      </div>

    </div>

    <SettingDialog v-if="isOpen" v-model="isOpen"/>

  </div>
</template>

<script>
  import SettingDialog from './dialog/SettingDialog.vue'

  export default {
    name: 'Toolbar',
    components: {
      SettingDialog
    },
    data () {
      return {
        isRun: false,
        isOpen: false
      }
    },
    mounted () {
      this.getRunState()
    },
    methods: {
      async getRunState () {
        const url = this.Interface.sysService.isOnline
        let data = {
          appid: this.appid,
        }

        const response = await this.request.dataGet(this, url, data)
        if (response.data.code === 1) {
          this.isRun = response.data.data
        } else {
          this.isRun = false
        }
      },

      async run () {
        this.isRun = true

        const url = this.Interface.webapi.start
        let data = {
          id: this.appid,
        }

        const response = await this.request.dataPut(this, url, data)
        if (response.data.code === 1) {
          this.$message.info('启动指令已经发送，启动过程大概需要1~2分钟，请稍等')
        } else {
          this.isRun = false
          this.$message.error(response.data.msg)
        }

      },

      async stop () {
        this.isRun = false

        const url = this.Interface.webapi.stop
        let data = {
          id: this.appid,
        }

        const response = await this.request.dataPut(this, url, data)
        if (response.data.code === 1) {
          this.$message.info('停止成功')
        } else {
          this.isRun = true
          this.$message.error(response.data.msg)
        }
      },

      save () {
        $rxbus.$emit('saveAllOpenedFiles', null)
      },

      async sync () {

        if (this.$store.state.needSaveFlag) {
          this.$message.warning('请先保存未保存的文件')
          return
        }

        const url = this.Interface.sysService.upgradeScript
        let data = {
          appid: this.appid,
        }

        const response = await this.request.dataPut(this, url, data)
        if (response.data.code === 1) {
          this.$message.info('同步脚本成功')
        } else {
          this.$message.error(response.data.msg)
        }
      },

      backup () {
        this.$emit('toAdd')
      },

      backupMirror () {
        this.$message.info('暂未实现')
      },

      setting () {
        this.isOpen = true
      }

    }
  }
</script>

<style>
</style>
