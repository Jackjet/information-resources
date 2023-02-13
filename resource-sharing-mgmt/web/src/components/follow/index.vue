<template>
  <div class="details-box-text">
    <div class="details-box-left">
      <img id="img-icon" src="@/assets/image/icon/信息资源管理.svg" alt="" />
      <span v-if="loginToken" @click="followClick">
        <img :src="
            !dataDetails.isFocus
              ? require('@/assets/image/icon/关注2.png')
              : require('@/assets/image/icon/关注1.png')
          " alt="" />
        关注
      </span>
    </div>
    <div class="details-box-center">
      <div class="details-box-header">
        <h2>{{ dataDetails.uviewNm ? dataDetails.uviewNm : '' }}</h2>
        <div class="details-box-right">
          <div class="icon-num">
            <i><img src="@/assets/image/icon/查看 (2).png" alt="" />
              {{ dataDetails.visitsCount }}</i>
            <i><img src="@/assets/image/icon/关注1.png" alt="" />
              {{ dataDetails.focusCount }}</i>
            <i style="cursor: pointer;color:red;" class="el-icon-folder-delete" @click="correctionClick(dataDetails.uviewId,dataDetails.name)">
              <em style="margin-left:5px;color:#000;font-size:13px;">纠错</em>
            </i>
          </div>
          <span>{{ dataDetails.name }}</span>
        </div>
      </div>
      <div class="details-box-content">
        <el-row>
          <el-col :span="8">
            <p>信息资源代码：{{ dataDetails.uviewNo ? dataDetails.uviewNo : 0 }}</p>
            <p>统一社会信用代码：{{ dataDetails.provOrgCode }}</p>
          </el-col>
          <el-col :span="8">
            <p>更新周期：
              <em v-if="dataDetails.updateCyc==='01'">实时</em>
              <em v-if="dataDetails.updateCyc==='02'">每日</em>
              <em v-if="dataDetails.updateCyc==='03'">每周</em>
              <em v-if="dataDetails.updateCyc==='04'">每月</em>
              <em v-if="dataDetails.updateCyc==='05'">每季度</em>
              <em v-if="dataDetails.updateCyc==='06'">每半年</em>
              <em v-if="dataDetails.updateCyc==='07'">每年</em>
              <em v-if="dataDetails.updateCyc==='08'">其他</em>
            </p>
            <p>
              共享类型：{{
                dataDetails.shareLv === "01"
                  ? "无条件共享"
                  : dataDetails.shareLv === "02"
                  ? "有条件共享"
                  : "不予共享"
              }}
            </p>
          </el-col>
          <el-col :span="8">
            <p>发布日期：{{ dataDetails.pubDt.split(' ')[0] }}</p>
            <p>
              开放类型：{{ dataDetails.pubSts === "01" ? "无条件开放":dataDetails.pubSts === "02" ?"有条件开放": "不予开放" }}
            </p>
          </el-col>
        </el-row>
        <p>共享条件：{{ dataDetails.shareCondition?dataDetails.shareCondition:"无" }}</p>
        <p>开放条件：{{ dataDetails.pubCondition?dataDetails.pubCondition:"无" }}</p>
        <p>信息资源摘要：{{ dataDetails.uviewDesc }}</p>
        <p>信息资源提供单位：{{ dataDetails.name }}</p>
      </div>
    </div>
    <el-dialog title="我要纠错" width="30%" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="form">
        <el-form-item label="单位名称：" label-width="100px">
          <el-input v-model="form.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="反馈类别：" label-width="100px" prop="type" :rules="[{ required: true, message: '请选中反馈类型'}]">
          <el-select v-model="form.type" placeholder="请选择反馈类别" style="width:100%">
            <el-option label="数据与实际情况不符" :value="1"></el-option>
            <el-option label="资源过时" :value="2"></el-option>
            <el-option label="数据无法下载" :value="3"></el-option>
            <el-option label="其他" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据描述：" label-width="100px" prop="describe" :rules="[{ required: true, message: '请填写数据描述'}]">
          <el-input v-model="form.describe" type="textarea" :rows="3" placeholder="请输入描述信息"></el-input>
        </el-form-item>
        <el-form-item style="text-align: right;">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitClick('form')">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import { getToken } from '@/utils/storage.js';
