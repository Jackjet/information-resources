<template>
  <el-autocomplete
    class="inline-input"
    v-model="inputValue"
    :size="size"
    clearable
    :trigger-on-focus="onFocus"
    :fetch-suggestions="querySearch"
    :placeholder="thePlaceholder"
  ></el-autocomplete>
</template>
<script>
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
    // 访问接口url,onFocus为false时使用
    theAddress: {
      type: String,
      default: ''
    },
    // 输入框唯一id,onFocus为false时, 用于本地存储查询
    theId: {
      type: String,
      default: ''
    },
    // 查询关键字
    theKey: {
      type: String,
      default: 'value'
    },
    // 返回接口数据路径
    theResponsepath: {
      type: Array,
      default: ['data', 'content']
    }
  },
  data() {
    return {
      restaurants: [],
      inputValue: ''
    }
  },
  methods: {
    async fetchList(that, value) {
      let data = {}
      data[that.theKey] = value
      let response = await this.request.autoInputGet(that, that.theAddress, data)
      return response
    },
    querySearch(queryString, cb) {
      if (this.onFocus) {
        let local = []
        if (this.theId !== '' && localStorage.getItem(this.theId)) {
          local = JSON.parse(localStorage.getItem(this.theId))
        }
        // 调用 callback 返回建议列表的数据
        cb(this.screen(local, queryString))
      } else {
        if (this.cancel) {
          this.cancel()
        }
        this.fetchList(this, queryString).then(response => {
          let res = response.data
          if (response.data.code === 1) {
            this.theResponsepath.forEach(item => {
              res = res[item]
            })
            // 调用 callback 返回建议列表的数据
            cb(this.screen(res, queryString))
          }
        }).catch(err => {
          console.log(err)
        })
      }
    },
    screen (data, str) {
      if (!this.onFocus) {
        // 参数必须是key为value的键值对
        data.map(item => {
          item.value = item[this.theKey]
        })
      }
      let results = str ? data.filter(this.createFilter(str)) : data
      if (results.length > 10) {
        results = results.slice(0,10)
      }
      return results
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) !== -1)
      }
    },
    // 返回参数
    returnValue () {
      return this.inputValue
    },
    // 设置本地存储
    setValue () {
      if (this.onFocus) {
        if (this.theId === '') {
          this.$message.warning('请设置输入框id')
          return false
        }
        let local = localStorage.getItem(this.theId)
        if (local) {
          let localArr = JSON.parse(local)
          let flag = true
          localArr.forEach(item => {
            if (item.value === this.inputValue) {
              flag = false
            }
          })
          if (flag) {
            localArr.push({'value': this.inputValue})
            localStorage.setItem(this.theId, JSON.stringify(localArr))
          }
        } else {
          localStorage.setItem(this.theId, JSON.stringify([{'value': this.inputValue}]))
        }
      }
    }
  }
}
</script>
