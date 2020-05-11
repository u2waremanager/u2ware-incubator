<template>
    <div id="cesiumContainer"></div>
</template>

<script>
import "cesium/Build/Cesium/Widgets/widgets.css";
import * as Cesium from 'cesium';
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA';
// import { Cesium3DTileset, createWorldTerrain, IonResource, Viewer } from 'cesium';

export default {
  name: 'CesiumViewer01',
  mounted: () => {
      var viewer = new Cesium.Viewer('cesiumContainer', {
          terrainProvider: Cesium.createWorldTerrain()
      });

      var tileset = new Cesium.Cesium3DTileset({
            url: Cesium.IonResource.fromAssetId(73494), //대전 대덕대교 
            // url: Cesium.IonResource.fromAssetId(19005), ////대구 운동장 
      });

      viewer.scene.primitives.add(tileset);
      viewer.zoomTo(tileset);

      console.log(Cesium.IonResource);
      console.log(Cesium.Ion);
  }
}

</script>

<style scoped>

div#cesiumContainer {
  width:640px;
  height:480px;
}

</style>
