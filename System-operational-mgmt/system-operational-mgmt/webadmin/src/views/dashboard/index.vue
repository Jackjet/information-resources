<template>
  <div class="dashboard-container">

    <!-- <img src="../../assets/image/index.png">
		<div class="box flex" >
				<div class="item" v-for="(item, index) in listMain" :key="index" :style="{background: 'url(' + item.img + ')', backgroundSize: '100% 100%'}" >
			</div>	
		</div> -->
    <div class="text-box">
      <h1>WELCOM</h1>
      <span>欢迎进入运行管理平台</span>
      <img src="@/assets/image/home_icon.png" alt="" />
    </div>
  </div>
</template>
<script>
import { mainMenuTree } from '@/api//user'
export default {
  data() {
    return {
      List: [
        {
          img: require('@/assets/image/zhishi.png')
        },
        {
          img: require('@/assets/image/data.png')
        },
        {
          img: require('@/assets/image/yunwei.png')
        },
        {
          img: require('@/assets/image/base.png')
        },
        {
          img: require('@/assets/image/userxw.png')
        },
        {
          img: require('@/assets/image/mokuai.png')
        },
        {
          img: require('@/assets/image/shujk.png')
        },
        {
          img: require('@/assets/image/yuanchen.png')
        },
        {
          img: require('@/assets/image/zidonghua.png')
        }
      ],
      listMain: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      const data = {}
      const that = this
      try {
        const res = await mainMenuTree(data)
        if (res.data.code === 1) {
          if (res.data.data.length > 0) {
            res.data.data.map(function (v, k) {
              let id = parseInt(v.entity.id) - 2
              that.listMain.push(that.List[id])
            })
          }
          // this.List.push({ img: require('@/assets/image/zhishi.png')})
        }
      } catch (even) {
        this.$message.error(even.msg)
      }
    },
    add: function () {
      this.$refs.add.initial('system')
    },
  }
}
</script>
<style lang="scss" scoped>
.dashboard {
  &-container {
    // margin: 2px;
    // padding: 0 10px;
    // background-color: rgb(240, 242, 245);
    // background-image: url(../../assets/image/index.png);
    height: 100vh;
    // position: relative;
  }
  //   &-text {
  //     font-size: 30px;
  //     line-height: 46px;
  //   }
}
// img {
//   width: 100%;
//   height: 40%;
//   max-height: 100%;
//   max-width: 100%;
// }
// .flex {
//   display: flex;
//   display: -webkit-flex;
//   flex-direction: row;
//   flex-wrap: wrap;
//   align-content: flex-start;
// }

.dashboard-container {
  background: #eff0f5;
  & .text-box {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    color: #666666;
    & h1 {
      line-height: 0;
      font-size: 3em;
      color: #c4c7cc;
    }
    & span {
      color: #c4c7cc;
    }
    & img {
      width: 150px;
      height: 150px;
      margin-top: 50px;
    }
  }
  //   .box {
  //     position: absolute;
  //     z-index: 5;
  //     background: #fff;
  //     width: 80%;
  //     height: 64%;
  //     left: 50%;
  //     top: 50%;
  //     transform: translate(-50%, -50%); /*自己的50% */
  //     border-radius: 5px;
  //     padding: 20px;
  //     .item {
  //       box-sizing: border-box;
  //       flex: 0 0 33%;
  //       // width: 30%;
  //       height: 30%;
  //       // background-image: url(../../assets/image/zhishi.png);
  //     }
  //   }
}
</style>