export default {
  props: {
    dataDetails: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      loginToken: getToken(),
      dialogFormVisible: false,
      // followNum: 0,
      btnFang: true,
      form: {
        name: '',
        uviewId: '',
        type: null,
        describe: ''
      },
      pubDtTime: "",
    };
  },
  mounted() {
    // this.pubDtTime = this.dataDetails.pubDt ? this.dataDetails.pubDt.split(' ')[0] : this.dataDetails.pubDt;
    // this.myFocusInfoCountByUviewId({ uviewId: this.dataDetails.uviewId }).then(
    //   (res) => {
    //     if (res.code === 1) {
    //       this.followNum = res.data;
    //     } else {
    //       this.$message.error(res.msg);
    //     }
    //   }
    // );
  },
  methods: {
    // 纠错
    correctionClick(id, name) {
      if (this.loginToken) {
        this.form.name = name;
        this.form.uviewId = id;
        this.dialogFormVisible = true;
      } else {
        this.$confirm('此操作需要登录, 是否跳转至登录页?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          location.href = "login.html?url=" + location.href;
        }).catch(() => {
          // window.location.reload();
          return false;
        });
      }
    },
    submitClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.correctionInsert(this.form).then(res => {
            if (res.code === 1) {
              this.$message.success(res.msg);
              this.dialogFormVisible = false;
            } else {
              this.$message.error(res.msg);
            }
          })
        } else {
          return false;
        }
      });
    },
    followClick() {
      if (this.btnFang) {
        if (getToken()) {
          if (!this.dataDetails.isFocus) {
            this.myFocusInfoInsert({ uviewId: this.dataDetails.id }).then((res) => {
              if (res.code === 1) {
                this.$message.success("关注成功");
                this.$emit("updataFun");
              } else {
                this.$message.error(res.msg);
              }
              setTimeout(() => {
                this.btnFang = true;
              }, 3000)
            });
          } else {
            this.myFocusInfoDelete(this.dataDetails.id).then((res) => {
              if (res.code === 1) {
                this.$message.success("取消成功！");
                this.$emit("updataFun");
              } else {
                this.$message.error(res.msg);
              }
              setTimeout(() => {
                this.btnFang = true;
              }, 3000)
            });
          }
        }
      }

    },
  },
};
</script>
<style lang="scss" scope>
.details-box-text {
  padding-top: 40px;
  width: 80%;
  margin: auto;
  display: flex;
  justify-content: space-between;
  .details-box-left {
    width: 150px;
    display: flex;
    flex-direction: column;
    align-items: center;
    #img-icon {
      width: 70px;
    }
    span {
      display: flex;
      justify-content: center;
      align-items: center;
      background: #c3e182;
      color: $font-color-white;
      width: 100px;
      padding: 5px 0;
      margin-top: 15px;
      cursor: pointer;
      img {
        width: 20px;
      }
    }
  }
  .details-box-center {
    flex: 1;
    .details-box-header {
      display: flex;
      justify-content: space-between;
      h2 {
        font-size: $font-size-large;
      }
      .details-box-right {
        width: 150px;
        .icon-num {
          display: flex;
          justify-content: space-between;
          align-items: center;
          img {
            width: 20px;
          }
          i {
            display: flex;
            align-items: center;
          }
        }
        span {
          display: block;
          background: orange;
          color: #fff;
          font-size: $font-size-small;
          text-align: center;
          width: 100%;
          line-height: 20px;
          border-top-left-radius: 10px;
          border-bottom-right-radius: 10px;
          margin-top: 10px;
        }
      }
    }
    .details-box-content {
      padding-top: 20px;
      line-height: 30px;
    }
  }
}
</style>