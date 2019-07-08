<template>
    <div>{{query}}</div>
</template>

<script>
import {Authentication} from './Authentication.js'

export default {
    name : 'Oauth2Callback',
    data: () => ({
        query : null,
        authServer  : 'http://localhost:9091',
        resourceServer  : 'http://localhost:9092',
    }),
    methods : {
        
        info(){
            const auth = this.$authentication.load();
            this.$log.debug(this.$options.name, 'info', auth);

            this.$axios({
                method : 'get',
                url : this.resourceServer+'/user/info',
                headers : {
                    'Authorization': 'Bearer '+auth.idToken
                }
            }).then((result1) => {
                this.$log.debug(this.$options.name, 'info', result1);

            }).catch((error1) => {
                this.$log.debug(this.$options.name, 'info', error1);
            });
        },
    },
    created: function() {
        this.$log.debug(this.$options.name, 'created');
    },
    mounted: function() {
        this.$log.debug(this.$options.name, 'mounted', this.$route.query);
        this.query = this.$route.query;

        this.$authentication = Authentication;
        this.$authentication.save(this.$route.query);
        
        this.$router.push('/');
        // or
        // this.info();
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
