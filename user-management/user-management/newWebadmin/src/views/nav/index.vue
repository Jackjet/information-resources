<!--
 * 默认主页
-->
<template>
  <main class="main-box">
    <div class="main-img">
      <div class="leftBox">
        <img class="line" src="../../assets/image/home/line.png" alt />
        <img v-for="(v,i) in iconData" :key="v.id" :class="v.class" :src="v.src" @click="handleLink(v)" alt />
        <div class="break">
          <img class="left" src="../../assets/image/home/forleft.png" alt />
          <img class="right" src="../../assets/image/home/forright.png" alt />
        </div>
        <div class="break1">
          <img class="left" src="../../assets/image/home/forleft.png" alt />
          <img class="right" src="../../assets/image/home/forright.png" alt />
        </div>
        <div class="break2">
          <img class="left" src="../../assets/image/home/forleft.png" alt />
          <img class="right" src="../../assets/image/home/forright.png" alt />
        </div>
      </div>
      <div class="rightBox">
        <div class="box1">
          集成中枢
        </div>
        <div class="box2">
          <span>联通数据孤岛</span>
          <span>一站式数据开发</span>
          <span>全局资源管理</span>
        </div>
        <div class="box3">
          松耦合敏捷集成构建API生态
        </div>
      </div>
    </div>
  </main>
</template>

<script>
import { getVerifyState } from "@/api/user";

