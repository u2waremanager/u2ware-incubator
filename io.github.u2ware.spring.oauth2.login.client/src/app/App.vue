<template>
  <div>
      <v-btn @click="initOAuth2">Oauth2 Load</v-btn>
        <pre>{{oauth2Result}}</pre>
    <v-list>
        <v-list-item two-line v-for="o in oauth2" :key="o.id">
            <v-list-item-content>
                <v-list-item-title>
                    <v-btn v-if="o.name != oauth2Result.client_registration_name" @click="loginOAuth2(o)">{{o.name}}</v-btn>
                    <v-btn v-if="o.name == oauth2Result.client_registration_name" @click="resourceOAuth2(o.uri)">{{o.name}} Resource</v-btn>
                </v-list-item-title>
                <v-list-item-subtitle>{{o.uri}}</v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>
    </v-list>

  </div>
</template>

<script>
import axios from 'axios'
// import qs from "qs";

export default {

    data : ()=>({
        oauth2 : [
            {
                name : 'GitHub',
                uri : 'http://localhost:9091/oauth2/authorization/github',
                enabled : false
            },
            {
                name : 'Google',
                uri : 'http://localhost:9091/oauth2/authorization/google',
                enabled : false
            }
        ],
        oauth2Result : {}
    }),
    methods : {

        initOAuth2(){

            axios({
				url : 'http://localhost:9091/oauth2',
			}).then(r => {
                console.log(r);
                this.oauth2 = r.data;
            });
        },

        loginOAuth2(oauth){
            console.log(oauth);
            var moveTo = oauth.uri + (oauth.enabled == false ? '' : window.location.href);
            console.log(moveTo);
            window.location.href = moveTo;
        },
        resourceOAuth2(){
            axios({
                url : 'http://localhost:9092/principal',                    //JWT
                // url : 'http://localhost:9092/authentication',               //JWT
                // url : this.oauth2Result.user_info_endpoint,                    //ACCESS_TOKEN
                headers : {
                    'Authorization' : 'Bearer '+this.oauth2Result.id_token  //JWT
                    // 'Authorization' : 'Bearer '+this.oauth2Result.access_token //ACCESS_TOKEN
                }
            }).then(r => {
                console.log(r);
            });

        }
    },
    mounted(){
        const queryParam = this.$route.query;
        this.oauth2Result = queryParam;
    }
}
</script>

<style>

</style>