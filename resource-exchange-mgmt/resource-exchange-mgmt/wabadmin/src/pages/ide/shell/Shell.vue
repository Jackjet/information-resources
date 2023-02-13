<template>
  <el-main style="height: 100%; position: relative;">
    <div class="vular-studio">
      <div v-if="$store.state.isLoading" class="loading-box">
        <img src="../images/loading.gif"/>
      </div>
      <Toolbar @toAdd="backup"/>
      <div class="workspace">
        <LeftArea>
          <template #top>
            <WidgetTabs>
              <tab :name="$t('widgets.micro-app-dir')" :icon="'fas fa-copy'" :selected="true">
                <FolderNode v-model="files"/>
              </tab>
            </WidgetTabs>
          </template>
          <template #bottom>
            <WidgetTabs>
              <tab :name="$t('widgets.doc')" :icon="'fas fa-th-list'" :selected="true">
                <ComponentNode :data="components" v-model="components"/>
              </tab>
            </WidgetTabs>
          </template>
        </LeftArea>
        <CenterArea>
          <template #top>
            <PagesArea>
            </PagesArea>
          </template>

          <template #bottom>
            <WidgetTabs>
              <tab :name="$t('widgets.log-output')" :icon="'fas fa-project-diagram'" :selected="true">
                <LogNode/>
              </tab>
            </WidgetTabs>
          </template>
        </CenterArea>
        <!--<RightArea>-->
        <!--<template #top>-->
        <!--<WidgetTabs>-->
        <!--<tab :name="$t('widgets.log-output')" :icon="'fas fa-project-diagram'" :selected="true">-->

        <!--</tab>-->
        <!--</WidgetTabs>-->
        <!--</template>-->
        <!--</RightArea>-->
      </div>
      <div class="add-plate" v-show="isHide">
        <div class="add-box">
          <h3>添加备份</h3>
          备份名称：<input type="text" v-model="name" placeholder="请输入备份名称">
          <div class="button-box">
            <button @click="close">取消</button>
            <button class="sub" @click="submit">确认</button>
          </div>
        </div>
      </div>
    </div>
  </el-main>
</template>

