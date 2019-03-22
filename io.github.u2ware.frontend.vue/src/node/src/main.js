import Vue from 'vue'
import './plugins/axios'
import './plugins/vuetify'
import App from './App.vue'
import router from './router'
import i18n from './i18n'
import 'roboto-fontface/css/roboto/roboto-fontface.css'
import 'material-design-icons-iconfont/dist/material-design-icons.css'

Vue.config.productionTip = false

new Vue({
  router,
  i18n,
  render: h => h(App)
}).$mount('#app')
