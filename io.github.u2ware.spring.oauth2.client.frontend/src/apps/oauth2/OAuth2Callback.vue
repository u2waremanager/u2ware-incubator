<template>
    <div>Callback....</div>
</template>

<script>
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
        this.$log.debug(this.$options.name, 'mounted');
        this.$log.debug(this.$options.name, this.$route.query);
        
        localStorage.oauth2 = this.$route.query;
        localStorage.token = this.$route.query.token;
        localStorage.jwtToken = this.$route.query.jwtToken;
        localStorage.clientRegistrationId = this.$route.query.clientRegistrationId;
        localStorage.principalName = this.$route.query.principalName;

        this.$router.push('/oauth2/logon');
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
