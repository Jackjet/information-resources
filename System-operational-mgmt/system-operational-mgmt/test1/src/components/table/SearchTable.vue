<template>
  <el-main>
    <el-col :span='24'>
      <el-form :inline='true' class='el-InputForm' v-if='tableForm.search'>
        <template v-for='(index, item) in tableForm.search'>
          <el-form-item :key='item' v-if="index.type === 'input'">
            <el-input clearable size='medium'
              :placeholder='index.placeholder'
              prefix-icon='el-icon-search'
              v-model="paramForm[index.model]">
            </el-input>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'number'">
            <el-input clearable size='medium'
              :min='0'
              onkeyup="this.value=this.value.replace(/[^\d.]/g,'');"
              :placeholder='index.placeholder'
              ondragenter='return false'
              @keydown.native="channelInputLimit"
              prefix-icon='el-icon-search'
              v-model.number="paramForm[index.model]">
            </el-input>
          </el-form-item>
          <el-form-item class='section' :key='item' v-if="index.type === 'section'">
            <el-col :span="11" class='price'>
              <el-input size='medium'
                type='input'
                class='price'
                onkeyup="this.value=this.value.replace(/[^\d.]/g,'');"
                :min='0'
                @keydown.native="channelInputLimit"
                :placeholder='index.placeholderStart'
                v-model.number="section[0]">
              </el-input>
            </el-col>
            <el-col class="line" :span="2">-</el-col>
            <el-col :span="11" class='price'>
              <el-input size='medium'
                type='input'
                class='price'
                onkeyup="this.value=this.value.replace(/[^\d.]/g,'');"
                @keydown.native="channelInputLimit"
                :min='0'
                :placeholder='index.placeholderEnd'
                v-model.number="section[1]">
              </el-input>
            </el-col>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'time'">
            <el-time-select clearable size='medium'
              class='time'
              unlink-panels
              v-model="paramForm[index.model]"
              :picker-options="{
                start: '08:30',
                step: '00:15',
                end: '18:30'
              }"
              :placeholder='index.placeholder'>
            </el-time-select>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'timerange'">
            <el-time-picker clearable size='medium'
              is-range
              class='time'
              :popper-append-to-body='false'
              v-model="paramForm[index.model]"
              range-separator="至"
              value-format='HH:mm:ss'
              :start-placeholder="index.placeholderStart"
              :end-placeholder="index.placeholderEnd">
            </el-time-picker>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'date'">
            <el-date-picker clearable size='medium'
              unlink-panels
              class='time'
              type='date'
              value-format='yyyy-MM-dd'
              :placeholder='index.placeholder'
              v-model='paramForm[index.model]'>
            </el-date-picker>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'daterange'">
            <el-date-picker clearable size='medium'
              unlink-panels
              class='daterange'
              type='daterange'
              value-format='yyyy-MM-dd'
              range-separator='至'
              :start-placeholder="index.placeholderStart"
              :end-placeholder="index.placeholderEnd"
              v-model='paramForm[index.model]'>
            </el-date-picker>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'datetime'">
            <el-date-picker clearable size='medium'
              unlink-panels
              type='datetime'
              value-format='yyyy-MM-dd HH:mm:ss'
              :placeholder="index.placeholder"
              v-model='paramForm[index.model]'>
            </el-date-picker>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'datetimerange'">
            <el-date-picker clearable size='medium'
              unlink-panels
              class='daterange'
              type='datetimerange'
              value-format='yyyy-MM-dd HH:mm:ss'
              range-separator='至'
              :start-placeholder="index.placeholderStart"
              :end-placeholder="index.placeholderEnd"
              v-model='paramForm[index.model]'>
            </el-date-picker>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'cascader'">
            <el-cascader
              clearable
              size='medium'
              class='el-forms'
              :options="options"
              v-model="paramForm[index.model]"
              :placeholder="item.placeholder">
            </el-cascader>
          </el-form-item>
          <el-form-item :key='item' v-if="index.type === 'select'">
            <el-select :placeholder='index.placeholder'
              :popper-append-to-body='false'
              v-model='paramForm[index.model]'
              clearable
              filterable
              :multiple='index.multiple'
              :allow-create='index.multiple'
              :default-first-option='index.multiple'
              size='medium'>
              <el-option v-for='(dates, index) in index.option' :disabled="dates.disabled" :label='dates.label' :value="dates.value" :key="index"></el-option>
            </el-select>
          </el-form-item>
        </template>
        <el-form-item v-if='tableForm.search.length > 0'>
          <el-button type="primary" size='medium' icon="el-icon-search" @click="submitQuery()">搜索</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span='24' style='text-align: left; padding: 0 2px;' >
      <template v-for='(item, index) in tableForm.item'>
        <el-button :key='index' type='primary' icon='el-icon-plus' plain size='mini' v-if="item === 'new' || item.type === 'new'" @click.native.prevent="item.method !==  undefined ? item.method() : ''">新增</el-button>
        <el-button :key='index' type='primary' icon="el-icon-delete" plain size='mini' v-if="item === 'delete' || item.type === 'delete'" @click.native.prevent="item.method !==  undefined ? item.method(deleteList) : ''">批量删除</el-button>
        <el-button :key='index' type='primary' icon='el-icon-document-copy' plain size='mini' v-if="item === 'import' || item.type === 'import'" @click.native.prevent="item.method !==  undefined ? item.method() : ''">导入表格</el-button>
        <el-button :key='index' type='primary' icon='el-icon-folder-opened' plain size='mini' v-if="item === 'export' || item.type === 'export'" @click.native.prevent="item.method !==  undefined ? item.method() : ''">导出表格</el-button>
      </template>
    </el-col>
    <el-col :span='24' v-if="tableForm.tab && tableForm.list">
      <el-table :data="tableForm.list.data" v-loading="false">
        <template v-for="(item, index) in tableForm.list.header">
          <el-table-column :key='index' :label="item.label" :prop="item.field">
              <template slot-scope="scope">
                {{scope.row[item.field]}}
              </template>
          </el-table-column>
        </template>
      </el-table>
    </el-col>
    <el-col :span='24' v-else-if='tableForm.list'>
      <el-table
        v-loading="loading"
        element-loading-text="拼命加载中"
        :data='tableForm.list.data'
        empty-text='暂无数据'
        class='el_tab_alage'
        :default-sort="{
          prop: 'createTime',
          order: 'descending'
        }"
        :row-key='getRowKeys'
        :expand-row-keys="expands"
        @expand-change="expandSelect"
        @selection-change="handleSelectionChange">
        <el-table-column align="center" width="50" label="" v-if="tableForm.list.type === 'radio'">
          <template slot-scope="scope">
            <el-radio :label="scope.$index" v-model="radio" @change="handleTemplateRow(scope.$index, scope.row)">&nbsp;</el-radio>
          </template>
        </el-table-column>
        <el-table-column label="序号" type="index" width="50" align="center" v-if="tableForm.list.type === 'index'"></el-table-column>
        <el-table-column type="selection" width="50" align="center" v-if="isSelection"></el-table-column>
        <el-table-column type="expand" v-if="tableForm.list.detaile">
          <template slot-scope="scope">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item :label="indexs.label"  v-for="(indexs, items) in tableForm.list.header" :key='items' v-show="indexs.expand">
                <span>{{scope.row[indexs.field]}}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <template v-for="(index, item) in tableForm.list.header">
          <el-table-column fit
            align='center'
            :key='item'
            :sortable='index.sort'
            v-if="!index.expand"
            :label="index.label"
            :prop="index.field">
            <template slot-scope="scope">
              <template v-if="index.type === 'image'">
                <el-image
                  style="width: 100px; height: 50px;"
                  :preview-src-list="[scope.row[index.field]]"
                  :src="scope.row[index.field]">
                </el-image>
              </template>
               <template v-else-if='index.type === "time"'>
                <i class="el-icon-time"></i>
                {{scope.row[index.field]}}
              </template>
              <template v-else>
                {{scope.row[index.field]}}
              </template>
            </template>
          </el-table-column>
        </template>
        <el-table-column fit align='center'
          :label="tableForm.list.operation.label"
          v-if="tableForm.list.operation">
          <template slot-scope="scope">
            <el-button
              v-for="(value, item) in tableForm.list.operation.event"
              type="text"
              :icon="value.icon"
              :key='item'
              @click.native.prevent="value.method !== undefined ? value.method(scope.row, scope) : ''">{{value.label}}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span='24' v-if='tableForm.paging'>
      <div class="pagination">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="tableForm.paging.currentPage"
          :page-sizes="pageSizes"
          :page-size="tableForm.paging.limit"
          layout="total, sizes, prev, pager, next, jumper" :total="tableForm.paging.total">
        </el-pagination>
      </div>
    </el-col>
  </el-main>
