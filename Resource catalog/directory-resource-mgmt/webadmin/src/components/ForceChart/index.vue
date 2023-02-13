<template>
  <div>
    <div :id="domId" ref="inflowOrOutflowChart" :class="className" :style="{height:'780px',width:'90%'}" style="margin-left:10%" />
    <div>
      <!--信息资源目录详情-->
      <el-dialog title="资源目录详情" :modal-append-to-body="false" :visible.sync="delDialog" width="70%">
        <InfoCatalogMaintain :id="ziyuanmulu" :type-form="1" />
      </el-dialog>
      <!--需求目录详情-->
      <el-dialog title="需求目录详情" :modal-append-to-body="false" :visible.sync="xuquDialog" width="48%">
        <RequirementCatalogMaintain :ziyuan-id="xuqiumulu" :type-form="1" />
      </el-dialog>
      <!--应用事项详情-->
      <el-dialog title="权责清单详情" :modal-append-to-body="false" :visible.sync="ywsxDialog" width="48%">
        <el-form ref="addbusinessForm1" :model="addbusinessForm1" align="left" class="demo-ruleForm" label-position="left">
          <el-form-item label="部门名称：" label-width="140px" prop="departmentName">
            <span>{{ addbusinessForm1.departmentName || addbusinessForm1.orgNm }}</span>
          </el-form-item>
          <el-form-item label="上级权责清单：" label-width="140px" prop="name">
            <span>{{ addbusinessForm1.name || addbusinessForm1.pBusiNm }}</span>
          </el-form-item>
          <el-form-item label="权责清单名称：" class="required_label" label-width="140px" prop="busiNm">
            <el-input v-model="addbusinessForm1.busiNm" disabled style="width:100%" placeholder="请输入权责清单名称" />
          </el-form-item>
          <el-form-item label="权责清单编码：" class="required_label" label-width="140px" prop="busiNo">
            <el-input v-model="addbusinessForm1.busiNo" style="width:100%" placeholder="机构编码" disabled />
          </el-form-item>
          <el-form-item label="服务对象：" class label-width="140px" prop="serviceObj">
            <el-select v-model="addbusinessForm1.serviceObj" disabled style="width:100%" placeholder="请选择服务对象">
              <el-option v-for="item in serviceObjs" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="所支撑的应用系统：" class="required_label" label-width="140px">
            <el-input v-if="!addbusinessForm1.appList || addbusinessForm1.appList.length == 0" disabled style="cursor: pointer;width:100%" placeholder="所支撑的应用系统" readonly suffix-icon="el-icon-plus" />

            <el-tag v-for="tag in addbusinessForm1.appList" v-else :key="tag.appsysId" class="posTag" style="margin-right: 10px" size="small" type="success" :disable-transitions="false">
              <el-tooltip class="item" effect="light" :open-delay="1000" :content="tag.appsysNm">
                <span>{{ tag.appsysNm }}</span>
              </el-tooltip>
            </el-tag>
          </el-form-item>
          <el-form-item label="备注：" label-width="140px">
            <el-input v-model="addbusinessForm1.remark" disabled :rows="5" type="textarea" style="width:100%" />
          </el-form-item>
        </el-form>
      </el-dialog>
      <!--应用系统详情-->
      <el-dialog :modal-append-to-body="false" title="应用系统详情" align="center" :visible.sync="yyxtDialog" width="48%" :close-on-click-modal="false">
        <el-form ref="addbusinessForm" :model="addbusinessForm" align="left" class="demo-ruleForm" label-position="left">
          <el-form-item label="部门名称：" label-width="140px" prop="departmentName">
            <span>{{ addbusinessForm.departmentName }}</span>
          </el-form-item>
          <el-form-item label="内设部门：" class label-width="140px">
            <select-tree v-if="yyxtDialog==true" ref="selectTreeDom" :node-key="'orgId'" :is-multiple="false" :check-strictly="true" disabled :select-ids="selectOrgs" :select-treedata="selectTreedata" @checkedChoose="getCheckedOrg(arguments)" />
          </el-form-item>
          <el-form-item label="应用系统代码：" class="required_label" label-width="140px" prop="appsysNo">
            <el-input v-model="addbusinessForm.appsysNo" disabled style="width:100%" placeholder="请输入应用系统代码" />
          </el-form-item>
          <el-form-item label="应用系统名称：" class="required_label" label-width="140px" prop="appsysNm">
            <el-input v-model="addbusinessForm.appsysNm" disabled style="width:100%" placeholder="请输入应用系统名称" />
          </el-form-item>
          <el-form-item label="应用系统简介：" class="required_label" label-width="140px">
            <el-input v-model="addbusinessForm.appsysDesc" disabled :rows="2" type="textarea" style="width:100%" />
          </el-form-item>

          <el-form-item label="建设性质：" label-width="140px" prop="nature" class>
            <el-select v-model="addbusinessForm.nature" disabled style="width:100%" placeholder="建设性质">
              <el-option v-for="item in buildNatures" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item label="部署位置：" label-width="140px" prop="position" class>
            <el-select v-model="addbusinessForm.position" disabled style="width:100%" placeholder="部署位置">
              <el-option v-for="item in deployLocations" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>

          <el-form-item label="接入网络类型：" label-width="140px" prop="netType" class>
            <el-select v-model="addbusinessForm.netType" disabled style="width:100%" placeholder="接入网络类型">
              <el-option v-for="item in networdTypes" :key="item.value" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="所支撑的权责清单：" class="required_label" label-width="140px">
            <el-input v-if="!addbusinessForm.busis || addbusinessForm.busis.length == 0" disabled style="cursor: pointer;width:100%" placeholder="所支撑的权责清单" readonly suffix-icon="el-icon-plus" />
            <el-tag v-for="tag in addbusinessForm.busis" v-if="addbusinessForm.busis && addbusinessForm.busis.length > 0&&tag!=null" :key="tag.busiId" class="posTag" style="margin-right: 10px;" size="small" type="success" :disable-transitions="false">
              <el-tooltip class="item" effect="light" :open-delay="1000" :content="tag.busiNm">
                <span>{{ tag.busiNm }}</span>
              </el-tooltip>
            </el-tag>
          </el-form-item>
          <el-form-item label="备注：" label-width="140px">
            <el-input v-model="addbusinessForm.remark" disabled :rows="5" type="textarea" style="width:100%" />
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import InfoCatalogMaintain from '@/views/Cataloging/infoCatalog/infoCatalogDetail'
import RequirementCatalogMaintain from '@/views/Cataloging/requirementCatalog/requirementCatalogDetail'
import { getDictByType } from '@/api/departmentRes'
import SelectTree from '@/components/SelectTree'
import { getOSDetail, getOrgTree } from '@/api/OSManagement'
import { getBusinessDetail } from '@/api/businessManagement'
export default {
  name: 'ForceChart',
  components: { InfoCatalogMaintain, RequirementCatalogMaintain, SelectTree }, // 这个LoginName最好和引入的vue的LoginName相同
  props: {
    className: {
      type: String,
      default: 'inflowOutflow'
    },
    domId: {
      type: String,
      default: 'inflowOutflowID'
    },
    width: {
      type: String,
      default: '90%'
    },
    data: {
      type: Object
    }
  },
  data() {
    return {
      height: document.documentElement.clientHeight,
      selectTreedata: [],
      delDialog: false,
      ziyuanmulu: null,
      xuqiumulu: null,
      xuquDialog: false,
      yyxtDialog: false,
      selectOrgs: [],
      addbusinessForm: {},
      addbusinessForm1: {},
      serviceObjs: {},
      ywsxDialog: false,
      deployLocations: [],
      buildNatures: [],
      networdTypes: []
    }
  },
  watch: {
    // 观察option的变化
    data: {
      handler(newVal, oldVal) {
        console.log(newVal, oldVal)
        if (newVal) {
          this.drawflow()
        } else {
          this.drawflow()
        }
      },
      deep: true // 对象内部属性的监听，关键。
    }
  },
  created() {
    getOrgTree({ orgId: this.$route.query.deptId }).then(res => {
      if (res.data) {
        this.selectTreedata = res.data.data
      }
    })
    // 获取数据字典选择项
    getDictByType({ type: 'build_nature' }).then(res => {
      if (res.data.errno === 0) {
        this.buildNatures = res.data.data
      }
    })
    getDictByType({ type: 'deploy_location' }).then(res => {
      if (res.data.errno === 0) {
        this.deployLocations = res.data.data
      }
    })
    getDictByType({ type: 'network_type' }).then(res => {
      if (res.data.errno === 0) {
        this.networdTypes = res.data.data
      }
    })
    getDictByType({ type: 'service_obj' }).then(res => {
      if (res.data.errno === 0) {
        this.serviceObjs = res.data.data
      }
    })
  },
  mounted() {
    console.log(document.getElementById('inflowOutflowID').clientWidth / 2)
    this.drawflow()
  },
  methods: {
    drawflow() {
      const inOrOutFlowChart = this.$echarts.init(
        document.getElementById('inflowOutflowID')
      )
      const color1 = '#172853' // 应用系统
      const color2 = '#E65555' // 权责清单
      const color3 = '#336AC8' // 资源目录
      const color4 = '#5ABD8C' // 需求目录
      if (this.data.nodes == undefined) {
        return false
      }
      this.data.nodes.forEach(node => {
        node.label = {
          formatter(x) {
            var val = x.data.name
            var strs = val.split('') // 字符串数组
            var str = ''
            var length = strs.length > 6 ? 6 : strs.length

            for (var i = 0; i < length; i++) {
              str += strs[i]
            }
            if (strs.length > 6) {
              return str + '...'
            } else {
              return str
            }
            // for (var i = 0, s; (s = strs[i++]); ) {
            //   //遍历字符串数组
            //   str += s;
            //   if (!(i % 8)) str += "\n"; //按需要求余
            // }
          }
        }
        if (node.category === 0) {
          node.x = (document.getElementById('inflowOutflowID').clientWidth) / 2
          node.y = 325
          node.fixed = true
          node.symbol = 'circle'
          node.symbolSize = [158, 95]
          node.itemStyle = {
            color: color1
          }
          node.label.borderRadius = 122
        } else if (node.category === 1) {
          node.symbol = 'circle'
          node.symbolSize = [80, 80]
          node.itemStyle = {
            color: color2
          }
        } else if (node.category === 2) {
          node.symbol = 'roundRect'
          node.symbolSize = [80, 28]
          node.itemStyle = {
            color: color3
          }
          node.label.borderRadius = 12
        } else if (node.category === 3) {
          node.symbol = 'roundRect'
          node.symbolSize = [80, 28]
          node.itemStyle = {
            color: color4
          }
          node.label.borderRadius = 12
        }
      })

      this.data.links.forEach(link => {
        link.label = {
          align: 'center',
          fontSize: 12
        }

        if (link.name === '权责清单') {
          link.lineStyle = {
            color: color1,
            curveness: 0,
            width: 4
          }
        } else if (link.name === '资源目录') {
          link.lineStyle = {
            color: color3,
            curveness: 0.45,
            width: 2
          }
        } else if (link.name === '需求目录') {
          link.lineStyle = {
            color: color2,
            curveness: 0.45,
            width: 2
          }
        }
      })
      const categories = [
        {
          name: '应用系统',
          symbol: 'roundRect',
          itemStyle: {
            color: color1
          }
        },
        {
          name: '权责清单',
          symbol: 'roundRect',
          itemStyle: {
            color: color2
          }
        },

        {
          name: '资源目录',
          symbol: 'circle',
          itemStyle: {
            color: color3
          }
        },
        {
          name: '需求目录',
          symbol: 'circle',
          itemStyle: {
            color: color4
          }
        }
      ]
      inOrOutFlowChart.setOption({
        legend: [
          {
            // selectedMode: 'single',
            data: categories.map(x => x.name)
            // icon: 'circle'
          }
        ],
        tooltip: {
          formatter(x) {
            return x.data.name
          }
        },
        series: [
          {
            type: 'graph',
            layout: 'force',
            draggable: true,
            roam: false,
            focusNodeAdjacency: true,
            categories: categories,
            edgeSymbol: ['none', 'arrow'],
            edgeSymbolSize: 12,
            edgeLabel: {
              normal: {
                show: false
              }
            },
            label: {
              show: true
            },
            force: {
              repulsion: 1800,
              edgeLength: [50, 150]
            },
            data: this.data.nodes,
            links: this.data.links
          }
        ]
      })
      inOrOutFlowChart.on('click', params => {
        switch (params.data.category) {
          case 0:
            getOSDetail({ appId: params.data.appId }).then(res1 => {
              if (res1.data.errno === 0) {
                this.addbusinessForm = Object.assign(
                  {},
                  this.addbusinessForm,
                  res1.data.data
                )
                this.selectOrgs = []
                setTimeout(() => {
                  this.addbusinessForm.belongTo
                    ? this.selectOrgs.push(this.addbusinessForm.belongTo)
                    : (this.selectOrgs = [])
                  this.addbusinessForm.departmentName = this.$route.query.pidDeptName
                  this.yyxtDialog = true
                }, 20)
              }
            })
            break
          case 1:
            getBusinessDetail({ busiId: params.data.appId }).then(res => {
              if (res.data.errno === 0) {
                this.addbusinessForm1 = Object.assign({}, res.data.data)
                this.ywsxDialog = true
              }
            })
            break
          case 2:
            this.ziyuanmulu = params.data.appId
            this.delDialog = true
            break
          case 3:
            this.xuqiumulu = params.data.appId
            this.xuquDialog = true
            break
        }
      })
    }
  }
}
</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
.el-dialog__header {
  text-align: center;
}
</style>
