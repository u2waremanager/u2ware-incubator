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
  components: {
  },
  data: () => ({

  }),
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


 