<template>
  <div class="table-box">
    <el-table :data="tableData" border style="width: 100%">
      <el-table-column
        v-for="(item, index) in tableHeader"
        :key="index"
        :prop="item.prop"
        :label="item.label"
        :width="item.width"
      >
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
export default {
  props: {
    tableHeader: {
      type: Array,
      default: () => {
        return [];
      },
    },
    tableData: {
      type: Array,
      default: () => {
        return [];
      },
    },
  },
};
</script>