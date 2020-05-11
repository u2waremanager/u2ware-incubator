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
      var baseVWorld = new olLayer.Tile({
        source: new olSource.XYZ({
            url : 'http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png',
            attributions : '<a href="http://map.vworld.kr">vWorld 공간정보오픈플랫폼</a>'
        })
      });
      console.log('baseVWorld', baseVWorld);

      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var center =  [14150224.074678827, 4485107.682281462];
      var zoom = 8;

      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          baseVWorld
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