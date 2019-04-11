<template>
    <button @click="call1">AAA</button>
</template>

<script>
export default {

    methods: {        
        call1() {
            this.$log.debug('aaaa');
            this.$log.debug(this.$axios);

            this.$axios
                .get('http://localhost:8080/persons')
                .then((result) => {
                    this.$log.debug(result);
                })
                .catch((error) => {
                    this.$log.debug(error);
                })

            this.$axios
                .get('http://localhost:8080/persons2')
                .then((result) => {
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
