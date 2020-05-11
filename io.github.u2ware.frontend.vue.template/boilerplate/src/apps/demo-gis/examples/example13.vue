<template>
<div>

    <div id="map" style="width:600px;height:400px;"></div>
    <input id="enable" type="button" value="Enable/disable" />

    <input type="button" value="Enable/disable lightning (only 3d)" onclick="javascript:toggleTime()"/>
    <select id="time">
      <option value="">Now</option>
      <option value="0">0:00</option>
      <option value="3">3:00</option>
      <option value="6">6:00</option>
      <option value="9">9:00</option>
      <option value="12">12:00</option>
      <option value="15">15:00</option>
      <option value="18">18:00</option>
      <option value="21">21:00</option>
    </select>
</div>  
</template>

<script>
import * as Cesium from 'cesium';
window.Cesium = Cesium; // expose Cesium to the OL-Cesium library
Cesium.Ion.defaultAccessToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiJhYmM5NDlmZC0xYWU5LTQ3YjQtYTg1NC1hZWFlMDhhNjIyMWQiLCJpZCI6NDY4Niwic2NvcGVzIjpbImFzciIsImdjIl0sImlhdCI6MTU0MTQxNTU5NH0.IYyAIxWJ2tqQKOR6OI2NlS8OT1MiXJ7LBgPFaJrlkmA';

// import Cesium from 'cesium';
import OLCesium from 'olcs/OLCesium.js';

import 'ol/ol.css';
import {transform} from 'ol/proj.js';
import olView from 'ol/View.js';
import {defaults as olControlDefaults} from 'ol/control.js';
import olSourceOSM from 'ol/source/OSM.js';
import olLayerTile from 'ol/layer/Tile.js';
import olMap from 'ol/Map.js';



export default {

    data: () => ({
      map : undefined
    }),

    mounted(){

const ol2d = new olMap({
  layers: [
    new olLayerTile({
      source: new olSourceOSM()
    })
  ],
  controls: olControlDefaults({
    attributionOptions: {
      collapsible: false
    }
  }),
  target: 'map',
  view: new olView({
    center: transform([25, 20], 'EPSG:4326', 'EPSG:3857'),
    zoom: 3
  })
});

const timeElt = document.getElementById('time');
const ol3d = new OLCesium({
  map: ol2d,
  time() {
    const val = timeElt.value;
    if (ol3d.getCesiumScene().globe.enableLighting && val) {
      const d = new Date();
      d.setUTCHours(val);
      return Cesium.JulianDate.fromDate(d);
    }
    return Cesium.JulianDate.now();
  }
});
const scene = ol3d.getCesiumScene();
scene.terrainProvider = Cesium.createWorldTerrain();
ol3d.setEnabled(true);


timeElt.style.display = 'none';

document.getElementById('enable').addEventListener('click', () => ol3d.setEnabled(!ol3d.getEnabled()));
window['toggleTime'] = function() {
  scene.globe.enableLighting = !scene.globe.enableLighting;
  if (timeElt.style.display == 'none') {
    timeElt.style.display = 'inline-block';
  } else {
    timeElt.style.display = 'none';
  }
};

    }


}
</script>

<style>

</style>