<template>
  <v-app>
    <v-toolbar app>
      <v-toolbar-title class="headline text-uppercase">
        <span>Vuetify</span>
        <span class="font-weight-light">MATERIAL DESIGN</span>
      </v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn
        flat
        href="https://github.com/vuetifyjs/vuetify/releases/latest"
        target="_blank"
      >
        <span class="mr-2">Latest Release</span>
      </v-btn>
    </v-toolbar>

    <v-content>
      <HelloWorld/>
      
      <div>
        <h1>{{ $t('message') }}</h1>
        <button v-for="entry in languages" :key="entry.title" @click="changeLocale(entry.language)">
            <flag :iso="entry.flag" v-bind:squared=false /> {{entry.title}}
        </button>
      </div>
    </v-content>
  </v-app>
</template>

<script>
import HelloWorld from './components/HelloWorld'
//import i18n from './i18n'

export default {
  name: 'App',
  components: {
    HelloWorld
  },
  data() {
      return {
          languages: [
              { flag: 'us', language: 'en', title: 'English' },
              { flag: 'es', language: 'es', title: 'Español' }
          ]
      };
  },
  methods: {
      changeLocale(locale) {

          window.console.log(locale);
          //window.console.log(i18n);
          window.console.log(this.i18n);
          //i18n.locale = locale;
          // console.log(i18n);
          // console.log(i18n.messages);
          // console.log(i18n.messages[locale]);
          // console.log(i18n.messages[locale].message);
      }
  }
}
</script>
