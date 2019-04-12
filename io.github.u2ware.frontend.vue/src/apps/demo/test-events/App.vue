<template>
    <Parent></Parent>
</template>

<script>
import Parent from './components/Parent.vue';
import Routes from '../../../routes/routes.js';

export default {

    components : {
        Parent
    },

    mounted: function (){

        this.$log.debug(Routes);
    }
}
</script>

<style>

</style>
