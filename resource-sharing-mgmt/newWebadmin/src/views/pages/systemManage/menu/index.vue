<template>
  <el-main class="main">
    <el-col :span="24" class="center">
      <div class="addMenu">
        <el-button type="primary" v-if="permissions.add" plain size='mini' @click="handleAdd" icon="el-icon-plus">新增</el-button>
        <el-button type="primary" v-if="permissions.batchDelete" plain size='mini' @click="handleDelete" icon="el-icon-delete" style="margin-left:10px;">批量删除</el-button>
      </div>
      <el-table v-loading="isSubmitLoading" class="table-list" :cell-style="{'font-size': '14px','height': '40px', 'padding': '0'}" :header-cell-style="{'font-size': '14px', 'height': '40px', 'padding': '0', 'background-color': '#F5F7FA'}" row-key="id" @expand-change='expandChange' :expand-row-keys="expand" :data="tableData" style="width: 100%; margin-top: 10px;" stripe="true" indent="16" @selection-change="handleSelectionChange" :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column align="center" type="selection" width="50"> </el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="type" align="center" label="类型"></el-table-column>
        <el-table-column prop="levelMsg" align="center" label="层级"></el-table-column>
        <el-table-column prop="seq" align="center" label="排序">
          <template slot-scope="scope">
            <template v-if="scope.row.seq === -1">
              <el-button type="text" size="medium" icon="el-icon-top" @click="menuSeq(0,scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq === 0">
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="menuSeq(1,scope.row)"></el-button>
            </template>
            <template v-else-if="scope.row.seq !== -1 && scope.row.seq !== 0 && scope.row.seq !== -2">
              <el-button type="text" size="medium" icon="el-icon-top" @click="menuSeq(0,scope.row)"></el-button>
              <el-button type="text" size="medium" icon="el-icon-bottom" @click="menuSeq(1,scope.row)"></el-button>
            </template>
            <!-- <span v-if="scope.row.level !==0">{{上移}}</span> -->
          </template>
        </el-table-column>
        <el-table-column align="center" label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.type !== '按钮' && permissions.add && scope.row.hasSystem === '0'" type="text" size="medium" icon="el-icon-plus" @click="handleAdd(scope.row)">新增
            </el-button>
            <el-button v-if="permissions.edit && scope.row.hasSystem === '0'" type="text" size="medium" icon="el-icon-edit-outline" @click="handleEdit(scope.row)">编辑
            </el-button>
            <el-button v-if="permissions.delete && scope.row.hasSystem === '0'" type="text" size="medium" icon="el-icon-delete" @click="handleSigleDelete(scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-main>
</template>

<script>
import { menuTreeFindall, menuTreeUpdateSeq, menuTreeDeleteAll, menuTreedelete } from "@/api/menu.js"
// import bus from '@/utils/bus'

export default {
  data() {
    return {
      permissions: {
        add: false,
        edit: false,
        delete: false,
        batchDelete: false
      },
      aaa: 'asdf',
      bbb: this.aaa,
      expand: [],
      tableData: [],
      isSubmitLoading: false,
      DeletelistiD: []
    }
  },
  computed: {
    user() {
      if (this.$store.state.user.expand) {
        this.expand = JSON.parse(this.$store.state.user.expand)
      }
    }
  },
  created() {
    this.$store.dispatch('setUserButtons').then(res => {
      let permissionsArr = JSON.parse(res);
      permissionsArr.forEach(item => {
        let itemArr = item.split('_')
        if (('/' + itemArr[0]) === this.$route.path) {
          this.permissions[itemArr[1]] = true;
        }
      })
    })
    this.fetchData()
  },
  methods: {
    expandChange(row) {
      let theIndex = -1
      this.expand.forEach((item, index) => {
        if (item === row.id) {
          theIndex = index
        }
      })
      if (theIndex === -1) {
        this.expand.push(row.id)
      } else {
        this.expand.splice(theIndex, 1)
      }
      this.$store.dispatch("setExpand", JSON.stringify(this.expand))
    },
    async fetchData() {
      const data = {}
      const that = this
      try {
        that.isSubmitLoading = true
        const res = await menuTreeFindall(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          that.tableData = that.getJsonTree(res.data.data, '')
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
        if (node.parentId === parentId || Boolean(parentId) === Boolean(node.parentId)) {
          let newNode = {};
          newNode.id = node.id
          newNode.name = node.name
          newNode.parentId = node.parentId
          newNode.level = node.level
          newNode.levelMsg = node.levelMsg
          newNode.parentName = node.parentName
          newNode.icon = node.icon
          newNode.path = node.path
          newNode.hasSystem = node.hasSystem
          newNode.typeIndex = node.type

          if (node.type === 0) {
            newNode.type = '菜单'
          } else if (node.type === 1) {
            newNode.type = '链接新窗口'
          } else if (node.type === 2) {
            newNode.type = '按钮'
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
    async menuSeq(type, row) {
      const that = this
      const data = {
        action: type,
        id: row.id
      }
      try {
        that.isSubmitLoading = true
        const res = await menuTreeUpdateSeq(data)
        that.isSubmitLoading = false
        if (res.data.code === 1) {
          this.fetchData()
        } else {
          this.$message.error(res.data.msg)
        }
      } catch (even) {
        that.isSubmitLoading = false
        this.$message.error(even.msg)
      }
    },
    //全选
    handleSelectionChange(vals) {
      const that = this
      that.DeletelistiD = []
      vals.map(function (v, k) {
        that.DeletelistiD.push(v.id)
      })
    },
    // 添加
    handleAdd(data) {
      const that = this
      that.$router.push({
        path: '/menuAdd',
        query: {
          parentId: data.id,
          level: data.levelMsg,
          handle: '新增'
        }
      })
    },
    // 编辑
    handleEdit: function (data) {
      const that = this
      that.$router.push({
        path: '/menuEdit',
        query: {
          id: data.id,
          level: data.levelMsg,
          name: data.name,
          path: data.path,
          icon: data.icon,
          parentId: data.parentId,
          type: data.typeIndex,
          handle: '编辑'
        }
      })
    },
    // 删除当前数据 重载列表
    async handleDelete(data) {
      const that = this
      if (that.DeletelistiD.length === 0) {
        return that.$message.warning('请先选择删除菜单')
      }
      that.$confirm('请确认是否批量删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await menuTreeDeleteAll(that.DeletelistiD + '')
        that.Loading = false
        if (response.data.code === 1) {
          that.$message.success('批量删除成功')
          this.fetchData()
        } else {
          that.$message.error(response.data.msg)
        }
      }).catch(() => {
        return false
      })
    },

    // 删除当前单条数据 重载列表
    async handleSigleDelete(data) {
      const that = this
      that.$confirm('请确认是否删除?', '提示', {
        type: 'warning'
      }).then(async () => {
        that.Loading = true
        const response = await menuTreedelete(data.id)
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
