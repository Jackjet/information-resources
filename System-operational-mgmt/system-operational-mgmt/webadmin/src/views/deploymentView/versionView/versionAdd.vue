<template>
  <el-drawer
    :title="modalObjjj"
    direction="rtl"
    v-loading="loading"
    :visible.sync= "dialogFormVisible"
    :before-close="CloseModal"
    custom-class="demo-drawer"
    size='48%'
    :wrapperClosable='false'
    ref="drawer">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item class="InpitWidth" label="安装路径:" style="text-align:left" prop="installPathId">
        <el-select style="width: 100%;" :disabled="disabled" clearable size="medium" v-model="ruleForm.installPathId" placeholder="请选择安装路径" @change="pathChange">
          <el-option
            v-for="item in installPathList"
            :label="item.path"
            :value="item.id"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="InpitWidth" label="节点名称:" style="text-align:left" prop="nodeName">
        <el-input autocomplete="off" disabled v-model="ruleForm.nodeName" placeholder="请选择节点名称"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="系统名称:" style="text-align:left" prop="sysName">
        <el-input autocomplete="off" disabled v-model="ruleForm.sysName" placeholder="请选择系统名称"></el-input>
      </el-form-item>
      
      <el-form-item class="InpitWidth" label="版本号:" prop="versionNumber">
        <el-input clearable autocomplete="off" :disabled="disabled || isUpload" v-model="ruleForm.versionNumber" placeholder="请输入版本号" @change="versionChange"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="部署状态:" v-if="id">
        <el-input disabled autocomplete="off" v-model="typeStageText"></el-input>
      </el-form-item>

      <el-form-item label="部署包:" prop="automatedPackPath">
        <el-input style="display:none;" clearable autocomplete="off" v-model="ruleForm.automatedPackPath"></el-input>
        <div style="display:flex; color: #58c5f5;" v-if="ruleForm.automatedPackPath">
          {{ruleForm.automatedPackPath}}
          <el-button v-if="!disabled" style="margin-left: 10px;" type="danger" size="mini" round @click="deleteZip">
            删除
          </el-button>
        </div>
        <div v-if="!disabled">
          <el-upload
            class="upload-demo"
            accept=".zip, .ZIP"
            :action="fileUrl"
            :headers="header"
            :data="uploadData"
            :show-file-list="false"
            :before-upload="handleContentChange"
            :on-success="handleAvatarSuccess"
          >
            <el-button size="small" type="primary">
              {{btnText}}
              <i class="el-icon-upload el-icon--right"></i>
            </el-button>
            <div slot="tip" class="el-upload__tip" style="color:red;">
              注：上传的部署程序包需要是zip格式的完整程序压缩包（该功能用于程序有大版本更新时使用）
            </div>
          </el-upload>
        </div>
      </el-form-item>
      <el-form-item class="InpitWidth" label="部署接口url:" prop="apiUrl">
        <el-input clearable autocomplete="off" :disabled="disabled" v-model="ruleForm.apiUrl" placeholder="请输入部署接口url"></el-input>
      </el-form-item>
      <el-form-item class="InpitWidth" label="方法:" style="text-align:left" prop="apiMethod">
        <el-select style="width: 100%;" clearable :disabled="disabled" size="medium" v-model="ruleForm.apiMethod" placeholder="请选择方法">
          <el-option label="get" value="get"></el-option>
          <el-option label="post" value="post"></el-option>
          <el-option label="put" value="put"></el-option>
          <el-option label="delete" value="delete"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="InpitWidth" label="备注:">
        <el-input type="textarea" placeholder="请输入备注" v-model="ruleForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <el-button @click="CloseModal">取 消</el-button>
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">确 定</el-button>
    </div>
  </el-drawer>
</template>

