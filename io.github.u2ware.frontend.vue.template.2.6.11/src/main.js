import Vue from 'vue'
import main from './main.vue'

import router from './plugins/router'
import vuetify from './plugins/vuetify'
import i18n from './plugins/i18n'
import './plugins/axios'
import './plugins/moment'

Vue.config.productionTip = false

new Vue({
  router,
  i18n,
  vuetify,
  render: h => h(main)
}).$mount('#app')
