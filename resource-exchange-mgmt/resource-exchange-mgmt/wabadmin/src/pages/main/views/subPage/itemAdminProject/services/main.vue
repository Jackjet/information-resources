<template>
  <el-main v-loading="Loading">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-input
            clearable
            size="medium"
            autocomplete="off"
            v-model="SearchItem.name"
            placeholder="请输入系统服务名称">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-select placeholder="请选择模板类型"
                     :popper-append-to-body='false'
                     clearable v-model="SearchItem.templateType"
                     size="medium">
            <el-option label="脚本运行服务" value="webapi"></el-option>
            <el-option label="消息集成服务" value="emq"></el-option>
            <el-option label="API集成服务" value="dcapi"></el-option>
            <el-option label="数据缓存服务" value="datacache"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="medium"
            icon="el-icon-search"
            @click="search">查询
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary"
                     size="medium"
                     icon="el-icon-plus"
                     @click="addService">添加
          </el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!-- 调用表格 -->
    <el-col :span="24">
      <!--      <tableForm :table-data='tableData'-->
      <!--                 :table-selection="tableSelection"-->
      <!--                 :table-label="tableHeader"-->
      <!--                 :table-option="tableOption">-->
      <!--      </tableForm>-->
      <el-table :data="tableData" style="width: 100%;margin-bottom: 20px;" row-key="id"
                lazy v-loading="Loading">
        <el-table-column
          prop="runNodeId"
          label="节点名称"
          align="center">
          <template slot-scope="scope">
            <el-image :src="require('@/assets/image/icons/节点 (1).png')"
                      style="margin-bottom:-5px;width: 20px;height: 20px;"></el-image>
            {{ scope.row.nodeName }}
          </template>
        </el-table-column>
        <el-table-column
          prop="templateType"
          label="服务类型"
          align="center">
          <template slot-scope="scope">
            <template v-if="scope.row.templateType === 'webapi'">
              <el-image :src="require('./images/webapi.png')" style="width: 20px;height: 20px;"
                        title="脚本运行服务"></el-image>
            </template>
            <template v-else-if="scope.row.templateType === 'emq'">
              <el-image :src="require('./images/emq.png')" style="width: 20px;height: 20px;" title="消息集成服务"></el-image>
            </template>
            <template v-else-if="scope.row.templateType === 'dcapi'">
              <el-image :src="require('./images/api.png')" style="width: 20px;height: 20px;" title="API集成服务"></el-image>
            </template>
            <template v-else-if="scope.row.templateType === 'datacache'">
              <el-image :src="require('./images/datacache.png')" style="width: 20px;height: 20px;"
                        title="数据缓存服务"></el-image>
            </template>
          </template>
        </el-table-column>
        <el-table-column
          prop="name"
          label="服务名称"
          align="center">
        </el-table-column>
        <el-table-column
          prop="status"
          label="状态"
          align="center"
          width="100px">
          <template slot-scope="scope">
            <template v-if="scope.row.status === 'online'">
              <div style="width: 150px;">
                <el-image :src="require('@/assets/image/icons/Online.png')"
                          style="float: left;width: 20px;height: 20px;"></el-image>
                <div style="float: left">在线</div>
              </div>
            </template>
            <template v-else-if="scope.row.status === 'offline'">
              <el-image :src="require('@/assets/image/icons/Online (1).png')"
                        style="float:left;width: 20px;height: 20px;"></el-image>
              <div style="float: left">离线</div>
            </template>
            <template v-else-if="scope.row.status === 'installing'">
              <el-image :src="require('@/assets/image/icons/installing.jpg')"
                        style="float:left;width: 20px;height: 20px;"></el-image>
              <div style="float: left">安装中</div>
            </template>
            <template v-else>
              <div style="float: left"></div>
            </template>
          </template>
        </el-table-column>
        <el-table-column
          prop="remark"
          label="备注"
          align="center">
        </el-table-column>
        <el-table-column fit align='center'
                         :label="tableOption.label" :width="tableOption.width"
                         v-if="tableOption.value === 0">
          <template slot-scope="scope">
            <template v-for="(value, item) in tableOption.options">
              <template v-if="value.type === 'develop'">
                <template v-if="scope.row.templateType === 'webapi'">
                  <pictureButton :key="item" :type="value.type" :label="value.label"
                                 @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
                </template>
              </template>
              <template v-else-if="value.type === 'setting'">
                <template v-if="scope.row.templateType !== 'webapi'">
                  <pictureButton :key="item" :type="value.type" :label="value.label"
                                 @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
                </template>
              </template>
              <template v-else>
                <pictureButton :key="item" :type="value.type" :label="value.label"
                               @click.native.prevent="value.method(scope.row, scope)"></pictureButton>
              </template>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <!-- 调用分页 -->
    <el-col :span='24'>
      <paging ref="pager" :total="total" @pageChange="pageChange"></paging>
    </el-col>

    <webapi
      ref="webapi"
      @Reload='submitLoading'>
    </webapi>
    <emq
      ref="emq"
      @Reload='submitLoading'>
    </emq>
    <dcapi
      ref="dcapi"
      @Reload='submitLoading'>
    </dcapi>
    <datacache
      ref="datacache"
      @Reload='submitLoading'>
    </datacache>

    <!-- 添加服务 -->
    <drawer
      ref="add"
      @Reload='submitLoading'>
    </drawer>
  </el-main>
