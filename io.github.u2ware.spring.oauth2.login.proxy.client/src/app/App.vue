<template>
  <div>
    <pre>{{oauth2Result}}</pre>
    <v-list>
        <v-list-item two-line v-for="o in oauth2" :key="o.id">
            <v-list-item-content>
                <v-list-item-title>
                    <v-btn v-if="o.name != oauth2Result.client_registration_name" @click="login(o)">{{o.name}} Login</v-btn>
                    <v-btn v-if="o.name == oauth2Result.client_registration_name" @click="userInfo1Request()">User Info By {{o.name}} </v-btn>
                    <v-btn v-if="o.name == oauth2Result.client_registration_name" @click="userInfo2Request()">User Info with access token</v-btn>
                    <v-btn v-if="o.name == oauth2Result.client_registration_name" @click="userInfo3Request()">User Info with JWT</v-btn>
                    <v-btn v-if="o.name == oauth2Result.client_registration_name" @click="userInfo4Request()">User Info By Resource Server</v-btn>
                    <v-btn v-if="o.name == oauth2Result.client_registration_name" @click="logout()">{{o.name}} Logout</v-btn>
                </v-list-item-title>
                <v-list-item-subtitle>{{o.uri}}</v-list-item-subtitle>
            </v-list-item-content>
        </v-list-item>
    </v-list>
    <table border="2">
        <tr>
        <td>userInfo1</td>
        <td><pre>{{userInfo1}}</pre></td>
        </tr>
        <tr>
        <td>userInfo2</td>
        <td><pre>{{userInfo2}}</pre></td>
        </tr>
        <tr>
        <td>userInfo3</td>
        <td><pre>{{userInfo3}}</pre></td>
        </tr>
        <tr>
        <td>userInfo4</td>
        <td><pre>{{userInfo4}}</pre></td>
        </tr>
    </table>

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
        oauth2Result : {},
        userInfo1 : null,
        userInfo2 : null,
        userInfo3 : null,
        userInfo4 : null,
    }),
    methods : {

        init(){
            axios({
				url : 'http://localhost:9091/oauth2/login',
			}).then(r => {
                console.log(r);
                this.oauth2 = r.data;
            });
        },

        login(oauth){
            console.log(oauth);
            var moveTo = oauth.uri + (oauth.enabled == false ? '' : 'http://localhost:8080');
            // var moveTo = oauth.uri + (oauth.enabled == false ? '' : '');
            console.log(moveTo);
            window.location.href = moveTo;
        },

        userInfo1Request(){
            axios({
                url : this.oauth2Result.access_token_user_info,                                         //Provided Authorization Server
                headers : {
                    'Authorization' : this.oauth2Result.token_type+' '+this.oauth2Result.access_token , //ACCESS_TOKEN
                }
            }).then(r => {
                console.log(r);
                this.userInfo1 = r.data;
            });
        },
        userInfo2Request(){
            axios({
                url : this.oauth2Result.id_token_user_info+'/'+this.oauth2Result.client_registration_id, //Authorization Server
                headers : {
                    'Authorization' : this.oauth2Result.token_type+' '+this.oauth2Result.access_token , //ACCESS_TOKEN
                }
            }).then(r => {
                console.log(r);
                this.userInfo2 = r.data;
            });
        },
        userInfo3Request(){
            axios({
                url : this.oauth2Result.id_token_user_info,                   
                headers : {
                    'Authorization' : this.oauth2Result.token_type+' '+this.oauth2Result.id_token  //JWT
                }
            }).then(r => {
                console.log(r);
                this.userInfo3 = r.data;
            });
        },
        userInfo4Request(){
            axios({
                url : 'http://localhost:9092/user',                    //Resource Server
                headers : {
                    'Authorization' : 'Bearer '+this.oauth2Result.id_token  //JWT
                }
            }).then(r => {
                console.log(r);
                this.userInfo4 = r.data;
            });
        },

        logout(){
            axios({
				url : 'http://localhost:9091/oauth2/logout',
			}).then(r => {
                console.log(r);
                window.location.href = 'http://localhost:8080';
            });
        }
    },
    mounted(){
        const queryParam = this.$route.query;
        this.oauth2Result = queryParam;
        this.init();
    }
}
</script>

<style>

</style>