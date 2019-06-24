<template>
<div>
    <p>
        <a class="btn btn-block btn-social btn-facebook" v-bind:href="auth2Server+'/login/facebook?callback='+callback">
            <span class="fa fa-facebook"></span>facebook
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-github" v-bind:href="auth2Server+'/login/github?callback='+callback">
            <span class="fa fa-github"></span>github
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-google" v-bind:href="auth2Server+'/login/google?callback='+callback">
            <span class="fa fa-google"></span>google
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-kakao"  v-bind:href="auth2Server+'/login/kakao?callback='+callback">
            <span class="fa fa-kakao"></span>kakao
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-naver" v-bind:href="auth2Server+'/login/naver?callback='+callback">
            <span class="fa fa-naver"></span>naver
        </a>
    </p>
</div>

</template>

<script>
export default {
    name : "OAuth2",

    data: () => ({
        // auth2Server  : 'http://localhost:9092',
        auth2Server  : 'http://devapi.hi-class.io:19081',

        callback : 'http://localhost:8080/oauth2/callback'
    }),

    methods : {
    }

}
</script>

<style>

</style>
