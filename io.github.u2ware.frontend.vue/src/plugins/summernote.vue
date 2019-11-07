<template>
<textarea :id="name"></textarea>
</template>

<script>
import JQuery from 'jquery'
const $ = JQuery;
require('bootstrap')
require('bootstrap/dist/css/bootstrap.min.css')
require('summernote/dist/summernote.css')
require('summernote/dist/summernote.js')

export default {

    props : {
        name: {
            type: String,
            required: true,
        },

        config: {
            type: Object,
            required: true
        }
    },

    mounted() {

        let vm = this;

        this.config.callbacks = {
            onInit: function () {
                $(vm.$el).summernote("code", vm.model);
            },
            onChange: function () {
                vm.$emit('input', $(vm.$el).summernote('code'));
            },
            onBlur: function () {
                vm.$emit('input', $(vm.$el).summernote('code'));
            }
        };

        $(this.$el).summernote(this.config);
    },

}
</script>

<style>

</style>