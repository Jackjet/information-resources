<template>
  <div class="app-container">

    <!-- 查询和其他操作 -->
    <div class="filter-container">
      <el-input v-model="listQuery.title" clearable class="filter-item" style="width: 200px;" placeholder="请输入标题关键字" />
      <el-input v-model="listQuery.content" clearable class="filter-item" style="width: 200px;" placeholder="请输入内容关键字" />
      <el-button v-permission="['GET /admin/noticenew/list']" class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">查询</el-button>
      <el-button v-permission="['GET /admin/noticenew/list']" class="filter-item" type="primary" @click="reset">重置</el-button>
      <!--<el-button :loading="downloadLoading" class="filter-item" type="primary" icon="el-icon-download" @click="handleDownload">导出</el-button>-->
    </div>

    <div class="operator-container">
      <el-button v-permission="['POST /admin/noticenew/create']" class="filter-item" type="primary" icon="el-icon-edit" @click="handleCreate">新增</el-button>
      <el-button v-permission="['GET /admin/noticenew/batch-delete']" class="filter-item" type="danger" icon="el-icon-delete" @click="handleBatchDelete">批量删除</el-button>
    </div>

    <!-- 查询结果 -->
    <el-table v-loading="listLoading" :data="list" element-loading-text="正在查询中。。。" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />

      <el-table-column align="center" label="通知标题" prop="title" show-overflow-tooltip />
      <el-table-column align="center" label="通知类型" prop="type">
        <template slot-scope="scope">
          <div v-if="scope.row.type == 1">物业管理</div>
          <div v-if="scope.row.type == 2">社区App</div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="通知详情" prop="content">
        <template slot-scope="scope">
          <el-dialog :visible.sync="contentDialogVisible" title="通知详情" :close-on-click-modal="false">
            <div class="content_wrap" v-html="contentDetail" />
          </el-dialog>
          <el-button type="primary" size="mini" @click="showContent(scope.row.content)">查看</el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="新增时间" prop="addTime" />

      <el-table-column align="center" label="管理员账号/姓名" prop="adminName">
        <template slot-scope="scope">
          <span>{{ scope.row.adminAccount || '暂无' }}/{{ scope.row.adminName|| '暂无' }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否已删除" prop="deleted">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.deleted" type="info">已删除</el-tag>
          <el-tag v-if="!scope.row.deleted" type="success">未删除</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200px" min-width="100" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button v-permission="['POST /admin/noticenew/update']" type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button v-if="!scope.row.deleted" v-permission="['GET /admin/noticenew/delete']" type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
          <el-button v-if="scope.row.deleted" v-permission="['GET /admin/noticenew/delete']" type="success" size="mini" @click="handleDelete(scope.row,2)">启用</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.current" :limit.sync="listQuery.size" @pagination="getList" />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="70%" :close-on-click-modal="false" @close="closedDialog('dataForm','dataForm')">
      <el-form ref="dataForm" :rules="rules" :model="dataForm" status-icon label-position="left" label-width="100px">
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="dataForm.title" />
        </el-form-item>
        <el-form-item label="通知类型" prop="type">
          <el-radio-group v-model="dataForm.type">
            <el-radio :label="'1'">物业管理</el-radio>
            <el-radio :label="'2'">社区App</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="通知内容" prop="content">
          <editor v-model="dataForm.content" :init="editorInit" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">确定</el-button>
        <el-button v-else type="primary" @click="updateData">确定</el-button>
      </div>
    </el-dialog>

    <el-tooltip placement="top" content="返回顶部">
      <back-to-top :visibility-height="100" />
    </el-tooltip>

  </div>
</template>
<style>
  .content_wrap img{
    max-width: 400px;
    max-height: 400px;
  }
</style>
<script>
import { listNotice, createNotice, updateNotice, deleteNotice, batchDeleteNotice } from '@/api/notice'
import BackToTop from '@/components/BackToTop'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import _ from 'lodash'
import Editor from '@tinymce/tinymce-vue'
import { createStorage } from '@/api/storage'
import { getToken } from '@/utils/auth'

export default {
  name: 'Notice',
  components: { BackToTop, Pagination, Editor },
  data() {
    const vm = this
    return {
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        current: 1,
        size: 10,
        title: undefined,
        content: undefined,
        sort: 'add_time',
        order: 'desc'
      },
      multipleSelection: [],
      contentDetail: '',
      contentDialogVisible: false,
      dataForm: {
        id: undefined,
        title: undefined,
        content: undefined
      },

      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '创建'
      },
      rules: {
        title: [
          { required: true, message: '通知标题不能为空', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '通知标题不能为空', trigger: 'blur' }
        ]
      },
      limitLength: false,
      txtNum: 0,
      txtLength: 0,
      editorInit: {
        language: 'zh_CN',
        height: 200,
        convert_urls: false,
        ax_wordlimit_num: 5000,
        ax_wordlimit_delay: 1500,
        ax_wordlimit_callback: function(editor, txt, num) {
          vm.$message.error('当前字数：' + txt.length + '，限制字数：' + num)
          vm.limitLength = true
          vm.txtLength = txt.length
          vm.txtNum = num
        },
        ax_wordlimit_okCallback: function(editor, txt, num) {
          console.log('当前字数：' + txt.length + '，限制字数：' + num)
          vm.limitLength = false
          vm.txtLength = txt.length
          vm.txtNum = num
        },
        plugins: ['ax_wordlimit advlist anchor autolink autosave code codesample colorpicker colorpicker contextmenu directionality emoticons fullscreen hr image imagetools importcss insertdatetime link lists media nonbreaking noneditable pagebreak paste preview print save searchreplace spellchecker tabfocus table template textcolor textpattern visualblocks visualchars '],
        toolbar: ['searchreplace bold italic underline strikethrough alignleft aligncenter alignright outdent indent  blockquote undo redo removeformat subscript superscript code codesample', 'hr bullist numlist link image charmap preview anchor pagebreak insertdatetime media table emoticons forecolor backcolor fullscreen'],
        images_upload_handler: function(blobInfo, success, failure) {
          const formData = new FormData()
          formData.append('file', blobInfo.blob())
          createStorage(formData).then(res => {
            success(res.data.data.url)
          }).catch(() => {
            failure('上传失败，请重新上传')
          })
        }
      },
      downloadLoading: false
    }
  },
  computed: {
    headers() {
      return {
        'X-Litemall-Admin-Token': getToken()
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    reset() {
      this.listQuery.title = ''
      this.listQuery.content = ''
      this.getList()
    },
    getList() {
      this.listLoading = true
      listNotice(this.listQuery)
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
      this.listQuery.current = 1
      this.getList()
    },
    resetForm() {
      this.dataForm = {
        id: undefined,
        title: '',
        type: '',
        content: ''
      }
    },
    closedDialog(attr, formName) {
      this.$refs[formName].resetFields()
      this[attr] = {}
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
          if (this.limitLength) {
            this.$message.error('当前字数：' + this.txtLength + '，限制字数：' + this.txtNum)
            return
          }
          if (this.dataForm.content.length > 5000) {
            this.$message.error('输入内容过多，长度不能超过5000')
            return
          }
          createNotice(this.dataForm)
            .then(response => {
              /* this.list.unshift(response.data.data)*/
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '创建成功'
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
          if (this.limitLength) {
            this.$message.error('当前字数：' + this.txtLength + '，限制字数：' + this.txtNum)
            return
          }
          if (this.dataForm.content.length > 5000) {
            this.$message.error('输入内容过多，长度不能超过5000')
            return
          }
          this.dataForm.deleted = false

          updateNotice(this.dataForm)
            .then(() => {
              /* for (const v of this.list) {
                if (v.id === this.dataForm.id) {
                  const index = this.list.indexOf(v)
                  this.list.splice(index, 1, this.dataForm)
                  break
                }
              }*/
              this.dialogFormVisible = false
              this.$notify.success({
                title: '成功',
                message: '更新成功'
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
    handleDelete(row, type) {
      if (type && type === 2) {
        this.dataForm = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dataForm.deleted = false
        updateNotice(this.dataForm)
          .then(() => {
            /* for (const v of this.list) {
                      if (v.id === this.dataForm.id) {
                        const index = this.list.indexOf(v)
                        this.list.splice(index, 1, this.dataForm)
                        break
                      }
                    }*/
            this.dialogFormVisible = false
            this.$notify.success({
              title: '成功',
              message: '更新成功'
            })
            this.getList()
          })
          .catch(response => {
            this.$notify.error({
              title: '失败',
              message: response.data.errmsg
            })
          })
      } else {
        deleteNotice(row)
          .then(response => {
            this.$notify.success({
              title: '成功',
              message: '删除通知成功'
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
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    showContent(content) {
      this.contentDetail = content
      this.contentDialogVisible = true
    },
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.error('请选择至少一条记录')
        return
      }
      const ids = []
      _.forEach(this.multipleSelection, function(item) {
        ids.push(item.id)
      })
      batchDeleteNotice({ idstr: ids.join(',') })
        .then(response => {
          this.$notify.success({
            title: '成功',
            message: '批量删除通知成功'
          })
          this.getList()
        })
        .catch(response => {
          this.$notify.error({
            title: '失败',
            message: response.data.errmsg
          })
        })
    },
    handleDownload() {
      this.downloadLoading = true
      import('@/vendor/Export2Excel').then(excel => {
        const tHeader = [
          '通知ID',
          '通知标题',
          '管理员ID',
          '新增时间',
          '更新时间'
        ]
        const filterVal = [
          'id',
          'title',
          'adminId',
          'addTime',
          'updateTime'
        ]
        excel.export_json_to_excel2(tHeader, this.list, filterVal, '通知')
        this.downloadLoading = false
      })
    }
  }
}
</script>
