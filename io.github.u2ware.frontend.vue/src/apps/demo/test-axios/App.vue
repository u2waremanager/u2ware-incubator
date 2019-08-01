<template>
    <button @click="call1">AAA</button>
</template>

<script>
import qs from 'qs';

export default {

    methods: {        
        call1() {
            this.$log.debug('aaaa');
            this.$log.debug(this.$axios);

            // this.$axios
            //     .get('http://google.com')
            //     .then((result) => {
            //         this.$log.debug(result);
            //     })
            //     .catch((error) => {
            //         this.$log.debug(error);
            //     })

            // this.$axios
            //     .get('http://sssss/persons2')
            //     .then((result) => {
            //         this.$log.debug(result);
            //     })
            //     .catch((error) => {
            //         this.$log.debug(error);
            //     })


            var p = {
                name : 'a',
                age : ["1","2"]
            };

            this.$axios({

                method : 'post',
                url : 'http://localhost:8080/apis/foos/!q',
                params : p,
                paramsSerializer: (params) => {
                    return qs.stringify(params, {arrayFormat: 'repeat'});
                },

            }).then((result) => {
                this.$log.debug(result);
            })
            .catch((error) => {
                this.$log.debug(error);
            })

        }
    }

}
</script>

<style>

</style>
