<template>
<div>
    <button type="button" class="btn btn-primary" autocomplate="off" data-loading-text="jquery with bootstrap" @click="clickBtn">
        Test
    </button>
</div>    
</template>
<script>
import JQuery from 'jquery'
const $ = JQuery;

export default {
    
    name: 'jQueryApp',

	methods: {
      clickBtn (event) {

          console.log($);
          console.log(event);
          console.log($(event.target));

        $(event.target).html('loading')
        
        setTimeout(function() {
          $(event.target).html('reset')
        }, 3000);
      }
	}    
}
</script>
<style>




</style>