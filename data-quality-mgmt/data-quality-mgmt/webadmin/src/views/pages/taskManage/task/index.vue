<template>
    <el-contain>
        <el-row :gutter="20" v-loading="loading">
            <el-col :span="6">
                <div class="container">
                    <div class="container" style="box-shadow:2px 2px; border-radius: 10px;box-shadow: 0 0 10px #c0c4cc;min-height: 80vh;">
                        <div>
                            <div>
                                <el-input
                                    placeholder="请输入搜索名称"
                                    v-model="filterText">
                                </el-input>
                            </div>
                            <div style="margin-top:5px;">
                                <el-button class='noClick' size="medium" icon="el-icon-refresh-left" @click="forestReset"></el-button>
                                <el-button class='noClick' size="medium" icon="el-icon-plus" @click="forestInsert"></el-button>
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
                            <el-button size="medium" @click="deleteGroup">删除</el-button>
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="18">
                <div class="container">
                    <el-form :inline="true" class='el-InputForm'>
                        <el-form-item>
                            <el-input size="medium" clearable placeholder="请输入任务名称" v-model="dataSourceSearchItem.name"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-select size="medium" clearable placeholder="请选择任务状态" v-model="dataSourceSearchItem.status">
                                <el-option label="已启动" value="1"></el-option>
                                <el-option label="未启动" value="0"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item>
                            <el-button size="medium" icon="el-icon-search" @click="getDataSourceList">查询</el-button>
                        </el-form-item>
                    </el-form>
                    <div style="padding-top: 20px;">
                         <el-button type="primary" plain size="medium" icon="el-icon-plus" @click="dataSourceInsert">新增</el-button>
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
                            label="任务名称">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            label="执行周期">
                            <template slot-scope="scope">
                                <span  v-if="scope.row.cycle == 0" >一次</span>
                                <span  v-if="scope.row.cycle == 1" >每日</span>
                                <span  v-if="scope.row.cycle == 2" >每周</span>
                                <span  v-if="scope.row.cycle == 3" >每月</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            prop="count"
                            label="规则数量">
                        </el-table-column>
                        <el-table-column
                            align="center"
                            label="任务状态">
                            <template slot-scope="scope">
                                <span  v-if="scope.row.status == 1" style='color:#70B603'>已启动</span>
                                <span  v-else style='color:#D70C0C'>未启动</span>
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            label="最后执行时间">
                            <template slot-scope="scope">
                                {{scope.row.lastTime}}
                            </template>
                        </el-table-column>
                        <el-table-column
                            align="center"
                            label="操作"
                            min-width="135px">
                            <template slot-scope="scope">
                                <el-button type="text" v-if="scope.row.status == 0" @click="dataSourceUpdate(scope.row)">编辑</el-button>
                                <el-button type="text" v-if="scope.row.status == 0"  @click="changeStatus(scope.row)">启动</el-button>
                                <el-button type="text" v-else @click="changeStatus(scope.row)">停止</el-button>
                                <el-button type="text" @click="excute(scope.row)">手动执行</el-button>
                                <el-button type="text" @click="goLog(scope.row)">查看日志</el-button>
                                <el-button type="text" v-if="scope.row.status == 0" @click="dataSourceDelete(scope.row)">删除</el-button>
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
import {taskFindAll,updateStatus,taskGroupFindAll,taskGroupUpdate,taskGroupInsert,taskGroupDelete,taskDeleteById,excuteOnce} from '@/api/task.js'
import $ from 'jquery'
import Pagination from '@/components/table/Pagination'
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

            //右侧任务
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
    async created() {
        const that = this
        await that.getDataSourceGroup()
        that.getDataSourceList()
    },
    methods: {
        goLog(row){
            const that = this
            that.$router.push({
                path: 'taskManageLog',
                query: {
                    type:'查看日志',
                    groupId: that.thisNodeId,
                    id:row.id,
                    name:row.name
                }
            })
        },
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
            let response = await taskGroupFindAll(param)
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
            let response = await taskGroupInsert(data)
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
            let response = await taskGroupUpdate(data)
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
                    let response = await taskGroupDelete(that.thisNodeId)
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
        //查询任务
        async getDataSourceList(){
            const that = this
            let params = that.dataSourceSearchItem
            params.groupId = that.thisNodeId
            params.size = that.pageSize
            params.page = that.currentPage

            that.loading = true
            let response = await taskFindAll(params)
            that.loading = false

            if(response.data.code === 1) {
                that.dataSource = response.data.data.content
                that.total = response.data.data.totalElements
            } else {
                that.$message.error(response.data.msg)
            }
        },

        //添加任务
        dataSourceInsert(){
            const that = this
            that.$router.push({
                path: 'taskManageAdd',
                query: {
                    type:'添加',
                    groupId: that.thisNodeId
                }
            })
        },
        // 执行一次
        excute(row){
            const that = this
            that.$confirm('执行一次任务, 是否继续?', '提示', {
                type: 'warning'
            }).then(async () => {
                let response = await excuteOnce({id:row.id})
                if(response.data.code === 1) {
                    that.$message.success('执行成功')
                    await that.getDataSourceList()
                } else {
                    that.$message.error(response.data.msg)
                }
            })
        },
        //编辑任务
        dataSourceUpdate(row){
            const that = this
            that.$router.push({
                path: 'taskManageEdit',
                query: {
                    type:'编辑',
                    id:row.id
                }
            })
        },
        //删除任务
        dataSourceDelete(row){
            const that = this
            that.$confirm('删除任务, 是否继续?', '提示', {
                type: 'warning'
            }).then(async () => {
                let response = await taskDeleteById(row.id)
                if(response.data.code === 1) {
                    that.$message.success('删除成功')
                    await that.getDataSourceList()
                } else {
                    that.$message.error(response.data.msg)
                }
            })
        },

        //启用或禁用任务
        changeStatus(row){
            const that = this
            let message = '停止'
            if(row.status === '0'){
                message = '启用'
            }

            that.$confirm(message + '任务, 是否继续?', '提示', {
                type: 'warning'
            }).then(async () => {
                let params = {
                    id: row.id
                }
                let response = await updateStatus(params)

                if(response.data.code === 1) {
                    that.$message.success(message + '成功')
                    await that.getDataSourceList()
                } else {
                    that.$message.error(response.data.msg)
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
