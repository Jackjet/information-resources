<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col ref="treeDomParents" :xl="4" :lg="5" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">数据元分类</span>
            <el-tooltip :content="treeExpansion ? '收起' : '展开'" effect="light" :open-delay="1000" placement="bottom">
              <hamburger style="float: right;position: relative;top: 5px;height: 30px" :toggle-click="collapseTree" :is-active="treeExpansion" class="hamburger-container" />
            </el-tooltip>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-input v-model="filterText" class="filter_input" clearable placeholder="输入编码名称" suffix-icon="el-icon-search" />
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" v-loading="treeLoading" accordion :data="treeData" :props="defaultProps" highlight-current node-key="id" :expand-on-click-node="false" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :filter-node-method="filterNode" @node-click="handleNodeClick">
                <span slot-scope="{ node, data }" class="custom-tree-node overflowEllips">
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="node.label" placement="right">
                    <span class="overflowEllips">{{ node.label }}</span>
                  </el-tooltip>
                  <div />
                </span>
              </el-tree>
            </el-scrollbar>
          </div>
        </el-row>
      </el-col>
      <!--tab选择区域-->
      <el-col ref="contentDomParents" :xl="20" :lg="19" class="tab_area">
        <div class="type_area">{{ rightTitle }}</div>
        <div class="operate_area">
          <div class="related_btns">
            <el-button v-permission="['POST /admin/sysDict/add']" class="add" size="mini" icon="el-icon-plus" @click="addmenu">新增</el-button>
            <el-button class="add" size="mini" @click="showCodenIfo(tableOption,'详情')">详情</el-button>
            <el-button class="add" size="mini" @click="listDialogFun">沉余分析</el-button>
            <el-input v-model="listQuery.value" placeholder="编码代码" clearable class="filter-item" style="width: 20%" @clear="handleFilter">
            </el-input>
            <el-input v-model="listQuery.name" placeholder="编码名称" clearable class="filter-item" style="width: 20%" @clear="handleFilter">
            </el-input>
            <el-button v-permission="['GET /admin/sysDict/list']" class="remove" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>
            <el-button v-permission="['GET /admin/sysDict/list']" class="remove" size="mini" icon="el-icon-delete" @click="qingkong">重置</el-button>
          </div>

          <el-table v-loading="tableLoading" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%">
            <el-table-column width="55" align="center">
              <template slot-scope="scope">
                <el-radio :label="scope.row.id" v-model="radioValue" @change="changeRedio($event,scope.row)">&nbsp;</el-radio>
              </template>
            </el-table-column>
            <el-table-column type="index" width="50" label="序号" />
            <el-table-column prop="value" label="编码代码" width="180" show-overflow-tooltip />
            <el-table-column prop="name" label="编码名称" width="180" show-overflow-tooltip />
            <el-table-column label="上级编码名称" show-overflow-tooltip>
              <template>
                <span>{{ rightTitle }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数据编码类型" prop="type" />
            <el-table-column label="操作" width="140" fixed="right">
              <template slot-scope="scope">
                <el-tooltip content="详情" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="detail" class-name @click="showCodenIfo(scope.row,'详情')" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['POST /admin/sysDict/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row,'编辑')" />
                  </span>
                </el-tooltip>
                <el-tooltip content="关联关系图" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="guanxi" class-name @click="routerChart(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip v-permission="['GET /admin/sysDict/delete']" content="移除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>
        <el-dialog :modal-append-to-body="false" title="沉余分析" align="center" :visible.sync="listDialog" width="48%" :close-on-click-modal="false">
          <el-table :data="tableDataDialog" stripe :header-cell-style="getRowClass" style="width: 100%">
            <el-table-column type="index" width="50" label="序号" />
            <el-table-column prop="value" label="编码代码" width="180" show-overflow-tooltip />
            <el-table-column prop="name" label="编码名称" width="180" show-overflow-tooltip />
            <el-table-column label="上级编码名称" show-overflow-tooltip>
              <template>
                <span>{{ rightTitle }}</span>
              </template>
            </el-table-column>
            <el-table-column label="数据编码类型" prop="type" />
          </el-table>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="remove" @click="listDialog = false">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog v-loading="dialogLoading" :modal-append-to-body="false" title="关联关系图" align="center" :visible.sync="chartDialog" width="48%" :close-on-click-modal="false">
          <div ref="relationship" id="relationship" style="width:100%;height:500px;" />
          <div class="dialogDom" align="center">
            <el-button size="mini" class="remove" @click="chartDialog = false">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog :modal-append-to-body="false" :title="dialogName" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addCodeForm','addCodeForm')">
          <el-form ref="addCodeForm" :model="addCodeForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="上级代码名称：" label-width="120px" prop="applyUserId">
              <span>{{ rightTitle }}</span>
            </el-form-item>
            <el-form-item label="数据编码代码：" class="required_label" label-width="120px" prop="value">
              <el-input v-model="addCodeForm.value" style="width:100%" />
            </el-form-item>
            <el-form-item label="数据编码名称：" class="required_label" label-width="120px" prop="name">
              <el-input v-model="addCodeForm.name" style="width:100%" />
            </el-form-item>
            <el-form-item v-if="canEditbianmaType" label="数据编码类型：" class="required_label" label-width="120px" prop="type">
              <el-input v-model="addCodeForm.type" style="width:100%" readonly disabled />
            </el-form-item>
            <el-form-item v-if="!canEditbianmaType" label="数据编码类型：" class="required_label" label-width="120px" prop="type1">
              <el-input v-model="addCodeForm.type1" style="width:100%" />
            </el-form-item>
            <el-form-item label="备注：" label-width="120px">
              <el-input v-model="addCodeForm.remarks" :rows="5" type="textarea" style="width:100%" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button v-permission="['POST /admin/sysDict/add','POST /admin/sysDict/update']" size="mini" class="add" @click="dialogName=='新增'?addCodeDetail(0):addCodeDetail(1)">保 存</el-button>
            <el-button size="mini" class="remove" @click="hideDialog(2)">取 消</el-button>
          </div>
        </el-dialog>
        <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
          <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  dsysDictList,
  dsysDictGetTree,
  dsysDictGetByTypet,
  dsysDictGet,
  dsysDictDelete,
  sysDictUpdate,
  sysDictDictAdd,
  getNotUseList,
  getAnalysisList
} from '@/api/assetCata'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import Hamburger from '@/components/Hamburger'
import mixinJs from '@/utils/mixin'
import DelConfirm from '@/components/DelConfirm'
import echarts from 'echarts'
export default {
  name: 'DataEncoding',
  components: { Pagination, DelConfirm, Hamburger },
  mixins: [mixinJs],
  data() {
    return {
      chartData: {},
      tableDataDialog: [],
      listDialog: false,
      chartDialog: false,
      dialogLoading: false,
      radioValue: "",
      tableOption: null,
      tempDelData: {},
      tipStr: '确定要删除所选数据编码吗？',
      delDialog: false,
      bianmaType: '', // 编码类型
      canEditbianmaType: true, // 是否可以编辑编码类型编码类型
      addCodeForm: {},
      dialogName: '新增',
      addWorkOrder: false,
      filterText: '',
      rightTitle: '',
      showType: '',
      listQuery: {
        page: 1,
        size: 10
      },
      mapIcon: mapIcon,
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        value: [
          { required: true, message: '请输入代码', trigger: 'blur' },
          {
            validator: this.hasChina,
            message: '不可输入中文',
            trigger: 'blur'
          },
          {
            validator: this.hasCharacter,
            message: '不可输入特殊字符',
            trigger: 'blur'
          }
        ],
        type: [{ required: true, message: '请输入编码类型', trigger: 'blur' }],
        type1: [{ required: true, message: '请输入编码类型', trigger: 'blur' }]
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      rightTitle: '',
      tableLoading: false
    }
  },
  watch: {
    filterText(val) {
      this.$refs.treeDom.filter(val)
    }
  },
  created() {
    this.getTreeData()
  },
  methods: {
    listDialogFun() {
      this.listDialog = true;
      getNotUseList({ pid: this.listQuery.pid }).then(res => {
        if (res.data.errno === 0) {
          this.tableDataDialog = res.data.data.records;
        }
      })
    },
    routerChart(val) {
      this.chartDialog = true;
      this.dialogLoading = true;
      getAnalysisList({ id: val.id }).then(res => {
        if (res.data.errno === 0) {
          this.dialogLoading = false;
          let data = {
            data: [],
            links: []
          }
          let resData = res.data.data;
          if (resData.from) {
            data.data.push({
              name: resData.from.uviewNm,
              value: "来源",
              obj: resData.from,
              x: -300,
              y: 300
            })
          }
          data.data.push({
            name: resData.sysDict.name,
            value: "数据源",
            obj: resData.sysDict,
            x: 300,
            y: 300
          })
          data.links.push({
            source: resData.from.uviewNm,
            target: resData.sysDict.name
          })
          if (resData.to) {
            resData.to.forEach((item, index) => {
              if (index < 10) {
                data.data.push({
                  name: item.uviewNm,
                  value: "被使用",
                  obj: item,
                  x: 900,
                  y: index * 100
                })
                data.links.push({
                  source: resData.sysDict.name,
                  target: item.uviewNm
                })
              }
            })
          }

          // console.log(res.data.data, "0000000000")
          this.echartsFun(data)
        }
      }).catch(err => {
        // this.$message.error(err.data || '出错了')
      })

    },
    echartsFun(data) {
      // let data = {
      //   data: [
      //     {
      //       name: 'Node 1',
      //       value: "000",
      //       x: 100,
      //       y: 300
      //     },
      //     {
      //       name: 'Node 2',
      //       x: 300,
      //       y: 300
      //     },
      //     {
      //       name: 'Node 3',
      //       x: 500,
      //       y: 300
      //     },
      //     {
      //       name: 'Node 4',
      //       x: 500,
      //       y: 400
      //     },
      //   ],
      //   links: [
      //     {
      //       source: 'Node 2',
      //       target: 'Node 1'
      //     },
      //     {
      //       source: 'Node 2',
      //       target: 'Node 3'
      //     },
      //     {
      //       source: 'Node 2',
      //       target: 'Node 4'
      //     },
      //   ],
      // }

      // this.$nextTick(function () {
      let myChart = echarts.init(this.$refs.relationship)
      myChart.setOption({
        // tooltip: {
        //   formatter: function (e) {
        //     return e.name + "：" + e.value;
        //   }
        // },
        animationDuration: 1500,
        animationEasingUpdate: 'quinticInOut',
        series: [
          {
            type: 'graph',
            layout: 'none',
            symbolSize: 40,
            roam: true,
            label: {
              show: true
            },
            edgeSymbol: ['circle', 'arrow'],
            edgeSymbolSize: [4, 10],
            edgeLabel: {
              fontSize: 20
            },
            data: data.data,
            links: data.links
          }
        ]
      })
      // })
      myChart.on("click", (e) => {
        if (e.data.value === '来源') {
          const h = this.$createElement;
          this.$msgbox({
            title: '数据元来源',
            message: h('div', null, [
              h('p', null, '资源信息名称：' + e.data.obj.uviewNm),
              h('p', null, '资源信息代码：' + e.data.obj.uviewNo),
              h('p', null, '创建人：' + e.data.obj.creator),
              h('p', null, '发布时间：' + e.data.obj.crtDt),
              h('p', null, '更新时间：' + e.data.obj.updateDt),
            ]),
            confirmButtonText: '取消',
          });
        } else if (e.data.value === '数据源') {
          const h = this.$createElement;
          this.$msgbox({
            title: '数据元',
            message: h('div', null, [
              h('p', null, '编码名称：' + e.data.obj.name),
              h('p', null, '编码代码：' + e.data.obj.value),
              h('p', null, '创建时间：' + e.data.obj.createDate),
              h('p', null, '编码类型：' + e.data.obj.type),
            ]),
            confirmButtonText: '取消',
          });
        } else if (e.data.value === '被使用') {
          const h = this.$createElement;
          this.$msgbox({
            title: '被使用数据元',
            message: h('div', null, [
              h('p', null, '资源信息名称：' + e.data.obj.uviewNm),
              h('p', null, '资源信息代码：' + e.data.obj.uviewNo),
              h('p', null, '更新人：' + e.data.obj.updater),
              h('p', null, '发布时间：' + e.data.obj.crtDt),
              h('p', null, '更新时间：' + e.data.obj.updateDt),
            ]),
            confirmButtonText: '取消',
          });
        }
      })
    },
    changeRedio(event, row) {
      this.tableOption = row;
    },
    closedDialog(attr, formName) {
      this.$refs[formName].resetFields()
      this[attr] = {}
      this.$refs.selectTreeDom ? this.$refs.selectTreeDom.clearAll() : ''
    },
    addmenu() {
      this.dialogName = '新增'
      this.addCodeForm.type = this.showType
      this.addWorkOrder = true
    },
    delRelated(data) {
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      dsysDictDelete({ id: this.tempDelData.id })
        .then(res => {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getTableData()
          this.getTreeData(this.listQuery.pid)
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    addCodeDetail(num) {
      if (num == 0) {
        this.$refs.addCodeForm.validate(valid => {
          if (valid) {
            this.addCodeForm.pid = this.listQuery.pid
            this.addCodeForm.isInter = 0
            if (!this.canEditbianmaType) {
              this.addCodeForm.type = this.addCodeForm.type1
            }
            sysDictDictAdd(this.addCodeForm)
              .then(res => {
                this.addCodeForm = {}
                this.$notify.success('新增编码成功')
                this.addWorkOrder = false
                this.getTableData()
                this.getTreeData(this.listQuery.pid)
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        })
      } else if (num == 1) {
        this.$refs.addCodeForm.validate(valid => {
          if (valid) {
            this.addCodeForm.pid = this.listQuery.pid
            this.addCodeForm.isInter = 0
            sysDictUpdate(this.addCodeForm)
              .then(res => {
                this.addCodeForm = {}
                this.$notify.success('更新编码成功')
                this.addWorkOrder = false
                this.getTableData()
                this.getTreeData(this.listQuery.pid)
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        })
      }
    },
    showCodenIfo(data, title) {
      if (data) {
        this.addCodeForm = Object.assign({}, data)
        this.dialogName = title;
        this.addWorkOrder = true
      } else {
        this.$message('请选择一项查看内容！');
      }

    },
    hideDialog(num) {
      if (num == 2) {
        this.addWorkOrder = false
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getTableData()
    },
    qingkong() {
      this.listQuery.name = ''
      this.listQuery.value = ''
      this.handleFilter()
    },
    filterNode(value, data) {
      if (!value) return true
      return data.name.indexOf(value) !== -1
    },

    getTableData() {
      this.tableLoading = true
      dsysDictList(this.listQuery)
        .then(response => {
          this.tableData = response.data.data.records
          this.total = response.data.data.total
          if (response.data.data.records.length > 0) {
            this.canEditbianmaType = true
            console.log(response.data.data.records[0].type)
            this.addCodeForm.type = response.data.data.records[0].type
            this.showType = response.data.data.records[0].type
          } else {
            this.addCodeForm.type = ''
            this.canEditbianmaType = false
          }
          this.tableLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.tableLoading = false
        })
    },
    getTreeData(thisId) {
      this.treeLoading = true

      dsysDictGetTree({ roleId: null })
        .then(response => {
          this.treeData = response.data.data

          if (thisId != null) {
            this.currentNodekey = thisId
          } else {
            this.currentNodekey = this.treeData[0].id
          }
          setTimeout(() => {
            this.$refs.treeDom.setCurrentKey(this.currentNodekey) // 一定要加这个选中了否则样式没有出来
            this.rightTitle = this.$refs.treeDom.getCurrentNode().name
            this.listQuery.pid = this.$refs.treeDom.getCurrentNode().id
            this.getTableData()
          }, 20)
          this.treeLoading = false
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    handleNodeClick(data) {
      this.rightTitle = data.name
      this.listQuery.pid = data.id
      this.getTableData()
    },
    getRowClass({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 0) {
        return 'background: #F6F6F6;font-weight:bold;color:rgba(36,36,36,1);'
      } else {
        return ''
      }
    }
  }
}
</script>

<style scoped>
.tree_area .el-tree {
  width: 100%;
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
.department_content {
  background: #fff;
  border: 1px solid rgba(230, 229, 234, 1);
  border-radius: 6px;
}

.tree_title {
  padding-left: 30px;
  padding-right: 1vh;
  height: 60px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  line-height: 60px;
  opacity: 1;
}

.tree_content {
  padding-top: 24px;
  padding-left: 20px;
  padding-bottom: 24px;
  box-sizing: border-box;
}

.tree_city {
  margin-right: 12px;
  margin-left: 6px;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}

.tree_desc,
.type_area {
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
}
.tree_area {
  background: #f9f9f9;
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}

.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tab_area {
  height: 100%;
}
.operate_area {
  padding: 0 45px;
}
.related_btns {
  margin-top: 10px;
  margin-bottom: 20px;
}
.add {
  background: #1f3365;
  min-width: 80px;
  color: #fff;
  height: 36px;
}
.remove {
  color: #1f3365;
  min-width: 80px;
  border-color: #1f3365;
  height: 36px;
}
.tr_detail_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  text-align: center;
  margin-right: 14px;
  cursor: pointer;
}
.tr_detail_icon svg {
  cursor: pointer;
  width: 20px;
  height: 20px;
}
.tr_del_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50% 50%;
  border: 2px solid rgba(46, 78, 161, 1);
  border-radius: 50%;
  opacity: 1;
  text-align: center;
  position: relative;
  cursor: pointer;
}
.tr_del_icon svg {
  cursor: pointer;
  width: 10px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
</style>
<style>
.el-radio__inner::after {
  background: #409eff !important;
}
</style>

