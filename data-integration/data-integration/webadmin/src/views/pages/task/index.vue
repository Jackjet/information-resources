<template>
  <el-main class="main">
    <el-row :gutter="20">
      <el-col span="5" style="padding:0;border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;height: 83vh;">
      <el-col :span="24" style="margin-top: 15px;">
        <el-form :inline="true">
          <el-form-item>
            <el-input clearable size="medium" placeholder="请输入搜索名称" @input="filterChange" v-model="filterText">
            </el-input>
          </el-form-item>
          <el-form-item style="margin-top: -15px;">
            <el-button size='mini' @click="restTree" icon="el-icon-refresh-left"></el-button>
            <el-button size='mini' @click="insertTaskType" icon="el-icon-plus"></el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="24" class="center">
        <el-tree
            class="filter-tree"
            :data="treeData"
            :props="defaultProps"
            node-key="id"
            :default-expanded-keys="showTree"
            accordion
            :filter-node-method="filterNode"
            @node-click="getNodeInfo"
            style="margin-top: 10px;margin-bottom: 20px;"
            ref="tree">
        </el-tree>
        <el-button size='mini' @click="updateTaskTypeName">重命名</el-button>
        <el-button size='mini' @click="deleteTaskType">删除</el-button>
      </el-col>
    </el-col>
      <el-col span="19">
        <el-row>
          <el-col :span="24">
            <el-form :inline="true" class='el-InputForm'>
              <el-form-item label="任务名称">
                <el-input clearable
                          size="medium"
                          placeholder="请输入任务名称"
                          v-model="SearchItem.name">
                </el-input>
              </el-form-item>
              <el-form-item label="标签">
                <autoInput style="width: 120px;margin-right: 5px;" v-model="SearchItem.metaKey" ref="metaName" the-id="metaName"
                           :the-responsepath="['data', 'content']" :on-focus="false" size="medium" the-key="name"
                           the-address="/webadmin/resources/findAllMetas" the-placeholder="请选择标签">
                </autoInput>
                <el-input style="width: 160px;" clearable size="medium" placeholder="请输入标签值" v-model="SearchItem.metaValue"></el-input>
              </el-form-item>
              <el-form-item style='margin-left: 15px;'>
                <el-button size='medium' @click="SearchNoteList" icon="el-icon-search">查询</el-button>
                <el-button size='medium' @click="reset" icon="el-icon-refresh-left">重置</el-button>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="24" style="margin-top: 10px;">
            <el-button type="primary" v-if="permissions.add" plain size='mini' @click="handleAdd" icon="el-icon-plus">新增
            </el-button>
          </el-col>
          <el-col :span='24'>
            <TableList :table-data='tableData'
                       v-loading="isSubmitLoading"
                       @onHandleSelectionChange="handleSelectionChange"
                       :table-selection="tableSelection"
                       :table-label="tableHeader"
                       :table-option="tableOpction">
            </TableList>
          </el-col>
          <el-col :span='24'>
            <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-dialog :title="taskTitle" :visible.sync="dialogVisible" :before-close="clearDialog">
      <el-form :model="ruleForm"
               :rules="rules"
               ref="ruleForm"
               label-width="80px"
               class="demo-ruleForm"
      >
        <el-row>
          <el-form-item label="名称:" prop='name'>
            <el-input
                clearable
                size="medium"
                placeholder="请输入名称"
                v-model="ruleForm.name"
                style="width: 400px"
            ></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <div style="margin-top: 20px;position: relative;left:38%">
        <el-button size="medium" @click="resetForm('ruleForm')">取消</el-button>
        <el-button size="medium" type="primary" @click="submitForm('ruleForm')">提 交</el-button>
      </div>
    </el-dialog>
    <el-dialog title="查看定时计划" :visible.sync="taskDialogVisible">
      <el-form :model="taskRuleForm" label-width="160px" class="demo-ruleForm">
        <el-form-item label="是否需要定时:">
          <el-radio disabled v-model="taskRuleForm.mode" label="定时">是</el-radio>
          <el-radio disabled v-model="taskRuleForm.mode" label="实时">否</el-radio>
        </el-form-item>
        <el-form-item label="重复:">
          <el-checkbox disabled true-label="Y" v-model="taskRuleForm.repeats"></el-checkbox>
        </el-form-item>
        <el-form-item label="调度周期:">
          <el-radio-group disabled v-model="taskRuleForm.schedulerType">
            <el-radio-button label="0">不需要定时</el-radio-button>
            <el-radio-button label="1">时间</el-radio-button>
            <el-radio-button label="2">天</el-radio-button>
            <el-radio-button label="3">周</el-radio-button>
            <el-radio-button label="4">月</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="以分计算时间间隔:" v-if="intervalSecondsShow">
          <el-slider
              disabled v-model="taskRuleForm.intervalSeconds" min="0" max="59" step="1">
          </el-slider>
        </el-form-item>
        <el-form-item label="以秒计算时间间隔:" v-if="intervalMinutesShow">
          <el-slider
              disabled v-model="taskRuleForm.intervalMinutes" min="0" max="59" step="1">
          </el-slider>
        </el-form-item>
        <el-form-item label="每天:" v-if="dayShow">
          <el-input-number disabled v-model="taskRuleForm.dayOfHour" :disabled="true"></el-input-number>
          <el-input-number disabled v-model="taskRuleForm.dayOfMinutes" :disabled="true"></el-input-number>
        </el-form-item>
        <el-form-item label="每周:" v-if="weekDayShow">
          <el-select disabled v-model="taskRuleForm.weekDay" disabled placeholder="请选择">
            <el-option key="0" label="星期天" value="0"></el-option>
            <el-option key="1" label="星期一" value="1"></el-option>
            <el-option key="2" label="星期二" value="2"></el-option>
            <el-option key="3" label="星期三" value="3"></el-option>
            <el-option key="4" label="星期四" value="4"></el-option>
            <el-option key="5" label="星期五" value="5"></el-option>
            <el-option key="6" label="星期六" value="6"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="每月:" v-if="dayOfMonthShow">
          <el-slider
              disabled :value="taskRuleForm.dayOfMonth" min="1" max="30" step="1">
          </el-slider>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-main>
