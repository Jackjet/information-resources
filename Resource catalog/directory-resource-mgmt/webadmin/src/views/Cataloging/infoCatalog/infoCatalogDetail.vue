<template>
  <div class="mt20 department_content contentArea">
    <el-row style="height: 100%">
      <!--tab选择区域-->
      <el-col :span="24">
        <div class="content_area">
          <div class="type_area">信息资源目录</div>
          <el-tabs v-model="activeName" class="tab_area res_tabs" @tab-click="handleTabClick">
            <el-tab-pane label="1、基本信息维护" name="0">
              <el-form ref="departmentFormDom" :model="departmentForm" label-width="100px" label-position="left" style="margin-left: 30px">
                <el-row>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="市平台编码：" prop="cityCataCode" label-width="160px" class="required_label">
                      <div>{{ departmentForm.cityCataCode }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="部门信息资源分类：" prop="typeName" label-width="160px" class="required_label">
                      <div v-for="(item,index) in departmentForm.cataInfoTempTypeRelDtoList" v-if="departmentForm.cataInfoTempTypeRelDtoList && departmentForm.cataInfoTempTypeRelDtoList.length>0" :key="index">
                        <div v-if="item&&item.type == '3'">{{ item.typeName }}</div>
                      </div>
                      <span v-if="departmentForm.cataInfoTempTypeRelDtoList.length==0 || !departmentForm.cataInfoTempTypeRelDtoList">暂无</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="信息资源名称：" prop="uviewNm" label-width="160px" class="required_label">
                      <div>{{ departmentForm.uviewNm }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="信息资源代码：" prop="uviewNo" label-width="160px" class="required_label">
                      <div>{{ departmentForm.uviewNo }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="信息资源提供单位：" prop="provOrgName" label-width="160px" class="required_label">
                      <div>{{ departmentForm.provOrgName }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="内设部门名称：" prop="belongToName" label-width="160px" class="required_label">
                      <div>{{ departmentForm.belongToName }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="统一社会信用代码：" prop="provOrgCode" label-width="160px" class="required_label">
                      <div>{{ departmentForm.provOrgCode }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="信息资源格式分类：" prop="mediaFmtName" label-width="160px" class="required_label">
                      <div>{{ departmentForm.mediaFmtName }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="信息资源格式类型：" prop="mediaFmtTypeName" label-width="160px" class="required_label">
                      <div>{{ departmentForm.mediaFmtTypeName }}</div>
                    </el-form-item>
                  </el-col>

                  <el-col :span="22" :offset="2">
                    <el-form-item label="更新周期：" prop="updater" label-width="160px" class="required_label">
                      <div>{{ departmentForm.updateCycName }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="发布日期：" prop="pubDt" label-width="160px" class="required_label">
                      <div>{{ departmentForm.pubDt }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="信息资源摘要：" prop="uviewDesc" label-width="160px" class="required_label">
                      <div>{{ departmentForm.uviewDesc }}</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="基础信息资源分类：" prop="cataInfoTempTypeRelDtoList" label-width="160px" class="required_label">
                      <span v-for="(item,index) in departmentForm.cataInfoTempTypeRelDtoList" v-if="departmentForm.cataInfoTempTypeRelDtoList && departmentForm.cataInfoTempTypeRelDtoList.length>0&&item&&item.type == '1'" :key="index" class="mr5">
                        <div v-if="item&&item.type == '1'" style="float: left;margin-right: 8px">
                          <el-tag type="success" effect="plain">{{ item.typeName }}</el-tag>
                        </div>
                      </span>
                      <span v-if="departmentForm.cataInfoTempTypeRelDtoList.length==0 || !departmentForm.cataInfoTempTypeRelDtoList">暂无</span>
                    </el-form-item>
                  </el-col>

                  <el-col :span="22" :offset="2">
                    <el-form-item label="主题信息资源分类：" prop="cataInfoTempTypeRelDtoList" label-width="160px" class="required_label">
                      <span v-for="(item,index) in departmentForm.cataInfoTempTypeRelDtoList" v-if="departmentForm.cataInfoTempTypeRelDtoList && departmentForm.cataInfoTempTypeRelDtoList.length>0&&item&&item.type == '2'" :key="index" class="mr5">
                        <div v-if="item&&item.type == '2'" style="float: left;margin-right: 8px">
                          <el-tag effect="plain">{{ item.typeName }}</el-tag>
                        </div>
                      </span>
                      <span v-if="departmentForm.cataInfoTempTypeRelDtoList.length==0 || !departmentForm.cataInfoTempTypeRelDtoList">暂无</span>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="共享类型：" prop="shareLv" label-width="160px" class="required_label">
                      <div v-if="departmentForm.shareLv == '01'">无条件共享</div>
                      <div v-else-if="departmentForm.shareLv == '02'">有条件共享</div>
                      <div v-else-if="departmentForm.shareLv == '03'">不予共享</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="共享条件：" prop="shareCondition" label-width="160px" class="required_label">
                      <el-tooltip class="item" effect="light" :open-delay="1000" :content="departmentForm.shareCondition">
                        <span>{{ departmentForm.shareCondition?departmentForm.shareCondition:"无" }}</span>
                      </el-tooltip>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="开放类型：" prop="pubSts" label-width="160px" class="required_label">
                      <div v-if="departmentForm.pubSts == '03'">不予开放</div>
                      <div v-if="departmentForm.pubSts == '02'">有条件开放</div>
                      <div v-if="departmentForm.pubSts == '01'">无条件开放</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="开放条件：" prop="pubCondition" label-width="160px" class="required_label">
                      <el-tooltip class="item" effect="light" :open-delay="1000" :content="departmentForm.pubCondition">
                        <span>{{ departmentForm.pubCondition?departmentForm.pubCondition:"无" }}</span>
                      </el-tooltip>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="共享范围:" prop="shareScope" label-width="160px" class="required_label">
                      <el-tooltip class="item" effect="light" :open-delay="1000" :content="departmentForm.shareScope">
                        <span>{{ departmentForm.shareScope }}</span>
                      </el-tooltip>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="是否涉密:" prop="secret" label-width="160px" class="required_label">
                      <div v-if="departmentForm.secret == 0">否</div>
                      <div v-else-if="departmentForm.secret == 1">是</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="数据范围:" prop="dataScope" label-width="160px" class="required_label">
                      <el-tooltip class="item" effect="light" :open-delay="1000" :content="departmentForm.dataScope">
                        <span>{{ departmentForm.dataScope }}</span>
                      </el-tooltip>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2" v-show="isHistory!=1">
                    <el-form-item label="是否关联权责:" prop="relBusi" label-width="160px" class="required_label">
                      <div v-if="departmentForm.relBusi == 0">否</div>
                      <div v-else-if="departmentForm.relBusi == 1">是</div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2">
                    <el-form-item label="变更说明：" prop="lastUpdateDesc" label-width="160px" class="required_label">
                      <el-tooltip class="item" effect="light" :open-delay="1000" :content="departmentForm.lastUpdateDesc">
                        <span>{{ departmentForm.lastUpdateDesc?departmentForm.lastUpdateDesc:"无" }}</span>
                      </el-tooltip>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2" v-show="departmentForm.relBusi==0 && isHistory!=1">
                    <el-form-item ref="relBusiMsgDom" label="权责清单补充说明:" prop="relBusiMsg" label-width="160px" class="required_label">
                      <el-tooltip class="item" effect="light" :open-delay="1000" :content="departmentForm.relBusiMsg">
                        <span>{{ departmentForm.relBusiMsg }}</span>
                      </el-tooltip>
                    </el-form-item>
                  </el-col>
                  <el-col :span="22" :offset="2" v-show="departmentForm.relBusi==1 && isHistory!=1">
                    <el-form-item label="关联权责清单:" prop="relBusList" label-width="160px" class="required_label">
                      <el-tree ref="treeRelBus" :data="relBusiOptions" show-checkbox node-key="busiId" default-expand-all :default-checked-keys="departmentForm.relBusList" :props="defaultProps">
                      </el-tree>
                    </el-form-item>
                  </el-col>
                </el-row>
              </el-form>
            </el-tab-pane>
            <el-tab-pane label="2、信息项维护" name="1">
              <div class="related_btns">
                <!-- <el-input
                  v-model="listQuery.srcField"
                  clearable
                  class="filter-item searchInputWidth"
                  placeholder="信息项名称"
                />
                <el-input
                  v-model="listQuery.engCd"
                  clearable
                  class="filter-item searchInputWidth"
                  placeholder="英文标识"
                />-->
                <el-input v-model="listQuery.srcFieldAndEngCd" clearable class="filter-item searchInputWidth" placeholder="信息项名称或标识" style="width: 25%;" />
                <el-button class="add" size="mini" @click="getTableData">查询</el-button>
              </div>
              <el-table :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column v-for="headItem in formtableData.insideArr" :key="headItem.id" fixed :prop="headItem.itemMark" :label="headItem.itemName">
                  <template slot-scope="scope">
                    <div v-if="headItem.itemCode">
                      <div v-if="headItem.multiple && scope.row[headItem.itemMark] && scope.row[headItem.itemMark].toString().charAt(0) === '['">
                        <el-tag v-for="(_item,_index) in JSON.parse(scope.row[headItem.itemMark])" :key="_item">{{ headItem.selectsObj[_item] }}</el-tag>
                      </div>
                      <div v-else-if="scope.row[headItem.itemMark] && scope.row[headItem.itemMark].toString().charAt(0) === '['">{{ headItem.selectsObj[JSON.parse(scope.row[headItem.itemMark])[0]] }}</div>
                      <div v-else>{{ headItem.selectsObj[scope.row[headItem.itemMark]] }}</div>
                    </div>
                    <div v-if="!headItem.itemCode">{{ scope.row[headItem.itemMark] }}</div>
                  </template>
                </el-table-column>
                <el-table-column v-for="(headItem,headIndex) in formtableData.outsideArr" :key="headItem.id" :prop="'ext'+(headIndex+1)" :label="headItem.itemName">
                  <template slot-scope="scope">
                    <div v-if="headItem.itemCode">
                      <div v-if="headItem.multiple && scope.row[`ext${headIndex+1}`] &&scope.row[`ext${headIndex+1}`].toString().charAt(0) === '['">
                        <el-tag v-for="(_item,_index) in JSON.parse(scope.row[`ext${headIndex+1}`])" :key="_item">{{ headItem.selectsObj[_item] }}</el-tag>
                      </div>
                      <div v-else-if="scope.row[`ext${headIndex+1}`] && scope.row[`ext${headIndex+1}`].toString().charAt(0) === '['">{{ headItem.selectsObj[JSON.parse(scope.row[`ext${headIndex+1}`])[0]] }}</div>
                      <div v-else>{{ headItem.selectsObj[scope.row[`ext${headIndex+1}`]] }}</div>
                    </div>
                    <div v-if="!headItem.itemCode">
                      <div>{{ scope.row[`ext${headIndex+1}`] }}</div>
                    </div>
                  </template>

                </el-table-column>
                <el-table-column label="操作" width="100px" fixed="right">
                  <template slot-scope="scope">
                    <el-tooltip content="查看脱敏" effect="light" :open-delay="1000" placement="bottom">
                      <span class="tr_detail_icon fl">
                        <svg-icon icon-class="tuomin" class-name="" @click="regdetail(scope.row)" />
                      </span>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
              <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
              <el-dialog :modal-append-to-body="false" title="数据脱敏规则详情" align="center" :visible.sync="regsetDialog" width="50%" :close-on-click-modal="false">
                <el-form :model="regSetForm" align="left" class="demo-ruleForm" label-position="left">
                  <el-form-item label="脱敏规则:" label-width="140px" class="required_label">
                    <span v-if="regSetForm.type == '0'">掩码规则</span>
                    <span v-else-if="regSetForm.type == '1'">正则表达式</span>
                    <span v-else-if="regSetForm.type == '2'">字符替换</span>
                  </el-form-item>
                  <div v-show="isShowStrInput">
                    <el-form-item label="保留前几个字符" class="required_label" label-width="140px">
                      <span>{{ regSetForm.head }}</span>
                    </el-form-item>
                    <el-form-item label="保留后几个字符" class="required_label" label-width="140px">
                      <span>{{ regSetForm.behind }}</span>
                    </el-form-item>
                  </div>

                  <el-form-item v-show="isShowregexpres" label="正则表达式" class="required_label" label-width="140px">
                    <span>{{ regSetForm.regular }}</span>
                  </el-form-item>
                  <el-form-item label="替换字符" class="required_label" label-width="140px">
                    <span>{{ regSetForm.letter }}</span>
                  </el-form-item>
                </el-form>
                <div class="dialogDom" align="center">
                  <el-button size="mini" class="remove" @click="regsetDialog=false">关 闭</el-button>
                </div>
              </el-dialog>

            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {
  resHistoryDetail,
  infoHistoryDetail,
  resDetail,
  infoResDetail,
  getFormItem,
  getRegInfo
} from '@/api/resCatalog'
import { getBusinessTreeByDeptId } from '@/api/businessManagement'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import { getDictByType } from '@/api/departmentRes'
import { checkMobile, checkMobileRepeat } from '@/utils/formValidateMixin'
import mixinJs from '@/utils/mixin'
import SelectTree from '@/components/SelectTree'
export default {
  name: 'InfoCatalogMaintain',
  components: { Pagination, SelectTree },
  mixins: [mixinJs],
  props: {
    id: {
      type: [String, Number, Array],
      default: () => []
    },

    typeForm: {
      type: [String, Number, Array],
      default: () => []
    }
  },

  data() {
    checkMobile
    checkMobileRepeat
    return {
      isHistory: 0,
      isShowStrInput: true,
      isShowregexpres: false,
      isShowRegRadio: false,
      regsetDialog: false,
      regSelect: [{
        value: '0',
        label: '掩码规则'
      }, {
        value: '1',
        label: '正则表达式'
      }, {
        value: '2',
        label: '字符替换'
      }],
      regSetForm: {},
      formtableData: [],
      id: '',
      selectOrgs: [],
      isDisabled: false,
      listQuery: {
        page: 1,
        limit: 10,
        srcField: '',
        engCd: ''
      },
      mapIcon: mapIcon,
      activeName: 0,
      tableData: [],
      total: 0,
      departmentForm: { cataInfoTempTypeRelDtoList: [] },
      departmentName: '',

      // 新增表单校验
      createRules: {
        // 暂未添加
        applyUserName: [
          { required: true, message: '请选择业主名称', trigger: 'blur' }
        ],
        address: [{ required: true, message: '请选择地址', trigger: 'blur' }],
        phone: [{ required: true, message: '请选择电话', trigger: 'blur' }],
        ownerType: [
          { required: true, message: '请选择业主类型', trigger: 'blur' }
        ],
        type: [{ required: true, message: '请选择类型', trigger: 'blur' }],
        repairType: [
          { required: true, message: '请选择报修类型', trigger: 'blur' }
        ],
        deadline: [
          { required: true, message: '请选择上门时间', trigger: 'blur' }
        ]
      },
      addDataItemForm: {},
      serviceObjs: [
        {
          value: '1',
          label: '字符串C'
        }
      ],
      selectResClassifyDialog: false,
      baseClassifyDialog: false,
      themeClassifyDialog: false,
      searchQuery: {
        keyword: '',
        current: 1,
        size: 5
      },
      searchTotal: 0,
      searchRelatedResults: [],
      choosedRelatedResults: [],
      treeData: [],
      basetreeData: [],
      defaultProps: {
        children: 'children',
        label: 'busiNm',
        value: 'busiId',
        disabled: this.disabledFn,
      },
      relBusiOptions: [],
      dialogTitle: '新增数据项',
      addWorkOrder: false,
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      loading: false,
      select1: [
        { value: '01', label: '电子文件' },
        { value: '02', label: '电子表格' },
        { value: '03', label: '数据库' },
        { value: '04', label: '图形图形' },
        { value: '05', label: '流媒体' },
        { value: '06', label: '自描述格式' }
      ],
      allResFomatterType: [
        { pro: '01', label: '.text' },
        { pro: '02', label: '.core' },
        { pro: '03', label: 'test' },
        { pro: '04', label: 'pro' },
        { pro: '05', label: '1.1.2' },
        { pro: '06', label: '1.1.1' }
      ],
      select2: [],
      mediaFmtTypeObj: {}
    }
  },
  watch: {
    id: {
      handler(newVal, oldVal) {
        console.log(newVal, oldVal)
        if (newVal) {
          this.getDetailData()
          this.getTableData()
        } else {
          this.getDetailData()
          this.getTableData()
        }
      },
      deep: true // 对象内部属性的监听，关键。
    }
  },
  created() {
    this.isHistory = this.$route.query.isHistory
    this.$store.dispatch('setInfoReBackDeptId', this.$route.query.provOrgCode)
    this.getDetailData()
    this.getTableData()
    this.getTableHeader()
    //获取部门下的权责清单树
    this.getBusinessTreeBy(this.$route.query.provOrgId)
  },
  methods: {
    disabledFn() {
      return true
    },
    //
    getBusinessTreeBy(detpId) {
      console.log(detpId, "==========")
      var params = { 'deptId': detpId }
      getBusinessTreeByDeptId(params).then(response => {
        console.log(response, "00000000000")
        if (response.data.errno == 0) {
          response.data.data[0].children.forEach(item => {
            // item.disabled = true;
            this.$set(item, "disabled", true)
          })
          this.relBusiOptions = response.data.data;
          // console.log(this.relBusiOptions, "00000000000")
        }
      })
    },
    /**
     * 脱敏弹框
     */
    regdetail(row) {
      const id = this.$route.params.id
      getRegInfo({ itemId: row.uviewstrId }).then(res => {
        if (res.data.errno == 0) {
          if (res.data.data) {
            if (res.data.data.type == '0') {
              this.isShowStrInput = true
              this.isShowregexpres = false
            } else if (res.data.data.type == '1') {
              this.isShowStrInput = false
              this.isShowregexpres = true
            } else {
              this.isShowStrInput = false
              this.isShowregexpres = false
            }
            this.regSetForm = Object.assign({}, res.data.data)
            this.regsetDialog = true
          } else {
            this.$message({
              message: '暂未配置数据脱敏规则,无法查看!',
              type: 'error'
            })
          }
        }
      })
    },
    // 获取列表表头
    getTableHeader() {
      const sendParam = {
        itemMark: '',
        itemName: '',
        itemNameAndItemMark: '',
        limit: 100,
        page: 1
      }
      getFormItem(sendParam).then(response => {
        if (response.data.errno == 0) {
          // this.formtableData = response.data.data.records
          var promises = []
          response.data.data.records.forEach(item => {
            item.selectsObj = {}
            if (item.itemCode) {
              promises.push(
                getDictByType({ type: item.itemCode }).then(res => {
                  res.data.data.forEach(item1 => {
                    item.selectsObj[item1.value] = item1.name
                  })
                })
              )
            }
            if (item.itemType == '04') {
              item.multiple = true
            }
          })
          Promise.all(promises).then(() => {
            this.formtableData.insideArr = response.data.data.records.filter(
              item => {
                return item.isInside == 1
              }
            )
            console.log(this.formtableData.insideArr)
            const keyMap = {
              SJXMC: 'srcField',
              SJLX: 'srcDataTyp',
              XSXH: 'sno',
              SJXMS: 'itemRemark',
              YWBS: 'engCd',
              SJCD: 'dataLen'
            }
            this.formtableData.insideArr.forEach(_item => {
              for (var key in keyMap) {
                if (_item['itemMark'] === key) {
                  _item['itemMark'] = keyMap[key]
                }
              }
            })
            console.log(this.formtableData.insideArr)
            this.formtableData.outsideArr = response.data.data.records.filter(
              item => {
                return item.isInside != 1
              }
            )
          })
        }
      })
    },
    getDetailData() {
      let rowid = this.$route.params.id
      const type = this.$route.query.isHistory
      if (this.typeForm == 1) {
        rowid = this.id
      }
      if (type == 1 && type != undefined) {
        resHistoryDetail({ id: rowid }).then(response => {
          if (response.data.errno == 0) {
            const mediaFmt = response.data.data.mediaFmt
            if (mediaFmt) {
              getDictByType({ type: `format_type_${mediaFmt}` }).then(res => {
                res.data.data.forEach(item => {
                  this.mediaFmtTypeObj[item.value] = item.name
                  this.departmentForm = Object.assign({}, response.data.data, {
                    cataInfoTempTypeRelDtoList:
                      response.data.data.cataInfoHistoryTypeRelDtoList
                  })
                  if (!this.departmentForm.cataInfoTempTypeRelDtoList) {
                    this.departmentForm.cataInfoTempTypeRelDtoList = []
                  }
                  this.departmentForm.mediaFmtTypeName = this.mediaFmtTypeObj[
                    this.departmentForm.mediaFmtType
                  ]
                })
              })
            } else {
              this.departmentForm = Object.assign({}, response.data.data)
              if (!this.departmentForm.cataInfoTempTypeRelDtoList) {
                this.departmentForm.cataInfoTempTypeRelDtoList = []
              }
            }

            // this.listQuery.srcField = response.data.data.uviewNm
            // this.listQuery.engCd = response.data.data.uviewNo
          }
        })
      } else {
        resDetail({ id: rowid }).then(response => {
          if (response.data.errno == 0) {
            const mediaFmt = response.data.data.mediaFmt
            if (mediaFmt) {
              getDictByType({ type: `format_type_${mediaFmt}` }).then(res => {
                res.data.data.forEach(item => {
                  this.mediaFmtTypeObj[item.value] = item.name
                  this.departmentForm = Object.assign({}, response.data.data)
                  if (!this.departmentForm.cataInfoTempTypeRelDtoList) {
                    this.departmentForm.cataInfoTempTypeRelDtoList = []
                  }
                  this.departmentForm.mediaFmtTypeName = this.mediaFmtTypeObj[
                    this.departmentForm.mediaFmtType
                  ]
                })
              })
            } else {
              this.departmentForm = Object.assign({}, response.data.data)
              if (!this.departmentForm.cataInfoTempTypeRelDtoList) {
                this.departmentForm.cataInfoTempTypeRelDtoList = []
              }
            }

            // this.listQuery.srcField = response.data.data.uviewNm
            // this.listQuery.engCd = response.data.data.uviewNo
          }
        })
      }
    },

    closeAddDialog() {
      this.searchRelatedResults = []
      this.searchTotal = 0
      this.searchQuery.current = 1
      this.searchQuery.keyword = ''
    },
    getTableData() {
      let id = this.$route.params.id
      const type = this.$route.query.isHistory
      if (this.typeForm == 1) {
        id = this.id
      }
      if (type == 1 && type != undefined) {
        infoHistoryDetail(Object.assign({}, this.listQuery, { uviewId: id }))
          .then(response => {
            if (response.data.errno == 0) {
              const resultArr = []
              this.tableData = response.data.data.records
              this.total = response.data.data.total
            }
          })
          .catch(() => {
            this.list = []
            this.total = 0
          })
      } else {
        infoResDetail(Object.assign({}, this.listQuery, { uviewId: id }))
          .then(response => {
            if (response.data.errno == 0) {
              const resultArr = []
              this.tableData = response.data.data.records
              this.total = response.data.data.total
            }
          })
          .catch(() => {
            this.list = []
            this.total = 0
          })
      }
    },

    handleTabClick(tab, event) {
      console.log(tab, event)
    },
    handleSelectionChange(val) {
      console.log(val)
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },

    handleNodeClick(data) {
      console.log(data)
    }
  }
}
</script>

<style scoped>
.content_area .el-form-item {
  margin-bottom: 0px;
}
.content_area .el-form-item {
  color: #242424;
}
.search_result {
  margin: 0 auto;
  margin-top: 20px;
}
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.department_content {
  background: #fff;
  border: 1px solid rgba(230, 229, 234, 1);
  border-radius: 6px;
  min-height: 750px !important;
  height: auto !important;
}

.tree_title {
  padding-left: 30px;
  height: 60px;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  line-height: 60px;
  opacity: 1;
}

.tree_content {
  padding-top: 24px;
  padding-left: 20px;
  padding-bottom: 24px;
  box-sizing: border-box;
}

.tree_city {
  margin-right: 12px;
  margin-left: 6px;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}

.tree_desc,
.type_area {
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
}
.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.tree_area {
  background: #f9f9f9;
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}

.related_btns {
  margin-top: 10px;
  margin-bottom: 20px;
}

.tr_detail_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  text-align: center;
  margin-right: 14px;
  cursor: pointer;
}

.tr_detail_icon svg {
  cursor: pointer;
  width: 20px;
  height: 20px;
}

.tr_del_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50% 50%;
  border: 2px solid rgba(46, 78, 161, 1);
  border-radius: 50%;
  opacity: 1;
  text-align: center;
  position: relative;
  cursor: pointer;
}

.tr_del_icon svg {
  cursor: pointer;
  width: 10px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}
.tab_area {
  padding-left: 40px;
  padding-right: 40px;
}
.selectBtn :hover,
.selectBtn:focus {
  color: rgba(31, 51, 101, 0.9) !important;
}
.codeHint {
  font-size: 12px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  line-height: 33px;
  color: rgba(126, 126, 126, 1);
  opacity: 1;
}
.multipleResSelect {
  width: 100% !important;
}
.searchInputWidth {
  width: 30%;
}
.mr5 {
  margin-right: 5px;
}
</style>
<style>
.res_tabs .el-tabs__item.is-active {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
  opacity: 1;
}
.res_tabs .el-tabs__item {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(77, 77, 77, 1);
  opacity: 1;
}
</style>
