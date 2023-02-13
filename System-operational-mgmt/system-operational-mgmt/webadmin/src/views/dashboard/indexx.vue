<template>
  <div class="dashboard-container">
    <div class="item">
      <h4>选项卡</h4>
      <data-tabs :option="DataOption"></data-tabs>
    </div>
    <div class="item">
      <h4>数据</h4>
      <data-display :option="option"></data-display>
    </div>
    <div class="item">
      <h4>卡片</h4>
      <data-cards :option="CardOption"></data-cards>
    </div>
  </div>
</template>
<script>
import DataTabs from '@/components/DataTab/DataTabs'
import DataDisplay from '@/components/DataDisplay/DataDisplay'
import DataCards from '@/components/DataCards/DataCards'
export default {
  name: 'Dashboard',
  components: {
    DataTabs,
    DataDisplay,
    DataCards
  },

  mounted() {
    setTimeout(() => {
      this.init()
    }, 4000)
  },
  data() {
    return {
      CardOption: {
        span: 6,
        data: [
          {
            src: 'https://up.enterdesk.com/edpic/9e/44/23/9e442370cdcf8635a81347432db1acc0.jpg',
            name: '',
            progress: [{
              name: 'Java',
              porss: 100
            }, {
              name: 'JavaScript',
              porss: 80
            }, {
              name: 'Nodejs',
              porss: 50
            }]
          },
          {
            src: 'https://up.enterdesk.com/edpic/44/a5/d5/44a5d51358487e50d5966f49e93a0bd3.jpg',
            name: '',
            progress: [{
              name: 'Java',
              porss: 100
            }, {
              name: 'JavaScript',
              porss: 80
            }, {
              name: 'Nodejs',
              porss: 50
            }]
          },
          {
            src: 'https://up.enterdesk.com/edpic/a7/61/58/a76158e3d1cbb85b18e84e2bab62955f.jpg',
            name: '',
            progress: [{
              name: 'Java',
              porss: 100
            }, {
              name: 'JavaScript',
              porss: 80
            }, {
              name: 'Nodejs',
              porss: 50
            }]
          },
          {
            src: 'https://up.enterdesk.com/edpic/ee/a0/86/eea0864600aa2167a4326d5152493341.jpg',
            name: '',
            progress: [{
              name: 'Java',
              porss: 100
            }, {
              name: 'JavaScript',
              porss: 80
            }, {
              name: 'Nodejs',
              porss: 50
            }]
          }
        ]
      },
      option: {
        span: 6,
        color: '',
        data: [
          {
            count: 10000,
            title: '年活跃数'
          },
          {
            count: 10000,
            title: '月活跃数'
          },
          {
            count: 10000,
            title: '日活跃数'
          },
          {
            count: 20000,
            title: '时活跃数 '
          }
        ]
      },
      DataOption: {
        span: 6,
        data: [
          {
            title: '今年统计',
            subtitle: '今年',
            count: 5000,
            allcount: 10000,
            text: '当前今年统计数',
            color: 'linear-gradient(to right, #00b4db, #0083b0)',
            key: '年'
          },
          {
            title: '本月统计',
            subtitle: '实时',
            count: 3112,
            allcount: 10222,
            text: '当前今年统计数',
            color: 'linear-gradient(to right, #06beb6, #48b1bf)',
            key: '月'
          },
          {
            title: '本天统计',
            subtitle: '本天',
            count: 908,
            allcount: 10222,
            text: '当前今年统计数',
            color: 'linear-gradient(to right, #7474bf, #348ac7)',
            key: '天'
          },
          {
            title: '本时统计',
            subtitle: '实时',
            count: 908,
            allcount: 10222,
            text: '当前今年统计数',
            color: 'linear-gradient(to right, #8e2de2, #4a00e0)',
            key: '时'
          }
        ]
      }
    }
  },
  methods: {
    init() {
      // this.CardOption.data[3].src = 'https://up.enterdesk.com/edpic/ee/a0/86/eea0864600aa2167a4326d515249331.jpg'
    }
  }
}
</script>

<style lang="scss" scoped>
@import '../../components/styles/data-tabs.scss';
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

</style>
