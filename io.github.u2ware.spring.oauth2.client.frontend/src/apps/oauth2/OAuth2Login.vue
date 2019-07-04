<template>
<div>
    <div v-if="! isAuthenticated">
        <p>
            <a class="btn btn-block btn-social btn-facebook" @click="login('facebook')">
                <span class="fa fa-facebook"></span>facebook
            </a>
        </p>
        <p>
            <a class="btn btn-block btn-social btn-github" @click="login('github')">
                <span class="fa fa-github"></span>github
            </a>
        </p>
        <p>
            <a class="btn btn-block btn-social btn-google" @click="login('google')">
                <span class="fa fa-google"></span>google
            </a>
        </p>
        <p>
            <a class="btn btn-block btn-social btn-kakao"  @click="login('kakao')">
                <span class="fa fa-kakao"></span>kakao
            </a>
        </p>
        <p>
            <a class="btn btn-block btn-social btn-naver" @click="login('naver')">
                <span class="fa fa-naver"></span>naver
            </a>
        </p>
        <p>
            <a class="btn btn-block btn-social btn-hello" @click="login('hello')">
                <span class="fa fa-hello"></span>hello
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
        auth2Server  : 'http://localhost:9091',
        // auth2Server  : 'http://devapi.hi-class.io:19081',
        callback : 'http://localhost:8080/callback',
    }),

    methods : {

        info(){

            const auth = this.$authentication.load();

            this.$axios({
                method : 'get',
                url : this.auth2Server + '/info/'+auth.clientRegistrationId,
                headers : {
                    'Authorization': auth.principalName
                }

            }).then((result) => {
                this.$log.debug(this.$options.name, 'info', result);
                this.isAuthenticated = true;
                this.message = result.data;

            }).catch((error) => {
                this.$log.debug(this.$options.name, 'info', error);
                this.isAuthenticated = false;
                this.message = error;

                this.$authentication.clear();
            })
        },

        login(id){




            var url = this.auth2Server+'/login/'+id+'?callback_uri='+this.callback;
            window.location.href = url;
        },

        logout(){

            const auth = this.$authentication.load();
            
            this.$axios({
                method : 'get',
                url : this.auth2Server + '/info/'+auth.clientRegistrationId,
                headers : {
                    'Authorization': auth.principalName
                }

            }).then((result) => {
                this.$log.debug(this.$options.name, 'logout', result);
                this.isAuthenticated = false;
                this.message = result.data;

                this.$authentication.clear();

            }).catch((error) => {
                this.$log.debug(this.$options.name, 'logout', error);
                this.isAuthenticated = false;
                this.message = error;

                this.$authentication.clear();
            })
        },
    },

    mounted(){
        this.$log.debug(this.$options.name, 'created');
        
        this.$authentication = Authentication;

        //this.info();
    }
}
</script>

<style>

</style>
