<template>
  <div id="map" style="width:640px;height:480px"></div>
</template>

<script>
import 'ol/ol.css';

import * as ol from 'ol';
import * as olLayer from 'ol/layer';
import * as olSource from 'ol/source';


export default {

    data: () => ({
      map : undefined
    }),

    mounted(){
      
      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var baseBing =  new olLayer.Tile({
          source: new olSource.BingMaps({
            key : 'ArbsA9NX-AZmebC6VyXAnDqjXk6mo2wGCmeYM8EwyDaxKfQhUYyk0jtx6hX5fpMn',
            imagerySet: ['AerialWithLabels'],
          })
      });
      console.log('baseBing', baseBing);

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
          baseBing
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