</template>

<script>
import area from '../js/area'

export default {
  props: {
    tableForm: {
      type: Object,
      default: () => {}
    }
  },

  data () {
    return {
      // 接收列表表单参数
      paramForm: {},
      // 加载状态 初始化加载状态
      loading: true,
      // 复选框
      isSelection: false,
      // 存储批量删除返回值
      deleteList: '',
      // 定义单选框默认值
      radio: '',
      // 存储单选框 复选框选中数据
      expands: [],
      // 定义分页展示条数规则
      pageSizes: [10, 20, 50, 100],
      /**
       * 接收父组件传递分页数值
       * @param page  当前页码
       * @param limit 总页数
       */
      page: {
        page: '',
        limit: ''
      },
      // 记录选择区间传值
      section: [],
      // 获取省市区三级联动 数据集合
      options: area
    }
  },

  mounted () {
    const table = this.tableForm
    // 是否存在搜索项目
    const models = table.search || []

    // 解析表单字段 赋值对象
    const mapChange = () => {
      let newObj = {}
      for (let item of models.values()) {
        newObj[item.model] = ''
      }
      return newObj
    }

    this.paramForm = mapChange()

    // 是否存在表头操作
    const checkbox = table.item || []

    checkbox.forEach(element => {
      // 并且开启批量操作 连带显示复选框
      if (element === 'delete' || element.type === 'delete') {
        this.isSelection = true
      }
    })

    // 如果存在需要显示列表分页 未当前子组件赋值
    if (table.paging) {
      this.page.page = table.paging.currentPage
      this.page.limit = table.paging.limit
    }

    /**
     * 获取三级联动 省市区数据 将区域代码转换为地区昵称
     */
    for (let first of area) {
      first.value = first.label
      // 二级联动匹配
      if (first.children && first.children !== []) {
        for (let level of first.children) {
          level.value = level.label
          // 三级联动匹配
          if (level.children && level.children !== []) {
            for (let last of level.children) {
              last.value = last.label
            }
          }
        }
      }
    }
  },

  methods: {
    /**
     * 执行列表模糊查询
     */
    submitQuery () {
      /**
       * 获取区间值筛选空字段
       * 如果存在空值移除该字段
       */
      if (this.section[0] === '' && this.section[1] === '') {
        this.section = []
      } else {
        this.section.map((item) => {
          return item !== '' ? item : ''
        })
      }

      for (let section of this.tableForm.search) {
        if (section.type === 'section') {
          this.paramForm[section.model] = this.section
        }
      }

      for (let [key, value] of Object.entries(this.paramForm)) {
        // 是否为数组字段
        if (Array.isArray(value)) {
          let Strings = ''

          // 整理数组 转换字符串已逗号分割 传递参数
          value.forEach((item, val) => {
            Strings += `${item},`
            this.paramForm[key] = Strings.slice(0, Strings.length - 1)
          })
        }

        // 筛选是否存在空字段 是否存在未查询字段
        if (value === '' || value === undefined || value === null || value.length === 0) {
          delete this.paramForm[key]
        }
      }

      // 向父组件传递查询参数
      this.$emit('initial', this.paramForm)
    },

    /**
     * 侦听表头复选框勾选事件
     */
    handleSelectionChange (item) {
      this.deleteList = item
    },

    getRowKeys (row) {
      return row.id
    },

    /**
     * 侦听表头单选框 选中事件 向父组件传递参数
     */
    handleTemplateRow (index, row) {
      this.$emit('onHandleTemplateRow', row)
    },

    /**
     * 实时更新勾选 复选框保存iD
     */
    expandSelect (row, expandedRows) {
      const that = this
      if (expandedRows.length) {
        that.expands = []
        if (row) {
          that.expands.push(row.id)
        }
      } else {
        // 如果当前勾选为空 清空目标数组
        that.expands = []
      }
    },

    // 自定义表单验证 禁止出入其他多余字符
    channelInputLimit (el) {
      let key = el.key
      if (key === 'e' || key === '-' || key === '.' || key === '+' || key === '_') {
        el.returnValue = false
        return false
      }
    },

    /**
     * 改变分页总页数 向父组件传递分页数据 更新列表数据
     */
    handleSizeChange (val) {
      this.page.limit = val
      this.$emit('pageChange', this.page)

      this.loading = true
      this.$emit('initial')
    },

    /**
     * 改变分页页码 向父组件传递分页数据 更新列表数据
     */
    handleCurrentChange (val) {
      this.page.page = val
      this.$emit('pageChange', this.page)

      this.loading = true
      this.$emit('initial')
    },

    closeDialog () {}
  }
}
</script>

<style lang="scss">
  @import '../styles/table.scss';
</style>
