<template>
<div>
  <div id="map" style="width:640px;height:480px"></div>
  <v-col cols="12" md="6">
  <v-btn-toggle multiple v-model="toggle_layer" >
    <v-btn x-small value="osm1" > osm </v-btn>
    <v-btn x-small value="osm2"> osm </v-btn>
    <v-btn x-small value="bing"> bing </v-btn>
    <v-btn x-small value="google"> google </v-btn>
  </v-btn-toggle> 
  <p/>
  <v-btn-toggle multiple v-model="toggle_layer" >
    <v-btn x-small value="vworld"> wworld </v-btn>
    <v-btn x-small value="vworld_wms"> sido </v-btn>
  </v-btn-toggle> 
  <p/>
  <v-btn-toggle multiple v-model="toggle_layer" >
    <v-btn x-small value="geoserver_tiger_image"> tiger-image </v-btn>
    <v-btn x-small value="geoserver_tiger_tile"> tiger-tile </v-btn>
    <v-btn x-small value="geoserver_solar_image"> solor-image </v-btn>
    <v-btn x-small value="geoserver_solar_tile"> solor-tile </v-btn>
    <v-btn x-small value="geoserver_panel_tile"> panel-tile </v-btn>
    <v-btn x-small value="geoserver_panel_vector"> panel-vector </v-btn>
  </v-btn-toggle> 


  <v-btn-toggle multiple v-model="toggle_layer" >
    <v-btn x-small value="geojson"> geojson </v-btn>
  </v-btn-toggle> 

  </v-col>

</div>
</template>

<script>
import 'ol/ol.css';

import * as ol from 'ol';
import * as olLayer from 'ol/layer';
import * as olSource from 'ol/source';

import * as olProj from 'ol/proj';
import * as olExtent from 'ol/extent';
// import * as olEasing from 'ol/easing';

// import olFeature from 'ol/Feature';
import * as olFormat from 'ol/format';
import * as olStyle from 'ol/style';
// import * as olGeom from 'ol/geom';



import geojson_data_sample from './geojson_data_sample.json';
import geojson_style_sample from './geojson_style_sample.js';

