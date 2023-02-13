<template>
  <!-- 主列表 -->
  <el-table :data='tableData' stripe @expand-change="expandSelect" :expand-row-keys="expands" :header-cell-style="headerStyle" :cell-style="cellStyle" empty-text='暂无数据' class='el_tab_alage' :row-key='getRowKeys' @selection-change="handleSelectionChange">
    <!-- 单选框 -->
    <el-table-column align="center" width="50" label="" v-if="tableSelection.key === true && tableSelection.type === 'radio'">
      <template slot-scope="scope">
        <el-radio :label="scope.$index" v-model="radio" @change="handleTemplateRow(scope.$index, scope.row)">&nbsp;</el-radio>
      </template>
    </el-table-column>
    <!-- index索引 -->
    <el-table-column label="序号" type="index" width="50" align="center" v-if="tableSelection.key === true && tableSelection.type === 'index'"></el-table-column>
    <!-- 多选框 -->
    <el-table-column type="selection" width="50" align="center" v-if="tableSelection.key === true && tableSelection.type === 'selection'"></el-table-column>
    <!-- 列表表头-->
    <el-table-column type="expand" v-if="tableSelection.key === true && tableSelection.type === 'expand'">
      <template slot-scope="scope">
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item :label="index.label" v-for="(index, item) in tableLabel" :key='item' v-show="index.type === 'expand'">
            <span>{{scope.row[index.list]}}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>
    <template v-for="(index, item) in tableLabel">
      <el-table-column fit align='center' :key='item' :sortable='index.sort' v-if="index.type !== 'expand'" :label="index.label" :width="index.width" :show-overflow-tooltip="false" :prop="index.list">
        <template slot-scope="scope">
          <!-- 图片 -->
          <template v-if="index.type === 'image'">
            <el-image v-if="scope.row[index.list] !== ''" style="width: 100px; height: 50px;" :src="scope.row[index.list]">
            </el-image>
            <div v-else></div>
          </template>
          <!-- 头像 -->
          <template v-else-if="index.type === 'head'">
            <el-image v-if="!(scope.row[index.list] === '' || scope.row[index.list] === null)" style="width: 50px; height: 50px;" :src="scope.row[index.list]">
            </el-image>
            <div v-else></div>
          </template>
          <!-- 按钮 -->
          <template v-else-if="index.type === 'btn'">
            <el-button type="text" @click.native.prevent="index.method(scope.row, scope)">
              <u>{{scope.row[index.list]}}</u>
            </el-button>
          </template>
          <!-- 下拉 -->
          <template v-else-if="index.type === 'select'">
            <el-select v-model="scope.row[index.list]" @change="changeType($event, scope.row, item)" size="medium">
              <el-option v-for="item in index.options" :key="item.value" :label="item.label" :value="item.value">
              </el-option>
            </el-select>
          </template>
          <!-- 内容自定义 -->
          <template v-else-if="index.type === 'html'">
            <div v-html="index.code(scope.row)"></div>
          </template>
          <!-- 正常显示 -->
          <template v-else>
            {{scope.row[index.list]}}
          </template>
        </template>
      </el-table-column>
    </template>
    <!-- 正常按钮操作 -->
    <el-table-column fit align='center' :label="tableOption.label" :fixed="tableOption.fixed ? tableOption.fixed : false" :width="tableOption.width" v-if="tableOption.value === 0">
      <template style="margin-left: 30px;" slot-scope="scope">
        <el-button align='right' v-for="(value, item) in tableOption.options" :key='item' v-if="value.show ? value.show(scope.row) : true" :type="value.type ? value.type : 'text'" :style="value.style ? JSON.parse(value.style) : {}" :plain='value.plain ? value.plain : false' :round='value.round ? value.round : false' :size='value.size ? value.size : false' :icon="value.icon" @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>
<script>
export default {
  data() {
    return {
      radio: '',
      expands: [],
      getRowKeys(row) {
        return row.id
      }
    }
  },
  props: {
    tableData: {
      type: Array,
      default: () => { }
    },
    tableSelection: {
      type: Object,
      default: () => { }
    },
    tableLabel: {
      type: Array,
      default: () => { }
    },
    tableOption: {
      type: Object,
      default: () => { }
    }
  },

  methods: {
    handleHref(row) {
      this.$router.push({ path: '/logManage/userLog/userLogs', query: { api: row.messageTitle } })
    },
    handleSelectionChange(val) {
      this.$emit('onHandleSelectionChange', val)
    },
    handleTemplateRow(index, row) {
      this.$emit('onHandleTemplateRow', row)
    },
    changeType(event, row) {
      this.$emit('onChangeType', event, row)
    },
    headerStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    cellStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },
    expandSelect(row, expandedRows) {
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

<style lang="scss" scoped>
/deep/ .el-table__row {
  cursor: pointer !important;
}
/deep/ .el-table th,
.el-table tr {
  cursor: pointer !important;
}
.demo-table-expand {
  font-size: 0;
}
/deep/ .gutter {
  display: inline !important;
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
  border: 1px solid #f0f0f0 !important;
  border-bottom: none !important;
  margin: {
    left: 0px;
    bottom: 20px !important;
    top: 20px !important;
  }

  .el-table__row {
    .cell {
      -webkit-line-clamp: 3 !important;
      -webkit-box-orient: vertical !important;
    }
    .el-button {
      margin: {
        top: 0 !important;
        right: 5px !important;
        bottom: 0 !important;
        left: 0 !important;
      }
    }
  }
}
</style>