<script>
  import Toolbar from './components/Toolbar.vue'
  import LeftArea from './components/LeftArea.vue'
  import CenterArea from './components/CenterArea.vue'
  import RightArea from './components/RightArea.vue'
  import WidgetTabs from './components/tabs/WidgetTabs.vue'
  import Tab from './components/tabs/Tab.vue'
  import PagesArea from './components/page/PagesArea.vue'
  import ComponentNode from './components/doc/ComponentNode.vue'
  import LogNode from './components/log/LogNode.vue'
  import Paho from '../../../assets/js/paho-mqtt-min.js'

  export default {
    name: 'Shell',
    components: {
      Toolbar,
      LeftArea,
      CenterArea,
      RightArea,
      WidgetTabs,
      Tab,
      PagesArea,
      ComponentNode,
      LogNode
    },
    data () {
      return {
        isHide: false,
        name: '',
        components: [],
        files: [],
      }
    },
    computed: {},
    methods: {
      async getFileTree () {
        const url = this.Interface.scriptFile.find + '?integrationId=' + this.integrationId + '&appid=' + this.appid

        const response = await this.request.dataGet(this, url)
        let result = response.data
        if (result.code === 1) {
          this.files = result.data
          console.log(this.files)

          this.$store.commit('rootFile', result.data)
        }
      },
      async getComponentsHints () {
        const url = this.Interface.scriptFile.getComponentsHints + '?integrationId=' + this.integrationId + '&appid=' + this.appid

        const response = await this.request.dataGet(this, url)
        let result = response.data
        if (result.code === 1) {
          this.$store.commit('componentsHints', result.data)
        }
      },
      async getComponents () {
        const url = this.Interface.scriptFile.findByAppid + '?integrationId=' + this.integrationId + '&appid=' + this.appid

        const response = await this.request.dataGet(this, url)
        let result = response.data
        if (result.code === 1) {
          this.$store.commit('projectComponentList', result.data)
          $rxbus.$emit('projectComponentList')
        }
      },
      async save (code, path) {
        let url = this.Interface.scriptFile.save
        let data = {
          path: path,
          content: code
        }

        const response = await this.request.dataPost(this, url, data)
        if (response.data.code !== 1) {
          this.$message.error('文件保存失败')
        }
      },

      async getWsInfo () {
        const url = this.Interface.scriptFile.wsInfo + '?appid=' + this.appid
        const response = await this.request.dataGet(this, url)
        let result = response.data
        if (result.code === 1) {
          let MathRandom = (this.S4() + this.S4() + '-' + this.S4() + '-' + this.S4() + '-' + this.S4() + '-' + this.S4() + this.S4() + this.S4())
          this.client = new Paho.MQTT.Client(result.data.mqWsUrl.configValue, 'clientid' + MathRandom)
          // 订阅mqtt
          this.client.connect({
            onSuccess: this.onConnect,
            userName: result.data.appid,
            password: result.data.appkey
          })
          this.client.onConnectedLost = this.onConnectedLost
          this.client.onMessageArrived = this.onMessageArrived
        }
      },
      onMessageArrived (message) {
        let obj = JSON.parse(message.payloadString)
        switch (obj.level) {
          case 'DEBUG':
            obj.color = '#2204FB'
            break
          case 'WARN':
            obj.color = '#FDE602'
            break
          case 'ERROR':
            obj.color = '#FD0100'
            break
          default:
            obj.color = '#D7D7D7'
        }
        $rxbus.$emit('consoleArrPushValue', obj)
      },
      onConnectedLost (responseObject) {
        console.log('onConnectionLost' + responseObject.errorMessage)
      },
      onConnect () {
        // 正式用
        this.client.subscribe(this.integrationId + '/webapi/' + this.appid + '/log')
      },
      S4 () {
        return (((1 + Math.random()) * 0x10000) | 0).toString(32).substring(1)
      },
      click (event) {
        $rxbus.$emit('globalClick', event)
      },
      close () {
        this.isHide = false
        this.name = ''
      },
      backup () {
        this.isHide = true
        this.name = ''
      },
      submit () {
        if (this.name === '') {
          this.$message.warning('名称不能为空')
          return false
        }
        this.add()
      },
      // 添加方法
      async add () {
        const that = this
        let url = that.Interface.backups.insert
        let data = {
          name: this.name,
          appid: this.appid
        }
        // POST请求添加数据
        that.loading = true
        const response = await that.request.dataPost(that, url, data)
        that.loading = false
        if (response.data.code === 1) {
          // 添加成功后 回调效果
          that.$message.success(response.data.msg)
          /**
           * 清空表单元素 重置验证信息.
           * @param imageUrl
           * @param detail
           */
          that.close()
        } else {
          // 添加上传失败后 回调失败信息
          that.$message.error(response.data.msg)
          return false
        }
      }
    },

    mounted () {
      $rxbus.$on('save', this.save)

      document.onkeydown = function (e) {
        let key = window.event.keyCode
        if (key === 83 && event.ctrlKey) { // == 83 && event.ctrlKey
          window.event.preventDefault() // 关闭浏览器快捷键
        }
      }

      this.globalClick(this.click)

      this.$store.commit('projectChange', null)

      this.$store.commit('isLoading', true)
      this.getFileTree()
      this.getComponentsHints()
      this.getComponents()
      this.getWsInfo()
      this.$store.commit('isLoading', false)
    }
  }
</script>

<style>
  .loading-box {
    position: fixed;
    z-index: 99999;
    top: 0;
    height: 0;
    width: 100%;
    height: 100%;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .add-plate {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    z-index: 999;
    background-color: rgba(255, 255, 255, 0);
  }
  .add-box {
    width: 400px;
    height: auto;
    color: #555;
    background-color: #fff;
    position: absolute;
    left: 50%;
    top: 10%;
    transform: translateX(-200px); 
    border-radius: 5px;
    padding: 10px;
  }
  .add-box h3 {
    width: 100%;
    text-align: center;
    font-size: 20px;
    color: #333;
  }
  .add-box input {
    -webkit-appearance: none;
    background-color: #FFFFFF;
    background-image: none;
    border-radius: 4px;
    border: 1px solid #DCDFE6;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    color: #606266;
    display: inline-block;
    font-size: inherit;
    height: 40px;
    line-height: 40px;
    outline: none;
    padding: 0 15px;
    -webkit-transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    transition: border-color 0.2s cubic-bezier(0.645, 0.045, 0.355, 1);
    width: 100%;
  }
  .button-box {
    text-align: right;
    padding: 10px 10px;
  }
  .button-box button{
    display: inline-block;
    line-height: 1;
    white-space: nowrap;
    cursor: pointer;
    background: #FFFFFF;
    border: 1px solid #DCDFE6;
    border-color: #DCDFE6;
    color: #606266;
    -webkit-appearance: none;
    text-align: center;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    outline: none;
    margin: 0;
    -webkit-transition: .1s;
    transition: .1s;
    font-weight: 500;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
    padding: 12px 20px;
    font-size: 14px;
    border-radius: 4px;
  }
  .button-box .sub {
    color: #FFFFFF;
    background-color: #5a6e8a;
    border-color: #5a6e8a;
  }
  .button-box .sub:hover {
    background: #7b8ba1;
    border-color: #7b8ba1;
    color: #FFFFFF;
  }
</style>
