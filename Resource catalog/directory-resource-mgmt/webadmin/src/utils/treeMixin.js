/**
 * @Description:
 * @author: YaphetS丶yz
 * @date: 2020/5/7
 */
// 滚动条记录， 适用于 keep-alive 组件
export default {
  data() {
    var vm = this
    return {

    }
  },
  computed: {

  },
  mounted() {

  },
  methods: {

  },
  activated() {

  },
  watch: {
    // 默认点击Tree第一个节点
    treeData(val) {
      alert(1)
      if (val) {
        this.$nextTick(() => {
          document.querySelector('.el-tree-node__content').click()
        })
      }
    }
  }
}

