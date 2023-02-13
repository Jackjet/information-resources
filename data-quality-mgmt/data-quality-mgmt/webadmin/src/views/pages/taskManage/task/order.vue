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
        <el-form-item label="规则名称:" :label-width="this.formLabelWidth" prop="verifyRuleName">
          <el-input
            clearable
            maxlength="100"
            size="medium"
            :disabled="true"
            placeholder="请输入规则名称"
            v-model="ruleForm.verifyRuleName"
          />
        </el-form-item>
        <el-form-item label="数据模板:" :label-width="this.formLabelWidth">
          <el-input
            clearable
            maxlength="100"
            size="medium"
            :disabled="true"
            placeholder="请输入数据模板"
            v-model="ruleForm.templateName"
          />
        </el-form-item>
        <el-form-item label="执行时间:" :label-width="this.formLabelWidth">
          <el-date-picker v-model="ruleForm.createTime" type="month" :disabled="true" placeholder="选择开始时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="标题:" :label-width="this.formLabelWidth" prop="title">
          <el-input
            clearable
            maxlength="100"
            size="medium"
            :disabled="disabled"
            placeholder="请输入标题"
            v-model="ruleForm.title"
          />
        </el-form-item>
        <el-form-item label="工单处理人:" :label-width="this.formLabelWidth" prop="handlerName">
          <el-col :span="18" class="inp">
            <el-input :disabled="true" size="medium" placeholder="请输入标题" v-model="ruleForm.handlerName" />
          </el-col>
          <el-col
            :span="6"
          >
            <div
            @click="dialogTableVisible = true"
            style="border-radius: 4px;border: 1px solid #DCDFE6;border-left:0;height34px;line-height:34px;margin-top:2px;text-align:center;cursor: pointer;"
            >
              <i class="el-icon-circle-plus" style="font-size:18px"></i>
              <span>选择</span>
            </div>
          </el-col>
        </el-form-item>
        <el-form-item label="工单级别:" :label-width="this.formLabelWidth" prop="level">
          <el-radio v-model="ruleForm.level" label="4">严重警告</el-radio>
          <el-radio v-model="ruleForm.level" label="3">警告</el-radio>
          <el-radio v-model="ruleForm.level" label="2">关注</el-radio>
          <el-radio v-model="ruleForm.level" label="1">一般</el-radio>
          <el-radio v-model="ruleForm.level" label="0">正常</el-radio>
        </el-form-item>
        <el-form-item label="描述:" :label-width="this.formLabelWidth">
          <el-input
            maxlength="200"
            type="textarea"
            :rows="3"
            placeholder="请输入描述"
            clearable
            size="medium"
            :disabled="disabled"
            v-model="ruleForm.description"
          ></el-input>
        </el-form-item>
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
    <el-dialog title="工单处理人" :visible.sync="dialogTableVisible" width='800px'>
      <el-table ref='multiple' @selection-change='selectionChange' :data="gridData" @row-click="singleElection">
        <el-table-column type='selection'>
        </el-table-column>
        <el-table-column property="name" label="姓名" ></el-table-column>
        <el-table-column property="phone" label="手机号"></el-table-column>
        <el-table-column  label="性别">
          <template slot-scope="scope" >
            <span v-if="scope.row.sex == 0">女</span>
            <span v-if="scope.row.sex == 1">男</span>
          </template>
        </el-table-column>
        <el-table-column property="roleName" label="角色"></el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleOk">确 定</el-button>
      </span>
    </el-dialog>
  </el-main>
</template>
<script>

import { findworkOrderById ,findUser,insertOrder} from "@/api/task.js";


let defaultAvatar = require("../../../../assets/image/user.png");

export default {
  data() {
    return {
      title: "生成工单",
      rule: "",
      value2: "",
      disabled: false,
      disabledo: false,
      checkValue:'',
      multipleSelection:[],
      ruleForm: {
        ruleName:'',
        templateName:'',
        templateId:'',
        createTime:'',
        handlerName:''
      },
      formLabelWidth: "120px",
      rules: {
        title: [
          {
            required: true,
            message: "请输入标题",
            trigger: "change"
          }
        ],
        handlerName: [
          {
            required: true,
            message: "请选择处理人",
            trigger: "change"
          }
        ],
         level: [
          {
            required: true,
            message: "请选择工单级别",
            trigger: "change"
          }
        ],
      },
      gridData: [],
      dialogTableVisible: false,
      loading: false
    };
  },
  created() {
    this.disabledo = true;
    this.ruleForm.verifyRuleId = this.$route.query.id;
    this.ruleForm.verifyRuleName = this.$route.query.ruleName;
    this.ruleForm.templateName = this.$route.query.templateName;
    this.ruleForm.templateId = this.$route.query.templateId;
    this.ruleForm.createTime = this.$route.query.createTime;
    this.userDetail()
  },
  methods: {
    // 详情
    async userDetail() {
      const that = this;
      that.loading = true;
      const response = await findUser();
      that.loading = false;
      if (response.data.code === 1) {
        this.gridData = response.data.data
        this.gridData.forEach((v,i)=>{
          v.check = false
        })
        console.log(this.gridData)
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },
    submitForm(ruleForm) {
      const that = this;
      this.$refs["ruleForm"].validate(valid => {
        if (valid) {
          that.loading = true;
          let data = that.ruleForm;
            // 编辑
            insertOrder(data).then(res => {
              that.loading = false;
              if (res.data.code === 1) {
                that.$message.success("生成成功");
                that.goBack();
              } else {
                that.$message.error(res.data.msg);
              }
            });
        } else {
          return false;
        }
      });
    },
    
    goBack() {
      const that = this
      that.$router.push({ 
          path: '/taskManageDetail'
      })
    },
    
    singleElection (row) {
        this.$refs.multiple.toggleRowSelection(row)
    },
    selectionChange(val){
      if(val.length > 1){
        this.$refs.multiple.clearSelection()
        this.$refs.multiple.toggleRowSelection(val.pop())
      }else{
        this.multipleSelection = val.pop()
      }
    },
    handleOk(){
      if(this.multipleSelection){
        this.ruleForm.handlerId = this.multipleSelection.id
        this.ruleForm.handlerName = this.multipleSelection.name
      }
      this.dialogTableVisible = false
    },
    getJsonTree(data, parentId) {
      let itemArr = [];
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId) {
          let newNode = {};
          newNode.value = node.id;
          newNode.label = node.name;
          if (node.children.length > 0) {
            newNode.children = this.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode);
        }
      }
      return itemArr;
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
    width: 65%;
    margin: 0 auto;

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
  text-align: center;
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
</style>
