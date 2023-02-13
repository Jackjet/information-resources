<template>
  <Modal v-model="inputValue" :size="'large'">
    <div class="dialog-head">
      <div><i class="fas fa-cog"></i> {{$t('toolbar.setting')}}</div>
      <span class="close-button" @click="inputValue = false">×</span></div>
    <div class="dialog-body">
      <div style="display: flex;flex-direction: row; padding:10px">
        <div><span style="font-size: 14px">当前/远程版本:{{ version }}</span></div>
        &nbsp;<i class="fa fa-share fa-1x pd2" style="width:25px; height:25px; cursor: pointer;" @click="askVersion"></i>
      </div>
      <div class="dialog-row">
        <form style="margin-right: 10px">
          <template v-for="(value, item) in keyValues">
            <div :key="item" class="dialog-row">
              <input class="dialog-row-input" type="text" v-model="value.key">
              <input class="dialog-row-input" type="text" v-model="value.value">
              <div class="dialog-buttonf " @click="deleteItem(value)">
                <i class="fas fa-minus pd5"></i> <div>删除</div>
              </div>
            </div>
          </template>
        </form>
        <div class="dialog-button" style="margin-top: 10px" @click="addItem()">
          <i class="fas fa-plus pd5"></i> <div>自定义增加</div>
        </div>
      </div>
    </div>
    <div class="dialog-footer">
      <div class="dialog-button" @click="save()">
        <i class="far fa-save pd5"></i> <div>暂存</div>
      </div>
      <div class="dialog-button" @click="publish()">
        <i class="fab fa-pushed pd5"></i> <div>发布</div>
      </div>
    </div>
  </Modal>
</template>

<script>
import Modal from '../Modal.vue'

export default {
  name: 'SettingDialog',
  components: {
    Modal
  },
  props: {
    value: {default: false}
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
    return {
      themes: [],
      selectedTheme: null,
      keyValues: [],
      version: ''
    }
  },
  mounted () {
    this.askInfo()
    this.askVersion()
  },

  methods: {
    async askInfo () {
      try {
        let url = this.Interface.customSysServiceConfig.find
        let parameters = { appid: this.appid, integrationId: this.integrationId }
        const response = await this.request.dataGet(this, url, parameters)
        if (response.data.code === 1) {
          this.keyValues = response.data.data.content
          console.log(this.keyValues)
        } else {
          alert('获取失败')
        }
      } catch (e) {
        alert(e)
      }
    },
    deleteItem (row) {
      if (row.key.length === 0 && row.value.length === 0) {
        let index = this.keyValues.indexOf(row)
        if (index > -1) {
          this.keyValues.splice(index, 1)
        }
      } else {
        if (confirm('该行存在数据, 确认是否继续进行删除操作?')) {
          let index = this.keyValues.indexOf(row)
          if (index > -1) {
            this.keyValues.splice(index, 1)
          }
        }
      }
    },
    addItem () {
      let keyValue = {
        key: '',
        value: ''
      }
      this.keyValues.push(keyValue)
    },
    async save () {
      try {
        const that = this
        let data = {}
        for (var item in that.keyValues) {
          if (that.keyValues[item].key === '' || that.keyValues[item].value === '') {
            alert('自定义配置不可为空')
            return false
          }
          if (data[that.keyValues[item].key]) {
            alert('自定义id不能重复')
            return false
          }

          data[that.keyValues[item].key] = that.keyValues[item].value
        }

        let url = this.Interface.customSysServiceConfig.update
        let parameter = {
          appid: that.appid,
          integrationId: that.integrationId,
          data: data
        }
        const response = await this.request.dataPut(this, url, parameter)
        if (response.data.code === 1) {
          alert('保存成功')
          this.inputValue = false
        } else {
          alert('保存失败')
        }
      } catch (e) {
        alert(e)
      }
    },
    async publish () {
      try {
        const that = this
        let data = {}
        for (var item in that.keyValues) {
          if (that.keyValues[item].key === '' || that.keyValues[item].value === '') {
            alert('自定义配置不可为空')
            return false
          }
          if (data[that.keyValues[item].key]) {
            alert('自定义id不能重复')
            return false
          }

          data[that.keyValues[item].key] = that.keyValues[item].value
        }

        let updateUrl = this.Interface.customSysServiceConfig.update
        let updateParameter = {
          appid: that.appid,
          integrationId: that.integrationId,
          data: data
        }
        const updateResponse = await this.request.dataPut(this, updateUrl, updateParameter)
        if (updateResponse.data.code !== 1) {
          alert('发布失败')
          return false
        }

        let url = this.Interface.sysService.upgradeConfig
        let parameter = {
          appid: that.appid
        }
        const response = await this.request.dataPut(this, url, parameter)
        if (response.data.code === 1) {
          alert('发布成功')
          this.inputValue = false
        } else {
          alert('发布失败:' + response.data.msg)
          return false
        }
      } catch (e) {
        alert(e)
      }
    },
    async askVersion () {
      const that = this
      try {
        let url = this.Interface.customSysServiceConfig.version
        let parameters = {
          appid: that.appid
        }
        const response = await this.request.dataGet(this, url, parameters)
        if (response.data.code === 1) {
          that.version = response.data.data
          console.log(this.keyValues)
        } else {
          alert('版本获取失败')
        }
      } catch (e) {
        alert(e)
      }
    }
  },

  watch: {}
}
</script>

