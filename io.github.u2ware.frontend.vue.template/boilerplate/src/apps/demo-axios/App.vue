<template>
  <div id="app">
    <img alt="Vue logo" src="../../assets/logo.png">
    <p>
      
      <v-btn @click="demo_axios()">Axios</v-btn>
    </p>
  </div>


</template>

<script>

export default {
  name: 'demo-axios',
  data : ()=>({

  }),
  methods:{

    demo_axios(){
      
      this.$axios.get("/").then(res => {

        console.log(res);
      });
    }
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