export default {
    data: () => ({
      map : undefined,

      projection : 'EPSG:3857', //default 
      center :  [14150224.074678827, 4485107.682281462],
      // center : olProj.transform([127, 36], 'EPSG:4326', 'EPSG:3857'),
      zoom : 7,

      // projection : 'EPSG:4326',
      // center : [127, 38],
      // zoom : 7,

      layers : {
        osm1 : undefined,
        osm2 : undefined,
        bing : undefined,
        google : undefined,
        vworld : undefined,
        vworld_wms : undefined,
        geoserver_tiger_image : undefined,
        geoserver_tiger_tile : undefined,
        geoserver_solar_image : undefined,
        geoserver_solar_tile : undefined,
        geoserver_panel_tile : undefined,
        geoserver_panel_vector : undefined,
        geojson : undefined,
      },
      toggle_layer : undefined
    }),
    mounted(){
      
      console.log(geojson_data_sample);
      console.log(geojson_style_sample);
      

      //////////////////////////////////////////////////////////
      this.layers.osm2 = new olLayer.Tile({
        visible : false,
        source: new olSource.OSM()
      });
      console.log('layer_osm', this.layers.osm1);

      //////////////////////////////////////////////////////////
      this.layers.osm1 = new olLayer.Tile({
        visible : false,
        source: new olSource.XYZ({
          url : 'https://{a-c}.tile.openstreetmap.org/{z}/{x}/{y}.png'
        })
      });
      console.log('layer_osm', this.layers.osm2);

      //////////////////////////////////////////////////////////
      this.layers.bing =  new olLayer.Tile({
          visible : false,
          source: new olSource.BingMaps({
            key : 'ArbsA9NX-AZmebC6VyXAnDqjXk6mo2wGCmeYM8EwyDaxKfQhUYyk0jtx6hX5fpMn',
            imagerySet: ['AerialWithLabels'],
          })
      });
      console.log('layer_bing', this.layers.bing);


      //////////////////////////////////////////////////////////
      this.layers.google = new olLayer.Tile({
        visible : false,
        source: new olSource.XYZ({
          url : 'http://mt0.google.com/vt/lyrs=m&hl=en&x={x}&y={y}&z={z}',
          attributions : '<a href="https://www.google.at/permissions/geoguidelines/attr-guide.html">Map data ©2015 Google</a>'
        })
      });
      console.log('layer_google', this.layers.google);

      //////////////////////////////////////////////////////////
      this.layers.vworld = new olLayer.Tile({
        visible : false,
        source: new olSource.XYZ({
            url : 'http://xdworld.vworld.kr:8080/2d/Base/service/{z}/{x}/{y}.png',
            attributions : '<a href="http://map.vworld.kr">vWorld 공간정보오픈플랫폼</a>'
        })
      });
      console.log('layer_vworld', this.layers.vworld);


      //////////////////////////////////////////////////////////
      this.layers.vworld_wms = new olLayer.Tile({
        
        visible : false,
        opacity : 0.5, 

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
      console.log('layer_vworld_wms', this.layers.vworld_wms);


      //////////////////////////////////////////////////////////
      this.layers.geoserver_tiger_image = new olLayer.Image({
        visible: false,
        source: new olSource.ImageWMS({
          url: 'http://localhost:9099/geoserver/wms',
          params: {
              'FORMAT': 'image/png',
              'VERSION': '1.1.1',  
              'LAYERS': 'tiger:tiger_roads',
              'exceptions': 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log('layer_geoserver_tiger_image', this.layers.geoserver_tiger_image);


      this.layers.geoserver_tiger_tile = new olLayer.Tile({
        visible: false,
        source: new olSource.TileWMS({
          url: 'http://localhost:9099/geoserver/wms',
          params: {
                'FORMAT': 'image/png', 
                'VERSION': '1.1.1',
                tiled: true,
                "LAYERS": 'tiger:tiger_roads',
                "exceptions": 'application/vnd.ogc.se_inimage',
                //BGCOLOR :'0xFFFFFF', //with opacity
            // tilesOrigin: -74.047185 + "," + 40.679648
          }
        })
      });
      console.log('layer_geoserver_tiger_tile', this.layers.geoserver_tiger_tile);


      //////////////////////////////////////////////////////////
      this.layers.geoserver_solar_image = new olLayer.Image({
        visible: false,
        source: new olSource.ImageWMS({
          url: 'http://localhost:9099/geoserver/wms',
          params: {
              'FORMAT': 'image/png',
              'VERSION': '1.1.1',  
              "LAYERS": 'solar_module:hongsung_base_geotiff',
              'exceptions': 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log('layer_geoserver_solar_image', this.layers.geoserver_solar_image);


      this.layers.geoserver_solar_tile = new olLayer.Tile({
        className : 'layer_geoserver_solar_tile',
        visible: false,
        source: new olSource.TileWMS({
        //   crossOrigin: 'anonymous'
          url: 'http://localhost:9099/geoserver/wms',
          params: {
                'FORMAT': 'image/png', 
                'VERSION': '1.1.1',
                tiled: true,
                "LAYERS": 'solar_module:hongsung_base_geotiff',
                "exceptions": 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log('layer_geoserver_solar_tile', this.layers.geoserver_solar_tile);


      //////////////////////////////////////////////////////////
      this.layers.geoserver_panel_tile = new olLayer.Tile({
        className : 'layer_geoserver_panel_tile',
        visible: false,
        source: new olSource.TileWMS({
        //   crossOrigin: 'anonymous'
          url: 'http://localhost:9099/geoserver/wms',
          params: {
                'FORMAT': 'image/png', 
                'VERSION': '1.1.1',
                tiled: true,
                "LAYERS": 'solar_module:hongsung_panel_postgis_view',
                "exceptions": 'application/vnd.ogc.se_inimage',
          }
        })
      });
      console.log('layer_geoserver_panel_tile', this.layers.geoserver_panel_tile);

			// this.layers.push(new olLayer.Vector(this.$context.vectors.solar_module));



			// this.layers.push(new olLayer.Vector({source : new olSource.Vector({format: new olFormat.GeoJSON()})}));

      //////////////////////////////////////////////////////////
      this.layers.geoserver_panel_vector = new olLayer.Vector({
          visible: false,
          source: new olSource.Vector({
                format: new olFormat.GeoJSON(),
                url : 'http://localhost:9099/geoserver/wfs'
                    +'?service=WFS'
                    +'&version=1.1.1'
                    +'&request=GetFeature'
                    +'&typename=solar_module:hongsung_panel_postgis_view'
                    +'&outputFormat=application/json'
                    +'&exceptions=application/json'
                    +'&srsname=EPSG:4326'
          }),
         style: new olStyle.Style({
            fill: new olStyle.Fill({
              color: [255, 255, 255, 0.1]
            }),
             stroke: new olStyle.Stroke({
                    color: 'gray',
                    width: 0.5
                })
            })
      });

     console.log('layer_geoserver_panel_vector', this.layers.geoserver_panel_vector);


      //////////////////////////////////////////////////////////
    this.layers.geojson = new olLayer.Vector(
      {
        style: (feature)=>{
          return geojson_style_sample[feature.getGeometry().getType()];
        },
        source: new olSource.Vector({
          features: (new olFormat.GeoJSON()).readFeatures(geojson_data_sample)
        })
      });

console.log('layer_geo_json_vector', this.layers.geojson);
      //////////////////////////////////////////////////////////
      //
      /////////////////////////////////////////////////////////
      this.map = new ol.Map({
        target: 'map',
        layers: [
          this.layers.osm1,
          this.layers.osm2,
          this.layers.bing,
          this.layers.google,
          this.layers.vworld,
          this.layers.vworld_wms,
          this.layers.geoserver_tiger_image,
          this.layers.geoserver_tiger_tile,
          this.layers.geoserver_solar_image,
          this.layers.geoserver_solar_tile,
          this.layers.geoserver_panel_tile,
          this.layers.geoserver_panel_vector,
          this.layers.geojson
        ],
        view: new ol.View({
          projection: this.projection,
          center: this.center,
          zoom: this.zoom
        }),
      });
      console.log(this.map);


      this.map.on('change', this.change);
      this.map.on('change:layerGroup', this.change_layerGroup);
      this.map.on('change:size', this.change_size);
      this.map.on('change:target', this.change_target);
      this.map.on('change:view', this.change_view);
      this.map.on('click', this.click);
      this.map.on('dblclick', this.dblclick);
      this.map.on('error', this.error);
      this.map.on('moveend', this.moveend);
      this.map.on('movestart', this.movestart);
      this.map.on('pointerdrag', this.pointerdrag);
      this.map.on('pointermove', this.pointermove);
      this.map.on('postcompose', this.postcompose);
      this.map.on('postrender', this.postrender);
      this.map.on('precompose', this.precompose);
      this.map.on('propertychange', this.propertychange);
      this.map.on('rendercomplete',this.rendercomplete);
      this.map.on('singleclick',this.singleclick);
    },

    watch : {
      toggle_layer(newVal) { 

        console.log(newVal);

        Object.keys(this.layers).forEach(k=>{
          console.log(k);
          this.layers[k].setVisible(newVal.includes(k));
        });
      },
    },	

    methods : {

      change(){},
      change_layerGroup(){},
      change_size(){},
      change_target(){},
      change_view(){},
      click(){},
      dblclick(){},
      error(){},
      moveend(){
        // this.print(evt)
      },
      movestart(){},
      pointerdrag(){},
      pointermove(){},
      postcompose(){},
      postrender(){},
      precompose(){},
      propertychange(){},
      rendercomplete(){},
      singleclick(evt){
        this.select(evt)
      },


      print(evt){
        var map = evt.map;

        var center = map.getView().getCenter();
        console.log('center', center);

        var extent = map.getView().calculateExtent(map.getSize());
        // var extentBottomLeft = olProj.toLonLat(olExtent.getBottomLeft(extent));
        // var extentTopRight = olProj.toLonLat(olExtent.getTopRight(extent));
        var extentCenter = olProj.toLonLat(olExtent.getCenter(extent));
        console.log('extentCenter', extentCenter);
      },
      select(evt){


        evt.map.forEachFeatureAtPixel(evt.pixel, (feature, layer) => {
          console.log('forEachFeatureAtPixel', feature, layer);
        });

        var layer = evt.map.getLayers().getArray()[10];

        var coordinate = evt.coordinate;
        var resolution = evt.map.getView().getResolution();
        var projection= evt.map.getView().getProjection();
        var params = {'INFO_FORMAT': 'application/json', 'FEATURE_COUNT': 50};

        // console.log('layer', layer);
        // console.log('coordinate', coordinate);
        // console.log('resolution', resolution);
        // console.log('projection', projection);
        // console.log('params', params);


        var url = layer.getSource().getFeatureInfoUrl(coordinate, resolution, projection, params);
        if(url){
            return this.$axios.get(url).then(r=>{
              console.log('getFeatureInfoUrl', r.data.features);
            });
        }

      

      }
    }

}
</script>

<style>

</style>