<template>
  <div class="mt20 department_content contentArea">
    <el-row style="height: 100%">
      <!--tab选择区域-->
      <el-col :span="24">
        <div class="content_area">
          <div class="type_area">基本信息</div>
          <el-form ref="detailFormDom" :model="detailForm" label-width="100px" label-position="left" style="margin:30px 0 0 30px">
            <el-row>
              <el-col :span="22" :offset="2">
                <el-form-item label="信息资源名称：" prop="resName" label-width="160px" class="required_label">
                  <span>{{ detailForm.name || '暂无' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="22" :offset="2">
                <el-form-item label="信息资源代码：" prop="resCode" label-width="160px" class="required_label">
                  <span>{{ detailForm.code || '暂无' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="22" :offset="2">
                <el-form-item label="信息资源需求提供单位：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ `${detailForm.deptName}${detailForm.orgName!=undefind?' / '+detailForm.orgName:''}` || '暂无' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="22" :offset="2">
                <el-form-item v-if="!detailForm.provOrgId" label="统一社会信用代码：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ detailForm.provOrgCode || '暂无' }}</span>
                </el-form-item>
                <el-form-item v-else label="信息资源提供单位：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ `${detailForm.provOrgName}${detailForm.belongToName!=undefind?' / '+detailForm.belongToName:''}` || '暂无' }}</span>
                </el-form-item>
              </el-col>

              <!-- <el-col :span="22" :offset="2">
                <el-form-item
                  label="内设机构"
                  label-width="160px"
                  class="required_label"
                >
                  <span>{{ detailForm.belongToName || '暂无' }}</span>
                </el-form-item>
              </el-col> -->
              <el-col :span="22" :offset="2">
                <el-form-item label="是否实时获取：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ detailForm.isAccess == 1 ? '否' : '是' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="22" :offset="2">
                <el-form-item label="建议获取方式：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ select1[detailForm.accessWay] || '暂无' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="22" :offset="2">
                <el-form-item label="期望数据使用频率：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ select2[detailForm.useFrequency] || '暂无' }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="22" :offset="2">
                <el-form-item label="信息资源描述：" prop="summary" label-width="160px" class="required_label">
                  <span>{{ detailForm.remark||'暂无' }}</span>
                </el-form-item>
              </el-col>

              <el-col :span="22" :offset="2">
                <el-form-item label="用途：" prop="fax" label-width="160px" class="required_label">
                  <span>{{ detailForm.purpose||'暂无' }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import SelectTree from '@/components/SelectTree'
import { getRequirementDetail } from '@/api/requirementCatalog'
import { getDepartmentTree, getDictByType } from '@/api/departmentRes'
export default {
  name: 'RequirementCatalogMaintain',
  components: { Pagination, SelectTree },
  mixins: [mixinJs],
  props: {
    id: {
      type: [String, Number, Array],
      default: () => []
    },
    ziyuanId: {
      type: [String, Number, Array],
      default: () => []
    },
    typeForm: {
      type: [String, Number, Array],
      default: () => []
    }
  },
  data() {
    return {
      isDisabled: false,
      listQuery: {
        current: 1,
        size: 10
      },
      activeName: 0,
      tableData: [],
      total: 0,
      departmentName: '',
      detailForm: {},
      select1: {},
      select2: {}
    }
  },
  watch: {
    ziyuanId: {
      handler(newVal, oldVal) {
        console.log(newVal, oldVal)
        if (newVal) {
          this.getDetailData()
        } else {
          this.getDetailData()
        }
      },
      deep: true // 对象内部属性的监听，关键。
    }
  },
  created() {
    this.$store.dispatch('setReBackDeptId', this.$route.query.deptId)
    getDictByType({ type: 'share_way' }).then(res => {
      if (res.data.errno === 0) {
        res.data.data.forEach(item => {
          this.select1[item.value] = item.name
        })
      }
    })
    getDictByType({ type: 'update_cycle' }).then(res => {
      if (res.data.errno === 0) {
        res.data.data.forEach(item => {
          this.select2[item.value] = item.name
        })
      }
    })
    this.getDetailData()
  },
  methods: {
    getDetailData() {
      let thisId = this.id
      if (this.typeForm == 1) {
        thisId = this.ziyuanId
      }
      getRequirementDetail({ id: thisId }).then(res2 => {
        if (res2.data.errno === 0) {
          if (res2.data.provOrgId == '' || !res2.data.provOrgId) {
            this.detailForm.isOpen = '1'
          } else {
            this.detailForm.isOpen = '0'
          }
          debugger
          this.detailForm = Object.assign({}, this.detailForm, res2.data.data)
        }
      })
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
