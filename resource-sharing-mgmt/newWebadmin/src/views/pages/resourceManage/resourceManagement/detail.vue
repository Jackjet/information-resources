<template>
  <el-main class="main">
    <div>
      <h4>{{title}}</h4>
    </div>
    <el-col class="main-center">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="150px" class="demo-ruleForm">
        <el-form-item label="信息资源名称:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.uviewNm"></el-input>
        </el-form-item>
        <el-form-item label="信息资源代码:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.uviewNo"></el-input>
        </el-form-item>
        <el-form-item label="信息资源提供单位:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.name"></el-input>
        </el-form-item>
        <el-form-item label="统一社会信用代码:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.provOrgCode"></el-input>
        </el-form-item>

        <el-form-item label="信息资源格式分类:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="mediaFmtName"></el-input>
        </el-form-item>
        <el-form-item label="信息资源格式类型:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="mediaFmtTypeName"></el-input>
        </el-form-item>
        <el-form-item label="更新周期:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="updateCyc"></el-input>
        </el-form-item>
        <el-form-item label="发布日期:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="pubDt"></el-input>
        </el-form-item>
        <el-form-item label="信息资源摘要:" :label-width="this.formLabelWidth">
          <el-input :rows='4' type="textarea" clearable size="medium" :disabled="true" v-model="ruleForm.uviewDesc"></el-input>
        </el-form-item>
        <el-form-item label="共享类型:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="shareLv"></el-input>
        </el-form-item>
        <el-form-item label="共享条件:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.shareCondition"></el-input>
        </el-form-item>
        <el-form-item label="开放类型:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="pubSts"></el-input>
        </el-form-item>
        <el-form-item label="开放条件:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.pubCondition"></el-input>
        </el-form-item>
        <el-form-item label="版本:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.version"></el-input>
        </el-form-item>
        <el-form-item label="挂接状态:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="hookStatus"></el-input>
        </el-form-item>
        <el-form-item label="下架状态:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="status"></el-input>
        </el-form-item>
        <el-form-item label="状态:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="auditStatus"></el-input>
        </el-form-item>
        <el-form-item label="访问量:" :label-width="this.formLabelWidth">
          <el-input clearable size="medium" :disabled="true" v-model="ruleForm.visitsCount"></el-input>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data='tableData' v-loading="isSubmitLoading" :table-label="tableHeader" :table-option="tableOpction" :table-selection="tableSelection">
      </TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <div class="demo-drawer__footer drawer_footer">
      <el-button size="medium" @click="goBack">返回列表</el-button>
    </div>
  </el-main>

</template>

