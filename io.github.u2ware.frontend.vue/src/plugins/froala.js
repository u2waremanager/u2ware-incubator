/////////////////////////////////////////
//npm install vue-froala-wysiwyg --save
/////////////////////////////////////////

// Require Froala Editor js file.
require('froala-editor/js/froala_editor.pkgd.min.js')

// Require Froala Editor css files.
require('froala-editor/css/froala_editor.pkgd.min.css')
require('froala-editor/css/froala_style.min.css')

// Import and use Vue Froala lib.
import Vue from 'vue'
import VueFroala from 'vue-froala-wysiwyg'

Vue.use(VueFroala)