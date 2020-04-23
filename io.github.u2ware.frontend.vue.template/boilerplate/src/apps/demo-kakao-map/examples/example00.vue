<template>
  <div id="map" style="width:300px; height:300px"></div>
</template>
<script> 
import loadScriptOnce from 'load-script-once';

export default { 
  data: () => ({
    map: null
  }),
  mounted() { 
          // console.log("loadScriptOnce", window);
          // console.log("loadScriptOnce", window.kakao);
          // console.log("loadScriptOnce", window.kakao.maps);
          // console.log("loadScriptOnce", kakao);
 window.kakao && window.kakao.maps ? this.initMap() : this.addScript(); 
  }, 
  methods : { 
    initMap() { 
      console.log("initMap", window);
      console.log("initMap", window.kakao);
      console.log("initMap", kakao);
      alert('initMap');
      var container = document.getElementById('map');
      var options = { center: new kakao.maps.LatLng(33.450701, 126.570667), level: 3 }; 
      var map = new kakao.maps.Map(container, options); 
      //마커추가하려면 객체를 아래와 같이 하나 만든다. 
      var marker = new kakao.maps.Marker({ position: map.getCenter() }); 
      marker.setMap(map); 
    }, 
    
    /* global kakao */ 
    addScript() { 
      alert('addScript');

      loadScriptOnce('http://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b').then(() => {

        alert('addScript1');
        kakao.maps.load(()=>{

          alert('addScript2');

          // <script src="http://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b"></script>


          console.log("loadScriptOnce1", window);
          console.log("loadScriptOnce2", window.kakao);
          console.log("loadScriptOnce3", window.kakao.maps);
          console.log("loadScriptOnce4", window.kakao.maps.Map);
          console.log("loadScriptOnce5", kakao);
          console.log("loadScriptOnce6", kakao.maps);
          console.log("loadScriptOnce7", kakao.maps.Map);

          // this.$emit('load', this.map);
        });


      }).catch(err => {
        console.error('failed to load!', err);
      });

      // let script = document.createElement('script'); 
      // /* global kakao */ 
      // script.onload = () => kakao.maps.load(this.initMap); 
      // script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b'; 
      // document.head.appendChild(script); 
    } 
  }
} 




</script>