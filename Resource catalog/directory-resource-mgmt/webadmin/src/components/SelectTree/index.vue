<template>
  <el-select
    v-model="mineStatus"
    :disabled="disabled"
    :multiple="isMultiple"
    :placeholder="placeholder!=null?placeholder:'所属部门'"
    @change="selectChange"
  >
    <el-option
      :value="mineStatusValue"
      style="height: auto;max-width:100%;background: #fff"
    >
      <el-scrollbar class="">
        <el-tree
          ref="treeForm"
          :data="selectTreedata"
          default-expand-all
          show-checkbox
          :node-key="nodeKey!=null?nodeKey:id"
          :default-checked-keys="selectIds"
          :check-on-click-node="true"
          :check-strictly="checkStrictly!=null?checkStrictly:!isMultiple"
          :props="defaultProps"
          @check-change="handleCheckChange"
        />
      </el-scrollbar>

    </el-option>
  </el-select>
</template>
<script>
export default {
  name: 'SelectTree', // 这个LoginName最好和引入的vue的LoginName相同
  props: {
    disabled: {
      type: [Boolean, String],
      default: false
    },
    isMultiple: Boolean, // 是否段暄
    selectTreedata: Array,
    checkStrictly: Boolean, // 是否子父组件完全关联
    placeholder: String, // 提示用语
    nodeKey: String, // key
    selectMoren: Array,
    selectIds: {
      type: [Array, String],
      default: []
    },
    label: {
      type: [String],
      default: ''
    }
  },
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: this.label || 'orgNm',
        id: 'orgId'
      },
      mineStatus: '',
      mineStatusValue: [],
      treeSelectNum: 0
    }
  },
  watch: {
    label: function(newVal, oldVal) {
      this.defaultProps.label = newVal
    },
    selectIds: {
      handler(newVal, oldVal) {
        if(oldVal.length >0){
            this.handleCheckChange()
        }else{
            this.init()
            this.handleCheckChange()
        }


      },
      deep: true
    }
  },
  created() {},
  mounted() {
    this.init()
  },
  methods: {
    clearAll() {
      this.$refs.treeForm.setCheckedKeys([])
    },
    selectChange(e) {
      var arrNew = []
      var dataLength = this.mineStatusValue.length
      var eleng = e.length
      for (let i = 0; i < dataLength; i++) {
        for (let j = 0; j < eleng; j++) {
          if (e[j] === this.mineStatusValue[i][this.defaultProps.label]) {
            arrNew.push(this.mineStatusValue[i])
          }
        }
      }
      this.$refs.treeForm.setCheckedNodes(arrNew) // 设置勾选的值
    },
    init() {
      if (this.selectIds.length == 0) {
        this.$refs.treeForm.setCheckedNodes([])
        return
      }
      if (!this.isMultiple) {
        this.treeSelectNum++
      }

      const res = this.checkStrictly
        ? this.$refs.treeForm.getCheckedNodes(false, true)
        : this.$refs.treeForm.getCheckedNodes(true, true)
      const arrLabel = []
      const arr = []
      res.forEach(item => {
        arrLabel.push(item[this.defaultProps.label])
        arr.push(item)
      })
      this.mineStatusValue = arr
      this.mineStatus = this.isMultiple
        ? arrLabel
        : arrLabel[0]
          ? arrLabel[0]
          : ''
      // 返回给父组件 mineStatus select显示的值（单选string 多选Array）
      // 返回给父组件 mineStatusValue 勾选的tree节点的data
    },
    handleCheckChange(data, checked, node) {
      console.log(data)
        console.log(this.treeSelectNum)
      if (data == null) {
        return
      }
      if (data.children == null) {
        this.treeSelectNum++
      } else {
        if (data.children.length > 0 && this.checkStrictly) {
          this.treeSelectNum++
        } else {
          this.treeSelectNum++
        }
      }
      let res
      console.log(this.treeSelectNum)
      if (this.isMultiple) {
      } else {
        if (this.treeSelectNum % 2 === 0) {
          if (checked) {
            this.$refs.treeForm.setCheckedNodes([])
            this.$refs.treeForm.setCheckedNodes([data])
            // 交叉点击节点
          } else {
            this.$refs.treeForm.setCheckedNodes([])
            // 点击已经选中的节点，置空
          }
        }
      }
      res = this.checkStrictly
        ? this.$refs.treeForm.getCheckedNodes(false, true)
        : this.$refs.treeForm.getCheckedNodes(true, true)
      console.log(res)
      const arrLabel = []
      const arr = []
      res.forEach(item => {
        arrLabel.push(item[this.defaultProps.label])
        arr.push(item)
      })
      this.mineStatusValue = arr
      this.mineStatus = this.isMultiple
        ? arrLabel
        : arrLabel[0]
          ? arrLabel[0]
          : ''
      // 返回给父组件 mineStatus select显示的值（单选string 多选Array）
      // 返回给父组件 mineStatusValue 勾选的tree节点的data
      this.$emit('checkedChoose', this.mineStatus, this.mineStatusValue)
    },
    cancelDel() {
      this.$emit('cancelDel')
    },
    confirmDel() {
      this.$emit('confirmDel')
    }
  }
}
</script>
<style scoped>
</style>
