<template>
  <div class="mt20 department_content contentArea">
    <el-row style="height: 100%">
      <!--tab选择区域-->
      <el-col :span="24">
        <div class="content_area">
          <div class="type_area">信息需求目录</div>
          <el-form ref="departmentFormDom" :model="departmentForm" label-width="100px" :rules="rules" label-position="left" style="margin:30px 0 0 30px">
            <el-row>
              <el-col :span="20" :offset="2">
                <el-form-item label="信息资源名称:" prop="name" label-width="145px" class="required_label">
                  <el-input v-model="departmentForm.name" minlength="3" maxlength="200" placeholder="输入信息资源目录名称，3-200个字符" />
                </el-form-item>
              </el-col>
              <!-- <el-col :span="20" :offset="2">
                            <el-form-item label="职能职责:" prop="responsibilities" label-width="140px" class="required_label">
                                <el-input rows="5" type="textarea" v-model="departmentForm.responsibilities" ></el-input>
                            </el-form-item>
              </el-col>-->
              <el-col :span="20" :offset="2">
                <el-form-item v-if="id!=0" label="信息资源代码:" prop="resCode" label-width="145px" class="required_label">
                  <el-input v-model="departmentForm.code" readonly placeholder="请输入资源提供代码" />
                  <div v-if="id=='0'" class="codeHint">
                    自动生成的信息资源代码格式：前6位为信息资源分类的类、项、目，中间6位为分类中细目的最后6
                    位，最后6位是细目分类下的顺序编码
                  </div>
                </el-form-item>
              </el-col>
              <el-col :span="9" :offset="2">
                <el-form-item label="信息资源需求方:" prop="deptName" label-width="145px" class="required_label">
                  <el-input v-model="departmentForm.deptName" placeholder="" disabled="" />
                </el-form-item>
              </el-col>
              <el-col :span="9" :offset="2">
                <el-form-item label="资源需求方内设部门名称:" prop="belongToName1" label-width="145px" class="required_label">
                  <!--v-if="selectTreedata2.length>0"-->
                  <select-tree :select-ids="selectOrgs2" :node-key="'orgId'" :is-multiple="false" :select-treedata="selectTreedata2" :check-strictly="true" class="multipleResSelect" @checkedChoose="getCheckedOrg1(arguments)" />
                </el-form-item>
              </el-col>
              <el-col :span="20" :offset="2">
                <el-form-item label="信息资源提供单位:" prop="isOpen" label-width="145px" class="required_label">
                  <el-radio-group v-model="departmentForm.isOpen" :disabled="id!=0">
                    <el-radio :label="'0'">本地部门</el-radio>
                    <el-radio :label="'1'">第三方</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <div v-if="departmentForm.isOpen=='0'">
                <el-col :span="9" :offset="2">
                  <el-form-item ref="localProvOrgCodeForm" label="部门选择树:" prop="provOrgId" label-width="145px" class="required_label">
                    <!-- <el-input v-model="departmentForm.resFomatterClassify" placeholder="" ></el-input> -->
                    <select-tree ref="selectTree" v-model="departmentForm.provOrgId" :disabled="id!=0" :select-ids="selectOrgs" :check-strictly="true" :is-multiple="false" :node-key="'orgId'" :select-treedata="selectTreedata" :placeholder="'部门选择树'" class="multipleResSelect" @checkedChoose="getCheckedDep(arguments)" />
                  </el-form-item>
                </el-col>
                <el-col :span="9" :offset="2">
                  <el-form-item v-if="selectTreedata1" label="资源提供单位内设部门名称:" prop="belongToName" label-width="145px" class="">
                    <select-tree :select-ids="selectOrgs1" :node-key="'orgId'" :is-multiple="false" :select-treedata="selectTreedata1" :check-strictly="true" class="multipleResSelect" @checkedChoose="getCheckedOrg(arguments)" />
                  </el-form-item>
                </el-col>
              </div>
              <el-col v-if="departmentForm.isOpen=='1'" :span="20" :offset="2">
                <el-form-item ref="dsfProvOrgCodeForm" label prop="provOrgCode" label-width="145px" class="required_label">
                  <el-input v-model="departmentForm.provOrgCode" :disabled="id!=0" placeholder="请输入第三机构代码" />
                </el-form-item>
              </el-col>
              <el-col :span="20" :offset="2">
                <el-form-item label="是否实时获取:" prop="isAccess" label-width="145px" class="required_label">
                  <el-radio-group v-model="departmentForm.isAccess">
                    <el-radio :label="'0'">是</el-radio>
                    <el-radio :label="'1'">否</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="9" :offset="2">
                <el-form-item label="建议获取方式:" prop="accessWay" label-width="145px" class="required_label">
                  <!-- <el-input v-model="departmentForm.resFomatterClassify" placeholder="" ></el-input> -->
                  <el-select v-model="departmentForm.accessWay" clearable placeholder="请选择">
                    <el-option v-for="item in select1" :key="item.value" :label="item.name" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="9" :offset="2">
                <el-form-item label="期望数据使用频率:" prop="useFrequency" label-width="145px" class="required_label">
                  <!-- <el-input v-model="departmentForm.resFomatterType" placeholder="请选择" ></el-input> -->
                  <el-select v-model="departmentForm.useFrequency" filterable placeholder="请选择" :loading="loading">
                    <el-option v-for="item in select2" :key="item.value" :label="item.name" :value="item.value" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="20" :offset="2">
                <el-form-item label="信息资源描述:" prop="remark" label-width="145px" class="required_label">
                  <el-input v-model="departmentForm.remark" rows="3" type="textarea" />
                </el-form-item>
              </el-col>

              <el-col :span="20" :offset="2">
                <el-form-item label="用途:" prop="purpose" label-width="145px" class="required_label">
                  <el-input v-model="departmentForm.purpose" rows="3" type="textarea" />
                </el-form-item>
              </el-col>

              <el-col :span="12" :offset="6">
                <el-form-item>
                  <el-button v-permission="['POST /admin/cataRequire/update','POST /admin/cataBusInfoRel/save']" class="add" @click="submit">保存</el-button>
                  <el-button class="remove" @click="gotoPath(1)">返回</el-button>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import SelectTree from '@/components/SelectTree'
