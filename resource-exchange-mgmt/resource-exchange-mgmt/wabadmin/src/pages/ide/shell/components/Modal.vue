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
      }"
      @click="modalClick">
      <slot></slot>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Modal',
  props: {
    value: { default: '' },
    width: { default: '800px' },
    height: { default: 'calc(100vh - 180px)' },
    top: {default: '40px'},
    left: {default: 'calc(50% - 400px)'},
    background: {default: '#191919'},
    color: {default: '#fff'}
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
    }
  },

  methods: {
    modalClick (event) {
      event.stopPropagation()
    }
  }
}
</script>

<style>
  .modal{
    border: 1px solid #524d4d;
    border-radius: 5px;
  }
  .modal-mask{
    position: fixed;
    z-index: 9999;
    top:0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5);
  }
  .modal-mask .modal{
    position: fixed;
    top:50%;
    left:50%;
    background: #fff;
    box-shadow: 3px 3px 6px 3px rgba(0, 0, 0, 0.1);
    transform: all 0.3s;
    display: flex;
    flex-flow: column;
    color: #474747;
  }

</style>
