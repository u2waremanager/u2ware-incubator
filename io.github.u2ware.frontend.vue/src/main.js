import Vue from 'vue'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

///////////////////////////////////////
// Options
///////////////////////////////////////
import i18n from './plugins/i18n'
import router from './plugins/router'

///////////////////////////////////////
// Plugins
///////////////////////////////////////
import './plugins/vuetify'
import './plugins/logger'
import './plugins/axios'
import './plugins/moment'
import './plugins/froala'
import './plugins/ckeditor5'
// import './plugins/summernote'
// import './plugins/jquery'

///////////////////////////////////////
// 
///////////////////////////////////////
import Apps from './Apps.vue'
// import CRoutes from '....'
// router.addRoutes(CRoutes);

Vue.config.productionTip = false

new Vue({
  i18n,
  router,
  render: h => h(Apps)
}).$mount('#app')
