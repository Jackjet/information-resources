
<template>
    <el-main class="main">
        <div>
            <h4>{{ title }}</h4>
        </div>
        <el-col class="main-center">
            <el-form
                    :model="ruleForm"
                    :rules="rules"
                    ref="ruleForm"
                    label-width="150px"
                    class="demo-ruleForm"
            >
                <el-form-item label="API名称:" :label-width="this.formLabelWidth">
                    <el-input
                            maxlength="100"
                            size="medium"
                            :disabled="true"
                            placeholder=""
                            v-model="ruleForm.name"
                    ></el-input>
                </el-form-item>
                <el-form-item label="资源目录:" :label-width="this.formLabelWidth">
                    <el-input
                            maxlength="100"
                            placeholder=""
                            size="medium"
                            :disabled="true"
                            v-model="this.uviewNm"
                    ></el-input>
                </el-form-item>
                <el-form-item label="路由:" :label-width="this.formLabelWidth">
                    <el-input
                            maxlength="100"
                            placeholder=""
                            size="medium"
                            :disabled="true"
                    ></el-input>
                </el-form-item>
                <el-form-item label="接口方式:" :label-width="this.formLabelWidth">
                    <el-input
                            maxlength="100"
                            placeholder=""
                            size="medium"
                            :disabled="true"
                            v-model="ruleForm.method"
                    ></el-input>
                </el-form-item>
                <el-form-item label="认证方式:" :label-width="this.formLabelWidth">
                    <el-input
                            maxlength="100"
                            placeholder="Key-Auth"
                            size="medium"
                            :disabled="true"
                            ></el-input
                    >
                </el-form-item>
                <el-form-item label="认证条件:" :label-width="this.formLabelWidth">
                    <el-input
                            maxlength="100"
                            placeholder="用户授权码"
                            size="medium"
                            :disabled="true"
                            ></el-input
                    >
                </el-form-item>
                <el-form-item
                        label="请求参数部分说明:"
                        :label-width="this.formLabelWidth"
                >
                    <el-input
                            maxlength="100"
                            placeholder=""
                            size="medium"
                            :disabled="true"
                            v-model="httpUrl"
                    ></el-input>
                </el-form-item>
                <el-form-item label="body数据参数" :label-width="this.formLabelWidth">
                </el-form-item>
                <el-form-item>
                    <Table :tableData="tableData1" :tableHeader="tableHeader1"></Table>
                </el-form-item>
                <el-form-item label="params数据参数" :label-width="this.formLabelWidth">
                </el-form-item>
                <el-form-item>
                    <Table :tableData="tableParamsData" :tableHeader="tableParamsHeader"></Table>
                </el-form-item>
                <el-form-item label="数据返回参数" :label-width="this.formLabelWidth">
                </el-form-item>
                <el-form-item>
                    <Table :tableData="tableData2" :tableHeader="tableHeader2"></Table>
                </el-form-item>
                <el-form-item label="常量参数" :label-width="this.formLabelWidth">
                </el-form-item>
                <el-form-item>
                    <Table :tableData="tableConstantsData" :tableHeader="tableConstantsHeader"></Table>
                </el-form-item>
                <div class="demo-drawer__footer drawer_footer">
                    <el-button size="medium" @click="goBack">返回</el-button>
                </div>
            </el-form>
        </el-col>
    </el-main>
</template>

<script>
    import Table from "@/components/table/detailTable.vue";
    import { assetApiExFind } from "@/api/assetApiEx.js";
    export default {
        components: {
            Table,
        },
        data() {
            return {
                title: "API详情",
                ruleForm: {
                    name: "",
                },
                formLabelWidth: "130px",
                uviewNm:'',
                httpUrl:'',
                tableData1: [],
                tableHeader1: [
                    {
                        prop: "name",
                        label: "参数名称",
                        width: "",
                    },
                    {
                        prop: "type",
                        label: "参数位置",
                        width: "",
                    },
                    {
                        prop: "isRequired",
                        label: "是否必填",
                        width: "",
                    },
                    {
                        prop: "defaultValue",
                        label: "默认值",
                        width: "",
                    },
                    {
                        prop: "regular",
                        label: "校验格式",
                        width: "",
                    },
                    {
                        prop: "description",
                        label: "描述",
                        width: "",
                    }
                ],
                tableParamsData:[],
                tableParamsHeader:[
                    {
                        prop: "name",
                        label: "参数名称",
                        width: "",
                    },
                    {
                        prop: "type",
                        label: "参数位置",
                        width: "",
                    },
                    {
                        prop: "isRequired",
                        label: "是否必填",
                        width: "",
                    },
                    {
                        prop: "defaultValue",
                        label: "默认值",
                        width: "",
                    },
                    {
                        prop: "regular",
                        label: "校验格式",
                        width: "",
                    },
                    {
                        prop: "description",
                        label: "描述",
                        width: "",
                    }
                ],
                tableData2: [],
                tableHeader2: [
                    {
                        prop: "name",
                        label: "参数名称",
                        width: "",
                    },
                    {
                        prop: "type",
                        label: "参数类型",
                        width: "",
                    },
                    {
                        prop: "description",
                        label: "描述",
                        width: "",
                    },
                ],
                tableConstantsData: [],
                tableConstantsHeader: [
                    {
                        prop: "name",
                        label: "参数名称",
                        width: "",
                    },
                    {
                        prop: "type",
                        label: "参数类型",
                        width: "",
                    },
                    {
                        prop: "description",
                        label: "描述",
                        width: "",
                    },
                ]
            };
        },
        created() {
            this.uviewNm=this.$route.query.uviewNm;
            this.assetApiExDetail();

            console.log(this.uviewNm);
        },
        methods: {
            // 详情
            async assetApiExDetail() {
                const that = this;
                let data = { id: this.$route.query.id};
                that.loading = true;
                const response = await assetApiExFind(data);
                that.loading = false;
                if (response.data.code === 1) {
                    that.ruleForm = response.data.data;
                    let jHost=  JSON.parse(response.data.data.host);
                    let shost='';
                    jHost.forEach(item=>{
                        shost+=item.target+","
                    });
                    if(shost.length>0){
                        shost=shost.substring(0,shost.length-1);
                    }
                    this.httpUrl='http://'+shost+response.data.data.path;
                    if(response.data.data.body!=""){
                        that.tableData1 = JSON.parse(response.data.data.body);
                    }
                    if(response.data.data.params!=""){
                        that.tableParamsData=JSON.parse(response.data.data.params);
                    }
                    if(response.data.data.response!=""){
                        that.tableData2 = JSON.parse(response.data.data.response);
                    }
                    if(response.data.data.constants!=""){
                        that.tableConstantsData=JSON.parse(response.data.data.constants);
                    }
                } else {
                    // 添加上传失败后 回调失败信息
                    that.$message.error(response.data.msg);
                    return false;
                }
            },
            goBack() {
                this.$router.push({
                    path: '/archApiExIndex',
                    query: {
                        uviewId: this.$route.query.uviewId,
                        uviewNm:this.$route.query.uviewNm
                    }
                });

            }
        },
    };
</script>
<style lang="scss" scoped>
    .main {
        display: flex;
        flex-direction: column;
        height: 100%;
        border-radius: 5px;
        padding: 20px;
        .main-center {
            padding: 20px;
            border-radius: 5px;
            width: 65%;
            margin: 0 auto;
        }
    }
    .demo-drawer__footer {
        margin-top: 80px;
        text-align: center;
    }
</style>