<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import adminDashboard from './sysAdmin'
import partDashboard from './partAdmin'

export default {
  name: 'Dashboard',
  components: { adminDashboard, partDashboard },
  data() {
    return {
      currentRole: 'partDashboard'
    }
  },
  computed: {
    ...mapGetters(['roles'])
  },
  created() {
    if (
      this.roles.includes('系统管理员') ||
      this.roles.includes('中心资源管理员')
    ) {
      this.currentRole = 'adminDashboard'
    }
  }
}
</script>
