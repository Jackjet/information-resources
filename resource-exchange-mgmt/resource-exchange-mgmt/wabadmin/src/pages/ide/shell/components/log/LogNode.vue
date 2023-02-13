<template>
  <div class="tree-node">
    <template>
      <div class="logoBox">
        <div class="content-log" id="contentLog">
          <div style="text-align:right;border-bottom: 1px solid #D7D7D7;">
            <div class="inile pd5" @click="empty"><i class="fa fa-eraser"></i></div>
          </div>
          <div class="box drag-cancel" ref="scro" id="copyContent">
            <div class="item" v-for="(item, index) in consoleArr" :style="'color:'+item.color">
              <div class="date">{{ item.time }}&nbsp;{{ item.level }}&nbsp;{{ item.message }}</div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </div>
</template>
<script>
export default {
  props: {
    // 模态框头
  },
  data() {
    return {
      arr: '',
      consoleArr: []
    }
  },
  mounted() {
    this.scrollToBottom()
    $rxbus.$on('consoleArrPushValue', this.consoleArrPushValue)
  },
  updated() {
    this.scrollToBottom()
  },
  methods: {
    consoleArrPushValue(obj) {
      this.consoleArr.push(obj)
    },
    scrollToBottom() {
      this.$nextTick(() => {
        var container = this.$refs.scro
        container.scrollTop = container.scrollHeight
      })
    },
    empty() {
      this.consoleArr = []
    },
  }
}
</script>

<style lang='scss' scoped>
::-webkit-scrollbar {
  display: none;
}

.pd5 {
  padding: 5px;
}

.inile {
  display: inline-block;
  cursor: pointer;
}

#contentLog .box {
  height: calc(100% - 70px) !important;
}

.btn {
  border: 1px solid #75b325;
  padding: 3px 7px;
  color: #fff;
  background: #75b325;
}

.content-log {
  position: relative;
  width: 100%;
  height: 180px;
  background-color: #333333;
  z-index: 5;
  overflow: auto;

  .item {
    display: flex;
    /*border-bottom: 1px solid #D7D7D7;*/
    font-size: 14px;
    text-align: left;
    word-break: break-all;

    div {
      padding: 5px 10px;
    }

    div:nth-child(n+1) {
      /*border-left: 1px solid #D7D7D7;*/
    }

    .date {
      width: 97%;
    }

    .name {
      width: 20%;
    }

    .msg {
      flex: 1;
    }
  }
}
</style>