import { getDictByType } from '@/api/departmentRes'
import { addRequirementCatalog, updateRequirementCatalog, getRequirementDetail, getProvDepTree, getOrgTree } from '@/api/requirementCatalog'
export default {
  name: 'RequirementCatalogMaintain',
  components: { Pagination, SelectTree },
  mixins: [mixinJs],
  props: {
    id: {
      type: [String, Number],
      default: () => []
    }
  },
  data() {
    const checkLength = function (rule, value, callback) {
      if (value) {
        if (value.length < 3 || value.length > 200) {
          return callback(new Error(rule.messaage))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    return {
      checkedOrgs: [],
      checkedOrgs1: [],
      selectOrgs: [],
      selectOrgs2: [],
      selectOrgs1: [],
      isDisabled: false,
      listQuery: {
        current: 1,
        size: 10
      },
      tableData: [],
      total: 0,
      departmentForm: { isOpen: '', provOrgId: '' },
      departmentName: '',
      rules: {
        name: [
          { required: true, message: '请输入信息资源名称', trigger: 'blur' },
          { validator: checkLength, message: '信息资源名称长度必须为3-200个字符', trigger: 'blur' }
        ],
        provOrgCode: [
          { required: true, message: '请输入第三方提供单位代码', trigger: 'blur' }
        ],
        deptName: [
          { required: true, message: '未发现信息资源提供单位', trigger: 'blur' }
        ],
        provOrgId: [
          { required: true, message: '请选择信息资源提供单位的部门', trigger: 'change' }
        ],
        belongTo: [
          { required: true, message: '请选择信息资源提供单位的机构', trigger: 'change' }
        ],
        isOpen: [
          { required: true, message: '请选择信息资源提供单位', trigger: 'change' }
        ],
        isAccess: [
          { required: true, message: '请选择是否实时获取', trigger: 'change' }
        ],
        accessWay: [
          { required: true, message: '请选择建议获取方式', trigger: 'change' }
        ],
        useFrequency: [
          { required: true, message: '请选择期望数据使用频率', trigger: 'change' }
        ]

      },
      loading: false,
      select1: [],
      select2: [],
      selectTreedata: [],
      selectTreedata1: null,
      selectTreedata2: [],
      dataObj: {},
      rowId: ''
    }
  },
  watch: {
    'departmentForm.isOpen'(newVal, oldVal) {
      console.log(newVal)
      if (oldVal == 1) {
        this.$refs['dsfProvOrgCodeForm'].clearValidate()
      }
      if (newVal == 1) {
        setTimeout(() => {
          this.$refs['dsfProvOrgCodeForm'].clearValidate()
          this.departmentForm = Object.assign({}, this.departmentForm, {
            provOrgCode: '',
            provOrgId: '',
            belongToCode: '',
            belongTo: ''
          })
        }, 20)

        /* this.departmentForm.provOrgCode = ''
        this.departmentForm.provOrgId = ''
        this.departmentForm.belongTo = ''
        this.departmentForm.belongToCode = ''*/
      }
    },
    deep: true
  },
  created() {
    this.rowId = this.$route.params.id
    this.dataObj.provOrgId = this.$route.query.provOrgId
    this.dataObj.provOrgName = this.$route.query.provOrgName
    this.departmentForm.deptName = this.dataObj.provOrgName
    this.$store.dispatch('setReBackDeptId', this.$route.query.deptId)
    // 获取表单数据字典
    getDictByType({ type: 'share_way' }).then(res => {
      if (res.data.errno === 0) {
        this.select1 = res.data.data
        getDictByType({ type: 'update_cycle' }).then(res1 => {
          if (res1.data.errno === 0) {
            this.select2 = res1.data.data
            if (this.id != 0) {
              getRequirementDetail({ id: this.id }).then(res2 => {
                if (res2.data.errno === 0) {
                  getOrgTree({ 'orgId': res2.data.data.provOrgId })
                    .then(response => {
                      this.selectTreedata1 = response.data.data
                      this.checkedOrgs = res2.data.data.belongTo
                      this.selectOrgs1.push(res2.data.data.belongTo)
                    })
                  getOrgTree({ 'orgId': res2.data.data.deptId })
                    .then(response => {
                      this.selectTreedata2 = response.data.data
                      this.checkedOrgs1 = res2.data.data.orgId
                      this.selectOrgs2.push(res2.data.data.orgId)
                    })

                  this.selectOrgs.push(res2.data.data.provOrgId)

                  if (res2.data.data.provOrgId == '' || !res2.data.data.provOrgId) {
                    this.departmentForm.belongToName = res2.data.data.belongToName
                    this.departmentForm.orgName = res2.data.data.orgName
                    this.departmentForm = Object.assign({}, res2.data.data, {
                      isOpen: '1'
                    })
                  } else {
                    setTimeout(() => {
                      // this.selectOrgs.push(this.departmentForm.belongTo ? this.departmentForm.belongTo : this.departmentForm.provOrgId)
                      this.departmentForm = Object.assign({}, res2.data.data, {
                        isOpen: '0'
                      })
                      console.log(this.selectOrgs)
                    }, 200)
                  }
                  setTimeout(() => {
                    this.departmentForm = Object.assign({}, this.departmentForm, res2.data.data)
                  }, 50)
                }
              }).catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
            } else {
              this.selectTreedata1 = []
            }
          }
        })
      }
    })
    this.getTreeData()
    // this.getSelectTreedata()
    this.getDemandTreedata()
  },
  methods: {
    // 需求方内设部门树
    getDemandTreedata() {
      getOrgTree({ 'orgId': this.dataObj.provOrgId })
        .then(response => {
          this.selectTreedata2 = response.data.data
        })
    },
    // 提供单位内设部门树
    getSelectTreedata() {
      getOrgTree({ 'orgId': this.departmentForm.provOrgId })
        .then(response => {
          this.selectTreedata1 = response.data.data
        })
    },
    // 内设部门
    getCheckedOrg(args) {
      this.checkedOrgs = args[1]
      console.log(args[1])
      this.departmentForm.belongTo = (args[1] && args[1].length > 0) ? args[1][0].orgId : this.departmentForm.belongTo
      this.departmentForm.belongToCode = (args[1] && args[1].length > 0) ? args[1][0].orgCd : this.departmentForm.belongToCode
      console.log(this.departmentForm.belongTo, this.departmentForm.belongToCode)
    },
    getCheckedOrg1(args) {
      this.checkedOrgs1 = args[1]
      console.log(args[1])
      this.departmentForm.orgId = (args[1] && args[1].length > 0) ? args[1][0].orgId : this.departmentForm.orgId
      this.departmentForm.orgCd = (args[1] && args[1].length > 0) ? args[1][0].orgCd : this.departmentForm.orgCd
    },
    getCheckedDep(args) {
      console.log(args[1])
      setTimeout(() => {
        this.$refs['localProvOrgCodeForm'].clearValidate()
      }, 200)

      // 判断选择的是部门 还是 机构
      // 如果是部门 则只穿provOrgId provOrgCode
      // 如果 是 机构 则需要 传递belongTo belongToCode
      if (args[1][0]) {
        // this.$refs['localProvOrgCodeForm'].clearValidate()
        if (args[1][0].parOrgId) { // 非部门
          this.$refs['localProvOrgCodeForm'].clearValidate()
          let deptId = ''
          let deptCd = ''
          // 获取node
          console.log(this.$refs.selectTree.$refs)
          // 获取到selectTree dom
          const node = this.$refs.selectTree.$refs.treeForm.getNode(args[1][0].orgId)
          // 递归获取到部门级别获取部门id和code
          _resolve(node)
          function _resolve(e) {
            for (const k in e) {
              if (k == 'parent') {
                if (e[k] != null && e[k].level != 0) {
                  _resolve(e.parent)
                } else {
                  deptId = e.data.orgId
                  deptCd = e.data.orgCd
                }
              }
            }
          }
          this.departmentForm.provOrgCode = deptCd
          this.departmentForm.provOrgId = deptId
          this.departmentForm.belongTo = args[1][0].orgId
          this.departmentForm.belongToCode = args[1][0].orgCd
        } else { // 部门
          this.departmentForm.provOrgCode = args[1][0].orgCd
          this.departmentForm.provOrgId = args[1][0].orgId
          // this.departmentForm.belongTo = ''
          // this.departmentForm.belongToCode = ''
          this.getSelectTreedata()
        }
      } else {
      }
    },
    // getCheckedOrg(args) {
    //   const orgId = args[1][0].orgId
    //   const orgCd = args[1][0].orgCd
    //   this.departmentForm.belongTo = orgId
    //   this.departmentForm.belongToCode = orgCd
    // },
    getTreeData() {
      getProvDepTree().then(res => {
        if (res.data) {
          this.selectTreedata = res.data[0].children
        }
      })
    },
    submit() {
      this.$refs.departmentFormDom.validate(valid => {
        if (valid) {
          if (!this.checkedOrgs || this.checkedOrgs.length == 0) {
            this.departmentForm.belongTo = 0
            this.departmentForm.belongToCode = ''
            // this.departmentForm.belongToName = ''
          }
          /* 资源树后数据空，清空数据*/
          // if (!this.checkedOrgs1 || this.checkedOrgs1.length == 0) {
          //   this.departmentForm.provOrgId = 0
          //   this.departmentForm.provOrgCode = ''
          //   // this.departmentForm.belongToName = ''
          // }
          const sendData = Object.assign({}, this.departmentForm, { deptId: this.$route.query.deptId })
          // 0 新增 1编辑
          if (this.id === 0 || this.id === '0') {
            addRequirementCatalog(sendData).then(res => {
              if (res.data.errno === 0) {
                this.$router.replace({
                  path: `/Cataloging/requirementCatalogMaintain/${this.rowId}`, query: {
                    provOrgId: this.dataObj.provOrgId,
                    provOrgName: this.dataObj.provOrgName
                  }
                })
                this.$notify.success(`新增信息需求目录成功`)
                this.gotoPath(1)
              }
            }).catch(err => {
              this.$message.error(err.data.errmsg || '出错了')
            })
          } else {
            updateRequirementCatalog(sendData).then(res => {
              if (res.data.errno === 0) {
                this.$notify.success(`编辑信息需求目录成功`)
                this.gotoPath(1)
              }
            }).catch(err => {
              this.$message.error(err.data.errmsg || '出错了')
            })
          }
        }
      })
    },
    gotoPath(num) {
      this.$router.push(
        { path: '/Cataloging/requirementCatalog' }
      )
    }
  }
}
</script>

<style scoped>
.search_result {
  margin: 0 auto;
  margin-top: 20px;
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
  min-height: 750px !important;
  height: auto !important;
}

.tree_title {
  padding-left: 30px;
  height: 60px;
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
.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tree_area {
  background: #f9f9f9;
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}

.related_btns {
  margin-top: 10px;
  margin-bottom: 20px;
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
.tab_area {
  padding-left: 40px;
  padding-right: 40px;
}
.selectBtn :hover,
.selectBtn:focus {
  color: rgba(31, 51, 101, 0.9) !important;
}
.codeHint {
  font-size: 12px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  line-height: 33px;
  color: rgba(126, 126, 126, 1);
  opacity: 1;
}
.multipleResSelect {
  width: 100% !important;
}
</style>
<style>
.res_tabs .el-tabs__item.is-active {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
  opacity: 1;
}
.res_tabs .el-tabs__item {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(77, 77, 77, 1);
  opacity: 1;
}
</style>
