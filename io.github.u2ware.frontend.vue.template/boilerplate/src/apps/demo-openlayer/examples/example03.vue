<template>
   <div>
 <vl-map data-projection="EPSG:4326" style="height: 400px">
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
      <vl-source-xyz 
      attributions='<a href="https://www.google.at/permissions/geoguidelines/attr-guide.html">Map data ©2015 Google</a>'
      url="http://mt0.google.com/vt/lyrs=m&hl=en&x={x}&y={y}&z={z}"
      ></vl-source-xyz>
    </vl-layer-tile>
  </vl-map>
    <div style="padding: 20px">
      Source: http://mt0.google.com/vt/lyrs=m&hl=en&x={x}&y={y}&z={z}<br>
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
        geolocPosition : undefined
      }
    },
  }

// Roadmap
// http://mt0.google.com/vt/lyrs=m&hl=en&x={x}&y={y}&z={z}
// Terrain
// http://mt0.google.com/vt/lyrs=p&hl=en&x={x}&y={y}&z={z}

// Altered roadmap
// http://mt0.google.com/vt/lyrs=r&hl=en&x={x}&y={y}&z={z}

// Satellite only
// http://mt0.google.com/vt/lyrs=s&hl=en&x={x}&y={y}&z={z}
// https://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}

// Terrain only
// http://mt0.google.com/vt/lyrs=t&hl=en&x={x}&y={y}&z={z}

// Hybrid
// http://mt0.google.com/vt/lyrs=y&hl=en&x={x}&y={y}&z={z}



</script>