</template>
<script>
import {
  findAll, deleteById, runTask, stopTask, getStatus, getTree, insertTaskGroup, updateTaskGroup, deleteTaskGroupById
} from "@/api/task.js";
import {findAllMetas} from "@/api/resources";
import TableList from '@/components/table/TableList'
import Pagination from '@/components/table/Pagination'
import autoInput from "@/components/input/autoCompleteInput"

export default {
  components: {TableList, Pagination, autoInput},
  data() {
    return {
      permissions: {
        add: false,
        edit: false,
        run: false,
        stop: false,
        getStatus: false,
        delete: false,
        taskLogs: false
      },
      headers: {
        Authorization: 'token ' + JSON.parse(sessionStorage.getItem("UserInfo")).token,
      },
      fileList: [],
      tableSelection: {
        key: false,
        type: 'selection',
        detaile: false
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      enbleList: [],
      tableData: [],
      tableHeader: [
        {id: false, type: '', label: '任务名称', list: 'name'},
        {id: false, type: '', label: '运行状态', list: 'status'},
        {id: false, type: 'btn', label: '集成模式', list: 'mode',method:(row) => {this.modeClick(row)}},
        {id: false, type: '', label: '标签', list: 'metasStr'},
        {id: false, type: '', label: '容器', list: 'containerName'},
        {id: false, type: '', label: '最后更新时间', list: 'updateTime'}
      ],
      tableOpction: {
        label: '操作',
        width: '230px',
        value: 0,
        options: [{
          label: '编辑',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.edit
          },
          icon: 'el-icon-edit-outline',
          State: false,
          method: (row) => {
            this.handleEdit(row)
          }
        }, {
          label: '编排',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.design
          },
          icon: 'el-icon-delete-outline',
          State: false,
          method: (row) => {
            this.handleDesign(row)
          }
        }, {
          label: '运行',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.run
          },
          icon: 'el-icon-delete-outline',
          State: false,
          method: (row) => {
            this.handleRun(row)
          }
        }, {
          label: '停止',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.stop
          },
          icon: 'el-icon-delete-outline',
          State: false,
          method: (row) => {
            this.handleStop(row)
          }
        }, {
          label: '刷新状态',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.getStatus
          },
          icon: 'el-icon-delete-outline',
          State: false,
          method: (row) => {
            this.handleRefresh(row)
          }
        }, {
          label: '运行日志',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.logs
          },
          icon: 'el-icon-delete-outline',
          State: false,
          method: (row) => {
            this.handleLogs(row)
          }
        }, {
          label: '删除',
          key: 0,
          type: 'text',
          show: (row) => {
            return this.permissions.delete
          },
          icon: 'el-icon-delete-outline',
          State: false,
          method: (row) => {
            this.handleDelete(row)
          }
        }]
      },
      SearchItem: {
        name: '',
        metaKey: '',
        metaValue: '',
        groupId: '',
      },
      lastItem: {
        name: '',
        metaKey: '',
        metaValue: '',
        groupId: '',
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      RootUrl: '',
      modalObjj: '',
      filterText: '',
      treeData: [],
      defaultProps: {
        children: 'child',
        label: 'name'
      },
      showTree: [],
      ruleForm: {
        name: '',
        parentId: ''
      },
      options: [],
      taskTitle: '',
      dialogVisible: false,
      taskTypeName:'',
      taskDialogVisible: false,
      taskRuleForm: {
        mode: '',
        repeats: '',
        schedulerType:'',
        intervalMinutes:'',
        intervalSeconds:'',
        dayOfHour:'',
        dayOfMinutes:'',
        weekDay:'',
        dayOfMonth:''
      },
      dayOfMonthShow: false,
      weekDayShow: false,
      dayShow: false,
      intervalMinutesShow: false,
      intervalSecondsShow: false,
    }
  },
  created() {
    let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
    permissionsArr.forEach(item => {
      let itemArr = item.split('_')
      if (('/' + itemArr[0]) === this.$route.path) {
        this.permissions[itemArr[1]] = true
      }
    })
    this.SearchItem.groupId=this.$route.query.groupId
    this.fetchData()
    this.findMetas()
    this.getTreeList()
  },
  methods: {
    // 重置
    reset() {
      const that = this
      that.$refs.metaName.clearValue()
      that.SearchItem.groupId =''
      that.SearchItem.name = ''
      that.SearchItem.metaKey = ''
      that.SearchItem.metaValue = ''
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      that.fetchData()
    },
    // 获取列表
    async fetchData() {
      const that = this
      let datas = ''
      datas = this.SearchItem
      datas.page = this.currentPage
      datas.size = this.pageSize
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]]
      })
      try {
        that.isSubmitLoading = true
        const res = await findAll(datas)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          res.data.data.content.map(function (v, k) {
            if (v.status === 0) {
              v.status = '未启动'
            } else if (v.status === 1) {
              v.status = '已启动'
            }
          })
          res.data.data.content.map(function (v, k) {
            if (v.mode === 0) {
              v.mode = ''
            } else if (v.mode === 1) {
              v.mode = '定时'
            } else if (v.mode === 2) {
              v.mode = '实时'
            }
          })
          res.data.data.content.map(function (v, k) {
            let metaStr = "";
            v.metas.forEach((item, index) => {
              if(item.metaKey !==''){
                if (index !== 0) {
                  metaStr += ",";
                }
                metaStr += item.metaKey + ":" + item.metaValue;
              }
            })
            v.metasStr = metaStr;
          })
          res.data.data.content.map(function (v, k) {
            v.intervalMinutes = parseInt(v.intervalMinutes);
          })
          res.data.data.content.map(function (v, k) {
            v.intervalSeconds = parseInt(v.intervalSeconds);
          })
          res.data.data.content.map(function (v, k) {
            v.dayOfMonth = parseInt(v.dayOfMonth);
          })
          res.data.data.content.map(function (v, k) {
            v.container = JSON.parse(v.container)
            v.containerName = v.container.name
          })
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    async findMetas() {
      const that = this
      that.loading = true
      const response = await findAllMetas()
      that.loading = false
      if (response.data.code === 1) {
        that.options = []
        response.data.data.content.map(function (v, k) {
          that.options.push({key: v.name,value: v.name, label: v.name})
        })
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      that.enbleList = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(v.id)
        that.enbleList.push({id: v.id, enable: v.enable})
      })
    },
    // 搜索
    SearchNoteList() {
      let metaKey = this.$refs.metaName.returnValue()
      this.SearchItem.metaKey = metaKey
      this.currentPage = 1
      this.$refs.page.Page(1)
      this.fetchData()
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]]
      })
      this.fetchData()
    },
    // 编辑
    handleEdit(data) {
      const that = this
      that.$router.push({
        path: '/taskAdd',
        query: {
          id: data.id,
          type: '编辑',
          name: data.name,
          container: data.container,
          startDataSource: data.startDataSource,
          endDataSource: data.endDataSource,
          metas: data.metas,
          groupId: data.groupId,
          remark: data.remark
        }
      })
    },
    // 添加
    handleAdd(data) {
      const that = this
      that.$router.push({
        path: '/taskAdd',
        query: {
          type: '新增',
          groupId: that.SearchItem.groupId
        }
      })
    },
    // 删除当前数据 重载列表
    async handleDelete(data) {
      const that = this
      that.$confirm('删除会清空任务信息, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.isSubmitLoading = true
        const response = await deleteById(data.id)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
    // 删除当前数据 重载列表
    async handleLogs(data) {
      const that = this
      that.$router.push({
        path: '/taskLogs',
        query: {
          id: data.id,
          type: '编辑',
          taskName: data.name,
          groupId: that.SearchItem.groupId
        }
      })
    },
    // 设计
    async handleDesign(data) {
      const that = this
      that.$router.push({
        path: '/taskDesign',
        query: {
          id: data.id,
          groupId: that.SearchItem.groupId,
          container: data.container
        }
      })
    },
    async handleRun(data) {
      const that = this
      that.isSubmitLoading = true
      const response = await runTask({id:data.id})
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        that.$message.success('运行成功')
        this.fetchData()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async handleStop(data) {
      const that = this
      that.isSubmitLoading = true
      const response = await stopTask({id:data.id})
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        that.$message.success('停止成功')
        this.fetchData()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    async handleRefresh(data) {
      const that = this
      that.isSubmitLoading = true
      const response = await getStatus({id:data.id})
      that.isSubmitLoading = false
      if (response.data.code === 1) {
        that.$message.success('更新成功')
        this.fetchData()
      } else {
        that.$message.error(response.data.msg)
      }
    },
    restTree() {
      const that = this
      that.filterText = ''
      that.ruleForm.parentId = ''
      that.SearchItem.groupId = ''
      that.taskTypeName = ''
      this.getTreeList()
    },
    insertTaskType() {
      const that = this
      that.taskTitle = "新增"
      that.dialogVisible = true
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    },
    filterChange() {
      // 这里每当输入的数据有变化就触发原生的过滤节点这个函数
      this.$refs.tree.filter(this.filterText);
    },
    getNodeInfo(data) {
      const that = this
      that.ruleForm.parentId = data.id
      that.SearchItem.groupId = data.id
      that.taskTypeName = data.name
      that.showTree = [data.id]
      this.fetchData()
    },
    updateTaskTypeName() {
      const that = this
      let id = that.SearchItem.groupId;
      if (id === '1') {
        this.$message.warning('该库名不可修改')
      } else {
        let parentId = that.ruleForm.parentId;
        if (!parentId) {
          this.$message.warning('请选择集成库')
        } else {
          that.taskTitle = "重命名"
          that.ruleForm.name = that.taskTypeName
          that.dialogVisible = true
        }
      }
    },
    deleteTaskType() {
      const that = this
      let parentId = that.ruleForm.parentId;
      if (!parentId) {
        this.$message.warning('请选择集成库')
      } else {
        that.$confirm('请确认是否删除?', '提示', {
          type: 'warning'
        }).then(async () => {
          that.Loading = true
          const response = await deleteTaskGroupById(parentId)
          that.Loading = false
          if (response.data.code === 1) {
            that.$message.success('删除成功')
            await this.getTreeList()
          } else {
            that.$message.error(response.data.msg)
          }
        }).catch(() => {
          return false
        })
      }
    },
    /*获取左侧树形列表*/
    async getTreeList() {
      const that = this
      try {
        const res = await getTree()
        if (res.data.code === 1) {
          that.treeData = res.data.data
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    resetForm(ruleForm) {
      this.$refs["ruleForm"].resetFields();
      this.CloseModal();
    },
    // 初始化表单
    CloseModal() {
      const that = this
      that.ruleForm.name = "";
      that.dialogVisible = false
      that.taskTitle = ''
    },
    modeClick(data) {
      this.taskDialogVisible = true
      switch(data.schedulerType){
        case "0":
          this.dayOfMonthShow= false
          this.weekDayShow= false
          this.dayShow= false
          this.intervalMinutesShow= false
          this.intervalSecondsShow= false
          break
        case "1":
          this.dayOfMonthShow= false
          this.weekDayShow= false
          this.dayShow= false
          this.intervalMinutesShow= true
          this.intervalSecondsShow= true
          break
        case "2":
          this.dayOfMonthShow= false
          this.weekDayShow= false
          this.dayShow= true
          this.intervalMinutesShow= false
          this.intervalSecondsShow= false
          break
        case "3":
          this.dayOfMonthShow= false
          this.weekDayShow= true
          this.dayShow= true
          this.intervalMinutesShow= false
          this.intervalSecondsShow= false
          break
        case "4":
          this.dayOfMonthShow= true
          this.weekDayShow= false
          this.dayShow= true
          this.intervalMinutesShow= false
          this.intervalSecondsShow= false
          break
        default:
          //这里是没有找到对应的值处理
          break
      }
      this.taskRuleForm.mode = data.mode
      this.taskRuleForm.repeats= data.repeats
      this.taskRuleForm.schedulerType= data.schedulerType
      this.taskRuleForm.intervalMinutes= data.intervalMinutes
      this.taskRuleForm.intervalSeconds= data.intervalSeconds
      this.taskRuleForm.dayOfHour= data.dayOfHour
      this.taskRuleForm.dayOfMinutes= data.dayOfMinutes
      this.taskRuleForm.weekDay= data.weekDay
      this.taskRuleForm.dayOfMonth= data.dayOfMonth
      console.log(this.taskRuleForm)
    },
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          // 取值
          const that = this
          let data = that.ruleForm
          // 新增
          if (that.taskTitle === '重命名') {
            data.id = that.SearchItem.groupId
            updateTaskGroup(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
                this.getTreeList()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          } else {
            insertTaskGroup(data).then((res) => {
              if (res.data.code === 1) {
                this.$message.success('成功');
                this.resetForm('ruleForm')
                this.getTreeList()
                this.loading = false;
              } else {
                this.$message.error(res.data.msg);
              }
            });
          }
        }
      });
    },
  }
}
</script>
<style lang="scss" scoped>
.el-tree-node.is-current > .el-tree-node__content {
  background-color: #EBEEF5 !important;
  color: #606266;
}
</style>