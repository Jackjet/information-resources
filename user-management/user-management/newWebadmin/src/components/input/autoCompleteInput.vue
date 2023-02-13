<template>
  <el-autocomplete
    class="inline-input"
    v-model="inputValue"
    :size="size"
    clearable
    :trigger-on-focus="onFocus"
    :fetch-suggestions="querySearch"
    :placeholder="thePlaceholder"
    @select="handleSelect"
  ></el-autocomplete>
</template>
<script>
import request from '@/utils/request'
import axios from 'axios'
export default {
  name: '',
  props: {
    // 是否获取焦点就开始查询，同时也是判断查询接口还是本地存储， false 为查询接口，true为查询本地存储
    onFocus: {
      type: Boolean,
      default: true
    },
    // 组件大小，依托elemetUI的大小参数
    size: {
      type: String,
      default: 'medium'
    },
    // 输入框为空时的输入建议
    thePlaceholder: {
      type: String,
      default: '请输入参数'
    },
    // 访问接口url,若为本地查询，则输入输入框唯一id
    theAddress: {
      type: String,
      default: ''
    },
    // 查询关键字
    theKey: {
      type: String,
      default: 'value'
    }
  },
  data() {
    return {
      restaurants: [],
      inputValue: ''
    }
  },
  methods: {
    fetchList(that, value) {
      let data = {}
      data[that.theKey] = value
      return request({
        url: that.theAddress,
        method: 'get',
        params: data,
        cancelToken: axios.CancelToken(function executor(c) {
          // executor 函数接收一个 cancel 函数作为参数
          that.cancel = c;
        })
      })
    },
    querySearch(queryString, cb) {
      if (this.onFocus) {
        let local = []
        if (localStorage.getItem(this.theAddress)) {
          local = JSON.parse(localStorage.getItem(this.theAddress))
        }
        // 调用 callback 返回建议列表的数据
        cb(this.shanxuan(local, queryString))
      } else {
        if (this.cancel) {
          this.cancel()
        }
        this.fetchList(this, queryString).then(response => {
          let res = response.data.data.content
          // 调用 callback 返回建议列表的数据
          cb(this.shanxuan(res, queryString))
        }).catch(err => {
          console.log(err)
        })
      }
    },
    shanxuan (data, str) {
      let results = str ? data.filter(this.createFilter(str)) : data
      if (!this.onFocus) {
        results.map(item => {
          item.value = item[this.theKey]
        })
      }
      return results
    },
    createFilter(queryString) {
      let useKey = this.onFocus ? 'value' : this.theKey
      return (restaurant) => {
        return (restaurant[useKey].toLowerCase().indexOf(queryString.toLowerCase()) !== -1)
      }
    },
    returnValue () {
      return this.inputValue
    },
    setValue () {
      if (this.onFocus) {
        let local = localStorage.getItem(this.theAddress)
        if (local) {
          let localArr = JSON.parse(local)
          localArr.push({'value': this.inputValue})
          localStorage.setItem(this.theAddress, JSON.stringify(localArr))
        } else {
          localStorage.setItem(this.theAddress, JSON.stringify([{'value': this.inputValue}]))
        }
      }
    },
    handleSelect(item) {
      console.log(item.value)
    }
  }
}
</script>
<style lang="scss" scoped>

</style>