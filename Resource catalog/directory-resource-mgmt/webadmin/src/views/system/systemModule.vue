<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--树形结构区域-->
      <el-col :span="4" class="tree_area">
        <el-row>
          <div class="tree_title">
            <span class="tree_desc">菜单树</span>
          </div>
        </el-row>
        <el-row class="tree_content_parent">
          <div class="tree_content">
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="treeDom" accordion :data="treeData" :props="defaultProps" highlight-current node-key="id" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :filter-node-method="filterNode" @node-click="handleNodeClick" />
            </el-scrollbar>
          </div>
        </el-row>
      </el-col>
      <!--tab选择区域-->
      <el-col :span="20" class="tab_area">
        <div class="type_area">{{ rightTitle }}</div>
        <div class="operate_area">
          <div class="related_btns">
            <el-row :gutter="20">
              <el-col :span="3">
                <el-button class="add" size="mini" icon="el-icon-plus" @click="addWorkOrder = true">新增资源</el-button>
              </el-col>
            </el-row>
          </div>

          <el-table :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%">
            <el-table-column prop="date" label="资源名称" width="180" show-overflow-tooltip />
            <el-table-column prop="name" label="资源类型" width="180" show-overflow-tooltip />
            <el-table-column prop="address" label="资源地址" show-overflow-tooltip />
            <el-table-column prop="name" label="资源状态" width="180">
              <template slot-scope="scope">
                <span class="tr_detail_icon fl greenColor">{{ scope.row.date }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" fixed="right">
              <template slot-scope="scope">
                <el-tooltip content="编辑" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_detail_icon fl">
                    <svg-icon icon-class="edit" class-name @click="showCodenIfo(scope.row)" />
                  </span>
                </el-tooltip>
                <el-tooltip content="移除" effect="light" :open-delay="1000" placement="bottom">
                  <span class="tr_del_icon fl">
                    <svg-icon icon-class="tr_del" class-name @click="deleteCodeInfo(scope.row)" />
                  </span>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
          <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getTableData" />
        </div>
        <el-dialog :title="dialogName" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false">
          <el-form ref="addCodeForm" :model="addCodeForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
            <el-form-item label="上级资源：" class="required_label" label-width="120px" prop="address">
              <el-input v-model="addCodeForm.address" style="width:100%" />
            </el-form-item>
            <el-form-item label="资源名称：" class="required_label" label-width="120px" prop="address">
              <el-input v-model="addCodeForm.date" style="width:100%" />
            </el-form-item>
            <el-form-item label="资源类型：" class="required_label" label-width="120px">
              <el-input v-model="addCodeForm.address" style="width:100%" />
            </el-form-item>
            <el-form-item label="资源类型：" class="required_label" label-width="120px">
              <el-select v-model="addCodeForm.region" placeholder="请选择组件类型">
                <el-option label="区域一" value="shanghai" />
                <el-option label="区域二" value="beijing" />
              </el-select>
            </el-form-item>
            <el-form-item label="资源地址：" label-width="120px">
              <el-input v-model="addCodeForm.address" style="width:100%" />
            </el-form-item>
            <el-form-item label="序号：" class="required_label" label-width="120px">
              <el-input v-model="addCodeForm.address" style="width:100%" />
            </el-form-item>
            <el-form-item label="功能状态：" class="required_label" label-width="120px">
              <el-select v-model="addCodeForm.region" placeholder="请选择组件类型">
                <el-option label="区域一" value="shanghai" />
                <el-option label="区域二" value="beijing" />
              </el-select>
            </el-form-item>
            <el-form-item label="资源备注：" label-width="120px">
              <el-input v-model="addCodeForm.address" :rows="5" type="textarea" style="width:100%" />
            </el-form-item>
          </el-form>
          <div class="dialogDom" align="center">
            <el-button size="mini" class="add" @click="addCodeDetail()">保 存</el-button>
            <el-button size="mini" class="remove" @click="hideDialog(2)">取 消</el-button>
          </div>
        </el-dialog>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
export default {
  name: 'DataEncoding',
  components: { Pagination },
  data() {
    return {
      addCodeForm: {},
      dialogName: '新增',
      addWorkOrder: false,
      filterText: '',
      rightTitle: '',
      listQuery: {
        current: 1,
        size: 20
      },
      mapIcon: mapIcon,
      treeData: [],
      // 新增表单校验
      createRules: {
        // 暂未添加
        applyUserName: [
          { required: true, message: '请选择业主名称', trigger: 'blur' }
        ],
        address: [{ required: true, message: '请选择地址', trigger: 'blur' }],
        phone: [{ required: true, message: '请选择电话', trigger: 'blur' }],
        ownerType: [
          { required: true, message: '请选择业主类型', trigger: 'blur' }
        ],
        type: [{ required: true, message: '请选择类型', trigger: 'blur' }],
        repairType: [
          { required: true, message: '请选择报修类型', trigger: 'blur' }
        ],
        deadline: [
          { required: true, message: '请选择上门时间', trigger: 'blur' }
        ]
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      activeName: 0,
      tableData: [],
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      total: 0,
      rightTitle: ''
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getTreeData()
    this.getTableData()
  },
  methods: {
    addCodeDetail() {
      this.$refs.addCodeForm.validate(valid => {
        if (valid) {
          this.$notify.success('新增编码成功')
          this.tableData.push(this.addCodeForm)
          //   editRepair(this.addCodeForm).then(res => {
          //     this.$notify.success("添加回访成功");
          //     this.returnList = false;
          //     this.getList();
          //   });
        }
      })
    },
    showCodenIfo(data) {
      this.addCodeForm = data
      this.addWorkOrder = true
    },
    hideDialog(num) {
      if (num == 2) {
        this.addWorkOrder = false
        this.addCodeForm = {}
      }
    },
    handleFilter() {
      this.listQuery.current = 1
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
      this.tableData = [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        },
        {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄'
        },
        {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄'
        },
        {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }
      ]
      this.total = this.tableData.length
      var _this = this
      var expandedkeysArr = []
      var _currentNodekey = ''
      if (this.treeData[0].children.length == 0 || !this.treeData[0].children) {
        this.currentNodekey = this.treeData[0].id
        this.expandedkeys.push(this.treeData[0].id)
      } else {
        _resolve(this.treeData[0])
      }
      function _resolve(e) {
        for (const k in e) {
          if (k == 'children') {
            if (e[k].length > 0) {
              expandedkeysArr.push(e.id)
              _resolve(e[k][0])
            } else {
              _currentNodekey = e.id
            }
          }
        }
        _this.expandedkeys = expandedkeysArr
        _this.currentNodekey = _currentNodekey
      }
      setTimeout(() => {
        this.rightTitle = this.$refs.treeDom.getCurrentNode().label
      }, 20)
    },
    getTreeData() {
      this.treeData = [
        {
          id: 1,
          label: '发展改革委',
          children: [
            {
              id: 10,
              label: '二级 1-1',
              children: [
                {
                  id: 101,
                  label: '二级 1-1-1',
                  children: []
                },
                {
                  id: 102,
                  label: '二级 1-1-2',
                  children: []
                }
              ]
            }
          ]
        },
        {
          label: '公安局',
          children: []
        },
        {
          label: '财政局',
          children: []
        }
      ]
    },
    handleNodeClick(data) {
      console.log(data)
      this.rightTitle = data.label
    },
    handleTabClick(tab, event) {
      console.log(tab, event)
    },
    handleSelectionChange(val) {
      console.log(val)
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
  padding-bottom: 20px;
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

