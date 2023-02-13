<template>
  <el-drawer
    :title="modalObjjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync="dialogFormVisible"
    :before-close="ClearTextConeten"
    custom-class="demo-drawer"
    size="45%"
    :wrapperClosable="false"
    ref="drawer"
  >
    <el-form
      :model="ruleForm"
      :rules="rules"
      ref="ruleForm"
      v-loading="loading"
    >
      <el-form-item
        prop="nodeId"
        label="节点名称:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
      >
        <el-select
          clearable
          style="width:100%;"
          v-model="ruleForm.nodeId"
          :disabled="ProhibitFromm"
          @change="nodeChange"
          placeholder="请选择节点名称"
        >
          <el-option
            v-for="item in nodeList"
            :key="item.value"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        prop="databaseId"
        label="数据库名称:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
      >
        <el-select
          clearable
          style="width:100%;"
          v-model="ruleForm.databaseId"
          :disabled="ProhibitFromm"
          placeholder="请选择数据库名称"
        >
          <el-option
            v-for="item in databasesList"
            :key="item.value"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="内存阈值（M）:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
        prop="ram"
      >
        <el-input
          clearable
          :disabled="ProhibitFrom"
          autocomplete="off"
          v-model.trim="ruleForm.ram"
          placeholder="请输入内存阈值（M）"
        ></el-input>
      </el-form-item>
      <el-form-item
        <el-form-item
        label="存储阈值（M）:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
        prop="rom"
      >
        <el-input
          clearable
          :disabled="ProhibitFrom"
          autocomplete="off"
          v-model.trim="ruleForm.rom"
          placeholder="请输入存储阈值（M）"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="告警方式:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
        prop="alarmWay"
      >
        <el-select
          clearable
          style="width:100%;"
          v-model="ruleForm.alarmWay"
          :disabled="ProhibitFrom"
          placeholder="请选择告警方式"
        >
          <el-option label="短信" value="sms"></el-option>
          <el-option label="邮件" value="email"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="通知人:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
      >
        <el-input
          type="textarea"
          clearable
          :disabled="ProhibitFrom"
          autocomplete="off"
          v-model="ruleForm.peopleNotified"
          placeholder="中间用逗号隔开"
        ></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button
        v-if="!ProhibitFrom"
        type="primary"
        @click="SuretoAddClick('ruleForm')"
        >确 定</el-button
      >
    </div>
  </el-drawer>
</template>

