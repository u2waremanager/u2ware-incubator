<template>
  <v-app id="inspire">


    <v-app-bar
      app
      clipped-left
      clipped-right
    >
      <v-app-bar-nav-icon @click.stop="drawer = !drawer" />
      <v-toolbar-title>Drone Service (Platform) Management System</v-toolbar-title>
      <v-spacer/>
      <v-app-bar-nav-icon @click.stop="drawerRight = !drawerRight" />

    </v-app-bar>

    <v-navigation-drawer
      v-model="drawer"
      app
      clipped
    >
      <v-list dense>
<!--         
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Dashboard</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
        
        <v-list-item link>
          <v-list-item-action>
            <v-icon>mdi-settings</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Settings</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
 -->

        <v-list-item>
          <v-list-item-action>
            <v-list-item-title>Lat</v-list-item-title>
          </v-list-item-action>
          <v-list-item-content>
            <v-text-field v-model="lat"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <v-list-item>
          <v-list-item-action>
            <v-list-item-title>Lng</v-list-item-title>
          </v-list-item-action>
          <v-list-item-content>
            <v-text-field v-model="lng"></v-text-field>
          </v-list-item-content>
        </v-list-item>

        <v-list-item>
          <v-list-item-content>
            <v-btn class="sm-1" outlined @click="reset()">Reset</v-btn>
            <v-btn class="sm-1" outlined @click="move()">Move</v-btn>
          </v-list-item-content>
        </v-list-item>

        <v-list-item>
          <v-list-item-action>
            <v-list-item-title>Detect</v-list-item-title>
          </v-list-item-action>
          <v-list-item-content>
            <v-checkbox
      v-model="detect"
      @change="option()"
    ></v-checkbox>

          </v-list-item-content>
        </v-list-item>




      </v-list>
    </v-navigation-drawer>


    <v-navigation-drawer
      v-model="drawerRight"
      app
      clipped
      right
    >
      <v-list dense>
        <v-list-item @click.stop="right = !right">
          <v-list-item-action>
            <v-icon>mdi-exit-to-app</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>Open Temporary Drawer</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>



    <v-content>
      <v-container
        class="fill-height"
        fluid
      >

      <kakao-map ref="map" :lat="lat" :lng="lng"></kakao-map>

      </v-container>
    </v-content>

    <v-footer app>
      <span>&copy; 2019</span>
    </v-footer>

    
  </v-app>
</template>

<script>
// import * as A from 'https://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b'

import KakaoMap from './KakaoMap.vue'

  export default {
    props: {
      source: String,
    },
    components : {
      KakaoMap
    },

    data: () => ({
      drawer: null,
      drawerRight: false,
      lat : "33.450701",
      lng : "126.570667",
      detect : 'true',
      flag : false
    }),

    methods : { 

      reset(){
        this.$refs.map.reset(this.lat, this.lng);
      },
      

      move(){
        this.$refs.map.move(this.lat, this.lng);
      },
      option(){
        this.$refs.map.option(this.detect);
      }      
    },

    created () {
      this.$vuetify.theme.dark = true;

      // if(window.kakao == undefined){
      //   const script = document.createElement('script'); 
      //   script.onload = () => {
      //     console.log(this.$options.name, "onload", window.kakao);
      //     this.$refs.map.reset(this.lat, this.lng);
      //   } 
      //   script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b'; 
      //   document.head.appendChild(script); 
      // }
    },
  }
</script>

