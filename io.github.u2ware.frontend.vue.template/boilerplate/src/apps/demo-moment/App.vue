<template>
  <div id="app">
    <img alt="Vue logo" src="../../assets/logo.png">
    <p>{{ new Date() | moment("dddd, MMMM Do YYYY") }}</p>

    <p>
      <v-row>
        <v-col cols="4" >
        </v-col>
        <v-col cols="2" >
          <v-text-field
                label="Outlined"
                placeholder="Placeholder"
                v-model="format"
                outlined
            ></v-text-field>
        </v-col>
        <v-col cols="1" >
          <v-btn @click="demo_moment()">format</v-btn>
        </v-col>
        <v-col cols="2" >
          {{formattedDate}}
        </v-col>
      </v-row>
    </p>
  </div>


</template>

<script>

export default {
  name: 'demo-moment',
  data : ()=>({
    format : 'YYYY-MM-DD',
    formattedDate : ''
  }),
  methods:{
    demo_moment(){
      this.formattedDate = this.$moment(new Date()).format(this.format);
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
