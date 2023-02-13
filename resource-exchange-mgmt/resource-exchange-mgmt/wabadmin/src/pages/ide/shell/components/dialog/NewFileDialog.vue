<template>
  <div v-if="inputValue" class="modal-mask" @click="inputValue = false">
    <div
      class="modal"
      :style="{
        top : top,
        left : left,
        width :width,
        height : height,
        background: background,
        color: color,
        border: border
      }"
      @click="modalClick">
      <slot>
        <div class="dialog-head">
          <div>
            <i class="fas fa-layer-group"></i>
            <template v-if="type ===0">{{$t('widgets.new-dir')}}</template>
            <template v-else-if="type ===1">{{$t('widgets.new-file')}}</template>
            <template v-else>{{$t('widgets.rename')}}</template>

          </div>
          <span class="close-button" @click="inputValue = false">×</span></div>
        <div class="dialog-body">
          <div style="margin-left:20px; margin-top: 30px;margin-right: 20px">

            <template v-if="type ===0 || type ===2">{{$t('widgets.dir-name')}}</template>
            <template v-else-if="type ===1 || type ===3">{{$t('widgets.file-name')}}</template>
            <template v-else>{{$t('widgets.file-dir')}}</template>
            ：
            <input style="height: 30px;width: 80%" autocomplete="off" v-model="name"/>
          </div>
        </div>
      </slot>
      <div class="dialog-footer">
        <div class="dialog-button cancel-btn" @click="inputValue = false">{{$t('widgets.cancel')}}</div>
        <div class="dialog-button confirm-btn" @click="confirm">

          <template v-if="type ===0 || type ===1 ">{{$t('widgets.create')}}</template>
          <template v-else>{{$t('widgets.rename')}}</template>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'NewFileDialog',
    components: {},
    props: {
      value: {default: false},
      width: {default: '550px'},
      height: {default: '200px'},
      top: {default: '225px'},
      left: {default: 'calc(50% - 300px)'},
      background: {default: '#191919'},
      color: {default: '#fff'},
      border: {default: '1px solid #544848'},
      type: 0,
      fileName: ''
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
        name: this.fileName
      }
    },

    mounted () {

    },

    methods: {
      modalClick (event) {
        event.stopPropagation()
      },
      confirm () {
        this.$emit('callback', this.name, this.type)
        this.inputValue = false
        this.name = ''
      }
    },

    watch: {}
  }
</script>

<style>
  .modal {
    border: 1px solid #524d4d;
    border-radius: 5px;
  }

  .dialog-head {
    height: 50px;
    border-bottom: #ddd solid 1px;
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
    height: 70px;
    /* border-top: #ddd solid 1px; */
    display: flex;
    flex-flow: row;
    align-items: center;
    justify-content: flex-end;
    padding-right: 50px;
  }

  .dialog-footer .dialog-button {
    font-size: 14px;
    padding: 0 30px;
    margin-right: 10px;
    cursor: pointer;
    border-radius: 3px;
    height: 36px;
    display: flex;
    align-items: center;
    border-radius: 18px;
  }

  .dialog-footer .dialog-button.confirm-btn {
    color: #fff;
    background: #75b325;
  }

  .dialog-footer .dialog-button.confirm-btn:hover {
    background: #649a1f;
  }

  .dialog-footer .dialog-button.cancel-btn {
    color: #6c757d;
    background: #fff;
    border: #95a0aa solid 1px;
  }

  .dialog-footer .dialog-button.cancel-btn:hover {
    background: #f9f9f9;
  }

  .dialog-button.disabled-label {
    background: #bbb;
    color: #eee;
    cursor: default;
  }

  .modal-mask {
    position: fixed;
    z-index: 9999;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5);
  }

  .modal-mask .modal {
    position: fixed;
    top: 50%;
    left: 50%;
    background: #fff;
    box-shadow: 3px 3px 6px 3px rgba(0, 0, 0, 0.1);
    transform: all 0.3s;
    display: flex;
    flex-flow: column;
    color: #474747;
  }

</style>
