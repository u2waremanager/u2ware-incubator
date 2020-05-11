<template>

  <div id="app">

    <v-row>
      <v-col cols="12" sm="3">
        <router-link to="/demo-gis"> Home </router-link> <br/>
        <p>&nbsp;</p><br/>

        <p>Map Provider</p>
        <router-link to="/demo-gis/map01"> Kakao </router-link> <br/>
        <p>&nbsp;</p><br/>


        <p>Openlayer (2D)</p>
        <router-link to="/demo-gis/openlayer01"> OSM</router-link> <br/>
        <router-link to="/demo-gis/openlayer02"> Bing</router-link> <br/>
        <p>&nbsp;</p><br/>

        <router-link to="/demo-gis/openlayer03"> XYZ[OSM]</router-link> <br/>
        <router-link to="/demo-gis/openlayer04"> XYZ[Google]</router-link> <br/>
        <router-link to="/demo-gis/openlayer05"> XYZ[VWorld]</router-link> <br/>
        <p>&nbsp;</p><br/>
        
        <router-link to="/demo-gis/openlayer06"> WMS[VWorld]</router-link> <br/>
        <router-link to="/demo-gis/openlayer07"> WMS[GeoServer - NY]</router-link> <br/>
        <router-link to="/demo-gis/openlayer08"> WMS[GeoServer - Smaple]</router-link> <br/>
<!-- 
        <router-link to="/demo-gis/openlayer09"> Absolute[Kakao]</router-link> <br/>
        <router-link to="/demo-gis/openlayer06"> WMS[GeoServer]</router-link> <br/>
-->


        <p>&nbsp;</p><br/>


        <p>Cesium (3D)</p>
        <router-link to="/demo-gis/cesium00"> Basic </router-link> <br/>
        <router-link to="/demo-gis/cesium01"> Ion[대덕]  </router-link> <br/>
        <router-link to="/demo-gis/cesium02"> Ion[대구] </router-link> <br/>
        <p>&nbsp;</p><br/>

        <p>Three.js (3D)</p>
        <router-link to="/demo-gis/three00"> FBX[대덕] </router-link> <br/>

        <p>&nbsp;</p><br/>


        <p>OL-Cesium (2D + 3D)</p>
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