<script>
import {
  databaseMonitoringInsert,
  databaseMonitoringUpdate,
  databaseMonitoringFindById,
  databaseMonitoringFindAllDatabases,
  databaseMonitoringFindAllNodes
} from "@/api/dataBase/dataBase";
export default {
  components: {
    // 注册组件
    // editor: editor
  },

  props: {
    modalObjjj: {
      type: String,
      default: () => {}
    }
  },

  data() {
    return {
      ruleForm: {
        alarmWay: "",
        databaseId: "",
        ram: "",
        rom: "",
        peopleNotified: "",
        nodeId: ""
      },
      objjj: {},
      rule: {},
      nodeList: [],
      databasesList: [],
      rules: {
        name: [
          {
            required: true,
            message: "请输入系统名称",
            trigger: "blur"
          }
        ],
        nodeId: [
          {
            required: true,
            message: "请选择节点名称",
            trigger: ["change", "blur"]
          }
        ],
        ram: [
          {
            required: false,
            pattern: /^\+?[1-9]\d*$/,
            message: "请输入正整数",
            trigger: ["change", "blur"]
          }
        ],
        rom: [
          {
            required: false,
            pattern: /^\+?[1-9]\d*$/,
            message: "请输入正整数",
            trigger: ["change", "blur"]
          }
        ],
        alarmWay: [
          {
            required: true,
            message: "请选择告警方式",
            trigger: ["change", "blur"]
          }
        ],
        databaseId: [
          {
            required: true,
            message: "请选择数据库名称",
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
  created() {},
  methods: {
    /**
     * @param value 当前需要操作的数据集
     * @param state 根据不同状态 展示不同操作
     */
    initial: function(value) {
      const that = this;
      that.dialogFormVisible = true;
      this.nodeList = [];
      this.databasesList = [];
      this.findNodes();
      if (value) {
        that.nowId = value;
        that.initFormDetail();
      } else {
        that.ProhibitFrom = false;
      }
    },
    async initFormDetail() {
      const that = this;
      try {
        let parameter = { id: that.nowId };
        that.loading = true;
        const response = await databaseMonitoringFindById(parameter);
        that.loading = false;
        this.findDatabases(response.data.data.nodeId);
        setTimeout(() => {
          that.ruleForm = response.data.data;
        }, 200);

        if (this.modalObjjj === "详情") {
          that.ProhibitFrom = true;
          // 禁用编辑器
        } else if (this.modalObjjj === "编辑") {
          that.ProhibitFromm = true;
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

    // 关闭模态框 清空所有表单项 为编辑器初始化
    CloseModal() {
      const that = this;
      that.ClearTextConeten();
      that.ProhibitFrom = false;
    },

    // 初始化表单
    ClearTextConeten() {
      const that = this;
      that.$refs["ruleForm"].resetFields();
      that.ruleForm.rom = "";
      that.ruleForm.ram = "";
      that.ruleForm.databaseId = "";
      that.ruleForm.nodeId = "";
      that.ruleForm.alarmWay = "";
      that.ruleForm.peopleNotified = "";
      that.ProhibitFrom = false;
      that.ProhibitFromm = false;
      that.dialogFormVisible = false;
      if (that.ruleForm.id) {
        delete that.ruleForm.id;
      }
    },

    SuretoAddClick(ruleForm) {
      const that = this;
      that.$refs[ruleForm].validate(index => {
        if (index === false) {
          return false;
        }
        let alarmWay = this.ruleForm.alarmWay;
        let peopleNotified = this.ruleForm.peopleNotified;
        let str = peopleNotified.replace(/，/gi, ",");
        let arr = str.split(",");
        if (peopleNotified !== "") {
          try {
            let reg = /^[1][3,4,5,7,8][0-9]{9}$/;
            if (alarmWay === "email") {
              reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
            }
            arr.forEach(item => {
              if (!reg.test(item) && item) {
                throw new Error(item);
              }
            });
          } catch (e) {
            that.loading = false;
            let msg = "手机号【" + e.message + "】格式不正确";
            if (alarmWay === "email") {
              msg = "邮箱【" + e.message + "】格式不正确";
            }
            that.$message.error(msg);
            return false;
          }
        }
        Object.entries(that.ruleForm).map((a, b) => {
          if (a[0] === "id") {
            delete that.ruleForm[a[0]];
          }
        });
        if (this.modalObjjj === "添加") {
          that.add(that.ruleForm);
          return false;
        } else if (this.modalObjjj === "编辑") {
          that.ruleForm.id = that.nowId;
          that.edit(that.ruleForm);
          return false;
        }
      });
    },

    // 添加方法
    async add(data) {
      const that = this;
      let da = data;
      let nodeName = "";
      this.nodeList.forEach(item => {
        if (this.ruleForm.nodeId === item.id) {
          nodeName = item.name;
        }
      });
      let databaseName = "";
      this.databasesList.forEach(item => {
        if (this.ruleForm.databaseId === item.id) {
          databaseName = item.name;
        }
      });
      da.nodeName = nodeName;
      da.databaseName = databaseName;
      that.loading = true;
      const response = await databaseMonitoringInsert(da);
      that.loading = false;
      if (response.data.code === 1) {
        that.$message.success(response.data.msg);
        that.ClearTextConeten();
        that.$emit("Reload");
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },

    // 修改方法
    async edit(data) {
      const that = this;
      that.loading = true;
      const response = await databaseMonitoringUpdate(data);
      that.loading = false;
      if (response.data.code === 1) {
        that.$message.success(response.data.msg);
        that.ClearTextConeten();
        that.$emit("Reload");
      } else {
        that.$message.error(response.data.msg);
        return false;
      }
    },

    async findNodes() {
      const res = await databaseMonitoringFindAllNodes();
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        });
      });
    },

    nodeChange(val) {
      this.ruleForm.databaseId = "";
      this.databasesList = [];
      this.findDatabases(val);
    },
    async findDatabases(nodeId) {
      let data = {
        nodeId: nodeId
      };
      const res = await databaseMonitoringFindAllDatabases(data);
      res.data.data.forEach(item => {
        this.databasesList.push({
          id: item.id,
          name: item.name
        });
      });
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
