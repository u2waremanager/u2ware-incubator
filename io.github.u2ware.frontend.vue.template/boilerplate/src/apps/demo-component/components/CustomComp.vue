<template>
<div>
	<p>myprop1_oldVal : {{myprop1_oldVal}}</p>
	<p>myprop1_newVal : {{myprop1_newVal}}</p>
	<p>myprop1 : {{myprop1}}</p>
</div>
</template>

<script>
export default {

	props : [
		'myprop1'
	],

	data : ()=>({
		myprop1_oldVal : null,
		myprop1_newVal : null,
	}),

	watch : {
		myprop1(newVal, oldVal) { // watch it
			this.myprop1_oldVal = oldVal;
			this.myprop1_newVal = newVal;
          console.log('Prop changed: ', newVal, ' | was: ', oldVal)
        }
	}

}
</script>

<style>

</style>