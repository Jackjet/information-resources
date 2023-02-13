<template>
  <div class="agency-page" v-if="listData.length">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane v-for="(item,index) in listData" :label="item.remarkName" :name="index.toString()" :key="index"></el-tab-pane>
    </el-tabs>
    <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
    </TableList>
    <div style="text-align: right;">
      <el-button @click="herfClick">更 多</el-button>
    </div>
  </div>
</template>
<script>
import TableList from "com/tableList";
export default {
  components: {
    TableList
  },
  data() {
    return {
      activeName: '0',
      herfUrl: "",
      listData: [],
      tableSelection: {
        key: true,
        type: "",
        detaile: false,
      },
      isSubmitLoading: false,
      tableData: [],
      tableHeader: [],
      tableOpction: {
        label: "操作",
        width: "200px",
        value: 1,
        options: [
          {
            label: "详情",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            method: (row) => {
              this.handleDetail(row);
            },
          },
        ],
      },
    };
  },
  created() {
    // 列表数据
    this.chargeFindAll().then(res => {
      if (res.code === 1) {
        this.listData = res.data;
        this.tableHeadeType(res.data[0].remarkName);
        this.tableData = res.data[0].data ? res.data[0].data.content : [];
        this.herfUrl = res.data[0].path ? res.data[0].path : '#';
      }
    })
    // 目录管理初审
    // this.fetchData_1();
    // // 目录管理终审
    // this.fetchData_2();
    // // 资源挂接审核
    // this.fetchData_3();
    // // 资源使用审核
    // this.fetchData_4();
    // // 部门需求审核
    // this.fetchData_5();
    // // 目录管理办理
    // this.fetchData_6();
    // // 资源挂接办理
    // this.fetchData_7();
  },
  methods: {
    handleClick() {
      this.tableHeadeType(this.listData[this.activeName * 1].remarkName);
      this.tableData = this.listData[this.activeName * 1].data ? this.listData[this.activeName * 1].data.content : [];
      this.herfUrl = this.listData[this.activeName * 1].path ? this.listData[this.activeName * 1].path : '#';
    },
    tableHeadeType(type) {
      // 判断显示对应表头
      switch (type) {
        case '目录管理初审': this.fetchData_1(); break;
        case '目录管理终审': this.fetchData_2(); break;
        case '资源挂接审核': this.fetchData_3(); break;
        case '资源申请初审': this.fetchData_4(); break;
        case '资源申请终审': this.fetchData_4(); break;
        case '部门需求审核': this.fetchData_5(); break;
        case '目录管理办理': this.fetchData_6(); break;
        case '资源挂接办理': this.fetchData_7(); break;
        case '目录删除审核': this.fetchData_8(); break;
        // default :
      }
    },
    herfClick() {
      window.open(this.herfUrl);
    },
    handleDetail(data) {
      console.log(data)
    },
    fetchData_1() {
      this.tableHeader = [
        {
          type: "html",
          label: "信息资源代码",
          list: "uviewNo",
          code: (row) => {
            return "<span>" + row.uviewNo + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "provOrgName",
          code: (row) => {
            return "<span>" + row.provOrgName + "</span>";
          },
        },
        {
          type: "html",
          label: "提交审核时间",
          list: "crtDt",
          code: (row) => {
            return "<span>" + row.crtDt.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "审核状态",
          list: "auditStatus",
          code: (row) => {
            if (row.auditStatus == 0) {
              return "<span>草稿</span>";
            } else if (row.auditStatus == 1) {
              return "<span>待初审</span>";
            } else if (row.auditStatus == 2) {
              return "<span>初审未通过</span>";
            } else if (row.auditStatus == 3) {
              return "<span>初审通过</span>";
            } else if (row.auditStatus == 4) {
              return "<span>终审未通过</span>";
            } else if (row.auditStatus == 5) {
              return "<span>已发布</span>";
            }
          },
        },
      ]
    },
    fetchData_2() {
      this.tableHeader = [
        {
          type: "html",
          label: "信息资源代码",
          list: "uviewNo",
          code: (row) => {
            return "<span>" + row.uviewNo + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "provOrgName",
          code: (row) => {
            return "<span>" + row.provOrgName + "</span>";
          },
        },
        {
          type: "html",
          label: "提交审核时间",
          list: "crtDt",
          code: (row) => {
            return "<span>" + row.crtDt.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "审核状态",
          list: "status",
          code: (row) => {
            if (row.auditStatus == 0) {
              return "<span>草稿</span>";
            } else if (row.auditStatus == 1) {
              return "<span>待初审</span>";
            } else if (row.auditStatus == 2) {
              return "<span>初审未通过</span>";
            } else if (row.auditStatus == 3) {
              return "<span>初审通过</span>";
            } else if (row.auditStatus == 4) {
              return "<span>终审未通过</span>";
            } else if (row.auditStatus == 5) {
              return "<span>已发布</span>";
            }
          },
        },
      ]
    },
    fetchData_3() {
      this.tableHeader = [
        {
          type: "html",
          label: "信息资源代码",
          list: "uviewNo",
          code: (row) => {
            return "<span>" + row.uviewNo + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "资源类型",
          list: "type",
          code: (row) => {
            if (row.type == 0) {
              return "<span>云文件</span>";
            } else if (row.type == 1) {
              return "<span>云数据</span>";
            }
          },
        },
        {
          type: "html",
          label: "资源名称",
          list: "name",
          code: (row) => {
            if (row.type === 0) {
              return "<span>" + row.name + "</span>";
            } else {
              return "<span>" + row.tableName + "</span>";
            }

          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            if (row.status == 0) {
              return "<span>草稿</span>";
            } else if (row.status == 1) {
              return "<span>待审核</span>";
            } else if (row.status == 2) {
              return "<span>审核通过</span>";
            } else if (row.status == 3) {
              return "<span>审核驳回</span>";
            }
          },
        },
      ]
    },
    fetchData_4() {
      this.tableHeader = [
        {
          type: "html",
          label: "云资源名称",
          list: "sourceApiName",
          code: (row) => {
            return "<span>" + row.resourceName + "</span>";
            // if (row.type == 1) {
            //   return "<span>" + row.sourceApiName + "</span>";
            // } else if (row.type == 2) {
            //   return "<span>" + row.fileName + "</span>";
            // } else if (row.type == 2) {
            //   return "<span>" + row.tableName + "</span>";
            // }
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "申请单位",
          list: "createDeptName",
          code: (row) => {
            return "<span>" + row.createDeptName + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "provOrgName",
          code: (row) => {
            return "<span>" + row.provOrgName + "</span>";
          },
        },
        {
          type: "html",
          label: "提交日期",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "资源类型",
          list: "type",
          code: (row) => {
            if (row.type == 1) {
              return "<span>云接口</span>";
            } else if (row.type == 2) {
              return "<span>云文件</span>";
            } else if (row.type == 3) {
              return "<span>云数据</span>";
            }
          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            if (row.status == 0) {
              return "<span>审核中</span>";
            } else if (row.status == 1) {
              return "<span>审核通过</span>";
            } else if (row.status == 2) {
              return "<span>审核驳回</span>";
            } else if (row.status == 3) {
              return "<span>审核失败</span>";
            }
          },
        },
      ]
    },
    fetchData_5() {
      this.tableHeader = [
        {
          type: "html",
          label: "提出日期",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "需求标题",
          list: "title",
          code: (row) => {
            return "<span>" + row.title + "</span>";
          },
        },
        {
          type: "html",
          label: "受理部门",
          list: "acceptDept",
          code: (row) => {
            return "<span>" + row.acceptDept + "</span>";
          },
        },
        {
          type: "html",
          label: "受理需求",
          list: "requestType",
          code: (row) => {
            if (row.requestType == 0) {
              return "<span>资源目录变更</span>";
            } else if (row.requestType == 1) {
              return "<span>资源目录新增</span>";
            } else if (row.requestType == 2) {
              return "<span>资源数据变更</span>";
            } else if (row.requestType == 3) {
              return "<span>资源数据新增</span>";
            }
          },
        },
        {
          type: "html",
          label: "提出部门",
          list: "createDeptName",
          code: (row) => {
            return "<span>" + row.createDeptName + "</span>";
          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            if (row.status == 0) {
              return "<span>未受理</span>";
            } else if (row.status == 1) {
              return "<span>已受理</span>";
            } else if (row.status == 2) {
              return "<span>已驳回</span>";
            }
          },
        },
      ]
    },
    fetchData_6() {
      this.tableHeader = [
        {
          type: "html",
          label: "信息资源代码",
          list: "uviewNo",
          code: (row) => {
            return "<span>" + row.uviewNo + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "provOrgName",
          code: (row) => {
            return "<span>" + row.provOrgName + "</span>";
          },
        },
        {
          type: "html",
          label: "注册时间",
          list: "crtDt",
          code: (row) => {
            return "<span>" + row.crtDt.split(' ')[0] + "</span>";
          },
        },
        // {
        //   type: "html",
        //   label: "版本",
        //   list: "createTime",
        //   code: (row) => {
        //     return "<span>" + row.createTime + "</span>";
        //   },
        // },
        {
          type: "html",
          label: "状态",
          list: "auditStatus",
          code: (row) => {
            if (row.auditStatus == 0) {
              return "<span>草稿</span>";
            } else if (row.auditStatus == 1) {
              return "<span>待初审</span>";
            } else if (row.auditStatus == 2) {
              return "<span>初审未通过</span>";
            } else if (row.auditStatus == 3) {
              return "<span>初审通过</span>";
            } else if (row.auditStatus == 4) {
              return "<span>终审未通过</span>";
            } else if (row.auditStatus == 5) {
              return "<span>已发布</span>";
            }
          },
        },
      ]
    },
    fetchData_7() {
      this.tableHeader = [
        {
          type: "html",
          label: "信息资源代码",
          list: "uviewNo",
          code: (row) => {
            return "<span>" + row.uviewNo + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uviewNm",
          code: (row) => {
            return "<span>" + row.uviewNm + "</span>";
          },
        },
        {
          type: "html",
          label: "同步时间",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "共享条件",
          list: "shareCondition",
          code: (row) => {
            if (!row.shareCondition) {
              return "<span>无</span>";
            } else {
              return "<span>" + row.shareCondition + "</span>";
            }

          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            // return row.hookStatus === '1' || row.dataHookStatus === '1' || row.fileHookStatus === '1' ? '<span>已挂接</span>' : '<span>未挂接</span>';
            if (row.status == 0) {
              return "<span>草稿</span>";
            } else if (row.status == 1) {
              return "<span>待审核</span>";
            } else if (row.status == 2) {
              return "<span>审核通过</span>";
            } else if (row.status == 3) {
              return "<span>审核驳回</span>";
            }
          },
        },
      ]
    },
    fetchData_8() {
      this.tableHeader = [
        {
          type: "html",
          label: "信息资源代码",
          list: "uview_no",
          code: (row) => {
            return "<span>" + row.uview_no + "</span>";
          },
        },
        {
          type: "html",
          label: "信息资源名称",
          list: "uview_nm",
          code: (row) => {
            return "<span>" + row.uview_nm + "</span>";
          },
        },
        {
          type: "html",
          label: "提供单位",
          list: "org_nm",
          code: (row) => {
            return "<span>" + row.org_nm + "</span>";
          },
        },
        {
          type: "html",
          label: "提交审核时间",
          list: "check_time",
          code: (row) => {
            return "<span>" + row.check_time.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "审核状态",
          list: "audit_status",
          code: () => {
            return "<span>待删除</span>";
            // if (row.auditStatus === '') {
            //   return "<span>待删除</span>";
            // } else if (row.auditStatus === '0') {
            //   return "<span>删除驳回</span>";
            // } else if (row.auditStatus === '1') {
            //   return "<span>已删除</span>";
            // }
          },
        },
      ]
    },
  },
};
</script>
<style lang="scss" scope>
.agency-page {
  padding: 20px;
  box-sizing: border-box;
}
.el-tabs__item {
  font-size: 16px;
}
</style>