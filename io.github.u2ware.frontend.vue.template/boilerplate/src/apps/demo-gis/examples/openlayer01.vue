<template>
  <div id="map" style="width:640px;height:480px"></div>
</template>

<script>
import 'ol/ol.css';

import * as ol from 'ol';
import * as olLayer from 'ol/layer';
import * as olSource from 'ol/source';

// import * as olProj from 'ol/proj';
// import * as olExtent from 'ol/extent';

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
        source: new olSource.OSM()
      });
      console.log('baseOSM', baseOSM);



      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var projection = 'EPSG:3857'; //default 
      // var projection = 'EPSG:4326'; //

      var center = [0,0];
      // var center = [127, 38];
      // var center = olProj.transform([127, 38], 'EPSG:4326', 'EPSG:3857');
      var zoom = 2;
      
      // var bounds = [14133818.022824, 4520485.8511757,  14136000,  4520792];

      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          baseOSM
        ],
        view: new ol.View({
          projection: projection,
          center: center,
          zoom: zoom
        }),
      });
      console.log(this.map);
      
      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      // this.map.getView().fit(bounds, this.map.getSize());
      console.log(this.map.getView());



      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      // this.map.on('moveend', (evt)=>{

      //   var map = evt.map;
      //   // var extent = map.getView().calculateExtent(map.getSize());
      //   // var bottomLeft = olProj.toLonLat(olExtent.getBottomLeft(extent));
      //   // var topRight = olProj.toLonLat(olExtent.getTopRight(extent));
      //   // var center = olProj.toLonLat(olExtent.getCenter(extent));
      //   // console.log('moveend','left', bottomLeft[0]);
      //   // console.log('moveend','bottom', bottomLeft[1]);
      //   // console.log('moveend','right', topRight[0]);
      //   // console.log('moveend','top', topRight[1]);
      //   // console.log('moveend','center', center);
      //   // console.log('moveend','center-lat', center[0]);
      //   // console.log('moveend','center-lon', center[1]);
      //   console.log('moveend','center', map.getView().getCenter());
      // });


      // this.map.on('change', (evt)=>{ console.log('change', evt);});
      // this.map.on('change:layerGroup', (evt)=>{ console.log('change:layerGroup', evt);});
      // this.map.on('change:size', (evt)=>{ console.log('change:size', evt);});
      // this.map.on('change:target', (evt)=>{ console.log('change:target', evt);});
      // this.map.on('change:view', (evt)=>{ console.log('change:view', evt);});
      // this.map.on('click', (evt)=>{ console.log('click', evt);});
      // this.map.on('dblclick', (evt)=>{ console.log('dblclick', evt);});
      // this.map.on('error', (evt)=>{ console.log('error', evt);});
      // this.map.on('moveend', (evt)=>{ console.log('moveend', evt);});
      // this.map.on('movestart', (evt)=>{ console.log('movestart', evt);});
      // this.map.on('pointerdrag', (evt)=>{ console.log('pointerdrag', evt);});
      // this.map.on('pointermove', (evt)=>{ console.log('pointermove', evt);});
      // this.map.on('postcompose', (evt)=>{ console.log('postcompose', evt);});
      // this.map.on('postrender', (evt)=>{ console.log('postrender', evt);});
      // this.map.on('precompose', (evt)=>{ console.log('precompose', evt);});
      // this.map.on('propertychange', (evt)=>{ console.log('propertychange', evt);});
      // this.map.on('rendercomplete', (evt)=>{ console.log('rendercomplete', evt);});
      // this.map.on('singleclick', (evt)=>{ console.log('singleclick', evt);});


      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////


    }

}
</script>

<style>

</style>