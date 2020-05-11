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
      

// (UA)초경량비행장치공역	lt_c_aisuac	lt_c_aisuac	lt_c_aisuac	2D/3D
// 경계구역	lt_c_aisaltc		lt_c_aisaltc	2D/3D
// 공중급유구역	lt_c_aisrflc		lt_c_aisrflc	2D/3D
// 공중전투기동훈련장	lt_c_aisacmc		lt_c_aisacmc	2D/3D
// 관제권	lt_c_aisctrc		lt_c_aisctrc	2D/3D
// 군작전구역	lt_c_aismoac		lt_c_aismoac	2D/3D
// 방공식별구역	lt_c_aisadzc		lt_c_aisadzc	2D/3D
// 비행금지구역	lt_c_aisprhc		lt_c_aisprhc	2D/3D
// 비행장교통구역	lt_c_aisatzc		lt_c_aisatzc	2D/3D
// 비행정보구역	lt_c_aisfirc		lt_c_aisfirc	2D/3D
// 비행제한구역	lt_c_aisresc		lt_c_aisresc	2D/3D
// 수색비행장비행구역	lt_l_aissearchl,
// lt_p_aissearchp		lt_l_aissearchl,
// lt_p_aissearchp	2D/3D
// 시계비행로	lt_l_aisvfrpath,
// lt_p_aisvfrpath		lt_l_aisvfrpath,
// lt_p_aisvfrpath	2D/3D
// 위험구역	lt_c_aisdngc		lt_c_aisdngc	2D/3D
// 접근관제구역	lt_c_aistmac		lt_c_aistmac	2D/3D
// 제한고도	lt_l_aisrouteu		lt_l_aisrouteu	2D/3D
// 한강회랑	lt_l_aiscorrid_ys,
// lt_l_aiscorrid_gj,
// lt_p_aiscorrid_ys,
// lt_p_aiscorrid_gj		lt_l_aiscorrid_ys,
// lt_l_aiscorrid_gj,
// lt_p_aiscorrid_ys,
// lt_p_aiscorrid_gj	2D/3D
// 항공로	lt_l_aispath		lt_l_aispath	2D/3D
// 헬기장	lt_p_aishcstrip		lt_p_aishcstrip	2D/3D

      var wmsVWorld = new olLayer.Tile({
        
        // opacity : 0.5, //with BGCOLOR
        source: new olSource.TileWMS({
          url: 'http://api.vworld.kr/req/wms',
          params: {
            KEY : 'D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F',
            DOMAIN : 'http://localhost:8080',
            FORMAT :'image/png',

            LAYERS :'lt_c_adsido,lt_c_adri,lt_c_adsigg,lt_c_ademd',
            STYLES :'lt_c_adsido,lt_c_adri,lt_c_adsigg,lt_c_ademd',

            // LAYERS :'lt_c_aisprhc,lt_c_aisresc',
            // STYLES :'lt_c_aisprhc,lt_c_aisresc',

            // BGCOLOR :'0xFFFFFF', //with opacity


            // CRS :'EPSG:4326',
            // BBOX :'14133818.022824,4520485.8511757,14134123.770937,4520791.5992888',
            // WIDTH :'256',
            // HEIGHT :'256',

            // TRANSPARENT :'false',
            // EXCEPTIONS :'text/xml',
          }
        })
      });
      console.log(wmsVWorld);



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
      // var projection = 'EPSG:4326'; 
      // var center = [127, 38];
      var projection = 'EPSG:3857'; //default 
      var center = [14133818.022824,4520485.8511757];
      var zoom = 7;
      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          baseVWorld, wmsVWorld
        ],
        view: new ol.View({
          projection : projection,
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