<template>
   <div>
 <vl-map data-projection="EPSG:900913" style="height: 400px">
    <vl-view :zoom.sync="zoom" :center.sync="center"></vl-view>

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


    <vl-layer-tile>
<!-- 
      <vl-source-wms
        attributions='<a href="http://map.vworld.kr">vWorld 공간정보오픈플랫폼</a>'
        url=
        "http://map.vworld.kr/js/wms.do?APIKEY=D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F&DOMAIN=http%3A%2F%2Flocalhost%3A8080&REQUEST=GetMap&LAYERS=lt_c_adsido&STYLES=lt_c_adsido&CRS=EPSG%3A900913&BBOX=14133818.022824,4520485.8511757,14134123.770937,4520791.5992888&WIDTH=256&HEIGHT=256&FORMAT=image%2Fpng"
        >
      </vl-source-wms>
 -->

      <vl-source-xyz 
      attributions='<a href="http://map.vworld.kr">vWorld 공간정보오픈플랫폼</a>'
      url="http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png"
      ></vl-source-xyz>



    </vl-layer-tile>
  </vl-map>
    <div style="padding: 20px">
      Source: http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png<br>
      Zoom: {{ zoom }}<br>
      Center: {{ center }}<br>
      Rotation: {{ rotation }}<br>
      My geolocation: {{ geolocPosition }}
    </div>
 </div>
</template>

<script>
        // url="http://api.vworld.kr/req/wms?&KEY=D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F&DOMAIN=http://localhost:8080&SERVICE=WMS&REQUEST=GetMap&VERSION=1.3.0&LAYERS=lp_pa_cbnd_bonbun,lp_pa_cbnd_bubun&STYLES=lp_pa_cbnd_bonbun_line,lp_pa_cbnd_bubun_line&CRS=EPSG:900913&BBOX=14133818.022824,4520485.8511757,14134123.770937,4520791.5992888&WIDTH=256&HEIGHT=256&FORMAT=image/png&TRANSPARENT=false&BGCOLOR=0xFFFFFF&EXCEPTIONS=text/xml">


// http://map.vworld.kr/js/vworldMapInit.js.do?version=2.0&apiKey=D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F
// http://api.vworld.kr/req/wmts/1.0.0/D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F/Base/{z}/{y}/{x}.png"
// http://apis.vworld.kr/wmts/1.0.0/Base/D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F/1/111757/50838.png" {z}/{y}/{x}.png
// https://xdworld.vworld.kr/2d/Base/service/
// https://xdworld.vworld.kr/2d/Base/service/16/55908/25432.png
// http://xdworld.vworld.kr:8080/2d
// http://apis.vworld.kr/tms/1.0.0/Base/D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F/17/111757/80233.png
// http://apis.vworld.kr/wmts/1.0.0/Base/D0074CD5-ED5F-3F0D-9862-897B1AEF6E3F/17/111757/50838.png


// http://xdworld.vworld.kr:8080/2d/Base/201612/{z}/{x}/{y}.png'
// http://xdworld.vworld.kr:8080/2d/Base/service/16/55908/25432.png
// http://xdworld.vworld.kr:8080/2d/Base/service/16/55910/25440.png
// http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png

  export default {
    data () {
      return { 
        zoom: 8,
        center: [14150224.074678827, 4485107.682281462],
        rotation: 0,
        geolocPosition : undefined
      }
    },
  }
</script>