<template>
<div>
    <p>
        <button @click="logout">logout</button>
    </p>
    <p>
        <button @click="info">info</button>
    </p>
    <p>
        <pre>{{auth}}</pre>
    </p>
</div>
</template>

<script>
export default {
    name : 'Oauth2Intro',
    data: () => ({
        auth  : null
    }),
    methods : {

        logout(){
            this.$log.debug(this.$options.name, 'logout');

            this.$axios({
                method : 'get',
                //url : 'http://localhost:9092/index',
                url : 'http://localhost:9091/logout/'+localStorage.clientRegistrationId,
                headers : {
                    'Authorization': localStorage.principalName
                }

            }).then((result) => {
                this.$log.debug(this.$options.name, 'logout', result);
                this.auth = result.data;
            }).catch((error) => {
                this.$log.debug(this.$options.name, 'logout', error);
                this.auth = error;
            })


        },
        info(){
            this.$log.debug(this.$options.name, 'info');

            this.$axios({
                method : 'get',
                //url : 'http://localhost:9092/index',
                url : 'http://localhost:9091/info/'+localStorage.clientRegistrationId,
                headers : {
                    'Authorization': localStorage.principalName
                }

            }).then((result) => {
                this.$log.debug(this.$options.name, 'info', result);
                this.auth = result.data;
            }).catch((error) => {
                this.$log.debug(this.$options.name, 'info', error);
                this.auth = error;
            })

        }
    },
    created: function() {
        this.$log.debug(this.$options.name, 'created');
    },
    mounted: function() {
        this.$log.debug(this.$options.name, 'mounted');
        this.$log.debug(this.$options.name, localStorage.oauth2);
        this.$log.debug(this.$options.name, localStorage.oauth2.clientRegistrationId);
        this.$log.debug(this.$options.name, localStorage.token);
        this.$log.debug(this.$options.name, localStorage.jwtToken);
        this.$log.debug(this.$options.name, localStorage.clientRegistrationId);
        this.$log.debug(this.$options.name, localStorage.principalName);
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
