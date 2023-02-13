<template>
  <div class="catalogue-page">
    <Breadcrumb :returnRouter="[{path:'',name:'资源目录'}]"></Breadcrumb>
    <div class="catalogue-box">
      <div class="catalogue-left">
        <!-- <h5>资源分类</h5> -->
        <div class="catalogue-nav">
          <el-menu :default-active="active" unique-opened class="el-menu-vertical-demo" @select="selectChange" text-color="#000" active-text-color="#ffd04b">
            <el-submenu v-for="item in leftNavList" :key="item.typId" :index="item.typCd" class="menu-back">

              <template slot="title">
                <img v-if="item.typNm==='基础类'" class="nav-icon" src="@/assets/image/ziyuan/基础类.svg" alt="" />
                <img v-if="item.typNm==='主题类'" class="nav-icon" src="@/assets/image/ziyuan/主题类.svg" alt="" />
                <img v-if="item.typNm==='部门类'" class="nav-icon" src="@/assets/image/ziyuan/部门类.svg" alt="" />
                <span>{{ item.typNm }}</span>
              </template>

              <el-menu-item-group v-if="item.displaySn!==3" class="leftNavScroll">
                <el-menu-item v-for="items in item.children" :key="items.typId" :index="items.typId">
                  {{ items.typNm }}
                </el-menu-item>
              </el-menu-item-group>

              <el-submenu v-else v-for="items in item.children" :key="items.typId" :index="items.typId" class="leftNavScroll">
                <template slot="title">{{ items.typNm }}</template>
                <el-menu-item v-for="itemss in items.children" :key="itemss.typId" :index="itemss.typId">
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="itemss.typNm" placement="right">
                    <span class="overflowEllips"> {{itemss.remark?itemss.remark:itemss.typNm}} </span>
                  </el-tooltip>
                </el-menu-item>
              </el-submenu>

            </el-submenu>
          </el-menu>
        </div>
      </div>
      <div class="catalogue-right">
        <el-row class="query-header" :gutter="20">
          <el-col :span="4">
            <el-input size="medium" placeholder="信息资源代码" v-model="query.uviewNo" clearable>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-input size="medium" placeholder="信息资源名称" v-model="query.uviewNm" clearable>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-select size="medium" v-model="query.isHook" clearable placeholder="挂接状态">
              <el-option label="已挂接" value="1"> </el-option>
              <el-option label="未挂接" value="0"> </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-select size="medium" v-model="query.hookType" clearable placeholder="挂接类型" @change="hookTypeChange">
              <el-option label="接口" value="1"> </el-option>
              <el-option label="文件" value="2"> </el-option>
              <el-option label="数据库" value="3"> </el-option>
            </el-select>
          </el-col>
          <el-col :span="6" style="padding:0px;">
            <div class="button-query">
              <el-button type="primary" size="medium" @click="queryClick">查 询</el-button>
              <el-button size="medium" @click="resetForm">重置</el-button>
            </div>
          </el-col>
        </el-row>
        <div class="catalogue-list">
          <ul class="list-box">
            <li v-if="resourcesList.length===0" style="display: block;font-size:24px;text-align: center;line-height: 100px;border:none;">
              暂无数据
            </li>
            <li class="list_item" v-for="item in resourcesList" :key="item.id">
              <div class="list_item_1">
                <img class="list_item_1_icon" src="@/assets/image/icon/信息资源管理.svg" alt="" />
              </div>
              <div class="list_item_2">
                <div class="list-item-title">
                  <h2 class="title_name">{{ item.uviewNm }}</h2>
                  <div class="list_item_4">
                    <span style="cursor: pointer;" @click="routerDetails(item.id)"><i class="el-icon-right"></i> 进入</span>
                    <span v-if="loginToken" style="cursor: pointer;" @click="followClick(item.uviewId, item.isFocus)"><img :src="
                      !item.isFocus
                        ? require('@/assets/image/icon/关注.png')
                        : require('@/assets/image/icon/关注1.png')
                    " alt="" />
                      关注</span>
                    <span>
                      <img src="@/assets/image/icon/查看 (2).png" alt="" />
                      访问：{{ item.visitsCount }}
                    </span>
                  </div>
                </div>
                <p>资源代码: {{ item.uviewNo }}</p>
                <p>资源描述: {{ item.uviewDesc }}</p>
                <p style="color:#AAAAAA">
                  挂接状态:
                  <span>{{ item.hookStatus==='1'||item.dataHookStatus==='1'||item.fileHookStatus==='1'?'已挂接':'未挂接' }}</span>
                  | 挂接类型:
                  <span v-if="item.hookStatus==='1'">云接口</span>
                  <span v-else-if="item.dataHookStatus==='1'">云数据</span>
                  <span v-else-if="item.fileHookStatus==='1'">云文件</span>
                  <span v-else>无</span>
                  | 共享条件:
                  <span>{{ item.shareCondition?item.shareCondition:"无" }}</span>
                  | 更新周期:
                  <span>{{ formatterUpdateCyc(item.updateCyc) }}</span>
                </p>
              </div>
            </li>
          </ul>
          <Pagination ref="page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></Pagination>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import Breadcrumb from "com/breadcrumb";
