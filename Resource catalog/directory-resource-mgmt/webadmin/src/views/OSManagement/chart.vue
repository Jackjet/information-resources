<template>
  <div class="mt20 department_content">
    <el-row style="height: 100%">
      <el-col ref="contentDomParents" :span="24" class="tab_area">
        <div class="type_area">资源关系图</div>
        <div class="operate_area">
          <force-chart :data="chartData" />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getDiagram } from '@/api/OSManagement'
import mixinJs from '@/utils/mixin'
import ForceChart from '@/components/ForceChart'
import { getDictByType } from '@/api/departmentRes'
import Hamburger from '@/components/Hamburger'
export default {
  name: 'OSManagementChart',
  components: { ForceChart },
  mixins: [mixinJs],
  props: {
    id: {
      type: [String, Number]
    }
  },
  data() {
    return {
      chartData: {}
    }
  },
  created() {
    this.$store.dispatch('setReBackOSId', this.$route.query.deptId)
    this.getData()
  },
  methods: {
    getData() {
      getDiagram({ appId: this.$route.params.id })
        .then(res => {
          if (res.data.errno === 0) {
            this.chartData = res.data.data
          }
        })
        .catch(err => {
          this.$message.error(err.data.errmsg || '出错了')
        })
    }
  }
}
</script>

<style scoped>
.tree_area .el-tree {
  width: 100%;
}
.overflowEllips {
  display: inline-block;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.el-tag .el-icon-close {
}
.product_resource_btn {
  display: inline-block;
  height: 20px;
  line-height: 20px;
  color: #1f3365;
  padding: 0px 5px;
  border: 1px solid rgba(31, 51, 101, 1);
  border-radius: 5px;
  cursor: pointer;
  margin-right: 14px;
  font-size: 12px;
}
.need_resource_btn {
  font-size: 12px;
  display: inline-block;
  height: 20px;
  line-height: 20px;
  border: 1px solid #5abd8c;
  color: #5abd8c;
  padding: 0px 5px;
  border-radius: 5px;
  cursor: pointer;
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
}
.filter_text {
  line-height: 36px;
  margin-right: 6px;
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}
.tree_title {
  padding-left: 30px;
  padding-right: 1vh;
  height: 60px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  line-height: 60px;
  opacity: 1;
}
.filter_input {
  opacity: 1;
  margin-bottom: 25px;
}
.filter_input > input {
  background: rgba(255, 255, 255, 1);
  border: 1px solid rgba(203, 203, 203, 1);
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

.tree_area {
  background: #f9f9f9;
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}

.tab_area {
  height: 100%;
}
.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}
.operate_area {
  padding: 0 45px;
}
.related_btns {
  margin-top: 15px;
}
.tr_detail_icon {
  color: #5abd8c;
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
  margin-right: 14px;
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
.level {
  width: 16px;
  display: inline-block;
  height: 16px;
  background: #1f3365;
  color: #fff;
  font-size: 14px;
  text-align: center;
  line-height: 16px;
  border-radius: 4px;
  margin-right: 4px;
}
</style>

