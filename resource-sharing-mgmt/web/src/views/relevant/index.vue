<template>
  <div class="relevant-page">
    <Breadcrumb :returnRouter="[{path:'',name:'网站相关'}]"></Breadcrumb>
    <div class="relevant-box">
      <ul class="left-tabs">
        <li :class="activeName==='1'?'active-class':''" @click="activeFun('1')">网站声明</li>
        <li :class="activeName==='2'?'active-class':''" @click="activeFun('2')">使用帮助</li>
        <li :class="activeName==='3'?'active-class':''" @click="activeFun('3')">关于我们</li>
      </ul>
      <div v-if="activeName==='1'" class="text-box">
        <p>请用户使用河北区政务信息资源共享交换平台{{url}}前仔细阅读本服务条款。使用网站提供的任何服务、应用、数据前，须完全接受本协议中所包含的条款、条件和网站即时刊登的通告，并且遵守中华人民共和国关于互联网的相关法律、规定与规则。 一旦发生用户协议的条款变动，我们将及时在网站页面上更新内容，如果用户在协议变动后继续访问或使用网站，则视为已经接受协议的变动。</p>
        <h2>一、权利与义务</h2>
        <h3>(一)用户的权利与义务</h3>
        <p>1．遵守所有与网络服务有关的网络协议、规定和程序。</p>
        <p>2．禁止对网站进行技术性破坏，不得干扰或妨害本网站提供正常的互联网服务。</p>
        <p>3．现阶段，用户有权按照申请流程获取本网站所提供的政务数据资源，在数据使用过程中，用户不得以任何理由将在本网站中获取的各种数据资源转让他人。</p>
        <p>4．用户应在应用本网站政务数据资源所产生的成果中注明政务数据资源来源为"河北区政务信息资源共享交换平台"，及时将应用情况进行备案，并应积极配合有关的用户需求调查和数据资源调查。</p>
        <h3>(二)网站的权利与义务</h3>
        <p>1．本网站包含的所有内容（包括但不限于：文本、图形、图片、视像及声音内容、LOGO标识，版面设计，专栏目录与名称、内容分类）的所有权归河北区委网络安全和信息化委员办公室所有。</p>
        <p>2．在任何情况下，本网站合理地认为用户的行为可能违反法律、法规，本网站可以立即终止向该用户提供服务。</p>
        <h2>二、免责声明</h2>
        <p>本网站所提供数据资源均为政务部门在履行政府职能过程中所采集，数据资源提供方负责对所提供数据的完整性、准确性、及时性。用户因使用本网站所提供数据而造成的任何损失，由用户自行承担。</p>
        <h2>三、隐私保护声明</h2>
        <p>本网站尊重并保护所有网站用户的个人隐私权，未经用户许可或根据相关法律、法规的强制性规定，本网站不会主动地将用户信息泄露给任意第三方。</p>
      </div>
      <div v-if="activeName==='2'" class="text-box">
        <h3>1、什么样的浏览器及界面分辨率可以满足浏览需求？</h3>
        <p>为了获得更好的浏览效果，建议您使用IE11以上，Microsoft Edge、Google Chrome浏览器登录本站点，并将电脑显示屏的分辨率调整为1360*768浏览本网站。</p>
        <h3>2、网站安全及使用常识</h3>
        <p>使用本网站时，为了保证您的资料安全及使用方便，请尽量遵守以下规则：</p>
        <p>（一）尽量做到经常更新您的杀毒软件，定时检查您的电脑是否感染病毒。</p>
        <p>（二）对您在本站注册和使用的所有账号密码请您牢记，并定期修改密码，以保证你的账号密码的安全。</p>
        <p>（三）在您浏览和使用网站中，发现功能或信息内容异常，请和河北区大数据管理中心联系。</p>
        <h3>3、如何联系我们？</h3>
        <p>在浏览本网站时遇到问题，或对本网站建设提出建议，联系电话：022-26262595。</p>
      </div>
      <div v-if="activeName==='3'" class="text-box">
        <p>河北区政务信息资源共享交换平台，于2022年11月10日正式开通，本网站面向于河北区政务部门用户使用，致力于提供天津市、河北区可共享政务数据资源的下载与服务，推动数据跨层级共享，为河北区政务部门开展数据资源的开发利用提供数据支撑。</p>
        <p>网站由河北区委网络安全和信息化委员办公室主办，河北区大数据管理中心承办并负责日常维护和运行管理。</p>
        <p>网站联系</p>
        <p>电子邮箱：hbqdsjglzx@tj.gov.cn</p>
        <p>电话：022-26262595</p>
      </div>
    </div>
  </div>
</template>

<script>
import Breadcrumb from "com/breadcrumb";
import config from '../../config/index.js';
export default {
  components: {
    Breadcrumb,
  },
  data() {
    return {
      activeName: "1",
      url: config.baseURL + "web/index.html#/home",
    };
  },
  // watch: {
  //   $route: {
  //     handler: function (val) {
  //       this.activeName = val.query.tabsNum;
  //     },
  //     // 深度观察监听
  //     deep: true,
  //   },
  // },
  // mounted() {
  //   this.activeName = this.$route.query.tabsNum;
  // },
  methods: {
    activeFun(index) {
      this.activeName = index;
    }
  }
};
</script>

<style lang="scss" scoped>
.relevant-page {
  width: 90%;
  margin: 0 auto;
  .relevant-box {
    margin-left: 50px;
    display: flex;
    .left-tabs {
      width: 230px;
      height: 159px;
      margin-right: 20px;
      // box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      li {
        height: 52px;
        line-height: 52px;
        text-align: center;
        cursor: pointer;
        // background: #fff;
        // border-bottom: 1px solid #eee;
        position: relative;
        background: url("~@/assets/image/ziyuan/背景.png") no-repeat;
        background-size: 100% 100%;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
        margin-bottom: 10px;
      }
      .active-class {
        color: #1aa2ff;
      }
      li.active-class:after {
        position: absolute;
        left: 0;
        top: 13px;
        content: "";
        display: block;
        width: 2px;
        height: 25px;
        background: #1aa2ff;
      }
      li.active-class:before {
        position: absolute;
        right: 15px;
        top: 20px;
        content: "";
        display: block;
        width: 0;
        height: 0;
        border-top: 7px solid transparent;
        border-bottom: 7px solid transparent;
        border-left: 10px solid #1aa2ff;
      }
    }
    .text-box {
      flex: 1;
      padding: 40px;
      background: #fff;
      margin-bottom: 30px;
      line-height: 30px;
      min-height: 500px;
      box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
      p {
        margin: 10px 0;
        text-indent: 2em;
      }
      h2 {
        font-size: 22px;
        text-indent: 2em;
      }
      h3 {
        font-size: 18px;
        text-indent: 2em;
      }
    }
  }
}
</style>
