<template>
  <el-table
    :data="tableData"
    empty-text="暂无数据"
    class="el_tab_alage"
    min-height="400"
    ref="multipleTable"
    @selection-change="handleSelectionChange"
    @select-all="handleSelectAll"
    @select="handleSelect"
  >
    <template v-for="(index, item) in tableLabel">
      <el-table-column
        fit
        :key="item"
        :prop="index.list"
        :label="index.label"
        :type="index.type"
        :width="index.width"
        align="center"
      >
        <template slot-scope="scope">
          <template v-if="scope.row[index.list] === '禁用'">
            <span class="redd">{{scope.row[index.list]}}</span>
          </template>
          <template v-else-if="index.label === '系统略缩图'||index.label === '系统图标'">
            <img :src="scope.row.pic" width="40" height="40" class="head_pic" />
          </template>
          <template v-else-if="index.label === '系统路径'">
           <a :href="scope.row.srcUrl" >{{scope.row.srcUrl}}</a>
          </template>
          <template v-else-if="index.type === 'operation'">
            <el-button
              v-for="(item, index) in scope.row[index.list]"
              size="mini"
              :type="item.type"
              :plain="item.State"
              :key="index"
              @click.native.prevent="operationClick(scope.row, scope, item.click)"
            >{{item.label}}</el-button>
          </template>
          <template v-else>{{scope.row[index.list]}}</template>
        </template>
      </el-table-column>
    </template>
    <el-table-column
      fit
      align="center"
      :width="tableOption.width"
      :label="tableOption.label"
      v-if="tableOption.value === 0"
    >
      <template slot-scope="scope">
        <el-button
          size="mini"
          v-for="(value, item) in tableOption.options"
          :key="item"
          :type="value.type"
          :plain="value.State"
          :icon="value.icon"
          @click.native.prevent="value.method(scope.row, scope)"
        >{{value.label}}</el-button>
      </template>
    </el-table-column>
    <el-table-column fit align="center" :label="tableOption.label" v-if="tableOption.value === 1">
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <el-button
            size="mini"
            :type="value.type"
            :plain="value.State"
            :icon="value.icon"
            v-if="scope.row.status === '待审批'"
            :key="item"
            @click.native.prevent="value.method(scope.row, scope)"
          >{{value.label}}</el-button>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 2"
    >
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <template v-if="item === 0">
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
            >{{scope.row.statusBtn}}</el-button>
          </template>
          <template v-else>
            <el-button
              size="mini"
              :type="value.type"
              :plain="value.State"
              :icon="value.icon"
              :key="item"
              @click.native.prevent="value.method(scope.row, scope)"
            >{{value.label}}</el-button>
          </template>
        </template>
      </template>
    </el-table-column>
    <el-table-column
      fit
      align="center"
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === 3"
    >
      <template slot-scope="scope" v-if="scope.row.id !== 'admin'">
        <el-button
          size="mini"
          v-for="(value, item) in tableOption.options"
          :key="item"
          :type="value.type"
          :plain="value.State"
          :icon="value.icon"
          @click.native.prevent="value.method(scope.row, scope)"
        >{{value.label}}</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
export default {
  data() {
    return {
      Select: ""
    };
  },

  props: {
    tableData: {
      type: Array,
      default: () => {}
    },
    tableLabel: {
      type: Array,
      default: () => {}
    },
    tableOption: {
      type: Object,
      default: () => {}
    },
    tableStyle: {
      type: Array,
      default: () => {}
    }
  },

  methods: {
    operationClick(event, row, type) {
      this.$emit(type, event);
    },
    nitialization_check(Select) {
      let that = this;
      that.Select = Select;
      that.$refs.multipleTable.toggleRowSelection(Select, true);
    },

    handleSelectionChange(val) {
      this.$emit("onHandleSelectionChange", val);
    },

    handleSelectAll(val, row) {
      this.$emit("onHandleSelectAll", val, row);
    },

    handleSelect(val, row) {
      this.$emit("onHandleSelect", row);
    }
  }
};
</script>

<style scoped lang="scss">
.redd {
  color: red;
}
.el_tab_alage {
  width: 98%;
  border: 1px solid #f0f0f0;
  margin: {
    bottom: 20px;
    top: 10px;
  }
  .el-table__row {
    @media screen and (max-width: 1020px) {
      .el-button {
        margin: {
          top: 0px !important;
          left: 10px !important;
          bottom: 0px !important;
          right: 0px !important;
        }
      }
    }
    @media screen and (max-width: 1360px) {
      .el-button + .el-button {
        margin: {
          top: 0px !important;
          bottom: 0px !important;
          right: 0px !important;
        }
      }
    }
  }
}
</style>
