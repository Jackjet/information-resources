<template>
  <div class="app-container mt20 department_content">
    <div class="type_area">资料发布</div>
    <div class="operate_area">
      <!-- 查询和其他操作 -->
      <div class="filter-container" style="margin-top: 15px;">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-button v-permission="['GET /admin/fileCenterRel/list1']" style="margin-left: 22px" class="remove fr" size="mini" icon="el-icon-search" @click="handleFilter">查询</el-button>
            <el-button v-permission="['POST /admin/fileCenterRel/save']" style="margin-left: 10px;position: relative;left: -10px" class="add " size="mini" @click="addUpload(0)">
              <svg-icon icon-class="i_upload" style="margin-right: 5px;position: relative;top:1px;font-size: 14px" />
              上传文件
            </el-button>
            <el-button v-permission="['POST /admin/fileCenterRel/removeBatch']" class="add headItem" size="mini" icon="el-icon-circle-close" @click="deleteFile">删除</el-button>
            <el-input v-model="listQuery.fileName" style="width:205px" clearable class="filter-item fr" placeholder="文件名称" @clear="handleFilter" />
            <!--   <span class="filter_text fr">文件名称</span>-->
          </el-col>
        </el-row>
      </div>
      <!-- 查询结果 -->
      <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" stripe :header-cell-style="getRowClass" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column label="文件名称" prop="fileName" show-overflow-tooltip />
        <el-table-column label="格式" prop="format" width="100px" align="center" />
        <el-table-column label="大小" prop="size" width="100px" align="center">
          <template slot-scope="scope">
            <div>
              {{ scope.row.size?scope.row.size+'M':scope.row.size }}
            </div>
          </template>
        </el-table-column>
        <el-table-column label="下载次数" prop="dwCount" width="80px" align="center" />
        <el-table-column label="发布时间" prop="createTime" />
        <el-table-column label="发布人" prop="createBy" />
        <el-table-column label="操作" prop="result" width="100px" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-tooltip v-permission="['POST /admin/fileCenterRel/update']" content="编辑" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl">
                <svg-icon icon-class="catalog_edit" class-name="" @click="addUpload(1,scope.row)" />
              </span>
            </el-tooltip>
            <el-tooltip v-permission="['GET /admin/fileCenterRel/detail']" content="下载" effect="light" :open-delay="1000" placement="bottom">
              <span class="tr_detail_icon fl">
                <svg-icon icon-class="down" class-name @click="getDownFile(scope.row)" />
              </span>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" :layout="'prev,pager,next'" @pagination="getList" />
      <!-- 删除 -->
      <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
        <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
      </el-dialog>
      <!--上传文件弹窗-->
      <el-dialog v-if="addWorkOrder" :modal-append-to-body="false" title="上传文件" align="center" :visible.sync="addWorkOrder" width="50%" :close-on-click-modal="false" @close="closedDialog1('uploadForm','uploadForm')">
        <el-form ref="uploadForm" :model="uploadForm" align="center" :rules="uploadRules" class="demo-ruleForm" label-position="left">
          <el-form-item ref="uploadItemDom" label="数据文件：" class="required_label" label-width="100px" prop="fileName2" align="left">
            <el-upload ref="uploadDom" :headers="headers" :action="uploadPath" :show-file-list="true" :limit="1" :on-exceed="handleExceed" :before-upload="beforeUpload" :on-success="uploadSuccess" :file-list="fileList" :on-remove="removeFlieList" accept=".zip,.rar,.pdf,.txt,.doc,.docx,.xls,.xlsx">
              <el-input v-model="uploadForm.fileName2" class="fl" placeholder="文件名称" style="width: 205px" />
              <el-button style="margin-left: 22px" class="remove" size="mini" icon="el-icon-search" :loading="btnLoading">浏览
              </el-button>
              <div slot="tip" class="el-upload__tip">
                <p style="text-align: left">上传说明：</p>
                <p style="text-align: left">1、只能上传一个文件，多个文件时请打包上传。</p>
                <p style="text-align: left">2、文件格式包括zip、rar、pdf、txt、doc、docx、xls、 xlsx </p>
                <p style="text-align: left">3、文件大小不超：40M</p>
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item ref="fileNameDom" label="文件名称：" class="required_label" label-width="100px" prop="fileName">
            <el-input v-model="uploadForm.fileName" style="width:100%" placeholder="文件名称" />
          </el-form-item>
          <el-form-item label="文件说明：" label-width="100px">
            <el-input v-model="uploadForm.fileRemark" type="textarea" style="width:100%" placeholder="文件说明" maxlength="255" />
          </el-form-item>
        </el-form>
        <div class="dialogDom" align="center">
          <el-button size="mini" class="add" @click="confirmUpload">提 交</el-button>
          <el-button size="mini" class="remove" @click="addWorkOrder=false;closedDialog1('uploadForm','uploadForm')">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<style>
