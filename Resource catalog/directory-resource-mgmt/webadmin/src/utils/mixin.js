// 滚动条记录， 适用于 keep-alive 组件
import {
  getIdCardInfo,
  validIdcard,
  isNumberOverall0,
  isHasChina,
  isHasCharacter
} from '@/utils/validate'
import {
  uploadPath, getFetchUrl
} from '@/api/storage'
import {
  getToken
} from '@/utils/auth'

import moment from 'moment'
export default {
  data() {
    var vm = this
    return {
      btnLoading: false,
      treeLoading: false,
      tableLoading: false,
      searchQuery: {
        keyword: '',
        current: 1,
        size: 5,
        busiNm: '',
        busiNmOrBusiNo: '',
        busiNoAndBusiNm: '',
        appNoOrAppNm: ''
      },
      searchTotal: 0,
      searchRelatedResults: [],
      uploadPath,
      validateAge: false,
      limitSatrtDate: '',
      pickerOptions: {
        disabledDate(time) {
          if (vm.limitSatrtDate) {
            return time.getTime() <= new Date(vm.createPropertyBill.startDate).getTime() - 24 * 60 * 60 * 1000
          }
        }
      },
      pickerOptions2: {
        disabledDate(time) {
          if (vm.createPropertyBill.startDate) {
            return time.getTime() <= new Date(vm.createPropertyBill.startDate).getTime() - 24 * 60 * 60 * 1000
          }
        }
      },
      treeExpansion: false,
      downloadPath: location.protocol + '//' + window.location.host + '/admin' + '/storage/fetch'
    }
  },
  computed: {
    headers() {
      return {
        'X-Resourcecatalog-Admin-Token': getToken()
      }
    }
  },
  mounted() {

  },
  methods: {
    downloadFileIframe(url, cb) {
      // getFetchUrl(url).then(res => {
        var _this = this
        var iframe = document.createElement('iframe')
        iframe.src = url
        iframe.id = 'iframedownload'
        document.body.appendChild(iframe)
        iframe.style.display = 'none'
        iframe.onload = function() {
          console.log('start downloading...')
          document.body.removeAttribute(iframe)
        }
        var timer = setInterval(function() {
          iframe = document.getElementById('iframedownload')
          var iframeDoc = iframe.contentDocument || iframe.contentWindow.document
          console.log(iframeDoc)
          // Check if loading is complete
          if (iframeDoc.readyState == 'complete' || iframeDoc.readyState == 'interactive') {
            clearInterval(timer)
            if (cb) { cb() }
          }
        }, 10)
      // }).catch(err => {
      //   this.$message.error('下载文件不存在')
      // })
    },
    uploadFail() {
      this.btnLoading = true
    },
    uploadProgress() {
      this.btnLoading = true
    },
    delFile() {
      this.fileList = []
    },
    handleChange(file, fileList) {
      this.fileList = fileList
    },
    beforeUpload(file) {
      const isLt40M = file.size / 1024 / 1024 <= 40
      if (!isLt40M) {
        this.$message.error('上传文件大小不能超过 40MB!')
      }
      return isLt40M
    },
    collapseTree() {
      const _this = this
      // el-col-lg-6 el-col-xl-4
      setTimeout(() => {
        // 展开
        console.log(this.$refs.treeDomParents, this.$refs.contentDomParents)
        _this.$refs.treeDomParents.$el.style.transition = '0.3s ease-in'
        _this.$refs.contentDomParents.$el.style.transition = '0.3s ease-out'
        _this.treeExpansion ? _shrink() : _expansion()
      })

      function _expansion() {
        _this.treeExpansion = true
        _this.$refs.treeDomParents.$el.classList.remove('el-col-lg-5', 'el-col-xl-4')
        _this.$refs.treeDomParents.$el.classList.add('el-col-lg-8', 'el-col-xl-8')
        _this.$refs.contentDomParents.$el.classList.remove('el-col-lg-19', 'el-col-xl-20')
        _this.$refs.contentDomParents.$el.classList.add('el-col-lg-16', 'el-col-xl-16')
      }

      function _shrink() {
        _this.treeExpansion = false
        _this.$refs.treeDomParents.$el.classList.add('el-col-lg-5', 'el-col-xl-4')
        _this.$refs.treeDomParents.$el.classList.remove('el-col-lg-8', 'el-col-xl-8')
        setTimeout(() => {
          _this.$refs.contentDomParents.$el.classList.add('el-col-lg-19', 'el-col-xl-20')
          _this.$refs.contentDomParents.$el.classList.remove('el-col-lg-16', 'el-col-lg-16')
        }, 290)
      }
    },
    closeAddDialog(arr, total, query, string) {
      this[arr] = []
      this[total] = 0
      this[query].current ? this[query].current = 1 : ''
      this[query].keyword ? this[query].keyword = '' : ''
      this[query].busiNm ? this[query].busiNm = '' : ''
      this[query].busiNmOrBusiNo ? this[query].busiNmOrBusiNo = '' : ''
      this[query].busiNoAndBusiNm ? this[query].busiNoAndBusiNm = '' : ''
      this[query].appNoOrAppNm ? this[query].appNoOrAppNm = '' : ''
      string ? this[query][string] = '' : ''
    },
    setTreeExpendChecked() {
      var _this = this
      var expandedkeysArr = []
      var _currentNodekey = ''
      if (this.treeData[0].children.length == 0 || !this.treeData[0].children) {
        this.currentNodekey = this.treeData[0].id
        this.expandedkeys.push(this.treeData[0].id)
      } else {
        _resolve(this.treeData[0])
      }

      function _resolve(e) {
        for (const k in e) {
          if (k == 'children') {
            if (e[k].length > 0) {
              expandedkeysArr.push(e.id)
              _resolve(e[k][0])
            } else {
              _currentNodekey = e.id
            }
          }
        }
        _this.expandedkeys = expandedkeysArr
        _this.currentNodekey = _currentNodekey
      }
    },
    getBusinessAddForm(form) {
      console.log(form)
      const currentNodePid = this.$refs.treeDom.getCurrentNode().pid
      const currentNodeId = this.$refs.treeDom.getCurrentNode().id || this.$refs.treeDom.getCurrentNode().busiNo
      const pData = this.$refs.treeDom.getNode(currentNodeId)
      console.log(pData)
      if (pData != null && pData.data.type != 'city') {
        this[form].name = pData.data.busiNm
        // this[form].id = pData.data.id
        if (!pData.data.deptId) {
          this[form].name = ''
          this[form].departmentName = pData.data.busiNm
        }
        if (pData.data.deptId) {
          // const departmentData = this.$refs.treeDom.getNode(pData.data.busiNo.substring(1, 3))
          const node = this.$refs.treeDom.getNode(this.$refs.treeDom.getCurrentNode().busiNo)
          let busiId = ''
          let busiNo = ''
          // 递归获取到部门级别获取部门id和code
          _resolve(node)

          function _resolve(e) {
            for (const k in e) {
              if (k == 'parent') {
                if (e[k] != null && e[k].level != 1) {
                  _resolve(e.parent)
                } else {
                  busiId = e.data.busiId
                  busiNo = e.data.busiNo
                }
              }
            }
          }
          console.log(busiId, busiNo)
          const departmentData = this.$refs.treeDom.getNode(busiNo)
          this[form].departmentName = departmentData.data.busiNm
          // this[form].deptId = departmentData.data.orgId
          // console.log()
        }
      } else {
        this[form].name = ''
      }
    },
    getPName1(form) {
      console.log(form)
      const currentNodePid = this.$refs.treeDom.getCurrentNode().pid
      const currentNodeId = this.$refs.treeDom.getCurrentNode().orgId
      const pData = this.$refs.treeDom.getNode(currentNodeId)
      if (pData != null && pData.data.type != 'city') {
        this[form].name = pData.data.label
        this[form].id = pData.data.id

        this[form].departmentName = pData.data.orgNm
      } else {
        this[form].name = ''
      }
    },
    getPName(form) {
      console.log(form)
      const currentNodePid = this.$refs.treeDom.getCurrentNode().pid
      const currentNodeId = this.$refs.treeDom.getCurrentNode().orgId
      const pData = this.$refs.treeDom.getNode(currentNodeId)
      if (pData != null && pData.data.type != 'city') {
        this[form].name = pData.data.label
        this[form].id = pData.data.id
        if (!pData.data.parOrgId) {
          this[form].name = ''
          this[form].departmentName = pData.data.orgNm
        }
        if (pData.data.parOrgId) {
          const departmentData = this.$refs.treeDom.getNode(pData.data.parOrgId)
          this[form].departmentName = departmentData.data.orgNm
        }
      } else {
        this[form].name = ''
      }
    },
    getRowClass({
      row,
      column,
      rowIndex,
      columnIndex
    }) {
      if (rowIndex === 0) {
        return 'background: #F6F6F6;font-weight:bold;color:rgba(36,36,36,1);'
      } else {
        return ''
      }
    },
    resetForm(formDom, formData) {
      this.$refs[formDom].resetFields()
      this[formData] = {}
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    closedDialog(attr, formName) {
      this.$refs[formName].resetFields()
      this[attr] = {}
      this.$refs.selectTreeDom ? this.$refs.selectTreeDom.clearAll() : ''
    },
    isNumber(rule, value, callback) {
      if (value) {
        if (!isNumberOverall0(value)) {
          return callback(new Error(rule.message || '必须为整数'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    },
    hasChina(rule, value, callback) {
      if (value) {
        if (isHasChina(value)) {
          return callback(new Error(rule.message || '不能包含汉字'))
        } else {
          callback()
        }
      } else {
        callback()
      }
    },
    hasCharacter(rule, value, callback) {
      if (value) {
        if (isHasCharacter(value)) {
          callback()
        } else {
          return callback(new Error(rule.message || '不能包含特殊字符'))
        }
      } else {
        callback()
      }
    },
    checkIdcard(rule, value, callback) {
      if (value) {
        if (!validIdcard(value)) {
          return callback(new Error('身份证格式不正确'))
        } else {
          this.formData.tableData.forEach((item, index) => {
            if (item.showEdit == true) {
              this.formData.tableData[index].age = getIdCardInfo(value).age
              this.formData.tableData[index].sex = getIdCardInfo(value).gender == '男' ? '0' : '1'
            }
          })

          callback()
        }
      } else {
        callback()
      }
    },

    checkIdCardAge(rule, value, callback) {
      if (value) {
        this.formData.tableData.forEach((item, index) => {
          if (item.showEdit == true) {
            if (this.formData.tableData[index].idCard && this.formData.tableData[index].age) {
              let currentAge = ''
              if (rule._type == 'idCard') {
                currentAge = getIdCardInfo(value).age
              } else {
                currentAge = getIdCardInfo(this.formData.tableData[index].idCard).age
              }
              if ((this.formData.tableData[index].age != currentAge)) {
                this.validateAge = true
                return callback(new Error('身份证与年龄不符'))
              } else {
                /* console.log(this.$refs[`tableData${index}idCardInput`])
                this.$refs[`tableData${index}ageInput`].focus()
                this.$refs[`tableData${index}idCardInput`].focus()
                this.$refs[`tableData${index}ageInput`].blur()
                this.$refs[`tableData${index}idCardInput`].blur()*/
                this.validateAge = false
                callback()
              }
            } else {
              this.validateAge = false
              callback()
            }
          }
        })
      } else {
        this.validateAge = false
        callback()
      }
    }
  },
  activated() {

  }
}
