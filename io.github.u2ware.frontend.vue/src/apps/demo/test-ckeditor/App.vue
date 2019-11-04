<template>

    <div>
        <div>
  <ckeditor  :editor="editor" v-model="editorData" :config="editorConfig">


      
  </ckeditor>

        </div>
<!-- 
<div id="snippet-image-resize">
	<h3>Resize me!</h3>

	<figure class="image">
		<img src="https://t1.daumcdn.net/cfile/tistory/99CB5D395D710DD91E" alt="Autumn fields">
		<figcaption>Autumn fields by Aleksander Nowodziński</figcaption>
	</figure>

	<h3>Resized image (width: 75%):</h3>

	<figure class="image image_resized" style="width:75%;">
		<img src="https://t1.daumcdn.net/cfile/tistory/99CB5D395D710DD91E" alt="Autumn fields">
	</figure>
</div>
 -->

    </div>
</template>

<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';
import MyCustomUploadAdapterPlugin from './MyCustomUploadAdapterPlugin';
// import Image from '@ckeditor/ckeditor5-image/src/image';
// import ImageToolbar from '@ckeditor/ckeditor5-image/src/imagetoolbar';
// import ImageCaption from '@ckeditor/ckeditor5-image/src/imagecaption';
// import ImageStyle from '@ckeditor/ckeditor5-image/src/imagestyle';
//import ImageResizeXX from '@ckeditor/ckeditor5-image/src/imageresize';

// import UploadAdapter from '@ckeditor/ckeditor5-adapter-ckfinder/src/uploadadapter';
//import SimpleUploadAdapter from '@ckeditor/ckeditor5-upload/src/adapters/simpleuploadadapter';



export default {
    data (){
        return {
            editor: ClassicEditor,
            // editorData: '<figure class="image image_resized" style="width:30%;"><img src="https://t1.daumcdn.net/cfile/tistory/99CB5D395D710DD91E" alt="Autumn fields"></figure><p>Content of the editor.</p>',
            editorData: '<p>Content of the editor.</p>',
            editorConfig: {
      
                extraPlugins: [MyCustomUploadAdapterPlugin],
                //viewportTopOffset: window.getViewportTopOffsetConfig(),

                image: {
                    // toolbar: [ 'imageTextAlternative', '|', 'imageStyle:full', 'imageStyle:side' ],
                         
                    //resizeUnit: 'px',
                    // You need to configure the image toolbar, too, so it uses the new style buttons.
                    toolbar: [ 'imageStyle:alignLeft', 'imageStyle:alignCenter', 'imageStyle:alignRight' ],
                    styles: [
                        // This option is equal to a situation where no style is applied.
                        'alignCenter',

                        // This represents an image aligned to the left.
                        'alignLeft',

                        // This represents an image aligned to the right.
                        'alignRight',

                        'full',
                        'side'

                    ]
                }
            },
        }
    }, 
    methods : {

    },
    computed : {

    },
    mounted(){


// ClassicEditor
// 	.create( document.querySelector( '#snippet-image-resize' ), {
// 		toolbar: {
// 			//viewportTopOffset: window.getViewportTopOffsetConfig()
// 		},
// 		//cloudServices: CS_CONFIG
// 	} )
// 	.then( editor => {
// 		window.editorResize = editor;
// 	} )
// 	.catch( err => {
// 		console.error( err );
// 	} );


        // ClassicEditor.create( document.querySelector( '#editor' ), {

        //     } ).then( editor => {
        //         console.log( 'Editor was initialized', editor );
        //     } ).catch( error => {
        //         console.error('XXXXXXCCCCCCC' );
        //     } );


        //this.editor.plugins = [SimpleUploadAdapter];
        // .get( 'FileRepository' ).createUploadAdapter = ( loader ) => {
        //     return new MyUploadAdapter( loader );
        // };
    }
}


</script>
<style>
/*
.ck-content .image-style-side {
    max-width: 50%;
    float: right;
    margin-left: var(--ck-image-style-spacing);
}
 .ck-content .image.image_resized {
    max-width: 100%;
} 
*/
.ck-editor__editable_inline {
    max-width : 100%;
    min-height: 500px ;
}

/* .ck-content .image-style-side {
    max-width: 50%;
    float: right;
    margin-left: var(--ck-image-style-spacing);
}
.ck-content .image.image_resized {
    max-width: 100%;
    display: block;
    box-sizing: border-box;    
}
.ck-content .image.image_resized img {
    width: 100%;
}

.ck-content .image.image_resized > figcaption {
    display: block;
}  */
</style>