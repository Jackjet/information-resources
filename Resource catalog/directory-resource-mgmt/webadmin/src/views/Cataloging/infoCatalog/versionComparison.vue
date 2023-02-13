<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <!--tab选择区域-->
      <el-col class="tab_area">
        <div class="type_area">
          {{ cataInfoHistoryCurrent.uviewNm  }}
        </div>
        <div class="operate_area">
          <el-col :span="12" class="containerItem">
            <div class="title">
              <span>版本:</span>
              <span>{{ cataInfoHistoryCurrent.version }}</span>
              <svg-icon icon-class="red-star" />
            </div>
            <div class="contentTable">
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.resClassify==cataInfoHistoryCompare.resClassify?'normal':'red'">部门信息资源分类</div>
                <div>{{ cataInfoHistoryCurrent.resClassify }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.uviewNm==cataInfoHistoryCompare.uviewNm?'normal':'red'">信息资源名称</div>
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCurrent.uviewNm">
                  <div>{{ cataInfoHistoryCurrent.uviewNm}}</div>
                </el-tooltip>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.uviewNo==cataInfoHistoryCompare.uviewNo?'normal':'red'">信息资源代码</div>
                <div>{{ cataInfoHistoryCurrent.uviewNo }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.provOrgName==cataInfoHistoryCompare.provOrgName?'normal':'red'">信息资源提供单位</div>
                <div>{{ cataInfoHistoryCurrent.provOrgName }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.belongToName==cataInfoHistoryCompare.belongToName?'normal':'red'">内设部门名称</div>
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCurrent.belongToName">
                  <div>{{ cataInfoHistoryCurrent.belongToName }}</div>
                </el-tooltip>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.mediaFmtName==cataInfoHistoryCompare.mediaFmtName?'normal':'red'">信息资源格式分类</div>
                <div>{{ cataInfoHistoryCurrent.mediaFmtName }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.mediaFmtTypeName==cataInfoHistoryCompare.mediaFmtTypeName?'normal':'red'">信息资源格式类型</div>
                <div>{{ cataInfoHistoryCurrent.mediaFmtTypeName }}</div>
              </div>

              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.updateCycName==cataInfoHistoryCompare.updateCycName?'normal':'red'">更新周期</div>
                <div>{{ cataInfoHistoryCurrent.updateCycName }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.pubDt==cataInfoHistoryCompare.pubDt?'normal':'red'">发布日期</div>
                <div>{{ cataInfoHistoryCurrent.pubDt }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.uviewDesc==cataInfoHistoryCompare.uviewDesc?'normal':'red'">信息资源摘要</div>
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCurrent.uviewDesc">
                  <div>{{ cataInfoHistoryCurrent.uviewDesc }}</div>
                </el-tooltip>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.baseClassify==cataInfoHistoryCompare.baseClassify?'normal':'red'">基础信息资源分类</div>
                <div>{{ cataInfoHistoryCurrent.baseClassify }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.themeClassify==cataInfoHistoryCompare.themeClassify?'normal':'red'">主题信息资源分类</div>
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCurrent.themeClassify">
                  <div>{{ cataInfoHistoryCurrent.themeClassify }}</div>
                </el-tooltip>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.shareLvName==cataInfoHistoryCompare.shareLvName?'normal':'red'">共享类型</div>
                <div>{{ cataInfoHistoryCurrent.shareLvName }}</div>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.shareCondition==cataInfoHistoryCompare.shareCondition?'normal':'red'">共享条件</div>
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCurrent.shareCondition">
                  <div>{{ cataInfoHistoryCurrent.shareCondition }}</div>
                </el-tooltip>
              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.pubSts==cataInfoHistoryCompare.pubSts?'normal':'red'">开放类型</div>
                <div>{{ cataInfoHistoryCurrent.pubSts=='01'?'无条件开放':(cataInfoHistoryCurrent.pubSts=='02'?'有条件开放':'不予开放') }}</div>

              </div>
              <div class="contentTableTr">
                <div :class="cataInfoHistoryCurrent.pubCondition==cataInfoHistoryCompare.pubCondition?'normal':'red'">开放条件</div>
                <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCurrent.pubCondition">
                  <div>{{ cataInfoHistoryCurrent.pubCondition }}</div>
                </el-tooltip>
              </div>
            </div>
          </el-col>
          <el-col :span="12" class="containerItem">
            <div class="title">
              <span>版本:</span>
              <span>{{ cataInfoHistoryCompare.version }}</span>
              <span />
            </div>
            <div class="contentTable">
              <div class="contentTable">
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.resClassify==cataInfoHistoryCompare.resClassify?'normal':'red'">部门信息资源分类</div>
                  <div>{{ cataInfoHistoryCompare.resClassify }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.uviewNm==cataInfoHistoryCompare.uviewNm?'normal':'red'">信息资源名称</div>
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCompare.uviewNm">
                    <div>{{cataInfoHistoryCompare.uviewNm }}</div>
                  </el-tooltip>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.uviewNo==cataInfoHistoryCompare.uviewNo?'normal':'red'">信息资源代码</div>
                  <div>{{ cataInfoHistoryCompare.uviewNo }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.provOrgName==cataInfoHistoryCompare.provOrgName?'normal':'red'">信息资源提供单位</div>
                  <div>{{ cataInfoHistoryCompare.provOrgName }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.belongToName==cataInfoHistoryCompare.belongToName?'normal':'red'">内设部门名称</div>
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCompare.belongToName">
                    <div>{{ cataInfoHistoryCompare.belongToName }}</div>
                  </el-tooltip>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.mediaFmtName==cataInfoHistoryCompare.mediaFmtName?'normal':'red'">信息资源格式分类</div>
                  <div>{{ cataInfoHistoryCompare.mediaFmtName }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.mediaFmtTypeName==cataInfoHistoryCompare.mediaFmtTypeName?'normal':'red'">信息资源格式类型</div>
                  <div>{{ cataInfoHistoryCompare.mediaFmtTypeName }}</div>
                </div>

                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.updateCycName==cataInfoHistoryCompare.updateCycName?'normal':'red'">更新周期</div>
                  <div>{{ cataInfoHistoryCompare.updateCycName }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.pubDt==cataInfoHistoryCompare.pubDt?'normal':'red'">发布日期</div>
                  <div>{{ cataInfoHistoryCompare.pubDt }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.uviewDesc==cataInfoHistoryCompare.uviewDesc?'normal':'red'">信息资源摘要</div>
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCompare.uviewDesc">
                    <div>{{ cataInfoHistoryCompare.uviewDesc }}</div>
                  </el-tooltip>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.baseClassify==cataInfoHistoryCompare.baseClassify?'normal':'red'">基础信息资源分类</div>
                  <div>{{ cataInfoHistoryCompare.baseClassify }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.themeClassify==cataInfoHistoryCompare.themeClassify?'normal':'red'">主题信息资源分类</div>
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCompare.themeClassify">
                    <div>{{ cataInfoHistoryCompare.themeClassify }}</div>
                  </el-tooltip>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.shareLvName==cataInfoHistoryCompare.shareLvName?'normal':'red'">共享类型</div>
                  <div>{{ cataInfoHistoryCompare.shareLvName }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.shareCondition==cataInfoHistoryCompare.shareCondition?'normal':'red'">共享条件</div>
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCompare.shareCondition">
                    <div>{{ cataInfoHistoryCompare.shareCondition }}</div>
                  </el-tooltip>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.pubSts==cataInfoHistoryCompare.pubSts?'normal':'red'">开放类型</div>
                  <div>{{ cataInfoHistoryCompare.pubSts=='01'?'无条件开放':(cataInfoHistoryCompare.pubSts=='02'?'有条件开放':'不予开放') }}</div>
                </div>
                <div class="contentTableTr">
                  <div :class="cataInfoHistoryCurrent.pubCondition==cataInfoHistoryCompare.pubCondition?'normal':'red'">开放条件</div>
                  <el-tooltip class="item" effect="light" :open-delay="1000" :content="cataInfoHistoryCompare.pubCondition">
                    <div>{{ cataInfoHistoryCompare.pubCondition }}</div>
                  </el-tooltip>
                </div>
              </div>
            </div>
          </el-col>
        </div>
        <div class="operate_item">
          <div class="item_title" style="border-bottom: 0;border-top:1px solid rgba(230, 229, 234, 1);margin-top: 20px">信息项</div>
          <el-col :span="12" class="containerItem">
            <div class="title">
              <span>版本:</span>
              <span>{{ cataInfoHistoryCurrent.version }}</span>
              <svg-icon icon-class="red-star" />
            </div>
            <div class="contentTable">
              <div v-for="item in compareDataText" v-if="item" :key="item ? item.name : Math.random(0,1)*10+new Date().getTime()">
                信息项【
                <span class="red">{{ item }}</span>
                】在比较版本中不存在
              </div>
            </div>
          </el-col>
          <el-col :span="12" class="containerItem">
            <div class="title">
              <span>版本:</span>
              <span>{{ cataInfoHistoryCompare.version }}</span>
              <span />
            </div>
            <div class="contentTable">
              <div v-for="item in compareDataTextOld" v-if="item" :key="item ? item.name : Math.random(0,1)*10+new Date().getTime()">
                信息项【
                <span class="red">{{ item }}</span>
                】在比较版本中不存在
              </div>
            </div>
          </el-col>
        </div>
        <div class="operate_item">
          <div class="item_title">信息项内容不一致清单</div>
          <el-table :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%;margin-top:20px;padding:0 45px">
            <el-table-column type="index" label="序号" />
            <el-table-column prop="code" label="信息项标识" width="180" />
            <el-table-column prop="type" label="信息项类型" width="180" />
            <el-table-column prop="currentVersion" :label="currentLabel" />
            <el-table-column prop="compareVersion" :label="compareLabel" />
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { historyCompare } from '@/api/resCatalog'
import mapIcon from '@/assets/mapIcon.png'
import DelConfirm from '@/components/DelConfirm'
import Pagination from '@/components/Pagination'
export default {
  name: 'VersionComparison',
  components: { Pagination, DelConfirm },
  props: {
    id: {
      type: [String, Number]
    }
  },
  data() {
    return {
      nowV: 1,
      oldV: 0,
      total: '',
      currentLabel: '',
      compareLabel: '',
      cataInfoHistoryCurrent: [],
      cataInfoHistoryCompare: [],
      compareDataText: [],
      compareDataTextOld: [],
      tableData: [],
      listQuery: {
        current: 1,
        size: 20
      },
      dataResourceCatalogId: '',
      selectedVersionData: {}
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.$store.dispatch('setInfoReBackDeptId', this.$route.query.provOrgCode)
    // 信息资源目录id
    this.dataResourceCatalogId = this.$route.query.dataResourceCatalogId || ''
    // 版本信息 路由url ：id 为版本id  params中 versionData 为版本管理中的列表数据
    // 通常用id查询到info后再渲染比直接用versionData 好由于没有几口 这里暂用versionData模拟
    this.selectedVersionData.versionNum = this.$route.query.versionNum || ''
    console.log(this.dataResourceCatalogId)
    // console.log(this.selectedVersionData )
    this.getList()
  },
  methods: {
    getRowClass({ row, column, rowIndex, columnIndex }) {
      if (rowIndex === 0) {
        return 'background: #F6F6F6;font-weight:bold;color:rgba(36,36,36,1);'
      } else {
        return ''
      }
    },
    getList() {
      historyCompare({ id: this.$props.id })
        .then(res => {
          var data = res.data.data
          // 当前记录
          this.cataInfoHistoryCurrent = Object.assign(
            {},
            data.cataInfoHistoryCurrent
          )
          // 基础类 主题类部门类 cd 1 2 3
          var baseClassifyArr = []
          var themeClassifyArr = []
          data.cataInfoHistoryCurrent.cataInfoHistoryTypeRelDtoList.forEach(
            item => {
              if (item.type == 3) {
                this.cataInfoHistoryCurrent.resClassify = item.typeName
              }
              if (item.type == 1) {
                baseClassifyArr.push(item.typeName)

              }
              if (item.type == 2) {
                themeClassifyArr.push(item.typeName)
              }
            }
          )
          this.cataInfoHistoryCurrent.themeClassify = themeClassifyArr.join(
            ','
          )
          this.cataInfoHistoryCurrent.baseClassify = baseClassifyArr.join(
            ','
          )
          // 历史记录
          this.cataInfoHistoryCompare = Object.assign(
            {},
            data.cataInfoHistoryCompare
          )
          var baseClassifyArrHis = []
          var themeClassifyArrHis = []
          data.cataInfoHistoryCompare.cataInfoHistoryTypeRelDtoList.forEach(
            item => {
              if (item.type == 3) {
                this.cataInfoHistoryCompare.resClassify = item.typeName
              }
              if (item.type == 1) {
                baseClassifyArrHis.push(item.typeName)
              }
              if (item.type == 2) {
                themeClassifyArrHis.push(item.typeName)
              }
            }
          )
          this.cataInfoHistoryCompare.themeClassify = themeClassifyArrHis.join(
            ','
          )
          this.cataInfoHistoryCompare.baseClassify = baseClassifyArrHis.join(
            ','
          )
          this.currentLabel = this.cataInfoHistoryCurrent.version
          this.compareLabel = this.cataInfoHistoryCompare.version

          this.compareDataText = this.cataInfoHistoryCurrent.surplusList
          this.compareDataTextOld = this.cataInfoHistoryCompare.surplusList
          this.tableData = data.cataItemCompareList
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    }
  }
}
</script>
<style scoped>
div {
  font-size: 14px;
}
.red {
  color: #e65555;
}
.contentTable {
  display: flex;
  flex-direction: column;
}
.contentTableTr {
  flex: 1;
  display: flex;
  line-height: 40px;
}
.contentTable .contentTableTr:nth-child(odd) {
  background: #f6f6f6;
}
.contentTable div {
  line-height: 40px;
}
.contentTableTr div {
  text-indent: 3vh;
  flex: 1;
  max-width: 20vw;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: block;
}
.containerItem {
  padding: 0 45px;
}
.containerItem .title {
  color: #242424;
  font-size: 15px;
  margin: 3vh 0;
  font-weight: Bold;
}
.department_content {
  background: #fff;
  border: 1px solid rgba(230, 229, 234, 1);
  border-radius: 6px;
}

.tab_area {
  height: 100%;
}
.item_title {
  line-height: 60px;
  padding-left: 45px;
  height: 60px;
  font-size: 16px;
  box-sizing: border-box;
  color: #1f3365;
  font-weight: 550;
}
.type_area {
  line-height: 40px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  min-height: 60px;
  box-sizing: border-box;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
}
.operate_area {
  overflow: hidden;
}
.operate_item {
  overflow: hidden;
}
.operate_item .item_title {
  margin: 0 45px;
  padding: unset;
}
.add {
  background: #1f3365;
  min-width: 80px;
  color: #fff;
  height: 36px;
}
.remove {
  color: #1f3365;
  min-width: 80px;
  border-color: #1f3365;
  height: 36px;
}
</style>

