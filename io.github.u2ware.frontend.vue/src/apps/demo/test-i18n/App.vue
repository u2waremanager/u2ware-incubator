<template>
<div>
    <button v-for="entry in i18n.languages" :key="entry.title" @click="i18n.changeLocale(entry.language)">
        <flag :iso="entry.flag" v-bind:squared=false /> {{entry.title}}
    </button>
    <h1>{{ $t('message') }}</h1>
</div>
</template>

<script>
import i18n from '../../../plugins/i18n'

export default {
    name: 'app',
    data() {
        return {
            i18n: i18n
        };
    },
}
</script>

<style>

</style>
