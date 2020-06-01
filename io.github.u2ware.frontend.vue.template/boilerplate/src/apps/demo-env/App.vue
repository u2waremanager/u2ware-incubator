<template>
  <div id="app">
    <img alt="Vue logo" src="../../assets/logo.png">
    <h2>VUE_APP_HELLO: {{hello}}</h2>
    <h2>VUE_APP_A: {{a}}</h2>
    <h2>VUE_APP_B: {{b}}</h2>
    <h2>VUE_APP_C: {{c}}</h2>
    <h2>VUE_APP_D: {{d}}</h2>
  </div>
</template>

<script>

export default {
  name: 'demo-env',
  data: () => ({
    hello : process.env.VUE_APP_HELLO,
    a : process.env.VUE_APP_A,
    b : process.env.VUE_APP_B,
    c : process.env.VUE_APP_C,
    d : process.env.VUE_APP_D
  }),
  components: {
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
