<template>
    <el-contain>
        <el-row :gutter="20" v-loading="loading">
            <el-col :span="6">
                <div class="container">
                    <div class="container" style="box-shadow:2px 2px; border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;min-height: 80vh;">
                        <div>
                            <div>
                                <el-input
                                    placeholder="输入关键字进行过滤"
                                    v-model="filterText">
                                </el-input>
                            </div>
                            <div style="margin-top:5px;">
                                <el-button size="medium" icon="el-icon-refresh-left" @click="forestReset"></el-button>
                                <el-button size="medium" icon="el-icon-plus" @click="forestInsert"></el-button>
                            </div>
                        </div>

                        <el-tree
                            ref="tree"
                            class="filter-tree"
                            :data="forestData"
                            :props="defaultProps"
                            default-expand-all
                            :filter-node-method="filterNode"
                            style="margin-top:10px;"
                            @node-click="nodeClick">
                        </el-tree>
                        <div>
                            <el-button size="medium" icon="el-icon-edit" @click="forestUpdate">重命名</el-button>
                            <el-button size="medium" icon="el-icon-delete" @click="deleteGroup">删除</el-button>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="18">
                <div class="container">
                    <el-form :inline="true" class='el-InputForm'>
                        <el-form-item>
                            <el-input size="medium" clearable placeholder="请输入数据源名称" v-model="dataSourceSearchItem.name"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-select size="medium" clearable placeholder="请选择数据源类型" v-model="dataSourceSearchItem.type">
                                <el-option label="Mysql" value="Mysql"></el-option>
                                <el-option label="Postgresql" value="Postgresql"></el-option>
                                <el-option label="Oracle" value="Oracle"></el-option>
                                <el-option label="SqlServer" value="SqlServer"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button size="medium" icon="el-icon-search" @click="getDataSourceList">查询</el-button>
                        </el-form-item>
                    </el-form>
                    <div style="padding-top: 20px;"> 
                        <el-button type="primary" plain  size="medium" icon="el-icon-plus" @click="dataSourceInsert">新增</el-button>
                    </div>  
                    <el-table
                        :data="dataSource"
                        stripe
                        style="width: 100%;min-height:64vh;margin-top: 15px;"
                        :header-cell-style="headerStyle"
                        :cell-style="cellStyle"
                        class='el_tab_alage'>
                        <el-table-column
                            align="center"
                            prop="name"
                            label="数据源名称">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="type"
                            label="数据源类型"
                            width="100px">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            label="数据源地址"
                            min-width="100px">
                            <template slot-scope="scope">
                                {{scope.row.ip}}:{{scope.row.port}}
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="createTime"
                            label="创建时间"
                            min-width="100px">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            label="操作"
                            min-width="135px">
                            <template slot-scope="scope">
                                <template v-if="scope.row.status === '0'">
                                    <el-button type="text" icon="el-icon-edit" @click="dataSourceUpdate(scope.row)">编辑</el-button>
                                    <el-button type="text" icon="el-icon-delete" @click="dataSourceDelete(scope.row)">删除</el-button>
                                </template>
                                <template v-if="scope.row.status === '1'">
                                    <el-button type="text" icon="el-icon-turn-off" @click="dataSourceEnableOrForbidden(scope.row)">禁用</el-button>
                                </template>
                                <template v-else>
                                    <el-button type="text" icon="el-icon-open" @click="dataSourceEnableOrForbidden(scope.row)">启用</el-button>
                                </template>
                                <el-button type="text" icon="el-icon-search" @click="dataUnit(scope.row)">元数据</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <pagination ref="page" :total="total" @pageChange="pageChange"></pagination>
                </div>
            </el-col>
        </el-row>

        <!--对话框-->
        <el-dialog
            title="添加组"
            :visible.sync="groupInsertDialog"
            width="30%"
            :before-close="insertHandleClose"
            class="container">
            <el-form ref="groupInsertForm" :model="groupInsertForm" :rules="groupInsertFormRule" class="container">
                <el-form-item prop="name" label="组名" :label-width="formLabelWidth">
                    <el-input v-model="groupInsertForm.name"></el-input>
                </el-form-item>
            </el-form>
            <div class="footbar">
                <el-button icon="el-icon-check" @click="forestInsertSubmit">保存</el-button>
            </div>
        </el-dialog>
        <el-dialog
            title="重命名"
            :visible.sync="groupUpdateDialog"
            width="30%"
            :before-close="updateHandleClose"
            class="container">
            <el-form ref="groupUpdateForm" :model="groupUpdateForm" :rules="groupUpdateFormRule" class="container">
                <el-form-item prop="name" label="组名" :label-width="formLabelWidth">
                    <el-input v-model="groupUpdateForm.name"></el-input>
                </el-form-item>
            </el-form>
            <div class="footbar">
                <el-button icon="el-icon-check" @click="forestUpdateSubmit">保存</el-button>
            </div>
        </el-dialog>
    </el-contain>
