<template>
  <el-drawer :title="modalObjj" direction="rtl" v-loading="loading" :visible.sync="dialogFormVisible" :before-close="ClearTextConeten" custom-class="demo-drawer" size="45%" :wrapperClosable="false" ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" v-loading="loading">
      <el-form-item prop="name" label="用户姓名:" :label-width="this.formLabelWidth" class="InpitWidth">
        <el-input clearable :disabled="nowId ? true : false" autocomplete="off" v-model="ruleForm.name" placeholder="请输入用户姓名"></el-input>
      </el-form-item>
      <!-- <el-form-item prop="tel" :required="isHaveTo" label="手机号:" :label-width="this.formLabelWidth" class="InpitWidth">
        <el-input clearable :disabled="ProhibitFromm" autocomplete="off" v-model="ruleForm.tel" placeholder="请输入手机号"></el-input>
      </el-form-item> -->
      <el-form-item label="手机号:" :label-width="this.formLabelWidth" class="InpitWidth">
        <el-input clearable :disabled="ProhibitFromm" autocomplete="off" v-model="ruleForm.tel" placeholder="请输入手机号"></el-input>
      </el-form-item>
      <el-form-item prop="roleId" label="角色:" :label-width="this.formLabelWidth" class="InpitWidth">
        <el-select style="width:100%;" v-model="ruleForm.roleId" clearable :disabled="ProhibitFromm" placeholder="请选择角色">
          <el-option v-for="item in roleList" :key="item.value" :label="item.name" :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="单位:" :label-width="this.formLabelWidth" class="InpitWidth">
        <el-input clearable :disabled="ProhibitFrom" autocomplete="off" v-model="ruleForm.organization" placeholder="请输入单位"></el-input>
      </el-form-item>
      <el-form-item label="备注:" :label-width="this.formLabelWidth" class="InpitWidth">
        <el-input type="textarea" clearable :disabled="ProhibitFrom" autocomplete="off" v-model="ruleForm.remark" placeholder="请输入备注"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button v-if="!ProhibitFrom" type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>
