<template>
   <div>

    <div style="width: 500px; height: 400px; margin: 0; padding:0; position:relative;">

        <div id="kakaomap" style="position: absolute; left:0px; top:0px; z-index: 1; width: 100%; height: 100%; margin: 0;  padding:0;"></div>
<!-- 
        <div id="olmap" style="position: absolute; left:0px; top:0px; z-index: 1; width: 100%; height: 100%; margin: 0;  padding:0;"></div> -->

 
        <vl-map data-projection="EPSG:4326" style="position: absolute; left:0px; top:0px; z-index: 1; width: 100%; height: 100%; margin: 0;  padding:0;">

            <vl-view :zoom.sync="zoom" :center.sync="center" v-on:update:center="updateCenter" v-on:update:zoom="updateZoom"></vl-view>

            <!--  -->

            <vl-geoloc @update:position="geolocPosition = $event">
                <template slot-scope="geoloc">
                <vl-feature v-if="geoloc.position" id="position-feature">
                    <vl-geom-point :coordinates="geoloc.position"></vl-geom-point>
                    <vl-style-box>
                    <vl-style-icon src="_media/marker.png" :scale="0.4" :anchor="[0.5, 1]"></vl-style-icon>
                    </vl-style-box>
                </vl-feature>
                </template>
            </vl-geoloc>

        </vl-map>
        
    </div>






    <div style="padding: 20px">


      Source: http://i{a-c}.maps.daum-img.net/map/image/G03/i/1.04/L${z}/${y}/${x}.png<br>
      Zoom: {{ zoom }}<br>
      Center: {{ center }}<br>
      Rotation: {{ rotation }}<br>
      My geolocation: {{ geolocPosition }}
    </div>
 </div>
</template>

<script>
// import 'ol/ol.css';
// import {Map, View} from 'ol';
// import {transform} from 'ol/proj';
// import TileLayer from 'ol/layer/Tile';
// import OSM from 'ol/source/OSM';

  export default {
    data () {
      return { 
        zoom: 3,
        center: [127.1136256, 37.3293056],
        rotation: 0,
        geolocPosition : undefined,
        kakaomap : undefined
      }
    },

    methods : {
        updateCenter(e){
            console.log('move1', e);
            console.log('move2', this.kakaomap.getCenter());

            // var center = ol.proj.transform(view.getCenter(), 'EPSG:3857', 'EPSG:4326');
        },
        updateZoom(){
            this.kakaomap.setLevel(16-this.zoom);
        }
    },

    mounted: function() {
        var container = document.getElementById('kakaomap');
        var options = { 
            center : new window.kakao.maps.LatLng(33.450701, 126.570667),
            level: this.zoom //지도의 레벨(확대, 축소 정도)
        };
        this.kakaomap = new window.kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
        console.log(this.kakaomap);

        // const view = new View({
        //         center: [127.1136256, 37.3293056],
        //         zoom: 0
        // });
        // view.on('change:center', function() {
        //     var center = transform(view.getCenter(), 'EPSG:3857', 'EPSG:4326');
        //     console.log("EPSG:4326 "+view.getCenter());
        //     console.log("EPSG:3857 "+center);
        //     // gmap.setCenter(new google.maps.LatLng(center[1], center[0]));
        // });

        // const olmap = new Map({
        //     target: 'olmap',
        //     // layers: [
        //     //     new TileLayer({
        //     //         source: new OSM()
        //     //     })
        //     // ],
        //     view: view
        // });


        // console.log(olmap);


    }
  }
</script>
<style >

    .maps {
      width: 960px;
      height: 500px;
      position: relative;
      border: 1px solid red;
    }
    #map {
      width: 100%;
      height: 100%;
    }
    .fill {
      width: 100%;
      height: 100%;
    }


</style>