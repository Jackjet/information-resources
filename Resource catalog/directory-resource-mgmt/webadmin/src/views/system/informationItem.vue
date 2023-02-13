<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--tab选择区域-->
      <el-col class="tab_area">
        <div class="type_area">信息项管理</div>
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="20">
              <el-col :span="3" style="min-height: 1px">
                <el-button v-permission="['POST /admin/archBusiUviewStrConfig/save']" class="add" size="mini" icon="el-icon-plus" @click="addMenu">新增信息项</el-button>
              </el-col>
              <el-col :span="12" :offset="9">
                <el-col :span="16">
                  <el-input v-model="listQuery.itemNameAndItemMark" clearable class="filter-item" style="width: 100%;border-radius:25px" placeholder="信息项编码或名称" @clear="handleFilter" />
                </el-col>
                <el-col :span="8" align="right">
                  <el-button v-permission="['GET /admin/archBusiUviewStrConfig/list']" class="remove" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>
                </el-col>
              </el-col>
            </el-row>

            <el-table :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px">
              <el-table-column prop="itemMark" label="信息项编码" show-overflow-tooltip />
              <el-table-column prop="itemName" label="信息项名称" show-overflow-tooltip />
              <el-table-column prop="isChoice" label="必填（必选）" />
              <el-table-column prop="itemType" label="组件类型" show-overflow-tooltip>
                <template slot-scope="scope">
                  <el-select ref="selectDom" v-model="scope.row.itemType" class="moniSpan" placeholder="请选择组件类型" disabled>
                    <el-option v-for="item in serviceObjs" :key="item.value" :label="item.name" :value="item.value" />
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column prop="itemCode" label="数据编码代码" show-overflow-tooltip />
              <el-table-column prop="isInside" label="系统内置" align="center">
                <template slot-scope="scope">
                  <span :class="scope.row.isInside==0?'normal':'red'">{{ scope.row.isInside==0?'否':'是' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150px" fixed="right">
                <template slot-scope="scope">
                  <el-tooltip v-if="scope.row.isInside==0" v-permission="['POST /admin/archBusiUviewStrConfig/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
                    <span class="tr_detail_icon fl">
                      <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row)" />
                    </span>
                  </el-tooltip>
                  <el-tooltip v-if="scope.row.isInside==0" v-permission="['POST /admin/archBusiUviewStrConfig/delete']" content="移除" effect="light" :open-delay="1000" placement="bottom">
                    <span class="tr_del_icon fl">
                      <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
                    </span>
                  </el-tooltip>
                </template>
              </el-table-column>
            </el-table>
            <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="getTableData" />
          </div>
        </div>
        <el-dialog :modal-append-to-body="false" :title="dialogName" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addCodeForm','addCodeForm')">
          <el-form ref="addCodeForm" :model="addCodeForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="信息项编码：" class="required_label" label-width="120px" prop="itemMark">
              <el-input v-model="addCodeForm.itemMark" :disabled="canChange" style="width:100%" />
            </el-form-item>
            <el-form-item label="信息项名称：" class="required_label" label-width="120px" prop="itemName">
              <el-input v-model="addCodeForm.itemName" style="width:100%" />
            </el-form-item>

            <el-form-item label="必填（必选）：" prop="isChoice" class="required_label" label-width="120px">
              <el-radio-group v-model="addCodeForm.isChoice">
                <el-radio key="1" label="是" value="1" />
                <el-radio key="0" label="否" value="0" />
              </el-radio-group>
            </el-form-item>
            <el-form-item label="组件类型：" prop="itemType" class="required_label" label-width="120px">
              <el-select v-model="addCodeForm.itemType" placeholder="请选择组件类型" @change="changeItem">
                <el-option v-for="item in serviceObjs" :key="item.value" :label="item.name" :value="item.value" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="showBianMa" label="数据编码代码：" class="required_label" label-width="120px" prop="itemCode">
              <el-input v-model="addCodeForm.itemCode" style="width:100%" />
            </el-form-item>
            <el-form-item label="显示序号：" class="required_label" label-width="120px" prop="orderNum">
              <el-input v-model="addCodeForm.orderNum" style="width:100%" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button v-permission="['POST /admin/archBusiUviewStrConfig/save','POST /admin/archBusiUviewStrConfig/update']" size="mini" class="add" @click="dialogName=='新增'?addCodeDetail(1):addCodeDetail(2)">保 存</el-button>
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
  archRead,
  archList,
  archUpdate,
  archSave,
  archDelete,
  dsysDictGetByTypet,
  getDisplaySn
} from '@/api/assetCata'
import mixinJs from '@/utils/mixin'
import mapIcon from '@/assets/mapIcon.png'
import DelConfirm from '@/components/DelConfirm'
import Pagination from '@/components/Pagination'
export default {
  name: 'InformationItem',
  components: { Pagination, DelConfirm },
  mixins: [mixinJs],
  data() {
    return {
      serviceObjs: '',
      canChange: false,
      showBianMa: false,
      tempDelData: {},
      tipStr: '确定要删除所选信息项元数据吗？',
      delDialog: false,
      addCodeForm: {},
      dialogName: '新增',
      addWorkOrder: false,
      filterText: '',
      listQuery: {
        page: 1,
        size: 10
      },
      mapIcon: mapIcon,
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加itemMark itemName isChoice itemType orderNum
        itemMark: [
          { required: true, message: '请输入信息项编码', trigger: 'blur' }
        ],
        itemName: [
          { required: true, message: '请输入信息项名称', trigger: 'blur' }
        ],
        itemCode: [
          { required: true, message: '请输入数据编码代码', trigger: 'blur' },
          {
            validator: this.hasChina,
            message: '输入正确的数据编码代码',
            trigger: 'blur'
          }
        ],
        isChoice: [{ required: true, message: '请选择', trigger: 'change' }],
        itemType: [
          { required: true, message: '请选择组件类型', trigger: 'change' }
        ],
        orderNum: [
          { required: true, message: '请输入信息项排序', trigger: 'blur' },
          {
            validator: this.isNumber,
            message: '数据长度必须是整数数字',
            trigger: 'blur'
          }
        ]
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      tableData: [],
      total: 0
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getTableData()
    this.getByType()
  },
  methods: {
    changeItem(data) {
      if (data == '03' || data == '04') {
        this.showBianMa = true
      } else {
        this.showBianMa = false
      }
    },
    getByType() {
      dsysDictGetByTypet({ type: 'component_type' })
        .then(res => {
          this.serviceObjs = res.data.data
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    addMenu() {
      this.addCodeForm = {}
      this.dialogName = '新增'
      this.canChange = false
      if (this.listQuery.total >= 26) {
        this.$notify.error('最多存在26个信息项')
        return
      }
      // 获取序号
      getDisplaySn()
        .then(res => {
          if (res.data.errno === 0) {
            this.addCodeForm = Object.assign(
              {},
              {
                orderNum: res.data.data
              }
            )
            this.addWorkOrder = true
          }
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    delRelated(data) {
      this.tempDelData = Object.assign({}, data)
      this.delDialog = true
    },
    confirmDelete() {
      archDelete([this.tempDelData.id])
        .then(res => {
          this.$notify.success('删除成功')
          this.delDialog = false
          this.tempDelData = {}
          this.getTableData()
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    addCodeDetail(num) {
      if (num == 1) {
        this.$refs.addCodeForm.validate(valid => {
          if (valid) {
            this.addCodeForm.orderNum = this.addCodeForm.orderNum * 1
            this.addCodeForm.isInside = '0'
            archSave(this.addCodeForm)
              .then(res => {
                this.$notify.success('新增信息项成功')
                this.addWorkOrder = false
                this.getTableData()
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        })
      } else {
        this.$refs.addCodeForm.validate(valid => {
          if (valid) {
            this.addCodeForm.orderNum = this.addCodeForm.orderNum * 1
            this.addCodeForm.isInside = '0'
            archUpdate(this.addCodeForm)
              .then(res => {
                this.$notify.success('更新信息项成功')
                this.addWorkOrder = false
                this.getTableData()
              })
              .catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        })
      }
    },
    showCodenIfo(data) {
      this.addCodeForm = Object.assign({}, data)
      this.addWorkOrder = true
      if (this.addCodeForm.isChoice == '是 ') {
        this.addCodeForm.isChoice = '是'
      } else if (this.addCodeForm.isChoice == '否 ') {
        this.addCodeForm.isChoice = '否'
      }
      this.dialogName = '编辑'
      this.canChange = true
    },
    hideDialog(num) {
      if (num == 2) {
        this.addWorkOrder = false
        this.addCodeForm = {}
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getTableData()
    },
    qingkong() {
      this.listQuery.userId = ''
      this.listQuery.mobile = ''
      this.listQuery.name = ''
      this.listQuery.type = ''
      this.listQuery.repairType = ''
      this.handleFilter()
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    deleteCodeInfo() {
      // 删除操作
    },
    getTableData() {
      this.listLoading = true
      archList(this.listQuery)
        .then(response => {
          this.tableData = response.data.data.records
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
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
.red {
  color: #e65555;
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
  padding: 20px 0;
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
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
}
.tree_content {
  margin-top: 24px;
  padding-left: 20px;
  padding-right: 20px;
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

.tab_area {
  height: 100%;
}
.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.operate_area {
  padding: 0 45px;
}
.related_btns {
  margin-top: 15px;
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
  color: #5abd8c;
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

