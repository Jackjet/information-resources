<template>
  <div class="demand-page">
    <Breadcrumb :returnRouter="[{path:'',name:'部门需求'}]"></Breadcrumb>
    <div class="demand-process">
      <h2>申请流程</h2>
      <div class="applay-process">
        <p>
          <img src="@/assets/image/icon/提交需求.svg" alt="">
          <span>需求申请</span>
        </p>
        <img style="margin-top: -30px;" src="@/assets/image/icon/right.svg" alt="">
        <p>
          <img src="@/assets/image/icon/受理单位处理.svg" alt="">
          <span>受理单位处理</span>
        </p>
        <img style="margin-top: -30px;" src="@/assets/image/icon/right.svg" alt="">
        <p>
          <img src="@/assets/image/icon/查看需求反馈.svg" alt="">
          <span>查看需求反馈</span>
        </p>
      </div>
      <div class="form-process">
        <!-- <h3>需求详情</h3> -->
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" label-position="left" class="form-data">
          <el-form-item label="需求标题" prop="title">
            <el-input v-model="form.title" placeholder="请输入需求标题"></el-input>
          </el-form-item>
          <el-form-item label="受理单位" prop="acceptDeptId">
            <el-select style="width:100%" v-model="form.acceptDeptId" @change="acceptDeptChange" placeholder="请选择受理单位">
              <el-option v-for="item in acceptDeptOption" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.acceptDeptId==='39'" label="申请类型" prop="type">
            <el-select style="width:100%" v-model="form.type" placeholder="请选择申请类型">
              <el-option label="云接口" value="1"> </el-option>
              <el-option label="云文件" value="2"> </el-option>
              <el-option label="云数据" value="3"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item v-if="form.type&&form.acceptDeptId==='39'" label="申请表模板">
            <a v-if="form.type==='1'" :href="downloadUrl+'web/jsonFile/接口资源类型数据申请表模板.docx'" style="margin-right:20px;">接口资源类型数据申请表模板</a>
            <a v-else :href="downloadUrl+'web/jsonFile/文件或数据库资源类型数据申请表模板.docx'">文件或数据库资源类型数据申请表模板</a>
          </el-form-item>
          <el-form-item label="缘由" prop="reason">
            <el-select style="width:100%" v-model="form.reason" placeholder="请选择缘由">
              <el-option label="政策相关" value="政策相关"> </el-option>
              <el-option label="非政策相关" value="非政策相关"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="缘由说明" prop="reasonDesc">
            <el-input type="textarea" v-model="form.reasonDesc" placeholder="请填写内容"></el-input>
            <span style="color:#999999;font-size:12px;">为便于受理单位处理您的需求，请说明需求提出的缘由</span>
          </el-form-item>
          <el-form-item label="需求类型" prop="requestType">
            <el-select style="width:100%" v-model="form.requestType" placeholder="请选择需求类型">
              <el-option label="资源目录变更" value="0"> </el-option>
              <el-option label="资源目录新增" value="1"> </el-option>
              <el-option label="资源数据变更" value="2"> </el-option>
              <el-option label="资源数据新增" value="3"> </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="需求描述" prop="describe">
            <el-input v-model="form.describe" placeholder="请填写内容"></el-input>
            <span style="color:#999999;font-size:12px;">为便于受理单位处理您的需求，请详细描述资源目录或共享资源的需求</span>
          </el-form-item>
          <el-form-item label="期望解决时间">
            <el-date-picker style="width:100%" v-model="form.expectTime" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期" :picker-options="{
                disabledDate: (time) => {
                  return dataTime.startTimeData(time);
                },
              }">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="联系人" prop="contacts">
            <el-input v-model="form.contacts" placeholder="请填写联系人"></el-input>
          </el-form-item>
          <el-form-item label="联系电话" prop="mobilePhone">
            <el-input v-model="form.mobilePhone" placeholder="请填写联系电话"></el-input>
          </el-form-item>
          <el-form-item label="电子邮箱" prop="email">
            <el-input v-model="form.email" placeholder="请填写联系邮箱"></el-input>
          </el-form-item>
          <el-form-item ref="fileUrl" label="附件" prop="fileUrl" :rules="[{ required: form.acceptDeptId === '39', message: '请上传文件', trigger: 'change' }]">
            <el-upload ref="upload" accept=".jpg,.jpeg,.word,.pdf,.doc,.docx" :headers="headers" :action="uploadUrl" :data="null" :before-upload="beforeUpload" :on-change="handleChange" :on-error="handleError" :on-remove="handleRemove" :auto-upload="true" :file-list="fileList">
              <el-button size="small"><img src="@/assets/image/icon/upload.svg" alt=""> 点击上传</el-button>
              <div slot="tip" class="el-upload__tip" style="color:#999999;font-size:12px;">
                <img src="@/assets/image/icon/listTips.svg" alt=""> 最多上传5个附件，每个附件不大于10M，支持word、pdf、jpg等文件格式
              </div>
            </el-upload>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onSubmit" :disabled="btnDisabled">提交</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import config from "@/config/index.js";
