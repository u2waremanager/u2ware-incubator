<template>
<div>
<!--   
    <script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b"></script> 
    -->

    <div id="map" style="width:800px;height:600px;"></div>
<!-- 
    <div class="custom_zoomcontrol radius_border"> 
        <span onclick="zoomIn()"><img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/ico_plus.png" alt="확대"></span>  
        <span onclick="zoomOut()"><img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/ico_minus.png" alt="축소"></span>
    </div> -->
</div>
</template>

<script>

//declare var kakao: Any;

export default {
    name : 'KakaoMap',
    props: {
      lat: String,
      lng: String,
    },

    data: () => ({

      level : 3,
      map : null,
      marker: null,
      polyline : null,
      

      path : [],
      pathCircle : [],
      pathContent : [],
      pathDetect : true,
    }),

    methods : { 

      reset(x, y){

          console.log(this.$options.name, "reset", window.kakao);
          console.log(this.$options.name, "reset", window.kakao.maps);
          console.log(this.$options.name, "reset", window.kakao.maps.LatLng);

          alert('reset');

          this.path = [];
          this.pathCircle = [];
          this.pathOverlay = [];

          var lat = (x == undefined) ? this.lat : x;
          var lng = (y == undefined) ? this.lng : y;

          // point
          var point = new window.kakao.maps.LatLng(lat, lng);
          
          // map instance
          var container = document.getElementById('map');
          var options = { 
            center: point, 
            level: this.level //지도의 레벨(확대, 축소 정도)
          };
          this.map = new window.kakao.maps.Map(container, options); //지도 생성 및 객체 리턴
          this.map.addControl(new window.kakao.maps.ZoomControl(), window.kakao.maps.ControlPosition.RIGHT);

          // marker
          this.marker = new window.kakao.maps.Marker({ 
            image : new window.kakao.maps.MarkerImage( 
                'http://localhost:8080/drone.png', // 마커이미지의 주소입니다    
                new window.kakao.maps.Size(80, 80), // 마커이미지의 크기입니다
                {offset: new window.kakao.maps.Point(38, 70)}),

            position: this.map.getCenter() ,
            title : 'Drone',
            map : this.map
          }); 

          // polyline
          this.polyline = new window.kakao.maps.Polyline({
              path: this.path, // 선을 구성하는 좌표배열 입니다
              strokeWeight: 5, // 선의 두께 입니다
              strokeColor: '#FFAE00', // 선의 색깔입니다
              strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
              strokeStyle: 'solid', // 선의 스타일입니다,
              map : this.map
          });
          
         



          this.move(point);
      },

      option(pathDetect){
        this.pathDetect = pathDetect;
        if(this.pathDetect == true){
            var point = this.path[this.path.length -1];
            this.map.setLevel(this.level);
            this.map.panTo(point);  
        }
        this.map.relayout();
      },

      move(lat, lng, timestamp){

          var point = (lng == undefined) ? lat : new window.kakao.maps.LatLng(lat, lng);
          this.path.push(point);

          // marker
          this.marker.setPosition(point);

          // polyline
          this.polyline.setPath(this.path);
          if(this.pathDetect == true){
            this.map.setLevel(this.level);
            this.map.panTo(point);  
            // this.map.setCenter(moveLatLon);
          }

          // pathCircle
          this.pathCircle.push(new window.kakao.maps.CustomOverlay({
              content: '<span class="dot"></span>',
              position: point,
              zIndex: 1,
              map : this.map
          }));

          // pathContent
          point.custom_distance = Math.round(this.polyline.getLength());
          point.custom_timestamp = (timestamp == undefined) ? new Date() : timestamp;
          if(point.custom_distance > 0){
            this.pathContent.push(new window.kakao.maps.CustomOverlay({
                content: '<div class="dotOverlay">거리 <span class="number">' + point.custom_distance + '</span>m</div>',  // 커스텀오버레이에 표시할 내용입니다
                position: point, // 커스텀오버레이를 표시할 위치입니다.
                yAnchor: -1,
                zIndex: 2,
                map: this.map, // 커스텀오버레이를 표시할 지도입니다
            }));      
          }


// <div class="dotOverlay">거리 <span class="number">' + distance + '</span>m</div>
// <div class="dotOverlay distanceInfo">총거리 <span class="number">' + distance + '</span>m</div>
    // var content = '<ul class="dotOverlay distanceInfo">';
    // content += '    <li>';
    // content += '        <span class="label">총거리</span><span class="number">' + distance + '</span>m';
    // content += '    </li>';
    // content += '    <li>';
    // content += '        <span class="label">도보</span>' + walkHour + walkMin;
    // content += '    </li>';
    // content += '    <li>';
    // content += '        <span class="label">자전거</span>' + bycicleHour + bycicleMin;
    // content += '    </li>';
    // content += '</ul>'// ....
    //       this.map.relayout();
      },
    

      addScript() { 
        // alert('addScript');
        // const script = document.createElement('script'); 
        // script.onload = () => window.kakao.maps.load(()=>{

        //   console.log(this.$options.name, "reset", window.kakao);
        //   console.log(this.$options.name, "reset", window.kakao.maps);
        //   console.log(this.$options.name, "reset", window.kakao.maps.LatLng);


        // });
        // script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?appkey=9efdcf8e31739fcc8bb772dd463b813b'; 
        // document.head.appendChild(script); 
      } 
    },


    created: function() {
        console.log(this.$options.name, "created");
    },
    mounted: function() {
        console.log(this.$options.name, "mounted");
        window.kakao && window.kakao.maps ? this.reset(this.lat, this.lng) : this.addScript(); 
    },
    updated: function() {
        console.log(this.$options.name, "updated");
        if(this.map) this.map.relayout();
    },
    destroyed: function() {
        console.log(this.$options.name, "destroyed");
    }    
}
</script>

<style>
.dot {overflow:hidden;float:left;width:12px;height:12px;background: url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/mini_circle.png');}    
.dotOverlay {position:relative;bottom:10px;border-radius:6px;border: 1px solid #ccc;border-bottom:2px solid #ddd;float:left;font-size:12px;padding:5px;background:/*#fff;*/gray;}
.dotOverlay:nth-of-type(n) {border:0; box-shadow:0px 1px 2px #888;}    
.number {font-weight:bold;/*color:#ee6152;*/}
.dotOverlay:after {content:'';position:absolute;margin-left:-6px;left:50%;bottom:-8px;width:11px;height:8px;/* background:url('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white_small.png') */}
.distanceInfo {position:relative;top:5px;left:5px;list-style:none;margin:0;}
.distanceInfo .label {display:inline-block;width:50px;}
.distanceInfo:after {content:none;}
</style>