<style>
  .modal{
    border: 1px solid #524d4d;
    border-radius: 5px;
  }
  .pd5{
    padding: 5px;
  }
  .pd2{
    padding-top: 3px;
  }
  .dialog-head {
    height: 50px;
    border-bottom: #544848 solid 1px;
    display: flex;
    flex: row;
    align-items: center;
    justify-content: space-between;
    padding-left: 10px;
    font-size: 18px;
    flex-shrink: 0;
  }

  .dialog-head .close-button {
    color: #999;
    margin-right: 10px;
    font-size: 20px;
    font-weight: bold;
    cursor: pointer;
  }

  .dialog-head .close-button:hover {
    color: #000;
  }

  .dialog-body {
    flex: 1;
    display: flex;
    flex-flow: column;
    overflow:auto
  }

  .dialog-body .tab-body {
    flex: 1;
    margin-top: 10px;
    display: flex;
    flex-flow: column;
    flex-wrap: wrap;
  }

  .tab-body::-webkit-scrollbar {
    width: 0.5rem;
    height: 0.5rem;
    background: #ddd;
  }

  .tab-body::-webkit-scrollbar-track {
    border-radius: 0;
  }

  .tab-body::-webkit-scrollbar-thumb {
    border-radius: 0;
    background: #aaa;
    transition: all .2s;
  }

  .tab-body::-webkit-scrollbar-thumb:hover {
    background-color: #bbb;
  }

  .tab-body::-webkit-scrollbar-corner {
    background: transparent;
  }

  .dialog-footer {
    height: 80px;
    /* border-top: #ddd solid 1px; */
    display: flex;
    flex-flow: row;
    align-items: center;
    justify-content: flex-end;
    padding-right: 50px;
  }

  .dialog-button {
    display: flex;
    flex-flow: row;
    font-size: 14px;
    border-radius: 3px;
    padding: 0 10px;
    height: 33px;
    align-items: center;
    color: #fff;
    background: #75b325;
  }
  .dialog-buttonf {
    display: flex;
    flex-flow: row;
    font-size: 14px;
    border-radius: 3px;
    padding: 0 10px;
    height: 33px;
    align-items: center;
    color: #000;
    background: #fff;
    margin-left: 10px;
  }
  .dialog-button:hover{
    background-color: #9D9D9D;
    border-color: #9D9D9D;
  }
  .dialog-footer .dialog-button {
    margin-right: 10px;
  }

  .dialog-row {
    display: flex;
    flex-direction: row;
    margin: auto;
    margin-top: 10px;
  }
  .dialog-row-input{
    width: 200px;
    height: 33px;
    margin-right: 5px;
    border-radius: 1px;
    border-color: #E0E0E0;
    border: none;
  }
  .dialog-row-input:focus{
  }

</style>
