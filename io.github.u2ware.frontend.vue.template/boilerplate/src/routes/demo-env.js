export default [
    {
        path: '/demo-env',
        name: 'demo-env',
        component: () => import(/* webpackChunkName: "demo-env" */ '../apps/demo-env/App.vue')
    },
]