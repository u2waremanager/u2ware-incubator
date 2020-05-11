export default [
    {
        path: '/demo-gis',
        name: 'demo-gis',
        component: () => import(/* webpackChunkName: "demo-gis" */ '../apps/demo-gis/App.vue'),
        children : [
            {
                path: '/demo-gis/map01',
                name: 'map01',
                component: () => import(/* webpackChunkName: "demo-gis-map01" */ '../apps/demo-gis/examples/map01.vue'),
            },        
            {
                path: '/demo-gis/cesium00',
                name: 'cesium00',
                component: () => import(/* webpackChunkName: "demo-gis-cesium00" */ '../apps/demo-gis/examples/cesium00.vue'),
            },        
            {
                path: '/demo-gis/cesium01',
                name: 'cesium01',
                component: () => import(/* webpackChunkName: "demo-gis-cesium01" */ '../apps/demo-gis/examples/cesium01.vue'),
            },        
            {
                path: '/demo-gis/cesium02',
                name: 'cesium02',
                component: () => import(/* webpackChunkName: "demo-gis-cesium02" */ '../apps/demo-gis/examples/cesium02.vue'),
            },        

            {
                path: '/demo-gis/openlayer00',
                name: 'openlayer00',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer00" */ '../apps/demo-gis/examples/openlayer00.vue'),
            },        
            {
                path: '/demo-gis/openlayer01',
                name: 'openlayer01',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer01" */ '../apps/demo-gis/examples/openlayer01.vue'),
            },        
            {
                path: '/demo-gis/openlayer02',
                name: 'openlayer02',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer02" */ '../apps/demo-gis/examples/openlayer02.vue'),
            },        
            {
                path: '/demo-gis/openlayer03',
                name: 'openlayer03',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer03" */ '../apps/demo-gis/examples/openlayer03.vue'),
            },        
            {
                path: '/demo-gis/openlayer04',
                name: 'openlayer04',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer04" */ '../apps/demo-gis/examples/openlayer04.vue'),
            },        
            {
                path: '/demo-gis/openlayer05',
                name: 'openlayer05',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer05" */ '../apps/demo-gis/examples/openlayer05.vue'),
            },        
            {
                path: '/demo-gis/openlayer06',
                name: 'openlayer06',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer06" */ '../apps/demo-gis/examples/openlayer06.vue'),
            },        
            {
                path: '/demo-gis/openlayer07',
                name: 'openlayer07',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer07" */ '../apps/demo-gis/examples/openlayer07.vue'),
            },        
            {
                path: '/demo-gis/openlayer08',
                name: 'openlayer08',
                component: () => import(/* webpackChunkName: "demo-gis-openlayer08" */ '../apps/demo-gis/examples/openlayer08.vue'),
            },        
            {
                path: '/demo-gis/overlap00',
                name: 'overlap00',
                component: () => import(/* webpackChunkName: "demo-gis-overlap00" */ '../apps/demo-gis/examples/overlap00.vue'),
            },        
            {
                path: '/demo-gis/overlap01',
                name: 'overlap01',
                component: () => import(/* webpackChunkName: "demo-gis-overlap01" */ '../apps/demo-gis/examples/overlap01.vue'),
            },        
            {
                path: '/demo-gis/overlap02',
                name: 'overlap02',
                component: () => import(/* webpackChunkName: "demo-gis-overlap02" */ '../apps/demo-gis/examples/overlap02.vue'),
            },        
            {
                path: '/demo-gis/three00',
                name: 'three00',
                component: () => import(/* webpackChunkName: "demo-gis-three00" */ '../apps/demo-gis/examples/three00.vue'),
            },        

        ]
    },
]