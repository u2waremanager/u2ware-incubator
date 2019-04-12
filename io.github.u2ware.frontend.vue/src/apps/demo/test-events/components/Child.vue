<template>
<div>
    <button type='button' @click="toParent"> Click Me!</button>    
</div> 
</template>

<script>
export default {

    methods : {
        toParent(){
            const event = {message : 'message'}
            this.$log.debug('Child', 'toParent', event);
            this.$emit('toParent', event);
        },

        fromParent(event){
            this.$log.debug('Child', 'fromParent', event);
        },

        customEvent(event){
            this.$log.debug('Child', 'customEvent', event);
        }

    },
    created : function(){
        this.$on('CustomEvent', this.customEvent);
        this.$root.$on('CustomEvent', this.customEvent);
    },
}
</script>

<style>

</style>
