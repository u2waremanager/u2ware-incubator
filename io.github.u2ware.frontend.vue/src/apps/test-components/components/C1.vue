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


  components: {
    'C2' : C2
  }

}
</script>


 