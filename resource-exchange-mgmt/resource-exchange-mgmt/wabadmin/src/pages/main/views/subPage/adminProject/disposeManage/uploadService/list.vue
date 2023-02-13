<template>
  <el-main>
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.name"
            placeholder="请输入服务名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select placeholder="请选择服务类型" :popper-append-to-body='false' clearable v-model="SearchItem.type" size="medium">
            <el-option label="节点运行服务" value="node"></el-option>
            <el-option label="API集成服务" value="dcapi"></el-option>
            <el-option label="消息集成服务" value="emq"></el-option>
            <el-option label="数据缓存服务" value="datacache"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size='medium'
            icon="el-icon-search"
            @click="search">查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     size='medium'
                     icon="el-icon-plus"
                     @click="add">添加
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- 调用表格 -->
    <el-col :span="24">
      <tableForm :table-data='tableData'
                 :table-selection="tableSelection"
                 :table-label="tableHeader"
                 :table-option="tableOption">
      </tableForm>
    </el-col>
    <el-col :span='24'>
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>
    <!-- 添加安装包 -->
    <dialog-form :isShow="isShow" :title="title" @closeDialog="closeDialog" @saveDialog="saveDialog">
      <el-form slot="form" ref="form" v-loading="loading" element-loading-text="上传中..." :model="form" :rules="rules" label-width="120px">
        <el-form-item label="服务名称">
          <el-input autocomplete="off" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="文件上传" prop="url" style="text-align: left;" v-show="!isEdit">
          <span v-if="form.filename">{{ form.filename }}</span>
          <el-upload
            class="upload-demo fl"
            :action='fileUrl'
            :headers='header'
            :data="fileData"
            :show-file-list='false'
            :before-upload='handleContentChange'
            :on-success='handleUploadInstallPackSuccess'>
            <el-button type="primary" plain>请上传安装包</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="服务类型" v-show="!isEdit">
          <el-select style="width: 100%;" placeholder="请选择服务类型" clearable v-model="form.type">
            <el-option label="节点运行服务" value="node"></el-option>
            <el-option label="API集成服务" value="dcapi"></el-option>
            <el-option label="消息集成服务" value="emq"></el-option>
            <el-option label="数据缓存服务" value="datacache"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input type="textarea" v-model="form.remark"></el-input>
        </el-form-item>
      </el-form>
    </dialog-form>
  </el-main>
</template>