import Breadcrumb from "com/breadcrumb";
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
    var contactsVerification = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请填写联系人"));
      } else if (value.length > 50) {
        callback(new Error("输入长度最多为50"));
      } else {
        callback();
      }
    };
    return {
      downloadUrl: config.baseURL,
      acceptDeptOption: [],
      dataTime: {
        // 开始时间范围限制
        startTimeData: (time) => {
          let nowData = new Date();
          nowData = new Date(nowData.setDate(nowData.getDate() - 1));
          return time < nowData;
        },
      },
      btnDisabled: false,
      form: {
        title: "",
        reason: "",
        reasonDesc: "",
        acceptDeptId: "",
        acceptDept: "",
        requestType: "",
        describe: "",
        contacts: "",
        mobilePhone: "",
        expectTime: "",
        email: "",
        fileUrl: "",
        type: "",
      },
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "change" }],
        acceptDeptId: [
          { required: true, message: "请选择受理单位", trigger: "change" },
        ],
        type: [{ required: true, message: "请选择申请类型", trigger: "change" }],
        reason: [{ required: true, message: "请选择缘由", trigger: "change" }],
        reasonDesc: [
          { required: true, message: "请填写缘由说明", trigger: "change" },
        ],
        requestType: [
          { required: true, message: "请选择需求类型", trigger: "change" },
        ],
        describe: [
          { required: true, message: "请填写需求描述", trigger: "change" },
        ],
        contacts: [
          { required: true, message: "请填写联系人", trigger: "change" },
          { validator: contactsVerification, trigger: "change" },
        ],
        mobilePhone: [
          { required: true, message: "请填写联系电话", trigger: "change" },
          { validator: mobilePhoneVerification, trigger: "change" },
        ],
        email: [
          { required: false, message: '', trigger: 'change' },
          { pattern: /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/, message: '请输入正确的邮箱' }
        ],
        // fileUrl: [
        //   { required: this.form.acceptDeptId === '39', message: '请上传文件', trigger: 'change' },
        // ],
      },
      fileList: [],
      uploadUrl: config.baseURL + "/webadmin/file/upload",
      headers: {
        Authorization: "token " + getToken(),
      },
    };
  },
  mounted() {
    this.organizationFindAll().then((res) => {
      if (res.code === 1) {
        this.acceptDeptOption = res.data.children;
      } else {
        this.$message.error(res.msg);
      }
    });
  },
  methods: {
    // 受理单位
    acceptDeptChange(row) {
      this.acceptDeptOption.forEach((item) => {
        if (row === item.id) {
          this.form.acceptDept = item.name;
        }
      });
    },
    onSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          this.btnDisabled = true;
          let dataUrl = [];
          this.fileList.forEach((item) => {
            dataUrl.push({
              name: item.response.fileName,
              url: item.response.fileDownloadUri,
            });
          });
          this.form.fileUrl = JSON.stringify(dataUrl);
          this.demandedInfoInsert(this.form).then((res) => {
            if (res.code === 1) {
              this.$message.success("提交成功！");
              // this.$router.push("/catalogue")
            } else {
              this.$message.error(res.msg);
            }
            setTimeout(() => {
              this.btnDisabled = false;
              this.form.expectTime = "";
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
      this.form.fileUrl = JSON.stringify(this.fileList);
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
      const extension2 = testmsg === "jpeg";
      const extension3 = testmsg === "pdf";
      const extension4 = testmsg === "doc";
      const extension5 = testmsg === "docx";
      let isFive = true
      const isLt2M = file.size / 1024 / 1024 < 10;
      if (!extension && !extension2 && !extension3 && !extension4 && !extension5) {
        this.$message({
          message: "上传文件只能是 jpg、jpeg、word、pdf格式!",
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
          message: "最多上传5个附件，不能再继续添加",
          type: "warning",
        });
        isFive = false
      }
      return (extension || extension2 || extension3 || extension4 || extension5) && isLt2M && isFive && isRepeat;
    },
    // 控制上传个数
    handleChange(file, fileList) {
      if (fileList.length > 0) {
        this.$refs['fileUrl'].clearValidate();
      }
      this.form.fileUrl = JSON.stringify(fileList);
      this.fileList = fileList

    },
    // 上传失败
    handleError() {
      this.$refs.upload.clearFiles();
      this.$message.error("上传失败");
    },
    // 删除文件
    handleRemove(file, fileList) {
      this.form.fileUrl = JSON.stringify(fileList);
      this.fileList = fileList;

      if (fileList.length == 0 && this.form.acceptDeptId === '39') {
        this.form.fileUrl = "";
        this.$refs['fileUrl'].validate();
      } else {
        this.$refs['fileUrl'].clearValidate();
      }

    },
  },
};
</script>
<style lang="scss" scope>
.demand-page {
  background: #f8f8f8;
  // padding-top: 70px;
  padding-bottom: 10px;
  .demand-process {
    margin: 50px;
    margin-top: 0;
    background: #fff;
    padding-top: 30px;
    h1 {
      border-bottom: 1.5px solid #ccc;
      line-height: 50px;
      font-weight: bold;
      padding-left: 40px;
    }
    h2 {
      font-size: 24px;
      text-align: center;
      // line-height: 60px;
      margin-bottom: 30px;
    }
    .applay-process {
      display: flex;
      justify-content: space-around;
      align-items: center;
      width: 600px;
      margin: auto;
      // img {
      //   width: 600px;
      // }
      p {
        // color: $font-color-white;
        // padding: 5px 15px;
        text-align: center;
        span {
          display: block;
          margin-top: 10px;
        }
      }
      // span {
      //   width: 0;
      //   height: 0;
      //   border: 10px solid transparent;
      //   border-left-color: #797979;
      // }
    }
    .form-process {
      width: 60%;
      padding: 50px;
      margin: auto;
      box-sizing: border-box;
      h3 {
        border-left: 5px solid $font-color-header;
        padding-left: 5px;
        margin: 50px 0;
        box-sizing: border-box;
      }
      .form-data {
        margin-left: 70px;
      }
    }
  }
}

.form-process .el-form-item__label {
  position: relative;
}
.form-process
  .el-form-item.is-required:not(.is-no-asterisk)
  .el-form-item__label-wrap
  > .el-form-item__label:before,
.form-process
  .el-form-item.is-required:not(.is-no-asterisk)
  > .el-form-item__label:before {
  position: absolute;
  left: -10px;
  top: 4px;
}
</style>