<template>
  <div id="app">
    <div id="nav">
      <router-link to="/demo-openlayer/example00">welcome</router-link> |
      <router-link to="/demo-openlayer/example01">osm</router-link> |
      <router-link to="/demo-openlayer/example02">bing</router-link> |
      <router-link to="/demo-openlayer/example03">google</router-link> |
      <router-link to="/demo-openlayer/example04">vWorld</router-link> |
      <router-link to="/demo-openlayer/example10">Openlayer - OSM</router-link> |
      <router-link to="/demo-openlayer/example11">Openlayer - GeoServer</router-link> |
      <router-link to="/demo-openlayer/example12">Openlayer - Kakao</router-link> |
    </div>

    <router-view/>
  </div>
</template>

<script>
import '../../plugins/openlayer'


export default {

}
</script>

<style>

</style>