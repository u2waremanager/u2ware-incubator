<template>
  <v-app id="inspire">
    <v-content>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>


          <v-flex xs12>
            <v-card class="elevation-12">
              <v-toolbar dark color="primary">
                <v-toolbar-title>OAuth2 Login Server</v-toolbar-title>
                <v-spacer></v-spacer>
              </v-toolbar>
              
              <v-card-text v-if="! isAuthenticated()">
                <v-btn v-for="client in clients" :key="client.name" :small="true" color="info" @click="login(client)">
                    {{client.clientName}}
                </v-btn>
              </v-card-text>
              
              <v-card-text v-if="isAuthenticated()">
                <v-btn :small="true" color="primary" @click="idTokenInfo">id Token User Info</v-btn>
                <v-btn :small="true" color="primary" @click="accessTokenInfo">Access Token User Info</v-btn>
                <v-btn :small="true" color="primary" @click="logout">logout</v-btn>
              </v-card-text>

              <v-card-actions>
                <pre>{{userInfo}}</pre>
              </v-card-actions>

            </v-card>

          </v-flex>



        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>



<script>

export default {
    name : 'OAuth2Login',

    data: () => ({
        userInfo : null,

        clients : [],

        oauth2AuthorizationServer : 'http://localhost:9093',
        oauth2ResourceServer      : 'http://localhost:9092',
        oauth2LoginServer      : 'http://localhost:9091',
    }),

    methods : {

        clientRegistrations(){
            this.$axios({
                method : 'get',
                url : this.oauth2LoginServer+'/token/clientRegistrations',

            }).then((result) => {
                this.$log.debug(this.$options.name, 'clientRegistrations', result);
                this.clients = result.data;

            }).catch((error) => {
                this.$log.debug(this.$options.name, 'clientRegistrations', error);
            })
        },

        isAuthenticated(){
            return this.$authentication.isAuthenticated();
        },


        accessTokenInfo(){

            const auth = this.$authentication.load();

            this.clients.forEach(client => {
                if(client.clientRegistrationId == auth.clientRegistrationId){


                    this.$log.debug(this.$options.name, 'accessTokenInfo', client, auth);
                    this.$log.debug(this.$options.name, 'accessTokenInfo', client, auth.accessToken);

                    alert(client.userInfoUri);

                    this.$axios({
                        method : 'get',
                        url : client.userInfoUri,
                        headers : {
                            'Authorization': "Bearer "+auth.accessToken,
                            // 'Access-Control-Allow-Origin' : 'GET'
                        }

                    }).then((result) => {
                        this.$log.debug(this.$options.name, 'accessTokenInfo', result);
                        this.userInfo = result.data;

                    }).catch((error) => {
                        this.$log.debug(this.$options.name, 'accessTokenInfo', error);
                        this.userInfo = {};
                    })
                }
            });


        },


        idTokenInfo(){
            const auth = this.$authentication.load();
            this.userInfo = {};


            alert(this.oauth2LoginServer+"/user/info");

            this.$axios({
                method : 'get',
                url : this.oauth2LoginServer+"/user/info",
                headers : {
                    'Authorization': "Bearer "+auth.idToken
                }

            }).then((result) => {
                this.$log.debug(this.$options.name, 'idTokenInfo', result);
                this.userInfo = result.data;

            }).catch((error) => {
                this.$log.debug(this.$options.name, 'idTokenInfo', error);

                this.userInfo = {};
            })
        },

        login(client){
            var url = client.authorizationUri+'?callback_uri=http://localhost:8080/callback';
            alert(url);
            window.location.href = url;
        },

        logout(){
            this.message = {};
            this.userInfo = {};
            this.$authentication.clear();
        },
    },

    created(){
        this.$log.debug(this.$options.name, 'created');
    },
    
    mounted(){
        this.$log.debug(this.$options.name, 'mounted');
        this.clientRegistrations();
    }
}
</script>

<style>

</style>
