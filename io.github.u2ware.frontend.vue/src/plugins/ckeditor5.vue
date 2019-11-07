<template>
<ckeditor :editor="editor" :config="config" @input="updateContent"></ckeditor>
</template>
<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';


function CustomizedUploadAdapterPlugin( editor ) {
    editor.plugins.get( 'FileRepository' ).createUploadAdapter = ( loader ) => {
        // console.log('FileRepository1', editor)
        // console.log('FileRepository2', editor.config)
        // console.log('FileRepository3', editor.config._config)
        // console.log('FileRepository4', editor.config._config.uploader)
        return new CustomizedUploadAdapter( loader , editor.config._config.uploader);
    };
}


class CustomizedUploadAdapter{

    constructor(loader, uploader) {
        this.loader = loader;
        this.uploader = uploader; //axios promise
    }

    upload(){
        return new Promise((resolve, reject) => {

            this.loader.file.then( (file) => {
                // console.log("file1", this.uploader);
                // console.log("file2", this.loader);
                // console.log("file3", file);
                this.uploader(file).then(r=>{
                    // console.log("uploader success", r);
                    resolve({
                        default : r.data,
                    })
                }).catch(e=>{
                    // console.log("uploader error", e);
                    reject(e);
                });
            });
        });
    }

    abort(){
        //alert('asdf!!!!!');
    }
}



export default {
    name: 'ckeditor5',
    props: {
        uploader: Function
    },

    data : function(){ return {

        editor : ClassicEditor,
        config : {
            extraPlugins : [
                CustomizedUploadAdapterPlugin
            ],
            image: {
                toolbar: [ 'imageStyle:alignLeft', 'imageStyle:alignCenter', 'imageStyle:alignRight' ],
                styles: ['alignCenter', 'alignLeft', 'alignRight', 'full', 'side']
            },

            mediaEmbed: {
                previewsInData : true
            },
            uploader : this.uploader
        }
    }},

    methods : {
        updateContent(r){
            this.$emit("input", r);
        },
    },
}
</script>
<style>
.ck-editor__editable {
    min-height: 600px ;
    max-height: 600px ;
}
</style>