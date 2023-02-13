<template>
  <el-main>
    <el-col :span="24">
      <el-table
        ref="multipleTable"
        :data="table.list.data"
        empty-text="暂无数据"
        class="el_tab_alage"
      >
        <template v-for="(item, index) in table.list.header">
          <el-table-column
            fit
            align="center"
            :key="index"
            :label="item.label"
            :width="item.width"
            :prop="item.field"
          >
            <template slot-scope="scope">
              {{ scope.row[item.field] }}
            </template>
          </el-table-column>
        </template>
      </el-table>
    </el-col>
  </el-main>
</template>

<script>
import { findTwo } from "@/api/http";
import TableList from "@/components/table/TableList.vue";
let idNum = 0;
export default {
  components: { TableList },
  data() {
    return {
      table: {
        list: {
          header: [
            {
              label: "姓名",
              field: "name",
            },
            {
              label: "手机号",
              field: "tel",
            },
            {
              label: "地址",
              field: "road",
            },
            {
              label: "类型",
              field: "type",
            }
          ],

          data: [],
        },
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    async getList() {
      try {
        const res = await findTwo({});
        console.log(res.data)
        this.table.list.data = res.data;
      } catch (even) {
        console.log(even);
        this.$message.error(even.msg);
      }
    }
  },
};
</script>
