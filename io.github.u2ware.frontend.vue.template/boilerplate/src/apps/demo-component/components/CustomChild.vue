
<template>
 <div>
    <v-btn @click="toParent">To Parent</v-btn>
    <p>fromParentEvent</p>
    <pre>{{fromParentEvent ? fromParentEvent.message : ''}}</pre>
</div> 
</template>

<script>
export default {
  name: 'custom-item',
  props : {

  },
  data: () => ({
      fromParentEvent : null
  }),
  methods : {
    fromParent(event){
        console.log('Child', 'fromParent', event);
        this.fromParentEvent = event;
    },
    toParent(event){
        event.message = new Date();
        console.log('Child', 'toParent', event);
        this.$emit('toParent', event);
    },    

  },
  created: function() {
      console.log(this.$options.name, 'created');
  },
  mounted: function() {
      console.log(this.$options.name, 'mounted');
  },
  updated: function() {
      console.log(this.$options.name, 'updated');
  },
  destroyed: function() {
      console.log(this.$options.name, 'destroyed');
  },
}
</script>

<style>

</style>