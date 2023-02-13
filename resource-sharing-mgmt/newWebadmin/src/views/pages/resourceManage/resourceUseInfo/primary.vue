<template>
  <el-main class="main">
    <el-col :span="24">
      <el-form :inline="true" class="el-InputForm">

        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入资源名称" v-model="SearchItem.resourceName">
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-input clearable size="medium" placeholder="请输入信息资源名称" v-model="SearchItem.uviewNm">
          </el-input>
        </el-form-item>

        <el-form-item>
          <el-select clearable v-model="SearchItem.type" placeholder="请选择资源类型">
            <el-option label="云接口" value="1"></el-option>
            <el-option label="云文件" value="2"></el-option>
            <el-option label="云数据" value="3"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-cascader size="medium" placeholder="请选择申请单位名称" :props="props" collapse-tags clearable v-model="SearchItem.createDeptName" :options="organizationOptions" />
        </el-form-item>

        <el-form-item style="margin-left: 1%">
          <el-button size="medium" @click="SearchNoteList" icon="el-icon-search">查询</el-button>
          <el-button size="medium" @click="reset" icon="el-icon-refresh-left">重置</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <el-col :span="24" class="center">
      <TableList :table-data="tableData" v-loading="isSubmitLoading" :table-selection="tableSelection" :table-label="tableHeader" :table-option="tableOpction">
      </TableList>
    </el-col>
    <el-col :span="24">
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
    </el-col>
    <el-dialog title="审核" :visible.sync="dialogVisible" width="30%">
      <div>
        <el-radio-group v-model="status">
          <el-radio label="1">通 过</el-radio>
          <el-radio label="2">驳 回</el-radio>
        </el-radio-group>
      </div>
      <el-form v-if="status==='2'" :model="form" ref="formRejectDetail" label-width="90px" style="margin-top: 30px;">
        <el-form-item prop="rejectDetail" label="驳回原因" :rules="[{ required: true, message: '请输入驳回原因', trigger: 'blur' },]">
          <el-input type="textarea" v-model="form.rejectDetail"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitFun">确 定</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>
