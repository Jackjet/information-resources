<template>
  <div class="app-container mt20 department_content">
    <div class="type_area">角色管理</div>
    <div class="operate_area">
      <!-- 查询和其他操作 -->
      <div class="filter-container" style="margin-top: 15px;">
        <el-row>
          <el-button v-permission="['POST /admin/role/create']" class="add fl" size="mini" icon="el-icon-plus" @click="handleCreate">新增</el-button>
          <el-button class="add fl" size="mini" @click="reset">重置</el-button>
          <el-button v-permission="['GET /admin/role/list']" class="remove fr" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>
          <el-input v-model="listQuery.name" clearable class="filter-item fr" style="width: 300px;margin-left: 15px" placeholder="请输入角色名称">
            <!--  <template slot="prepend">角色名称：</template>-->
          </el-input>
        </el-row>
      </div>

      <!-- 查询结果 -->
      <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row>
        <el-table-column align="center" label="角色名称" prop="name" sortable show-overflow-tooltip />

        <el-table-column align="center" label="说明" prop="desc" show-overflow-tooltip />

        <el-table-column align="center" label="操作" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope" align="center">
            <el-tooltip content="编辑" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon">
                <svg-icon icon-class="edit" class-name @click="handleUpdate(scope.row)" />
              </span>
            </el-tooltip>
            <el-tooltip content="删除" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_del_icon" style="margin-right: 14px;position: relative;top:2px">
                <svg-icon icon-class="tr_del" class-name @click="delRelated(scope.row)" />
              </span>
              <!--<span class="tr_del_icon tr_detail_icon" style="position: relative;top:2px">
                <svg-icon icon-class="tr_del" style class-name @click="delRelated(scope.row)" />
              </span>-->
            </el-tooltip>
            <el-tooltip content="授权" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon">
                <svg-icon icon-class="Jurisdiction" class-name @click="handlePermission(scope.row)" />
              </span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </div>
    <!-- 添加或修改对话框 -->
    <el-dialog :modal-append-to-body="false" align="center" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" align="center" status-icon label-position="left" label-width="100px" style="width: 400px;">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="dataForm.name" />
        </el-form-item>
        <el-form-item label="说明" prop="desc">
          <el-input v-model="dataForm.desc" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" align="center">
        <el-button v-if="dialogStatus=='create'" type="primary" size="mini" class="add" @click="createData">确定</el-button>
        <el-button v-else type="primary" size="mini" class="add" @click="updateData">确定</el-button>
        <el-button size="mini" class="remove" @click="dialogFormVisible = false">取消</el-button>
      </div>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
    <!-- 权限配置对话框 -->
    <el-dialog :modal-append-to-body="false" :visible.sync="permissionDialogFormVisible" title="权限配置" :close-on-click-modal="false">
      <el-tree ref="tree" :data="systemPermissions" :default-checked-keys="assignedPermissions" show-checkbox node-key="id" highlight-current>
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span>{{ data.label }}</span>
          <el-tag v-if="data.api" type="success" size="mini">{{ data.api }}</el-tag>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer" align="center">
        <el-button size="mini" class="remove" @click="permissionDialogFormVisible = false">取消</el-button>
        <el-button size="mini" class="add" type="primary" @click="updatePermission">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listRole,
  createRole,
  updateRole,
  deleteRole,
  getPermission,
  updatePermission
} from '@/api/role'
import Pagination from '@/components/Pagination'
import DelConfirm from '@/components/DelConfirm'

export default {
  name: 'Role',
  components: { Pagination, DelConfirm },
  data() {
    return {
      tempDelData: {},
      tipStr: '确认删除所选角色信息吗？',
      delDialog: false,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        name: '',
        sort: 'add_time',
        order: 'desc'
      },
      dataForm: {
        id: undefined,
        name: undefined,
        desc: undefined
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        name: [{ required: true, message: '角色名称不能为空', trigger: 'blur' }]
      },
      permissionDialogFormVisible: false,
      systemPermissions: null,
      assignedPermissions: null,
      permissionForm: {
        roleId: undefined,
        permissions: []
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    delRelated(data) {
      this.tempDelData = data
      this.delDialog = true
    },
    confirmDelete() {
      // console.log(this.tempDelData);
      this.handleDelete(this.tempDelData)
      this.delDialog = false
      // this.tempDelData = {};
    },
    getList() {
      this.listLoading = true
      listRole(this.listQuery)
        .then(response => {
          this.list = response.data.data.records
          this.total = response.data.data.total
          this.listLoading = false
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        name: undefined,
        desc: undefined
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.dataForm.enabled = 1
          this.dataForm.deleted = 0
          createRole(this.dataForm)
            .then(response => {
              this.list.unshift(response.data.data)
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '新增角色成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleUpdate(row) {
      this.dataForm = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          updateRole(this.dataForm)
            .then(() => {
              for (const v of this.list) {
                if (v.id === this.dataForm.id) {
                  const index = this.list.indexOf(v)
                  this.list.splice(index, 1, this.dataForm)
                  break
                }
              }
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '更新角色信息成功'
              })
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        }
      })
    },
    handleDelete(row) {
      deleteRole(row)
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '删除角色信息成功'
          })
          const index = this.list.indexOf(row)
          this.list.splice(index, 1)
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handlePermission(row) {
      this.permissionDialogFormVisible = true
      this.permissionForm.roleId = row.id
      getPermission({ roleId: row.id }).then(response => {
        this.systemPermissions = response.data.data.systemPermissions
        this.assignedPermissions = response.data.data.assignedPermissions
      })
    },
    updatePermission() {
      this.permissionForm.permissions = this.$refs.tree.getCheckedKeys(true)
      updatePermission(this.permissionForm)
        .then(response => {
          this.permissionDialogFormVisible = false
          this.$notify.success({
            title: '成功',
            message: '授权成功'
          })
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    reset() {
      this.listQuery.name = ''
      this.getList()
    }
  }
}
</script>
<style scoped>
.filter-container .filter-item {
  display: table;
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
  border-color: #1f3365;
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
  width: 8px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
</style>