<script>
  import dialogForm from '../../../../integration/dialogForm'
  import tableForm from '../../../../integration/tableList'
  import paging from '../../../../integration/pagination'
  import * as session from '../../../../../../../assets/js/common'

  export default {
    components: {tableForm, paging, dialogForm},

    data () {
      return {
        loading: false,
        id: '',
        fileData: {
          type: ''
        },
        title: '添加安装包',
        isShow: false,
        isEdit: false,
        typeArr: [
          {
            id: 'node',
            name: '节点运行服务'
          },
          {
            id: 'dcapi',
            name: 'API网关服务'
          },
          {
            id: 'emq',
            name: '消息集成服务'
          },
          {
            id: 'datacache',
            name: '数据缓存服务'
          }
        ],
        form: {
          type: '',
          filename: '',
          url: '',
          name: '',
          remark: ''
        },
        rules: {
          type: [
            {required: true, message: '请选择服务类型', trigger: 'blur'}
          ]
        },
        header: {
          'Authorization': 'token ' + session.default.session('currentUser').token
        },
        fileUrl: this.Interface.SysPluginInstallPack.upload,
        tableData: [],
        SearchItem: {
          name: '',
          type: ''
        },
        lastSearch: {
          name: '',
          type: ''
        },
        tableSelection: {},
        tableHeader: [
          {
            id: false,
            type: '',
            label: '名称',
            list: 'name'
          },
          {
            id: false,
            type: '',
            label: '类型',
            list: 'type'
          },
          {
            id: false,
            type: '',
            label: '版本',
            list: 'version'
          },
          {
            id: false,
            type: '',
            label: '创建时间',
            list: 'createTime'
          },
          {
            id: false,
            type: '',
            label: '备注',
            list: 'remark'
          }
        ],
        tableOption: {
          label: '操作',
          value: 0,
          options: [
            {
              label: '编辑',
              key: 0,
              type: 'text',
              icon: 'el-icon-edit',
              State: false,
              method: (row) => {
                this.edit(row)
              }
            }
          ]
        },
        total: 0,
        pageSize: 20,
        currentPage: 1
      }
    },

    mounted () {
      this.getList()
    },

    methods: {
      add () {
        this.title = '添加安装包'
        this.isShow = true
        this.isEdit = false
        this.id = ''
      },
      handleContentChange (file) {
        let that = this
        let fileSize = file.size / 1024 / 1024 < 500
        this.loading = true
        if (file.type !== 'application/zip' && file.type !== 'application/x-zip-compressed') {
          that.$message.error('仅支持zip格式文件上传')
          that.loading = false
          return false
        }
        if (!fileSize) {
          that.$message.error('上传超出文件限制大小')
          that.loading = false
          return false
        }

        let filename = file.name
        if (filename) {
          // node-1.zip
          filename = filename.split('.')[0]
          this.form.name = filename
          if (filename && filename.indexOf('-') !== -1) {
            this.form.type = filename.split('-')[0]
          }
        }

        this.fileData.type = this.form.type
      },

      handleUploadInstallPackSuccess (response, file, fileList) {
        let that = this
        this.loading = false
        if (response) {
          that.form.filename = response.fileName
          that.$message.success('上传成功')
        } else {
          that.$message.error('上传失败')
        }
      },
      closeDialog () {
        this.isShow = false
        this.id = ''
        this.$refs['form'].resetFields()
        this.form = {
          type: '',
          url: '',
          filename: '',
          name: '',
          remark: ''
        }
      },
      saveDialog () {
        const that = this
        this.$refs['form'].validate(async (valid) => {
          if (valid) {
            try {
              let url = that.Interface.SysPluginInstallPack.insert
              let obj = {
                name: that.form.name,
                type: that.form.type,
                filename: that.form.filename,
                remark: that.form.remark
              }
              let response = null
              if (that.id) {
                url = that.Interface.SysPluginInstallPack.update
                obj.id = that.id
                response = await this.request.dataPut(that, url, obj)
              } else {
                response = await this.request.dataPost(that, url, obj)
              }
              if (response.data.code === 1) {
                that.isShow = false
                that.$message.success(response.data.msg)
                that.getList()
                that.closeDialog()
                return false
              }
              that.$message.error(response.data.msg)
            } catch (even) {
              that.$message.error('数据获取失败')
            }
          }
        })
      },
      // 编辑
      async edit (row) {
        this.isEdit = true
        this.title = '编辑'
        this.isShow = true
        this.id = row.id
        this.form.name = row.name
        this.form.remark = row.remark
      },
      // 列表
      async getList () {
        const that = this
        try {
          let findUrl = this.Interface.SysPluginInstallPack.findAll
          let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
          let obj = {
            name: this.SearchItem.name,
            type: this.SearchItem.type
          }
          const response = await this.request.dataGet(that, url, obj)
          response.data.data.content.forEach(item => {
            switch (item.type) {
              case 'node':
                item.type = '节点运行服务'
                break
              case 'dcapi':
                item.type = 'API集成服务'
                break
              case 'emq':
                item.type = '系统消息服务'
                break
              case 'datacache':
                item.type = '数据缓存服务'
                break
            }
          })
          that.tableData = response.data.data.content
          that.total = response.data.data.totalElements
          // if (that.total === 0) {
          //   that.add()
          // }
        } catch (even) {
          that.$message.error('数据获取失败')
        }
      },

      // 查询
      search: function () {
        const that = this
        Object.entries(that.SearchItem).map((item, index) => {
          that.lastSearch[item[0]] = that.SearchItem[item[0]]
        })
        that.currentPage = 1
        this.getList()
      },

      // 分页
      pageChange (item) {
        const that = this
        that.currentPage = item.page
        that.pageSize = item.limit
        that.getList()
      }
    }
  }
</script>
