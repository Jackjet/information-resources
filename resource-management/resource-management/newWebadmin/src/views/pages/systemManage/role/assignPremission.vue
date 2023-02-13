<template>
  <el-main>
    <template>
      <el-col :span="24" class="warp-main" v-loading="isSubmitLoading">
        <el-form label-width="120px">
          <el-row class='el-row-el ele-tree ml' v-if="dataArray.length">
            <div style="text-align: center;margin: 30px 0;">管理系统菜单权限</div>
             <el-form-item class="el-button-el" label="角色权限名称:" :label-width="this.formLabelWidth">
                <el-input clearable style="width: 220px;"
                    :disabled="true"
                    size="medium"
                    placeholder="请输入角色权限名称"
                    v-model="name">
                </el-input>
            </el-form-item>
            <div style="text-align: left;">
              <el-checkbox :indeterminate="isTreeAll" v-model="checkAllTree" @change="TreeAllChange">全选</el-checkbox>
            </div>
            <el-tree :data="dataArray" :render-content="renderContent" show-checkbox @check-change="TreeChange" :default-checked-keys="choose" default-expand-all node-key="id" ref="tree" highlight-current :props="defaultProps"></el-tree>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item class="el-button-el">
                <el-button type="" @click="onPageReturn">取 消</el-button>
                <el-button :disabled="isAdmin" type="primary" @click="onSubmit">保 存</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
    </template>
  </el-main>
</template>

<script>
import { roleMenuTreeInsert,  findAllRoleIdChoose } from "@/api/role.js"
import { menuTreeFindall } from "@/api/menu.js"
export default {
  data () {
    return {
      id: '',
      dataArray: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      name: '',
      choose: [],
      checkAllTree: false,
      formLabelWidth: '120px',
      isTreeAll: false,
      isAdmin: false,
      isSubmitLoading: false,
      TreeLength: 0,
      createTimes: ''
    }
  },

  mounted () {
    let that = this
    that.isSubmitLoading = true
    that.id = that.$route.query.id
    that.name = that.$route.query.name
    that.isAdmin = that.$route.query.name === 'admin'
    that.getList()
  },

  methods: {
    renderContent(h, { node, data }) {
      let dom;
      // 懒加载图标设置
      dom = (
              <p class="custom-tree-node">
                <img src={require("@/assets/image/role.png")}></img>
                <span style="margin-left:5px;" title={data.desc}>
                  {node.label}
                </span>
              </p>
            );
      return dom;
    },
    TreeAllChange (val) {
      const that = this
      if(val){
        that.checkAllTree = true
        that.isTreeAll = false
      }else{
        that.isTreeAll = false
        that.checkAllTree = false
      }
      if (that.checkAllTree) {
        // 全选
        that.$refs.tree.setCheckedNodes(that.dataArray)
      } else {
        // 取消选中
        that.$refs.tree.setCheckedKeys([])
      }
    },
    TreeChange (data, checked, indeterminate) {
      let that = this
      let nowLength = that.$refs.tree.getCheckedKeys().concat(that.$refs.tree.getHalfCheckedKeys()).length
      that.TreeLength=that.$refs.tree.treeItems.length
      if (nowLength === 0) {
        that.checkAllTree = false
        that.isTreeAll = false
      } else if (nowLength === that.TreeLength) {
        that.checkAllTree = true
        that.isTreeAll = false
      } else {
        that.checkAllTree = false
        that.isTreeAll = true
      }
    },
    // 列表
    async getList () {
      const that = this
      try {
        that.isSubmitLoading = true
        const response = await menuTreeFindall()
        if (response.data.code === 1) {
          that.isSubmitLoading = false
          let arrTree = JSON.stringify(response.data.data)
          let arrChoose = JSON.stringify(response.data.data)
          that.dataArray = that.getJsonTreeAssiagn(JSON.parse(arrTree), null)
          this.findCurrent()
        }else{
          that.$message.error(response.data.msg)
        }
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    getJsonTreeAssiagn(data, parentId) {
      const that = this
      let itemArr = []
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId || Boolean(parentId) === Boolean(node.parentId)) {
          let newNode = {};
          newNode.id = node.id
          newNode.label = node.name
          if (node.children.length > 0) {
            newNode.children = that.getJsonTreeAssiagn(node.children, node.id);
          }
          itemArr.push(newNode)
        }
      }
      return itemArr
    },
    // 列表
    async findCurrent () {
      const that = this
      let data = {
        roleId: that.id,
      }
      try {
        that.isSubmitLoading = true
        const response = await findAllRoleIdChoose(data)
        that.isSubmitLoading = false
        if (response.data.code === 1) {
          that.choose =response.data.data
          if(that.choose.length === 0){
            that.isTreeAll = false
          }else{
            that.isTreeAll = true
          }
          return false
        }
        that.$message.error(response.data.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    // 提交数据
    async onSubmit () {
      let that = this
      try {
        let obj = {
          roleId: that.id,
          // menuTreeIds: this.$refs.tree.getCheckedKeys()
          menuTreeIds: this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
        }
        const response = await roleMenuTreeInsert(obj)
        if (response.data.code === 1) {
          that.isSubmitLoading = false
          that.$message.success('分配权限成功')
          this.onPageReturn()
          return false
        }
        that.$message.error(response.data.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    onPageReturn() {
      this.$router.push('/role')
    }
  }
}
</script>
<style lang="scss" scoped>
  .el-main{
    background: #00000000;
    height: 100%;
    width: 100%;
    padding: 0 30px;
    min-height: 600px;
  }
  .warp-main{
    background: #fff;
    padding: 0px 15px;
    margin-top: 20px;
    border-radius: 5px;
    min-height: 350px;
  }
  .el-button-el{
    margin-top: 20px;
  }
</style>
