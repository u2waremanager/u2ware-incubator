<template>
<div>
    <div v-if="! isAuthenticated">
        <p v-for="client in clients">
            <a @click="login(client.uri)">
                {{client.name}}
            </a>
        </p>
    </div>

    <div v-if="isAuthenticated">
        <p>
            <button @click="logout">logout</button>
        </p>
        <p>
            <button @click="info">info</button>
        </p>
    </div>
    <p>
        <pre>{{message}}</pre>
    </p>

</div>


</template>




<script>
import {Authentication} from './Authentication.js'

export default {
    name : 'OAuth2Login',

    data: () => ({
        isAuthenticated : false,
        message : null,

        clients : [],

        authServer  : 'http://localhost:9091',
        authCallback : 'http://localhost:8080/callback',
        resourceServer  : 'http://localhost:9092',
    }),

    methods : {

        clientRegistrations(){
            this.$axios({
                method : 'get',
                url : this.authServer + '/token/clientRegistrations',

            }).then((result) => {
                this.$log.debug(this.$options.name, 'clientRegistrations', result);
                this.clients = result.data;

            }).catch((error) => {
                this.$log.debug(this.$options.name, 'clientRegistrations', error);
            })
        },


        info(){
            const auth = this.$authentication.load();

            this.$axios({
                method : 'get',
                url : this.resourceServer + '/user/info',
                headers : {
                    'Authorization': "Bearer "+auth.idToken
                }

            }).then((result) => {
                this.$log.debug(this.$options.name, 'info', result);
                this.message = result.data;

                this.isAuthenticated = true;

            }).catch((error) => {
                this.$log.debug(this.$options.name, 'info', error);

                this.message = {};

                this.isAuthenticated = false;
                this.$authentication.clear();
            })
        },

        login(uri){
            var url = this.authServer+uri+'?callback_uri='+this.authCallback;

            alert(url);

            window.location.href = url;
        },

        logout(){
            this.message = {};
            this.isAuthenticated = false;
            this.$authentication.clear();
        },
    },

    created(){
        this.$authentication = Authentication;
        this.clientRegistrations();
    },
    mounted(){
        this.$log.debug(this.$options.name, 'created');
        this.info();
    }
}
</script>

<style>

</style>
