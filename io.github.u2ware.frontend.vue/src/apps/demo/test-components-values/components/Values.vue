<template>
<div>
  AAA
    <input type="text" v-model="model"/>

  BBB
</div>
</template>

<script>

export default {
  name: 'Value',
    props: {
      value: String,
    //   option1: String,
    //   option2: String,
    },

    computed: {
      model: {
        get () {
          return this.value
        },
        set (value) {
          this.$emit('input', value)
        }
      }
    },
    methods: {


    },
  created: function() {
    console.log(this.$options.name, "created");
  },
  mounted: function() {
    console.log(this.$options.name, "mounted");
  },
  updated: function() {
    console.log(this.$options.name, "updated");
  },
  destroyed: function() {
    console.log(this.$options.name, "destroyed");
  },

}
</script>

<style>

</style>
