<template>
    <Parent></Parent>
</template>

<script>
import Parent from './components/Parent.vue';
// import Routes from '../DemoRoutes.js';

export default {

    components : {
        Parent
    },

    mounted: function (){

        this.$log.debug(this.$router);
    }
}
</script>

<style>

</style>
