<template>
    <el-contain>
        <div class="data_field_container" v-loading="loading">
            <el-row>
                <el-col>
                    <div class="data_field_container">
                        <i class="el-icon-s-data" style="height: 34px;width:34px;"></i>
                        <span style="display:inline-block;height: 34px;line-height:34px;flex-shrink: 0;font-size:18px;">{{tableName}}</span>
                        <el-button size="medium" icon="el-icon-back" class="header_unit" @click="goBack">返回列表</el-button>
                    </div>
                </el-col>
            </el-row>
            <el-row>
                <el-col>
                    <el-table
                        :data="dataUnitField"
                        stripe
                        style="width: 100%;min-height:64vh;margin-top: 15px;"
                        :header-cell-style="headerStyle"
                        :cell-style="cellStyle"
                        class='el_tab_alage'>
                        <el-table-column
                            align="center"
                            prop="columnName"
                            label="字段名">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="columnType"
                            label="字段类型">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="columnKey"
                            label="是否为主键">
                            <template slot-scope="scope">
                                <template v-if="scope.row.columnKey">
                                    是
                                </template>
                                <template v-else>
                                    否
                                </template>
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="isNullable"
                            label="是否可以为空">
                            <template slot-scope="scope">
                                <template v-if='scope.row.isNullable === "YES"'>
                                    是
                                </template>
                                <template v-else>
                                    否
                                </template>
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
import {getDataUnitFields} from "@/api/sourceManage.js";
import Pagination from '@/components/table/Pagination'
export default{
    components: {Pagination},
    data() {
        return {
            loading: false,
            dataUnitId:'',
            tableName:'',
            dataSourceId: '',
            dataBaseName: '',

            dataUnitField:[],
            total: 0,
            pageSize: '20',
            currentPage: '1'
        }
    },
    methods:{
        //表格样式
        headerStyle () {
            return {
                'font-size': '14px',
                height: '40px',
                padding: '0',
                background: '#F5F7FA'
            }
        },
        cellStyle () {
            return {
                'font-size': '14px',
                height: '40px',
                padding: '0'
            }
        },
        async getDataUnitFields(){
            const that = this

            let params = {
                dataUnitId: that.dataUnitId,
                size: that.pageSize,
                page: that.currentPage
            }

            that.loading = true
            let response = await getDataUnitFields(params)
            that.loading = false

            if(response.data.code === 1) {
                that.dataUnitField = response.data.data.content
                that.total = response.data.data.totalElements
            } else {
                that.$message.error(response.data.msg)
            }
        },
        // 翻页
        pageChange(item) {
            let that = this
            that.pageSize = item.limit
            that.currentPage = item.page
            that.getDataUnitFields()
        },
         //返回上一级
        goBack(){
            const that = this

            that.$router.push({ 
                path: '/datasource/manage/dataUnit',
                query: {
                    dataSourceId: that.dataSourceId,
                    dataBaseName: that.dataBaseName
                }
            })
        }
    },
    created(){
        const that = this
        that.dataUnitId = that.$route.query.dataUnitId
        that.tableName = that.$route.query.tableName
        that.dataSourceId = that.$route.query.dataSourceId
        that.dataBaseName = that.$route.query.dataBaseName
        
        that.getDataUnitFields()
    }
}
</script>

<style>
    .data_field_container{
        padding: 15px;
    }
</style>