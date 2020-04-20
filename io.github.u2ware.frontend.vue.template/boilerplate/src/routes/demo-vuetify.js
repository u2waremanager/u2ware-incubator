
export default [

  {
    path: '/demo-vuetify',
    name: 'App',
    component: () => import(/* webpackChunkName: "basic-vuetify-index" */ '../apps/demo-vuetify/Index.vue')
  },
  {
    path: '/demo-vuetify/layouts/baseline',
    name: 'demo-vuetify-layouts-baseline',
    meta: { title: 'basic-vuetify-layouts-baseline' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-baseline" */ '../apps/demo-vuetify/layouts/baseline.vue')
  },
  {
    path: '/demo-vuetify/layouts/baselineFlipped',
    name: 'demo-vuetify-layouts-baselineFlipped',
    meta: { title: 'basic-vuetify-layouts-baselineFlipped' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-baselineFlipped" */ '../apps/demo-vuetify/layouts/baseline-flipped.vue')
  },
  {
    path: '/demo-vuetify/layouts/centered',
    name: 'demo-vuetify-layouts-centered',
    meta: { title: 'basic-vuetify-layouts-centered' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-centered" */ '../apps/demo-vuetify/layouts/centered.vue')
  },
  {
    path: '/demo-vuetify/layouts/complex',
    name: 'demo-vuetify-layouts-complex',
    meta: { title: 'basic-vuetify-layouts-complex' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-complex" */ '../apps/demo-vuetify/layouts/complex.vue')
  },
  {
    path: '/demo-vuetify/layouts/dark',
    name: 'demo-vuetify-layouts-dark',
    meta: { title: 'basic-vuetify-layouts-dark' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-dark" */ '../apps/demo-vuetify/layouts/dark.vue')
  },
  {
    path: '/demo-vuetify/layouts/googleContacts',
    name: 'demo-vuetify-layouts-googleContacts',
    meta: { title: 'basic-vuetify-layouts-googleContacts' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-googleContacts" */ '../apps/demo-vuetify/layouts/google-contacts.vue')
  },
  {
    path: '/demo-vuetify/layouts/googleYoutube',
    name: 'demo-vuetify-layouts-googleYoutube',
    meta: { title: 'basic-vuetify-layouts-googleYoutube' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-googleYoutube" */ '../apps/demo-vuetify/layouts/google-youtube.vue')
  },
  {
    path: '/demo-vuetify/layouts/googleKeep',
    name: 'demo-vuetify-layouts-googleKeep',
    meta: { title: 'basic-vuetify-layouts-googleKeep' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-googleKeep" */ '../apps/demo-vuetify/layouts/google-keep.vue')
  },
  {
    path: '/demo-vuetify/layouts/sandbox',
    name: 'demo-vuetify-layouts-sandbox',
    meta: { title: 'basic-vuetify-layouts-sandbox' },
    component: () => import(/* webpackChunkName: "basic-vuetify-layouts-sandbox" */ '../apps/demo-vuetify/layouts/sandbox.vue')
  }
]