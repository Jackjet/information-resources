<template>
  <div class="home-page">
    <div class="swiper-content">
      <div class="full-text-search">
        <span></span>
        <el-autocomplete v-show="searchFang" style="width:100%;" class="inline-input" v-model="fullSearch" :fetch-suggestions="querySearch" placeholder="请输入内容" :trigger-on-focus="false" @select="querySelect">
          <!-- <el-button slot="append" icon="el-icon-search"></el-button> -->
        </el-autocomplete>
        <i class="el-icon-search" @click="searchBtn"></i>
      </div>
      <div class="swiper-container">
        <div class="swiper-wrapper">
          <div class="swiper-slide" data-swiper-autoplay="2000">
            <img src="@/assets/image/banner2/indexbanner1.png" alt="" />
          </div>
          <!-- <div class="swiper-slide" data-swiper-autoplay="2000">
            <img src="@/assets/image/banner2/indexbanner2.png" alt="" />
          </div> -->
          <div class="swiper-slide" data-swiper-autoplay="2000">
            <img src="@/assets/image/banner2/indexbanner3.png" alt="" />
          </div>
        </div>
        <div class="swiper-pagination"></div>
      </div>
    </div>

    <div class="data-focus">
      <h2>数据汇聚</h2>
      <div class="data-box">
        <ul>
          <li>
            <span class="data-name"><img src="@/assets/image/svg_icon/入驻部门.svg" alt=""> 入驻部门</span>
            <span class="data-num">{{ gathering.settleIn }}<i>个</i></span>
          </li>
          <li>
            <span class="data-name"><img src="@/assets/image/svg_icon/资源目录.svg" alt=""> 资源目录</span>
            <span class="data-num">{{ gathering.catalogue }}<i>个</i></span>
          </li>
          <li>
            <span class="data-name"><img src="@/assets/image/svg_icon/共享需求.svg" alt=""> 部门需求</span>
            <span class="data-num">{{ gathering.share }}<i>个</i></span>
          </li>
          <li>
            <span class="data-name"><img src="@/assets/image/svg_icon/资源需求.svg" alt=""> 资源申请</span>
            <span class="data-num">{{ gathering.resources }}<i>个</i></span>
          </li>
        </ul>
      </div>
    </div>

    <div class="resources-content">
      <h2 class="resources-content-title">政务信息资源分类</h2>
      <el-menu :default-active="activeIndex" class="el-menu-demo" active-text-color="#409EFF" mode="horizontal" @select="changeSelect">
        <el-menu-item index="2852">部门信息资源</el-menu-item>
        <el-menu-item index="3">主题信息资源</el-menu-item>
        <el-menu-item index="2">基础信息资源</el-menu-item>
      </el-menu>
      <div class="resources-list">
        <ul v-if="this.activeIndex === '3'||this.activeIndex === '2'">
          <li v-for="(item, index) in leftNavList" :key="index" style="cursor:pointer" @click="goDetail(item.typId)">
            <span class="icon-img">
              <img :src="imgurl + item.imgUrl" alt="" />
            </span>
            <p>{{ item.typNm }}</p>
          </li>
        </ul>
        <ul v-else>
          <li v-for="(item, index) in bumenIcon" :key="index" style="cursor:pointer" @click="goDetail(item.id)">
            <span class="icon-img">
              <img :src="imgurl + item.url" alt="" />
            </span>
            <p class="overflowEllips">{{ item.name }}</p>
          </li>
          <li style="cursor:pointer" @click="rouetrClick">
            <span class="icon-img">
              <img :src="imgurl + 'svg_15.svg'" alt="" />
            </span>
            <p class="overflowEllips">更多资源</p>
          </li>
        </ul>
        <!-- <ol v-else>
          <li v-for="(item, index) in leftNavList.slice(0,14)" :key="index" style="cursor:pointer" @click="goDetail(item.typId)">
            <el-tooltip class="item" effect="light" :open-delay="1000" :content="item.typNm" placement="top">
              <p class="overflowEllips">{{ item.typNm }}</p>
            </el-tooltip>
          </li>
          <li v-if="leftNavList.length>12" style="cursor:pointer">
            <p class="overflowEllips" @click="rouetrClick">更多资源<i class="el-icon-right el-icon--right"></i></p>
          </li>
        </ol> -->
      </div>
    </div>

    <div class="newest-data">
      <h2>最新上线</h2>
      <el-menu :default-active="activeIndex1" class="el-menu-newest" active-text-color="#409EFF" mode="horizontal" @select="handleSelect">
        <el-menu-item index="1">最新目录</el-menu-item>
        <el-menu-item index="2">最新资源</el-menu-item>
        <el-menu-item index="3">最热目录</el-menu-item>
      </el-menu>
      <div class="newest-content">
        <ul v-if="resourcesList.length">
          <li v-for="(item, index) in resourcesList" :key="index" @click="detailsClick(item.id)">
            <span class="newest-icon">
              <img src="@/assets/image/icon/信息资源管理.svg" alt="" />
            </span>
            <el-tooltip class="item" effect="light" :open-delay="1000" :content="item.uviewNm" placement="top">
              <span class="newest-name overflowEllips"> {{item.uviewNm}} </span>
            </el-tooltip>
            <!-- <span class="newest-name">{{ item.uviewNm }}</span> -->
            <span class="newest-time">{{ item.pubDt.split(' ')[0] }}</span>
            <div class="newest-condition">
              <i class="condition_1">{{
                  item.shareLv === "01"
                      ? "无条件共享"
                      : item.shareLv === "02"
                      ? "有条件共享"
                      : "不予共享"
                }}</i>
              <i class="condition_2" v-if="item.typNm">{{ item.typNm }}</i>
            </div>
            <!-- <span class="newest-department">{{ item.name }}</span> -->
          </li>
        </ul>
      </div>
      <div class="router-button">
        <el-button type="primary" size="medium" @click="rouetrClick">更多资源<i class="el-icon-right el-icon--right"></i>
        </el-button>
      </div>
    </div>
  </div>
