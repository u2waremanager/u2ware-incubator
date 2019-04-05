import Vue from 'vue'
import Router from 'vue-router'
import All from '../apps/All.vue'

Vue.use(Router)

const routes = [
  {
    path: '/',
    name: 'all',
    meta: {
      title: ''
    },
    component: All
  },
  {
    path: '/test-axios',
    name: 'test-axios',
    meta: {
      title: 'test-axios'
    },
    component: () => import(/* webpackChunkName: "test-axios" */ '../apps/test-axios/App.vue'),
  },
  {
    path: '/test-i18n',
    name: 'test-i18n',
    meta: {
      title: 'test-i18n'
    },
    component: () => import(/* webpackChunkName: "test-i18n" */ '../apps/test-i18n/App.vue'),
  },
  {
    path: '/test-components',
    name: 'test-components',
    meta: {
      title: 'test-components'
    },
    component: () => import(/* webpackChunkName: "test-components" */ '../apps/test-components/App.vue'),
  },
  {
    path: '/basic',
    name: 'basic',
    meta: {
      title: 'basic'
    },
    component: () => import(/* webpackChunkName: "basic" */ '../apps/basic/App.vue'),
  },
  {
    path: '/basic-router',
    name: 'basic-router',
    component: () => import(/* webpackChunkName: "basic-router" */ '../apps/basic-router/App.vue'),
    children : [
      {
        path : '/basic-router',
        name: 'basic-router-home',
        meta: {
          title: 'basic-router-home'
        },
        component: () => import(/* webpackChunkName: "basic-router-home" */ '../apps/basic-router/views/Home.vue')
      },
      {
        path : '/basic-router/about',
        name: 'basic-router-about',
        meta: {
          title: 'basic-router-about'
        },
        component: () => import(/* webpackChunkName: "basic-router-about" */ '../apps/basic-router/views/About.vue')
      },
    ]
  },
  {
    path: '/vuetify',
    name: 'vuetify',
    meta: {
      title: 'vuetify'
    },
    component: () => import(/* webpackChunkName: "vuetify" */ '../apps/vuetify/App.vue'),
  },
  {
    path: '/vuetify-layouts',
    name: 'vuetify-layouts',
    meta: {
      title: 'vuetify-layouts'
    },
    component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/App.vue'),
    children : [
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/baseline.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/baselineFlipped.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/centered.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/complex.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/dark.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/googleContacts.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/googleKeep.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/googleYoutube.vue'),
    // component: () => import(/* webpackChunkName: "vuetify-layouts" */ '../apps/vuetify-layouts/sandbox.vue'),
  ]
  },
  {
    path : '/vuetify-layouts/centered',
    name: 'vuetify-layouts-centered',
    meta: {
      title: 'vuetify-layouts-centered'
    },
    component: () => import(/* webpackChunkName: "vuetify-layouts-centered" */ '../apps/vuetify-layouts/components/centered.vue')
  },
  {
    path : '/vuetify-layouts/googleContacts',
    name: 'vuetify-layouts-googleContacts',
    meta: {
      title: 'vuetify-layouts-googleContacts'
    },
    component: () => import(/* webpackChunkName: "vuetify-layouts-googleContacts" */ '../apps/vuetify-layouts/components/googleContacts.vue')
  },
  {
    path : '/vuetify-layouts/sandbox',
    name: 'vuetify-layouts-sandbox',
    meta: {
      title: 'vuetify-layouts-sandbox'
    },
    component: () => import(/* webpackChunkName: "vuetify-layouts-sandbox" */ '../apps/vuetify-layouts/components/sandbox.vue')
  },
  
];





const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
});


router.beforeEach((to, from, next) => {
  // This goes through the matched routes from last to first, finding the closest route with a title.
  // eg. if we have /some/deep/nested/route and /some, /deep, and /nested have titles, nested's will be chosen.
  const nearestWithTitle = to.matched.slice().reverse().find(r => r.meta && r.meta.title);

  // Find the nearest route element with meta tags.
  const nearestWithMeta = to.matched.slice().reverse().find(r => r.meta && r.meta.metaTags);
  //const previousNearestWithMeta = from.matched.slice().reverse().find(r => r.meta && r.meta.metaTags);

  // If a route with a title was found, set the document (page) title to that value.
  if(nearestWithTitle) document.title = nearestWithTitle.meta.title;

  // Remove any stale meta tags from the document using the key attribute we set below.
  Array.from(document.querySelectorAll('[data-vue-router-controlled]')).map(el => el.parentNode.removeChild(el));

  // Skip rendering meta tags if there are none.
  if(!nearestWithMeta) return next();

  // Turn the meta tag definitions into actual elements in the head.
  nearestWithMeta.meta.metaTags.map(tagDef => {
    const tag = document.createElement('meta');

    Object.keys(tagDef).forEach(key => {
      tag.setAttribute(key, tagDef[key]);
    });

    // We use this to track which meta tags we create, so we don't interfere with other ones.
    tag.setAttribute('data-vue-router-controlled', '');

    return tag;
  })
  // Add the meta tags to the document head.
  .forEach(tag => document.head.appendChild(tag));

  next();
});


export default router;
