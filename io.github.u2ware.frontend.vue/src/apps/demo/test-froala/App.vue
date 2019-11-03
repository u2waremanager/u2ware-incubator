<template>
  <div>
    <froala :tag="'textarea'" :config="config" v-model="model"></froala>
  </div>
</template>

<script>
import VueFroala from 'vue-froala-wysiwyg';

export default {

    name: 'AAAAA',
    components : {

    },
    data () {
        return {
            config: {
                videoEditButtons: ['videoDisplay', 'videoRemove'],
                immediateVueModelUpdate : true,
                events: {
                    'froalaEditor.initialized': function () {
                        console.log('initialized')
                    }
                }
            },
            model: 'Edit Your Content Here!<img src="https://s3-eu-west-1.amazonaws.com/froala-eu/temp_files%2F1572709636387-IMG_0003.jpg" style="width: 300px;" class="fr-fic fr-dib fr-draggable">'
        }
    },
}
</script>

<style>

</style>
