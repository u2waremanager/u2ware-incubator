<template>
<div>
    <pre>{{content}}</pre>
    <ckeditor5  v-model="content" :uploader="uploader"></ckeditor5>
</div>
</template>

<script>
import Ckeditor5 from '../../../plugins/ckeditor5.vue';

export default {
    components : {
        Ckeditor5
    },
    data: ()=> ({
       content : ' ',
       uploader(){
            return new Promise((resolve, reject) => {
                alert('uploader');
               
                let flag = true;

                if(flag == true){
                    resolve({default : 'http://uploader.jpg'})
                }else{
                    reject()
                }
            });
        }
    }), 
    methods : {

    }
}
</script>
<style>
</style>