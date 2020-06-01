<template>
<div>
    <pre>{{content}}</pre>
    <button @click="changeFontFamily">Font Family</button>
    <button @click="changeFontSize">Font Size</button>
    <summer-note  name="editor" v-model="content" :config="config"></summer-note>
</div>

</template>

<script>

import SummerNote from '../../../plugins/summernote.vue'


export default {

    components : {
        SummerNote
    },

    data : () => ({
        content : ' ',
        config : {
            toolbar : [],
            // toolbar: [
            //     ['style', ['bold', 'italic', 'underline', 'clear']],
            //     ['font', ['strikethrough', 'superscript', 'subscript']],
            //     ['fontname', ['fontNames']],
            //     ['fontsize', ['fontsizes']],
            //     ['color', ['color']],
            //     ['para', ['ul', 'ol', 'paragraph']],
            //     ['height', ['height']]
            // ],
            // fontNames: ['궁서', 'Arial', 'Arial Black', 'Comic Sans MS', 'Courier New'],
            // fontsizes: ["40", "50", "60", "70", "80", "90", "100", "110", "120"]
            height : 400
        }
    }), 
    methods : {
        changeFontFamily(){
            // $('#myId').summernote('fontName', '궁서');
            // $('#myId').summernote('fontName', {'font-family' : '돋움', 'font-size' : this.fontSize});
        },
        changeFontSize(){
            // $('#myId').summernote('fontName', '궁서');
            // $('#myId').summernote('fontName', {'font-family' : this.fontFamily, 'font-size' : '50px'});
        },
    },
    mounted(){

    }
}
</script>

<style>

</style>