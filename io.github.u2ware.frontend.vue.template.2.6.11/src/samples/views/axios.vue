<template>
<div>
    <pre>{{r1}}</pre>
    <hr/>
    <pre>{{r2}}</pre>
</div>
</template>

<script>
import $$axios from "axios";

export default {

    data: () => ({
        r1 : null,
        r2 : null,
    }),

    mounted(){

        this.$axios({url : '/'}).then(r=>{
            this.r1 = r;
        });

        $$axios({url : '/'}).then(r=>{
            this.r2 = r;
        })
    }

}
</script>

<style>

</style>