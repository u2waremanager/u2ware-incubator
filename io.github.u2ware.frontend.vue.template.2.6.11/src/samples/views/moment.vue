<template>
  <div>{{$moment(today).format('YYYY-MM-DD HH:mm:ss')}}

      <v-btn text @click="myMethod(!myMethodRunning)">{{myMethodRunning ? 'STOP' : 'START'}}</v-btn>

  </div>
</template>

<script>
import task from './Task'

export default {

    data : ()=>({
        today : new Date(),
        myMethodRunning : false,
    }),
    methods : {
        myMethod(isRun) {
            if(isRun != undefined){
                this.myMethodRunning = (isRun) ? task.startup('myMethod', 1000, this.myMethod): task.shutdown('myMethod');
            }else{
                this.today = new Date();
            }
        },
    },
    mounted(){
        console.log(this);
        console.log(this.$moment);
        this.myMethod(true);
    }
}
</script>

<style>

</style>