<template>
  <el-col :span="24">
    <el-tabs class="tabs" v-model="activeName" tabPosition="right" style="margin-right: 200px">
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="Linux" name="Linux">
        <el-main>
          <div class="contentDocuments">
            <h1>
              <span class="line"></span>
              Linux系统部署说明
              <span class="line"></span>
            </h1>
            <div class="sketch">本程序适应在Linux上安装（目前支持centos7.0+，其它系统理论也支持，但不适应此文档，需要安装的话，请联系官方人员）， 需要提前安装以下程序：jdk1.8，wget，unzip</div>
            <h2>安装步骤如下：</h2>
            <div class="doc-title">1.复制以下命令，到目标服务器执行</div>
            <div class="url" style="margin-bottom:40px;">wget  {{url}}</div>
            <div class="doc-title">2.执行指令，修改 install.sh 为可执行文件</div>
            <div class="url" style="margin-bottom:40px;">chmod 777 {{hehe}}</div>
            <div class="doc-title">3.下载解压安装包</div>
            <div class="url" style="font-size: 14px;margin-bottom:40px;">./{{hehe}}</div>
            <div class="doc-title">4.基础服务自启动配置</div>
            <div class="url" style="font-size: 14px;margin-bottom:40px;">
              <p>a.添加执行权限</p>
              <p>chmod +x /etc/rc.d/rc.local</p>
              <br>
              <p>b.编辑  rc.local  文件</p>
              <p>vi  /etc/rc.d/rc.local </p>
              <br>
              <p>c.添加 run.sh 所在路径到 rc.local，然后保存退出</p>
              <p>wq</p>
              <p>例：</p>
              <div>
                <img src="./images/start_config.png" style="width: 100%"/>
              </div>
            </div>
            <div class="ps">PS：查看日志，如为报异常，表示安装完成，如有异常输出，请联系官方开发人员解决</div>
          </div>
        </el-main>
      </el-tab-pane>
      <el-tab-pane label="Windows" name="Windows">
        <el-main>
          <div class="contentDocuments">
            <h1>
              <span class="line"></span>
              Windows系统部署说明
              <span class="line"></span>
            </h1>
            <h2>安装步骤如下：</h2>
            <div class="doc-title">1.复制URL到浏览器下载文件</div>
            <div class="url" style="margin-bottom:40px;">{{batUrl}}</div>
            <div class="doc-title">2.解压安装包</div>
            <div class="url" style="margin-bottom:40px;">选择安装路径，将zip放入，然后解压 节点appid-时间戳.zip</div>
            <div class="doc-title">3.执行文件夹中的run.bat文件</div>
            <div class="url" style="margin-bottom:40px;">
              <p>先把服务安装位置的路径复制，然后打开.bat文件，放到25行 如下图所示位置，保存文件；最后鼠标左键双击执行run.bat文件</p>
              <img src="./images/bat_url.png" style="width: 100%"/>
            </div>
            <div class="doc-title">4.基础服务自启动配置</div>
            <div class="url" style="margin-bottom:40px;">
              <p>a.找到隐藏的“启动”文件夹</p>
              <p>C:\Users\Administrator\AppData\Roaming\Microsoft\Windows\StartMenu\Programs\Startup</p>
              <br>
              <p>b.把文件run.bat 复制到“启动”目录下面即可</p>
            </div>
            <div class="ps">PS：查看日志，如为报异常，表示安装完成，如有异常输出，请联系官方开发人员解决</div>
          </div>
        </el-main>
      </el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
      <el-tab-pane label="" name="" disabled></el-tab-pane>
    </el-tabs>`
  </el-col>
</template>

<script>
export default {
  data () {
    return {
      url: '',
      batUrl: '',
      hehe: '',
      activeName: 'Linux'
    }
  },
  mounted () {
    this.url = this.$route.query.url
    this.batUrl = this.$route.query.batUrl
    let hehe = this.url.split('/')
    this.hehe = hehe[hehe.length - 1]
  },
  methods: {
  }
}
</script>

<style lang="scss">
.contentDocuments{
  width: 700px;
  margin: 0 auto 20px;
  text-align: left;
  h1{
    height: 80px;
    line-height: 80px;
    text-align: center;
    font-size: 20px;
    .line {
      display: inline-block;
      width: 100px;
      margin-bottom: 5px;
      border-top: 1px solid #000;
    }
  }
  .sketch{
    padding: 27px 25px;
    margin: .5em auto;
    border-radius: 0;
    overflow: hidden;
    position: relative;
    border: solid 1px #e8d192;
    background-color: rgba(245,240,217,.75);
  }
  h2{
    padding: 20px 0;
    font-size: 16px;
  }
  .doc-title{
    padding: 20px 0;
    font-weight: bold;
    color: #fff;
    text-shadow: 0.1em 0em 0.5em black;
    font-size: 18px;
    font-weight: bold;
  }
  .url{
    border-radius: 2px;
    background: #f7f7f7;
    padding: 0px 5px;
    font-size: 14px;
    span{
      color:red;
    }
  }
  .lookout{
    color:#00A2E8;
    font-size: 14px;
  }
  .ps{
    color: red;
    font-size: 14px;
  }
}
.tabs .el-tabs__item{
  font-size: 18px;
  height: 60px !important;
  line-height: 60px !important;
}
</style>
