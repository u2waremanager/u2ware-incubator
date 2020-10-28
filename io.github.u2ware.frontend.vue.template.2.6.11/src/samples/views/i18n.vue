<template>
<div>
    <v-btn v-for="entry in $i18n.languages" :key="entry.title" @click="$i18n.changeLocale(entry.language)">
        <!-- <flag :iso="entry.flag" v-bind:squared=false /> -->
        {{entry.title}}
    </v-btn>
    <h1>{{ $t('message') }}</h1>
</div>  
</template>

<script>
export default {

    mounted(){
        console.log(this.$i18n);
        console.log(this.$i18n.languages);
    }
}
</script>

<style>

</style>