import Pagination from "com/pagination";
import { getToken } from '@/utils/storage.js';
export default {
  components: {
    Breadcrumb,
    Pagination,
  },
  data() {
    return {
      loginToken: getToken(),
      leftNavList: [],
      active: '',
      organizationOptions: [],
      query: {
        uviewNm: "",
        uviewNo: "",
        isHook: "",//挂接状态
        hookType: "",//挂接类型
        provOrgId: "",
        typId: "",
        page: 1,
        size: 5,
      },
      resourcesList: [],
      total: 0,
    };
  },
  mounted() {
    if (this.$route.query.typId) {
      this.active = this.$route.query.typId
    } else {
      this.active = ''
    }
    if (this.$route.query.typId) {
      this.query.typId = this.$route.query.typId
    }
    this.resourcesListFun(this.query);
    this.dictAssetCateFindAll().then((res) => {
      if (res.code === 1) {
        this.leftNavList = res.data;
      } else {
        this.$message.error(res.msg);
      }
    });

    // this.organizationFindAll().then((res) => {
    //   if (res.code === 1) {
    //     let arrData = []
    //     arrData.push(res.data);
    //     this.organizationOptions = this.getJsonTree(arrData, '');
    //   } else {
    //     this.$message.error(res.msg);
    //   }
    // });
  },
  methods: {
    getJsonTree(data, parentId) {
      let itemArr = []
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId) {
          let newNode = {};
          newNode.value = node.name;
          newNode.label = node.name;
          if (node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode);
        }
      }
      return itemArr;
    },
    hookTypeChange() {
      if (!this.query.isHook) {
        this.$message("请先选择挂接状态");
        this.query.hookType = "";
      }
    },
    queryClick() {
      if (typeof (this.query.provOrgId) != 'string') {
        this.query.provOrgId = this.query.provOrgId[this.query.provOrgId.length - 1]
      }
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.resourcesListFun(this.query);
    },
    resetForm() {
      this.query = {
        uviewNm: "",
        uviewNo: "",
        isHook: "",//挂接状态
        hookType: "",//挂接类型
        provOrgId: "",
        typId: "",
        page: 1,
        size: 5,
      }
      this.resourcesListFun(this.query);
    },
    resourcesListFun(obj) {
      this.archBusiUviewExFindAll(obj).then((res) => {
        if (res.code === 1) {
          this.resourcesList = res.data.content;

          this.total = res.data.totalElements;
        } else {
          this.$message.error(res.msg);
        }
      });
    },
    formatterUpdateCyc(cellValue) {
      if (cellValue == '01') {
        return '实时'
      } else if (cellValue == '02') {
        return '每日'
      } else if (cellValue == '03') {
        return '每周'
      } else if (cellValue == '04') {
        return '每月'
      } else if (cellValue == '05') {
        return '每季度'
      } else if (cellValue == '06') {
        return '每半年'
      } else if (cellValue == '07') {
        return '每年'
      }
    },
    sizeChange(val) {
      this.query.size = val;
      this.resourcesListFun(this.query);
    },
    currentChange(val) {
      this.query.page = val;
      this.resourcesListFun(this.query);
    },
    selectChange(key) {
      this.query.typId = key;
      this.query.page = 1;
      this.$refs.page.Page(1);
      this.resourcesListFun(this.query);
    },
    routerDetails(id) {
      this.$router.push({
        path: "/catalogue/details",
        query: { id: id },
      });
    },
    followClick(id, isFocus) {
      if (!isFocus) {
        this.myFocusInfoInsert({ uviewId: id }).then((res) => {
          if (res.code === 1) {
            this.$message.success("关注成功");
            this.resourcesListFun(this.query);
          } else {
            this.$message.error(res.msg);
          }
        });
      } else {
        this.myFocusInfoDelete(id).then((res) => {
          if (res.code === 1) {
            this.$message.success("取消成功！");
            this.resourcesListFun(this.query);
          } else {
            this.$message.error(res.msg);
          }
        });
      }
    },
  },
};
</script>
<style lang="scss" scope>
.catalogue-page {
  width: 90%;
  margin: 0 auto;
}
.catalogue-box {
  display: flex;
  padding: 0 50px;
  box-sizing: border-box;
  .catalogue-left {
    width: 230px;
    box-sizing: border-box;
    h5 {
      background: #0ea6fa;
      padding: 15px 30px;
      color: $font-color-white;
      box-sizing: border-box;
    }
    .catalogue-nav {
      .nav-icon {
        width: 20px;
        margin-right: 10px;
      }
    }
    .leftNavScroll {
      max-height: 500px;
      overflow-y: scroll;
      margin-right: 5px;
    }
    .leftNavScroll::-webkit-scrollbar {
      width: 4px;
      background: none;
    }
    .leftNavScroll::-webkit-scrollbar-thumb {
      border-radius: 2px;
      box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.2);
      background: #535353;
    }
    .leftNavScroll::-webkit-scrollbar-track {
      // box-shadow: inset 0 0 2px rgba(0, 0, 0, 0.2);
      // border-radius: 2px;
      border: none;
      background: none;
    }
  }
  .catalogue-right {
    flex: 1;
    margin-left: 20px;
    padding: 0 20px;
    margin-bottom: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
    .query-header {
      margin: 20px 0;
    }
    .catalogue-list {
      .list-box {
        min-height: 500px;
      }
      .list-box li {
        display: flex;
        // justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #ccc;
        padding: 10px 0;
        .list_item_1 {
          width: 120px;
          display: flex;
          flex-direction: column;
          align-items: center;
          .list_item_1_icon {
            width: 64px;
          }
          p {
            font-size: $font-size-small;
            color: $font-color-1;
          }
          span {
            width: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #c3e182;
            font-size: $font-size-small;
            color: #fff;
            cursor: pointer;
            margin-top: 5px;
            img {
              width: 15px;
            }
          }
        }

        .list_item_2 {
          flex: 1;
          color: #666666;
          font-size: $font-size-small;
          line-height: 24px;
          .list-item-title {
            display: flex;
            justify-content: space-between;
            h2 {
              font-size: $font-size-middle;
              color: #333333;
              cursor: pointer;
              max-width: 500px;
            }
            .list_item_4 {
              display: flex;
              span {
                display: flex;
                align-items: center;
                font-size: 12px;
                margin-right: 20px;
                img {
                  width: 20px;
                }
              }
            }
          }
          span {
            padding: 2px 3px;
            border-radius: 2px;
          }
        }
        .list_item_3 {
          flex: 1;
          color: $font-color-2;
          font-size: $font-size-middle;
        }
      }
      .list-box li:last-child {
        border: none;
      }
    }
  }
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.menu-back {
  background: url("~@/assets/image/ziyuan/背景.png") no-repeat;
  background-size: 100% 100%;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  margin-bottom: 10px;
}
.el-menu {
  border-right: none;
}
.el-submenu__title i {
  font-weight: bold;
  color: #0080ff;
}
.el-submenu__icon-arrow {
  font-size: 16px;
}
</style>