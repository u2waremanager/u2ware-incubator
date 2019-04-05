import Vue from 'vue'
import Router from 'vue-router'
import Blank from '../apps/Blank.vue'


Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'blank',
      component: Blank
    },
    {
      path: '/test-i18n',
      name: 'test-i18n',
      component: () => import(/* webpackChunkName: "test-i18n" */ '../apps/test-i18n/App.vue'),
    },
    {
      path: '/test-components',
      name: 'test-components',
      component: () => import(/* webpackChunkName: "test-components" */ '../apps/test-components/App.vue'),
    },
    {
      path: '/basic',
      name: 'basic',
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
          component: () => import(/* webpackChunkName: "basic-router-home" */ '../apps/basic-router/views/Home.vue')
        },
        {
          path : '/basic-router/about',
          name: 'basic-router-about',
          component: () => import(/* webpackChunkName: "basic-router-about" */ '../apps/basic-router/views/About.vue')
        },
      ]
    },
  ]
})
