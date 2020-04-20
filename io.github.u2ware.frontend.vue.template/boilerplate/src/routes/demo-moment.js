export default [
    {
        path: '/demo-moment',
        name: 'demo-moment',
        component: () => import(/* webpackChunkName: "demo-moment" */ '../apps/demo-moment/App.vue')
    },
]