<template>
<div>
    <div id="cesiumContainer"></div>
    <v-btn-toggle v-model="toggle_zoom" >
        <v-btn x-small value="layer1"> layer1 </v-btn>
        <v-btn x-small value="tileset1"> tileset1 </v-btn>
        <v-btn x-small value="tileset2"> tileset2 </v-btn>
      </v-btn-toggle> 

</div>
</template>

<script>
import "cesium/Build/Cesium/Widgets/widgets.css";
import * as Cesium from 'cesium';
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA';
// import { Cesium3DTileset, createWorldTerrain, IonResource, Viewer } from 'cesium';

export default {
  name: 'CesiumViewer00',
  data : ()=>({

    viewer : undefined,

    layer1 : undefined,
    tileset1 : undefined,
    tileset2 : undefined,

    toggle_zoom : undefined,

  }),
  mounted(){
      this.viewer = new Cesium.Viewer('cesiumContainer', {
        terrainProvider: Cesium.createWorldTerrain()}
      );

      ////////////////////////////////
      this.layer1 = new Cesium.IonImageryProvider({ assetId: 4 });
      this.viewer.imageryLayers.addImageryProvider(this.layer1);

      ////////////////////////////////
      this.tileset1 = new Cesium.Cesium3DTileset({
            url: Cesium.IonResource.fromAssetId(73494), //대전 대덕대교 
      });
      this.viewer.scene.primitives.add(this.tileset1);

      ////////////////////////////////
      this.tileset2 = new Cesium.Cesium3DTileset({
            url: Cesium.IonResource.fromAssetId(19005), ////대구 운동장 
      });
      this.viewer.scene.primitives.add(this.tileset2);

      //////////////////////////////
      var czml = [
        {
          id: "document",
          name: "CZML Point",
          version: "1.0",
        },
        {
          id: "point 1",
          name: "point",
          position: {
            cartographicDegrees: [-111.0, 40.0, 0],
          },
          point: {
            color: {
              rgba: [255, 255, 255, 255],
            },
            outlineColor: {
              rgba: [255, 0, 0, 255],
            },
            outlineWidth: 4,
            pixelSize: 20,
          },
        },
      ];
      this.viewer.dataSources.add(Cesium.CzmlDataSource.load(czml));


      console.log('viewer', this.viewer);
      console.log('layer1', this.layer1);
      console.log('tileset1', this.tileset1);
      console.log('tileset2', this.tileset2);
  },
  watch : {
      toggle_zoom(newVal) { 
        console.log(newVal);
        console.log(this[newVal]);
        this.viewer.zoomTo(this[newVal]);
      },
    },	



}

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