</template>
<script>
import Swiper from "swiper";
import config from '../../config/index.js';

export default {
  name: "HomePage",
  data() {
    return {
      searchFang: false,
      imgurl: config.baseURL + '/web/img/',
      bumenIcon: [
        {
          url: "svg_1.svg",
          name: "市场监管局",
          id: "2876"
        },
        {
          url: "svg_2.svg",
          name: "卫健委",
          id: "2856"
        },
        {
          url: "svg_3.svg",
          name: "教育局",
          id: "2862"
        },
        {
          url: "svg_4.svg",
          name: "民政局",
          id: "2865"
        },
        {
          url: "svg_5.svg",
          name: "科技局",
          id: "2867"
        },
        {
          url: "svg_6.svg",
          name: "住建委",
          id: "2855"
        },
        {
          url: "svg_7.svg",
          name: "城管委",
          id: "2854"
        },
        {
          url: "svg_8.svg",
          name: "司法局",
          id: "2869"
        },
        {
          url: "svg_9.svg",
          name: "统计局",
          id: "2873"
        },
        {
          url: "svg_10.svg",
          name: "审计局",
          id: "2864"
        },
        {
          url: "svg_11.svg",
          name: "文旅局",
          id: "2870"
        },
        {
          url: "svg_12.svg",
          name: "体育局",
          id: "2872"
        },
        {
          url: "svg_13.svg",
          name: "规自局",
          id: "2878"
        },
        {
          url: "svg_14.svg",
          name: "人防办",
          id: "2859"
        },
      ],
      restaurants: [],
      fullSearch: "",
      activeIndex: "2852",
      activeIndex1: "1",
      leftNavList: [],
      resourcesList: [],
      sharedResource: [],
      gathering: {
        settleIn: 0,
        catalogue: 0,
        share: 0,
        resources: 0,
      },
    };
  },
  mounted() {
    new Swiper(".swiper-container", {
      autoplay: 2000,
      observer: true,
      loop: true,
      autoplayDisableOnInteraction: false,
      pagination: ".swiper-pagination",
    });
    this.classFun(this.activeIndex);
    this.latelyGoOnline(null);
    this.dataGathering();
  },
  methods: {
    searchBtn() {
      this.searchFang = !this.searchFang;
    },
    querySearch(queryString, callback) {
      let data = {
        uviewNm: this.fullSearch,
        uviewNo: "",
        hookStatus: "",
        provOrgId: "",
        typId: "",
        page: 1,
        size: 10,
      }
      this.archBusiUviewExFindAll(data).then((res) => {
        if (res.code === 1) {
          for (let i of res.data.content) {
            i.value = i.uviewNm;
            i["value-key"] = i.uviewNm;
          }
          this.restaurants = res.data.content;
          callback(this.restaurants);
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    querySelect(item) {
      this.$router.push({
        path: "/catalogue/details",
        query: {
          id: item.id,
        },
      });
    },
    changeSelect(key) {
      this.activeIndex = "" + key;
      this.classFun(key);
    },
    handleSelect(key) {
      if (key === "1") {
        this.latelyGoOnline(null);
      } else if (key === "2") {
        this.latelyGoOnline(1, 'hook_time');
      } else {
        this.latelyGoOnline(1, 'visits_count');
      }
    },
    // 分类
    classFun(num) {
      this.dictAssetCateFindAllByParTypId({ parTypId: num }).then((res) => {
        if (res.code === 1) {
          this.leftNavList = res.data;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    // 数据汇聚
    dataGathering() {
      this.dataAggregationFind().then((res) => {
        if (res.code === 1) {
          this.gathering.settleIn = res.data.org;
          this.gathering.catalogue = res.data.uview;
          this.gathering.share = res.data.demanded;
          this.gathering.resources = res.data.resourceUse;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    //最新上线
    latelyGoOnline(type, order) {
      this.archBusiUviewExFindAll({ isHook: type, page: 1, size: 5, order: order }).then(
        (res) => {
          if (res.code === 1) {
            this.resourcesList = res.data.content;
          } else {
            this.$message.error(res.msg);
          }
        }
      );
    },
    detailsClick(row) {
      this.$router.push({
        path: "/catalogue/details",
        query: {
          id: row,
        },
      });
    },
    rouetrClick() {
      this.$router.push("/catalogue");
    },
    goDetail(typId) {
      this.$router.push({
        path: "/catalogue",
        query: {
          typId: typId,
          activeIndex: this.activeIndex
        }
      });
    }
  },
};
</script>

<style lang="scss" scoped>
.swiper-content {
  width: 100%;
  // position: relative;
  border-radius: 100% / 0 0 40% 40%;
  overflow: hidden;
  .full-text-search {
    position: absolute;
    width: 220px;
    height: 30px;
    right: 210px;
    top: 15px;
    z-index: 101;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .el-icon-search {
      cursor: pointer;
      color: #fff;
      margin-left: 10px;
    }
  }
  .swiper-slide {
    width: 100%;
    height: 350px;
    img {
      display: block;
      width: 100%;
      height: 100%;
    }
  }
}

.resources-content {
  margin: 0 auto;
  margin-top: 100px;
  .resources-content-title {
    text-align: center;
    font-weight: bold;
    color: #000;
    margin-top: 20px;
    font-size: 20px;
  }
  .el-menu-demo {
    margin: 0 auto;
    display: flex;
    justify-content: center;
  }
}

.resources-list {
  padding: 32px 200px;
  ul {
    display: flex;
    flex-wrap: wrap;
    li:hover {
      box-shadow: 8px 8px 20px 0px rgba(55, 99, 170, 0.1);
      border-radius: 4px;
    }
    li {
      width: 20%;
      height: 150px;
      // margin: 10px;
      box-sizing: border-box;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      span {
        display: block;
        width: 48px;
        height: 48px;
        margin: 10px auto;
        img {
          display: block;
          width: 100%;
          height: 100%;
        }
      }
      p {
        text-align: center;
        font-size: 14px;
        color: #333333;
      }
    }
  }

  ol {
    display: flex;
    flex-wrap: wrap;
    li {
      width: 18%;
      padding: 10px;
      text-align: center;
      background: cornflowerblue;
      color: #fff;
      margin: 10px;
      border-radius: 5px;
      box-sizing: border-box;
      color: #fff;
    }
  }
}

.data-focus {
  width: 70%;
  background: #fff;
  position: absolute;
  top: 270px;
  z-index: 20;
  left: 15%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  border-radius: 5px;
  h2 {
    font-size: 20px;
    line-height: 60px;
    text-align: center;
    font-weight: bold;
  }
  ul {
    display: flex;
    justify-content: space-around;
    padding-bottom: 20px;
    span {
      display: block;
      text-align: center;
    }
    .data-name {
      font-size: $font-size-big;
      line-height: 30px;
      color: #3d485d;
    }
    .data-num {
      font-size: 26px;
      color: #0080ff;
      font-weight: bold;
      line-height: 30px;
      i {
        color: #3d485d;
        font-size: 14px;
      }
    }
  }
}

.newest-data {
  margin-top: -32px;
  background: url("~@/assets/image/svg_icon/back.png") no-repeat;
  background-size: 100% 100%;
  h2 {
    font-size: 20px;
    line-height: 60px;
    text-align: center;
    font-weight: bold;
    background: #fff;
  }
  .el-menu-newest {
    display: flex;
    justify-content: center;
  }
  .newest-content {
    width: 80%;
    margin: 0 auto;
    ul {
      display: flex;
      margin: 20px 0;
      li {
        width: 18%;
        height: 220px;
        margin: 10px;
        cursor: pointer;
        padding-top: 10px;
        box-sizing: border-box;
        border: 2px solid #ffffff;
        box-shadow: -8px -8px 20px 0px rgba(255, 255, 255, 0.1);
        box-shadow: 8px 8px 20px 0px rgba(55, 99, 170, 0.1);
        border-radius: 10px;
        span {
          display: block;
          text-align: center;
          line-height: 30px;
        }
        .newest-name {
          padding: 0 20px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          box-sizing: border-box;
          color: #333333;
          font-size: 16px;
          font-weight: 500;
        }
        .newest-icon {
          width: 60px;
          height: 60px;
          margin: 10px auto;
          img {
            display: block;
            width: 100%;
            height: 100%;
          }
        }
        .newest-time {
          color: #999999;
          font-size: 12px;
          font-weight: 400;
        }
        .newest-condition {
          display: flex;
          justify-content: space-around;
          font-size: $font-size-big;
          color: #fff;
          background: #3d3e49;
          border-radius: 12px 0 12px 0;
          width: 80%;
          margin: 0 auto;
          padding: 3px 0;
          // .condition_1 {
          //   color: #568bd5;
          // }
          // .condition_2 {
          //   color: #2ce496;
          // }
        }
        .newest-department {
          color: #f59a23;
        }
      }
    }
  }
  .router-button {
    height: 100px;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}

.swiper-container-horizontal > .swiper-pagination-bullets,
.swiper-pagination-custom,
.swiper-pagination-fraction {
  bottom: 100px;
}
.el-menu-item {
  font-size: 16px;
  color: #3d485d;
}

.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.resources-content .el-menu--horizontal > .el-menu-item.is-active {
  color: #0080ff;
  border-bottom-color: #0080ff;
}
.newest-data .el-menu--horizontal > .el-menu-item.is-active {
  color: #0080ff;
  border-bottom-color: #0080ff;
}
</style>

<style>
.full-text-search .el-input__inner {
  background: none;
  height: 30px;
  line-height: 30px;
  color: #fff;
}
.full-text-search .el-input-group__append {
  background: none;
  color: #fff;
}
</style>

