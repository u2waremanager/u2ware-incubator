/**
 * vue add i18n
 * 
 */
import Vue from 'vue'
import VueI18n from 'vue-i18n'

Vue.use(VueI18n)


const i18n = new VueI18n({
    locale: process.env.VUE_APP_I18N_LOCALE || 'en',
    fallbackLocale: process.env.VUE_APP_I18N_FALLBACK_LOCALE || 'en',
    messages: loadLocaleMessages()
})

VueI18n.prototype.languages = [
  { flag: 'us', language: 'en', title: 'English' },
  { flag: 'es', language: 'es', title: 'Español' },
  { flag: 'ko', language: 'ko', title: 'Korea' }
];
  
VueI18n.prototype.changeLocale = function(locale){
  //alert(locale);
  console.log('changeLocale: '+locale);
  this.locale = locale;
}
  
function loadLocaleMessages () {
  const locales = require.context('../locales', true, /[A-Za-z0-9-_,\s]+\.json$/i)
  const messages = {}
  locales.keys().forEach(key => {
    const matched = key.match(/([A-Za-z0-9-_]+)\./i)
    if (matched && matched.length > 1) {
      const locale = matched[1];
      //console.log("locales : ../locales/"+locale+".json");
      messages[locale] = locales(key)
      //alert('loadLocaleMessages : '+locale)
    }
  })
  return messages
}

export default i18n;

