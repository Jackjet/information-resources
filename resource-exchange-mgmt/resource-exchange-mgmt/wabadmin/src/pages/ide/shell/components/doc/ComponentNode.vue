<template>
  <div class="tree-node" style="height: 170px; overflow-y: scroll;">
    <template>
      <table id="table" border="1">
        <thead style="text-align: left">
        <tr>
          <th>名称</th>
          <th>版本</th>
          <th>文档</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="component in components">
          <td>
            <template>
              {{getComponentName(component.developerId,component.componentName)}}
            </template>
          </td>
          <td>{{component.componentVersion}}</td>
          <td>
            <div class="inile " @click="doc(component)"><i class="fa fa-file pd5"></i></div>
          </td>
        </tr>
        </tbody>
      </table>
    </template>
  </div>
</template>
<script>

  export default {
    name: 'ComponentNode',
    props: {},
    data () {
      return {
        components: [],
        component: {
          componentGroup: '',
          componentName: '',
          componentVersion: '',
          developerId: ''
        }
      }
    },
    computed: {
      inputValue: {
        get: function () {
          return this.value
        },
        set: function (val) {
          this.$emit('input', val)
        }
      }
    },
    mounted () {
      $rxbus.$on('projectComponentList', this.onComponentList)
    },

    methods: {
      getComponentName (developerId, componentName) {
        if ('net' === developerId) {
          developerId = 'dc'
        }
        return developerId + '_' + componentName
      },

      doc (row) {
        let txtUrl = this.Interface.apiDoc + '?componentName=' + row.componentName + '&developerId=' + row.developerId + '&version=' + row.componentVersion
        window.open(txtUrl, '_blank')
      },
      onComponentList () {
        this.components = this.$store.state.projectComponentList
      }
    },

    watch: {}
  }
</script>

<style>
  ::-webkit-scrollbar {
    display: none;
  }

  .pd5 {
    padding: 5px;
  }

  .inile {
    display: inline-block;
    cursor: pointer;
  }

  #table {
    border: none;
    width: 100%;
  }

  #table th {
    border: none;
    padding: 5px;
  }

  #table td {
    border: none;
    padding: 5px;
  }
</style>
