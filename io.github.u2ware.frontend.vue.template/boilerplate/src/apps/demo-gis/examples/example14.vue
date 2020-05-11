<template>
<div>

    <div id="map" style="width:600px;height:400px;"></div>
    <input id="enable" type="button" value="Enable/disable" />

</div>  
</template>

<script>
import * as Cesium from 'cesium';
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA';

window.Cesium = Cesium; // expose Cesium to the OL-Cesium library

import 'ol/ol.css';
import OLCesium from 'olcs/OLCesium.js';
import {transform} from 'ol/proj.js';
import olView from 'ol/View.js';
import {defaults as olControlDefaults} from 'ol/control.js';
import olSourceOSM from 'ol/source/OSM.js';
import olLayerTile from 'ol/layer/Tile.js';
import olMap from 'ol/Map.js';


export default {

    data: () => ({
      map : undefined
    }),

    mounted(){

      const ol2d = new olMap({
        layers: [
          new olLayerTile({
            source: new olSourceOSM()
          })
        ],
        controls: olControlDefaults({
          attributionOptions: {
            collapsible: false
          }
        }),
        target: 'map',
        view: new olView({
          center: transform([25, 20], 'EPSG:4326', 'EPSG:3857'),
          zoom: 3
        })
      });

      const ol3d = new OLCesium({map: ol2d});
      const scene = ol3d.getCesiumScene();
      scene.terrainProvider = Cesium.createWorldTerrain();
      ol3d.setEnabled(true);

      var tileset = new Cesium.Cesium3DTileset({
          url: Cesium.IonResource.fromAssetId(73494)
      });
      scene.primitives.add(tileset);

      
      //ol3d.zoomTo(tileset);
      console.log(ol3d);
      console.log(scene);
      console.log(scene.view);
      console.log(scene.entities);

      


      // ol3d.getDataSources().add(tileset, {
      //       camera: scene.camera,
      //       canvas: scene.canvas
      //     }
      // );
      
      // ol3d.getDataSources().add(Cesium.KmlDataSource.load('deduck_sample.kml',
      //   //   'https://api3.geo.admin.ch/ogcproxy?url=' +
      //   // 'https%3A%2F%2Fdav0.bgdi.admin.ch%2Fbazl_web%2FActive_Obstacles.kmz', 
        
      //   {
      //       camera: scene.camera,
      //       canvas: scene.canvas
      //     }
      // ));

      document.getElementById('enable').addEventListener('click', () => ol3d.setEnabled(!ol3d.getEnabled()));

    }


}
</script>

<style>

</style>