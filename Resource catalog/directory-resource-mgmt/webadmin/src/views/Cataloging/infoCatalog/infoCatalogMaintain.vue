<template>
  <div class="mt20 department_content contentArea">
    <el-row style="height: 100%">
      <!--tab选择区域-->
      <el-col :span="24">
        <div class="content_area">
          <div class="type_area">信息资源目录</div>
          <el-tabs v-model="activeName" class="tab_area res_tabs" @tab-click="handleTabClick">
            <el-tab-pane label="1、基本信息维护" name="0">
              <el-form ref="departmentFormDom" :model="departmentForm" label-width="100px" :rules="rules" label-position="left" style="margin-left: 30px">
                <el-row>
                  <el-col :span="20" :offset="2">
                    <el-form-item ref="resClassifyDom" label="部门信息资源分类:" prop="resClassify" label-width="145px" class="required_label ">
                      <el-input v-model="departmentForm.resClassify" placeholder="请选择部门信息资源分类" :disabled="id!=0" readonly>
                        <el-button slot="append" :disabled="id!=0" class="selectBtn" @click="selcetResClassify">选择</el-button>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="信息资源名称:" prop="uviewNm" label-width="145px" class="required_label ">
                      <el-input v-model="departmentForm.uviewNm" placeholder="输入信息资源目录名称，3-200个字符" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item ref="uviewNoDom" label="信息资源代码:" prop="uviewNo" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.uviewNo" placeholder="请选择部门信息资源分类生成信息资源代码" disabled />
                      <div class="codeHint">
                        自动生成的信息资源代码格式：前6位为信息资源分类的类、项、目，中间6位为分类中细目的最后6
                        位，最后6位是细目分类下的顺序编码
                      </div>
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="信息资源提供单位:" prop="provOrgName" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.provOrgName" placeholder="" disabled="" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="内设部门名称:" prop="belongToName" label-width="145px" class="required_label">
                      <el-select v-model="departmentForm.belongToList" placeholder="请选择" multiple @change="departmentChange">
                        <el-option v-for="item in selectTreedata" :key="item.orgId" :label="item.orgNm" :value="item.orgId">
                        </el-option>
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="统一社会信用代码:" prop="provOrgCode" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.provOrgCode" disabled />
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="信息资源格式分类:" prop="mediaFmt" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.mediaFmt" clearable placeholder="请选择" @change="getSelect2(0)">
                        <el-option v-for="item in select1" :key="item.value" :label="item.name" :value="item.value" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="信息资源格式类型:" prop="mediaFmtType" label-width="145px" class="required_label">
                      <!-- <el-input v-model="departmentForm.resFomatterType" placeholder="请选择" ></el-input> -->
                      <el-select style="width:100%;" v-model="departmentForm.mediaFmtType" placeholder="请先选择信息资源格式分类" :loading="loading">
                        <el-option v-for="item in select2" :key="item.value" :label="item.name" :value="item.value" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="更新周期:" prop="updateCyc" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.updateCyc" placeholder="请选择">
                        <el-option v-for="item in updateCycles" :key="item.value" :label="item.name" :value="item.value" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <!-- <el-col :span="9" :offset="2">
                    <el-form-item label="发布日期:" prop="pubDt" label-width="145px" class="required_label">
                      <el-date-picker style="width:100%;" v-model="departmentForm.pubDt" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" :picker-options="setDisabled" />
                    </el-form-item>
                  </el-col> -->
                  <el-col :span="20" :offset="2">
                    <el-form-item label="信息资源摘要:" prop="uviewDesc" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.uviewDesc" rows="3" type="textarea" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="基础信息资源分类:" prop="baseClassify" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.baseClassify" placeholder="请选择基础信息资源分类" readonly>
                        <el-button slot="append" class="selectBtn" @click="baseClassifySelect">选择</el-button>
                      </el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="主题信息资源分类:" prop="themeClassify" label-width="145px" class="required_label">
                      <select-tree v-if="showSelectTree" :select-ids="selectOrgs1" :node-key="'typId'" :is-multiple="true" :select-treedata="selectTreedata1" :placeholder="'请选择主题信息资源分类'" :label="'typNm'" class="multipleResSelect" @checkedChoose="getCheckedOrg1(arguments)" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="共享类型:" prop="shareLv" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.shareLv" placeholder="请选择" @change="changeType($event)">
                        <el-option v-for="item in shareTypes" :key="item.value" :label="item.name" :value="item.value" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="共享范围:" prop="shareScope" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.shareScope" placeholder="请选择">
                        <el-option v-for="item in shareScopes" :key="item" :label="item" :value="item" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2" v-if="departmentForm.shareLv=='02'">
                    <el-form-item ref="shareConditionDom" label="共享条件:" prop="shareCondition" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.shareCondition" rows="3" type="textarea" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="开放类型:" prop="pubSts" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.pubSts" placeholder="请选择" @change="changeRadio($event)">
                        <!-- <el-option v-for="item in yesOrNoStrArr" :key="item.value" :label="item.label" :value="item.value" /> -->
                        <el-option label="无条件开放" value="01" />
                        <el-option label="有条件开放" value="02" />
                        <el-option label="不予开放" value="03" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2" v-if="departmentForm.pubSts=='02'">
                    <el-form-item ref="pubConditionDom" label="开放条件:" prop="pubCondition" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.pubCondition" rows="3" type="textarea" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="是否涉密:" prop="secret" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.secret" placeholder="请选择">
                        <el-option v-for="item in yesOrNoIntArr" :key="item.value" :label="item.label" :value="item.value" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="9" :offset="2">
                    <el-form-item label="数据范围:" prop="dataScope" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.dataScope" placeholder="请选择">
                        <el-option v-for="item in dataScopes" :key="item" :label="item" :value="item" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2">
                    <el-form-item label="是否关联权责:" prop="relBusi" label-width="145px" class="required_label">
                      <el-select style="width:100%;" v-model="departmentForm.relBusi" placeholder="请选择">
                        <el-option v-for="item in yesOrNoIntArr" :key="item.value" :label="item.label" :value="item.value" />
                      </el-select>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2" v-if="departmentForm.relBusi==0">
                    <el-form-item ref="relBusiMsgDom" label="权责清单补充说明:" prop="relBusiMsg" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.relBusiMsg" rows="3" type="textarea" :disabled="departmentForm.relBusi==1" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2" v-if="departmentForm.relBusi==1">
                    <el-form-item label="关联权责清单:" prop="relBusList" label-width="145px" class="required_label">
                      <el-tree ref="treeRelBus" :data="relBusiOptions" show-checkbox node-key="busiId" :default-checked-keys="departmentForm.relBusList" :props="{ multiple: true, checkStrictly: false, label:'busiNm',value:'busiId' }" @check-change="treeClickChange">
                      </el-tree>
                    </el-form-item>
                  </el-col>
                  <el-col :span="20" :offset="2" v-if="id != '0'">
                    <el-form-item label="变更说明:" prop="lastUpdateDesc" label-width="145px" class="required_label">
                      <el-input v-model="departmentForm.lastUpdateDesc" rows="3" type="textarea" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12" :offset="6">
                    <el-form-item>
                      <el-button class="add" @click="submit">保存</el-button>
                      <el-button class="remove" @click="reset">返回
                      </el-button>
                    </el-form-item>
                  </el-col>
                </el-row>

              </el-form>

            </el-tab-pane>
            <el-tab-pane label="2、信息项维护" name="1" :disabled="id==0 && isSaveSuccess">
              <div class="related_btns">
                <el-button class="add" size="mini" icon="el-icon-plus" @click="addRelated('add')">新增</el-button>
                <el-button class="add" size="mini" icon="el-icon-close" @click="deleteRes">删除</el-button>
                <el-input v-model="listQuery.srcFieldAndEngCd" clearable class="filter-item searchInputWidth" placeholder="信息项名称或标识" style="width: 25%;" />
                <el-button class="add" size="mini" @click="getTableData">查询</el-button>
              </div>

              <el-table v-if="showInfoItemsTable" :data="tableData" stripe :header-cell-style="getRowClass" style="width: 100%" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" />
                <el-table-column v-for="headItem in formtableData.insideArr" :key="headItem.id" fixed :prop="headItem.itemMark" :label="headItem.itemName">
                  <template slot-scope="scope">
                    <div v-if="headItem.itemCode">
                      <div v-if="headItem.multiple && scope.row[headItem.itemMark] && scope.row[headItem.itemMark].toString().charAt(0) === '['">
                        <el-tag v-for="(_item,_index) in JSON.parse(scope.row[headItem.itemMark])" :key="_item">{{ headItem.selectsObj[_item] }}</el-tag>
                      </div>
                      <div v-else-if="scope.row[headItem.itemMark] && scope.row[headItem.itemMark].toString().charAt(0) === '['">
                        {{ headItem.selectsObj[JSON.parse(scope.row[headItem.itemMark])[0]] }}
                      </div>
                      <div v-else>{{ headItem.selectsObj[scope.row[headItem.itemMark]] }}</div>
                    </div>
                    <div v-if="!headItem.itemCode">
                      {{ scope.row[headItem.itemMark] }}
                    </div>
                  </template>
                </el-table-column>
                <el-table-column v-for="(headItem,headIndex) in formtableData.outsideArr" :key="headItem.id" :prop="'ext'+(headIndex+1)" :label="headItem.itemName">
                  <template slot-scope="scope">
                    <div v-if="headItem.itemCode">
                      <div v-if="headItem.multiple && scope.row[`ext${headIndex+1}`] &&scope.row[`ext${headIndex+1}`].toString().charAt(0) === '['">
                        <el-tag v-for="(_item,_index) in JSON.parse(scope.row[`ext${headIndex+1}`])" :key="_item">{{ headItem.selectsObj[_item] }}</el-tag>
                      </div>
                      <div v-else-if="scope.row[`ext${headIndex+1}`] && scope.row[`ext${headIndex+1}`].toString().charAt(0) === '['">
                        {{ headItem.selectsObj[JSON.parse(scope.row[`ext${headIndex+1}`])[0]] }}
                      </div>
                      <div v-else>{{ headItem.selectsObj[scope.row[`ext${headIndex+1}`]] }}</div>
                    </div>
                    <div v-if="!headItem.itemCode">
                      <div>{{ scope.row[`ext${headIndex+1}`] }}</div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="100px" fixed="right">
                  <template slot-scope="scope">
                    <el-tooltip content="编辑" effect="light" :open-delay="1000" placement="bottom">
                      <span class="tr_detail_icon fl">
                        <svg-icon icon-class="catalog_edit" class-name="" @click="editListRow(scope.row)" />
                      </span>
                    </el-tooltip>
                    <!-- <el-tooltip content="脱敏" effect="light" :open-delay="1000" placement="bottom">
                      <span class="tr_detail_icon fl">
                        <svg-icon icon-class="tuomin" class-name="" @click="regReset(scope.row)" />
                      </span>
                    </el-tooltip> -->
                    <!-- <el-tooltip content="编辑脱敏" effect="light" :open-delay="1000" placement="bottom">
                      <span class="tr_detail_icon fl">
                        <svg-icon icon-class="tuomin" class-name="" @click="editRegReset(scope.row)" />
                      </span>
                    </el-tooltip> -->
                  </template>
                </el-table-column>
              </el-table>
              <pagination v-show="total>0" :layout="'prev,pager,next'" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getTableData" />
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>

    <!--新增/编辑数据项-->
    <el-dialog :modal-append-to-body="false" :title="dialogTitle" align="center" :visible.sync="addWorkOrder" width="48%" :close-on-click-modal="false" @close="closedDialog('addbusinessForm','addbusinessForm')">
      <el-form ref="addbusinessForm" :model="addDataItemForm" align="left" :rules="createRules" class="demo-ruleForm" label-position="left">
        <div>
          <div v-for="(formitem) in formDatas" :key="formitem.id">
            <!--input-->
            <div v-if="formitem.itemType == '01'">
              <el-form-item :label="formitem.itemName" class="required_label" label-width="140px" :prop="formitem.itemMark">
                <el-input v-model="addDataItemForm[formitem.itemMark]" style="width:100%" />
              </el-form-item>
            </div>
            <!--select single-->
            <div v-if="formitem.itemType == '03'">
              <el-form-item :label="formitem.itemName" class="required_label" label-width="140px" :prop="formitem.itemMark">
                <el-select v-model="addDataItemForm[formitem.itemMark]" style="width:100%">
                  <el-option v-for="item in formitem['selects']" :key="item.value" :label="item.name" :value="item.value" />
                </el-select>
              </el-form-item>
            </div>
            <!--select mulity-->
            <div v-if="formitem.itemType == '04'">
              <el-form-item :label="formitem.itemName" class="required_label" label-width="140px" :prop="formitem.itemMark">
                <el-select v-model="addDataItemForm[formitem.itemMark]" multiple style="width:100%">
                  <el-option v-for="item in formitem['selects']" :key="item.value" :label="item.name" :value="item.value" />
                </el-select>
              </el-form-item>
            </div>
            <!--textarea-->
            <div v-if="formitem.itemType == '02'">
              <el-form-item :label="formitem.itemName" label-width="140px">
                <el-input v-model="addDataItemForm[formitem.itemMark]" :rows="5" type="textarea" style="width:100%" />
              </el-form-item>
            </div>
          </div>
          <div v-for="(formitem,index) in formDatasNoInside" :key="formitem.id">
            <!--input-->
            <div v-if="formitem.itemType == '01'">
              <el-form-item :label="formitem.itemName" class="required_label" label-width="140px" :prop="`ext${index+1}`">
                <el-input v-model="addDataItemForm[`ext${index+1}`]" style="width:100%" />
              </el-form-item>
            </div>
            <!--select single-->
            <div v-if="formitem.itemType == '03'">
              <el-form-item :label="formitem.itemName" class="required_label" label-width="140px" :prop="`ext${index+1}`">
                <el-select v-model="addDataItemForm[`ext${index+1}`]" style="width:100%">
                  <el-option v-for="item in formitem['selects']" :key="item.value" :label="item.name" :value="item.value" />
                </el-select>
              </el-form-item>
            </div>
            <!--select mulity-->
            <div v-if="formitem.itemType == '04'">
              <el-form-item :label="formitem.itemName" class="required_label" label-width="140px" :prop="`ext${index+1}`">
                <el-select v-model="addDataItemForm[`ext${index+1}`]" multiple style="width:100%">
                  <el-option v-for="item in formitem['selects']" :key="item.value" :label="item.name" :value="item.value" />
                </el-select>
              </el-form-item>
            </div>
            <!--textarea-->
            <div v-if="formitem.itemType == '02'">
              <el-form-item :label="formitem.itemName" label-width="140px" :prop="`ext${index+1}`">
                <el-input v-model="addDataItemForm[`ext${index+1}`]" :rows="5" type="textarea" style="width:100%" />
              </el-form-item>
            </div>
          </div>
        </div>
      </el-form>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="addMenuDetail()">保 存</el-button>
        <el-button size="mini" class="remove" @click="addWorkOrder=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 部门信息资源分类树 -->
    <el-dialog :modal-append-to-body="false" :title="'选择信息资源分类'" align="left" :visible.sync="selectResClassifyDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog">
      <el-row>
        <el-col class="">
          <div class="" style="height: 350px;overflow: auto">
            <el-tree ref="treeDom" show-checkbox accordion :data="treeData" :props="defaultProps" highlight-current node-key="typId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :filter-node-method="filterNode" :check-strictly="true" @check-change="handleCheckChange" :default-checked-keys="deptDefaultCheckedKeys" />
          </div>
        </el-col>
      </el-row>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="resClassifyConfirm">确 定</el-button>
        <el-button size="mini" class="remove" @click="close('selectResClassifyDialog','treeDom')">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 基础信息资源分类树 -->
    <el-dialog :modal-append-to-body="false" :title="'基础信息资源分类'" align="left" :visible.sync="baseClassifyDialog" width="48%" :close-on-click-modal="false" @close="closeAddDialog">
      <el-row>
        <el-col class="">
          <div class="" style="height: 350px;overflow: auto">
            <el-scrollbar class="scrollbar_device">
              <el-tree ref="basetreeDom" show-checkbox accordion :data="basetreeData" :props="defaultProps" highlight-current node-key="typId" :current-node-key="currentNodekey" :default-expanded-keys="expandedkeys" :check-strictly="true" @check-change="handleCheckChange1" :default-checked-keys="baseDefaultCheckedKeys" />
            </el-scrollbar>
          </div>
        </el-col>
      </el-row>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="baseClassifyConfirm">确 定</el-button>
        <el-button size="mini" class="remove" @click="close('baseClassifyDialog','basetreeDom')">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog :modal-append-to-body="false" title="删除" class="del_dialog" :visible.sync="delDialog" center :append-to-body="true" :lock-scroll="false" width="325px" :close-on-click-modal="false">
      <del-confirm :tip-str="tipStr" @cancelDel="delDialog=false" @confirmDel="confirmDelete" />
    </el-dialog>
    <el-dialog :modal-append-to-body="false" title="配置数据脱敏规则" align="center" :visible.sync="regsetDialog" width="50%" :close-on-click-modal="false">
      <el-form :model="regSetForm" :rules="regRules" align="left" class="demo-ruleForm" label-position="left">
        <el-form-item label="是否脱敏:" label-width="145px" prop="isReg" class="required_label">
          <el-radio-group v-model="isReg" @change="isRegRadio($event)">
            <el-radio :label="'01'">是</el-radio>
            <el-radio :label="'02'">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <div v-show="isShowRegRadio">
          <el-form-item label="选择规则:" label-width="140px" class="required_label" prop="type">
            <el-select v-model="regSetForm.type" clearable placeholder="请选择" @change="getChangeReg($event)">
              <el-option v-for="item in regSelect" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <div v-show="isShowStrInput">
            <el-form-item label="保留前几个字符" class="required_label" label-width="140px" prop="head">
              <el-input v-model="regSetForm.head" style="width:30%" />
            </el-form-item>
            <el-form-item label="保留后几个字符" class="required_label" label-width="140px" prop="behind">
              <el-input v-model="regSetForm.behind" style="width:30%" />
            </el-form-item>
          </div>

          <el-form-item v-show="isShowregexpres" label="正则表达式" class="required_label" label-width="140px" prop="regular">
            <el-input v-model="regSetForm.regular" placeholder="请输入正则表达式" style="width:70%" />
          </el-form-item>
          <el-form-item label="替换字符" class="required_label" label-width="140px" prop="letter">
            <el-input v-model="regSetForm.letter" style="width:30%" />
          </el-form-item>
        </div>

      </el-form>
      <div class="dialogDom" align="center">
        <el-button size="mini" class="add" @click="saveRegSet">保 存</el-button>
        <el-button size="mini" class="remove" @click="regsetDialog=false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  getDepartRes,
  getOrgTree,
  getInfoSelect2,
  getInfoSelect1,
  getUviewNo,
  saveOrUpdateRes,
  resDetail,
  infoResDetail,
  deleteInfo,
  createInfo,
  getDataTypes,
  editInfo,
  getFormItem,
  getOrderNum,
  getRegInfo,
  saveAddOrUpdate,
  delRegInfo
} from '@/api/resCatalog'
import { getBusinessTreeByDeptId } from '@/api/businessManagement'
import mapIcon from '@/assets/mapIcon.png'
import Pagination from '@/components/Pagination'
import mixinJs from '@/utils/mixin'
import SelectTree from '@/components/SelectTree'
import DelConfirm from '@/components/DelConfirm'
import moment from 'moment'
import { getDictByType } from '../../../api/departmentRes'
export default {
  name: 'InfoCatalogMaintain',
  components: { Pagination, DelConfirm, SelectTree },
  mixins: [mixinJs],
  props: {
    id: {
      type: [String, Number],
      default: () => []
    }
  },
  data() {
    const checkLength = function (rule, value, callback) {
      if (value) {
        if (value.length < 3 || value.length > 200) {
          return callback(new Error(rule.messaage))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    const yingwen = function (rule, value, callback) {
      var reg = /^[a-zA-Z0-9_]{0,}$/;
      if (value) {
        if (!reg.test(value)) {
          return callback(new Error(rule.messaage))
        } else {
          callback()
        }
      } else {
        callback()
      }
    }
    return {
      setDisabled: {
        disabledDate(time) {
          return time.getTime() > Date.now();
        },
      },
      isShowStrInput: true,
      isShowregexpres: false,
      isShowRegRadio: false,
      regsetDialog: false,
      showInfoItemsTable: false,
      isSaveSuccess: true,
      delDialog: false,
      isOpenDisabled: true,
      selectOrgs1: [],
      isDisabled: true,
      listQuery: {
        page: 1,
        limit: 10,
        srcFieldAndEngCd: ''
      },
      mapIcon: mapIcon,
      activeName: 0,
      tableData: [],
      total: 0,
      isReg: '02',
      regSetForm: {
        type: '0'
      },
      departmentForm: {
        pubSts: '01',
        relBusi: 0,
        secret: 0,
        shareScope: '全市',
        dataScope: '区本级数据',
        shareLv: '01',
        shareCondition: '',
        pubCondition: '',
        relBusList: [],
        belongToList: []

      },
      departmentName: '',
      tipStr: '确定删除该信息项吗',
      rules: {
        resClassify: [
          { required: true, message: '请选择部门信息资源分类', trigger: 'blur' }
        ],
        uviewNm: [
          { required: true, message: '请输入信息资源名称', trigger: 'blur' },
          { validator: checkLength, message: '信息资源名称长度必须为3-200个字符', trigger: 'blur' }
        ],
        uviewNo: [
          { required: true, message: '请选择部门信息资源分类生成信息资源代码', trigger: 'blur' }
        ],
        provOrgName: [
          { required: true, message: '未发现信息资源提供单位', trigger: 'blur' }
        ],
        provOrgCode: [
          { required: true, message: '请输入统一社会信用代码', trigger: 'blur' },
          { validator: this.hasChina, message: '统一社会信用代码不能包含汉字', trigger: 'blur' }
        ],
        mediaFmt: [
          { required: true, message: '请选择信息资源格式分类', trigger: 'change' }
        ],
        mediaFmtType: [
          { required: true, message: '请选择信息资源格式类型', trigger: 'change' }
        ],
        updateCyc: [
          { required: true, message: '请选择更新周期', trigger: 'change' }
        ],
        pubDt: [
          { required: true, message: '请选择发布日期', trigger: 'change' }
        ],
        uviewDesc: [
          { required: true, message: '请输入摘要', trigger: 'blur' }
        ],
        shareLv: [
          { required: true, message: '请选择共享类型', trigger: 'change' }
        ],
        shareScope: [
          { required: true }
        ],
        shareCondition: [
          { required: true, message: '请输入共享条件', trigger: 'blur' }
        ],
        pubSts: [
          { required: true, message: '请选择是否向社会开发', trigger: 'change' }
        ],
        pubCondition: [
          { required: true, message: '请输入开放条件', trigger: 'blur' }
        ],
        secret: [
          { required: true }
        ],
        dataScope: [
          { required: true }
        ],
        relBusi: [
          { required: true }
        ],
        relBusiMsg: [
          { required: true, message: '请输入权责清单补充说明', trigger: 'change' }
        ],
        relBusList: [
          { required: true, message: '请选择关联权责清单', trigger: 'change' }
        ],
        lastUpdateDesc: [
          { required: true, message: '请输入变更说明', trigger: 'change' }
        ]
      },
      // 新增表单校验
      createRules: {
        // 暂未添加
        SJXMC: [
          { required: true, message: '请输入数据项名称', trigger: 'blur' }
        ],
        YWBS: [{ required: true, message: '请输入英文标识', trigger: 'blur' }, { validator: yingwen, message: '不能含有中文或特殊字符', trigger: 'blur' }],
        SJLX: [{ required: true, message: '请选择数据类型', trigger: 'change' }],
        SJCD: [{ required: true, message: '请输入数据长度', trigger: 'blur' },
        { validator: this.isNumber, message: '数据长度必须是整数数字', trigger: 'blur' }
        ],
        XSXH: [{ required: true, message: '请输入显示序号', trigger: 'blur' }, { validator: this.isNumber, message: '显示序号必须是整数数字', trigger: 'blur' }]
      },
      regRules: {
        isReg: [
          { required: true, message: '', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '', trigger: 'blur' }
        ],
        head: [
          { required: true, message: '请输入保留的前几个字符', trigger: 'blur' },
          { validator: this.isNumber, message: '输入项必须是整数数字', trigger: 'blur' }
        ],
        behind: [
          { required: true, message: '请输入保留的后几个字符', trigger: 'blur' },
          { validator: this.isNumber, message: '输入项必须是整数数字', trigger: 'blur' }
        ],
        regular: [
          { required: true, message: '请输入正则表达式', trigger: 'blur' }
        ],
        letter: [
          { required: true, message: '请输入替换字符', trigger: 'blur' }
        ]

      },
      addDataItemForm: {},
      formDatas: [],
      formDatasNoInside: [],
      serviceObjs: [],
      selectResClassifyDialog: false,
      baseClassifyDialog: false,
      themeClassifyDialog: false,
      searchQuery: {
        keyword: '',
        current: 1,
        size: 5
      },
      searchTotal: 0,
      searchRelatedResults: [],
      choosedRelatedResults: [],
      treeData: [],
      basetreeData: [],
      themetreeData: [],
      defaultProps: {
        children: 'children',
        label: 'typNm'
      },
      dialogTitle: '新增数据项',
      selectTreedata: [],
      selectTreedata1: [],
      updateCycles: [],
      addWorkOrder: false,
      currentNodekey: '', // 默认选中的节点树
      expandedkeys: [], // 默认展开的节点树
      baseDefaultCheckedKeys: [],//基础分类默认选中
      deptDefaultCheckedKeys: [],//部门分类选中
      loading: false,
      select1: [],
      select2: [],
      shareTypes: [
      ],
      shareScopes: [
        '全市',
        '市本级',
        '其他'
      ],
      dataScopes: [
        '全市数据',
        '市本级数据',
        '区本级数据',
        '其他'
      ],
      // yesOrNoStrArr: [{
      //   value: '01',
      //   label: '无条件开放'
      // }, {
      //   value: '02',
      //   label: '有条件开放'
      // }, , {
      //   value: '03',
      //   label: '不予开放'
      // }],
      yesOrNoIntArr: [{
        value: 1,
        label: '是'
      }, {
        value: 0,
        label: '否'
      }],
      relBusiOptions: [],
      dataObj: {},
      themeClassifyArr: [],
      resClassifyNode: undefined,
      baseClassifyNode: [],
      showSelectTree: false,
      treeSelectNum1: 0,
      treeSelectNum2: 0,
      multipleSelection: [],
      currentDate: '',
      uviewstrId: 0,
      componentObjs: {},
      formtableData: [],
      uviewId: '',
      rulesArr: [],
      regSelect: [{
        value: '0',
        label: '掩码规则'
      }, {
        value: '1',
        label: '正则表达式'
      }, {
        value: '2',
        label: '字符替换'
      }],
      keyMap: {
        'SJXMC': 'srcField',
        'SJLX': 'srcDataTyp',
        'XSXH': 'sno',
        'SJXMS': 'itemRemark',
        'YWBS': 'engCd',
        'SJCD': 'dataLen'
      },
      tuominId: ''
    }
  },
  created() {
    this.rowId = this.$route.params.id
    this.$store.dispatch('setInfoReBackDeptId', this.$route.query.provOrgCode)
    this.dataObj.provOrgCode = this.$route.query.provOrgCode
    this.dataObj.provOrgId = this.$route.query.provOrgId
    this.dataObj.provOrgName = this.$route.query.provOrgName
    this.dataObj.socialCreditCd = this.$route.query.socialCreditCd
    this.dataObj.activeName = this.$route.query.activeName ? this.$route.query.activeName.toString() : '0'
    this.activeName = this.dataObj.activeName || '0'
    this.departmentForm.provOrgName = this.dataObj.provOrgName
    this.departmentForm.provOrgCode = this.dataObj.socialCreditCd

    this.departmentForm.pubDt = new Date().toLocaleDateString().replace(/\//g, '-');
    this.getSelectTreedata()
    // 部门信息资源分类树选择typCd 3
    this.getResClassifyTree()
    // 基本信息资源分类树 typCd 1
    this.getBaseClassifyTree()
    // 主题信息资源分类树 typCd 2
    this.getThemeClassifyTree()
    this.getSelect2()
    this.getSelect1()
    this.getShareTypes()
    this.getUpdateCycles()
    this.getDataType()
    // 信息项列表查询

    // 获取组件类型数据字典
    this.getComponents()
    // 非0 为编辑 获取详情数据
    if (this.id != '0') {
      this.getResFormData()
    } else {
      this.showSelectTree = true
    }
    this.getTableHeader()
    //获取部门下的权责清单树
    this.getBusinessTreeByDeptId(this.$route.query.provOrgId)
  },
  methods: {
    treeClickChange() {
      // getCheckedNodes
      this.departmentForm.relBusList = this.$refs.treeRelBus.getCheckedKeys(true);
    },
    getSelectText(type, attr, data) {

    },
    close(dialog, treeDom) {
      this[dialog] = false
      // this.$refs[treeDom].setCheckedKeys([])
    },
    //
    getBusinessTreeByDeptId(detpId) {
      var params = { 'deptId': detpId }
      getBusinessTreeByDeptId(params).then(response => {
        if (response.data.errno == 0) {
          this.relBusiOptions = response.data.data
        }
      })
    },
    // 获取数据类型
    getDataType() {
      getDataTypes({ type: 'data_type' })
        .then(response => {
          if (response.data.errno == 0) {
            this.serviceObjs = response.data.data
          }
        })
    },
    getComponents() {
      getDataTypes({ type: 'component_type' })
        .then(response => {
          if (response.data.errno == 0) {
            response.data.data.forEach(item => {
              this.componentObjs[item.value] = item.name
            })
          }
        })
    },
    // 删除
    deleteRes() {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: '请至少选择一条信息资源',
          type: 'error'
        })
      } else {
        this.delDialog = true
      }
    },
    isRegRadio(data) {
      console.log(data)
      if (data == '01') {
        this.isShowRegRadio = true
      } else {
        this.isShowRegRadio = false
      }
    },
    getChangeReg(data) {
      console.log(data)
      if (data == '0') {
        this.isShowStrInput = true
        this.isShowregexpres = false
      } else if (data == '1') {
        this.isShowStrInput = false
        this.isShowregexpres = true
      } else {
        this.isShowStrInput = false
        this.isShowregexpres = false
      }
    },
    // 脱敏保存
    saveRegSet() {
      const isDelet = this.isReg
      if (isDelet == '01') {
        //  新增/编辑
        // let isEdit = this.regSetForm.id
        // if(!isEdit){
        // let sendObj = Object.assign({},this.regSetForm,{itemId:1},{deleted:0})
        const sendObj = Object.assign({}, this.regSetForm, { itemId: this.tuominId }, { deleted: 0 })
        delete sendObj.isReg
        saveAddOrUpdate(sendObj)
          .then(response => {
            if (response.data.errno == 0) {
              this.regSetForm.id ? this.$notify.success(`更新脱敏成功`) : this.$notify.success(`新增脱敏成功`)
              this.regSetForm = {}
              this.regsetDialog = false
            }
          }).catch(err => {
            this.$message({
              message: err.data.errmsg,
              type: 'error'
            })
          })
      } else {
        // 删除
        if (this.regSetForm.id) {
          delRegInfo({ id: this.regSetForm.id })
            .then(response => {
              if (response.data.errno == 0) {
                this.$notify.success(`删除脱敏成功`)
                this.regSetForm = {}
                this.regsetDialog = false
              }
            })
        } else {
          this.regsetDialog = false
        }
      }

      this.regsetDialog = false
    },
    // 确认删除
    confirmDelete() {
      const listData = this.multipleSelection
      const idArr = []
      for (const i in listData) {
        idArr.push(listData[i].uviewstrId)
      }
      deleteInfo(
        `/cataInfoItemTempRel/delete?uviewId=${this.rowId}`,
        idArr
      )
        .then(response => {
          if (response.data.errno == 0) {
            this.$notify.success('删除成功')
            this.getTableData()
            this.delDialog = false
            this.tempDelData = {}
          }
        })
    },
    // 表单数据回显
    getResFormData() {
      // 获取详情
      resDetail({ id: this.id }).then(res => {
        if (res.data.errno === 0) {
          // 给树进行赋值 以及对开放条件 共享条件进项diable
          if (res.data.data.pubSts == 0) {
            this.isOpenDisabled = true
          }
          if (res.data.data.shareLv != 2) {
            this.isDisabled = true
          }
          this.departmentForm = Object.assign({}, res.data.data)
          var baseArray = []
          res.data.data.cataInfoTempTypeRelDtoList.forEach(item => {
            if (item.type == 3) {
              this.departmentForm.resClassify = item.typeName
              this.resClassifyNode = item
            }
            if (item.type == 1) {
              baseArray.push(item.typeName)
              this.baseClassifyNode.push({
                typId: item.typeId,
                typNm: item.typeName
              })
              this.baseDefaultCheckedKeys.push(item.typeId)
            }
            if (item.type == 2) {
              this.selectOrgs1.push(item.typeId)
              // this.themeClassifyArr = []
              this.themeClassifyArr.push(item)
            }
          })
          this.departmentForm.baseClassify = baseArray.join('/')
          this.getSelect2(this.departmentForm.mediaFmt)

          console.log(this.departmentForm)
          this.showSelectTree = true
        }
      })
    },
    // 获取列表表头
    getTableHeader() {
      const sendParam = {
        itemMark: '',
        itemName: '',
        itemNameAndItemMark: '',
        limit: 100,
        page: 1
      }
      getFormItem(sendParam)
        .then(response => {
          if (response.data.errno == 0) {
            // this.formtableData = response.data.data.records
            var promises = []
            response.data.data.records.forEach(item => {
              item.selectsObj = {}
              if (item.itemCode) {
                promises.push(
                  getDictByType({ type: item.itemCode }).then(res => {
                    res.data.data.forEach(item1 => {
                      item.selectsObj[item1.value] = item1.name
                    })
                  })
                )
              }
              if (item.itemType == '04') {
                item.multiple = true
              }
            })
            Promise.all(promises).then(() => {
              this.formtableData.insideArr = response.data.data.records.filter(item => {
                return item.isInside == 1
              })
              this.formtableData.insideArr.forEach(_item => {
                for (var key in this.keyMap) {
                  if (_item['itemMark'] === key) {
                    _item['itemMark'] = this.keyMap[key]
                  }
                }
              })

              this.formtableData.outsideArr = response.data.data.records.filter(item => {
                return item.isInside != 1
              })
              this.showInfoItemsTable = true
              if (this.activeName == '1') {
                setTimeout(() => {
                  this.getTableData()
                }, 20)
              }
            })
          }
        })
    },

    // 获取表单项目
    getFormItems(cb, type) {
      const sendParam = {
        itemMark: '',
        itemName: '',
        itemNameAndItemMark: '',
        limit: 100,
        page: 1
      }
      getFormItem(sendParam)
        .then(response => {
          if (response.data.errno == 0) {
            var promises = []
            const arr = []
            response.data.data.records.forEach(item => {
              if (item.itemCode) {
                promises.push(
                  getDictByType({ type: item.itemCode }).then(res => {
                    item.selects = res.data.data
                  }
                  )
                )
              }
            })
            Promise.all(promises).then(() => {
              this.formDatas = response.data.data.records.filter(item => {
                return item.isInside == 1
              })
              this.formDatasNoInside = response.data.data.records.filter(item => {
                return item.isInside != 1
              })
              this.formDatas.forEach(item => {
                if (item.isChoice == '是') {
                  arr.push(item.itemMark)
                }
              })
              this.formDatasNoInside.forEach((item, index) => {
                if (item.isChoice == '是') {
                  arr.push(`ext${index + 1}`)
                }
              })
              this.rulesArr = arr
              this.rulesArr.forEach(_item => {
                if (!this.createRules[_item]) {
                  this.createRules[_item] = [
                    { required: true, message: '该项为必填项', trigger: 'change' }
                  ]
                }
              })
              if (cb) { cb() }
              if (type == 'add') {
                this.getOrderNumber()
              } else {
                this.addWorkOrder = true
              }
            })
            // console.log(this.formDatas)
          }
        })
    },
    // 新增权责清单
    addRelated(type) {
      this.dialogTitle = '新增数据项'
      this.addDataItemForm = {}
      this.getFormItems(null, type)
    },
    getOrderNumber() {
      getOrderNum({ uviewId: this.rowId }).then(res => {
        console.log(this.addDataItemForm)
        this.addWorkOrder = true
        this.addDataItemForm = Object.assign({}, this.addDataItemForm, {
          'XSXH': res.data.data * 1
        })
      })
    },
    editListRow(row) {
      // console.log(row)
      this.addDataItemForm = {}
      // this.addDataItemForm = Object.assign({}, row)
      this.uviewstrId = row.uviewstrId
      this.dialogTitle = '编辑数据项'
      this.getFormItems(() => {
        this.addDataItemForm = Object.assign({}, row)
        this.formDatas.forEach((item, i) => {
          if (item.itemType == '04' && this.addDataItemForm[this.keyMap[item.itemMark]] && this.addDataItemForm[this.keyMap[item.itemMark]].toString().indexOf('[') < 0) {
            this.addDataItemForm[this.keyMap[item.itemMark]] = [this.addDataItemForm[this.keyMap[item.itemMark]]]
          }
          if (item.itemType == '03' && this.addDataItemForm[this.keyMap[item.itemMark]] && this.addDataItemForm[this.keyMap[item.itemMark]].toString().indexOf('[') > -1) {
            this.addDataItemForm[this.keyMap[item.itemMark]] = JSON.parse(this.addDataItemForm[this.keyMap[item.itemMark]])[0]
          }
        })
        this.formDatasNoInside.forEach((item, i) => {
          if (item.itemType == '04' && this.addDataItemForm[`ext${i + 1}`] && this.addDataItemForm[`ext${i + 1}`].toString().indexOf('[') < 0) {
            this.addDataItemForm[`ext${i + 1}`] = [this.addDataItemForm[`ext${i + 1}`]]
          }
          if (item.itemType == '03' && this.addDataItemForm[`ext${i + 1}`] && this.addDataItemForm[`ext${i + 1}`].toString().indexOf('[') > -1) {
            this.addDataItemForm[`ext${i + 1}`] = JSON.parse(this.addDataItemForm[`ext${i + 1}`])[0]
          }
        })
        // 多选数组分割处理
        for (const key in this.addDataItemForm) {
          if (!Array.isArray(this.addDataItemForm[key]) && this.addDataItemForm[key].toString().charAt(0) === '[' && this.addDataItemForm[key].toString().charAt(this.addDataItemForm[key].length - 1) === ']') {
            this.addDataItemForm[key] = JSON.parse(this.addDataItemForm[key])
            console.log(key, this.addDataItemForm[key])
          } else {
          }
        }
        this.addDataItemForm = Object.assign({}, this.addDataItemForm, {
          SJCD: row.dataLen || '',
          YWBS: row.engCd || '',
          SJXMS: row.itemRemark || '',
          XSXH: row.sno || '',
          SJLX: row.srcDataTyp || '',
          SJXMC: row.srcField || ''
        })

        // console.log(this.addDataItemForm)
      })
      // this.addDataItemForm = Object.assign({},row)
      // this.addWorkOrder = true
      //
    },

    /**
     * 脱敏弹框
     */
    regReset(row) {
      this.tuominId = row.uviewstrId
      getRegInfo({ itemId: row.uviewstrId }).then(res => {
        if (res.data.errno == 0) {
          if (res.data.data) {
            this.isReg = '01'
            if (res.data.data.type == '0') {
              this.isShowStrInput = true
              this.isShowregexpres = false
            } else if (res.data.data.type == '1') {
              this.isShowStrInput = false
              this.isShowregexpres = true
            } else {
              this.isShowStrInput = false
              this.isShowregexpres = false
            }

            this.regSetForm = Object.assign({}, res.data.data)
            this.isShowRegRadio = true
            this.regsetDialog = true
          } else {
            this.isReg = '02',
              this.regSetForm = {}
            this.isShowRegRadio = false
            this.regsetDialog = true
          }
        }
      })
    },
    closeAddDialog() {
      this.searchRelatedResults = []
      this.searchTotal = 0
      this.searchQuery.current = 1
      this.searchQuery.keyword = ''
    },
    // 信息项维护
    getTableData() {
      infoResDetail(Object.assign({}, this.listQuery, { uviewId: this.rowId != 0 ? this.rowId : this.uviewId }))
        .then(response => {
          if (response.data.errno == 0) {
            const resultArr = []
            this.tableData = response.data.data.records
            console.log(this.tableData)
            this.total = response.data.data.total
          }
        })
        .catch(() => {
          this.list = []
          this.total = 0
        })
    },
    selcetResClassify() {
      if (this.resClassifyNode) {
        let value = this.resClassifyNode.typeId || this.resClassifyNode.typId
        this.deptDefaultCheckedKeys = [value]
      } else {
        this.deptDefaultCheckedKeys = []
      }
      this.selectResClassifyDialog = true
    },
    baseClassifySelect() {
      // if (this.baseClassifyNode && this.baseClassifyNode.length > 0) {
      //   this.$refs.basetreeDom.setCheckedNodes(this.baseClassifyNode)
      // }
      this.baseClassifyDialog = true
    },
    themeClassifySelect() {
      this.themeClassifyDialog = true
    },
    handleTabClick(tab, event) {
      this.getTableData()
      console.log(tab, event)
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    RelatedResultsSelectionChange(val) {
      console.log(val)
      this.choosedRelatedResults = val
    },
    changeType(data) {
      console.log(data)
      if (data != '02') {
        this.isDisabled = true
        this.departmentForm.shareCondition = '无'
        this.$refs.shareConditionDom.clearValidate()
      } else {
        this.isDisabled = false
        this.departmentForm.shareCondition = ''
      }
    },
    changeRadio(data) {

      // if (data == '01') {
      //   this.departmentForm = Object.assign({}, this.departmentForm, { pubCondition: '' })
      //   this.isOpenDisabled = false
      // } else {
      //   this.departmentForm.pubCondition = '无'
      //   this.isOpenDisabled = true
      //   this.$refs.pubConditionDom.clearValidate()
      // }
      if (data != '02') {
        this.departmentForm.pubCondition = '无'
        this.isOpenDisabled = true
        this.$refs.pubConditionDom.clearValidate()
      } else {
        this.departmentForm.pubCondition = ''
        this.isOpenDisabled = false
      }
    },
    submit() {
      this.$refs.departmentFormDom.validate(valid => {
        if (valid) {
          // 格式化信息资源分类数据组装
          // 类型 1-基础信息 2-主题信息 3-部门信息
          const tempArr = []
          const cataInfoTempTypeRelList = []
          if (this.resClassifyNode) {
            tempArr.push(Object.assign({}, this.resClassifyNode, { _type: '3' }))
          }
          if (this.baseClassifyNode) {
            for (var i = 0; i < this.baseClassifyNode.length; i++) {
              tempArr.push(Object.assign({}, this.baseClassifyNode[i], { _type: '1' }))
            }
          }
          if (this.themeClassifyArr.length > 0) {
            this.themeClassifyArr.forEach(item => {
              item._type = '2'
              tempArr.push(item)
            })
            // console.log(tempArr)
          }
          tempArr.forEach(item => {
            cataInfoTempTypeRelList.push({
              infoId: this.id != 0 ? this.id : '',
              typeId: item.typId || item.typeId,
              type: item._type
            })
          })
          this.departmentForm.uviewNm = this.departmentForm.uviewNm.replace(/\s+/g, '')

          const sendData = Object.assign({}, this.departmentForm, { auditStatus: this.id != 0 ? this.departmentForm.auditStatus : '0', cataInfoTempTypeRelList: cataInfoTempTypeRelList, provOrgId: this.dataObj.provOrgId * 1 })
          // console.log(sendData)
          // 调用新增接口
          saveOrUpdateRes(sendData).then(res => {
            if (res.data.errno === 0) {
              this.id != 0 ? this.$notify.success(`编辑成功`) : this.$notify.success(`新增成功`)
              this.rowId = this.uviewId = res.data.data ? res.data.data : this.rowId
              // 新增成功后 应根据返回的id replace到 编辑页面 对应的 activeName为1 编辑成功不需要
              // 这么做是为了 解决用户手动刷新浏览器还是在 新增页面 错以为数据丢失实际已经添加了
              if (this.id == 0) {
                this.$router.replace({
                  path: `/Cataloging/infoCatalogMaintain/${this.rowId}`, query: {
                    provOrgId: this.dataObj.provOrgId,
                    provOrgName: this.dataObj.provOrgName,
                    provOrgCode: this.dataObj.provOrgCode,
                    socialCreditCd: this.dataObj.socialCreditCd,
                    activeName: 1
                  }
                })
              } else {
                setTimeout(() => {
                  this.isSaveSuccess = false
                  this.getTableData()
                  this.activeName = '1'
                  document.body.scrollTop = 0
                  document.documentElement.scrollTop = 0
                  // this.$router.push({path:'/Cataloging/infoCatalogList'})
                }, 120)
              }
            }
          }).catch(err => {
            this.$message.error(err.data.errmsg || '出错了~')
          })
          // this.$notify.success(`${this.dialogTitle}成功`);
        }
      })
    },
    reset() {
      this.$router.push({ path: '/Cataloging/infoCatalogList' })
    },
    // 新增信息项维护弹窗保存
    addMenuDetail() {
      this.$refs.addbusinessForm.validate(valid => {
        if (valid) {
          const param = {
            uviewId: this.rowId,
            uviewstrId: this.uviewstrId && this.uviewstrId != 0 ? this.uviewstrId : 0,
            dataLen: this.addDataItemForm['SJCD'],
            engCd: this.addDataItemForm['YWBS'],
            itemRemark: this.addDataItemForm['SJXMS'],
            sno: parseInt(this.addDataItemForm['XSXH']),
            srcDataTyp: this.addDataItemForm['SJLX'],
            srcField: this.addDataItemForm['SJXMC']
          }
          // 多选数组分割处理
          for (const key in this.addDataItemForm) {
            if (Array.isArray(this.addDataItemForm[key])) {
              this.addDataItemForm[key] = JSON.stringify(this.addDataItemForm[key])
            }
          }
          const sendData = Object.assign({}, this.addDataItemForm, param)
          if (this.dialogTitle == '新增数据项') {
            createInfo(sendData)
              .then(response => {
                if (response.data.errmsg == '英文标识已经存在') {
                  this.$message({
                    message: '英文标识已经存在,请重新输入',
                    type: 'error'
                  })
                } else if (response.data.errno == 0) {
                  this.$notify.success(`${this.dialogTitle}成功`)
                  this.addWorkOrder = false
                  this.listQuery.page = 1
                  this.getTableData()
                }
              }).catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          } else {
            editInfo(sendData)
              .then(response => {
                if (response.data.errmsg == '英文标识已经存在') {
                  this.$message({
                    message: '英文标识已经存在,请重新输入',
                    type: 'error'
                  })
                } else if (response.data.errno == 0) {
                  this.$notify.success(`${this.dialogTitle}成功`)
                  this.addWorkOrder = false
                  this.addDataItemForm = {}
                  this.listQuery.page = 1
                  this.getTableData()
                }
              }).catch(err => {
                this.$message.error(err.data.errmsg || '出错了')
              })
          }
        }
      })
    },
    // 信息格式类型
    getSelect2(num) {
      if (!num || num == 0) { this.departmentForm = Object.assign({}, this.departmentForm, { mediaFmtType: '' }) }
      getInfoSelect1({ 'type': `format_type_${num || this.departmentForm.mediaFmt}` })
        .then(response => {
          this.select2 = response.data.data || []
          console.log(this.select2)
        })
    },
    // 信息格式分类
    getSelect1() {
      getInfoSelect1({ 'type': 'format_type' })
        .then(response => {
          this.select1 = response.data.data
        })
    },
    getShareTypes() {
      getInfoSelect1({ 'type': 'share_type' })
        .then(response => {
          this.shareTypes = response.data.data
        })
    },
    // 周期
    getUpdateCycles() {
      getInfoSelect1({ 'type': 'update_cycle' })
        .then(response => {
          this.updateCycles = response.data.data
          console.log(this.updateCycles)
        })
    },
    // 内设部门树
    getSelectTreedata() {
      getOrgTree({ 'orgId': this.dataObj.provOrgId })
        .then(response => {
          this.selectTreedata = response.data.data
        })
    },
    //
    departmentChange(valueArray) {
      var belongToCodeArray = []
      for (var i = 0; i < valueArray.length; i++) {
        var targetOrg = this.selectTreedata.find(item => { return item.orgId == valueArray[i] })
        belongToCodeArray.push(targetOrg.orgCd)
      }
      this.departmentForm.belongToCode = belongToCodeArray.join()
      this.departmentForm.belongTo = this.departmentForm.belongToList.join()

    },
    getCheckedOrg1(args) {
      // this.checkedTreeNodes = args[1];
      this.themeClassifyArr = args[1]
    },
    // 部门信息资源分类树选择change
    handleCheckChange(data, checked, node) {
      if (data == null) {
        return
      }
      // if (data.children == null) {
      //   this.treeSelectNum2++
      // } else {
      //   if (data.children.length > 0 && this.checkStrictly) {
      //     this.treeSelectNum2++
      //   } else {
      //     this.treeSelectNum2++
      //   }
      // }
      this.treeSelectNum2++
      let res
      if (this.isMultiple) {
      } else {
        if (this.treeSelectNum2 % 2 === 0) {
          if (checked) {
            this.$refs.treeDom.setCheckedNodes([])
            this.$refs.treeDom.setCheckedNodes([data])
            // 交叉点击节点
          } else {
            this.$refs.treeDom.setCheckedNodes([])
            // 点击已经选中的节点，置空
          }
        }
      }
    },
    handleCheckChange1(data, checked, node) {
      if (data == null) {
        return
      }
      // if (data.children == null) {
      //   this.treeSelectNum1++
      // } else {
      //   if (data.children.length > 0 && this.checkStrictly) {
      //     this.treeSelectNum1++
      //   } else {
      //     this.treeSelectNum1++
      //   }
      // }
      // this.treeSelectNum1++
      // let res
      // if (this.isMultiple) {
      // } else {
      //   if (this.treeSelectNum1 % 2 === 0) {
      //     if (checked) {
      //       this.$refs.basetreeDom.setCheckedNodes([])
      //       this.$refs.basetreeDom.setCheckedNodes([data])
      //       // 交叉点击节点
      //     } else {
      //       this.$refs.basetreeDom.setCheckedNodes([])
      //       // 点击已经选中的节点，置空
      //     }
      //   }
      // }
    },
    // 部门信息资源分类树选择
    getResClassifyTree() {
      getDepartRes({ 'code': '3', 'orgId': (this.dataObj.provOrgId) * 1 })
        .then(response => {
          // 数据处理 只有第三级或一下 才可以点击
          const res = []
          function normalLizedArr(arr) {
            arr.forEach((item, index) => {
              if (item.typType !== '细目') {
                item.disabled = true
                normalLizedArr(item.children)
              }
            })
          }
          normalLizedArr(response.data.data)
          this.treeData = response.data.data
          console.log(this.treeData)
        })
    },
    // 基本信息资源分类树
    getBaseClassifyTree() {
      getDepartRes({ 'code': '1', 'orgId': (this.dataObj.provOrgId) * 1 })
        .then(response => {
          this.basetreeData = response.data.data
        })
    },
    // 主题信息资源分类树
    getThemeClassifyTree() {
      getDepartRes({ 'code': '2', 'orgId': (this.dataObj.provOrgId) * 1 })
        .then(response => {
          this.selectTreedata1 = response.data.data
        })
    },
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    resClassifyConfirm() {
      const selectValue = this.$refs.treeDom.getCheckedNodes(false, true)[0]
      // console.log(selectValue)
      if (selectValue == undefined || selectValue == null || selectValue == '') {
        this.$message({
          message: '请选择一条信息资源',
          type: 'error'
        })
      } else {
        getUviewNo({ fullTypCd: selectValue.fullTypCd }).then(res => {
          this.departmentForm = Object.assign({}, this.departmentForm, {
            resClassify: selectValue.typNm,
            uviewNo: res.data.errmsg
          })
          this.resClassifyNode = selectValue
          this.$refs.resClassifyDom.clearValidate()
          this.$refs.uviewNoDom.clearValidate()
          this.selectResClassifyDialog = false
        })
      }
    },

    handleNodeClick(data) {
      true
      console.log(data)
    },
    // 基础资源分类树确定
    baseClassifyConfirm() {
      const selectValue = this.$refs.basetreeDom.getCheckedNodes(false, true)
      if (selectValue && selectValue.length > 0) {
        var nameArray = []
        for (var i = 0; i < selectValue.length; i++) {
          nameArray.push(selectValue[i].typNm)
        }
        this.departmentForm.baseClassify = nameArray.join('/')
      } else {
        this.departmentForm.baseClassify = ''
      }
      this.baseClassifyNode = selectValue
      // if (selectValue == undefined || selectValue == null || selectValue == '') {
      //   this.$message({
      //     message: '请选择一条基础信息资源',
      //     type: 'error'
      //   })
      // } else {
      //   this.baseClassifyNode = selectValue
      //   this.departmentForm.baseClassify = selectValue.typNm
      //   this.baseClassifyDialog = false
      // }

      // if (selectValue) {
      //   this.baseClassifyNode = selectValue
      //   this.departmentForm.baseClassify = selectValue.typNm
      // }else{
      //   this.baseClassifyNode = null
      //   this.departmentForm.baseClassify = null
      // }
      this.baseClassifyDialog = false
    }
  }
}
</script>

<style scoped>
.search_result {
  margin: 0 auto;
  margin-top: 20px;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.department_content {
  background: #fff;
  border: 1px solid rgba(230, 229, 234, 1);
  border-radius: 6px;
  min-height: 750px !important;
  height: auto !important;
}

.tree_title {
  padding-left: 30px;
  height: 60px;
  box-sizing: border-box;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  line-height: 60px;
  opacity: 1;
}

.tree_content {
  padding-top: 24px;
  padding-left: 20px;
  padding-bottom: 24px;
  box-sizing: border-box;
}

.tree_city {
  margin-right: 12px;
  margin-left: 6px;
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(36, 36, 36, 1);
  opacity: 1;
}

.tree_desc,
.type_area {
  font-size: 16px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
}

.type_area {
  line-height: 60px;
  padding-left: 45px;
  border-bottom: 1px solid rgba(230, 229, 234, 1);
  height: 60px;
  box-sizing: border-box;
}

.tree_area {
  background: rgba(251, 251, 251, 1);
  border-right: 1px solid rgba(230, 229, 234, 1);
  opacity: 1;
  border-radius: 6px 0px 0px 6px;
  height: 100%;
}

.related_btns {
  margin-top: 10px;
  margin-bottom: 20px;
}

.tr_detail_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  text-align: center;
  margin-right: 14px;
  cursor: pointer;
}

.tr_detail_icon svg {
  cursor: pointer;
  width: 20px;
  height: 20px;
}

.tr_del_icon {
  box-sizing: border-box;
  display: inline-block;
  width: 20px;
  height: 20px;
  border-radius: 50% 50%;
  border: 2px solid rgba(46, 78, 161, 1);
  border-radius: 50%;
  opacity: 1;
  text-align: center;
  position: relative;
  cursor: pointer;
}

.tr_del_icon svg {
  cursor: pointer;
  width: 10px;
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  margin: auto;
}

.tab_area {
  padding-left: 40px;
  padding-right: 40px;
}

.selectBtn :hover,
.selectBtn:focus {
  color: rgba(31, 51, 101, 0.9) !important;
}

.codeHint {
  font-size: 12px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  line-height: 33px;
  color: rgba(126, 126, 126, 1);
  opacity: 1;
}

.multipleResSelect {
  width: 100% !important;
}
</style>
<style>
.res_tabs .el-tabs__item.is-active {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: rgba(23, 40, 83, 1);
  opacity: 1;
}

.res_tabs .el-tabs__item {
  font-size: 14px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: rgba(77, 77, 77, 1);
  opacity: 1;
}
.el-tag {
  margin-right: 10px;
}
.button-new-tag {
  margin-right: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
</style>
