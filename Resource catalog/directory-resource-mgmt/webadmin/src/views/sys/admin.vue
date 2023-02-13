<template>
  <div class="app-container mt20 department_content">
    <div class="type_area">用户管理</div>
    <div class="operate_area">
      <!-- 查询和其他操作 -->
      <div class="filter-container" style="margin-top: 15px;">
        <el-row>
          <!-- <el-button v-permission="['POST /admin/user/create']" class="add fl" size="mini" icon="el-icon-plus" @click="handleCreate">新增</el-button>
          <el-button v-permission="['POST /admin/user/delete']" :loading="downloadLoading" class="add fl" size="mini" icon="el-icon-delete" @click="handleDelete">删除</el-button>
          <el-button v-permission="['POST /admin/user/resetpwd']" class="add fl" size="mini" @click="reset">密码重置</el-button> -->
          <el-input v-model="listQuery.name" clearable class="filter-item" style="width: 180px;margin-left: 15px" placeholder="账号或者姓名关键字" />
          <select-tree v-model="listQuery.orgId" :select-ids="selectOrgs" :is-multiple="false" :check-strictly="true" :select-treedata="orgTree" node-key="orgId" placeholder="所属部门" class="multipleResSelect filter-item" @checkedChoose="getCheckedOrg(arguments)">
            <template slot="prepend">所属部门</template>
          </select-tree>
          <el-select v-model="listQuery.roleId" placeholder="所属角色" class="filter-item" clearable @change="handleFilter">
            <el-option v-for="item in roleOption" :key="item.value" :label="item.label" :value="item.value" />
            <template slot="prepend">所属角色</template>
          </el-select>
          <el-button v-permission="['GET /admin/user/list']" class="remove fr" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>
        </el-row>
      </div>

      <!-- 查询结果 -->
      <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" stripe :header-cell-style="getRowClass" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column v-if="false" align="center" label="管理员ID" style="display:none" prop="id" sortable />
        <el-table-column label="姓名" prop="name" />
        <el-table-column label="用户账号" prop="username" show-overflow-tooltip />
        <el-table-column label="用户头像" prop="avatar">
          <template slot-scope="scope">
            <img v-if="scope.row.avatar" :src="scope.row.avatar" width="40">
          </template>
        </el-table-column>
        <el-table-column label="所属部门" prop="orgnm" show-overflow-tooltip />
        <el-table-column label="所属角色" prop="rolenm" show-overflow-tooltip />
        <el-table-column label="手机号" prop="phone" />
        <el-table-column label="状态" prop="disabled">
          <template slot-scope="scope">
            <div v-if="scope.row.disabled==0">
              <span class="state_ok">启用</span>
            </div>
            <div v-else>
              <span class="state_err">禁用</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" class-name="small-padding fixed-width" fixed="right">
          <template slot-scope="scope">
            <el-tooltip content="编辑" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl">
                <svg-icon icon-class="edit" class-name @click="handleUpdate(scope.row)" />
              </span>
            </el-tooltip>
            <!-- <el-tooltip v-permission="['POST /admin/user/isDisabled']" :content="scope.row.disabled != 0 ? '启用':'禁用'" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl">
                <svg-icon v-if="scope.row.disabled != 0 " icon-class="qiyong" class-name @click="changeDisabled(0,scope.row)" />
                <svg-icon v-else icon-class="jinyong" class-name @click="changeDisabled(1,scope.row)" />
              </span>
            </el-tooltip> -->
            <el-tooltip v-permission="['GET /admin/user/read']" content="授权" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl">
                <svg-icon icon-class="Jurisdiction" class-name @click="handlePermission(scope.row)" />
              </span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" :layout="'prev,pager,next'" @pagination="getList" />
    </div>
    <!-- 添加或修改对话框 -->
    <el-dialog :modal-append-to-body="false" align="center" :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="48%" :close-on-click-modal="false" @close="close()">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px" align="left">
        <el-form-item label="用户账号" class="required_label" prop="username" label-width="130px">
          <el-input v-model="dataForm.username" placeholder="请输入用户账号" />
        </el-form-item>
        <el-form-item label="用户姓名" class="required_label" prop="name" label-width="130px">
          <el-input v-model="dataForm.name" placeholder="请输入用户姓名" />
        </el-form-item>
        <!-- <el-form-item label="用户密码" class="required_label" prop="password" label-width="130px">
          <el-input v-model="dataForm.password" type="password" auto-complete="off" />
        </el-form-item>-->
        <el-form-item label="用户角色" class="required_label" prop="roleId" label-width="130px" width="100%">
          <el-select v-model="dataForm.roleId" style="width:100%" placeholder="请选择用户角色">
            <el-option v-for="item in roleOption" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item ref="localProvOrgCodeForm" label="所属部门" class="required_label" prop="orgId" label-width="130px">
          <select-tree v-if="dialogFormVisible" v-model="dataForm.orgId" :select-ids="selectOrgsAdd" :is-multiple="false" :check-strictly="true" :select-treedata="orgTree" node-key="orgId" placeholder="请选择所属部门" @checkedChoose="getAddPeopleChose(arguments)">
            <template slot="prepend">所属部门</template>
          </select-tree>
        </el-form-item>
        <el-form-item label="手机号" class="required_label" prop="phone" label-width="130px">
          <el-input v-model="dataForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="管理员头像" prop="avatar" label-width="130px">
          <el-upload :headers="headers" :action="uploadPath" :show-file-list="false" :on-success="uploadAvatar" class="avatar-uploader" accept=".jpg, .jpeg, .png, .gif">
            <img v-if="dataForm.avatar" :src="dataForm.avatar" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
          </el-upload>
        </el-form-item>

        <el-form-item label="备注" label-width="120px">
          <el-input v-model="dataForm.remark" :rows="5" type="textarea" placeholder="请填写备注" />
        </el-form-item>
      </el-form>
      <div class="dialogDom" align="center">
        <el-button v-if="dialogStatus=='create'" v-permission="['POST /admin/user/create']" size="mini" class="add" type="primary" @click="createData">确定</el-button>
        <el-button v-else v-permission="['POST /admin/user/update']" size="mini" class="add" type="primary" @click="updateData">确定</el-button>
        <el-button size="mini" class="remove" @click="cancel">取消</el-button>
      </div>
    </el-dialog>

    <!-- 权限配置对话框 -->
    <el-dialog :modal-append-to-body="false" align="center" :visible.sync="permissionDialogFormVisible" title="权限配置" :close-on-click-modal="false">
      <el-row>
        <el-col :span="12" align="left">
          <span class="leftSpan">选择用户具备的目录分类管理权限</span>
          <el-form ref="addCodeForm" :model="addCodeForm" align="left" class="demo-ruleForm" style="margin-top:3vh" label-position="left">
            <el-form-item class="dialogFormItem" label="用户账号：" label-width="85px" prop="username">
              <span>{{ addCodeForm.username }}</span>
            </el-form-item>
            <el-form-item class="dialogFormItem" label="用户姓名：" label-width="85px" prop="name">
              <span>{{ addCodeForm.name }}</span>
            </el-form-item>
            <el-form-item class="dialogFormItem" label="所属部门：" label-width="85px" prop="orgnm">
              <span>{{ addCodeForm.orgnm }}</span>
            </el-form-item>
          </el-form>
        </el-col>
        <el-col :span="12" class="leftTree">
          <el-tree ref="tree" :data="systemPermissions" :default-checked-keys="assignedPermissions" show-checkbox node-key="typId" :props="defaultProps" highlight-current>
            <span slot-scope="{ node, data }" class="custom-tree-node">
              <span>
                <span class="level">{{ data.typType }}</span>
                {{ data.typCd }}{{ node.label }}
              </span>
            </span>
          </el-tree>
        </el-col>
      </el-row>

      <div slot="footer" class="dialog-footer" align="center">
        <el-button v-permission="['POST /admin/user/setCataRole']" size="mini" class="add" type="primary" @click="updatePermission">确定</el-button>
        <el-button size="mini" class="remove" @click="permissionDialogFormVisible = false">取消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listAdmin,
  createAdmin,
  updateAdmin,
  deleteAdmin,
  ableAdmin,
  resetAdmin,
  setCataRole,
  readminAdmin,
  getDeptTree,
  getRoleOptions,
  getCataTree
} from '@/api/admin'
import { uploadPath } from '@/api/storage'
import { getToken } from '@/utils/auth'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { checkMobile, checkIdcard } from '@/utils/formValidateMixin'
import { mapGetters } from 'vuex'
import SelectTree from '@/components/SelectTree'
export default {
  name: 'Admin',
  components: { Pagination, SelectTree },
  data() {
    checkMobile
    checkIdcard
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入管理员名称'))
      } else {
        const nameReg = new RegExp('^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$')
        if (!nameReg.test(value)) {
          callback(new Error('请输入正确格式，长度6-20, 结尾不能为特殊字符'))
        } else {
          callback()
        }
      }
    }
    return {
      defaultProps: {
        label: 'typNm'
      },
      selecetCata: [],
      addCodeForm: {},
      selectOrgs: [],
      selectOrgsAdd: [],
      permissionDialogFormVisible: false,
      systemPermissions: null,
      assignedPermissions: [],
      permissionForm: {
        roleId: undefined,
        permissions: []
      },
      uploadPath,
      list: null,
      total: 0,
      roleOptions: null,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 10,
        username: undefined,
        sort: 'add_time',
        order: 'desc'
      },
      dataForm: {
        id: undefined,
        username: undefined,
        password: undefined,
        avatar: undefined,
        roleId: -1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      roleOption: {},
      orgTree: [],
      textMap: {
        update: '编辑用户',
        create: '创建用户'
      },

      rules: {
        name: [{ required: true, message: '姓名不能为空', trigger: 'blur' }],
        username: [
          { required: true, message: '管理员名称不能为空', trigger: 'blur' },
          { validator: validatePass, trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        idCard: [
          { required: true, message: '身份证为空', trigger: 'blur' },
          { validator: checkIdcard, trigger: 'blur' }
        ],
        orgId: [
          { required: true, message: '请选择所属部门', trigger: 'change' }
        ],
        roleId: [{ required: true, message: '用户角色不能为空', trigger: 'blur' }],
        type: [
          { required: true, message: '请选择员工类型', trigger: 'change' }
        ],
        // phone: [
        //   { required: true, message: '手机号不能为空', trigger: 'blur' },
        //   { validator: checkMobile, trigger: 'blur' }
        // ]
      },
      downloadLoading: false,
      propertys: [],
      types: [],
      isStaff: false,
      onlyPropertyOwner: false,
      selectOrgVal: '',
      checkedTreeNodes: '',
      typetrees: []
    }
  },
  computed: {
    ...mapGetters(['userid', 'propertyId', 'roles']),
    headers() {
      return {
        'X-Resourcecatalog-Admin-Token': getToken()
      }
    }
  },
  created() {
    this.getList()
    this.getOptions()
  },
  methods: {
    cancel() {
      this.dataForm = {}
      this.dialogFormVisible = false
    },
    loadNode(node, resolve) {
      if (node.level === 0) {
        return resolve([{ name: node.label }])
      }
      let data = []
      getCataTree({ pId: node.key }).then(response => {
        data = response.data.data
        resolve(data)
      })
    },
    changeDisabled(num, row) {
      ableAdmin({ id: row.id, disabled: num })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '修改成功'
          })
          this.getList()
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    getCheckedOrg(args) {
      this.selectOrgVal = args[0]
      this.checkedTreeNodes = args[1]
      if (args[1].length > 0) {
        this.listQuery.orgId = args[1][0].orgId
      } else {
        this.listQuery.orgId = null
      }
    },
    getAddPeopleChose(args) {
      setTimeout(() => {
        this.$refs['localProvOrgCodeForm'].clearValidate() //选择后清除 这个input的验证
      }, 20)
      if (args[1].length > 0) {
        this.dataForm.orgId = [args[1][0].orgId]
      } else {
        this.dataForm.orgId = null
      }
    },
    getOptions() {
      getRoleOptions()
        .then(response => {
          this.roleOption = response.data.data.list
        })
        .catch(() => {
          this.list = []
          this.total = 0
          this.listLoading = false
        })
      getDeptTree({ isTop: 0 })
        .then(response => {
          this.orgTree = response.data
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },

    handlePermission(row) {
      // 获取权限树
      readminAdmin({ id: row.id }).then(response => {
        this.assignedPermissions = response.data.data.typeIds
        this.addCodeForm = row
        this.permissionDialogFormVisible = true
        this.permissionForm.id = row.id
        getCataTree({ roleId: null }).then(response => {
          this.systemPermissions = response.data.data
        })
      })
    },
    updatePermission() {
      // this.permissionForm.typeIds = this.$refs.tree.getCheckedKeys(true);
      this.typetrees = []
      var data = this.$refs.tree.getCheckedNodes(false, true)
      var leafs = []
      data.forEach(item => {
        if (item.children.length > 0) {
          leafs.push({ id: item.typId, isLeaf: 0 })
        } else {
          leafs.push({ id: item.typId, isLeaf: 1 })
        }
      })
      this.permissionForm.typeIds = leafs
      setCataRole(this.permissionForm)
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
    toTree(data, id, pid) {
      // 删除 所有 children,以防止多次调用
      data.forEach(function (item) {
        delete item.children
      })
      // 将数据存储为 以 id 为 KEY 的 map 索引数据列
      var map = {}
      data.forEach(function (item) {
        map[item[id]] = item
      })
      var val = []
      var ids = []
      data.forEach(function (item) {
        // 以当前遍历项的pid,去map对象中找到索引的id
        var parent = map[item[pid]]
        // 好绕啊，如果找到索引，那么说明此项不在顶级当中,那么需要把此项添加到，他对应的父级中
        if (parent) {
          (parent.children || (parent.children = [])).push(item)
        } else {
          // 如果没有在map中找到对应的索引ID,那么直接把 当前的item添加到 val结果集中，作为顶级
          // val是获取树形 ids获取所有的选中的包括半选中的id
          val.push(item)
        }
      })
      return val
    },
    pinjie(data) {
      var children = []
      this.typetrees.forEach(item => {
        if (data.parTypId == item.typId) {
          children.push({ typId: item.typId })
        }
      })
    },
    handleSelectionChange(val) {
      this.selecetCata = val
    },
    compareName(a) {
      let params = {
        username: a
      }

      if (this.dialogStatus == 'create') {
        params = {
          username: a
        }
      } else {
        params = {
          id: this.dataForm.id,
          username: a
        }
      }
      // 判断是否存在
      compareNameUrl(params)
        .then(response => {
          if (response.data.data > 0) {
            this.$notify.error({
              title: '失败',
              message: '用户名已经存在'
            })
            this.dataForm.username = undefined
          }
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    },
    close() {
      this.isStaff = false
      this.dataForm = {}
      this.selectOrgsAdd = []
    },
    formatRole(roleId) {
      for (let i = 0; i < this.roleOptions.length; i++) {
        if (roleId === this.roleOptions[i].value) {
          return this.roleOptions[i].label
        }
      }
      return ''
    },
    getList() {
      this.listLoading = true
      listAdmin(this.listQuery)
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
        username: undefined,
        password: undefined,
        avatar: undefined,
        roleId: []
      }
    },
    uploadAvatar: function (response) {
      console.log(response)

      if (response.errno === 0) {
        this.dataForm.avatar = response.data.url
      } else {
        this.$message({
          message: response.errmsg,
          type: 'error'
        })
      }
    },
    handleCreate() {
      this.resetForm()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.selectOrgsAdd = []
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          if (this.dataForm.type) {
            this.dataForm.type = this.dataForm.type.join(',')
          }
          if (this.onlyPropertyOwner) {
            this.dataForm.propertyId = this.propertyId
          }
          createAdmin(this.dataForm)
            .then(response => {
              this.list.unshift(response.data.data)
              this.getList()
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '新增管理员成功'
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
      /* 字符串数组拼接为int型数组 */
      if (this.dataForm.roleId) {
        const roleIds = []
        const roleIdStrs = (this.dataForm.roleId + '').split(',')
        for (const roleIdStr of roleIdStrs) {
          roleIds.push(parseInt(roleIdStr))
        }
        this.dataForm.roleId = roleIds[0]
      }
      if (this.dataForm.orgId) {
        this.dataForm.orgId = [this.dataForm.orgId]
      }
      if (row.orgId) {
        this.selectOrgsAdd = this.dataForm.orgId
      }
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    getRowClass({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 0) {
        return 'background: #F6F6F6;font-weight:bold;color:rgba(36,36,36,1);'
      } else {
        return ''
      }
    },
    updateData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          if (this.dataForm.type) {
            this.dataForm.type = this.dataForm.type.join(',')
          }

          if (this.onlyPropertyOwner) {
            this.dataForm.propertyId = this.propertyId
          }
          updateAdmin(this.dataForm)
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
                message: '更新管理员成功'
              })
              this.getList()
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
      if (this.selecetCata.length == null || this.selecetCata.length == 0) {
        this.$message({
          message: '清先勾选删除项目',
          type: 'error'
        })
        return
      }
      const grouos = []
      this.selecetCata.forEach(element => {
        grouos.push(element.id)
      })
      this.$confirm('确认删除？')
        .then(_ => {
          deleteAdmin({ id: grouos })
            .then(response => {
              this.$notify.success({
                title: '成功',
                message: '删除管理员成功'
              })
              this.getList()
            })
            .catch(response => {
              this.$notify.error({
                title: '失败',
                message: response.data.errmsg
              })
            })
        })
        .catch(_ => { })
    },
    handleDownload() {
      // this.downloadLoading = true;
      // import("@/vendor/Export2Excel").then(excel => {
      //   const tHeader = ["管理员ID", "管理员名称", "管理员头像"];
      //   const filterVal = ["id", "username", "avatar"];
      //   excel.export_json_to_excel2(
      //     tHeader,
      //     this.list,
      //     filterVal,
      //     "管理员信息"
      //   );
      //   this.downloadLoading = false;
      // });
    },
    roleChange(roles) {
      if (roles.indexOf(5) > -1) {
        this.isStaff = true
      } else {
        this.isStaff = false
      }
    },
    reset() {
      if (this.selecetCata.length == null || this.selecetCata.length == 0) {
        this.$message({
          message: '清先勾选需要重置的账号',
          type: 'error'
        })
        return
      }
      const grouos = []
      this.selecetCata.forEach(element => {
        grouos.push(element.id)
      })

      resetAdmin({ id: grouos })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: `重置密码成功,新密码：${response.data.errmsg}`
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    }
  }
}
</script>

<style>
.leftSpan {
  color: #172853;
  font-size: 18px;
}
.leftTree {
  border: 1px solid rgb(201, 201, 201);
  max-height: 400px;
  min-height: 200px;
  overflow-y: scroll;
}
.dialogFormItem {
  margin-bottom: 5px;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
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
  width: 20px !important;
  height: 20px !important;
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
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #20a0ff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 145px;
  height: 145px;
  display: block;
}

.filter-container .filter-item {
  display: table;
  float: left;
  margin-right: 15px;
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
.level {
  width: 16px;
  display: inline-block;
  height: 16px;
  background: #1f3365;
  color: #fff;
  font-size: 14px;
  text-align: center;
  line-height: 16px;
  border-radius: 4px;
  margin-right: 4px;
}
</style>

