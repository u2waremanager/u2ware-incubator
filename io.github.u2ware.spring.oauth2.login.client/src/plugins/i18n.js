import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)

function loadLocaleMessages () {
  const locales = require.context('../', true, /[A-Za-z0-9-_,\s]+\.locales.json$/i)
  const messages = {}
  locales.keys().forEach(key => {
    console.log("locales : "+key);
    const matched = key.match(/([A-Za-z0-9-_]+)\./i)
    if (matched && matched.length > 1) {
      const locale = matched[1]
      messages[locale] = locales(key)
    }
  })
  return messages
}

//////////////////////////////
// Add
/////////////////////////////////

VueI18n.prototype.languages = [
  { flag: 'us', language: 'en', title: 'English' },
  { flag: 'es', language: 'es', title: 'Espa챰ol' }
];

VueI18n.prototype.changeLocale = function(locale){
  //alert(locale);
  console.log('changeLocale: '+locale);
  this.locale = locale;
}



export default new VueI18n({
  locale: process.env.VUE_APP_I18N_LOCALE || 'en',
  fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
  messages: loadLocaleMessages()
})
