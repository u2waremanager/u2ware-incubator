<template>
    <div>{{query}}</div>
</template>

<script>
import {Authentication} from './Authentication.js'

export default {
    name : 'Oauth2Callback',
    data: () => ({
        query : null
    }),
    methods : {
        nimbus(){

            this.$axios({
                method : 'get',
                url : 'http://localhost:9092/user/nfo',
                headers : {
                    'Authorization': 'Bearar '+result1.data
                }
            }).then((result2) => {
                this.$log.debug(this.$options.name, 'result2', result2);


            }).catch((error2) => {
                this.$log.debug(this.$options.name, 'error2', error2);

            });
        }
    },
    created: function() {
        this.$log.debug(this.$options.name, 'created');
    },
    mounted: function() {
        this.$log.debug(this.$options.name, 'mounted', this.$route.query);
        this.query = this.$route.query;

        this.$authentication = Authentication;
        
        this.$authentication.save(this.$route.query);
        
        //this.$router.push('/');
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
