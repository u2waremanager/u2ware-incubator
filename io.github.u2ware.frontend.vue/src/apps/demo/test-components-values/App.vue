<template>
    <Values v-model="aa"></Values>
</template>

<script>
import Values from './components/Values.vue';

export default {

    data: () => ({
        
        aa : 'bbbbb'
    
    }),

    components : {
        Values
    },

    mounted: function (){

        this.$log.debug(this.$router);
    },
    updated: function (){
        this.$log.debug(this.aa);
    }
}
</script>

<style>

</style>
