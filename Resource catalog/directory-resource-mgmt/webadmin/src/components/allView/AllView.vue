<template>
  <div id="allView" style="overflow: auto" align="center">
    <div style="position: fixed; width: 20px; height: 60px">
      <!-- <img :id="allResBtnId" title="全部资源" @click="allResBtnOnClick" width="20px" height="20px"  :src="btn_allres" style="margin-bottom: 3px; cursor: pointer">
      <img :id="partResBtnId" title="最佳效果" @click="partResBtnOnClick" width="20px" height="20px"  :src="btn_partres" style="margin-top: 3px;margin-bottom: 3px;cursor: pointer"> -->
      <img title="放大" width="20px" height="20px" :src="zoomIn" style="margin-top: 3px;margin-bottom: 3px;cursor: pointer" @click="zoomInSvg">
      <img title="缩小" width="20px" height="20px" :src="zoomOut" style="margin-top: 3px;cursor: pointer" @click="zoomOutSvg">
    </div>
    <div style="width:100%;height: 700px">
      <svg :id="topoSvgId" />
    </div>
  </div>
</template>
<script>
//   import API from '@/common/api'
import $ from 'jquery'
import zoomIn from '@/assets/zoomIn.png'
import zoomOut from '@/assets/zoomOut.png'
import { createResourceAllView, createDepartViewTree, createCategoryViewTree, isIE } from '@/utils/d3-topology'
export default {
  name: 'AllView',
  props: {
    currentId: {
      type: String,
      required: true
    },
    showType: {
      type: String,
      required: true
    },
    topoSvgId: {
      type: String,
      required: true
    },
    allResBtnId: {
      type: String,
      required: true
    },
    partResBtnId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      btn_allres: '/static/images/btn_allres_light.png',
      btn_partres: '/static/images/btn_partres_light.png',
      zoomIn: zoomIn,
      zoomOut: zoomOut,
      current: [],
      allData: [],
      scale: 1,
      initTranslate: '',
      maxZoom: 20,
      minZoom: 0.4,
      centerX: -1,
      centerY: -1,
      svgHeight: -1,
      svgWidht: -1,
      viewData:
       [
         {
           'id': '3748',
           'outResId': '1389',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': 'dataAuth',
           'description': '数据鉴权测试',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/8f3cfbcfec5c4c63a0fa6b7a8ecc608f',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1001',
               'typeName': '1234'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1553671080000,
           'updateTime': 1553671118000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3747',
           'outResId': '1385',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': 'ceshiceshi01',
           'description': 'test',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/64f5a2085d33496499a494fc822f394d',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '884',
               'typeName': 'test'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1553074855000,
           'updateTime': 1553764530000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '008',
             'typeName': '警务督察处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3746',
           'outResId': '1251',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '测试资源目录',
           'description': '1',
           'availableCitys': '西安',
           'resSign': 'qaz',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-api/510400/39fc9438f2444f288e4328bf60df3fbf',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1001',
               'typeName': '1234'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1552473084000,
           'updateTime': 1553831348000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3745',
           'outResId': '1383',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': 'ceshi0002',
           'description': '3',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/c0df783f0f74452eb20e2baefcf75537',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1001',
               'typeName': '1234'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1551769496000,
           'updateTime': 1553668597000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3744',
           'outResId': '1381',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '12',
           'description': '12',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/5dc822f6297f45879a227685d97f1951',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1001',
               'typeName': '1234'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1551768827000,
           'updateTime': 1551768885000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3520',
           'outResId': '1165',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '全国',
           'description': '全国',
           'availableCitys': '全国',
           'resSign': 'testgate1',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/mscx-app/510400/USER_50043/7be49848f01146359787a45c5999c20c',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '884',
               'typeName': 'test'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1545209036000,
           'updateTime': 1545270146000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3519',
           'outResId': '1163',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '全国',
           'description': 'test',
           'availableCitys': '全国',
           'resSign': 'test',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/mscx-app/510400/USER_50043/61e8d480036e4d43a1f924954b0cc2ef',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '884',
               'typeName': 'test'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1545198207000,
           'updateTime': 1545212527000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3515',
           'outResId': '1247',
           'targetCustomers': [
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': '漳州测试生成API2',
           'description': '坎坎坷坷扩扩扩',
           'availableCitys': '漳州',
           'resSign': 'zztestapi2',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-api/510400/62ff7ac8ef17475e89a6df830abf6bed',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '785',
               'typeName': 'KPI'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1530333616000,
           'updateTime': 1530340958000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3501',
           'outResId': '1227',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '网关关键字',
           'description': '网关搜索',
           'availableCitys': '西安',
           'resSign': 'haoyp',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-api/510400/f8421f5a271246a0918eddb3d5af08f9',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1001',
               'typeName': '1234'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1530253972000,
           'updateTime': 1530253971000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3498',
           'outResId': '1221',
           'targetCustomers': [
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '企业缴纳公积金总额',
           'description': '企业缴纳公积金总额',
           'availableCitys': '攀枝花',
           'resSign': 'gongjijinchaxun',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-api/510400/5b82fa7b2f004ab583eed7c02088adb7',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '255',
               'typeName': '公积金'
             },
             {
               'typeId': '884',
               'typeName': 'test'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1525944453000,
           'updateTime': 1525944453000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': 'test',
           'outItem': 'test',
           'matchLevel': null,
           'renewalFrequency': {
             'typeId': null,
             'typeName': ''
           },
           'existHome': 'Y',
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3492',
           'outResId': '1135',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核中微应用43',
           'description': '审核中比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appbtzt430',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/c7b27ce8f8b04c2b9c18fcd6221b2be9',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '13',
               'typeName': '学位'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524799701000,
           'updateTime': 1524799701000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': 'Y',
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3495',
           'outResId': '1161',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': '测试有条件共享微应用',
           'description': '一卡通查询',
           'availableCitys': '测试',
           'resSign': 'csytj',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/mscx-app/510400/USER_50043/f9ca0a5a5fcd4bc28f03db913fdbde21',
           'serviceCategory': {
             'typeId': '25004',
             'typeName': '价格监管'
           },
           'serviceTags': [
             {
               'typeId': '62',
               'typeName': '测试'
             },
             {
               'typeId': '923',
               'typeName': '        测试1  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524796678000,
           'updateTime': 1524796678000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '001',
             'typeName': null
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3494',
           'outResId': '1159',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': '测试无条件共享微应用',
           'description': '一卡通查询服务为您提供北京一卡通的消费记录查询，让您对您的每一笔账单都心中有数。',
           'availableCitys': '测试',
           'resSign': 'yikatong',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/mscx-app/510400/USER_50043/1ffc62b71dec40daba01ad23895ddc16',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '62',
               'typeName': '测试'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524795638000,
           'updateTime': 1524795638000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '005',
             'typeName': '综合科'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3736',
           'outResId': '1373',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '李玉苹测试有条件共享11',
           'description': '李玉苹测试有条件共享11',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/c22700b17d33461a80a431cd996bad92',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '364',
               'typeName': '公共交通'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524737040000,
           'updateTime': 1524737050000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '005',
             'typeName': '综合科'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3735',
           'outResId': '1371',
           'targetCustomers': [
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '李玉苹测试无条件共享数据1',
           'description': '啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/4afee383a39146f9ad19bad2eec482dd',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '923',
               'typeName': '        测试1  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524735523000,
           'updateTime': 1552461602000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '013',
             'typeName': '地籍管理处'
           },
           'shareType': {
             'typeId': '03',
             'typeName': '不予共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3734',
           'outResId': '1369',
           'targetCustomers': [
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '李玉苹测试无条件共享数据',
           'description': '测试ceshi测试ceshi测试',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/13bb143cbc5544f8b69a0ca176954122',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '2',
               'typeName': '高校'
             },
             {
               'typeId': '923',
               'typeName': '        测试1  '
             },
             {
               'typeId': '957',
               'typeName': '空格测试'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524735449000,
           'updateTime': 1542273785000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400009',
             'typeName': '经济和信息化委员会'
           },
           'organization': {
             'typeId': '022',
             'typeName': '工业园区处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3733',
           'outResId': '1367',
           'targetCustomers': [
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '测试文件',
           'description': '测试文件',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/00745cd6310c4a708ed83b3f20dd0478',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '885',
               'typeName': 'test1'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524712981000,
           'updateTime': 1524712990000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '019',
             'typeName': '政府宣传处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3488',
           'outResId': '1211',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '测试文件存储',
           'description': '测试',
           'availableCitys': '不限',
           'resSign': 'testfs',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-api/510400/0a818a1a4f784406836ccc7fded45870',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '923',
               'typeName': '        测试1  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524557822000,
           'updateTime': 1524557873000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3732',
           'outResId': '1365',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': '测试文件存储',
           'description': '测试数据',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/share-data/510400/f61f949dc5f74792a98d42ade2231bbe',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '923',
               'typeName': '        测试1  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524554506000,
           'updateTime': 1524563407000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3493',
           'outResId': '1157',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '测试文件存储',
           'description': '测试',
           'availableCitys': '不限',
           'resSign': 'testfs',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/tmp/mscx-app/510400/USER_50043/84f6380613a94d10b1844b19ce33ad7e',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '923',
               'typeName': '        测试1  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1524547602000,
           'updateTime': 1524558084000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3731',
           'outResId': '1363',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': '李玉苹测试文件下载',
           'description': '测试',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/c6ed2b3d0e864999b398582accf7dc04',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '957',
               'typeName': '空格测试'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1523869683000,
           'updateTime': 1523869714000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3487',
           'outResId': '1209',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             }],
           'name': '李玉苹测试请求参数和返回示例格式',
           'description': '测试',
           'availableCitys': '海淀区',
           'resSign': 'ceshishili',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/3ecc2bd6fa6c4152a6c4a19bd1688cf8',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '62',
               'typeName': '测试'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1523869234000,
           'updateTime': 1523869355000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '005',
             'typeName': '综合科'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3486',
           'outResId': '1207',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             }],
           'name': '李玉苹测试API地址在前台显示样式',
           'description': '测试',
           'availableCitys': '北京',
           'resSign': 'ceshilyp',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/89a95d82fe3742a68abdb3e088fecdd3',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '62',
               'typeName': '测试'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1523869030000,
           'updateTime': 1523869030000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3477',
           'outResId': '1137',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过微应用43',
           'description': '审核通过比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appbtzt431',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/5c414d22b1d64f928cb8f9e0e630e41c',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '16',
               'typeName': '规范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521190992000,
           'updateTime': 1521190992000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': 'Y',
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3476',
           'outResId': '1133',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过微应用资源43',
           'description': '编辑审核通过比赛信息查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'appbj43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/tmp/mscx-app/510400/USER_50043/10fdd1bf700340deb99ac489a3dc2e3a',
           'serviceCategory': {
             'typeId': '25004',
             'typeName': '价格监管'
           },
           'serviceTags': [
             {
               'typeId': '13',
               'typeName': '学位'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521190502000,
           'updateTime': 1521190622000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3475',
           'outResId': '1127',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微服务前后加空格43',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appkg43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/779d7db2db2c4d5cae54dbaf1901077e',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521189920000,
           'updateTime': 1521189920000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': 'N',
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3474',
           'outResId': '1125',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证微应用必填项43',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appyz43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://101.200.52.215:19938/tmp/mscx-app/510400/USER_50043/3e91d52eefee440bb6b94df266acbc0d',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '10',
               'typeName': '器材配备'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521189788000,
           'updateTime': 1552989697000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3473',
           'outResId': '1123',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微应用文testCHANG20个字符43',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符43',
           'availableCitys': '北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花',
           'resSign': 'appt123456789Engli43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/9cd8115cf0cc4518805db102661b0d92',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '3',
               'typeName': '名单'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521189648000,
           'updateTime': 1521189648000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '019',
             'typeName': '政府宣传处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3472',
           'outResId': '1121',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件微服务资源43',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx432',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/0ca36ce8588d47348192f11c3bf94729',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '16',
               'typeName': '规范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521189463000,
           'updateTime': 1521189463000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3471',
           'outResId': '1119',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件微服务资源43',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx431',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/dbb12cc7448e414fa10e1039835c2342',
           'serviceCategory': {
             'typeId': '25006',
             'typeName': '信用体系'
           },
           'serviceTags': [
             {
               'typeId': '16',
               'typeName': '规范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521189354000,
           'updateTime': 1521189354000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '021',
             'typeName': '发展信息处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3293',
           'outResId': '1185',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过API资源43',
           'description': '审核通过二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'apibyzt431',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/f1593005eafa41aaa18c2ca5c576013e',
           'serviceCategory': {
             'typeId': '25006',
             'typeName': '信用体系'
           },
           'serviceTags': [
             {
               'typeId': '8',
               'typeName': '证书'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521187271000,
           'updateTime': 1521187271000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '时间',
           'outItem': '结果',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': 'Y',
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3292',
           'outResId': '1181',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过API资源43',
           'description': '编辑审核通过二级注册建造师查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'apibj43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/5c4bbf41c8244b2fb5f3ed7fba7bbdeb',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '12',
               'typeName': '中职'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521186881000,
           'updateTime': 1521186986000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3291',
           'outResId': '1175',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': 'API前后加空格43',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'apikg43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/7b0682d3869145ffbfc96c75c331d4ba',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521186360000,
           'updateTime': 1521186360000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '021',
             'typeName': '发展信息处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3290',
           'outResId': '1173',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': 'API文testCHANG20个字符43',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符43',
           'availableCitys': '北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花',
           'resSign': 'apit123456789Engli43',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/70659160049b4b968920fc52bfc1b404',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1',
               'typeName': '标准'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521186110000,
           'updateTime': 1521186109000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3289',
           'outResId': '1171',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件API资源43',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'apigxlx432',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/0cfb49c0c89f46868a5776ce5e161495',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '2',
               'typeName': '高校'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521185890000,
           'updateTime': 1521185889000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3288',
           'outResId': '1169',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件API资源43',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'apigxlx431',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/3c1c1fd690794fdd97ac6d5c06d98dea',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '12',
               'typeName': '中职'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521185764000,
           'updateTime': 1521185764000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '008',
             'typeName': '警务督察处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3730',
           'outResId': '1345',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过数据43',
           'description': '审核通过数据描述43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/72a82a6464d7409fbe23c2b1d36d4048',
           'serviceCategory': {
             'typeId': '25007',
             'typeName': '城乡建设'
           },
           'serviceTags': [
             {
               'typeId': '10',
               'typeName': '器材配备'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521183663000,
           'updateTime': 1521183673000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3729',
           'outResId': '1341',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过数据资源43',
           'description': '编辑审核通过数据描述43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/72304a8c10614d828c5be5680314c748',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '8',
               'typeName': '证书'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521182977000,
           'updateTime': 1521183351000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '019',
             'typeName': '政府宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3728',
           'outResId': '1335',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据前后加空格43',
           'description': '数据描述43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/b1ec1b118f0f40f9862798a7e67dcc90',
           'serviceCategory': {
             'typeId': '25006',
             'typeName': '信用体系'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '        空格  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521182633000,
           'updateTime': 1521182646000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3727',
           'outResId': '1333',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证数据必填项43',
           'description': '数据描述43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/bce60cc0db63408ab5eb1fa0100c9469',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '2',
               'typeName': '高校'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521182488000,
           'updateTime': 1521182502000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3726',
           'outResId': '1331',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据中文testCHANG20个字符43',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/dea95c2f408d4c7b809345a8a9b79846',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '9',
               'typeName': '要求'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521182375000,
           'updateTime': 1521182386000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3725',
           'outResId': '1329',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件数据资源43',
           'description': '有条件数据描述43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/6f9e5b9de2064250a63c179605241172',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '17',
               'typeName': '规程'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521182195000,
           'updateTime': 1521182206000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3724',
           'outResId': '1327',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件数据资源43',
           'description': '无条件数据描述43',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/d1fbc1587b01465f9e3417b62e0ec645',
           'serviceCategory': {
             'typeId': '25002',
             'typeName': '食品药品安全'
           },
           'serviceTags': [
             {
               'typeId': '7',
               'typeName': '中小学'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521182086000,
           'updateTime': 1521182098000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '002',
             'typeName': '贷款申请'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3723',
           'outResId': '1317',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过数据42',
           'description': '审核通过数据描述42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/80cd8aa467ee419294c3fa9a61254a24',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521177611000,
           'updateTime': 1521177622000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3722',
           'outResId': '1313',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过数据资源42',
           'description': '编辑审核通过数据描述42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/3fd6b3e2425744f28c4c07d6bec21c2c',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '9',
               'typeName': '要求'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521176915000,
           'updateTime': 1521177293000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3721',
           'outResId': '1307',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据前后加空格42',
           'description': '数据描述42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/f6261fa23a584e51a80874e1a97ecaef',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '        空格  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521176570000,
           'updateTime': 1521176582000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '002',
             'typeName': '贷款申请'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3720',
           'outResId': '1305',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证数据必填项42',
           'description': '数据描述42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/85d0e24e010344c5b7146f15fc28e87e',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '11',
               'typeName': '高考'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521176424000,
           'updateTime': 1521176437000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3719',
           'outResId': '1303',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据中文testCHANG20个字符42',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/8a6aaa587d524d23834d07f227f828ed',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521176306000,
           'updateTime': 1521176317000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '007',
             'typeName': '治安管理处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3718',
           'outResId': '1301',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件数据资源42',
           'description': '有条件数据描述42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/c7765cb0538a4ed28ad86d7c96485d6b',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521176135000,
           'updateTime': 1521176148000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3717',
           'outResId': '1299',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件数据资源42',
           'description': '无条件数据描述42',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/6125cfdf7b4e428081bac73ccb462e5d',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '2',
               'typeName': '高校'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521176027000,
           'updateTime': 1521176039000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '018',
             'typeName': '政府监督处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3716',
           'outResId': '1297',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过数据资源41',
           'description': '编辑审核通过数据描述41',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/f8c393aba3a64dc8a92dacd7d5028288',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '5',
               'typeName': '教师'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521144001000,
           'updateTime': 1521144374000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3715',
           'outResId': '1291',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据前后加空格41',
           'description': '数据描述41',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/147c52ba3c8d40678486508810160891',
           'serviceCategory': {
             'typeId': '25006',
             'typeName': '信用体系'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '        空格  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521143658000,
           'updateTime': 1521143670000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '018',
             'typeName': '政府监督处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3714',
           'outResId': '1289',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证数据必填项41',
           'description': '数据描述41',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/f137f2aba2f64dd9a426b05ab35411e0',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '4',
               'typeName': '指标'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521143516000,
           'updateTime': 1521143528000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '006',
             'typeName': '人事科'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3713',
           'outResId': '1287',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据中文testCHANG20个字符41',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符41',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/7aaac07cea834826af93136100285068',
           'serviceCategory': {
             'typeId': '25004',
             'typeName': '价格监管'
           },
           'serviceTags': [
             {
               'typeId': '3',
               'typeName': '名单'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521143403000,
           'updateTime': 1521143414000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '008',
             'typeName': '警务督察处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3712',
           'outResId': '1285',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件数据资源41',
           'description': '有条件数据描述41',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/bec719a731a54ea2855f18f908fcdcda',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '1',
               'typeName': '标准'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521143232000,
           'updateTime': 1521143244000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '006',
             'typeName': '人事科'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3711',
           'outResId': '1283',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件数据资源41',
           'description': '无条件数据描述41',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/76ccafd31b2b4c78a3a1dabd49529397',
           'serviceCategory': {
             'typeId': '25007',
             'typeName': '城乡建设'
           },
           'serviceTags': [
             {
               'typeId': '13',
               'typeName': '学位'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521143124000,
           'updateTime': 1521143136000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '008',
             'typeName': '警务督察处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3470',
           'outResId': '1109',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微服务前后加空格39',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appkg39',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/fc5cedb23c5f420a9c35edaf8e21745f',
           'serviceCategory': {
             'typeId': '25004',
             'typeName': '价格监管'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521090554000,
           'updateTime': 1521090554000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3469',
           'outResId': '1107',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证微应用必填项39',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'appyz39',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/3a318451d2ed403699d3814d5df30caf',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '11',
               'typeName': '高考'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521090483000,
           'updateTime': 1521090483000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3468',
           'outResId': '1105',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件微服务资源39',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx392',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/5ee32f55b1844081a6d4a8e815927c8c',
           'serviceCategory': {
             'typeId': '25002',
             'typeName': '食品药品安全'
           },
           'serviceTags': [
             {
               'typeId': '11',
               'typeName': '高考'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521090283000,
           'updateTime': 1521090283000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3467',
           'outResId': '1103',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件微服务资源39',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx391',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/b9235d214d2949f4a78030fc9ddc57ea',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '13',
               'typeName': '学位'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521090237000,
           'updateTime': 1521090237000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '018',
             'typeName': '政府监督处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3466',
           'outResId': '1093',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过微应用38',
           'description': '审核通过二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appbtzt381',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/429809b22949422cadba2ea88d221fab',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '5',
               'typeName': '教师'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521085857000,
           'updateTime': 1521085857000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3465',
           'outResId': '1089',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过微应用资源38',
           'description': '编辑审核通过比赛信息查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'appbj38',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/tmp/mscx-app/510400/USER_50043/4428ad2bb4124731880ec7b04c49646c',
           'serviceCategory': {
             'typeId': '25002',
             'typeName': '食品药品安全'
           },
           'serviceTags': [
             {
               'typeId': '10',
               'typeName': '器材配备'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521085658000,
           'updateTime': 1521085717000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '002',
             'typeName': '贷款申请'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3464',
           'outResId': '1085',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证微应用必填项38',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'appyz38',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/7dae27b0a91f49b3ac5ca73b37fbfbaf',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '7',
               'typeName': '中小学'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521085154000,
           'updateTime': 1521085154000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '006',
             'typeName': '人事科'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3463',
           'outResId': '1083',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件微服务资源38',
           'description': '基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx382',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/fa67a000d44248538f5037d711060b91',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '16',
               'typeName': '规范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521085001000,
           'updateTime': 1521085001000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3462',
           'outResId': '1081',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件微服务资源38',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx381',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/47cc7e4a0d284622b138d28fc0c7c4df',
           'serviceCategory': {
             'typeId': '25007',
             'typeName': '城乡建设'
           },
           'serviceTags': [
             {
               'typeId': '3',
               'typeName': '名单'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521084955000,
           'updateTime': 1521084955000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '018',
             'typeName': '政府监督处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3461',
           'outResId': '1071',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过微应用37',
           'description': '审核通过二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appbtzt371',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/e8eb438bf23a41ba91833fe2a7f99225',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '6',
               'typeName': '体育'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521060683000,
           'updateTime': 1521060683000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '019',
             'typeName': '政府宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3460',
           'outResId': '1067',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过微应用资源37',
           'description': '编辑审核通过二级注册建造师查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'appbj37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/tmp/mscx-app/510400/USER_50043/98a8d856ba84439db8a3cd1a26618c3b',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '1',
               'typeName': '标准'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521060479000,
           'updateTime': 1521060538000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3459',
           'outResId': '1061',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微服务前后加空格37',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appkg37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/3ce71438f35a4d179a0fc5d0575d6e6d',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521060204000,
           'updateTime': 1521060204000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3458',
           'outResId': '1059',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证微应用必填项37',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appyz37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/6f4d923b902e40d895f500ac33433619',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '6',
               'typeName': '体育'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521060135000,
           'updateTime': 1521060135000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '004',
             'typeName': '行政室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3457',
           'outResId': '1057',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微应用文testCHANG20个字符37',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符37',
           'availableCitys': '北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花',
           'resSign': 'appt123456789Engli37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/df31dd657d5340d1b34b11160c4cb856',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '8',
               'typeName': '证书'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521060071000,
           'updateTime': 1521060071000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3456',
           'outResId': '1055',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件微服务资源37',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx372',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/3abc6741405741f0a40f23450d484113',
           'serviceCategory': {
             'typeId': '25004',
             'typeName': '价格监管'
           },
           'serviceTags': [
             {
               'typeId': '6',
               'typeName': '体育'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521059969000,
           'updateTime': 1521059969000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '004',
             'typeName': '行政室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3455',
           'outResId': '1053',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件微服务资源37',
           'description': '基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx371',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/f9a541c3b9ff4cc596a66d2b69dc3a39',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '11',
               'typeName': '高考'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521059922000,
           'updateTime': 1521059922000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '008',
             'typeName': '警务督察处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3287',
           'outResId': '1159',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过API资源37',
           'description': '审核通过基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'apibyzt371',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/3d289464a1a04a44a17a3d19f5ba8ba9',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '6',
               'typeName': '体育'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521058697000,
           'updateTime': 1521058696000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3286',
           'outResId': '1155',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过API资源37',
           'description': '编辑审核通过外卖信息查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'apibj37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/037bdab8163d4ef899011a82981aa031',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '4',
               'typeName': '指标'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521058433000,
           'updateTime': 1521058506000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '004',
             'typeName': '行政室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3285',
           'outResId': '1149',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': 'API前后加空格37',
           'description': '基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'apikg37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/f17528898a2c443fb1b9871a3c3a2e73',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521058068000,
           'updateTime': 1521058068000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '004',
             'typeName': '行政室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3284',
           'outResId': '1147',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证API必填项37',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'apiyz37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/059847de904042e280fb701ba3234077',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '6',
               'typeName': '体育'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521057964000,
           'updateTime': 1521057964000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '021',
             'typeName': '发展信息处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3283',
           'outResId': '1145',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': 'API文testCHANG20个字符37',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符37',
           'availableCitys': '北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花',
           'resSign': 'apit123456789Engli37',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/3a38903bd5ed47ff94145c62eb8e31d5',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '1',
               'typeName': '标准'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521057886000,
           'updateTime': 1521057886000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3282',
           'outResId': '1143',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件API资源37',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'apigxlx372',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/e8cb90915c4b4e8988f832d8001620ac',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '5',
               'typeName': '教师'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521057717000,
           'updateTime': 1521057717000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3281',
           'outResId': '1141',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件API资源37',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'apigxlx371',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/ed87a5476c2c49018b1dd511c8643f2a',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521057625000,
           'updateTime': 1521057624000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '005',
             'typeName': '综合科'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3710',
           'outResId': '1273',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过数据37',
           'description': '审核通过数据描述37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/0408b42e0ea247c69967be950ea0676a',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '12',
               'typeName': '中职'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521056324000,
           'updateTime': 1521056334000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3709',
           'outResId': '1269',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过数据资源37',
           'description': '编辑审核通过数据描述37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/81bd734f10ae423bb076736857456350',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '4',
               'typeName': '指标'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521056008000,
           'updateTime': 1521056196000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3708',
           'outResId': '1263',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据前后加空格37',
           'description': '数据描述37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/b4d683edeee54075a291d16eae3f6927',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '        空格  '
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521055849000,
           'updateTime': 1521055861000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400003',
             'typeName': '攀枝花交通局'
           },
           'organization': {
             'typeId': '006',
             'typeName': '人事科'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3707',
           'outResId': '1261',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证数据必填项37',
           'description': '数据描述37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/fba02a1555e74eae9c8c23cf6e3abeb1',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '6',
               'typeName': '体育'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521055769000,
           'updateTime': 1521055781000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '019',
             'typeName': '政府宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3706',
           'outResId': '1259',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '数据中文testCHANG20个字符37',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/184bd9682a4649a9955667267878a2f0',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '4',
               'typeName': '指标'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521055718000,
           'updateTime': 1521055729000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '001',
             'typeName': '行政审批'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3705',
           'outResId': '1257',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件数据资源37',
           'description': '有条件数据描述37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/5973665914d142b39eaf7e6ab9463301',
           'serviceCategory': {
             'typeId': '25006',
             'typeName': '信用体系'
           },
           'serviceTags': [
             {
               'typeId': '8',
               'typeName': '证书'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521055610000,
           'updateTime': 1521055623000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3704',
           'outResId': '1255',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件数据资源37',
           'description': '无条件数据描述37',
           'availableCitys': null,
           'resSign': null,
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-data/510400/eee0ebf03bab49c9adf9ab8928452204',
           'serviceCategory': {
             'typeId': '25007',
             'typeName': '城乡建设'
           },
           'serviceTags': [
             {
               'typeId': '10',
               'typeName': '器材配备'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1521055564000,
           'updateTime': 1521055576000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '02',
             'typeName': '数据文件'
           },
           'department': {
             'typeId': '510400004',
             'typeName': '攀枝花公安局'
           },
           'organization': {
             'typeId': '008',
             'typeName': '警务督察处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '02',
             'typeName': '数据文件'
           }
         },
         {
           'id': '3454',
           'outResId': '1043',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过微应用36',
           'description': '审核通过基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'appbtzt361',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/0365c35a7eb04368a27270e1c929eb69',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '2',
               'typeName': '高校'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520975309000,
           'updateTime': 1520975309000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3453',
           'outResId': '1039',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             },
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过微应用资源36',
           'description': '编辑审核通过基金从业人员资格信息查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'appbj36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/tmp/mscx-app/510400/USER_50043/cf627c90a5314fccb91532ef1681e5f3',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520975106000,
           'updateTime': 1520975165000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3452',
           'outResId': '1033',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微服务前后加空格36',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'appkg36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/00621165fca345628899b18b996efa39',
           'serviceCategory': {
             'typeId': '25002',
             'typeName': '食品药品安全'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520974830000,
           'updateTime': 1520974830000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400007',
             'typeName': '人民政府办公厅'
           },
           'organization': {
             'typeId': '018',
             'typeName': '政府监督处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3451',
           'outResId': '1031',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证微应用必填项36',
           'description': '二级注册建造师查询',
           'availableCitys': '不限',
           'resSign': 'appyz36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/e24f4a383e7c484f9dd5d18f262a8f3b',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '7',
               'typeName': '中小学'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520974762000,
           'updateTime': 1520974762000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '020',
             'typeName': '发展宣传处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3450',
           'outResId': '1029',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '微应用文testCHANG20个字符36',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符36',
           'availableCitys': '北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花',
           'resSign': 'appt123456789Engli36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/556912c32c0347119fdd4b65dfdae871',
           'serviceCategory': {
             'typeId': '25001',
             'typeName': '社会保障'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520974701000,
           'updateTime': 1520974701000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3449',
           'outResId': '1027',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件微服务资源36',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx362',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/dbc185a605c346c6b9e61e20b14d23ee',
           'serviceCategory': {
             'typeId': '25005',
             'typeName': '能源安全'
           },
           'serviceTags': [
             {
               'typeId': '17',
               'typeName': '规程'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520974601000,
           'updateTime': 1520974601000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '011',
             'typeName': '政策法规'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3448',
           'outResId': '1025',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件微服务资源36',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'appgxlx361',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/mscx-app/510400/USER_50043/5565085911b34ca484f7ef97becd935c',
           'serviceCategory': {
             'typeId': '25004',
             'typeName': '价格监管'
           },
           'serviceTags': [
             {
               'typeId': '7',
               'typeName': '中小学'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520974553000,
           'updateTime': 1520974553000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '03',
             'typeName': '微应用'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '016',
             'typeName': '双拥工作处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '03',
             'typeName': '微应用'
           }
         },
         {
           'id': '3280',
           'outResId': '1131',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '审核通过API资源36',
           'description': '审核通过比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'apibyzt361',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/ca8641458ae74827a340e8e3e4e28616',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '15',
               'typeName': '示范'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520973346000,
           'updateTime': 1520973345000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3279',
           'outResId': '1127',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '编辑审核通过API资源36',
           'description': '编辑审核通过二级注册建造师查询',
           'availableCitys': '编辑审核通过不限',
           'resSign': 'apibj36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/716565b938e346f3ad7b245a2d60057d',
           'serviceCategory': {
             'typeId': '24999',
             'typeName': '公共服务'
           },
           'serviceTags': [
             {
               'typeId': '9',
               'typeName': '要求'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520973084000,
           'updateTime': 1520973157000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3278',
           'outResId': '1121',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': 'API前后加空格36',
           'description': '基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'apikg36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/58872700a6f44e48bdd28b44e67dd386',
           'serviceCategory': {
             'typeId': '25000',
             'typeName': '健康保障'
           },
           'serviceTags': [
             {
               'typeId': '922',
               'typeName': '时间'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520972720000,
           'updateTime': 1520972719000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400002',
             'typeName': '攀枝花教育局'
           },
           'organization': {
             'typeId': '003',
             'typeName': '办公科室'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3277',
           'outResId': '1119',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '验证API必填项36',
           'description': '外卖信息查询',
           'availableCitys': '不限',
           'resSign': 'apiyz36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/aa9fc3baa846478baff4c8d541f8aa76',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '5',
               'typeName': '教师'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520972616000,
           'updateTime': 1520972615000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400001',
             'typeName': '攀枝花公积金管理中心'
           },
           'organization': {
             'typeId': '002',
             'typeName': '贷款申请'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3276',
           'outResId': '1117',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': 'API文testCHANG20个字符36',
           'description': '格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符测试格式!@#$%^&teCE20个字符36',
           'availableCitys': '北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花北京西，西安北，广州，上海，福州，攀枝花',
           'resSign': 'apit123456789Engli36',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/d5a3a2b6b3d44c11a38a07ca5dc831d8',
           'serviceCategory': {
             'typeId': '25008',
             'typeName': '社区治理'
           },
           'serviceTags': [
             {
               'typeId': '8',
               'typeName': '证书'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520972538000,
           'updateTime': 1520972537000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400008',
             'typeName': '发展和改革委员会'
           },
           'organization': {
             'typeId': '021',
             'typeName': '发展信息处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3275',
           'outResId': '1115',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '有条件API资源36',
           'description': '比赛信息查询',
           'availableCitys': '不限',
           'resSign': 'apigxlx362',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/20da67c776f542e49bd6c88584263af5',
           'serviceCategory': {
             'typeId': '25006',
             'typeName': '信用体系'
           },
           'serviceTags': [
             {
               'typeId': '8',
               'typeName': '证书'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520972369000,
           'updateTime': 1520972369000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': 'Y',
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400006',
             'typeName': '攀枝花民政局'
           },
           'organization': {
             'typeId': '015',
             'typeName': '民间组织管理处'
           },
           'shareType': {
             'typeId': '02',
             'typeName': '有条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         },
         {
           'id': '3274',
           'outResId': '1113',
           'targetCustomers': [
             {
               'typeId': '01',
               'typeName': '个人'
             },
             {
               'typeId': '02',
               'typeName': '企业'
             },
             {
               'typeId': '03',
               'typeName': '政府'
             }],
           'name': '无条件API资源36',
           'description': '基金从业人员资格信息查询',
           'availableCitys': '不限',
           'resSign': 'apigxlx361',
           'serviceProvider': null,
           'belongToCitys': [
             {
               'typeId': '510400',
               'typeName': '攀枝花'
             }],
           'icon': 'http://dcw-shenzhoushuyun.oss-cn-shanghai.aliyuncs.com/share-api/510400/db53b4d0754d4535b29558f59e1411ca',
           'serviceCategory': {
             'typeId': '25003',
             'typeName': '安全生产'
           },
           'serviceTags': [
             {
               'typeId': '3',
               'typeName': '名单'
             }],
           'lastUpdatePlatform': '510400',
           'createPlatform': '510400',
           'createTime': 1520972277000,
           'updateTime': 1520972277000,
           'updateBy': '共享平台节点管理员',
           'createBy': '共享平台节点管理员',
           'inputItem': '',
           'outItem': '',
           'matchLevel': null,
           'renewalFrequency': null,
           'existHome': null,
           'areaCode': '510400',
           'synchroDate': '2019-03-31',
           'status': {
             'typeId': '0',
             'typeName': '审核通过'
           },
           'typeAlias': {
             'typeId': '01',
             'typeName': 'API'
           },
           'department': {
             'typeId': '510400005',
             'typeName': '攀枝花国土资源局'
           },
           'organization': {
             'typeId': '012',
             'typeName': '综合规划处'
           },
           'shareType': {
             'typeId': '01',
             'typeName': '无条件共享'
           },
           'resourceType': {
             'typeId': '01',
             'typeName': 'API'
           }
         }]

    }
  },
  watch: {
    'showType': {
      handler: function(val, oldval) {
        this.currentId = ''
        this.refresh()
      }
    },
    'currentId': {
      handler: function(val, oldval) {
        this.refresh()
      }
    }
  },
  mounted() {
    this.refresh()
  },
  methods: {
    refresh() {
      const _this = this
      _this.init()
      if (_this.showType === 'first') {
        // API.Topo.list({'serviceProvider': _this.currentId}).then((res) => {
        // _this.allData = res
        _this.allData = _this.viewData
        _this.current = _this.initResShowBtn(_this.allData)
        $('#' + _this.topoSvgId).empty()
        createResourceAllView(_this.topoSvgId, _this.createViewTree(), _this.scale)
        // })
      } else if (_this.showType === 'second') {
        // API.Topo.list({'categoryType': _this.currentId}).then((res) => {
        _this.allData = _this.viewData
        _this.current = _this.initResShowBtn(_this.allData)
        $('#' + _this.topoSvgId).empty()
        createResourceAllView(_this.topoSvgId, _this.createViewTree(), _this.scale)
        // })
      }
    },
    init() {
      this.btn_allres = '/static/images/btn_allres_light.png'
      this.btn_partres = '/static/images/btn_partres_light.png'
      this.zoomIn = zoomIn
      this.zoomOut = zoomOut
      this.current = []
      this.scale = 1
      this.initTranslate = ''
      this.maxZoom = 20
      this.minZoom = 0.4
      this.centerX = -1
      this.centerY = -1
      this.svgHeight = -1
      this.svgWidht = -1
    },
    allResBtnOnClick() {
      const _this = this
      var allResBtn = $('#' + _this.allResBtnId)
      var partResBtn = $('#' + _this.partResBtnId)
      if (allResBtn.attr('src').indexOf('btn_allres_light') > 0) {
        allResBtn.attr('src', '/static/images/btn_allres_unlight.png')
        partResBtn.attr('src', '/static/images/btn_partres_light.png')
        $('#' + _this.topoSvgId).empty()
        _this.current = _this.allData
        createResourceAllView(_this.topoSvgId, _this.createViewTree(), _this.scale)
      }
    },
    partResBtnOnClick() {
      const _this = this
      var subData = []
      var allResBtn = $('#' + _this.allResBtnId)
      var partResBtn = $('#' + _this.partResBtnId)
      if (partResBtn.attr('src').indexOf('btn_partres_light') > 0) {
        allResBtn.attr('src', '/static/images/btn_allres_light.png')
        partResBtn.attr('src', '/static/images/btn_partres_unlight.png')
        var count = 0
        _this.allData.forEach(function(value) {
          if (count >= _this.getResMaxSize()) {
            return
          }
          count++
          subData.push(value)
        })
        _this.current = subData
        $('#' + _this.topoSvgId).empty()
        createResourceAllView(_this.topoSvgId, _this.createViewTree(), _this.scale)
      }
    },
    zoomInSvg() {
      if (this.scale >= 10) {
        return
      }
      var split = isIE() ? ' ' : ','
      var g = $('#' + this.topoSvgId).find('g').eq(0)
      if (undefined === this.initTranslate) {
        this.initTranslate = g.attr('transform')

        this.centerX = parseFloat(this.initTranslate.replace('translate', '').replace('(', '').replace(')', '').split(split)[0])
        this.centerY = parseFloat(this.initTranslate.replace('translate', '').replace('(', '').replace(')', '').split(split)[1])
        this.svgWidht = $('#' + this.topoSvgId).width()
        this.svgHeight = $('#' + this.topoSvgId).height()
      }

      this.scale += 0.1
      var transform = 'translate(' + this.centerX * this.scale + split + this.centerY * this.scale + ')'
      g.attr('transform', transform)
      $('#' + this.topoSvgId).attr('width', this.svgWidht * this.scale)
      $('#' + this.topoSvgId).attr('height', this.svgHeight * this.scale)
      $('#' + this.topoSvgId).empty()
      createResourceAllView(this.topoSvgId, this.createViewTree(), this.scale)
    },
    zoomOutSvg() {
      if (this.scale <= 0.8) {
        return
      }
      var split = isIE() ? ' ' : ','
      var g = $('#' + this.topoSvgId).find('g').eq(0)
      if (undefined === this.initTranslate) {
        this.initTranslate = g.attr('transform')
        this.centerX = parseFloat(this.initTranslate.replace('translate', '').replace('(', '').replace(')', '').split(split)[0])
        this.centerY = parseFloat(this.initTranslate.replace('translate', '').replace('(', '').replace(')', '').split(split)[1])
        this.svgWidht = $('#' + this.topoSvgId).width()
        this.svgHeight = $('#' + this.topoSvgId).height()
      }
      this.scale -= 0.1
      var transform = 'translate(' + this.centerX * this.scale + split + this.centerY * this.scale + ')'
      g.attr('transform', transform)
      $('#' + this.topoSvgId).attr('width', this.svgWidht * this.scale)
      $('#' + this.topoSvgId).attr('height', this.svgHeight * this.scale)
      $('#' + this.topoSvgId).empty()
      createResourceAllView(this.topoSvgId, this.createViewTree(), this.scale)
    },
    createViewTree() {
      if (this.showType === 'first') {
        return createDepartViewTree(this.current)
      } else if (this.showType === 'second') {
        return createCategoryViewTree(this.current)
      }
    },
    getResMaxSize() {
      return 100
    },
    initResShowBtn(data) {
      const _this = this
      var subData = []
      var allResBtn = $('#' + _this.allResBtnId)
      var partResBtn = $('#' + _this.partResBtnId)
      if (data.length < _this.getResMaxSize()) {
        allResBtn.attr('src', '/static/images/btn_allres_gray.png')
        partResBtn.attr('src', '/static/images/btn_partres_gray.png')
        subData = data
      } else {
        allResBtn.attr('src', '/static/images/btn_allres_light.png')
        partResBtn.attr('src', '/static/images/btn_partres_unlight.png')
        var count = 0
        data.forEach(function(value) {
          if (count >= _this.getResMaxSize()) {
            return
          }
          count++
          subData.push(value)
        })
      }
      return subData
    }
  }
}
</script>
<style>
  .node circle {
    stroke: steelblue;
    stroke-width: 1px;
    cursor:pointer;
  }
  .node text {
    fill: #6A788D;
    cursor: pointer;
  }
  .node text:hover {
    fill: #000000;
  }
  .node {
    font-size: 9px;
    display:block;
  }

  .hidenode {
    font: 12px sans-serif;
    display:none;
  }

  .link {
    fill: none;
    stroke: #ccc;
    stroke-width: 1.5px;
  }
</style>
