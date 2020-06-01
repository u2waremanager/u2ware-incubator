export default [
    {
        path: '/demo-axios',
        name: 'demo-axios',
        component: () => import(/* webpackChunkName: "demo-axios" */ '../apps/demo-axios/App.vue')
    },
]