/*
  # title 名字 用来区分 用户系统管理 和其他系统
  # class 类名 用作定位
  # src 图片的路径
  # status 判断是否可用
  # href 链接地址
*/
export default {
  name: "Nav",
  data() {
    return {
      iconData: [
        {
          id: "1",
          title: "API编排服务",
          class: "apiArrange",
          src: "apiArrange.png",
          status: 1,
          href: process.env.VUE_APP_BASE_API_ARRANGE_URL
        },
        {
          id: "2",
          title: "API集成服务",
          class: "apiIntegration",
          src: "apiIntegration.png",
          status: 1,
          href: process.env.VUE_APP_BASE_API_INTEGRATION_URL
        },
        {
          id: "3",
          title: "资源管理",
          class: "resource",
          src: "resource.png",
          status: 1,
          href: process.env.VUE_APP_BASE_RESOURCE_URL
        },
        {
          id: "4",
          title: "消息集成服务",
          class: "infoIntegration",
          src: "infoIntegration.png",
          status: 0,
          href: "/homePage"
        },
        {
          id: "5",
          title: "数据集成服务",
          class: "dataIntegration",
          src: "dataIntegration.png",
          status: 0,
          href: process.env.VUE_APP_BASE_DATA_INTEGRATION_URL
        },
        {
          id: '6',
          title: '用户系统管理',
          class: 'userSystem',
          src: 'userSystem.png',
          status: 1,
          href: '/homePage'
        }
      ]
    };
  },
  mounted() {
    document.title = '集成中枢系统'
    this.init();
  },
  destroyed() {
    document.title = '用户管理系统'
  },
  methods: {
    async init() {
      // let datas= await getVerifyState()
      // if(datas.data.code == 1){
      //   this.iconData = datas.data.data
      // }
      this.iconData.forEach((v, i) => {
        v.class = v.status
          ? "icon " + v.class
          : "icon " + v.class + " disabled";
        v.src = require("../../assets/image/home/" + v.src);
      });
    },
    handleLink(data) {
      if (data.status) {
        if (data.title === "用户系统管理") {
          sessionStorage.setItem("navInfo", "userSystem");
          this.$router.push(data.href);
        } else {
          let token = JSON.parse(sessionStorage.getItem('UserInfo'))
          window.location.href = data.href + '?UserInfo=1&id=' + token.id + '&name=' + token.name + '&roles=' + token.roles.join(',') + '&token=' + token.token
          // this.$router.push(data.href);
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
.main-box {
  display: flex;
  position: relative;
  flex-direction: column;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100%;
  width: 100%;
  margin: 0 auto;
  // min-height: 600px;
  border-radius: 5px;
  box-sizing: border-box;
  .main-center {
    width: 65%;
    margin: 0 auto;
  }
  .main-img {
    // position: absolute;;
    background: url("../../assets/image/home/background.png");
    background-size: 100% 100%;
    // padding: 15%;
    box-sizing: border-box;
    height: 100%;
    width: 100%;
    // min-height: 600px;
    position: relative;
    display: flex;
    .leftBox {
      width: 58%;
      height: 100%;
    }
    .rightBox {
      width: 42%;
      height: 100%;
      padding: 0 50px;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      .box1 {
        border-bottom: 3px solid white;
        font-size: 35px;
        font-weight: 900;
        width: 100%;
        color: white;
        padding: 20px 0;
        margin: 20px 0;
      }
      .box2 {
        width: 100%;
        display: flex;
        justify-content: space-between;
        color: white;
        font-size: 20px;
        margin-bottom: 20px;
      }
      .box3 {
        width: 100%;
        color: white;
        font-size: 20px;
      }
    }
    .line {
      width: 100%;
      height: 563px;
      //  min-height: 600px;
    }
    .icon:hover {
      cursor: pointer;
      transform: scale(1.2);
    }
    .disabled {
      -webkit-filter: grayscale(100%);
      -moz-filter: grayscale(100%);
      -ms-filter: grayscale(100%);
      -o-filter: grayscale(100%);
      filter: grayscale(100%);
      filter: gray;
      filter: progid:DXImageTransform.Microsoft.BasicImage(grayscale=1);
    }
    .disabled:hover {
      transform: none;
    }
    .apiArrange {
      position: absolute;
      left: 12%;
      top: 11%;
      width: 110px;
      height: 192.5px;
    }
    .apiIntegration {
      position: absolute;
      left: 21.8%;
      top: 26.5%;
      width: 110px;
      height: 165px;
    }
    .resource {
      position: absolute;
      left: 37%;
      top: 18%;
      width: 150px;
      height: 162px;
    }
    .infoIntegration {
      position: absolute;
      left: 32%;
      top: 36%;
      width: 110px;
      height: 162.8px;
    }
    .dataIntegration {
      position: absolute;
      left: 42%;
      top: 47%;
      width: 110px;
      height: 161.7px;
    }
    .userSystem {
      position: absolute;
      left: 13%;
      top: 42%;
      width: 110px;
      height: 184px;
    }
    .break img,
    .break2 img,
    .break1 img {
      width: 30px;
      height: 26.25px;
    }
    .break {
      width: 75px;
      height: 50px;
      position: absolute;
      top: 35%;
      left: 18%;
    }
    .break1 {
      width: 75px;
      height: 50px;
      position: absolute;
      top: 46%;
      left: 28%;
    }
    .break2 {
      width: 75px;
      height: 50px;
      position: absolute;
      top: 56%;
      left: 38%;
    }
    .left {
      position: absolute;
      -webkit-animation: right-to-left 2s linear infinite; /* Chrome, Safari, Opera */
      animation: right-to-left 2s linear infinite;
    }
    @-webkit-keyframes right-to-left {
      0% {
        left: 0%;
        top: 0%;
      }
      50% {
        left: 11%;
        top: 6%;
      }
      100% {
        left: 0%;
        top: 0%;
      }
    }
    .right {
      position: absolute;
      -webkit-animation: right-to-right 2s linear infinite; /* Chrome, Safari, Opera */
      animation: right-to-right 2s linear infinite;
    }
    @-webkit-keyframes right-to-right {
      0% {
        right: 0%;
        bottom: 0%;
      }
      50% {
        right: 11%;
        bottom: 6%;
      }
      100% {
        right: 0%;
        bottom: 0%;
      }
    }
  }
  // img{
  //   display: inline-block;
  //   max-width: 100%;
  //   max-height: 100%;
  //   width: 100%;
  //   height: 100%;
  // }
}
</style>
