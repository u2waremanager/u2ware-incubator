export default [
    {
        path: '/demo-i18n',
        name: 'demo-i18n',
        component: () => import(/* webpackChunkName: "demo-i18n" */ '../apps/demo-i18n/App.vue')
    },
]