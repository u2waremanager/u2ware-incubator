<template>
  <div id="app">
    <div>
      <button v-for="entry in languages" :key="entry.title" @click="changeLocale(entry.language)">
          <flag :iso="entry.flag" v-bind:squared=false /> {{entry.title}}
      </button>
    </div>
    <img alt="Vue logo" src="./assets/logo.png">
    <h1>{{ $t('message') }}</h1>
<!--     <HelloWorld msg="Welcome to Your Vue.js App"/>
 -->
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import i18n from './i18n'

export default {
  name: 'app',
  components: {
    HelloWorld
  },
  data() {
      return {
          languages: [
              { flag: 'us', language: 'en', title: 'English' },
              { flag: 'es', language: 'es', title: 'Español' }
          ]
      };
  },
  methods: {
      changeLocale(locale) {
          i18n.locale = locale;
          console.log(i18n);
          console.log(i18n.messages);
          console.log(i18n.messages[locale]);
          console.log(i18n.messages[locale].message);
      }
  }  
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