import { insert, update, roleList, findById } from "@/api/omManger/user";
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    modalObjj: {
      type: String,
      default: () => { }
    }
  },

  data() {
    let validateName = (rule, value, callback) => {
      const reg = /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/g;
      if (!value && this.isHaveTo) {
        callback(new Error("请输入手机号"));
      } else if (!reg.test(value) && this.isHaveTo) {
        callback(new Error("请输入正确的手机号"));
      } else {
        callback();
      }
    };
    return {
      isShow: true,
      ruleForm: {
        tel: "",
        name: "",
        remark: "",
        roleId: "",
        roleName: "",
        organization: ""
      },
      rule: {},
      roleList: [],
      rules: {
        name: [
          {
            required: true,
            message: "请输入用户姓名",
            trigger: "blur"
          }
        ],
        tel: [
          {
            validator: validateName,
            trigger: ["change", "blur"]
          }
        ],
        roleId: [
          {
            required: true,
            message: "请选择角色",
            trigger: ["change", "blur"]
          }
        ]
      },

      // 是否触发禁用表单项
      ProhibitFrom: false,
      ProhibitFromm: false,
      dialogFormVisible: this.show,
      formLabelWidth: "120px",
      lock: false,
      dialogVisible: true,
      loading: false
    };
  },
  watch: {
    show() {
      this.dialogFormVisible = this.show;
    }
  },
  computed: {
    isHaveTo() {
      return this.isShow;
    }
  },
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function (value) {
      const that = this;
      that.dialogFormVisible = true;
      that.roleList = [];
      that.nowId = value;
      this.isShow = true;
      that.ProhibitFrom = false;
      if (value) {
        that.initFormDetail(value);
      } else {
        this.getRoleList();
      }
    },
    async initFormDetail(value) {
      const that = this;
      try {
        let parameter = { id: value };
        that.loading = true;
        const response = await findById(parameter);
        that.loading = false;
        that.ruleForm = response.data.data;
        this.getRoleList();
        if (this.modalObjj === "详情") {
          that.ProhibitFrom = true;
          // 禁用编辑器
        } else if (this.modalObjj === "编辑") {
          that.ProhibitFromm = false;
          if (value === "admin") {
            this.isShow = false;
            that.ProhibitFromm = true;
          }
          that.lock = true;
        } else {
          that.lock = false;
          that.ProhibitFrom = false;
          that.ProhibitFromm = false;
        }
      } catch (even) {
        that.$message.error("数据获取失败");
      }
    },

    CloseModal() {
      const that = this;
      that.ClearTextConeten();
      that.ProhibitFrom = false;
    },

    ClearTextConeten() {
      const that = this;
      that.$refs["ruleForm"].resetFields();
      that.ruleForm.tel = "";
      that.ruleForm.remark = "";
      that.ruleForm.name = "";
      that.ruleForm.roleId = "";
      that.ruleForm.organization = "";
      that.ProhibitFrom = false;
      that.ProhibitFromm = false;
      that.dialogFormVisible = false;
      if (that.ruleForm.id) {
        delete that.ruleForm.id;
      }
    },

    SuretoAddClick(ruleForm) {
      const that = this;
      that.$refs[ruleForm].validate(async index => {
        if (index === false) {
          return false;
        }
        that.loading = true;
        let roleName = "";
        this.roleList.forEach(item => {
          if (this.ruleForm.roleId === item.id) {
            roleName = item.name;
          }
        });
        let data = {
          tel: this.ruleForm.tel,
          name: this.ruleForm.name,
          remark: this.ruleForm.remark,
          roleId: this.ruleForm.roleId,
          roleName: roleName,
          organization: this.ruleForm.organization
        };
        let response = await insert(data);
        if (that.nowId) {
          data.id = that.nowId;
          response = await update(data);
        }
        that.loading = false;
        if (response.data.code === 1) {
          that.$message.success(response.data.msg);
          that.ClearTextConeten();
          that.$emit("Reload");
        } else {
          that.$message.error(response.data.msg);
          return false;
        }
      });
    },
    async getRoleList() {
      const that = this;
      that.loading = true;
      const response = await roleList();
      that.loading = false;
      if (response.data.code === 1) {
        response.data.data.content.map(function (v) {
          that.roleList.push({ id: v.id, name: v.name });
        });
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    }
  }
};
</script>

<style lang="scss">
.el-drawer {
  .el-drawer {
    .el-drawer__body {
      padding: 10px 20px !important;
    }
  }
  .InpitWidth {
    width: 80%;
    min-width: 302px;
  }
  .avatar-border-red .el-upload {
    border: 1px dashed #f56c6c !important;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #b6b5b5;
    }
    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;
      .el-progress-circle {
        margin: 13% auto 10%;
      }
    }
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    float: left;
    position: relative;
    overflow: hidden;
    &:hover {
      border-color: #b6b5b5;
    }
    .el-progress {
      position: absolute;
      z-index: 999;
      top: 0;
      left: 0;
      width: 100%;
      height: 100%;
      background: white;
      .el-progress-circle {
        margin: 13% auto 10%;
      }
    }
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 200px;
    height: 178px;
    line-height: 178px;
    text-align: center;
    cursor: pointer;
    border-radius: 6px;
  }
  .avatar {
    width: 200px;
    height: 180px;
    display: block;
  }
}

.edit_main {
  text-align: left;
  padding: 10px 15px;
  border-radius: 5px;
  min-height: 250px;
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  color: #c0c4cc;
  cursor: not-allowed;
  img {
    width: auto;
    height: auto;
  }
}

.ClassDisable .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  float: left;
  position: relative;
  overflow: hidden;
  cursor: no-drop;
  &:hover {
    border-color: #b6b5b5;
  }
  .el-progress {
    position: absolute;
    z-index: 999;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: white;
    .el-progress-circle {
      margin: 13% auto 10%;
    }
  }
}

.buttonentry {
  float: left;
  text-align: left;
}
.a {
  width: 6vw !important;
}
.drawer_footer {
  text-align: center;
}
:focus {
  outline: 0;
}
</style>
