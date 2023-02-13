<template>
  <!-- 主列表 -->
  <el-table
    :data='tableData'
    @expand-change="expandSelect"
    :expand-row-keys="expands"
    :max-height="tableSelection.tableHeight"
    empty-text='暂无数据'
    class='el_tab_alage'
    :row-key='getRowKeys'
    @selection-change="handleSelectionChange">
    <!-- 单选框 -->
    <el-table-column align="center" width="50" label="" v-if="tableSelection.key === true && tableSelection.type === 'radio'">
      <template slot-scope="scope">
        <el-radio :label="scope.$index" v-model="radio" @change="handleTemplateRow(scope.$index, scope.row)">&nbsp;</el-radio>
      </template>
    </el-table-column>
    <!-- 特定单选框 选择服务页面 -->
    <el-table-column align="center" width="50" label="" v-if="tableSelection.key === true && tableSelection.type === 'radioService'">
      <template slot-scope="scope">
        <el-radio :label="scope.row.id" v-model="radioService" @change="handleTemplateRow(scope.$index, scope.row)">&nbsp;</el-radio>
      </template>
    </el-table-column>
    <!-- index索引 -->
    <el-table-column label="序号" type="index" width="50" align="center" v-if="tableSelection.key === true && tableSelection.type === 'index'"></el-table-column>
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
          <!-- 图片 -->
          <template v-if="index.type === 'image'">
            <el-image
              v-if="scope.row[index.list] !== ''"
              style="width: 100px; height: 50px;"
              :src="scope.row[index.list]">
            </el-image>
            <div v-else></div>
          </template>
           <!-- 列表图标 -->
           <template v-if="index.type === 'icon'">
             <el-image
               v-if="scope.row[index.list] !== ''"
               style="width: 20px; height: 20px;"
               :src="scope.row[index.list]" :title="scope.row[index.title]">
             </el-image>
             <div v-else></div>
           </template>
          <!-- 头像 -->
          <template v-else-if="index.type === 'head'">
            <el-image
              v-if="!(scope.row[index.list] === '' || scope.row[index.list] === null)"
              style="width: 50px; height: 50px;"
              :src="scope.row[index.list]">
            </el-image>
            <div v-else></div>
          </template>
          <!-- 按钮 -->
          <template v-else-if="index.type === 'btn'">
            <el-button type="text"
              @click.native.prevent="index.method(scope.row, scope)">
              <u>{{scope.row[index.list]}}</u>
            </el-button>
          </template>
          <!-- 打包页面 按钮 -->
          <template v-else-if="index.type === 'btnToPack' && scope.row.status === '失败'">
            <el-button type="text" style="text-align: center;margin-right: 0px !important;"
              @click.native.prevent="index.method(scope.row, scope)">
              <u style="color: red;">{{scope.row[index.list]}}</u>
            </el-button>
          </template>
          <!-- 服务列表页 运行状态 -->
          <template v-else-if="index.type === 'serviceStatus'">
            <span v-if="scope.row.type_c === '离线'" style="color: red">{{scope.row[index.list]}}</span>
            <span v-else style="color: green">{{scope.row[index.list]}}</span>
          </template>
          <!-- 下拉 -->
          <template v-else-if="index.type === 'select'">
            <el-select v-model="scope.row[index.list]"
             @change="changeType($event, scope.row, item)"
             size="medium">
              <el-option
                v-for="item in index.options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
          </template>
          <!-- 复选框 -->
          <template v-else-if="index.type === 'checked'">
            <el-checkbox
              v-for="item in scope.row[index.list]"
              :key="item.value"
              v-model="item.checked"
              :label="item.label"
              :value="item.value">
            </el-checkbox>
          </template>
          <!-- 内容自定义 -->
          <template v-else-if="index.type === 'html'">
            <div v-html="index.code(scope.row)"></div>
          </template>
          <!-- 按钮 -->
          <template v-else-if="index.type === 'nav'">
            <router-link
              tag="a"
              target="_blank"
              :to="{
                name: index.path,
                query: {
                  url: scope.row[index.list]
                }
              }"
            >安装文档</router-link>
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
<!--      :label="tableOption.label"-->
<!--      :width="tableOption.width"-->
<!--      v-if="tableOption.value === 0">-->
<!--      <template slot-scope="scope">-->
<!--        <template v-if="tableOption.type === 'checked'">-->
<!--          <el-checkbox @change="changeCheckbox(scope.row, scope)"></el-checkbox>-->
<!--        </template>-->
<!--        <template v-else>-->
<!--          <el-button v-for="(value, item) in tableOption.options"-->
<!--            :key='item'-->
<!--            :type="value.type"-->
<!--            :plain='value.State'-->
<!--            :icon="value.icon"-->
<!--            @click.native.prevent="value.method(scope.row, scope)">{{value.label}}-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </template>-->
<!--    </el-table-column>-->
    <el-table-column fit align='center'
      :label="tableOption.label" :width="tableOption.width"
      v-if="tableOption.value === 0">
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <pictureButton :key="item" :type="value.type" :label="value.label" @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
        </template>
      </template>
    </el-table-column>
    <!-- 通过状态显示按钮操作 分开设置 -->
    <!-- 组件配置列表 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === '组件配置'">
      <template slot-scope="scope">
        <el-button
          v-for="(value, item) in tableOption.options"
          :type="value.type"
          :plain='value.State'
          :icon="value.icon"
          :key='item'
          v-if="!(item === 1 && scope.row.repoType !== 'official')"
          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
    <!-- 自定义脚本-组件配置列表的删除按钮 -->
    <el-table-column fit align='center'
                     :label="tableOption.label"
                     :width="tableOption.width"
                     v-if="tableOption.value === 'custom_config'">
      <template slot-scope="scope">
        <template v-for="(value, item) in tableOption.options">
          <pictureButton :key="item" :type="value.type" :label="value.label" v-if="!((item === 0 && scope.row.componentName === 'dcrun') || (item === 1 && scope.row.repoType === '其他组件库'))" @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
        </template>
      </template>
    </el-table-column>
    <!-- 组件打包列表 -->
    <el-table-column fit align='center'
      :label="tableOption.label"
      :width="tableOption.width"
      v-if="tableOption.value === '组件打包'">
      <template slot-scope="scope">
        <el-button
          v-for="(value, item) in tableOption.options"
          :type="value.type"
          :plain='value.State'
          :icon="value.icon"
          :key='item'
          v-if="!(item === 1 && scope.row.status !== '成功')"
          @click.native.prevent="value.method(scope.row, scope)">{{value.label}}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script>
import pictureButton from './pictureButton'
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

    handleTemplateRow (index, row) {
      this.$emit('onHandleTemplateRow', row)
    },

    clearRadio () {
      this.radio = ''
    },
    clearRadioService () {
      this.radioService = ''
    },
    chooseRadioService (val) {
      this.radioService = val
    },

    changeType (event, row) {
      this.$emit('onChangeType', event, row)
    },
    changeCheckbox (event, row) {
      this.$emit('changeCheckbox', event)
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
