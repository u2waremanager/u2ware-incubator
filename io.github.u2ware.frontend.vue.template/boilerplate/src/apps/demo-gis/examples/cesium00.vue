<template>
    <div id="cesiumContainer"></div>
</template>

<script>
import "cesium/Build/Cesium/Widgets/widgets.css";
import * as Cesium from 'cesium';
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA';
// import { Cesium3DTileset, createWorldTerrain, IonResource, Viewer } from 'cesium';

export default {
  name: 'CesiumViewer00',
  mounted: () => {
      var viewer = new Cesium.Viewer('cesiumContainer'
      , { terrainProvider: Cesium.createWorldTerrain()}
      );

      var imageryLayer = viewer.imageryLayers.addImageryProvider(
        new Cesium.IonImageryProvider({ assetId: 4 })
      );

      viewer.zoomTo(imageryLayer).otherwise(function (error) {
        console.log(error);
      });

      console.log(viewer);
  }
}
// cesiumPath is path of Cesium.js', for example:
  // local Cesium Build package:
  // cesiumPath: /static/Cesium/Cesium.js
  // Personal online Cesium Build package：
  // cesiumPath: 'https://zouyaoji.top/vue-cesium/statics/Cesium/Cesium.js'
  // Personal online SuperMap Cesium Build package：
  // cesiumPath: 'https://zouyaoji.top/vue-cesium/statics/SuperMapCesium/Cesium.js'
  // Official Online Cesium Build package：
  // cesiumPath: 'https://unpkg.com/cesium/Build/Cesium/Cesium.js',
  // accessToken : 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA'

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

div#cesiumContainer {
  width:640px;
  height:480px;
  /* height: 100vh; */
  /* width: 100vw; */
}

</style>
