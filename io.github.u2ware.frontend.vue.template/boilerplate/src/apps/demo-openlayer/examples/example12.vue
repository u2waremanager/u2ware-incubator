<template>
   <div stype="width: 100%; height: 400px">
    <div id="kakaomap" style="position: absolute; left:0px; top:30px; z-index: 1; width: 100%; height: 400px; margin: 0;  padding:0;"></div>
    <div id="map" style="position: absolute; left:0px; top:30px; z-index: 1; width: 100%; height: 400px; margin: 0;  padding:0;"></div>
  </div>

</template>





<script>
import 'ol/ol.css';


import * as ol from 'ol';
// import * as olLayer from 'ol/layer';
// import * as olSource from 'ol/source';

import proj4 from 'proj4';
import {get as getProjection} from 'ol/proj';
import {register} from 'ol/proj/proj4';
import {transform} from 'ol/proj';




  export default {
    data () {
      return { 
        zoom: 3,
        center: [127.1136256, 37.3293056],
        rotation: 0,
        geolocPosition : undefined,
        kakaomap : undefined,
        map : undefined
      }
    },

    methods : {
        updateCenter(e){
            console.log('move1', e);
            console.log('move2', this.kakaomap.getCenter());

            // var center = ol.proj.transform(view.getCenter(), 'EPSG:3857', 'EPSG:4326');
        },
        updateZoom(){
            //this.kakaomap.setLevel(16-this.zoom);
        }
    },

    mounted: function() {

        //////////////////////////////
        var container = document.getElementById('kakaomap');
        var options = { 
            center : new window.kakao.maps.LatLng(33.450701, 126.570667),
            level: this.zoom //지도의 레벨(확대, 축소 정도)
        };
        this.kakaomap = new window.kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
        console.log('kakaomap', this.kakaomap);



        console.log("proj4", proj4);
        console.log("register ", register);
        console.log("getProjection: ", getProjection);

        // //////////////////////////////
        proj4.defs('ESPG:5181', '+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +units=m +no_defs');
        proj4.defs('EPSG:27700', '+proj=tmerc +lat_0=49 +lon_0=-2 +k=0.9996012717 ' +
            '+x_0=400000 +y_0=-100000 +ellps=airy ' +
            '+towgs84=446.448,-125.157,542.06,0.15,0.247,0.842,-20.489 ' +
            '+units=m +no_defs');

        register(proj4);


        var proj4326 = getProjection('EPSG:4326');
        console.log("proj4326: ", proj4326);

        var proj5181 = getProjection('ESPG:5181');
        console.log("proj5181: ", proj5181);

        var proj27700 = getProjection('EPSG:27700');
        console.log("proj27700: ", proj27700);

        // // proj5181.setExtent([0, 0, 0, 0]);



        //////////////////////////////
        var view = new ol.View({
          center: [0, 0],
          zoom: 3,
        });
        view.on('change:center', () =>{
          console.log('view.getCenter()' , view.getCenter());
          var center = transform(view.getCenter(), 'ESPG:5181', 'EPSG:4326');
          console.log('center' , center);
          // this.kakaomap.setCenter(new window.kakao.maps.LatLng(center[1], center[0]));
          // this.kakaomap.setCenter(new window.kakao.maps.LatLng(33.450701, 126.570667));
        });
        // view.on('change:resolution', () =>{
        //   console.log(view.getZoom());
        //   this.kakaomap.setLevel(view.getZoom());
        // });


        this.map = new ol.Map({
          target: 'map',
          view: view
        });
        console.log(this.map);



    }
  }
</script>
<style >

</style>