<script>
import { archBusiUviewExFind, assetDictFindAll } from "@/api/archBusiUviewEx.js";
import { archBusiUviewStrExFind } from "@/api/archBusiUviewStrEx.js"
import TableList from '@/components/table/tableList'
import Pagination from '@/components/table/Pagination'
export default {
  components: { TableList, Pagination },
  data() {
    return {
      title: '详情',
      ruleForm: {
        uviewNm: '',
        uviewNo: "",
        belongTo: '',
        belongToCode: '',
        provOrgId: '',
        provOrgCode: '',
        mediaFmt: '',
        mediaFmtType: '',
        updateCyc: '',
        pubDt: '',
        uviewDesc: '',
        shareCondition: '',
        pubSts: '',
        pubCondition: '',
        version: '',
        visitsCount: ''
      },
      formLabelWidth: "180px",
      total: 0,
      pageSize: "20",
      currentPage: "1",
      tableSelection: {
        key: true,
        type: '',
        detaile: false
      },
      isSubmitLoading: false,
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: 'html',
          label: '信息项名称',
          list: 'srcField',
          code: (row) => {
            return '<span>' + row.srcField + '</span>'
          }
        },
        {
          type: 'html',
          label: '信息项英文标识',
          list: 'engCd',
          code: (row) => {
            return '<span>' + row.engCd + '</span>'
          }
        },
        {
          type: 'html',
          label: '信息项数据类型',
          list: 'srcDataTyp',
          code: (row) => {
            return '<span>' + row.srcDataTyp + '</span>'
          }
        },
        {
          type: 'html',
          label: '信息项数据长度',
          list: 'dataLen',
          code: (row) => {
            return '<span>' + row.dataLen + '</span>'
          }
        },
        {
          type: 'html',
          label: '信息项描述',
          list: 'itemRemark',
          code: (row) => {
            return '<span>' + row.itemRemark + '</span>'
          }
        },
        {
          type: 'html',
          label: '显示序号',
          list: 'sno',
          code: (row) => {
            return '<span>' + row.sno + '</span>'
          }
        }
      ],
      tableOpction: {
        label: '操作',
        width: '200px',
        value: 'sadasfa',
        options: [{}]
      },
      lastItem: {
        srcField: '',
        engCd: '',
        srcDataTyp: '',
        dataLen: '',
        itemRemark: '',
        sno: ''
      },
      hookStatus: '',
      status: '',
      auditStatus: '',
      pubSts: '',
      shareLv: '',
      updateCyc: '',
      pubDt: '',
      mediaFmtName: '',
      mediaFmtTypeName: ''
    }
  },
  created() {
    this.archBusiUviewExDetail();
  },
  methods: {
    // 详情
    async archBusiUviewExDetail() {
      const that = this
      let data = { id: this.$route.query.id }
      that.loading = true
      const response = await archBusiUviewExFind(data)
      that.loading = false
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
        that.ruleForm.shareCondition = response.data.data.shareCondition ? response.data.data.shareCondition : '无';
        that.ruleForm.pubCondition = response.data.data.pubCondition ? response.data.data.pubCondition : '无';
        this.pubDt = response.data.data.pubDt.split(' ')[0];

        assetDictFindAll({ type: 'format_type' }).then(res => {
          res.data.forEach(item => {
            if (response.data.data.mediaFmt === item.value) {
              this.mediaFmtName = item.name;
            }
          })
        })
        assetDictFindAll().then(res => {
          res.data.forEach(item => {
            if (response.data.data.mediaFmtType === item.value) {
              this.mediaFmtTypeName = item.name;
            }
          })
        })

        if (response.data.data.hookStatus === '1' || response.data.data.fileHookStatus === '1' || response.data.data.dataHookStatus === '1') {
          this.hookStatus = "已挂接";
        } else {
          this.hookStatus = "未挂接";
        }
        // switch (response.data.data.hookStatus) {
        //   case "0":
        //     this.hookStatus = "未挂接";
        //     break;
        //   case "1":
        //     this.hookStatus = "已挂接";
        //     break;
        //   default:
        //     this.hookStatus = "无";
        //     break;
        // }

        switch (response.data.data.status) {
          case "0":
            this.status = "未下架";
            break;
          case "1":
            this.status = "已下架";
            break;
          default:
            this.status = "无";
            break;
        }

        assetDictFindAll({ type: 'is_open' }).then(res => {
          res.data.forEach(item => {
            if (response.data.data.pubSts === item.value) {
              this.pubSts = item.name;
            }
          })
        })

        assetDictFindAll({ type: 'share_type' }).then(res => {
          res.data.forEach(item => {
            if (response.data.data.shareLv === item.value) {
              this.shareLv = item.name;
            }
          })
        })

        assetDictFindAll({ type: 'audit_status' }).then(res => {
          res.data.forEach(item => {
            if (response.data.data.auditStatus === item.value) {
              this.auditStatus = item.name;
            }
          })
        })

        assetDictFindAll({ type: 'update_cycle' }).then(res => {
          res.data.forEach(item => {
            if (response.data.data.updateCyc === item.value) {
              this.updateCyc = item.name;
            }
          })
        })
        this.archBusiUviewStrExData(response.data.data.uviewId);

      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
    goBack() {
      // this.$router.push('/resourceManagement')
      this.$router.go(-1);
    },
    // 获取列表
    async archBusiUviewStrExData(dataUviewId) {
      const that = this
      let data = {}
      data.page = this.currentPage,
        data.size = this.pageSize
      data.uviewId = dataUviewId
      // Object.entries(that.SearchItem).map((item, index) => {
      //     that.lastItem[item[0]] = that.SearchItem[item[0]]
      // })
      try {
        this.isSubmitLoading = true
        const res = await archBusiUviewStrExFind(data)
        this.isSubmitLoading = false;
        if (res.data.code === 1) {
          // this.tableData = res.data.data;
          this.tableData = res.data.data.content
          this.total = res.data.data.totalElements
        } else {
          this.$message.error(res.data.msg)
          return false
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 翻页
    pageChange(item) {
      let that = this
      this.pageSize = item.limit
      this.currentPage = item.page
      // Object.entries(that.SearchItem).map((item, index) => {
      //     that.SearchItem[item[0]] = that.lastItem[item[0]]
      // })
      this.archBusiUviewStrExData('page')
    }
  }
}
</script>
<style lang="scss" scoped>
.main {
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;
  .main-center {
    padding: 20px;
    border-radius: 5px;
    width: 65%;
    margin: 0 auto;
  }
}
.demo-drawer__footer {
  margin-top: 80px;
  text-align: center;
}
</style>