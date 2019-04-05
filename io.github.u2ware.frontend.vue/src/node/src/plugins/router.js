import Vue from 'vue'
import Router from 'vue-router'
import Dummy from '../apps/Dummy.vue'


Vue.use(Router)

export default new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'dummy',
      component: Dummy
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
      // children : [
      //   {
      //     path : '/about',
      //     component: () => import(/* webpackChunkName: "welcome_about" */ '../views/welcome/About.vue')
      //   },
      // ]
    },
  ]
})
