<template>
<div>
    <p>
        <a class="btn btn-block btn-social btn-facebook" href="http://localhost:9091/login/facebook?callback=http://localhost:8080/oauth2/callback">
            <span class="fa fa-facebook"></span>facebook
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-github" href="http://localhost:9091/login/github?callback=http://localhost:8080/oauth2/callback">
            <span class="fa fa-github"></span>github
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-google" href="http://localhost:9091/login/google?callback=http://localhost:8080/oauth2/callback">
            <span class="fa fa-google"></span>google
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-kakao" href="http://localhost:9091/login/kakao?callback=http://localhost:8080/oauth2/callback">
            <span class="fa fa-kakao"></span>kakao
        </a>
    </p>
    <p>
        <a class="btn btn-block btn-social btn-naver" href="http://localhost:9091/login/naver?callback=http://localhost:8080/oauth2/callback">
            <span class="fa fa-naver"></span>naver
        </a>
    </p>
</div>

</template>

<script>
export default {
    name : "OAuth2",

    methods : {
    }

}
</script>

<style>

</style>
