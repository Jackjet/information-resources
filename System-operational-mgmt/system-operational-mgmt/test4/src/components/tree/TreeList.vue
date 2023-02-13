<template>
  <div
    v-loading="Loading"
    class="page-tree"
    @contextmenu.prevent="handleTreeClick"
  >
    <el-tree
      ref="Treenode"
      class="tree-component disabled-select"
      :show-checkbox="checkBox"
      :node-key="nodeKey"
      :load="loadMore"
      :lazy="true"
      highlight-current
      :render-content="renderContent"
      :props="treeProps"
      @node-click="handleClick"
    />
  </div>
</template>

<script>
export default {
  name: "Tree",
  props: {
    baseData: {
      type: Array,
      default: () => {
        return [];
      }
    },
    lazyData: {
      type: Array,
      default: () => {
        return [
          {
            key: "id",
            label: "name",
            type: "",
            api: () => {},
            params: { key: "", value: "", type: "url" },
            sucFieldList: [], // 数据响应成功的字段列表
            leaf: true
          }
        ];
      }
    },
    // 设置树状的字段结构
    treeProps: {
      type: Object,
      default: () => {
        return {
          children: "children",
          label: "name",
          isLeaf: "leaf"
        };
      }
    },
    // 传入一个随机数，让树组件更新
    treeRefresh: {
      type: Number
    }
  },
  data() {
    return {
      Loading: false,
      node: null,
      // 每个level的节点信息
      nodeInfoList: {}
    };
  },
  watch: {
    treeRefresh(val) {
      if (this.lazy) {
        const level = 0;
        this.nodeInfoList[level].node.childNodes = [];
        this.loadMore(
          this.nodeInfoList[level].node,
          this.nodeInfoList[level].resolve
        );
      }
    }
  },
  mounted() {},
  methods: {
    // 自定义渲染内容
    renderContent(h, { node, data }) {
      let dom;
      // 懒加载图标设置
      if (true) {
        if (data.leaf) {
          dom = (
            <p class="custom-tree-node">
              <img src={require("@/assets/image/wp.png")}></img>
              <span style="margin-left:5px;" title={data.desc}>
                {node.label}
              </span>
            </p>
          );
        } else {
          dom = (
            <p class="custom-tree-node">
              <img
                src={
                  node.expanded
                    ? require("@/assets/image/wj.png")
                    : require("@/assets/image/wj.png")
                }
              ></img>
              <span style="margin-left:5px;" title={data.desc}>
                {node.label}
              </span>
            </p>
          );
        }
      } else {
        dom = (
          <p class="custom-tree-node" title={data.desc}>
            <span style="margin-left:5px;" title={data.desc}>
              {node.label}
            </span>
          </p>
        );
      }
      return dom;
    },

    handleClick(data, node, vm) {
      this.$emit("handleEvent", { data, node, vm });
    },

    // 在树状数据中找到某一条数据
    getSelectData(key, data, val) {
      for (const item of data) {
        if (item[key] === val) {
          return item;
        }
      }
    },
    // 懒加载数据
    loadMore(node, resolve) {
      let arr = [];
      let Data2 = [
        {
          id: 1100011,
          name: "丰运县",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "云台县",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "西城县",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "东城县",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "丰运县",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "云台县",
          pid: 2,
          status: 2,
          flag: 2
        }
      ];

      let Data1 = [
        {
          id: 1100011,
          name: "密云",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "云台",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "西城",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "东城",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "丰运",
          pid: 2,
          status: 2,
          flag: 2
        },
        {
          id: 1100011,
          name: "云台",
          pid: 2,
          status: 2,
          flag: 2
        }
      ];

      let Data = [
        {
          id: 110000,
          name: "北京市",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 120000,
          name: "天津市",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 130000,
          name: "河北省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 140000,
          name: "山西省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 150000,
          name: "内蒙古自治区",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 210000,
          name: "辽宁省",
          pid: 1,
          status: 1,
          flag: 1
        },
        {
          id: 220000,
          name: "吉林省",
          pid: 1,
          status: 1,
          flag: 1
        },
        {
          id: 230000,
          name: "黑龙江省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 310000,
          name: "上海市",
          pid: 1,
          status: 1,
          flag: 1
        },
        {
          id: 320000,
          name: "江苏省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 330000,
          name: "浙江省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 340000,
          name: "安徽省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 350000,
          name: "福建省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 360000,
          name: "江西省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 370000,
          name: "山东省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 410000,
          name: "河南省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 420000,
          name: "湖北省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 430000,
          name: "湖南省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 440000,
          name: "广东省",
          pid: 1,
          status: 0,
          flag: 1
        },
        {
          id: 450000,
          name: "广西壮族自治区",
          pid: 1,
          status: 0,
          flag: 1
        }
      ];

      // 加载loading
      if (node.level === 0) {
        this.Loading = true;
      }
      // 存下每个懒加载的数据
      this.$set(this.nodeInfoList, "node" + node.level, { node, resolve });
      // 懒加载延迟时间
      const timeStamp = 100
      const treeProps = this.treeProps
      const levelInfo = this.lazyData[node.level]
      const params = levelInfo.params
      let data;
      if (params.type === "url") {
        data =
          this.refreshLevel > 0
            ? node.data[levelInfo.key]
            : params.value || params.value === 0
            ? params.value
            : node.data[levelInfo.key];
        console.log(data)
      } else {
        console.log("没有传参数类型")
      }
      switch (node.level) {
        case 0:
          arr = Data
          break;
        case 1:
          arr = Data1
          break;
        case 2:
          arr = Data2
          break;
      }
      arr.forEach(item => {
        // 保证key的唯一
        item.key = levelInfo.type + item[levelInfo.key];
        item["level" + node.level + "data"] = node.data;
        item[treeProps.label] = item[levelInfo.label];
        item.type = levelInfo.type;
        item[treeProps.isLeaf] = levelInfo.leaf;
      });
      // 得到数据后把数据给到父级，方便父级用到
      this.$emit("update:baseData", [...this.baseData, ...arr]);
      // 延迟加载，保证加载动画
      setTimeout(() => {
        resolve(arr);
      }, timeStamp);
      // 加载loading
      if (node.level === 0) {
        this.Loading = false;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.page-tree {
}
</style>
