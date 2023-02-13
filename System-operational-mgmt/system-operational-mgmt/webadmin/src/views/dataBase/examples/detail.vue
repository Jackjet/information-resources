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
          style="width:100%;"
          clearable
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
          style="width:100%;"
          clearable
          v-model="ruleForm.databaseId"
          :disabled="ProhibitFromm"
          placeholder="请选择数据库名称"
        >
          <el-option
            v-for="item in databaseList"
            :key="item.value"
            :label="item.name"
            :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="参数配置名称:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
        prop="configId"
      >
        <el-select
          style="width:100%;"
          clearable
          v-model="ruleForm.configId"
          :disabled="ProhibitFrom"
          @change="selectGett"
          placeholder="请选择参数配置名称"
        >
          <el-option
            v-for="item in configIdList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item
        label="实例名称:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
        prop="name"
      >
        <el-input
          clearable
          :disabled="ProhibitFromm"
          autocomplete="off"
          v-model.trim="ruleForm.name"
          placeholder="请输入实例名称"
        ></el-input>
      </el-form-item>
      <el-form-item
        label="备注:"
        :label-width="this.formLabelWidth"
        class="InpitWidth"
      >
        <el-input
          type="textarea"
          clearable
          :disabled="ProhibitFrom"
          autocomplete="off"
          v-model="ruleForm.remark"
          placeholder="请输入备注"
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
  parameterConfigList,
  findById,
  operationinsert,
  update,
  findAllNodes,
  findAllDatabases
} from "@/api/dataBase/dataBase";
export default {
  props: {
    modalObjjj: {
      type: String,
      default: () => {}
    }
  },

  data() {
    return {
      ruleForm: {
        configId: "",
        databaseId: "",
        name: "",
        remark: "",
        nodeId: ""
      },
      obj: {},
      objj: {},
      objjj: {},
      rule: {},
      nodeList: [],
      databaseList: [],
      configIdList: [],
      rules: {
        name: [
          {
            required: true,
            validator: this.common.english,
            msg: "请输入实例名称",
            trigger: ["change", "blur"]
          }
        ],
        nodeId: [
          {
            required: true,
            message: "请选择节点名称",
            trigger: ["change", "blur"]
          }
        ],
        databaseId: [
          {
            required: true,
            message: "请选择数据库名称",
            trigger: ["change", "blur"]
          }
        ],
        configId: [
          {
            required: true,
            message: "请选择参数配置名称",
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
      // Http: 'http://apptest.liangxin.net.cn/',
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
      this.databaseList = [];
      this.configIdList = [];
      this.findNodes();
      if (value) {
        that.nowId = value;
        that.initFormDetail(value);
      } else {
        that.ProhibitFrom = false;
        this.findList();
      }
    },

    selectGett(vId) {
      const that = this;
      that.objjj = this.configIdList.find(item => {
        return item.value === vId; //筛选出匹配数据
      });
    },
    async initFormDetail(value) {
      const that = this;
      try {
        let parameter = { id: value };
        that.loading = true;
        const response = await findById(parameter);
        that.loading = false;
        this.findDatabases(response.data.data.nodeId);
        this.findList();
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
      that.ruleForm.remark = "";
      that.ruleForm.name = "";
      that.ruleForm.databaseId = "";
      that.ruleForm.nodeId = "";
      that.ruleForm.configId = "";
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
      let nodeName = "";
      this.nodeList.forEach(item => {
        if (this.ruleForm.nodeId === item.id) {
          nodeName = item.name;
        }
      });
      let databaseName = "";
      this.databaseList.forEach(item => {
        if (this.ruleForm.databaseId === item.id) {
          databaseName = item.name;
        }
      });
      let da = data;
      da.nodeName = nodeName;
      da.configName = that.objjj.label;
      da.databaseName = databaseName;
      that.loading = true;
      const response = await operationinsert(da);
      that.loading = false;
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg);
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten();
        // 重新触发父组件加载loading
        that.$emit("Reload");
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false;
      }
    },

    // 修改方法
    async edit(data) {
      const that = this;
      // PUT 请求修改数据
      data.configName = that.objjj.label;
      that.loading = true;
      const response = await update(data);
      that.loading = false;
      if (response.data.code === 1) {
        // 添加成功后 回调效果
        that.$message.success(response.data.msg);
        /**
         * 清空表单元素 重置验证信息.
         * @param imageUrl
         * @param detail
         */
        that.ClearTextConeten();
        // 重新触发父组件加载loading
        that.$emit("Reload");
      } else {
        // 添加上传失败后 回调失败信息
        that.$message.error(response.data.msg);
        return false;
      }
    },

    async findNodes() {
      const res = await findAllNodes();
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        });
      });
    },

    nodeChange(val) {
      this.ruleForm.databaseId = "";
      this.databaseList = [];
      this.findDatabases(val);
    },

    async findDatabases(nodeId) {
      let data = {
        nodeId: nodeId
      };
      const res = await findAllDatabases(data);
      res.data.data.forEach(item => {
        this.databaseList.push({
          id: item.id,
          name: item.name
        });
      });
    },

    async findList() {
      const that = this;
      that.loading = true;
      const response = await parameterConfigList();
      that.loading = false;
      if (response.data.code === 1) {
        that.configIdList = [];
        response.data.data.content.map(function(v) {
          that.configIdList.push({ value: v.id, label: v.name });
        });
      } else {
        // 添加上传失败后 回调失败信息
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
