<template>
  <el-main class="main">
    <div><h4>{{ title }}</h4></div>
    <el-col class="main-center">
      <el-form
        :model="ruleForm"
        v-loading="isSubmitLoading"
        :rules="rules"
        ref="ruleForm"
        label-width="150px"
        class="demo-ruleForm">
        <el-form-item label="名称:" :label-width="this.formLabelWidth" prop='name'>
          <el-input
            clearable
            maxlength='100'
            size="medium"
            :disabled="disabled"
            placeholder="请输入名称"
            v-model="ruleForm.name"
          ></el-input>
        </el-form-item>
        <el-form-item label="类型:" prop="type" :label-width="this.formLabelWidth">
          <el-select @change="typeChange" v-model="ruleForm.type" style="width: 100%;" clearable :disabled='disabled || isBtn' placeholder="请选择类型">
            <el-option key="0" label="菜单" value="0"></el-option>
            <el-option key="1" label="链接新窗口" value="1"></el-option>
            <el-option key="2" label="按钮" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          prop="icon"
          label="菜单图标:"
          :label-width="this.formLabelWidth"
          class='InpitWidth'>
          <el-select v-model="ruleForm.icon" style="width: 100%;" clearable :disabled='disabled' placeholder="请选择菜单图标">
            <el-option
              v-for="item in theIcon"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              <div class="icon-box">
                <span style="line-height: 1;" v-html="item.value"></span>
                <span style="margin-left: 10px;">{{ item.name }}</span>
              </div>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-show="ruleForm.type === '0'" label="绑定页面:" :label-width="this.formLabelWidth">
          <el-select v-model="ruleForm.path" style="width: 100%;" clearable :disabled='disabled' placeholder="请选择绑定页面">
            <el-option v-for="item in theRouter" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-show="ruleForm.type === '1'" label="页面地址:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" placeholder="请输入页面地址" v-model="ruleForm.path"></el-input>
        </el-form-item>
        <el-form-item v-show="ruleForm.type === '2'" label="绑定字段:" :label-width="this.formLabelWidth">
          <el-input clearable maxlength='100' size="medium" placeholder="请输入绑定字段" v-model="ruleForm.path"></el-input>
        </el-form-item>
        <div class="demo-drawer__footer drawer_footer">
          <el-button size="medium" @click="goBack">取 消</el-button>
          <el-button size="medium" type="primary" v-if="!disabled" @click="submitForm('ruleForm')">保 存</el-button>
        </div>
      </el-form>
    </el-col>
  </el-main>

</template>

<script>
import {menuTreeInsert, menuTreeUpdate} from "@/api/menu.js"
import { icons } from '@/icons/iconList'
import bus from '@/utils/bus'

export default {
  components: {},
  data() {
    return {
      title: '新增',
      theIcon: [],
      theRouter: [],
      disabled: false,
      isBtn: false,
      isSubmitLoading: false,
      ruleForm: {
        id: '',
        name: '',
        icon: '',
        type: '',
        path: '',
        parentId: ''
      },
      formLabelWidth: "120px",
      rules: {
        name: [{
          required: true,
          message: "请输入名称",
          trigger: "change",
        }],
        type: [{
          required: true,
          message: '请选择类型',
          trigger: ['change', 'blur']
        }]
      },
    }
  },
  created() {
    this.theIcon.splice(0)
    for (let i in icons) {
      this.theIcon.push({id: i, name: icons[i].label, value: icons[i].icon})
    }
    this.theRouter.splice(0)
    this.$router.options.routes.forEach(item => {
      if (item.path === '/') {
        item.children.forEach(sub => {
          this.theRouter.push({name: sub.label, value: sub.path})
        })
      }
    })
    this.title = this.$route.query.handle
    if (this.$route.query.handle === '新增') {
      if (this.$route.query.level === '三级') {
        this.ruleForm.type = '2'
        this.isBtn = true
      }
      this.ruleForm.parentId = this.$route.query.parentId
    } else if (this.$route.query.handle === '编辑') {
      if (this.$route.query.level === '四级') {
        this.ruleForm.type = '2'
        this.isBtn = true
      }
      this.ruleForm = this.$route.query
    }
  },
  methods: {
    typeChange () {
      this.ruleForm.path = ''
    },
    submitForm(ruleForm) {
      this.$refs["ruleForm"].validate((valid) => {
        if (valid) {
          this.isSubmitLoading = true
          // 取值
          const that = this
          let data = JSON.parse(JSON.stringify(that.ruleForm))
          if (this.title === '编辑') {
            // 编辑
            menuTreeUpdate(data).then((res) => {
              this.isSubmitLoading = false
              if (res.data.code === 1) {
                bus.$emit('permissionData')
                this.$message.success('编辑成功')
                this.goBack()
              } else {
                this.$message.error(res.data.msg)
              }
            })
          } else {
            // 新增
            menuTreeInsert(data).then((res) => {
              this.isSubmitLoading = false
              if (res.data.code === 1) {
                bus.$emit('permissionData')
                this.$message.success('新增成功')
                this.goBack()
              } else {
                this.$message.error(res.data.msg)
              }
            })
          }
        } else {
          return false
        }
      })
    },
    goBack() {
      this.$router.push('/menus')
    }
  }
}
</script>
<style lang="scss" scoped>
  .icon-box {
    display: flex;
    align-items: center;
    height: 100%;
    /deep/ svg {
      width: 20px;
      height: 20px;
    }
  }
 .main{
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: 5px;
  padding: 20px;
  .main-center{
    padding: 20px;
    background-color: #fff;
    border-radius: 5px;
    width: 65%;
    margin: 0 auto;
  }
 }
 .demo-drawer__footer{
   margin-top: 80px;
   text-align: center;
 }
</style>
