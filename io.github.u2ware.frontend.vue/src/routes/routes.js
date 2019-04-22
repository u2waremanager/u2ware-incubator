export default [
    {
      path: '/',
      name: 'demo',
      meta: {
        title: ''
      },
      component: () => import(/* webpackChunkName: "test-axios" */ '../apps/demo/Demo.vue'), 
    },
    {
      path: '/test-axios',
      name: 'test-axios',
      meta: {
        title: 'test-axios'
      },
      component: () => import(/* webpackChunkName: "test-axios" */ '../apps/demo/test-axios/App.vue'),
    },
    {
      path: '/test-i18n',
      name: 'test-i18n',
      meta: {
        title: 'test-i18n'
      },
      component: () => import(/* webpackChunkName: "test-i18n" */ '../apps/demo/test-i18n/App.vue'),
    },
    {
      path: '/test-components',
      name: 'test-components',
      meta: {
        title: 'test-components'
      },
      component: () => import(/* webpackChunkName: "test-components" */ '../apps/demo/test-components/App.vue'),
    },
    {
      path: '/test-events',
      name: 'test-events',
      meta: {
        title: 'test-events'
      },
      component: () => import(/* webpackChunkName: "test-events" */ '../apps/demo/test-events/App.vue'),
    },
    {
      path: '/basic',
      name: 'basic',
      meta: {
        title: 'basic'
      },
      component: () => import(/* webpackChunkName: "basic" */ '../apps/demo/basic/App.vue'),
    },
    {
      path: '/basic-router',
      name: 'basic-router',
      component: () => import(/* webpackChunkName: "basic-router" */ '../apps/demo/basic-router/App.vue'),
      children : [
        {
          path : '/basic-router',
          name: 'basic-router-home',
          meta: {
            title: 'basic-router-home'
          },
          component: () => import(/* webpackChunkName: "basic-router-home" */ '../apps/demo/basic-router/views/Home.vue')
        },
        {
          path : '/basic-router/about',
          name: 'basic-router-about',
          meta: {
            title: 'basic-router-about'
          },
          component: () => import(/* webpackChunkName: "basic-router-about" */ '../apps/demo/basic-router/views/About.vue')
        },
      ]
    },
    {
      path: '/vuetify',
      name: 'vuetify',
      meta: {
        title: 'vuetify'
      },
      component: () => import(/* webpackChunkName: "vuetify" */ '../apps/demo/vuetify/App.vue'),
    },
    {
      path: '/vuetify-layouts',
      name: 'vuetify-layouts',
      meta: {
        title: 'vuetify-layouts'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/App.vue'),
      children : [
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/baseline.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/baselineFlipped.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/centered.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/complex.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/dark.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/googleContacts.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/googleKeep.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/googleYoutube.vue'),
      // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/demo/vuetify-layouts/sandbox.vue'),
    ]
    },
    {
      path : '/vuetify-layouts/baseline',
      name: 'vuetify-layouts-baseline',
      meta: {
        title: 'vuetify-layouts-baseline'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-baseline" */ '../apps/demo/vuetify-layouts/components/baseline.vue')
    },
    {
      path : '/vuetify-layouts/baselineFlipped',
      name: 'vuetify-layouts-baselineFlipped',
      meta: {
        title: 'vuetify-layouts-baselineFlipped'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-baselineFlipped" */ '../apps/demo/vuetify-layouts/components/baselineFlipped.vue')
    },
    {
      path : '/vuetify-layouts/centered',
      name: 'vuetify-layouts-centered',
      meta: {
        title: 'vuetify-layouts-centered'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-centered" */ '../apps/demo/vuetify-layouts/components/centered.vue')
    },
    {
      path : '/vuetify-layouts/complex',
      name: 'vuetify-layouts-complex',
      meta: {
        title: 'vuetify-layouts-complex'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-complex" */ '../apps/demo/vuetify-layouts/components/complex.vue')
    },
    {
      path : '/vuetify-layouts/dark',
      name: 'vuetify-layouts-dark',
      meta: {
        title: 'vuetify-layouts-dark'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-dark" */ '../apps/demo/vuetify-layouts/components/dark.vue')
    },
    {
      path : '/vuetify-layouts/googleContacts',
      name: 'vuetify-layouts-googleContacts',
      meta: {
        title: 'vuetify-layouts-googleContacts'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-googleContacts" */ '../apps/demo/vuetify-layouts/components/googleContacts.vue')
    },
    {
      path : '/vuetify-layouts/googleYoutube',
      name: 'vuetify-layouts-googleYoutube',
      meta: {
        title: 'vuetify-layouts-googleYoutube'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-googleYoutube" */ '../apps/demo/vuetify-layouts/components/googleYoutube.vue')
    },
    {
      path : '/vuetify-layouts/googleKeep',
      name: 'vuetify-layouts-googleKeep',
      meta: {
        title: 'vuetify-layouts-googleKeep'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-googleKeep" */ '../apps/demo/vuetify-layouts/components/googleKeep.vue')
    },
    {
      path : '/vuetify-layouts/sandbox',
      name: 'vuetify-layouts-sandbox',
      meta: {
        title: 'vuetify-layouts-sandbox'
      },
      component: () => import(/* webpackChunkName: "vuetify-layouts-sandbox" */ '../apps/demo/vuetify-layouts/components/sandbox.vue')
    },
    

  ];