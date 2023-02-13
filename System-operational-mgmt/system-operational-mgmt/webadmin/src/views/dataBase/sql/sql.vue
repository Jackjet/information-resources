<template>
  <el-main>
    <el-form
      :model="ruleForm"
      ref="ruleForm"
      :rules="rules"
      v-loading="loading"
      label-width="100px"
    >
      <el-col :span="12">
        <el-form-item
          class="InpitWidth"
          label="节点名称"
          style="text-align:left"
          prop="nodeId"
        >
          <el-select
            style="width: 100%;"
            clearable
            size="medium"
            v-model="ruleForm.nodeId"
            placeholder="请选择节点名称"
            @change="nodeChange"
          >
            <el-option
              v-for="item in nodeList"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item
          class="InpitWidth"
          label="数据库名称:"
          style="text-align:left"
          prop="databaseId"
        >
          <el-select
            style="width: 100%;"
            clearable
            size="medium"
            v-model="ruleForm.databaseId"
            placeholder="请选择数据库名称"
          >
            <el-option
              v-for="item in databasesList"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="输入SQL:" prop="content">
          <el-input
            type="textarea"
            :autosize="{ minRows: 8, maxRows: 8}"
            placeholder="请输入SQL"
            v-model="ruleForm.content"
          ></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="返回结果:">
          <el-input 
            type="textarea" 
            :autosize="{ minRows: 8, maxRows: 8}"
            disabled v-model="msg"
           ></el-input>
        </el-form-item>
      </el-col>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button type="primary" @click="SuretoAddClick('ruleForm')"
        >执 行</el-button
      >
    </div>
  </el-main>
</template>

<script>
import {
  sqlPost,
  getFindAllNodes,
  getFindAllDatabases
} from "@/api/dataBase/dataBase";
import { sessionStorageGet } from "@/utils/storage";
export default {
  data() {
    return {
      nodeList: [],
      databasesList: [],
      ruleForm: {
        nodeId: "",
        databaseId: "",
        content: ""
      },
      rules: {
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
        content: [
          {
            required: true,
            message: "请输入SQL",
            trigger: ["change", "blur"]
          }
        ]
      },
      msg: "",
      loading: false
    };
  },
  created() {
    this.nodeList = [];
    this.databasesList = [];
    this.getFindAllNodes();
  },
  methods: {
    nodeChange() {
      this.ruleForm.databaseId = "";
      this.databasesList = [];
      this.getNodeList();
    },
    async getFindAllNodes() {
      const res = await getFindAllNodes();
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        });
      });
    },
    async getNodeList() {
      let data = {
        nodeId: this.ruleForm.nodeId
      };
      const res = await getFindAllDatabases(data);
      res.data.data.forEach(item => {
        this.databasesList.push({
          id: item.id,
          name: item.name
        });
      });
    },
    async SuretoAddClick(ruleForm) {
      let that = this;
      that.$refs[ruleForm].validate(async index => {
        if (index === false) {
          return false;
        }
        that.loading = true;
        let data = {
          nodeId: this.ruleForm.nodeId,
          databaseId: this.ruleForm.databaseId,
          content: this.ruleForm.content
        };
        let arr = ["delete", "update", "create", "insert"];
        let str = this.ruleForm.content.toLowerCase();
        let isSave = true
        arr.map(item => {
          if (str.indexOf(item) > -1) {
            isSave = false
          }
        });
        if (sessionStorageGet("id") !== "admin" && !isSave) {
          that
            .$confirm("危险操作，没有权限执行，是否提交申请?", "提示", {
              type: "warning"
            })
            .then(async () => {
              const response = await sqlPost(data);
              that.loading = false;
              if (response.data.code === 1) {
                that.msg = response.data.data;
                that.$message.success(response.data.msg);
                return false;
              }
              that.msg = "";
              that.$message.error(response.data.msg);
            })
            .catch(() => {
              that.loading = false;
              return false;
            });
        } else {
          const response = await sqlPost(data);
          that.loading = false;
          if (response.data.code === 1) {
            that.msg = response.data.data;
            that.$message.success(response.data.msg);
            return false;
          }
          that.msg = "";
          that.$message.error(response.data.msg);
        }
      });
    }
  }
};
</script>

<style lang="scss">
@import "~@/styles/drawer.scss";
</style>
