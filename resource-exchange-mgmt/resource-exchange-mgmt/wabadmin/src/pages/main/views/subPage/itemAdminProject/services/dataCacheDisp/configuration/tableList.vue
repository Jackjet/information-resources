<template>
  <!-- 主列表 -->
  <el-table
    :data='tableData'
    @expand-change="expandSelect"
    :expand-row-keys="expands"
    empty-text='暂无数据'
    class='el_tab_alage'
    :row-key='getRowKeys'
    @selection-change="handleSelectionChange">
    <!-- 多选框 -->
    <el-table-column type="selection" width="50" align="center" v-if="tableSelection.key === true && tableSelection.type === 'selection'"></el-table-column>
    <!-- 列表表头-->
    <el-table-column type="expand" v-if="tableSelection.detaile === true">
      <template slot-scope="scope">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item :label="index.label"  v-for="(index, item) in tableLabel" :key='item' v-show="index.type === 'expand'">
            <span>{{scope.row[index.list]}}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
    <template v-for="(index, item) in tableLabel">
      <el-table-column fit
                       align='center'
                       :key='item'
                       :sortable='index.sort'
                       v-if="index.type !== 'expand'"
                       :label="index.label"
                       :width="index.width"
                       :show-overflow-tooltip="true"
                       :prop="index.list">
        <template slot-scope="scope">
          <!-- 复选框 -->
          <template v-if="index.type === 'checked'">
            <el-checkbox
              v-for="item in scope.row[index.list]"
              :key="item.value"
              v-model="item.checked"
              :label="item.label"
              :value="item.value">
            </el-checkbox>
          </template>
          <!-- 正常显示 -->
          <template v-else>
            {{scope.row[index.list]}}
          </template>
        </template>
      </el-table-column>
    </template>
    <!-- 正常按钮操作 -->
<!--    <el-table-column fit align='center'-->
<!--                     :label="tableOption.label"-->
<!--                     :width="tableOption.width"-->
<!--                     v-if="tableOption.value === 0">-->
<!--      <template slot-scope="scope">-->
<!--        <template>-->
<!--          <el-button v-for="(value, item) in tableOption.options"-->
<!--                     :key='item'-->
<!--                     :type="value.type"-->
<!--                     :plain='value.State'-->
<!--                     :icon="value.icon"-->
<!--                     @click.native.prevent="value.method(scope.row, scope)">{{value.label}}-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </template>-->
<!--    </el-table-column>-->

    <el-table-column fit align='center'
                     :label="tableOption.label" :width="tableOption.width"
                     v-if="tableOption.value === 0">
      <template slot-scope="scope">
        <pictureButton :key="item" :type="value.type" :label="value.label"  v-for="(value, item) in tableOption.options" @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import pictureButton from '../../../../../integration/pictureButton'
export default {
  components: { pictureButton },
  data () {
    return {
      radioService: '',
      radio: '',
      expands: [],
      getRowKeys (row) {
        return row.id
      }
    }
  },

  props: {
    tableData: {
      type: Array,
      default: () => {}
    },
    tableSelection: {
      type: Object,
      default: () => {}
    },
    tableLabel: {
      type: Array,
      default: () => {}
    },
    tableOption: {
      type: Object,
      default: () => {}
    }
  },

  methods: {
    handleSelectionChange (val) {
      this.$emit('onHandleSelectionChange', val)
    },

    expandSelect (row, expandedRows) {
      const that = this
      if (expandedRows.length) {
        that.expands = []
        if (row) {
          that.expands.push(row.id)
        }
      } else {
        that.expands = []
      }
    }
  }
}
</script>

<style lang="scss">
  .demo-table-expand {
    font-size: 0;
  }

  .el-table thead th {
    background-color: #f9f9f9;
  }

  .demo-table-expand label {
    width: 90px !important;
    color: #99a9bf !important;
  }

  .demo-table-expand .el-form-item {
    margin-right: 0 !important;
    margin-bottom: 0 !important;
    width: 50%;
  }

  .el-table--scrollable-x .el-table__body-wrapper::-webkit-scrollbar {
    overflow-x: hidden;
  }

  .el-table .el-table__body-wrapper::-webkit-scrollbar-thumb {
    background-color: rgba(169, 178, 196, 0.3) !important;
  }

  .el-table__fixed-right {
    height: calc(100% - 27px) !important;
  }

  .el_tab_alage {
    width: 99.5% !important;
    border:1px solid #f0f0f0 !important;
    border-bottom: none !important;
    margin: {
      left: 2px;
      bottom: 20px !important;
      top: 10px !important;
    }

    .el-table__row {
      .cell {
        -webkit-line-clamp: 3 !important;
        -webkit-box-orient: vertical !important;
      }
      .el-button {
        margin: {
          top: 0 !important;
          right: 10px !important;
          bottom:0 !important;
          left: 0 !important;
        }
      }
    }
  }
</style>
