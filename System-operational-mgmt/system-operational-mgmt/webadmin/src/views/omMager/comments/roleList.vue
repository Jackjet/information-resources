<template>
  <el-main>
    <template>
      <el-col :span="24" class="warp-main" v-loading="isSubmitLoading">
        <el-form label-width="120px">
          <el-row class='el-row-el ele-tree ml' v-if="data2.length">
            <div style="text-align: center;margin: 30px 0;">管理系统菜单权限</div>
            <div style="text-align: left;">
              <el-checkbox :indeterminate="isTreeAll" v-model="checkAllTree" @change="TreeAllChange">全选</el-checkbox>
            </div>
            <el-tree :data="data2" :render-content="renderContent" show-checkbox @check-change="TreeChange" :default-checked-keys="choose" default-expand-all node-key="id" ref="tree" highlight-current :props="defaultProps"></el-tree>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item class="el-button-el">
                <el-button type="primary" @click="onPageReturn">取消</el-button>
                <el-button :disabled="isAdmin" type="primary" @click="onSubmit">确定</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
    </template>
  </el-main>
</template>

<script>
import { getRolePermissionList, insertRolePermission } from '@/api/omManger/comments'
export default {
  data () {
    return {
      id: '',
      data2: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      choose: [],
      chooseArr: [],
      checkAllTree: false,
      isTreeAll: true,
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
      let that = this
      if (that.checkAllTree) {
        // 全选
        that.$refs.tree.setCheckedNodes(that.data2)
      } else {
        // 取消选中
        that.$refs.tree.setCheckedKeys([])
      }
    },
    TreeChange (data, checked, indeterminate) {
      let that = this
      let nowLength = that.$refs.tree.getCheckedKeys().concat(that.$refs.tree.getHalfCheckedKeys()).length
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
    // 详情
    async getFindList () {
      const that = this
      try {
        // let url = this.api.role.getRoleList
        const response = await getRolePermissionList({ roleId : this.id })
        if (response.code === 1) {
          let arr = []
          response.data.forEach(item => {
            arr.push(item.permissionId)
          })

          that.choose = arr
          return false
        }
        that.$message.error(response.msg)
      } catch (even) {
        that.$message.error('数据获取失败')
      }
    },
    // 列表
    async getList () {
      const that = this
      try {
        const response = await getRolePermissionList({ roleId : this.id })
        if (response.data.code === 1) {
          that.isSubmitLoading = false
          that.data2 = response.data.data.menuTree
          that.choose = response.data.data.choose
        //   that.getFindList()
          return false
        }
        that.$message.error(response.msg)
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
          menuTreeIds: this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys())
        }
        const response = await insertRolePermission(obj)
        if (response.data.code === 1) {
          that.isSubmitLoading = false
          that.$message.success(response.data.msg)
        //   that.$router.push({ path: 'roleList' })
          this.$router.push({ path: 'permissionView' })
          // that.getList()
          return false
        }
        that.$message.error(response.msg)
      } catch (even) {
        console.log(even)
        that.$message.error('数据获取失败')
      }
    },
   onPageReturn() {
      this.$router.push({ path: 'permissionView' })
   }

  }
}
</script>