</template>
<script>
import {getDataSourceGroup, groupInsert,groupUpdate,groupDelete,getDataSource,deleteDataSourceById,dataSourceEnableOrForbidden} from "@/api/sourceManage.js";
import Pagination from '@/components/table/Pagination'
import $ from 'jquery'
export default {
    components: {Pagination},
    data() {
        return {
            //页面全局
            loading:false,
            formLabelWidth: '20%',

            //左侧森林部分
            filterText:'',
            thisNodeId: '',
            forestData:[{

            }],
            defaultProps: {
                children: 'children',
                label: 'name'
            },

            //添加组
            groupInsertDialog:false,
            groupInsertForm:{
                name:''
            },
            groupInsertFormRule:{
                name: [{
                    required: true,
                    message: '请输入组名称',
                    trigger: ['blur', 'change'],
                }]
            },

            //重命名
            groupUpdateDialog:false,
            groupUpdateForm:{
                name:''
            },
            groupUpdateFormRule:{
                name: [{
                    required: true,
                    message: '请输入组名称',
                    trigger: ['blur', 'change'],
                }]
            },

            //右侧数据源
            dataSourceSearchItem:{
                name:'',
                type:''
            },
            dataSource: [],
            total: 0,
            pageSize: '20',
            currentPage: '1'
        }
    },
    watch: {
        filterText(val) {
            this.$refs.tree.filter(val);
        }
    },

    methods: {
        //左侧forest
        filterNode(value, data) {
            if (!value) return true
            return data.name.indexOf(value) !== -1
        },
        nodeClick(v1,v2,v3){
            $('.el-tree-node__content').css('background-color','white')
            let dom = $(v3.$el).find('.el-tree-node__content').eq(0)
            let bool = dom.attr('choose')
            let id = ''
            if(bool == 'true'){
                $(v3.$el).find('.el-tree-node__content').eq(0).css('background-color','white')
                dom.attr('choose',false)
                id = ''
            }else{
                $(v3.$el).find('.el-tree-node__content').eq(0).css('background-color','#F5F7FA')
                $('.el-tree-node__content').attr('choose',false)   
                dom.attr('choose',true)   
                id = v1.id
            }
            const that = this
            that.thisNodeId = id
            that.getDataSourceList()
        },

        async getDataSourceGroup (){
            const that = this
            let param = {}

            that.loading = true
            let response = await getDataSourceGroup(param)
            that.loading = false

            if(response.data.code === 1){
                that.forestData = response.data.data
            } else {
                that.$message.error(response.data.msg)
            }
        },
        forestReset(){
            const that = this
            that.filterText = ''
            that.thisNodeId = ''
            that.getDataSourceGroup()
            that.getDataSourceList()
        },
        //添加组
        forestInsert(){
            const that = this
            that.groupInsertDialog = true
        },
        forestInsertSubmit(){
            const that = this
            let data = that.groupInsertForm
            if(that.thisNodeId !== undefined && that.thisNodeId !== ''){
                data.parentId = that.thisNodeId
            }else{
               delete data['parentId'] 
            }

            that.$refs["groupInsertForm"].validate((valid) => {
                if(valid){
                    that.saveforestInsertSubmit(data)
                }
            })
        },
        async saveforestInsertSubmit(data){
            const that = this
            that.loading = true
            let response = await groupInsert(data)
            that.loading = false
            if(response.data.code === 1){
                that.insertHandleClose()
                that.$message.success('添加成功')
                this.thisNodeId = ''
            } else{
                that.$message.error(response.data.msg)
            }
            that.getDataSourceGroup()
        },     
        insertHandleClose(){
            const that = this
            that.groupInsertDialog = false
            that.$refs['groupInsertForm'].resetFields();
        },

        //重命名
        forestUpdate(){
            const that = this
            if(that.thisNodeId === undefined || that.thisNodeId === ''){
                that.$message.error('请选择组')
                return false
            }else{
                that.groupUpdateDialog = true
            }
        },
        forestUpdateSubmit(){
            const that = this
            let data = that.groupUpdateForm
            if(that.thisNodeId === undefined || that.thisNodeId === ''){
                that.$message.error('请选择组')
                return false
            }else{
                data.id = that.thisNodeId

                that.$refs["groupUpdateForm"].validate((valid) => {
                    if(valid){
                        that.saveforestUpdateSubmit(data)
                    }
                })
            }
        },
        async saveforestUpdateSubmit(data){
            const that = this
            that.loading = true
            let response = await groupUpdate(data)
            that.loading = false
            if(response.data.code === 1){
                that.updateHandleClose()
                that.$message.success('修改成功')
            } else{
                that.$message.error(response.data.msg)
            }
            that.getDataSourceGroup()
        },
        updateHandleClose(){
            const that = this
            that.groupUpdateDialog = false
            that.$refs['groupUpdateForm'].resetFields();
        },

        //删除组
        deleteGroup(){
            const that = this
            if(that.thisNodeId === undefined || that.thisNodeId === ''){
                that.$message.error('请选择组')
                return false
            }else{
                that.$confirm('删除组数据, 是否继续?', '提示', {
                    type: 'warning'
                }).then(async () => { 
                    let params = {
                        id :that.thisNodeId
                    }

                    that.loading = true
                    let response = await groupDelete(params)
                    that.loading = false

                    if(response.data.code === 1){
                        that.$message.success('删除成功')
                        that.forestReset()
                    }else {
                        that.$message.error(response.data.msg)
                    }
                }).catch(() => {
                    return false
                })
            }
        },

        //右侧
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
        //查询数据源
        async getDataSourceList(){
            const that = this
            let params = that.dataSourceSearchItem
            params.groupId = that.thisNodeId
            params.size = that.pageSize
            params.page = that.currentPage
            
            that.loading = true
            let response = await getDataSource(params)
            that.loading = false

            if(response.data.code === 1) {
                that.dataSource = response.data.data.content
                that.total = response.data.data.totalElements
            } else {
                that.$message.error(response.data.msg)
            }
        },

        //添加数据源
        dataSourceInsert(){
            const that = this
            that.$router.push({ 
                path: '/datasource/manage/add',
                query: {
                    type:'add',
                    groupId: that.thisNodeId
                }
            })
        },

        //编辑数据源
        dataSourceUpdate(row){
            const that = this
            that.$router.push({ 
                path: '/datasource/manage/edit',
                query: {
                    type:'edit',
                    id:row.id
                }
            })
        },
        //删除数据源
        dataSourceDelete(row){
            const that = this
            that.$confirm('删除数据源, 是否继续?', '提示', {
                type: 'warning'
            }).then(async () => { 
                let params = {
                    id: row.id
                }
                let response = await deleteDataSourceById(params)

                if(response.data.code === 1) {
                    that.$message.success('删除成功')

                    await that.getDataSourceList()
                } else {
                    that.$message.error(response.data.msg)
                }
            })
        },

        //启用或禁用数据源
        dataSourceEnableOrForbidden(row){
            const that = this
            let message = '禁用'
            if(row.status === '0'){
                message = '启用'
            }

            that.$confirm(message + '数据源, 是否继续?', '提示', {
                type: 'warning'
            }).then(async () => { 
                let params = {
                    id: row.id
                }
                let response = await dataSourceEnableOrForbidden(params)

                if(response.data.code === 1) {
                    that.$message.success(message + '成功')

                    await that.getDataSourceList()
                } else {
                    that.$message.error(response.data.msg)
                }
            })
        },

        //查看元数据
        dataUnit(row){
            const that = this
            that.$router.push({ 
                path: '/datasource/manage/dataUnit',
                query: {
                    dataSourceId:row.id,
                    dataBaseName:row.dataBaseName
                }
            })
        },

        // 翻页
        pageChange(item) {
            let that = this
            that.pageSize = item.limit
            that.currentPage = item.page
            that.getDataSourceList()
        }
    },
    async created() {
        const that = this
        await that.getDataSourceGroup()
        that.getDataSourceList()
    }
}
</script>

<style lang="scss" scoped>
    .el_tab_alage {
        width: 99.5% !important;
        border:1px solid #f0f0f0 !important;
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
                bottom:0 !important;
                left: 0 !important;
                }
            }
        }
    }
</style>

<style>
    .container {
        padding: 15px;
    }

    .footbar {
        padding: 0 15px 0 15px;
        display: flex;
        flex-direction: row-reverse;
    }
    .searchItem {
        width: 200px;
    }
</style>
