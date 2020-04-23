export default [
    {
        path: '/demo-openlayer',
        name: 'demo-openlayer',
        component: () => import(/* webpackChunkName: "demo-openlayer" */ '../apps/demo-openlayer/App.vue'),
        children : [
            {
                path: '/demo-openlayer/example00',
                name: 'example00',
                component: () => import(/* webpackChunkName: "demo-openlayer-example00" */ '../apps/demo-openlayer/examples/example00.vue'),
            },        
            {
                path: '/demo-openlayer/example01',
                name: 'example01',
                component: () => import(/* webpackChunkName: "demo-openlayer-example01" */ '../apps/demo-openlayer/examples/example01.vue'),
            },        
            {
                path: '/demo-openlayer/example02',
                name: 'example01',
                component: () => import(/* webpackChunkName: "demo-openlayer-example02" */ '../apps/demo-openlayer/examples/example02.vue'),
            },        
            {
                path: '/demo-openlayer/example03',
                name: 'example01',
                component: () => import(/* webpackChunkName: "demo-openlayer-example03" */ '../apps/demo-openlayer/examples/example03.vue'),
            },        
            {
                path: '/demo-openlayer/example04',
                name: 'example04',
                component: () => import(/* webpackChunkName: "demo-openlayer-example04" */ '../apps/demo-openlayer/examples/example04.vue'),
            },        
            {
                path: '/demo-openlayer/example05',
                name: 'example05',
                component: () => import(/* webpackChunkName: "demo-openlayer-example05" */ '../apps/demo-openlayer/examples/example05.vue'),
            }
        ]
    },
]