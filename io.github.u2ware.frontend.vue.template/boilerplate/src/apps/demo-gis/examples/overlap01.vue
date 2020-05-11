<template>
<div>

    <div id="map" style="width:600px;height:400px;"></div>
   
</div>  
</template>

<script>
import * as Cesium from 'cesium';
window.Cesium = Cesium; // expose Cesium to the OL-Cesium library
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA';


import 'ol/ol.css';
// import {transform} from 'ol/proj.js';
// import olView from 'ol/View.js';
// import {defaults as olControlDefaults} from 'ol/control.js';
// import olSourceOSM from 'ol/source/OSM.js';
// import olLayerTile from 'ol/layer/Tile.js';
// import olMap from 'ol/Map.js';

import * as ol from 'ol';
import * as olLayer from 'ol/layer';
import * as olSource from 'ol/source';
import OLCesium from 'olcs/OLCesium.js';


export default {

    data: () => ({
      map : undefined
    }),

    mounted(){

      var base =  new olLayer.Tile({
            source: new olSource.OSM()
          });

      var tiled = new olLayer.Tile({
        
        opacity : 0.5,
        source: new olSource.TileWMS({

          url: 'http://api.vworld.kr/req/wms',
          params: {
            KEY : 'D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F',
            DOMAIN : 'http://localhost:8080',

            LAYERS :'lt_c_aisprhc,lt_c_aisresc',
            STYLES :'lt_c_aisprhc,lt_c_aisresc',
            CRS :'EPSG:4326',
            BBOX :'14133818.022824,4520485.8511757,14134123.770937,4520791.5992888',
            WIDTH :'256',
            HEIGHT :'256',
            FORMAT :'image/png',
            TRANSPARENT :'false',
            BGCOLOR :'0xFFFFFF',
            EXCEPTIONS :'text/xml',
          }
        })
      });




console.log(tiled);




      const ol2d = new ol.Map({
        target: 'map',
        layers: [
          base, tiled
        ],
        view: new ol.View({
          center: [14150224.074678827, 4485107.682281462],
          zoom: 8
        })
      });
      console.log(ol2d);


      var tileset = new Cesium.Cesium3DTileset({
            url: Cesium.IonResource.fromAssetId(73494), //대전 대덕대교 
            // url: Cesium.IonResource.fromAssetId(19005), ////대구 운동장 
      });


        const ol3d = new OLCesium({
          map: ol2d,
        });
        const scene = ol3d.getCesiumScene();
        scene.terrainProvider = Cesium.createWorldTerrain();


        scene.primitives.add(tileset);


        ol3d.setEnabled(true);


      console.log(ol3d);

    }


}
</script>

<style>

</style>