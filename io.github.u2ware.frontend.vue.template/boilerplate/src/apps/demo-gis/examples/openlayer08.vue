<template>
<div>
  <div id="map" style="width:1024px;height:768px"></div>
  <p>X: {{mx}}, Y : {{my}}</p>
  <pre>{{shape}}</pre>
</div>
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
      map : undefined,
      mx : undefined,
      my : undefined,
      shape : undefined,
    }),

    mounted(){



        var wmsSource = new olSource.TileWMS({
          url: 'http://localhost:9099/geoserver/wms',
          params: {
                'FORMAT': 'image/png', 
                'VERSION': '1.1.1',
                tiled: true,
                // "LAYERS": 'sample00:Layer00',
                //  "LAYERS": 'sample00:hongsung_0720,sample00:Layer00',
                // "LAYERS": 'sample00:Layer00',

                "LAYERS": 'solar_module:hongsung_base_geotiff,solar_module:hongsung_panel_postgis_view',
                "exceptions": 'application/vnd.ogc.se_inimage',
                //BGCOLOR :'0xFFFFFF', //with opacity
            // tilesOrigin: -74.047185 + "," + 40.679648

          },
        //   crossOrigin: 'anonymous'
        });







      var tiled = new olLayer.Tile({
        // visible: false,
       // opacity : 0.5,
        source: wmsSource
      });
      console.log(tiled);


      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      var baseOSM = new olLayer.Tile({
        source: new olSource.OSM()
      });
      console.log('baseOSM', baseOSM);

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
      var projection = 'EPSG:4326'; 
      // var projection = 'EPSG:32652'; 
      // var projection = 'EPSG:3857'; 
      

      // var center = [0,0];
      var center = [126.64395737884323, 36.53944581622059];
      
      var zoom = 19;


 
      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          baseOSM, 
          tiled
        ],
        view: new ol.View({
          projection : projection,
          center: center,
          zoom: zoom
        })
      });
      console.log(this.map);


      this.map.on('pointermove', (evt)=>{ 
        // console.log('pointermove', evt.map.getView().getCenter());
        var c = evt.map.getView().getCenter();
        this.mx = c[0];
        this.my = c[1];
      });
      this.map.on('singleclick', (evt)=>{ 
        console.log('singleclick', evt.map.getView().getCenter());

        var view = evt.map.getView();
        console.log('singleclick view', view);
        var viewResolution = view.getResolution();
        console.log('singleclick viewResolution', viewResolution);

        console.log('singleclick wmsSource', wmsSource);
        console.log('singleclick wmsSource.getFeatureInfoUrl', wmsSource.getFeatureInfoUrl);

        var url = wmsSource.getFeatureInfoUrl(evt.coordinate, viewResolution, view.getProjection(),
          {'INFO_FORMAT': 'application/json', 'FEATURE_COUNT': 50});



        if (url) {
          //GeoServer
            this.$axios.get(url).then(res => {
                // console.log(res.data.features);
                this.shape = res.data.features;
            });
        }
      });


// raster2pgsql -s 4326 -C -l 2,4 -F -t 2700x2700 C:\Users\u2war\Downloads\GRAY_50M_SR_OB.tif sample00.gray_50m_sr_ob | psql -h localhost -p 5432 -U postgres 

 



    }
}
</script>

<style>

</style>