<script>
import {
  installPathFindAll,
  installPathFindById,
  deploymentInsert,
  deploymentUpdate,
  deploymentFindById,
  dataApi,
  deleteDeploymentFile
} from '@/api/deploymentView/http'
let validateName = (rule, value, callback) => {
  const reg = /^\d+(\.\d+){2}$/
  if (!value) {
    callback(new Error(rule.msg))
  } if (!reg.test(value)) {
    callback(new Error('只允许3位'))
  } else {
    callback()
  }
}
export default {
  props: {
    modalObjjj: {
      type: String,
      default: () => {}
    }
  },

  data () {
    return {
      btnText: '上传',
      id: '',
      disabled: false,
      installPathList: [],
      apiList: [],
      typeStage: '',
      typeStageText: '',
      ruleForm: {
        installPathId: '',
        nodeId: '',
        nodeName: '',
        sysId: '',
        sysName: '',
        versionNumber: '',
        automatedPackPath: '',
        apiUrl: '',
        apiMethod: '',
        remark: ''
      },
      rules: {
        installPathId: [{
          required: true,
          message: '请选择安装路径',
          trigger: ['change', 'blur']
        }],
        nodeName: [{
          required: true,
          message: '请输入节点名称',
          trigger: ['change', 'blur']
        }],
        sysName: [{
          required: true,
          message: '请输入系统名称',
          trigger: ['change', 'blur']
        }],
        versionNumber: [{
          required: true,
          validator: validateName,
          msg: '请输入版本号',
          trigger: ['change', 'blur']
        }],
        automatedPackPath: [{
          required: true,
          message: '请上传部署包',
          trigger: ['change', 'blur']
        }],
        apiUrl: [{
          required: true,
          message: '请输入部署接口url',
          trigger: ['change', 'blur']
        }],
        apiMethod: [{
          required: true,
          message: '请选择方法',
          trigger: ['change', 'blur']
        }],
      },
      fileUrl: process.env.VUE_APP_BASE_API + "/webadmin/service/file/uploadDeploymentFile?type=deployment",
      header: {
        Authorization: "token " + localStorage.getItem("token")
      },
      uploadData: {
        nodeId: '',
        systemId: '',
        versionNumber: '',
        isUpdate: ''
      },
      isUpload: false,
      dialogFormVisible: this.show,
      loading: false,
    }
  },
  watch: {
    show () { this.dialogFormVisible = this.show }
  },
  methods: {
    initial (row) {
      this.installPathList = []
      this.apiList = []
      this.dialogFormVisible = true
      this.id = row.id
      this.getInstallPathList()
      this.getApiList()
      if (row.id) {
        this.uploadData.isUpdate = '1'
        this.typeStage = row.typeStage
        switch (row.typeStage) {
          case "NotDeployed":
            this.typeStageText = "未部署"
            break;
          case "Deploying":
            this.disabled = true
            this.typeStageText = "部署中"
            break;
          case "Deployed":
            this.disabled = true
            this.typeStageText = "已部署"
            break;
          case "NotDeployAble":
            this.disabled = true
            this.typeStageText = "不可部署"
            break;
          case "InitDeployAble":
            this.disabled = true
            this.typeStageText = "初次部署"
            break;
        }
        this.details()
      }
    },
    async getInstallPathList () {
      let data = {
        page: '1',
        size: '10000'
      }
      const res = await installPathFindAll(data)
      res.data.data.content.forEach(item => {
        this.installPathList.push({
          id: item.id,
          path: item.path
        })
      })
    },
    async getApiList () {
      let data = {
        page: '1',
        size: '10000'
      }
      const res = await dataApi(data)
      res.data.data.content.forEach(item => {
        this.apiList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    async pathChange (e) {
      const res = await installPathFindById({id: e})
      let Data = res.data.data
      this.uploadData.nodeId = Data.nodeId
      this.uploadData.systemId = Data.sysId

      this.ruleForm.nodeId = Data.nodeId
      this.ruleForm.nodeName = Data.nodeName
      this.ruleForm.sysId = Data.sysId
      this.ruleForm.sysName = Data.sysName
    },
    versionChange (e) {
      this.uploadData.versionNumber = e
    },
    handleContentChange(file) {
      let that = this;
      let FileSize = file.size / 1024 / 1024 < 500;
      let nodeId = that.ruleForm.nodeId
      let systemId = that.ruleForm.sysId
      let versionNumber = that.ruleForm.versionNumber
      console.log(this.uploadData)
      if (!systemId || !nodeId) {
        that.$message.error("请选择安装路径");
        return false;
      }
      if (!versionNumber) {
        that.$message.error("请输入版本号");
        return false;
      }
      if (!FileSize) {
        that.$message.error("上传超出文件限制大小");
        return false;
      }
      that.loading = true
      that.btnText = '正在上传...'
      that.isSubmitLoading = true;
    },
    handleAvatarSuccess(response, file, fileList) {
      let that = this;
      that.btnText = '上传'
      that.loading = false
      if (response.code === 1) {
        that.isUpload = true
        that.ruleForm.automatedPackPath = response.data.fileDownloadUri;
        that.$message.success(response.msg);
        return false;
      }
      that.$message.error(response.msg);
    },
    async deleteZip () {
      if (this.id) {
        this
          .$confirm("删除此文件后不可恢复，请确认是否删除?", "提示", {
            type: "warning",
          })
          .then(() => {
            this.deleteFn()
          })
          .catch(() => {
            return false;
          });
        return false
      }
      this.deleteFn()
    },
    async deleteFn () {
      let that = this;
      let str = this.ruleForm.automatedPackPath
      let fileName = str.replace('downloadDeploymentFile/', '');
      let data = {
        type: 'deployment',
        nodeId: this.uploadData.nodeId,
        systemId: this.uploadData.systemId,
        versionNumber: this.uploadData.versionNumber,
        fileName: fileName
      }
      const res = await deleteDeploymentFile(data)
      if (res.data.code === 1) {
        that.ruleForm.automatedPackPath = '';
        that.isUpload = false
        // that.$message.success(res.data.msg);
        // if (this.id) {
        //   this.SuretoAddClick('ruleForm')
        // }
        return false;
      }
      that.$message.error(res.data.msg);
    },
    async details () {
      const res = await deploymentFindById({ id: this.id })
      this.isUpload = true
      this.ruleForm = res.data.data
      this.uploadData.nodeId = res.data.data.nodeId
      this.uploadData.systemId = res.data.data.sysId
      this.uploadData.versionNumber = res.data.data.versionNumber
    },

    CloseModal (isSave) {
      if (this.id && isSave !== '1' && this.ruleForm.automatedPackPath === '') {
        this.$message.error('请上传部署包');
        return false
      }
      if (!this.id && isSave !== '1' && this.ruleForm.automatedPackPath !== '') {
        this.deleteZip()
      }
      this.$refs['ruleForm'].resetFields()
      this.dialogFormVisible = false
      this.id = ''
      this.typeStage = ''
      this.disabled = false
      this.isUpload = false
      this.ruleForm = {
        installPathId: '',
        nodeId: '',
        nodeName: '',
        sysId: '',
        sysName: '',
        versionNumber: '',
        automatedPackPath: '',
        apiUrl: '',
        apiMethod: '',
        remark: ''
      }
      this.uploadData = {
        nodeId: '',
        systemId: '',
        versionNumber: '',
        isUpdate: ''
      }
    },

    SuretoAddClick (ruleForm) {
      const that = this
      that.$refs[ruleForm].validate(async (index) => {
        if (index === false) {
          return false
        }
        if (that.loading) {
          return false
        }
        that.loading = true
        let data = {
          installPathId: this.ruleForm.installPathId,
          nodeId: this.ruleForm.nodeId,
          nodeName: this.ruleForm.nodeName,
          sysId: this.ruleForm.sysId,
          sysName: this.ruleForm.sysName,
          versionNumber: this.ruleForm.versionNumber,
          automatedPackPath: this.ruleForm.automatedPackPath,
          apiUrl: this.ruleForm.apiUrl,
          apiMethod: this.ruleForm.apiMethod,
          remark: this.ruleForm.remark,
          type: 'deployment'
        }
        let response = null
        if (that.id) {
          data.id = that.id
          response = await deploymentUpdate(data)
        } else {
          response = await deploymentInsert(data)
        }
        that.loading = false
        if (response.data.code === 1) {
          that.$message.success(response.data.msg)
          that.$emit('getList')
          that.CloseModal('1')
          return false
        }
        that.$message.error(response.data.msg)
      })
    }
  }
}
</script>

<style lang="scss">
  @import "~@/styles/drawer.scss";
</style>
