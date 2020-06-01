<template>

  <div id="app">

    <v-row>
      <v-col cols="12" sm="3">
        <router-link to="/demo-gis"> Home </router-link> <br/>
        <p>&nbsp;</p><br/>

        <p>Map Provider</p>
        <router-link to="/demo-gis/map01"> Kakao </router-link> <br/>
        <p>&nbsp;</p><br/>

        <p>Cesium</p>
        <router-link to="/demo-gis/cesium00"> Basic </router-link> <br/>
        <p>&nbsp;</p><br/>


        <p>Openlayer</p>
        <router-link to="/demo-gis/openlayer00"> Openlayer</router-link> <br/>
        <p>&nbsp;</p><br/>
        <p>&nbsp;</p><br/>

        <p>OL-Cesium</p>
        <router-link to="/demo-gis/overlap00">OSM </router-link> <br/>
        <router-link to="/demo-gis/overlap01">OSM + WML[비행금지] + Ion[대덕]</router-link> <br/>
        <router-link to="/demo-gis/overlap02">OSM + KML</router-link> <br/>
        <p>&nbsp;</p><br/>



      </v-col>
      <v-col cols="12" sm="9">
          <router-view/>
      </v-col>

    </v-row>
  </div>
</template>

<script>

import '../../plugins/axios'

export default {

  updated(){
    console.log(this.router)
  }
}
</script>

<style>

</style>