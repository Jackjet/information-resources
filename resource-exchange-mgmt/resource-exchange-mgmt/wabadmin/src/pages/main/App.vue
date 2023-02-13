<template>
  <div id="app">
    <transition name="fade">
      <router-view/>
    </transition>
  </div>
</template>

<script>
import '@/assets/css/theme/green/index.css'
import '@/assets/css/theme/purple/index.css'
import '@/assets/css/theme/yellow/index.css'
import '@/assets/css/theme/default/index.css'
import '@/assets/css/theme/red/index.css'
import '@/assets/css/theme/black/index.css'

export default {
  name: 'App'
}
</script>

<style lang="scss">
  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }

  .fade-enter, .fade-leave-to {
    opacity: 0;
  }

  .bounce-enter-active {
    animation: bounce-in .5s;
  }

  .bounce-leave-active {
    animation: bounce-in .5s reverse;
  }

  @keyframes bounce-in {
    0% {
      transform: scale(0);
    }
    50% {
      transform: scale(1.5);
    }
    100% {
      transform: scale(1);
    }
  }

  #app {
    font-family: '微软雅黑';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
  }
</style>
