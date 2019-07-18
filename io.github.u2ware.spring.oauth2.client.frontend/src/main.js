import Vue from 'vue'
import i18n from './plugins/i18n'
import router from './plugins/router'
import Apps from './Apps.vue'

import './plugins/axios'
import './plugins/vuetify'
import './plugins/logger'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

import a from "./apps/oauth2/Authentication.js";


Vue.config.productionTip = false;
Vue.prototype.$authentication = a;


new Vue({
  i18n,
  router,
  render: h => h(Apps)
}).$mount('#app')