<script>
import { resourceUseInfoFindAll, resourceUseInfoAudit } from "@/api/resourceUseInfo.js";
import { organizationFindAll } from "@/api/organization.js";
import TableList from "@/components/table/tableList";
import Pagination from "@/components/table/Pagination";
export default {
  components: { TableList, Pagination },
  data() {
    return {
      dialogVisible: false,
      status: "1",
      form: { rejectDetail: "" },
      props: { checkStrictly: true },
      organizationOptions: [],
      tableSelection: {
        key: true,
        type: "",
        detaile: false,
      },
      isSubmitLoading: false,
      DeletelistiD: [],
      listiRead: [],
      tableData: [],
      tableHeader: [
        {
          type: "html",
          label: "资源名称",
          list: "resourceName",
          code: (row) => {
            return "<span>" + row.resourceName + "</span>";
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
            if (row.provOrgName === null) {
              return "";
            } else {
              return "<span>" + row.provOrgName + "</span>";
            }
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
          label: "提交日期",
          list: "createTime",
          code: (row) => {
            return "<span>" + row.createTime.split(' ')[0] + "</span>";
          },
        },
        {
          type: "html",
          label: "状态",
          list: "status",
          code: (row) => {
            switch (row.status) {
              case "0":
                return "<span>待审核</span>";
                break;
              case "1":
                return "<span>审核通过待实施</span>";
                break;
              case "2":
                return "<span>已实施</span>";
                break;
              case "3":
                return "<span>已驳回</span>";
                break;
              default:
                return "<span>审核失败</span>";
                break;
            }
          },
        },
      ],
      tableOpction: {
        label: "操作",
        width: "200px",
        value: 0,
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
          {
            label: "审核",
            key: 0,
            type: "text",
            icon: "el-icon-tickets",
            State: false,
            method: (row) => {
              this.handleUpdate(row);
            },
          },
        ],
      },
      time: [],
      SearchItem: {
        uviewNm: "",
        uviewNo: "",
        sourceApiName: "",
        status: "0",
        createDeptName: "",
        // provOrgId: JSON.parse(sessionStorage.getItem('UserInfo')).organizations[0],
        resourceName: "",
      },
      lastItem: {
        uviewNm: "",
        uviewNo: "",
        sourceApiName: "",
        status: "0",
        createDeptName: "",
        sourceApiDesc: "",
        uviewApiId: "",
        resourceName: "",
      },
      total: 0,
      pageSize: "20",
      currentPage: "1",
      toId: "",
    };
  },
  created() {
    this.fetchData();
    this.findorganizations()
  },
  methods: {
    // 重置
    reset() {
      const that = this;
      that.SearchItem.uviewNm = ""
      that.SearchItem.uviewNo = ""
      that.SearchItem.createDeptName = ""
      that.SearchItem.sourceApiName = ""
      that.SearchItem.resourceName = ""
      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
    },
    // 获取列表
    async fetchData(type) {
      const that = this;
      let data = {};
      data = this.SearchItem
      data.page = this.currentPage
      data.size = this.pageSize

      if (typeof data.provOrgName !== 'string' && typeof data.provOrgName !== 'undefined') {
        data.provOrgName = data.provOrgName[data.provOrgName.length - 1]
      }
      if (typeof data.createDeptName !== 'string' && typeof data.createDeptName !== 'undefined') {
        data.createDeptName = data.createDeptName[data.createDeptName.length - 1]
      }

      Object.entries(that.SearchItem).map((item, index) => {
        that.lastItem[item[0]] = that.SearchItem[item[0]];
      });
      try {
        that.isSubmitLoading = true;
        const res = await resourceUseInfoFindAll(data);
        that.isSubmitLoading = false;
        if (res.data.code === 1) {
          this.tableData = res.data.data.content;
          this.total = res.data.data.totalElements;
        } else {
          this.$message.error(res.data.msg);
        }
      } catch (even) {
        this.$message.error(even.msg);
      }
    },
    // 搜索
    SearchNoteList() {
      this.currentPage = 1;
      this.$refs.page.Page(1);
      this.fetchData();
    },
    // 翻页
    pageChange(item) {
      let that = this;
      this.pageSize = item.limit;
      this.currentPage = item.page;
      Object.entries(that.SearchItem).map((item, index) => {
        that.SearchItem[item[0]] = that.lastItem[item[0]];
      });
      this.fetchData("page");
    },
    // 详情
    handleDetail(data) {
      if (data.type === '1') {
        this.$router.push({
          path: "/assetApiExDetail",
          query: {
            id: data.id,
          },
        });
      } else {
        this.$router.push({
          path: "/resourceUseInfoDetail",
          query: {
            id: data.id,
          },
        });
      }
    },
    //审核
    handleUpdate(data) {
      this.toId = data.id;
      this.dialogVisible = true;
    },
    submitFun() {
      if (this.status === '1') {
        this.$confirm('请确认是否审核通过！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          resourceUseInfoAudit({ status: this.status, id: this.toId }).then(res => {
            if (res.data.code === 1) {
              this.$message.success("审核通过成功");
              this.dialogVisible = false;
              this.fetchData();
            }
          })
        }).catch(() => { });
      } else {
        this.$refs['formRejectDetail'].validate((valid) => {
          if (valid) {
            this.$confirm('请确认是否审核驳回！', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              resourceUseInfoAudit({ rejectDetail: this.form.rejectDetail, status: this.status, id: this.toId }).then(res => {
                if (res.data.code === 1) {
                  this.$message.success("审核驳回成功");
                  this.dialogVisible = false;
                  this.fetchData();
                  this.form.rejectDetail = "";
                }
              })
            }).catch(() => {
              this.form.rejectDetail = "";
            });
          } else {
            return false;
          }
        });

      }
    },
    async findorganizations(data) {
      const that = this
      that.loading = true
      const response = await organizationFindAll()
      that.loading = false
      if (response.data.code === 1) {
        let arrData = []
        arrData.push(response.data.data)
        that.organizationOptions = this.getJsonTree(arrData, '')
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg)
        return false
      }
    },
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
    }
  },
};
</script>
