<template>
<div class="hello">
     <h1>BBBBBB</h1>
    <h2>{{ foo }}</h2>
    <h2>{{ bar }}</h2>
</div>
</template>

<script>

export default {

  name : "C1",
  props: {
    foo: String,
    bar: String,
  },
  data: () => ({

  }),
  components: {
    'C2' : C2
  },
  methods : {

  },
  created: function() {
      this.$log.debug('C2', 'created');
  },
  mounted: function() {
      this.$log.debug('C2', 'mounted');
  },
  updated: function() {
      this.$log.debug('C2', 'updated');
  },
  destroyed: function() {
      this.$log.debug('C2', 'destroyed');
  },
}
</script>


 