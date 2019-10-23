<template>
<div>
    <button v-for="entry in $i18n.languages" :key="entry.title" @click="$i18n.changeLocale(entry.language)">
        <!-- <flag :iso="entry.flag" v-bind:squared=false /> -->
        {{entry.title}}
    </button>
    <h1>{{ $t('message') }}</h1>
    
</div>
</template>

<script>

export default {
    name: 'app'
}
</script>

<style>

</style>
