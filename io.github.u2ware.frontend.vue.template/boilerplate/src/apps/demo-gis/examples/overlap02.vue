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
// import OLCesium from 'olcs/OLCesium.js';


export default {

    data: () => ({
      map : undefined
    }),

    mounted(){

      var base =  new olLayer.Tile({
            source: new olSource.OSM() //EPSG:3857
          });
      console.log(base);
      
      // var tiled = new olLayer.Tile({
      //   visible: false,
      //   source: new olSource.TileWMS({
      //     url: 'http://localhost:9099/geoserver/sample00/wms',
      //     params: {
      //           'FORMAT': 'image/png', 
      //           'VERSION': '1.1.1',
      //           tiled: true,
      //           "LAYERS": 'sample00:Layer00',
      //           "exceptions": 'application/vnd.ogc.se_inimage',
      //        tilesOrigin: -74.047185 + "," + 40.679648
      //     }
      //   })
      // });

      var untiled = new olLayer.Image({
        source: new olSource.ImageWMS({
          ratio: 1,
          url: 'http://localhost:9099/geoserver/sample00/wms',
          params: {'FORMAT': 'image/png',
                   'VERSION': '1.1.1',  
                "LAYERS": 'sample00:Layer00',
                "exceptions": 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log(untiled);


      var tiled = new olLayer.Tile({
        visible: false,
        source: new olSource.TileWMS({
          url: 'http://localhost:9099/geoserver/sample00/wms',
          params: {'FORMAT': 'image/png', 
                   'VERSION': '1.1.1',
                   tiled: true,
                "LAYERS": 'sample00:Layer00',
                "exceptions": 'application/vnd.ogc.se_inimage',
             tilesOrigin: -1 + "," + -1
          }
        })
      });
      console.log(tiled);


    // var bounds = [-1, -1, 0, 0];

      const ol2d = new ol.Map({
        target: 'map',
        layers: [
           base, tiled
        ],
        view: new ol.View({
          center: [0,0],
          // center: transform([127, 38], 'EPSG:4326', 'EPSG:3857'),
          // center: transform([128.67894, 35.82843], 'EPSG:4326', 'EPSG:3857'),
          zoom: 2
        })
      });
      console.log(ol2d);

      // ol2d.getView().fit(bounds, ol2d.getSize());


        // const ol3d = new OLCesium({
        //   map: ol2d,
        // });
        // const scene = ol3d.getCesiumScene();
        // scene.terrainProvider = Cesium.createWorldTerrain(); //EPSG:4326


        // var tileset = new Cesium.Cesium3DTileset({
        //       // url: Cesium.IonResource.fromAssetId(73494), //대전 대덕대교 
        //       // url: Cesium.IonResource.fromAssetId(19005), ////대구 운동장 
        //     url: Cesium.IonResource.fromAssetId(95528), ////대구 운동장 
        // });
        // scene.primitives.add(tileset);

        // ol3d.getDataSources().add(Cesium.KmlDataSource.load('/deaduck_bridge_97pit.kmz', {
        //       camera: scene.camera,
        //       canvas: scene.canvas
        //     }
        // ));

        // const camera = ol3d.getCamera();
        // camera.setTilt(0.5);
        // console.log(camera);

        // ol3d.setEnabled(true);
        // console.log(ol3d);

    }


}
</script>

<style>

</style>