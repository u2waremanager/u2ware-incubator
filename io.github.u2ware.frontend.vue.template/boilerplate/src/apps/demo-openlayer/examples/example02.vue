<template>
  <div>
    <vl-map data-projection="EPSG:4326" :load-tiles-while-animating="true" :load-tiles-while-interacting="true" style="height: 400px">
        <vl-view :zoom.sync="zoom" :center.sync="center" :rotation.sync="rotation"></vl-view>


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



        <vl-layer-tile id="bingmaps">
            <vl-source-bingmaps :api-key="apiKey" :imagery-set="imagerySet"></vl-source-bingmaps>
        </vl-layer-tile>
    </vl-map>
    <div style="padding: 20px">
      Source: bingmaps<br>
      Zoom: {{ zoom }}<br>
      Center: {{ center }}<br>
      Rotation: {{ rotation }}<br>
      My geolocation: {{ geolocPosition }}
    </div>
  </div>
</template>

<script>
  export default {
    data () {
      return { 
        zoom: 8,
        center: [127.1136256, 37.3293056],
        rotation: 0,
        apiKey: 'ArbsA9NX-AZmebC6VyXAnDqjXk6mo2wGCmeYM8EwyDaxKfQhUYyk0jtx6hX5fpMn',
        imagerySet: 'AerialWithLabels',
      }
    },
  }
</script>