<template>
<div>
    <Child ref="child" v-on:toParent="fromChild"></Child>
</div>
</template>

<script>
import Child from './Child.vue';

export default {

    components : {
        Child
    },

    methods : {
        fromChild(event){
            this.$log.debug('Parent', 'fromChild', event);
            this.toChild(event);
        },

        toChild(event){
            event.message = event.message + " from Parent";
            this.$log.debug('Parent', 'toChild', event);
            this.$refs.child.fromParent(event);

            this.$emit('CustomEvent', event); // Do nothing..
            this.$root.$emit('CustomEvent', event);
        }
    }

}
</script>

<style>

</style>
