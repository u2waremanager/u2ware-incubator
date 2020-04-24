<template>
  <div id="map" style="width:100%;height:400px"></div>
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
      

      console.log(ol);

      this.map = new ol.Map({
        target: 'map',
        layers: [
          new olLayer.Tile({
            source: new olSource.OSM()
          })
        ],
        view: new ol.View({
          center: [0, 0],
          zoom: 2
        })
      });
      //map.addLayer(osmLayer);
      console.log(this.map);
    }

}
</script>

<style>

</style>