<template>
  <el-main class="main">
    <div>
      <h4>{{ title }}</h4>
    </div>
    <el-col class="main-center">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm"
        v-loading="loading"
      >
        <el-form-item label :label-width="this.formLabelWidth">
          <span style="color:#8080FF;font-size:18px">任务基本信息</span>
        </el-form-item>
        <el-form-item label="任务名称:" :label-width="this.formLabelWidth" prop="name">
          <el-input
            clearable
            style="width:370px"
            maxlength="100"
            size="medium"
            :disabled="disabled"
            placeholder="请输入任务名称"
            v-model="ruleForm.name"
          />
        </el-form-item>
        <el-form-item label="执行周期:" :label-width="this.formLabelWidth" prop="cycle">
          <el-radio-group v-model="ruleForm.cycle" @change="radioChange()">
            <el-radio label="0">执行一次</el-radio>
            <el-radio label="1">每日</el-radio>
            <el-radio label="2">每周</el-radio>
            <el-radio label="3">每月</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item
          label="执行时间:"
          :label-width="this.formLabelWidth"
          prop="executionTime"
          v-if="ruleForm.cycle == '0'"
        >
          <el-date-picker
            v-model="ruleForm.executionTime"
            style="width:370px"
            
            value-format='yyyy-MM-dd HH:mm:ss'
            type="datetime"
            placeholder="请选择执行时间"
          ></el-date-picker>
        </el-form-item>
        <el-form-item
          label="执行时间:"
          :label-width="this.formLabelWidth"
          prop="executionTime"
          v-if="ruleForm.cycle == '1'"
        >
          <el-time-picker
            v-model="ruleForm.executionTime"
            value-format='HH:mm:ss'
            style="width:370px"
            placeholder="请选择执行时间"
          ></el-time-picker>
        </el-form-item>
        <el-form-item label="执行时间:" :label-width="this.formLabelWidth" v-if="ruleForm.cycle == '2'">
          <el-col :span="4">
            <el-form-item prop="executionWeek">
              <el-select v-model="ruleForm.executionWeek" placeholder="请选择执行周">
                <el-option
                  v-for="item in options1"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item prop="executionTime">
              <el-time-picker value-format='HH:mm:ss' v-model="ruleForm.executionTime" placeholder="请选择执行时间"></el-time-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="执行时间:" :label-width="this.formLabelWidth" v-if="ruleForm.cycle == '3'">
          <el-col :span="4">
            <el-form-item prop="executionDay">
              <el-date-picker
                v-model="ruleForm.executionDay"
                type="date"
                format="d"
                value-format='d'
                @focus="getBlur()"
                placeholder="请选择执行日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-form-item prop="executionTime">
              <el-time-picker value-format='HH:mm:ss' v-model="ruleForm.executionTime"  placeholder="请选择执行时间"></el-time-picker>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item label="报告描述:" :label-width="this.formLabelWidth">
          <el-input
            maxlength="200"
            style="width:370px"
            type="textarea"
            :rows="3"
            placeholder="请输入报告描述"
            clearable
            size="medium"
            :disabled="disabled"
            v-model="ruleForm.description"
          ></el-input>
        </el-form-item>
        <el-form-item label :label-width="this.formLabelWidth">
          <span style="color:#8080FF;font-size:18px">质量检查规则</span>
        </el-form-item>
        <el-form-item label :label-width="this.formLabelWidth">
          <el-button size="medium" @click="chooseRule">选择规则</el-button>
        </el-form-item>
        <el-table v-if="tableData1.length>0" v-loading="loading1" :data="tableData1" border style="width: 100%">
          <el-table-column label="规则名称"  prop="name"></el-table-column>
          <el-table-column label="数据模板" prop="ruleTemplateName" width="120"></el-table-column>
          <el-table-column label="规则数据源" show-overflow-tooltip>
              <template slot-scope="scope">
                {{JSON.parse(scope.row.metadataData).sourceName}}
            </template>
          </el-table-column>
          <el-table-column label="规则实体表" show-overflow-tooltip>
              <template slot-scope="scope">
                  {{JSON.parse(scope.row.metadataData).source.name}}
              </template>
          </el-table-column>
          <el-table-column width="120px" show-overflow-tooltip>
              <template slot-scope="scope">
                <el-button type="danger" @click="deleteRule(scope.row)" class="el-icon-delete">删除</el-button>
              </template>
          </el-table-column>
        </el-table>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button
            size="medium"
            type="primary"
            v-if="!disabled"
            @click="submitForm('ruleForm')"
          >保 存</el-button>
        </div>
      </el-form>
    </el-col>
    <el-dialog title :visible.sync="dialogTableVisible" width="800px">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        height="500"
        style="width: 100%"
        :row-key="getRowKeys"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" :reserve-selection="true" width="55"></el-table-column>
        <el-table-column label="规则名称"  prop="name"></el-table-column>
        <el-table-column prop="ruleTemplateName" label="数据模板" width="120"></el-table-column>
        <el-table-column label="规则数据源" show-overflow-tooltip>
            <template slot-scope="scope">
                {{JSON.parse(scope.row.metadataData).sourceName}}
            </template>
        </el-table-column>
        <el-table-column label="规则实体表" show-overflow-tooltip>
            <template slot-scope="scope">
                {{JSON.parse(scope.row.metadataData).source.name}}
            </template>
        </el-table-column>
      </el-table>
      <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleOk">确 定</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>
