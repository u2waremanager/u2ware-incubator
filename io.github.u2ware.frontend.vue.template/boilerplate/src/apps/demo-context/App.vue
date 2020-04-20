<template>
  <div id="app">
    <img alt="Vue logo" src="../../assets/logo.png">
    <p>
      <v-btn v-for="entry in $context.code.types" :key="entry" >
          {{entry}}
      </v-btn>
    </p>
  </div>


</template>

<script>

export default {
  name: 'demo-context',
  props : {

  },
  data: () => ({

  }),
  methods : {

  },
  created: function() {
      console.log(this.$options.name, 'created', this.$context);
  },
  mounted: function() {
      console.log(this.$options.name, 'mounted', this.$context);
  },
  updated: function() {
      console.log(this.$options.name, 'updated', this.$context);
  },
  destroyed: function() {
      console.log(this.$options.name, 'destroyed', this.$context);
  },
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
