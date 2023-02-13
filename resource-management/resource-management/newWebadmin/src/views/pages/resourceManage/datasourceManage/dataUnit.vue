<template>
  <el-contain>
    <div class="data_unit_container" v-loading="loading">
      <el-row>
        <el-col>
          <div class="data_unit_container">
            <i class="el-icon-s-data" style="height: 34px;width:34px;"></i>
            <span style="display:inline-block;height: 34px;line-height:34px;flex-shrink: 0;font-size:18px;">{{dataBaseName}}</span>
            <el-button size="medium" icon="el-icon-back" class="header_unit" @click="goBack">返回列表</el-button>
            <el-button size="medium" icon="el-icon-refresh" class="header_unit" @click="updateDataUnit">更新元数据</el-button>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col>
          <el-table :data="dataUnit" stripe style="width: 100%;min-height:64vh;margin-top: 15px;" :header-cell-style="headerStyle" :cell-style="cellStyle" class='el_tab_alage'>
            <el-table-column align="center" prop="name" label="表名">
            </el-table-column>
            <el-table-column align="center" prop="createTime" label="更新时间">
              <template slot-scope="scope">
                {{scope.row.createTime.split(' ')[0]}}
              </template>
            </el-table-column>

            <el-table-column align="center" label="操作" min-width="135px">
              <template slot-scope="scope">
                <el-button type="text" icon="el-icon-search" @click="getDataUnitFields(scope.row)">显示字段</el-button>
                <el-button type="text" icon="el-icon-refresh" @click="updateDataUnitFields(scope.row)">更新字段</el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
        </el-col>
      </el-row>
    </div>
  </el-contain>
</template>
<script>
import { getDataUnit, updateDataUnit, updateDataUnitFields, getDataUnitFields } from "@/api/sourceManage.js"
import Pagination from '@/components/table/Pagination'
export default {
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataSourceId: '',
      dataBaseName: '',
      dataUnit: [],
      total: 0,
      pageSize: '20',
      currentPage: '1',
      type: ''
    }
  },
  methods: {
    //查询元数据
    async getDataUnit() {
      const that = this
      let params = {
        dataSourceId: that.dataSourceId,
        size: that.pageSize,
        page: that.currentPage
      }

      that.loading = true
      let response = await getDataUnit(params)
      that.loading = false

      if (response.data.code === 1) {
        that.dataUnit = response.data.data.content
        that.total = response.data.data.totalElements
      } else {
        that.$message.error(response.data.msg)
      }
    },

    //更新元数据
    async updateDataUnit() {
      const that = this

      that.$confirm('更新元数据会删除实体表下的字段信息，需要重新更新字段信息, 是否继续?', '提示', {
        type: 'warning'
      }).then(async () => {
        let data = {
          dataSourceId: that.dataSourceId
        }

        that.loading = true
        let response = await updateDataUnit(data)
        that.loading = false

        if (response.data.code === 1) {
          that.$message.success('更新成功')
          await that.getDataUnit()
        } else {
          that.$message.error(response.data.msg)
        }
      })
    },

    //返回上一级
    goBack() {
      const that = this

      that.$router.push({
        path: '/datasource'
      })
    },

    //表格样式
    headerStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0',
        background: '#F5F7FA'
      }
    },
    cellStyle() {
      return {
        'font-size': '14px',
        height: '40px',
        padding: '0'
      }
    },

    // 翻页
    pageChange(item) {
      let that = this
      that.pageSize = item.limit
      that.currentPage = item.page
      that.getDataUnit()
    },

    //更新字段
    async updateDataUnitFields(row) {
      const that = this

      let data = {
        dataSourceId: row.dataSourceId,
        dataUnitId: row.id
      }

      that.loading = true
      let response = await updateDataUnitFields(data)
      that.loading = false

      if (response.data.code === 1) {
        that.$message.success('更新成功')
      } else {
        that.$message.error(response.data.msg)
      }
    },
    //查询字段
    getDataUnitFields(row) {
      const that = this
      that.$router.push({
        path: '/datasourceDataField',
        query: {
          dataUnitId: row.id,
          tableName: row.name,
          dataSourceId: that.dataSourceId,
          dataBaseName: that.dataBaseName,
          type: that.type
        }
      })
    }

  },
  created() {
    const that = this
    that.dataSourceId = that.$route.query.dataSourceId
    that.dataBaseName = that.$route.query.dataBaseName
    that.type = that.$route.query.type

    that.getDataUnit()
  }
}
</script>

<style lang="scss" scoped>
.el_tab_alage {
  width: 99.5% !important;
  border: 1px solid #f0f0f0 !important;
  border-bottom: none !important;
  margin: {
    left: 0px;
    bottom: 20px !important;
    top: 20px !important;
  }

  .el-table__row {
    .cell {
      -webkit-line-clamp: 3 !important;
      -webkit-box-orient: vertical !important;
    }
    .el-button {
      margin: {
        top: 0 !important;
        right: 5px !important;
        bottom: 0 !important;
        left: 0 !important;
      }
    }
  }
}
</style>

<style>
.header {
  display: flex;
  flex-direction: row;
  padding: 15px;
}

.header_button {
  display: flex;
  flex-direction: row;
  padding: 15px;
}

.header_unit {
  margin-left: 10px;
  float: right;
}
.data_unit_container {
  padding: 15px;
}
</style>
