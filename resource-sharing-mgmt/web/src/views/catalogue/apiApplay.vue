<template>
  <div class="api-applay-page">
    <Breadcrumb :returnRouter="[{path:'/catalogue',name:'资源目录'}, {path:'',name:'立即申请'}]"></Breadcrumb>
    <div class="applay-box">
      <h2>申请流程</h2>
      <div class="applay-process">
        <p style="background: #0ea6fa">申请资源</p>
        <span></span>
        <p style="background: #facd91">提供单位处理</p>
        <span></span>
        <p style="background: #c3e182">查看资源反馈</p>
      </div>
      <div class="applay-resources">
        <h3>您申请的资源</h3>
        <div class="resources-name">
          <p>资源名称： {{ apiDateils.name }} <span>{{title}}</span></p>
          <p>提供单位： {{ apiDateils.supplier }}</p>
          <p v-if="title!=='云接口'">描述： {{ apiDateils.detail }}</p>
        </div>
        <h3>申请说明</h3>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px" label-position="left" class="form-data">
          <el-form-item label="用途" prop="resourceUse">
            <el-checkbox-group v-model="form.resourceUse">
              <el-checkbox label="系统应用" name="系统应用"></el-checkbox>
              <el-checkbox label="内部业务办理" name="内部业务办理"></el-checkbox>
              <el-checkbox label="比对核查" name="比对核查"></el-checkbox>
              <el-checkbox label="其他" name="其他"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="支持业务" prop="supportBusiness">
            <el-input v-model="form.supportBusiness" placeholder="请填支持业务"></el-input>
          </el-form-item>
          <el-form-item label="使用期限" prop="useEndTime">
            <el-date-picker @change="timeChange" style="width: 100%" v-model="time" type="daterange" range-separator="~" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd HH:mm:ss" :picker-options="{
                disabledDate: (time) => {
                  return dataTime.startTimeData(time);
                },
              }">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="说明" prop="instructions">
            <el-input type="textarea" v-model="form.instructions" placeholder="请输入内容"></el-input>
          </el-form-item>
          <el-form-item label="联系人" prop="contacts">
            <el-input v-model="form.contacts" placeholder="请填联系人"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="mobilePhone">
            <el-input v-model="form.mobilePhone" placeholder="请填写联系电话"></el-input>
          </el-form-item>
          <el-form-item label="电子邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请填写联系邮箱"></el-input>
          </el-form-item>
          <el-form-item label="申请依据">
            <el-upload ref="upload" accept=".jpg,.word,.pdf,.doc,.docx" :headers="headers" :action="uploadUrl" :data="null" :before-upload="beforeUpload" :on-change="handleChange" :on-error="handleError" :on-remove="handleRemove" :auto-upload="true" :file-list="fileList">
              <el-button size="small">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">
                最多上传5个附件，每个附件不大于10M，支持word、pdf、jpg等文件格式
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button :disabled="btnDisabled" type="primary" @click="submitForm('form')">提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import Breadcrumb from "com/breadcrumb";
import config from "@/config/index.js";
import { getToken } from '@/utils/storage.js';
export default {
  components: {
    Breadcrumb,
  },
  data() {
    var mobilePhoneVerification = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请填写联系电话"));
      } else if (!/^1[3456789]\d{9}$/.test(value) && !/^[0][1-9]{2,3}-[0-9]{5,10}$/.test(value)) {
        callback(new Error("手机号格式错误！"));
      } else {
        callback();
      }
    };
    return {
      title: this.$route.query.type,
      dataTime: {
        // 开始时间范围限制
        startTimeData: (time) => {
          let nowData = new Date();
          nowData = new Date(nowData.setDate(nowData.getDate() - 1));
          return time < nowData;
        },
      },
      btnDisabled: false,
      apiDateils: {
        name: "",
        supplier: "",
        detail: "",
      },
      time: [],
      form: {
        // 提交类型
        type: this.$route.query.type === "云接口" ? '1' : this.$route.query.type === "云文件" ? '2' : '3',
        dataId: "",// 云数据ID
        fileId: "",//云文件ID
        tableName: "",//云数据表名
        uviewId: "",
        uviewApiId: "",
        sourceApiName: "",
        sourceApiDesc: "",
        resourceUse: [],
        supportBusiness: "",
        useStartTime: "",
        useEndTime: "",
        instructions: "",
        contacts: "",
        mobilePhone: "",
        email: "",
        fileUrl: "",
      },
      rules: {
        contacts: [{ required: true, message: '联系人不能为空', trigger: 'blur', }],
        resourceUse: [{ required: true, message: '请选择用途', trigger: 'change', }],
        supportBusiness: [{ required: true, message: '请输入支持业务', trigger: 'change', }],
        useEndTime: [{ required: true, message: '请选择期限时间', trigger: 'change', }],
        instructions: [{ required: true, message: '请输入说明', trigger: 'change', }],
        mobilePhone: [
          { required: true, message: '请填写联系电话', trigger: 'change', },
          { validator: mobilePhoneVerification, trigger: "change" },
        ],
        email: [
          // { required: false, message: '', trigger: 'change' },
          { pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, message: '请输入正确的邮箱' }
        ]
      },
      fileList: [],
      uploadUrl: config.baseURL + "/webadmin/file/upload",
      headers: {
        Authorization: "token " + getToken(),
      },
    };
  },
  mounted() {

    if (this.$route.query.type === "云文件") {
      // 云文件详情
      this.assetFileExFind({ id: this.$route.query.id }).then(res => {
        if (res.code === 1) {
          this.form.fileId = res.data.id;
          this.form.fileName = res.data.name;
          this.form.detail = res.data.detail;
          this.apiDateils.detail = res.data.detail;
        }
      })
    } else if (this.$route.query.type === "云数据") {
      // 云数据详情
      this.assetDataExFind({ id: this.$route.query.id }).then(res => {
        if (res.code === 1) {
          this.form.dataId = res.data.id;
          this.form.tableName = res.data.tableName;
          this.apiDateils.detail = res.data.detail;
        }
      })
    } else if (this.$route.query.type === "云接口") {
      // 云接口详情
      this.assetApiExFind({ id: this.$route.query.id }).then((res) => {
        if (res.code === 1) {
          this.form.sourceApiName = res.data.name;
          this.form.sourceApiDesc = res.data.detail;
          this.apiDateils.name = res.data.name;
          this.apiDateils.detail = res.data.detail;
        } else {
          this.$message.error(res.msg);
        }
      });
    }

    this.archBusiUviewExFind({ id: this.$route.query.Id }).then((res) => {
      if (res.code === 1) {
        this.apiDateils.supplier = res.data.name;
        this.form.uviewId = res.data.uviewId;
        this.form.uviewApiId = this.$route.query.id;
      } else {
        this.$message.error(res.msg);
      }
    });

  },
  methods: {
    timeChange(val) {
      this.form.useStartTime = val[0];
      this.form.useEndTime = val[1];
    },
    submitForm(formName) {
      let dataUrl = [];
      this.fileList.forEach((item) => {
        dataUrl.push({
          name: item.response.fileName,
          url: item.response.fileDownloadUri,
        });
      });
      this.form.fileUrl = JSON.stringify(dataUrl);
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.btnDisabled = true;
          this.form.resourceUse = JSON.stringify(this.form.resourceUse);
          this.resourceUseInfoInsert(this.form).then((res) => {
            if (res.code === 1) {
              this.$message.success("申请成功！");
              // this.form.resourceUse = JSON.parse(this.form.resourceUse);
            } else {
              this.$message.error(res.msg);
              // this.form.resourceUse = JSON.parse(this.form.resourceUse);
            }
            setTimeout(() => {
              this.btnDisabled = false;
              this.time = [];
              this.form.fileUrl = "";
              this.fileList = [];
              this.$refs['form'].resetFields();
            }, 3000)
          });
        } else {
          return false;
        }
      });
    },
    //附件限制
    beforeUpload(file) {
      let arr = JSON.parse(JSON.stringify(this.fileList))
      let isRepeat = true
      arr.forEach((v, i) => {
        if (i != (arr.length - 1)) {
          if (v.name == file.name) {
            isRepeat = false
          }
        }
      })
      var testmsg = file.name.substring(file.name.lastIndexOf(".") + 1);
      const extension = testmsg === "jpg";
      const extension3 = testmsg === "pdf";
      const extension4 = testmsg === "doc";
      const extension5 = testmsg === "docx";
      let isFive = true
      const isLt2M = file.size / 1024 / 1024 < 10;
      if (!extension && !extension3 && !extension4 && !extension5) {
        this.$message({
          message: "上传文件只能是 jpg、word、pdf格式!",
          type: "warning",
        });
      }
      if (!isRepeat) {
        this.$message({
          message: "同文件名的文档已上传，不能重复上传",
          type: "warning",
        });
      }
      if (!isLt2M) {
        this.$message({
          message: "上传文件大小不能超过 10MB!",
          type: "warning",
        });
      }
      if (this.fileList.length > 5) {
        this.$message({
          message: "已上传5个附件，不能再继续添加!",
          type: "warning",
        });
        isFive = false
      }
      return (extension || extension3 || extension4 || extension5) && isLt2M && isFive && isRepeat;
    },
    // 控制上传个数
    handleChange(file, fileList) {
      // this.fileList = fileList.slice(-5);
      this.fileList = fileList
    },
    // 上传失败
    handleError() {
      this.$refs.upload.clearFiles();
      this.$message.error("上传失败");
    },
    // 删除文件
    handleRemove(file, fileList) {
      this.fileList = fileList;
    },
  },
};
</script>
<style lang="scss" scope>
.api-applay-page {
  // padding-top: 70px;
  .applay-box {
    width: 50%;
    margin: 0 auto;
    h2 {
      font-size: 24px;
      text-align: center;
      line-height: 60px;
    }
    .applay-process {
      display: flex;
      justify-content: space-around;
      align-items: center;
      p {
        color: $font-color-white;
        padding: 5px 15px;
      }
      span {
        width: 0;
        height: 0;
        border: 10px solid transparent;
        border-left-color: #797979;
      }
    }
    .applay-resources {
      h3 {
        font-size: $font-size-large;
        line-height: 80px;
      }
      .resources-name {
        margin-left: 50px;
        line-height: 34px;
        span {
          background: $font-color-header;
          color: $font-color-white;
          font-size: $font-size-small;
          padding: 2px 5px;
          border-top-left-radius: 10px;
          border-bottom-right-radius: 10px;
          margin-left: 10px;
        }
      }
      .form-data {
        margin-left: 50px;
      }
    }
  }
}
</style>