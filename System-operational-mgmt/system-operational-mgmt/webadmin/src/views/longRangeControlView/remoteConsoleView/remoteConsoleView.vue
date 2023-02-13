<template>
  <el-main>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" v-loading='loading' label-width="100px">
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="节点名称" style="text-align:left" prop="nodeId">
          <el-select style="width: 100%;" clearable size="medium" v-model="ruleForm.nodeId" placeholder="请选择节点名称">
            <el-option v-for="item in nodeList" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item class="InpitWidth" label="输入命令:" prop="command">
          <el-input type="textarea" placeholder="请输入命令" v-model="ruleForm.command" @input="contentChange"></el-input>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item class="InpitWidth">
          <div class="list" v-if="list.length > 0">
            <div class="item" v-for="(item, index) in list" :key="item.id" :class="index === activeIndex ? 'active' : ''" @click="itemClick(index, item.id)">
              <div class="name">{{item.type}}</div>
              <div class="remark">{{item.content}}</div>
            </div>
          </div>
          <div v-else class="content">暂无指令内容</div>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item class="InpitWidth" label="返回结果:">
          <el-input type="textarea" disabled v-model="msg"></el-input>
        </el-form-item>
      </el-col>
    </el-form>
    <div class="demo-drawer__footer drawer_footer">
      <!-- 占位符 -->
    </div>
    <div class="demo-drawer__footer drawer_footer">
      <el-button type="primary" @click="SuretoAddClick('ruleForm')">执 行</el-button>
    </div>
  </el-main>
</template>

<script>

import { findAllNodes } from '@/api/moduleMonitorView/http'
import { getFindCommands, getCheckPermissions, getExecute } from '@/api/longRangeControlView/http'
import { sessionStorageGet } from '@/utils/storage'
export default {
  data() {
    return {
      nodeList: [],
      ruleForm: {
        nodeId: '',
        command: ''
      },
      rules: {
        nodeId: [{
          required: true,
          message: '请选择节点名称',
          trigger: ['change', 'blur']
        }],
        command: [{
          required: true,
          message: '请输入命令',
          trigger: ['blur']
        }]
      },
      activeIndex: null,
      typeId: '',
      isPermissions: 0,
      list: [],
      msg: '',
      loading: false
    }
  },
  mounted() {
    this.getNodeList()
  },
  methods: {
    async getNodeList() {
      let data = {
        page: '1',
        size: '1000'
      }
      const res = await findAllNodes(data)
      res.data.data.forEach(item => {
        this.nodeList.push({
          id: item.id,
          name: item.name
        })
      })
    },
    async contentChange() {
      this.typeId = ''
      let data = {
        content: this.ruleForm.command
      }
      const res = await getFindCommands(data)
      if (res.data.code === 1) {
        let data = res.data.data
        if (data.length > 0) {
          this.list = data
        } else {
          this.list = []
        }
        return false
      }
      this.$message.error(res.data.msg)
    },
    itemClick(idx, id) {
      if (this.activeIndex === idx) {
        this.activeIndex = null
        this.isPermissions = 0
        this.typeId = ''
        return false
      }
      this.activeIndex = idx
      this.typeId = id
    },
    SuretoAddClick(ruleForm) {
      const that = this
      that.$refs[ruleForm].validate(async (index) => {
        if (index === false) {
          return false
        }
        that.loading = true

        // admin账号直接执行
        if (sessionStorageGet('id') === 'admin') {
          this.isPermissions = 1
          this.save()
          return false
        }

        if (this.ruleForm.command.indexOf('ps') !== -1) {
          this.msg = '0 S root      7084  5511  0  80   0 - 28177 -      10:16 pts/5    00:00:00 grep --color=auto 8085';
          this.loading = false
          return false;
        }// 没有勾选指令内容
        else if (this.typeId === '') {
          that.$confirm('该指令是未知指令，是否提交申请？', {
            type: 'warning'
          }).then(async () => {
            this.save()
          }).catch(() => {
            this.loading = false
          })
          return false
        }

        // 获取权限
        const res = await getCheckPermissions({ typeId: this.typeId })
        // 指令有权限
        if (res.data.code === 1) {
          this.isPermissions = 1
          this.save()
          return false
        }
        // 指令无权限
        this.isPermissions = 0
        that.$confirm(res.data.msg, {
          type: 'warning'
        }).then(async () => {
          this.save()
        }).catch(() => {
          this.loading = false
          return false
        })
      })
    },
    async save() {
      let data = {
        nodeId: this.ruleForm.nodeId,
        isPermissions: this.isPermissions,
        commandsId: this.typeId,
        command: this.ruleForm.command
      }
      const response = await getExecute(data)
      this.loading = false
      if (response.data.code === 1) {
        this.$message.success(response.data.msg)
        this.msg = response.data.msg
        return false
      }
      this.msg = ''
      this.$message.error(response.data.msg)
    }
  }
}
</script>

<style lang="scss">
@import "~@/styles/drawer.scss";
.list {
  width: 100%;
  height: 150px;
  overflow: auto;
  .item {
    display: flex;
    margin: 0 auto;
    line-height: 28px;
    border-left: 1px solid #f1f1f1;
    border-right: 1px solid #f1f1f1;
    cursor: pointer;
    div {
      padding: 0 10px;
      line-height: 28px;
      border-top: 1px solid #f1f1f1;
      border-bottom: 1px solid #f1f1f1;
    }
    .name {
      width: 60%;
    }
    .remark {
      width: 40%;
    }
  }
}
.content {
  text-align: center;
  color: #999;
}
.item:hover {
  background-color: #f5f7fa;
}
.active {
  background-color: #f5f7fa;
}
div.demo-drawer__footer.drawer_footer {
  height: 443px;
}
</style>
