<template>
  <div id="map" style="width:100%;height:400px"></div>
</template>

<script>
import 'ol/ol.css';

import * as ol from 'ol';
// import * as olProj from 'ol/proj';
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
      
      var format = 'image/png';
      var bounds = [-74.047185, 40.679648,
                    -73.907005, 40.882078];

      var untiled = new olLayer.Image({
        source: new olSource.ImageWMS({
          ratio: 1,
          url: 'http://localhost:8080/geoserver/wms',
          params: {
              'FORMAT': format,
              'VERSION': '1.1.1',  
              'LAYERS': 'tiger-ny',
              'exceptions': 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log(untiled);


      var tiled = new olLayer.Tile({
        visible: false,
        source: new olSource.TileWMS({
          url: 'http://localhost:8080/geoserver/wms',
          params: {'FORMAT': format, 
                   'VERSION': '1.1.1',
                   tiled: true,
                "LAYERS": 'tiger-ny',
                "exceptions": 'application/vnd.ogc.se_inimage',
             tilesOrigin: -74.047185 + "," + 40.679648
          }
        })
      });
      console.log(tiled);



      this.map = new ol.Map({
        target: 'map',
        layers: [
          untiled,
          tiled
        ],
        view: new ol.View({
           projection: 'EPSG:4326'
        })
      });
      this.map.getView().fit(bounds, this.map.getSize());
      

      console.log(this.map);
    }

}
</script>

<style>

</style>