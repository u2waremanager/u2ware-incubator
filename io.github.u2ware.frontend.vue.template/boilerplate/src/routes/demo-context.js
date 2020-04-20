export default [
    {
        path: '/demo-context',
        name: 'demo-context',
        component: () => import(/* webpackChunkName: "demo-context" */ '../apps/demo-context/App.vue')
    },
]