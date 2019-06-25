<template>
    <div></div>
</template>

<script>
import {Authentication} from './Authentication.js'

export default {
    name : 'Oauth2Callback',
    data: () => ({

    }),
    methods : {

    },
    created: function() {
        this.$log.debug(this.$options.name, 'created');
    },
    mounted: function() {
        this.$log.debug(this.$options.name, 'mounted', this.$route.query);
        
        this.$authentication = Authentication;
        
        this.$authentication.save(this.$route.query);
        this.$router.push('/');
    },
    updated: function() {
        this.$log.debug(this.$options.name, 'updated');
    },
    destroyed: function() {
        this.$log.debug(this.$options.name, 'destroyed');
    },
}
</script>

<style>

</style>
