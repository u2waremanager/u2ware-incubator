<template>
    <div>{{message}}</div>
</template>

<script>
export default {
    name : 'Oauth2Callback',
    data: () => ({
        message : "....",
        oauth2AuthorizationServer : 'http://localhost:9093',
        // oauth2ResourceServer      : 'http://localhost:9092',
        oauth2ResourceServer      : 'http://localhost:9091',
        oauth2LoginServer      : 'http://localhost:9091',
    }),
    methods : {

    },
    created: function() {
        this.$log.debug(this.$options.name, 'created');
    },
    mounted: function() {
        this.$log.debug(this.$options.name, 'mounted', this.$route.query);

        this.$axios({
            method : 'get',
            url : this.oauth2LoginServer+"/user/info",
            headers : {
                'Authorization': "Bearer "+this.$route.query.idToken
            }

        }).then((result) => {
            this.$log.debug(this.$options.name, 'info', result);
            this.$authentication.save(this.$route.query);
            this.$router.push('/');

        }).catch((error) => {
            this.$log.debug(this.$options.name, 'info', error);
            this.message = 'error';
        })

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
