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
      var baseGoogle = new olLayer.Tile({
        source: new olSource.XYZ({
          url : 'http://mt0.google.com/vt/lyrs=m&hl=en&x={x}&y={y}&z={z}',
          attributions : '<a href="https://www.google.at/permissions/geoguidelines/attr-guide.html">Map data ©2015 Google</a>'
        })
      });
      console.log('baseGoogle', baseGoogle);

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
          baseGoogle
        ],
        view: new ol.View({
          center: center,
          zoom: zoom
        })
      });
      console.log(this.map);
    }
}
</script>

<style>

</style>