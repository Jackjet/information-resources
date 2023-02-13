<template>
  <el-main v-loading="Loading">
    <el-col :span="24">
      <el-form :inline="true" class='el-InputForm'>
        <el-form-item>
          <el-button type="primary" @click="back" icon="el-icon-back">返 回</el-button>
        </el-form-item>
        <el-form-item
          label="节点名称:">
          <div :title="nodeName" class="status-box tit-box">
            {{ nodeName }}
          </div>
        </el-form-item>
        <el-form-item
          label="系统服务名称:">
          <div :title="nowName" class="status-box tit-box">
            {{ nowName }}
          </div>
        </el-form-item>
        <el-form-item
          label="当前/远程版本:">
          <div :title="nowNum" class="status-box tit-box">
            {{ nowNum }}
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="text"
                     @click="renovate">
            <i class="el-icon-refresh"></i>
          </el-button>
        </el-form-item>
      </el-form>
      <el-tabs style="margin-bottom: 20px;" type="border-card" v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="key配置" name="keyList">
          <keyList ref="keyList"></keyList>
        </el-tab-pane>
        <el-tab-pane label="消息服务配置" name="messageServiceConfig">
          <messageServiceConfig ref="messageServiceConfig"></messageServiceConfig>
        </el-tab-pane>
      </el-tabs>
    </el-col>
  </el-main>
</template>

<script>
import keyList from './configuration/keyList'
import messageServiceConfig from './configuration/messageServiceConfig'
export default {
  components: { keyList, messageServiceConfig },

  data () {
    return {
      activeName: 'keyList',
      nowNum: '',
      onLine: '',
      id: '',
      nodeID: '',
      Loading: false,
      nowName: '',
      nodeName: ''
    }
  },

  async mounted () {
    this.id = this.$route.query.id
    this.nodeID = this.$route.query.nodeID
    this.nowName = this.$route.query.name
    this.nodeName = this.$route.query.nodeName
    await this.renovate()
    this.$refs.keyList.initial(this.id, this.onLine)
  },

  methods: {
    handleClick (tab) {
      this.$refs[tab.name].initial(this.id, this.onLine)
    },
    async renovate () {
      const that = this
      try {
        let findUrl = that.Interface.sysService.info
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, {appid: that.id})
        that.loading = false
        if (response.data.code === 1) {
          that.nowNum = response.data.data.currentVersion + '/' + response.data.data.remoteVersion
          that.onLine = response.data.data.status
        } else {
          that.$message.error('数据获取失败')
        }
      } catch (even) {
        that.loading = false
        that.$message.error('数据获取失败')
      }
    },
    async beforeUpdata (callback) {
      const that = this
      try {
        let findUrl = that.Interface.runNode.isOnline
        that.loading = true
        const response = await that.request.dataGet(that, findUrl, {id: that.nodeID})
        that.loading = false
        if (response.data.code === 1) {
          if (response.data.data) {
            callback()
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
    async installService () {
      const that = this
      try {
        let findUrl = that.Interface.dataCache.install
        that.loading = true
        const response = await that.request.dataPost(that, findUrl, {appid: that.id})
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
    async toUp () {
      const that = this
      try {
        let findUrl = that.Interface.sysService.upgrade
        that.loading = true
        const response = await that.request.dataPut(that, findUrl, {appid: that.id})
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success('远程更新成功')
        } else {
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.loading = false
        that.$message.error('操作失败')
      }
    },
    async toPlay () {
      const that = this
      try {
        let findUrl = that.Interface.dataCache.start
        that.loading = true
        const response = await that.request.dataPut(that, findUrl, {id: that.id})
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
    async toPause () {
      const that = this
      try {
        let findUrl = that.Interface.dataCache.stop
        that.loading = true
        const response = await that.request.dataPut(that, findUrl, {id: that.id})
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
    back () {
      this.$router.push({
        path: 'services'
      })
    }
  }
}
</script>

<style lang="scss">
  .status-box {
    height: 40px;
    line-height: 40px;
  }
  .tit-box {
    max-width: 150px;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }
</style>
