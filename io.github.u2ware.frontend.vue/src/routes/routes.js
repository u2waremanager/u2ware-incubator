export default [
    {
        path: '/demo',
        name: 'demo',
        meta: { title: ''},
        component: () => import(/* webpackChunkName: "demo" */ '../apps/demo/Demo.vue'), 
    },
    {
        path: '/demo/basic',
        name: 'basic',
        meta: { title: 'basic'},
        component: () => import(/* webpackChunkName: "basic" */ '../apps/demo/basic/App.vue'),
    },
    {
        path: '/demo/basic-router',
        name: 'basic-router',
        component: () => import(/* webpackChunkName: "basic-router" */ '../apps/demo/basic-router/App.vue'),
        children : [
            {
                path : '/demo/basic-router',
                name: 'basic-router-home',
                meta: {title: 'basic-router-home'},
                component: () => import(/* webpackChunkName: "basic-router-home" */ '../apps/demo/basic-router/views/Home.vue')
            },
            {
                path : '/demo/basic-router/about',
                name: 'basic-router-about',
                meta: { title: 'basic-router-about'},
                component: () => import(/* webpackChunkName: "basic-router-about" */ '../apps/demo/basic-router/views/About.vue')
            },
        ]
    },
    {
        path: '/demo/basic-vuetify',
        name: 'basic-vuetify',
        meta: { title: 'basic-vuetify' },
        component: () => import(/* webpackChunkName: "basic-vuetify" */ '../apps/demo/basic-vuetify/App.vue'),
    },
    {
        path: '/demo/basic-vuetify-layouts',
        name: 'basic-vuetify-layouts',
        meta: { title: 'basic-vuetify-layouts' },
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts" */ '../apps/demo/basic-vuetify-layouts/App.vue'),
    },
    {
        path : '/demo/basic-vuetify-layouts/baseline',
        name: 'basic-vuetify-layouts-baseline',
        meta: {title: 'basic-vuetify-layouts-baseline'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-baseline" */ '../apps/demo/basic-vuetify-layouts/components/baseline.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/baselineFlipped',
        name: 'basic-vuetify-layouts-baselineFlipped',
        meta: {title: 'basic-vuetify-layouts-baselineFlipped'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-baselineFlipped" */ '../apps/demo/basic-vuetify-layouts/components/baselineFlipped.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/centered',
        name: 'basic-vuetify-layouts-centered',
        meta: { title: 'basic-vuetify-layouts-centered'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-centered" */ '../apps/demo/basic-vuetify-layouts/components/centered.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/complex',
        name: 'basic-vuetify-layouts-complex',
        meta: { title: 'basic-vuetify-layouts-complex'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-complex" */ '../apps/demo/basic-vuetify-layouts/components/complex.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/dark',
        name: 'basic-vuetify-layouts-dark',
        meta: { title: 'basic-vuetify-layouts-dark'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-dark" */ '../apps/demo/basic-vuetify-layouts/components/dark.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/googleContacts',
        name: 'basic-vuetify-layouts-googleContacts',
        meta: { title: 'basic-vuetify-layouts-googleContacts'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-googleContacts" */ '../apps/demo/basic-vuetify-layouts/components/googleContacts.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/googleYoutube',
        name: 'basic-vuetify-layouts-googleYoutube',
        meta: { title: 'basic-vuetify-layouts-googleYoutube'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-googleYoutube" */ '../apps/demo/basic-vuetify-layouts/components/googleYoutube.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/googleKeep',
        name: 'basic-vuetify-layouts-googleKeep',
        meta: {title: 'basic-vuetify-layouts-googleKeep'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-googleKeep" */ '../apps/demo/basic-vuetify-layouts/components/googleKeep.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/sandbox',
        name: 'basic-vuetify-layouts-sandbox',
        meta: {title: 'basic-vuetify-layouts-sandbox'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-sandbox" */ '../apps/demo/basic-vuetify-layouts/components/sandbox.vue')
    },
    {
        path : '/demo/basic-vuetify-layouts/X1',
        name: 'basic-vuetify-layouts-X1',
        meta: {title: 'basic-vuetify-layouts-X1'},
        component: () => import(/* webpackChunkName: "basic-vuetify-layouts-X1" */ '../apps/demo/basic-vuetify-layouts/components/X1.vue')
    },
    {
        path: '/demo/test-axios',
        name: 'test-axios',
        meta: {title: 'test-axios'},
        component: () => import(/* webpackChunkName: "test-axios" */ '../apps/demo/test-axios/App.vue'),
    },
    {
        path: '/demo/test-i18n',
        name: 'test-i18n',
        meta: {title: 'test-i18n'},
        component: () => import(/* webpackChunkName: "test-i18n" */ '../apps/demo/test-i18n/App.vue'),
    },
    {
        path: '/demo/test-components',
        name: 'test-components',
        meta: {title: 'test-components'},
        component: () => import(/* webpackChunkName: "test-components" */ '../apps/demo/test-components/App.vue'),
    },
    {
        path: '/demo/test-components-events',
        name: 'test-components-events',
        meta: {title: 'test-components-events'},
        component: () => import(/* webpackChunkName: "test-components-events" */ '../apps/demo/test-components-events/App.vue'),
    },
    {
        path: '/demo/test-moment',
        name: 'test-moment',
        meta: {title: 'test-moment'},
        component: () => import(/* webpackChunkName: "test-moment" */ '../apps/demo/test-moment/App.vue'),
    },
    {
        path: '/demo/test-froala',
        name: 'test-froala',
        meta: {title: 'test-froala'},
        component: () => import(/* webpackChunkName: "test-froala" */ '../apps/demo/test-froala/App.vue'),
    },
    {
        path: '/demo/test-tiptap',
        name: 'test-tiptap',
        meta: {title: 'test-tiptap'},
        component: () => import(/* webpackChunkName: "test-tiptap" */ '../apps/demo/test-tiptap/App.vue'),
    },
    {
        path: '/demo/test-ckeditor',
        name: 'test-ckeditor',
        meta: {title: 'test-ckeditor'},
        component: () => import(/* webpackChunkName: "test-ckeditor" */ '../apps/demo/test-ckeditor/App.vue'),
    },
  
/*
    {
      path: '/:room/:username',
      name: 'conference',
      meta: {
        title: 'Conference'
      },
      component: () => import(.....), 
    }

    // 
    this.$route.params.room
    this.$route.params.username
*/    
]
