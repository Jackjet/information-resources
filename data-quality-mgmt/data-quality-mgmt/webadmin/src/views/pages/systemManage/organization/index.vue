<template>
  <el-main class="main">
    <el-col :span="24" style="padding-bottom: 20px; background-color: #fff;">
      <el-table v-loading="isSubmitLoading" class="table-list" :cell-style="{'font-size': '14px','height': '40px', 'padding': '0'}" :header-cell-style="{'font-size': '14px', 'height': '40px', 'padding': '0', 'background-color': '#F5F7FA'}" :data="tableData" style="width: 100%" row-key="id" stripe="true" indent="16" default-expand-all='true'>
        <el-table-column prop="name" label="组织机构名称"></el-table-column>
        <el-table-column prop="levelMsg" label="层级" align="center"></el-table-column>
        <el-table-column prop="seq" align="center" label="排序">
          <template slot-scope="scope">
            <template v-if="scope.row.seq === -1">
              <el-button type="text" size="medium" icon="el-icon-top" @click="organizationSeq(0, scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq === 0">
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="organizationSeq(1, scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq !== -1 && scope.row.seq !== 0 && scope.row.seq !== -2">
              <el-button type="text" size="medium" icon="el-icon-top" @click="organizationSeq(0, scope.row)"></el-button>
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="organizationSeq(1, scope.row)"></el-button>
            </template>
            <!-- <span v-if="scope.row.level !==0">{{上移}}</span> -->
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button
              type="text"
              size="medium"
              icon="el-icon-plus"
              v-if="permissions.add"
              @click="handleAdd(scope.row)">新增</el-button>
            <el-button
              type="text"
              size="medium"
              icon="el-icon-edit-outline"
              v-if="permissions.edit"
              @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
              size="medium"
              type="text"
              icon="el-icon-delete"
              v-if="scope.row.levelMsg !== '一级' && permissions.delete"
              @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column> -->
      </el-table>
    </el-col>
  </el-main>
</template>

<script>
import { organizationFindAll, organizationUpdateSeq, organizationDelete } from "@/api/organization.js"
export default {
  data() {
    return {
      permissions: {
        add: false,
        edit: false,
        delete: false
      },
      tableData: [],
      isSubmitLoading: false
    }
  },
  created() {
    let permissionsArr = JSON.parse(sessionStorage.getItem("UserButtons"))
    permissionsArr.forEach(item => {
      let itemArr = item.split('_')
      if (('/' + itemArr[0]) === this.$route.path) {
        this.permissions[itemArr[1]] = true
      }
    })
    this.fetchData()
  },
  methods: {
    async fetchData() {
      const data = {}
      const that = this
      try {
        that.isSubmitLoading = true
        const res = await organizationFindAll(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          that.tableData = []
          let datas = []
          datas.push(res.data.data)
          that.tableData = that.getJsonTree(datas, '')
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        that.isSubmitLoading = false
        this.$message.error(even.msg)
      }
    },
    getJsonTree(data, parentId) {
      const that = this
      let itemArr = []
      for (let i = 0; i < data.length; i++) {
        let node = data[i];
        if (node.parentId === parentId) {
          let newNode = {};
          newNode.id = node.id
          newNode.component = node.component
          newNode.name = node.name
          newNode.parentId = node.parentId
          newNode.level = node.level
          newNode.levelMsg = node.levelMsg
          newNode.parentName = node.parentName
          newNode.redirect = node.redirect
          newNode.meta = node.meta
          newNode.path = node.path
          newNode.child = node.child
          newNode.typeIndex = node.type

          if (node.type === 0) {
            newNode.type = '菜单'
          } else if (node.type === 1) {
            newNode.type = '功能按钮'
          } else if (node.type === 2) {
            newNode.type = '链接新窗口'
          } else if (node.type === 3) {
            newNode.type = '链接内页'
          } else if (node.type === 4) {
            newNode.type = '内置功能页'
          }
          if (i !== 0 && i === data.length - 1) {
            newNode.seq = -1
          } else if (data.length === 1) {
            newNode.seq = -2
          } else {
            newNode.seq = node.seq
          }
          if (node.children.length > 0) {
            newNode.children = that.getJsonTree(node.children, node.id);
          }
          itemArr.push(newNode)
        }
      }
      return itemArr;
    },
    // 上下移动
    async organizationSeq(type, row) {
      const that = this
      const data = {
        action: type,
        id: row.id
      }
      try {
        that.isSubmitLoading = true
        const res = await organizationUpdateSeq(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.fetchData()
        } else {
          that.isSubmitLoading = false
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    // 添加
    handleAdd(data) {
      const that = this
      that.$router.push({
        path: '/organizationAdd',
        query: {
          id: data.id,
          name: data.name,
          level: data.level,
          levelMsg: data.levelMsg,
          type: '新增'
        }
      })
    },
    handleEdit: function (data) {
      const that = this
      that.$router.push({
        path: '/organizationEdit',
        query: {
          id: data.id,
          parentId: data.parentId,
          parentName: data.parentName,
          name: data.name,
          level: data.level,
          levelMsg: data.levelMsg,
          type: '编辑'
        }
      })
    },
    // 删除当前单条数据 重载列表
    handleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await organizationDelete(data.id)
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },
  }
}
</script>
<style lang="scss" scoped>
</style>