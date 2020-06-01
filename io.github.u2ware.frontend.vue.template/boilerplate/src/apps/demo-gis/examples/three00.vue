<template>
 <div id="map" style="width:600px;height:400px;"></div>
</template>

<script>
import * as THREE from 'three';
import Stats from 'three/examples/jsm/libs/stats.module.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader.js';

// var container, stats, controls, light;
// var camera, scene, renderer ;
            

export default {

    data: () => ({
      map : undefined,
      container: undefined,
      stats: undefined,
      controls: undefined,
      light: undefined,
      scene: undefined,
      renderer: undefined,
    }),

    methods: {



        // init(){

        // },
        animate() {

            requestAnimationFrame( this.animate );

            this.renderer.render( this.scene, this.camera );

            this.stats.update();
        }


    },


    mounted(){
				// this.container = document.createElement( 'div' );
                // document.body.appendChild( this.container );
                
                this.container = document.getElementById( 'map' );


				this.camera = new THREE.PerspectiveCamera( 45, window.innerWidth / window.innerHeight, 1, 2000 );
				this.camera.position.set( 2, 18, 28 );

				this.scene = new THREE.Scene();
                this.scene.background = new THREE.Color( 'white' );

				// this.light = new THREE.HemisphereLight( 0xffffff, 0x444444 );
				// this.light.position.set( 0, 1, 0 );
				// this.scene.add( this.light );

				// this.light = new THREE.DirectionalLight( 0xffffff );
				// this.light.position.set( 0, 1, 0 );
                // this.scene.add( this.light );
                
				this.light = new THREE.HemisphereLight( 0x444444, 0x444444, 2 );
				this.light.position.set( 0, 200, 0 );
				this.scene.add( this.light );


				this.light = new THREE.AmbientLight( 0x444444, 4); 
				this.light.position.set( 0, 200, 0 );
				this.scene.add( this.light );


				this.light = new THREE.DirectionalLight( 0x444444, 4 );
				this.light.position.set( 0, 200, 100 );
				// this.light.castShadow = true;
				// this.light.shadow.camera.top = 180;
				// this.light.shadow.camera.bottom = - 100;
				// this.light.shadow.camera.left = - 120;
				// this.light.shadow.camera.right = 120;
				this.scene.add( this.light );

				console.log(this.light);
				console.log(this.light.ambientIntensity);
				console.log(this.light.exposure);


// ambientIntensity
// exposure








				// grid
				var gridHelper = new THREE.GridHelper( 28, 28, 0x303030, 0x303030 );
				this.scene.add( gridHelper );

				// stats
				this.stats = new Stats();
				this.container.appendChild( this.stats.dom );

				// model
				var loader = new FBXLoader();
				// loader.load( '/A200205_deaduck_bridge_p4pr_main_b4_simplified_3d_mesh.fbx', function ( object ) {
                loader.load( '/daegu_mer_0920_1_simplified_3d_mesh.fbx', function ( object ) {

				// loader.load( '/nurbs.fbx',  ( object ) =>{


                    console.log(object);
                    console.log(object);
                  

					this.scene.add( object );

				} );

				this.renderer = new THREE.WebGLRenderer({antialias: true});
				this.renderer.setPixelRatio( window.devicePixelRatio );
				this.renderer.setSize( 640, 480);
				this.renderer.physicallyCorrectLights = true;
				this.renderer.gammaOutput = true;
				this.renderer.gammaFactor = 2.2;
				this.renderer.toneMappingExposure = 2;






				this.container.appendChild( this.renderer.domElement );

				this.controls = new OrbitControls( this.camera, this.renderer.domElement );
				this.controls.target.set( 0, 12, 0 );
                this.controls.update();
                

                this.renderer.render( this.scene, this.camera );

                this.stats.update();
                
                this.animate();
    }

}
</script>

<style>

</style>