<script>
import {qualityFindAll,taskInsert,taskFindById,taskUpdate} from '@/api/task.js'
import Pagination from '@/components/table/Pagination'

export default {
  components: {Pagination},
  data() {
    return {
      title: "新增",
      rule: "完整性非空约束",
      options1: [
        {
          value: "1",
          label: "星期一"
        },
        {
          value: "2",
          label: "星期二"
        },
        {
          value: "3",
          label: "星期三"
        },
        {
          value: "4",
          label: "星期四"
        },
        {
          value: "5",
          label: "星期五"
        },
        {
          value: "6",
          label: "星期六"
        },
        {
          value: "7",
          label: "星期七"
        }
      ],
      loading1:true,
      selectDate: "",
      pickerOptions: {
        onPick: ({ maxDate, minDate }) => {
          this.selectDate = minDate.getTime();
          if (maxDate) {
            this.selectDate = "";
          }
        },
        disabledDate: time => {
          if (this.selectDate !== "") {
            const one = 30 * 24 * 3600 * 1000;
            const minTime = this.selectDate - one;
            const maxTime = this.selectDate + one;
            return time.getTime() < minTime || time.getTime() > maxTime;
          }
        }
      },
      dialogTableVisible: false,
      disabled: false,
      tableData: [],
      multipleSelection: [],
      tableData1: [],
      disabledo: false,
      ruleForm: {
        name: "",
        cycle: "0",
        executionWeek:'',
        executionTime:'',
        executionDay:'',
        description:'',
        groupId:''
      },
      formLabelWidth: "100px",
      rules: {
        name: [
          {
            required: true,
            message: "请输入姓名",
            trigger: "change"
          }
        ],
        cycle: [
          {
            required: true,
            message: "请选择执行周期",
            trigger: "change"
          }
        ],
        executionWeek: [
          {
            required: true,
            message: "请选择执行周",
            trigger: "change"
          }
        ],
        executionDay: [
          {
            required: true,
            message: "请选择执行日期",
            trigger: "change"
          }
        ],
        executionTime: [
          {
            required: true,
            message: "请选择执行时间",
            trigger: "change"
          }
        ],
      },
      total: 0,
      pageSize: '20',
      currentPage: '1',
      getRowKeys(row) {
        return row.id;
      },
      val:0
    };
  },
  created() {
    this.title = this.$route.query.type;
    this.ruleForm.groupId = this.$route.query.groupId
    this.getQuality()
    if (this.$route.query.type === "编辑") {
      this.disabledo = true;
      this.userDetail()
    } else if (this.$route.query.type === "详情") {
      this.disabledo = true;
      this.disabled = true;
    }
  },
  methods: {
    async getQuality(){
      const that = this;
       let params = {}
      params.size = that.pageSize
      params.page = that.currentPage
      params.status = 1
      const response = await qualityFindAll(params);
      if (response.data.code === 1) {
        that.tableData = response.data.data.content;
        that.total = response.data.data.totalElements
        //  if(this.val == 1){
          that.$nextTick(function(){
            that.toggleSelection(that.tableData1)
          })
        //   this.val = 2
        // }
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },
    // 获得焦点
    getBlur() {
      this.$nextTick(() => {
        let dom = window.document.getElementsByClassName(
          "el-date-picker__header"
        );
        if (dom.length > 0) {
          dom[0].style.display = "none";
        }
      });
    },
    deleteRule(row){
      let arr = this.tableData1.filter((v,i)=>{
       return v.id != row.id
      })
      this.tableData1 = arr
      console.log(this.tableData1,row)
      let that = this 
    },
    radioChange() {
      this.ruleForm.executionTime = ''
      this.ruleForm.executionWeek = ''
      this.$nextTick(()=>{
          this.$refs["ruleForm"].clearValidate()
      })
    },
    chooseRule() {
      let that = this 
      that.dialogTableVisible = true; 
      that.$nextTick(function(){
        that.$refs.multipleTable.clearSelection();
      })
      // if(this.val == 0){
        that.$nextTick(function(){
          that.toggleSelection(that.tableData1)
        })
      //   this.val = 1
      // }
      
    },
    handleOk() {
      this.dialogTableVisible = false;
        this.loading1 = true
       this.tableData1 = JSON.parse(JSON.stringify(this.multipleSelection));
        setTimeout(()=>{
          this.loading1 = false
        },500)
    },
    handleSelectionChange(val) {
      console.log(val)
        this.multipleSelection = val
        // if(this.val != 2){
        //   this.tableData1.forEach((v,i)=>{
        //     let count = 0
        //     this.tableData.forEach((v1,i1)=>{
        //       if(v.id == v1.id){
        //         count = 1
        //       }
        //     })
        //     if(count == 0){
        //       this.multipleSelection.push(v)
        //     }
        //   })
        // }
    },
    // 详情
    async userDetail() {
      const that = this;
      let data = { id: this.$route.query.id };
      that.loading = true;
      const response = await taskFindById(data);
      that.loading = false;
      if (response.data.code === 1) {
        that.ruleForm = response.data.data;
        if(that.ruleForm.cycle == 3){
          let time = that.ruleForm.executionTime.split(' ')
          that.ruleForm.executionDay = time[0]
          that.ruleForm.executionTime = time[1]
        }
        that.loading1 = true
        that.tableData1 = that.ruleForm.verifyRules
        that.loading1 = false
        that.multipleSelection = that.ruleForm.verifyRules
        delete that.ruleForm['verifyRules']
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },
    toggleSelection(rows) {
      let that = this
      if (rows) {
        rows.forEach(row => {
          that.tableData.forEach((v,i)=>{
            if(row.id == v.id){
              if(that.$refs.multipleTable){
                that.$refs.multipleTable.toggleRowSelection(v,true);
              }
            }
          })
        });
      } else {
        that.$refs.multipleTable.clearSelection();
      }
    },
    submitForm(ruleForm) {
      const that = this;
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          that.loading = true;
          let data = that.ruleForm;
          let arr = []
          that.tableData1.forEach((v,i)=>{
            arr.push(v.id)
          })
          data.ruleIds = arr
          if(data.cycle == '3'){
            data.executionWeek = ''
            data.executionTime = data.executionDay+' '+data.executionTime
          }
          delete data['executionDay']
          if (that.$route.query.type === "编辑") {
            // 编辑
            taskUpdate(data).then(res => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success("编辑成功");
                that.goBack();
              } else {
                that.$message.error(res.data.msg);
              }
            });
          } else {
            // 新增
            taskInsert(data).then(res => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success("新增成功");
                that.goBack();
              } else {
                that.$message.error(res.data.msg);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    goBack() {
      this.$router.push("/taskManage");
    },
    // 翻页
    pageChange(item) {
        let that = this
        that.pageSize = item.limit
        that.currentPage = item.page
        that.getQuality()
    }
  }
};
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
    width: 100%;
    text-align: left;
    .avatar-uploader {
      width: 64px;
      height: 64px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }

    .avatar-uploader-icon {
      box-sizing: border-box;
      font-size: 28px;
      color: #b7b7b7;
      width: 64px;
      height: 64px;
      line-height: 64px;
      text-align: center;
      &:hover {
        border: 1px dashed #b7b7b7;
        color: #fff;
        background-color: rgba(0, 0, 0, 0.3);
      }
    }
    .avatar {
      position: relative;
      width: 64px;
      height: 64px;
      display: block;
      border-radius: 50%;
    }
    .el-upload-action {
      position: absolute;
      top: 0;
      left: 0;
      display: block;
      width: 100%;
      height: 100%;
      font-size: 0;
      color: #fff;
      text-align: center;
      line-height: 64px;
      &:hover {
        font-size: 20px;
        background-color: #000;
        background-color: rgba(0, 0, 0, 0.3);
        border-radius: 50%;
      }
    }
  }
}
.demo-drawer__footer {
  margin-top: 80px;
  text-align: center !important;
}
/deep/ .el-dialog__footer{
  clear: both;
}
.local {
  color: #7f7f7f;
  display: flex;
  line-height: 16px;
  font-size: 14px;
  align-items: center;
  div {
    display: flex;
    align-items: flex-start;
  }
}
.el-date-editor.el-input,
.el-date-editor.el-input__inner {
  width: 100%;
}
// /deep/ .el-dialog__body{
//   height: 500px;
//   overflow: auto;
// }
// /deep/ .el-dialog__body::-webkit-scrollbar{
//   width: 0;
// }
</style>