</template>

<script>
import tableForm from '../../../integration/tableList'
import paging from '../../../integration/pagination'
import drawer from './add'
import pictureButton from '../../../integration/pictureButton'
import webapi from './serviceDisp/appInfo'
import emq from './EMQDisp/appInfo'
import dcapi from './APIDisp/appInfo'
import datacache from './dataCacheDisp/configuration/appInfo'
import * as session from '../../../../../../assets/js/common'

export default {
  components: {tableForm, paging, drawer, pictureButton, webapi, emq, dcapi, datacache},

  data () {
    return {
      integrationId: this.common.session('currentUser').id,
      tableData: [],
      SearchItem: {
        name: '',
        templateType: ''
      },
      tableSelection: {
        key: false,
        type: 'selection'
      },
      tableOption: {
        label: '操作',
        value: 0,
        width: '440px',
        options: [{
          label: '信息',
          key: 0,
          type: 'info',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.info(row)
          }
        },
        {
          label: '删除',
          key: 0,
          type: 'delete',
          icon: 'el-icon-delete',
          State: false,
          method: (row) => {
            this.delete(row)
          }
        },
        {
          label: '配置',
          key: 0,
          type: 'setting',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.more(row)
          }
        },
        {
          label: '开发',
          key: 0,
          type: 'develop',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.develop(row)
          }
        },
        {
          label: '安装',
          key: 0,
          type: 'install',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.beforeUpdata(row, 'install')
          }
        },
        {
          label: '启动',
          key: 0,
          type: 'start',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.beforeUpdata(row, 'start')
          }
        },
        {
          label: '停止',
          key: 0,
          type: 'stop',
          icon: 'el-icon-s-tools',
          State: false,
          method: (row) => {
            this.beforeUpdata(row, 'stop')
          }
        },
        {
          label: '日志',
          key: 0,
          type: 'logger',
          icon: 'el-icon-tickets',
          State: false,
          method: (row) => {
            this.toRecord(row)
          }
        }]
      },
      Loading: false,
      total: 0,
      pageSize: 20,
      currentPage: 1
    }
  },
  mounted () {
    this.initialService()
  },

  methods: {
    // 初始化获取当前全部列表数据
    async initialService () {
      const that = this
      try {
        let findUrl = this.Interface.sysService.find
        let url = findUrl + '?page=' + this.currentPage + '&size=' + this.pageSize
        let parameter = {
          integrationId: that.integrationId,
          name: that.SearchItem.name,
          templateType: that.SearchItem.templateType
        }
        that.Loading = true
        const response = await this.request.dataGet(that, url, parameter)
        that.Loading = false
        if (response.data.code === 1) {
          if (this.currentPage !== 1 && response.data.data.content.length === 0) {
            this.$refs.pager.handleCurrentChange(1)
            return false
          }
          that.tableData = response.data.data.content
          that.total = response.data.data.totalElements
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.Loading = false
        that.$message.error('数据获取失败')
      }
    },

    async more (row) {
      const that = this
      let toPath = ''
      switch (row.templateType) {
        case 'webapi':
          toPath = 'serviceDisp'
          break
        case 'emq':
          toPath = 'EMQDisp'
          break
        case 'dcapi':
          toPath = 'APIDisp'
          break
        case 'datacache':
          toPath = 'dataCacheDisp'
          break
        default:
          console.log(1)
          break
      }
      that.$router.push({
        path: toPath,
        query: {
          id: row.id,
          name: row.name,
          nodeName: row.nodeName,
          url: row.url
        }
      })
    },

    addService () {
      this.$refs.add.initial()
    },

    // 删除当前单条数据 重载列表
    delete (row) {
      const that = this
      that.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let url = this.Interface.sysService.delete
        that.Loading = true
        const response = await this.request.dataDelete(that, url, {id: row.id})
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          that.initialService()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        that.Loading = false
        return false
      })
    },

    toRecord (row) {
      this.$router.push({
        path: 'record',
        query: {
          id: row.id,
          nodeID: row.runNodeId
        }
      })
    },

    // 模糊查询
    search: function () {
      const that = this
      that.currentPage = 1
      this.$refs.pager.changePage()
      this.initialService()
    },

    // 获取搜索当前分页 页码 条数
    pageChange (item) {
      const that = this
      that.currentPage = item.page
      that.pageSize = item.limit
      that.initialService()
    },

    info (row) {
      this.$refs[row.templateType].initial(row.id)
    },
    submitLoading () {
      this.initialService()
    },
    async beforeUpdata (row, type) {
      const that = this
      try {
        let findUrl = that.Interface.runNode.isOnline
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, {id: row.runNodeId})
        that.loading = false
        if (response.data.code === 1) {
          if (response.data.data) {
            switch (type) {
              case 'install':
                let flag = true
                if (row.templateType === 'dcapi') {
                  flag = await this.findDatabase(row, that.Interface.api.config.find)
                } else if (row.templateType === 'emq') {
                  flag = await this.findDatabase(row, that.Interface.emq.config.find)
                } else {
                  await this.installService(row)
                }
                console.log(flag)
                if (flag) {
                  this.$alert('安装指令已经发送，安装过程大概需要2-3分钟，请每隔10秒刷新下浏览器，再查看在线状态', '提示')
                }
                break
              case 'start':
                await this.toPlay(row)
                break
              case 'stop':
                await this.toPause(row)
                break
            }
          } else {
            that.$message.warning('当前节点处于离线不可操作状态')
          }
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },
    async findDatabase (row, findUrl) {
      const that = this
      try {
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, {appid: row.id})
        that.loading = false
        if (response.data.code === 1) {
          if (response.data.data.host === '' || response.data.data.port === '' || response.data.data['spring.datasource.url'] === '' || response.data.data['server.port'] === '') {
            that.$message.warning('该服务尚未配置数据库信息，不可执行安装操作，请先配置数据库信息')
            return false
          } else {
            await that.installService(row)
          }
        } else {
          that.$message.error(response.data.msg)
          return false
        }
        return true
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
        return false
      }
    },
    async installService (row) {
      console.log(row)
      const that = this
      let url = ''
      if (row.templateType === 'webapi') {
        url = this.Interface.sysService.install
      } else if (row.templateType === 'emq') {
        url = this.Interface.emq.install
      } else if (row.templateType === 'datacache') {
        url = this.Interface.dataCache.install
      } else {
        url = this.Interface.api.install
      }
      try {
        that.loading = true
        const response = await that.request.dataPost(that, url, {appid: row.id})
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success('安装成功')
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.loading = false
        that.$message.error('操作失败')
      }
    },
    async toPlay (row) {
      const that = this
      try {
        let url = ''
        if (row.templateType === 'webapi') {
          url = this.Interface.webapi.start
        } else if (row.templateType === 'emq') {
          url = this.Interface.emq.start
        } else if (row.templateType === 'datacache') {
          url = this.Interface.dataCache.start
        } else {
          url = this.Interface.api.start
        }
        that.loading = true
        const response = await that.request.dataPut(that, url, {id: row.id})
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.loading = false
        that.$message.error('操作失败')
      }
    },
    async toPause (row) {
      const that = this
      try {
        let url = ''
        if (row.templateType === 'webapi') {
          url = this.Interface.webapi.stop
        } else if (row.templateType === 'emq') {
          url = this.Interface.emq.stop
        } else if (row.templateType === 'dataCache') {
          url = this.Interface.dataCache.stop
        } else {
          url = this.Interface.api.stop
        }
        that.loading = true
        const response = await that.request.dataPut(that, url, {id: row.id})
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.loading = false
        that.$message.error('操作失败')
      }
    },
    develop (row) {
      console.log(row)
      let url = 'shell.html?appid=' + row.id + '&integrationId=' + session.default.session('currentUser').id
      window.open(url, '_blank')
    }
  }
}
</script>
