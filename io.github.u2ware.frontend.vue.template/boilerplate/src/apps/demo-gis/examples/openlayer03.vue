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
      var baseOSM = new olLayer.Tile({
        source: new olSource.XYZ({
          url : 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        })
      });
      console.log('baseOSM', baseOSM);

      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var center = [0, 0];
      var zoom = 2;

      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          baseOSM
        ],
        view: new ol.View({
          center: center,
          zoom: zoom
        })
      });
      //map.addLayer(osmLayer);
      console.log(this.map);
    }

}
</script>

<style>

</style>