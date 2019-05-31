<template>
<div class="hello">
     <h1>C11</h1>
     <C2 bar="aaaaa" foo="RRRR"/>
     <C2 bar="xxxxx" foo="xxxx"/>
</div>
</template>

<script>

import C2 from './C2.vue'


export default {
  name : "C1",
  data: () => ({

  }),
  components: {
    'C2' : C2
  },
  methods : {

  },
  created: function() {
      this.$log.debug(this.$options.name, 'created');
  },
  mounted: function() {
      this.$log.debug(this.$options.name, 'mounted');
  },
  updated: function() {
      this.$log.debug(this.$options.name, 'updated');
  },
  destroyed: function() {
      this.$log.debug(this.$options.name, 'destroyed');
  },

}
</script>


 