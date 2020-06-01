export default [
    {
        path: '/demo-component',
        name: 'demo-component',
        component: () => import(/* webpackChunkName: "demo-component" */ '../apps/demo-component/App.vue')
    },
]