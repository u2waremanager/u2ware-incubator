<template>
    <div>

        <b>vue-cli-service serve --mode blingbling</b>

<pre>{{env}}</pre>
    </div>
  
</template>

<script>
export default {
    
    data : ()=>({
        env : process.env
    }),

    mounted(){
        console.log( process.env);
    }
}
</script>

<style>

</style>