<template>
  <div id="app">
    <img alt="Vue logo" src="../../assets/logo.png">
    <p>
      <v-btn v-for="entry in $i18n.languages" :key="entry.title" @click="$i18n.changeLocale(entry.language)">
          <!-- <flag :iso="entry.flag" v-bind:squared=false /> -->
          {{entry.title}}
      </v-btn>
    </p>
    <h1>{{ $t('message') }}</h1>
  </div>


</template>

<script>

export default {
  name: 'demo-i18n',
  data : ()=>({

  }),
  methods:{

  
  }

}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