.el-upload__tip > p {
  line-height: 20px;
  margin: 0;
}
.leftSpan {
  color: #172853;
  font-size: 18px;
}
.leftTree {
  border: 1px solid rgb(201, 201, 201);
  max-height: 400px;
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
</style>

<script>
import { getToken } from '@/utils/auth'
import { docPublishList, getUploadDetail, delFile, saveFile, updateFile, getCount } from '@/api/doc'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters } from 'vuex'
import DelConfirm from '@/components/DelConfirm'
import mixinJs from '@/utils/mixin'
export default {
  name: 'Operation',
  components: { Pagination, DelConfirm },
  mixins: [mixinJs],
  data() {
    return {
      fileId: '',
      flag: 0,
      multipleSelection: [],
      tipStr: '确认删除所选文件吗？',
      delDialog: false,
      fileList: [],
      uploadForm: {},
      addWorkOrder: false,
      timeRange: null,
      listLoading: false,
      list: null,
      total: 0,
      listQuery: {
        current: 1,
        size: 10,
        fileName: ''
      },
      uploadRules: {
        fileName: [{ required: true, message: '请选择上传文件', trigger: 'change' }],
        fileName2: [{ required: true, message: '请选择上传文件', trigger: 'change' }]
      }
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
    // console.log(this.userid, this.propertyId, this.roles);
  },
  methods: {
    // 删除
    deleteFile(data) {
      this.tempDelData = Object.assign({}, data)
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请至少选择一条信息资源',
          type: 'error'
        })
      } else {
        this.delDialog = true
      }
    },
    // 删除
    confirmDelete() {
      console.log(this.tempDelData)
      const listData = this.multipleSelection
      const idArr = []
      for (const i in listData) {
        idArr.push(listData[i].id)
      }
      delFile(idArr)
        .then(response => {
          if (response.data.errno == 0) {
            this.$notify.success('删除成功')
            this.getList()
            this.delDialog = false
            this.tempDelData = {}
          } else {
            this.$message.error(response.data.errmsg || '出错了~')
          }
        }).catch(err => {
          console.log(err)
          this.$message.error(err.data.errmsg || '出错了~')
        })
    },
    confirmUpload() {
      this.$refs['uploadForm'].validate(valid => {
        if (valid) {
          let sendData = {}
          if (this.flag == 0) {
            sendData = Object.assign({}, this.uploadForm)
            saveFile(sendData).then(res => {
              if (res.data.errno === 0) {
                this.$notify.success('上传文件成功')
                this.addWorkOrder = false
                this.closedDialog1('uploadForm', 'uploadForm')
                this.getList()
              }
            }).catch(err => {
              this.$message.error(err.data.errmsg || '出错了')
            })
          } else {
            sendData = Object.assign({}, {
              fileName: this.uploadForm.fileName,
              fileRemark: this.uploadForm.fileRemark,
              storageId: this.uploadForm.storageId,
              id: this.fileId
            })
            updateFile(sendData).then(res => {
              if (res.data.errno === 0) {
                this.$notify.success('上传文件成功')
                this.addWorkOrder = false
                this.closedDialog1('uploadForm', 'uploadForm')
                this.getList()
              }
            }).catch(err => {
              this.$message.error(err.data.errmsg || '出错了')
            })
          }
        }
      })
    },
    removeFlieList(file, fileList) {
      this.uploadForm = Object.assign({}, this.uploadForm, {
        storageId: '',
        fileName: '',
        fileName2: ''
      })
    },
    closedDialog1(attr, formName) {
      this.$refs[formName].resetFields()
      this[attr] = {}
      this.fileList = []
      this.$refs.selectTreeDom ? this.$refs.selectTreeDom.clearAll() : ''
    },
    addUpload(type, data) {
      if (type === 0) {
        // 新增
        this.flag = 0
        this.addWorkOrder = true
      } else {
        const id = data.id
        getUploadDetail({ id: id }).then(res => {
          if (res.data.errno === 0) {
            this.flag = 1
            this.fileId = data.id
            this.uploadForm = Object.assign({}, res.data.data, { fileName2: res.data.data.name })
            this.fileList = []

            this.fileList.push({
              name: this.uploadForm.name,
              url: this.uploadForm.url
            })
            this.addWorkOrder = true
          }
        }).catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
      }
    },
    // 下载
    getDownFile(data) {
      this.downloadFileIframe(data.url, () => {
        getCount({ id: data.id }).then(res => {
          if (res.data.errno === 0) {
            this.getList()
          }
        }).catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
      })
    },
    uploadSuccess(response) {
      this.btnLoading = false
      console.log(response.data)
      const arr = response.data.name.split('.')
      this.uploadForm = Object.assign({}, this.uploadForm, {
        storageId: response.data.id * 1,
        fileName: arr.slice(0, arr.length - 1).join('.'),
        fileName2: response.data.name
      })
      this.$refs.uploadItemDom.clearValidate()
      this.$refs.fileNameDom.clearValidate()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    getList() {
      this.listLoading = true
      console.log(this.listQuery)
      docPublishList(this.listQuery).then(res => {
        if (res.data.errno === 0) {
          this.list = res.data.data.records || []
          this.total = res.data.data.total
        }
        this.listLoading = false
      }).catch(err => {
        this.listLoading = false
        this.$message.error(err.data.errmsg || '出错了')
      })
    }

  }
}
</script>

<style scoped>
.el-upload--text {
  min-width: 350px;
}
.filter-container .filter-item {
  display: table;
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
.filter_text {
  margin-left: 36px;
  line-height: 36px;
  margin-right: 6px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
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

