<template>
  <div id="map" style="width:640px;height:480px"></div>
</template>

<script>
import 'ol/ol.css';

import * as ol from 'ol';
import * as olLayer from 'ol/layer';
import * as olSource from 'ol/source';


// import {olMap, olView} from 'ol';
// import olLayerTile from 'ol/layer/Tile';
// import olSourceOSM from 'ol/source/OSM';


export default {

    data: () => ({
      map : undefined
    }),

    mounted(){
      
      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var untiled = new olLayer.Image({
        source: new olSource.ImageWMS({
          // ratio: 1,
          url: 'http://localhost:9099/geoserver/wms',
          params: {
              'FORMAT': 'image/png',
              'VERSION': '1.1.1',  
              'LAYERS': 'tiger:tiger_roads',
              'exceptions': 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log(untiled);


      var tiled = new olLayer.Tile({
        // visible: false,
       // opacity : 0.5,
        source: new olSource.TileWMS({
          url: 'http://localhost:9099/geoserver/wms',
          params: {
                'FORMAT': 'image/png', 
                'VERSION': '1.1.1',
                tiled: true,
                "LAYERS": 'tiger:tiger_roads',
                "exceptions": 'application/vnd.ogc.se_inimage',
                //BGCOLOR :'0xFFFFFF', //with opacity
            // tilesOrigin: -74.047185 + "," + 40.679648
          }
        })
      });
      console.log(tiled);




      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var baseOSM = new olLayer.Tile({
        source: new olSource.XYZ({
          url : 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        })
      });
      console.log('baseOSM', baseOSM);

      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var projection = 'EPSG:4326'; 
      var center = [-73.96705563197177, 40.80369196946623];
      // var projection = 'EPSG:3857'; //default 
      // var center = [14133818.022824,4520485.8511757];
      var zoom = 16;



      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          tiled
        ],
        view: new ol.View({
          projection : projection,
          center: center,
          zoom: zoom
        })
      });
      console.log(this.map);


      this.map.on('pointermove', (evt)=>{ 
        console.log('pointermove', evt.map.getView().getCenter());
      });







    }

}
</script>

<style>

</style>